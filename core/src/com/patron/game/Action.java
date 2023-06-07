package com.patron.game;

public abstract class Action{
    public Effect[] effects;
    public String name = "";
    public int count = 1;
    boolean toAll = false;
    public boolean isWithEffect = false;

    public abstract int getSummaryValue();
    public abstract Move getMove();
}


 class Attack extends Action{

    public int damage;

    public Attack(int damage){
        this.damage = damage;
        this.name = "Aтака";
    }
    public Attack(int damage, int count){
        this.damage = damage;
        this.count = count;
        this.name = "Aтака";
    }
    public Attack(int damage, Effect[] effects) {
        this.damage = damage;
        this.effects = effects;
        isWithEffect = true;
        this.name = "Aтака";
    }
    public Attack(int damage, int count, Effect[] effects) {
        this.damage = damage;
        this.effects = effects;
        this.count = count;
        isWithEffect = true;
        this.name = "Aтака";
    }

    public Move getMove(){
        if (effects == null) return  Move.ATTACK;
        else return Move.EFFECTED_ATTACK;

    }
    public int getSummaryValue() {
        return count *damage;
    }

 }
class Defend extends Action{
    public int block;

    public Defend(int block){
        this.block = block;
        this.name = "Захист";
    }
    public Defend(int block, boolean toAll){
        this.block = block;
        this.name = "Захист";
        this.toAll = toAll;
    }
    public Defend(int block, int count){
        this.block = block;
        this.count = count;
        this.name = "Захист";
    }
    public Defend(int block, Effect[] effects) {
        this.block = block;
        this.effects = effects;
        isWithEffect = true;
        this.name = "Захист";
    }
    public Defend(int block, Effect[] effects, boolean toAll) {
        this.block = block;
        this.effects = effects;
        isWithEffect = true;
        this.name = "Захист";
        this.toAll = toAll;
    }
    public Defend(int block, int count, Effect[] effects) {
        this.block = block;
        this.effects = effects;
        this.count = count;
        isWithEffect = true;
        this.name = "Захист";
    }
    public Move getMove(){
        return  Move.DEFEND;
    }
    public int getSummaryValue() {
        return count *block;
    }

}

class Impact extends Action{
    public Impact(){
        toName();
    }
    public Impact(Effect[] effects) {
        this.effects = effects;
        isWithEffect = true;
        toName();
    }
    public Impact(Effect[] effects, boolean toAll) {
        this.effects = effects;
        isWithEffect = true;
        toName();
        this.toAll = toAll;
    }
    private void toName(){
        if (effects == null) name = "Невідома дія";
        else  if (effects[0].enemy == null) name = "Негативний вплив";
        else name = "Позитивний вплив";
    }
    public Move getMove(){
        if (effects == null) return  Move.IMPACT;
        else  if (effects[0].enemy == null) return  Move.DEBUFF;
        else return  Move.BUFF;
    }
    public int getSummaryValue() {
        return 0;
    }

}


