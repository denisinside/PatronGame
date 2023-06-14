package com.patron.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;

public class Player {
    private int maxHealth = 75;
    private int maxEnergy = 3;
   private int health;
   private int energy;
   int money = 0;
   private int cardPerRound = 5;
   private int armor = 0;
    protected ArrayList<Effect> effects = new ArrayList<>();
    protected Array<Artefact> artefacts = new Array<>();


    private double damageMultiplier = 1;
    private double defendMultiplier = 1;
    private double attackMultiplier = 1;

    private int strengthBuff = 0;
    private int defendBuff = 0;
    public PlayerActor actor;


    public Player(){
       health = maxHealth;
       energy = maxEnergy;
        actor = new PlayerActor();
        actor.healthBar.setCurrentValue(health);
   }
   private void featureArtefact(String name){
        if (name.equals("Рюкзак")) cardPerRound += 1;
       if (name.equals("Червоне яблуко")){
           maxHealth += 10;
           heal(10);
       }
       if (name.equals("Зелене яблуко")){
           maxHealth += 5;
           heal(maxHealth-health);
       }
       if (name.equals("Золотий злиток")) addGold(200);
       if (name.equals("Срібний злиток")) addGold(100);
       if (name.equals("Бронзовий злиток")) addGold(50);

       if (name.equals("Око Буданова")) MoveDisplay.setShowMoreInfo(true);
       }
   public void test(){
        addArtefact(ArtefactFactory.getArtefact("Око Буданова"));
     }
   public void addArtefact(Artefact artefact){
        artefacts.add(artefact);
        GameProgress.topPanel.addActor(artefact);
       featureArtefact(artefact.name);
   }
   public boolean ifHasArtefact(String name){
    for (Artefact artefact : artefacts)
        if (artefact.name.equals(name)) return  true;
    return false;
   }
    public void addEffect(Effect effect){
        if (effect instanceof RadiationEffect)
            if(ifHasArtefact("Слиз"))
                effect.moves=-1;

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
    public void effectCheck(){
        Iterator<Effect> iterator = effects.iterator();
        while (iterator.hasNext()){
            Effect effect = iterator.next();
            if (!effect.isPermanent && effect.isInstant) effect.moves--;
            if (effect.moves > 0)effect.effectResult();
            if (!effect.isPermanent && !effect.isInstant) effect.moves--;
            if ((!effect.isPermanent && effect.moves <= 0) || (effect.isPermanent && effect.moves == 0)){
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

    }
    public void getDamage(int damage){
        damage = (int)Math.round(damage*damageMultiplier);
        if (armor != 0 && armor >= damage)  armor -= damage;
        else {
            if (armor != 0){
                damage -= armor;
                armor = 0;
            }
           if (health - damage >= 0){
               health -= damage;
           }
           else  health = 0;
            actor.addValue(damage, Color.PINK, EnemyActor.valueType.DAMAGE);
           actor.healthBar.showArmor(false);
        }
        actor.healthBar.setArmor(armor);
        actor.healthBar.setCurrentValue(health);
    }
    public int getArmor() {
        return armor;
    }
    public void addArmor(int armor) {
        this.armor += (int) Math.round((armor+defendBuff)*defendMultiplier);
        actor.healthBar.setArmor(this.armor);
        actor.healthBar.showArmor(true);
    }

    public void setArmor(int armor) {
        this.armor = armor;
        actor.healthBar.setArmor(this.armor);
        actor.healthBar.showArmor(armor != 0);
    }

    public int getMaxHealth() {
        return maxHealth;
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
        actor.healthBar.setCurrentValue(health);
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

    public void addGold(int i) {
        money += i;
    }
}
