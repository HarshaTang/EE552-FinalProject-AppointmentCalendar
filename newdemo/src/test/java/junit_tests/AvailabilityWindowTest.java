package junit_tests;
import org.junit.jupiter.api.Test;	// used for testing

import backend.AvailabilityWindow;

import static org.junit.jupiter.api.Assertions.*;	// for assertions

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;

public class AvailabilityWindowTest {
    // Write some tests here
	
	private AvailabilityWindow testAvailWindow;
	
	@BeforeEach
	public void beforeEachTestMethods() { 
		testAvailWindow = new AvailabilityWindow(LocalTime.of(9, 0), LocalTime.of(17, 30));
	}
	
	@Test
	void canReserve() {
		assertEquals(true, testAvailWindow.reserveSlot(LocalTime.of(9, 0)));
		assertEquals(true, testAvailWindow.reserveSlot(LocalTime.of(9, 30)));
		assertEquals(true, testAvailWindow.reserveSlot(LocalTime.of(13, 0)));
		assertEquals(true, testAvailWindow.reserveSlot(LocalTime.of(15, 30)));
		assertEquals(true, testAvailWindow.reserveSlot(LocalTime.of(16, 0)));
	}
	
	@Test
	void canTimeSlotStatus() {
		// check status before reserve
		assertEquals(true, testAvailWindow.getTimeSlotStatus(LocalTime.of(9, 0)));
		
		// reserve slot
		testAvailWindow.reserveSlot(LocalTime.of(9, 0));
		
		// check status after reserve
		assertEquals(false, testAvailWindow.getTimeSlotStatus(LocalTime.of(9, 0)));
	}
	
	@Test
	void canGetTimeSlotTextFree() {
		// check text before reserve
		assertEquals("Free", testAvailWindow.getTimeSlotText(LocalTime.of(9, 0)));
	}
	
	@Test
	void canGetTimeSlotTextUnavailable() {
		// reserve slot
		testAvailWindow.reserveSlot(LocalTime.of(9, 0), null);
		
		// check text after reserve
		assertEquals("Unavailable", testAvailWindow.getTimeSlotText(LocalTime.of(9, 0)));
	}
	
	@Test
	void canGetTimeSlotTextWithInput() {
		String text = "JUnitTest";
		
		// reserve slot with text
		testAvailWindow.reserveSlot(LocalTime.of(10, 0), text);
		
		// check text after reserve with text
		assertEquals(text, testAvailWindow.getTimeSlotText(LocalTime.of(10, 0)));
	}
	
}
