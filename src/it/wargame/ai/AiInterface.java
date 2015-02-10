package it.wargame.ai;

import it.wargame.creatures.Creature;
import it.wargame.events.NextTurnEvent;

import com.google.common.eventbus.Subscribe;

public interface AiInterface {

	public void setCreature(Creature creature);
	
	@Subscribe
	public void handleNextTurn(NextTurnEvent e);

}
