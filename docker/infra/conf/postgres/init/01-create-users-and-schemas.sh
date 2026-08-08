#!/usr/bin/env bash
set -euo pipefail

: "${IDENTITY_DB_PASSWORD:?IDENTITY_DB_PASSWORD is required}"
: "${BOARD_DB_PASSWORD:?BOARD_DB_PASSWORD is required}"

psql \
  --username "$POSTGRES_USER" \
  --dbname "$POSTGRES_DB" \
  --set=db_name="$POSTGRES_DB" \
  --set=identity_password="$IDENTITY_DB_PASSWORD" \
  --set=board_password="$BOARD_DB_PASSWORD" <<'SQL'
CREATE ROLE identity_user
    LOGIN
    PASSWORD :'identity_password'
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE;

CREATE ROLE board_user
    LOGIN
    PASSWORD :'board_password'
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE;

CREATE SCHEMA identity AUTHORIZATION identity_user;
CREATE SCHEMA board AUTHORIZATION board_user;

ALTER ROLE identity_user IN DATABASE :"db_name"
    SET search_path = identity, pg_catalog;

ALTER ROLE board_user IN DATABASE :"db_name"
    SET search_path = board, pg_catalog;

REVOKE CREATE ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA identity FROM PUBLIC;
REVOKE ALL ON SCHEMA board FROM PUBLIC;
SQL