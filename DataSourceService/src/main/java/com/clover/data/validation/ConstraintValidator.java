package com.clover.data.validation;

public interface ConstraintValidator<T> {
    public ValidationResult validate(T object);
}
