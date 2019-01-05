package Logic;

public class Wedge {
	private int price;

	Wedge(int price) {

		this.price = price;
	}

	int getPrice() {
		return price;
	}

	public String toString() {
		return "$" + price;
	}
}
