package indexing;


import java.util.HashMap;


public class PostingList {

	public HashMap<Integer,Posting> postings = new HashMap<>();

	public HashMap<Integer, Posting> getPostings() {
		return postings;
	}

	public Posting getLastPosting(){
		return postings.get(postings.size()-1);
	}
	
	public void setPostings(HashMap<Integer,Posting> postings) {
		this.postings = postings;
	}
	
	  public void addPosting(Posting posting, int docID) {
	    	
	        // First entry?
	        if (this.postings == null) {
	            this.postings = new HashMap<Integer,Posting>();
	        }

	        this.postings.put(posting.getDocID(),posting);
	    	
	    }
	  
	  public void print(){
		  for(int i = 0; i<postings.size();i++){
			  System.out.println(postings.get(i).getDocID());
		  }
	  }
	  public int size(){
		  return postings.size();
	  }
}
