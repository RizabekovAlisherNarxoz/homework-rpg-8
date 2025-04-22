package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class BerserkState implements HeroState {

    @Override
    public String getName() { return "Berserk"; }

    @Override
    public int modifyOutgoingDamage(int basePower) { return (int)(basePower * 1.5); }

    @Override
    public int modifyIncomingDamage(int rawDamage) { return (int)(rawDamage * 1.3); }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("  >> " + hero.getName() + " is Berserk! Damage boosted 1.5x, but takes 1.3x incoming!");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        if (hero.getHp() > hero.getMaxHp() * 0.5) {
            System.out.println("  >> " + hero.getName() + " calms down. Berserk fades. State: Normal");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() { return true; }
}
