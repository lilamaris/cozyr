#!/usr/bin/env bash
set -euo pipefail

SCRIPT_ROOT="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd -- "${SCRIPT_ROOT}/.." && pwd)"
TMP_FILE_STATE="${SCRIPT_ROOT}/.state.tmp"
DOCKER_IMAGE_STATE="${SCRIPT_ROOT}/.state.img"

source "${SCRIPT_ROOT}/lib/utils.sh"

temp_dir=""
generated_output=""
paths=()

if [[ -f "$TMP_FILE_STATE" ]]; then
  while IFS= read -r path || [[ -n "$path" ]]; do
    paths+=("$path")
  done < "$TMP_FILE_STATE"
  temp_dir="${paths[0]:-}"
  generated_output="${paths[1]:-}"
  if [[ "$temp_dir" != /*/cozyr-api-demo.* || "$temp_dir" == *$'\n'* ]]; then
    fail "Invalid demo temporary directory in $TMP_FILE_STATE"
  fi
fi
export COZYR_REGISTRY_HOST=localhost
export COZYR_BIND_DIR="${temp_dir:-/tmp/cozyr-api-demo-missing}"
export COZYR_GENERATED_OUTPUT="${generated_output:-/tmp/cozyr-api-demo-missing/generated}"

compose=(docker compose -p lilamaris-cozyr-api-demo -f "${SCRIPT_ROOT}/docker-compose.yml")
run "shutdown and cleanup" "${compose[@]}" down --volumes --remove-orphans

if [[ -f "$DOCKER_IMAGE_STATE" ]]; then
  while IFS= read -r image || [[ -n "$image" ]]; do
    if docker image inspect "$image" >/dev/null 2>&1; then
      run "remove demo image" docker image rm "$image"
    fi
  done < "$DOCKER_IMAGE_STATE"
fi

if [[ -n "$temp_dir" ]]; then
  if [[ -f "$temp_dir/auth/source/script/cleanup-demo.sh" ]]; then
    run "cleanup auth api" bash "$temp_dir/auth/source/script/cleanup-demo.sh"
  fi
  run "remove temporary data" rm -rf -- "$temp_dir"
fi

rm -f -- "$TMP_FILE_STATE"
rm -f -- "$DOCKER_IMAGE_STATE"
