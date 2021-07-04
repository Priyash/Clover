package com.clover.data.validation;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ValidationResult {

    private Boolean isValid;
    private List<String> validationMessages;
}
