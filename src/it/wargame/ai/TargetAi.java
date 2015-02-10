package it.wargame.ai;

import it.wargame.Wargame;
import it.wargame.creatures.Creature;
import it.wargame.events.AttackEvent;
import it.wargame.events.MoveEvent;
import it.wargame.events.NextTurnEvent;
import it.wargame.map.GameMap;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import com.google.common.eventbus.Subscribe;

public class TargetAi implements AiInterface {

	private AStarPathFinder pathFinder;

	private List<MoveEvent> moves = new ArrayList<MoveEvent>();

	private Creature creature;

	private GameMap map;

	private static final int MAXDISTANCE = 100;

	public TargetAi(GameMap map) {
		this.map = map;
		Wargame.eventBus.register(this);
		pathFinder = new AStarPathFinder(map, MAXDISTANCE, true);
	}

	@Subscribe
	public void handleNextTurn(NextTurnEvent e) {
		List<Creature> enemies = getEnemies(map.getCreatures());
		Couple couple = new Couple();
		float distance = 1000;
		Creature target = null;
		for (Creature enemy : enemies) {
			Vector2f en = new Vector2f(enemy.getX(), enemy.getY());
			Vector2f un = new Vector2f(creature.getX(), creature.getY());
			if (en.distance(un) < distance) {
				distance = en.distance(un);
				target = enemy;
			}
		}
		if (target != null) {
			couple = new Couple(creature, target);
		}

		// move unit to target using Astar
		moveUnit(couple);

		// if is in range, attack
		if (couple.unit.isInRange(couple.target)) {
			Log.debug("target in range attack!!");
			Wargame.eventBus.post(new AttackEvent(couple.unit, couple.target));
		}

	}

	private void moveUnit(Couple couple) {
		Path path = pathFinder.findPath(new DummyMover(), couple.unit.getX(),
				couple.unit.getY(), couple.target.getX(), couple.target.getY());
		int stepIndex = 1;
		if (path != null && path.getLength() > 0) {
			Step step = path.getStep(stepIndex);
			MoveEvent move = new MoveEvent(couple.unit, step.getX(),
					step.getY(), Creature.GROUP_AI);
			Wargame.eventBus.post(move);
		}
		moves.clear();
	}

	private List<Creature> getEnemies(ArrayList<Creature> creatures) {
		List<Creature> result = new ArrayList<Creature>();
		for (Creature c : creatures) {
			if (c.isGroupPlayer()) {
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

		public Couple() {
			// TODO Auto-generated constructor stub
		}

	}

	private class DummyMover implements Mover {

	}

	@Override
	public void setCreature(Creature creature) {
		this.creature = creature;
		
	}

}
