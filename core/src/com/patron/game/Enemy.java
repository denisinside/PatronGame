package com.patron.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Enemy {
    static Player player;
    protected String name;
    protected int maxHealth;
    protected int health;
    protected int armor = 0;
    protected ArrayList<Action> enemyMoves = new ArrayList<>();
    protected int moveIndex = 0;
    protected ArrayList<Effect> effects = new ArrayList<>();
    protected static Random random = new Random();

    protected double damageMultiplier = 1;
    protected double defendMultiplier = 1;
    protected double attackMultiplier = 1;

    protected int strengthBuff = 0;
    protected int defendBuff = 0;

    public EnemyActor actor;
    public Enemy(String name,int hp){
        this.name = name;
        maxHealth = health = hp;
        actor = new EnemyActor(200,300,this);
    }

    public void shuffleMoves(Action[]attacks, Action[]blocks){
        int attackIndex = 0, blockIndex = 0;
        boolean next = random.nextBoolean();
         if (next) {
             enemyMoves.add(attacks[attackIndex]);
             attackIndex++;
             next = false;
         }else{
             enemyMoves.add(blocks[blockIndex]);
             blockIndex++;
             next = true;
         }
        while (blockIndex != blocks.length || attackIndex != attacks.length){
                if (next){
                    if (attacks.length != attackIndex){
                        enemyMoves.add(attacks[attackIndex]);
                        attackIndex++;
                    }
                    next = false;
                }else{
                    if (blocks.length != attackIndex){
                        enemyMoves.add(blocks[blockIndex]);
                        blockIndex++;
                    }
                    next = true;
                }
        }

        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }
    public void addEffect(Effect effect){
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
        } catch (CloneNotSupportedException g) {
            g.printStackTrace();
        }
    }
    public void heal(int healAmount) {
        if (healAmount + health <= maxHealth){
            health += healAmount;
        }
        else health = maxHealth;
        actor.healthBar.setCurrentValue(health);
        actor.addValue(healAmount, Color.GREEN, EnemyActor.valueType.HEAL);

    }
    public void effectCheck(){
        Iterator<Effect> iterator = effects.iterator();
        while (iterator.hasNext()){
            Effect effect = iterator.next();
            if (!effect.isPermanent && effect.isInstant) effect.moves--;
            if (effect.moves > 0)effect.effectResult();
            if (!effect.isPermanent && !effect.isInstant) effect.moves--;
            if (!effect.isPermanent && effect.moves <= 0){
                effect.setBase();
                iterator.remove();
                actor.effectPanel.removeEffect(effect);
            }
            System.out.println(effect);
            if(health <= 0) return;
        }
        actor.healthBar.setCurrentValue(health);
    }
    public void getDamage(int damage){
        damage = (int)Math.round(damage*damageMultiplier);
        if (armor != 0 && armor >= damage)  armor -= damage;
        else {
            if (armor != 0){
                damage -= armor;
                armor = 0;
            }
            health -= damage;
            actor.addValue(damage, Color.PINK, EnemyActor.valueType.DAMAGE);
            actor.healthBar.showArmor(false);
        }
        actor.healthBar.setArmor(armor);
    }
    public void makeMove(){
        switch (enemyMoves.get(moveIndex).getMove()){
            case EFFECTED_ATTACK:
            case BUFFED_ATTACK:
            case ATTACK:
                attack(enemyMoves.get(moveIndex)); break;
            case DEFEND: defend(enemyMoves.get(moveIndex)); break;
            case IMPACT: castImpact(enemyMoves.get(moveIndex)); break;
            case DEBUFF: castDebuff(enemyMoves.get(moveIndex)); break;
            case BUFF: castBuff(enemyMoves.get(moveIndex));
        }
        Fight.checkKill();
        moveIndex = (moveIndex + 1) % enemyMoves.size();
        actor.moveDisplay.setMove(enemyMoves.get(moveIndex));
    }
    public Move nextMove(){
        return enemyMoves.get(moveIndex).getMove();
    }
    public void castImpact(Action impact){

    }
    public void castBuff(Action impact){
        if (!impact.toAll)   for (Effect e : impact.effects) addEffect(e);
        else for (Enemy enemy : Fight.enemies){
            for (Effect e : impact.effects) enemy.addEffect(e);
        }
    }
    public void castDebuff(Action impact){
        for (Effect e : impact.effects) player.addEffect(e);
    }
    public void attack(Action attack){
        int damage = (int) Math.round((attack.getSummaryValue()+strengthBuff)*attackMultiplier);
        player.getDamage(damage);
        actor.enemySprite.addAction(Actions.sequence(
                Actions.moveBy(-100,0,0.8f),
                Actions.moveBy(100,0,0.8f)
        ));
        if (attack.isWithEffect)
            for (Effect e : attack.effects)player.addEffect(e);
    }
    public void defend(Action block){
        if (!block.toAll) {
            int armor = (int) Math.round((block.getSummaryValue() + defendBuff) * defendMultiplier);
            addArmor(armor);
            if (block.isWithEffect)
                for (Effect e : block.effects) addEffect(e);
        }  else{
            block.toAll = false;
            for (Enemy enemy : Fight.enemies) enemy.defend(block);
        }
        actor.healthBar.setArmor(armor);
        actor.healthBar.showArmor(true);
    }
    public int getActualAttackDamage(){
        return (int)Math.round(enemyMoves.get(moveIndex).getSummaryValue()*attackMultiplier);
    }
    public int getActualBlock(){
        return (int)Math.round(enemyMoves.get(moveIndex).getSummaryValue()*defendMultiplier);
    }
    public boolean ifHas(Effect effect){
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
        actor.healthBar.showArmor(armor!=0);
    }

    public void addArmor(int armor) {
        this.armor += armor;
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
        actor.enemySprite.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.moveBy(actor.width/2, actor.height/2,2f),
                    Actions.sizeTo(actor.width/20, actor.height/20,2f),
                    Actions.fadeOut(2)),
                Actions.run(()->{
                    actor.addAction(Actions.removeActor());
                    actor.moveDisplay.addAction(Actions.removeActor());
                    for (Effect effect : effects)
                        effect.effectIcon.addAction(Actions.removeActor());
                })
        ));

    }
}
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