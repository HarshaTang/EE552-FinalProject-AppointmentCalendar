package backEnd;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class AvailabilityWindow {
	
	private final LocalTime startTime;
	private final LocalTime endTime;
	private final int maxSlots;
	private int reservedSlots;
	
	// ArrayList -> contains all slots. 
	// If a slot has been reserved, that index becomes false -> meaning you can't reserve it
	// however, if the slot is open, the index is true. -> 
	LinkedHashMap<LocalTime, Boolean> timeWindow;
	
	public AvailabilityWindow(LocalTime startTime, LocalTime endTime, int maxSlots) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.maxSlots = maxSlots;
		this.reservedSlots = 0;
	}

	public boolean reserveSlot(LocalTime time) {
		if (time.isBefore(startTime) || time.isAfter(endTime)) {
			return false; // Time is outside the availability window
		} 
		else if (reservedSlots < maxSlots) {
			reservedSlots++;
			return true; // No more slots available
		} 
		else {
			return false;
		}
	}
	
	public boolean requestOpenSlots() {
		if (reservedSlots < maxSlots) {
			return true; 
		} else {
			return false;
		}
	}

	public int getRemainingSlots() {		
		return maxSlots - reservedSlots;
	}

	@Override
	public String toString() {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		
		String formattedStartTime 	= timeFormatter.format(startTime);
		String formattedEndtime 	= timeFormatter.format(endTime);
		
		return formattedStartTime + " - " + formattedEndtime;
	}
	
	public LocalTime getTime(int number) {
		LocalTime time = null;
		try {
			time = LocalTime.of(number, 0);
		} catch (DateTimeException e) {
			System.out.println("ERROR! Enter Valid Date: "+e);
			e.printStackTrace();
		}
		return time;
	}
	
	public static void main(String[] args) {
		AvailabilityWindow testAvailWindow = new AvailabilityWindow(LocalTime.of(9, 0), LocalTime.of(17, 0), 5);
		
		/** ================================================
		 * 	SAMPLE TEST 1
		 *  Get Availability Windows Time Range
		 *  ================================================
		 */
		System.out.println("Availability Windows Time Range:\t"+testAvailWindow.toString());
		
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		System.out.println(testAvailWindow.reserveSlot(testAvailWindow.getTime(9)));
		
		
	
		
	}

}
