package gui;

import furniture.Furniture;
import game.Game;

import item.Item;
import item.Sentinal;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import building.Room;

import people.Hero;
import people.People;
import people.Zombie;

import control.Slave;

import thread.ClockThread;

import data.GameData;
import data.StartScreenData;

public class MainPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame frame;
	private StartScreenData ssd;

	private int playerUID;
	private float alpha;
	private boolean redOut;

	public void paint(Graphics g) {
		Game game = frame.getGame();
		if (game != null && game.state == Game.PLAYING) {
			drawGame(g);
		} else {
			drawStart(g);
		}
	}

	public void drawStart(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 1300, 750);
		g.setColor(Color.white);
		g.fillRect(5, 5, 1290, 740);
		// single
		Rectangle sRect = ssd.getSingleRect();
		g.drawImage(ssd.getSingleImg(), sRect.x, sRect.y, sRect.width,
				sRect.height, null);

		// multi
		Rectangle mRect = ssd.getMultiRect();
		g.drawImage(ssd.getMultiImg(), mRect.x, mRect.y, mRect.width,
				mRect.height, null);

		// quit
		Rectangle qRect = ssd.getQuitRect();
		g.drawImage(ssd.getQuitImg(), qRect.x, qRect.y, qRect.width,
				qRect.height, null);

		if (ssd.isSingleVisible()) {
			// new
			Rectangle nRect = ssd.getNewGameRect();
			g.drawImage(ssd.getNewGameImg(), nRect.x, nRect.y, nRect.width,
					nRect.height, null);

			// load
			Rectangle lRect = ssd.getLoadGameRect();
			g.drawImage(ssd.getLoadGameImg(), lRect.x, lRect.y, lRect.width,
					lRect.height, null);
		}

		if (ssd.isMultiVisible()) {
			// server
			Rectangle serRect = ssd.getServerRect();
			g.drawImage(ssd.getServerImg(), serRect.x, serRect.y,
					serRect.width, serRect.height, null);

			// client
			Rectangle cliRect = ssd.getClientRect();
			g.drawImage(ssd.getClientImg(), cliRect.x, cliRect.y,
					cliRect.width, cliRect.height, null);
		}

		drawPointer(g);
	}

	private void drawPointer(Graphics g) {
		int ind = ssd.getSelectedIndex();
		if (ind == 0) {
			g.drawImage(ssd.getPointerImg(), ssd.getSingleRect().x - 40,
					ssd.getSingleRect().y, 30, 50, null);
		} else if (ind == 1) {
			g.drawImage(ssd.getPointerImg(), ssd.getMultiRect().x - 40,
					ssd.getMultiRect().y, 30, 50, null);
		} else if (ind == 2) {
			g.drawImage(ssd.getPointerImg(), ssd.getQuitRect().x - 40,
					ssd.getQuitRect().y, 30, 50, null);
		} else if (ind == 3) {
			g.drawImage(ssd.getPointerImg(), ssd.getServerRect().x - 40,
					ssd.getServerRect().y, 30, 50, null);
		} else if (ind == 4) {
			g.drawImage(ssd.getPointerImg(), ssd.getClientRect().x - 40,
					ssd.getClientRect().y, 30, 50, null);
		} else if (ind == 5) {
			g.drawImage(ssd.getPointerImg(), ssd.getNewGameRect().x - 40,
					ssd.getNewGameRect().y, 30, 50, null);
		} else if (ind == 6) {
			g.drawImage(ssd.getPointerImg(), ssd.getLoadGameRect().x - 40,
					ssd.getLoadGameRect().y, 30, 50, null);
		}
		// shouldnt go past
	}

	/**
	 * 
	 * @param g
	 */
	public synchronized void drawGame(Graphics gr) {
		Graphics2D g = (Graphics2D) gr.create();
		// get player who owns this frame's current room
		GameData data = frame.getGame().getData();
		Hero h = data.getHero(playerUID);
		this.alpha = h.getBattery();
		// draw room
		Room room = h.getCurrentRoom();
		room.draw(g);
		/*
		 * Room room = null; try { room = h.getCurrentRoom(); } catch
		 * (NullPointerException e) { System.err.println("Hero " + playerUID +
		 * "'s current room is null"); } room.draw(g);
		 */

		// draw dead people
		for (int i = 0; i < data.getCharacters().size(); i++) {
			People p = data.getCharacters().get(i);
			if (p.getCurrentRoom() == room) {
				if (p.getState() == People.DEAD) {
					p.draw(g);
				}
			}
		}

		// draw furniture
		for (int i = 0; i < data.getFurniture().size(); i++) {
			Furniture f = data.getFurniture().get(i);
			if (f.getRoom() == room) {
				f.draw(g);
			}
		}
		// draw items
		for (int i = 0; i < data.getItems().size(); i++) {
			Item it = data.getItems().get(i);
			if(it instanceof Sentinal)continue;
			if (it.getRoom() == room) {
				//System.out.println("drew item");
				it.draw(g);
			}
		}

		// draw living people
		for (int i = 0; i < data.getCharacters().size(); i++) {
			People p = data.getCharacters().get(i);
			if (p.getCurrentRoom() == room) {
				if (p.getState() == People.ALIVE) {
					// System.out.println("Drawing player "+p.getUid());
					p.draw(g);
				}
			}
		}

		drawDarkness(g);
	}

	private void drawDarkness(Graphics gr) {
		Graphics2D g = (Graphics2D) gr.create();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		
		// determine if we are dead or not
		/*
		int state = this.frame.getGame().getData().getHero(playerUID).getState();
		if(state==People.DEAD)redOut=true;
		*/
		if (redOut) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.black);
		}
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public MainPanel(MainFrame frame) {
		this.frame = frame;
		this.ssd = new StartScreenData(frame);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Point p = new Point(x, y);
		// System.out.println("Hover "+x+" "+y);
		if (ssd.getSingleRect().contains(p)) {
			ssd.setSelectedIndex(0);
		} else if (ssd.getMultiRect().contains(p)) {
			ssd.setSelectedIndex(1);
		} else if (ssd.getQuitRect().contains(p)) {
			ssd.setSelectedIndex(2);
		} else if (ssd.isMultiVisible() && ssd.getServerRect().contains(p)) {
			ssd.setSelectedIndex(3);
		} else if (ssd.isMultiVisible() && ssd.getClientRect().contains(p)) {
			ssd.setSelectedIndex(4);
		} else if (ssd.isSingleVisible() && ssd.getNewGameRect().contains(p)) {
			ssd.setSelectedIndex(5);
		} else if (ssd.isSingleVisible() && ssd.getLoadGameRect().contains(p)) {
			ssd.setSelectedIndex(6);
		}
		frame.repaint();
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
		int x = e.getX();
		int y = e.getY();
		Point p = new Point(x, y);

		if (ssd.getSingleRect().contains(p)) {
			ssd.resetMenus();
			ssd.showSingleMenu();
			// ssd.setSelectedIndex(0);
		} else if (ssd.getMultiRect().contains(p)) {
			ssd.resetMenus();
			ssd.showMultiMenu();
			// ssd.setSelectedIndex(1);
		} else if (ssd.getQuitRect().contains(p)) {
			int i = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to quit?");
			if (i == 0) {
				System.exit(1);
			}
		} else if (ssd.isSingleVisible() && ssd.getNewGameRect().contains(p)) {
			/*
			 * frame.setGame(new SinglePlayerGame(frame));
			 * frame.getGame().setState(Game.PLAYING); frame.toggleBp();
			 * frame.getMp().setRed(false);
			 */
		} else if (ssd.isSingleVisible() && ssd.getLoadGameRect().contains(p)) {
			/*
			 * frame.setGame(new SinglePlayerGame(frame,1));
			 * frame.getGame().setState(Game.PLAYING); frame.toggleBp();
			 * frame.getMp().setRed(false);
			 */
		} else if (ssd.isMultiVisible() && ssd.getServerRect().contains(p)) {
			runServer();
		} else if (ssd.isMultiVisible() && ssd.getClientRect().contains(p)) {
			runClient();
		} else {
			ssd.setSelectedIndex(0);
			ssd.resetMenus();
		}

		frame.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	private void runClient() {
		try {
			// prompt user for address
			String addr = JOptionPane.showInputDialog("Enter an address");
			int port = 32768;
			int i = JOptionPane.showConfirmDialog(null, "Address: " + addr
					+ "\n\nPort: " + port);
			if (i == 0) {
				Socket s = new Socket(addr, port);
				JOptionPane.showMessageDialog(null, "CLIENT CONNECTED TO "
						+ addr + ":" + port);
				new Slave(s, frame).start();
			}
		} catch (IOException ioe) {
			System.out.println("I/O error: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}

	private void runServer() {
		// these should all be final
		int port = 32768;
		int gameClock = 20;
		int broadcastClock = 5;
		Game game = new Game(frame);
		frame.setGame(game);

		// make a new frame, that displays the current clients connect to my
		// server
		ClockThread clk = new ClockThread(game, frame);
		ServerFrame sf = new ServerFrame(clk, port, gameClock, broadcastClock,
				game);
	}

	public void setUid(int uid) {
		this.playerUID = uid;
		System.out.println("Canvas belongs to : " + playerUID);
	}
	
	public int getUid(){
		return this.playerUID;
	}

	public void setRed(boolean b) {
		this.redOut = b;
	}
}