package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.domain.User;

public interface UserStore {
    User save(User user);
}
