package javajesus.level.interior;

import java.awt.Point;

import javajesus.entities.Entity;
import javajesus.entities.Spawner;
import javajesus.entities.npcs.NPC;
import javajesus.entities.solid.furniture.Chest;
import javajesus.entities.transporters.TransporterInterior;
import javajesus.level.Level;

public class RancheroHouseInterior extends Interior {

	private static final long serialVersionUID = 7955092802065217014L;

	private Point exitPoint;

	public RancheroHouseInterior(Point point, Level level) {
		super("/Buildings/Generic Interiors/Ranchero_House_Interior.png", new Point(256, 304), level);
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
		return new Entity[] {new TransporterInterior(this, 256, 304, nextLevel, exitPoint)};
	}

}
