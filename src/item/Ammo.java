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

public class Ammo extends Item implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image ammoImg = new ImageIcon("images/item/ammo.png").getImage();

	public Ammo(int uid, Room room){
		this.uid = uid;
		this.room = room;
		//this.bounds = something;
		this.type = Item.AMMO;
	}
	
	@Override
	public void use(Hero h) {
		h.setAmmo(100);
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(ammoImg, bounds.x, bounds.y, bounds.width, bounds.height, null);
	}

	@Override
	public void toOutputStream(ObjectOutputStream dout) throws IOException{
		dout.writeInt(AMMO);
		dout.writeInt(uid);
		dout.writeInt(bounds.x);
		dout.writeInt(bounds.y);
		dout.writeObject(this.room);
	}
	
	public static Ammo fromInputStream(ObjectInputStream din) throws IOException{
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
		
		Ammo ammo = new Ammo(uid,room);
		
		Rectangle rec = null;
		if (ammo.getRoom()==null) {
			rec = new Rectangle(x,y,90,90);
		} else {
			rec = new Rectangle(x,y,50,50);
		}
		
		ammo.bounds = rec;
		
		return ammo;
	}
}
