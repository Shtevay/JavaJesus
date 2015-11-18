package game.entities.structures;

import game.entities.SolidEntity;
import game.entities.structures.transporters.Transporter;
import game.graphics.Screen;
import game.graphics.Sprite;

import java.awt.Point;

import level.Level;
import level.interior.PoorHouseInterior;

public class QuackerHQ extends SolidEntity {

	public QuackerHQ(Level level, int x, int y) {
		super(level, x, y, 136, 99);
		level.addEntity(new Transporter(level, x + 53, y + 83,
				new PoorHouseInterior(new Point(x + 40, y + 67), this.level)));
	}

	public void render(Screen screen) {

		screen.render((int) x, (int) y, new int[] {0xFF111111, 0xFFFFFFFF, 0xFF4D4DFF }, Sprite.quacker_hq);

	}
}