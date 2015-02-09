package it.wargame.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Button {

	private String text;
	private int x;
	private int y;
	private int w;
	private int h;

	public Button(int x, int y) {
		this.x = x;
		this.y = y;
		this.h = 24;
		this.w = 32;
	}

	public Button setText(String text) {
		this.text = text;
		this.w = 10 * text.length() + 1;
		return this;
	}

	public void render(Graphics g) {
		g.setLineWidth(1);
		g.drawRect(x, y, w, h);
		g.drawString(text, x, y);
		g.resetLineWidth();
	}
	public void update(GameContainer gc, StateBasedGame state, int arg2) throws SlickException {
		Input input = gc.getInput();
		int mx = input.getMouseX();
		int my = input.getMouseY();
		Rectangle r = new Rectangle(x, y, w, h);
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && r.contains(mx, my)) {
			handleClick();
		}
	}

	public abstract void handleClick();
}
