package finalproject.ee552.mainapp;

import finalproject.ee552.backend.AppointmentScheduler;
import finalproject.ee552.backend.ConsoleDisplayCalendarView;
import finalproject.ee552.backend.JSONCalendar;

public class AppointmentCalendarConsoleDemo {

	public static void main(String[] args) {
		/* ================================================
		 * 	Set-Up the JSON Reader
		 *  ================================================
		 */
		JSONCalendar jsonData = new JSONCalendar();
		jsonData.read("savedSession.json");
		
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

		
		/* ================================================
		 *  Request More TimeSlots
		 *  ================================================
		 */

		System.out.println("Request Time on April 8th, 11:45 AM :" + scheduler.reserve(8, "11:45 AM", "Student A Appointment"));
		System.out.println("Request Time on April 8th, 3:11 PM  :" + scheduler.reserve(8, "3:11 PM", "Student B Appointment"));
		System.out.println("Request Time on April 8th, 9:00 AM  :" + scheduler.reserve(8, "9:00 AM", "Faculty Meeting"));
		System.out.println("Request Time on April 8th, 10:11 AM :" + scheduler.reserve(8, "10:11 AM", "Student A Appointment"));
		System.out.println("Request Time on April 8th, 12:45 PM :" + scheduler.reserve(8, "12:45 PM", "Lunch Meeting"));
		System.out.println("Request Time on April 8th, 4:11 PM  :" + scheduler.reserve(8, "4:11 PM", "Student B Appointment"));

		System.out.println("Request Time on April 9th, 9:33 AM  :" + scheduler.reserve(9, "9:33 AM", "Meeting Sample"));
		System.out.println("Request Time on April 9th, 11:30 AM :" + scheduler.reserve(9, "11:30 AM", "Student A Appointment"));
		System.out.println("Request Time on April 9th, 12:33 PM :" + scheduler.reserve(9, "12:33 PM", "Lunch Date"));
		System.out.println("Request Time on April 9th, 2:30 PM  :" + scheduler.reserve(9, "2:30 PM", "PhD Presentations"));
		System.out.println("Request Time on April 9th, 12:33 PM :" + scheduler.reserve(9, "12:33 PM", "PhD Presentations"));
		System.out.println("Request Time on April 9th, 4:30 PM  :" + scheduler.reserve(9, "4:30 PM", "Student B Appointment"));

		System.out.println("Request Time on April 10th, 9:30 AM :" + scheduler.reserve(10, "9:30 AM", "Meeting Sample"));
		System.out.println("Request Time on April 10th, 3:30 PM :" + scheduler.reserve(10, "3:30 PM", "PhD Presentations"));
		System.out.println("Request Time on April 10th, 4:30 PM :" + scheduler.reserve(10, "4:30 PM", "PhD Presentations"));
		System.out.println("Request Time on April 10th, 2:30 PM :" + scheduler.reserve(10, "2:30 PM", "Student A Appointment"));
		System.out.println("Request Time on April 10th, 1:30 PM :" + scheduler.reserve(10, "1:30 PM", "Tennis Team Meet"));
		System.out.println("Request Time on April 10th, 11:30 AM:" + scheduler.reserve(10, "11:30 AM", "Freshman Welcome Meet"));

		
		/* ================================================
		 *  Cancel Appointments
		 *  ================================================
		 */

		System.out.println();
		System.out.println("Request Time on April 11th, 9:30 AM :" + scheduler.reserve(11, "9:30 AM", "Meeting Sample"));
		System.out.println("Request Time on April 11th, 3:30 PM :" + scheduler.reserve(11, "3:30 PM", "Meeting Sample"));
		System.out.println("Cancel 11th-9:30AM: "+scheduler.removeAppointment(11, "9:30 AM"));
		System.out.println("Cancel 11th-3:30PM: "+scheduler.removeAppointment(11, "3:30 PM"));

		
		/* ================================================
		 *  Get Available Slots in Each Day of Month
		 *  ================================================
		 */

		System.out.println("\nGet Available Slots in Each Day of Month (Max 17 slots per day):");
		for (int i = 1; i <= scheduler.getNumDays(); i++) {
			System.out.println("\tNumber of Available Slots on (April " + i + ") = "+scheduler.getNumberOfAvailableSlots(i));
		}
		System.out.println();

		/* ================================================
		 *  Display 7 Day View
		 *  ================================================
		 */
		ConsoleDisplayCalendarView viewer = new ConsoleDisplayCalendarView(scheduler);
		System.out.println(viewer.generateWeeklyView(1));
		
		/* ================================================
		 *  Store current scheduler object in a JSON file
		 *  ================================================
		 */
		jsonData.write(scheduler, "savedSession.json");
		System.out.println("\nSuccess! Finished writing current session to JSON File!");

	}

}
