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

	public String printUp() {
		if (index > -1) {

			return records.get(--index);
		}
		return records.get(index);
	}

	public String printDown() {

		if (index < 10) {

			return records.get(++index);
		}
		return records.get(index);
	}

}
