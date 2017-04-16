import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

	final static int coreCount = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws Exception {

		System.out.println("Welcome to EMPShell\nhttps://github.com/CezaryRJ");
		String currentPath = System.getProperty("user.dir");

		Scanner inn;
		Manager manager = new Manager();

		HashMap<String, Boolean> database;
		HashMap<String, String> userSettings = new HashMap<String, String>();
		try {
			// read file database
			int counter = 0;
			database = new HashMap<String, Boolean>();
			inn = new Scanner(new File("files.txt"));
			while (inn.hasNextLine()) {

				database.put(inn.nextLine(), true);
				counter++;

			}
			System.out.println(counter + " files in databse");

		} catch (FileNotFoundException e) {
			// scan all subfolders on first boot

			manager.indexer(currentPath);

		}

		try {
			inn = new Scanner(new File("userSettings.txt"));
			String tmp;

			String tmp2 = "";
			int counter = 0;

			while (inn.hasNext()) {
				tmp = inn.nextLine();

				while (!tmp.substring(counter, counter + 1).equals(" ")) {
					counter++;
				}
				tmp2 = tmp.substring(counter + 1);
				tmp = tmp.substring(0, counter);

				userSettings.put(tmp, tmp2);

			}

			System.out.println("User settings loaded");
		} catch (FileNotFoundException e) {
			// scan all subfolders on first boot

			System.out.println("No user setting found");

		}

		ArrayList<String> tokens = new ArrayList<>();
		inn = new Scanner(System.in);
		tokens = manager.tokenize(inn.nextLine(), " ");

		boolean userCmd = false;
		while (true) {

			if (tokens.get(0).equals("ls")) {
				manager.listFiles(currentPath);

			} else if (tokens.get(0).equals("cd")) {
				if (tokens.size() == 1) {
					currentPath = manager.exitFolder(currentPath);
				} else {

					currentPath = manager.enterFolder(currentPath, tokens.get(1));
				}

			} else if (tokens.get(0).equals("exe")) {
				manager.open(currentPath, tokens);
			} else if (tokens.get(0).equals("help")) {
				manager.help();
			} else if (tokens.get(0).equals("goto")) {
				if (tokens.size() == 1) {
					String tmp = manager.goToc();
					if (new File(tmp).isDirectory()) {
						currentPath = manager.goToc();
						System.out.println(currentPath);
					} else {
						System.out.println("No such directory");
					}

				} else {
					if (new File(tokens.get(1)).exists()) {
						currentPath = tokens.get(1);
						System.out.println(currentPath);

					} else {
						System.out.println("No such directory");
					}
				}
			} else if (tokens.get(0).equals("index")) {
				manager.indexer(currentPath);
			} else if (tokens.get(0).equals("delete")) {
				manager.delete(currentPath, tokens);
			} else if (tokens.get(0).equals("opendir")) {
				manager.openFolder(currentPath);
			} else if (tokens.get(0).equals("credits")) {
				manager.credits();
			} else if (tokens.get(0).equals("exit")) {
				System.exit(0);
			}

			else {
				userCmd = true;
			}

			if (userCmd == false) {
				tokens = manager.tokenize(inn.nextLine(), " ");
			} else {
				tokens = manager.tokenize(userSettings.get(tokens.get(0)), " ");
				userCmd = false;
			}

		}

	}

}
