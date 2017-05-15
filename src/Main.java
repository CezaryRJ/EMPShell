import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	// sort by name, size, ranking/popularity

	final static int coreCount = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws Exception {

		System.out.println("Welcome to EMPShell\nhttps://github.com/CezaryRJ");
		String currentPath = System.getProperty("user.dir");

		

		Manager manager = new Manager(currentPath);

		// Start loop
		ArrayList<String> tokens;
		String tmp;
		@SuppressWarnings("resource")
		Scanner inn = new Scanner(System.in);
		while (true) {

			
			tokens = manager.tokenize(inn.nextLine(), " ");
			tmp = tokens.get(0);

			if (manager.runVoid.get(tmp) != null) {
				tokens.remove(0);

				manager.runVoid.get(tmp).run(tokens);
			}

			else {
				System.out.println("No such Command");
			}

		}
		
		
	}
}
