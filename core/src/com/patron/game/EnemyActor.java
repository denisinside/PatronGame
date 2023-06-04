package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

public class EnemyActor extends Actor {
    public Enemy enemy;
    private Sprite enemySprite;
    public HealthBar healthBar;
    float width, height, x, y;

    public EnemyActor(float width, float height, Enemy enemy){
        this.width = width;
        this.height = height;
        this.enemy = enemy;

        setWidth(width);
        setHeight(height+50);
        healthBar = new HealthBar(getWidth(),20,enemy.maxHealth);

        // потім шукати буде по клас нейму, але зараз буде 1 картинка
        enemySprite = new Sprite(new Texture(Gdx.files.internal("bandit.png")));
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        enemySprite.setBounds(getX(),getY()+50,getWidth(),getHeight()-50);
        healthBar.setBounds(getX(),getY(),getWidth(),30);
        setBounds(getX(), getY(), getWidth(), getHeight());

        healthBar.setCurrentValue(enemy.health);
        healthBar.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        enemySprite.draw(batch);
        healthBar.draw(batch,parentAlpha);
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
        CardActor.fontNameBig.draw(batch,((int)currentValue)+"/"+((int)maxValue),getX(),getY()+getHeight()-getHeight()/4,getWidth(), Align.center,true);
        if (showArmor) {
            armorIcon.draw(batch);
            CardActor.fontNameBig.draw(batch,armor+"",getX()-30,getY()+getHeight()/2,60, Align.center,true);
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
