package GUI;

import Logic.Contestant;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;


public class PlayerCardGUI {
	private Contestant[] a = new Contestant[2];
	private Color[] colors = new Color[2];
	private int tracker;
	private Label score, name;
	private Rectangle r1;
	private ImageView icon;
	 PlayerCardGUI(Contestant[] a) {
		this.a = a;
		tracker = 0;
	}
	 StackPane setUp()
	{
		StackPane card = new StackPane();
		StackPane test = new StackPane();
		 score = new Label();
		
		 a[0].setImage(new Image("Nerd_with_Glasses_Emoji.png"));
		 a[1].setImage(new Image("Smiling_Emoji_with_Eyes_Opened.png"));
		 
		name = new Label();
		score.fontProperty().set(new Font("Century Gothic", 40));
		
		score.setStyle("-fx-font-weight: bold");
		name.fontProperty().set(new Font("Century Gothic", 40));
		name.setStyle("-fx-font-weight: bold");
		
		presetColors();
		  r1 = new Rectangle();
		 r1.setWidth(400);
		 r1.setHeight(400);
		 r1.setArcHeight(20);
		 r1.setArcWidth(20);
		 r1.setStroke(Color.BLACK.brighter());
		 r1.setStrokeWidth(6);
		VBox one = new VBox(10);
		 icon = new ImageView();
		 icon.fitHeightProperty().set(r1.getHeight()/2);
		 icon.fitWidthProperty().set(r1.getWidth()/2);
		 
		one.getChildren().addAll(icon, name);
		one.setAlignment(Pos.CENTER);
		test.getChildren().add(one);
		card.getChildren().addAll( r1, one);
		update();
		
		return card;
	}
	
	
	
	 void nextTurn() {
		if(tracker== a.length-1)
			tracker = 0;
		else
			tracker++;
		update();
	}
	
	 void update() {
		icon.setImage(a[tracker].getImage());
		name.setText(a[tracker].getName() + "\n" + ("Score: " + a[tracker].getTotal()));
		r1.setFill(colors[tracker]);
	}

	 void reset()
	{
		for(Contestant contestant : a) 
			contestant.bankrupt();
		update();
	}
	
	private void presetColors()
	{
		colors[0] = Color.LIGHTBLUE;
		colors[1] = Color.LIGHTPINK;
	}
	 Contestant getLeader()
	{
		Contestant leader = a[0];
		for(int i =1; i<a.length; i++)
		{
			if(a[i].getTotal()>leader.getTotal())
				leader = a[i];
		}
		return leader;
	}
	 Contestant getCurrentPlayer()
	{
		return a[tracker];
	}
	
	
	
}
