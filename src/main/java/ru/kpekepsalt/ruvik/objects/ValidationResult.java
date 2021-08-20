package ru.kpekepsalt.ruvik.objects;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Represents result of object validation
 */
public class ValidationResult {

    private Set<ConstraintViolation<Object>> violations;

    public ValidationResult(Set<ConstraintViolation<Object>> validationResult)
    {
        violations = validationResult;
    }

    /**
     * @return First constraint violation or null if object is valid
     */
    public ConstraintViolation<Object> getFirstViolation()
    {
        return violations.stream().findFirst().orElse(null);
    }

    /**
     * @return First constrain violation message of empty string if objecy is valid
     */
    public String getFirstErrorMessage()
    {
        String message = "";
        ConstraintViolation<Object> constraintViolation = getFirstViolation();
        if(constraintViolation != null)
        {
            message = constraintViolation.getMessage();
        }
        return message;
    }

    /**
     * @return true if object is valid
     */
    public boolean isValid()
    {
        return violations.size() == 0;
    }

}
