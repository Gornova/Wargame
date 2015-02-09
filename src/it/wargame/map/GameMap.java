package it.wargame.map;

import it.wargame.creatures.Creature;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameMap {

	private int[][] map;
	private int size;

	public static final int GRASS = 0;
	public static final int BLOCK = 1;

	private GameMapRenderer mapRenderer;
	private Creature selectedCreature;

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
	
	public GameMap placeBlock(int x, int y){
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

	public boolean isGrass(int x, int y){
		return getTile(x,y) == GRASS;
	}
	
	public boolean isBlock(int x, int y) {
		return getTile(x,y) == BLOCK;
	}

}
