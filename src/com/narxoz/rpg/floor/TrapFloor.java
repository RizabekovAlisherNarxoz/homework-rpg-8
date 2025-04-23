package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import java.util.List;

public class TrapFloor extends TowerFloor {

    @Override
    protected String getFloorName() { return "Poison Trap Chamber"; }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  Poisoned darts line the walls. Watch every step!");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamage = 0;
        for (Hero hero : party) {
            if (!hero.isAlive()) continue;
            int trapDmg = 8;
            hero.takeDamage(trapDmg);
            totalDamage += trapDmg;
            System.out.println("  " + hero.getName() + " is struck by poisoned darts for " + trapDmg + " dmg! HP: " + hero.getHp());
            hero.setState(new PoisonedState(3));
            System.out.println("  " + hero.getName() + " is now Poisoned (3 turns)!");
        }
        boolean cleared = party.stream().anyMatch(Hero::isAlive);
        return new FloorResult(cleared, totalDamage, "The party pushed through the trap room.");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("  [Loot] The party finds a small pouch of gold.");
    }
}
