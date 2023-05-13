package backend;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* class: Appointment Scheduler
 * Description: The purpose of this class is to create schedules, reserve slots for multiple days within a specified month. 
 * This class is the primary one that the front-end side will interface with. 
 */
public class AppointmentScheduler {
	private MonthlyCalendarApp monthlyCalendar;
	private Map<Integer, AvailabilityWindow> monthlyCalendarTimeSlots;
	private int year;
	private int month;
	
	/* class constructor: AppointmentScheduler
	 * Description: Takes in a year and month to create an object
	 * For now, we are limiting this class to a single month, rather than handling multiple months.
	 * This constructor assumes that the daily schedule starts from 9am to 5:30pm
	 */
	public AppointmentScheduler(int aYear, int aMonth) {
		LocalTime startTime = LocalTime.of(9, 0);
		LocalTime endTime   = LocalTime.of(17, 30);
		
		this.year = aYear;
		this.month = aMonth;
		
		this.monthlyCalendar = new MonthlyCalendarApp(aYear, aMonth);
		
		// getting the day month map
		Map<Integer, String> dayMonthMap = this.monthlyCalendar.getDayMonthMap();
		this.monthlyCalendarTimeSlots = new HashMap<Integer, AvailabilityWindow>();
		
		for (Integer i: dayMonthMap.keySet()) {
			this.monthlyCalendarTimeSlots.put(i, new AvailabilityWindow(startTime, endTime));
		}
	}
	
	/* class constructor: AppointmentScheduler
	 * Description: Takes in a year and month to create an object - along with a start time and end time for a single day
	 * For now, we are limiting this class to a single month, rather than handling multiple months.
	 * 
	 * This constructor can allow you to specify daily schedules that are not part of the standard working day of 9am-5:30 pm
	 */
	public AppointmentScheduler(int aYear, int aMonth, LocalTime startTime, LocalTime endTime) {
		this.monthlyCalendar = new MonthlyCalendarApp(aYear, aMonth);
		
		// getting the day month map
		Map<Integer, String> dayMonthMap = this.monthlyCalendar.getDayMonthMap();
		this.monthlyCalendarTimeSlots = new HashMap<Integer, AvailabilityWindow>();
		
		for (Integer i: dayMonthMap.keySet()) {
			this.monthlyCalendarTimeSlots.put(i, new AvailabilityWindow(startTime, endTime));
		}
	}
	
	/*  @Function: getDayOfWeek
	 *  @param: int - day
	 *  @return: String - day of week
	 *  @description: returns a string value of day of week of a particular day. For ex: 1 = SATURDAY
	 */
	public String getDayOfWeek(int day) {
		return this.monthlyCalendar.getDayOfWeek(day);
	}
	
	/*  @Function: getNumDays
	 *  @param: none
	 *  @return: int
	 *  @description: returns the total number of days within the class constructor's specified month
	 */
	public int getNumDays() {
		return this.monthlyCalendarTimeSlots.keySet().size();
	}
	
	/*  @Function: getYear
	 *  @param: none
	 *  @return: int
	 *  @description: returns the year value
	 */
	public int getYear() {
		return this.year;
	}
	
	/*  @Function: getMonth
	 *  @param: none
	 *  @return: int
	 *  @description: returns the month value
	 */
	public int getMonth() {
		return this.month;
	}
	
	/*  @Function: getTimeSlotsMap
	 *  @param: none
	 *  @return: Map<String, String>
	 *  @description: returns the a map of the time slots as String local time and text
	 */
	public LinkedHashMap<String, String> getTimeSlotsMap(int day) {
		return this.monthlyCalendarTimeSlots.get(day).getTimeSlotsMap();
	}
	
	/*  @Function: reserve - overloaded function
	 *  @param: int - day input, LocalTime - time requested, String, text input
	 *  @return: boolean
	 *  @description: returns a true or false if it is able to sucessfully reserve a slot that's requested
	 */
	public boolean reserve(int day, LocalTime timeRequest, String text) {
		return this.monthlyCalendarTimeSlots.get(day).reserveSlot(timeRequest, text);
	}
	
	/*  @Function: reserve - overloaded function
	 *  @param: int - day input, String - time requested, String, text input
	 *  @return: boolean
	 *  @description: returns a true or false if it is able to sucessfully reserve a slot that's requested
	 */
	public boolean reserve(int day, String timeStr, String text) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
		
		try {
			LocalTime timeRequest = LocalTime.parse(timeStr, format);
			return this.monthlyCalendarTimeSlots.get(day).reserveSlot(timeRequest, text);
		} catch (Exception e) {
			System.out.println("ERROR! Wrong time format was entered!");
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*  @Function: displayScheduleOnDay
	 *  @param: int - day input
	 *  @return: String
	 *  @description: returns the full schedule for a given day as a string value
	 */
	public String displayScheduleOnDay(int day) {
		return this.monthlyCalendarTimeSlots.get(day).displayTimeWindows();
	}
	
	/*  @Function: populateDataFromJSON
	 *  @param: JSONReadFile - contains a jsonFile object with file read in, each having a schedule populated
	 *  @return: none
	 *  @description: populates the current schedule with the JSON file's values that has been read in. 
	 */
	public void populateDataFromJSON(JSONCalendar jsonFile) {
		List<JSONDayMap> daysMap = jsonFile.getMonthData(this.year, this.month);
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
		
		for (JSONDayMap day: daysMap) {
			int dayNumber = Long.valueOf(day.getDay()).intValue();
			for (String time: day.getTimeSlots().keySet()) {
				String text = day.getTimeSlots().get(time);
				LocalTime timeFormatted = LocalTime.parse(time, format);
				reserve(dayNumber, timeFormatted, text);
				
			}
		}
	}
	
	/*  @Function: getCalendarLayoutMap
	 *  @param: none
	 *  @return: List<Map<String, Integer>> - It is a weekly list, each list has a string = day of week (SATURDAY), mapped to integer representing the day of the month = 1
	 *  @description: returns the full calendar layout as a list of maps. 
	 */
	public List<Map<String, Integer>> getCalendarLayoutMap() {
		return this.monthlyCalendar.getFullCalendarLayout();
	}
	
	public String displayCalendarLayout () {
		return this.monthlyCalendar.displayCalendarLayout();
	}
	
	/*  @Function: getAvailabilityStatus
	 *  @param: int - day input, String - time requested
	 *  @return: boolean
	 *  @description: gets the availability status (available or not - true/false) of a given day and time slot 
	 */
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
	
	/*  @Function: getAvailabilityText
	 *  @param: int - day input, String - time requested
	 *  @return: String
	 *  @description: gets the availability status' text of a given day and time slot 
	 *  Usually if it is unavailable, there is often a unique text associated with it
	 */
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
	
	/*  @Function: removeAppointment
	 *  @param: int - day input, String - time requested
	 *  @return: boolean
	 *  @description: cancels the appointment and returns a boolean indicating if it was successful or not. true means success. false means it failed
	 */
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
			//System.out.println("Appointment cannot be cancelled!");
			return false;
		}
		
		return false;
	}
	
	/*  @Function: getNumberOfAvailableSlots
	 *  @param: int - day input
	 *  @return: int
	 *  @description: returns the number of available slots within a given day
	 */
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
}
