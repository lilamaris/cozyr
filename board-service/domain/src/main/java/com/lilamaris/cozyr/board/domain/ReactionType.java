package com.lilamaris.cozyr.board.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReactionType {
    THUMBS_UP("thumbs-up"),
    THUMBS_DOWN("thumbs-down"),
    WATCHING("watching"),
    ROCKET("rocket"),
    PARTY("party");

    private final String canonicalName;
}
