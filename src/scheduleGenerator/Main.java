package scheduleGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

/**
 * This class handles the interaction of one frame to another as well as
 * handling initialization.
 * 
 * @author Mason Schneider and Orion Martin. Created Oct 8, 2012.
 */
public class Main {

	private static ArrayList<Day> days;
	private static ArrayList<Worker> workers;
	private static File standardPath = new File("schedule_data.ser");
	private static File path;
	/**
	 * Configures days.
	 */
	static Config config;
	/**
	 * Configures workers.
	 */
	static WorkerSetup wSet;
	/**
	 * Displays schedule.
	 */
	static CalendarGUI cal;
	private static Schedule schedule;

	/**
	 * Program starts here.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		fileChange();
		
		Schedule localSchedule = getSchedule();
		//Code to open the config file.
		
		// SWAP 3, TEAM 6
		// There was a big issue in how this was being ran that allowed for null values to be passed in and cause the program to crash.
		// (honestly not sure what was the cause in here, but I fixed it)
		try {
			recallConfigFile();
			if(localSchedule == null){
				config.setVisible(true);
			}else{
				cal = new CalendarGUI(getSchedule());
				cal.setVisible(true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			
		}
	}

	public static void fileChange() {
		// SWAP 3, TEAM 6
		// Added a gui pop up that allows for using schedule files other
		// than the standard schedule_data.ser
		JFrame frame = new JFrame();
		String text = (String)JOptionPane.showInputDialog(frame,
				"Enter schedule name:");

		config = new Config();
		if(text.length() >= 5){
			System.out.println(text.substring(text.length()-5, text.length()));
		}
		if(text == null || text.isEmpty()){
			path = standardPath;
		}
		else if(text.length() >= 5 && text.substring(text.length()-5, text.length()).equals(".ser")){
			path = new File(text);
		}else{
			path = new File(text + ".ser");
		}
	}

	/**
	 * Changes visible of config.
	 * 
	 */
	public static void toggleConfig() {
		config.setVisible(!config.isVisible());
	}

	/**
	 * Changes visible of calendar.
	 * 
	 */
	public static void toggleCalendar() {
		cal.setVisible(!cal.isVisible());
	}

	/**
	 * Changes visible of worker setup.
	 * 
	 */
	public static void toggleWorkerSetup() {
		if (wSet != null) {
			wSet.setVisible(!wSet.isVisible());
		}
	}

	/**
	 * Returns the value of the field called 'schedule'.
	 * 
	 * @return Returns the schedule.
	 */
	public static Schedule getSchedule() {
		return Main.schedule;
	}

	/**
	 * Sets the field called 'schedule' to the given value.
	 * 
	 * @param schedule
	 *            The schedule to set.
	 */
	public static void setSchedule(Schedule schedule) {
		Main.schedule = schedule;
	}

	/**
	 * Sets the value of workers.
	 * 
	 * @return workers
	 */
	public static ArrayList<Worker> getWorkers() {
		return workers;
	}

	/**
	 * Sets workers.
	 * 
	 * @param w
	 */
	public static void setWorkers(ArrayList<Worker> w) {
		workers = w;
	}

	/**
	 * Returns the value of the field called 'days'.
	 * 
	 * @return Returns the days.
	 */
	public static ArrayList<Day> getDays() {
		return days;
	}

	/**
	 * Sets the field called 'days' to the given value.
	 * 
	 * @param d
	 */
	public static void setDays(ArrayList<Day> d) {
		days = d;
	}
	
	/**
	 * Dumps data to the file schedule_data.ser.
	 *
	 */
	public static void dumpConfigFile(){
		
		try {
			path.delete();
			path.createNewFile();
			FileOutputStream dumpConfig = new FileOutputStream(path);
			ObjectOutputStream fileStore = new ObjectOutputStream(dumpConfig);
			fileStore.writeObject(days);
			fileStore.writeObject(workers);
			fileStore.writeObject(schedule);
			fileStore.writeObject(HTMLGenerator.getTables());
			fileStore.close();
			dumpConfig.close();
			
			System.out.println("Stored");

		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Recalls data from schedule_data.ser.
	 *
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void recallConfigFile() throws ClassNotFoundException, IOException{
		if(path.exists()) {
			FileInputStream recallConfig = new FileInputStream(path);
			ObjectInputStream fileRecall = new ObjectInputStream(recallConfig);
			days = (ArrayList<Day>) fileRecall.readObject();
			workers = (ArrayList<Worker>) fileRecall.readObject();
			schedule = (Schedule) fileRecall.readObject();
			HTMLGenerator.setTables((String)fileRecall.readObject());
			
			fileRecall.close();
			recallConfig.close();
		}
	}
}
