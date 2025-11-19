package ru.yandex.practicum;

import java.util.*;
import java.util.stream.Collectors;

public class ParkMate {

    private static String[] EVENTS = {"ENTER", "LEAVE", "LEAVE", "ENTER", "LEAVE", "LEAVE", "PASS"};
    private static String[] CAR_TYPES = {"NORMAL", "ELECTRIC", "PREMIUM"};
    private static Character[] NUMA = {'A', 'B', 'Е', 'K', 'M', 'H', 'O', 'P', 'C', 'T', 'Y', 'X'};

    public static void main(String[] args) throws InterruptedException {
        System.out.println("parking open");
        int totalSpots = Math.max(20, (int) Math.round(Math.random() * 100));
        int electricSpots = Math.max((int) Math.round(totalSpots * 0.1), (int) Math.round(Math.random() * 20));
        int premiumSpots = Math.max((int) Math.round(totalSpots * 0.1), (int) Math.round(Math.random() * 20));
        int totalCars = (int) (Math.random() * 1000);

        List<AbstractParkingLot> parkingLots = new ArrayList<AbstractParkingLot>();
        AbstractParkingLot validatingLot = new ValidationParkingLot(totalSpots, electricSpots, premiumSpots);
        parkingLots.add(validatingLot);
        parkingLots.add(new ParkingLot(totalSpots, electricSpots, premiumSpots));

        boolean isEmpty = false;
        while (!isEmpty || totalCars > 0) {
            ParkingException validatingException = null;
            boolean validatingEmpty = false;
            for (AbstractParkingLot parkingLot : parkingLots) {
                try {
                    generateEvent(parkingLot);
                    if(parkingLot instanceof ValidationParkingLot) {
                        validatingEmpty = parkingLot.isEmpty();
                    } else {
                        isEmpty = isEmpty || parkingLot.isEmpty();
                    }
                } catch (ParkingException pe) {
                    if(parkingLot instanceof ValidationParkingLot) {
                        validatingException = pe;
                    } else {
                        validatingException = null;
                    }
                    System.out.println("error handled " + pe.getMessage());
                    pe.printStackTrace();
                } catch (Exception e) {
                    if(!(parkingLot instanceof  ValidationParkingLot)) {
                        throw new RuntimeException(e);
                    }
                }
                if(validatingEmpty != isEmpty) {
                    isEmpty = validatingEmpty;
                    System.out.println("warning, error parking state handled");
                }
                if(validatingException != null) {
                    validatingException = null;
                    System.out.println("warning, parking state has exception");
                }
                Thread.sleep(100);
            }
            totalCars--;
        }
        System.out.println("parking closed");
    }

    public static void generateEvent(AbstractParkingLot parkingLot) throws ParkingException {
        String nextEvent =  EVENTS[(int)(Math.random() * EVENTS.length)];
        if(nextEvent.equals("ENTER")) {
            String carType = CAR_TYPES[(int)(Math.random() * CAR_TYPES.length)];
            StringBuilder number = new StringBuilder();
            number.append(NUMA[(int)(Math.random() * NUMA.length)]);
            number.append((int) Math.round(Math.random() * 9));
            number.append((int) Math.round(Math.random() * 9));
            number.append((int) Math.round(Math.random() * 9));
            number.append(NUMA[(int)(Math.random() * NUMA.length)]);
            number.append(NUMA[(int)(Math.random() * NUMA.length)]);
            parkingLot.enter(carType, new String(number));
        } else if(nextEvent.equals("LEAVE")) {
            List<String> candidates = new ArrayList(parkingLot.getNumbers());
            if (!candidates.isEmpty()) {
                String candidate = candidates.get((int) (Math.random() * candidates.size()));
                parkingLot.leave(candidate);
            }
        } else if(nextEvent.equals("PASS")) {
            //do nothing :)
        } else {
            throw new RuntimeException("unknown event " + nextEvent);
        }
    }

    private static class ValidationParkingLot extends AbstractParkingLot {

        private Character[] state;

        private HashMap<String, Character> numbers;

        public ValidationParkingLot(int totalSpots, int electricSpots, int premiumSpots) {
            super(totalSpots, electricSpots, premiumSpots);
            state = new Character[totalSpots];
            int normalSpots = totalSpots - electricSpots - premiumSpots;
            for(int n = 0; n < normalSpots; n++) {
                state[n] = 'n';
            }
            for (int e = normalSpots; e < normalSpots + electricSpots; e++) {
                state[e] = 'e';
            }
            for (int p = normalSpots + electricSpots; p < totalSpots; p++) {
                state[p] = 'p';
            }
            numbers = new HashMap<>();
            System.out.println("parking started " + Arrays.toString(state));
        }

        @Override
        public void enter(String carType, String number) throws ParkingException {
            if(numbers.containsKey(number)){
                throw new ParkingException("already parked");
            }
            char place = carType.charAt(0);
            numbers.put(number, place);
            int p;
            int res = -1;
            for(p = 0; p < state.length; p++){
                if(state[p] == Character.toLowerCase(place)){
                    state[p] = place;
                    res = p;
                    break;
                }
            }
            if (res == -1) {
                throw new ParkingException("no free parking spot");
            }
            System.out.println(number + " parked at " + place + p);
        }

        @Override
        public void leave(String number) throws ParkingException {
            if(!numbers.containsKey(number)){
                throw new ParkingException("not parked");
            }
            char place = numbers.get(number);
            int p;
            int res = -1;
            for(p = 0; p < state.length; p++){
                if(state[p] == place){
                    state[p] = Character.toLowerCase(place);
                    res = p;
                    break;
                }
            }
            if(res == -1) {
                throw new ParkingException("spot not found");
            }
            numbers.remove(number);
            System.out.println(number + " leaved from " + place + p);
        }

        @Override
        public Set<String> getNumbers() {
            return numbers.keySet();
        }

        @Override
        boolean isEmpty() {
            return numbers.isEmpty();
        }
    }
}
