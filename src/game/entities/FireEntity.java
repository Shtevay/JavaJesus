package game.entities;

import game.graphics.Screen;
import game.graphics.SpriteSheet;

import java.awt.Rectangle;

import level.Level;

public class FireEntity extends Entity  {

	private static final long serialVersionUID = 4640952686511603038L;
	
	private long lastIterationTime;
	private int delay;
	private int xTile;
	private int yTile = 15;
	private int[] color = { 0xFFF7790A, 0xFFF72808, 0xFF000000 };

	public FireEntity(Level level, int x, int y) {
		super(level);
		this.x = x;
		this.y = y;
		this.lastIterationTime = System.currentTimeMillis();
		this.delay = 100;
		this.bounds = new Rectangle(8, 8);
		this.bounds.setLocation(x, y);
	}

	public void tick() {
		if ((System.currentTimeMillis() - lastIterationTime) >= (delay)) {
			lastIterationTime = System.currentTimeMillis();
			if (xTile < 4) {
				xTile++;
			} else {
				xTile = 0;
			}
		}

	}

	public int getXTile() {
		return xTile;
	}

	public void render(Screen screen) {

		screen.render(x, y, xTile + yTile * SpriteSheet.tiles.boxes, color, 0, 1, SpriteSheet.tiles);

	}

}