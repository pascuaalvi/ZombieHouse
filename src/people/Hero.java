package people;

import game.Game;
import item.Ammo;
import item.Battery;
import item.Item;
import item.Key;
import item.Sentinal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import data.GameData;

import building.Room;

public class Hero extends People implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Gun Attack
	public Image gunfireR = new ImageIcon(
			"images/people/Hero/HeroGun/HeroGunFire/Right.png")

	.getImage();
	public Image gunfireL = new ImageIcon(
			"images/people/Hero/HeroGun/HeroGunFire/Left.png").getImage();
	public Image gunfireU = new ImageIcon(
			"images/people/Hero/HeroGun/HeroGunFire/Up.png").getImage();
	public Image gunfireD = new ImageIcon(
			"images/people/Hero/HeroGun/HeroGunFire/Down.png").getImage();

	// Gun Idle
	public static Image gunR = new ImageIcon(
			"images/people/Hero/HeroGun/Right.png").getImage();
	public static Image gunL = new ImageIcon(
			"images/people/Hero/HeroGun/Left.png").getImage();
	public static Image gunU = new ImageIcon(
			"images/people/Hero/HeroGun/Up.png").getImage();
	public static Image gunD = new ImageIcon(
			"images/people/Hero/HeroGun/Down.png").getImage();

	// Sword Idle
	public static Image swdR = new ImageIcon(
			"images/people/Hero/HeroSword/Idle/Right.png").getImage();
	public static Image swdL = new ImageIcon(
			"images/people/Hero/HeroSword/Idle/Left.png").getImage();
	public static Image swdU = new ImageIcon(
			"images/people/Hero/HeroSword/Idle/Up.png").getImage();
	public static Image swdD = new ImageIcon(
			"images/people/Hero/HeroSword/Idle/Down.png").getImage();

	// Sword Animate
	public static Image slshR = new ImageIcon(
			"images/people/Hero/HeroSword/Animation/Right/HeroSword2.png")
			.getImage();
	public static Image slshL = new ImageIcon(
			"images/people/Hero/HeroSword/Animation/Left/HeroSword2.png")
			.getImage();
	public static Image slshU = new ImageIcon(
			"images/people/Hero/HeroSword/Animation/Up/HeroSword2.png")
			.getImage();
	public static Image slshD = new ImageIcon(
			"images/people/Hero/HeroSword/Animation/Down/HeroSword2.png")
			.getImage();

	public static int GUN = 0;
	public static int SWORD = 1;

	private int selectedWeapon;
	private int attackPower;
	private int ammo;
	private int lag;
	private int range;
	private float battery;
	private Item[] inventory;
	private boolean attacking;
	private boolean pickingUp;

	@Override
	public void draw(Graphics2D g) {
		if (super.bounds != null) {

			if (super.hp <= 0) {
				g.drawImage(super.blood, super.bounds.x, super.bounds.y,
						super.bounds.width, super.bounds.height, null);
			} else {
				g.drawImage(super.img, super.bounds.x, super.bounds.y,
						super.bounds.width, super.bounds.height, null);
				if (attacking || lag > 0) {
					lag--;
					g.setColor(Color.black);
					if (ammo > 0) {
						if (super.getDirection() == People.UP) {
							g.drawLine(super.getMidX() + 5, super.getMidY(),
									super.getMidX() + 5, super.getMidY()
											- range);
						} else if (super.getDirection() == People.DOWN) {
							g.drawLine(super.getMidX() - 5, super.getMidY(),
									super.getMidX() - 5, super.getMidY()
											+ range);
						} else if (super.getDirection() == People.LEFT) {
							g.drawLine(super.getMidX(), super.getMidY() - 5,
									super.getMidX() - range,
									super.getMidY() - 5);
						} else if (super.getDirection() == People.RIGHT) {
							g.drawLine(super.getMidX(), super.getMidY() + 5,
									super.getMidX() + range,
									super.getMidY() + 5);
						}
					}
				}
			}
		}
	}

	public Hero(int id) {
		this.uid = id;
		this.type = People.HERO;
		this.bounds = new Rectangle(10 + (60 * id), 10 + (60 * id), 50, 50);
	}

	public void setWeapon(int weapon) {
		this.selectedWeapon = weapon;
	}

	public void setPower(int power) {
		this.attackPower = power;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public void setBattery(float batt) {
		this.battery = batt;
	}

	@Override
	public void toOutputStream(ObjectOutputStream out) throws IOException {
		out.writeInt(type);
		out.writeInt(uid);

		out.writeInt(state);
		out.writeInt(hp);
		out.writeInt(direction);
		out.writeInt(speed);

		out.writeInt(bounds.x);
		out.writeInt(bounds.y);

		out.writeInt(selectedWeapon);
		out.writeInt(attackPower);
		out.writeInt(ammo);
		out.writeInt(range);
		out.writeFloat(battery);

		out.writeBoolean(attacking);
		out.writeBoolean(pickingUp);

		out.writeObject(currentRoom);

		// inventory
		out.writeInt(inventory.length);
		for (Item i : inventory) {
			i.toOutputStream(out);
			/*
			 * if(i instanceof Key){ Key k = (Key)i; out.writeObject(k); }else
			 * if(i instanceof Ammo){ Ammo a = (Ammo)i; out.writeObject(a);
			 * }else if(i instanceof Ammo){ Battery b = (Battery)i;
			 * out.writeObject(b); }
			 */
		}
	}

	public static Hero fromInputStream(ObjectInputStream din)
			throws IOException {
		// read Hero data
		int id = din.readInt();

		int state = din.readInt();
		int hp = din.readInt();
		int dir = din.readInt();
		int speed = din.readInt();

		int x = din.readInt();
		int y = din.readInt();
		Rectangle rec = new Rectangle(x, y, 50, 50);

		int weapon = din.readInt();
		int power = din.readInt();
		int amm = din.readInt();
		int ran = din.readInt();
		float batt = din.readFloat();

		boolean attack = din.readBoolean();
		boolean pick = din.readBoolean();

		Room room = null;
		try {
			room = (Room) din.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// read inventory
		int ninventory = din.readInt();
		Item[] toBeInventory = new Item[3];
		for (int i = 0; i < ninventory; i++) {
			toBeInventory[i] = Item.fromInputStream(din);
		}

		// create hero and set data
		Hero hero = new Hero(id);
		hero.setState(state);

		hero.setHp(hp);
		hero.setDirection(dir);

		hero.setSpeed(speed);

		hero.bounds.setBounds(rec);
		hero.setCurrentRoom(room);

		hero.setWeapon(weapon);
		hero.setPower(power);
		hero.setAmmo(amm);
		hero.setRange(ran);
		hero.setBattery(batt);

		hero.setAttacking(attack);
		hero.setPicking(pick);

		hero.setInventory(toBeInventory);

		if (dir == DOWN) {
			if (weapon == GUN) {
				hero.setImg(Hero.gunD);
			} else {
				hero.setImg(Hero.swdD);
			}
		} else if (dir == UP) {
			if (weapon == GUN) {
				hero.setImg(Hero.gunU);
			} else {
				hero.setImg(Hero.swdU);
			}
		} else if (dir == RIGHT) {
			if (weapon == GUN) {
				hero.setImg(Hero.gunR);
			} else {
				hero.setImg(Hero.swdR);
			}
		} else if (dir == LEFT) {
			if (weapon == GUN) {
				hero.setImg(Hero.gunL);
			} else {
				hero.setImg(Hero.swdL);
			}
		}

		// at last... return the hero
		return hero;
	}

	private void setInventory(Item[] toBeInventory) {
		this.inventory = toBeInventory;
	}

	public void setPicking(boolean pick) {
		this.pickingUp = pick;
	}

	public void setAttacking(boolean attack) {
		this.attacking = attack;
	}

	// === actions ===

	public void attack() {
		this.attacking = true;
		setAttackingImage();
	}

	private void setAttackingImage() {
		if (this.direction == UP) {
			if (selectedWeapon == GUN) {
				this.img = this.gunfireU;
			} else {
				this.img = this.slshU;
			}
		} else if (this.direction == DOWN) {
			if (selectedWeapon == GUN) {
				this.img = this.gunfireD;
			} else {
				this.img = this.slshD;
			}
		} else if (this.direction == RIGHT) {
			if (selectedWeapon == GUN) {
				this.img = this.gunfireR;
			} else {
				this.img = this.slshR;
			}
		} else if (this.direction == LEFT) {
			if (selectedWeapon == GUN) {
				this.img = this.gunfireL;
			} else {
				this.img = this.slshL;
			}
		}
	}

	public void shiftGun() {
		this.selectedWeapon = GUN;
		this.range = 800;
		this.attackPower = 10;
	}

	public void shiftMelee() {
		this.selectedWeapon = SWORD;
		this.range = 100;
		this.attackPower = 30;
	}

	public void pickUp() {
		this.pickingUp = true;
	}

	/**
	 * Picks up an item. Puts it into inventory. Removes from room.
	 * 
	 * @param item
	 */
	public void pickUpItem(Item item) {
		// check if we already have this item
		for (Item it : inventory) {
			if (it instanceof Key && item instanceof Key)
				return;
			if (it instanceof Ammo && item instanceof Ammo)
				return;
			if (it instanceof Battery && item instanceof Battery)
				return;
		}

		int x = 10;
		int y1 = 10;
		int y2 = 110;
		int y3 = 210;
		int size = 90;

		item.setRoom(null);
		if (item instanceof Key) {
			inventory[0] = item;
			item.setBounds(new Rectangle(x, y1, size, size));
		} else if (item instanceof Ammo) {
			inventory[1] = item;
			item.setBounds(new Rectangle(x, y2, size, size));
		} else if (item instanceof Battery) {
			inventory[2] = item;
			item.setBounds(new Rectangle(x, y3, size, size));
		}

	}

	/**
	 * Drops an item into room at hero's position. Removes it from inventory.
	 * 
	 * @param i
	 */
	public void dropItem(int i) {
		Item item = this.inventory[i];
		item.setRoom(currentRoom);
		item.initialiseLocation(bounds.x, bounds.y);
		inventory[i] = new Sentinal();
	}

	/**
	 * Uses an item, removes from inventory, excluding key
	 * 
	 * @param i
	 */
	public void useItem(int i) {
		Item item = inventory[i];
		if (item instanceof Key)
			return;
		inventory[i] = new Sentinal();
		item.use(this);
	}

	public Item[] getInventory() {
		return this.inventory;
	}

	public int getSelectedWeapon() {
		return selectedWeapon;
	}

	public int getAttackPower() {
		return attackPower;
	}

	public int getAmmo() {
		return ammo;
	}

	public int getRange() {
		return range;
	}

	public float getBattery() {
		return battery;
	}

	public String getWeaponString() {
		if (this.selectedWeapon == GUN) {
			return "Gun";
		} else if (this.selectedWeapon == SWORD) {
			return "Sword";
		} else {
			return "";
		}
	}

	public void initialise(GameData data) {
		this.hp = 100;
		this.ammo = 100;
		this.direction = DOWN;
		this.currentRoom = data.getStartRoom();
		this.speed = 3;
		this.state = ALIVE;
		this.battery = 0.0001f;
		this.setImg(gunD);
		this.shiftGun();
		Item[] inventory2 = { new Sentinal(), new Sentinal(), new Sentinal() };
		this.inventory = inventory2;
		this.attacking = false;
		this.pickingUp = false;
	}

	/**
	 * Clocktick extension for hero attacking
	 * 
	 * @param game
	 */
	public void attemptAttack(Game game) {
		if (this.attacking != true)
			return;

		GameData data = game.getData();
		if (selectedWeapon == GUN) {
			if (ammo > 0) {
				this.ammo--;
			}
		}

		if ((selectedWeapon == GUN && ammo > 0) || (selectedWeapon == SWORD)) {
			if (super.getDirection() == People.UP) {
				for (int i = super.getMidY(); (i != (super.getMidY() - range)); i--) {
					if (i == (0))
						break;
					for (People p : data.getCharacters()) {
						if (p == this)
							continue;
						if (p.getState() == People.DEAD)
							continue;
						if (p.bounds.contains(new Point(super.getMidX(), i))) {
							// this person got hit
							p.takeDamage(attackPower);
							this.attacking = false;
							return;
						}
					}
				}
			} else if (super.getDirection() == People.DOWN) {
				for (int i = super.getMidY(); (i != (super.getMidY() + range)); i++) {
					if (i == (game.getFrame().getHeight()))
						break;
					for (People p : data.getCharacters()) {
						if (p == this)
							continue;
						if (p.getState() == People.DEAD)
							continue;
						if (p.bounds.contains(new Point(super.getMidX(), i))) {
							// this person got hit
							p.takeDamage(attackPower);
							this.attacking = false;
							return;
						}
					}
				}
			} else if (super.getDirection() == People.LEFT) {
				for (int i = super.getMidX(); (i != (super.getMidX() - range)); i--) {
					if (i == (0))
						break;
					for (People p : data.getCharacters()) {
						if (p == this)
							continue;
						if (p.getState() == People.DEAD)
							continue;
						if (p.bounds.contains(new Point(i, super.getMidY()))) {
							// this person got hit
							p.takeDamage(attackPower);
							this.attacking = false;
							return;
						}
					}
				}
			} else if (super.getDirection() == People.RIGHT) {
				for (int i = super.getMidX(); (i != (super.getMidX() + range)); i++) {
					if (i == (game.getFrame().getWidth()))
						break;
					for (People p : data.getCharacters()) {
						if (p == this)
							continue;
						if (p.getState() == People.DEAD)
							continue;
						if (p.bounds.contains(i, super.getMidY())) {
							// this person got hit
							p.takeDamage(attackPower);
							this.attacking = false;
							return;
						}
					}
				}
			}
		}

		this.attacking = false;
	}

	/**
	 * Clocktick extension for hero picking up item
	 * 
	 * @param game
	 */
	public void attemptPickUp(Game game) {
		if (this.pickingUp != true)
			return;

		ArrayList<Item> items = game.getData().getItems();
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			// if the item is in the same room as us
			if (item.getRoom() == this.currentRoom) {
				// if our bounds intersect
				if (item.getBounds().intersects(bounds)) {
					// pick up item;
					this.pickUpItem(item);
					this.pickingUp = false;
					return;
				}
			}
		}

		this.pickingUp = false;
	}
}
