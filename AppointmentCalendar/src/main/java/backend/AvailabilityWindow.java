package backend;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class AvailabilityWindow {
	
	private final LocalTime startTime;
	private final LocalTime endTime;
	private LinkedHashMap<LocalTime, Boolean> timeWindow;
	private LinkedHashMap<LocalTime, String> timeWindowText;
	
	private static final String DEFAULT_TIME_SLOT_TEXT_AVAIL = "Free";
	private static final String DEFAULT_TIME_SLOT_TEXT_UNAVAIL = "Unavailable";
	
	public AvailabilityWindow(LocalTime startTime, LocalTime endTime) {
		// initialize variables
		this.timeWindow = new LinkedHashMap<LocalTime, Boolean>();
		this.timeWindowText = new LinkedHashMap<LocalTime, String>();

		this.startTime = roundedTime(startTime);
		this.endTime = roundedTime(endTime);
		
		this.timeWindow = generateTimeWindows(this.startTime, this.endTime);
		this.timeWindowText = generateTimeWindowsText();
	}
	
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
		
		//System.out.println("[Original, After]:\t["+tempTime+", "+newTime+"]");
		
		return newTime;
	}
	
	private LinkedHashMap<LocalTime, Boolean> generateTimeWindows(LocalTime startTime, LocalTime endTime) {
		LinkedHashMap<LocalTime, Boolean> timeWindowSlots = new LinkedHashMap<LocalTime, Boolean>();
		
		Duration durationBetweenTimes = Duration.between(startTime, endTime);
		double hoursDuration = durationBetweenTimes.toMinutes()/60.0;
		int maxTimeSlots = (int) (hoursDuration * 2);
		//System.out.println("Hours In-Between: "+hoursDuration+"\tMaxTimeSlots: "+maxTimeSlots);
		
		LocalTime tempTime = startTime;
		for (int i = 0; i < maxTimeSlots; i++) {
			if (i != 0) {
				tempTime = tempTime.plusMinutes(30);
			}
			timeWindowSlots.put(tempTime, true);
		}
		
		return timeWindowSlots;
	}
	
	private LinkedHashMap<LocalTime, String> generateTimeWindowsText() {
		LinkedHashMap<LocalTime, String> timeWindowText = new LinkedHashMap<LocalTime, String>();
		
		for (LocalTime t: this.timeWindow.keySet()) {
			timeWindowText.put(t, DEFAULT_TIME_SLOT_TEXT_AVAIL);
		}
		return timeWindowText;
	}
	
	public String displayTimeWindows() {
		StringBuilder sb = new StringBuilder();
		
		for (LocalTime b: this.timeWindow.keySet()) {
			sb.append(formatTimeDisplay(b) + "\t"+this.timeWindow.get(b) + "\t" + this.timeWindowText.get(b) + "\n");
		}
		
		return sb.toString();
	}
	
	public boolean reserveSlot(LocalTime time, String text, boolean logSwitch) {
		boolean result = reserve(time, text, logSwitch);
		return result;
	}
	
	public boolean reserveSlot(LocalTime time, boolean logSwitch) {
		boolean result = reserve(time, null, logSwitch);
		return result;
	}

	private boolean reserve(LocalTime time,String text, boolean logSwitch) {
		LocalTime roundedTime = roundedTime(time);
		String formattedTime = formatTimeDisplay(roundedTime);
		
		String timeSlotText = "";
		
		
		// Time is outside the availability window
		if (roundedTime.isBefore(startTime) || roundedTime.isAfter(endTime)) {
			if (logSwitch == true) {
				System.out.println("Your requested time of " + formattedTime + " is unavailable!");
			}
			return false; 
		} 
		// Time is within the availability window
		else {
			// check if the time slot exists
			if (this.timeWindow.containsKey(roundedTime) == true) {
				if (this.timeWindow.get(roundedTime) == true) {
					if (logSwitch == true) {
						System.out.println("Your requested " + formattedTime + " appointment has been scheduled!");
					}
					this.timeWindow.put(roundedTime, false);
					
					if (text != null) {
						timeSlotText = text;
					} else {
						timeSlotText = DEFAULT_TIME_SLOT_TEXT_UNAVAIL;
					}
					
					this.timeWindowText.put(roundedTime, timeSlotText);
					return true;
				} else {
					if (logSwitch == true) {
						System.out.println("Your requested time of " + formattedTime + " is already taken!");
					}
					return false;
				}
				
			} else {
				if (logSwitch == true) {
					System.out.println("Your requested time of " + formattedTime + " is unavailable!");
				}
				return false;
			}
		} 
	}
	
	public boolean getTimeSlotStatus(LocalTime timeRequest) {
		// check if the time is in the list
		if (this.timeWindow.containsKey(timeRequest)) {
			// if it exists, check the status
			if (this.timeWindow.get(timeRequest) == true) {
				return true;
			} 
		} 
		
		return false;
	}
	
	public String getTimeSlotText(LocalTime timeRequest) {
		// check if the time is in the list
		if (this.timeWindowText.containsKey(timeRequest)) {
			// if it exists, check the status
			return this.timeWindowText.get(timeRequest);
		} 
		
		return null;
	}
	
	public boolean cancel (LocalTime timeRequest) {
		if (this.timeWindow.containsKey(timeRequest)) {
			// if it exists, check the status
			if (this.timeWindow.get(timeRequest) == false) {
				this.timeWindow.put(timeRequest, true);
				this.timeWindowText.put(timeRequest, DEFAULT_TIME_SLOT_TEXT_AVAIL);
				return true;
			} 
		} 
		return false;
	}
	
	public int getNumberOfAvailableSlots() {
		int count = 0;
		for (LocalTime b: this.timeWindow.keySet()) {
			if (this.timeWindow.get(b) == true) {
				count++;
			}
		}
		return count;
	}
	
	private String formatTimeDisplay(LocalTime time) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		
		return timeFormatter.format(time);
	}

	@Override
	public String toString() {
		return formatTimeDisplay(startTime) + " - " + formatTimeDisplay(endTime);
	}

}
