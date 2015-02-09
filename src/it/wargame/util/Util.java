package it.wargame.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Util {

	private Util() {
	}

	public static Image get(String name) {
		try {
			return new Image("res/" + name);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return null;
	}

}
