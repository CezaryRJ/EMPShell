package indexing;

import java.io.File;
import java.util.ArrayList;

public class InvertedIndex {

	private Lexicon lexicon = new Lexicon();

	private ArrayList<PostingList> invertedIndex;

	public void addFile(File inn, int docId) {

		PostingList tmp;

		String fileName = inn.getName();
		int lexiconId = lexicon.lookup(fileName);

		// not found
		if (lexiconId == 0) {
			lexiconId = lexicon.addValue(fileName);
			tmp = new PostingList();
			tmp.addPosting(new Posting(docId));
			invertedIndex.add(lexiconId, tmp);
		} else {
			tmp = invertedIndex.get(lexiconId);
		}

		tmp.addPosting(new Posting(docId));

	}
	
	public Lexicon getLexicon(){
		return lexicon;
	}
	
	public ArrayList<PostingList> getInvertedIndex() {
		return invertedIndex;
	}

	public void setInvertedIndex(ArrayList<PostingList> invertedIndex) {
		this.invertedIndex = invertedIndex;
	}

}
