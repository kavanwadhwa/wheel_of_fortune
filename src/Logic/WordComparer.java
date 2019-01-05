package Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class WordComparer {
	private Map<Character, ArrayList<String>> dictionary = new TreeMap<Character, ArrayList<String>>();
	private TreeMap<Integer, ArrayList<String>> basedOnLength = new TreeMap<Integer, ArrayList<String>>();
	private TreeMap<Character, TreeMap<Integer, ArrayList<String>>> done = new TreeMap<Character, TreeMap<Integer, ArrayList<String>>>();
	private File words = RandomPhrase.fileUsed;
	private ArrayList<String> allWords = new ArrayList<String>();

	WordComparer() throws FileNotFoundException {

		for (int i = 0; i < 26; i++) {
			ArrayList<String> empty = new ArrayList<String>();
			char b = 'a';
			dictionary.put((char) (b + i), empty);
		}
		Scanner read = new Scanner(words);
		while (read.hasNextLine()) {
			String line = read.nextLine();
			String[] words = line.split("\\s+");
			for (int i = 0; i < words.length; i++) {
				if (!allWords.contains(words[i].toLowerCase())) {
					allWords.add(words[i].toLowerCase());
					dictionary.get(words[i].toLowerCase().charAt(0)).add(words[i].toLowerCase());
				}
			}
		}
		basedOnLength = new TreeMap<Integer, ArrayList<String>>();
		for (int i = 0; i < 26; i++) {
			ArrayList<String> wordsWithLetter = dictionary.get((char) ('a' + i));
			for (int j = 0; j < wordsWithLetter.size(); j++) {
				String word = wordsWithLetter.get(j);
				if (!basedOnLength.containsKey(word.length())) {
					basedOnLength.put(word.length(), new ArrayList<String>(Arrays.asList(word)));
				} else if (basedOnLength.get(word.length()).contains(word)) {

				} else {
					basedOnLength.get(word.length()).add(word);
				}
			}
			done.put((char) ('a' + i), basedOnLength);
		}
		read.close();
	}

	private ArrayList<String> findMatchingWords(BoardPhrase phrase, String regex, String word) {
		ArrayList<String> matchingWords = new ArrayList<String>();
		if (regex.substring(0, 1).matches("[a-zA-Z]")) {
			matchingWords = done.get(word.charAt(0)).get(word.length());

		} else
			matchingWords = basedOnLength.get(word.length());
		for (int i = 0; i < matchingWords.size(); i++) {
			if (!matchingWords.get(i).matches(regex)) {
				matchingWords.remove(i);
				i--;
			} else {
				// check word with incorrect chars
				HashSet<Character> wordChars = new HashSet<Character>();
				for (char a : matchingWords.get(i).toCharArray())
					wordChars.add(a);

				wordChars.retainAll(phrase.getIncorrectLetters());
				if (wordChars.size() > 0) {
					matchingWords.remove(i);
					i--;
				}
			}

		}
		return matchingWords;
	}

	/*
	 * MAKE SURE THAT YOU DO NOT INCLUDE LETTERS THAT HAVE ALREADY BEEN GUESSED!!!
	 * KEEP TRACK OF LETTERS GUESSEDi
	 */
	String odds(BoardPhrase phrase, BoardWord word, int index) {
		String distribution = "Probabilities: \n";
		ArrayList<String> matches = findMatchingWords(phrase, word.getRegex(), word.getAnswer());
		Map<Character, Integer> breakdown = new TreeMap<Character, Integer>();
		Map<Character, Double> probabilities = new TreeMap<Character, Double>();
		for (int i = 0; i < matches.size(); i++) {
			if (!breakdown.containsKey(matches.get(i).charAt(index))) {
				if (phrase.getCorrectLetters().contains(matches.get(i).charAt(index))) {
					matches.remove(matches.get(i));
					i--;
				} else
					breakdown.put(matches.get(i).charAt(index), 1);
			} else {
				if (phrase.getCorrectLetters().contains(matches.get(i).charAt(index))) {
					matches.remove(matches.get(i));
					i--;
				} else
					breakdown.put(matches.get(i).charAt(index), breakdown.get(matches.get(i).charAt(index)) + 1);
			}
		}
		ArrayList<Character> keys = new ArrayList<Character>(breakdown.keySet());


		for (int k = 0; k < breakdown.size(); k++) {
			int m = breakdown.get(keys.get(k));
			probabilities.put((Character) breakdown.keySet().toArray()[k],
					(double) ((double) m / matches.size()) * 100);
			distribution += keys.get(k) + ": " + String.format("%.2f", (double) ((double) m / matches.size()) * 100)
					+ "%" + "\n";

		}
		System.out.println(matches);
		distribution = sorted(probabilities);
		return distribution;
	}

	private String sorted(Map<Character, Double> change) {
		String sorted = "";
		Collection<Double> probabilities = change.values();
		ArrayList<Double> p = new ArrayList<Double>(probabilities);
		Collections.sort(p, Collections.reverseOrder());
		ArrayList<Character> keys = new ArrayList<Character>(change.keySet());
		ArrayList<Character> used = new ArrayList<Character>();
		int total = change.size();
		while (used.size() != total) {
			for (int m = 0; m < total + 1; m++) {
				if (used.size() != total) {
					if (change.get(keys.get(m)) == p.get(0) && !used.contains(keys.get(m))) {
						sorted += keys.get(m) + ": " + String.format("%.2f", change.remove(keys.get(m))) + "%" + "\n";
						used.add(keys.get(m));
						change.remove(keys.get(m), change.get(keys.get(m)));
						p.remove(0);
						if (total > 0)
							m = -1;
						else
							m = 0;
					}
				}
			}
		}
		return sorted;
	}
}
