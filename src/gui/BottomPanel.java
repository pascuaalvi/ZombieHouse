package gui;

import game.Game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import people.*;

import data.GameData;

public class BottomPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	private MainFrame frame;
	private float alpha = 0.5f;
	private boolean hoverInv = false;
	private boolean hoverLeave = false;

	// dimensions
	private int invX;
	private int invY;
	private int invWidth;
	private int invHeight;

	private int invTextX;
	private int invTextY;

	private int leaveX;
	private int leaveY;
	private int leaveWidth;
	private int leaveHeight;

	private int leaveTextX;
	private int leaveTextY;

	private int[] playerInfoX = new int[4];
	private int playerInfoY = 20;
	private int playerInfoY2 = 35;
	private int playerInfoY3 = 50;

	@Override
	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D) gr.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		drawButtons(g);
		drawAllPlayerStatus(g);
		drawHover(g);
	}

	private synchronized void drawAllPlayerStatus(Graphics2D g) {
		// there should only be up to four!
		if (frame.getGame() == null) {
			System.out.println("game is still null");
			return;
		}
		GameData data = frame.getGame().getData();
		g.setColor(Color.white);
		int j = 0;
		for (int i = 0; i < data.getCharacters().size(); i++) {
			People p = data.getCharacters().get(i);
			if (p instanceof Hero) {
				Hero h = (Hero) p;
				g.drawString(h.getWeaponString(), playerInfoX[j], playerInfoY2);
				g.drawString("Gun Ammo: " + h.getAmmo() + "/100",
						playerInfoX[j], playerInfoY3);
				g.drawString(h.getHp() + "/100", playerInfoX[j++], playerInfoY);
			}
		}
	}

	private void drawButtons(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRoundRect(invX, invY, invWidth, invHeight, 5, 5);
		g.fillRoundRect(leaveX, leaveY, leaveWidth, leaveHeight, 5, 5);
		g.setColor(Color.white);
		g.drawString("Inventory", invTextX, invTextY);
		g.drawString("Leave", leaveTextX, leaveTextY);
	}

	private void drawHover(Graphics2D g) {
		g.setColor(Color.white);
		if (hoverInv) {
			g.fillRoundRect(invX, invY, invWidth, invHeight, 5, 5);
		} else if (hoverLeave) {
			g.fillRoundRect(leaveX, leaveY, leaveWidth, leaveHeight, 5, 5);
		}
	}

	private void setDimensions() {
		invX = 5;
		invY = 5;
		invWidth = 65;
		invHeight = 48;
		invTextX = 13;
		invTextY = 32;

		leaveX = frame.getWidth() - 70;
		leaveY = 5;
		leaveWidth = 65;
		leaveHeight = 48;
		leaveTextX = frame.getWidth() - 53;
		leaveTextY = 32;

		int space = (leaveX - 5) - (invWidth + 10);
		this.playerInfoX[0] = 75 + 20;
		this.playerInfoX[1] = (space / 4) + 20;
		this.playerInfoX[2] = (space / 2) + 20;
		this.playerInfoX[3] = ((space * 3) / 4) + 20;
	}

	public BottomPanel(MainFrame frame) {
		this.frame = frame;
		setDimensions();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		this.hoverInv = false;
		this.hoverLeave = false;
		if (x > invX && x < invX + invWidth && y > invY && y < invY + invHeight) {
			this.hoverInv = true;
			// this.repaint();
		} else if (x > leaveX && x < leaveX + leaveWidth && y > leaveY
				&& y < leaveY + leaveHeight) {
			this.hoverLeave = true;
		}
		frame.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("Pressed");
		int x = e.getX();
		int y = e.getY();
		if (x > invX && x < invX + invWidth && y > invY && y < invY + invHeight) {
			frame.toggleIp();
		} else if (x > leaveX && x < leaveX + leaveWidth && y > leaveY
				&& y < leaveY + leaveHeight) {
			// go back to start screen, prompt user
			int i = JOptionPane.showConfirmDialog(null,
					"Go back to start screen?");
			if (i == 0) {

				frame.getGame().setState(Game.NOTPLAYING);
				frame.toggleBp();
				frame.getMp().setRed(false);
				frame.closeInventory();
				frame.getGame().setData(null);
				frame.setGame(null);
				

			}
			frame.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
