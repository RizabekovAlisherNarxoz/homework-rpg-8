package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import java.util.List;

public class TowerRunner {

    private final List<Hero> party;
    private final List<TowerFloor> floors;

    public TowerRunner(List<Hero> party, List<TowerFloor> floors) {
        this.party = party;
        this.floors = floors;
    }

    public TowerRunResult run() {
        int floorsCleared = 0;
        for (TowerFloor floor : floors) {
            if (party.stream().noneMatch(Hero::isAlive)) {
                System.out.println("\nAll heroes have fallen. The tower climb ends.");
                break;
            }
            FloorResult result = floor.explore(party);
            System.out.println("[Result] " + result.getSummary());
            if (result.isCleared()) {
                floorsCleared++;
            } else {
                System.out.println("Floor not cleared. The party retreats.");
                break;
            }
        }
        int surviving = (int) party.stream().filter(Hero::isAlive).count();
        boolean reachedTop = floorsCleared == floors.size();
        return new TowerRunResult(floorsCleared, surviving, reachedTop);
    }
}
