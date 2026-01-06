package ru.yandex.practicum.model;

import java.util.Objects;

public final class Client {
    private final String id;
    private final Car car;

    public Client(String id, Car car) {
        this.id = Objects.requireNonNull(id, "id must not be null").trim();
        this.car = Objects.requireNonNull(car, "car must not be null");
    }

    public String getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }
}
