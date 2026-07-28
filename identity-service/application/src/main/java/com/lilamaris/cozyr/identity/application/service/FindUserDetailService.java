package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.application.model.user.UserDetail;
import com.lilamaris.cozyr.identity.application.port.in.FindUserDetailUseCase;
import com.lilamaris.cozyr.identity.application.port.in.query.FindUserDetailQuery;
import com.lilamaris.cozyr.identity.application.port.out.UserReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserDetailService implements FindUserDetailUseCase {
    private final UserReader reader;

    @Override
    public UserDetail find(FindUserDetailQuery query) {
        return reader.findDetailById(query.userId())
                .orElseThrow(() -> new ApplicationException(IdentityServiceProgressCode.USER_NOT_FOUND));
    }
}
