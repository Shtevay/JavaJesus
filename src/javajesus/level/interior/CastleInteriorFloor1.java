package javajesus.level.interior;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.transporters.Stairs;
import javajesus.entities.transporters.Transporter;
import javajesus.entities.transporters.TransporterInterior;
import javajesus.level.Level;
import javajesus.utility.Direction;

public class CastleInteriorFloor1 extends Interior {

	public CastleInteriorFloor1(Point point, Level level) throws IOException {
		super("/VISUAL_DATA/STATICS/ARCHITECTURE/GENERIC/INTERIORS/Castle_Interiors/Castle_1_Living_Quarters",
		        new Point(496, 456), level);
	}

	@Override
	public Transporter[] getTransporters() throws IOException {
		return new Transporter[] { new TransporterInterior(this, 504, 472, outside),

		        // Up
		        new Stairs(this, 376, 224, new CastleBattlements(new Point(384, 320), this), Direction.SOUTH,
		                Stairs.STONE),

		        new Stairs(this, 632, 224, new CastleBattlements(new Point(656, 320), this), Direction.SOUTH,
		                Stairs.STONE),
		        // Down
		        new Stairs(this, 336, 280, new CastleInterior(new Point(360, 248), this), Direction.SOUTH,
		                Stairs.STONE),

		        new Stairs(this, 672, 280, new CastleInterior(new Point(672, 248), this), Direction.SOUTH,
		                Stairs.STONE) };
	}

}
