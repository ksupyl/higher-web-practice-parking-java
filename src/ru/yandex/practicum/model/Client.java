package ru.yandex.practicum.model;

import java.util.Objects;

public final class Client {
    private final String id;
    private final Car car;

    public Client(String id, Car car) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id must not be null or empty");
        }
        if (car == null) {
            throw new IllegalArgumentException("car must not be null");
        }
        this.id = id.trim();
        this.car = car;
    }

    public String getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }
}
