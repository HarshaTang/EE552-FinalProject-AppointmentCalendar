package junit_tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;	// used for testing

import backend.AppointmentScheduler;

import static org.junit.jupiter.api.Assertions.*;	// for assertions

import java.time.LocalTime;

public class AppointmentSchedulerTest {
    // Write some tests here
	private AppointmentScheduler scheduler;
	
	@BeforeEach
	public void beforeEachTestMethods() { 
		// need to initialize with a year and month, and a start/end time
		LocalTime startTime = LocalTime.of(9, 0);
		LocalTime endTime   = LocalTime.of(17, 30);
		scheduler = new AppointmentScheduler(2023, 4, startTime, endTime);
	}
	
	@Test
	public void canReserveSlots() {
		// Can reserve successfully
		boolean canReserve = scheduler.reserve(1, "9:45 AM", null);
		assertEquals(true, canReserve);
		
		// the availability is taken, so it's false
		boolean statusActual = scheduler.getAvailabilityStatus(1, "9:45 AM");
		assertEquals(false, statusActual);
		
		// reserve again in the same window
		boolean canReserve2 = scheduler.reserve(1, "9:45 AM", null);
		assertEquals(false, canReserve2);
	}
	
	@Test
	public void canGetNumberOfAvailableSlots() {
		// get the number of slots
		assertEquals(17, scheduler.getNumberOfAvailableSlots(1));
		
		// reserve a slot and get availability slots
		scheduler.reserve(1, "9:45 AM", null);
		assertEquals(16, scheduler.getNumberOfAvailableSlots(1));
		
		// reserve more
		scheduler.reserve(1, "1:11 PM", null);
		scheduler.reserve(1, "12:33 PM", null);
		scheduler.reserve(1, "4:30 PM", null);
		scheduler.reserve(1, "2:30 PM", null);
		scheduler.reserve(1, "5:30 PM", null);
		
		assertEquals(12, scheduler.getNumberOfAvailableSlots(1));
	}
	
	@Test
	public void canCancelAppointments() {
		// reserve a slot
		scheduler.reserve(1, "10:00 AM", null);
		
		// check available slots
		assertEquals(16, scheduler.getNumberOfAvailableSlots(1));
		
		// cancel it
		assertEquals(true, scheduler.removeAppointment(1, "10:00 AM"));
		
		// check available slots again
		assertEquals(17, scheduler.getNumberOfAvailableSlots(1));

		// cancel it again
		assertEquals(false, scheduler.removeAppointment(1, "10:00 AM"));
	}
	
}
