#!/usr/bin/env bash
set -euo pipefail

: "${BOARD_DB_PASSWORD:?BOARD_DB_PASSWORD is required}"
: "${STATISTICS_DB_PASSWORD:?STATISTICS_DB_PASSWORD is required}"
: "${RESERVATION_DB_PASSWORD:?RESERVATION_DB_PASSWORD is required}"

psql \
  --username "$POSTGRES_USER" \
  --dbname "$POSTGRES_DB" \
  --set=db_name="$POSTGRES_DB" \
  --set=board_password="$BOARD_DB_PASSWORD" \
  --set=statistics_password="$STATISTICS_DB_PASSWORD" \
  --set=reservation_password="$RESERVATION_DB_PASSWORD" <<'SQL'

CREATE ROLE board_user
    LOGIN
    PASSWORD :'board_password'
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE;

CREATE ROLE statistics_user
  LOGIN
  PASSWORD :'statistics_password'
  NOSUPERUSER
  NOCREATEDB
  NOCREATEROLE;

CREATE ROLE reservation_user
  LOGIN
  PASSWORD :'reservation_password'
  NOSUPERUSER
  NOCREATEDB
  NOCREATEROLE;

CREATE SCHEMA board AUTHORIZATION board_user;
CREATE SCHEMA statistics AUTHORIZATION statistics_user;
CREATE SCHEMA reservation AUTHORIZATION reservation_user;

ALTER ROLE board_user IN DATABASE :"db_name"
    SET search_path = board, pg_catalog;

ALTER ROLE statistics_user IN DATABASE :"db_name"
    SET search_path = statistics, pg_catalog;

ALTER ROLE reservation_user IN DATABASE :"db_name"
    SET search_path = reservation, pg_catalog;

REVOKE CREATE ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA board FROM PUBLIC;
REVOKE ALL ON SCHEMA statistics FROM PUBLIC;
REVOKE ALL ON SCHEMA reservation FROM PUBLIC;
SQL
