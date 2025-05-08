package app.carinspection.platform.inspection.controller;

import java.util.UUID;

import app.carinspection.platform.inspection.api.response.ApiErrorResponse;
import app.carinspection.platform.inspection.api.response.ApiResponse;
import app.carinspection.platform.inspection.exception.ApplicationException;
import app.carinspection.platform.inspection.exception.ErrorType;
import app.carinspection.platform.inspection.infrastructure.validation.ValiadationUtil;
import app.carinspection.platform.inspection.model.dto.InspectionDTO;
import app.carinspection.platform.inspection.service.InspectionService;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class InspectionController implements HttpService {
    private final InspectionService inspectionService;

    public InspectionController(DynamoDbClient dynamoDbClient) {
        this.inspectionService = new InspectionService(dynamoDbClient);
    }

    @Override
    public void routing(HttpRules rules) {
        rules.get("/", this::getAllInspections)
            .get("/{inspectionId}", this::getInspectionById)
            .post("/", this::createInspection)
            .put("/{inspectionId}", this::updateInspection)
            .delete("/{inspectionId}", this::deleteInspection);
    }

    private void getAllInspections(ServerRequest request, ServerResponse response) {
        try {
            var inspections = inspectionService.findAllByInspectorId(UUID.fromString(request.query().first("inspectorId").orElseThrow(()-> new ApplicationException(ErrorType.MISSING_REQUIRED_INSPECTOR_ID))));
            response.send(ApiResponse.of(inspections));
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }

    private void getInspectionById(ServerRequest request, ServerResponse response) {
        try {
            UUID inspectionId = UUID.fromString(request.path().pathParameters().first("inspectionId").orElseThrow(()-> new ApplicationException(ErrorType.MISSING_REQUIRED_UNIQUE_ID)));
            response.send(ApiResponse.of(inspectionService.findById(inspectionId)));
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }

    private void createInspection(ServerRequest request, ServerResponse response) {
        try {
            var inspectionDTO = request.content().asOptional(InspectionDTO.class).orElseThrow(()-> new ApplicationException(ErrorType.MISSING_REQUIRED_PAYLOAD));
            ValiadationUtil.validate(inspectionDTO);
            response.status(201).send(ApiResponse.of(inspectionService.createNewInspection(inspectionDTO)));
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }

    private void updateInspection(ServerRequest request, ServerResponse response) {
        try {
            UUID inspectionId = UUID.fromString(request.path().pathParameters().first("inspectionId").orElseThrow(()-> new ApplicationException(ErrorType.MISSING_REQUIRED_UNIQUE_ID)));
            InspectionDTO inspectionDTO = request.content().asOptional(InspectionDTO.class).orElseThrow(()-> new ApplicationException(ErrorType.MISSING_REQUIRED_PAYLOAD));
            ValiadationUtil.validate(inspectionDTO);
            response.send(ApiResponse.of(inspectionService.updateInspection(inspectionId, inspectionDTO)));
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }

    private void deleteInspection(ServerRequest request, ServerResponse response) {
        try {
            UUID inspectionId = UUID.fromString(request.path().pathParameters().first("inspectionId").orElseThrow(()-> new ApplicationException(ErrorType.MISSING_REQUIRED_UNIQUE_ID)));
            inspectionService.deleteInspection(inspectionId);
        } catch (ApplicationException thrownApplicationException) {
            handleException(response, thrownApplicationException);
        }
    }

    /**
     * Handle exception
     * @param response
     * @param thrownApplicationException
     */
    private void handleException(ServerResponse response, ApplicationException thrownApplicationException) {
        response.status(thrownApplicationException.getErrorType().getStatusCode()).send(ApiErrorResponse.of(thrownApplicationException.getErrorType()));
    }
}
