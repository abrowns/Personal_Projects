/*
 * File: SpellingBee.java
 * ----------------------
 * This program contains the starter file for the SpellingBee application.
 * 
 * to run this app, at the terminal, type:
 * 	javac SpellingBee.java
 * 	java SpellingBee
 * 
 * Original Author: Ken Whitener
 * Edited by: 		Andrew Brown
 */

import java.awt.Color; // import the Color class
import java.io.File; // import file class
import java.io.FileNotFoundException; // import File not found class
import java.util.ArrayList;
import java.util.Random; // import array list class
import java.util.Scanner; // random class for the shuffle method

public class SpellingBee {

	public void run() {
		// create a new SpellingBeeGraphics object. Use this to call methods to add
		// fields and buttons
		sbg = new SpellingBeeGraphics();

		/*
		 * WORKFLOW:
		 * ========
		 * 1. Add a field or button depending on the need: sbg.addField or sbg.addButton
		 * 2. Define the action that should be taken when the field or button is used
		 * 3. The action will then be defined as a method with the same name
		 * 4. Add the action to the field or button
		 * 
		 * Below are two examples of how to add a field and a button
		 */

		// add a new text field with the name "Puzzle" and the action "puzzleAction"
		// "puzzleAction" should be defined as a method that takes a string as a
		// parameter
		sbg.addField("Puzzle", (s) -> puzzleAction(s));
		sbg.addField("Guess Word", (s) -> guessWordAction(s));

		// add a new Button with the name "Solve" and the action "solveAction"
		// "solveAction" should be defined as a method that takes no parameters
		sbg.addButton("Solve", (s) -> solveAction());
		sbg.addButton("Clear screen", (s) -> clearScreen()); // button to clear the screen
		sbg.addButton("Shuffle", (s) -> shuffleAction()); // shuffle button

	}

	private void clearScreen() {
		usedWords.clear();
		sbg.clearWordList();
		sbg.showMessage(""); // made my own clear screen method to clear entire screen

		guessPoints = 0;
		guessedWords = 0; // resets points and number of words
	}

	ArrayList<String> usedWords = new ArrayList<>(); // sets up a new list to keep track of already used words

	int guessPoints = 0; // sets up the users points for guessing
	int guessedWords = 0; // sets up the amount of users guessed words

	private void guessWordAction(String s) {
		sbg.showMessage(""); // clear message

		String guessWord = sbg.getField("Guess Word"); // assigns the word in the guess word field
		String puzzleLetters = sbg.getField("Puzzle"); // assigns puzzle letters

		guessWord = guessWord.toLowerCase();
		puzzleLetters = puzzleLetters.toLowerCase(); // makes sure both are lowercase in order to correctly compare

		ArrayList<String> validWords = validWordList(puzzleLetters);

		boolean isValidWord = false; // initializes the valid word with false
		int valWordInd = 0;
		for (int wordInd = 0; wordInd < validWords.size(); ++wordInd) {
			String validWord = validWords.get(wordInd);
			if (validWord.equals(guessWord)) { // compares the current valid word with the guessed word
				isValidWord = true; // if the word is valid, boolean condition is set to true and moves on
				valWordInd = wordInd; // also gets the word index from the valid word list to get the score from score
										// list
				break;
			}
		}

		if (isValidWord == false) {
			String failReason = failCheck(guessWord);
			sbg.showMessage(failReason, Color.RED);
		}

		String beehiveLetters = sbg.getBeehiveLetters();
		ArrayList<Integer> scoresList = scoreList(beehiveLetters);

		for (int Ind = 0; Ind < usedWords.size(); ++Ind) {
			String usedWord = usedWords.get(Ind);
			if (usedWord.equals(guessWord)) { // compares the guessed words with the current word being guessed
				isValidWord = false;
				// makes sure no words are used more than once
				sbg.showMessage("The word was already used", Color.RED);
			}

		}

		if (isValidWord == true) {
			int score = scoresList.get(valWordInd); // gets the score from score list

			if (score > guessWord.length()) {
				sbg.addWord(guessWord + " (" + score + ")", Color.BLUE);
				usedWords.add(guessWord);
			} else {
				sbg.addWord(guessWord + " (" + score + ")");
				usedWords.add(guessWord); // keeps track of guessed words only if they are valid
			}
			guessPoints += score;
			guessedWords += 1; // keeping track of all points and number of guessed words

			sbg.showMessage(guessedWords + " words guessed; " + guessPoints + " points", Color.BLACK);
		}

		sbg.clearField("Guess Word"); // clears the guess word feild after everything

	}

	private String failCheck(String word) { // returns the reason a guessed word is false

		if (word.length() < 4) { // makes sure there is at least 4 letters in the word
			String failReason = "The word does not include at least 4 letters";
			return failReason;
		}

		String beehiveLetters = sbg.getBeehiveLetters();
		beehiveLetters = beehiveLetters.toLowerCase(); // makes sure beehive letters are lowercase for comparison

		for (int lettInd = 0; lettInd < word.length(); ++lettInd) {
			for (int beeInd = 0; beeInd < beehiveLetters.length(); ++beeInd) {
				if (beehiveLetters.charAt(beeInd) == word.charAt(lettInd)) { // if letters match beehive letters, moves
																				// on to next beehive letter
					break;
				} else if (beeInd == beehiveLetters.length() - 1) {
					// if the beehive index makes it to the last letter and it does not break, it
					// means the letter was not in the beehive
					String failReason = "The word includes letters not in beehive";
					return failReason;
				}
			}
		}

		char centerLetter = beehiveLetters.charAt(0); // gets center letter of beehive
		for (int lettInd = 0; lettInd < word.length(); ++lettInd) {
			if (word.charAt(lettInd) == centerLetter) { // if any letter equals the center letter, it breaks out of the
														// loop
				break;
			}
			if (lettInd == word.length() - 1) { // if the loop makes it here without breaking, it means the center
												// letter isn't in the word
				String failReason = "The word does not include center letter";
				return failReason;
			}
		}

		String failReason = "The word is not in the dictionary";
		return failReason; // if it makes it this far without returning anything else, it means the word
							// wasn't in the dictionary

	}

	ArrayList<String> wordDict = wordsToList(); // creates the word list from created method for biggest scope

	// define the puzzleAction method that will execute when
	// you hit "ENTER" after clicking in the "Puzzle" field
	private void puzzleAction(String s) {

		guessPoints = 0;
		guessedWords = 0;
		usedWords.clear(); // resets score and points and used words list

		clearScreen(); // modified to clear entire screen

		if (isValidPuzzle(s)) {
			sbg.setBeehiveLetters(s);
		} else {
			sbg.showMessage("Invalid puzzle initialization", Color.RED); // isValidPuzzle() returned false
		}

	}

	private boolean isValidPuzzle(String lettSequence) {
		if (lettSequence.length() != 7) {
			return false;
		}
		for (int ind = 0; ind < lettSequence.length(); ++ind) { // loop through entered letter sequence to initialize
																// puzzle
			if (!Character.isLetter(lettSequence.charAt(ind))) { // I'm watching the video to get a walkthrough,
				return false;
			} // but pausing when you ask how do we think we can solve the problem so I can
				// come up with my own solutions

		}
		for (int ind = 0; ind < lettSequence.length(); ++ind) { // looping through every char to check all of them for
																// dupes
			for (int ind2 = ind + 1; ind2 < lettSequence.length(); ++ind2) { // looping through every char with current
																				// char being checked
				// index2 = index1 + 1 because it can't be able to check the same letter it's
				// checking a copy for
				// also the char. behind it doesn't matter because it would check that before
				// anyway
				if (lettSequence.charAt(ind2) == lettSequence.charAt(ind)) {
					return false;
				} // I came up with this and currently typing this before hearing your solution in
					// the video (it's fun to come up with these solutions on my own)
					// I actually didn't think we'd come up with the same solution lol
			}
		}

		return true; // all if statements return false, therfor true

	}

	// define the solveAction method that will execute when
	// you click the "Solve" button. It does not get the contents of the PUZZLE
	// field
	private void solveAction() {
		sbg.clearWordList(); // clears screen before adding anything
		guessPoints = 0;
		guessedWords = 0;
		usedWords.clear(); // resets score and points and used words list

		String beehiveLetters = sbg.getBeehiveLetters(); // current beehive letters assignment
		ArrayList<String> validWords = validWordList(beehiveLetters); // gets list of all valid based on puzzle letters
		ArrayList<Integer> scoresList = scoreList(beehiveLetters); // gets list of all scores and total score

		for (int wordNum = 0; wordNum < validWords.size(); ++wordNum) {
			String word = validWords.get(wordNum); // sets current word in valid list
			int score = scoresList.get(wordNum); // gets the score for that word

			if (score > word.length()) {
				sbg.addWord(word + " (" + score + ")", Color.BLUE); // if the score is bigger than the length of the
																	// word that means it is a pangram bonus
			} else
				sbg.addWord(word + " (" + score + ")"); // adds word to screen along with the score

			// the total score isn't iterated through because it's only looping through the
			// size of the word list which means it stops before the total score
		}

		int totalScore = scoresList.get(scoresList.size() - 1); // gets the total score
		int totalWords = validWords.size(); // gets total words in list

		String message = totalWords + " words; " + totalScore + " points";

		sbg.showMessage(message, Color.BLACK);

	}

	private void shuffleAction() {

		String beehiveLetters = sbg.getBeehiveLetters();
		Random random = new Random(); // 'random' instance for random integer use

		ArrayList<Character> beehive = new ArrayList<>(); // creates a new list to move the letters around

		for (int beeInd = 0; beeInd < beehiveLetters.length(); ++beeInd) {
			beehive.add(beehiveLetters.charAt(beeInd));
		} // added all characters to the beehive list

		for (int beeInd = 1; beeInd < beehive.size(); ++beeInd) { // loops through the list to shuffle but makes sure
																	// the middle character stays the same
			int randInt = random.nextInt(beehiveLetters.length()); // gets a random Integer between 0 and the length of
																	// the beehive letters -1
			if (randInt != 0) { // makes sure that the random int doesn't choose the first letter
				char oldLetter = beehive.get(beeInd); // gets the letter of the current position in the loop
				char newLetter = beehive.get(randInt); // gets the letter of the position from the random int
				beehive.set(randInt, oldLetter);
				beehive.set(beeInd, newLetter); // switches both and keeps going til all the letters have been switched
												// around
			}
		}

		String newBeehive = ""; // creates new beehive string to add all the shuffled letters to

		for (int beeInd = 0; beeInd < beehive.size(); ++beeInd) {
			newBeehive += beehive.get(beeInd);
		}

		sbg.setBeehiveLetters(newBeehive);

	}

	/* Constants */
	// The name of the file containing the puzzle dictionary
	private static final String ENGLISH_DICTIONARY = "EnglishWords.txt";

	/* Private instance variables */
	private SpellingBeeGraphics sbg; // no instance created yet, just scoped the reference variable

	private ArrayList<String> wordsToList() { // reads English words from text file, and puts them into a list

		ArrayList<String> wordList = new ArrayList<>();
		try {
			File engDict = new File("EnglishWords.txt");
			Scanner engWords = new Scanner(engDict);
			while (engWords.hasNextLine()) { // loops through every line adding every word from dict file to a list
				String word = engWords.nextLine();
				if (word.length() >= 4)
					wordList.add(word); // quick check for 4 letter words minimum to add to list
			}
			engWords.close();

		} catch (FileNotFoundException fnf) {
			System.out.println("File Not found");
		}
		return wordList; // returns the list of words
	}

	private ArrayList<String> validWordList(String puzzle) { // creates list of valid words from dictionary based on
																// given puzzle letters

		ArrayList<String> validWords = new ArrayList<>(); // create blank list for valid words

		for (int ind = 0; ind < wordDict.size(); ++ind) { // loops through the length (size) of the word dict
			String word = (String) wordDict.get(ind); // sets current word in loop to a variable

			puzzle = puzzle.toLowerCase(); // converts puzzle letters to lowercase for comparison

			int validLettCount = 0; // initiates valid letter counter
			for (int letterInd = 0; letterInd < word.length(); ++letterInd) { // loops through all letters in the
																				// current word
				for (int puzzInd = 0; puzzInd < puzzle.length(); ++puzzInd) { // loops through puzzle letters to check
																				// if they're used
					if (word.charAt(letterInd) == puzzle.charAt(puzzInd)) {
						++validLettCount; // keeps count of the letters that match
						break; // doesn't have to continue if the letters match
					}

				}

			}

			char middleChar = puzzle.charAt(0); // sets first character in puzzle to a variable (middle letter in
												// beehive)
			boolean useMiddle = false; // initiates use middle with false
			for (int letterInd = 0; letterInd < word.length(); ++letterInd) {
				if (word.charAt(letterInd) == middleChar) {
					useMiddle = true; // sets useMiddle to true if any of the letters in the word are the middle
										// letter
				}
			}

			if (validLettCount == word.length() && useMiddle == true)
				validWords.add(word);
			// if valid letter count is the same number as the length of the word it means
			// all letters are valid
			// also has to use middle letter anywhere in the word for validity
		}

		return validWords;
	}

	// creates a list of the scores from the valid word list in order
	// also adds total score to the end of the list
	private ArrayList<Integer> scoreList(String puzzle) {

		puzzle = puzzle.toLowerCase();
		ArrayList<String> validWords = validWordList(puzzle); // creates a list of valid words based on puzzle letters
		ArrayList<Integer> scoreList = new ArrayList<>(); // initializes new list of word scores

		int totalScore = 0; // initalizes total score of all words

		for (int wordInd = 0; wordInd < validWords.size(); ++wordInd) { // loops through the valid word list

			int wordScore = 1; // initializes the score of each word to 1 for default
			String word = validWords.get(wordInd); // current word in loop
			if (word.length() > 4) {
				wordScore = word.length(); // if the word is more than 4 letters the score is the length of the word
			}
			int letterCount = 0; // initializes letter count
			for (int puzzInd = 0; puzzInd < puzzle.length(); ++puzzInd) {

				for (int lettInd = 0; lettInd < word.length(); ++lettInd) {
					// loops through puzzle letters then the words' letters to check if all puzzle
					// letters are being used

					if (word.charAt(lettInd) == puzzle.charAt(puzzInd)) {
						++letterCount; // keeps track of how many letters from the puzzle are used in the word
						break; // continues with the puzzle letter count so only 1 of each letter is counted
					}

				}

			}
			if (letterCount == puzzle.length())
				wordScore += 7; // adds 7 to the word score if all letters from the puzzle are used

			scoreList.add(wordScore); // adds score to list

			totalScore += wordScore; // keeps track of the total score after every score is calculated

		}

		scoreList.add(totalScore);

		return scoreList;
	}

}
