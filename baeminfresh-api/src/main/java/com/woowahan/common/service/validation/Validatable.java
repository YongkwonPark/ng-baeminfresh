package com.woowahan.common.service.validation;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

/**
 * @author ykpark@woowahan.com
 */
public interface Validatable {

    default void validate(SmartValidator validator, BindingResult bindingResult, Object...validationHints) throws BindException {
        validator.validate(this, bindingResult, validationHints);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }

}
