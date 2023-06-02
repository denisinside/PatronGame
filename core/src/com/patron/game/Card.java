package com.patron.game;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Random;

enum Rarity{
    STATUS,
    CURSE,
    COMMON,
    RARE,
    LEGENDARY
}
enum UseType{
    DEFENSE,
    HEAL,
    DEBUFF,
    BUFF
}

public class Card implements Cloneable{
    protected static Player player;
    protected static Random rnd  = new Random();
    protected String name, description;
    protected int cost,  costNow;
    protected int enemyDamage, playerDamage;
    protected int skillStrength;
    protected boolean burning, ghostly, disposable, playable = true;
    protected UseType type;
    protected String target; // all, random, one
    protected Effect[] effects = null;
    protected Rarity rarity;
    public CardActor cardActor;
    public Card(String name, String description, Rarity rarity, int cost){
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        costNow = cost;
        cardActor = new CardActor(this,100,100);
    }

    private void init(){
        switch (name){
            case "Різанина": ghostly = true; break;
            default:
        }
    }
    public void use(Enemy enemy, ArrayList<Enemy> enemies){}
    public String getName() {
        return name;
    }

    public int getEnemyDamage() {
        return enemyDamage;
    }

    public int getCost() {
        return cost;
    }

    public int getCostNow() {
        return costNow;
    }

    public void setCostNow(int costNow) {
        this.costNow = costNow;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getPlayerDamage() {
        return (int)Math.round(playerDamage*player.getDamageMultiplier());
    }
    public String getActualDescription(){
        String result = description;
        result = result.replaceAll("enemyDamage", ""+ getEnemyDamageWithBuff());
        result = result.replaceAll("armor", ""+getPlayerDefenseWithBuff());
        result = result.replaceAll("playerDamage", ""+getPlayerDamage());

        return result;
    }
    protected int getEnemyDamageWithBuff(){
        return (int)Math.round((enemyDamage+player.getStrengthBuff())*player.getAttackMultiplier());
    }
    protected int getPlayerDefenseWithBuff(){
        return (int)Math.round((skillStrength+player.getDefendBuff())*player.getDefendMultiplier());
    }
    protected void pickUpCard(int amount){
        Fight.cardDeal(amount);
    }
    public String toString() {
        return "| " + name + " | " + (playable ? cost : 'x') + " | " + getActualDescription();
    }
}

class AttackCard extends Card{
    int attackCount;
    public AttackCard(String name, String description,Rarity rarity,  int energy, int damage, String target, int attackCount){
        super(name, description,  rarity,energy);
        this.enemyDamage = damage;
        this.attackCount = attackCount;
        this.target = target;
    }
    public AttackCard(String name, String description,Rarity rarity,  int energy, int damage, String target, int attackCount, Effect[]effects){
        super(name, description, rarity, energy);
        this.enemyDamage = damage;
        this.attackCount = attackCount;
        this.effects = effects;
        this.target = target;
    }
    private void uniqueUse(Enemy enemy, ArrayList<Enemy> enemies){
        switch (name){
            case "Айкідо":
                pickUpCard(1);
                break;
            case "Безглуздий удар":
                Fight.draw.add(rnd.nextInt(Fight.draw.size()), CardFactory.getStatus("Запаморочення"));
                break;
            case "У слабке місце":
                if (enemy.ifHas(new VulnerabilityEffect(1))) player.setEnergy(player.getEnergy()+1);
                break;
            case "Відчайдушний удар":
                Fight.draw.add(rnd.nextInt(Fight.draw.size()), CardFactory.getStatus("Рана"));
                break;
            case "Маневр":
                skillStrength = 5;
                player.addArmor(getPlayerDefenseWithBuff());
                break;
            case "Збити з ніг":
                Fight.draw.add(rnd.nextInt(Fight.draw.size()), Fight.getCardFromDiscard());
            default:
        }
    }
    @Override
    public void use(Enemy enemy, ArrayList<Enemy> enemies) {
        ArrayList<Enemy> enemies1 = new ArrayList<>(enemies);
        if (target.equals("one")) {
            enemies1.clear();
            enemies1.add(enemy);
        }
        if (!target.equals("random")) {
            for (Enemy enemy1 : enemies1) {
                for (int i = 0; i < attackCount; i++) {
                    enemy1.getDamage(getEnemyDamageWithBuff());
                }
                if (effects != null) for (Effect effect : effects){
                    try {
                        Effect effectCopy = (Effect) effect.clone();
                        effectCopy.setEnemy(enemy1);
                        enemy1.addEffect(effectCopy);
                        if (effectCopy.isInstant) effectCopy.effectResult();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            for (int i = 0; i < attackCount; i++){
                Enemy temp = enemies1.get(rnd.nextInt(enemies1.size()));
                temp.getDamage(getEnemyDamageWithBuff());
                if (effects != null) for (Effect effect : effects){
                    effect.setEnemy(enemy);
                    temp.addEffect(effect);
                }
            }
        }
        uniqueUse(enemy,enemies1);
    }

}
class SkillCard extends Card{
    int blockCount = 1;
    public SkillCard(String name, String description,Rarity rarity,  int energy, UseType type, String useType, int skillStrength){
        super(name, description, rarity, energy);
        this.skillStrength = skillStrength;
        this.type = type;
        this.target = useType;
    }
    public SkillCard(String name, String description,Rarity rarity,  int energy, UseType type, String useType, Effect[]effects){
        super(name, description, rarity, energy);
        this.effects = effects;
        this.type = type;
        this.target = useType;
    }
    @Override
    public void use(Enemy enemy, ArrayList<Enemy> enemies){
        if (target.equals("player")) switch (type){
            case DEFENSE: for(int i = 0; i < blockCount; i++) player.addArmor(getPlayerDefenseWithBuff());
            break;
            case HEAL: player.heal(skillStrength); break;
            case BUFF:
            case DEBUFF:
                break;
        }
        if (effects != null )for (Effect effect : effects) player.addEffect(effect);

    }
}

class StatusCard extends Card{
    public StatusCard(String name, String description, boolean playable){
        super(name,description,Rarity.STATUS, 1);
        this.playable = playable;
        if (name.equals("Запаморочення")) disposable = true;
        if (name.equals("Опромінювання")){
            burning = true;
            disposable = true;
        }
        if (name.equals("Слиз")) burning = true;
    }

    @Override
    public void use(Enemy enemy, ArrayList<Enemy> enemies){
        switch (name){
            case "Опік":
                playerDamage = 2;
                player.getDamage(getPlayerDamage());
                break;
            case "Опромінювання":
                player.addEffect(new RadiationEffect(2));
                break;
            default:
        }
    }
}
class CurseCard extends Card{
    public CurseCard(String name, String description){
        super(name, description, Rarity.CURSE, 1);
        playable = name.equals("Зацикленість");
    }

    @Override
    public void use(Enemy enemy, ArrayList<Enemy> enemies){
        switch (name){
            case "Сором":
                player.addEffect(new FragilityEffect(2));
                break;
            case "Зрада":
                player.addEffect(new VulnerabilityEffect(2));
                break;
            case "Безсилля":
                player.addEffect(new WeaknessEffect(2));
                break;
            case "Прокрастинація":
                player.getDamage(Fight.inHand.size());
                break;
            case "Пуста миска":
                playerDamage = 2;
                player.getDamage(getPlayerDamage());
                disposable = true;
                break;
            case "Зацикленість":
                Fight.draw.add(0, CardFactory.getCurse("Зацикленість"));
                break;
            default:
        }
    }
}