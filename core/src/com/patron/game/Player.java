package com.patron.game;

import java.util.ArrayList;
import java.util.Iterator;

public class Player {
    private int maxHealth = 75;
    private int maxEnergy = 3;
   private int health;
   private int energy;
   private int cardPerRound = 5;
   private int armor = 0;
    protected ArrayList<Effect> effects = new ArrayList<>();


    private double damageMultiplier = 1;
    private double defendMultiplier = 1;
    private double attackMultiplier = 1;

    private int strengthBuff = 0;
    private int defendBuff = 0;


    public Player(){
       health = maxHealth;
       energy = maxEnergy;
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
            effectCopy.setEnemy(null);
            effects.add(effectCopy);
            if (effectCopy.isInstant) effectCopy.effectResult();
        } catch (CloneNotSupportedException g) {
            g.printStackTrace();
        }
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
        }
    }
    public void heal(int healAmount) {
        if (healAmount + health <= maxHealth) health += healAmount;
        else health = maxHealth;
    }
    public void getDamage(int damage){
        damage = (int)Math.round(damage*damageMultiplier);
        if (armor != 0 && armor >= damage)  armor -= damage;
        else {
            if (armor != 0){
                damage -= armor;
                armor = 0;
            }
           if (health - damage >= 0) health -= damage;
           else  health = 0;
        }
    }
    public int getArmor() {
        return armor;
    }
    public void addArmor(int armor) {
        this.armor += (int) Math.round((armor+defendBuff)*defendMultiplier);
        System.out.println("Захистився на " + (int) Math.round((armor+defendBuff)*defendMultiplier));
    }

    public void setArmor(int armor) {
        this.armor = armor;
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

    public int getCardPerRound() {
        return cardPerRound;
    }

    public void setCardPerRound(int cardPerRound) {
        this.cardPerRound = cardPerRound;
    }

    public int getHealth() {
        return health;
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

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setStrengthBuff(int strengthBuff) {
        this.strengthBuff = strengthBuff;
    }

    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public double getDefendMultiplier() {
        return defendMultiplier;
    }

    public int getDefendBuff() {
        return defendBuff;
    }

    public int getStrengthBuff() {
        return strengthBuff;
    }

    public void printStats() {
        String format = "%-15s%-15s%-15s%-15s%n";
        System.out.format(format, "Name", "Health", "Energy", "Armor");
        System.out.format(format, "----", "------", "------", "-------");
        System.out.format(format, "Патрон", health +"/"+ maxHealth, energy, armor);
        for (Effect effect : effects) System.out.println(effect);
        System.out.println("\n");
    }
}
