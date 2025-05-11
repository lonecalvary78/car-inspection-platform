package app.carinspection.platform.car.model;

import java.util.List;

import app.carinspection.platform.car.model.entity.Car;

public record Page(int currentPage, int totalPages, List<Car> data) {
    public static Page of(int currentPage, int totalPages, List<Car> data) {
        return new Page(currentPage, totalPages, data);
    }
}
