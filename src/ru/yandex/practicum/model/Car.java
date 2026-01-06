package ru.yandex.practicum.model;

import java.util.Objects;

public class Car {
    private final String number;
    private final CarType type;

    public Car(String number, CarType type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public CarType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Car otherCar = (Car) obj;
        return Objects.equals(number, otherCar.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "Car{"
                + "number='" + number + '\''
                + ", type=" + type
                + '}';
    }
}
