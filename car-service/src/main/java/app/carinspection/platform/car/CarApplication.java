package app.carinspection.platform.car;

import java.time.Duration;

import app.carinspection.platform.car.handler.CarResourceHandler;
import app.carinspection.platform.car.infrastructure.db.DatabaseUtil;
import app.carinspection.platform.car.infrastructure.health.DatabaseHealthCheck;
import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.observe.ObserveFeature;
import io.helidon.webserver.observe.health.HealthObserver;


public class CarApplication {
    private WebServer carServer;
    private static long serverStartTime;

    public static void main(String[] args) {
        serverStartTime = System.currentTimeMillis();
        new CarApplication();
    }

    private CarApplication() {
        LogConfig.configureRuntime();
        var applicationConfig = Config.create();
        start(applicationConfig);
    }

    private void start(Config applicationConfig) {
     ObserveFeature observe = ObserveFeature.builder()
                .observersDiscoverServices(true)
                .addObserver(HealthObserver.builder()
                                     .details(true)
                                     .useSystemServices(true)
                                     .addCheck(new DatabaseHealthCheck())        
                                     .build())
                .build();

        this.carServer = WebServer.builder()
                .featuresDiscoverServices(false)
                .addFeature(observe)
        .config(applicationConfig.get("server")).routing(CarApplication::routing).build();
        this.carServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(()->carServer.stop()));
    }

    private static void routing(HttpRouting.Builder routingBuilder) {
        var dbClient = DatabaseUtil.newClient();
        DatabaseUtil.createTableWhenNotExist(dbClient);
        routingBuilder.register("/cars", new CarResourceHandler(dbClient));
    }

    private static boolean isStarted() {
        return Duration.ofMillis(System.currentTimeMillis() - serverStartTime).getSeconds() >= 8;
    }
}
