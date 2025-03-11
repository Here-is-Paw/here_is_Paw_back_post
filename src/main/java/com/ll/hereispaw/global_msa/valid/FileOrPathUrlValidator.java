package com.ll.hereispaw.global_msa.valid;

import com.ll.hereispaw.domain_msa.post.missing.dto.request.MissingCreateRequest;
import com.ll.hereispaw.global_msa.valid.FileOrPathUrlRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileOrPathUrlValidator implements ConstraintValidator<FileOrPathUrlRequired, MissingCreateRequest> {
    @Override
    public void initialize(FileOrPathUrlRequired constraintAnnotation) {
    }

    @Override
    public boolean isValid(MissingCreateRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true; // null 객체는 다른 어노테이션(@NotNull)에서 처리
        }

        // file이나 pathUrl 중 하나라도 값이 있으면 유효함
        return request.hasfile() || (request.getPathUrl() != null && !request.getPathUrl().isEmpty());
    }
}