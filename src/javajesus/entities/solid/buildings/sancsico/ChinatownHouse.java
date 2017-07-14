package javajesus.entities.solid.buildings.sancsico;

import java.awt.Point;

import javajesus.entities.solid.buildings.Building;
import javajesus.entities.transporters.Transporter;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.ChinatownHouseInterior;

/*
 * A typical house in Chinatown
 */
public class ChinatownHouse extends Building {

	// serialization
	private static final long serialVersionUID = 2403522342845736281L;

	/**
	 * Creates a chinatown house
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 */
	public ChinatownHouse(Level level, int x, int y) {
		super(level, x, y, new int[] { 0xFF618249, 0xFF992B2B, 0xFFFFFFFF }, Sprite.chinatown_house);

		// change the bounds
		setBounds(getBounds().x + 5, getBounds().y, getBounds().width - 10, getBounds().height);

		level.add(new Transporter(level, x + 26, y + 41,
		        new ChinatownHouseInterior(new Point(x + 32, y + 53), getLevel())));
	}

}