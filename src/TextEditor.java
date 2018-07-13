/*
 * TextEditor - The main class. Handles the input, the caret and the main loop.
 * 
 * @author (Mohamed Ebsim) 
 * @version (June 2017)
 */

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

public class TextEditor {
	
	static List<Paragraph> text; // An array-list of all the paragraphs
	static int[] caret = new int[2]; // An array of the caret's location [index of paragraph that the caret is in, position in the unformatted text]
	static WindowRenderer wm;
	static int displayWidth; // To check for changes in the display width
	static int displayHeight; // To check for changes in the display height
	static int numOfCharacters = 0; // To count number of characters
	static final int MAX_CHARACTERS = 256; // To limit how many characters are writable
	
	// This methods deals with the input. It will manipulate the paragraphs and their contents depending on which command is given
	public static void input(String i, TrueTypeFont font) {
		// If you still have characters left until you hit the limit
		if(numOfCharacters != MAX_CHARACTERS) {
			if(i.equals("DELETE")) {
				// Checks for deleting when at the beginning of a paragraph (needs to fall into the previous paragraph)
				if(caret[1] == 0 && caret[0] != 0) {
					caret[1] = text.get(caret[0] - 1).getOriginalWords().length();
					text.get(caret[0]-1).addLetter(text.get(caret[0]-1).getOriginalWords().length(), text.get(caret[0]).getOriginalWords());
					text.remove(caret[0]);
					caret[0]--;
					text.get(caret[0]).sort(Display.getWidth(), 10, font);
					numOfCharacters--;
				} else if(!text.get(text.size() - 1).getOriginalWords().isEmpty() && caret[1] != 0) { // Generic deleting of character relative to 
					text.get(caret[0]).deleteLetter(caret[1]);
					text.get(caret[0]).sort(Display.getWidth(), 10, font);
					caret[1]--;
					numOfCharacters--;
				}
			} else if(i.equals("RETURN")) { // Checks for return
				text.add(caret[0] + 1, new Paragraph());
				caret[0] ++;
				if(caret[1] != text.get(caret[0]).getOriginalWords().length() - 1) { // Checks if you are returning with the paragraph instead of at the end
					text.get(caret[0]).setParagraph(text.get(caret[0]-1).getOriginalWords().substring(caret[1]));
					text.get(caret[0]-1).deleteSetOfLetters(caret[1], text.get(caret[0]-1).getOriginalWords().length());
					text.get(caret[0]-1).sort(Display.getWidth(), 10, font);
				}
				text.get(caret[0]).sort(Display.getWidth(), 10, font);
				caret[1] = 0;
				numOfCharacters++;
			} else if(i.equals("TAB") && numOfCharacters <= MAX_CHARACTERS - 4) { // Adds for spaces
				text.get(caret[0]).addLetter(caret[1], "    ");
				text.get(caret[0]).sort(Display.getWidth(), 10, font);
				numOfCharacters += 4;
				caret[1] += 4;
			} else if(i.equals("RIGHT")) { // Moves caret right
				if(caret[1] == text.get(caret[0]).getOriginalWords().length() && caret[0] != text.size() - 1) {
					caret[0]++;
					caret[1] = 0;
				} else if(caret[1] != text.get(caret[0]).getOriginalWords().length()) {
					caret[1]++;
				}
			} else if(i.equals("LEFT")) { // Moves caret left
				if(caret[1] == 0 && caret[0] != 0) {
					caret[0]--;
					caret[1] = text.get(caret[0]).getOriginalWords().length();
				} else if(caret[1] != 0) {
					caret[1]--;
				}
			} else if(!i.equals("") && !i.equals("TAB")) { // It is any letter in general
				text.get(caret[0]).addLetter(caret[1], i);
				text.get(caret[0]).sort(Display.getWidth(), 10, font);
				caret[1] += i.length();
				numOfCharacters += i.length();
			}
		} else { // If you've maxed out on characters
			if(i.equals("DELETE")) { // Deleting letters to make more space
				// Checks for deleting when at the beginning of a paragraph (needs to fall into the previous paragraph)
				if(caret[1] == 0 && caret[0] != 0) {
					caret[1] = text.get(caret[0] - 1).getOriginalWords().length();
					text.get(caret[0]-1).addLetter(text.get(caret[0]-1).getOriginalWords().length(), text.get(caret[0]).getOriginalWords());
					text.remove(caret[0]);
					caret[0]--;
					text.get(caret[0]).sort(Display.getWidth(), 10, font);
					numOfCharacters--;
				} else if(!text.get(text.size() - 1).getOriginalWords().isEmpty() && caret[1] != 0) { // Generic deleting of character relative to 
					text.get(caret[0]).deleteLetter(caret[1]);
					text.get(caret[0]).sort(Display.getWidth(), 10, font);
					caret[1]--;
					numOfCharacters--;
				}
			} else if(i.equals("RIGHT")) { // Moves caret right
				if(caret[1] == text.get(caret[0]).getOriginalWords().length() && caret[0] != text.size() - 1) {
					caret[0]++;
					caret[1] = 0;
				} else if(caret[1] != text.get(caret[0]).getOriginalWords().length()) {
					caret[1]++;
				}
			} else if(i.equals("LEFT")) { // Moves caret left
				if(caret[1] == 0 && caret[0] != 0) {
					caret[0]--;
					caret[1] = text.get(text.size() - 1).getOriginalWords().length();
				} else if(caret[1] != 0) {
					caret[1]--;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		// Prepare the window for rendering
		wm = new WindowRenderer(1200, 850);
		wm.createWindow("Text Editor"); // Name for the moment
		wm.prepare();
		displayWidth = Display.getWidth();
		
		// Initialize various key components
		KB kb = new KB();
		TextRenderer textRenderer = new TextRenderer(12);
		text = new ArrayList<Paragraph>();
		
		// This is where the file is set up and loaded in.
		text.add(new Paragraph());
		
		// Set up caret for any size text
		caret[0] = text.size() - 1;
		caret[1] = text.get(text.size() - 1).getOriginalWords().length();
		
		// Preparing all the text for rendering
		for(Paragraph p:text)
			p.sort(Display.getWidth(), 10, textRenderer.getFont());
		
		// Main loop
		while(!Display.isCloseRequested()) {
			// Clear screen
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			// Get input
			String i = kb.checkKeys();
			input(i, textRenderer.getFont());
			
			// Check if screen changed sizes
			if(displayWidth != Display.getWidth() || displayHeight != Display.getHeight()) {
				for(Paragraph p:text)
					p.sort(Display.getWidth(), 10, textRenderer.getFont());
				displayWidth = Display.getWidth();
				displayHeight = Display.getHeight();
				wm.changeViewPoint();
			}
			
			// Render text + caret
			textRenderer.draw(text, caret);
			textRenderer.drawNumOfWords(numOfCharacters);
			
			// Update screen
			wm.update();
		}
		
		// Close window
		wm.destroy();
	}
	
}