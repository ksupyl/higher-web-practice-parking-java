package ru.yandex.practicum;

import java.util.HashSet;
import java.util.Set;

public class ParkingLot extends AbstractParkingLot {

    public ParkingLot(int totalSpots, int electricSpots, int premiumSpots) {
        super(totalSpots, electricSpots, premiumSpots);
    }

    @Override
    public void enter(String carType, String number) throws ParkingException {
        //TODO
    }

    @Override
    public void leave(String number) throws ParkingException {
        //TODO
    }

    @Override
    public Set<String> getNumbers() {
        //TODO
        return new HashSet<>();
    }
}
