package it.wargame.map;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import it.wargame.creatures.Creature;

public class GameMap implements TileBasedMap {

	private int[][] map;
	private int size;

	public static final int GRASS = 0;
	public static final int BLOCK = 1;

	private GameMapRenderer mapRenderer;
	private Creature selectedCreature;

	public ArrayList<Creature> creatures = new ArrayList<Creature>();

	public GameMap(int size) {
		this.size = size;
		map = new int[size][size];
		mapRenderer = new GameMapRenderer(this);
	}

	public GameMap init() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				map[i][j] = GRASS;
			}
		}
		return this;
	}

	public GameMap placeBlock(int x, int y) {
		map[x][y] = BLOCK;
		return this;
	}

	public int getTile(int x, int y) {
		if (x < 0 || x > size || y < 0 || y > size) {
			return -1;
		}
		return map[x][y];
	}

	public int getSize() {
		return size;
	}

	public void render(GameContainer container, StateBasedGame state, Graphics g) throws SlickException {
		mapRenderer.render(container, state, g, selectedCreature);
	}

	public void drawMoveable(Creature selectedCreature) {
		this.selectedCreature = selectedCreature;
	}

	public boolean isGrass(int x, int y) {
		return getTile(x, y) == GRASS;
	}

	public boolean isBlock(int x, int y) {
		return getTile(x, y) == BLOCK;
	}

	@Override
	public boolean blocked(PathFindingContext context, int x, int y) {
		return isBlock(x, y) || isCreatureAt(x, y, Creature.GROUP_AI);
	}

	@Override
	public float getCost(PathFindingContext context, int x, int y) {
		return 0;
	}

	@Override
	public int getHeightInTiles() {
		return size;
	}

	@Override
	public int getWidthInTiles() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	public void renderCreatures(GameContainer container, StateBasedGame state, Graphics g) throws SlickException {
		for (Creature c : creatures) {
			c.render(container, state, g);
		}
	}

	public void removeDeadCreatures(GameContainer gc, StateBasedGame state, int arg2) {
		// check dead creatures
		for (Iterator<Creature> iterator = creatures.iterator(); iterator.hasNext();) {
			Creature t = iterator.next();
			if (t.isDead()) {
				iterator.remove();
			}
		}

	}

	public boolean isTargetable(int tx, int ty, int targetGroup) {
		for (Creature c : creatures) {
			if (c.getOwner() != targetGroup) {
				if (c.getX() == tx && c.getY() == ty) {
					return true;
				}
			}
		}
		return false;
	}

	public Creature getCreature(int x, int y) {
		for (Creature c : creatures) {
			if (c.isLocation(x, y)) {
				return c;
			}
		}
		return null;
	}

	public boolean isCreatureAt(int x, int y, int targetGroup) {
		return getCreature(x, y) != null && getCreature(x, y).getOwner() == targetGroup ? true : false;
	}

	public void resetMoveAttackCreatures() {
		// reset moves&atacks
		for (Creature c : creatures) {
			c.setMoved(false);
			c.setAttacked(false);
		}

	}

	public ArrayList<Creature> getCreatures() {
		return creatures;
	}

	public boolean isValid(int x, int y) {
		if (x >= 0 && x < size && y >= 0 && y < size) {
			return true;
		}
		return false;
	}

	public boolean isCreatureAtNotPartOfGroup(int x, int y, int group) {
		return getCreature(x, y) != null && getCreature(x, y).getOwner() != group ? true : false;
	}

}
