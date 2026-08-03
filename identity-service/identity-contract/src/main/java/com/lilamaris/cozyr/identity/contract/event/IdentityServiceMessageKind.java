package com.lilamaris.cozyr.identity.contract.event;

import com.lilamaris.cozyr.kernel.message.MessageKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IdentityServiceMessageKind implements MessageKind {
    USER_CREATED("user.created", 1),
    USER_UPDATED("user.updated", 1);

    private final String canonicalName;
    private final int version;

    @Override
    public String canonicalName() {
        return canonicalName;
    }

    @Override
    public int version() {
        return version;
    }
}
