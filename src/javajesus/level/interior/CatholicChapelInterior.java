package javajesus.level.interior;

import java.awt.Point;

import javajesus.entities.Entity;
import javajesus.entities.Spawner;
import javajesus.entities.npcs.NPC;
import javajesus.entities.solid.furniture.Chest;
import javajesus.entities.transporters.TransporterInterior;
import javajesus.level.Level;

public class CatholicChapelInterior extends Interior {

	private static final long serialVersionUID = 5172539623347110557L;

	private Point exitPoint;

	public CatholicChapelInterior(Point point, Level level) {
		super("/Buildings/Generic Interiors/Catholic_Chapel_Interior.png", new Point(248, 264), level);
		this.exitPoint = point;
	}

	protected NPC[] getNPCPlacement() {
		return null;
	}

	protected Spawner[] getSpawnerPlacement() {
		return null;
	}

	protected Chest[] getChestPlacement() {
		return null;
	}

	protected Entity[] getOtherPlacement() {
		return new Entity[] {new TransporterInterior(this, 256, 278, nextLevel, exitPoint)};
	}

}
