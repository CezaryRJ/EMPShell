import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DataBase {
	HashMap<String, Boolean> database = new HashMap<String, Boolean>();

	public void readData(String file) {
		Scanner inn = new Scanner(new File(file));

		try {
			// read file database
			int counter = 0;
			inn = new Scanner(new File("empdb.txt"));
			while (inn.hasNextLine()) {

				database.put(inn.nextLine(), true);
				counter++;

			}
			System.out.println(counter + " files in databse");

		} catch (FileNotFoundException e) {
			// scan all subfolders on first boot, or if db file is not found

			tmpArr = new ArrayList<>();
			tmpArr.add(currentPath);
			manager.runVoid.get("index").run(tmpArr);

		}

		/*
		 * HashMap<String, String> userSettings = new HashMap<String, String>();
		 * 
		 * try { //load user setting if present inn = new Scanner(new
		 * File("userSettings.txt")); String tmp;
		 * 
		 * String tmp2 = ""; int counter = 0;
		 * 
		 * while (inn.hasNext()) { tmp = inn.nextLine();
		 * 
		 * while (!tmp.substring(counter, counter + 1).equals(" ")) { counter++;
		 * } tmp2 = tmp.substring(counter + 1); tmp = tmp.substring(0, counter);
		 * 
		 * userSettings.put(tmp, tmp2);
		 * 
		 * }
		 * 
		 * System.out.println("User settings loaded"); } catch
		 * (FileNotFoundException e) {
		 * 
		 * System.out.println("No user setting found");
		 * 
		 * }
		 */
	}

}
