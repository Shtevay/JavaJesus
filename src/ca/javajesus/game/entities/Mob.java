package ca.javajesus.game.entities;

import java.awt.Rectangle;
import java.util.Random;

import ca.javajesus.game.SoundHandler;
import ca.javajesus.game.entities.particles.HealthBar;
import ca.javajesus.game.entities.structures.Transporter;
import ca.javajesus.game.entities.vehicles.Vehicle;
import ca.javajesus.game.gfx.SpriteSheet;
import ca.javajesus.level.Level;
import ca.javajesus.level.tile.Tile;

public abstract class Mob extends Entity {

	protected String name;
	protected double speed;
	protected int numSteps = 0;
	public boolean isMoving;
	public int movingDir = 1;
	protected int scale = 1;
	public double velocity;
	public int width;
	public int height;
	public double health;
	public double startHealth;
	public HealthBar bar;
	public Rectangle hitBox;
	protected SpriteSheet sheet;
	public boolean hasDied;
	public boolean onFire = false;
	protected int healthTickCount = 0;
	protected int lastDirection = 0;
	protected boolean movingRandomly = false;
	protected double scaledSpeed;
	public boolean isTargeted = false;
	public Rectangle standBox;
	protected boolean isAvoidingCollision = false;
	public boolean renderOnTop = false;

	public int strength, defense, accuracy, evasion;

	private Random random = new Random();

	public Mob(Level level, String name, double x, double y, int speed,
			int width, int height, SpriteSheet sheet, double defaultHealth) {
		super(level);
		this.name = name;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.health = defaultHealth;
		this.startHealth = defaultHealth;
		this.hitBox = new Rectangle(width, height);
		this.standBox = new Rectangle(width + 4, height + 4);
		this.sheet = sheet;
		this.defense = 1;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public double getStartHealth() {
		return startHealth;
	}

	public void move(int xa, int ya, double speed) {
		if (xa != 0 && ya != 0) {
			move(xa, 0, speed);
			move(0, ya, speed);
			numSteps--;
			return;
		}
		velocity = this.speed * speed;
		numSteps++;
		if (!hasCollided(xa, ya)) {
			if (ya < 0)
				movingDir = 0;
			if (ya > 0)
				movingDir = 1;
			if (xa < 0)
				movingDir = 2;
			if (xa > 0)
				movingDir = 3;

			x += xa * velocity;
			y += ya * velocity;
		}
	}

	public abstract boolean hasCollided(int xa, int ya);

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	protected boolean isSolidTile(int xa, int ya, int x, int y) {
		if (level == null) {
			return false;
		}
		int xx = (int) this.x;
		int yy = (int) this.y;
		Tile lastTile = level.getTile((xx + x) >> 3, (yy + y) >> 3);
		Tile newTile = level.getTile((xx + x + xa) >> 3, (yy + y + ya) >> 3);
		if (!lastTile.equals(newTile) && newTile.isSolid()) {
			return true;
		}
		return false;
	}

	protected boolean isDamageTile(int xa, int ya, int x, int y) {
		if (level == null) {
			return false;
		}
		int xx = (int) this.x;
		int yy = (int) this.y;
		Tile lastTile = level.getTile((xx + x) >> 3, (yy + y) >> 3);
		Tile newTile = level.getTile((xx + x + xa) >> 3, (yy + y + ya) >> 3);
		if (!lastTile.equals(newTile) && newTile.isSolid()) {
			return true;
		}
		return false;
	}

	protected boolean isWaterTile(int xa, int ya, int x, int y) {
		if (level == null) {
			return false;
		}
		int xx = (int) this.x;
		int yy = (int) this.y;
		Tile lastTile = level.getTile((xx + x) >> 3, (yy + y) >> 3);
		Tile newTile = level.getTile((xx + x + xa) >> 3, (yy + y + ya) >> 3);
		if (!lastTile.equals(newTile) && newTile.equals(Tile.WATER)) {
			return true;
		}
		return false;
	}

	protected boolean isSolidEntityCollision(int xa, int ya) {
		renderOnTop = true;
		for (Entity entity : level.getEntities()) {
			if (entity instanceof SolidEntity) {
				if (!(entity instanceof Transporter)) {
					if (this.hitBox.intersects(((SolidEntity) entity).shadow)) {
						renderOnTop = false;
					}
				}
				Rectangle temp = new Rectangle(
						((SolidEntity) entity).bounds.width,
						((SolidEntity) entity).bounds.height - 8);
				temp.setLocation(
						(int) ((SolidEntity) entity).bounds.x - 2 * xa,
						(int) ((SolidEntity) entity).bounds.y - 2 * ya);
				if (this.hitBox.intersects(temp))
					return true;
			} else if (entity instanceof Vehicle && entity != this) {
				Rectangle temp = new Rectangle(((Vehicle) entity).hitBox.width,
						((Vehicle) entity).hitBox.height - 8);
				temp.setLocation((int) ((Vehicle) entity).x - 2 * xa,
						(int) ((Vehicle) entity).y - 2 * ya);
				if (this.hitBox.intersects(temp))
					return true;
			}

		}
		return false;
	}

	protected boolean isMobCollision() {
		if (level == null) {
			return false;
		}
		for (Mob mob : level.getMobs()) {
			if (mob == this)
				continue;
			if (this.hitBox.intersects(mob.hitBox) && !mob.isAvoidingCollision)
				return true;
		}
		return false;
	}

	protected boolean isMobCollision(int xa, int ya) {
		for (Mob mob : level.getMobs()) {
			if (mob != this) {
				Rectangle temp = new Rectangle(mob.hitBox.width,
						mob.hitBox.height);
				temp.setLocation((int) mob.x - 2 * xa, (int) mob.y - 2 * ya);
				if (this.hitBox.intersects(temp))
					return true;
			}

		}
		return false;
	}

	protected void moveAroundMobCollision() {

		int xa = 0;
		int ya = 0;

		for (Mob mob : level.getMobs()) {
			if (mob == this)
				continue;

			Rectangle intersection = hitBox.intersection(mob.hitBox);
			double xx = intersection.getCenterX();
			double yy = intersection.getCenterY();
			if ((int) xx > (int) this.hitBox.getCenterX()) {
				xa--;
			}
			if ((int) xx < (int) this.hitBox.getCenterX()) {
				xa++;
			}
			if ((int) yy > (int) this.hitBox.getCenterY()) {
				ya--;
			}
			if ((int) yy < (int) this.hitBox.getCenterY()) {
				ya++;
			}

			if (xa != 0 || ya != 0) {
				break;
			}
		}
		if (xa != 0 || ya != 0) {
			move(xa, ya, scaledSpeed);
			isAvoidingCollision = true;
			isMoving = true;
		} else {
			isMoving = false;
		}
	}

	public String toString() {
		return name;
	}

	/** Triggers the death animation and closure */
	public void kill() {

		hasDied = true;
		level.remEntity(this);
		this.isTargeted = false;
	}

	public void checkTile(double x, double y) {
		if (level.getTile((int) x / 8, (int) y / 8) == Tile.FIRE) {
			onFire = true;
			healthTickCount = 0;
		}
	}

	public void damage(int a, int b) {
		int damage = random.nextInt(b - a + 1) + a;
		damage -= defense;
		if (damage > 0)
			this.health -= damage;
	}

	public void damage(int a) {
		int damage = a;
		damage -= defense;
		if (damage > 0)
			this.health -= damage;
	}
}
