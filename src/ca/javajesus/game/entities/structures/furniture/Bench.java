package ca.javajesus.game.entities.structures.furniture;

import ca.javajesus.level.Level;

public class Bench extends Furniture{
	public Bench(Level level, int x, int y) {
		super(level, x, y, Furniture.bench, new int[] {444, 123, 323});
		this.bounds.setSize(getSprite().xSize - 8, getSprite().ySize);
		this.shadow.setSize(0, 0);
		this.bounds.setLocation(x, y);
	}
}
