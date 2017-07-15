package indexing;


import java.util.ArrayList;
import java.util.Arrays;

public class InvertedIndex {

	private Lexicon lexicon = new Lexicon();

	private ArrayList<PostingList> postingLists = new ArrayList<>();

	public void addFile(String path, int id) {

		ArrayList<String> tags = getTags(path);
		
		for (int i = 0; i < tags.size(); i++) {
			
			if (lexicon.lookup(tags.get(i)) == -1) {
				
				lexicon.addValue(tags.get(i));
				postingLists.add(new PostingList());
				
				////double storage, this should be changed
				postingLists.get(lexicon.lookup(tags.get(i))).addPosting(new Posting(id),id);
			} else {
				postingLists.get(lexicon.lookup(tags.get(i))).addPosting(new Posting(id),id);
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
	
	public ArrayList<String> getTags(String inn) {

		ArrayList<String> out = new ArrayList<String>(Arrays.asList(inn.split("\\\\")));
		
		out.add("." + getExtention(out.get(out.size()-1)));
	
		
		//System.out.println(out);
		return  out;
	}
	public ArrayList<String> readMeta() {

		return null;
	}
	
	public String getExtention(String inn){
		String[] out = inn.split("\\.");
		return out[out.length-1];
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
