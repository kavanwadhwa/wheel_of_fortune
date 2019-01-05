package GUI;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import Logic.Contestant;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class OpeningScreen extends Application{
	private Button play;
	private VBox buttons;			
	private Stage primaryStage;
	private Scene opening;
	private MainGUI main;
	private Scene show;
	@Override
	public void start(Stage primaryStage) throws Exception {
	
		main = new MainGUI();
		this.primaryStage = primaryStage;
		primaryStage.setScene(setUp());
		primaryStage.show();
		
	}
	
	private Scene setUp()
	{
		buttons = new VBox();
		play = new Button("PLAY!");
		play.setStyle("-fx-font-weight: bold;-fx-font-size: 45; -fx-background-color: transparent;");
		StackPane playButton = setUpPlayButton();
		 
		BorderPane menu = new BorderPane();
		buttons.setPadding(new Insets(40));
		buttons.getChildren().addAll(playButton);
		
		Image back = new Image("background.png");
		menu.setPrefSize(back.getWidth(), back.getHeight());
		
		menu.setBottom(buttons);
		opening = new Scene(menu);
		
		BackgroundImage background= new BackgroundImage(new Image("background.png",true),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		menu.setBackground(new Background(background));

		return opening;
		
	}

	private Contestant[] selectPlayers() throws IOException
	{
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Start");
		dialog.setHeaderText("Enter Players:");

		ButtonType start = new ButtonType("Start playing!");
		dialog.getDialogPane().getButtonTypes().addAll(start);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField playerOne = new TextField();
		playerOne.setPromptText("Player 1 Name");
		TextField playerTwo = new TextField();
		playerTwo.setPromptText("Player 2 Name ");

		grid.add(new Label("Player 1:"), 0, 0);
		grid.add(playerOne, 1, 0);
		grid.add(new Label("Player 2:"), 0, 1);
		grid.add(playerTwo, 1, 1);

		
		Node checkStart = dialog.getDialogPane().lookupButton(start);
		checkStart.setDisable(true);
		
		playerTwo.textProperty().addListener((observable, oldValue, newValue) -> {
		    checkStart.setDisable(newValue.trim().isEmpty());
		});
		
		dialog.getDialogPane().setContent(grid);
		Platform.runLater(() -> playerOne.requestFocus());
		dialog.setResultConverter(startButton -> {
		    if (startButton == start)
		        return new Pair<>(playerOne.getText(), playerTwo.getText());
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();
		Contestant[] contestants = new Contestant[2];
		result.ifPresent(playerInformation -> {
			contestants[0]= new Contestant("" + playerInformation.getKey());
			contestants[1] = new Contestant("" + playerInformation.getValue());
		});
		show = main.setUp(primaryStage, contestants);
		primaryStage.hide();
		primaryStage.setScene(show);
		primaryStage.show();
		return contestants;
	}
	
	private StackPane setUpPlayButton()
	{
		play = new Button("PLAY!");
		play.setStyle("-fx-font-weight: bold;-fx-font-size: 45; -fx-background-color: transparent;");
		Rectangle r1 = new Rectangle();
		StackPane p = new StackPane();
		
		 r1.setWidth(200);
		 r1.setHeight(100);
		 r1.setArcHeight(20);
		 r1.setArcWidth(20);
		 r1.setStroke(Color.BLACK);
		 r1.setFill(Color.LIGHTGRAY);
		 
		 p.getChildren().addAll(r1, play);
			setFadeTransition(r1);
		 play.setOnAction(new EventHandler<ActionEvent>() {
				@Override // Override the handle method
				public void handle(ActionEvent e) {
					try {
						selectPlayers();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			});
		 play.setPrefSize(r1.getWidth(), r1.getHeight());
		 DropShadow shadow = new DropShadow();
		 shadow.setHeight(100);
		 shadow.setOffsetX(10);
		 shadow.setOffsetY(10);
		 r1.setEffect(shadow);
		 return p;
	}
	
	private void setFadeTransition(Rectangle r1) {
		FadeTransition ft = new FadeTransition(Duration.millis(1000), r1);
		ft.setFromValue(1.0);
	    ft.setToValue(0.8);
		ft.setCycleCount(Timeline.INDEFINITE);
		ft.setAutoReverse(true);
		ft.play(); // Start animation
	}
	
	public static void main(String[] args) {
		
		launch(args);
	}
}
