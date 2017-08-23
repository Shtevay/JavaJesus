package javajesus.entities.solid.trees;

import javajesus.entities.Entity;
import javajesus.graphics.Sprite;
import javajesus.level.Level;

public class LargeTreeWinter extends Tree {
    /**
     * Large Tree - Winter Variety ctor()
     * 
     * @param level - level it is on
     * @param x - x coordinate
     * @param y - y coordinate
     */
    public LargeTreeWinter(Level level, int x, int y) {
        super(level, x, y, Sprite.LARGE_TREE_WINTER, 14, 12, 10);
    }

    @Override
    public byte getId(){
        return Entity.LARGE_TREE_WINTER;
    }
}
