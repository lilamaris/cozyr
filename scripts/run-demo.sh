#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat >&2 <<EOF

EOF
}

# Initial script variables
script_root="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
project_root="$(cd -- "${script_root}/.." && pwd)"

source "${script_root}/lib/utils.sh"

registry_host="localhost"
namespace="cozyr"
tag="local"
host_os="$(uname -s)"
host_arch="$(uname -m)"
host_platform="$(get_platform "$host_arch")"

modules=(
  gateway
  board-service/launcher
  reservation-service/launcher
  statistics-service/launcher
)

log_info "Host OS/Arch: $host_os/$host_arch, Platform: $host_platform"

# Prepare build & Validate gradle module
[[ -f gradlew ]] || {
  log_error "Gradle wrapper not found."
  exit 1
}

validate_gradle_module "${modules[@]}" || {
  log_error "Failed to validate modules."
  exit 1
}

read -r -a tasks <<< "$(convert_to_gradle_path "${modules[@]}")"

gradle_command=(./gradlew "${tasks[@]}")

# Build gradle module
run "build gradle module" "${gradle_command[@]}"

# Prepare build docker image
command -v git >/dev/null 2>&1 && tag=$(git rev-parse --short HEAD)

log_info "Image tag: $tag"

run_no_output "check docker daemon" docker info || {
  log_error "docker daemon is unavailable or permission was denied."
  exit 1
}

run_no_output "check docker buildx" docker buildx version || {
  log_error "docker buildx is unavailable"
  exit 1
}

run_no_output "check docker compose" docker compose version || {
  log_error "docker compose plugin is not available."
  exit 1
}