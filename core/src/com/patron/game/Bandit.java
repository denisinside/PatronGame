package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Bandit extends Enemy {
    public Bandit() {
        super("Бандит", random.nextInt(6) + 30,
                "bandit",200,300);

        goldReward = MathUtils.random(10, 18);
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
        super("Радіоактивний щур", random.nextInt(7) + 27,
                "bandit",200,300);
        goldReward = MathUtils.random(13, 20);

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
        super("Солдатик-солдат", random.nextInt(5) + 14,
                "Soldier",200,300);
        goldReward = MathUtils.random(8, 14);

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
        super("Солдатик-медик", random.nextInt(4) + 12,
                "bandit",200,300);
        goldReward = MathUtils.random(8, 10);

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
        super("Солдатик-броневик", random.nextInt(3) + 10,
                "FireBagTank",200,300);
        goldReward = MathUtils.random(7, 10);

        enemyMoves.add(new Defend(random.nextInt(2) + 4, true));
        enemyMoves.add(new Defend(random.nextInt(4) + 6));

        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void defend(Action block) {
        super.defend(block);
        if (Fight.enemies.size() == 1) {
            enemyMoves.clear();
            enemyMoves.add(new Attack(random.nextInt(3) + 5));
        }
    }
}

class FireBagSniper extends Enemy {
    public FireBagSniper() {
        super("Солдатик-снайпер", random.nextInt(4) + 15,
                "bandit",200,300);
        goldReward = MathUtils.random(9, 15);

        enemyMoves.add(new Impact());
        enemyMoves.add(new Attack(20));
        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }
}

class MossyFox extends Enemy {

    public MossyFox() {
        super("Моховий лис", random.nextInt(8) + 22,
                "MossyFox",290,230);
        goldReward = MathUtils.random(9, 12);

        if (MathUtils.random(1) == 1) {
            enemyMoves.add(new Attack(6));
            enemyMoves.add(new Impact(new Effect[]{new StrengthEffect(this, 3)}));
        } else {
            enemyMoves.add(new Impact(new Effect[]{new StrengthEffect(this, 3)}));
            enemyMoves.add(new Attack(6));
        }


        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void death() {
        super.death();
        player.addEffect(new VulnerabilityEffect(3));
    }

    @Override
    public void initialEffect() {

        Effect prikol = new Effect("Спори", "Після смерті гравець отримає 2 вразливості", false, true, 2);
        prikol.effectType = EffectType.BUFF;
        addEffect(prikol);
    }
}

class DeeDee extends Enemy {
    public DeeDee() {
        super("Ді-Ді", random.nextInt(8) + 54,
                "DeeDee",150,150);
        goldReward = MathUtils.random(16, 24);

        enemyMoves.add(
                new Attack(7, null)
        );
        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void attack(Action attack) {
        super.attack(attack);
        Fight.discard.add(CardFactory.getStatus("Рана"));
    }
}

class Marky extends Enemy {
    Effect disability;

    public Marky() {
        super("Маркі", MathUtils.random(4) + 43,
                "Marky",130,160);
        goldReward = MathUtils.random(10, 18);

        disability = new Effect("Павутина", "Всі твої атаки не наносять шкоди", true, false) {
            @Override
            public void setBase() {
                player.setAttackMultiplier(1);
            }

            @Override
            public void effectResult() {
                player.setAttackMultiplier(0);
            }
        };
        disability.moves = 2;

        if (MathUtils.random(1) == 1) {
            enemyMoves.add(new Attack(12));
            enemyMoves.add(new Attack(7, new Effect[]{new VulnerabilityEffect(2)}));
            enemyMoves.add(new Impact(new Effect[]{disability}));
        } else {
            enemyMoves.add(new Attack(7, new Effect[]{new VulnerabilityEffect(2)}));
            enemyMoves.add(new Attack(12));
            enemyMoves.add(new Impact(new Effect[]{disability}));
        }
        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

}

class Joey extends Enemy {
    public Joey() {
        super("Джої", MathUtils.random(4) + 40,
                "Joey",130,150);
        goldReward = MathUtils.random(10, 18);

        if (MathUtils.random(1) == 1) {
            enemyMoves.add(new Attack(MathUtils.random(2) + 10));
            enemyMoves.add(new Attack(6, new Effect[]{new WeaknessEffect(2)}));
        } else {
            enemyMoves.add(new Attack(6, new Effect[]{new WeaknessEffect(2)}));
            enemyMoves.add(new Attack(MathUtils.random(2) + 10));
        }


        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }
}

class Schweinokar extends Enemy {
    public Schweinokar() {
        super("Швайнокарась", MathUtils.random(4) + 65,
                "Schweinokar",350,180);
        goldReward = MathUtils.random(18, 26);
        if (MathUtils.random(1) == 1) {
            enemyMoves.add(new Attack(3, 2));
            enemyMoves.add(new Attack(7, new Effect[]{new CureEffect(this, 3)}));
            enemyMoves.add(new Attack(3, 2));
            enemyMoves.add(new Attack(8, new Effect[]{new FragilityEffect(3)}));
        } else {
            enemyMoves.add(new Attack(3, 2));
            enemyMoves.add(new Attack(8, new Effect[]{new FragilityEffect(3)}));
            enemyMoves.add(new Attack(3, 2));
            enemyMoves.add(new Attack(7, new Effect[]{new CureEffect(this, 3)}));
        }

        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void initialEffect() {
        addEffect(new PlatedArmorEffect(this, 15));
    }
}

class Shroomjak extends Enemy {
    Impact impact;

    public Shroomjak() {
        super("Shroomjak", MathUtils.random(7) + 120,
                "Shroomjak",250,250);
        goldReward = MathUtils.random(16, 23);

        Effect effect = new Effect("Екстаз", "Патрону надто добре, щоб згадати скільки коштує карта", true, true) {
            @Override
            public void effectResult() {
                for (Card card : GameProgress.playerDeck) {
                    card.setCostNow(MathUtils.random(3));
                    card.cardActor.energyActor.setEnergyAmount(card.getCostNow());
                }
            }

            @Override
            public void setBase() {
                for (Card card : GameProgress.playerDeck) {
                    card.setCostNow(card.getCost());
                    card.cardActor.energyActor.setEnergyAmount(card.getCostNow());
                }
            }
        };
        effect.moves = 1;
        impact = new Impact(new Effect[]{effect});
        enemyMoves.add(impact);
        if (MathUtils.random(1) == 1) {
            enemyMoves.add(new Attack(6, new Effect[]{new VulnerabilityEffect(2)}));
            enemyMoves.add(new Attack(13));
            enemyMoves.add(new Attack(6, new Effect[]{new FragilityEffect(2)}));
            enemyMoves.add(new Attack(13));
        } else {
            enemyMoves.add(new Attack(6, new Effect[]{new FragilityEffect(2)}));
            enemyMoves.add(new Attack(13));
            enemyMoves.add(new Attack(6, new Effect[]{new VulnerabilityEffect(2)}));
            enemyMoves.add(new Attack(13));
        }


        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void castDebuff(Action impact) {
        super.castDebuff(impact);
        enemyMoves.remove(impact);
    }
}

class MisterMeow extends Enemy {
    boolean secondChance = false;

    public MisterMeow() {
        super("Містер Мяу", MathUtils.random(4) + 24,
                "MisterMeow",350,250);
        goldReward = MathUtils.random(21, 27);
        Effect effect = new Effect("9 життів", "Щось з цим кошеням не так..", true, true) {
        };
        effect.setEnemy(this);
        effect.moves = 1;

        enemyMoves.add(new Impact(new Effect[]{effect}));
        enemyMoves.add(new Attack(2, 3));
        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void death() {
        if (!secondChance) {
            secondChance = true;
            setMaxHealth(MathUtils.random(10) + 119);
            setHealth(getMaxHealth());
            actor.healthBar.setMaxValue(getHealth());
            float x = actor.enemySprite.getX(), y = actor.enemySprite.getY();
            actor.enemySprite = new Image(new Sprite(new Texture(Gdx.files.internal("enemies\\MisterMeow2.png"))));
            actor.enemySprite.setPosition(x,y);
            setBasicAnimation();

            enemyMoves.clear();
            for (Effect effect : effects)
                if (effect.name.equals("9 життів")) {
                    effect.effectIcon.addAction(Actions.removeActor());
                    actor.effectPanel.removeEffect(effect);
                    effects.remove(effect);
                    break;
                }
            enemyMoves.add(new Impact(new Effect[]{new StrengthEffect(-1), new WeaknessEffect(2)}));
            enemyMoves.add(new Impact(new Effect[]{new AgilityEffect(-1), new VulnerabilityEffect(2)}));
            enemyMoves.add(new Attack(3, 4));
            actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
        } else super.death();
    }
}

class Diggy extends Enemy {
    public Diggy() {
        super("Деггет", MathUtils.random(6) + 60,
                "Diggy",300,300);
        goldReward = MathUtils.random(12, 18);
        if (MathUtils.random(1) == 1) {
            enemyMoves.add(new Attack(8));
            enemyMoves.add(new Defend(10, true));
            enemyMoves.add(new Attack(4, 3));
            enemyMoves.add(new Defend(10, true));
        } else {
            enemyMoves.add(new Defend(10, true));
            enemyMoves.add(new Attack(4, 3));
            enemyMoves.add(new Defend(10, true));
            enemyMoves.add(new Attack(8));
        }

        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }
}

class Norbert extends Enemy {
    public Norbert() {
        super("Норберт", MathUtils.random(9) + 45,
                "Norbert",300,400);
        goldReward = MathUtils.random(10, 16);
        if (MathUtils.random(1) == 1) {
            enemyMoves.add(new Impact(new Effect[]{new StrengthEffect(this, 2)}, true));
            enemyMoves.add(new Attack(5, new Effect[]{new FragilityEffect(3)}));
            enemyMoves.add(new Impact());
        } else {
            enemyMoves.add(new Attack(5, new Effect[]{new FragilityEffect(3)}));
            enemyMoves.add(new Impact(new Effect[]{new StrengthEffect(this, 2)}, true));
            enemyMoves.add(new Impact());
        }

        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void castImpact(Action impact) {
        super.castImpact(impact);
        for (Enemy enemy : Fight.enemies) enemy.heal(10);
    }
}

class ExplosiveCat extends Enemy {

    public ExplosiveCat() {
        super("Кіт-терорист", random.nextInt(8) + 22,
                "ExplosiveCat",200,180);
        goldReward = MathUtils.random(9, 12);

        enemyMoves.add(new Attack(6));
        enemyMoves.add(new Attack(6));
        enemyMoves.add(new Impact());

        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void castImpact(Action impact) {
        player.getDamage(30);
        death();
    }

    @Override
    public void initialEffect() {

        Effect prikol = new Effect("Вибух", "Коли ефект пропаде, Патрон отримає 30 шкоди від вибуху", false, false, 3);
        prikol.effectType = EffectType.BUFF;
        addEffect(prikol);
    }
}

class Hedgehog extends Enemy {
    public Hedgehog() {
        super("Норберт", MathUtils.random(7) + 35,
                "Hedgehog",250,125);
        goldReward = MathUtils.random(10, 16);
        Effect spikes = new Effect("Колючки", "Наносить шкоду атакуючому", true, true, 2);
        spikes.effectType = EffectType.BUFF;
        spikes.enemy = this;
        if (MathUtils.random(1) == 1) {
            enemyMoves.add(new Impact(new Effect[]{spikes}));
            enemyMoves.add(new Attack(5));
        } else {
            enemyMoves.add(new Attack(5));
            enemyMoves.add(new Impact(new Effect[]{spikes}));
        }
        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void getDamage(int damage) {
        super.getDamage(damage);
        for (Effect effect : effects)
            if (effect.name.equals("Колючки")) {
                player.getDamage(effect.moves);
                return;
            }
    }
}

class Cutter extends Enemy {
    int ultaNumber;
    Attack ultaAttack;

    public Cutter() {
        super("Різак", MathUtils.random(10) + 145,
                "Cutter",240,240);
        goldReward = MathUtils.random(30, 40);
        ultaNumber = 2;
        ultaAttack = new Attack(4,ultaNumber);

        enemyMoves.add(ultaAttack);
        enemyMoves.add(new Attack(12));
        enemyMoves.add(ultaAttack);


        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    @Override
    public void attack(Action attack) {
        super.attack(attack);
        if (((Attack)attack).damage == 15) ultaNumber++;
        ultaAttack.count = ultaNumber;
    }

    @Override
    public void getDamage(int damage) {
        super.getDamage(damage);
        for (Effect effect : effects)
            if (effect.name.equals("Колючки")) {
                player.getDamage(effect.moves);
                return;
            }
    }
}
