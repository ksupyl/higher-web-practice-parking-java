package ru.yandex.practicum.model;

public class ParkingSpot {
    private final int id;
    private final SpotType type;

    private Car parkedCar;

    public ParkingSpot(int id, SpotType type) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public SpotType getType() {
        return type;
    }

    public boolean isFree() {
        return parkedCar == null;
    }

    public Car getParkedCar() {
        return parkedCar;
    }

    // Парковка машины
    public boolean park(Car car) {
        if ((car == null) || !isFree()) {
            return false;
        }
        parkedCar = car;
        return true;
    }

    // Освобождение места
    public void free() {
        parkedCar = null;
    }

    @Override
    public String toString(){
        return "ParkingSpot{"
                + "id=" + id
                + ", type=" + type
                + ", parkedCar=" + parkedCar
                + '}';
    }
}
