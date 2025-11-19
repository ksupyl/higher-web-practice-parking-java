package ru.yandex.practicum;

import java.util.Set;

public abstract class AbstractParkingLot {

    public AbstractParkingLot(int totalSpots, int electricSpots, int premiumSpots) {

    }

    boolean isEmpty() {
        return false;
    }


    public abstract void enter(String carType, String number) throws ParkingException;

    public abstract void leave(String number) throws ParkingException;

    public abstract Set<String> getNumbers();
}
