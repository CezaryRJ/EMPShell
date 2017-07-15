package evaluator;

import java.util.ArrayList;
import java.util.List;

import general.Manager;

public class Value {

	List<Object> args;
	Manager manager;
	String func;

	Value(Manager manager) {
		this.manager = manager;
		args = new ArrayList<>();

	}

	public void parse(List<Object> inn) {

		func = inn.get(0).toString();
	
		inn.remove(0);
		inn.remove(inn.size() - 1);
		for (int i = 0; i < inn.size(); i++) {
			if (manager.isFunction(inn.get(i).toString())) {
				List<Object> ting = new ArrayList<>();

				int counter = 1;
				ting.add(inn.get(i));
				while (counter != 0) {
					i++;
					if (inn.get(i).toString().equals(":")) {
						counter--;
					} else if (manager.isFunction(inn.get(i).toString())) {

						counter++;
					}

					ting.add(inn.get(i));

				}
				args.add(new Value(manager));
		
			
				((Value) args.get(args.size() - 1)).parse(ting);
				
			} else {
				
				args.add(inn.get(i));
			}

		}
	}

	public Object evaluate() throws Exception {
		for (int i = 0; i < args.size(); i++) {
			if (args.get(i) instanceof Value) {
			args.set(i,((Value) args.get(i)).evaluate());
			}

		}
		return manager.runVoid.get(func).run(args);
	}

}
