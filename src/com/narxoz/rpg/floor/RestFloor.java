package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import java.util.List;

public class RestFloor extends TowerFloor {

    @Override
    protected String getFloorName() { return "Sanctuary of Rest"; }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  A peaceful room glows with warm firelight. The party pauses to breathe.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        for (Hero hero : party) {
            if (!hero.isAlive()) continue;
            hero.heal(20);
            hero.setState(new NormalState());
            System.out.println("  " + hero.getName() + " rests and recovers 20 HP. HP: " + hero.getHp() + ". State: Normal");
        }
        return new FloorResult(true, 0, "Party rested and recovered.");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        System.out.println("  [Hook] Rest floor grants no loot — shouldAwardLoot() returns false.");
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
    }
}
