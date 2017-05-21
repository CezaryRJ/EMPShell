package indexing;

import java.util.ArrayList;

public class PostingList {

	private ArrayList<Posting> postings = new ArrayList<>();

	public ArrayList<Posting> getPostings() {
		return postings;
	}

	public void setPostings(ArrayList<Posting> postings) {
		this.postings = postings;
	}
	
}
