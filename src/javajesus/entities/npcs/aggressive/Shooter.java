package javajesus.entities.npcs.aggressive;

import java.awt.geom.Ellipse2D;

import javajesus.SoundHandler;
import javajesus.entities.Damageable;
import javajesus.entities.LongRange;
import javajesus.entities.Mob;
import javajesus.entities.monsters.Monster;
import javajesus.entities.npcs.NPC;
import javajesus.entities.projectiles.Bullet;
import javajesus.graphics.Screen;
import javajesus.level.Level;
import javajesus.utility.Direction;

/*
 * A friendly NPC that shoots at a long range
 */
public abstract class Shooter extends NPC implements LongRange {

	// the range the shooter will stand back when shooting
	private Ellipse2D.Double standRange;

	// Range that the monster can target another
	private Ellipse2D.Double aggroRadius;

	// the attack range radius, 32 (number of units) * 8 (units) = 256
	protected static final int RADIUS = 256;

	// cooldown from attacks
	protected boolean cooldown = true;

	// internal timer for attack cooldown
	private int attackTickCount;

	// the amount of ticks between attacks
	private static final int attackDelay = 100;

	// range of damage
	private static final int DAMAGE_RANGE = 4;

	// how long the attack position is rendered in ticks
	private static final int attackAnimationLength = 15;

	// how fast the npcs toggles steps
	private static final int WALKING_ANIMATION_SPEED = 4;

	public Shooter(Level level, String name, int x, int y, int speed, int width, int height, int defaultHealth,
	        int[] color, int xTile, int yTile, String walkPath, int walkDistance) {
		super(level, name, x, y, speed, width, height, defaultHealth, color, xTile, yTile, walkPath, walkDistance);

		// initialize the radius
		if (level != null) {
			this.aggroRadius = new Ellipse2D.Double(x - RADIUS / 2, y - RADIUS / 2, RADIUS, RADIUS);
			this.standRange = new Ellipse2D.Double(x - RADIUS / 4, y - RADIUS / 4, RADIUS / 2, RADIUS / 2);
			checkRadius();
		}
	}

	/**
	 * Updates the targeted mob
	 */
	protected void checkRadius() {

		// if the target is dead or out of range, reset the target
		if (target != null && (target.isDead() || !(aggroRadius.intersects(target.getBounds())))) {
			target.setTargeted(false);
			target = null;
		}

		// assign a new target
		if (target == null) {
			// last mob in case no targetable mob
			Mob last = null;
			for (Mob mob : getLevel().getMobs()) {
				if ((mob instanceof Monster) && aggroRadius.intersects(mob.getBounds()) && !mob.isDead()) {
					// target the mob if it is not being targeted already
					if (!mob.isTargeted()) {
						target = mob;
						mob.setTargeted(true);
						return;

						// mob already being targetted
					} else {
						last = mob;
					}
				}
			}

			// at this point, no target has been selected
			target = last;
		}

	}


	/**
	 * Updates the Shooter
	 */
	public void tick() {
		super.tick();
		checkRadius();

		// attacking cooldown loop
		if (cooldown) {
			attackTickCount++;
			isShooting = attackTickCount < attackAnimationLength;
			if (attackTickCount > attackDelay) {
				attackTickCount = 0;
				cooldown = false;
			}
		}

		// attack the target if given a chance
		if (!cooldown && target != null && aggroRadius.intersects(target.getOuterBounds())) {
			cooldown = true;
			checkDirection();
			this.attack(DAMAGE_RANGE, target);
		}

		// change in x and y
		int dx = 0, dy = 0;

		// whether or not the monster should move
		// check the bounds if the monster prefers long range or not
		if (target != null && !(getRange().intersects(target.getOuterBounds()))) {

			// move towards the target horizontally
			if (target.getX() > getX()) {
				dx++;
			} else if (target.getX() < getX()) {
				dx--;
			}

			// move towards the target vertically
			if (target.getY() > getY()) {
				dy++;
			} else if (target.getY() < getY()) {
				dy--;
			}

		}

		// move the shooter towards the target
		if ((dx != 0 || dy != 0) && !collisionStrategy.isMobCollision(dx, dy)) {
			move(dx, dy);
		}
	}

	/**
	 * Updates the direction the mob is shooting
	 */
	private void checkDirection() {

		// move towards the target horizontally
		if (target.getX() > getX()) {
			setDirection(Direction.EAST);
		} else if (target.getX() < getX()) {
			setDirection(Direction.WEST);
		}

		// move towards the target vertically
		if (target.getY() > getY()) {
			setDirection(Direction.SOUTH);
		} else if (target.getY() < getY()) {
			setDirection(Direction.NORTH);
		}
	}

	/**
	 * Displays the shooter on the screen
	 */
	public void render(Screen screen) {

		// if not shooting, render with the parent
		if (!isShooting || isSwimming) {

			super.render(screen);

		} else {

			// default color
			int[] color = getColor();

			// change color if knockback
			if (isHit) {
				color = mobHitColor;
			}

			// attack animation
			if (isShooting && !isSwimming) {

				// modifier used for rendering in different scales/directions
				int modifier = UNIT_SIZE;

				// no x or y offset, use the upper left corner as absolute
				int xOffset = getX(), yOffset = getY();

				// the horizontal position on the spritesheet
				int xTile = 14;

				// whether or not to render backwards
				boolean flip = ((numSteps >> WALKING_ANIMATION_SPEED) & 1) == 1;

				// adjust spritesheet offsets
				if (getDirection() == Direction.NORTH) {
					xTile += 8;
				} else if (isLatitudinal()) {
					xTile = +4 + (flip ? 2 : 0);
					flip = getDirection() == Direction.WEST;
				}

				// Upper body 1
				screen.render(xOffset + (modifier * (flip ? 1 : 0)), yOffset, xTile, yTile, getSpriteSheet(), flip,
				        color);

				// Upper body 2
				screen.render(xOffset + modifier - (modifier * (flip ? 1 : 0)), yOffset, xTile + 1, yTile,
				        getSpriteSheet(), flip, color);

				// Lower Body 1
				screen.render(xOffset + (modifier * (flip ? 1 : 0)), yOffset + modifier, xTile, yTile + 1,
				        getSpriteSheet(), flip, color);

				// Lower Body 2
				screen.render(xOffset + modifier - (modifier * (flip ? 1 : 0)), yOffset + modifier, xTile + 1,
				        yTile + 1, getSpriteSheet(), flip, color);
			}
		}

	}

	/**
	 * Moves a monster on the level
	 * 
	 * @param dx the total change in x
	 * @param dy the total change in y
	 */
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);

		aggroRadius.setFrame(getX() - RADIUS / 2, getY() - RADIUS / 2, RADIUS, RADIUS);
		standRange.setFrame(getX() - RADIUS / 4, getY() - RADIUS / 4, RADIUS / 2, RADIUS / 2);

	}

	/**
	 * Deals damage to another mob Calculated by getStrength() + a random number
	 * in the range
	 * 
	 * @param range - random offset to add to strength
	 * @param other - the other mob to attack
	 */
	@Override
	public void attack(int range, Damageable other) {

		// bullet offset
		int xOffset = 0, yOffset = 0;

		// offset is dependent on the direction of the shooter
		if (getDirection() == Direction.NORTH) {
			xOffset = 7;
		} else if (getDirection() == Direction.SOUTH) {
			xOffset = 7;
			yOffset = 8;
		} else if (getDirection() == Direction.WEST) {
			yOffset = 6;
		} else {
			xOffset = 8;
			yOffset = 6;
		}

		getLevel().add(new Bullet(getLevel(), getX() + xOffset, getY() + yOffset,
		        target.getX() + (int) target.getBounds().getWidth() / 2,
		        target.getY() + (int) target.getBounds().getHeight() / 2, this, getStrength() + random.nextInt(range),
		        SoundHandler.revolver));
	}

	@Override
	public Ellipse2D.Double getRange() {
		return standRange;
	}

	@Override
	public int getStrength() {
		// TODO Auto-generated method stub
		return 30;
	}

	@Override
	public int getDefense() {
		// TODO Auto-generated method stub
		return 0;
	}

}
