package it.wargame.map;

import it.wargame.creatures.Creature;
import it.wargame.util.Util;

import org.newdawn.slick.Color;
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

	public void render(GameContainer container, StateBasedGame state, Graphics g, Creature selectedCreature) throws SlickException {
		Color color;
		// green if a tile is in range of selected creature, gray is already moved
		for (int i = 0; i < map.getSize(); i++) {
			for (int j = 0; j < map.getSize(); j++) {
				if (map.getTile(i, j) == GameMap.GRASS) {
					grassImage = Util.get("grass.png").getScaledCopy(2);
					if (selectedCreature!=null && selectedCreature.isInRangeMovement(i, j)){
						if (!selectedCreature.isMoved()){
							color = Color.green;
						} else {
							color = Color.lightGray;
						}
						g.drawImage(grassImage, i * 64, j * 64, color);
					} else {
						g.drawImage(grassImage, i * 64, j * 64);						
					}

				}
			}
		}
	}
}
