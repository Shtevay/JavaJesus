package game.entities.projectiles;

import game.entities.Entity;
import game.entities.Mob;
import game.entities.Player;
import game.entities.SolidEntity;
import game.entities.particles.Explosion;
import game.entities.vehicles.Vehicle;
import game.graphics.Screen;

import javax.sound.sampled.Clip;

import level.Level;

public class Missile extends Projectile {

	private static final long serialVersionUID = 4423384187877615283L;

	public Missile(Level level, double x, double y, double xPos, double yPos,
			Mob mob, double damage, Clip clip) {
		super(level, 6, 6, 1, new int[] { 0xFF000000, 0xFF5B5B5B, 0xFFFFEA02 },
				x, y, 5, xPos, yPos, mob, damage, clip);
		sound.fire(sound.revolver);
	}

	public Missile(Level level, double x, double y, int direction, Mob mob,
			double damage, Clip clip) {
		super(level, 6, 6, 1, new int[] { 0xFF000000, 0xFF5B5B5B, 0xFFFFEA02 },
				x, y, 5, direction, mob, damage, clip);
		sound.fire(sound.revolver);
		switch (direction) {
		case 0:
			this.tileNumber = 3 + 3 * sheet.boxes;
			return;
		case 1:
			this.tileNumber = 3 + 2 * sheet.boxes;
			return;
		case 2:
			this.tileNumber = 3 + 1 * sheet.boxes;
			return;
		case 3:
			this.tileNumber = 4 + 1 * sheet.boxes;
			return;
		}
	}

	public void render(Screen screen) {

		renderOnTop = true;

		if (hasCollided((int) x, (int) y)) {
			level.remEntity(this);
			level.addEntity(new Explosion(level, x, y - 8));
			return;
		}

		this.y += speed * yPoint;
		this.x += speed * xPoint;

		bounds.setLocation((int) this.x - (this.width / 2), (int) this.y
				- (this.height / 2));
		for (Entity entity : level.getEntities()) {
			if (entity instanceof SolidEntity) {
				if (bounds.intersects(((SolidEntity) entity).getBounds())) {
					level.remEntity(this);
					level.addEntity(new Explosion(level, x, y - 8));
					return;
				} else if (bounds.intersects(((SolidEntity) entity).shadow)) {
					renderOnTop = false;
				}
			}
			if (entity instanceof Mob) {
				Mob mobs = (Mob) entity;
				if (bounds.intersects(mobs.getBounds())) {
					if (mobs != mob) {
						if (mobs instanceof Vehicle) {
							mobs.damage((int) damage, (int) damage + 4);
							level.remEntity(this);
							level.addEntity(new Explosion(level, x, y - 8));
						} else if (!mobs.isDead()) {
							mobs.damage((int) damage, (int) damage + 4);
							level.remEntity(this);
							level.addEntity(new Explosion(level, x, y - 8));
							if (mobs.getHealth() < 0 && mob instanceof Player) {
								((Player) mob).score += 10;
							}
						}
					}
				}
			}

		}
		screen.render((int) this.x, (int) this.y, tileNumber, color, 1, 1,
				sheet);
	}

}