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

usage() {
  cat >&2 <<EOF
Usage:
  ${0##*/} [options] -t|--tag <tag> <deploy context>

Options:
  -t, --tag <tag>                       Image tag                   (required)
  -r, --registry <registry>             Docker registry address     (optional, default: localhost:5000)
  -f, --file <docker compose file>      Docker compose file         (optional, default: docker-compose.yml)
  -n, --namespace <namespace>           Image namespace             (optional, default: cozyr)

Examples:
  ${0##./} \\
    --registry registry.example.com \\
    --tag dev \\
    --namespace cozyr \\
    /opt/cozyr
EOF
}

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || {
    log_error "Missing required command: $1"
    exit 1
  }
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

require_cmd docker

docker compose version >/dev/null 2>&1 || {
  log_error "Docker compose plugin is not available"
  exit 1
}

docker info >/dev/null 2>&1 || {
  log_error "Docker daemon is unavailable or permission was denied"
  exit 1
}

registry_host="localhost:5000"
compose_file="docker-compose.yml"
tag=""
namespace="cozyr"
deploy_context=""

while (( $# > 0 )); do
  case "$1" in
    -r|--registry)
      require_option_value "$1" "${2:-}"
      registry_host="$2"
      shift 2
      ;;
    -f|--file)
      require_option_value "$1" "${2:-}"
      compose_file="$2"
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
    -h|--help)
      usage
      exit 0
      ;;
    --)
      shift
      require_option_value "<deploy context>" "${2:-}"
      deploy_context="$1"
      shift
      break
      ;;
    -*)
      log_error "Unknown option: $1"
      usage
      exit 2
      ;;
    *)
      if [[ -n "$deploy_context" ]]; then
        log_error "Unexpected argument: $1"
        usage
        exit 2
      fi

      deploy_context="${1%%/}"
      shift
      ;;
  esac
done

[[ -n "$tag" ]] || {
  log_error "Missing required option: <tag>"
  usage
  exit 2
}

[[ -n "$deploy_context" ]] || {
  log_error "Missing required argument: <deploy context>"
  usage
  exit 2
}

[[ -n "$registry_host" ]] || {
  log_error "Registry host must not be empty"
  exit 2
}

[[ -n "$compose_file" ]] || {
  log_error "Compose file must not be empty"
  exit 2
}

[[ -n "$tag" ]] || {
  log_error "Tag must not be empty"
  exit 2
}

[[ -n "$namespace" ]] || {
  log_error "Namespace must not be empty"
  exit 2
}

[[ -d "$deploy_context" ]] || {
  log_error "Deploy context is not a directory: ${deploy_context}"
  exit 1
}

cd -- "$deploy_context" || {
  log_error "Failed to change directory to project root: ${deploy_context}"
  exit 1
}

deploy_context="$(pwd -P)"

[[ -f "${deploy_context}/${compose_file}" ]] || {
  log_error "Compose file not found: ${deploy_context}/${compose_file}"
  exit 1
}
log_info "Script summary:"
printf '  Image name: %s\n' "${registry_host}/${namespace}/<service>:${tag}"
printf '  Deploy context: %s\n' "${deploy_context})"
printf '  Compose file: %s\n' "${deploy_context}/${compose_file}"

export REGISTRY_HOST="${registry_host}"
export NAMESPACE="${namespace}"
export IMAGE_TAG="${tag}"
export COMPOSE_FILE="${compose_file}"

log_info "Validating docker compose configuration"
docker compose config --quiet

log_info "Pulling images"
docker compose pull

log_info "Starting services"
docker compose up \
  -d \
  --remove-orphans \
  --wait \
  --wait-timeout 120

log_info "Deployment completed"
docker compose ps