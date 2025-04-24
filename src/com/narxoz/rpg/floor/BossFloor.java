package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class BossFloor extends TowerFloor {

    private Monster boss;

    @Override
    protected String getFloorName() { return "The Dragon's Lair"; }

    @Override
    protected void announce() {
        System.out.println("\n!!! ================================================= !!!");
        System.out.println("!!!   BOSS FLOOR: THE ANCIENT DRAGON HAS AWAKENED!   !!!");
        System.out.println("!!! ================================================= !!!");
    }

    @Override
    protected void setup(List<Hero> party) {
        boss = new Monster("Ancient Dragon", 80, 15);
        System.out.println("  The Ancient Dragon emerges from the darkness! [HP: 80 | Attack: 15]");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamage = 0;
        int round = 1;
        while (boss.isAlive() && party.stream().anyMatch(Hero::isAlive)) {
            System.out.println("\n  [Boss Round " + round + "]");
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.getState().onTurnStart(hero);
                if (!hero.isAlive()) continue;
                if (hero.getState().canAct()) {
                    int dmg = hero.getState().modifyOutgoingDamage(hero.getAttackPower());
                    boss.takeDamage(dmg);
                    System.out.println("  " + hero.getName() + " [" + hero.getState().getName() + "] attacks Dragon for "
                            + dmg + " dmg. Dragon HP: " + boss.getHp());
                }
                hero.getState().onTurnEnd(hero);
                if (hero.isAlive() && hero.getHp() <= hero.getMaxHp() * 0.3
                        && !(hero.getState() instanceof BerserkState)) {
                    System.out.println("  >> " + hero.getName() + "'s HP is critical! Enters Berserk state!");
                    hero.setState(new BerserkState());
                }
            }
            if (boss.isAlive()) {
                Hero target = party.stream().filter(Hero::isAlive).findFirst().orElse(null);
                if (target != null) {
                    int rawDmg = boss.getAttackPower();
                    int finalDmg = Math.max(1, target.getState().modifyIncomingDamage(rawDmg) - target.getDefense() / 2);
                    target.takeDamage(finalDmg);
                    totalDamage += finalDmg;
                    System.out.println("  Dragon attacks " + target.getName() + " for " + finalDmg
                            + " dmg. " + target.getName() + " HP: " + target.getHp());
                    if (round % 3 == 0 && !(target.getState() instanceof StunnedState)) {
                        System.out.println("  Dragon roars! " + target.getName() + " is stunned!");
                        target.setState(new StunnedState());
                    }
                }
            }
            round++;
        }
        boolean cleared = !boss.isAlive();
        return new FloorResult(cleared, totalDamage,
                cleared ? "The Ancient Dragon has been slain!" : "The party was overwhelmed by the Dragon.");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("  [Loot] The Dragon's hoard! Each surviving hero heals 30 HP.");
        for (Hero h : party) {
            if (h.isAlive()) h.heal(30);
        }
    }
}
