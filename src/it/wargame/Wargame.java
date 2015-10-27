package it.wargame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;

import it.wargame.gamestates.GameWorld;
import it.wargame.gamestates.MenuWorld;

public class Wargame extends StateBasedGame {

	public static final int MENU_WORLD = 0;
	public static final int GAME_WORLD = 1;
	public static EventBus eventBus = new EventBus();

	public static boolean single = true;

	public Wargame(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MenuWorld(MENU_WORLD));
		addState(new GameWorld(GAME_WORLD));
	}

	public static void main(String[] args) throws SlickException {
		Wargame game = new Wargame("Wargame");
		AppGameContainer container = new AppGameContainer(game);
		container.setDisplayMode(384, 448, false);
		container.setShowFPS(false);
		container.setAlwaysRender(true);
		container.setTargetFrameRate(40);
		container.start();
	}

}
