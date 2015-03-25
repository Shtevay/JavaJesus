package ca.javajesus.game.entities;

import java.awt.Rectangle;

import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.game.gfx.SpriteSheet;
import ca.javajesus.level.Level;

public class FireEntity extends Entity {

	private long lastIterationTime;
	private int delay;
	private int xTile;
	private int yTile = 15;
	private int color = Colors.get(-1, Colors.fromHex("#F7790A"), 540, -1);
	private final Rectangle hitBox = new Rectangle(8, 8);

	public FireEntity(Level level, int x, int y) {
		super(level);
		this.x = x;
		this.y = y;
		this.lastIterationTime = System.currentTimeMillis();
		this.delay = 100;
		this.hitBox.setLocation(x, y);
	}
	
	public Rectangle getBounds() {
		return hitBox;
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
