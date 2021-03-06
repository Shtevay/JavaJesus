package javajesus.entities.transporters;

import javajesus.graphics.Screen;
import javajesus.graphics.SpriteSheet;
import javajesus.level.Level;

/*
 * A ladder for in caves
 */
public class Ladder extends Transporter {

	// color set of the ladder
	private static final int[] color = { 0xD5D9DA00, 0xD5D9DA00, 0xFFFFDE00 };
	
	// dimensions of the transporter
	private static final int WIDTH = 8, HEIGHT = 16;

	/**
	 * Creates a Transporter to change the level
	 * @param currentLevel the level it is on
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param outside the new level
	 */
	public Ladder(Level currentLevel, int x, int y, Level nextLevel) {
		super(currentLevel, x - 8, y, WIDTH, HEIGHT, nextLevel);
	}

	/**
	 * Displays the ladder to the screen
	 */
	public void render(Screen screen) {
		screen.render(getX(), getY(), 6, 0, SpriteSheet.stairs, false, color);
		screen.render(getX(), getY() + 8, 6, 1, SpriteSheet.stairs, false, color);
	}
}