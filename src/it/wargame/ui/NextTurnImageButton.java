package it.wargame.ui;

import it.wargame.Wargame;
import it.wargame.events.NextTurnEvent;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.util.Log;

public class NextTurnImageButton extends Button {

	private Image image;

	public NextTurnImageButton(int x, int y, Image image) {
		super(x, y);
		this.image = image;
		this.w = image.getWidth();
		this.h = image.getHeight();
	}

	@Override
	public void handleClick() {
		Wargame.eventBus.post(new NextTurnEvent());
		Log.info("pressed");
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(image, x, y);
	}

}
