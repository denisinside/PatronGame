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
    private HealthBar healthBar;
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
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        enemySprite.draw(batch);
        healthBar.draw(batch,parentAlpha);
    }
}
class HealthBar extends Actor {
    private float width;
    private float height;
    private float maxValue;
    private float currentValue;
    private final Color backgroundColor;
    private final Color foregroundColor;
    private Color borderColor;
    private int borderWidth;

    private ShapeRenderer shapeRenderer;

    public HealthBar(float width, float height, int maxValue) {
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;

        this.backgroundColor = Color.FIREBRICK;
        this.foregroundColor = Color.RED;
        this.borderColor = Color.BROWN;
        borderWidth = 5;

        shapeRenderer = new ShapeRenderer();
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = (int) MathUtils.clamp(currentValue, 0, maxValue);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(getX(), getY(), width, height);

        shapeRenderer.setColor(foregroundColor);
        float fillWidth =(currentValue / maxValue) * width;
        System.out.println(currentValue + " " + maxValue);
        shapeRenderer.rect(getX(), getY(), fillWidth, height);

        shapeRenderer.setColor(borderColor);
        shapeRenderer.rectLine(getX(), getY(), getX() + width, getY(), borderWidth); // Верхняя граница
        shapeRenderer.rectLine(getX(), getY() + height, getX() + width, getY() + height, borderWidth); // Нижняя граница
        shapeRenderer.rectLine(getX(), getY(), getX(), getY() + height, borderWidth); // Левая граница
        shapeRenderer.rectLine(getX() + width, getY(), getX() + width, getY() + height, borderWidth); // Правая граница

        shapeRenderer.end();

        batch.begin();
        CardActor.fontNameBig.draw(batch,(int)currentValue+"/"+(int)maxValue,getX(),getY()+getHeight()-getHeight()/4,getWidth(), Align.center,true);

    }
}
