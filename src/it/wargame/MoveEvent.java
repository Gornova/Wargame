package it.wargame;

public class MoveEvent {

	private int x;
	private int y;
	private Creature creature;
	private int owner;

	public MoveEvent(Creature creature, int x, int y, int owner) {
		this.creature = creature;
		this.x = x;
		this.y = y;
		this.owner = owner;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Creature getCreature() {
		return creature;
	}

	public void setCreature(Creature creature) {
		this.creature = creature;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public boolean haveSameTarget(MoveEvent m) {
		if (this.getX() == m.getX() && this.getY() == m.getY()) {
			return true;
		}
		return false;
	}

}
