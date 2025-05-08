package app.carinspection.platform.inspection.service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import app.carinspection.platform.inspection.exception.ApplicationException;
import app.carinspection.platform.inspection.exception.ErrorType;
import app.carinspection.platform.inspection.mapper.InspectionMapper;
import app.carinspection.platform.inspection.model.dto.InspectionDTO;
import app.carinspection.platform.inspection.repository.InspectionRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class InspectionService {
    private final Logger logger = Logger.getLogger(InspectionService.class.getName());

    private final InspectionRepository inspectionRepository;

    public InspectionService(DynamoDbClient dynamoDbClient) {
        this.inspectionRepository = new InspectionRepository(dynamoDbClient);
    }

    /**
     * Find all inspections by inspector id
     * @param inspectorId
     * @return
     */
    public List<InspectionDTO> findAllByInspectorId(UUID inspectorId) {
        logger.info("findAllByInspectorId");
        return inspectionRepository.findAllByInspetorId(inspectorId).stream().map(InspectionMapper.INSTANCE::toDTO).toList();
    }

    /**
     * Find inspection by id
     * @param id
     * @return
     */
    public InspectionDTO findById(UUID id) throws ApplicationException {
        logger.info("findById");
        return inspectionRepository.findById(id).map(InspectionMapper.INSTANCE::toDTO).orElseThrow(()-> new ApplicationException(ErrorType.INSPECTION_NOT_FOUND));
    }

    /**
     * Create new inspection
     * @param inspectionDTO
     * @return
     */
    public InspectionDTO createNewInspection(InspectionDTO inspectionDTO) throws ApplicationException {
        logger.info("createNewInspection");
        var inspection = InspectionMapper.INSTANCE.toEntity(inspectionDTO);

        if(isInspectionForCarAlreadyExists(inspection.getCarId())) {
            throw new ApplicationException(ErrorType.INSPECTION_ALREADY_EXISTS);
        }

        return InspectionMapper.INSTANCE.toDTO(inspectionRepository.save(inspection));
    }
    
    /**
     * Update inspection
     * @param id
     * @param amendedInspectionDTO
     * @return
     */
    public InspectionDTO updateInspection(UUID id, InspectionDTO amendedInspectionDTO) throws ApplicationException {
        logger.info("updateInspection");
        var updatedInspection = inspectionRepository.findById(id).map(inspection->inspection.mergeWith(InspectionMapper.INSTANCE.toEntity(amendedInspectionDTO))).orElseThrow(()-> new ApplicationException(ErrorType.INSPECTION_NOT_FOUND));
        return InspectionMapper.INSTANCE.toDTO(inspectionRepository.save(updatedInspection));
    }

    /**
     * Delete inspection
     * @param id
     */
    public void deleteInspection(UUID id) throws ApplicationException {
        logger.info("deleteInspection");
        var inspection = inspectionRepository.findById(id).orElseThrow(()-> new ApplicationException(ErrorType.INSPECTION_NOT_FOUND));
        inspectionRepository.delete(inspection);
    }

    /**
     * Check if inspection for car already exists
     * @param carId
     * @return
     */
    private boolean isInspectionForCarAlreadyExists(UUID carId) {
        return inspectionRepository.countByCarId(carId) > 0;
    }
    
}
