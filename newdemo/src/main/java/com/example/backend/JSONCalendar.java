package backend;

import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* class: JSON Month Map
 * Description: The purpose of this class is to map multiple day values to a single month
 * This makes reading/organizing data from a JSON file easy
 * Each Month has a list of Days (JSONDayMap)
 */
class JSONMonthMap {
	private long month;
	private List<JSONDayMap> days;
	
	/* class constructor: JSONMonthMap
	 * @param: long - month value
	 * Description: creates the constructor and populates the month/ initializes the days list
	 */
	public JSONMonthMap (long month) {
		this.month = month;
		this.days = new ArrayList<JSONDayMap>();
	}
	
	/*  @Function: getMonth
	 *  @param: none
	 *  @return: long
	 *  @description: gets the month value
	 */
	public long getMonth() {
		return this.month;
	}
	
	/*  @Function: getMonth
	 *  @param: JSONDayMap object's day
	 *  @return: none
	 *  @description: allows to add JSONDayMap objects to the list
	 */
	public void addDays (JSONDayMap day) {
		this.days.add(day);
	}
	
	/*  @Function: getMonth
	 *  @param: none
	 *  @return: List<JSONDayMap> - the days list associated with a month
	 *  @description: returns the current existing list of JSONDayMap objects
	 */
	public List<JSONDayMap> getDayMap () {
		return this.days;
	}
}

/* class: JSON Day Map
 * Description: The purpose of this function is to map time slots to a single day
 * This makes read/organizing data from a JSON file easy
 * Each day has a Map of Time Slots
 */
class JSONDayMap {
	private long day;
	private Map<String, String> timeSlots;
	
	/* class constructor: JSONDayMap
	 * @param: long - day value
	 * Description: creates the constructor and populates the day/ initializes the time slots map
	 */
	public JSONDayMap (long day) {
		this.day = day;
		this.timeSlots = new HashMap<String, String>();
	}
	
	/*  @Function: getDay
	 *  @param: none
	 *  @return: long
	 *  @description: gets the day value
	 */
	public long getDay() {
		return this.day;
	}
	
	/*  @Function: setTimeSlots
	 *  @param: String - requested time, String - appointment text
	 *  @return: none
	 *  @description: populates the map with a specified time and text
	 */
	public void setTimeSlots(String time, String text) {
		this.timeSlots.put(time, text);
	}
	
	/*  @Function: getTimeSlots
	 *  @param: none
	 *  @return: Map<String, String> - the time slots map associated with a month
	 *  @description: returns the current existing map representing the time slots
	 */
	public Map<String, String> getTimeSlots() {
		return this.timeSlots;
	}
}

/* class: JSONReadFile
 * Description: The purpose of this class is to read the JSON File in a easy to call object
 */
public class JSONCalendar {
	private Map<Long, List<JSONMonthMap>> yearlyData;
	private String JSONInputFilePath;
	private String JSONOutputFilePath;
	
	/* enums: JSONKeys
	 * Description: creates the enums of keys that are in the JSON file
	 */
	private enum JSONKeys {
		YEAR, MONTH_NUMBER, MONTHS_LIST, DAYS_IN_MONTH_DICT;
	}
	
	/* constructor class: JSONReadFile
	 * Description: initializes the file path to read the file properly
	 * Also, initializes the yearly data map and starts reading the file
	 */
	public JSONCalendar () {
		this.yearlyData = new HashMap<Long, List<JSONMonthMap>>();
	}
	
	/* @Function: read
	 * @param: String - file name to be read in
	 * @return: none
	 * Description: takes in the file name parameter and starts reading it
	 */
	public void read(String inputFileName) {
		this.JSONInputFilePath = System.getProperty("user.dir") + File.separator + inputFileName;
		readFile(inputFileName);
	}
	
	/* @Function: write
	 * @param: String - file name to be write out
	 * @return: none
	 * Description: takes in the file name parameter and starts writing to it
	 */
	public void write(AppointmentScheduler scheduler, String outputFileName) throws IOException {
		this.JSONOutputFilePath = System.getProperty("user.dir") + File.separator + outputFileName;
		writeFile(scheduler, outputFileName);
	}
	
	/*  @Function: writeFile
	 *  @param: String - fileName of the output json file
	 *  @return: none
	 *  @description: writes to the json file and populates all the values
	 */
	private void writeFile(AppointmentScheduler scheduler, String fileName) throws IOException {
		File file = new File(fileName);
		FileWriter writer = new FileWriter(file);
		
		// Store values to outputData map
		Map<String, Object> outputData = new HashMap<>();
		
		// STEP 1: Storing the Year
		outputData.put(JSONKeys.YEAR.name(), scheduler.getYear());
		
		// STEP 2: Storing the list of months
        Map<String, Object> monthsListMap = new HashMap<>();
        monthsListMap.put(JSONKeys.MONTH_NUMBER.name(), scheduler.getMonth());

	    // STEP 3: Storing the days in a month dictionary
	    Map<String, Object> daysInMonthDict = new HashMap<>();
	    for (int days = 1; days <= scheduler.getNumDays(); days++) {
	    	String dayNumber = String.valueOf(days);
	    	daysInMonthDict.put(dayNumber, scheduler.getTimeSlotsMap(days));
	    }
	    
	    // STEP 4: Storing days in month dict in the months list map
	    monthsListMap.put(JSONKeys.DAYS_IN_MONTH_DICT.name(), daysInMonthDict);
	    
	    // STEP 4.5: Adding monthslistmap to an arraylist
	    List<Map<String, Object>> tempList = new ArrayList<>();
	    tempList.add(monthsListMap);
	    
	    // STEP 5: Storing dictionary in the output data final map
	    outputData.put(JSONKeys.MONTHS_LIST.name(), tempList);
	    
	    // Writing to file using object mapper - and using writeValueAsString
	    writer.write(new ObjectMapper().writeValueAsString(outputData));
	    
	    // Closing the writer
	    writer.close();
	}
	
	/*  @Function: readFile
	 *  @param: String - fileName of the input json file
	 *  @return: none
	 *  @description: reads the json file and populates all the values
	 *  Creates a Map<Long, List<JSONMonthMap>>. Long represents the year value, and each year is mapped to list of months (JSONMonthMap)
	 */
	private void readFile(String fileName) {
		JSONParser parser = new JSONParser();
		
		try {
			Object o = parser.parse(new FileReader(this.JSONInputFilePath));
			JSONObject jO = (JSONObject) o;
			
			long year = (long) jO.get(JSONKeys.YEAR.name());
			List<JSONMonthMap> monthsList = new ArrayList<JSONMonthMap>();
			
			JSONArray monthsArray = (JSONArray) jO.get(JSONKeys.MONTHS_LIST.name());
			Iterator<JSONObject> monthsIter = monthsArray.iterator();
			while(monthsIter.hasNext()) {
				// Go to the next value
				JSONObject mO = monthsIter.next();
				
				// Get the month data and create a monthMap
				long month = (long) mO.get(JSONKeys.MONTH_NUMBER.name());
				JSONMonthMap monthMap = new JSONMonthMap(month);
				
				// traverse through the days
				JSONObject dO = (JSONObject) mO.get(JSONKeys.DAYS_IN_MONTH_DICT.name());
				for (Object dayKey: dO.keySet()) {
					// get the days data and create the daysMap
					long dayNumber = Long.valueOf((String) dayKey);
					JSONDayMap dayMap = new JSONDayMap(dayNumber);
					
					// traverse through the time slots
					JSONObject tO = (JSONObject) dO.get(dayKey);
					for (Object time: tO.keySet()) {
						// get the time and the boolean value and store it
						String timeStr = (String) time;
						String timeValue = (String) tO.get(time);
						dayMap.setTimeSlots(timeStr, timeValue);
					}
					// add the days to the month map
					monthMap.addDays(dayMap);
				}
				// add the months to the list
				monthsList.add(monthMap);
			}
			// add the list of months mapped to a year
			this.yearlyData.put(year, monthsList);
			
		} catch (Exception e) {
			System.out.println("ERROR! Unable to read from JSON File!");
			e.printStackTrace();
		}
	}
	
	/*  @Function: printData
	 *  @param: none
	 *  @return: none
	 *  @description: function prints the data that is read in from the JSON file. Used for checking to see if the class works
	 */
	private void printData () {
		// Map<Long, List<JSONMonthMap>> yearlyData;
		for (long year: this.yearlyData.keySet()) {
			System.out.println("Current Year: " + year);
			for (JSONMonthMap month: this.yearlyData.get(year)) {
				System.out.println("Current Month: " + month.getMonth());
				for (JSONDayMap day: month.getDayMap()) {
					System.out.println("Current Day: " + day.getDay());
					for (String time: day.getTimeSlots().keySet()) {
						System.out.println("\t"+time+"\t"+day.getTimeSlots().get(time));
					}
				}
			}
		}
	}
	
	/*  @Function: checkMonthExists
	 *  @param: List<JSONMonthMap> - monthsMap, long - monthVal
	 *  @return: boolean
	 *  @description: checks to see if a particular month value exists in the list of JSONMonthMap
	 */
	private boolean checkMonthExists(List<JSONMonthMap> monthsMap, long monthVal) {
		for (JSONMonthMap month: monthsMap) {
			if (month.getMonth() == monthVal) {
				return true;
			}
		}
		
		return false;
	}
	
	/*  @Function: getMonthData
	 *  @param: long - year, long - month
	 *  @return: List<JSONDayMap>
	 *  @description: based on a specified year and month, returns the list of JSONDayMap. 
	 *  Used in AppointmentScheduler to populate data from a file
	 */
	public List<JSONDayMap> getMonthData(long year, long month) {
		JSONMonthMap tempMonth = null;
		if (checkMonthExists(this.yearlyData.get(year), month) == true) {
			for (JSONMonthMap m: this.yearlyData.get(year)) {
				if (m.getMonth() == month) {
					tempMonth = m;
				}
			}
		}
		
		if (tempMonth == null) {
			tempMonth = this.yearlyData.get(year).get(0);
		}
		
		return tempMonth.getDayMap();
	}

}
