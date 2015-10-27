package it.wargame.gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import it.wargame.Wargame;

public class MenuWorld extends BasicGameState {

	private int id;

	public MenuWorld(int menuWorld) {
		this.id = menuWorld;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {

	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("WARGAME", 150, 50);
		g.drawString("Wargame is a simple, opensource,", 40, 200);
		g.drawString("little wargame. written in Java", 40, 220);

		g.drawString("Press S for single player", 80, 350);
		g.drawString("Press A fo AI battle", 80, 370);
	}

	@Override
	public void update(GameContainer container, StateBasedGame state, int arg2) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_S)) {
			Wargame.single = true;
			state.enterState(Wargame.GAME_WORLD);
		}
		if (container.getInput().isKeyPressed(Input.KEY_A)) {
			Wargame.single = false;
			state.enterState(Wargame.GAME_WORLD);
		}
	}

	@Override
	public int getID() {
		return id;
	}

}
