package com.ll.hereispaw.global_msa.exception;

import com.ll.hereispaw.global_msa.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
