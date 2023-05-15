package finalproject.ee552.backend;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/* class: Availability Window
 * Description: The purpose of this class is to create the availability windows (time slots) for a single day. 
 * This is to be used in the AppointmentScheduler class. If you combine list/map of multiple single days, you can create a whole month!
 */
public class AvailabilityWindow {
	
	private final LocalTime startTime;
	private final LocalTime endTime;
	private LinkedHashMap<LocalTime, Boolean> timeWindow;
	private LinkedHashMap<LocalTime, String> timeWindowText;
	
	private static final String DEFAULT_TIME_SLOT_TEXT_AVAIL = "Free";
	private static final String DEFAULT_TIME_SLOT_TEXT_UNAVAIL = "Unavailable";
	private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
	
	/* class constructor: AvailabilityWindow
	 * Description: This constructor can allow you to specify daily schedules with a start time and end time
	 */
	public AvailabilityWindow(LocalTime startTime, LocalTime endTime) {
		// initialize variables
		this.timeWindow = new LinkedHashMap<>();
		this.timeWindowText = new LinkedHashMap<>();

		this.startTime = roundedTime(startTime);
		this.endTime = roundedTime(endTime);
		
		this.timeWindow = generateTimeWindows(this.startTime, this.endTime);
		this.timeWindowText = generateTimeWindowsText();
	}
	
	/*  @Function: roundedTime
	 *  @param: LocalTime - time input that can be anything like "9:45 AM" or "9:23 AM"
	 *  @return: LocalTime
	 *  @description: returns a rounded time - floor or ceiling depending on the time. 
	 *  "9:45 AM" becomes "10:00 AM" 
	 *  "9:23 AM" becomes "9:30 AM"
	 *  "9:33 AM" becomes "9:30 AM"
	 */
	private LocalTime roundedTime (LocalTime tempTime) {
		LocalTime newTime = null;
		
		// round startTimes/endTimes to nearest 30 minute intervals
		int inMinutes = tempTime.getMinute();
		int timeNearestMin = (int) (Math.round(inMinutes/ 30.0)*30);
		
		if (timeNearestMin == 60) {
			newTime = tempTime.withHour(tempTime.getHour()+1).withMinute(0);
		} else {
			newTime = tempTime.withMinute(timeNearestMin);
		}
		
		return newTime;
	}
	
	/*  @Function: generateTimeWindows
	 *  @param: LocalTime -  start time, LocalTime - end time
	 *  @return: LinkedHashMap<LocalTime, Boolean>
	 *  @description: generates a linked hash map from the specified start time and end time. 
	 *  The time slots are broken down into 30 minute increments from start to end.
	 *  It creates a map in order (linked list) and the default values are set as true, meaning you can take an appointment with it
	 */
	private LinkedHashMap<LocalTime, Boolean> generateTimeWindows(LocalTime startTime, LocalTime endTime) {
		LinkedHashMap<LocalTime, Boolean> timeWindowSlots = new LinkedHashMap<>();
		
		Duration durationBetweenTimes = Duration.between(startTime, endTime);
		double hoursDuration = durationBetweenTimes.toMinutes()/60.0;
		int maxTimeSlots = (int) (hoursDuration * 2);
		
		LocalTime tempTime = startTime;
		for (int i = 0; i < maxTimeSlots; i++) {
			if (i != 0) {
				tempTime = tempTime.plusMinutes(30);
			}
			timeWindowSlots.put(tempTime, true);
		}
		
		return timeWindowSlots;
	}
	
	/*  @Function: generateTimeWindowsText
	 *  @param: none
	 *  @return: LinkedHashMap<LocalTime, String>
	 *  @description: Just like the previous function of generateTimeWindows(), this function populates the text associated with a timeslot. 
	 *  It populates a default value
	 */
	private LinkedHashMap<LocalTime, String> generateTimeWindowsText() {
		LinkedHashMap<LocalTime, String> tempTimeWindowText = new LinkedHashMap<>();
		
		for (LocalTime t: this.timeWindow.keySet()) {
			tempTimeWindowText.put(t, DEFAULT_TIME_SLOT_TEXT_AVAIL);
		}
		return tempTimeWindowText;
	}
	
	/*  @Function: displayTimeWindows
	 *  @param: none
	 *  @return: String
	 *  @description: Displays all the time windows for a single day in the following format:
	 *  TIME_SLOT	BOOLEAN_STATUS		TEXT_ASSOCIATED
	 */
	public String displayTimeWindows() {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<LocalTime, Boolean> entry: this.timeWindow.entrySet()) {
			sb.append(formatTimeDisplay(entry.getKey())).append("\t")
					.append(entry.getValue()).append("\t")
					.append(this.timeWindowText.get(entry.getKey()))
					.append("\n");
		}
		
		return sb.toString();
	}
	
	/*  @Function: reserveSlot - overloaded function
	 *  @param: LocalTime - time requested, String - text of appointment
	 *  @return: boolean
	 *  @description: calls the reserve function to attempt to reserve a slot
	 */
	public boolean reserveSlot(LocalTime time, String text) {
		return reserve(time, text);
	}
	
	/*  @Function: reserveSlot - overloaded function
	 *  @param: LocalTime - time requested
	 *  @return: boolean
	 *  @description: calls the reserve function to attempt to reserve a slot
	 */
	public boolean reserveSlot(LocalTime time) {
		return reserve(time, null);
	}
	
	/*  @Function: reserve
	 *  @param: LocalTime - time requested, String - text of appointment, boolean - logger on/off
	 *  @return: boolean
	 *  @description: attempts to reserve a slot based on the specified time and text conditions, and returns a true or false if it is successful.
	 *  true is successful, and false is unsuccessful.
	 */
	private boolean reserve(LocalTime time,String text) {
		LocalTime roundedTime = roundedTime(time);
		
		String timeSlotText = "";
		
		// return false if text is set as "Free"
		if (DEFAULT_TIME_SLOT_TEXT_AVAIL.equals(text)) {
			return false;
		}
		// Time is outside the availability window
		else if (roundedTime.isBefore(startTime) || roundedTime.isAfter(endTime)) {
			// Requested time is unavailable
			return false; 
		} 
		// Time is within the availability window
		else if (this.timeWindow.containsKey(roundedTime)) {
			if (Boolean.TRUE.equals(this.timeWindow.get(roundedTime))) { // if true
				// Requested time is scheduled
				
				this.timeWindow.put(roundedTime, false);
				
				if (text != null) {
					timeSlotText = text;
				} else {
					timeSlotText = DEFAULT_TIME_SLOT_TEXT_UNAVAIL;
				}
				
				this.timeWindowText.put(roundedTime, timeSlotText);
				return true;
			} else {
				// Requested time is taken already
				return false;
			}
			
		} else {
			// Requested time is unavailable
			return false;
		}
		
	}
	
	/*  @Function: getTimeSlotStatus
	 *  @param: LocalTime - time requested
	 *  @return: boolean
	 *  @description: gets the status of the specified time slot.
	 *  returns true if it is available
	 *  returns false if it is unavailable
	 */
	public boolean getTimeSlotStatus(LocalTime timeRequest) {
		// if it exists, get the time status, and return true or false
		return Boolean.TRUE.equals(this.timeWindow.get(timeRequest));
	}
	
	/*  @Function: getTimeSlotText
	 *  @param: LocalTime - time requested
	 *  @return: String
	 *  @description: returns the text associated with a specified time slot. returns that string value
	 */
	public String getTimeSlotText(LocalTime timeRequest) {
		// check if the time is in the list
		if (this.timeWindowText.containsKey(timeRequest)) {
			// if it exists, check the status
			return this.timeWindowText.get(timeRequest);
		} 
		
		return null;
	}

	/*  @Function: getTimeSlotsMap
	 *  @param: none
	 *  @return: Map<String, String>
	 *  @description: returns the timeWindowText as a map <String, String>
	 */
	public LinkedHashMap<String, String> getTimeSlotsMap() {
		LinkedHashMap<String, String> strStrMap = new LinkedHashMap<>();

		for (Map.Entry<LocalTime, String> entry: this.timeWindowText.entrySet()) {
			String formattedTime = formatTimeDisplay(entry.getKey());
			strStrMap.put(formattedTime, entry.getValue());
		}

		return strStrMap;
	}
	
	
	/*  @Function: cancel
	 *  @param: LocalTime - time requested
	 *  @return: boolean
	 *  @description: tries to cancel the requested time slot's appointment
	 *  returns true if the cancellation was successful
	 *  returns false if the cancellation was unsuccessful
	 */
	public boolean cancel (LocalTime timeRequest) {
		// if it exists, check the status
		if (Boolean.FALSE.equals(this.timeWindow.get(timeRequest))) {
			this.timeWindow.put(timeRequest, true);
			this.timeWindowText.put(timeRequest, DEFAULT_TIME_SLOT_TEXT_AVAIL);
			return true;
		}
		return false;
	}
	
	/*  @Function: getNumberOfAvailableSlots
	 *  @param: none
	 *  @return: int
	 *  @description: returns the total number of slots available within a single day
	 */
	public int getNumberOfAvailableSlots() {
		int count = 0;
		for (Map.Entry<LocalTime, Boolean> entry: this.timeWindow.entrySet()) {
			if (Boolean.TRUE.equals(entry.getValue())) {
				count++;
			}
		}
		return count;
	}
	
	/*  @Function: formatTimeDisplay
	 *  @param: LocalTime - time requested
	 *  @return: String
	 *  @description: returns a formatted string value of a LocalTime
	 */
	private String formatTimeDisplay(LocalTime time) {
		return timeFormatter.format(time);
	}
	
	/*  @Function: toString - override
	 *  @param: none
	 *  @return: String
	 *  @description: returns the time range of the availability windows for the day.
	 */
	@Override
	public String toString() {
		return formatTimeDisplay(startTime) + " - " + formatTimeDisplay(endTime);
	}

}
