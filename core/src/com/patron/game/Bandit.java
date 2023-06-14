package com.patron.game;

import com.badlogic.gdx.math.MathUtils;

public class Bandit extends Enemy {
    public Bandit() {
        super("Бандит", random.nextInt(6) + 30);

        goldReward = MathUtils.random(10,18);
        Attack[] attacks = {
                new Attack(random.nextInt(4) + 6),
                new Attack(random.nextInt(4) + 8),
                new Attack(random.nextInt(4) + 10)
        };
        Defend[] blocks = {
                new Defend(random.nextInt(3) + 8),
                new Defend(random.nextInt(4) + 10)
        };
        shuffleMoves(attacks, blocks);
    }
}

class RadioactiveRat extends Enemy {
    public RadioactiveRat() {
        super("Радіоактивний щур", random.nextInt(7) + 27);
        goldReward = MathUtils.random(13,20);

        Attack[] attacks = {
                new Attack(random.nextInt(1) + 1, new Effect[]{new RadiationEffect(3)}),
                new Attack(random.nextInt(2) + 1, new Effect[]{new RadiationEffect(2)}),
                new Attack(random.nextInt(1) + 2, new Effect[]{new RadiationEffect(3)})
        };
        Defend[] blocks = {
                new Defend(random.nextInt(2) + 2, new Effect[]{new CureEffect(this, 5)}),
                new Defend(random.nextInt(2) + 3, new Effect[]{new CureEffect(this, 5)})
        };
        shuffleMoves(attacks, blocks);

    }

}

class FireBagSoldier extends Enemy {
    public FireBagSoldier() {
        super("Солдатик-солдат", random.nextInt(5) + 14);
        goldReward = MathUtils.random(8,14);

        Attack[] attacks = {
                new Attack(random.nextInt(4) + 5),
                new Attack(random.nextInt(5) + 6)
        };
        Defend[] blocks = {
                new Defend(random.nextInt(4) + 5)
        };
        shuffleMoves(attacks, blocks);

    }
}

class FireBagMedic extends Enemy {
    public FireBagMedic() {
        super("Солдатик-медик", random.nextInt(4) + 12);
        goldReward = MathUtils.random(8,10);

        Attack[] attacks = {
                new Attack(random.nextInt(3) + 3, new Effect[]{new WeaknessEffect(2)})
        };
        Impact[] impacts = {
                new Impact(new Effect[]{new CureEffect(this, 4)}, true),
                new Impact(new Effect[]{new FragilityEffect(2)}),
                new Impact(new Effect[]{new WeaknessEffect(2)})
        };

        shuffleMoves(attacks, impacts);
    }
}

class FireBagTank extends Enemy {
    public FireBagTank() {
        super("Солдатик-броневик", random.nextInt(3) + 10);
        goldReward = MathUtils.random(7,10);

        enemyMoves.add(new Defend(random.nextInt(2) + 4, true));
        enemyMoves.add(new Defend(random.nextInt(4) + 6));

        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void defend(Action block) {
        super.defend(block);
        if (Fight.enemies.size() == 1){
            enemyMoves.clear();
            enemyMoves.add(new Attack(random.nextInt(3)+5));
        }
    }
}
class FireBagSniper extends Enemy {
    public FireBagSniper() {
        super("Солдатик-снайпер", random.nextInt(4) + 15);
        goldReward = MathUtils.random(9,15);

        enemyMoves.add(new Impact());
        enemyMoves.add(new Attack(20));
        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }
}

