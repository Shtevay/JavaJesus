package ca.javajesus.game.entities;

import ca.javajesus.game.Game;
import ca.javajesus.game.InputHandler;
import ca.javajesus.game.entities.monsters.Demon;
import ca.javajesus.game.entities.projectiles.Bullet;
import ca.javajesus.game.entities.vehicles.Vehicle;
import ca.javajesus.game.entities.weapons.GreatSword;
import ca.javajesus.game.entities.weapons.Sword;
import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.game.gfx.SpriteSheet;
import ca.javajesus.items.Inventory;
import ca.javajesus.level.Level;
import ca.javajesus.level.Level1;

public class Player extends Mob {

	private InputHandler input;
	private int colour = Colors.get(-1, 111, 300, 543);
	private int scale = 1;
	protected boolean isSwimming = false;
	public boolean isSwinging = false;
	protected boolean isShooting = false;
	private int tickCount = 0;
	private boolean canChangeLevel;
	private Level nextLevel;
	private double scaledSpeed;
	private int swingTickCount = 0;
	private boolean cooldown = true;
	public int gunType = 4;
	private boolean genericCooldown;
	public boolean isDriving;
	protected Vehicle vehicle;
	protected int shootingDir;
	public int score;
	private Sword sword;
	private Inventory inventory;

	public Player(Level level, double x, double y, InputHandler input) {
		super(level, "player", x, y, 1, 14, 16, SpriteSheet.player, 100);
		this.input = input;
		this.score = 0;
		sword = new GreatSword(level, this);
		this.inventory = new Inventory();
	}

	public double getPlayerVelocity() {
		return velocity;
	}

	public Level getLevel() {
		if (level == null) {
			return new Level1("/levels/water_test_level.png");
		}
		return level;
	}

	public void changeLevel(Level level) {
		if (input.e.isPressed()) {
			this.nextLevel = level;
			this.canChangeLevel = true;
		}
	}

	public void tick() {

		if (!isDriving) {
			updateHealth();
		}

		int xa = 0;
		int ya = 0;
		if (input.t.isPressed()) {
			if (!genericCooldown) {
				level.addEntity(new Demon(level, "Demon", (int) this.x,
						(int) this.y, 1));
			}
			genericCooldown = true;
		}
		if (input.up.isPressed() || input.down.isPressed()
				|| input.left.isPressed() || input.right.isPressed()) {
			if (!isSwinging && !isSwimming && !isDriving)
				isShooting = true;
		}
		if (input.space.isPressed()) {
			if (!isShooting && !isSwimming && !isDriving && !isSwinging) {
				isSwinging = true;
				level.addEntity(sword);
			}
		}

		if (input.w.isPressed()) {
			ya--;
		}
		if (input.s.isPressed()) {
			ya++;
		}
		if (input.a.isPressed()) {
			xa--;
		}
		if (input.d.isPressed()) {
			xa++;
		}
		if (input.i.isPressed()) {
			Game.displayInventory();
		}
		if (input.up.isPressed()) {
			shootingDir = 0;
		}
		if (input.down.isPressed()) {
			shootingDir = 1;
		}
		if (input.left.isPressed()) {
			shootingDir = 2;
		}
		if (input.right.isPressed()) {
			shootingDir = 3;
		}
		if (input.e.isPressed()) {
			if (!genericCooldown) {
				if (isDriving) {
					isDriving = false;
					this.x -= 20;
					vehicle.isUsed = false;
					level.addEntity(this.bar);
				} else {
					for (Entity entity : level.getEntities()) {
						if (entity instanceof Vehicle) {
							if (this.hitBox
									.intersects(((Vehicle) entity).hitBox)) {
								this.vehicle = (Vehicle) entity;
								this.x = vehicle.x;
								this.y = vehicle.y;
								isDriving = true;
								vehicle.isUsed = true;
								level.remEntity(this.bar);
							}
						}
					}
				}
			}
			genericCooldown = true;
		}
		if (isSwimming) {
			scaledSpeed = 0.35;
		} else if (input.shift.isPressed() && !isDriving) {
			scaledSpeed = 3;
		} else if (isDriving && input.shift.isPressed()) {
			scaledSpeed = 7;
		} else if (isDriving) {
			scaledSpeed = 5;
		} else {
			scaledSpeed = 1;
		}

		if ((xa != 0 || ya != 0) && !isSolidEntityCollision(xa, ya)
				&& !isDriving) {
			move(xa, ya, scaledSpeed);
			isMoving = true;
		} else if (isDriving && (xa != 0 || ya != 0) && !vehicle.isSolidEntityCollision(xa * 2, ya * 2)) {
			vehicle.isMoving = true;
			move(xa, ya, scaledSpeed);
			isMoving = true;
		} else {
			isMoving = false;
			if (isDriving)
				vehicle.isMoving = false;
		}
		int xx = (int) x;
		int yy = (int) y;
		if (level.getTile(xx >> 3, yy >> 3).getId() == 3) {
			isSwimming = true;
		}
		if (isSwimming && level.getTile(xx >> 3, yy >> 3).getId() != 3) {
			isSwimming = false;
		}
		tickCount++;

		// The current time in ticks in which an attack is started
		if (isSwinging || isShooting) {
			swingTickCount++;
		}

		// Swinging cooldown
		if (swingTickCount > 200) {
			isSwinging = false;
			swingTickCount = 0;
		} else if (isShooting && swingTickCount % 10 == 0) {
			cooldown = false;
		} else {
			cooldown = true;
		}

		if (genericCooldown) {
			if (tickCount % 100 == 0) {
				genericCooldown = false;
			}
		}

	}

	public void render(Screen screen) {

		if (isDriving) {
			this.vehicle.x = this.x;
			this.vehicle.y = this.y;
			this.vehicle.movingDir = this.movingDir;
			if (this.vehicle.hasDied) {
				isDriving = false;
				vehicle.isUsed = false;
			}
			return;
		}

		this.hitBox.setLocation((int) this.x, (int) this.y - 8);
		this.standBox.setLocation((int) this.x - 2, (int) this.y - 10);
		if (canChangeLevel) {
			level.remEntity(this);
			level.remEntity(bar);
			init(nextLevel);
			screen.getGame().updateLevel();
			level.init();
			canChangeLevel = false;
			level.addEntity(this);
			level.addEntity(bar);
		}

		int xTile = 0;
		int yTile = 0;
		int walkingAnimationSpeed = 4;
		if (scaledSpeed == 3) {
			numSteps++;
		}

		int flipTop = (numSteps >> walkingAnimationSpeed) & 1;
		int flipBottom = (numSteps >> walkingAnimationSpeed) & 1;

		if (movingDir == 0) {
			xTile += 10;
			if (!isMoving) {
				xTile = 8;
			}
		}
		if (movingDir == 1) {
			xTile += 2;
			if (!isMoving) {
				xTile = 0;
			}
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingAnimationSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
			flipBottom = (movingDir - 1) % 2;
			if (!isMoving) {
				xTile = 4;
			}
		}

		int modifier = 8 * scale;
		double xOffset = x - modifier / 2.0;
		double yOffset = y - modifier / 2.0 - 4;

		// Handles swimming animation
		if (isSwimming) {
			int waterColour = 0;
			yOffset += 4;
			if (tickCount % 60 < 15) {
				waterColour = Colors.get(-1, 225, -1, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				yOffset -= 1;
				waterColour = Colors.get(-1, 115, 225, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				waterColour = Colors.get(-1, 115, -1, -1);
			} else {
				yOffset -= 1;
				waterColour = Colors.get(-1, 225, 225, -1);
			}
			screen.render(xOffset, yOffset + 3, 0 + 10 * 32, waterColour, 0x00,
					1, sheet);
			screen.render(xOffset + 8, yOffset + 3, 0 + 10 * 32, waterColour,
					0x01, 1, sheet);
		}

		// Handles fire animation
		if (onFire) {
			int fireColour = 0;
			if (tickCount % 60 < 15) {
				fireColour = Colors.get(Colors.toHex("#F51F07"),
						Colors.toHex("#F7790A"), 540, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				fireColour = Colors.get(Colors.toHex("#F51F07"),
						Colors.toHex("#F7790A"), 540, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				fireColour = Colors.get(Colors.toHex("#F51F07"),
						Colors.toHex("#F7790A"), 540, -1);
			} else {
				fireColour = Colors.get(Colors.toHex("#F51F07"),
						Colors.toHex("#F7790A"), 540, -1);
			}
			screen.render(xOffset, yOffset + 3, 0 + 11 * 32, fireColour, 0x00,
					1, sheet);
			screen.render(xOffset + 8, yOffset + 3, 0 + 11 * 32, fireColour,
					0x01, 1, sheet);
		}

		// Normal Player movement -- Not Attacking Anything
		if (!isShooting) {
			// Upper body 1
			screen.render(xOffset + (modifier * flipTop), yOffset, xTile
					+ yTile * 32, colour, flipTop, scale, sheet);
			// Upper Body 2
			screen.render(xOffset + modifier - (modifier * flipTop), yOffset,
					(xTile + 1) + yTile * 32, colour, flipTop, scale, sheet);

			if (!isSwimming) {
				// Lower Body 1
				screen.render(xOffset + (modifier * flipBottom), yOffset
						+ modifier, xTile + (yTile + 1) * 32, colour,
						flipBottom, scale, sheet);
				// Lower Body 2
				screen.render(xOffset + modifier - (modifier * flipBottom),
						yOffset + modifier, (xTile + 1) + (yTile + 1) * 32,
						colour, flipBottom, scale, sheet);

			}
		}

		// Handles Shooting Animation
		if (isShooting) {
			xTile = gunType;
			yTile = 2;

			if (shootingDir == 1) {
				yTile += 2;
			}
			if (shootingDir == 0) {
				yTile += 2;
				xTile += 16;
			} else if (shootingDir > 1) {
				xTile += 4 + ((numSteps >> walkingAnimationSpeed) & 1) * 2;
				flipTop = (shootingDir - 1) % 2;
				flipBottom = (shootingDir - 1) % 2;
			}

			// Upper Body 1
			screen.render(xOffset + (modifier * flipTop), yOffset, xTile
					+ yTile * 32, colour, flipTop, scale, sheet);
			// Upper Body 2
			screen.render(xOffset + modifier - (modifier * flipTop), yOffset,
					(xTile + 1) + yTile * 32, colour, flipTop, scale, sheet);

			// Lower Body 1
			screen.render(xOffset + (modifier * flipBottom),
					yOffset + modifier, xTile + (yTile + 1) * 32, colour,
					flipBottom, scale, sheet);

			// Lower Body 2
			screen.render(xOffset + modifier - (modifier * flipBottom), yOffset
					+ modifier, (xTile + 1) + (yTile + 1) * 32, colour,
					flipBottom, scale, sheet);

			if (!cooldown) {
				int bulletOffset = 0;
				if (shootingDir == 2) {
					bulletOffset = -7;
				}
				if (shootingDir == 0) {
					bulletOffset += 1;
				}
				if (shootingDir == 1) {
					bulletOffset -= 11;
				}

				level.addEntity(new Bullet(level, (this.x + 1 + bulletOffset),
						(this.y - 2), shootingDir, this));
				isShooting = false;
				swingTickCount = 0;
			}

		}

	}

	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;

		if (isDriving) {
			for (int x = xMin; x < xMax; x++) {
				if (isSolidTile(xa, ya, x, yMin)
						|| isWaterTile(xa, ya, x, yMin)) {
					return true;
				}
			}
			for (int x = xMin; x < xMax; x++) {
				if (isSolidTile(xa, ya, x, yMax)
						|| isWaterTile(xa, ya, x, yMax)) {
					return true;
				}
			}
			for (int y = yMin; y < yMax; y++) {
				if (isSolidTile(xa, ya, xMin, y)
						|| isWaterTile(xa, ya, xMin, y)) {
					return true;
				}
			}
			for (int y = yMin; y < yMax; y++) {
				if (isSolidTile(xa, ya, xMax, y)
						|| isWaterTile(xa, ya, xMax, y)) {
					return true;
				}
			}
		}

		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMin)) {
				return true;
			}
		}
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMax)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMin, y)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMax, y)) {
				return true;
			}
		}

		return false;
	}
}
