package indexing;

public class Posting {

	private int docID;
	
	
	public Posting(int docID){
		this.setDocID(docID);
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	
}
