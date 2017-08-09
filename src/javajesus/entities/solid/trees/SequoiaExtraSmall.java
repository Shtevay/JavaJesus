package javajesus.entities.solid.trees;

import javajesus.entities.Entity;
import javajesus.graphics.Sprite;
import javajesus.level.Level;

public class SequoiaExtraSmall extends Tree
{
    /**
     * Extra Small Sequoia ctor()
     * 
     * @param level - level it is on
     * @param x - x coordinate
     * @param y - y coordinate
     */
    public SequoiaExtraSmall(Level level, int x, int y) {
        super(level, x, y, Sprite.SEQUOIA_EXTRA_SMALL, 4, 8, 2);
    }

    // need to add in the entity ID
    @Override
    public byte getId(){
        return Entity.SEQUOIA_EXTRA_SMALL;
    }

}