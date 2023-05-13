package junit_tests;
import org.junit.jupiter.api.Test;	// used for testing

import backend.MonthlyCalendarApp;

import static org.junit.jupiter.api.Assertions.*;	// for assertions

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

public class MonthlyCalendarAppTest {
    // Write some tests here
	
	private MonthlyCalendarApp sampleCalendar;
	
	@BeforeEach
	public void beforeEachTestMethods() { 
		sampleCalendar = new MonthlyCalendarApp(2023, 4);
	}
	
	@Test
	void canGetTodayYearMonth() {
		String output = "2023/4";
		assertEquals(output, sampleCalendar.getYearMonthDate());
	}
	
	@Test
	void canGetDayOfWeek() {
		/** ================================================
		 * 	SAMPLE TEST
		 *  Sample Day of Week Print Outs
		 *  ================================================
		 */
		assertEquals("SATURDAY"	, sampleCalendar.getDayOfWeek(1));
		assertEquals("SUNDAY"	, sampleCalendar.getDayOfWeek(2));
		assertEquals("MONDAY"	, sampleCalendar.getDayOfWeek(3));
		assertEquals("TUESDAY"	, sampleCalendar.getDayOfWeek(4));
		assertEquals("WEDNESDAY", sampleCalendar.getDayOfWeek(5));
		assertEquals("THURSDAY"	, sampleCalendar.getDayOfWeek(6));
		assertEquals("FRIDAY"	, sampleCalendar.getDayOfWeek(7));
	}
	
	@Test
	void canGetNumericalDayMonthMap() {
		/** ================================================
		 * 	SAMPLE TEST
		 *  Getting Month represented numerically as a map
		 *  ================================================
		 */
		// generate map for testing
		Map<Integer, String> dayMonthMapTest = new HashMap<Integer, String>();
		dayMonthMapTest.put(1, "SATURDAY");
		dayMonthMapTest.put(2, "SUNDAY");
		dayMonthMapTest.put(3, "MONDAY");
		dayMonthMapTest.put(4, "TUESDAY");
		dayMonthMapTest.put(5, "WEDNESDAY");
		dayMonthMapTest.put(6, "THURSDAY");
		dayMonthMapTest.put(7, "FRIDAY");
		dayMonthMapTest.put(8, "SATURDAY");
		dayMonthMapTest.put(9, "SUNDAY");
		dayMonthMapTest.put(10, "MONDAY");
		dayMonthMapTest.put(11, "TUESDAY");
		dayMonthMapTest.put(12, "WEDNESDAY");
		dayMonthMapTest.put(13, "THURSDAY");
		dayMonthMapTest.put(14, "FRIDAY");
		dayMonthMapTest.put(15, "SATURDAY");
		dayMonthMapTest.put(16, "SUNDAY");
		dayMonthMapTest.put(17, "MONDAY");
		dayMonthMapTest.put(18, "TUESDAY");
		dayMonthMapTest.put(19, "WEDNESDAY");
		dayMonthMapTest.put(20, "THURSDAY");
		dayMonthMapTest.put(21, "FRIDAY");
		dayMonthMapTest.put(22, "SATURDAY");
		dayMonthMapTest.put(23, "SUNDAY");
		dayMonthMapTest.put(24, "MONDAY");
		dayMonthMapTest.put(25, "TUESDAY");
		dayMonthMapTest.put(26, "WEDNESDAY");
		dayMonthMapTest.put(27, "THURSDAY");
		dayMonthMapTest.put(28, "FRIDAY");
		dayMonthMapTest.put(29, "SATURDAY");
		dayMonthMapTest.put(30, "SUNDAY");
		
		Map<Integer, String> dayMonthMapActual = sampleCalendar.getDayMonthMap();
		
		try {
			for (int i: dayMonthMapActual.keySet()) {
				String expected = dayMonthMapTest.get(i);
				String actual 	= dayMonthMapActual.get(i);
				assertEquals(expected, actual);
			}
		} catch (Exception e) {
			System.out.println("Error canGetNumericalDayMonthMap Test!");
			e.printStackTrace();
		}
	}
	
	@Test
	void canGetFullCalendarLayout() {
		/** ================================================
		 * 	SAMPLE TEST
		 *  Get Full Calendar as ArrayMap
		 *  ================================================
		 */
		
		List<Map<String, Integer>> fullCalendarExpected = new ArrayList<>();
		Map<String, Integer> tempMap = new HashMap<>();
		tempMap.put("WEDNESDAY", 0);
		tempMap.put("MONDAY", 0);
		tempMap.put("THURSDAY", 0);
		tempMap.put("SUNDAY", 0);
		tempMap.put("TUESDAY", 0);
		tempMap.put("FRIDAY", 0);
		tempMap.put("SATURDAY", 1);
		fullCalendarExpected.add(tempMap);
		
		tempMap = new HashMap<>();
		tempMap.put("WEDNESDAY", 5);
		tempMap.put("MONDAY", 3);
		tempMap.put("THURSDAY", 6);
		tempMap.put("SUNDAY", 2);
		tempMap.put("TUESDAY", 4);
		tempMap.put("FRIDAY", 7);
		tempMap.put("SATURDAY", 8);
		fullCalendarExpected.add(tempMap);

		tempMap = new HashMap<>();
		tempMap.put("WEDNESDAY", 12);
		tempMap.put("MONDAY", 10);
		tempMap.put("THURSDAY", 13);
		tempMap.put("SUNDAY", 9);
		tempMap.put("TUESDAY", 11);
		tempMap.put("FRIDAY", 14);
		tempMap.put("SATURDAY", 15);
		fullCalendarExpected.add(tempMap);
		
		tempMap = new HashMap<>();
		tempMap.put("WEDNESDAY", 19);
		tempMap.put("MONDAY", 17);
		tempMap.put("THURSDAY", 20);
		tempMap.put("SUNDAY", 16);
		tempMap.put("TUESDAY", 18);
		tempMap.put("FRIDAY", 21);
		tempMap.put("SATURDAY", 22);
		fullCalendarExpected.add(tempMap);
		
		tempMap = new HashMap<>();
		tempMap.put("WEDNESDAY", 26);
		tempMap.put("MONDAY", 24);
		tempMap.put("THURSDAY", 27);
		tempMap.put("SUNDAY", 23);
		tempMap.put("TUESDAY", 25);
		tempMap.put("FRIDAY", 28);
		tempMap.put("SATURDAY", 29);
		fullCalendarExpected.add(tempMap);
		
		tempMap = new HashMap<>();
		tempMap.put("WEDNESDAY", 0);
		tempMap.put("MONDAY", 0);
		tempMap.put("THURSDAY", 0);
		tempMap.put("SUNDAY", 30);
		tempMap.put("TUESDAY", 0);
		tempMap.put("FRIDAY", 0);
		tempMap.put("SATURDAY", 0);
		fullCalendarExpected.add(tempMap);
		
		List<Map<String, Integer>> fullCalendarActual = sampleCalendar.getFullCalendarLayout();
		
		int index = 0;
		for (Map<String, Integer> actualMap: fullCalendarActual) {
			
			for (String actualKey: actualMap.keySet()) {
				int expectedValue  = fullCalendarExpected.get(index).get(actualKey);
				int actualValue    = actualMap.get(actualKey);
				
				//System.out.println("actualKey: " +actualKey);
				//System.out.println("\texpectedValue: "+expectedValue+"\tactualValue: "+actualValue);
				assertEquals(expectedValue, actualValue);
				
			}
			index++;
		}
	}
	
}
