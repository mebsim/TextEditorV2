/*
 * TextRenderer - It renders the text and the number of characters onto the screen. It also stores the font.
 * 
 * @author (Mohamed Ebsim) 
 * @version (June 2017)
 */

import java.awt.Font;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class TextRenderer {
	
	private TrueTypeFont font;
	
	// Creates the font
	public TextRenderer(int fontSize) {
		Font awtFont = new Font("Monoco 11", Font.PLAIN, fontSize); // Creates a font using the "Monoco 11" font, plain (non-italicized & non-bold) of this font-size
		font = new TrueTypeFont(awtFont, true); // Uses settings of font above this, selects anti-aliasing
	}
	
	// Returns the font
	public TrueTypeFont getFont() {
		return font;
	}
	
	// Renders the number of characters into the corner of the screen
	public void drawNumOfWords(int num) {
		font.drawString((Display.getWidth()-font.getWidth(Integer.toString(num))), (Display.getHeight()-font.getHeight()), Integer.toString(num), Color.black);
	}
	
	// Renders text onto the screen, renders the caret
	public void draw(List<Paragraph> paragraphs, int[] caret) {
		// Set the x and y to the top left corner (along with a little spacing)
		int x = 10;
		int y = 10;
		int[] caretPos;
		
		// Goes through all the paragraphs
		for(int i = 0; i < paragraphs.size(); i++) {
			
			// Splits the text into an array of words
			String[] pSplit = paragraphs.get(i).getWords().split(" ");
			
			// Caret rendering
			if(i == caret[0]) {
				caretPos = paragraphs.get(i).getClosestReturn(caret[1]);
				font.drawString(10 + font.getWidth(paragraphs.get(i).getOriginalWords().substring(caretPos[1], caret[1]))-1, y + (font.getHeight()*caretPos[0])+3, "|", Color.black);
				font.drawString(10 + font.getWidth(paragraphs.get(i).getOriginalWords().substring(caretPos[1], caret[1]))-1, y + (font.getHeight()*caretPos[0])-3, "|", Color.black);
			}
			
			// Loop through all the words in the array
			for(String s:pSplit) {
				if(!s.contains("\n")) { // Checks if it should return
					font.drawString(x, y, (s + " "), Color.black);
					x += font.getWidth((s + " "));
				} else {
					x = 10;
					y += font.getHeight();
					font.drawString(x, y, (s.substring(1, s.length()) + " "), Color.black);
					x += font.getWidth(s.substring(1, s.length()) + " ");
				}
			}
			y += font.getHeight();
			x = 10;
		}
	}
	
}
