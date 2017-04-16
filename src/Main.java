import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		System.out.println("Welcome to EMPShell");
		String currentPath = System.getProperty("user.dir");
		Timer timer = new Timer();
		Scanner inn;
		Manager manager = new Manager();
		timer.start();
		final int coreCount = Runtime.getRuntime().availableProcessors();

		try {
			// read file database
			int counter = 0;
			HashMap<String, Boolean> database = new HashMap<String, Boolean>();
			inn = new Scanner(new File("files.txt"));
			while (inn.hasNextLine()) {

				database.put(inn.nextLine(), true);
				counter++;

			}
			System.out.println(counter + " files in databse");

		} catch (FileNotFoundException e) {
			// scan all subfolders on first boot

			PrintWriter writer = new PrintWriter("files.txt", "UTF-8");
			File[] listOfFiles = new File(currentPath).listFiles();

			ArrayList<ArrayList<String>> cache = new ArrayList<>();
			cache.add(new ArrayList<String>());

			Thread[] crawler = new Thread[coreCount];

			File tmp;

			for (int i = 0; i < listOfFiles.length; i++) {
				tmp = new File(listOfFiles[i].getPath());

				if (tmp.isDirectory()) {
					for (int x = 0; x < crawler.length; x++) {
						if (crawler[x] == null || !crawler[x].isAlive()) {
							cache.add(new ArrayList<String>());
							crawler[x] = new Thread(new Crawler(tmp.getAbsolutePath(), cache.get(cache.size() - 1)));
							crawler[x].start();
							break;
						}
					}

				} else {
					cache.get(0).add(tmp.getAbsolutePath());
				}

			}
			for (int i = 0; i < crawler.length; i++) {
				if (crawler[i] != null) {
					crawler[i].join();

				}

			}
			int counter = 0;
			for (int i = 0; i < cache.size(); i++) {
				ArrayList<String> tmpArr = cache.get(i);
				for (int x = 0; x < cache.get(i).size(); x++) {
					writer.println(tmpArr.get(x));
					counter++;

				}

			}
			writer.close();
			System.out.print(counter + " files gathered in ");

		}
		timer.stop();

		ArrayList<String> tokens = new ArrayList<>();
		inn = new Scanner(System.in);
		while (true) {
			tokens = manager.tokenize(inn.nextLine(), " ");

			if (tokens.get(0).equals("ls")) {
				manager.listFiles(currentPath);

			}
			if (tokens.get(0).equals("cd")) {
				if (tokens.size() == 1) {
					currentPath = manager.exitFolder(currentPath);
				} else {

					currentPath = manager.enterFolder(currentPath, tokens.get(1));
				}

			} else if (tokens.get(0).equals("exit")) {
				System.exit(0);
			}

		}

	}

}
