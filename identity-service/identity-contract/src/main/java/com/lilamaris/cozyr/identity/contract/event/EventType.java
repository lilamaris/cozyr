package com.lilamaris.cozyr.identity.contract.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    USER_CREATED("user", "created");

    private final String resource;
    private final String action;
}
