package evaluator;

import java.util.List;

import general.Manager;

public class CommandEvaluator {

	Manager manager;
	Value root;
	Object tmp;

	public CommandEvaluator(Manager manager) {
		this.manager = manager;
	}

	public void parse(List<Object> inn, Manager manager) throws Exception {
		root = new Value(manager);
		root.parse(inn);
		tmp = root.evaluate();
		if (tmp != null) {
			System.out.println(tmp);
		}

	}

}
