package thread;

import java.io.File;

import people.Hero;
import people.People;

import building.Room;

import data.RoomReader;
import game.Game;
import gui.MainFrame;

public class ClockThread extends Thread{
	
	private Game game;
	private MainFrame frame;
	
	public ClockThread(Game game, MainFrame frame){
		this.game = game;
		this.frame = frame;
	}
	
	@Override
	public void run(){
		
		while(true){
			//System.out.println("Playing");
			if(game.state==Game.NOTPLAYING){
				break;
			}
			delay();
			game.clocktick();
			//System.out.println("Repainting");
			frame.repaint();
		}
	}
	
	private void delay(){
		try {
			this.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
