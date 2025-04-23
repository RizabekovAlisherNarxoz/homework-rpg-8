package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import java.util.ArrayList;
import java.util.List;

public class CombatFloor extends TowerFloor {

    private final String floorName;
    private List<Monster> monsters;

    public CombatFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void setup(List<Hero> party) {
        monsters = new ArrayList<>();
        monsters.add(new Monster("Skeleton", 30, 8));
        monsters.add(new Monster("Zombie", 25, 6));
        System.out.println("  Two monsters appear: Skeleton (30 HP) and Zombie (25 HP)!");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamage = 0;
        int round = 1;
        while (monsters.stream().anyMatch(Monster::isAlive) && party.stream().anyMatch(Hero::isAlive)) {
            System.out.println("\n  [Round " + round + "]");
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.getState().onTurnStart(hero);
                if (!hero.isAlive()) continue;
                if (hero.getState().canAct()) {
                    Monster target = monsters.stream().filter(Monster::isAlive).findFirst().orElse(null);
                    if (target != null) {
                        int dmg = hero.getState().modifyOutgoingDamage(hero.getAttackPower());
                        target.takeDamage(dmg);
                        System.out.println("  " + hero.getName() + " [" + hero.getState().getName() + "] attacks "
                                + target.getName() + " for " + dmg + " dmg. " + target.getName() + " HP: " + target.getHp());
                        if (!target.isAlive()) System.out.println("  " + target.getName() + " is defeated!");
                    }
                }
                hero.getState().onTurnEnd(hero);
                checkBerserk(hero);
            }
            for (Monster m : monsters) {
                if (!m.isAlive()) continue;
                Hero target = party.stream().filter(Hero::isAlive).findFirst().orElse(null);
                if (target == null) break;
                int rawDmg = m.getAttackPower();
                int finalDmg = Math.max(1, target.getState().modifyIncomingDamage(rawDmg) - target.getDefense() / 2);
                target.takeDamage(finalDmg);
                totalDamage += finalDmg;
                System.out.println("  " + m.getName() + " attacks " + target.getName() + " for " + finalDmg
                        + " dmg. " + target.getName() + " HP: " + target.getHp());
                checkBerserk(target);
            }
            round++;
        }
        boolean cleared = monsters.stream().noneMatch(Monster::isAlive);
        return new FloorResult(cleared, totalDamage, cleared ? "Party defeated all monsters!" : "Party was defeated.");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("  [Loot] Each surviving hero heals 10 HP.");
        for (Hero h : party) {
            if (h.isAlive()) h.heal(10);
        }
    }

    private void checkBerserk(Hero hero) {
        if (hero.isAlive() && hero.getHp() <= hero.getMaxHp() * 0.3
                && !(hero.getState() instanceof BerserkState)) {
            System.out.println("  >> " + hero.getName() + "'s HP is critical! Enters Berserk state!");
            hero.setState(new BerserkState());
        }
    }
}
