package javajesus.entities.solid.trees;

import javajesus.entities.Entity;
import javajesus.graphics.Sprite;
import javajesus.level.Level;

public class LargeTree extends Tree {
    /**
     * Large Tree - Generic Variety ctor()
     * 
     * @param level - level it is on
     * @param x - x coordinate
     * @param y - y coordinate
     */
    public LargeTree(Level level, int x, int y) {
        super(level, x, y, Sprite.LARGE_TREE, 14, 12, 13);
    }

    @Override
    public byte getId(){
        return Entity.LARGE_TREE;
    }

}
