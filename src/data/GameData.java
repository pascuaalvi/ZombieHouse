package data;

import furniture.Box;
import furniture.Furniture;
import gui.MainFrame;

import item.Ammo;
import item.Battery;
import item.Item;
import item.Key;

import java.io.File;
import java.util.ArrayList;

import people.Hero;
import people.People;
import people.Zombie;

import building.Room;

public class GameData {

	private Room[][][] building;

	private ArrayList<People> characters = new ArrayList<People>();
	private ArrayList<Furniture> furniture = new ArrayList<Furniture>();
	private ArrayList<Item> items = new ArrayList<Item>();

	private int heroIdDispenser = 0;
	private int zombieIdDispenser = 5;
	private int furnitureIdDispenser = 100;
	private int itemIdDispenser = 150;

	private final int startingLevel = 0;
	private final int startingX = 2;
	private final int startingY = 1;

	private MainFrame frame;

	public GameData(MainFrame frame) {
		this.frame = frame;
		// the following four methods should only be used once per game
		setupBuilding();
		setupFurniture();
		setupZombies();
		setupItems();
	}

	/**
	 * Initialise zombies
	 */
	private void setupZombies() {
		// NOTE THAT >>> this is a test zombie
		// specify room
		Room r = building[0][2][1];
		// create zombie
		Zombie z = new Zombie(zombieIdDispenser++);
		// set its CurrentRoom
		z.setCurrentRoom(r);
		// initialise... IMPORTANT
		z.initialise(this);
		// add to characters
		characters.add(z);

		int population = 5; // Minimum number of zombies per room, change at
							// your leisure

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					// System.out.println(i+" "+j+" "+k);
					// specify room
					Room room = building[i][j][k];
					if (room == null)
						continue;
					// Number of zombies per room increases per level
					int randomizedVal = (int) (Math.random() * (population));
					for (int cnt = 0; cnt < randomizedVal; cnt++) {
						// create zombie
						z = new Zombie(zombieIdDispenser++);
						room.incZombieCount();
						// set its position, based on room and uid
						determineStartLocation(z, room);
						// set its CurrentRoom
						z.setCurrentRoom(room);
						// initialise... IMPORTANT
						z.initialise(this);
						// add to characters
						characters.add(z);
					}
					// System.out.println(this.zombieIdDispenser);
				}
			}
		}
		
	}

	private void determineStartLocation(Zombie z, Room r) {
		int count = r.getZombieCount();
		int[] xp = r.getPortalX();
		int[] yp = r.getPortalY();
		int id = z.getUid();
		if (count < 6) {
			z.bounds.setLocation(xp[count], yp[count]);
		}
	}

	/**
	 * Initialise items for game
	 */
	private void setupItems() {
		// specify room
		Room r = building[0][2][1];
		// pick item
		Key k = new Key(itemIdDispenser++, r);
		// initialise location, IMPORTANT
		k.initialiseLocation(1000, 200);
		// add to items
		items.add(k);
		
		
		Room room = building[0][1][1];
		Ammo b = new Ammo(itemIdDispenser++, room);
		// initialise location, IMPORTANT
		b.initialiseLocation(1000, 200);
		// add to items
		items.add(b);
	}

	/**
	 * Initialise Furniture for each room
	 */
	private void setupFurniture() {
		// specify the room
		Room r = building[0][2][1];
		// pick a furniture
		Box b = new Box(furnitureIdDispenser++, r);
		// initialise its location, IMPORTANT
		b.initialiseLocation(500, 30);
		// add it to list of furniture
		furniture.add(b);
	}

	public Room getRoom(int level, int x, int y) {
		return building[level][x][y];
	}

	/**
	 * Initial setup of the buildings rooms.
	 */
	private void setupBuilding() {
		// level 1
		this.building = new Room[4][3][3];
		building[0][1][0] = RoomReader.readFile(new File(
				"levelFiles/level1_room1"));
		building[0][2][1] = RoomReader.readFile(new File(
				"levelFiles/level1_room2"));
		building[0][1][2] = RoomReader.readFile(new File(
				"levelFiles/level1_room3"));
		building[0][0][1] = RoomReader.readFile(new File(
				"levelFiles/level1_room4"));
		building[0][1][1] = RoomReader.readFile(new File(
				"levelFiles/level1_room5"));
		// level 2
		building[1][1][0] = RoomReader.readFile(new File(
				"levelFiles/level2_room1"));
		building[1][2][1] = RoomReader.readFile(new File(
				"levelFiles/level2_room2"));
		building[1][1][2] = RoomReader.readFile(new File(
				"levelFiles/level2_room3"));
		building[1][0][1] = RoomReader.readFile(new File(
				"levelFiles/level2_room4"));
		building[1][1][1] = RoomReader.readFile(new File(
				"levelFiles/level2_room5"));
		// level 3
		building[2][1][0] = RoomReader.readFile(new File(
				"levelFiles/level3_room1"));
		building[2][2][1] = RoomReader.readFile(new File(
				"levelFiles/level3_room2"));
		building[2][1][2] = RoomReader.readFile(new File(
				"levelFiles/level3_room3"));
		building[2][0][1] = RoomReader.readFile(new File(
				"levelFiles/level3_room4"));
		building[2][1][1] = RoomReader.readFile(new File(
				"levelFiles/level3_room5"));
		// level 4
		building[3][1][0] = RoomReader.readFile(new File(
				"levelFiles/level4_room1"));
		building[3][2][1] = RoomReader.readFile(new File(
				"levelFiles/level4_room2"));
		building[3][1][2] = RoomReader.readFile(new File(
				"levelFiles/level4_room3"));
		building[3][0][1] = RoomReader.readFile(new File(
				"levelFiles/level4_room4"));
		building[3][1][1] = RoomReader.readFile(new File(
				"levelFiles/level4_room5"));
	}

	/**
	 * Should only be used at the beginning of a game.
	 * 
	 * @return
	 */
	public int createPlayer() {
		int id = this.heroIdDispenser++;
		Hero h = new Hero(id);
		characters.add(h);
		h.setCurrentRoom(building[this.startingLevel][this.startingX][this.startingY]);
		return id;
	}

	public People getPlayer(int uid) {
		for (int i = 0; i < characters.size(); i++) {
			People p = characters.get(i);
			if (p.getUid() == uid) {
				return p;
			}
		}
		return null;
	}

	public Hero getHero(int uid) {
		for (int i = 0; i < characters.size(); i++) {
			People p = characters.get(i);
			if (p instanceof Hero) {
				if (p.getUid() == uid) {
					return (Hero) p;
				}
			}
		}
		return null;
	}

	public ArrayList<People> getCharacters() {
		return characters;
	}

	public void setCharacters(ArrayList<People> characters) {
		this.characters = characters;
	}

	public ArrayList<Furniture> getFurniture() {
		return furniture;
	}

	public void setFurniture(ArrayList<Furniture> furniture) {
		this.furniture = furniture;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public Room[][][] getBuilding() {
		return building;
	}

	public void clearPeople() {
		this.characters.clear();
	}

	public void clearFurniture() {
		this.furniture.clear();
	}

	public void clearItems() {
		this.items.clear();
	}

	public Room getStartRoom() {
		// TODO Auto-generated method stub
		return building[0][2][1];
	}

	/**
	 * Returns the room north of the one given
	 * 
	 * @param currentRoom
	 * @return
	 */
	public Room north(Room currentRoom) {
		for (int z = 0; z < this.building.length; z++) {
			for (int y = 0; y < this.building[0].length; y++) {
				for (int x = 0; x < this.building[0][0].length; x++) {
					if (currentRoom == building[z][x][y]) {
						return building[z][x][y - 1];
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the room south of the one given
	 * 
	 * @param currentRoom
	 * @return
	 */
	public Room south(Room currentRoom) {
		for (int z = 0; z < this.building.length; z++) {
			for (int y = 0; y < this.building[0].length; y++) {
				for (int x = 0; x < this.building[0][0].length; x++) {
					if (currentRoom == building[z][x][y]) {
						return building[z][x][y + 1];
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the room east of the one given
	 * 
	 * @param currentRoom
	 * @return
	 */
	public Room east(Room currentRoom) {
		for (int z = 0; z < this.building.length; z++) {
			for (int y = 0; y < this.building[0].length; y++) {
				for (int x = 0; x < this.building[0][0].length; x++) {
					if (currentRoom == building[z][x][y]) {
						return building[z][x + 1][y];
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the room west of the one given
	 * 
	 * @param currentRoom
	 * @return
	 */
	public Room west(Room currentRoom) {
		for (int z = 0; z < this.building.length; z++) {
			for (int y = 0; y < this.building[0].length; y++) {
				for (int x = 0; x < this.building[0][0].length; x++) {
					if (currentRoom == building[z][x][y]) {
						return building[z][x - 1][y];
					}
				}
			}
		}
		return null;
	}

	public Room levelAdvance(Room currentRoom) {
		for (int z = 0; z < this.building.length; z++) {
			for (int y = 0; y < this.building[0].length; y++) {
				for (int x = 0; x < this.building[0][0].length; x++) {
					if (currentRoom == building[z][x][y]) {
						return building[z + 1][x][y];
					}
				}
			}
		}
		return null;
	}

}
