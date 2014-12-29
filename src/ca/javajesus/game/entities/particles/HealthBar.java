package ca.javajesus.game.entities.particles;

import ca.javajesus.game.entities.Mob;
import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.level.Level;

public class HealthBar extends Particle {

	private int yOffset;

	private static int healthBarColour = Colors.get(-1, 111, -1, 400);
	private Mob mob;

	public HealthBar(Level level, int tileNumber, double x, double y, Mob mob) {
		super(level, tileNumber, healthBarColour, x, y);
		this.mob = mob;
		this.yOffset = 0;
	}

	public HealthBar(Level level, int tileNumber, double x, double y, Mob mob,
			int yOffset) {
		super(level, tileNumber, healthBarColour, x, y);
		this.mob = mob;
		this.yOffset = yOffset;
	}

	public void render(Screen screen) {

		this.x = mob.x;
		this.y = mob.y + 8;

		screen.render(this.x + 3, this.y, tileNumber + 14 * 32, color, 1, 1,
				sheet);
		screen.render(this.x - 5, this.y, tileNumber + 1 + 14 * 32, color, 1,
				1, sheet);
	}

	public void setOffset(int yTileOffset) {
		this.tileNumber = yTileOffset * 32;
	}
	
	public void updateHealthBar(double health, double startHealth) {
		if ((health > 11 * startHealth / 12.0) && (health <= startHealth)) {
			setOffset(2);
		} else if ((health > 10 * startHealth / 12.0)
				&& (health <= 11 * startHealth / 12.0)) {
			setOffset(3);
		} else if ((health > 9 * startHealth / 12.0)
				&& (health <= 10 * startHealth / 12.0)) {
			setOffset(4);
		} else if ((health > 8 * startHealth / 12.0)
				&& (health <= 9 * startHealth / 12.0)) {
			setOffset(5);
		} else if ((health > 7 * startHealth / 12.0)
				&& (health <= 8 * startHealth / 12.0)) {
			setOffset(6);
		} else if ((health > 6 * startHealth / 12.0)
				&& (health <= 7 * startHealth / 12.0)) {
			setOffset(7);
		} else if ((health > 5 * startHealth / 12.0)
				&& (health <= 6 * startHealth / 12.0)) {
			setOffset(8);
		} else if ((health > 4 * startHealth / 12.0)
				&& (health <= 5 * startHealth / 12.0)) {
			setOffset(9);
		} else if ((health > 3 * startHealth / 12.0)
				&& (health <= 4 * startHealth / 12.0)) {
			setOffset(10);
		} else if ((health > 2 * startHealth / 12.0)
				&& (health <= 3 * startHealth / 12.0)) {
			setOffset(11);
		} else if ((health > 100 / 12.0) && (health <= 200 / 12.0)) {
			setOffset(12);
		} else {
			setOffset(13);
		} if(health <= 0) {
			level.remEntity(this);
		}
	}
}