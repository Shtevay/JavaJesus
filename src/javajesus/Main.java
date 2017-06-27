package javajesus;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.SplashScreen;
import java.io.File;

import engine.GameEngine;
import javajesus.utility.JJStrings;

/**
 * @author Derek
 * 
 * This is the main driver of JavaJesus
 * It initializes the sound and launcher
 */
public class Main {

	/**
	 * Main Method
	 * 
	 * @param args run time arguments
	 */
	public static void main(String[] args) {

		try {
			
			// create a splash loading screen
			SplashScreen splash = SplashScreen.getSplashScreen();
	        splash.createGraphics();
	        splash.update();

			// start the launcher and sound
			SoundHandler.initialize();
			
			// load the custom font
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/GUI/Font/PressStart2P.ttf")));
			
			// initialize a launcher
			new GameEngine(JJStrings.NAME, JavaJesus.WINDOW_WIDTH, JavaJesus.WINDOW_HEIGHT, new Launcher())
			.start();
			
			// close the loading screen
			splash.close();

			// report any errors
		} catch (Exception e) {
			System.err.println("Launcher Failed to Start!");
			e.printStackTrace();
			
			System.exit(-1);
		}

	}

}
