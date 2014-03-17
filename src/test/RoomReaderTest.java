package test;

import java.awt.Graphics;
import java.io.File;

import javax.swing.JFrame;

import building.Room;
import data.RoomReader;

public class RoomReaderTest {
	
	public static void main(String[]args){
		testRoom();
	}
	
	public static void testRoom(){
		// enter level and room number
		final Room r = RoomReader.readFile(new File("levelFiles/level4_room5"));
		JFrame f = new JFrame("@Test - RoomReader"){
			@Override
			public void paint(Graphics g){
				r.draw(g);
			}
		};
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(1300,750);
		f.setVisible(true);
		//assertTrue(1==1);
	}
}
