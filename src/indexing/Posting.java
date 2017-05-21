package indexing;

public class Posting {

	private String docID;
	
	Posting(String docID){
		this.setDocID(docID);
	}

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}
}
