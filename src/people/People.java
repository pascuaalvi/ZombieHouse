package people;

import furniture.Furniture;
import game.Game;

import item.*;

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
import building.Stairs;

public abstract class People implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int HERO = 100;
	public static int ZOMBIE = 101;

	public static int ALIVE = 50;
	public static int DEAD = 51;

	public Image img;
	public int type;
	public int uid;
	public Room currentRoom;
	public Rectangle bounds;
	public int hp;
	public int direction;
	public int state;
	public int speed;

	public int queued;

	public Image blood = new ImageIcon("images/game/Blood_.png").getImage();

	public static int STOP = 0;
	public static int UP = 1;
	public static int DOWN = 2;
	public static int LEFT = 3;
	public static int RIGHT = 4;

	public void moveRight() {
		queued = RIGHT;
	}

	public void moveLeft() {
		queued = LEFT;
	}

	public void moveUp() {
		queued = UP;
	}

	public void moveDown() {
		queued = DOWN;
	}

	public void idle() {
		queued = STOP;
	}

	public int getDirection() {
		// TODO Auto-generated method stub
		return direction;
	}

	public int getMidX() {
		return this.bounds.x + (this.bounds.width / 2);
	}

	public int getMidY() {
		return this.bounds.y + (this.bounds.height / 2);
	}

	public abstract void toOutputStream(ObjectOutputStream dout)
			throws IOException;

	public static People fromInputStream(ObjectInputStream din)
			throws IOException {
		int type = din.readInt();

		if (type == HERO) {
			return Hero.fromInputStream(din);
		} else if (type == ZOMBIE) {
			return Zombie.fromInputStream(din);
		}
		return null;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getQueued() {
		return queued;
	}

	public void setQueued(int queued) {
		this.queued = queued;
	}

	public Image getBlood() {
		return blood;
	}

	public void setBlood(Image blood) {
		this.blood = blood;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public abstract void draw(Graphics2D g);

	/**
	 * Carries out direction changes and movement for characters. If the
	 * character is a zombie, we need to determine who the target is and which
	 * direction we are moving in first.
	 * 
	 * If the character is a hero, we need to check if we are attacking or picking
	 * up an item.
	 * 
	 * Also, skip tick if youre dead.
	 * 
	 * @param game
	 */
	public void clocktick(Game game) {
		if(this.state==DEAD)return;
		// zombie clocktick
		if (this instanceof Zombie) {
			Zombie z = (Zombie) this;
			z.chooseTarget(game);
			z.chooseDirection(game);
		}

		// hero clocktick
		if (this instanceof Hero) {
			Hero h = (Hero) this;
			h.attemptAttack(game);
			h.attemptPickUp(game);
		}

		// direction
		if (this.queued == UP) {
			this.direction = UP;
		} else if (this.queued == DOWN) {
			this.direction = DOWN;
		} else if (this.queued == LEFT) {
			this.direction = LEFT;
		} else if (this.queued == RIGHT) {
			this.direction = RIGHT;
		}

		// movement
		if (this.queued == UP) {
			// TODO check for collisions first before moving
			if (collsionCheck(UP, game)) {
				return;
			}
			this.bounds.setLocation(bounds.x, bounds.y - speed);
		} else if (this.queued == DOWN) {
			if (collsionCheck(DOWN, game)) {
				return;
			}
			this.bounds.setLocation(bounds.x, bounds.y + speed);
		} else if (this.queued == LEFT) {
			if (collsionCheck(LEFT, game)) {
				return;
			}
			this.bounds.setLocation(bounds.x - speed, bounds.y);
		} else if (this.queued == RIGHT) {
			if (collsionCheck(RIGHT, game)) {
				return;
			}
			this.bounds.setLocation(bounds.x + speed, bounds.y);
		}
	}

	/**
	 * Returns true if person has collided with something. Collide cases
	 * involve... - the border - walls - other people - furniture - doors -
	 * locked doors
	 * 
	 * @param dir
	 * @param game
	 */
	private boolean collsionCheck(int dir, Game game) {
		// border collsion
		borderAdjustment();

		// collision with walls
		if (wallCollision(dir, game)) {
			return true;
		}

		// collision with other people
		if (playerCollsion(dir, game)) {
			return true;
		}

		// collision with furniture
		if (furnitureCollision(dir, game)) {
			return true;
		}

		// collision with door
		if (doorCollision(dir, game)) {
			return true;
		}

		// collision with locked door
		if (lockedDoorCollision(dir, game)) {
			return true;
		}

		// and finally, collision with stairs
		if (stairsCollision(game)) {
			return true;
		}

		return false;
	}

	private boolean stairsCollision(Game game) {
		Stairs s = this.currentRoom.getStairs();
		GameData data = game.getData();
		if (s.getBounds().intersects(bounds)) {
			if (this instanceof Hero) {
				// use stairs
				this.currentRoom = data.levelAdvance(currentRoom);
				this.bounds.setLocation(bounds.x, 670);
			}
		}
		return false;
	}

	private boolean wallCollision(int dir, Game game) {
		ArrayList<Rectangle> walls = this.currentRoom.getWalls();
		for (int i = 0; i < walls.size(); i++) {
			Rectangle rec = walls.get(i);
			// if bounds intersect
			if (rec.intersects(bounds)) {
				adjustPosition(dir);
				return true;
			}
		}
		return false;
	}

	private boolean lockedDoorCollision(int dir, Game game) {
		ArrayList<Rectangle> lockedDoors = this.currentRoom.getLockedDoors();
		for (int i = 0; i < lockedDoors.size(); i++) {
			Rectangle door = lockedDoors.get(i);
			// if we've hit the door...
			if (door.intersects(bounds)) {
				// if we are a hero...
				if (this instanceof Hero) {
					// if we have a key...
					Hero h = (Hero) this;
					if (containsKey(h.getInventory())) {
						// use door, determine by direction
						useDoor(dir, game);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean containsKey(Item[] inventory) {
		for (int i = 0; i < inventory.length; i++) {
			Item item = inventory[i];
			if (item instanceof Key) {
				return true;
			}
		}
		return false;
	}

	private boolean doorCollision(int dir, Game game) {
		ArrayList<Rectangle> doors = this.currentRoom.getDoors();
		for (int i = 0; i < doors.size(); i++) {
			Rectangle door = doors.get(i);
			// if we've hit the door...
			if (door.intersects(bounds)) {
				// use door, determine by direction
				useDoor(dir, game);
				return true;
			}
		}
		return false;
	}

	/**
	 * Set new room, set new coordinates/bounds.
	 * 
	 * @param dir
	 * @param game
	 */
	private void useDoor(int dir, Game game) {
		GameData data = game.getData();
		if (dir == UP) {
			this.currentRoom = data.north(currentRoom);
			this.bounds.setLocation(bounds.x, 670);
		} else if (dir == DOWN) {
			this.currentRoom = data.south(currentRoom);
			this.bounds.setLocation(bounds.x, 30);
		} else if (dir == LEFT) {
			this.currentRoom = data.west(currentRoom);
			this.bounds.setLocation(1220, bounds.y);
		} else if (dir == RIGHT) {
			this.currentRoom = data.east(currentRoom);
			this.bounds.setLocation(30, bounds.y);
		}
	}

	/**
	 * If this person has collided with a piece of furniture return true. We
	 * must also push the object that we have collided with.
	 * 
	 * @param dir
	 * @param game
	 * @return
	 */
	private boolean furnitureCollision(int dir, Game game) {
		GameData data = game.getData();
		for (int i = 0; i < data.getFurniture().size(); i++) {
			Furniture f = data.getFurniture().get(i);
			// if in same room...
			if (f.getRoom() == this.currentRoom) {
				// if bounds intersect...
				if (f.getBounds().intersects(bounds)) {
					// push furniture
					adjustFurniturePosition(dir, f);
					return true;
				}
			}
		}
		return false;
	}

	private void adjustFurniturePosition(int dir, Furniture f) {
		if (dir == UP) {
			f.bounds.setLocation(f.bounds.x, f.bounds.y - 2);
			this.bounds.setLocation(bounds.x, bounds.y + 1);
		} else if (dir == DOWN) {
			f.bounds.setLocation(f.bounds.x, f.bounds.y + 2);
			this.bounds.setLocation(bounds.x, bounds.y - 1);
		} else if (dir == RIGHT) {
			f.bounds.setLocation(f.bounds.x + 2, f.bounds.y);
			this.bounds.setLocation(bounds.x - 1, bounds.y);
		} else if (dir == LEFT) {
			f.bounds.setLocation(f.bounds.x - 2, f.bounds.y);
			this.bounds.setLocation(bounds.x + 1, bounds.y);
		}
	}

	/**
	 * If this person has collided with another person return true. Collision
	 * Cases: - Hero, do nothing - Zombie, hero takes damage
	 * 
	 * @param game
	 * @return
	 */
	private boolean playerCollsion(int dir, Game game) {
		GameData data = game.getData();
		for (int i = 0; i < data.getCharacters().size(); i++) {
			People p = data.getCharacters().get(i);
			// if we are the same person, continue loop.
			if (this == p)
				continue;
			// if we're in the same room...
			if (p.getCurrentRoom() == this.currentRoom) {
				// if the other person is still alive
				if (p.state == ALIVE) {
					// if the bounds of the two players intersect...
					if (p.getBounds().intersects(bounds)) {
						// if we are a hero, and if its a zombie...
						if (this instanceof Hero && p instanceof Zombie) {
							// player takes damage
							this.takeDamage(1);
						} else if(this instanceof Zombie && p instanceof Hero){
							p.takeDamage(i);
						}
						// player has collided with another
						adjustPosition(dir);
						return true;
					}
				}
			}
		}
		return false;
	}

	public void takeDamage(int i) {
		this.hp-=i;
		if(this.hp<=0){
			this.state = DEAD;
		}
	}

	/**
	 * Sets the person position back to its previous state.
	 * 
	 * @param dir
	 */
	private void adjustPosition(int dir) {
		if (dir == UP) {
			this.bounds.setLocation(bounds.x, bounds.y + speed);
		} else if (dir == DOWN) {
			this.bounds.setLocation(bounds.x, bounds.y - speed);
		} else if (dir == RIGHT) {
			this.bounds.setLocation(bounds.x - speed, bounds.y);
		} else if (dir == LEFT) {
			this.bounds.setLocation(bounds.x + speed, bounds.y);
		}
	}

	/**
	 * If we are attempting to walk off screen, this method will re-adjust the
	 * hero's position back into the frame.
	 */
	private void borderAdjustment() {
		// ignore this variable, was used for testing
		// System.out.println("border adjust");
		// System.exit(1);
		int amount = 1;
		if (this.bounds.x <= 0) {
			this.bounds.setLocation(0, bounds.y);
		}
		if (this.bounds.y <= 0) {
			this.bounds.setLocation(bounds.x, 0);
		}
		if (this.bounds.x + (bounds.width * amount) > 1300) {
			this.bounds.setLocation(1300 - (bounds.width * amount), bounds.y);
		}
		if (this.bounds.y + (bounds.height * amount) > 750) {
			this.bounds.setLocation(bounds.x, 750 - (bounds.height * amount));
		}
	}

}