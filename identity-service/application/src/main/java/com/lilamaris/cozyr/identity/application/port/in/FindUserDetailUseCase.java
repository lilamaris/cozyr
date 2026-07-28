package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.model.UserDetail;
import com.lilamaris.cozyr.identity.application.port.in.query.FindUserDetailQuery;

public interface FindUserDetailUseCase {
    UserDetail find(FindUserDetailQuery query);
}
