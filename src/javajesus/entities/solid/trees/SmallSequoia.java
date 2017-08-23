package javajesus.entities.solid.trees;

import javajesus.entities.Entity;
import javajesus.graphics.Sprite;
import javajesus.level.Level;

/*
 * Small Sequoia Tree
 */
public class SmallSequoia extends Tree {

	/**
	 * Small Sequoia ctor()
	 * 
	 * @param level - level it is on
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public SmallSequoia(Level level, int x, int y) {
		super(level, x, y, Sprite.SMALL_SEQUOIA, 5, 8, 5);

	}

	@Override
    public byte getId(){
        return Entity.SMALL_SEQUOIA;
    }
}
