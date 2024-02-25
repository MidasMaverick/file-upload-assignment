package com.fileupload.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileNotEmptyValidator implements ConstraintValidator<MultipartFileNotEmpty, MultipartFile> {
    @Override
    public void initialize(MultipartFileNotEmpty constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return file == null || !file.isEmpty();
    }
}
