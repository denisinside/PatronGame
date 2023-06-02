package com.patron.game;

public class Effect implements Cloneable{
    protected static Player player;
    protected String name;
    protected int moves;
    protected boolean isPermanent = false;
    protected Enemy enemy = null;
    protected boolean isInstant = false;
    public Effect(String name, boolean isInstant, int moves){
        this.name = name;
        this.moves = moves;
        this.isInstant = isInstant;
    }
    public Effect(String name, boolean isInstant, boolean isPermanent){
        this.name = name;
        this.isPermanent = isPermanent;
        this.isInstant = isInstant;
    }
    public Effect(String name, boolean isInstant, boolean isPermanent, int moves){
        this.name = name;
        this.isPermanent = isPermanent;
        this.isInstant = isInstant;
        this.moves = moves;
    }
    public void effectResult(){}
    public void setBase(){}

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return name + "   " + moves;
    }
}

class RadiationEffect extends Effect{
    public RadiationEffect(int moves){
        super("Отруєння",false,moves);
    }
    public RadiationEffect(Enemy enemy, int moves){
        super("Отруєння",false,moves);
        this.enemy = enemy;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
            if (enemy != null) enemy.setHealth(enemy.getHealth()-moves);
            else player.setHealth(player.getHealth()-moves);
        }
    }
}

class WeaknessEffect extends Effect{
    public WeaknessEffect(int moves){
        super("Слабкість",true,moves);
    }
    public WeaknessEffect(Enemy enemy, int moves){
        super("Слабкість",true,moves);
        this.enemy = enemy;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
            if (enemy != null) enemy.setAttackMultiplier(0.75);
            else player.setAttackMultiplier(0.75);
        }
    }
    @Override
    public void setBase() {
        if (enemy != null) enemy.setAttackMultiplier(1);
        else player.setAttackMultiplier(1);

    }
}
class FragilityEffect extends Effect{
    public FragilityEffect(int moves){
        super("Крихкість",true,moves);
    }
    public FragilityEffect(Enemy enemy, int moves){
        super("Крихкість",true,moves);
        this.enemy = enemy;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
            if (enemy != null) enemy.setDefendMultiplier(0.75);
            else player.setDefendMultiplier(0.75);
        }
    }
    @Override
    public void setBase() {
        if (enemy != null) enemy.setDefendBuff(1);
        else player.setDefendBuff(1);
    }
}
class VulnerabilityEffect extends Effect{
    public VulnerabilityEffect(int moves){
        super("Вразливість",true,moves);
    }
    public VulnerabilityEffect(Enemy enemy, int moves){
        super("Вразливість",true,moves);
        this.enemy = enemy;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
            if (enemy != null) enemy.setDamageMultiplier(1.5);
            else player.setDamageMultiplier(1.5);
        }
    }
    @Override
    public void setBase() {
        if (enemy != null) enemy.setDamageMultiplier(1);
        else player.setDamageMultiplier(1);

    }
}
class CureEffect extends Effect{
    public CureEffect(int moves){
        super("Зцілення",false,moves);
    }
    public CureEffect(Enemy enemy, int moves){
        super("Зцілення",false,moves);
        this.enemy = enemy;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
            if (enemy != null) enemy.heal(moves);
            else player.heal(moves);
        }
    }
}

class BleedingEffect extends Effect{
    public BleedingEffect(int moves){
        super("Кровотеча",false,true,moves);
    }
    public BleedingEffect(Enemy enemy, int moves){
        super("Кровотеча",false,true, moves);
        this.enemy = enemy;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
            if (enemy != null) enemy.setHealth(enemy.getHealth()-moves);
            else player.setHealth(player.getHealth()-moves);
        }
    }
}