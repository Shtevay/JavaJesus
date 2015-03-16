package ca.javajesus.game.entities.structures;

import java.awt.Rectangle;

import ca.javajesus.game.Game;
import ca.javajesus.game.entities.SolidEntity;
import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.game.gfx.Sprite;
import ca.javajesus.level.Level;
import ca.javajesus.level.RandomLevel;

public class SanCiscoSkyscraper extends SolidEntity {

	protected int color = Colors.get(-1, Colors.fromHex("#edece0"),
			Colors.fromHex("#ffffff"), Colors.fromHex("#030074"));

	public SanCiscoSkyscraper(Level level, int x, int y) {
		super(level, x, y, 106, 338);
		this.shadow = new Rectangle(width, (8 * height / 9));
		this.shadow.setLocation(x, y);
		this.bounds = new Rectangle(width, (height / 9) - 8);
		this.bounds.setLocation(x, y + shadow.height);
		level.addEntity(new TransporterGlass(level, x + 44, y + 660,
				new RandomLevel(Game.WIDTH, Game.HEIGHT)));
		level.addEntity(new TransporterGlass(level, x + 100, y + 660,
				new RandomLevel(Game.WIDTH, Game.HEIGHT)));
		level.addEntity(new TransporterGlass(level, x + 156, y + 660,
				new RandomLevel(Game.WIDTH, Game.HEIGHT)));
	}

	public void render(Screen screen) {

		screen.render(x, y, color, Sprite.sanCisco_skyscraper);

	}

}
