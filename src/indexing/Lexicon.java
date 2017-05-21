package indexing;
import java.util.HashMap;


public class Lexicon {
	private HashMap<String, Integer> lexicon = new HashMap<String, Integer>();
	
	
	public int addValue(String inn){
		
		if(lexicon.containsKey(inn)){
			return lexicon.get(inn);
		}
		
		lexicon.put(inn, lexicon.size());
		return lexicon.size();
	}
	public int lookup(String key){
		return lexicon.get(key);
	}

	public HashMap<String, Integer> getLexicon() {
		return lexicon;
	}

	public void setLexicon(HashMap<String, Integer> lexicon) {
		this.lexicon = lexicon;
	}

}
