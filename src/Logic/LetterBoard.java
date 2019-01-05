package Logic;

import java.util.ArrayList;

public class LetterBoard {

	private ArrayList<Character> guesses = new ArrayList<Character>();
	private int consonants;
	public LetterBoard()
	{
		guesses = new ArrayList<Character>();
		consonants = 0;
	}
	public boolean alreadyGuessed(char letter)
	{
		
		return guesses.contains(letter);
	}
	
	public void add(char letterClicked)
	{
		guesses.add(letterClicked);
		if(!isVowel(letterClicked))
			consonants++;
	}
	
	public boolean isVowel(char a) {
		if (a == 'a' || a == 'o' || a == 'e' || a == 'i' || a == 'u')
			return true;
		else
			return false;
	}

	public boolean noMoreConsonants()
	{
		return consonants==21;
	}
	
	public boolean vowelsRemain() {
		boolean val = (!(guesses.contains('a')&&guesses.contains('e') && guesses.contains('o')
				&& guesses.contains('u') && guesses.contains('i')));
		return val;
	}
	public void restart() {
		guesses.removeAll(guesses);
		consonants =0;
	}
}


