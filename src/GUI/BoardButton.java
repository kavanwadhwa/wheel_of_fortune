package GUI;
import javafx.scene.control.Button;

public class BoardButton extends Button {
	private int index, whichWord;
	private char letter;
	 BoardButton(char letter, int whichWord, int index)
	{
		super("" + letter);
		this.index = index;
		this.whichWord = whichWord;
		this.letter = letter;	;
	}
	
	  int getIndex()
	{
		return index;
	}
	
	 int getWhichWord()
	{
		return whichWord;
	}
	
	 char getLetter()
	{
		return letter;
	}
	
}

