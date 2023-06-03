package com.patron.game;

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
    protected Random random = new Random();

    protected double damageMultiplier = 1;
    protected double defendMultiplier = 1;
    protected double attackMultiplier = 1;

    protected int strengthBuff = 0;
    protected int defendBuff = 0;

    public EnemyActor enemyActor;
    public Enemy(){
        health = maxHealth;
        enemyActor = new EnemyActor(200,300);
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
        } catch (CloneNotSupportedException g) {
            g.printStackTrace();
        }
    }
    public void heal(int healAmount) {
        if (healAmount + health <= maxHealth) health += healAmount;
        else health = maxHealth;
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
            }
            if(health <= 0) return;
        }
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
        }
    }
    public void makeMove(){
        switch (enemyMoves.get(moveIndex).getMove()){
            case ATTACK: attack(enemyMoves.get(moveIndex)); break;
            case DEFEND: defend(enemyMoves.get(moveIndex)); break;
            case IMPACT: castImpact(enemyMoves.get(moveIndex));
        }
        moveIndex = (moveIndex + 1) % enemyMoves.size();
    }
    public Move nextMove(){
        /*switch (enemyMoves.get(moveIndex).getMove()){
            case ATTACK: *//*if (enemyMoves.get(moveIndex).isWithEffect) return Move.EFFECTED_ATTACK;
                         else return Move.ATTACK;*//*
                return Move.ATTACK;
            case DEFEND: *//*if (enemyMoves.get(moveIndex).isWithEffect)  return Move.BUFFED_DEFENSE;
                         else return Move.DEFEND;*//*
                return  Move.DEFEND;
        }*/
        return enemyMoves.get(moveIndex).getMove();
    }
    public void castImpact(Action impact){
        if(impact.name.equals("Негативний вплив")){
            for (Effect e : impact.effects) player.addEffect(e);
        }else if(impact.name.equals("Позитивний вплив")){
            if (!impact.toAll)   for (Effect e : impact.effects) addEffect(e);
            else for (Enemy enemy : Fight.enemies){
                for (Effect e : impact.effects) enemy.addEffect(e);
            }
        }
    }
    public void attack(Action attack){
        int damage = (int) Math.round((attack.getSummaryValue()+strengthBuff)*attackMultiplier);
        player.getDamage(damage);
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
    }
    public void printStats() {
        String values = null;
        String format = "%-20s%-20s%-20s%-20s%n";
        System.out.format(format, "Name", "Health", nextMove(), "Armor");
        System.out.format(format, "----", "------", "------", "-------");
        if (nextMove() == Move.ATTACK) values = (int)Math.round(enemyMoves.get(moveIndex).getSummaryValue()*attackMultiplier) + " x " + enemyMoves.get(moveIndex).count;
        if (nextMove() == Move.DEFEND) values = (int)Math.round(enemyMoves.get(moveIndex).getSummaryValue()*defendMultiplier)+ " x " + enemyMoves.get(moveIndex).count;
        System.out.format(format, name, health+"/"+ maxHealth, values, armor);
        for (Effect effect : effects) System.out.println(effect);
        System.out.println("\n");
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
}
enum Move {
    ATTACK,
    BUFFED_DEFENSE,
    DEFEND,
    EFFECTED_ATTACK,
    IMPACT
}