package app.carinspection.platform.car.model.entity;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.utils.StringUtils;

@Data
@DynamoDbBean
public class Car {
    public static String TABLE_NAME = "car";
    private static final String INDEX_BY_OWNER_ID = "ix-car-ower-id";
 
    
    @Getter(onMethod = @__(@DynamoDbPartitionKey))
    private UUID id;

    @Getter(onMethod = @__(@DynamoDbSecondaryPartitionKey(indexNames = {INDEX_BY_OWNER_ID}))) 
    private UUID ownerId;

    private String brand;
    private String model;
    private String year;
    private String color;
    private String licensePlate;
    private String vin;
    
     @Getter(onMethod = @__(@DynamoDbVersionAttribute))   
    private Long version;

    public Car mergeWith(Car amendedCar) {
        if(amendedCar != null) {
            if(StringUtils.isNotBlank(amendedCar.getBrand()) && amendedCar.getBrand().compareTo(getBrand()) != 0) {
                setBrand(amendedCar.getBrand());
            }
            if(StringUtils.isNotBlank(amendedCar.getModel()) && amendedCar.getModel().compareTo(getModel()) != 0) {
                setModel(amendedCar.getModel());
            }
            if(StringUtils.isNotBlank(amendedCar.getYear()) && amendedCar.getYear().compareTo(getYear()) != 0) {
                setYear(amendedCar.getYear());
            }
            if(StringUtils.isNotBlank(amendedCar.getColor()) && amendedCar.getColor().compareTo(getColor()) != 0) {
                setColor(amendedCar.getColor());
            }
            if(StringUtils.isNotBlank(amendedCar.getLicensePlate()) && amendedCar.getLicensePlate().compareTo(getLicensePlate()) != 0) {
                setLicensePlate(amendedCar.getLicensePlate());
            }  
        }
        return this;
    }
    
}
