package ca.javajesus.game.entities.structures;

import java.awt.Point;

import ca.javajesus.game.entities.SolidEntity;
import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.game.gfx.Sprite;
import ca.javajesus.level.Level;
import ca.javajesus.level.interior.PoorHouseInterior;

public class GenericHospital extends SolidEntity {

	protected int color = Colors.get(-1, 111, Colors.fromHex("#fffab0"),
			Colors.fromHex("#abd3ff"));

	public GenericHospital(Level level, int x, int y) {
		super(level, x, y, 78, 67);
		level.addEntity(new Transporter(level, x + 38, y + 50,
				new PoorHouseInterior(new Point(x + 40, y + 67), this.level)));
	}

	public void render(Screen screen) {

		screen.render((int) x, (int) y, color, Sprite.generic_hospital);

	}

}
