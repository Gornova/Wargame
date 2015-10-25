package it.wargame.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.util.Log;

import it.wargame.Wargame;
import it.wargame.creatures.Creature;
import it.wargame.events.AttackEvent;
import it.wargame.events.MoveEvent;
import it.wargame.events.NextTurnEvent;
import it.wargame.map.GameMap;

public class TestAi implements AiInterface {

	private Creature creature;
	private GameMap gameMap;

	public TestAi(GameMap gameMap) {
		this.gameMap = gameMap;
		Wargame.eventBus.register(this);
	}

	@Override
	public void setCreature(Creature creature) {
		this.creature = creature;

	}

	@Override
	public void handleNextTurn(NextTurnEvent e) {
		// compute a list of all possible moves
		List<Move> moves = buildMoves();
		// rank them
		moves = rankMoves(moves);
		// select first and execute
		if (moves != null && !moves.isEmpty()) {
			Move m = moves.get(0);
			execute(m);
		} else {
			Log.debug("No moves to select!");
		}
	}

	private List<Move> buildMoves() {
		List<Move> moves = new ArrayList<Move>();
		// distance travelled by this unit, so far just one
		int movement = 1;
		// compute a list of all points around cell where it can go
		int cx = creature.getX();
		int cy = creature.getY();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (gameMap.isValid(cx + i, cy + j) && !gameMap.isBlock(cx + i, cy + j)) {
					// for each possible move, check for type (move, attack)
					Move m = new Move(i, j);
					setType(m);
					moves.add(m);
				}
			}
		}
		return moves;
	}

	private void setType(Move m) {
		int tx = creature.getX() + m.dx;
		int ty = creature.getY() + m.dy;
		if (gameMap.isCreatureAt(tx, ty, Creature.GROUP_PLAYER)) {
			Creature target = gameMap.getCreature(tx, ty);
			m.target = target;
			m.attack = true;
			m.movement = false;
		} else {
			m.attack = false;
			m.movement = true;
		}

	}

	private List<Move> rankMoves(List<Move> moves) {
		// for now ranking moves use utility (goal) as follow:
		// first kill archer, then warrior, then move
		for (Move move : moves) {
			calculateUtility(move);
		}
		// order using utility
		Collections.sort(moves);
		return moves;
	}

	// utility is a value between 0 an 1 (a score for moves)
	private void calculateUtility(Move move) {
		double utility = 0;
		if (move.attack) {
			if (move.target.isArcher()) {
				utility = 0.8;
			} else if (move.target.isWarrior()) {
				utility = 0.6;
			}
		} else if (move.movement) {
			utility = 0.2;
		}
		move.utility = utility;
	}

	private void execute(Move m) {
		if (m.movement) {
			int tx = creature.getX() + m.dx;
			int ty = creature.getY() + m.dy;
			MoveEvent move = new MoveEvent(creature, tx, ty, Creature.GROUP_AI);
			Wargame.eventBus.post(move);
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

}
