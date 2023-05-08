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
		 * 	SAMPLE TEST 1
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
		
		/** ================================================
		 * 	SAMPLE TEST 2
		 *  Request More TimeSlots
		 *  ================================================
		 */
		scheduler.reserve(8, "11:45 AM", "Student A Appointment", true);
		scheduler.reserve(8, "3:11 PM", "Student B Appointment", true);
		scheduler.reserve(8, "9:00 AM", "Faculty Meeting", true);
		scheduler.reserve(8, "10:11 AM", "Student A Appointment", true);
		scheduler.reserve(8, "12:45 PM", "Lunch Meeting", true);
		scheduler.reserve(8, "4:11 PM", "Student B Appointment", true);
		
		scheduler.reserve(9, "9:33 AM", "Meeting Sample", true);
		scheduler.reserve(9, "11:30 AM", "Student A Appointment", true);
		scheduler.reserve(9, "12:33 PM", "Lunch Date", true);
		scheduler.reserve(9, "2:30 PM", "PhD Presentations", true);
		scheduler.reserve(9, "12:33 PM", "PhD Presentations", true);
		scheduler.reserve(9, "4:30 PM", "Student B Appointment", true);
		
		scheduler.reserve(10, "9:30 AM", "Meeting Sample", true);
		scheduler.reserve(10, "3:30 PM", "PhD Presentations", true);
		scheduler.reserve(10, "4:30 PM", "PhD Presentations", true);
		scheduler.reserve(10, "2:30 PM", "Student A Appointment", true);
		scheduler.reserve(10, "1:30 PM", "Tennis Team Meet", true);
		scheduler.reserve(10, "11:30 AM", "Freshman Welcome Meet", true);
		
		/** ================================================
		 * 	SAMPLE TEST 4
		 *  Cancel Appointments
		 *  ================================================
		 */
		System.out.println();
		scheduler.reserve(11, "9:30 AM", "Meeting Sample", true);
		scheduler.reserve(11, "3:30 PM", "Meeting Sample", true);
		System.out.println("Cancel 11th-9:30AM: "+scheduler.removeAppointment(11, "9:30 AM"));
		System.out.println("Cancel 11th-3:30PM: "+scheduler.removeAppointment(11, "3:30 PM"));
		
		/** ================================================
		 * 	SAMPLE TEST 6
		 *  Get Available Slots in Each Day of Month
		 *  ================================================
		 */
		System.out.println("\nGet Available Slots in Each Day of Month (Max 17 slots per day):");
		for (int i = 1; i <= scheduler.getNumDays(); i++) {
			System.out.println("\tNumber of Available Slots on (April " + i + ") = "+scheduler.getNumberOfAvailableSlots(i));
		}
		
		
		
		// TODO: Store the current calendar status in a JSON file to be 
		
	}

}
