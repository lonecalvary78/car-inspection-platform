package app.carinspection.platform.car.service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import app.carinspection.platform.car.exception.ApplicationException;
import app.carinspection.platform.car.exception.ErrorType;
import app.carinspection.platform.car.mapper.CarMapper;
import app.carinspection.platform.car.model.dto.CarDTO;
import app.carinspection.platform.car.model.entity.Car;
import app.carinspection.platform.car.repository.CarRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class CarService {
    private final Logger logger = Logger.getLogger(CarService.class.getName());
    private final CarRepository carRepository;

    public CarService(DynamoDbClient dynamoDbClient) {
        this.carRepository = new CarRepository(dynamoDbClient);
    }

    public List<CarDTO> findAllByOwnerId(UUID ownerId) {
        logger.info("findAllByOwnerId");
        return carRepository.findAllByOwnerId(ownerId).stream().map(this::toDTO).toList();
    }

    public CarDTO findById(UUID carId) throws ApplicationException {
        logger.info("findById");
        return carRepository.findById(carId).map(this::toDTO).orElseThrow(() -> new ApplicationException(ErrorType.CAR_NOT_FOUND));
    }

    public CarDTO createNewCar(CarDTO carDTO) throws ApplicationException {
        logger.info("createNewCar");
        if (carRepository.countAllByOwnerIdAndLicensePlate(carDTO.ownerId(), carDTO.licensePlate()) > 0) {
            throw new ApplicationException(ErrorType.CAR_ALREADY_EXISTS);
        }
        
        var car = CarMapper.INSTANCE.toEntity(carDTO);
        return toDTO(carRepository.save(car));
    }

    public CarDTO updateCar(UUID carId, CarDTO carDTO) throws ApplicationException {
        logger.info("updateCar");
        var updatedCar = carRepository.findById(carId).map(car->car.mergeWith(CarMapper.INSTANCE.toEntity(carDTO))).orElseThrow(() -> new ApplicationException(ErrorType.CAR_NOT_FOUND));
        return toDTO(carRepository.save(updatedCar));
    }

    public void deleteCar(UUID carId) throws ApplicationException {
        logger.info("deleteCar");
        var car = carRepository.findById(carId).orElseThrow(() -> new ApplicationException(ErrorType.CAR_NOT_FOUND));
        carRepository.delete(car);
    }

    private CarDTO toDTO(Car car) {
        return CarMapper.INSTANCE.toDTO(car);
    }
}
