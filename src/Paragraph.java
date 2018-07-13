/*
 * Paragraph - Contains "paragraphs" of text (one set of words without return). Formats the text for rendering based off width of screen
 * Contains positions of the returns that are needed for use of rendering the caret.
 * 
 * @author (Mohamed Ebsim) 
 * @version (June 2017)
 */

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.TrueTypeFont;

public class Paragraph {
	
	private String paragraph; // Regular paragraph
	private String oParagraph; // Formatted to size of page
	private List<Integer> returnPos; // Array-list of the positions of the returns in the regular text
	
	public Paragraph() {
		paragraph = "";
		oParagraph = paragraph;
		returnPos = new ArrayList<Integer>();
	}
	
	// Deletes a specified group of characters
	public void deleteSetOfLetters(int startPos, int endPos) {
		paragraph = paragraph.substring(0, startPos) + paragraph.substring(endPos);
	}
	
	// Deletes one character at a specified location
	public void deleteLetter(int pos) {
		paragraph = paragraph.substring(0, pos - 1) + paragraph.substring(pos);
	}
	
	// Adds a specified letter at a specified location
	public void addLetter(int pos, String letter) {
		paragraph = paragraph.substring(0, pos) + letter + paragraph.substring(pos);
	}
	
	// Gets the formatted words
	public String getWords() {
		return oParagraph;
	}
	
	// Gets the unformatted words
	public String getOriginalWords() {
		return paragraph;
	}
	
	// Gets the array-list of returns 
	public List<Integer> getReturns() {
		return returnPos;
	}
	
	// Sets the unformatted words
	public void setParagraph(String newParagraph) {
		paragraph = newParagraph;
	}
	
	// Gets the last return based on a given position
	public int[] getClosestReturn(int caretPos) {
		int[] pos = {0,0};
		for(int i = returnPos.size() - 1; i >= 0; i--) {
			if(caretPos >= returnPos.get(i)) {
				pos[0] = i;
				pos[1] = returnPos.get(i);
				return pos;
			}
		}
		return pos;
	}
	
	// Gets the number of returns
	public int getReturnNum() {
		return returnPos.size();
	}
	
	// Made to sort and format the words and apply appropriate breaks at appropriate times depending on window and font size
	public void sort(int width, int startPos, TrueTypeFont font) {
		// Clears existing values
		oParagraph = "";
		returnPos.clear();
		returnPos.add(0);
		int x = startPos;
		int numOfCharacters = 0;
		
		// Splits the phrase into words by splitting at all the spaces in the phrase
		String[] words = paragraph.split(" ");
		
		// Loop through all the words
		for(int i = 0; i < words.length; i++) {
			// Shifts all words that would go off the edge of the screen into a new line
			if(x != startPos) {
				if(x + font.getWidth(words[i]) > width - startPos*2) {
					words[i] = "\n" + words[i];
					x = startPos;
					returnPos.add(numOfCharacters);
					numOfCharacters -= 1;
				}
			}
			
			// Discovers when to break the lines that are larger than the entire width of the screen
			if(font.getWidth(words[i]) > width - startPos*2) {
				int x2 = startPos; // Since these words are long, they would have already been shifted into a new line
				
				// Loops through all the characters in the word
				for(int j = 0; j < words[i].length(); j++) {
					
					// Checks if the word until the jth place is longer than the screen
						// If it is, adds a break
					if(x2 + font.getWidth(String.valueOf((words[i].charAt(j)))) > width - startPos) {
						// System.out.println("Break detected."); // For Debugging
						words[i] = words[i].substring(0, j) + " \n" + words[i].substring(j);
						returnPos.add(numOfCharacters);
						j += 2; // 3 letters in " \n", but since for loop will increase by one next round, only two added.
						x2 = startPos;
					}
					x2 += font.getWidth(String.valueOf(words[i].charAt(j)));
					numOfCharacters ++; // Adds for each character
				}
				x = x2; // Sets the general x to the spot where the calculation has found that the String ends
				numOfCharacters++; // Is for the space
			} else {
				x += (font.getWidth(words[i]) % width);
				x += (font.getWidth(" "));
				numOfCharacters += words[i].length() + 1; // The plus one is for the space
			}
		}
		
		// Puts the word together in its formatted form
		for(int i = 0; i < words.length; i++)
			oParagraph += words[i] + " ";
	}
	
}
