package app.carinspection.platform.car.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import app.carinspection.platform.car.model.dto.CarDTO;
import app.carinspection.platform.car.model.entity.Car;

@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);
    
    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "ownerId", source = "ownerId"),
        @Mapping(target = "brand", source = "brand"),
        @Mapping(target = "model", source = "model"),
        @Mapping(target = "year", source = "year"),
        @Mapping(target = "color", source = "color"),
        @Mapping(target = "licensePlate", source = "licensePlate"),
        @Mapping(target = "vin", source = "vin"),
        @Mapping(target = "createdAt", source = "createdAt"),
        @Mapping(target = "updatedAt", source = "updatedAt"),        
        @Mapping(target = "version", ignore = true)
    })
    CarDTO toDTO(Car car);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "ownerId", source = "ownerId"),
        @Mapping(target = "brand", source = "brand"),
        @Mapping(target = "model", source = "model"),
        @Mapping(target = "year", source = "year"),
        @Mapping(target = "color", source = "color"),
        @Mapping(target = "licensePlate", source = "licensePlate"),
        @Mapping(target = "vin", source = "vin"),
        @Mapping(target = "createdAt", source = "createdAt"),
        @Mapping(target = "updatedAt", source = "updatedAt"),
        @Mapping(target = "version", ignore = true)
    })
    Car toEntity(CarDTO carDTO);
}
