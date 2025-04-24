package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.CombatFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Hero warrior = new Hero("Warrior", 100, 20, 8);
        warrior.setState(new NormalState());

        Hero mage = new Hero("Mage", 70, 25, 4);
        mage.setState(new PoisonedState(2));

        System.out.println("=== THE HAUNTED TOWER: ASCENDING THE FLOORS ===");
        System.out.println("Warrior  — HP: 100 | ATK: 20 | DEF: 8 | State: " + warrior.getState().getName());
        System.out.println("Mage     — HP:  70 | ATK: 25 | DEF: 4 | State: " + mage.getState().getName());
        System.out.println("================================================");

        List<Hero> party = Arrays.asList(warrior, mage);

        List<TowerFloor> floors = Arrays.asList(
            new CombatFloor("Skeleton Crypt"),
            new TrapFloor(),
            new RestFloor(),
            new BossFloor()
        );

        TowerRunner runner = new TowerRunner(party, floors);
        TowerRunResult result = runner.run();

        System.out.println("\n=== TOWER RUN COMPLETE ===");
        System.out.println("Floors cleared  : " + result.getFloorsCleared() + " / " + floors.size());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Tower status    : " + (result.isReachedTop() ? "CONQUERED!" : "Not fully cleared"));
        for (Hero h : party) {
            System.out.println("  " + h.getName() + " — HP: " + h.getHp() + "/" + h.getMaxHp()
                    + " | State: " + h.getState().getName() + (h.isAlive() ? "" : " [FALLEN]"));
        }
    }
}
