package search;

import java.util.HashMap;

import indexing.Posting;

public class ResultSet {

	HashMap<Integer, Posting> results;

	public void add(Posting inn) {
		results.put(inn.getDocID(), inn);
	}

	public void remove(int docID) {
		results.remove(docID);
	}

	public void setResults(HashMap<Integer, Posting> results) {
		this.results = results;
	}

	public void print() {
		if(results == null || results.size() <= 0){
			System.out.println("No results found");
			return;
		}
		for (Integer docId : results.keySet()) {

			System.out.println(docId);

		}
		System.out.println("Number of results : " + results.size());
	}
}
