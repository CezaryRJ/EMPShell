package general;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import math.Add;
import search.QueryEvaluator;

/**
 * Manages all modules under a 100 line limit
 * 
 * @author Cezary
 *
 */
public class Manager {

	static String path;

	static String openDir;

	public HashMap<String, runVoid> runVoid = new HashMap<>();

	DataBase database = new DataBase();
	QueryEvaluator evaluator = new QueryEvaluator(database);

	Manager(String path) throws Exception {
		Manager.path = path;
		openDir = path;
		database.readData("empdb.txt");
		
		runVoid.put("+", new Add());
		runVoid.put("s", evaluator);
		runVoid.put("dir", new ls());
		runVoid.put("credits", new credits());
		runVoid.put("opendir", new openFolder());
		runVoid.put("del", new delete());
		runVoid.put("index", database);
		runVoid.put("help", new helper());
		runVoid.put("exit", new exit());
		runVoid.put("start", new open());
		runVoid.put("random", new random());
		runVoid.put("goto", new goToc());
		runVoid.put("cd", new enterFolder());
		runVoid.put("mix", new PartyMixer(this));
	}
	
	public boolean isFunction(String inn){
		if(runVoid.containsKey(inn)){
			return true;
		}
		return false;
	}

	/**
	 * @param inn
	 *            String to be tokenized
	 * @param limiter
	 *            Where it string will be split
	 * @return an array list of tokens
	 */
	public static ArrayList<Object> tokenize(String inn, String limiter) {

		ArrayList<Object> out = new ArrayList<>();
		int counter = 0;

		for (int i = 0; i < inn.length() - 1; i++) {

			if (inn.substring(i, (i + 1)).equals(limiter)) {

				out.add(inn.substring(counter, i));
				counter = (i + 1);
			}
		}
		out.add(inn.substring(counter, inn.length()));

		return out;
	}

	/**
	 * A module that prints a random number given an input
	 * 
	 * @author Cezary
	 *
	 */
	class random extends runVoid {

		public Object run(List<Object> inn) {
			int size = inn.size();
			int random;

			if (size == 0) {
				random = (int) (Math.random() * 10000);
				System.out.println(random);
			} else if (size == 1) {
				random = (int) (Math.random() * Integer.parseInt((String) inn.get(0)));
				System.out.println(random);
			} else {
				random = (int) (Math.random() * (Integer.parseInt((String) inn.get(0)) - Integer.parseInt((String) inn.get(1)))
						+ Integer.parseInt((String) inn.get(1)));
				System.out.println(random);

			}
return null;
		}

		public void help() {
			System.out.println("random - print a random number up to 100000\n"
					+ "random <number> - print a random number up to the given number\n"
					+ "random <number1><number2> - print a random number between number1 and number2");

		}
	}

	/**
	 * Lists all files in a directory, used my the "dir" command
	 * 
	 * @author Cezary
	 *
	 */
	class ls extends runVoid {

		public Object run(List<Object> inn) {
			File[] listOfFiles = new File(path).listFiles();
			ArrayList<String> files = new ArrayList<>();
			ArrayList<String> folders = new ArrayList<>();
			Path tmp;
			for (int i = 0; i < listOfFiles.length; i++) {
				tmp = Paths.get(listOfFiles[i].getAbsolutePath());
				if (listOfFiles[i].isDirectory()) {
					folders.add(tmp.getFileName().toString());
				} else {
					files.add(tmp.getFileName().toString());
				}

			}
			for (int i = 0; i < folders.size(); i++) {
				System.out.println("\\" + folders.get(i));

			}
			for (int i = 0; i < files.size(); i++) {
				System.out.println(files.get(i));

			}
			return null;
		}

		
	}

	/**
	 * prints credits
	 * 
	 * @author Cezary
	 *
	 */
	class credits extends runVoid {

		@Override
		public Object run(List<Object> inn) {
			System.out.println("Created by Cezary Radoslaw Jaksula\nhttps://github.com/CezaryRJ");
			return null;
		}

		

	}

	/**
	 * Open curernt path in windows explorer
	 * 
	 * @author Cezary
	 *
	 */
	class openFolder extends runVoid {

		@Override
		public Object run(List<Object> inn) {
			try {
				Desktop.getDesktop().open(new File(path));
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			return null;
		}


	}

	/**
	 * Removes a given file from the current directory, this is final and cannot
	 * be undone
	 * 
	 * @author Cezary
	 *
	 */

	class delete extends runVoid {

		@Override
		public Object run(List<Object> inn) {

			File tmp;
			for (int i = 2; i < inn.size(); i++) {

				tmp = new File(inn.get(1) + "\\" + inn.get(i));
				if (tmp.delete()) {
					System.out.println("File " + tmp.getName() + " has been deleted");
				} else {
					System.out.println("File " + tmp.getName() + " could not be deleted");
				}
			}
			return null;
		}

		

	}

	/**
	 * If the clpboard contains a valid path, the shall will to that directory
	 * 
	 * @author Cezary
	 *
	 */

	class goToc extends runVoid {

		public Object run(List<Object> inn) throws Exception {
			path = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			if (new File(path).isDirectory()) {

				System.out.println(path);

			} else {
				System.out.println("No such directory");
			}
return null;
		}

		

	}

	/**
	 * Prints instructions for all, or a givne module
	 * 
	 * @author Cezary
	 *
	 */
	class helper extends runVoid {

		public Object run(List<Object> inn) throws Exception {

			try {
				runVoid.get(inn.get(0)).help();
			} catch (Exception e) {
				Iterator<String> iterator = runVoid.keySet().iterator();
				while(iterator.hasNext()){
					runVoid.get(iterator.next()).help();;
				}

			}
			return null;
		}

		

	}

	/**
	 * Opens a file with the default program in windows
	 * 
	 * @author Cezary
	 *
	 */

	class open extends runVoid {

		@Override
		public Object run(List<Object> inn) throws Exception {

			for (int i = 0; i < inn.size(); i++) {
		
				try {
					if (new File(path + "\\" + inn.get(i)).exists()) {
						Desktop.getDesktop().open(new File(path + "\\" + inn.get(i)));
					} else {
						System.out.println("Failed to open file \\" + inn.get(i) + " file does not exist");
					}
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			return null;
		}


	}

	/**
	 * Terminates the shell
	 * 
	 * @author Cezary
	 *
	 */

	class exit extends runVoid {

		@Override
		public Object run(List<Object> inn) throws Exception {

			database.writeToFile();
			System.out.println("Exiting - Have a nice day");
			System.exit(0);
			return null;

		}

		@Override
		public void help() {
			System.out.println("exit - Closes the shell");

		}

	}

	/**
	 * Enters a folder given a directory
	 * 
	 * @author Cezary
	 *
	 */

	class enterFolder extends runVoid {

		@Override
		public Object run(List<Object> inn) {

			if (inn.size() > 0) {

				String tmp = path + "\\" + inn.get(0);
				File tmpf = new File(tmp);
				System.out.println(tmp);
				if (tmpf.exists() && tmpf.isDirectory()) {

					path = tmp;
				} else {
					System.out.println("Invalid path name");

				}
				// if no input is given, exit current directory
			} else {
				path = (String) exitFolder(path);
			}
			return null;
		}

		/**
		 * Exits the current folder, and enters a previous one
		 * 
		 * @param path
		 *            - Current directory path
		 * @return
		 */
		public Object exitFolder(String path) {

			ArrayList<Object> tmp = tokenize(path, "\\");

			Object out = tmp.get(0);
			for (int i = 1; i < tmp.size() - 1; i++) {
				out = out + "\\" + tmp.get(i);
			}
			System.out.println(out);
			return out;
		}

		public String getPath() {
			return path;
		}

		@Override
		public void help() {
			System.out.println("cd - Enters a given folder");

		}
	}

}
