package com.fileupload.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = MultipartFileNotEmptyValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartFileNotEmpty {
    String message() default "must no be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
