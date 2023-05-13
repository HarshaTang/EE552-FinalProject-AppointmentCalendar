package backend;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ConsoleDisplayCalendarView {
	
	private int timeSpacing = 10;
	private int wordsSpacing = 27;
	private int headerBarsNumber = 1*timeSpacing + 7*wordsSpacing + 9;
	private int stringCutOff = wordsSpacing - 3;
	
	private AppointmentScheduler scheduler;
	
	public ConsoleDisplayCalendarView (AppointmentScheduler aScheduler) {
		this.scheduler = aScheduler;
	}
	
	public void generateWeeklyView(int startDay) {
		int newStart = 0;
		int newEnd = 0;

		int maxDays = scheduler.getNumDays();
		
		if (startDay <= maxDays) {
			// Fix Days Start and End
			if ( (startDay + 7) > maxDays) {
				newStart = startDay;
				newEnd = maxDays;
				int diff = newEnd - newStart;
				headerBarsNumber = 1*timeSpacing + (diff+1)*wordsSpacing + (newEnd-newStart)+3;
				
			} else {
				newStart = startDay;
				newEnd = startDay + 6;
			}
			
			String test = genView(newStart, newEnd);
			System.out.println(test);
		} else {
			System.out.println("Cannot display invalid date! The last day your requested month is "+maxDays+"!");
		}
		
	}
	
	private String genView (int startDay, int endDay) {
		StringBuilder sb = new StringBuilder();
		
		// STEP 1: Add Bars
		sb.append("=".repeat(headerBarsNumber)).append("\n");
		
		// STEP 2: Write the headers
		sb.append("|").append(StringUtils.center("", timeSpacing));
		for (int curr = startDay; curr <= endDay; curr++) {
			String dayOfWeek = this.scheduler.getDayOfWeek(curr);
			sb.append("|").append(StringUtils.center(dayOfWeek, wordsSpacing));
			//sb.append(String.format("|%-10s|", dayOfWeek));
		}
		sb.append("|").append("\n");
		
		// STEP 3: Write the Day Numbers
		sb.append("|").append(StringUtils.center("", timeSpacing));
		for (int curr = startDay; curr <= endDay; curr++) {
			sb.append("|").append(StringUtils.center(String.valueOf(curr), wordsSpacing));
		}
		sb.append("|").append("\n");
		
		// STEP 4: Write the # of Available Slots
		sb.append("|").append(StringUtils.center("", timeSpacing));
		for (int curr = startDay; curr <= endDay; curr++) {
			int numSlots = scheduler.getNumberOfAvailableSlots(curr);
			String numSlotsText = "Total # of Free Slots: " + numSlots;
			sb.append("|").append(StringUtils.center(String.valueOf(numSlotsText), wordsSpacing));
		}
		sb.append("|").append("\n");
		
		// STEP 5: Add Bars
		sb.append("=".repeat(headerBarsNumber)).append("\n");
		
		// STEP 6: Write the individual days
		List<String> timeSlotsKeys = new ArrayList<String>(scheduler.getTimeSlotsMap(endDay).keySet());
		//System.out.println("timeSlotsKeys: "+ timeSlotsKeys);
		for (String time: timeSlotsKeys) {
			sb.append("|").append(StringUtils.center(time, timeSpacing));
			
			for (int currDay = startDay; currDay <= endDay; currDay++) {
				String slotText = scheduler.getAvailabilityText(currDay, time);
				if (slotText.length() >= wordsSpacing) {
					slotText = slotText.substring(0, stringCutOff) + "...";
				}
				sb.append("|").append(StringUtils.center(slotText, wordsSpacing));
			}
			sb.append("|").append("\n").append("-".repeat(headerBarsNumber)).append("\n");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		// This is a s
		JSONCalendar jsonData = new JSONCalendar();
		jsonData.read("savedSession.json");
		
		int year = 2023;
		int month = 4;
		AppointmentScheduler scheduler = new AppointmentScheduler(year, month);
		scheduler.populateDataFromJSON(jsonData);
		
		ConsoleDisplayCalendarView viewer = new ConsoleDisplayCalendarView(scheduler);
		
		viewer.generateWeeklyView(1);
	}
	
}
