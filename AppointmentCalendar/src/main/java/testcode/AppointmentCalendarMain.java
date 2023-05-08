package testcode;

/** 
 * This code serves as a testing ground for our project code.
 * Also allows us to save the current calendar status to a file repeatedly
 */

import backEnd.AppointmentScheduler;
import backEnd.JSONReadFile;

public class AppointmentCalendarMain {

	public static void main(String[] args) {
		/** ================================================
		 * 	Set-Up the JSON Reader
		 *  ================================================
		 */
		JSONReadFile jsonData = new JSONReadFile("AppointmentsTemplate_WithText.json");
		//jsonData.printData();
		
		/** ================================================
		 * 	Set-Up the Scheduler + Read from JSON File
		 *  ================================================
		 */
		int year = 2023;
		int month = 4;
		AppointmentScheduler scheduler = new AppointmentScheduler(year, month);
		scheduler.populateDataFromJSON(jsonData.getMonthData(year, month), false);	// True - Display Reservation Logs. False - Don't Display
		
		/** ================================================
		 * 	SAMPLE TEST
		 *  Display Schedule Read from JSON File
		 *  ================================================
		 */
		System.out.println("Reading from JSON File - TimeSlots for April 1st:");
		System.out.println(scheduler.displayScheduleOnDay(1));
		System.out.println("Reading from JSON File - TimeSlots for April 2nd:");
		System.out.println(scheduler.displayScheduleOnDay(2));
		System.out.println("Reading from JSON File - TimeSlots for April 3rd:");
		System.out.println(scheduler.displayScheduleOnDay(3));
		System.out.println("Reading from JSON File - TimeSlots for April 4th:");
		System.out.println(scheduler.displayScheduleOnDay(4));
		System.out.println("Reading from JSON File - TimeSlots for April 5th:");
		System.out.println(scheduler.displayScheduleOnDay(5));
		System.out.println("Reading from JSON File - TimeSlots for April 6th:");
		System.out.println(scheduler.displayScheduleOnDay(6));
		System.out.println("Reading from JSON File - TimeSlots for April 7th:");
		System.out.println(scheduler.displayScheduleOnDay(7));
		
		// TODO: Store the current calendar status in a JSON file to be 

	}

}
