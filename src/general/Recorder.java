package general;
import java.util.ArrayList;

public class Recorder {

	ArrayList<Object> records = new ArrayList<>();

	
	
	Recorder(){
		
		records.add("No commands in storage");
	
	}

	public void setCommand(ArrayList<Object> tokens){
		records = new ArrayList<Object>(tokens);
		
	}
	public ArrayList<Object> getCommand(){

		return records;
	}
	
	public void print(){
		System.out.println(records);
	}
}

