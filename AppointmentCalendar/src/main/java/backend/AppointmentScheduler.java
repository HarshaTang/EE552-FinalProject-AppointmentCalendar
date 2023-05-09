/**
 * 
 */
package backend;

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
	
	public int getNumDays() {
		return this.monthlyCalendarTimeSlots.keySet().size();
	}
	
	public boolean reserve(int day, LocalTime timeRequest, String text, boolean logSwitch) {
		return this.monthlyCalendarTimeSlots.get(day).reserveSlot(timeRequest, text, logSwitch);
	}
	
	public boolean reserve(int day, String timeStr, String text, boolean logSwitch) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
		
		try {
			LocalTime timeRequest = LocalTime.parse(timeStr, format);
			return this.monthlyCalendarTimeSlots.get(day).reserveSlot(timeRequest, text, logSwitch);
		} catch (Exception e) {
			System.out.println("ERROR! Wrong time format was entered!");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String displayScheduleOnDay(int day) {
		return this.monthlyCalendarTimeSlots.get(day).displayTimeWindows();
	}
	
	public void populateDataFromJSON(List<JSONDayMap> daysMap, boolean logSwitch) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
		
		for (JSONDayMap day: daysMap) {
			int dayNumber = Long.valueOf(day.getDay()).intValue();
			for (String time: day.getTimeSlots().keySet()) {
				String text = day.getTimeSlots().get(time);
				LocalTime timeFormatted = LocalTime.parse(time, format);
				reserve(dayNumber, timeFormatted, text, logSwitch);
				
			}
		}
	}
	
	public List<Map<String, Integer>> getCalendarLayoutMap() {
		return this.monthlyCalendar.getFullCalendarLayout();
	}
	
	public boolean getAvailabilityStatus (int day, String timeStr) {
		if (this.monthlyCalendarTimeSlots.containsKey(day)) {
			AvailabilityWindow timeWindow = this.monthlyCalendarTimeSlots.get(day);
			DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
			
			try {
				LocalTime timeRequest = LocalTime.parse(timeStr, format);
				return timeWindow.getTimeSlotStatus(timeRequest);
			} catch (Exception e) {
				System.out.println("ERROR! Wrong time format was entered!");
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public String getAvailabilityText (int day, String timeStr) {
		if (this.monthlyCalendarTimeSlots.containsKey(day)) {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
			AvailabilityWindow timeWindow = this.monthlyCalendarTimeSlots.get(day);
			
			try {
				LocalTime timeRequest = LocalTime.parse(timeStr, format);
				return timeWindow.getTimeSlotText(timeRequest);
			} catch (Exception e) {
				System.out.println("ERROR! Wrong time format was entered!");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public boolean removeAppointment (int day, String timeStr) {
		if (getAvailabilityStatus(day, timeStr) == false) {
			AvailabilityWindow timeWindow = this.monthlyCalendarTimeSlots.get(day);
			DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
			
			try {
				LocalTime timeRequest = LocalTime.parse(timeStr, format);
				return timeWindow.cancel(timeRequest);
			} catch (Exception e) {
				System.out.println("ERROR! Wrong time format was entered!");
				e.printStackTrace();
			}
		} else {
			System.out.println("Appointment cannot be cancelled!");
		}
		
		return false;
	}
	
	public int getNumberOfAvailableSlots (int day) {
		try {
			return this.monthlyCalendarTimeSlots.get(day).getNumberOfAvailableSlots();
			
		} catch (Exception e) {
			System.out.println("ERROR! Day doesn't exist!");
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	/*
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
		scheduler.reserve(1, "9:45 AM", null, true);
		scheduler.reserve(1, "1:11 PM", null, true);
		scheduler.reserve(1, "12:33 PM", null, true);
		scheduler.reserve(1, "4:30 PM", null, true);
		scheduler.reserve(1, "2:30 PM", null, true);
		scheduler.reserve(1, "5:30 PM", null, true);
		System.out.println(scheduler.displayScheduleOnDay(1));
		
		/** ================================================
		 * 	SAMPLE TEST 2
		 *  Showcase TimeSlots on April 2nd
		 *  ================================================
		 */
		System.out.println("Showcase TimeSlots on April 2nd (Should be Empty):");
		System.out.println(scheduler.displayScheduleOnDay(2));
		
		/** ================================================
		 * 	SAMPLE TEST 3
		 *  Requesting Status of April 1st - 10AM
		 *  ================================================
		 */
		System.out.println("Request Availability Status of April 1st - 10AM: " + scheduler.getAvailabilityStatus(1, "10:00 AM"));
		
		/** ================================================
		 * 	SAMPLE TEST 4
		 *  Requesting Text of April 1st - 10AM
		 *  ================================================
		 */
		System.out.println("Requesting Text of April 1st - 10AM: " + scheduler.getAvailabilityText(1, "10:00 AM"));
		
		/** ================================================
		 * 	SAMPLE TEST 5
		 *  Cancel April 1st - 10AM Appointment
		 *  ================================================
		 */
		System.out.println("\nCancel April 1st - 10AM Appointment: ");
		System.out.println("Number of Available Slots on April 1st [BEFORE]: "+scheduler.getNumberOfAvailableSlots(1));
		System.out.println("Removed Appointment: "+scheduler.removeAppointment(1, "10:00 AM"));
		System.out.println("Number of Available Slots on April 1st [AFTER] : "+scheduler.getNumberOfAvailableSlots(1));

	}
}
