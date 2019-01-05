package GUI;

import java.io.IOException;

import Logic.Contestant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGUI{
	 MainGUI(){
	}
	 Scene setUp(Stage primaryStage, Contestant[] contestants) throws IOException
	{
		
		PlayerCardGUI card = new PlayerCardGUI(contestants);
		
		WheelGUI wheelGUI = new WheelGUI();
		
		RefactoredLetterBoardGUI letterboard = new RefactoredLetterBoardGUI(wheelGUI);
		
		BoardGUI control = new BoardGUI(letterboard, wheelGUI, card, primaryStage);
		
		GridPane main = new GridPane();
		
		//set center wheel and player card
		VBox board = control.getBoard();
		VBox wheel = wheelGUI.setUp(control);
		
		GridPane wheelAndCard = new GridPane();
		wheelAndCard.hgapProperty().bind(board.widthProperty().divide(6));
		
		StackPane holdWheel = new StackPane();
		StackPane holdCard = new StackPane();
		holdWheel.getChildren().add(wheel);
		holdCard.getChildren().add(card.setUp());
		
		wheelAndCard.add(holdWheel, 0, 1);
		wheelAndCard.add(holdCard, 1, 1);
		wheelAndCard.setAlignment(Pos.CENTER);
		
		main.add(board, 0, 0);
		main.add(wheelAndCard, 0,1);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(10));
		borderPane.setLeft(letterboard.setUp(main));
		borderPane.setCenter(main);
		
		Image image = new Image("p2.jpg");
		ImageView setter = new ImageView(image);
		setter.fitWidthProperty().bind(primaryStage.widthProperty());
		setter.fitHeightProperty().bind(primaryStage.heightProperty());
		
		StackPane background = new StackPane();
		background.getChildren().addAll(setter, borderPane);
		Scene scene = new Scene(background);
		return scene;
	}
}
	
	


