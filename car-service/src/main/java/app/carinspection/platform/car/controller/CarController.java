package app.carinspection.platform.car.controller;

import java.util.UUID;

import app.carinspection.platform.car.api.response.ApiErrorResponse;
import app.carinspection.platform.car.exception.ApplicationException;
import app.carinspection.platform.car.exception.ErrorType;
import app.carinspection.platform.car.infrastructure.validation.ValiadationUtil;
import app.carinspection.platform.car.model.dto.CarDTO;
import app.carinspection.platform.car.service.CarService;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class CarController implements HttpService {
    private CarService carService;

    public CarController(DynamoDbClient dynamoDbClient) {
        this.carService = new CarService(dynamoDbClient);
    }

    @Override
    public void routing(HttpRules rules) {
        rules
        .get("/", this::findAllCarsByOwnerId)
        .get("/{carId}", this::findCarById)
        .post("/", this::createNewCar)
        .put("/{carId}", this::updateCar)
        .delete("/{carId}", this::deleteCar);
    }

    private void findAllCarsByOwnerId(ServerRequest request, ServerResponse response) {
        try {
            UUID ownerId = UUID.fromString(request.query().first("ownerId").orElseThrow(() -> new ApplicationException(ErrorType.MISSING_REQUIRED_OWNER_ID)));
            var cars = carService.findAllByOwnerId(ownerId);
            response.send(cars);
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }

    private void findCarById(ServerRequest request, ServerResponse response) {
        try {
            UUID carId = UUID.fromString(request.query().first("carId").orElseThrow(() -> new ApplicationException(ErrorType.MISSING_REQUIRED_UNIQUE_ID)));
            var car = carService.findById(carId);
            response.send(car);
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }

    private void createNewCar(ServerRequest request, ServerResponse response) {
        try {
            CarDTO carDTO = request.content().asOptional(CarDTO.class).orElseThrow(() -> new ApplicationException(ErrorType.MISSING_REQUIRED_PAYLOAD));
            ValiadationUtil.validate(carDTO);
            var createdCar = carService.createNewCar(carDTO);
            response.send(createdCar);
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }

    private void updateCar(ServerRequest request, ServerResponse response) {
        try {
            UUID carId = UUID.fromString(request.query().first("carId").orElseThrow(() -> new ApplicationException(ErrorType.MISSING_REQUIRED_UNIQUE_ID)));
            CarDTO carDTO = request.content().asOptional(CarDTO.class).orElseThrow(() -> new ApplicationException(ErrorType.MISSING_REQUIRED_PAYLOAD));
            ValiadationUtil.validate(carDTO);
            var updatedCar = carService.updateCar(carId, carDTO);
            response.send(updatedCar);
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }

    private void deleteCar(ServerRequest request, ServerResponse response) {
        try {
            UUID carId = UUID.fromString(request.query().first("carId").orElseThrow(() -> new ApplicationException(ErrorType.MISSING_REQUIRED_UNIQUE_ID)));
            carService.deleteCar(carId);
            response.send();
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }
    
    private void handleException(ServerResponse response, ApplicationException thrownApplicationException){
        response.status(thrownApplicationException.getErrorType().getStatusCode()).send(ApiErrorResponse.of(thrownApplicationException.getErrorType()));
    }
    
}
