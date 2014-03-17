package game;

import furniture.Furniture;
import gui.MainFrame;

import item.Item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import people.Hero;
import people.People;
import people.Zombie;

import data.GameData;

public class Game {

	public static int state;
	public static int NOTPLAYING = 0;
	public static int READY = 1;
	public static int PLAYING = 2;
	public static int PAUSE = 3;
	public static int WAITING = 4;

	private GameData data;
	private MainFrame frame;

	public Game(MainFrame frame) {
		this.frame = frame;
		this.data = new GameData(frame);
		this.state = NOTPLAYING;
	}

	public byte[] toByteArray()  {
		try{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream dout = new ObjectOutputStream(bout);

		// 0. State of the game
		dout.writeInt(state);
		
		// 1. write characters
		int nchar = data.getCharacters().size();
		dout.writeInt(nchar);
		for(int i=0; i<data.getCharacters().size(); i++){
			People p = data.getCharacters().get(i);
			p.toOutputStream(dout);
		}

		// 2. write furniture
		int nfurn = data.getFurniture().size();
		dout.writeInt(nfurn);
		for(int i=0; i<data.getFurniture().size(); i++){
			Furniture f = data.getFurniture().get(i);
			f.toOutputStream(dout);
		}

		// 3. write items
		int nitem = data.getItems().size();
		dout.writeInt(nitem);
		for(int i=0; i<data.getItems().size(); i++){
			Item it = data.getItems().get(i);
			it.toOutputStream(dout);
		}

		dout.flush();
		return bout.toByteArray();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	public synchronized void fromByteArray(byte[] bytes) throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		ObjectInputStream din = new ObjectInputStream(bin);
		
		// 0. read state
		this.state = din.readInt();
		
		// 1. read people
		int npeople = din.readInt();
		data.clearPeople();
		for(int i=0; i<npeople; i++){
			data.getCharacters().add(People.fromInputStream(din));
		}
		
		// 2. read furniture
		int nfurn = din.readInt();
		data.clearFurniture();
		for(int i=0; i<nfurn; i++){
			data.getFurniture().add(Furniture.fromInputStream(din));
		}
		
		// 3. read items
		int nitems = din.readInt();
		data.clearItems();
		for(int i=0; i<nitems; i++){
			data.getItems().add(Item.fromInputStream(din));
		}
	}

	public void disconnectPlayer(int uid) {
		
	}

	public GameData getData() {
		return data;
	}

	public void setData(GameData data) {
		this.data = data;
	}

	public MainFrame getFrame() {
		return frame;
	}

	public void setFrame(MainFrame frame) {
		this.frame = frame;
	}

	public void clocktick() {
		// tick people
		for(People p : this.getData().getCharacters()){
			//if(p instanceof Zombie)System.out.println("IS ZOMBIE");
			//if(p instanceof Hero)System.out.println("IS HERO");
			p.clocktick(this);
		}
	}

	public void setState(int state) {
		Game.state = state;
	}

}
