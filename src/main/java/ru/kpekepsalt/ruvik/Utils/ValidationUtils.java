package ru.kpekepsalt.ruvik.Utils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Utils for objects validation using Validation API
 */
public class ValidationUtils {

    private static ValidationUtils instance;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private ValidationUtils() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public static ValidationUtils getInstance()
    {
        if(instance == null)
        {
            instance = new ValidationUtils();
        }
        return instance;
    }

    /**
     * @return Validation API validator instance
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * Qualitatively validates given object
     * @param o Object to validate
     * @return true if object if valid
     */
    public static boolean isValid(Object o)
    {
        return getInstance().getValidator().validate(o).isEmpty();
    }

}
