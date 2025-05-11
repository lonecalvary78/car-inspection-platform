package app.carinspection.platform.car.service;

import java.util.UUID;

import app.carinspection.platform.car.model.entity.Car;
import app.carinspection.platform.car.exception.ApplicationException;
import app.carinspection.platform.car.exception.ErrorType;
import app.carinspection.platform.car.infrastructure.pagination.PaginationUtil;
import app.carinspection.platform.car.model.Page;
import app.carinspection.platform.car.repository.CarRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class CarService {
    private final CarRepository carRepository;

    public CarService(DynamoDbClient dynamoDbClient) {
        this.carRepository = new CarRepository(dynamoDbClient);
    }

    public Page getAllCarsByOwerId(UUID carOwnerId, int currentPage) {
        var totalCount = carRepository.countByOwnerId(carOwnerId);
        var pagedCars = carRepository.findAllByOwerId(carOwnerId, currentPage);
        return PaginationUtil.constructNew(currentPage, totalCount, pagedCars);
    }

    public Car findCarById(UUID carId) throws ApplicationException {
        return carRepository.findById(carId).orElseThrow(()-> new ApplicationException(ErrorType.CAR_NOT_FOUND));
    }

    public Car createNew(Car car) throws ApplicationException {
        if(carRepository.countByOwnerIdAndVin(car.getOwnerId(), car.getVin())>0) {
            throw new ApplicationException(ErrorType.DUPLICATE_ENTRY);
        }
        return carRepository.save(car);
    }

    public Car updateCar(UUID carId, Car amendedCar) throws ApplicationException {
        var modifiedCar = carRepository.findById(carId).map(car->car.mergeWith(amendedCar)).orElseThrow(()->new ApplicationException(ErrorType.CAR_NOT_FOUND));
        return carRepository.update(modifiedCar);
    }

    public void deleteCar(UUID carId) throws ApplicationException {
        var car = carRepository.findById(carId).orElseThrow((()->new ApplicationException(ErrorType.CAR_NOT_FOUND)));
        carRepository.delete(car);
    }

}
