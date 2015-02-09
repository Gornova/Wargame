package it.wargame.creatures;

import it.wargame.util.Util;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class CreatureRenderer {

	private Creature c;
	private Image creatureImage;

	public CreatureRenderer(Creature c) {
		this.c = c;
	}

	public void render(GameContainer container, StateBasedGame state, Graphics g) throws SlickException {
		String name = "";
		int tx = c.getX() * 64;
		int ty = c.getY() * 64;
		if (c.isWarrior()) {
			if (c.isAI()) {
				name = "warrior_ai.png";
			} else {
				name = "warrior_player.png";
			}
		} else if (c.isArcher()) {
			if (c.isAI()) {
				name = "archer_ai.png";
			} else {
				name = "archer_player.png";
			}
		}
		creatureImage = Util.get(name).getScaledCopy(2);
		if (c.isMoved()) {
			g.setColor(Color.lightGray);
			g.fillRect(tx, ty, 64, 64);
			g.setColor(Color.white);
		}
		g.drawImage(creatureImage, tx, ty);
		g.drawString("" + c.getHp(), tx, ty);
		g.drawString("" + c.getId(), tx + 48, ty);

	}
}
