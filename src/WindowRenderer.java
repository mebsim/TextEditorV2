/*
 * WindowRenderer - Creates the window, updates it and can also destroy it. Sets up the display for text rendering
 * Taken from a project of mine (https://github.com/mebsim/TextEditor/blob/master/src/render/WindowRenderer.java).
 * There are a few changes, mainly the width and fps difference. I have also fixed the error that occurs when 
 * resizing by taking out one line that was commented out and changing it a bit. I also added a constructor
 * 
 * @author (Mohamed Ebsim) 
 * @version (June 2017)
 */

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class WindowRenderer {
	
	int fps = 30; // Number of Frames per Second
	int width = 1200; // The width of the window
	int height = 850; // The height of the window
	
	public WindowRenderer(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	// Creates a new window with a given title
    public void createWindow(String title) {
    	try {
			Display.setDisplayMode(new DisplayMode(width, height)); // Creates the window
			Display.setTitle(title); // Sets the title for the window
			Display.create(); // Brings the window into existence
			Display.setResizable(true);
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) { // If it fails, shows it in the console
			System.err.println("Display failed to be created.");
			e.printStackTrace();
			Display.destroy();
			System.exit(0);
		}
    }
    
    // Updates the screen
    public void update() {
    	Display.update(); // Update the screen
   		Display.sync(fps); // just use this for now, might switch later
    }
    
    // Destroys the screen
    public void destroy() {
    	Display.destroy(); // Destroys the Display
    	// System.exit(0); // The program exits
    }
    
    // Prepares the screen with the required settings for drawing text
    public void prepare() {
    		// Enables alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		// GL11.glEnable(GL11.GL_TEXTURE_2D); // allows textures
		// GL11.glEnable(GL11.GL_SMOOTH);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // Allows Alpha
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // Clear screen color
        	
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1); // The view point
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
    
    // Updates the Viewport
    public void changeViewPoint() {
    	GL11.glViewport(0,0,Display.getWidth(), Display.getHeight()); //NEW
    	GL11.glMatrixMode(GL11.GL_PROJECTION);
    	GL11.glLoadIdentity();
    	GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
    }

    // Returns the width
	public int getWidth() {
		return width;
	}

	// Returns the height
	public int getHeight() {
		return height;
	}

}
