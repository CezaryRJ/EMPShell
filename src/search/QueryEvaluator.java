package search;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import general.DataBase;
import general.Timer;
import general.runVoid;
import indexing.Posting;
import indexing.PostingList;

public class QueryEvaluator extends runVoid {

	public ResultSet mostRecent;

	DataBase data;

	Timer timer;

	public QueryEvaluator(DataBase data) {
		this.data = data;

		timer = new Timer();
	}

	public ResultSet evaluateQuery(List<Object> inn) {
		// find the smallest posting list
		int smallest = 0;
		for (int i = 1; i < inn.size(); i++) {
			if (data.lookup((String) inn.get(smallest)).size() > data.lookup((String) inn.get(i)).size()) {
				smallest = i;
			}
		}
		ResultSet out = new ResultSet();

		out.setResults(new HashMap<Integer, Posting>(data.lookup((String) inn.get(smallest)).postings));

		PostingList tmp;
		int key;
		Iterator<Integer> iter = out.results.keySet().iterator();
		inn.remove(smallest);
		for (int i = 0; i < inn.size(); i++) {
			tmp = data.lookup((String) inn.get(i));

			// iterate over hashmap
			while (iter.hasNext()) {
				key = iter.next();

				if (!tmp.postings.containsKey(key)) {
					iter.remove();
				}
			}
		}
		return out;
	}

	public void printRecent() {
		if (mostRecent.results == null || mostRecent.results.size() < 1) {
			System.out.println("No results found");
			return;
		}
		int i = 0;
		for (Integer docId : mostRecent.results.keySet()) {

			System.out.println(i + " " + data.files.get(docId).getPath());
			i++;
		}

	}

	@Override
	public Object run(List<Object> inn) throws Exception {

		timer.start();

		if (inn.get(0) == null) {
			System.out.println("Error, empty result set");
			return mostRecent;
		}

		if (inn.get(0).equals("?")) {

			openResult(inn);

			timer.stop();
		} else {
			mostRecent = evaluateQuery(inn);
			timer.stop();
			printRecent();

		}
		return mostRecent;

	}

	public void openResult(List<Object> inn) throws IOException {

		// input must be in ascending order

		Iterator<Integer> iterator = mostRecent.results.keySet().iterator();

		int length = 0;
		int tmp = 0;
		for (int x = 1; x < inn.size(); x++) {
			length = Integer.parseInt((String) inn.get(x)) - length + 1;
			for (int i = 0; i < length; i++) {
				tmp = iterator.next();
			}

			Desktop.getDesktop().open(new File(data.files.get(mostRecent.results.get(tmp).getDocID()).getPath()));

		}

	}

}
