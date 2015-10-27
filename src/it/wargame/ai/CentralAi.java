package it.wargame.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.Log;

import it.wargame.Wargame;
import it.wargame.creatures.Creature;
import it.wargame.events.AttackEvent;
import it.wargame.events.NextTurnEvent;
import it.wargame.map.GameMap;

/**
 * An AI that control all units on map
 */
public class CentralAi implements AiInterface {

	private List<Creature> creatures = new ArrayList<Creature>();
	private GameMap gameMap;
	private InfluenceMap influence;
	private int myGroup;
	private Color color;

	public CentralAi(GameMap gameMap, int myGroup, Color color) {
		this.gameMap = gameMap;
		this.influence = new InfluenceMap(gameMap.getSize());
		Wargame.eventBus.register(this);
		this.myGroup = myGroup;
		this.color = color;
	}

	@Override
	public void setCreature(Creature creature) {

	}

	public void add(Creature c) {
		this.creatures.add(c);
	}

	@Override
	public void handleNextTurn(NextTurnEvent e) {
		// compute influence map
		influence.update(gameMap, myGroup);
		// for every creature, handle AI
		for (Creature creature : creatures) {
			handleAi(creature);
		}
	}

	private void handleAi(Creature creature) {
		// compute a list of all possible moves
		List<Move> moves = buildMoves(creature);
		// rank them
		moves = rankMoves(moves, creature);
		// select first and execute
		if (moves != null && !moves.isEmpty()) {
			Move m = moves.get(0);
			execute(m, creature);
		} else {
			Log.debug("No moves to select!");
		}
	}

	private List<Move> buildMoves(Creature creature) {
		List<Move> moves = new ArrayList<Move>();
		// compute a list of all points around cell where it can go
		int cx = creature.getX();
		int cy = creature.getY();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (gameMap.isValid(cx + i, cy + j) && !gameMap.isBlock(cx + i, cy + j)
						&& !gameMap.isCreatureAt(cx + i, cy + j, myGroup)) {
					// for each possible move, check for type (move, attack)
					Move m = new Move(i, j);
					setType(m, creature);
					moves.add(m);
				}
			}
		}
		return moves;
	}

	private void setType(Move m, Creature creature) {
		int tx = creature.getX() + m.dx;
		int ty = creature.getY() + m.dy;
		if (gameMap.isCreatureAtNotPartOfGroup(tx, ty, myGroup)) {
			Creature target = gameMap.getCreature(tx, ty);
			m.target = target;
			m.attack = true;
			m.movement = false;
		} else {
			m.attack = false;
			m.movement = true;
		}

	}

	private List<Move> rankMoves(List<Move> moves, Creature creature) {
		// for now ranking moves use utility (goal) as follow:
		// first kill archer, then warrior, then move
		for (Move move : moves) {
			calculateUtility(move, creature);
		}
		// order using utility
		Collections.sort(moves);
		return moves;
	}

	// utility is a value between 0 an 1 (a score for moves)
	// bonus: use influence map by enemy units!
	private void calculateUtility(Move move, Creature creature) {
		double utility = 0;
		int tx = creature.getX() + move.dx;
		int ty = creature.getY() + move.dy;
		if (move.attack) {
			if (move.target.isArcher()) {
				utility = 0.8;
			} else if (move.target.isWarrior()) {
				utility = 0.6;
			}
		} else if (move.movement) {
			// put into equation that if a movement get near an enemy, is more
			// imporatant that one is just moving
			if (enemyNearby(tx, ty)) {
				utility = 0.4;
			} else {
				utility = 0.2;
			}

		}
		if (influence.influence[tx][ty] > 0) {
			utility += influence.influence[tx][ty];
		}
		move.utility = utility;
	}

	private boolean enemyNearby(int tx, int ty) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (gameMap.isValid(tx + i, ty + j) && !gameMap.isBlock(tx + i, ty + j)
						&& gameMap.isCreatureAtNotPartOfGroup(tx + i, ty + j, myGroup)) {
					// if there is an enemy nearby
					return true;
				}
			}
		}
		return false;
	}

	private void execute(Move m, Creature creature) {
		if (m.movement) {
			int tx = creature.getX() + m.dx;
			int ty = creature.getY() + m.dy;
			// MoveEvent move = new MoveEvent(creature, tx, ty,
			// Creature.GROUP_AI);
			// Wargame.eventBus.post(move);
			// don't use move event, but event, because other unit must know
			// where units are
			creature.move(tx, ty);
		} else {
			AttackEvent attack = new AttackEvent(creature, m.target);
			Wargame.eventBus.post(attack);
		}

	}

	// just moves, in delta for current position
	private class Move implements Comparable<Move> {

		public double utility;
		public Creature target;
		public boolean movement;
		public boolean attack;
		public int dx;
		public int dy;

		public Move(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		@Override
		public int compareTo(Move o) {
			if (this.utility > o.utility) {
				return -1;
			} else if (this.utility < o.utility) {
				return 1;
			}
			return 0;
		}

	}

	public void render(Graphics g) {
		influence.render(g, color);
	}

}
