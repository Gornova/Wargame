package it.wargame;

public class AttackEvent {

	private int x;
	private int y;
	private Creature attacker;
	private int owner;

	public AttackEvent(Creature attacker, int x, int y, int owner) {
		this.attacker = attacker;
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
		return attacker;
	}

	public void setCreature(Creature creature) {
		this.attacker = creature;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

}
