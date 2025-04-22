package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private int turnsRemaining;

    public PoisonedState(int turns) {
        this.turnsRemaining = turns;
    }

    @Override
    public String getName() { return "Poisoned"; }

    @Override
    public int modifyOutgoingDamage(int basePower) { return (int)(basePower * 0.8); }

    @Override
    public int modifyIncomingDamage(int rawDamage) { return (int)(rawDamage * 1.2); }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("  >> " + hero.getName() + " takes 5 poison damage! HP: " + (hero.getHp() - 5));
        hero.takeDamage(5);
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            System.out.println("  >> " + hero.getName() + "'s poison wears off. State: Normal");
            hero.setState(new NormalState());
        } else {
            System.out.println("  >> " + hero.getName() + " is still Poisoned (" + turnsRemaining + " turns left).");
        }
    }

    @Override
    public boolean canAct() { return true; }
}
