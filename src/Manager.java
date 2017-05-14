import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Manages all modules under a 100 line limit
 * 
 * @author Cezary
 *
 */
public class Manager {

	String path;

	HashMap<String, runVoid> runVoid = new HashMap<>();

	Manager(String path) {
		this.path = path;

		runVoid.put("dir", new ls());
		runVoid.put("credits", new credits());
		runVoid.put("opendir", new openFolder());
		runVoid.put("del", new delete());
		runVoid.put("index", new indexer());
		runVoid.put("help", new help());
		runVoid.put("exit", new exit());
		runVoid.put("start", new open());
		runVoid.put("random", new random());
		runVoid.put("goto", new goToc());
		runVoid.put("cd", new enterFolder());
		runVoid.put("mix", new PartyMixer(this));
	}

	/**
	 * @param inn
	 *            String to be tokenized
	 * @param limiter
	 *            Where it string will be split
	 * @return an array list of tokens
	 */
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

	/**
	 * A module that prints a random number given an input
	 * 
	 * @author Cezary
	 *
	 */
	class random implements runVoid {

		public void run(ArrayList<String> inn) {
			int size = inn.size();
			int random;

			if (size == 0) {
				random = (int) (Math.random() * 10000);
				System.out.println(random);
			} else if (size == 1) {
				random = (int) (Math.random() * Integer.parseInt(inn.get(0)));
				System.out.println(random);
			} else {
				random = (int) (Math.random() * (Integer.parseInt(inn.get(0)) - Integer.parseInt(inn.get(1)))
						+ Integer.parseInt(inn.get(1)));
				System.out.println(random);

			}

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
	class ls implements runVoid {

		public void run(ArrayList<String> inn) {
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

		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * prints credits
	 * 
	 * @author Cezary
	 *
	 */
	class credits implements runVoid {

		@Override
		public void run(ArrayList<String> inn) {
			System.out.println("Created by Cezary Radoslaw Jaksula\nhttps://github.com/CezaryRJ");

		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Open curernt path in windows explorer
	 * 
	 * @author Cezary
	 *
	 */
	class openFolder implements runVoid {

		@Override
		public void run(ArrayList<String> inn) {
			try {
				Desktop.getDesktop().open(new File(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Removes a given file from the current directory, this is final and cannot
	 * be undone
	 * 
	 * @author Cezary
	 *
	 */

	class delete implements runVoid {

		@Override
		public void run(ArrayList<String> inn) {

			File tmp;
			for (int i = 2; i < inn.size(); i++) {

				tmp = new File(inn.get(1) + "\\" + inn.get(i));
				if (tmp.delete()) {
					System.out.println("File " + tmp.getName() + " has been deleted");
				} else {
					System.out.println("File " + tmp.getName() + " could not be deleted");
				}
			}
		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	class indexer implements runVoid {

		@Override
		public void run(ArrayList<String> inn) throws Exception {

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

		@Override
		public void help() {
			System.out.println("Indexes the current directry, as well as any subdirectory");

		}

	}

	/**
	 * If the clpboard contains a valid path, the shall will to that directory
	 * 
	 * @author Cezary
	 *
	 */

	class goToc implements runVoid {

		public void run(ArrayList<String> inn) {

			try {
				inn = new ArrayList<>();
				inn.add((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
				runVoid.get("cd").run(inn);

			} catch (Exception e) {
				System.out.println("No such directory");
			}

		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Prints instructions for all, or a givne module
	 * 
	 * @author Cezary
	 *
	 */
	class help implements runVoid {

		public void run(ArrayList<String> inn) throws Exception {

			try {
				runVoid.get(inn.get(0)).help();
			} catch (Exception e) {
				for (Entry<String, runVoid> entry : runVoid.entrySet()) {
					entry.getValue().help();
					System.out.println();
				}

			}

		}

		public void help() {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Opens a file with the default program in windows
	 * 
	 * @author Cezary
	 *
	 */

	class open implements runVoid {

		@Override
		public void run(ArrayList<String> inn) throws Exception {

			for (int i = 0; i < inn.size(); i++) {
				// System.out.println("test");
				try {
					if (new File(path + "\\" + inn.get(i)).exists()) {
						Desktop.getDesktop().open(new File(path + "\\" + inn.get(i)));
					} else {
						System.out.println("Failed to open file \\" + inn.get(i) + " file does not exist");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Terminates the shell
	 * 
	 * @author Cezary
	 *
	 */

	class exit implements runVoid {

		@Override
		public void run(ArrayList<String> inn) throws Exception {
			System.exit(0);

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

	class enterFolder implements runVoid {

		@Override
		public void run(ArrayList<String> inn) {

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
				path = exitFolder(path);
			}
		}

		/**
		 * Exits the current folder, and enters a previous one
		 * 
		 * @param path
		 *            - Current directory path
		 * @return
		 */
		public String exitFolder(String path) {

			ArrayList<String> tmp = tokenize(path, "\\");

			String out = tmp.get(0);
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
