/**
 * 
 */
package backEnd;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;

public class AppointmentScheduler {
	private final Map<DayOfWeek, AvailabilityWindow> availabilityWindows;

	public AppointmentScheduler(Map<DayOfWeek, AvailabilityWindow> availabilityWindows) {
		this.availabilityWindows = availabilityWindows;
	}

	public boolean reserveSlot(DayOfWeek dayOfWeek, LocalTime time) {
		AvailabilityWindow availabilityWindow = availabilityWindows.get(dayOfWeek);
		if (availabilityWindow == null) {
			return false; // Availability window not found for the given day
		}
		return availabilityWindow.reserveSlot(time);
	}

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

	}
}
