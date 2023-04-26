package backEnd;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class AvailabilityWindow {
	
	private final LocalTime startTime;
	private final LocalTime endTime;
	private LinkedHashMap<LocalTime, Boolean> timeWindow;
	
	public AvailabilityWindow(LocalTime startTime, LocalTime endTime) {
		// initialize variables
		timeWindow = new LinkedHashMap<LocalTime, Boolean>();

		this.startTime = roundedTime(startTime);
		this.endTime = roundedTime(endTime);
		
		this.timeWindow = generateTimeWindows(this.startTime, this.endTime);
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
	
	public String displayTimeWindows() {
		StringBuilder sb = new StringBuilder();
		
		for (LocalTime b: this.timeWindow.keySet()) {
			sb.append(formatTimeDisplay(b) + "\t"+this.timeWindow.get(b) + "\n");
		}
		
		return sb.toString();
	}

	public boolean reserveSlot(LocalTime time) {
		LocalTime roundedTime = roundedTime(time);
		
		// Time is outside the availability window
		if (time.isBefore(startTime) || time.isAfter(endTime)) {
			System.out.println("Your requested time is unavailable!");
			return false; 
		} 
		// Time is within the availability window
		else {
			// check if the time slot exists
			if (this.timeWindow.get(roundedTime) == true) {
				System.out.println("Your requested "+roundedTime+" appointment has been scheduled!");
				timeWindow.put(roundedTime, false);
				return true;
			} else {
				System.out.println("Your requested time is unavailable!");
				return false;
			}
		} 
	}
	
	// TODO delete later
	public boolean requestOpenSlots() {
		return false;
	}
	
	// TODO delete later
	public int getRemainingSlots() {		
		return 0;
	}
	
	private String formatTimeDisplay(LocalTime time) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		
		return timeFormatter.format(time);
	}

	@Override
	public String toString() {
		return formatTimeDisplay(startTime) + " - " + formatTimeDisplay(endTime);
	}
	
	public static void main(String[] args) {
		AvailabilityWindow testAvailWindow = new AvailabilityWindow(LocalTime.of(9, 0), LocalTime.of(17, 30));
		
		/** ================================================
		 * 	SAMPLE TEST 1
		 *  Get Availability Windows Time Range
		 *  ================================================
		 */
		System.out.println("Availability Windows Time Range:\t"+testAvailWindow.toString());

		/** ================================================
		 * 	SAMPLE TEST 2
		 *  Display Availability Windows - Before Reserve
		 *  ================================================
		 */
		System.out.println("\nDisplay Availability Windows:\n"+testAvailWindow.displayTimeWindows());
		
		/** ================================================
		 * 	SAMPLE TEST 3
		 *  Reserving Time-Slots
		 *  ================================================
		 */
		System.out.println("Reserving Time-Slots");
		testAvailWindow.reserveSlot(LocalTime.of(9, 0));
		testAvailWindow.reserveSlot(LocalTime.of(9, 30));
		testAvailWindow.reserveSlot(LocalTime.of(13, 0));
		testAvailWindow.reserveSlot(LocalTime.of(15, 30));
		testAvailWindow.reserveSlot(LocalTime.of(16, 0));
		
		/** ================================================
		 * 	SAMPLE TEST 4
		 *  Display Availability Windows - After Reserve
		 *  ================================================
		 */
		System.out.println("\nDisplay Availability Windows:\n"+testAvailWindow.displayTimeWindows());
		
		
	}

}
