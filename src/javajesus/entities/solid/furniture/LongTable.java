package javajesus.entities.solid.furniture;

import javajesus.entities.Entity;
import javajesus.graphics.Sprite;
import javajesus.level.Level;

public class LongTable extends Furniture {
	
	// sprites used
	protected final static Sprite longCastleTable_horizontal = new Sprite("/VISUAL_DATA/STATICS/FURNITURE/long_horizontal.png");
	protected final static Sprite longCastleTable_vertical = new Sprite("/VISUAL_DATA/STATICS/FURNITURE/long_vertical.png");

	/**
	 * @param level - level it is on
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param orientation - Furniture.HORIZONTAL/VERTICAL
	 */
	public LongTable(Level level, int x, int y) {
		super(level, x, y, Furniture.VERTICAL);

	}
	
	@Override
    public byte getId(){
        return Entity.LONG_TABLE;
    }

	@Override
	protected Sprite getSprite(byte orientation) {
		switch (orientation) {
		case Furniture.HORIZONTAL:
			return longCastleTable_horizontal;
		default:
			return longCastleTable_vertical;
		}
	}
}
