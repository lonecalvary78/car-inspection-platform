package app.carinspection.platform.car.repostory;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import app.carinspection.platform.car.infrastructure.db.DatabaseUtil;
import app.carinspection.platform.car.model.entity.Car;
import app.carinspection.platform.car.repository.CarRepository;
import app.carinspection.platform.car.testcase.SimpleTestCase;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(value=org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class CarRepositoryTest {
    private LocalStackContainer localStackContainer;
    private DynamoDbClient dynamoDbClient;
    private CarRepository carRepository;

    @BeforeAll
    public void setup() {
        this.localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:4.3.0"));
        this.localStackContainer.start();
        this.dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                .credentialsProvider(() -> AwsBasicCredentials.create(
                        localStackContainer.getAccessKey(),
                        localStackContainer.getSecretKey()))
                .region(Region.of(localStackContainer.getRegion()))        
                .build();
        this.carRepository = new CarRepository(dynamoDbClient);
        DatabaseUtil.createTableWhenNotExist(dynamoDbClient);                
    }


    @Test
    @DisplayName("To find all cars based on car ownder")
    @Order(1)
    public void findAllByOwerId() {
        Assertions.assertThrows(IllegalArgumentException.class,()->carRepository.findAllByOwerId(null,0));
    }

    @ParameterizedTest
    @MethodSource("constructTestcasesForFindById")
    @DisplayName("To get car based on the unique Id")
    @Order(2)
    public void findCarById(SimpleTestCase testCase) {
        var carId = (UUID)testCase.inputData();
        if(testCase.throwAnyException()) {
            Assertions.assertThrows(IllegalArgumentException.class,() -> carRepository.findById(carId));       
        } else {
            Assertions.assertDoesNotThrow(()->carRepository.findById(carId));
        }
    }

    @Test
    @DisplayName("Save a new car")
    @Order(3)
    public void saveNeCar() {
        Assertions.assertThrows(IllegalArgumentException.class,() -> carRepository.save(null));        
    }

    @Test
    @DisplayName("Update an existing car")
    @Order(4) 
    public void updateCar() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> carRepository.update(null)); 
    }

    @ParameterizedTest
    @MethodSource("constructTestCasesForDeleteCar")
    @DisplayName("Delete an existing car")
    @Order(5) 
    public void deleteCar(SimpleTestCase testCase) {
        var car = (Car)testCase.inputData();
        if(testCase.throwAnyException()) {
            Assertions.assertThrows(testCase.expectedException(), () -> carRepository.delete(car));
        } else {
            Assertions.assertDoesNotThrow(()->carRepository.delete(car));

        }
    }   

    @Test
    @DisplayName("Get total records based on the car owner")
    @Order(6)
    public void getTotalRecordsByOwner() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->carRepository.countByOwnerId(null));
    }

    @Test
    @DisplayName("Get total records based on the car owner and vin number")
    @Order(7)
    public void getTotalRecordsByOwnerAndVinNumber() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->carRepository.countByOwnerIdAndVin(null, null));
    }

    @AfterAll
    public void teardown() {
        this.dynamoDbClient.close();
        this.localStackContainer.stop();
        this.localStackContainer.close();
    }

    private Stream<SimpleTestCase> constructTestcasesForFindById() {
        return Stream.of(
            SimpleTestCase.of(null, null,true, IllegalArgumentException.class),
            SimpleTestCase.of(UUID.randomUUID(), null,false, null)
        );
    }

    private Stream<SimpleTestCase> constructTestCasesForDeleteCar() {
        var car = new Car();
        car.setId(UUID.randomUUID());
        car.setOwnerId(UUID.randomUUID());
        car.setVin("676887");
        return Stream.of(
            SimpleTestCase.of(car,null,false, null),
            SimpleTestCase.of(null,null,true, IllegalArgumentException.class)
        );
    }
}
