package control;

// This file is part of the Multi-player Pacman Game.
//
// Pacman is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published
// by the Free Software Foundation; either version 3 of the License,
// or (at your option) any later version.
//
// Pacman is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
// the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public
// License along with Pacman. If not, see <http://www.gnu.org/licenses/>
//
// Copyright 2010, David James Pearce.

import game.Game;
import gui.MainFrame;

import java.awt.event.*;
import java.io.*;
import java.net.*;

import people.Hero;

/**
 * A slave connection receives information about the current state of the board
 * and relays that into the local copy of the board. The slave connection also
 * notifies the master connection of key presses by the player.
 */
public final class Slave extends Thread implements KeyListener {
	private final Socket socket;
	private Game game;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private int uid;
	private int totalSent;

	private MainFrame display;

	private int hoverCount = 1;

	/**
	 * Construct a slave connection from a socket. A slave connection does no
	 * local computation, other than to display the current state of the board;
	 * instead, board logic is controlled entirely by the server, and the slave
	 * display is only refreshed when data is received from the master
	 * connection.
	 * 
	 * @param socket
	 * @param dumbTerminal
	 */
	public Slave(Socket socket, MainFrame display) {
		this.socket = socket;
		this.display = display;
		this.display.setKeyListeners(this);
	}

	public void run() {
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());

			/*
			 * while(true){ System.out.println(input.readInt()); if(1==2)break;
			 * }
			 */

			// First job, is to read the period so we can create the clock
			uid = input.readInt();
			display.getMp().setUid(uid);

			System.out.println("HERO CLIENT UID: " + uid);

			game = new Game(display);
			display.setGame(game);
			display.toggleBp();
			boolean exit = false;

			while (!exit) {
				int size = input.readInt();
				// System.out.println(size);
				byte[] data = new byte[size];
				input.readFully(data);
				game.fromByteArray(data);

				display.repaint();
			}

			socket.close(); // release socket ... v.important!
		} catch (Exception e) {
			System.err.println("I/O Error: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	/**
	 * The following method calculates the rate of data received in bytes/s,
	 * albeit in a rather coarse manner.
	 * 
	 * @param amount
	 * @return
	 */
	private int rate(int amount) {
		rateTotal += amount;
		long time = System.currentTimeMillis();
		long period = time - rateStart;
		if (period > 1000) {
			// more than a second since last calculation
			currentRate = (rateTotal * 1000) / (int) period;
			rateStart = time;
			rateTotal = 0;
		}

		return currentRate;
	}

	private int rateTotal = 0; // total accumulated this second
	private int currentRate = 0; // rate of reception last second
	private long rateStart = System.currentTimeMillis(); // start of this
															// accumulation
															// perioud

	// The following intercept keyboard events from the user.

	/**
	 * Crystal's.
	 */
	public void keyPressed(KeyEvent e) {
		// System.out.println("press");
		Hero h = game.getData().getHero(uid);
		try {
			int code = e.getKeyCode();
			// System.out.println("Sending "+code);
			if (code == KeyEvent.VK_D) {
				output.writeInt(3);
				totalSent += 4;
			} else if (code == KeyEvent.VK_A) {
				output.writeInt(4);
				totalSent += 4;
			} else if (code == KeyEvent.VK_W) {
				output.writeInt(1);
				totalSent += 4;
			} else if (code == KeyEvent.VK_S) {
				output.writeInt(2);
				totalSent += 4;
			} else if (code == KeyEvent.VK_J) {
				// attack
				output.writeInt(6);
				totalSent += 4;
			} else if (code == KeyEvent.VK_I) {
				// switch to gun
				output.writeInt(7);
				totalSent += 4;
			} else if (code == KeyEvent.VK_L) {
				// switch to melee
				output.writeInt(8);
				totalSent += 4;
			} else if (code == KeyEvent.VK_K) {
				// pick up item
				output.writeInt(9);
				totalSent += 4;
			} else if (code == KeyEvent.VK_SPACE) {
				// open inventory
				this.display.toggleIp();
			}

			// following is for navigating the inventory
			else if (display.isIpVisible() && code == KeyEvent.VK_UP) {
				// if inventory is open... and if hover count is within
				// bounds...
				--hoverCount;
				if (hoverCount == 0)
					hoverCount = 3;
				game.getFrame().getIp().setHoverOn(hoverCount);

			} else if (display.isIpVisible() && code == KeyEvent.VK_DOWN) {
				// if inventory is open... and if hover count is within
				// bounds...
				++hoverCount;
				if (hoverCount == 4)
					hoverCount = 1;
				game.getFrame().getIp().setHoverOn(hoverCount);

			}

			// the following is for using and dropping items
			else if (display.isIpVisible() && code == KeyEvent.VK_LEFT) {
				if (hoverCount == 1) {
					// drop key
					output.writeInt(10);
				} else if (hoverCount == 2) {
					// drop ammo
					output.writeInt(11);
				} else if (hoverCount == 3) {
					// drop battery
					output.writeInt(12);
				}
				this.totalSent+=4;
				// shouldnt be anymore cases
			}

			// using items
			else if (display.isIpVisible() && code == KeyEvent.VK_RIGHT) {
				if (hoverCount == 1) {
					// drop key
					output.writeInt(13);
				} else if (hoverCount == 2) {
					// drop ammo
					output.writeInt(14);
				} else if (hoverCount == 3) {
					// drop battery
					output.writeInt(15);
				}
				this.totalSent+=4;
				// shouldnt be anymore cases
			}

			output.flush();
			display.repaint();
		} catch (IOException ioe) {
			// something went wrong trying to communicate the key press to the
			// server. So, we just ignore it.
		}
	}

	public void keyReleased(KeyEvent e) {
		try {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_D || code == KeyEvent.VK_A
					|| code == KeyEvent.VK_W || code == KeyEvent.VK_S) {
				output.writeInt(5);
				totalSent += 4;
			}
			
			output.flush();
		} catch (IOException ioe) {
			// something went wrong trying to communicate the key press to the
			// server. So, we just ignore it.
		}

	}

	public void keyTyped(KeyEvent e) {

	}
}
