package backEnd;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalendarApp {
	
	private int year;
	private int month;
	private int day;
	
	public CalendarApp () {
		// Using local Date to get the current year, month, and day values
		LocalDate currentDate = LocalDate.now();
		
		this.year = currentDate.getYear();
		this.month = currentDate.getMonthValue();
		this.day = currentDate.getDayOfMonth();
	}
	
	// TODO Implement custom Calendar later
	/*
	public CalendarApp (int aYear, int aMonth, int aDay) {
		this.year = aYear;
		this.month = aMonth;
		this.day = aDay;
	}
	*/
	
	public int getYear() {
		return this.year;
	}
	
	public int getMonth() {
		return this.month;
	}
	
	public int getDay() {
		return this.day;
	}
	
	public String getFullDate() {
		String date = this.year + "/" + this.month + "/" + this.day;
		
		return date;
	}
	
	public String getYearMonthDate() {
		String date = this.year + "/" + this.month;
		
		return date;
	}
	
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
	
	public Map<Integer, String> getDayMonthMap() {
		Map<Integer, String> dayMonthMap = new HashMap<Integer, String>();
		//Map<String, Integer> dayMonthMap = new HashMap<String, Integer>(); // if you want to swap to Map<String, Integer> format
		
		int maxLengthOfMonth = YearMonth.of(this.year, this.month).lengthOfMonth();
		
		for (int i = 1; i <= maxLengthOfMonth; i++) {
			dayMonthMap.put(i, getDayOfWeek(i));
			//dayMonthMap.put(getDayOfWeek(i), i); // if you want to swap to Map<String, Integer> format
		}
		
		return dayMonthMap;
	}
	
	// TODO implement a Monthly Map Display ArrayList -> Has the exact Structure from Sunday to Saturday
	// Has each week with numbers associated.
	public ArrayList<Map<String, Integer>> getCalendarDisplay() {
		// TODO implement ArrayList here. 
		// Each array index should have the following Map Keys: SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
		// Each Value for the Key should have a numerical representation. 0 if the Month doesn't have a date for that day. 
		
		return null;
	}

	public static void main(String[] args) {
		CalendarApp sampleCalendar = new CalendarApp();
		
		// Sample Day of Week Print Outs
		System.out.println("Sample Day of Week Print Outs: On "+sampleCalendar.getYearMonthDate()+":");
		for (int i = 1; i <= 7; i++) {
			System.out.println("\tDay "+i+" falls on a "+sampleCalendar.getDayOfWeek(i));
		}
		
		// Getting Month represented numerically as a map
		Map<Integer, String> dayMonthMap = sampleCalendar.getDayMonthMap();
		int count = 0;
		System.out.println("\nDisplaying months numerically as a map:");
		for (Integer i: dayMonthMap.keySet()) {
			if (count == 7) {
				System.out.println();
				count = 0;
			}
			System.out.print(i + "=" + dayMonthMap.get(i) + "\t");
			count++;
		}
		
	}

}
