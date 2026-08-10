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

require_option_value() {
  local option="$1"
  local value="${2:-}"

  [[ -n "$value" ]] || {
    log_error "Option requires a value: ${option}"
    usage
    exit 2
  }
}

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || {
    log_error "Missing required command: $1"
    exit 1
  }
}

usage() {
  cat >&2 <<EOF
Usage:
  ${0##*/} [options] <build target>...

Options:
  -r, --registry <registry host>        Docker image registry address               (required)
  -t, --tag <tag>                       Docker image tag                            (optional, default: git commit short hash)
  -n, --namespace <namespace>           Docker image namespace                      (optional, default: cozyr)  
  -p, --platform <platform>             Docker image build platform                 (optional, default: linux/amd64)
  --artifact-output <path>              Build JAR output path                       (optional, default: build/libs/app.jar)
  --docker-file <path>                  Dockerfile path relative to build target    (optional, default: Dockerfile)
  --push-image                          Push image to <registry host>               (optional, default: false)
  --dry-run                             Evaluates a command without any changes     (optional, default: false)   
  
Examples:
  #{0##*/} \\
    --registry registry.example.com
    --tag latest
    --namespace cozyr
    --platform linux/amd64
    --artifact-output build/libs/app.jar
    --push-image
EOF
}

require_cmd git

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd -- "${SCRIPT_DIR}/.." && pwd)"

cd "${PROJECT_ROOT}"

registry=""
tag="$(git rev-parse --short HEAD)"
namespace="cozyr"
platform="linux/amd64"
artifact_output="build/libs/app.jar"
docker_file="Dockerfile"
push_image=false
dry_run=false
build_targets=()

while (( $# > 0 )); do
  case "$1" in
    -r|--registry)
      require_option_value "$1" "${2:-}"
      registry="$2"
      shift 2
      ;;
    -t|--tag)
      require_option_value "$1" "${2:-}"
      tag="$2"
      shift 2
      ;;
    -n|--namespace)
      require_option_value "$1" "${2:-}"
      namespace="$2"
      shift 2
      ;;
    -p|--platform)
      require_option_value "$1" "${2:-}"
      platform="$2"
      shift 2
      ;;
    --artifact-output)
      require_option_value "$1" "${2:-}"
      artifact_output="$2"
      shift 2
      ;;
    --docker-file)
      require_option_value "$1" "${2:-}"
      docker_file="$2"
      shift 2
      ;;
    --push-image)
      push_image=true
      shift
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

[[ -n "$tag" ]] || {
  log_error "tag must not be empty"
  exit 2
}

[[ -n "$namespace" ]] || {
  log_error "namespace must not be empty"
  exit 2
}

[[ -n "$platform" ]] || {
  log_error "platform must not be empty"
  exit 2
}

[[ -n "$docker_file" ]] || {
  log_error "dockerfile must not be empty"
  exit 2
}

log_info "Running Context Summary"
printf '  Namespace: %s\n' "$namespace"
printf '  Tag: %s\n' "$tag"
printf '  Platform: %s\n' "$platform"
printf '  Build output: %s\n' "$artifact_output"
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

  local target_dir="$(gradle_project_path_to_dir "$target")"
  local artifact_path="${PROJECT_ROOT}/${target_dir}/${artifact_output}"
  local docker_file_path="${PROJECT_ROOT}/${target_dir}/${docker_file}"
  local staged_artifact_name="app.jar"
  local image_name="$(gradle_project_path_to_image_name "$target")"

  [[ -f "${artifact_path}" || "$dry_run" == "true" ]] || {
    log_error "Missing artifact in $artifact_path. Are the Gradle project path and file system path does not matched?"
    exit 1
  }

  [[ -f "${docker_file_path}" || "$dry_run" == "true" ]] || {
    log_error "Missing Dockerfile in ${docker_file_path}. Are the Gradle project path and Dockerfile path matched?"
    exit 1
  }

  local image="${registry}/${namespace}/${image_name}:${tag}"
  local docker_context="${context_root}/${image_name}"

  if [[ "${dry_run}" == "true" ]]; then
    docker_context="${TMPDIR:-/tmp}/cozyr-docker-context/${image_name}"
  else
    log_info "Creating tmp directory: ${docker_context}..."
    mkdir -p "${docker_context}" || {
      log_error "Failed to create Docker context: ${docker_context}"
      return 1
    }

    log_info "Copying artifact: ${artifact_path} -> ${docker_context}/${staged_artifact_name}"
    cp "${artifact_path}" "${docker_context}/${staged_artifact_name}" || {
      log_error "Failed to copy artifact: ${artifact_path} -> ${docker_context}/${staged_artifact_name}"
      return 1
    }

    log_info "Copying dockerfile: ${docker_file_path} -> ${docker_context}/Dockerfile"
    cp "${docker_file_path}" "${docker_context}/Dockerfile" || {
      log_error "Failed to copy docker image: ${docker_file_path} -> ${docker_context}/Dockerfile"
      return 1
    }
  fi

  local staged_dockerfile="${docker_context}/Dockerfile"

  local args=(
    buildx build
    --platform "${platform}"
    -f "${staged_dockerfile}"
    --build-arg JAR_FILE="${staged_artifact_name}"
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
