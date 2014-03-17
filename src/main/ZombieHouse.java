package main;

import gui.MainFrame;

import javax.swing.SwingUtilities;


public class ZombieHouse {
	
	public ZombieHouse(){
		MainFrame mf = new MainFrame();
	}
	
	public static void main(String[] args) {
		// ImgResource class loading
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ZombieHouse zh = new ZombieHouse();
			}
		});
	}
}
