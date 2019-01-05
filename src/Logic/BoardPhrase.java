package Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BoardPhrase {
	private String answer;
	private ArrayList<Character> letters = new ArrayList<Character>();
	private Map<Character, Integer> count = new TreeMap<Character, Integer>();
	private int length = 0;
	private String[] wordsAsString;
	private Set<Character> incorrectLetters;
	private Set<Character> correctLetters;
	private BoardWord[] words;
	private RandomPhrase generator;

	public BoardPhrase() throws IOException {
		generator = new RandomPhrase();
	
		incorrectLetters = new HashSet<Character>();
		correctLetters = new HashSet<Character>();
		correctLetters.add('.');
		correctLetters.add('\'');
		correctLetters.add('-');
		
			this.answer = generator.getARandomPhrase().toLowerCase();
	
		
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
		wordsAsString = answer.split("\\s");
		words = new BoardWord[wordsAsString.length];
		for (int m = 0; m < wordsAsString.length; m++) {
			words[m] = new BoardWord(this, wordsAsString[m]);
		}
	}
	
	public String getCategory() {
		return generator.getCategory();
	}

	public String[] getWordsAsString() {
		return wordsAsString;
	}

	public BoardWord[] getWords() {
		return words;
	}
	
	public int getLengthOfWord(int index)
	{
		return words[index].getLength();
	}

	public int getNumOfLetters(char a) {
		return count.get(a);
	}

	public String getCurrentState() {
		return lettersToWord();
	}

	public String lettersToWord() {
		String currentWord = "";
		for (int i = 0; i < letters.size(); i++) {
			if(letters.get(i)=='_')
				currentWord+=' ';
			else
				currentWord += letters.get(i);
		}
		return currentWord;

	}

	public String currentView() {
		String spaced = "";
		for (int i = 0; i < answer.length(); i++) {
			spaced += letters.get(i) + " ";
		}
		return spaced;
	}

	public String getAnswer() {
		return answer;
	}

	public ArrayList<Character> getLetters() {
		return letters;
	}

	public Set<Character> getIncorrectLetters() {
		return incorrectLetters;
	}

	public Set<Character> getCorrectLetters() {
		return correctLetters;
	}

	public boolean updateWord(char letter) {
		for (int l = 0; l < words.length; l++) {
			words[l].updateWord(letter);
		}

		if (answer.indexOf("" + letter) >= 0) {
			correctLetters.add(letter);
			for (int i = 0; i < answer.length(); i++) {
				if (answer.charAt(i) == letter) {
					letters.set(i, letter);
				}
			}
			return true;
		} else {
			incorrectLetters.add(letter);
			return false;
		}
	}

	public int getLength() {
		return length;
	}

	public boolean check() {
		return answer.toLowerCase().trim().equals(getCurrentState().toLowerCase().trim());
	}

}
