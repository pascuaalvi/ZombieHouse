package people;

import game.Game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import data.GameData;

import building.Room;

public class Zombie extends People implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Image down = new ImageIcon("images/people/Zombie/Down.png")
			.getImage();

	private static Image up = new ImageIcon("images/people/Zombie/Up.png")
			.getImage();

	private static Image left = new ImageIcon("images/people/Zombie/Left.png")
			.getImage();

	private static Image right = new ImageIcon("images/people/Zombie/Right.png")
			.getImage();

	private int targetUID;

	@Override
	public void draw(Graphics2D g) {
		if (this.bounds == null)
			return;
		if (super.hp <= 0) {
			g.drawImage(super.blood, bounds.x, bounds.y, bounds.width,
					bounds.height, null);
		} else {
			g.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height,
					null);
		}
	}

	public Zombie(int uid) {
		this.uid = uid;
		this.type = People.ZOMBIE;
		this.bounds = new Rectangle(700 + (60 * uid), 500 + (60 * uid), 50, 50);
	}

	@Override
	public void toOutputStream(ObjectOutputStream dout) throws IOException {
		dout.writeInt(type);
		dout.writeInt(uid);
		dout.writeInt(state);
		dout.writeInt(hp);
		dout.writeInt(direction);
		dout.writeInt(speed);
		dout.writeInt(bounds.x);
		dout.writeInt(bounds.y);

		dout.writeObject(currentRoom);
		dout.writeInt(targetUID);
	}

	public static Zombie fromInputStream(ObjectInputStream din)
			throws IOException {
		// read all the stuff
		int id = din.readInt();
		int state = din.readInt();
		int hp = din.readInt();
		int dir = din.readInt();
		int speed = din.readInt();
		int x = din.readInt();
		int y = din.readInt();
		Rectangle rec = new Rectangle(x, y, 50, 50);

		Room room = null;

		try {
			room = (Room) din.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		int target = din.readInt();

		// make the zombie
		Zombie zom = new Zombie(id);
		zom.bounds = rec;
		zom.setCurrentRoom(room);
		zom.setDirection(dir);
		if (dir == UP) {
			zom.setImg(Zombie.up);
		} else if (dir == DOWN) {
			zom.setImg(Zombie.down);
		} else if (dir == RIGHT) {
			zom.setImg(Zombie.right);
		} else if (dir == LEFT) {
			zom.setImg(Zombie.left);
		} else {
			zom.setImg(down);
		}
		zom.setHp(hp);
		zom.setSpeed(speed);
		zom.setState(state);
		zom.setTarget(target);

		return zom;
	}

	private void setTarget(int target) {
		this.targetUID = target;
	}

	public void initialise(GameData data) {
		this.hp = 100;
		this.direction = DOWN;
		this.currentRoom = data.getStartRoom();
		this.speed = 1;
		this.state = ALIVE;
		this.setImg(Zombie.down);
		this.targetUID = Integer.MIN_VALUE;
	}

	/**
	 * Zombie chooses target by looping through the characters,
	 * checks if theyre in the same room,
	 * checks if its not another zombie,
	 * sets its targetId to this person,
	 * and returns.
	 * targetID is set to MIN_VALUE if no such person is found.
	 * @param game
	 */
	public void chooseTarget(Game game) {
		ArrayList<People> characters = game.getData().getCharacters();
		for (int i = 0; i < characters.size(); i++) {
			People p = characters.get(i);
			if (p.getCurrentRoom() == this.currentRoom) {
				if (p instanceof Hero) {
					this.targetUID = p.getUid();
					return;
				}
			}
		}
		this.targetUID = Integer.MIN_VALUE;
	}

	/**
	 * Chooses direction based on its targetId.
	 * If there is no target, just move randomly.
	 * Lag factor will decrease zombie speed slightly.
	 * @param game
	 */
	public void chooseDirection(Game game) {
		if(targetUID==Integer.MIN_VALUE){
			this.queued = People.STOP;
			return;
		}
		if(game.getData().getHero(targetUID)==null){
			this.queued = People.STOP;
			this.targetUID = Integer.MIN_VALUE;
			return;
		}
		
		int x = game.getData().getHero(targetUID).bounds.x - this.bounds.x;
		int y = game.getData().getHero(targetUID).bounds.y - this.bounds.y;
		// which do we use, x or y? true-x, false-y
		boolean which;
		int using;

		if (evaluateDumbness()) {
			this.queued = People.STOP;
			return;
		}

		if (Math.abs(x) < Math.abs(y)) {
			// we're using y
			which = false;
			using = y;
		} else {
			// we're using x
			which = true;
			using = x;
		}

		if (using < 0) {
			if (which) {
				queued = LEFT;
			} else {
				queued = UP;
			}
		} else if (using >= 0) {
			if (which) {
				queued = RIGHT;
			} else {
				queued = DOWN;
			}
		}
	}

	private boolean evaluateDumbness() {
		if (Math.random() > 0.85) {
			return true;
		}
		return false;
	}

}
