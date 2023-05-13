package backend;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* class: Monthly Calendar App
 * Description: The purpose of this class is to establish a calendar layout for a specified year and month
 * The class can dynamically obtain the structure of a month and create maps to represent the calendar. 
 * You can also prompt it questions like what day of week a certain day in a month falls in. 
 * For example April 1st, 2023 = SATURDAY
 */
public class MonthlyCalendarApp {
	
	private int year;
	private int month;
	private Map<Integer, String> dayMonthMap;
	private List<Map<String, Integer>> fullCalendarLayout;

	/* class constructor: MonthlyCalendarApp
	 * @param: int - aYear, int - aMonth
	 * Description: intializes the calendar layout for a specified year and month
	 */
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
	
	/*  @Function: getDayOfWeek
	 *  @param: int - day input
	 *  @return: String
	 *  @description: gets the string value associated with a particular day. 
	 *  The string value represents a day of week - For example: SATURDAY, SUNDAY, MONDAY .... 
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
	
	/*  @Function: generateDayMonthMap
	 *  @param: none
	 *  @return: Map<Integer, String> - A map of days mapped to day of weeks
	 *  @description: generates a map of days mapped to day of week. 
	 *  For example: 1: SATURDAY, 2:SUNDAY, 3:MONDAY.... so on.
	 */
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
	
	/*  @Function: generateFullCalendarLayout
	 *  @param: none
	 *  @return: List<Map<String, Integer>> list of week maps
	 *  @description: Generates the full calendar layout for the specified class object's month. 
	 *  This is a list of weeks. Each week is a map. Each map is a String, Integer. String represents day of week. Integer represents day number
	 *  For Example for 2023/April, the first week map will look like:
	 *      tempMap.put("WEDNESDAY", 0);
			tempMap.put("MONDAY", 0);
			tempMap.put("THURSDAY", 0);
			tempMap.put("SUNDAY", 0);
			tempMap.put("TUESDAY", 0);
			tempMap.put("FRIDAY", 0);
			tempMap.put("SATURDAY", 1);
	 *  This is because April 1st falls on a Saturday. 
	 *  The full Calendar Layout looks like: 
	 *  	SUN	MON	TUE	WED	THU	FRI	SAT	
			0	0	0	0	0	0	1	
			2	3	4	5	6	7	8	
			9	10	11	12	13	14	15	
			16	17	18	19	20	21	22	
			23	24	25	26	27	28	29	
			30	0	0	0	0	0	0	
	 */
	private List<Map<String, Integer>> generateFullCalendarLayout() {
		List<Map<String, Integer>> fullCalendarMapList = new ArrayList<Map<String, Integer>>();
		// Each array index should have the following Map Keys: SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
		// Each Value for the Key should have a numerical representation. 0 if the Month doesn't have a date for that day. 
		
		// initializes a week format with 0s for values
		Map<String, Integer> weekFormatMap = new HashMap<String, Integer> ();
		weekFormatMap.put(DayOfWeek.SUNDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.MONDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.TUESDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.WEDNESDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.THURSDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.FRIDAY.name(), 0);
		weekFormatMap.put(DayOfWeek.SATURDAY.name(), 0);
		
		// stores the week format in a temporary map. This is reinitialized each week
		Map<String, Integer> tempWeekFormatMap = new HashMap<String, Integer>();
		tempWeekFormatMap.putAll(weekFormatMap);
		
		// start the for loop to go through the days in a month
		for (int i = 1; i <= this.dayMonthMap.size(); i++) {
			
			// stores the numerical day values associated with a day of week
			String dayOfWeek = this.dayMonthMap.get(i);
			tempWeekFormatMap.put(dayOfWeek, i);
			
			// Add Week to Calendar, and Reset Week Structure - each week is a map added to a list
			if ( dayOfWeek.equals(DayOfWeek.SATURDAY.name()) || (i == this.dayMonthMap.size()) ) {
				fullCalendarMapList.add(tempWeekFormatMap);
				
				tempWeekFormatMap = new HashMap<String, Integer>();
				tempWeekFormatMap.putAll(weekFormatMap);
			}
		}
		
		return fullCalendarMapList;
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
	
	/*  @Function: getYearMonthDate
	 *  @param: none
	 *  @return: String
	 *  @description: returns the year and month neatly displayed a string value
	 */
	public String getYearMonthDate() {
		String date = this.year + "/" + this.month;
		
		return date;
	}
	
	/*  @Function: getDayMonthMap
	 *  @param: none
	 *  @return: Map<Integer, String>
	 *  @description: returns this.dayMonthMap. A map representing day numbers to day of week strings
	 */
	public Map<Integer, String> getDayMonthMap() {
		return this.dayMonthMap;
	}
	
	/*  @Function: getFullCalendarLayout
	 *  @param: none
	 *  @return: List<Map<String, Integer>>
	 *  @description: returns this.fullCalendarLayout. 
	 *  A list of maps representing each week. 
	 *  Each week has day of week strings mapped to day numbers
	 */
	public List<Map<String, Integer>> getFullCalendarLayout() {
		return this.fullCalendarLayout;
	}
	
	/*  @Function: displayCalendarLayout
	 *  @param: none
	 *  @return:String
	 *  @description: returns the string to neatly display the calendar layout
	 */
	public String displayCalendarLayout() {
		return genCalendarLayout(getFullCalendarLayout());
	}
	
	/*  @Function: genCalendarLayout
	 *  @param: none
	 *  @return:String
	 *  @description: generates the calendar layout with a calendar input passed in
	 */
	private String genCalendarLayout(List<Map<String, Integer>> calendar) {
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
		
		for (Map<String, Integer> m: calendar) {
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
