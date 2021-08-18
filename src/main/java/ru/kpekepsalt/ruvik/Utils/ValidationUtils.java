package ru.kpekepsalt.ruvik.Utils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidationUtils {

    public static boolean isValid(Object o)
    {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        return validator.validate(o).isEmpty();
    }

}
