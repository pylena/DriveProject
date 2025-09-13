package com.example.simpledriveproject.modules.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Base64Validator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsBase64 {
    String message() default "not valid base64 string";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
