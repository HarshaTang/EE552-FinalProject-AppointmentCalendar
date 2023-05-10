package backend;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthlyCalendarApp {
	
	private int year;
	private int month;
	private Map<Integer, String> dayMonthMap;
	private List<Map<String, Integer>> fullCalendarLayout;

	public MonthlyCalendarApp (int aYear, int aMonth) throws DateTimeException {
		LocalDate inputDate = null;
		try {
			inputDate = YearMonth.of(aYear, aMonth).atDay(1);
			//System.out.println(newDate);
			
			this.year = inputDate.getYear();
			this.month = inputDate.getMonthValue();
			//this.day = inputDate.getDayOfMonth();
			
			this.dayMonthMap = generateDayMonthMap();				// Run First
			this.fullCalendarLayout = generateFullCalendarLayout();	// Run Second
		} catch (DateTimeException e) {
			System.out.println("ERROR! Enter Valid Date: "+e);
			e.printStackTrace();
		}
		
	}
	
	/** ================================================
	 * 			PRIVATE AUTOMATED FUNCTIONS
	 *  ================================================
	 */
	public String getDayOfWeek(int day) { 
		DayOfWeek dayOfWeek = null;
		String numericalDayOfWeek = "INVALID";	// default is -1, meaning bad day input
		
		int maxLengthOfMonth = YearMonth.of(this.year, this.month).lengthOfMonth();
		//System.out.println("maxLengthOfMonth: "+maxLengthOfMonth);
		
		if (day > maxLengthOfMonth) {
			return numericalDayOfWeek;
		} else {
			LocalDate tempDate = LocalDate.of(this.year, this.month, day);
			dayOfWeek = tempDate.getDayOfWeek();
			numericalDayOfWeek = dayOfWeek.toString();
		}

		return numericalDayOfWeek;
	}
	
	private Map<Integer, String> generateDayMonthMap() {
		Map<Integer, String> dayMonthMap = new HashMap<Integer, String>();
		//Map<String, Integer> dayMonthMap = new HashMap<String, Integer>(); // if you want to swap to Map<String, Integer> format
		
		int maxLengthOfMonth = YearMonth.of(this.year, this.month).lengthOfMonth();
		
		for (int i = 1; i <= maxLengthOfMonth; i++) {
			dayMonthMap.put(i, getDayOfWeek(i));
			//dayMonthMap.put(getDayOfWeek(i), i); // if you want to swap to Map<String, Integer> format
		}
		
		return dayMonthMap;
	}
	
	private List<Map<String, Integer>> generateFullCalendarLayout() {
		List<Map<String, Integer>> fullCalendarMapList = new ArrayList<Map<String, Integer>>();
		// Each array index should have the following Map Keys: SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
		// Each Value for the Key should have a numerical representation. 0 if the Month doesn't have a date for that day. 
		
		Map<String, Integer> weekFormatMap = new HashMap<String, Integer> ();
		weekFormatMap.put(DayOfWeek.SUNDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.MONDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.TUESDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.WEDNESDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.THURSDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.FRIDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.SATURDAY.name(), 0);
		
		Map<String, Integer> tempWeekFormatMap = new HashMap<String, Integer>();
		tempWeekFormatMap.putAll(weekFormatMap);
		for (int i = 1; i <= this.dayMonthMap.size(); i++) {
			String dayOfWeek = this.dayMonthMap.get(i);
			tempWeekFormatMap.put(dayOfWeek, i);
			
			// Add Week to Calendar, and Reset Week
			if ( dayOfWeek.equals(DayOfWeek.SATURDAY.name()) || (i == this.dayMonthMap.size()) ) {
				fullCalendarMapList.add(tempWeekFormatMap);
				
				tempWeekFormatMap = new HashMap<String, Integer>();
				tempWeekFormatMap.putAll(weekFormatMap);
			}
		}
		
		return fullCalendarMapList;
	}
	
	/** ================================================
	 * 			PUBLIC GETTER FUNCTIONS
	 *  ================================================
	 */
	public int getYear() {
		return this.year;
	}
	
	public int getMonth() {
		return this.month;
	}
	
	/*
	public int getDay() {
		return this.day;
	}
	
	public String getYearMonthDayDate() {
		String date = this.year + "/" + this.month + "/" + this.day;
		
		return date;
	}
	*/
	
	public String getYearMonthDate() {
		String date = this.year + "/" + this.month;
		
		return date;
	}
	
	public Map<Integer, String> getDayMonthMap() {
		return this.dayMonthMap;
	}
	 
	public List<Map<String, Integer>> getFullCalendarLayout() {
		return this.fullCalendarLayout;
	}
	
	/** ================================================
	 * 			PUBLIC DISPLAY FUNCTIONS
	 *  ================================================
	 */
	public String displayCalendarLayout() {
		StringBuilder sb = new StringBuilder();
		
		// Set Up Header
		sb.append("SUN"); 	sb.append("\t");
		sb.append("MON"); 	sb.append("\t");
		sb.append("TUE"); 	sb.append("\t");
		sb.append("WED"); 	sb.append("\t");
		sb.append("THU"); 	sb.append("\t");
		sb.append("FRI"); 	sb.append("\t");
		sb.append("SAT"); 	sb.append("\t");
		sb.append("\n");
		
		List<Map<String, Integer>> fullCalendar = getFullCalendarLayout();
		for (Map<String, Integer> m: fullCalendar) {
			sb.append(m.get(DayOfWeek.SUNDAY.name())); 		sb.append("\t");
			sb.append(m.get(DayOfWeek.MONDAY.name())); 		sb.append("\t");
			sb.append(m.get(DayOfWeek.TUESDAY.name())); 	sb.append("\t");
			sb.append(m.get(DayOfWeek.WEDNESDAY.name())); 	sb.append("\t");
			sb.append(m.get(DayOfWeek.THURSDAY.name())); 	sb.append("\t");
			sb.append(m.get(DayOfWeek.FRIDAY.name())); 		sb.append("\t");
			sb.append(m.get(DayOfWeek.SATURDAY.name())); 	sb.append("\t");
			sb.append("\n");
		}
		
		return sb.toString();
	}

}
