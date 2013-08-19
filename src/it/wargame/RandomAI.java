package it.wargame;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.util.Log;

import com.google.common.eventbus.Subscribe;

public class RandomAI implements AiInterface {

	private ArrayList<Creature> creatures;

	private GameWorld world;

	public RandomAI(ArrayList<Creature> creatures, GameWorld world) {
		this.creatures = creatures;
		this.world = world;
		Wargame.eventBus.register(this);
	}

	@Subscribe
	public void handleNextTurn(NextTurnEvent e) {
		Random rnd = new Random();
		for (Creature c : creatures) {
			if (c.isAI() && !c.isMoved()) {
				int mx = (rnd.nextBoolean() ? 1 : -1);
				if (!world.isValid(c.getX() + mx, c.getY())) {
					mx = -1 * mx;
				}
				int my = (rnd.nextBoolean() ? 1 : -1);
				if (!world.isValid(mx, c.getY() + my)) {
					my = -1 * my;
				}

				if (world.isValid(c.getX() + mx, c.getY() + my)
						&& world.isCreature(c.getX() + mx, c.getY() + my) == null) {
					Wargame.eventBus.post(new MoveEvent(c, c.getX() + mx, c.getY() + my, Creature.GROUP_AI));
					Log.info("moved to " + c.getX() + "," + c.getY());
				}
			}
		}
	}

}
