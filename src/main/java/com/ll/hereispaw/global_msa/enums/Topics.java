package com.ll.hereispaw.global_msa.enums;

import lombok.Getter;

@Getter
public enum Topics {
    DOG_FACE("dog-face-request"), SEARCH("meiliesearch");

    // Getter 메서드
    private final String topicName;

    Topics(String topicName) {
        this.topicName = topicName;
    }
}
