package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class EnemyActor extends Actor {
    public Enemy enemy;
    public EnemySprite enemySprite;
    public HealthBar healthBar;
    public MoveDisplay moveDisplay;
    public EffectPanel effectPanel;
    Label name;
    float width, height, x, y;
    Array<Label> valuesDisplay;
    Array<Actor> otherValues;

    static class EnemySprite extends Actor{
        Sprite enemySprite;
        public EnemySprite(Sprite sprite){
            enemySprite = sprite;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            enemySprite.setColor(getColor());
            enemySprite.draw(batch);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            enemySprite.setBounds(getX(),getY(),getWidth(),getHeight());

        }
    }

    public EnemyActor(float width, float height, Enemy enemy){
        this.width = width;
        this.height = height;
        this.enemy = enemy;
        valuesDisplay = new Array<>();
        otherValues = new Array<>();

        setWidth(width);
        setHeight(height+50);
        healthBar = new HealthBar(getWidth(),20,enemy.maxHealth);

        moveDisplay = new MoveDisplay();
        moveDisplay.setPosition(getX()+getWidth()/2+MoveDisplay.width/2,getY()+getHeight());

        effectPanel = new EffectPanel(getWidth());

        name = new Label(enemy.name,new Label.LabelStyle(Fonts.ICON_NUMBERS,Color.WHITE));
        name.setAlignment(Align.center);
        name.addAction(Actions.alpha(0));

        // потім шукати буде по клас нейму, але зараз буде 1 картинка
        if (enemy instanceof Bandit)
        enemySprite = new EnemySprite(new Sprite(new Texture(Gdx.files.internal("assets/enemies/Bandit.png"))));
        else if(enemy instanceof RadioactiveRat)
            enemySprite = new EnemySprite(new Sprite(new Texture(Gdx.files.internal("assets/enemies/RadioRat.png"))));

        addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                name.addAction(Actions.fadeIn(0.3f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                name.addAction(Actions.fadeOut(0.3f));
            }
        });
    }
    enum valueType{
        DAMAGE,
        HEAL,
        EFFECT_DAMAGE
    }
    public void addEffect(Effect effect){
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = Fonts.characters;
        fontParameter.size = 24;
        fontParameter.borderWidth = 6;
        BitmapFont font = Fonts.lisichkaComicFontGenerator.generateFont(fontParameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Label label = new Label(effect.name,new Label.LabelStyle(font,effect.color));
        valuesDisplay.add(label);
        label.setPosition(getX() + getWidth()/2- label.getWidth()/2, getY() + getHeight()/2);
        label.addAction(Actions.alpha(0));

        EffectPanel.EffectIcon effectIcon = new EffectPanel.EffectIcon(effect);
        effectIcon.setPosition(getX(), getY()+Math.abs(getHeight()-getWidth()));
        effectIcon.tooltip = null;
        effectIcon.showMoves = false;
        effectIcon.addAction(Actions.alpha(0));
        otherValues.add(effectIcon);

        label.setSize(150,150);
        label.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0,getHeight()/4,2f, Interpolation.fastSlow),
                        Actions.sizeTo(120,120,2f, Interpolation.smooth),
                        Actions.fadeIn(0.3f)
                ),
                Actions.fadeOut(0.2f),
                Actions.run(()->{
                    label.addAction(Actions.removeActor());
                    valuesDisplay.removeValue(label,true);
                })
        ));
        effectIcon.setSize(getWidth(),getWidth());
        effectIcon.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(getX()-(getWidth()/10-getWidth())/2,getY()-(getWidth()/10-getWidth())/2,2f, Interpolation.fastSlow),
                        Actions.sizeTo(getWidth()/10,getWidth()/10,2f, Interpolation.smooth),
                        Actions.fadeIn(1.5f)
                ),
                Actions.fadeOut(0.2f),
                Actions.run(()->{
                    effectIcon.addAction(Actions.removeActor());
                    otherValues.removeValue(effectIcon,true);
                })
        ));
    }
    public void addValue(int value, Color color, valueType valueType){
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = Fonts.characters;
        fontParameter.size = 48;
        fontParameter.borderWidth = 6;
        BitmapFont font = Fonts.lisichkaComicFontGenerator.generateFont(fontParameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        Label label = new Label(value+"",new Label.LabelStyle(font,color));
        valuesDisplay.add(label);
        label.setPosition(MathUtils.random(getX(),getX()+getWidth()), (float) (MathUtils.random(getY()+getHeight()/4,getY()+getHeight())));
        label.addAction(Actions.alpha(0));

        switch (valueType){
            case HEAL: label.setSize(300,300);
                label.addAction(Actions.sequence(
                        Actions.parallel(
                                Actions.moveBy(MathUtils.random(-getWidth()/5,getWidth()/5),MathUtils.random(-getHeight()/7,getHeight()/7),2f, Interpolation.circleIn),
                                Actions.sizeTo(30,30,2f, Interpolation.bounceIn),
                                Actions.fadeIn(0.3f)
                        ),
                        Actions.fadeOut(0.2f),
                        Actions.run(()->{
                            label.addAction(Actions.removeActor());
                            valuesDisplay.removeValue(label,true);
                        })
                ));
                break;
            case DAMAGE: label.setSize(900,900);
                label.addAction(Actions.sequence(
                        Actions.parallel(
                                Actions.moveBy(MathUtils.random(-getWidth()/5,getWidth()/5),MathUtils.random(-getHeight()/7,getHeight()/7),2f, Interpolation.elastic),
                                Actions.sizeTo(30,30,2f, Interpolation.sineIn),
                                Actions.fadeIn(0.3f)
                        ),
                        Actions.delay(0.2f),
                        Actions.fadeOut(0.2f),
                        Actions.run(()->{
                            label.addAction(Actions.removeActor());
                            valuesDisplay.removeValue(label,true);
                        })
                ));
                break;
            case EFFECT_DAMAGE: label.setSize(600,600);
                label.addAction(Actions.sequence(
                        Actions.parallel(
                                Actions.moveBy(MathUtils.random(-getWidth()/5,getWidth()/5),MathUtils.random(-getHeight()/7,getHeight()/7),2f, Interpolation.circle),
                                Actions.sizeTo(30,30,2f, Interpolation.circle),
                                Actions.fadeIn(0.3f)
                        ),
                        Actions.delay(0.2f),
                        Actions.fadeOut(0.2f),
                        Actions.run(()->{
                            label.addAction(Actions.removeActor());
                            valuesDisplay.removeValue(label,true);
                        })
                ));
                break;
        }
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        enemySprite.setBounds(enemySprite.getX(),enemySprite.getY(),getWidth(),getHeight()-50);
        enemySprite.act(delta);
        healthBar.setBounds(getX(),getY(),getWidth(),30);
        setBounds(getX(), getY(), getWidth(), getHeight());

        healthBar.setCurrentValue(enemy.health);
        healthBar.act(delta);

        effectPanel.setPosition(getX(),getY()- effectPanel.getHeight());
        effectPanel.act(delta);

        moveDisplay.setPosition(getX()+getWidth()/2-MoveDisplay.width/2,getY()+getHeight());
        moveDisplay.act(delta);

        name.setBounds(getX(), effectPanel.getY()- name.getHeight(),getWidth(),name.getHeight());
        name.act(delta);
        for (Label label : valuesDisplay){
            label.act(delta);
            label.setBounds(label.getX(),label.getY(),label.getWidth(),label.getHeight());
            label.getStyle().font.getData().setScale(label.getWidth()/150);
        }
        for (Actor actor : otherValues){
            actor.act(delta);
            actor.setColor(getColor());
            actor.setBounds(actor.getX(),actor.getY(),actor.getWidth(),actor.getHeight());
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        enemySprite.setPosition(getX(),getY()+50);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        enemySprite.draw(batch,parentAlpha);
        healthBar.draw(batch,parentAlpha);
        effectPanel.draw(batch,parentAlpha);
        moveDisplay.draw(batch,parentAlpha);
        name.draw(batch,parentAlpha);
        for (Label label : valuesDisplay){
            label.draw(batch,parentAlpha);
        }
        for (Actor actor : otherValues){
            actor.draw(batch,parentAlpha);
        }
    }
}
class HealthBar extends Actor {
    private Sprite armorIcon;
    private float width,height;
    private float maxValue,currentValue;
    private final Color backgroundColor, foregroundColor, borderColor;
    private int borderWidth;
    public boolean showArmor = false;
    private int armor = 0;

    private ShapeRenderer shapeRenderer;

    public HealthBar(float width, float height, int maxValue) {
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;

        this.backgroundColor = Color.FIREBRICK;
        this.foregroundColor = Color.RED;
        this.borderColor = Color.BROWN;
        borderWidth = 5;
        armorIcon = new Sprite(new Texture(Gdx.files.internal("armor.png")));

        shapeRenderer = new ShapeRenderer();
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setMaxValue(float maxValue){
        this.maxValue = maxValue;
    }
    public void setCurrentValue(float currentValue) {
        this.currentValue = (int) MathUtils.clamp(currentValue, 0, maxValue);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (showArmor)shapeRenderer.setColor(Color.ROYAL);
        else shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(getX(), getY(), width, height);

        if (showArmor)shapeRenderer.setColor(Color.SKY);
        else shapeRenderer.setColor(foregroundColor);
        float fillWidth =(currentValue / maxValue) * width;
        shapeRenderer.rect(getX(), getY(), fillWidth, height);

        if (showArmor)shapeRenderer.setColor(Color.NAVY);
        else shapeRenderer.setColor(borderColor);
        shapeRenderer.rectLine(getX(), getY(), getX() + width, getY(), borderWidth);
        shapeRenderer.rectLine(getX(), getY() + height, getX() + width, getY() + height, borderWidth);
        shapeRenderer.rectLine(getX(), getY(), getX(), getY() + height, borderWidth);
        shapeRenderer.rectLine(getX() + width, getY(), getX() + width, getY() + height, borderWidth);

        shapeRenderer.end();

        batch.begin();
        Fonts.ALBIONIC_LARGE_DESC.draw(batch,((int)currentValue)+"/"+((int)maxValue),getX(),getY()+getHeight()-getHeight()/4,getWidth(), Align.center,true);
        if (showArmor) {
            armorIcon.draw(batch);
            Fonts.ALBIONIC_LARGE_DESC.draw(batch,armor+"",getX()-30,getY()+getHeight()/2,60, Align.center,true);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
       armorIcon.setBounds(getX()-30,getY()-25,60,60);
    }

    public void showArmor(boolean b) {
        showArmor = b;
    }
}
class MoveDisplay extends Actor{
    public static Sprite attack;
    public static Sprite buffed_attack;
    public static Sprite effected_attack;
    public static Sprite defend;
    public static Sprite buffed_defend;
    public static Sprite buff;
    public static Sprite debuff;
    public static Sprite impact;
    private Sprite move;
    private Action action;
    protected Tooltip tooltip;
    public static int width = 80,height = 80;
    public MoveDisplay(){
        setWidth(width);
        setHeight(height);

        move = new Sprite();
        tooltip = new Tooltip("12345", "0987654321",this);

    }
    public void setMove(Action action){
        this.action = action;
        switch (action.getMove()){
            case DEFEND: move = new Sprite(defend);
                tooltip.setText("Захист", "Ворог планує піти в захист");
                break;
            case ATTACK: move = new Sprite(attack);
                tooltip.setText("Атака", "Ворог планує атакувати!");
                break;
            case EFFECTED_ATTACK: move = new Sprite(effected_attack);
                tooltip.setText("Заклинальний удар", "Ворог планує атакувати та накласти негативний ефект!");
                break;
            case BUFF: move = new Sprite(buff);
                tooltip.setText("Чари", "Ворог планує накласти на себе позитивний ефект");
                break;
            case DEBUFF: move = new Sprite(debuff);
                tooltip.setText("Заклинання", "Ворог планує накласти на вас негативний ефект!");
                break;
            case IMPACT: move = new Sprite(impact);
                tooltip.setText("Невідома дія", "Ворог замислив щось невідоме...");
                break;
            case BUFFED_ATTACK: move = new Sprite(buffed_attack);
                tooltip.setText("Чарівна атака", "Ворог планує атакувати та накласти на себе чари!");
                break;
            case BUFFED_DEFENSE: move = new Sprite(buffed_defend);
                tooltip.setText("Чарівний захист", "Ворог планує піти в захист та накласти на себе чари!");
                break;
        }
    }
    public static void init(){
        attack = new Sprite(new Texture(Gdx.files.internal("attack.png")));
        buffed_attack = new Sprite(new Texture(Gdx.files.internal("buffed_attack.png")));
        effected_attack = new Sprite(new Texture(Gdx.files.internal("effected_attack.png")));
        defend = new Sprite(new Texture(Gdx.files.internal("defend.png")));
        buffed_defend = new Sprite(new Texture(Gdx.files.internal("buffed_defend.png")));
        buff = new Sprite(new Texture(Gdx.files.internal("buff.png")));
        debuff = new Sprite(new Texture(Gdx.files.internal("debuff.png")));
        impact = new Sprite(new Texture(Gdx.files.internal("impact.png")));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (move.getTexture() != null)move.draw(batch);
        if (action != null && (action.getMove() == Move.ATTACK || action.getMove() == Move.BUFFED_ATTACK || action.getMove() == Move.EFFECTED_ATTACK))
            if (((Attack)action).count == 1)
                Fonts.ATTACK_FONT.draw(batch,((Attack)action).damage+"",getX()-getWidth()/4,getY()+getHeight()/4,getWidth(),Align.left,true);
            else
                Fonts.ATTACK_FONT.draw(batch,((Attack)action).damage+"x"+((Attack)action).count,getX()-getWidth()/4,getY()+getHeight()/4,getWidth(),Align.left,true);
        tooltip.draw(batch,parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        move.setBounds(getX(),getY(),getWidth(),getHeight());
        tooltip.act(delta);
        setBounds(getX(),getY(),getWidth(),getHeight());
    }
}
class EffectPanel extends Actor{
    Array<EffectIcon> effectIcons;
    public EffectPanel(float width){
        effectIcons = new Array<>();
        setWidth(width);
    }
    public void addEffect(Effect effect){
        effect.effectIcon = new EffectIcon(effect);
        effectIcons.add(effect.effectIcon);
        Fight.tooltipedActors.addActor(effect.effectIcon);
    }
    public void removeEffect(Effect effect){
       effectIcons.removeValue(effect.effectIcon,true);
        effect.effectIcon.addAction(Actions.removeActor());

    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (Actor actor : effectIcons){
            actor.draw(batch,parentAlpha);
        }
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        int height = 0;
        for (EffectIcon actor : effectIcons){
            int x = effectIcons.indexOf(actor,true)*65 > getWidth() ? effectIcons.indexOf(actor,true)*65 - (int)((effectIcons.size)-(getWidth()/65)) : effectIcons.indexOf(actor,true)*65;
            int y = effectIcons.indexOf(actor,true)*65 > getWidth() ? 60*2 : 60;
            actor.setPosition(getX()+x,getY()+y-65);
            height = Math.max(height,y);
            actor.act(delta);
        }
        setBounds(getX(),getY(),getWidth(),height);
    }

    static class EffectIcon extends Actor{
        Sprite icon;
        Tooltip tooltip;
        Effect effect;
        boolean showMoves = true;
        public EffectIcon(Effect effect){
            this.effect = effect;
            tooltip = new Tooltip(effect.name,effect.description,this);


            if (effect instanceof CureEffect)
                icon = new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\Effects\\Health.png")));
            else if(effect instanceof RadiationEffect)
                icon = new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\Effects\\Poison.png")));
            else if(effect instanceof WeaknessEffect)
                icon = new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\Effects\\Weakness.png")));
            else if(effect instanceof VulnerabilityEffect)
                icon = new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\Effects\\Vulnerability.png")));
            else if(effect instanceof FragilityEffect)
                icon = new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\Effects\\Fragility.png")));
            else if (effect instanceof BleedingEffect)
                icon = new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\Effects\\Bleeding.png")));
            else
                icon = new Sprite(new Texture(Gdx.files.internal("debuff_effect.png")));

            setSize(65,65);
        }
        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            icon.draw(batch);
            if (showMoves)Fonts.ICON_NUMBERS.draw(batch,effect.moves+"",getX()-getWidth()/4,getY()+getHeight()/4,getWidth(),Align.left,true);
            if (tooltip!=null) tooltip.draw(batch,parentAlpha);

        }
        @Override
        public void act(float delta) {
            super.act(delta);
            icon.setColor(getColor());
            icon.setBounds(getX(),getY(),getWidth(),getHeight());
            if (tooltip!=null)tooltip.act(delta);
        }


    }
}