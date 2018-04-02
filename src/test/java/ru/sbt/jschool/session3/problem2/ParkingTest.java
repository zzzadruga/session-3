package ru.sbt.jschool.session3.problem2;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParkingTest {
    @Test public void testParkingCapacity() throws Exception {
        Parking parking = new Parking(2, 0);
        parking.arrival(1, 0);
        parking.arrival(2, 0);
        assertFalse(parking.arrival(3, 0));
    }

    @Test public void testReArrivalWithoutDeparture() throws Exception {
        Parking parking = new Parking(1, 0);
        parking.arrival(1, 0);
        assertFalse(parking.arrival(1, 0));
    }

    @Test(expected = Exception.class)
    public void testDepartureWithoutArrival() throws Exception {
        Parking parking = new Parking(1, 0);
        parking.departure(1, 0);
    }

    @Test(expected = Exception.class)
    public void testDepartureBeforeArrival() throws Exception {
        Parking parking = new Parking(1, 0);
        parking.arrival(1, 1);
        parking.departure(1, 0);
    }

    @Test public void testCalculationParkingCost() throws Exception {
        Parking parking = new Parking(1, 1);
        parking.arrival(1, 5);
        assertEquals(2, parking.departure(1, 6), 0);
        parking.arrival(2, 6);
        assertEquals(1, parking.departure(2, 7), 0);
        parking.arrival(3, 5);
        assertEquals(3, parking.departure(3, 7), 0);
        parking.arrival(4, 0);
        assertEquals(16, parking.departure(4, 10), 0);
        parking.arrival(5, 22);
        assertEquals(1, parking.departure(5, 23), 0);
        parking.arrival(6, 23);
        assertEquals(2, parking.departure(6, 24), 0);
        parking.arrival(7, 22);
        assertEquals(3, parking.departure(7, 24), 0);
        parking.arrival(8, 23);
        assertEquals(14, parking.departure(8, 30), 0);
        parking.arrival(9, 22);
        assertEquals(16, parking.departure(9, 31), 0);
        parking.arrival(9, 22);
        assertEquals(16, parking.departure(9, 31), 0);
        parking.arrival(10, 0);
        assertEquals(0, parking.departure(10, 0), 0);
        parking.arrival(11, 0);
        assertEquals(75, parking.departure(11, 55), 0);
        parking.arrival(12, 3);
        assertEquals(64, parking.departure(12, 52), 0);
        parking = new Parking(1, 0);
        parking.arrival(1, 1);
        assertEquals(0, parking.departure(1, 59), 0);
        parking.arrival(1, 1);
        assertEquals(0, parking.departure(1, 1), 0);

    }
}
