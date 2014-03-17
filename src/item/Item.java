package item;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import people.Hero;

import building.Room;

public abstract class Item implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int BATTERY = 4;
	public static int KEY = 5;
	public static int AMMO = 6;

	public int type;
	public int uid;
	public Room room;
	public Rectangle bounds;
	public Image img;

	public abstract void use(Hero h);

	public abstract void draw(Graphics2D g);

	public void pickUp(/* Hero h */) {
		// put in hero's inventory
		// remove from the room
	}
	
	public void drop(/* Hero h */){
		// remove from hero's inventory
		// put in room
	}

	public abstract void toOutputStream(ObjectOutputStream dout) throws IOException;
	
	public static Item fromInputStream(ObjectInputStream din) throws IOException {
		int type = din.readInt();
		
		if(type==Item.BATTERY){
			return Battery.fromInputStream(din);
		}else if(type==Item.AMMO){
			return Ammo.fromInputStream(din);
		}else if(type==Item.KEY){
			return Key.fromInputStream(din);
		}else{
			// 5555 - sentinal type
			return Sentinal.fromInputStream(din);
		}
		
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public void initialiseLocation(int x, int y){
		this.bounds = new Rectangle(x,y,30,30);
	}
	
}
