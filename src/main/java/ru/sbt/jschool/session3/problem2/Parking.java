package ru.sbt.jschool.session3.problem2;

import java.time.LocalTime;
import java.util.*;

public class Parking {
    private static final int NIGHT_FACTOR = 2;
    private static final int BEGIN_NIGHT_HOURS = 23;
    private static final int END_NIGHT_HOURS = 6;
    private static final int DAY = 24;
    private int capacity;
    private Map<Long, ParkingTime> cars;
    private float costPerHour;

    public Parking(int capacity, float costPerHour) {
        this.capacity = capacity;
        this.costPerHour = costPerHour;
        cars = new HashMap<>(capacity, 1);
    }

    public boolean arrival(long carId, long time) {
        if (cars.containsKey(carId) || cars.size() >= capacity) {
            return false;
        } else {
            cars.put(carId, new ParkingTime(time));
            return true;
        }
    }

    public float departure(long carId, long time) throws Exception {
        ParkingTime car;
        if (cars.containsKey(carId)) {
            if (time < (car = cars.get(carId)).arrival) {
                throw new Exception("Время выезда раньше времени въезда!");
            } else {
                car.departure = time;
                return getPrice(car);
            }
        } else {
            throw new Exception("Автомобиль отсутствует на парковке!");
        }

    }

    private float getPrice(ParkingTime parkingTime) {
        Set<Long> nightHours = getNightHoursSet();
        int numberOfNightHours = getNumberOfNightHours(nightHours, parkingTime.arrival, parkingTime.departure);
        return ((parkingTime.departure - parkingTime.arrival) +
                numberOfNightHours * (NIGHT_FACTOR - 1)) * costPerHour;
    }

    private int getNumberOfNightHours(Set<Long> nightHours, long arrival, long departure) {
        int numberOfNightHours = 0;
        long i;
        for (i = arrival; i <= departure; i++) {
            if (nightHours.contains(i % DAY)) {
                numberOfNightHours++;
            } else if (i % DAY == (END_NIGHT_HOURS + 1)) {
                numberOfNightHours--;
            }
        }
        if (nightHours.contains(i % DAY) || (i % DAY == (END_NIGHT_HOURS + 1))) {
            numberOfNightHours--;
        }
        return numberOfNightHours;
    }

    private Set<Long> getNightHoursSet() {
        LocalTime begin = LocalTime.of(BEGIN_NIGHT_HOURS, 0);
        LocalTime end = LocalTime.of(END_NIGHT_HOURS, 0);
        HashSet<Long> nightHours = new HashSet<>();
        while (!begin.equals(end)) {
            nightHours.add((long) begin.getHour());
            begin = begin.plusHours(1);
        }
        nightHours.add((long) begin.getHour());
        return nightHours;
    }

    public static void main(String[] args) throws Exception {
        Parking parking = new Parking(30, 40);
        parking.arrival(1, 23);
        System.out.println(parking.departure(1, 30));
    }

    class ParkingTime {
        private Long arrival;
        private Long departure;

        public ParkingTime(long arrival) {
            this.arrival = arrival;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ParkingTime)) return false;
            ParkingTime that = (ParkingTime) o;
            return Objects.equals(arrival, that.arrival) &&
                    Objects.equals(departure, that.departure);
        }

        @Override
        public int hashCode() {
            return Objects.hash(arrival, departure);
        }
    }
}
