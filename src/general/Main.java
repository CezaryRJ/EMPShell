package general;
import java.util.ArrayList;
import java.util.Scanner;

import evaluator.CommandEvaluator;

public class Main {

	// sort by name, size, ranking/popularity

	public static void main(String[] args) throws Exception {

		System.out.println("Welcome to EMPShell\nhttps://github.com/CezaryRJ\nLoading database . . . .");

		
		
		Manager manager = new Manager(System.getProperty("user.dir"));
		
		CommandEvaluator evaluator = new CommandEvaluator(manager);

		
		Recorder recorder = new Recorder();
		// Start loop

		ArrayList<Object> tokens;
		Object tmp;
	
		@SuppressWarnings("resource")
		Scanner inn = new Scanner(System.in);
		
		
		while (true) {

			tmp = inn.nextLine();
			tokens = Manager.tokenize((String)tmp, " ");
			tmp = tokens.get(0);
			tokens.add(":");
			
		
		
			if (manager.isFunction((String) tmp)) {
			
				recorder.setCommand(tokens);
				recorder.print();
				evaluator.parse(tokens, manager);
				
			} else if (tokens.get(0).equals("") ) {

				evaluator.parse(recorder.getCommand(), manager);

			}else {
				System.out.println("No such command");
			}

		}

	}
}
