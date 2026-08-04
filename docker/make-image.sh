#!/usr/bin/env bash
set -euo pipefail

RED=$'\033[0;31m'
GREEN=$'\033[0;32m'
YELLOW=$'\033[0;33m'
NC=$'\033[0m'

log_info() {
  printf '%s[INFO]%s %s\n' "$GREEN" "$NC" "$1"
}

log_error() {
  printf '%s[ERROR]%s %s\n' "$RED" "$NC" "$1"
}

log_warn() {
  printf '%s[WARN]%s %s\n' "$YELLOW" "$NC" "$1"
}

log_command() {
  local message="$1"
  shift

  printf '%s[INFO]%s %s' "$GREEN" "$NC" "$message"
  printf ' %q' "$@"
  printf '\n'
}

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || {
    log_error "Missing required command: $1"
    exit 1
  }
}

usage() {
  printf 'Usage: %s [-t|--tag <tag>] [--namespace <namespace>] [--platform <platform>] [--output-dir <artifact output path>] [--artifact-name <artifact name>] [docker-file <docker-file>] [--push-image] [--dry-run] -r|--registry <registry-url> <build-targets>...' "$0" >&2
}

require_cmd git

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd -- "${SCRIPT_DIR}/.." && pwd)"

cd "${PROJECT_ROOT}"

tag=""
namespace=""
platform=""
output_dir=""
artifact_name=""
docker_file=""
push_image=""
dry_run=false
registry=""
build_targets=()

while (( $# > 0 )); do
  case "$1" in
    -t|--tag)
      tag="${2:-}"
      shift 2
      ;;
    --namespace)
      namespace="${2:-}"
      shift 2
      ;;
    --platform)
      platform="${2:-}"
      shift 2
      ;;
    --output-dir)
      output_dir="${2:-}"
      shift 2
      ;;
    --artifact-name)
      artifact_name="${2:-}"
      shift 2
      ;;
    --docker-file)
      docker_file="${2:-}"
      shift 2
      ;;
    --push-image)
      push_image=true
      shift
      ;;
    -r|--registry)
      registry="${2:-}"
      shift 2
      ;;
    --dry-run)
      dry_run=true
      shift
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    --)
      shift
      build_targets+=("$@")
      break
      ;;
    -*)
      log_error "Unknown option: $1"
      usage
      exit 1
      ;;
    *)
      build_targets+=("$@")
      break
      ;;
  esac
done

[[ -n "$registry" ]] || {
  log_error "Missing required option: -r|--registry <registry-url>"
  usage
  exit 1
}

[[ ${#build_targets[@]} -gt 0 ]] || {
  log_error "Missing required argument: <build-targets>..."
  usage
  exit 1
}

[[ -n "$namespace" ]] || {
  namespace="cozyr"
  log_warn "Set the namespace to the default value: 'cozyr'"
}

[[ -n "$tag" ]] || {
  tag="$(git rev-parse --short HEAD)"
  log_warn "Set the tag to the default value(commit head hash of the current branch): $tag"
}

[[ -n "$platform" ]] || {
  platform="linux/amd64"
  log_warn "Set the platform to the default value: ${platform}"
}

[[ -n "$output_dir" ]] || {
  output_dir="build/libs"
  log_warn "Set the output dir to the default value: ${output_dir}"
}

[[ -n "$artifact_name" ]] || {
  artifact_name="app.jar"
  log_warn "Set the artifact name to the default value: ${artifact_name}"
}

[[ -n "$docker_file" ]] || {
  docker_file="Dockerfile"
  log_warn "Set the docker file to the default value: ${docker_file}"
}

[[ -n "$push_image" ]] || {
  push_image=false
  log_warn "Set the push image to the default value: ${push_image}"
}

log_info "Running Context Summary"
printf '  Namespace: %s\n' "$namespace"
printf '  Tag: %s\n' "$tag"
printf '  Platform: %s\n' "$platform"
printf '  Build output: %s\n' "$output_dir"
printf '  Dry run: %s\n' "$dry_run"
printf '  Registry: %s\n' "$registry"
printf '  Build targets:\n'
printf '\t%s\n' "${build_targets[@]}"

context_root=""

[[ "${dry_run}" == "true" ]] || {
  context_root="$(mktemp -d)"
  log_info "Making temporary workspace: $context_root"
}


validate_gradle_project_path() {
  local project_path="$1"
  [[ "$project_path" == :* ]]
}

gradle_project_path_to_dir() {
  local project_path="${1#:}"
  printf '%s' "${project_path//:/\/}"
}

gradle_project_path_to_image_name() {
  local project_path="${1#:}"
  local parts=()

  IFS=':' read -r -a parts <<< "$project_path"

  printf '%s\n' "${parts[0]}"
}

build_gradle_project() {
  local target="$1"
  local cmd=(./gradlew "${target}:bootJar")

  [[ "$dry_run" == "true" ]] && {
    log_command "Executing (dry-run):" "${cmd[@]}"
    return 0
  }

  log_command "Executing:" "${cmd[@]}"
  "${cmd[@]}"
}

build_docker_image() {
  local target="$1"

  local artifact_path="${PROJECT_ROOT}/$(gradle_project_path_to_dir "$target")/${output_dir}/${artifact_name}"
  local image_name="$(gradle_project_path_to_image_name "$target")"

  [[ -f "${artifact_path}" || "$dry_run" == "true" ]] || {
    log_error "Missing artifact in $artifact_path. Are the Gradle project path and file system path does not matched?"
    exit 1
  }

  local image="${registry}/${namespace}/${image_name}:${tag}"
  local docker_context="${context_root}/${image_name}"

  if [[ "${dry_run}" == "true" ]]; then
    docker_context="${TMPDIR:-/tmp}/cozyr-docker-context/${image_name}"
  else
    mkdir -p "${docker_context}" || {
      log_error "Failed to create Docker context: ${docker_context}"
      return 1
    }

    cp "${artifact_path}" "${docker_context}/${artifact_name}" || {
      log_error "Failed to copy artifact: ${artifact_path} -> ${docker_context}/${artifact_name}"
      return 1
    }
  fi

  local args=(
    buildx build
    --platform "${platform}"
    -f "${docker_file}"
    --build-arg JAR_FILE="${artifact_name}"
    -t "${image}"
  )

  if [[ "${push_image}" == "true" ]]; then
    args+=(--push)
  else
    args+=(--load)
  fi

  local cmd=(docker "${args[@]}" "${docker_context}")

  [[ "$dry_run" == "true" ]] && {
    log_command "Executing (dry-run):" "${cmd[@]}"
    return 0
  }

  log_command "Executing:" "${cmd[@]}"
  "${cmd[@]}"
}

trap 'rm -rf "${context_root}"' EXIT

for target in "${build_targets[@]}"; do
  validate_gradle_project_path "$target" || {
    log_error "Invalid Gradle project path: $target"
    exit 1
  }

  build_gradle_project "$target" || {
    log_error "Failed to build jar: $target"
    exit 1
  }

  build_docker_image "$target" || {
    log_error "Failed to build image: $target"
    exit 1
  }
done
