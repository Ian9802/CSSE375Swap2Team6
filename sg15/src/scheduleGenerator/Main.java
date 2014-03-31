package scheduleGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class handles the interaction of one frame to another as well as
 * handling initialization.
 * 
 * @author Mason Schneider and Orion Martin. Created Oct 8, 2012.
 */
// SWAP 2, TEAM 6
// REFACTORING FOR ENHANCEMENT FROM BAD SMELL
// Smell fixes by changing to config handler class.
// SWAP 1, TEAM 5
// SMELL: Divergent Change - Main first of all shouldn't contain this many methods. 
// This class is trying to do too many things rather than simply 'running' the program.
public class Main {

	static ArrayList<Day> days;
	static ArrayList<Worker> workers;
    private static File standardPath = new File("schedule_data.ser");
    private static File path;
	
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
        // SWAP 2, TEAM 06
        // BONUS FEATURE / FURTHER ELABORATION (Our group didn't do an additional feature last time so we added one)
        // Now have the option to load a schedule from file via the command line
        // Before this was a hard coded value, which helped lead to code smell.
        //path = new File("schedule_data.ser");
        config = new Config();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter schedule file name:");
        String input = scanner.next();
        scanner.close();
        if(input.length() >=5){
            System.out.println(input.substring(input.length()-5, input.length()));
        }
        if (input == null || input.isEmpty()){
            path = standardPath;
        }
        else if(input.length() >=5){
            if(input.substring(input.length()-5, input.length()).equals(".ser")){
                path = new File(input);
            }
            else{
                path = new File(input + ".ser");
            }
        }
        else{
            path = new File(input + ".ser");
        }
        // Code to open the config file.

        try {
            conH.recallConfigFile(path);
            if (getSchedule() != (null)) {
                cal = new CalendarGUI(getSchedule());
                cal.setVisible(true);
            } else {
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
