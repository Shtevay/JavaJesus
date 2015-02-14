package ca.javajesus.items;

import java.util.ArrayList;
import java.util.List;

import ca.javajesus.game.entities.Player;

public class Inventory {

	public List<Item> items = new ArrayList<Item>();

	public Inventory() {
		giveDefaultItems();
		setOffsets();
	}
	
	private void setOffsets() {
		int offset = 0;
		for (Item e: items) {
			e.xOffset += offset;
			offset += 10;
		}
	}

	private void giveDefaultItems() {
		
		items.add(Item.apple);
		
		items.add(Item.banana);
		
		items.add(Item.orange);
		
		items.add(Item.feather);
		
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public void removeItem(Item item) {
		items.remove(item);
	}

	public void sortItemsAlphabetically() {
		int[] tempList = new int[items.size()];
		String tempString = items.get(0).name;
		for (int i = 0; i < tempList.length; i++) {
			for (int j = 1; j < tempList.length; j++) {
				if (tempString.compareToIgnoreCase(items.get(j).name) > 0) {
					tempString = items.get(j).name;
					tempList[i] = j;
				}
			}
			items.add(items.get(tempList[i]));
			// items.set(tempList[i], Item.MAX);
			tempString = items.get(0).name;
		}
		for (int i = 0; i < tempList.length; i++)
			items.remove(0);
	}

	public void sortItemsByID() {
		int[] tempList = new int[items.size()];
		int tempNum = Integer.MAX_VALUE;
		for (int i = 0; i < tempList.length; i++) {
			for (int j = 0; j < tempList.length; j++) {
				if (items.get(j).id < tempNum) {
					tempNum = items.get(j).id;
					tempList[i] = j;
				}
			}
			items.add(items.get(tempList[i]));
			// items.set(tempList[i], Item.MAX);
			tempNum = Integer.MAX_VALUE;
		}
		for (int i = 0; i < tempList.length; i++)
			items.remove(0);
	}

}
