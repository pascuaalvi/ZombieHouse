package data;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import building.Room;
import building.Stairs;

import furniture.Furniture;

public class RoomReader {

	/**
	 * Will be used to parse a level file.
	 * @param file
	 * @return
	 */
	public static Room readFile(File file){
		ArrayList<Rectangle> walls = new ArrayList<Rectangle>();
		ArrayList<Rectangle> floors = new ArrayList<Rectangle>();
		ArrayList<Rectangle> doors = new ArrayList<Rectangle>();
		ArrayList<Rectangle> lockedDoors = new ArrayList<Rectangle>();
		ArrayList<Rectangle> gates = new ArrayList<Rectangle>();
		ArrayList<Rectangle> removableGates = new ArrayList<Rectangle>();
		ArrayList<Rectangle> stairs = new ArrayList<Rectangle>();
		
		ArrayList<Furniture> furniture = new ArrayList<Furniture>();

		try {
			Scanner scan = new Scanner(file);
			//Add level to file and read that HERE
			while(scan.hasNextLine()){
				String next = scan.next();
				if(next.equals("floor")){
					floors.add((new Rectangle(scan.nextInt(),scan.nextInt(),scan.nextInt(),scan.nextInt())));
					scan.nextLine();
				}
				if(next.equals("gate")){
					gates.add((new Rectangle(scan.nextInt(),scan.nextInt(),scan.nextInt(),scan.nextInt())));
					scan.nextLine();
				}
				if(next.equals("wall")){
					walls.add((new Rectangle(scan.nextInt(),scan.nextInt(),scan.nextInt(),scan.nextInt())));
					scan.nextLine();
				}
				if(next.equals("door")){
					doors.add((new Rectangle(scan.nextInt(),scan.nextInt(),scan.nextInt(),scan.nextInt())));
					scan.nextLine();
				}
				if(next.equals("lockedDoor")){
					lockedDoors.add((new Rectangle(scan.nextInt(),scan.nextInt(),scan.nextInt(),scan.nextInt())));
					scan.nextLine();
				}
				if(next.equals("removableGate")){
					removableGates.add((new Rectangle(scan.nextInt(),scan.nextInt(),scan.nextInt(),scan.nextInt())));
					scan.nextLine();
				}
				if(next.equals("stair")){
					// NEED TO MODIFY THIS
					stairs.add((new Rectangle(scan.nextInt(),scan.nextInt(),scan.nextInt(),scan.nextInt())));
					scan.nextLine();
				}
			}
			Stairs st = new Stairs(stairs);
			return new Room(walls,doors,lockedDoors,st);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	private static Rectangle convertFromPercentage(double x, double y, double width, double height){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int newx = (int)(screenSize.width*(x/100));
		int newy = (int)(screenSize.height*(y/100));
		int newwidth = (int)(screenSize.width*(width/100));
		int newheight = (int)(screenSize.height*(height/100));
		return new Rectangle(newx,newy,newwidth,newheight);
	}

}
