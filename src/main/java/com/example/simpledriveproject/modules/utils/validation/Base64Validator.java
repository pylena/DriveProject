package com.example.simpledriveproject.modules.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Base64;

public class Base64Validator implements ConstraintValidator<IsBase64, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        try {
            Base64.getDecoder().decode(value);
            return true;
        } catch(IllegalArgumentException iae) {
            return false;
        }
    }
}
