import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	// sort by name, size, ranking/popularity

	public static void main(String[] args) throws Exception {

		System.out.println("Welcome to EMPShell\nhttps://github.com/CezaryRJ");

		
		
		Manager manager = new Manager(System.getProperty("user.dir"));

		Recorder recorder = new Recorder();
		// Start loop

		ArrayList<String> tokens;
		String tmp;
		String raw;
		@SuppressWarnings("resource")
		Scanner inn = new Scanner(System.in);
		while (true) {

			tmp = inn.nextLine();
			raw = tmp;
			tokens = Manager.tokenize(tmp, " ");
			tmp = tokens.get(0);

			if (manager.runVoid.get(tmp) != null) {
				// try regular command
				tokens.remove(0);
				manager.runVoid.get(tmp).run(tokens);
				recorder.add(raw.trim());
			} else if (tokens.size() == 1) {

				try {
					// try to get command for history
					tokens = Manager.tokenize(recorder.getInput(Integer.parseInt(tokens.get(0))), " ");
					tmp = tokens.get(0);

					System.out.println("Executing command: " + recorder.getInput(Integer.parseInt(raw)));

					if (manager.runVoid.get(tmp) != null) {
						tokens.remove(0);
						manager.runVoid.get(tmp).run(tokens);

					}

				} catch (NumberFormatException e) {
					// get latest command
					tokens = Manager.tokenize(recorder.getLatest(), " ");
					System.out.println("Executing command: " + recorder.getLatest());
					tmp = tokens.get(0);
					if (manager.runVoid.get(tmp) != null) {
						tokens.remove(0);
						manager.runVoid.get(tmp).run(tokens);

					} else {

						System.out.println("No such command");

					}
				} catch (NullPointerException e) {
					System.out.println("No such command");

				}

			}

		}

	}
}
