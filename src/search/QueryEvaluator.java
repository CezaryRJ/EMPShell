package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import general.DataBase;

import indexing.Posting;
import indexing.PostingList;

public class QueryEvaluator {

	public ResultSet mostRecent;
	DataBase data;

	public QueryEvaluator(DataBase data) {
		this.data = data;
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
		Iterator iter = out.results.keySet().iterator();
		queryTerms.remove(smallest);
		for (int i = 0; i < queryTerms.size(); i++) {
			tmp = data.lookup(queryTerms.get(i));
			
			// iterate over hashmap
			while(iter.hasNext()){
				key = (int) iter.next();
				
				if (!tmp.postings.containsKey(key)) {
					iter.remove();
				}
			}
		}
		mostRecent = out;
	}
	public void printRecent(){
		if(mostRecent.results == null || mostRecent.results.size() <= 0){
			System.out.println("No results found");
			return;
		}
		for (Integer docId : mostRecent.results.keySet()) {

			System.out.println(data.files.get(docId).getPath());

		}
		
	}
	

}
