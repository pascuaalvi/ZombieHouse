package furniture;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import building.Room;

public abstract class Furniture {
	
	public static int BED = 1;
	public static int BOX = 2;
	public static int COUCH = 3;
	
	public int type;
	public int uid;
	public Room room;
	public Rectangle bounds;	
	public abstract void draw(Graphics2D g) ;
	
	public abstract void toOutputStream(ObjectOutputStream dout)throws IOException;
	
	public static Furniture fromInputStream(ObjectInputStream din) throws IOException{
		int type = din.readInt();
		
		if(type==BOX){
			return Box.fromInputStream(din);
		}else if(type==BED){
			return Bed.fromInputStream(din);
		}else if(type==COUCH){
			return Couch.fromInputStream(din);
		}
		return null;
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
	
	
	
}
