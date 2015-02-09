package it.wargame;

import it.wargame.gamestates.GameWorld;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;

public class Wargame extends StateBasedGame {

	public static EventBus eventBus = new EventBus();

	public Wargame(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new GameWorld());
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
