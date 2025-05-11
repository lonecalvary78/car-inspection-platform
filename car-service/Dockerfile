FROM bellsoft/liberica-openjdk-rocky:21

ARG IMAGE_VERSION=1.0.0
ARG SERVER_PORT=8181
LABEL image.name="car-service"
LABEL image.version="${IMAGE_VERSION}"
LABEL maintainer="lonecalvary78.de@gmail.com"
COPY target/car-service.jar /app/car-service.jar
COPY target/lib/ /app/lib/

EXPOSE ${SERVER_PORT}
ENV SERVER_PORT=${SERVER_PORT}
ENTRYPOINT ["java","-jar","/app/car-service.jar"]