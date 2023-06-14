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
        return name;
    }
}

class RadiationEffect extends Effect{
    public RadiationEffect(int moves){
        super("Отруєння",false,moves);
        description = "Напочатку ходу отруєна істота втрачає ОЗ, з кожним ходом зменшується на 1";
        effectType = EffectType.DEBUFF;
        color = Color.LIME;
    }
    public RadiationEffect(Enemy enemy, int moves){
        super("Отруєння",false,moves);
        description = "Напочатку ходу отруєна істота втрачає ОЗ, з кожним ходом зменшується на 1";
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
        super("Слабкість",true,moves);
        description = "Ослаблені істоти наносять атаками на 25% шкоди менше";
        effectType = EffectType.DEBUFF;
        color = Color.SKY;
    }
    public WeaknessEffect(Enemy enemy, int moves){
        super("Слабкість",true,moves);
        description = "Ослаблені істоти наносять атаками на 25% шкоди менше";
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
        super("Крихкість",true,moves);
        description = "Крихка істота отримує на 25% броні менше";
        effectType = EffectType.DEBUFF;
        color = Color.ROYAL;
    }
    public FragilityEffect(Enemy enemy, int moves){
        super("Крихкість",true,moves);
        description = "Крихка істота отримує на 25% броні менше";
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
        super("Вразливість",true,moves);
        description = "Вразлива істота отримує на 25% більше шкоди від атак";
        effectType = EffectType.DEBUFF;
        color = Color.FIREBRICK;
    }
    public VulnerabilityEffect(Enemy enemy, int moves){
        super("Вразливість",true,moves);
        description = "Вразлива істота отримує на 25% більше шкоди від атак";
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
        super("Зцілення",false,moves);
        description = "Істота зі зціленням отримує ОЗ, наприкінці ходу зменшується на 1";
        effectType = EffectType.BUFF;
        color = Color.GREEN;
    }
    public CureEffect(Enemy enemy, int moves){
        super("Зцілення",false,moves);
        description = "Істота зі зціленням отримує ОЗ, наприкінці ходу зменшується на 1";
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
        super("Кровотеча",false,true,moves);
        description = "Істота зі кровотечею втрачає ОЗ наприкінці ходу";
        effectType = EffectType.DEBUFF;
        color = Color.RED;
    }
    public BleedingEffect(Enemy enemy, int moves){
        super("Кровотеча",false,true, moves);
        description = "Істота зі кровотечею втрачає ОЗ наприкінці ходу";
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
        super("Сила",true,moves);
        description = "Збільшує силу атаки";
        effectType = EffectType.BUFF;
        color = Color.FIREBRICK;
        isPermanent = true;
        isInstant = true;
    }
    public StrengthEffect(Enemy enemy, int moves){
        super("Сила",true,moves);
        description = "Збільшує силу атаки";
        effectType = EffectType.BUFF;
        color = Color.FIREBRICK;
        this.enemy = enemy;
        isPermanent = true;
        isInstant = true;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
            if (enemy != null) enemy.setStrengthBuff(moves);
            else player.setStrengthBuff(moves);
        }
    }
    @Override
    public void setBase() {
        if (enemy != null) enemy.setStrengthBuff(0);
        else player.setStrengthBuff(0);

    }
}

class AgilityEffect extends Effect{
    public AgilityEffect(int moves){
        super("Сила",true,moves);
        description = "Збільшує ефект захисту";
        effectType = EffectType.BUFF;
        color = Color.FIREBRICK;
        isPermanent = true;
        isInstant = true;
    }
    public AgilityEffect(Enemy enemy, int moves){
        super("Сила",true,moves);
        description = "Збільшує ефект захисту";
        effectType = EffectType.BUFF;
        color = Color.FIREBRICK;
        this.enemy = enemy;
        isPermanent = true;
        isInstant = true;
    }
    @Override
    public void effectResult(){
        if (moves > 0) {
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
enum EffectType{
    BUFF,
    DEBUFF
}