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
		//only print if there is a value at the end, end if it is worth printing
		if (tmp != null && ((tmp instanceof String) || (tmp instanceof Double) || (tmp instanceof Integer))) {

			System.out.println(tmp);
		}

	}

}
