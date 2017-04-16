import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		System.out.println("Welcome to EMPShell");
		String currentPath = System.getProperty("user.dir");
		
		Scanner inn;
		Manager manager = new Manager();
		
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

			manager.indexer(currentPath);

		}
		
		ArrayList<String> tokens = new ArrayList<>();
		inn = new Scanner(System.in);
		while (true) {
			tokens = manager.tokenize(inn.nextLine(), " ");

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
			}
			else if(tokens.get(0).equals("index")){
				manager.indexer(currentPath);
			}
			else if (tokens.get(0).equals("delete")){
				manager.delete(currentPath,tokens);
			}
			else if (tokens.get(0).equals("opendir")){
				manager.openFolder(currentPath);
			}
			else if (tokens.get(0).equals("credits")){
				manager.credits();
			}

			else if (tokens.get(0).equals("exit")) {
				System.exit(0);
			}

		}

	}

}
