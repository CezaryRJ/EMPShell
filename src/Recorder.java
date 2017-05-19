import java.util.ArrayList;

public class Recorder {

	ArrayList<String> records = new ArrayList<>();

	int index = 0;
	int size = 10;
	
	Recorder(){
		records.add("No commands in storage");
		index++;
	}

	public void add(String inn) {
		if (inn.equals("")) {
			return;
		}
		if (records.size() < size) {
			records.add(inn);
			index++;

		} else {
			records.remove(0);
			records.add(inn);
		}

	}

	public void printHistory() {
		for (int i = 0; i < records.size(); i++) {
			System.out.println(records.get(i));
		}
	}

	public String getInput(int inn) {
		if (inn <= index) {

			return records.get(index - inn);
		}
		return null;
	}

	public String getLatest() {
		return records.get(index-1);
	}

	public String printUp() {
		if (index > -1) {

			return records.get(--index);
		}
		return records.get(index);
	}

	public String printDown() {

		if (index < size) {

			return records.get(++index);
		}
		return records.get(index);
	}

}
