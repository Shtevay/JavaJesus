package ca.javajesus.game.entities.structures;

import java.awt.Point;

import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.game.gfx.SpriteSheet;
import ca.javajesus.level.Level;

public class TransporterLadder extends Transporter {

	public TransporterLadder(Level currentLevel, int x, int y,
			Level nextLevel) {
		super(currentLevel, x, y, nextLevel);
		this.color = Colors.get(-1, Colors.fromHex("#663300"),
				Colors.fromHex("#663300"), Colors.fromHex("#ffde00"));
	}

	public TransporterLadder(Level currentLevel, int x, int y,
			Level nextLevel, Point point) {
		super(currentLevel, x, y, nextLevel, point);
		this.color = Colors.get(-1, Colors.fromHex("#663300"),
				Colors.fromHex("#663300"), Colors.fromHex("#ffde00"));
	}

	public void render(Screen screen) {
		screen.render(x + 0, y - 8, 6 + 5 * 32, color, 0, 1, SpriteSheet.tiles);
		screen.render(x + 0, y + 0, 6 + 6 * 32, color, 0, 1, SpriteSheet.tiles);
	}
}