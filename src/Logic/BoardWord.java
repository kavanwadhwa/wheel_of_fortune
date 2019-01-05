package Logic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class BoardWord {
	private String answer;
	private ArrayList<Character> letters = new ArrayList<Character>();
	private Map<Character, Integer> count = new TreeMap<Character, Integer>();
	private BoardPhrase associatedPhrase;

	 BoardWord(BoardPhrase associatedPhrase, String word) {
		this.associatedPhrase = associatedPhrase;
		this.answer = word.toLowerCase();

		for (int i = 0; i < answer.length(); i++) {
			if (answer.charAt(i) == ' ')
				letters.add(' ');
			else if (answer.charAt(i) == '\'' || answer.charAt(i) == '-' || answer.charAt(i) == '.')
				letters.add(answer.charAt(i));
			else {
				letters.add('_');
				if (!count.containsKey(answer.charAt(i)))
					count.put(answer.charAt(i), 1);
				else
					count.put(answer.charAt(i), count.get(answer.charAt(i)) + 1);
			}
		}
	}

	
	 String getAnswer() {
		return answer;
	}

	 boolean updateWord(char letter) {

		if (answer.contains("" + letter)) {
			for (int i = 0; i < answer.length(); i++) {
				if (answer.charAt(i) == letter) {
					letters.set(i, letter);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	 public int getLength() {
		return answer.length();
	}

	String getRegex() {

		String regex = "";
		for (int i = 0; i < answer.length(); i++) {
			if (letters.get(i) == '_') {
				regex += "[a-zA-Z]";
			} 
			else if((""+letters.get(i)).matches("[a-zA-Z]"))
			{
				regex+=letters.get(i);
			}
			else {
				regex += "[" + letters.get(i) + "]";
			}
		}
		return regex;
	}

	public String getProbabilityAtIndex(int i) throws FileNotFoundException {
		WordComparer a = new WordComparer();
		return a.odds(associatedPhrase, this, i);
	}

}
