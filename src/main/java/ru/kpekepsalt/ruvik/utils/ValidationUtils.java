package ru.kpekepsalt.ruvik.utils;

import ru.kpekepsalt.ruvik.objects.ValidationResult;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

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

    /**
     * Qualitatively validates given object
     * @param o Object to validate
     * @return Validation result
     */
    public static ValidationResult validate(Object o)
    {
        return new ValidationResult(
                getInstance()
                        .getValidator()
                        .validate(o)
        );
    }

}
