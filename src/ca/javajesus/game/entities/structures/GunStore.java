package ca.javajesus.game.entities.structures;

import java.awt.Point;

import ca.javajesus.game.entities.SolidEntity;
import ca.javajesus.game.entities.structures.transporters.Transporter;
import ca.javajesus.game.graphics.Screen;
import ca.javajesus.game.graphics.Sprite;
import ca.javajesus.level.Level;
import ca.javajesus.level.interior.GunStoreInterior;
import ca.javajesus.level.interior.PoorHouseInterior;

public class GunStore extends SolidEntity {

	public GunStore(Level level, int x, int y) {
		super(level, x, y, 70, 42);
		level.addEntity(new Transporter(level, x + 29, y + 26,
				new GunStoreInterior(new Point(x + 35, y + 37), this.level)));
	}

	public void render(Screen screen) {

		screen.render((int) x, (int) y, new int[] { 0xFF111111, 0xFFFFFAB0, 0xFFABD3FF }, Sprite.gunstore);

	}

}
