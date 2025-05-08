package app.carinspection.platform.inspection.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import app.carinspection.platform.inspection.model.dto.InspectionDTO;
import app.carinspection.platform.inspection.model.entity.Inspection;

@Mapper
public interface InspectionMapper {
    InspectionMapper INSTANCE = Mappers.getMapper(InspectionMapper.class);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "carId", source = "carId"),
        @Mapping(target = "inspectionType", source = "inspectionType"),
        @Mapping(target = "inspectionDate", source = "inspectionDate"),
        @Mapping(target = "inspectorId", source = "inspectorId"),
        @Mapping(target = "inspectionStatus", source = "inspectionStatus"),
        @Mapping(target = "inspectionResult", source = "inspectionResult"),
        @Mapping(target = "createdAt", source = "createdAt"),
        @Mapping(target = "updatedAt", source = "updatedAt")
    })
    InspectionDTO toDTO(Inspection inspection);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "carId", source = "carId"),
        @Mapping(target = "inspectionType", source = "inspectionType"),
        @Mapping(target = "inspectionDate", source = "inspectionDate"),
        @Mapping(target = "inspectorId", source = "inspectorId"),
        @Mapping(target = "inspectionStatus", source = "inspectionStatus"),
        @Mapping(target = "inspectionResult", source = "inspectionResult"),
        @Mapping(target = "createdAt", source = "createdAt"),
        @Mapping(target = "updatedAt", source = "updatedAt")
    })    
    Inspection toEntity(InspectionDTO inspectionDTO);
}
