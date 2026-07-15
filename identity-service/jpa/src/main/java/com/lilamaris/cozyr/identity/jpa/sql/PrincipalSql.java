package com.lilamaris.cozyr.identity.jpa.sql;

public class PrincipalSql {
    public static final String FIND_BY_USER_ID = """
            SELECT
                u.id AS userId,
                u.display_name AS displayName,
                us.service AS service,
                us.role AS roleName
            FROM cozyr_user u
            LEFT JOIN user_scope us
                ON us.user_id = u.id
            WHERE u.id = :userId
            """;
}
