package com.patron.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;

public class Player {
    public PlayerActor actor;
    protected ArrayList<Effect> effects = new ArrayList<>();
    protected Array<Artefact> artefacts = new Array<>();
    int money = 0;
    Color color;
    private int maxHealth = 75;
    private int maxEnergy = 3;
    private int health;
    private int energy;
    private int cardPerRound = 5;
    private int armor = 0;
    private double damageMultiplier = 1;
    private double defendMultiplier = 1;
    private double attackMultiplier = 1;
    private int strengthBuff = 0;
    private int defendBuff = 0;
    private boolean isTakingDamage = false;
    private boolean isHealing = false;

    public Player() {
        health = maxHealth;
        energy = maxEnergy;
        actor = new PlayerActor();
        actor.healthBar.setCurrentValue(health);
        color = actor.playerSprite.getColor();
    }

    public void setBasicAnimation() {
        int random = MathUtils.random(1) == 1 ? -MathUtils.random(25) - 5 : MathUtils.random(25) + 5;
        float duration = (MathUtils.random(15) + 10) / 10f;
        actor.playerSprite.addAction(Actions.forever(
                Actions.sequence(
                        Actions.moveBy(-random, 0, duration),
                        Actions.moveBy(random * 2, 0, duration),
                        Actions.moveBy(-random, 0, duration)
                )
        ));
    }

    private void featureArtefact(String name) {
        if (name.equals("Рюкзак")) cardPerRound += 1;
        if (name.equals("Фляга з кров'ю")) {
            setMaxHealth((int) (getMaxHealth() * 0.7));
            actor.healthBar.setMaxValue(getMaxHealth());
            if (health > maxHealth) health = maxHealth;
            for (Card card : GameProgress.playerDeck)
                if (card.name.equals("Удар лапою"))
                    GameProgress.playerDeck.set(GameProgress.playerDeck.indexOf(card), CardFactory.createCard("Укус"));
        }
        if (name.equals("Червоне яблуко")) {
            maxHealth += 10;
            heal(10);
        }
        if (name.equals("Зелене яблуко")) {
            maxHealth += 5;
            heal(maxHealth - health);
        }
        if (name.equals("Золотий злиток")) addGold(200);
        if (name.equals("Срібний злиток")) addGold(100);
        if (name.equals("Бронзовий злиток")) addGold(50);

        if (name.equals("Око Буданова")) MoveDisplay.setShowMoreInfo(true);

        if (name.equals("Ленд-ліз"))
            for (int i = 0; i < 3; i++)
                GameProgress.playerDeck.add(GameProgress.allCards.get(MathUtils.random(GameProgress.allCards.size() - 1)));

    }

    public void addArtefact(Artefact artefact) {
        artefacts.add(artefact);
        GameProgress.topPanel.addActor(artefact);
        featureArtefact(artefact.name);
    }

    public boolean ifHasArtefact(String name) {
        for (Artefact artefact : artefacts)
            if (artefact.name.equals(name)) return true;
        return false;
    }

    public void addEffect(Effect effect) {
        if (effect instanceof RadiationEffect)
            if (ifHasArtefact("Слиз"))
                effect.moves = -1;

        for (Effect e : effects) {
            if (e.getClass().equals(effect.getClass())) {
                e.moves += effect.moves;
                return;
            }
        }
        try {
            Effect effectCopy = (Effect) effect.clone();
            effectCopy.setEnemy(null);
            effects.add(effectCopy);
            actor.effectPanel.addEffect(effectCopy);
            actor.addEffect(effect);
            if (effectCopy.isInstant) effectCopy.effectResult();
        } catch (CloneNotSupportedException g) {
            g.printStackTrace();
        }
    }

    public void effectCheck() {
        Iterator<Effect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            if (!effect.isPermanent && effect.isInstant) effect.moves--;
            if (effect.moves > 0) effect.effectResult();
            else if (effect.moves != 0 && effect.isPermanent) effect.effectResult();
            if (!effect.isPermanent && !effect.isInstant) effect.moves--;
            if ((!effect.isPermanent && effect.moves <= 0) || (effect.isPermanent && effect.moves == 0)) {
                effect.setBase();
                iterator.remove();
                actor.effectPanel.removeEffect(effect);
            }
        }
        actor.healthBar.setCurrentValue(health);
    }

    public void heal(int healAmount) {
        if (healAmount + health <= maxHealth) health += healAmount;
        else health = maxHealth;
        actor.healthBar.setCurrentValue(health);
        actor.addValue(healAmount, Color.GREEN, EnemyActor.valueType.HEAL);

        if (!isHealing) {
            isHealing = true;
            actor.playerSprite.addAction(Actions.sequence(
                    Actions.color(Color.GREEN, 0.3f),
                    Actions.color(color, 0.3f),
                    Actions.run(() -> {
                        isHealing = false;
                    })
            ));
        }

    }

    public void getDamage(int damage) {
        damage = (int) Math.round(damage * damageMultiplier);
        if (armor != 0 && armor >= damage) armor -= damage;
        else {
            if (armor != 0) {
                damage -= armor;
                armor = 0;
            }
            if (health - damage >= 0) {
                health -= damage;
            } else health = 0;
            actor.addValue(damage, Color.PINK, EnemyActor.valueType.DAMAGE);
            actor.healthBar.showArmor(false);

            if (!isTakingDamage) {
                isTakingDamage = true;
                actor.playerSprite.addAction(Actions.sequence(
                        Actions.color(Color.RED, 0.3f),
                        Actions.color(color, 0.3f),
                        Actions.run(() -> {
                            isTakingDamage = false;
                        })
                ));
            }
        }
        actor.healthBar.setArmor(armor);
        actor.healthBar.setCurrentValue(health);
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
        actor.healthBar.setArmor(this.armor);
        actor.healthBar.showArmor(armor != 0);
    }

    public void addArmor(int armor) {
        this.armor += armor;
        actor.healthBar.setArmor(this.armor);
        actor.healthBar.showArmor(true);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public int getCardPerRound() {
        return cardPerRound;
    }

    public void setCardPerRound(int cardPerRound) {
        this.cardPerRound = cardPerRound;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        actor.healthBar.setCurrentValue(health);
    }

    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    public void setAttackMultiplier(double attackMultiplier) {
        this.attackMultiplier = attackMultiplier;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public double getDefendMultiplier() {
        return defendMultiplier;
    }

    public void setDefendMultiplier(double defendMultiplier) {
        this.defendMultiplier = defendMultiplier;
    }

    public int getDefendBuff() {
        return defendBuff;
    }

    public void setDefendBuff(int defendBuff) {
        this.defendBuff = defendBuff;
    }

    public int getStrengthBuff() {
        return strengthBuff;
    }

    public void setStrengthBuff(int strengthBuff) {
        this.strengthBuff = strengthBuff;
    }

    public void addGold(int i) {
        money += i;
    }
}
