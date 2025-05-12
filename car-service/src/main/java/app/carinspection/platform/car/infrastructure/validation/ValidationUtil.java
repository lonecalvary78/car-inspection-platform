package app.carinspection.platform.car.infrastructure.validation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import app.carinspection.platform.car.exception.ApplicationException;
import app.carinspection.platform.car.exception.ErrorType;
import app.carinspection.platform.car.model.entity.Car;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    /**
     * To validate the car payload
     * @param payload
     * @throws ApplicationException
     */
    public void validatePayload(Car payload) throws ApplicationException {
        var errors = validate(payload);
        if(errors != null && !errors.isEmpty()) {
            throw new ApplicationException(ErrorType.VALIDATION_ERROR, errors);
        }
    }

    private Map<String, String> validate(Car car) {
        Map<String,String> errors = new HashMap<>();
        Set<ConstraintViolation<Car>> validations = validator.validate(car);
        if(validations != null) {
            for(ConstraintViolation<Car> validation: validations) {
                errors.put(validation.getPropertyPath().toString(), validation.getMessage());
            }
        }
        return errors;
    }
}
