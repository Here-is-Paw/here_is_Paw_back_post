package com.ll.hereispaw.global_msa.enums;

import lombok.Getter;

@Getter
public enum PostState {
    OPEN(0), IN_CARE(1), DONE(2);

    // Getter 메서드
    private final int code;

    PostState(int code) {
        this.code = code;
    }
}
