package javajesus.level;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import javajesus.Hideable;
import javajesus.JavaJesus;
import javajesus.entities.Damageable;
import javajesus.entities.Entity;
import javajesus.entities.Mob;
import javajesus.entities.Player;
import javajesus.entities.SolidEntity;
import javajesus.graphics.JJFont;
import javajesus.graphics.Screen;
import javajesus.level.tile.AnimatedTile;
import javajesus.level.tile.Tile;
import javajesus.save.LevelData;
import javajesus.save.SaveData;

/*
 * A level contains a set of tiles and set of entities that
 * are displayed to the screen and handles their
 * logic and rendering
 */
public abstract class Level {

	// all tiles on each level
	protected final int[] levelTiles;

	// list of all entities on the map
	private final List<Entity> entities = new ArrayList<Entity>(JavaJesus.ENTITY_LIMIT);

	// list of all mobs on the map
	private final List<Mob> mobs = new ArrayList<Mob>(JavaJesus.ENTITY_LIMIT);
	
	// list of all things that can be damaged
	private final List<Damageable> damageables = new ArrayList<Damageable>(JavaJesus.ENTITY_LIMIT);

	private final List<Hideable> hideables = new ArrayList<Hideable>(JavaJesus.ENTITY_LIMIT);

	// where the player will go when entering this level
	private final Point spawnPoint;

	// name of the level
	private final String name;
	
	// gets the name add-on for entity files
	private static final String ENTITY = "_entities";
	
	// gets the home directory
	private static final String DIR = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
	        + "/My Games/JavaJesus/File";
	
	// story level names
	public static final String BAUTISTA = "Bautista's Domain", EDGE_MAIN = "Edge of the Woods",
	        EDGE_TOP = "Edge of the Woods Top", HILLSBOROUGH = "Lord Hillsborough's Domain", ORCHARD = "Orchard Valley",
	        CISCO = "San Cisco", JUAN = "San Juan", TECH = "Tech Topia";
	
	// size of each level
	public static final int LEVEL_WIDTH = 200, LEVEL_HEIGHT = 200;

	// the range of how many entities to render/tick on the screen
	public static final Rectangle renderRange = new Rectangle(JavaJesus.IMAGE_WIDTH, JavaJesus.IMAGE_HEIGHT);

	// global offsets for determining the range to display things
	private int xOffset, yOffset;


	/**
	 * Creates a level from the specified image path for the first time
	 * 
	 * @param imagePath - the image path of the original file
	 * @param loadNow - whether or not to load it now
	 * @param name - the name of this level
	 * @param saveFile - the destination of the save file in My Games
	 */
	public Level(final String path, final String name, final Point spawn, int saveFile) throws IOException {
		
		// instance data
		this.name = name;
		this.spawnPoint = spawn;
		levelTiles = new int[LEVEL_WIDTH * LEVEL_HEIGHT];
		
		// check if save file exists
		File f = new File(DIR + saveFile);
		
		// file does not exist
		if (!f.exists()) {
			
			// make the directory
			f.mkdirs();
			
			// load the original files into memory
			load(path, true);
			
			// now save in the new folder
			save(DIR + saveFile + "/" + name);
			
			// directory does exist
		} else {
			
			// select the file
			f = new File(DIR + saveFile + "/" + name);
			
			// if it exists, load it
			if (f.exists()) {
				load(DIR + saveFile + "/" + name, false);
				
				// load from original, then save
			} else {
				
				// load the original files into memory
				load(path, true);
				
				// now save in the new folder
				save(DIR + saveFile + "/" + name);
			}
		}
	}
	
	/**
	 * A default level for random generation
	 * @param name
	 */
	public Level(String name, Point spawn) {
		
		// instance data
		this.name = name;
		this.spawnPoint = spawn;
		levelTiles = new int[LEVEL_WIDTH * LEVEL_HEIGHT];
	}

	/**
	 * Loads an image from the file
	 */
	private void load(String path, boolean classpath) throws IOException {
		
		// files to load
		File level, entities;
		
		// load with or without classpath
		if (classpath) {
			level = new File(Level.class.getResource(path).getFile());
			entities = new File(Level.class.getResource(path + ENTITY).getFile());
		} else {
			level = new File(path);
			entities = new File(path + ENTITY);
		}
		
		// load  the tiles
		LevelData.load(level, levelTiles);

		// load the entity data
		SaveData.load(this, entities);
	}
	
	/**
	 * Saves the level data to the specified path
	 */
	private void save(String path) throws IOException {
		
		// save the tile data
		LevelData.save(path, levelTiles);
		
		// save the entity data
		SaveData.save(path + ENTITY, entities);
		
	}

	/**
	 * Changes a tile on the map
	 * 
	 * @param x - the x coord of the tile
	 * @param y - the y coord of the tile
	 * @param newTile - the tile to replace it with
	 */
	public void alterTile(int x, int y, Tile newTile) {
		this.levelTiles[x + y * LEVEL_WIDTH] = newTile.getId();
	}

	/**
	 * Removes dead mobs from the level
	 */
	public void clear() {
		for (int i = 0; i < mobs.size(); i++) {
			Mob m = mobs.get(i);
			if (m.isDead())
				remove(m);
		}
	}

	/**
	 * Updates all entities and tiles on the map
	 */
	public void tick() {

		// update all entities and living mobs
		for (int i = 0; i < getEntities().size(); i++) {
			Entity e = getEntities().get(i);

			if (e.getBounds().intersects(renderRange) && (!(e instanceof Mob) || !((Mob) e).isDead())) {
				e.tick();
			}
		}

		// update all tiles
		for (AnimatedTile t : AnimatedTile.tileList) {
			t.tick();
		}
	}

	/**
	 * Renders a tile on the screen
	 * 
	 * @param screen - the screen to display it on
	 * @param xOffset - the xoffset on the screen
	 * @param yOffset - the yoffset on the screen
	 */
	public void renderTile(Screen screen) {
		
		// set the screen offsets
		screen.setOffset(xOffset, yOffset);
		
		// render the tiles visible on the screen
		for (int y = (yOffset >> 3); y < (yOffset + screen.getHeight() >> 3) + 1; y++) {
			for (int x = (xOffset >> 3); x < (xOffset + screen.getWidth() >> 3) + 1; x++) {
				getTile(x, y).render(screen, this, x << 3, y << 3);
			}

		}
	}
	
	/**
	 * Sets offsets for tile and entity rendering
	 * @param xOffset - x offset
	 * @param yOffset - y offset
	 */
	public void setOffset(int xOffset, int yOffset) {
		
		// base offsets
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		// if the player moves off-screen, fix the tiles in place
		if (xOffset < 0)
			this.xOffset = 0;

		if (xOffset > ((LEVEL_WIDTH << 3) - JavaJesus.IMAGE_WIDTH))
			this.xOffset = ((LEVEL_WIDTH << 3) - JavaJesus.IMAGE_WIDTH);

		if (yOffset < 0)
			this.yOffset = 0;

		if (yOffset > ((LEVEL_HEIGHT << 3) - JavaJesus.IMAGE_HEIGHT))
			this.yOffset = ((LEVEL_HEIGHT << 3) - JavaJesus.IMAGE_HEIGHT);
		
	}

	/**
	 * Displays the entities of the level on the screen
	 * 
	 * @param screen - the screen to display it on
	 * @param player - the player to render entities around
	 */
	public void renderEntities(Screen screen, Player player) {

		// the range around the player to display the entities
		renderRange.setLocation(xOffset, yOffset);
		
		// render everything that is behind a building first
		for (Hideable entity : hideables) {

			if (entity.getBounds().intersects(renderRange) && entity.isBehindBuilding()) {
				entity.render(screen);
			}

		}

		// render all buildings
		for (Entity e : this.getEntities()) {
			if ((e.getBounds().intersects(renderRange) && !(e instanceof Hideable))
			        || (e instanceof SolidEntity && ((SolidEntity) e).getShadow().intersects(renderRange))) {
				e.render(screen);
			}
			
		}

		// now render everything else on top
		for (Hideable e : hideables) {
			if (e.getBounds().intersects(renderRange) && !e.isBehindBuilding()) {
				e.render(screen);
			}

		}
	}
	
	/**
	 * Renders collision boxes onto the screen
	 * 
	 * @param screen - screen to render pixels
	 */
	public void renderCollisionBoxes(Screen screen) {
		
		for (Entity e: this.getEntities()) {
			screen.renderCollisionBox(e.getBounds());
		}
	}

	/**
	 * Renders text on the screen
	 * 
	 * @param msg - message to display
	 * @param screen - the screen to display it on
	 * @param x - the x offset
	 * @param y - the y offset
	 * @param color - the color of the message
	 * @param scale - how big to render it
	 */
	public void renderFont(String msg, Screen screen, int x, int y, int[] color, int scale) {
		JJFont.render(msg, screen, x, y, color, scale);
	}

	/**
	 * Gets the tile type at the specified x,y coords
	 * 
	 * @param x - the x coord in tile coordinates
	 * @param y - the y coord in tile coordinates
	 * @return the type of tile, VOID if nothing
	 */
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= LEVEL_WIDTH || y < 0 || y >= LEVEL_HEIGHT)
			return Tile.VOID;
		
		return Tile.tileList[levelTiles[x + y * LEVEL_WIDTH]];

	}
	
	/**
	 * Gets the tile type from the entity coordinates
	 * 
	 * @param x - the x coord in entity coordinates
	 * @param y - the y coord in entity coordinates
	 * @return the type of tile, VOID if nothing
	 */
	public Tile getTileFromEntityCoords(int x, int y) {
		return getTile(x >> 3, y >> 3);
	}

	/**
	 * Adds an entity to this level
	 * 
	 * @param entity - the entity to add
	 */
	public synchronized void add(Entity entity) {

		if (entity instanceof Mob) {
			mobs.add((Mob) entity);

		}
		if (entity instanceof Hideable) {
			hideables.add((Hideable) entity);
		}
		if (entity instanceof Damageable) {
			damageables.add((Damageable) entity);
		}

		entities.add(entity);
	}

	/**
	 * Removes an entity from the level
	 * 
	 * @param entity - the entity to remove
	 */
	public synchronized void remove(Entity entity) {
		entities.remove(entity);
		if (entity instanceof Mob) {
			mobs.remove(entity);
		}
		if (entity instanceof Hideable) {
			hideables.remove(entity);
		}
		if (entity instanceof Damageable) {
			damageables.remove(entity);
		}

	}

	/**
	 * @return a list of all entities on the map
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * @return a list of all mobs on this map
	 */
	public List<Mob> getMobs() {
		return mobs;
	}
	
	/**
	 * @return a list of all damageables on this map
	 */
	public List<Damageable> getDamageables() {
		return damageables;
	}

	/**
	 * Returns a representation of this object as a string
	 */
	public String toString() {
		return name + "\n Mobs: " + this.getMobs();
	}

	/**
	 * @return the name of the level
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the spawnpoint on this level
	 */
	public Point getSpawnPoint() {
		return spawnPoint;
	}
	
	/**
	 * Sets the spawn point on this level
	 * in entity coordinates
	 * 
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 */
	public void setSpawnPoint(int x, int y) {
		spawnPoint.setLocation(x, y);
	}

}
