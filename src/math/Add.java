package math;

import java.util.List;

import general.runVoid;

public class Add extends runVoid{

	@Override
	public Object run(List<Object> list) throws Exception {
		double out = (new Double(list.get(0).toString())) + new Double(list.get(1).toString());
		for(int i = 2;i <list.size();i++){
			out = out + new Double(list.get(i).toString());
		}
		
		return out;
	}

}
