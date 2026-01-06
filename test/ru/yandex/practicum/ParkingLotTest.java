package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {
    // Парковка (enter)
    @Test
    void enterAddsCarNumberToParking() {
        ParkingLot lot = new ParkingLot(1, 0, 0);

        boolean thrown = false;
        try {
            lot.enter("NORMAL", "A111AA");
        } catch (ParkingException e) {
            thrown = true;
        }

        assertFalse(thrown);
        assertTrue(lot.getNumbers().contains("A111AA"));
        assertFalse(lot.isEmpty());
    }

    // Отъезд (leave)
    @Test
    void leaveRemovesCarNumberAndFreesSpot() {
        ParkingLot lot = new ParkingLot(1, 0, 0);

        boolean thrownEnter = false;
        try {
            lot.enter("NORMAL", "A111AA");
        } catch (ParkingException e) {
            thrownEnter = true;
        }
        assertFalse(thrownEnter);

        boolean thrownLeave = false;
        try {
            lot.leave("A111AA");
        } catch (ParkingException e) {
            thrownLeave = true;
        }

        assertFalse(thrownLeave);
        assertFalse(lot.getNumbers().contains("A111AA"));
        assertTrue(lot.isEmpty());
    }

    // Совместимость типов
    @Test
    void normalCarCannotParkIfOnlyPremiumSpotsExist() {
        // Только premium, а пытаемся припарковать обычную машину
        ParkingLot lot = new ParkingLot(1, 0, 1);

        boolean thrown = false;
        try {
            lot.enter("NORMAL", "N111NN");
        } catch (ParkingException e) {
            thrown = true;
            assertNotNull(e);
        }

        assertTrue(thrown);
    }

    @Test
    void electricCarCannotParkIfNoElectricSpotsExist() {
        // Только normal, а пытаемся припарковать электрическую машину
        ParkingLot lot = new ParkingLot(1, 0, 0);

        boolean thrown = false;
        try {
            lot.enter("ELECTRIC", "E111EE");
        } catch (ParkingException e) {
            thrown = true;
            assertNotNull(e);
        }

        assertTrue(thrown);
    }

    @Test
    void premiumCarCanParkOnNormalWhenNoPremiumSpotsExist() {
        // Одно normal место для Premium
        ParkingLot lot = new ParkingLot(1, 0, 0);

        boolean thrown = false;
        try {
            lot.enter("PREMIUM", "P111PP");
        } catch (ParkingException e) {
            thrown = true;
        }

        assertFalse(thrown);
        assertTrue(lot.getNumbers().contains("P111PP"));
    }

    @Test
    void premiumCarPrefersPremiumSpotWhenAvailable() {
        // Одно normal и одно premium
        ParkingLot lot = new ParkingLot(2, 0, 1);

        boolean thrownPremium = false;
        try {
            lot.enter("PREMIUM", "P111PP");
        } catch (ParkingException e) {
            thrownPremium = true;
        }
        assertFalse(thrownPremium);

        // Если premium занял premium-место, normal должен суметь припарковаться
        boolean thrownNormal = false;
        try {
            lot.enter("NORMAL", "N111NN");
        } catch (ParkingException e) {
            thrownNormal = true;
        }
        assertFalse(thrownNormal);
    }

    // Полная парковка
    @Test
    void enterThrowsWhenParkingIsFull() {
        // Заезжает сначала одна Normal машина, а потом пытается вторая Normal
        ParkingLot lot = new ParkingLot(1, 0, 0);

        boolean thrown1 = false;
        try {
            lot.enter("NORMAL", "A111AA");
        } catch (ParkingException e) {
            thrown1 = true;
        }
        assertFalse(thrown1);

        boolean thrown2 = false;
        try {
            lot.enter("NORMAL", "B222BB");
        } catch (ParkingException e) {
            thrown2 = true;
            assertNotNull(e);
        }

        assertTrue(thrown2);
        assertTrue(lot.getNumbers().contains("A111AA"));
        assertFalse(lot.getNumbers().contains("B222BB"));
    }

    //Проверка корректности парковки, если запрошено больше электро/премиум мест, чем totalSpots,
    @Test
    void constructorClampsSpotCountsWhenSumExceedsTotal() {
        // total=3, electric=2, premium=5 => ожидание: electric=2, premium=1, normal=0
        ParkingLot lot = new ParkingLot(3, 2, 5);

        boolean thrownE1 = false;
        try {
            lot.enter("ELECTRIC", "E111EE");
        } catch (ParkingException e) {
            thrownE1 = true;
        }
        assertFalse(thrownE1);

        boolean thrownE2 = false;
        try {
            lot.enter("ELECTRIC", "E222EE");
        } catch (ParkingException e) {
            thrownE2 = true;
        }
        assertFalse(thrownE2);

        boolean thrownP1 = false;
        try {
            lot.enter("PREMIUM", "P111PP");
        } catch (ParkingException e) {
            thrownP1 = true;
        }
        assertFalse(thrownP1);

        // 4-я машина НЕ должна встать, потому что totalSpots = 3
        boolean thrownP2 = false;
        try {
            lot.enter("PREMIUM", "P222PP");
        } catch (ParkingException e) {
            thrownP2 = true;
            assertNotNull(e);
        }
        assertTrue(thrownP2);

        // Обычная машина тоже НЕ должна встать, потому что обычных мест (normal) = 0
        boolean thrownN = false;
        try {
            lot.enter("NORMAL", "N111NN");
        } catch (ParkingException e) {
            thrownN = true;
            assertNotNull(e);
        }
        assertTrue(thrownN);
    }

    // Обработка непонятных ошибок
    // Пытаемся убрать с парковки машину, которой и не было
    @Test
    void leaveThrowsIfCarWasNotParked() {
        ParkingLot lot = new ParkingLot(1, 0, 0);

        boolean thrown = false;
        try {
            lot.leave("A111AA");
        } catch (ParkingException e) {
            thrown = true;
            assertNotNull(e);
        }

        assertTrue(thrown);
    }

    // Пытаемся припарковать два раза одну и ту же машину
    @Test
    void enterThrowsIfCarAlreadyParked() {
        ParkingLot lot = new ParkingLot(2, 0, 0);

        boolean thrown1 = false;
        try {
            lot.enter("NORMAL", "A111AA");
        } catch (ParkingException e) {
            thrown1 = true;
        }
        assertFalse(thrown1);

        boolean thrown2 = false;
        try {
            lot.enter("NORMAL", "A111AA");
        } catch (ParkingException e) {
            thrown2 = true;
            assertNotNull(e);
        }

        assertTrue(thrown2);
    }

    // Пытаемся припарковать неизвестный тип машины
    @Test
    void enterThrowsOnUnknownCarType() {
        ParkingLot lot = new ParkingLot(1, 0, 0);

        boolean thrown = false;
        try {
            lot.enter("FLYING", "A111AA");
        } catch (ParkingException e) {
            thrown = true;
            assertNotNull(e);
        }

        assertTrue(thrown);
    }

    // Подаём пустой номер
    @Test
    void enterThrowsOnBlankNumber() {
        ParkingLot lot = new ParkingLot(1, 0, 0);

        boolean thrown = false;
        try {
            lot.enter("NORMAL", "   ");
        } catch (ParkingException e) {
            thrown = true;
            assertNotNull(e);
        }

        assertTrue(thrown);
    }
}
