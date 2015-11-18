package game.entities.npcs;

import game.ChatHandler;
import game.entities.Mob;
import game.entities.Player;
import game.entities.npcs.aggressive.Knight;
import game.entities.npcs.aggressive.Shooter;
import game.entities.particles.HealthBar;
import game.graphics.JJFont;
import game.graphics.Screen;
import game.graphics.SpriteSheet;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import level.Level;
import quests.Quest;

public class NPC extends Mob {

	private static final long serialVersionUID = 7279751732700782799L;

	/** Range that the NPC can walk */
	protected Ellipse2D.Double walkRadius;
	protected final int RADIUS = 32 * 8;
	protected int xTile;
	protected int yTile;

	/** Movement type and the distance they travel */
	protected String walkPath;
	protected int walkDistance;

	/** NPC origin */
	protected double xPos;
	protected double yPos;

	/** Determines direction for NPC movement */
	protected boolean dir1 = true;
	protected boolean dir2;
	protected boolean dir3;
	protected boolean dir4;

	public ArrayList<Quest> quests = new ArrayList<Quest>();
	public Quest currentQuest;

	protected boolean movingToOrigin = false;

	public enum Gender {
		MALE, FEMALE, BOY, GIRL
	}

	public NPC(Level level, String name, int x, int y, int speed, int width,
			int height, int defaultHealth, int[] color, int xTile, int yTile,
			String walkPath, int walkDistance) {
		super(level, name, x, y, speed, width, height, SpriteSheet.npcs,
				defaultHealth);
		this.walkRadius = new Ellipse2D.Double(x - RADIUS / 2, y - RADIUS / 2,
				RADIUS, RADIUS);
		this.color = color;
		this.xTile = xTile;
		this.yTile = yTile;
		this.walkPath = walkPath;
		this.walkDistance = walkDistance;
		this.xPos = x;
		this.yPos = y;
		this.setHitBox(new Rectangle(width, height));
		this.bar = new HealthBar(level, this.x, this.y, this);
		if (level != null)
			level.addEntity(bar);
	}

	public void tick() {
		super.tick();
		if (tickCount > 360) {
			tickCount = 0;
			movingToOrigin = true;
		}
		if (!(this instanceof Shooter) && (tickCount % 2 == 0)) {
			if (script != null && !script.isCompleted())
				script.moveToPoint();
			else if (movingToOrigin)
				findOrigin();
			else {
				findPath();
			}
		}
	}

	protected void findOrigin() {

		if (xPos == this.x && yPos == this.y) {
			movingToOrigin = false;
			return;
		}
		int xa = 0;
		int ya = 0;
		if (xPos > this.x) {
			xa++;
		}
		if (xPos < this.x) {
			xa--;
		}
		if (yPos > this.y) {
			ya++;
		}
		if (yPos < this.y) {
			ya--;
		}
		if ((xa != 0 || ya != 0) && !isSolidEntityCollision(xa, ya)
				&& !isMobCollision(xa, ya)) {
			setMoving(true);
			move(xa, ya);
		} else {
			setMoving(false);
		}
	}

	protected void findPath() {
		switch (walkPath) {
		case "linear": {
			moveLinear();
			break;
		}
		case "triangle": {
			moveTriangle();
			break;
		}
		case "square": {
			moveSquare();
			break;
		}
		case "cross": {
			moveCross();
			break;
		}
		case "circle": {
			moveCircle();
			break;
		}
		default:
			break;
		}
	}

	public void render(Screen screen) {
		super.render(screen);
		int xTile = this.xTile;
		int yTile = this.yTile;

		int walkingAnimationSpeed = 4;

		int flip = (numSteps >> walkingAnimationSpeed) & 1;

		if (getDirection() == Direction.NORTH) {
			xTile += 8;
		} else if (isLatitudinal(getDirection())) {
			xTile += 4 + ((numSteps >> walkingAnimationSpeed) & 1) * 2;
			if (getDirection() == Direction.WEST) {
				flip = 1;
			} else {
				flip = 0;
			}
		}

		int modifier = 8 * scale;
		int xOffset = x - modifier;
		int yOffset = y - modifier;

		if (isDead) {
			xTile = 12;
			isShooting = false;
		}

		if (isShooting && !isDead && !isSwimming && !(this instanceof Knight)) {

			xTile = 14;

			if (getDirection() == Direction.NORTH) {
				xTile += 8;
			}
			if (isLatitudinal(getDirection())) {
				xTile += 4 + ((numSteps >> walkingAnimationSpeed) & 1) * 2;
				if (getDirection() == Direction.WEST) {
					flip = 1;
				} else {
					flip = 0;
				}
			}

			// Upper body 1
			screen.render(xOffset + (modifier * flip), yOffset, xTile + yTile
					* sheet.boxes, this.color, flip, scale, sheet);

			// Upper body 2
			screen.render(xOffset + modifier - (modifier * flip), yOffset,
					(xTile + 1) + yTile * sheet.boxes, this.color, flip, scale,
					sheet);

			// Lower Body 1
			screen.render(xOffset + (modifier * flip), yOffset + modifier,
					xTile + (yTile + 1) * sheet.boxes, this.color, flip, scale,
					sheet);

			// Lower Body 2
			screen.render(xOffset + modifier - (modifier * flip), yOffset
					+ modifier, (xTile + 1) + (yTile + 1) * sheet.boxes,
					this.color, flip, scale, sheet);
		} else if (isShooting && !isDead && !isSwimming) {

			xTile = 14;

			if (getDirection() == Direction.NORTH) {
				xTile += 14;
			} else if (getDirection() == Direction.SOUTH) {
				xTile += 12;
			} else {
				if (getDirection() == Direction.WEST) {
					flip = 1;
				} else {
					flip = 0;
				}
			}

			if (isLatitudinal(getDirection())) {
				for (int i = 0; i < 2; i++) {

					screen.render(xOffset + (2 * modifier * flip), yOffset
							+ (modifier * i),
							xTile + (yTile + i) * sheet.boxes, color, flip,
							scale, sheet);

					screen.render(xOffset + modifier, yOffset + (modifier * i),
							(xTile + 1) + (yTile + i) * sheet.boxes, color,
							flip, scale, sheet);

					screen.render(xOffset + 2 * modifier
							- (2 * modifier * flip), yOffset + (modifier * i),
							(xTile + 2) + (yTile + i) * sheet.boxes, color,
							flip, scale, sheet);
				}
			} else {
				// Upper body 1
				screen.render(xOffset + (modifier * flip), yOffset, xTile
						+ yTile * sheet.boxes, this.color, flip, scale, sheet);

				// Upper body 2
				screen.render(xOffset + modifier - (modifier * flip), yOffset,
						(xTile + 1) + yTile * sheet.boxes, this.color, flip,
						scale, sheet);

				// Lower Body 1
				screen.render(xOffset + (modifier * flip), yOffset + modifier,
						xTile + (yTile + 1) * sheet.boxes, this.color, flip,
						scale, sheet);

				// Lower Body 2
				screen.render(xOffset + modifier - (modifier * flip), yOffset
						+ modifier, (xTile + 1) + (yTile + 1) * sheet.boxes,
						this.color, flip, scale, sheet);

				// Lower Body 1
				screen.render(xOffset + (modifier * flip), yOffset + 2
						* modifier, xTile + (yTile + 2) * sheet.boxes,
						this.color, flip, scale, sheet);

				// Lower Body 2
				screen.render(xOffset + modifier - (modifier * flip), yOffset
						+ 2 * modifier,
						(xTile + 1) + (yTile + 2) * sheet.boxes, this.color,
						flip, scale, sheet);
			}

		} else {

			if (!isSwimming) {
				// Lower Body 1
				screen.render(xOffset + (modifier * flip), yOffset + modifier,
						xTile + (yTile + 1) * sheet.boxes, color, flip, scale,
						sheet);
				// Lower Body 2
				screen.render(xOffset + modifier - (modifier * flip), yOffset
						+ modifier, (xTile + 1) + (yTile + 1) * sheet.boxes,
						color, flip, scale, sheet);

			}

			// Upper body 1
			screen.render(xOffset + (modifier * flip), yOffset, xTile + yTile
					* sheet.boxes, color, flip, scale, sheet);
			// Upper Body 2
			screen.render(xOffset + modifier - (modifier * flip), yOffset,
					(xTile + 1) + yTile * sheet.boxes, color, flip, scale,
					sheet);
		}

		if (currentQuest != null && !isTalking) {
			JJFont.render("?", screen, (int) xOffset + 4, (int) yOffset - 10,
					new int[] { 0xFF000000, 0xFF000000, 0xFFFFCC00 }, 1);
		}

	}

	private void moveLinear() {
		int xa = 0;
		int ya = 0;
		if (dir1) {
			xa++;
			if (this.x > this.walkDistance + xPos) {
				dir1 = false;
				dir2 = true;
			}
		} else if (dir2) {
			xa--;
			if (this.x < xPos - this.walkDistance) {
				dir1 = true;
				dir2 = false;
			}
		}
		if ((xa != 0 || ya != 0) && !isSolidEntityCollision(xa, ya)
				&& !isMobCollision(xa, ya)) {
			move(xa, ya);
			setMoving(true);
		} else {
			setMoving(false);
		}

	}

	private void moveTriangle() {
		int xa = 0;
		int ya = 0;
		if (dir1) {
			xa++;
			if (this.x > this.walkDistance + xPos) {
				dir1 = false;
				dir2 = true;
			}
		} else if (dir2) {
			xa--;
			ya--;
			if (this.x < xPos) {
				dir2 = false;
				dir3 = true;
			}
		} else if (dir3) {
			xa--;
			ya++;
			if (this.x < xPos - this.walkDistance) {
				dir3 = false;
				dir1 = true;
			}
		}
		if ((xa != 0 || ya != 0) && !isSolidEntityCollision(xa, ya)
				&& !isMobCollision(xa, ya)) {
			move(xa, ya);
			setMoving(true);
		} else {
			setMoving(false);
		}

	}

	private void moveSquare() {
		int xa = 0;
		int ya = 0;
		if (dir1) {
			xa++;
			if (this.x > this.walkDistance + xPos) {
				dir1 = false;
				dir2 = true;
			}
		} else if (dir2) {
			ya++;
			if (this.y > this.walkDistance + yPos) {
				dir2 = false;
				dir3 = true;
			}
		} else if (dir3) {
			xa--;
			if (this.x < xPos - this.walkDistance) {
				dir3 = false;
				dir4 = true;
			}
		} else if (dir4) {
			ya--;
			if (this.y < yPos - this.walkDistance) {
				dir4 = false;
				dir1 = true;
			}
		}
		if ((xa != 0 || ya != 0) && !isSolidEntityCollision(xa, ya)
				&& !isMobCollision(xa, ya)) {
			move(xa, ya);
			setMoving(true);
		} else {
			setMoving(false);
		}

	}

	private void moveCross() {
		int xa = 0;
		int ya = 0;
		if (!dir1 && !dir2 && !dir3 && !dir4) {
			Random random = new Random();
			switch (random.nextInt(4)) {
			case 0: {
				dir1 = true;
				break;
			}
			case 1: {
				dir2 = true;
				break;
			}
			case 2: {
				dir3 = true;
				break;
			}
			case 3: {
				dir4 = true;
				break;
			}
			}
		}

		if (dir1) {
			xa++;
			if (this.x > this.walkDistance + xPos) {
				dir1 = false;
			}
		} else if (dir2) {
			ya++;
			if (this.y > this.walkDistance + yPos) {
				dir2 = false;
			}
		} else if (dir3) {
			xa--;
			if (this.x < xPos - this.walkDistance) {
				dir3 = false;
			}
		} else if (dir4) {
			ya--;
			if (this.y < yPos - this.walkDistance) {
				dir4 = false;
			}
		}
		if ((xa != 0 || ya != 0) && !isSolidEntityCollision(xa, ya)
				&& !isMobCollision(xa, ya)) {
			move(xa, ya);
			setMoving(true);
		} else {
			setMoving(false);
		}

	}

	private void moveCircle() {

		// Some random code with some random values. Don't ask me how it works.
		double miniTick = tickCount / 20.0;
		int xa = (int) (walkDistance * Math.cos(miniTick / walkDistance));
		int ya = (int) (walkDistance * Math.sin(miniTick / walkDistance));
		if ((xa != 0 || ya != 0) && !isSolidEntityCollision(xa, ya)
				&& !isMobCollision(xa, ya)) {
			move(xa, ya);
			setMoving(true);
		} else {
			setMoving(false);
		}

	}

	public void addQuest(Quest quest) {
		quests.add(quest);
	}

	public void setQuest(int num) {
		this.currentQuest = quests.get(num);
	}

	public void speak(Player player) {

		isTalking = true;
		switch (player.getDirection()) {
		case NORTH: {
			setDirection(Direction.SOUTH);
			break;
		}
		case SOUTH: {
			setDirection(Direction.NORTH);
			break;
		}
		case WEST: {
			setDirection(Direction.EAST);
			break;
		}
		case EAST: {
			setDirection(Direction.WEST);
			break;
		}
		}

		if (currentQuest != null) {
			if (!player.activeQuests.contains(currentQuest)) {
				player.activeQuests.add(currentQuest);
			}
			currentQuest.update();
			switch (currentQuest.getPhase()) {
			case 0: {
				ChatHandler.displayText(
						name + ": " + currentQuest.preDialogue(), Color.blue);
				sound.play(sound.levelup);
				currentQuest.nextPhase();
				return;
			}
			case 1: {
				ChatHandler.displayText(name + ": " + currentQuest.dialogue(),
						Color.blue);
				return;
			}
			case 2: {
				ChatHandler.displayText(
						name + ": " + currentQuest.postDialogue(), Color.CYAN);
				sound.play(sound.chest);
				if (!player.completedQuests.contains(currentQuest)) {
					player.completedQuests.add(currentQuest);
					player.activeQuests.remove(currentQuest);
				}
				nextQuest();
				return;
			}
			}
		}

		switch (random.nextInt(13)) {
		case 0: {
			ChatHandler.displayText(name + ": I used to be an adventurer too!",
					Color.black);
			return;
		}
		case 1: {
			ChatHandler.displayText(name + ": Nice shirt!", Color.white);
			return;
		}
		case 2: {
			ChatHandler.displayText(name + ": Are you Jesus?", Color.white);
			return;
		}
		case 3: {
			ChatHandler.displayText(name
					+ ": This is some nice weather we've been having.",
					Color.white);
			return;
		}
		case 4: {
			ChatHandler.displayText(name
					+ ": You are not from around here are you!", Color.white);
			return;
		}
		case 5: {
			ChatHandler.displayText(name + ": Hello Officer!", Color.white);
			return;
		}
		case 6: {
			ChatHandler.displayText(name + ": Who goes there!", Color.white);
			return;
		}
		case 7: {
			ChatHandler
					.displayText(
							name
									+ ": Have you been to San Cisco? I hear they're having lovely weather.",
							Color.white);
			return;
		}
		case 8: {
			ChatHandler
					.displayText(
							name
									+ ": It's you! It really is! All Hail the Hero of the Bay!",
							Color.white);
			return;
		}
		case 9: {
			ChatHandler
					.displayText(
							name
									+ ": I'm not racist but when you're driving in the East Bay,"
									+ " roll up your windows and lock your doors.",
							Color.white);
			return;
		}
		case 10: {
			ChatHandler
					.displayText(
							name
									+ ": Have you seen my friend Bob? He's a peasant and he seems to have"
									+ "literally dissapeared!", Color.white);
			return;
		}
		case 11: {
			ChatHandler.displayText(name
					+ ": Nasty business it is with those Apes in the North!"
					+ " Nasty business indeed.", Color.white);
			return;
		}
		case 12: {
			ChatHandler.displayText(name
					+ ": Hola, mi nombre es Esteban Norteruta!", Color.white);
			return;
		}
		default: {
			ChatHandler.displayText(name + ": Hello!", Color.white);
			return;
		}
		}

	}

	protected void nextQuest() {
		quests.remove(currentQuest);
		currentQuest = null;
		if (quests.contains(0)) {
			currentQuest = quests.get(0);
		}
	}

	public static NPC getRandomNPC(Level level, int x, int y) {
		Random random = new Random();
		switch (random.nextInt(11)) {

		case 0:
			return new NPC(level, "Knight", x, y, 1, 16, 16, 100, new int[] {
					0xFF111111, 0xFF7E7E7E, 0xFFFFFFFF }, 0, 2, "linear", 20);
		case 1:
			return new NPC(level, "Policeman", x, y, 1, 16, 16, 100, new int[] {
					0xFF2A2A2A, 0xFF000046, 0xFFEDC5AB }, 0, 4, "triangle", 20);
		case 2:
			return new NPC(level, "Citizen-Female", x, y, 1, 16, 16, 100,
					new int[] { 0xFF111111, 0xFFA51818, 0xFFEDC5AB }, 0, 8,
					"cross", 30);
		case 3:
			return new NPC(level, "Citizen-Male", x, y, 1, 16, 16, 100,
					new int[] { 0xFF111111, 0xFFA51818, 0xFFEDC5AB }, 0, 0,
					"circle", 2);
		case 4:
			return new NPC(level, "Fox", x, y, 1, 16, 16, 100, new int[] {
					0xFF111111, 0xFFFFA800, 0xFFFFFFFF }, 0, 14, "cross", 50);
		case 5:
			return new NPC(level, "Tech Warrior", x, y, 1, 16, 16, 100,
					new int[] { 0xFF000000, 0xFF42FF00, 0xFFEDC5AB }, 0, 12,
					"triangle", 20);
		case 6:
			return new NPC(level, "Peasant-Male", x, y, 1, 16, 16, 100,
					new int[] { 0xFF111111, 0xFF715B17, 0xFFEDC5AB }, 0, 16,
					"square", 100);
		case 7:
			return new NPC(level, "Peasant-Female", x, y, 1, 16, 16, 100,
					new int[] { 0xFF111111, 0xFF715B17, 0xFFEDC5AB }, 0, 18,
					"cross", 0);
		case 8:
			return new NPC(level, "Peasant-Boychild", x, y, 1, 16, 16, 9001,
					new int[] { 0xFF111111, 0xFF715B17, 0xFFEDC5AB }, 14, 16,
					"square", 0);
		case 9:
			return new NPC(level, "Peasant-Girlchild", x, y, 1, 16, 16, 9000,
					new int[] { 0xFF111111, 0xFF715B17, 0xFFEDC5AB }, 14, 18,
					"cross", 0);

		default:
			return new Jesus(level, x, y, "stand", 30);

		}
	}

}