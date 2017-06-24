package search;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

	public void evaluateQuery(ArrayList<String> queryTerms) {
		// find the smallest posting list
		int smallest = 0;
		for (int i = 1; i < queryTerms.size(); i++) {
			if (data.lookup(queryTerms.get(smallest)).size() > data.lookup(queryTerms.get(i)).size()) {
				smallest = i;
			}
		}
		ResultSet out = new ResultSet();

		out.setResults(new HashMap<Integer, Posting>(data.lookup(queryTerms.get(smallest)).postings));

		PostingList tmp;
		int key;
		Iterator<Integer> iter = out.results.keySet().iterator();
		queryTerms.remove(smallest);
		for (int i = 0; i < queryTerms.size(); i++) {
			tmp = data.lookup(queryTerms.get(i));

			// iterate over hashmap
			while (iter.hasNext()) {
				key = iter.next();

				if (!tmp.postings.containsKey(key)) {
					iter.remove();
				}
			}
		}
		mostRecent = out;
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
	public void run(ArrayList<String> inn) throws Exception {

		timer.start();
		if (inn.get(0).equals("?")) {
			Iterator<Integer> iterator = mostRecent.results.keySet().iterator();

			for (int i = 0; i < Integer.parseInt(inn.get(1)); i++) {
				iterator.next();
			}

			Desktop.getDesktop()
					.open(new File(data.files.get(mostRecent.results.get(iterator.next()).getDocID()).getPath()));
			timer.stop();
		} else {
			evaluateQuery(inn);
			printRecent();
			timer.stop();
			System.out.println();
		}

	}

	
}
