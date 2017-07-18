package javajesus.entities.vehicles;

import javajesus.level.Level;

/*
 * A car for driving
 */
public class CenturyLeSabre extends Car {

	// serialization
	private static final long serialVersionUID = -6160110452474389163L;
	
	// used for offsetting the bounds based on direction
	private static final int SHORT_SIDE = 32, LONG_SIDE = 40;

	/**
	 * Creates a Century Le Sabre
	 * 
	 * @param level - level it is on
	 * @param x - x position
	 * @param y - y position
	 */
	public CenturyLeSabre(Level level, int x, int y) {
		super(level, "Century LeSabre", x, y, SHORT_SIDE, LONG_SIDE, 0);
	}
}
