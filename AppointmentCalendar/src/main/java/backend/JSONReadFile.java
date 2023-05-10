package backend;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class JSONMonthMap {
	private long month;
	private List<JSONDayMap> days;
	
	public JSONMonthMap (long month) {
		this.month = month;
		this.days = new ArrayList<JSONDayMap>();
	}
	
	public long getMonth() {
		return this.month;
	}
	
	public void addDays (JSONDayMap day) {
		this.days.add(day);
	}
	
	public List<JSONDayMap> getDayMap () {
		return this.days;
	}
}

class JSONDayMap {
	private long day;
	private Map<String, String> timeSlots;
	
	public JSONDayMap (long day) {
		this.day = day;
		this.timeSlots = new HashMap<String, String>();
	}
	
	public long getDay() {
		return this.day;
	}
	
	public void setTimeSlots(String time, String text) {
		this.timeSlots.put(time, text);
	}
	
	public Map<String, String> getTimeSlots() {
		return this.timeSlots;
	}
}

public class JSONReadFile {
	private Map<Long, List<JSONMonthMap>> yearlyData;
	private String JSONFilePath;
	
	private enum FileNames {
		year, month, months, day, days, timeSlots;
	}
	
	public JSONReadFile (String fileName) {
		String filePath = System.getProperty("user.dir");
		
		this.JSONFilePath = filePath + File.separator + fileName;
		
		this.yearlyData = new HashMap<Long, List<JSONMonthMap>>();
		readFile(fileName);
	}
	
	private void readFile(String fileName) {
		JSONParser parser = new JSONParser();
		
		try {
			Object o = parser.parse(new FileReader(this.JSONFilePath));
			JSONObject jO = (JSONObject) o;
			
			long year = (long) jO.get(FileNames.year.name());
			List<JSONMonthMap> monthsList = new ArrayList<JSONMonthMap>();
			
			JSONArray monthsArray = (JSONArray) jO.get(FileNames.months.name());
			Iterator<JSONObject> monthsIter = monthsArray.iterator();
			while(monthsIter.hasNext()) {
				// Go to the next value
				JSONObject mO = monthsIter.next();
				
				// Get the month data and create a monthMap
				long month = (long) mO.get(FileNames.month.name());
				JSONMonthMap monthMap = new JSONMonthMap(month);
				
				// traverse through the days
				JSONObject dO = (JSONObject) mO.get(FileNames.days.name());
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
	
	private boolean checkMonthExists(List<JSONMonthMap> monthsMap, long monthVal) {
		for (JSONMonthMap month: monthsMap) {
			if (month.getMonth() == monthVal) {
				return true;
			}
		}
		
		return false;
	}
	
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
