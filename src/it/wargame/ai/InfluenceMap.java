package it.wargame.ai;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import it.wargame.creatures.Creature;
import it.wargame.map.GameMap;

/**
 * Influence map computed from given gameMap<br>
 * =0 => neutral <br>
 * >0 => my side <br>
 * <0 => enemy side
 * 
 */
public class InfluenceMap {

	private static final float MAX_INFLUENCE = 12;
	public float[][] influence;
	private int size;

	public InfluenceMap(int size) {
		this.size = size;
		init();
	}

	private void init() {
		this.influence = new float[size][size];
	}

	public void update(GameMap gameMap, int myGroup) {
		init();
		// each enemy creature causes influence on map
		for (Creature creature : gameMap.getCreatures()) {
			if (creature.isNotGroup(myGroup)) {
				propagate(creature.getX(), creature.getY(), gameMap);
			}
		}
	}

	private void propagate(int x, int y, GameMap gameMap) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i == x && j == y) {
					influence[i][j] = MAX_INFLUENCE;
				} else {
					Vector2f c = new Vector2f(x, y);
					Vector2f d = new Vector2f(i, j);
					influence[i][j] = clamp(MAX_INFLUENCE - Math.abs(c.distance(d) * 1.2f), 0, MAX_INFLUENCE);
				}
			}
		}

	}

	public void render(Graphics g, Color color) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (influence[i][j] > 0) {
					float alpha = clamp(influence[i][j] / MAX_INFLUENCE, 0, 0.5f);
					color.a = alpha;
					g.setColor(color);
					g.fillRect(i * 32, j * 32, 32, 32);
					g.setColor(Color.white);
				}
			}
		}
	}

	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}

}
