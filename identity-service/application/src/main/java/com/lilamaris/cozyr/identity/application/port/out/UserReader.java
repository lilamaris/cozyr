package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.application.model.user.UserCursor;
import com.lilamaris.cozyr.identity.application.model.user.UserDetail;
import com.lilamaris.cozyr.identity.application.model.user.UserFilter;
import com.lilamaris.cozyr.identity.application.model.user.UserSummary;
import com.lilamaris.cozyr.identity.domain.User;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

import java.util.Optional;
import java.util.UUID;

public interface UserReader {
    Optional<User> findById(UUID id);

    Optional<UserDetail> findDetailById(UUID userId);

    CursorResult<UserSummary, UserCursor> findSummaries(UserFilter filter, CursorRequest<UserCursor> request);
}
