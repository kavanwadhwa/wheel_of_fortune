package GUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import Logic.WheelSingleton;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class WheelGUI implements Subject {
	private WheelSingleton wheel;
	private int totalAngle;
	private Label wedgeLabel;
	private BoardGUI main;
	private boolean spun;
	private Button spin;
	public int currentVal;
	private Slider power;
	private Random random;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private ChangeListener<Animation.Status> listener;
	
	VBox setUp(BoardGUI main) throws FileNotFoundException {
		random = new Random();
		this.main = main;
		totalAngle = 0;

		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(new Double[] { 0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });

		Image wheelImage = new Image("wheel.png");
		ImageView view = new ImageView(wheelImage);
		view.setFitHeight(300);
		view.setFitWidth(300);

		wheel = WheelSingleton.getInstance();

		Pane test = new StackPane();
		Pane secondLayer = new StackPane();
		Circle a = new Circle(150, 150, 150);

		wedgeLabel = new Label("");
		wedgeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
		a.setFill(Color.BLACK);
		test.getChildren().addAll(a, view);
		secondLayer.getChildren().addAll(test, wedgeLabel);
		RotateTransition rt = new RotateTransition(Duration.millis(5000), test);

		VBox wheelPart = new VBox(10);
		wheelPart.setAlignment(Pos.CENTER);
		spin = new Button("SPIN!");
		spin.setPrefSize(view.getFitWidth() / 2, view.getFitHeight() / 5);

		Image arrowImage = new Image("arrow.png");

		ImageView arrow = new ImageView(arrowImage);
		arrow.setRotate(90);
		arrow.setFitHeight(30);
		arrow.setFitWidth(30);
		createSlider();
		wheelPart.getChildren().addAll(arrow, secondLayer, spin, power);

		spin.setOnAction(new EventHandler<ActionEvent>() {
			@Override // Override the handle method
			public void handle(ActionEvent e) {

				spin(rt);

				spin.setDisable(true);

			}
		});

		return wheelPart;
	}

	private void spinSound() {
		Media sound = new Media(new File("Music\\edit.wav").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private int spin(RotateTransition rt) {

		spinSound();
		wedgeLabel.setText("");
		int spinAngle = random.nextInt(100) + 500 + 5 * (int) (power.valueProperty().doubleValue() + 1);
		totalAngle += spinAngle;
		rt.setByAngle(spinAngle);
		listener = new ChangeListener<Animation.Status>() {

			@Override
			public void changed(ObservableValue<? extends Status> obs, Status oldStatus, Status newStatus) {

				main.buyVowel.setDisable(true);
				main.guessPhrase.setDisable(true);

				if (newStatus == Animation.Status.STOPPED) {
					spun = true;
					currentVal = wheel.spin(totalAngle);
					wedgeLabel.setText(wheel.getLabelText());
					notifyObserver();
					if (spun) {
						obs.removeListener(this);
					}
				}
			}
		};

		rt.statusProperty().addListener(listener);
		rt.play();

		return currentVal;

	}

	private void createSlider() {
		power = new Slider(0, 100, 50);
		power.showTickLabelsProperty().set(false);
		power.setStyle("-fx-control-inner-background: palegreen;");
		power.disableProperty().bind(spin.disableProperty());
	}

	 void setSpin(boolean value) {
		spin.setDisable(value);
		
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
		for (Observer o : observers) 
			o.update(currentVal);
	}

	@Override
	public void notifyObserver2(int landed) {
		// TODO Auto-generated method stub
		
	}

}
