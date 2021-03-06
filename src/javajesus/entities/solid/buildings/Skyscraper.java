package javajesus.entities.solid.buildings;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Entity;
import javajesus.entities.transporters.Door;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.SkyscraperLobby;

/*
 * A large skyscraper!
 */
public class Skyscraper extends Building {

	/**
	 * Creates a skyscraper
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 * @throws IOException 
	 */
	public Skyscraper(Level level, int x, int y) throws IOException {
		super(level, x, y, new int[] { 0xFF111111, 0xFF673101, 0xFFABD3FF }, Sprite.skyscraper);

		if (level != null)
		level.add(new Door(level, x + 38, y + 232, new SkyscraperLobby(new Point(x + 44, y + 243), level),0,0));
	}

	@Override
    public byte getId(){
        return Entity.SKYSCRAPER;
    }
}
