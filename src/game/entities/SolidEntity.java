package game.entities;

import game.graphics.Screen;

import java.awt.Rectangle;

import level.Level;

public class SolidEntity extends Entity {

	private static final long serialVersionUID = -3116586717644036975L;

	public Rectangle shadow;
	protected int width;
	protected int height;
	// public final int x, y;
	protected int[] color;

	public SolidEntity(Level level, int x, int y, int width, int height) {
		super(level);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.shadow = new Rectangle(width, (height / 2));
		this.shadow.setLocation(x, y);
		this.bounds = new Rectangle(width, (height / 2) - 8);
		this.bounds.setLocation(x, y + shadow.height);
	}

	public void tick() {

	}

	public void render(Screen screen) {

	}

	public String toString() {
		return "Solid Entity";
	}

}