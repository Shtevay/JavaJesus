package ca.javajesus.game.entities.npcs;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import quests.Quest;
import ca.javajesus.game.ChatHandler;
import ca.javajesus.game.entities.Mob;
import ca.javajesus.game.entities.Player;
import ca.javajesus.game.entities.particles.HealthBar;
import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.JJFont;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.game.gfx.SpriteSheet;
import ca.javajesus.level.Level;

public class NPC extends Mob {

	public static NPC npc1 = new NPC(Level.level1, "Knight", 200, 100, 1, 16,
			16, 100, Colors.get(-1, 111, Colors.fromHex("#7e7e7e"),
					Colors.fromHex("#FFFFFF")), 0, 2, "linear", 20, 8);
	public static NPC npc2 = new NPC(Level.level1, "Policeman", 160, 250, 1,
			16, 16, 100, Colors.get(-1, Colors.fromHex("#2a2a2a"),
					Colors.fromHex("#000046"), 543), 0, 4, "triangle", 20, 8);
	public static NPC npc3 = new NPC(Level.level1, "Citizen-Female", 200, 400,
			1, 16, 16, 100, Colors.get(-1, 111, 300, 543), 0, 8, "cross", 30, 8);
	public static NPC npc4 = new NPC(Level.level1, "Citizen-Male", 200, 500, 1,
			16, 16, 100, Colors.get(-1, 111, 300, 543), 0, 0, "circle", 2, 8);
	public static NPC npc5 = new NPC(Level.level1, "Fox", 250, 75, 1, 16, 16,
			100, Colors.get(-1, 111, Colors.fromHex("#ffa800"), 555), 0, 14,
			"cross", 50, 8);
	public static NPC npc6 = new NPC(Level.level1, "Tech Warrior", 400, 250, 1,
			16, 16, 100, Colors.get(-1, 000, Colors.fromHex("#42ff00"), 543),
			0, 12, "triangle", 20, 8);
	public static NPC npc7 = new NPC(Level.level1, "Peasant-Male", 2005, 950,
			1, 16, 16, 100,
			Colors.get(-1, 111, Colors.fromHex("#715b17"), 543), 0, 16,
			"square", 100, 8);
	public static NPC npc8 = new NPC(Level.level1, "Peasant-Female", 2025, 950,
			1, 16, 16, 100,
			Colors.get(-1, 111, Colors.fromHex("#715b17"), 543), 0, 18,
			"cross", 0, 8);
	public static NPC npc9 = new NPC(Level.level1, "Peasant-Boychild", 2035,
			950, 1, 16, 16, 9001, Colors.get(-1, 111,
					Colors.fromHex("#715b17"), 543), 14, 16, "square", 0, 8);
	public static NPC npc10 = new NPC(Level.level1, "Peasant-Girlchild", 2045,
			950, 1, 16, 16, 9000, Colors.get(-1, 111,
					Colors.fromHex("#715b17"), 543), 14, 18, "cross", 0, 8);

	public static NPC Jesus = new Jesus(Level.level1, 300, 400, "stand", 30);

	protected boolean isSwimming = false;

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
	protected int tickCount;

	public ArrayList<Quest> quests = new ArrayList<Quest>();
	public Quest currentQuest;

	protected boolean movingToOrigin = false;

	public NPC(Level level, String name, int x, int y, int speed,
			int width, int height, int defaultHealth, int color, int xTile,
			int yTile, String walkPath, int walkDistance, int yChange) {
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
		this.bar = new HealthBar(level, 0 + 2 * 32, this.x, this.y, this,
				yChange);
		if (level != null)
			level.addEntity(bar);
		this.speed = 1;
	}

	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;
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

	public void tick() {

		if (isDead) {
			return;
		}

		if (isTalking) {
			talkCount++;
			if (talkCount > 350) {
				talkCount = 0;
				isTalking = false;
			}
		}

		if (isHit) {
			isHitTicks++;
			if (isHitTicks > 20) {
				isHitTicks = 0;
				isHit = false;
			}
		}

		tickCount++;
		if (tickCount > 360) {
			tickCount = 0;
			movingToOrigin = true;
		}

		if (isMobCollision()) {
			moveAroundMobCollision();
			return;
		}
		int xx = (int) x;
		int yy = (int) y;
		if (level.getTile(xx >> 3, yy >> 3).getId() == 3) {
			isSwimming = true;
		}
		if (isSwimming && level.getTile(xx >> 3, yy >> 3).getId() != 3) {
			isSwimming = false;
		}

		if (movingToOrigin)
			findOrigin();
		else {
			for (Mob mob : level.getMobs()) {
				if (mob == this)
					continue;
				if (this.getOuterBounds().intersects(mob.getBounds()))
					return;
			}
			findPath();
		}

	}

	protected void findOrigin() {

		if ((int) xPos == (int) this.x && (int) yPos == (int) this.y) {
			movingToOrigin = false;
		}

		int xa = 0;
		int ya = 0;
		if ((int) xPos > (int) this.x) {
			xa++;
		}
		if ((int) xPos < (int) this.x) {
			xa--;
		}
		if ((int) yPos > (int) this.y) {
			ya++;
		}
		if ((int) yPos < (int) this.y) {
			ya--;
		}
		if ((xa != 0 || ya != 0) && !isSolidEntityCollision(xa, ya)
				&& !isMobCollision(xa, ya)) {
			move(xa, ya);
			setMoving(true);
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
		this.getBounds().setLocation((int) this.x - (this.width / 2), (int) this.y
				- (this.height / 2));
		this.getOuterBounds().setLocation((int) this.x - (int) getBounds().getWidth() / 2
				- 2, (int) this.y - (int) getBounds().getHeight() / 2 - 2);
		int xTile = this.xTile;
		int yTile = this.yTile;

		int walkingAnimationSpeed = 4;

		int flipTop = (numSteps >> walkingAnimationSpeed) & 1;
		int flipBottom = (numSteps >> walkingAnimationSpeed) & 1;

		if (getDirection() == 0) {
			xTile += 10;
		}
		if (getDirection() == 1) {
			xTile += 2;
		} else if (getDirection() > 1) {
			xTile += 4 + ((numSteps >> walkingAnimationSpeed) & 1) * 2;
			flipTop = (getDirection() - 1) % 2;
			flipBottom = (getDirection() - 1) % 2;
		}

		int modifier = 8 * scale;
		double xOffset = x - modifier / 2.0;
		double yOffset = y - modifier / 2.0 - 4;

		if (isDead)
			xTile = 12;

		if (isSwimming) {
			if (isOnFire()) {
				setOnFire(false);
			}
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
		if (!isSwimming) {
			// Lower Body 1
			screen.render(xOffset + (modifier * flipBottom),
					yOffset + modifier, xTile + (yTile + 1) * 32, color,
					flipBottom, scale, sheet);
			// Lower Body 2
			screen.render(xOffset + modifier - (modifier * flipBottom), yOffset
					+ modifier, (xTile + 1) + (yTile + 1) * 32, color,
					flipBottom, scale, sheet);

		}

		// Upper body 1
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile
				* 32, color, flipTop, scale, sheet);
		// Upper Body 2
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset,
				(xTile + 1) + yTile * 32, color, flipTop, scale, sheet);

		if (currentQuest != null && !isTalking) {
			JJFont.render("?", screen, (int) xOffset + 4, (int) yOffset - 10,
					Colors.get(-1, -1, -1, Colors.fromHex("#FFCC00")), 1);
		}

		if (isTalking) {
			JJFont.render(name, screen, (int) xOffset
					- ((name.length() - 1) / 2 * 8), (int) yOffset - 10,
					Colors.get(-1, -1, -1, Colors.fromHex("#FFCC00")), 1);
		}

		if (isHit) {
			JJFont.render(damageTaken, screen, (int) xOffset + isHitX,
					(int) yOffset - 10 + isHitY, isHitColor, 1);
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
		case 0: {
			setDirection(1);
			break;
		}
		case 1: {
			setDirection(0);
			break;
		}
		case 2: {
			setDirection(3);
			break;
		}
		case 3: {
			setDirection(2);
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
				ChatHandler.sendMessage(
						name + ": " + currentQuest.preDialogue(), Color.blue);
				sound.play(sound.levelup);
				currentQuest.nextPhase();
				return;
			}
			case 1: {
				ChatHandler.sendMessage(name + ": " + currentQuest.dialogue(),
						Color.blue);
				return;
			}
			case 2: {
				ChatHandler.sendMessage(
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
			ChatHandler.sendMessage(name + ": I used to be an adventurer too!",
					Color.black);
			return;
		}
		case 1: {
			ChatHandler.sendMessage(name + ": Nice shirt!", Color.white);
			return;
		}
		case 2: {
			ChatHandler.sendMessage(name + ": Are you Jesus?", Color.white);
			return;
		}
		case 3: {
			ChatHandler.sendMessage(name
					+ ": This is some nice weather we've been having.",
					Color.white);
			return;
		}
		case 4: {
			ChatHandler.sendMessage(name
					+ ": You are not from around here are you!", Color.white);
			return;
		}
		case 5: {
			ChatHandler.sendMessage(name + ": Hello Officer!", Color.white);
			return;
		}
		case 6: {
			ChatHandler.sendMessage(name + ": Who goes there!", Color.white);
			return;
		}
		case 7: {
			ChatHandler
					.sendMessage(
							name
									+ ": Have you been to San Cisco? I hear they're having lovely weather.",
							Color.white);
			return;
		}
		case 8: {
			ChatHandler
					.sendMessage(
							name
									+ ": It's you! It really is! All Hail the Hero of the Bay!",
							Color.white);
			return;
		}
		case 9: {
			ChatHandler
					.sendMessage(
							name
									+ ": I'm not racist but when you're driving in the East Bay,"
									+ " roll up your windows and lock your doors.",
							Color.white);
			return;
		}
		case 10: {
			ChatHandler
					.sendMessage(
							name
									+ ": Have you seen my friend Bob? He's a peasant and he seems to have"
									+ "literally dissapeared!", Color.white);
			return;
		}
		case 11: {
			ChatHandler.sendMessage(name
					+ ": Nasty business it is with those Apes in the North!"
					+ " Nasty business indeed.", Color.white);
			return;
		}
		case 12: {
			ChatHandler.sendMessage(name
					+ ": Hola, mi nombre es Esteban Norteruta!", Color.white);
			return;
		}
		default: {
			ChatHandler.sendMessage(name + ": Hello!", Color.white);
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
			return new NPC(level, "Knight", x, y, 1, 16, 16, 100, Colors.get(
					-1, 111, Colors.fromHex("#7e7e7e"),
					Colors.fromHex("#FFFFFF")), 0, 2, "linear", 20, 8);
		case 1:
			return new NPC(level, "Policeman", x, y, 1, 16, 16, 100,
					Colors.get(-1, Colors.fromHex("#2a2a2a"),
							Colors.fromHex("#000046"), 543), 0, 4, "triangle",
					20, 8);
		case 2:
			return new NPC(level, "Citizen-Female", x, y, 1, 16, 16, 100,
					Colors.get(-1, 111, 300, 543), 0, 8, "cross", 30, 8);
		case 3:
			return new NPC(level, "Citizen-Male", x, y, 1, 16, 16, 100,
					Colors.get(-1, 111, 300, 543), 0, 0, "circle", 2, 8);
		case 4:
			return new NPC(level, "Fox", x, y, 1, 16, 16, 100, Colors.get(-1,
					111, Colors.fromHex("#ffa800"), 555), 0, 14, "cross", 50, 8);
		case 5:
			return new NPC(level, "Tech Warrior", x, y, 1, 16, 16, 100,
					Colors.get(-1, 000, Colors.fromHex("#42ff00"), 543), 0, 12,
					"triangle", 20, 8);
		case 6:
			return new NPC(level, "Peasant-Male", x, y, 1, 16, 16, 100,
					Colors.get(-1, 111, Colors.fromHex("#715b17"), 543), 0, 16,
					"square", 100, 8);
		case 7:
			return new NPC(level, "Peasant-Female", x, y, 1, 16, 16, 100,
					Colors.get(-1, 111, Colors.fromHex("#715b17"), 543), 0, 18,
					"cross", 0, 8);
		case 8:
			return new NPC(level, "Peasant-Boychild", x, y, 1, 16, 16, 9001,
					Colors.get(-1, 111, Colors.fromHex("#715b17"), 543), 14,
					16, "square", 0, 8);
		case 9:
			return new NPC(level, "Peasant-Girlchild", x, y, 1, 16, 16, 9000,
					Colors.get(-1, 111, Colors.fromHex("#715b17"), 543), 14,
					18, "cross", 0, 8);

		default:
			return new Jesus(level, x, y, "stand", 30);

		}
	}

}
