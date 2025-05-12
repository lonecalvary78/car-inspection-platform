package app.carinspection.platform.car.infrastructure.validation;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import app.carinspection.platform.car.exception.ApplicationException;
import app.carinspection.platform.car.model.entity.Car;
import app.carinspection.platform.car.testcase.SimpleTestCase;
import software.amazon.awssdk.utils.StringUtils;

public class ValidationUtilTest {
    
    @ParameterizedTest
    @MethodSource("prepareTestCasesForValidationTest")
    @DisplayName("To validate the car payload")
    public void validate(SimpleTestCase testCase) {
        var car = (Car)testCase.inputData();
        if(testCase.throwAnyException()) {
            Assertions.assertThrows(testCase.expectedException(),()->ValidationUtil.validatePayload(car));
        } else {
            Assertions.assertDoesNotThrow(()->ValidationUtil.validatePayload(car));
        }
    }

    private static Stream<SimpleTestCase> prepareTestCasesForValidationTest() {
        return Stream.of(
            SimpleTestCase.of(null, null, true, IllegalArgumentException.class),
            SimpleTestCase.of(newPayload("Toyota", "Corolla", "Red", UUID.randomUUID(), "2020", "57677907", "5786M"), null, false, null),
            SimpleTestCase.of(newPayload("Toyota", "Corolla", "Red", UUID.randomUUID(), "2020", null, "5786M"), null, true, ApplicationException.class)
        );
    }

    private static  Car newPayload(String brand, String model, String colour, UUID carOwnerId, String year, String vin, String licensePlate) {
        var car = new Car();
        if(StringUtils.isNotBlank(brand)) {
            car.setBrand(brand);
        }
        
        if(StringUtils.isNotBlank(model)) {
            car.setModel(model);
        }
        
        if(StringUtils.isNotBlank(colour)) {
            car.setColor(colour);
        }

        if(carOwnerId != null) {
            car.setOwnerId(carOwnerId);
        }

        if(StringUtils.isNotBlank(year)) {
            car.setYear(year);
        }

        if(StringUtils.isNotBlank(vin)) {
            car.setVin(vin);
        }

        if(StringUtils.isNotBlank(licensePlate)) {
            car.setLicensePlate(licensePlate);
        }
        return car;
    }
}
