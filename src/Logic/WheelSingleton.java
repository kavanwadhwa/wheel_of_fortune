package Logic;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class WheelSingleton implements WheelBuilder {
	
	private static WheelSingleton firstInstance = null;
	private ArrayList<Wedge> wedges;
	private ArrayList<Integer> order;
	private int numWedges, index;
	private String label;
	private WheelSingleton() {
		wedges = new ArrayList<Wedge>();
		order = new ArrayList<Integer>(Arrays.asList(5000, 0, 300, 500, 450, 500, 800, -1, 700, 1, 650, 0, 900, 500, 350, 
				600, 500, 400, 550, 800, 300, 700, 900, 500));
		//order = new ArrayList<Integer>(Arrays.asList(-1, 0, -1, 0, -1, 0, -1, -1, 0, -1, 0, 0, -1, 0, 0, 
				//-1, 500, -1, 550, -1, 300, 700, 900, 500));
		numWedges = order.size();
		buildWedges();
		label = new String("");
	}
	
	public static WheelSingleton getInstance() throws FileNotFoundException {
		if(firstInstance == null) {
			firstInstance = new WheelSingleton();
		}
		return firstInstance;
	}
	
	@Override
	public void buildWedges() {
		for(int i = 0; i<24; i++) {
			wedges.add(new Wedge(order.get(i)));
		}
	}

	@Override
	public ArrayList<Wedge> getWedges() {
		// TODO Auto-generated method stub
		return wedges;
	}
	
	public String getLabelText()
	{
		return label;
	}
	public int spin(int totalAngle) {
		index = getIndex(totalAngle);
		int value = wedges.get(index).getPrice();
		switch (value) {
		case 0:
			label = "Bankrupt!";
			break;
		case -1:
			label = "Lose a Turn!";
			break;
		case 1:
			label = "Free Play!";
			break;
		default:
			label = "$" + value;
			break;
		}
		return value;
	}
	
	 int getIndex(int totalAngle) {
		index = (int)((totalAngle / getDegrees()) % getNumWedges());
		return index;
	}
	private int getDegrees() {
		return (360/numWedges);
	}
	
	private int getNumWedges()
	{
		return wedges.size();
	}
}
