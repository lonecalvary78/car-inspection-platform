package app.carinspection.platform.car.repository;

import app.carinspection.platform.car.infrastructure.pagination.RangeQueryUntil;
import app.carinspection.platform.car.model.entity.Car;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CarRepository {
    private DynamoDbTable<Car> carTable;

    public CarRepository(DynamoDbClient dynamoDbClient) {
        this.carTable = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build().table(Car.TABLE_NAME, TableSchema.fromBean(Car.class));
    }

    public List<Car> findAllByOwerId(UUID ownerId, int currentPage) {
        if(ownerId == null) {
            throw new IllegalArgumentException("Car owner should not be empty");
        }
        if(currentPage <=0) {
            throw new IllegalArgumentException("Current page should be greater 0");
        }
        var startPos = RangeQueryUntil.getStartPosition(currentPage);
        var endPos = RangeQueryUntil.getEndPosition(currentPage);
        return carTable.query(r -> r.queryConditional(
            QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(ownerId.toString())
                .build())
        ).limit(endPos)).items().stream().skip(startPos).toList();
    }

    public Optional<Car> findById(UUID carId) {
        if(carId == null) {
            throw new IllegalArgumentException("carId should not be empty");
        }
        return Optional.ofNullable(carTable.getItem(Key.builder().partitionValue(carId.toString()).build()));
    }

    public Car save(Car car){
        if(car == null) {
            throw new IllegalArgumentException("Car should not be empty");
        }
        carTable.putItem(car);
        return car;
    }

    public Car update(Car car) {
        if(car == null || car.getId() == null) {
            throw new IllegalArgumentException("Car should not be empty");
        }
        return carTable.updateItem(car);
    }

    public void delete(Car car) {
        if(car == null || car.getId() == null) {
            throw new IllegalArgumentException("Car should not be empty");
        }
        carTable.deleteItem(car);
    }

    public Long countByOwnerId(UUID ownerId){
        if(ownerId == null) {
            throw new IllegalArgumentException("ownerId should not be empty");
        }
        AtomicLong count = new AtomicLong(0);
        carTable.query(r -> r.queryConditional(
            QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(ownerId.toString())
                .build())
        )).forEach(page -> count.addAndGet(page.items().size()));
        return count.get();
    }

    public Long countByOwnerIdAndVin(UUID ownerId, String vin){
        if(ownerId == null) {
            throw new IllegalArgumentException("ownerId should not be empty");
        }
        if(vin == null) {
            throw new IllegalArgumentException("vin should not be empty");
        }

        return carTable.query(r -> r.queryConditional(
            QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(ownerId.toString())
                .build())
        )).items().stream().filter(car->car.getVin().equals(vin)).count();
    }   
}
