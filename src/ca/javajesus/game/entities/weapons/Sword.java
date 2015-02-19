package ca.javajesus.game.entities.weapons;

import ca.javajesus.game.entities.Entity;
import ca.javajesus.game.entities.Player;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.game.gfx.Sprite;
import ca.javajesus.level.Level;

public class Sword extends Entity {

	/** List of rotated sprites */
	private Sprite[] sprites;
	/** Current Sprite */
	private Sprite sprite;
	private Player player;
	protected int color = 222;
	private final int swingSpeed = 50;
	private int tickCount = 0;
	private int swordCount = 0;

	public Sword(Level level, Sprite[] sprites, Player player, int color) {
		super(level);
		this.sprites = sprites;
		this.sprite = sprites[sprites.length - 1];
		this.player = player;
		this.color = color;
		sound.sheathe.start();
	}

	public void tick() {

		tickCount++;
		if (tickCount > swingSpeed) {
			tickCount = 0;
			swordCount = 0;
			level.remEntity(this);
		}
		if (tickCount % (swingSpeed / sprites.length) == 0) {
			swordCount++;
			if (sprites.length - swordCount - 1 >= 0)
				this.sprite = sprites[sprites.length - swordCount - 1];
		}
	}

	public void render(Screen screen) {
		int dir = player.movingDir;
		switch (player.movingDir) {
		case 3:
			this.x = player.x + 3;
			this.y = player.y - 30;
			break;

		case 2:
			this.x = player.x + 3;
			this.y = player.y - 30;
			break;

		case 1:
			this.x = player.x - 30;
			this.y = player.y + 10;
			this.sprite = sprites[0];
			dir = 0;
			break;

		case 0:
			this.x = player.x + 1;
			this.y = player.y - 40;
			this.sprite = sprites[sprites.length - 1];
			dir = 2;
			break;

		}

		screen.render((int) x, (int) y, dir, color,
				sprite);

	}

}
