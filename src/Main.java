import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	// sort by name, size, ranking/popularity

	public static void main(String[] args) throws Exception {

		System.out.println("Welcome to EMPShell\nhttps://github.com/CezaryRJ");

		Manager manager = new Manager(System.getProperty("user.dir"));

		// Start loop
		Recorder recorder = new Recorder();

		recorder.add("a");
		recorder.add("b");
		recorder.add("c");
		recorder.add("d");
		recorder.add("e");

		System.out.println(recorder.printUp());
		System.out.println(recorder.printUp());
		System.out.println(recorder.printUp());
		System.out.println(recorder.printUp());
		
		System.out.println(recorder.printDown());
		System.out.println(recorder.printDown());
		
	

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
