package main;

import backend.AppointmentScheduler;
import backend.ConsoleDisplayCalendarView;
import backend.JSONCalendar;

public class AppointmentCalendarMain {

	public static void main(String[] args) {
		/* ================================================
		 * 	Set-Up the JSON Reader
		 *  ================================================
		 */
		JSONCalendar jsonData = new JSONCalendar();
		jsonData.read("savedSession.json");
		//jsonData.printData();
		
		/* ================================================
		 * 	Set-Up the Scheduler + Read from JSON File
		 *  ================================================
		 */
		int year = 2023;
		int month = 4;
		AppointmentScheduler scheduler = new AppointmentScheduler(year, month);
		scheduler.populateDataFromJSON(jsonData);
		
		/* ================================================
		 *  Displaying your current month
		 *  ================================================
		 */
		System.out.println("Displaying Calendar Layout For:"+year+"/"+month);
		System.out.println(scheduler.displayCalendarLayout());
		
		/* ================================================
		 *  Display Schedule Read from JSON File
		 *  ================================================
		 */
		/*
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
		
		System.out.println("Reading from JSON File - TimeSlots for April 30th:");
		System.out.println(scheduler.displayScheduleOnDay(30));
		*/
		
		/* ================================================
		 *  Request More TimeSlots
		 *  ================================================
		 */
		/*
		scheduler.reserve(8, "11:45 AM", "Student A Appointment");
		scheduler.reserve(8, "3:11 PM", "Student B Appointment");
		scheduler.reserve(8, "9:00 AM", "Faculty Meeting");
		scheduler.reserve(8, "10:11 AM", "Student A Appointment");
		scheduler.reserve(8, "12:45 PM", "Lunch Meeting");
		scheduler.reserve(8, "4:11 PM", "Student B Appointment");
		
		scheduler.reserve(9, "9:33 AM", "Meeting Sample");
		scheduler.reserve(9, "11:30 AM", "Student A Appointment");
		scheduler.reserve(9, "12:33 PM", "Lunch Date");
		scheduler.reserve(9, "2:30 PM", "PhD Presentations");
		scheduler.reserve(9, "12:33 PM", "PhD Presentations");
		scheduler.reserve(9, "4:30 PM", "Student B Appointment");
		
		scheduler.reserve(10, "9:30 AM", "Meeting Sample");
		scheduler.reserve(10, "3:30 PM", "PhD Presentations");
		scheduler.reserve(10, "4:30 PM", "PhD Presentations");
		scheduler.reserve(10, "2:30 PM", "Student A Appointment");
		scheduler.reserve(10, "1:30 PM", "Tennis Team Meet");
		scheduler.reserve(10, "11:30 AM", "Freshman Welcome Meet");
		*/
		
		/* ================================================
		 *  Cancel Appointments
		 *  ================================================
		 */
		/*
		System.out.println();
		scheduler.reserve(11, "9:30 AM", "Meeting Sample");
		scheduler.reserve(11, "3:30 PM", "Meeting Sample");
		System.out.println("Cancel 11th-9:30AM: "+scheduler.removeAppointment(11, "9:30 AM"));
		System.out.println("Cancel 11th-3:30PM: "+scheduler.removeAppointment(11, "3:30 PM"));
		*/
		
		/* ================================================
		 *  Get Available Slots in Each Day of Month
		 *  ================================================
		 */
		/*
		System.out.println("\nGet Available Slots in Each Day of Month (Max 17 slots per day):");
		for (int i = 1; i <= scheduler.getNumDays(); i++) {
			System.out.println("\tNumber of Available Slots on (April " + i + ") = "+scheduler.getNumberOfAvailableSlots(i));
		}
		System.out.println();
		*/
		/* ================================================
		 *  Display 7 Day View
		 *  ================================================
		 */
		ConsoleDisplayCalendarView viewer = new ConsoleDisplayCalendarView(scheduler);
		viewer.generateWeeklyView(1);
		
		/* ================================================
		 *  Store current scheduler object in a JSON file
		 *  ================================================
		 */
		try {
			jsonData.write(scheduler, "savedSession.json");
			System.out.println("\nSuccess! Finished writing current session to JSON File!");
			
		} catch (Exception e) {
			System.out.println("ERROR! Failed to write saved session to file!");
			e.printStackTrace();
		}
	}

}
