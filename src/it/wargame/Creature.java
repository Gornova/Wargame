package it.wargame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.Subscribe;

public class Creature {

	public final static int WARRIOR = 0;
	public final static int ARCHER = 1;
	private int type;
	private int movement;
	private int damage;
	private int range;
	private int hp;
	private int x;
	private int y;
	private CreatureRenderer renderer;
	private int group;
	public final static int GROUP_PLAYER = 0;
	public final static int GROUP_AI = 1;

	private boolean moved = false;
	private boolean attacked = false;
	private int id = -1;

	private static int idGenerator = 0;

	public Creature(int type) {
		idGenerator++;
		this.id = idGenerator;
		this.type = type;
		if (type == WARRIOR) {
			this.hp = 6;
			this.damage = 1;
			this.movement = 2;
			this.range = 2;
		} else if (type == ARCHER) {
			this.hp = 4;
			this.damage = 1;
			this.movement = 1;
			this.range = 3;
		}
		renderer = new CreatureRenderer(this);
		Wargame.eventBus.register(this);
	}
	public Creature setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Creature setGroup(int g) {
		this.group = g;
		return this;
	}

	public boolean isWarrior() {
		return type == WARRIOR ? true : false;
	}

	public boolean isArcher() {
		return type == ARCHER ? true : false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static Creature buildWarrior() {
		return new Creature(Creature.WARRIOR);
	}

	public static Creature buildArcher() {
		return new Creature(Creature.ARCHER);
	}

	public void render(GameContainer container, StateBasedGame state, Graphics g) throws SlickException {
		renderer.render(container, state, g);
	}
	public boolean isLocation(int x, int y) {
		return (this.x == x && this.y == y) ? true : false;
	}

	public String getType() {
		switch (type) {
			case WARRIOR :
				return "warrior";
			case ARCHER :
				return "archer";
			default :
				return "default";
		}
	}
	public int getMovement() {
		return movement;
	}
	public int getDamage() {
		return damage;
	}
	public int getHp() {
		return hp;
	}

	public boolean isMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	public boolean isAI() {
		return group == GROUP_AI ? true : false;
	}

	public boolean isPlayer() {
		return group == GROUP_PLAYER ? true : false;
	}
	public String getGroup() {
		switch (group) {
			case GROUP_AI :
				return "AI";
			case GROUP_PLAYER :
				return "PLAYER";
			default :
				return "default";
		}
	}

	// TODO: insted of this, create a separated handler ?
	@Subscribe
	public void handleMoveEvent(MoveEvent mv) {
		if (mv.getCreature().getId() == this.getId() && mv.getOwner() == group) {
			if (!isMoved()) {
				setLocation(mv.getX(), mv.getY());
				setMoved(true);
			}
		}
	}

	private void move(int tx, int ty) {
		if (Math.abs(tx - x) >= movement) {
			tx = movement;
		}
		if (Math.abs(ty - y) >= movement) {
			ty = movement;
		}
		setLocation(tx, ty);
	}
	@Subscribe
	public void handleAttackEvent(AttackEvent ae) {
		if (ae.getTarget().getId() == this.getId()) {
			if (!ae.getTarget().isAttacked()) {
				handleCombat(ae.getAttacker(), this);
				ae.getTarget().setAttacked(true);
			}
		}
	}
	private void handleCombat(Creature atk, Creature def) {
		def.hp -= atk.damage;
		if (def.hp > 0) {
			atk.hp -= def.damage;
		}

	}
	public int getId() {
		return id;
	}
	public boolean isDead() {
		return hp <= 0 ? true : false;
	}
	public int getOwner() {
		return group;
	}
	public void setAttacked(boolean b) {
		this.attacked = b;
	}
	public boolean isAttacked() {
		return attacked;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}

	public boolean isInRange(Creature target) {
		return new Vector2f(this.x, this.y).distance(new Vector2f(target.getX(), target.getY())) < range ? true : false;
	}
}
