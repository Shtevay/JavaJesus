package javajesus.quest.original;

import java.awt.Rectangle;

import javajesus.entities.Mob;
import javajesus.entities.monsters.Demon;
import javajesus.quest.Quest;
/**
 * 
 * @author shtevay
 * ALPHA QUEST TO LIBERATE THE FARM ON THE ALPHA LEVEL OF DEMONS
 * Given by SWAT OFFICER, CODE 2
 * Activates the Alpha Peasant Spawner upon completion
 *
 */
public class LiberateFarm extends Quest {
	
	//objectives
	private static final int FARM_CLEAR = 0;

	//bounds of the farm
	private static final Rectangle farmBounds = new Rectangle(32, 144, 696, 280);

	public LiberateFarm() {
		super("Liberate  Farm", "/WORLD_DATA/QUEST_DATA/Liberate_Farm.json", 1);
	}

	@Override
	public void update() {
		
		// if a demon is not found, assume true
		boolean killed = true;
		
		// search for any demons
		for (Mob m: giver.getLevel().getMobs()) {
			if (m instanceof Demon && m.getBounds().intersects(farmBounds) && !m.isDead()) {
				killed = false;
				break;
			}
		}
	
		// update the objective
		objectives[FARM_CLEAR] = killed;
	}

}
