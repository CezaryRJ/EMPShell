import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DataBase implements runVoid {
	/*
	 * 0 - music 1 - images 2 - other
	 */
	HashMap<Integer, HashMap<String, Boolean>> dataBase = new HashMap<>();
	HashMap<Integer, String> labels = new HashMap<>();
	static int classes = 3;

	DataBase() {
		labels.put(0, "Music");
		labels.put(1, "Images");
		labels.put(2, "Other");
	}

	public void readData(String file) throws Exception {

		try {

			for (int i = 0; i < classes; i++) {
				// create more classes if needed
				if (dataBase.get(i) == null) {
					dataBase.put(i, new HashMap<String, Boolean>());
				}

			}

			// read file database
			Scanner scanner = new Scanner(new File(file));
			classes = Integer.parseInt(scanner.nextLine());
			for (int x = 0; x < classes; x++) {
				int classMembers = Integer.parseInt(scanner.nextLine());
				for (int i = 0; i < classMembers; i++) {
					dataBase.get(x).put(scanner.nextLine(), true);
				}

			}
			for (int i = 0; i < classes; i++) {

				System.out.println("Class " + labels.get(i) + " contains " + dataBase.get(i).size() + " files");
			}
			System.out.println("Files in databse : " + scanner.nextLine());
			scanner.close();

			System.out.println("Database file has been sucesfully read");

		} catch (FileNotFoundException e) {
			// scan all subfolders on first boot, or if db file is not found
			System.out.println("No databse file found, performing first time scan");
			index(Manager.path);
		}
		/*
		 * 
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

	public void index(String path) throws Exception {

		Timer timer = new Timer();
		timer.start();

		File[] listOfFiles = new File(path).listFiles();

		ArrayList<ArrayList<ArrayList<String>>> cache = new ArrayList<>();
		cache.add(new ArrayList<ArrayList<String>>());
		for (int y = 0; y < classes; y++) {
			cache.get(cache.size() - 1).add(new ArrayList<String>());
		}
		Crawler thisFolder = new Crawler(path, cache.get(0));

		ArrayList<Thread> crawler = new ArrayList<>();
		File tmp;
		int threads = 0;

		for (int i = 0; i < listOfFiles.length; i++) {
			tmp = new File(listOfFiles[i].getPath());

			if (tmp.isDirectory()) {
				System.out.println(tmp);
				cache.add(new ArrayList<ArrayList<String>>());
				for (int y = 0; y < classes; y++) {
					cache.get(cache.size() - 1).add(new ArrayList<String>());
				}
				crawler.add(new Thread(new Crawler(tmp.getAbsolutePath(), cache.get(cache.size() - 1))));

				threads++;

			} else { // denne sjekke filene i root mappen
				thisFolder.classifier.classify(tmp.getAbsolutePath());
			}

		}
		int maxThreads = 0;
		if (Runtime.getRuntime().availableProcessors() * 2 > crawler.size()) {
			maxThreads = crawler.size();
		} else {
			maxThreads = Runtime.getRuntime().availableProcessors() * 2;
		}

		int threadIndex = 0;
		for (int i = 0; i < maxThreads; i++) {
			// start first threads
			crawler.get(threadIndex).start();
			threadIndex++;
		}

		for (int i = 0; i < crawler.size(); i++) {
			// start first threads and wait so start new
			crawler.get(i).join();
			try {
				crawler.get(threadIndex).start();
			} catch (Exception e) {

			}
		}

		for (int i = 0; i < classes; i++) {
			// create more classes if needed
			if (dataBase.get(i) == null) {
				dataBase.put(i, new HashMap<String, Boolean>());
			}

		}

		// classes will be names 0,1,2,3 etc. 0 - music, 1- images and so on
		for (int b = 0; b < cache.size(); b++) {
			for (int c = 0; c < cache.get(b).size(); c++) {
				for (int d = 0; d < cache.get(b).get(c).size(); d++) {

					dataBase.get(c).put(cache.get(b).get(c).get(d), true);

				}
			}

		}

		int fileCounter = 0;
		for (int i = 0; i < classes; i++) {

			System.out.println("Class " + labels.get(i) + " contains " + dataBase.get(i).size() + " files");
			fileCounter += dataBase.get(i).size();
		}
		System.out.print(fileCounter + " files gathered in ");
		timer.stop();
		System.out.println("\n" + threads + " threads have been used for this task");

	}

	public void writeToFile() throws FileNotFoundException {
		System.out.println("Writing database to file");
		PrintWriter writer = new PrintWriter(new File("empdb.txt"));
		int counter = 0;
		writer.println(classes);
		for (int i = 0; i < dataBase.size(); i++) {
			writer.println(dataBase.get(i).size());
			counter += dataBase.get(i).size();
			for (String entry : dataBase.get(i).keySet()) {
				writer.println(entry);
			}
		}
		writer.println(counter);
		writer.close();
	}

	public ArrayList<String> tokenize(String inn, String limiter) {

		ArrayList<String> out = new ArrayList<>();
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

	@Override
	public void run(ArrayList<String> inn) throws Exception {
		index(Manager.path);

	}

	@Override
	public void help() {
		// TODO Auto-generated method stub

	}

}
