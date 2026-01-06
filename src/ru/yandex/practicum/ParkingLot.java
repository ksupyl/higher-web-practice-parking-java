package ru.yandex.practicum;

import ru.yandex.practicum.model.ParkingSpot;
import ru.yandex.practicum.model.SpotType;
import ru.yandex.practicum.model.Car;
import ru.yandex.practicum.model.CarType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class ParkingLot extends AbstractParkingLot {

    private final List<ParkingSpot> spots = new ArrayList<>();
    private final Map<String, ParkingSpot> occupiedByNumber = new HashMap<>();

    public ParkingLot(int totalSpots, int electricSpots, int premiumSpots) {
        super(totalSpots, electricSpots, premiumSpots);

        int normalSpots = totalSpots - electricSpots - premiumSpots;

        if (normalSpots < 0) {
            normalSpots = 0;
        }

        int id = 1;

        // Обычные места
        for (int i = 0; i < normalSpots; i++) {
            spots.add(new ParkingSpot(id++, SpotType.NORMAL));
        }

        // Места для электромобилей
        for (int i = 0; i < electricSpots; i++) {
            spots.add(new ParkingSpot(id++, SpotType.ELECTRIC));
        }

        // Премиум-места
        for (int i = 0; i < premiumSpots; i++) {
            spots.add(new ParkingSpot(id++, SpotType.PREMIUM));
        }
    }

    @Override
    boolean isEmpty() {
        return occupiedByNumber.isEmpty();
    }


    @Override
    public void enter(String carType, String number) throws ParkingException {
        try {
            // Обработка входных данных
            CarType type = parseCarType(carType);
            String normalizedNumber = normalizeNumber(number);

            // Защита от повторной парковки одной и той же машины
            if (occupiedByNumber.containsKey(normalizedNumber)) {
                throw new ParkingException("Car already parked: " + normalizedNumber);
            }

            // Находим свободное место для машины в соответствии с ТЗ
            ParkingSpot spot = findSpotFor(type);
            if (spot == null) {
                throw new ParkingException("No free spot for car type: " + type);
            }

            // Занимаем место
            Car car = new Car(normalizedNumber, type);
            boolean parked = spot.park(car);
            if (!parked) {
                throw new ParkingException("Failed to park car: " + normalizedNumber);
            }

            // Фиксируем место за номером машины
            occupiedByNumber.put(normalizedNumber, spot);

        } catch (ParkingException e) {
            // Обработка ожидаемых ошибок
            throw e;
        } catch (Exception e) {
            // Обработка любой неожиданной ошибки
            throw new ParkingException("Error while processing ENTER: " + e.getMessage());
        }
    }

    @Override
    public void leave(String number) throws ParkingException {
        try {
            // Обработка входных данных
            String normalizedNumber = normalizeNumber(number);

            // Поиск места машины
            ParkingSpot spot = occupiedByNumber.get(normalizedNumber);
            if (spot == null) {
                throw new ParkingException("Car is not parked: " + normalizedNumber);
            }

            // Освобождаем место
            spot.free();

            // Удаляем информацию о машине и её месте
            occupiedByNumber.remove(normalizedNumber);

        } catch (ParkingException e) {
            // Обработка ожидаемых ошибок
            throw e;
        } catch (Exception e) {
            // Обработка любой неожиданной ошибки
            throw new ParkingException("Error while processing LEAVE: " + e.getMessage());
        }
    }

    @Override
    public Set<String> getNumbers() {
        return new HashSet<>(occupiedByNumber.keySet());
    }

    // Проверка на пустую или null строку
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Нормализация номера машины
    private static String normalizeNumber(String number) throws ParkingException {
        if (isBlank(number)) {
            throw new ParkingException("Car number is empty");
        }
        return number.trim();
    }

    // Перевод строки в тип машины
    private static CarType parseCarType(String carType) throws ParkingException {
        if (isBlank(carType)) {
            throw new ParkingException("Car type is empty");
        }
        String normalized = carType.trim().toUpperCase();

        return switch (normalized) {
            case "NORMAL" -> CarType.NORMAL;
            case "ELECTRIC" -> CarType.ELECTRIC;
            case "PREMIUM" -> CarType.PREMIUM;
            default -> throw new ParkingException("Unknown car type: " + carType);
        };

    }

    // Поиск первого свободного места нужного типа
    private ParkingSpot findFirstFreeSpot(SpotType type) {
        for (ParkingSpot spot : spots) {
            if (spot.getType() == type && spot.isFree()) {
                return spot;
            }
        }
        return null;
    }

    // Поиск места для машины
    private ParkingSpot findSpotFor(CarType carType) {
        if (carType == CarType.NORMAL) {
            return findFirstFreeSpot(SpotType.NORMAL);
        }
        if (carType == CarType.ELECTRIC) {
            return findFirstFreeSpot(SpotType.ELECTRIC);
        }

        ParkingSpot premium = findFirstFreeSpot(SpotType.PREMIUM);
        if (premium != null) {
            return premium;
        }
        return findFirstFreeSpot(SpotType.NORMAL);
    }
}
