package indexing;

import java.util.ArrayList;


public class PostingList {

	public ArrayList<Posting> postings = new ArrayList<>();

	public ArrayList<Posting> getPostings() {
		return postings;
	}

	public Posting getLastPosting(){
		return postings.get(postings.size()-1);
	}
	
	public void setPostings(ArrayList<Posting> postings) {
		this.postings = postings;
	}
	
	  public void addPosting(Posting posting) {
	    	
	        // First entry?
	        if (this.postings == null) {
	            this.postings = new ArrayList<Posting>(1);
	        }

	        this.postings.add(posting);
	    	
	    }
	  
	  public void print(){
		  for(int i = 0; i<postings.size();i++){
			  System.out.println(postings.get(i).getDocID());
		  }
	  }
}
