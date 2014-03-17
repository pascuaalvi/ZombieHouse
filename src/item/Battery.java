package item;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;

import people.Hero;

import building.Room;

public class Battery extends Item implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image battImg = new ImageIcon("images/item/battery.png").getImage();

	public Battery(int uid, Room room){
		this.uid = uid;
		this.room = room;
		//this.bounds = something;
		this.type = Item.BATTERY;
	}
	
	@Override
	public void use(Hero h) {
		h.setBattery(0.0f);
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(battImg, bounds.x, bounds.y, bounds.width, bounds.height, null);
	}

	@Override
	public void toOutputStream(ObjectOutputStream dout) throws IOException{
		dout.writeInt(BATTERY);
		dout.writeInt(uid);
		dout.writeInt(bounds.x);
		dout.writeInt(bounds.y);
		dout.writeObject(this.room);
	}
	
	public static Battery fromInputStream(ObjectInputStream din) throws IOException{
		int uid = din.readInt();
		int x = din.readInt();
		int y = din.readInt();
		Room room = null;
		try {
			room = (Room) din.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Battery batt = new Battery(uid,room);
		
		Rectangle rec = null;
		if (batt.getRoom()==null) {
			rec = new Rectangle(x,y,90,90);
		} else {
			rec = new Rectangle(x,y,50,50);
		}
		
		batt.bounds = rec;
		
		return batt;
	}		
	
}
