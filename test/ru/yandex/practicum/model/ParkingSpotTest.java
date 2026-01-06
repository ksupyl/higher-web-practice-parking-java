package ru.yandex.practicum.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingSpotTest {
    // Создание нового места
    @Test
    void newSpotIsFree() {
        ParkingSpot spot = new ParkingSpot(1, SpotType.NORMAL);

        assertTrue(spot.isFree());
        assertNull(spot.getParkedCar());
    }

    // Парковка и освобождение места
    @Test
    void parkAndFreeWorkCorrectly() {
        ParkingSpot spot = new ParkingSpot(1, SpotType.NORMAL);
        Car car = new Car("A111AA", CarType.NORMAL);

        assertTrue(spot.park(car));
        assertFalse(spot.isFree());
        assertEquals(car, spot.getParkedCar());

        spot.free();
        assertTrue(spot.isFree());
        assertNull(spot.getParkedCar());
    }

    // Парковка первой машины, потом пытаемся вторую
    @Test
    void cannotParkSecondCarWhenOccupied() {
        ParkingSpot spot = new ParkingSpot(1, SpotType.NORMAL);

        assertTrue(spot.park(new Car("A111AA", CarType.NORMAL)));
        assertFalse(spot.park(new Car("B222BB", CarType.NORMAL)));
    }
}
