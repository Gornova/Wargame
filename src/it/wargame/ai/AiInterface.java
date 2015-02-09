package it.wargame.ai;

import it.wargame.events.NextTurnEvent;

import com.google.common.eventbus.Subscribe;

public interface AiInterface {

	@Subscribe
	public void handleNextTurn(NextTurnEvent e);

}
