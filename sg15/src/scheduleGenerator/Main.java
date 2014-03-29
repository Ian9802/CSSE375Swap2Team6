package scheduleGenerator;

import java.io.File;
import java.util.ArrayList;

/**
 * This class handles the interaction of one frame to another as well as
 * handling initialization.
 * 
 * @author Mason Schneider and Orion Martin. Created Oct 8, 2012.
 */
// SWAP 2, TEAM 6
// Smell fixes by changing to config handler class.
// SWAP 1, TEAM 5
// SMELL: Divergent Change - Main first of all shouldn't contain this many methods. 
// This class is trying to do too many things rather than simply 'running' the program.
public class Main {

	static ArrayList<Day> days;
	static ArrayList<Worker> workers;
	static File path = new File("schedule_data.ser");
	
	// Configures days.
	static Config config = new Config();
	//Configures workers.
	static WorkerSetup wSet;
	//Displays schedule.
	static CalendarGUI cal;
	static ConfigHandler conH = new ConfigHandler();
	private static Schedule schedule;
	

	/**
	 * Program starts here.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//path = new File("schedule_data.ser");
		//config = new Config();
		
		//Code to open the config file.
		
		try {
			conH.recallConfigFile(path, days, workers, schedule);
			if(getSchedule() != (null)){
				cal = new CalendarGUI(getSchedule());
				//config.setVisible(true);
				cal.setVisible(true);
			} else{
				config.setVisible(true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			
		}
	}

	//Changes visible of config.

	public static void toggleConfig() {
		config.setVisible(!config.isVisible());
	}

	//Changes visible of calendar.

	public static void toggleCalendar() {
		cal.setVisible(!cal.isVisible());
	}

	//Changes visible of worker setup.

	public static void toggleWorkerSetup() {
		if (wSet != null) {
			wSet.setVisible(!wSet.isVisible());
		}
	}

	/**
	 * Returns the value of the field called 'schedule'.
	 * @return Returns the schedule.
	 */
	public static Schedule getSchedule() {
		return Main.schedule;
	}

	/**
	 * Sets the field called 'schedule' to the given value.
	 * @param schedule The schedule to set.
	 */
	public static void setSchedule(Schedule schedule) {
		Main.schedule = schedule;
	}

	/**
	 * Sets the value of workers.
	 * @return workers
	 */
	public static ArrayList<Worker> getWorkers() {
		return workers;
	}

	/**
	 * Sets workers.
	 * @param w
	 */
	public static void setWorkers(ArrayList<Worker> w) {
		workers = w;
	}

	/**
	 * Returns the value of the field called 'days'.
	 * @return Returns the days.
	 */
	public static ArrayList<Day> getDays() {
		return days;
	}

	/**
	 * Sets the field called 'days' to the given value.
	 * @param d
	 */
	public static void setDays(ArrayList<Day> d) {
		days = d;
	}
	
	public static void dumpConfigFile(){
	conH.dumpConfigFile(path, days, workers, schedule);
	}
}
