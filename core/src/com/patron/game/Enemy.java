package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

enum Move {
    ATTACK,
    BUFFED_ATTACK,
    EFFECTED_ATTACK,
    DEFEND,
    BUFFED_DEFENSE,
    BUFF,
    DEBUFF,
    IMPACT
}

public class Enemy {
    protected static Random random = new Random();
    static Player player;
    public int goldReward;
    public EnemyActor actor;
    protected String name;
    protected int maxHealth;
    protected int health;
    protected int armor = 0;
    protected ArrayList<Action> enemyMoves = new ArrayList<>();
    protected int moveIndex = 0;
    protected ArrayList<Effect> effects = new ArrayList<>();
    protected double damageMultiplier = 1;
    protected double defendMultiplier = 1;
    protected double attackMultiplier = 1;
    protected int strengthBuff = 0;
    protected int defendBuff = 0;
    protected boolean isDeath;
    protected Sound blockSound,effectSound,healSound,deathSound;
    Color color;

    public Enemy(String name, int hp,String path, int width,int height) {
        this.name = name;
        maxHealth = health = hp;
        actor = new EnemyActor(path,width, height, this);
        color = actor.enemySprite.getColor();

        blockSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Block.mp3"));
        effectSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Effect.mp3"));
        healSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Heal.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\EnemyDeath.mp3"));
    }

    public void setBasicAnimation(){
        int random = MathUtils.random(1) == 1 ? -MathUtils.random(25)-5 : MathUtils.random(25)+5;
        float duration = (MathUtils.random(15)+10)/10f;
        actor.enemySprite.addAction(Actions.forever(
                Actions.sequence(
                        Actions.moveBy(-random,0,duration),
                        Actions.moveBy(random*2,0,duration),
                        Actions.moveBy(-random,0,duration)
                )
        ));
    }

    public void shuffleMoves(Action[] attacks, Action[] blocks) {
        int attackIndex = 0, blockIndex = 0;
        boolean next = random.nextBoolean();
        if (next) {
            enemyMoves.add(attacks[attackIndex]);
            attackIndex++;
            next = false;
        } else {
            enemyMoves.add(blocks[blockIndex]);
            blockIndex++;
            next = true;
        }
        while (blockIndex != blocks.length || attackIndex != attacks.length) {
            if (next) {
                if (attacks.length != attackIndex) {
                    enemyMoves.add(attacks[attackIndex]);
                    attackIndex++;
                }
                next = false;
            } else {
                if (blocks.length != attackIndex) {
                    enemyMoves.add(blocks[blockIndex]);
                    blockIndex++;
                }
                next = true;
            }
        }

        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    public void addEffect(Effect effect) {
        effectSound.setVolume(effectSound.play(),GameSound.soundVolume);
        for (Effect e : effects) {
            if (e.getClass().equals(effect.getClass())) {
                e.moves += effect.moves;
                return;
            }
        }
        try {
            Effect effectCopy = (Effect) effect.clone();
            effectCopy.setEnemy(this);
            effects.add(effectCopy);
            actor.effectPanel.addEffect(effectCopy);
            actor.addEffect(effect);
            effectCopy.initialEffect();
        } catch (CloneNotSupportedException g) {
            g.printStackTrace();
        }
    }

    private boolean isTakingDamage = false;
    private boolean isHealing = false;
    public void heal(int healAmount) {
        healSound.setVolume(healSound.play(),GameSound.soundVolume);
        if (healAmount + health <= maxHealth) {
            health += healAmount;
        } else health = maxHealth;
        actor.healthBar.setCurrentValue(health);
        actor.addValue(healAmount, Color.GREEN, EnemyActor.valueType.HEAL);
        if (!isHealing ){
            isHealing = true;
            actor.enemySprite.addAction(Actions.sequence(
                    Actions.color(Color.GREEN,0.3f),
                    Actions.color(color,0.3f),
                    Actions.run(()->{
                        isHealing = false;
                    })
            ));
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
            if (!effect.isPermanent && effect.moves <= 0) {
                effect.setBase();
                iterator.remove();
                actor.effectPanel.removeEffect(effect);
            }
            if (health <= 0) return;
            effect.endOfTurn();
        }
        actor.healthBar.setCurrentValue(health);
    }

    public void getDamage(int damage) {
        damage = (int) Math.round(damage * damageMultiplier);
        if (armor != 0 && armor >= damage) armor -= damage;
        else {
            if (armor != 0) {
                damage -= armor;
                armor = 0;
            }
            health -= damage;
            for (Effect effect : effects) effect.getDamage();
            actor.addValue(damage, Color.PINK, EnemyActor.valueType.DAMAGE);
            actor.healthBar.showArmor(false);
            if (!isTakingDamage ){
                isTakingDamage = true;
                actor.enemySprite.addAction(Actions.sequence(
                        Actions.color(Color.RED,0.3f),
                        Actions.color(color,0.3f),
                        Actions.run(()->{
                            isTakingDamage = false;
                        })
                ));
            }
        }
        actor.healthBar.setArmor(armor);
    }

    public void makeMove() {
        switch (enemyMoves.get(moveIndex).getMove()) {
            case EFFECTED_ATTACK:
            case BUFFED_ATTACK:
            case ATTACK:
                attack(enemyMoves.get(moveIndex));
                break;
            case BUFFED_DEFENSE:
            case DEFEND:
                defend(enemyMoves.get(moveIndex));
                break;
            case IMPACT:
                castImpact(enemyMoves.get(moveIndex));
                break;
            case DEBUFF:
                castDebuff(enemyMoves.get(moveIndex));
                break;
            case BUFF:
                castBuff(enemyMoves.get(moveIndex));
        }
        Fight.checkKill();
        moveIndex = (moveIndex + 1) % enemyMoves.size();
        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }

    public void castImpact(Action impact) {

    }

    public void castBuff(Action impact) {
        if (!impact.toAll) for (Effect e : impact.effects) addEffect(e);
        else for (Enemy enemy : Fight.enemies) {
            for (Effect e : impact.effects) enemy.addEffect(e);
        }
    }

    public void castDebuff(Action impact) {
        for (Effect e : impact.effects) player.addEffect(e);
    }

    public void attack(Action attack) {
        Sound attackSound = GameSound.getAttackSound();
        attackSound.setVolume(attackSound.play(),GameSound.soundVolume);
        int damage = (int) Math.round((attack.getSummaryValue() + strengthBuff * attack.count) * attackMultiplier);
        player.getDamage(damage);
        actor.enemySprite.addAction(Actions.sequence(
                Actions.moveBy(-100, 0, 0.8f),
                Actions.moveBy(100, 0, 0.8f)
        ));
        if (attack.isWithEffect)
            if (attack.effects != null){
                if (attack.effects[0].enemy == null) for (Effect e : attack.effects) player.addEffect(e);
                else for (Effect e : attack.effects) addEffect(e);
            }
    }

    public void defend(Action block) {
        blockSound.setVolume(blockSound.play(),GameSound.soundVolume*1.5f);
        if (!block.toAll) {
            int armor = (int) Math.round((block.getSummaryValue() + defendBuff * block.count) * defendMultiplier);
            addArmor(armor);
            if (block.isWithEffect)
                for (Effect e : block.effects) addEffect(e);
        } else {
            block.toAll = false;
            for (Enemy enemy : Fight.enemies) enemy.defend(block);
        }
        actor.healthBar.setArmor(armor);
        actor.healthBar.showArmor(true);
    }

    public int getActualAttackDamage() {
        return (int) Math.round(enemyMoves.get(moveIndex).getSummaryValue() * attackMultiplier);
    }

    public int getActualBlock() {
        return (int) Math.round(enemyMoves.get(moveIndex).getSummaryValue() * defendMultiplier);
    }

    public boolean ifHas(Effect effect) {
        for (Effect e : effects) if (e.getClass() == effect.getClass()) return true;
        return false;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
        actor.healthBar.setArmor(this.armor);
        actor.healthBar.showArmor(this.armor != 0);
    }

    public void addArmor(int armor) {
        this.armor += armor;
        actor.healthBar.setArmor(this.armor);
        actor.healthBar.showArmor(this.armor != 0);
    }

    public void setAttackMultiplier(double attackMultiplier) {
        this.attackMultiplier = attackMultiplier;
    }

    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public void setDefendBuff(int defendBuff) {
        this.defendBuff = defendBuff;
    }

    public void setDefendMultiplier(double defendMultiplier) {
        this.defendMultiplier = defendMultiplier;
    }

    public void setStrengthBuff(int strengthBuff) {
        this.strengthBuff = strengthBuff;
    }

    public void death() {
        deathSound.setVolume(deathSound.play(),GameSound.soundVolume);
        actor.enemySprite.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(actor.width / 2, actor.height / 2, 2f),
                        Actions.sizeTo(actor.width / 20, actor.height / 20, 2f),
                        Actions.fadeOut(2)),
                Actions.run(() -> {
                    actor.addAction(Actions.removeActor());
                    actor.moveDisplay.addAction(Actions.removeActor());
                    for (Effect effect : effects)
                        effect.effectIcon.addAction(Actions.removeActor());
                })
        ));
        isDeath = true;

    }

    public void initialEffect() {
    }
}