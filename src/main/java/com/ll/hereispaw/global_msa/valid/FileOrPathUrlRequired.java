package com.ll.hereispaw.global_msa.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileOrPathUrlValidator.class)
@Documented
public @interface FileOrPathUrlRequired {
    String message() default "file 또는 pathUrl 중 하나는 반드시 필요합니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}