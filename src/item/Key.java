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

import data.GameData;

import building.Room;

public class Key extends Item implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image keyImg = new ImageIcon("images/item/key.png").getImage();

	public Key(int uid, Room room){
		this.uid = uid;
		this.room = room;
		//this.bounds = something;
		this.type = Item.KEY;
	}
	
	@Override
	public void use(Hero h) {
		// unsupported
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(keyImg, bounds.x, bounds.y, bounds.width, bounds.height, null);
	}

	@Override
	public void toOutputStream(ObjectOutputStream dout) throws IOException{
		dout.writeInt(KEY);
		dout.writeInt(uid);
		dout.writeInt(bounds.x);
		dout.writeInt(bounds.y);
		dout.writeObject(this.room);
	}
	
	public static Key fromInputStream(ObjectInputStream din) throws IOException{
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
		
		Rectangle rec = null;
		if(room==null){
			rec = new Rectangle(x,y,90,90);
		}else{
			rec = new Rectangle(x,y,50,50);
		}
		
		Key key = new Key(uid,room);
		key.bounds = rec;
		
		return key;
	}

}
