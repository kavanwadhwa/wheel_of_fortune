package GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import Logic.BoardPhrase;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BoardGUI implements Observer {
	BoardPhrase phrase;
	private RefactoredLetterBoardGUI letterBoard;
	private WheelGUI wheel;
	private GridPane board, addCategory;
	private VBox one = new VBox(10);
	private ArrayList<Button> buttons = new ArrayList<Button>();
	public Button buyVowel = new Button("Buy a Vowel");
	public Button guessPhrase = new Button("Guess Phrase");
	private PlayerCardGUI card;
	private int COL_CONST = 10;
	private boolean freePass = false;
	private Stage primaryStage;
	private HBox two, categoryHolder;
	private ArrayList<String> phrasesUsed;

	protected BoardGUI(Subject letterBoard, WheelGUI wheel, PlayerCardGUI card, Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.letterBoard = (RefactoredLetterBoardGUI) letterBoard;
		phrasesUsed = new ArrayList<String>();
		letterBoard.register(this);

		wheel.register(this);
		this.wheel = wheel;

		this.card = card;

		board = new GridPane();
		addCategory = new GridPane();
		categoryHolder = new HBox();
		
	}

	RefactoredLetterBoardGUI getLetterBoard() {
		return letterBoard;
	}

	VBox getBoard() throws IOException {
		revealSound();
		do
		{
		this.phrase = new BoardPhrase();
		}
		while(phrasesUsed.contains(phrase.getAnswer()));
		phrasesUsed.add(phrase.getAnswer());
		System.out.println(phrasesUsed);
		one.setPadding(new Insets(0, 0, 0, 10));
		initButtons();
		setUpBoard();
		activateBuyVowel();
		activateGuessPhrase();
		setUpButtonRow();
		initCategoryRow();
		combineBoardAndCategoryRow();
		one.getChildren().addAll(addCategory, two);
	
		return one;
	}

	private void initButtons() {
		int spot = 0;
		for (int i = 0; i < phrase.getWords().length; i++) {
			for (int l = 0; l < phrase.getLengthOfWord(i); l++) {
				BoardButton b = new BoardButton(phrase.getCurrentState().charAt(spot), i, l);
				b.setFont(new Font("Comic Sans", 30));
				b.setOnAction(new EventHandler<ActionEvent>() {
					@Override // Override the handle method
					public void handle(ActionEvent e) {
						try {

							printProbabilities(b);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				b.setStyle("-fx-background-color: white; -fx-border-color: black;");

				b.setPrefSize(100, 100);
				buttons.add(b);
				spot++;
			}
			Button space = new Button("-");
			space.setStyle("-fx-background-color: transparent;");
			space.setDisable(true);
			buttons.add(space);
			spot++;
		}
	}

	private void setUpBoard() {
		// tracker variables
		int numWords = 0;
		int length = 0;
		int count = 0;
		int index = 0;
		int i = 0;

		String[] words = phrase.getWordsAsString();
		for (int z = 0; z < words.length; z++) {
			if (words[z].length() > COL_CONST) {
				COL_CONST = words[z].length();
			}
		}

		while (numWords < words.length) {

			for (int k = numWords; k < words.length; k++) {
				length = length + words[k].length();
				if (length > COL_CONST) {
					length -= words[k].length() + 1;
					k = words.length;
				} else {
					numWords++;
					if (k != words.length - 1) {
						length++;
					}
				}

			}
			for (int j = 0; j < COL_CONST; j++) {
				Rectangle r1 = new Rectangle();
				r1.setWidth(100);
				r1.setHeight(100);
				r1.setStroke(Color.BLACK);
				if (count < length) {
					if (!buttons.get(index).getText().equals("-")) {
						r1.setFill(Color.WHITE);
						StackPane stackPane = new StackPane();
						stackPane.getChildren().addAll(r1, buttons.get(index));
						board.add(stackPane, j, i);
					} else {
						r1.setFill(Color.GREEN);
						board.add(r1, j, i);
					}
					count++;
					index++;

					// last word ends on last spot in line so we skip space
					if (length == COL_CONST && count == length) {

						index++;
						count++;
					}
				}

				// last word in this line ends at least one spot before last spot in line so we
				// skip the
				// space
				else if (length <= COL_CONST - 1 && count == length) {
					r1.setFill(Color.GREEN);
					board.add(r1, j, i);
					index++;
					count++;
				} else {
					r1.setFill(Color.GREEN);
					board.add(r1, j, i);
				}
			}
			i++;
			length = 0;
			count = 0;
		}
		// pad with one line of green boxes
		padIt(i);
	}

	private void activateBuyVowel() {
		buyVowel.setPrefSize(200, 50);
		buyVowel.setDisable(true);
		buyVowel.setOnAction(new EventHandler<ActionEvent>() {
			@Override // Override the handle method
			public void handle(ActionEvent e) {
				if (card.getCurrentPlayer().getTotal() >= 250) {
					letterBoard.changeVowelState(false);
					buyVowel.setDisable(true);
					card.getCurrentPlayer().addMoney(-250);
					card.update();
				}
			}
		});
	}

	private void activateGuessPhrase() {
		guessPhrase.setPrefSize(200, 50);
		guessPhrase.setOnAction(new EventHandler<ActionEvent>() {
			@Override // Override the handle method
			public void handle(ActionEvent e) {

				TextInputDialog guess = new TextInputDialog();
				guess.setTitle("Wheel of Fortune");
				guess.setHeaderText(null);
				guess.setContentText("Please enter your guess:");

				Optional<String> result = guess.showAndWait();

				if (result.isPresent()) {
					String phraseGuess = guess.getResult().toLowerCase().trim();
					if (phraseGuess.equals(phrase.getAnswer().toLowerCase().trim()))
						doWin();
					else {
						buzzerSound();
						card.nextTurn();
						guessPhrase.setDisable(true);
					}
					letterBoard.disableAll();
					buyVowel.setDisable(true);
					
				} else {
					letterBoard.disableAll();
					buyVowel.setDisable(true);
						guessPhrase.setDisable(true);
					card.nextTurn();
				}
				if (letterBoard.getLetterBoard().noMoreConsonants())
					guessPhrase.setDisable(false);
			}
		});
		guessPhrase.setDisable(true);
	}

	private void setUpButtonRow() {
		two = new HBox();
		two.getChildren().addAll(buyVowel, guessPhrase);
		two.setAlignment(Pos.CENTER);
		two.prefWidthProperty().bind(board.widthProperty());
	}

	private void initCategoryRow() {
		Label category = new Label(phrase.getCategory().toUpperCase());
		category.setFont(new Font("Century Gothic", 30));
		categoryHolder.setAlignment(Pos.CENTER);
		categoryHolder.getChildren().add(category);
		categoryHolder.setStyle("-fx-background-color: white; -fx-border-color: black;");
	}

	private void combineBoardAndCategoryRow() {
		addCategory.setVgap(1);
		addCategory.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
		addCategory.add(board, 0, 0);
		addCategory.add(categoryHolder, 0, 1);
	}

	private void updatePhrase(char a) {
		for (int i = 0; i < phrase.getAnswer().length(); i++) {
			if (phrase.getCurrentState().charAt(i) == a) {
				int index = i;
				FadeTransition ft = new FadeTransition(Duration.millis(1300), buttons.get(i));
				buttons.get(i).setStyle("-fx-background-color: blue;");
				ft.setFromValue(1.0);
				ft.setToValue(1.0);
				ft.setCycleCount(1);
				ft.setAutoReverse(true);
				ft.statusProperty().addListener((obs, oldStatus, newStatus) -> {
					if (newStatus == Animation.Status.STOPPED)
						buttons.get(index).setStyle("-fx-background-color: white; -fx-border-color: black;");
					buttons.get(index).setDisable(true);
				});
				ft.play(); // Start animation
			}
			buttons.get(i).setText(phrase.getCurrentState().toUpperCase().charAt(i) + "");
		}
	}

	private void showAnswer() {
		for (int i = 0; i < phrase.getAnswer().length(); i++) {

			buttons.get(i).setText(phrase.getAnswer().toUpperCase().charAt(i) + "");

		}
	}

	private boolean checkWin() {
		if (phrase.check()) {
			doWin();
		}
		return phrase.check();

	}

	private void doWin() {
		solveSound();
		showAnswer();
		card.getCurrentPlayer().addMoney(1001);
		card.update();

		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");
		Alert alert = new Alert(AlertType.INFORMATION,
				"Phrase correctly guessed! The winner of this round is: " + card.getLeader().getName()
						+ " with total earnings of " + "$" + card.getLeader().getTotal() + "! Play again?",
				yes, no);
		alert.setTitle("Correct Guess");
		alert.setHeaderText(null);
		// alert.setContentText(");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.isPresent() && result.get() == yes) {
			restart();
		} else
			shutDown();
	}

	private void printProbabilities(BoardButton b) throws FileNotFoundException {
		Alert odds = new Alert(AlertType.INFORMATION);
		odds.setTitle("Letter Probability");
		odds.setHeaderText("Letter Probabilities: ");
		odds.setContentText(null);
		odds.setContentText(phrase.getWords()[b.getWhichWord()].getProbabilityAtIndex(b.getIndex()));
		odds.showAndWait();
	}

	private void shutDown() {
		guessPhrase.setDisable(true);
		buyVowel.setDisable(true);
		wheel.setSpin(true);
		letterBoard.disableAll();
		for(int l = 0; l<buttons.size(); l++)
		{
			buttons.get(l).setDisable(true);
		}
	}

	private void padIt(int i) {
		for (int j = 0; j < COL_CONST; j++) {
			Rectangle r1 = new Rectangle();
			r1.setWidth(100);
			r1.setHeight(100);
			r1.setStroke(Color.BLACK);
			r1.setFill(Color.GREEN);
			board.add(r1, j, i);
		}
	}

	private void dingSound() {
		Media sound = new Media(new File("Music\\Ding.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void buzzerSound() {
		Media sound = new Media(new File("Music\\Buzzer.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void solveSound() {
		Media sound = new Media(new File("Music\\PuzzleSolve.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void revealSound() {
		Media sound = new Media(new File("Music\\PuzzleReveal.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	public boolean updateBoard(char a) {
		phrase.updateWord(a);
		if (phrase.getAnswer().indexOf(a) >= 0) {
			dingSound();
			updatePhrase(a);
			if (!letterBoard.getLetterBoard().isVowel(a) && !freePass) {
				card.getCurrentPlayer().addMoney(wheel.currentVal * phrase.getNumOfLetters(a));
				card.update();
			}
			if (letterBoard.getLetterBoard().vowelsRemain() && card.getCurrentPlayer().getTotal() >= 250
					&& !letterBoard.getLetterBoard().noMoreConsonants()) {
				buyVowel.setDisable(false);
				wheel.setSpin(true);
			}
			guessPhrase.setDisable(false);
		}

		else if (!freePass) {
			buzzerSound();
			card.nextTurn();
			buyVowel.setDisable(true);
			guessPhrase.setDisable(true);
		}

		freePass = false;
		wheel.setSpin(false);
		letterBoard.noMoreVowels();
		if (letterBoard.getLetterBoard().noMoreConsonants()) {
			System.out.println("WOKRING HERE");
			if (card.getCurrentPlayer().getTotal() >= 250)
				buyVowel.setDisable(false);
			wheel.setSpin(true);
			guessPhrase.setDisable(false);
		}

		return checkWin();

	}

	private void restart() {
		primaryStage.hide();
		buttons.removeAll(buttons);
		board.getChildren().removeAll(board.getChildren());
		categoryHolder.getChildren().removeAll(categoryHolder.getChildren());
		addCategory.getChildren().removeAll(addCategory.getChildren());
		one.getChildren().removeAll(one.getChildren());
		try {
			this.getBoard();
			revealSound();
		} catch (IOException e) {
			e.printStackTrace();
		}
		card.reset();
		// restart letters and score
		letterBoard.getLetterBoard().restart();
		primaryStage.show();
	}

	@Override
	public void update(int landed) {

		if (!(landed > 0)) {

			// bankrupt
			if (landed == 0)
				card.getCurrentPlayer().bankrupt();
			card.nextTurn();
			letterBoard.disableAll();
			wheel.setSpin(false);
		} else if (letterBoard.getLetterBoard().noMoreConsonants() && card.getCurrentPlayer().getTotal() < 250)
			guessPhrase.setDisable(false);
		// freePlay
		else if (landed == 1) {
			freePass = true;
		}

		// TODO Auto-generated method stub

	}
}
