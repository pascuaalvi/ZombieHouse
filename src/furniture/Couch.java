package furniture;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

import building.Room;

public class Couch extends Furniture{

	private Image couchImg = new ImageIcon("images/furniture/couch.png").getImage();
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(couchImg, bounds.x, bounds.y, bounds.width, bounds.height, null);
	}
	
	public Couch(int uid, Room room){
		this.uid = uid;
		this.type = Furniture.COUCH;
		//this.bounds = something;
		this.room = room;
	}

	@Override
	public void toOutputStream(ObjectOutputStream dout) throws IOException{
		dout.writeInt(COUCH);
		dout.writeInt(uid);
		dout.writeInt(bounds.x);
		dout.writeInt(bounds.y);
		dout.writeObject(room);
	}
	
	public static Couch fromInputStream(ObjectInputStream din) throws IOException{
		int uid = din.readInt();
		int x = din.readInt();
		int y = din.readInt();
		Rectangle rec = new Rectangle(x,y,130,100);
		Room room = null;
		try{
			room = (Room) din.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Couch couch = new Couch(uid,room);
		couch.bounds.setBounds(rec);
		return couch;
	}
	
}
