import java.util.ArrayList;

public class Recorder {

	ArrayList<String> records = new ArrayList<>();

	int index = 0;

	public void add(String inn) {
		if (records.size() < 10) {
			records.add(inn);
			index++;
		} else {
			records.remove(0);
			records.add(inn);
		}
	}

	public void printUp() {
		if (index > 0) {
			System.out.println('\r' + records.get(index));
			index--;
		}
	}

	public void printDown() {
		if(index < 10){
		System.out.println('\r' + records.get(index));
		index++;}
	}

}
