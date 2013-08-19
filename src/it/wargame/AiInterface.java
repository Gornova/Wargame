package it.wargame;

import com.google.common.eventbus.Subscribe;

public interface AiInterface {

	@Subscribe
	public void handleNextTurn(NextTurnEvent e);

}
