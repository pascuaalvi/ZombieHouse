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

import java.util.*;
import java.io.*;
import java.net.*;

import people.People;


/**
 * A master connection receives events from a slave connection via a socket.
 * These events are registered with the board. The master connection is also
 * responsible for transmitting information to the slave about the current board
 * state. 
 */
public final class Master extends Thread {
	private final Game game;
	private final int broadcastClock;
	private final int uid;
	private final Socket socket;

	public Master(Socket socket, int uid, int broadcastClock, Game game) {
		this.game = game;	
		this.broadcastClock = broadcastClock;
		this.socket = socket;
		this.uid = uid;
	}
	
	public void run() {		
		try {
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			// First, write the period to the stream	
			
			/*
			while (true) {
				output.writeInt(uid);
				if(1==2)break;
			}
			*/
			
			output.writeInt(uid);
			boolean exit = false;
			
			while(!exit) {
				try {
					
					if(input.available() != 0) {
						// read direction event from client.
						int dir = input.readInt();
						switch(dir) {
							case 1:
								game.getData().getPlayer(uid).moveUp();
								break;
							case 2:
								//System.out.println("MOVING DOWN");
								game.getData().getPlayer(uid).moveDown();
								break;
							case 3:
								game.getData().getPlayer(uid).moveRight();
								break;
							case 4:
								game.getData().getPlayer(uid).moveLeft();
								break;
							case 5:
								game.getData().getPlayer(uid).idle();
								break;
							case 6:
								game.getData().getHero(uid).attack();
								break;
							case 7:
								game.getData().getHero(uid).shiftGun();
								break;
							case 8:
								game.getData().getHero(uid).shiftMelee();
								break;
							case 9:
								game.getData().getHero(uid).pickUp();
								break;
							case 10:
								game.getData().getHero(uid).dropItem(0);
								break;
							case 11:
								game.getData().getHero(uid).dropItem(1);
								break;
							case 12:
								game.getData().getHero(uid).dropItem(2);
								break;
							case 13:
								game.getData().getHero(uid).useItem(0);
								break;
							case 14:
								game.getData().getHero(uid).useItem(1);
								break;
							case 15:
								game.getData().getHero(uid).useItem(2);
								break;
							
						}
					} 
					
					// Now, broadcast the state of the board to client, BY TELLING THE SERVER
					//output.writeObject(board); // not serializable exc
					//for(People pe : board.getGameData().getCharacters()){
					//	output.writeObject(pe);
					//}
					
					byte[] state = game.toByteArray(); 
					output.writeInt(state.length);
					output.write(state);
					output.flush();
					Thread.sleep(broadcastClock);
				} catch(InterruptedException e) {					
				}
			}
			
			output.flush();
			//output.write(board.wallsToByteArray());
			
			/*
			boolean exit=false;
			while(!exit) {
				try {
					
					if(input.available() != 0) {
						
						// read direction event from client.
						int dir = input.readInt();
						switch(dir) {
							case 1:
								board.player(uid).moveUp();
								break;
							case 2:
								board.player(uid).moveDown();
								break;
							case 3:
								board.player(uid).moveRight();
								break;
							case 4:
								board.player(uid).moveLeft();
								break;
						}
					} 
					
					// Now, broadcast the state of the board to client, BY TELLING THE SERVER
					byte[] state = board.toByteArray(); 
					output.writeInt(state.length);
					output.write(state);
					output.flush();
					Thread.sleep(broadcastClock);
				} catch(InterruptedException e) {					
				}
			}
			*/
			socket.close(); // release socket ... v.important!
		} catch(IOException e) {
			System.err.println("PLAYER " + uid + " DISCONNECTED");
			game.disconnectPlayer(uid);
		}		
	}
}
