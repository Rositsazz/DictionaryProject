package windowBuilder.Trie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Trie implements Dictionary {

	private Node head = null;
	private int numericalValueOfA = 97;
	private int numChildren = 0;
	private Vector<String> words;

	public Trie() {
		head = new Node();
		words = new Vector<>();
	}

	public void add(String word, String description) {
		if (word.length() == 0) {
			return;
		}
		word = word.toLowerCase();
		numChildren++;
		words.addElement(word);
		addRecursively(word, description, head);
	}

	private void addRecursively(String word, String description, Node head) {

		if (word.length() == 0) {
			head.description = description;
			return;
		}
		int charOffest = word.charAt(0) - numericalValueOfA;
		word = word.substring(1);
		Node nextHead = new Node();
		if (head.children[charOffest] == null) {
			head.children[charOffest] = nextHead;
		} else {
			nextHead = head.children[charOffest];
		}
		addRecursively(word, description, nextHead);
	}

	public boolean isEmpty() {
		return numChildren == 0;
	}

	public boolean remove(String word) {
		if (word.length() == 0) {
			return false;
		}
		String description = searchRecursively(word, head);
		numChildren--;
		if (description != null) {
			words.remove(word);
			description = null;
			return true;
		}
		return false;
	}

	public void update(String word, String description) {
		add(word, description);
	}

	public String search(String word) {
		word = word.toLowerCase();
		if (word.length() == 0) {
			return " ";
		}
		String description = searchRecursively(word, head);
		if (description != null)
			return description;
		return null;
	}

	private String searchRecursively(String word, Node node) {
		if (word.length() == 0) {
			return node.description;
		}
		int charOffest = word.charAt(0) - numericalValueOfA;
		word = word.substring(1);
		if (node.children[charOffest] != null) {
			return searchRecursively(word, node.children[charOffest]);
		}
		return null;
	}

	public int numberOfChildren() {
		return numChildren;
	}

	@SuppressWarnings("finally")
	public StringBuilder showAll() {
		StringBuilder result = new StringBuilder();
		try {

			for (String word : words) {
				result.append(word + " : " + search(word) + "\n");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	public void clear() {
		try {
			head = null;
			numChildren = 0;
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		// save the information in file
		try {

			String content = "";
			for (String word : words) {
				content += word + " :: " + search(word) + "\n";
			}

			File file = new File("words.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void read(String file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			while (line != null) {
				String[] separator = line.split(" :: ", 2);
				String word = separator[0].trim();
				String description = separator[1].trim();
				add(word, description);
				line = br.readLine();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
