package finalproject.ee552.mainapp;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

import finalproject.ee552.backend.AppointmentScheduler;
import finalproject.ee552.backend.ConsoleDisplayCalendarView;
import finalproject.ee552.backend.JSONCalendar;

public class InteractiveConsoleApp {
	
	private int barSize = 75;
	
	private AppointmentScheduler scheduler; 
	private JSONCalendar jsonData;
	
	private int year;
	private int month;
	
	private String exitMessage;
	private String introductionOptions;
	private String calendarOptions;
	
	private Scanner input;
	private StringBuilder sbProf;
	
	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("h:mm a");
	
	private InteractiveConsoleApp(int aYear, int aMonth) {
		this.year = aYear;
		this.month = aMonth;
		this.exitMessage = getExitMessage();
		this.introductionOptions = getIntroductionOptions();
		this.calendarOptions = getCalendarOptions();
		
		/* ================================================
		 * 	Set-Up the JSON Reader
		 *  ================================================
		 */
		this.jsonData = new JSONCalendar();
		this.jsonData.read("savedSession.json"); // Read the Data
		
		/* ================================================
		 * 	Set-Up the Scheduler + Read from JSON File
		 *  ================================================
		 */
		this.scheduler = new AppointmentScheduler(this.year, this.month);
		scheduler.populateDataFromJSON(jsonData);  // populate the scheduler with JSON data
		
		
	}
	
	private String getExitMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("=".repeat(barSize)).append("\n");
		sb.append(StringUtils.center("Thank you for using the Appointment-Calendar, Professor!!", barSize)).append("\n");
		sb.append("=".repeat(barSize)).append("\n");
		
		return sb.toString();
	}
	
	private String getIntroductionOptions() {
		StringBuilder sb = new StringBuilder();
		sb.append("=".repeat(barSize)).append("\n");
		sb.append(StringUtils.center("Welcome to the Appointment-Calendar App!", barSize)).append("\n");
		sb.append("=".repeat(barSize)).append("\n");
		
		sb.append("Please Choose Your Current Status: ").append("\n");
		sb.append("1. I am a Professor ").append("\n");
		sb.append("2. I am a Student [Under Construction] ").append("\n");
		
		return sb.toString();
	}
	
	private String getCalendarOptions() {
		StringBuilder sb = new StringBuilder();
		sb.append("=".repeat(barSize)).append("\n");
		sb.append(StringUtils.center("Professor's Appointment-Calendar App!", barSize)).append("\n");
		sb.append(StringUtils.center("APRIL of 2023", barSize)).append("\n");
		sb.append("=".repeat(barSize)).append("\n");
		
		sb.append("Please Choose Your Desired Action: ").append("\n");
		sb.append("1. Make Reservation").append("\n");
		sb.append("2. Cancel Reservation").append("\n");
		sb.append("3. Check Time-Slot Availability Status").append("\n");
		sb.append("4. Total Available Slots on a Particular Day").append("\n");
		sb.append("5. Check Schedule on a Particular Day").append("\n");
		sb.append("6. View 7-Day Calendar").append("\n");
		sb.append("7. Save Current Calendar to JSON File").append("\n");
		
		return sb.toString();
	}
	
	private int getDayInput() {
		input = new Scanner(System.in);
		String profChoice = "-1";
		
		while (!profChoice.isEmpty()) {
			System.out.print("Which day do you want?: ");
			profChoice = input.nextLine();
			
			int daychoice = 0;
			
			if (profChoice.isEmpty()) {
				return 0;
			} else {
				try {
					daychoice = Integer.parseInt(profChoice);
					int maxDays = scheduler.getNumDays();
					if (daychoice <= maxDays) {
						return daychoice;
					} else {
						System.out.println("Your entered day does not exist within the month!");
					}
				} catch (Exception e) {
					System.out.println("Please Enter Correct Inputs!");
				}
			}
		} 
		
		return 0;
	}
	
	private boolean checkTimeInput(String inputVal) {
		try {
			LocalTime.parse(inputVal, dateFormat);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private String getTimeInput() {
		input = new Scanner(System.in);
		String profChoice = "-1";
		
		while (!profChoice.isEmpty()) {
			System.out.print("Enter Time in the format of hh:mm a (Example: '9:30 AM' or '11:00 PM'): ");
			profChoice = input.nextLine();
			
			if (profChoice.isEmpty()) {
				return null;
			}
			else {
				if (Boolean.TRUE.equals(checkTimeInput(profChoice))) {
					return profChoice;
				}
			}
		} 
		
		return null;
	}
	
	private String getStringInput() {
		input = new Scanner(System.in);
		
		System.out.print("\nWhich Text-Value do you want to assign?\n(Hit ENTER to assign default of 'UNAVAILABLE'): ");
		String profChoice = input.nextLine();
		
		if (profChoice.isEmpty()) {
			return null;
		} else {
			return profChoice;
		}
	}
	
	private void makeReservationLogic() {
		System.out.println("Professor Chose Option 1: Make Reservation");
		int daychoice = getDayInput();
		String timeChoice = getTimeInput();
		String textChoice = getStringInput();
		System.out.println("textchoice: " + textChoice);
		
		if ( (daychoice != 0) && (timeChoice != null) ) {
			sbProf = new StringBuilder();
			
			String textChoiceText = "";
			
			sbProf.append("\nYour Request to Reserve an Appointment for your chosen (DAY, TIME) of ")
			.append("(").append(daychoice).append(", ").append(timeChoice).append(")");
			
			if (textChoice == null) {
				textChoiceText = "Unavailable', because you didn't choose to enter a value for text.";
			} else {
				textChoiceText = textChoice+"'";
				textChoice = textChoice.trim();
			}
			
			if (Boolean.TRUE.equals(this.scheduler.reserve(daychoice, timeChoice, textChoice))) {
				sbProf.append(" is a Success!").append("\n")
				.append("An appointment has been made with a text-value as '").append(textChoiceText).append("!");
			} else {
				sbProf.append(" Failed! The Slot is already Taken!");
			}
			System.out.println(sbProf.toString());
		} else {
			System.out.println("You chose not to Make Reservation!");
		}
	}
	
	private void cancelReservationLogic() {
		System.out.println("Professor Chose Option 2: Cancel Reservation");
		int daychoice = getDayInput();
		String timeChoice = getTimeInput();
		
		if ( (daychoice != 0) && (timeChoice != null) ) {
			sbProf = new StringBuilder();
			
			String prevText = this.scheduler.getAvailabilityText(daychoice, timeChoice);
			
			sbProf.append("\nYour Request to Cancel an Appointment for your chosen (DAY, TIME) of ")
			.append("(").append(daychoice).append(", ").append(timeChoice).append(")");
			
			if (Boolean.TRUE.equals(this.scheduler.removeAppointment(daychoice, timeChoice))) {
				sbProf.append(" for ")
				.append(prevText)
				.append(" is a Success! It has been Cancelled!");
			} else {
				sbProf.append(" Failed! The Slot is already Available!");
			}
			
			System.out.println(sbProf.toString());
		} else {
			System.out.println("You chose not to Cancel Reservation!");
		}
	}
	
	private void checkTimeSlotsStatus() { 
		System.out.println("Professor Chose Option 3: Check Time-Slot Availability Status");
		int daychoice = getDayInput();
		String timeChoice = getTimeInput();
		
		if ( (daychoice != 0) && (timeChoice != null) ) {
			sbProf = new StringBuilder();
			
			sbProf.append("\nThe Availability Status for your chosen (DAY, TIME) of ")
			.append("(").append(daychoice).append(", ").append(timeChoice).append(")").append(": ")
			.append(this.scheduler.getAvailabilityStatus(daychoice, timeChoice));
			
			System.out.println(sbProf.toString());
		} else {
			System.out.println("You chose not to Check Time-Slot Availability Status!");
		}
	}
	
	private void checkAvailableSlotsInDay() {
		System.out.println("Professor Chose Option 4: Total Available Slots on a Particular Day");
		int daychoice = getDayInput();
		
		if ( daychoice != 0 ) {
			sbProf = new StringBuilder();
			
			sbProf.append("\nThe Total Available Slots for your chosen DAY of ")
			.append("(").append(daychoice).append(") is: ")
			.append(this.scheduler.getNumberOfAvailableSlots(daychoice));
			
			System.out.println(sbProf.toString());
		} else {
			System.out.println("You chose not to Total Available Slots on a Particular Day!");
		}
	}
	
	private void displayScheduleInDay() {
		System.out.println("Professor Chose Option 5: Check Schedule on a Particular Day");
		int daychoice = getDayInput();
		
		if (daychoice != 0) {
			System.out.println(this.scheduler.displayScheduleOnDay(daychoice));
		} else {
			System.out.println("You chose not to Check Schedule on a Particular Day!");
		}
	}
	
	private void viewSevenDayCalendar() {
		System.out.println("Professor Chose Option 6: View 7-Day Calendar");
		int daychoice = getDayInput();
		
		if (daychoice != 0) {
			ConsoleDisplayCalendarView viewer = new ConsoleDisplayCalendarView(scheduler);
			System.out.println(viewer.generateWeeklyView(daychoice));
		} else {
			System.out.println("You chose not to View the 7-Day Calendar!");
		}
	}
	
	private void saveSchedulerToJSON () { 
		System.out.println("Professor Chose Option 7: Save Current Calendar to JSON File");
		jsonData.write(this.scheduler, "savedSession.json");
		System.out.println("\nSuccess! Finished writing current session to savedSession.json File!");
	}
	
	private void runProfessorCalendar() {
		input = new Scanner(System.in);

		String profChoice = "";
		
		while (!"exit".equalsIgnoreCase(profChoice)) {
			
			System.out.println(this.calendarOptions);
			
			System.out.print("What's your Choice Professor? (Type 'exit' to exit): ");
			profChoice = input.nextLine();
			
			// MAKE RESERVATION
			if ("1".equals(profChoice)) {
				makeReservationLogic();
			} 
			// CANCEL RESERVATION
			else if ("2".equals(profChoice)) {
				cancelReservationLogic();
			} 
			// CHECK TIME-SLOT-STATUS
			else if ("3".equals(profChoice)) {
				checkTimeSlotsStatus();
			} 
			// CHECK AVAILABLE SLOTS IN A DAY
			else if ("4".equals(profChoice)) {
				checkAvailableSlotsInDay();
			} 
			// DISPLAY SCHEDULE ON DAY
			else if ("5".equals(profChoice)) {
				displayScheduleInDay();
			} 
			// VIEW 7 DAY CALENDAR
			else if ("6".equals(profChoice)) {
				viewSevenDayCalendar();
			} 
			// SAVE CURRENT CALENDAR TO JSON FILE
			else if ("7".equals(profChoice)) {
				saveSchedulerToJSON();
			}
			
			if ("exit".equalsIgnoreCase(profChoice)) {
				input.close();
				System.out.println(this.exitMessage);
				System.exit(1);
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		InteractiveConsoleApp app = new InteractiveConsoleApp(2023, 4);  // april of 2023
		Scanner sc = new Scanner(System.in);
		
		System.out.println(app.introductionOptions);
		
		String choice = "0";
		
		while (!choice.isEmpty()) {
			System.out.print("Your Choice (Hit ENTER to exit): ");
			choice = sc.nextLine();
			
			if ("1".equals(choice)) {
				System.out.println("\n");
				app.runProfessorCalendar();
			} else if ("2".equals(choice)) {
				System.out.println("Student's Calendar is Currently Down for Maintenance... Sorry!");
			} else if (choice.isEmpty()) {
				System.out.println("\nHave a Wonderful Day!");
			} else {
				System.out.println("Invalid Entry! Please Enter Approved Value from the Choice List Above Menu!");
			}
		}
		sc.close();
	}

}
