package scheduleGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// SWAP 2, TEAM 6
// Now inputting and outputing the Config class to a file, has been factored out into it's own class.
// Could in the future had different ways tho save the config as different ConfigHandlers
public class ConfigHandler {
	/**
	 * Dumps data to the file schedule_data.ser.
	 *
	 */

	public void dumpConfigFile(File path, ArrayList<Day> days,
			ArrayList<Worker> workers, Schedule schedule){
		
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
	public void recallConfigFile(File path)
					throws ClassNotFoundException, IOException{
		if(path.exists()) {
			FileInputStream recallConfig = new FileInputStream(path);
			ObjectInputStream fileRecall = new ObjectInputStream(recallConfig);
            ArrayList<Day> days = (ArrayList<Day>) fileRecall.readObject();
            ArrayList<Worker> workers = (ArrayList<Worker>) fileRecall.readObject();
            Schedule schedule = (Schedule) fileRecall.readObject();
			HTMLGenerator.setTables((String)fileRecall.readObject());
			
			fileRecall.close();
			recallConfig.close();
			Main.setDays(days);
			Main.setWorkers(workers);
			Main.setSchedule(schedule);
		}
	}


}
