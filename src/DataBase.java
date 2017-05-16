import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DataBase {
	HashMap<String, Boolean> audio = new HashMap<String, Boolean>();
	HashMap<String, Boolean> image = new HashMap<String, Boolean>();
	HashMap<String, Boolean> other = new HashMap<String, Boolean>();

	public void readData(String file) {
		/*
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

			index(System.getProperty("user.dir"));
		}
		*/

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

	public void index(String path) throws Exception {

		Timer timer = new Timer();
		timer.start();
		// must cut of first directory

		ArrayList<String> pathTokens = tokenize(path, "\\");

		String out = pathTokens.get(0);
		for (int i = 1; i < pathTokens.size(); i++) {
			out = out + "\\" + pathTokens.get(i);
		}

		System.out.println(out + "\\empdb.txt");
		PrintWriter writer = new PrintWriter(out + "\\empdb.txt", "UTF-8");

		File[] listOfFiles = new File(path).listFiles();

		ArrayList<ArrayList<ArrayList<String>>> cache = new ArrayList<>();
		cache.add(new ArrayList<ArrayList<String>>());
		for (int y = 0; y < 3; y++) {
			cache.get(cache.size() - 1).add(new ArrayList<String>());
		}
		Crawler thisFolder = new Crawler(path, cache.get(0));

		Thread[] crawler = new Thread[Runtime.getRuntime().availableProcessors()];

		File tmp;
		int threads = 0;

		for (int i = 0; i < listOfFiles.length; i++) {
			tmp = new File(listOfFiles[i].getPath());

			if (tmp.isDirectory()) {
				for (int x = 0; x < crawler.length; x++) {

					if (crawler[x] == null || !crawler[x].isAlive()) {
						cache.add(new ArrayList<ArrayList<String>>());
						for (int y = 0; y < 3; y++) {
							cache.get(cache.size() - 1).add(new ArrayList<String>());
						}
						crawler[x] = new Thread(new Crawler(tmp.getAbsolutePath(), cache.get(cache.size() - 1)));
						crawler[x].start();
						threads++;
						break;
					}
				}

			} else {
				// denne sjekke filene i root mappen
				thisFolder.classifier.classify(tmp.getAbsolutePath());
			}

		}
		for (int i = 0; i < crawler.length; i++) {
			if (crawler[i] != null) {

				crawler[i].join();

			}

		}

		int counter = 0;
		int fileCounter = 0;
		// legg sammen resultater

		for (int i = 0; i < 3; i++) {

			for (int x = 0; x < cache.size(); x++) {
				counter += cache.get(x).get(i).size();

			}
			System.out.println(counter);
			writer.println(counter);

			for (int x = 0; x < cache.size(); x++) {
				for (int y = 0; y < cache.get(x).get(i).size(); y++) {
					writer.println(cache.get(x).get(i).get(y));

				}

			}

			fileCounter += counter;
			counter = 0;
		}

		/*
		 * for (int b = 0; b < cache.size(); b++) { for (int c = 0; c <
		 * cache.get(b).size(); c++) { for (int d = 0; d <
		 * cache.get(b).get(c).size(); d++) {
		 * writer.println(cache.get(b).get(c).get(d)); counter++; } }
		 * 
		 * counter = 0;
		 * 
		 * }
		 */
		writer.print(fileCounter);
		writer.close();
		System.out.print(fileCounter + " files gathered in ");
		timer.stop();
		System.out.println("\n" + threads + " threads have been used for this task");

	}
	public void writeToFile(){
		
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

}
