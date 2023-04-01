package com.dv.Lokana.validations;

import com.dv.Lokana.annotations.PasswordMatches;
import com.dv.Lokana.payload.request.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        SignupRequest userRequest = (SignupRequest) value;
        return userRequest.getPassword().equals(userRequest.getConfirmPassword());
    }
}
