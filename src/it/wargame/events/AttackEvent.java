package it.wargame.events;

import it.wargame.creatures.Creature;

public class AttackEvent {

	private Creature attacker;
	private Creature target;

	public AttackEvent(Creature attacker, Creature target) {
		this.attacker = attacker;
		this.target = target;
	}

	public Creature getAttacker() {
		return attacker;
	}

	public Creature getTarget() {
		return target;
	}

}
