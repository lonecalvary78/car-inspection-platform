package app.carinspection.platform.car.handler;

import java.util.Optional;
import java.util.UUID;

import app.carinspection.platform.car.api.request.ApiRequest;
import app.carinspection.platform.car.api.response.ApiErrorResponse;
import app.carinspection.platform.car.application.CarApplication;
import app.carinspection.platform.car.exception.ApplicationException;
import app.carinspection.platform.car.exception.ErrorType;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class CarResourceHandler implements HttpService {
    private CarApplication carApplication;

    public CarResourceHandler(DynamoDbClient dynamoDbClient) {
        this.carApplication = new CarApplication(dynamoDbClient);
    }

    @Override
    public void routing(HttpRules rules) {
        rules.get("/", this::getAllCarsByOwerId)
        .get("/{carId}",this::findCarById)
        .post("/",this::createNewCar)
        .put("/{carId}",this::updateCar)
        .delete("/{carId}",this::deleteCar);
    }

    private void getAllCarsByOwerId(ServerRequest request, ServerResponse response) {
        try {
            var carOwnerId = UUID.fromString(request.query().first("carOwnerId").asOptional().orElseThrow(()-> new ApplicationException(ErrorType.MISSING_REQUIRED_REFERENCE)));
            var currentPage = Integer.parseInt(request.query().first("currentPage").asOptional().orElse("1"));
            response.send(carApplication.findAllCarsByOwnerId(carOwnerId, currentPage));
        } catch(ApplicationException thrownApplicationException) {
            handleAnyException(response, thrownApplicationException);
        }
    }

    private void findCarById(ServerRequest request, ServerResponse response) {
        try {
            var carId = UUID.fromString(request.path().pathParameters().first("carId").orElseThrow(()-> new ApplicationException(ErrorType.MISSING_UNIQUE_ID)));
            response.send(carApplication.findCarById(carId));
        } catch(ApplicationException thrownApplicationException) {
            handleAnyException(response, thrownApplicationException);
        } 
    }

    private void createNewCar(ServerRequest request, ServerResponse response) {
        try {
            var apiRequest = request.content().asOptional(ApiRequest.class).orElseThrow(()->new ApplicationException(ErrorType.MISSING_CAR_PAYLOAD));
            var car = Optional.ofNullable(apiRequest.data()).orElseThrow(()-> new ApplicationException(ErrorType.MISSING_CAR_PAYLOAD));
            response.status(201).send(carApplication.createNewCar(car));
        } catch(ApplicationException thrownApplicationException) {
            handleAnyException(response, thrownApplicationException);
        }
    }

    private void updateCar(ServerRequest request, ServerResponse response) {
        try {
            var carId = UUID.fromString(request.path().pathParameters().first("carId").orElseThrow(()-> new ApplicationException(ErrorType.MISSING_UNIQUE_ID)));
            var apiRequest = request.content().asOptional(ApiRequest.class).orElseThrow(()->new ApplicationException(ErrorType.MISSING_CAR_PAYLOAD));
            var car = Optional.ofNullable(apiRequest.data()).orElseThrow(()-> new ApplicationException(ErrorType.MISSING_CAR_PAYLOAD));
            response.send(carApplication.updateCar(carId, car));
        } catch(ApplicationException thrownApplicationException) {
            handleAnyException(response, thrownApplicationException);
        }
    }

    private void deleteCar(ServerRequest request, ServerResponse response) {
        try {
           var carId = UUID.fromString(request.path().pathParameters().first("carId").orElseThrow(()-> new ApplicationException(ErrorType.MISSING_UNIQUE_ID)));
           carApplication.deleteCar(carId);
           response.status(204).send();
        } catch(ApplicationException thrownApplicationException) {
            handleAnyException(response, thrownApplicationException);
        }
    }

    private void handleAnyException(ServerResponse response, ApplicationException thrownApplicationException) {
        var errorType = thrownApplicationException.getErrorType();
        response.status(errorType.getStatusCode()).send(ApiErrorResponse.of(errorType));
    }

}
