package com.patron.game;

import com.badlogic.gdx.graphics.Color;

public class Effect implements Cloneable{
    protected static Player player;
    protected String name,description;
    protected int moves;
    protected boolean isPermanent = false;
    protected Enemy enemy = null;
    protected boolean isInstant = false;
    public EffectType effectType;
    public EffectPanel.EffectIcon effectIcon;
    public Color color;
    public Effect(String name,String description, boolean isInstant, int moves){
        this.name = name;
        this.description = description;
        this.moves = moves;
        this.isInstant = isInstant;
    }
    public Effect(String name,String description, boolean isInstant, boolean isPermanent){
        this.name = name;
        this.description = description;
        this.isPermanent = isPermanent;
        this.isInstant = isInstant;
    }
    public Effect(String name,String description, boolean isInstant, boolean isPermanent, int moves){
        this.name = name;
        this.description = description;
        this.isPermanent = isPermanent;
        this.isInstant = isInstant;
        this.moves = moves;
    }

    public void endOfTurn(){}
    public void startOfTurn(){}
    public void initialEffect(){}
    public void getDamage(){}
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
        return name;
    }
}

class RadiationEffect extends Effect{
    public RadiationEffect(int moves){
        super("Отруєння","Напочатку ходу отруєна істота втрачає ОЗ, з кожним ходом зменшується на 1",false,moves);
        effectType = EffectType.DEBUFF;
        color = Color.LIME;
    }
    public RadiationEffect(Enemy enemy, int moves){
        super("Отруєння","Напочатку ходу отруєна істота втрачає ОЗ, з кожним ходом зменшується на 1",false,moves);
        this.enemy = enemy;
        color = Color.LIME;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
            if (enemy != null){
                enemy.setHealth(enemy.getHealth()-moves);
                enemy.actor.addValue(moves, Color.SCARLET, EnemyActor.valueType.EFFECT_DAMAGE);
            }
            else{
                player.setHealth(player.getHealth()-moves);
                player.actor.addValue(moves, Color.SCARLET, EnemyActor.valueType.EFFECT_DAMAGE);
            }
        }
    }
}

class WeaknessEffect extends Effect{
    public WeaknessEffect(int moves){
        super("Слабкість","Ослаблені істоти наносять атаками на 25% шкоди менше",true,moves);
        effectType = EffectType.DEBUFF;
        color = Color.SKY;
    }
    public WeaknessEffect(Enemy enemy, int moves){
        super("Слабкість","Ослаблені істоти наносять атаками на 25% шкоди менше",true,moves);
        this.enemy = enemy;
        color = Color.SKY;
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
        super("Крихкість","Крихка істота отримує на 25% броні менше",true,moves);
        effectType = EffectType.DEBUFF;
        color = Color.ROYAL;
    }
    public FragilityEffect(Enemy enemy, int moves){
        super("Крихкість","Крихка істота отримує на 25% броні менше",true,moves);
        this.enemy = enemy;
        color = Color.ROYAL;
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
        if (enemy != null) enemy.setDefendMultiplier(1);
        else player.setDefendMultiplier(1);
    }
}
class VulnerabilityEffect extends Effect{
    public VulnerabilityEffect(int moves){
        super("Вразливість","Вразлива істота отримує на 25% більше шкоди від атак",true,moves);
        effectType = EffectType.DEBUFF;
        color = Color.FIREBRICK;
    }
    public VulnerabilityEffect(Enemy enemy, int moves){
        super("Вразливість","Вразлива істота отримує на 25% більше шкоди від атак",true,moves);
        this.enemy = enemy;
        color = Color.FIREBRICK;
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
        super("Зцілення","Істота зі зціленням отримує ОЗ, наприкінці ходу зменшується на 1",false,moves);
        effectType = EffectType.BUFF;
        color = Color.GREEN;
    }
    public CureEffect(Enemy enemy, int moves){
        super("Зцілення","Істота зі зціленням отримує ОЗ, наприкінці ходу зменшується на 1",false,moves);
        this.enemy = enemy;
        color = Color.GREEN;
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
        super("Кровотеча","Істота зі кровотечею втрачає ОЗ наприкінці ходу",false,true,moves);
        effectType = EffectType.DEBUFF;
        color = Color.RED;
    }
    public BleedingEffect(Enemy enemy, int moves){
        super("Кровотеча","Істота зі кровотечею втрачає ОЗ наприкінці ходу",false,true, moves);
        this.enemy = enemy;
        color = Color.RED;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
            if (enemy != null){
                enemy.setHealth(enemy.getHealth()-moves);
                enemy.actor.addValue(moves, Color.SCARLET, EnemyActor.valueType.EFFECT_DAMAGE);
            }
            else{
                player.setHealth(player.getHealth()-moves);
                player.actor.addValue(moves, Color.SCARLET, EnemyActor.valueType.EFFECT_DAMAGE);
            }
        }
    }
}
class StrengthEffect extends Effect{
    public StrengthEffect(int moves){
        super("Сила","Збільшує силу атаки",true,moves);
        effectType = EffectType.BUFF;
        color = Color.ORANGE;
        isPermanent = true;
        isInstant = true;
    }
    public StrengthEffect(Enemy enemy, int moves){
        super("Сила","Збільшує силу атаки",true,moves);
        effectType = EffectType.BUFF;
        color = Color.ORANGE;
        this.enemy = enemy;
        isPermanent = true;
        isInstant = true;
    }
    @Override
    public void effectResult(){
        if (moves != 0) {
            if (enemy != null) enemy.setStrengthBuff(moves);
            else player.setStrengthBuff(moves);
        }
        System.out.println(player.getStrengthBuff());
    }
    @Override
    public void setBase() {
        if (enemy != null) enemy.setStrengthBuff(0);
        else player.setStrengthBuff(0);
        System.out.println("dsgwaeg");
    }
}

class AgilityEffect extends Effect{
    public AgilityEffect(int moves){
        super("Сила","Збільшує ефект захисту",true,moves);
        effectType = EffectType.BUFF;
        color = Color.OLIVE;
        isPermanent = true;
        isInstant = true;
    }
    public AgilityEffect(Enemy enemy, int moves){
        super("Сила","Збільшує ефект захисту",true,moves);
        effectType = EffectType.BUFF;
        color = Color.OLIVE;
        this.enemy = enemy;
        isPermanent = true;
        isInstant = true;
    }
    @Override
    public void effectResult(){
        if (moves != 0) {
            if (enemy != null) enemy.setDefendBuff(moves);
            else player.setDefendBuff(moves);
        }
    }
    @Override
    public void setBase() {
        if (enemy != null) enemy.setDefendBuff(0);
        else player.setDefendBuff(0);

    }
}

class PlatedArmorEffect extends Effect {

    public PlatedArmorEffect(int moves){
        super("Панцирна броня","Надає захист вкінці ходу. З кожною незаблокованою атакою зменшується на 1",false,moves);
        effectType = EffectType.BUFF;
        color = Color.LIGHT_GRAY;
        isPermanent = true;
    }
    public PlatedArmorEffect(Enemy enemy, int moves){
        super("Панцирна броня","Надає захист вкінці ходу. З кожною незаблокованою атакою зменшується на 1",false,moves);
        effectType = EffectType.BUFF;
        color = Color.LIGHT_GRAY;
        this.enemy = enemy;
        isPermanent = true;
    }

    @Override
    public void getDamage() {
        moves--;
        if (moves <= 0)
            if (enemy == null){
                player.effects.remove(this);
                player.actor.effectPanel.removeEffect(this);
            }
            else{
                enemy.addArmor(moves);
                enemy.actor.effectPanel.removeEffect(this);
            }

    }

    @Override
    public void endOfTurn() {
        if (enemy == null) player.addArmor(moves);
        else enemy.addArmor(moves);
    }

    @Override
    public void initialEffect() {
        if (enemy == null) player.addArmor(moves);
        else enemy.addArmor(moves);

    }
}
enum EffectType{
    BUFF,
    DEBUFF
}