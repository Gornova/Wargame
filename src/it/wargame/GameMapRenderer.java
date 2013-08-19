package it.wargame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameMapRenderer {

	private GameMap map;
	private Image grassImage;

	public GameMapRenderer(GameMap map) {
		this.map = map;
	}

	public void render(GameContainer container, StateBasedGame state, Graphics g) throws SlickException {
		for (int i = 0; i < map.getSize(); i++) {
			for (int j = 0; j < map.getSize(); j++) {
				if (map.getTile(i, j) == GameMap.GRASS) {
					grassImage = Util.get("grass.png").getScaledCopy(2);
					g.drawImage(grassImage, i * 64, j * 64);
				}
			}
		}
	}
}
