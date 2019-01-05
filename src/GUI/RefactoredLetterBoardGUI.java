package GUI;
import java.util.ArrayList;

import Logic.LetterBoard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.*;

public class RefactoredLetterBoardGUI implements Subject, Observer {
	static char letterClicked = ' ';
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private ArrayList<Button> letters = new ArrayList<Button>();
	private LetterBoard letterBoard;
	private boolean alreadyShown = false;

	 RefactoredLetterBoardGUI(Subject wheel) {
		wheel.register(this);
		letterBoard = new LetterBoard();
	}

	 GridPane setUp(GridPane main) {
		for (int i = 0; i < 26; i++) {
			char a = (char) ('a' + i);
			Button add = new Button("" + a);
			add.setFont(new Font("Verdana", 20));
			add.setDisable(true);
			add.prefHeightProperty().bind(main.heightProperty().divide(13));
			add.setPrefWidth(60);
			add.setStyle("-fx-background-color : lightblue; -fx-border-color: black; -fx-border-weight: 5px;");
			add.setOnAction(new EventHandler<ActionEvent>() {
				@Override // Override the handle method
				public void handle(ActionEvent e) {
					letterClicked = add.getText().charAt(0);
					add.setDisable(true);
					letterBoard.add(letterClicked);
					disableAll();
					notifyObserver();
				}
			});
			letters.add(add);
		}

		// Create a pane and set properties
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		VBox one = new VBox();
		VBox two = new VBox();

		// Place nodes in the pane at positions column,row
		for (int l = 0; l < 26; l++) {
			if (l < 13)
				one.getChildren().add(letters.get(l));
			else
				two.getChildren().add(letters.get(l));
		}
		pane.add(one, 0, 0);
		pane.add(two, 1, 0);
		pane.setStyle("-fx-background-color: white;");
		return pane;
	}

	 void noMoreVowels() {
		if (!letterBoard.vowelsRemain() && !alreadyShown) {
			alreadyShown = true;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("HI");
			alert.setHeaderText(null);
			alert.setContentText("No more vowels to guess!");
			alert.showAndWait();
		}
	}

	 void changeVowelState(boolean value) {
		for (int i = 0; i < 26; i++) {
			char check = letters.get(i).getText().charAt(0);
			if (!letterBoard.alreadyGuessed(check)) {
				if (letterBoard.isVowel(check))
					letters.get(i).setDisable(value);
				else
					letters.get(i).setDisable(!value);
			}
		}
	}

	 void disableAll() {
		for (int i = 0; i < 26; i++)
			letters.get(i).setDisable(true);
	}

	@Override
	public void register(Observer o) {
		observers.add(o);
	}

	@Override
	public void unregister(Observer o) {
		observers.remove(o);
	}

	@Override
	public void notifyObserver() {
		for (Observer o : observers) {
			if (o.updateBoard(letterClicked))
				disableAll();
		}
	}

	 LetterBoard getLetterBoard() {
		return letterBoard;
	}

	@Override
	public void notifyObserver2(int landed) {
		for (Observer o : observers)
			o.update(landed);
	}

	@Override
	public boolean updateBoard(char a) {
		return false;
	}

	private void enableAll() {
		for (int i = 0; i < 26; i++) {
			if (!letterBoard.alreadyGuessed(letters.get(i).getText().charAt(0)))
				letters.get(i).setDisable(false);
		}
	}

	@Override
	public void update(int landed) {
		switch (landed) {
		case 0:
			break;
		case -1:
			break;
		case 1:
			enableAll();
			break;
		default:
			if (!letterBoard.noMoreConsonants())
				changeVowelState(true);
			break;
		}

	}
}