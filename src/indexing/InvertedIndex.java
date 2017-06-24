package indexing;


import java.util.ArrayList;

public class InvertedIndex {

	private Lexicon lexicon = new Lexicon();

	private ArrayList<PostingList> postingLists = new ArrayList<>();

	public void addFile(String path, int id) {

		String[] tags = getTags(path);
		
		for (int i = 0; i < tags.length; i++) {
			
			if (lexicon.lookup(tags[i]) == -1) {
				
				lexicon.addValue(tags[i]);
				postingLists.add(new PostingList());
				
				////double storage, this should be changed
				postingLists.get(lexicon.lookup(tags[i])).addPosting(new Posting(id),id);
			} else {
				postingLists.get(lexicon.lookup(tags[i])).addPosting(new Posting(id),id);
			}

		}
	}
	
	public Lexicon getLexicon(){
		return lexicon;
	}
	
	public ArrayList<PostingList> getInvertedIndex() {
		return postingLists;
	}

	public void setInvertedIndex(ArrayList<PostingList> invertedIndex) {
		this.postingLists = invertedIndex;
	}
	
	public String[] getTags(String inn) {

		return inn.split("\\\\");
	}
	public ArrayList<String> readMeta() {

		return null;
	}
	public void clear(){
		lexicon = new Lexicon();
		postingLists = new ArrayList<PostingList>();
	}
	public PostingList lookup(String inn){
		int tmp = lexicon.lookup(inn);
		if(tmp == -1){
			return new PostingList();
		}
		return postingLists.get(lexicon.lookup(inn));
		
	}

}
