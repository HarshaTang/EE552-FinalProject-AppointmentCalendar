/**
 * 
 */
package backEnd;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class AppointmentScheduler {
	private MonthlyCalendarApp monthlyCalendar;
	private AvailabilityWindow availablilityWindow;
	private Map<Integer, AvailabilityWindow> monthlyCalendarTimeSlots;
	
	private final Map<DayOfWeek, AvailabilityWindow> availabilityWindows;

	public AppointmentScheduler(Map<DayOfWeek, AvailabilityWindow> availabilityWindows) {
		this.availabilityWindows = availabilityWindows;
	}
	
	// New Scheduler Constructor
	public AppointmentScheduler(int aYear, int aMonth, LocalTime startTime, LocalTime endTime) {
		this.availabilityWindows = null;
		this.monthlyCalendar = new MonthlyCalendarApp(aYear, aMonth);
		this.availablilityWindow = new AvailabilityWindow(startTime, endTime);
		
		// getting the day month map
		Map<Integer, String> dayMonthMap = this.monthlyCalendar.getDayMonthMap();
		this.monthlyCalendarTimeSlots = new HashMap<Integer, AvailabilityWindow>();
		
		for (Integer i: dayMonthMap.keySet()) {
			this.monthlyCalendarTimeSlots.put(i, new AvailabilityWindow(startTime, endTime));
		}
	}
	
	public void reserve(int day, int hour, int minute) {
		LocalTime timeRequest = LocalTime.of(hour, minute);
		this.monthlyCalendarTimeSlots.get(day).reserveSlot(timeRequest);
	}
	
	public String displayScheduleOnDay(int day) {
		return this.monthlyCalendarTimeSlots.get(day).displayTimeWindows();
	}
	
	// Function to reserve the slot
	public boolean reserveSlot(DayOfWeek dayOfWeek, LocalTime time) {
		AvailabilityWindow availabilityWindow = availabilityWindows.get(dayOfWeek);
		if (availabilityWindow == null) {
			return false; // Availability window not found for the given day
		}
		return availabilityWindow.reserveSlot(time);
	}

	// Do we need this function?
	public String getAvailabilityHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table>\n");
		sb.append("<tr><th>Day</th><th>Availability</th><th>Remaining Slots</th></tr>\n");
		for (Map.Entry<DayOfWeek, AvailabilityWindow> entry : availabilityWindows.entrySet()) {
			DayOfWeek dayOfWeek = entry.getKey();
			AvailabilityWindow availabilityWindow = entry.getValue();
			sb.append("<tr><td>").append(dayOfWeek).append("</td><td>").append(availabilityWindow).append("</td><td>")
					.append(availabilityWindow.getRemainingSlots()).append("</td></tr>\n");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		// need to initialize with a year and month, and a start/end time
		LocalTime startTime = LocalTime.of(9, 0);
		LocalTime endTime   = LocalTime.of(17, 30);
		AppointmentScheduler scheduler = new AppointmentScheduler(2023, 4, startTime, endTime);
		
		/** ================================================
		 * 	SAMPLE TEST 1
		 *  Reserve TimeSlots on April 1st, and Display Calendar
		 *  ================================================
		 */
		System.out.println("Reserve TimeSlots on April 1st, and Display Calendar:");
		scheduler.reserve(1, 9, 45);
		scheduler.reserve(1, 13, 11);
		scheduler.reserve(1, 12, 33);
		scheduler.reserve(1, 16, 23);
		scheduler.reserve(1, 14, 30);
		System.out.println(scheduler.displayScheduleOnDay(1));
		
		/** ================================================
		 * 	SAMPLE TEST 2
		 *  Showcase TimeSlots on April 2nd
		 *  ================================================
		 */
		System.out.println("Showcase TimeSlots on April 2nd (Should be Empty):");
		System.out.println(scheduler.displayScheduleOnDay(2));
		
	}
}
