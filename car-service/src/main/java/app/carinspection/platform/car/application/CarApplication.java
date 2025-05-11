package app.carinspection.platform.car.application;

import java.util.UUID;
import java.util.logging.Logger;

import app.carinspection.platform.car.api.response.ApiResponse;
import app.carinspection.platform.car.exception.ApplicationException;
import app.carinspection.platform.car.model.entity.Car;
import app.carinspection.platform.car.service.CarService;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class CarApplication {
    private final Logger logger = Logger.getLogger(CarApplication.class.getName());
    private final CarService carService;

    public CarApplication(DynamoDbClient dynamoDbClient) {
        this.carService = new CarService(dynamoDbClient);
    }

    public ApiResponse findAllCarsByOwnerId(UUID carOwnerId, int currentPage) {
        logger.info("findAllCarsByOwnerId[carOwnerId: %s, currentPage: %d]".format(carOwnerId.toString(),currentPage));
        return toApiResponse(carService.getAllCarsByOwerId(carOwnerId, currentPage));
    }

    public ApiResponse findCarById(UUID carId) throws ApplicationException {
        logger.info("findCarById[]carId: %s".format(carId.toString()));
        return toApiResponse(carService.findCarById(carId));
    }

    public ApiResponse createNewCar(Car car) throws ApplicationException {
        logger.info("createNewCar");
        return  toApiResponse(carService.createNew(car));
    }

    public ApiResponse updateCar(UUID carId, Car amendedCar) throws ApplicationException {
        logger.info("updateCar[carId: %s]".format(carId.toString()));
        return toApiResponse(carService.updateCar(carId, amendedCar));
    }

    public void deleteCar(UUID carId) throws ApplicationException {
        logger.info("deleteCar[carId: %s]".format(carId.toString()));
        carService.deleteCar(carId);
    }

    private ApiResponse toApiResponse(Object data) {
        return ApiResponse.of(data);
    }
}
