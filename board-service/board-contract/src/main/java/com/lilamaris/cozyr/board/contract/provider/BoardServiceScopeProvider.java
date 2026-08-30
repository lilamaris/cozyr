package com.lilamaris.cozyr.board.contract.provider;

import com.lilamaris.cozyr.identity.contract.provider.ServiceScopeProvider;
import com.lilamaris.cozyr.identity.contract.schema.Role;
import com.lilamaris.cozyr.identity.contract.schema.Scope;

public class BoardServiceScopeProvider implements ServiceScopeProvider {
    @Override
    public Scope provide() {
        return Scope.of("board", Role.USER);
    }
}
