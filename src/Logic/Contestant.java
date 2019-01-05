package Logic;
import javafx.scene.image.Image;



public class Contestant {
	private String name;
	private int money;
	private Image image;
	
	public Contestant(String name) {
		this.name = name;
		this.money = 0;
		image = new Image("people.png");
	}

	public String getName() {
		return name;
	}
	
	public void setImage(Image image) {
		this.image = image; 
	}
	
	public Image getImage()
	{
		return image;
	}


	public int getTotal() {
		return money;
	}

	public void addMoney(int amount) {
		money += amount;
	}

	public void removeMoney(int amount) {
		money -= amount;
	}

	public void bankrupt()
	{
		this.money = 0;
	}
	public String toString() {
		String s = "Contestant name: " + name + "\nTotal winnings: " + money;
	
		return s;
	}

}
