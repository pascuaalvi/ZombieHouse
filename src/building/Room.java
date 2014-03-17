package building;

import item.Item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

import furniture.Furniture;

public class Room implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Rectangle> walls;
	private ArrayList<Rectangle> doors;
	private ArrayList<Rectangle> lockedDoors;
	// private ArrayList<Rectangle> stairs;

	private Stairs stairs = null;

	private int zombieCount = 0;
	private int[] portalX = { 300, 500, 900, 950, 1100, 1200 };
	private int[] portalY = { 90, 90, 500, 100, 10, 700 };

	public void draw(Graphics gr) {
		Graphics2D g = (Graphics2D) gr.create();

		g.setColor(new Color(153, 73, 0));
		for (Rectangle r : doors) {
			g.fill(r);
		}

		g.setColor(new Color(153, 153, 0));
		for (Rectangle r : lockedDoors) {
			g.fill(r);
		}

		g.setColor(new Color(64, 64, 64));
		for (Rectangle r : walls) {
			g.fill(r);
		}

		g.setColor(new Color(255, 255, 255));
		if (stairs != null)
			stairs.draw(g);

	}

	public Room(ArrayList<Rectangle> walls, ArrayList<Rectangle> doors,
			ArrayList<Rectangle> locked, Stairs stairs) {
		this.walls = walls;
		this.doors = doors;
		this.lockedDoors = locked;
		this.stairs = stairs;
		// this.stairs = stairs;
	}

	public ArrayList<Rectangle> getWalls() {
		return walls;
	}

	public void setWalls(ArrayList<Rectangle> walls) {
		this.walls = walls;
	}

	public ArrayList<Rectangle> getDoors() {
		return doors;
	}

	public void setDoors(ArrayList<Rectangle> doors) {
		this.doors = doors;
	}

	public ArrayList<Rectangle> getLockedDoors() {
		return lockedDoors;
	}

	public void setLockedDoors(ArrayList<Rectangle> lockedDoors) {
		this.lockedDoors = lockedDoors;
	}

	public Stairs getStairs() {
		return stairs;
	}

	public void setStairs(Stairs stairs) {
		this.stairs = stairs;
	}

	public int getZombieCount() {
		return zombieCount;
	}

	public void setZombieCount(int zombieCount) {
		this.zombieCount = zombieCount;
	}

	public int[] getPortalX() {
		return this.portalX;
	}

	public int[] getPortalY(){
	return this.portalY;
	}

	public void incZombieCount() {
		this.zombieCount++;
	}
}