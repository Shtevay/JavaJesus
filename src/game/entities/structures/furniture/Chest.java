package game.entities.structures.furniture;

import game.ChatHandler;
import game.entities.Entity;
import game.entities.Player;
import game.entities.SolidEntity;
import game.graphics.Screen;
import game.graphics.SpriteSheet;
import game.gui.overview.InventoryGUI;
import items.Gun;
import items.Item;
import items.Sword;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import level.Level;

public class Chest extends Entity implements SolidEntity {

	private static final long serialVersionUID = 1L;
	
	protected int[] color = new int[] { 0xFF111111, 0xFF452909, 0xFFFFE011 };
	protected boolean isOpen = false;
	ArrayList<Item> contents;

	Random random = new Random();

	public Chest(Level level, int x, int y) {
		super(level, x, y, 5, 9);
		contents = new ArrayList<Item>();
		this.shadow.setSize(0, 0);
		this.bounds.setSize(5, 9);
		this.bounds.setLocation(x, y);

	}

	public Chest(Level level, int x, int y, ArrayList<Item> items) {
		super(level, x, y, 5, 9);
		this.contents = items;
		this.shadow.setSize(0, 0);
		this.bounds.setSize(5, 9);
		this.bounds.setLocation(x, y);
	}

	public Chest(Level level, int x, int y, String type, int amt) {
		super(level, x, y, 5, 9);
		contents = new ArrayList<Item>();
		fillRandomItems(type, amt);
		this.shadow.setSize(0, 0);
		this.bounds.setSize(5, 9);
		this.bounds.setLocation(x, y);
	}

	private Gun getRandomGun() {
		switch (random.nextInt(5)) {
		case 0:
			return (Gun) Item.revolver;
		case 1:
			return (Gun) Item.laserRevolver;
		case 2:
			return (Gun) Item.shotgun;
		case 3:
			return (Gun) Item.assaultRifle;
		case 4:
			return (Gun) Item.crossBow;
		default:
			return (Gun) Item.bazooka;
		}
	}

	private Sword getRandomSword() {
		switch (random.nextInt(5)) {
		case 0:
			return (Sword) Item.longSword;

		case 1:
			return (Sword) Item.claymore;
		case 2:
			return (Sword) Item.sabre;
		case 3:
			return (Sword) Item.heavenlyShortSword;
		case 4:
			return (Sword) Item.heavenlySword;
		default:
			return (Sword) Item.shortSword;
		}
	}

	private Item getRandomItem() {
		switch (random.nextInt(4)) {
		case 0:
			return Item.apple;
		case 1:
			return Item.banana;
		case 2:
			return Item.orange;
		default:
			return Item.feather;

		}
	}

	private void fillRandomItems(String string, int amt) {
		switch (string) {
		case "gun":
			contents.add(getRandomGun());
			break;
		case "sword":
			contents.add(getRandomSword());
			break;
		case "item":
			contents.add(getRandomItem());
			break;
		default:
			switch (random.nextInt(3)) {
			case 0:
				contents.add(getRandomGun());
				break;
			case 1:
				contents.add(getRandomSword());
				break;
			default:
				contents.add(getRandomItem());
				break;
			}
		}
		if (amt > 0)
			fillRandomItems(string, amt - 1);
	}

	public void open() {
		isOpen = true;
	}
	
	public ArrayList<Item> getContents() {
		return contents;
	}

	public void render(Screen screen) {

		if (!isOpen) {
			screen.render(x, y, 0 + 20 * 32, color, 0, 1, SpriteSheet.tiles);
			screen.render(x + 8, y, 1 + 20 * 32, color, 0, 1, SpriteSheet.tiles);
		} else {
			screen.render(x, y, 2 + 20 * 32, color, 0, 1, SpriteSheet.tiles);
			screen.render(x + 8, y, 3 + 20 * 32, color, 0, 1, SpriteSheet.tiles);
		}

	}

	@Override
	public Rectangle getShadow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
