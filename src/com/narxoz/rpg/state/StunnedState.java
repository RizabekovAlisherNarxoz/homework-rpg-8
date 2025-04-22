package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState {

    @Override
    public String getName() { return "Stunned"; }

    @Override
    public int modifyOutgoingDamage(int basePower) { return basePower; }

    @Override
    public int modifyIncomingDamage(int rawDamage) { return rawDamage; }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("  >> " + hero.getName() + " is Stunned and cannot act this turn!");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        System.out.println("  >> " + hero.getName() + " recovers from stun. State: Normal");
        hero.setState(new NormalState());
    }

    @Override
    public boolean canAct() { return false; }
}
