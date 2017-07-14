package javajesus.entities.solid.buildings;

import java.awt.Point;

import javajesus.entities.transporters.Transporter;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.ProjectsLobby;

/*
 * Some building that northway made
 */
public class Projects extends Building {

	// serialization
	private static final long serialVersionUID = -7645682813842777028L;

	/**
	 * Creates a projects building??
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 */
	public Projects(Level level, int x, int y) {
		super(level, x, y, new int[] { 0xFF111111, 0xFF93DA85, 0xFF6CB1FE }, Sprite.projects);

		level.add(new Transporter(level, x + 44, y + 64, new ProjectsLobby(new Point(x + 45, y + 73), level)));
	}

}