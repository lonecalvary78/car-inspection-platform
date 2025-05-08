package app.carinspection.platform.car.infrastructure.validation;

import app.carinspection.platform.car.exception.ApplicationException;
import app.carinspection.platform.car.exception.ErrorType;
import jakarta.validation.Validation;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValiadationUtil {

    public void validate(Object object) throws ApplicationException {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(object);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("");
            throw new ApplicationException(ErrorType.VALIDATION_ERROR, errorMessage);
        }
    }
}
