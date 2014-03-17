package furniture;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

import building.Room;

public class Box extends Furniture{
	
	private Image boxImg = new ImageIcon("images/furniture/box.png").getImage();
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(boxImg, bounds.x, bounds.y, bounds.width, bounds.height, null);
	}
	
	public Box(int uid, Room room){
		this.uid = uid;
		this.type = Furniture.BOX;
		this.room = room;
	}

	@Override
	public void toOutputStream(ObjectOutputStream dout) throws IOException{
		dout.writeInt(BOX);
		dout.writeInt(uid);
		dout.writeInt(bounds.x);
		dout.writeInt(bounds.y);
		dout.writeObject(room);
	}
	
	public static Box fromInputStream(ObjectInputStream din) throws IOException{
		int uid = din.readInt();
		int x = din.readInt();
		int y = din.readInt();
		Rectangle rec = new Rectangle(x,y,150,150);
		Room room = null;
		try{
			room = (Room) din.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Box box = new Box(uid,room);
		box.bounds = rec;
		return box;
	}

	public void initialiseLocation(int i, int j) {
		this.bounds = new Rectangle(i,j,150,150);
	}
	
}
