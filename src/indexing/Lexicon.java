package indexing;

import java.util.HashMap;
import java.util.Map;

public class Lexicon {
	private HashMap<String, Integer> lexicon = new HashMap<String, Integer>();

	public HashMap<String, Integer> getLexicon() {
		return lexicon;
	}

	public void setLexicon(HashMap<String, Integer> lexicon) {
		this.lexicon = lexicon;
	}

}
