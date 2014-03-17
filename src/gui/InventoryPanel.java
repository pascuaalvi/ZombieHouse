package gui;

import item.Sentinal;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import people.Hero;
import people.People;

import data.GameData;

public class InventoryPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	private float alpha = 0.5f;
	private int x = 10;
	private int y1 = 10;
	private int y2 = 110;
	private int y3 = 210;
	private int size = 90;

	private MainFrame frame;
	private Rectangle slot1;
	private Rectangle slot2;
	private Rectangle slot3;

	private boolean slot1Hover = false;
	private boolean slot2Hover = false;
	private boolean slot3Hover = false;

	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D) gr.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		drawInventoryPositions(g);
		drawHover(g);
		drawInventory(g);

	}

	/**
	 * Draws the player's inventory
	 * 
	 * @param g
	 */
	private void drawInventory(Graphics2D g) {
		GameData data = frame.getGame().getData();
		int id = frame.getMp().getUid();
		Hero h = data.getHero(id);

		for (int i = 0; i < h.getInventory().length; i++) {
			if (h.getInventory()[i] != null) {
				if(!(h.getInventory()[i] instanceof Sentinal)){
					h.getInventory()[i].draw(g);
				}
			}
		}

	}

	private void drawHover(Graphics2D g) {
		g.setColor(Color.white);
		if (slot1Hover) {
			g.fillRect(x, y1, size, size);
		} else if (slot2Hover) {
			g.fillRect(x, y2, size, size);
		} else if (slot3Hover) {
			g.fillRect(x, y3, size, size);
		}
		// not past here
	}

	private void drawInventoryPositions(Graphics2D g) {
		g.setColor(new Color(150, 150, 150));
		g.fillRect(x, y1, size, size);
		g.fillRect(x, y2, size, size);
		g.fillRect(x, y3, size, size);
	}

	public InventoryPanel(MainFrame frame) {
		this.frame = frame;
		setDimensions();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	private void setDimensions() {
		this.slot1 = new Rectangle(x, y1, size, size);
		this.slot2 = new Rectangle(x, y2, size, size);
		this.slot3 = new Rectangle(x, y3, size, size);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());
		resetHover();
		if (slot1.contains(p)) {
			slot1Hover = true;
		} else if (slot2.contains(p)) {
			slot2Hover = true;
		} else if (slot3.contains(p)) {
			slot3Hover = true;
		}
		// never get past here
	}

	private void resetHover() {
		slot1Hover = false;
		slot2Hover = false;
		slot3Hover = false;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());
		if (slot1.contains(p)) {

		} else if (slot2.contains(p)) {

		} else if (slot3.contains(p)) {

		} else {

		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setHoverOn(int h) {
		resetHover();
		if (h == 1)
			slot1Hover = true;
		else if (h == 2)
			slot2Hover = true;
		else if (h == 3)
			slot3Hover = true;

	}

	public boolean getHover1() {
		return this.slot1Hover;
	}

	public boolean getHover2() {
		return this.slot2Hover;
	}

	public boolean getHover3() {
		return this.slot3Hover;
	}
}
