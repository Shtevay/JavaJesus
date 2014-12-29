package ca.javajesus.level;

import java.awt.Point;

import ca.javajesus.game.entities.monsters.Monster;
import ca.javajesus.game.entities.npcs.NPC;
import ca.javajesus.game.entities.npcs.Policeman;
import ca.javajesus.game.entities.particles.ArmorPickup;
import ca.javajesus.game.entities.structures.CastleTower;
import ca.javajesus.game.entities.structures.CatholicChurch;
import ca.javajesus.game.entities.structures.CaveEntrance;
import ca.javajesus.game.entities.structures.Hut;
import ca.javajesus.game.entities.structures.NiceHouse;
import ca.javajesus.game.entities.structures.PoorHouse;
import ca.javajesus.game.entities.structures.SanCiscoSkyscraper;
import ca.javajesus.game.entities.structures.Skyscraper;
import ca.javajesus.game.entities.vehicles.Vehicle;

public class Level1 extends Level {

	public Level1() {
		super("/Levels/tile_tester_level.png");
	}
	
	public Point spawnPoint() {
		return new Point(50, 50);
	}

	public void initNPCPlacement() {
		this.addEntity(NPC.npc1);
		this.addEntity(NPC.npc2);
		this.addEntity(NPC.npc3);
		this.addEntity(NPC.npc4);
		this.addEntity(NPC.npc5);
		this.addEntity(NPC.npc6);
		this.addEntity(new Policeman(this, 400, 100, 200, "linear", 20));
		this.addEntity(NPC.npc8);
		this.addEntity(NPC.npc9);
		this.addEntity(NPC.npc10);
		
		this.addEntity(Monster.gang1);
		this.addEntity(Monster.horseThing1);
		this.addEntity(Monster.monkey);
		this.addEntity(Monster.gang2);
		//this.addEntity(Monster.man2);
				
	}

	@Override
	public void initSpawnerPlacement() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initChestPlacement() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void otherEntityPlacement() {
		this.addEntity(Vehicle.vehicle1);
		this.addEntity(Vehicle.boat1);
		this.addEntity(new PoorHouse(this, 100, 50));
		this.addEntity(new NiceHouse(this, 200, 50));
		this.addEntity(new Hut(this, 300, 50));
		this.addEntity(new CatholicChurch(this, 400, 50));
		this.addEntity(new CastleTower(this, 500, 50));
		this.addEntity(new CaveEntrance(this, 600, 50));
		this.addEntity(new Skyscraper(this, 200, 200));
		this.addEntity(new SanCiscoSkyscraper(this, 100, 200));
		this.addEntity(new ArmorPickup(this, 100, 25));
		
	}

}
