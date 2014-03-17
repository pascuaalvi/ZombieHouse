package building;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

public class Stairs implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Rectangle> stairs;
	private Rectangle bounds;
	
	public Stairs(List<Rectangle> stairs) {
		super();
		this.stairs = stairs;
		
		int rx = 3000;
		int ry = 3000;
		int rw = -3000;
		int rh = -3000;
		for(Rectangle r: stairs){
			if(r.x < rx){
				rx = r.x;
			}
			if(r.y < ry){
				ry = r.y;
			}
			if(r.width > rw){
				rw = r.width;
			}
			if(r.height > rh){
				rh = r.height;
			}
		}
		this.bounds = new Rectangle(rx,ry,rw,rh);
	}

	public void draw(Graphics g){
		for(Rectangle r: stairs){
			g.drawRect(r.x, r.y, r.width, r.height);
		}
	}

	public List<Rectangle> getStairs() {
		return stairs;
	}

	public void setStairs(List<Rectangle> stairs) {
		this.stairs = stairs;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	
	
}
