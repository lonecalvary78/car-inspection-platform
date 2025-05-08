package app.carinspection.platform.inspection.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.Map;

import app.carinspection.platform.inspection.exception.ApplicationException;
import app.carinspection.platform.inspection.exception.ErrorType;
import app.carinspection.platform.inspection.model.dto.InspectionDTO;
import app.carinspection.platform.inspection.model.entity.Inspection;
import app.carinspection.platform.inspection.model.entity.InspectionStatus;
import app.carinspection.platform.inspection.model.entity.InspectionType;
import app.carinspection.platform.inspection.repository.InspectionRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class InspectionSchedulerService {
    private final Logger logger = Logger.getLogger(InspectionSchedulerService.class.getName());
    private final InspectionService inspectionService;
    private final InspectionRepository inspectionRepository;
    private final ScheduledExecutorService scheduler;
    private final Timer scheduleInspectionTimer;
    private final Timer checkRemindersTimer;
    private final Timer updateExpiredTimer;

    public InspectionSchedulerService(DynamoDbClient dynamoDbClient, ScheduledExecutorService scheduler, MeterRegistry meterRegistry) {
        this.inspectionService = new InspectionService(dynamoDbClient);
        this.inspectionRepository = new InspectionRepository(dynamoDbClient);
        this.scheduler = scheduler;
        
        // Initialize metrics
        this.scheduleInspectionTimer = Timer.builder("inspection.schedule")
            .description("Time taken to schedule an inspection")
            .register(meterRegistry);
        this.checkRemindersTimer = Timer.builder("inspection.check_reminders")
            .description("Time taken to check and send reminders")
            .register(meterRegistry);
        this.updateExpiredTimer = Timer.builder("inspection.update_expired")
            .description("Time taken to update expired inspections")
            .register(meterRegistry);
            
        setupSchedulers();
    }

    private void setupSchedulers() {
        // Schedule inspection reminders daily
        scheduler.scheduleAtFixedRate(
            this::checkAndSendReminders,
            getDelayUntilNextDay(),
            TimeUnit.DAYS.toSeconds(1),
            TimeUnit.SECONDS
        );

        // Schedule inspection status updates hourly
        scheduler.scheduleAtFixedRate(
            this::updateExpiredInspections,
            0,
            TimeUnit.HOURS.toSeconds(1),
            TimeUnit.SECONDS
        );
    }

    public InspectionDTO scheduleInspection(UUID carId, UUID inspectorId, InspectionType type, Instant scheduledDate) 
            throws ApplicationException {
        return scheduleInspectionTimer.record(() -> {
            try {
                if (scheduledDate.isBefore(Instant.now())) {
                    throw new ApplicationException(ErrorType.INVALID_REQUEST, "Scheduled date must be in the future");
                }

                InspectionDTO inspection = new InspectionDTO(
                    UUID.randomUUID(),
                    carId,
                    type,
                    scheduledDate,
                    inspectorId,
                    InspectionStatus.SCHEDULED,
                    null,
                    Instant.now(),
                    Instant.now(),
                    null
                );

                return inspectionService.createNewInspection(inspection);
            } catch (ApplicationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void checkAndSendReminders() {
        checkRemindersTimer.record(() -> {
            try {
                logger.info("Checking for upcoming inspections to send reminders");
                
                // Calculate tomorrow's date range
                Instant startOfTomorrow = Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
                Instant endOfTomorrow = startOfTomorrow.plus(1, ChronoUnit.DAYS);

                // Find all scheduled inspections for tomorrow
                Expression filterExpression = Expression.builder()
                    .expression("inspectionStatus = :status AND inspectionDate BETWEEN :start AND :end")
                    .expressionValues(Map.of(
                        ":status", AttributeValue.fromS(InspectionStatus.SCHEDULED.toString()),
                        ":start", AttributeValue.fromS(startOfTomorrow.toString()),
                        ":end", AttributeValue.fromS(endOfTomorrow.toString())
                    ))
                    .build();

                inspectionRepository.findByExpression(filterExpression)
                    .forEach(inspection -> {
                        logger.info("Found upcoming inspection: " + inspection.getId());
                        // Notification logic would go here
                    });
            } catch (Exception e) {
                logger.severe("Failed to check and send reminders: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    private void updateExpiredInspections() {
        updateExpiredTimer.record(() -> {
            try {
                logger.info("Updating expired inspections");
                
                // Find all scheduled inspections that are in the past
                Expression filterExpression = Expression.builder()
                    .expression("inspectionStatus = :status AND inspectionDate < :now")
                    .expressionValues(Map.of(
                        ":status", AttributeValue.fromS(InspectionStatus.SCHEDULED.toString()),
                        ":now", AttributeValue.fromS(Instant.now().toString())
                    ))
                    .build();

                inspectionRepository.findByExpression(filterExpression)
                    .forEach(inspection -> {
                        try {
                            logger.info("Found expired inspection: " + inspection.getId());
                            inspection.setInspectionStatus(InspectionStatus.MISSED);
                            inspectionRepository.update(inspection);
                        } catch (Exception e) {
                            logger.severe("Failed to update inspection " + inspection.getId() + ": " + e.getMessage());
                        }
                    });
            } catch (Exception e) {
                logger.severe("Failed to process expired inspections: " + e.getMessage());
            }
        });
    }

    private long getDelayUntilNextDay() {
        Instant now = Instant.now();
        Instant nextDay = now.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
        return ChronoUnit.SECONDS.between(now, nextDay);
    }
} 