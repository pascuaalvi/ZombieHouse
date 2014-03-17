package item;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import people.Hero;

public class Sentinal extends Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sentinal is basically a dummy item.
	 */
	public Sentinal(){
		this.type = 5555;
	}

	@Override
	public void use(Hero h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toOutputStream(ObjectOutputStream dout) throws IOException {
		dout.writeInt(this.type);
	}

	public static Sentinal fromInputStream(ObjectInputStream din) throws IOException{
		return new Sentinal();
	}
}
