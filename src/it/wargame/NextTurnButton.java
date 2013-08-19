package it.wargame;

import org.newdawn.slick.util.Log;

public class NextTurnButton extends Button {

	public NextTurnButton(int x, int y) {
		super(x, y);
	}

	@Override
	public void handleClick() {
		Wargame.eventBus.post(new NextTurnEvent());
		Log.info("pressed");
	}

}
