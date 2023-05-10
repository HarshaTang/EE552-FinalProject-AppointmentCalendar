package junit_tests;
import org.junit.jupiter.api.Test;	// used for testing

import backend.AppointmentScheduler;
import backend.JSONCalendar;

import static org.junit.jupiter.api.Assertions.*;	// for assertions

import org.junit.jupiter.api.BeforeEach;

public class JSONCalendarTest {
    // Write some tests here
	
	private JSONCalendar jsonData;
	private AppointmentScheduler scheduler;
	
	@BeforeEach
	public void beforeEachTestMethods() {
		/** ================================================
		 * 	Set-Up the JSON Reader
		 *  ================================================
		 */
		jsonData = new JSONCalendar();
		jsonData.read("AppointmentsTemplate_WithText.json");
		//jsonData.printData();
		
		/** ================================================
		 * 	Set-Up the Scheduler + Read from JSON File
		 *  ================================================
		 */
		int year = 2023;
		int month = 4;
		scheduler = new AppointmentScheduler(year, month);
		scheduler.populateDataFromJSON(jsonData);
	}
	
	@Test
	void canGetTimeSlotTextWithInput() {
		String text = "Meeting with Professor Marcus Aurelius";
		
		// check text after reserve with text
		assertEquals(text, scheduler.getAvailabilityText(1, "9:00 AM"));
	}
	
	@Test
	void canTimeSlotStatus() {
		// check status after reserve
		assertEquals(false, scheduler.getAvailabilityStatus(1, "9:00 AM"));
	}
	
}
