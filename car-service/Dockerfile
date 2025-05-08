FROM bellsoft/liberica-openjre-debian:21

COPY target/*.jar /app/car-service.jar
COPY target/libs/* /app/libs/
ENTRYPOINT ["java","-jar","/app/car-service.jar"]