package it.wargame;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

import com.google.common.eventbus.Subscribe;

public class TargetAi implements AiInterface {

	private ArrayList<Creature> creatures;

	private GameWorld world;

	private Bresenham bresenham;

	private List<MoveEvent> moves = new ArrayList<MoveEvent>();

	public TargetAi(ArrayList<Creature> creatures, GameWorld world) {
		this.creatures = creatures;
		this.world = world;
		Wargame.eventBus.register(this);
		bresenham = new Bresenham();
	}

	@Subscribe
	public void handleNextTurn(NextTurnEvent e) {
		// get enemies and units
		List<Creature> enemies = getEnemies(creatures);
		List<Creature> units = getUnit(creatures);
		// assign a target for every unit
		List<Couple> couples = new ArrayList<Couple>();
		for (Creature unit : units) {
			float distance = 1000;
			Creature target = null;
			for (Creature enemy : enemies) {
				Vector2f en = new Vector2f(enemy.getX(), enemy.getY());
				Vector2f un = new Vector2f(unit.getX(), unit.getY());
				if (en.distance(un) < distance) {
					distance = en.distance(un);
					target = enemy;
				}
			}
			if (target != null) {
				couples.add(new Couple(unit, target));
			}

		}
		// move unit to target using bresenham
		for (Couple couple : couples) {
			int l = bresenham.plot(couple.unit.getX(), couple.unit.getY(), couple.target.getX(), couple.target.getY());
			if (l > 0) {
				bresenham.next();
				// couple.unit.setLocation(bresenham.getX(), bresenham.getY());
				if (world.isValid(bresenham.getX(), bresenham.getY())
						&& world.isCreature(bresenham.getX(), bresenham.getY()) == null) {
					MoveEvent move = new MoveEvent(couple.unit, bresenham.getX(), bresenham.getY(), Creature.GROUP_AI);
					if (moves.size() > 0) {
						List<MoveEvent> toadd = new ArrayList<MoveEvent>();
						for (MoveEvent m : moves) {
							if (!m.haveSameTarget(move)) {
								toadd.add(move);
								Wargame.eventBus.post(move);
							} else {
								Log.debug("same target, warning!");
							}
						}
						for (MoveEvent a : toadd) {
							moves.addAll(toadd);
						}

					} else {
						Wargame.eventBus.post(move);
						moves.add(move);
						Log.info("moved to " + bresenham.getX() + "," + bresenham.getY());
					}
				}

			}
		}
		moves.clear();

		// if is in range, attack
		for (Couple couple : couples) {
			if (couple.unit.isInRange(couple.target)) {
				Log.debug("target in range attack!!");
				Wargame.eventBus.post(new AttackEvent(couple.unit, couple.target));
			}

		}

	}
	private List<Creature> getEnemies(ArrayList<Creature> creatures) {
		List<Creature> result = new ArrayList<Creature>();
		for (Creature c : creatures) {
			if (c.isPlayer()) {
				result.add(c);
			}
		}
		return result;
	}

	private List<Creature> getUnit(ArrayList<Creature> creatures) {
		List<Creature> result = new ArrayList<Creature>();
		for (Creature c : creatures) {
			if (c.isAI()) {
				result.add(c);
			}
		}
		return result;
	}

	private class Couple {

		public Creature unit;
		public Creature target;

		public Couple(Creature unit, Creature target) {
			this.unit = unit;
			this.target = target;
		}

	}

}
