package backEnd;

import java.time.LocalTime;

public class AvailabilityWindow {
	
	private final LocalTime startTime;
	private final LocalTime endTime;
	private final int maxSlots;
	private int reservedSlots;

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
		if (reservedSlots >= maxSlots) {
			return false; // No more slots available
		}
		reservedSlots++;
		return true;
	}

	public int getRemainingSlots() {
		return maxSlots - reservedSlots;
	}

	@Override
	public String toString() {
		return startTime + " - " + endTime;
	}
	
	public static void main(String[] args) {

	}

}
