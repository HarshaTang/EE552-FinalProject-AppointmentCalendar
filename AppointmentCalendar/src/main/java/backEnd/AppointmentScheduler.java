/**
 * 
 */
package backEnd;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentScheduler {
	private MonthlyCalendarApp monthlyCalendar;
	private Map<Integer, AvailabilityWindow> monthlyCalendarTimeSlots;
	
	public AppointmentScheduler(int aYear, int aMonth) {
		LocalTime startTime = LocalTime.of(9, 0);
		LocalTime endTime   = LocalTime.of(17, 30);
		
		this.monthlyCalendar = new MonthlyCalendarApp(aYear, aMonth);
		
		// getting the day month map
		Map<Integer, String> dayMonthMap = this.monthlyCalendar.getDayMonthMap();
		this.monthlyCalendarTimeSlots = new HashMap<Integer, AvailabilityWindow>();
		
		for (Integer i: dayMonthMap.keySet()) {
			this.monthlyCalendarTimeSlots.put(i, new AvailabilityWindow(startTime, endTime));
		}
	}
	
	public AppointmentScheduler(int aYear, int aMonth, LocalTime startTime, LocalTime endTime) {
		this.monthlyCalendar = new MonthlyCalendarApp(aYear, aMonth);
		
		// getting the day month map
		Map<Integer, String> dayMonthMap = this.monthlyCalendar.getDayMonthMap();
		this.monthlyCalendarTimeSlots = new HashMap<Integer, AvailabilityWindow>();
		
		for (Integer i: dayMonthMap.keySet()) {
			this.monthlyCalendarTimeSlots.put(i, new AvailabilityWindow(startTime, endTime));
		}
	}
	
	public void reserve(int day, LocalTime timeRequest, boolean logSwitch) {
		this.monthlyCalendarTimeSlots.get(day).reserveSlot(timeRequest, logSwitch);
	}
	
	public void reserve(int day, int hour, int minute, boolean logSwitch) {
		LocalTime timeRequest = LocalTime.of(hour, minute);
		this.monthlyCalendarTimeSlots.get(day).reserveSlot(timeRequest, logSwitch);
	}
	
	public void reserve(int day, String timeStr, boolean logSwitch) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
		
		try {
			LocalTime timeRequest = LocalTime.parse(timeStr, format);
			this.monthlyCalendarTimeSlots.get(day).reserveSlot(timeRequest, logSwitch);
		} catch (Exception e) {
			System.out.println("ERROR! Wrong time format was entered!");
			e.printStackTrace();
		}
	}
	
	public String displayScheduleOnDay(int day) {
		return this.monthlyCalendarTimeSlots.get(day).displayTimeWindows();
	}
	
	public void populateDataFromJSON(List<JSONDayMap> daysMap, boolean logSwitch) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
		
		for (JSONDayMap day: daysMap) {
			int dayNumber = Long.valueOf(day.getDay()).intValue();
			for (String time: day.getTimeSlots().keySet()) {
				boolean timeValue = day.getTimeSlots().get(time);
				
				if (timeValue == false) {
					LocalTime timeFormatted = LocalTime.parse(time, format);
					reserve(dayNumber, timeFormatted, logSwitch);
				}
			}
		}
	}
	
	/*
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
	*/
	
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
		scheduler.reserve(1, "9:45 AM", true);
		scheduler.reserve(1, "1:11 PM", true);
		scheduler.reserve(1, "12:33 PM", true);
		scheduler.reserve(1, "4:30 PM", true);
		scheduler.reserve(1, "2:30 PM", true);
		scheduler.reserve(1, "5:30 PM", true);
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
