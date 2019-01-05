package Logic;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RandomPhrase {
	public static ArrayList<File> files;
	public static File fileUsed;
	private Random random;
	private String category;
	
	 RandomPhrase()
	{
		files = new ArrayList<File>();
		random = new Random();
		
		category = "";
	}
	
	 String getCategory()
	{
		return category;
	}
	private void populateFiles()
	{
		files.add(new File("TextFiles/Landmarks.txt"));
		files.add(new File("TextFiles/Sports.txt"));
		files.add(new File("textFiles/Food And Drink.txt"));
		//files.add(new File("TextFiles/Music.txt"));
		
	}
	
	
	 String getARandomPhrase() throws IOException {
		this.populateFiles();
		String phrase = "";
	
		int fileNumber = random.nextInt(files.size());
		fileUsed = files.get(fileNumber);
		BufferedReader reader = new BufferedReader(new FileReader(fileUsed));
		int lines = 0;
		while (reader.readLine() != null) lines++;
		
		int place = random.nextInt(lines);
		int count = 0;
		BufferedReader reader2 =  new BufferedReader(new FileReader(files.get(fileNumber)));
		category = files.get(fileNumber).getName();
		category = category.substring(0, category.length()-4);
		String nextLine = reader2.readLine();
		while(nextLine != null) 
		{
			if(count==place)
			{	
				phrase = nextLine.trim();
				nextLine = null;	
			}
			else
			{
				count++;
				nextLine = reader2.readLine();
			}
		}
		reader.close();
		reader2.close();
		
		return phrase;
	}
}

