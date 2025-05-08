package app.carinspection.platform.car.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import app.carinspection.platform.car.model.entity.Car;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class CarRepository {
    private DynamoDbTable<Car> carTable;

    public CarRepository(DynamoDbClient dynamoDbClient) {
        this.carTable = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build().table(Car.TABLE_NAME, TableSchema.fromClass(Car.class));
    }


    public List<Car> findAllByOwnerId(UUID ownerId) {
        return carTable.scan(ScanEnhancedRequest.builder().filterExpression(filterByOwnerId(ownerId)).build()).items().stream().toList();
    }

    public Optional<Car> findById(UUID id) {
        return Optional.ofNullable(carTable.getItem(Key.builder().partitionValue(id.toString()).build()));
    }
    
    public Car save(Car car) {
        carTable.putItem(car);
        return car;
    }

    public Car update(Car car) {
        return carTable.updateItem(car);
    }

    public void delete(Car car) {
        carTable.deleteItem(car);
    }
    
    public Long countAllByOwnerId(UUID ownerId) {
        return carTable.scan(ScanEnhancedRequest.builder().filterExpression(filterByOwnerId(ownerId)).build()).items().stream().count();
    }

    public Long countAllByOwnerIdAndLicensePlate(UUID ownerId, String licensePlate) {
        return carTable.scan(ScanEnhancedRequest.builder().filterExpression(filterByOwnerIdAndLicensePlate(ownerId, licensePlate)).build()).items().stream().count();
    }

    private Expression filterByOwnerId(UUID ownerId) {
        return Expression.builder().expression("ownerId = :ownerId").expressionValues(Map.of(":ownerId", AttributeValue.builder().s(ownerId.toString()).build())).build();
    }

    private Expression filterByOwnerIdAndLicensePlate(UUID ownerId, String licensePlate) {
        return Expression.builder().expression("ownerId = :ownerId AND licensePlate = :licensePlate").expressionValues(Map.of(":ownerId", AttributeValue.builder().s(ownerId.toString()).build(), ":licensePlate", AttributeValue.builder().s(licensePlate).build())).build();
    }
}
