package com.clover.data.validation;

import com.clover.data.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Qualifier("ProductTitleValidator")
public class ProductTitleValidator implements ConstraintValidator<String> {
    @Autowired
    private ValidationResult validationResult;

    @Override
    public ValidationResult validate(String title) {
        List<String> errorMessages = new ArrayList<>();
        Boolean isValid = true;
        try {
            if(StringUtils.isEmpty(title) || StringUtils.isBlank(title)) {
                errorMessages.add("Product title cannot be empty");
                isValid = false;
            }
            if(title.length() < 2) {
                errorMessages.add("Product title should be minimum of length 3");
                isValid =  false;
            }

            validationResult.setIsValid(isValid);
            validationResult.setValidationMessages(errorMessages);
            return validationResult;
        } catch (Exception ex) {
            log.error("Exception while validating the product fields", ex);
        }
        return null;
    }
}
