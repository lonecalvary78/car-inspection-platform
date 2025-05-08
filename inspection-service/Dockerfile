FROM bellsoft/liberica-openjre-debian:21

COPY target/*.jar /app/inspection-service.jar
COPY target/libs/* /app/libs/
ENTRYPOINT ["java","-jar","/app/inspection-service.jar"]