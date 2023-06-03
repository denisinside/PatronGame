package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class EnemyActor extends Actor {
    public Enemy enemy;
    private Sprite enemySprite;
    float width, height, x, y;

    public EnemyActor(float width, float height, Enemy enemy){
        this.width = width;
        this.height = height;
        this.enemy = enemy;

        setWidth(width);
        setHeight(height+50);

        // потім шукати буде по клас нейму, але зараз буде 1 картинка
        enemySprite = new Sprite(new Texture(Gdx.files.internal("bandit.png")));
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        enemySprite.setBounds(getX(),getY()+50,getWidth(),getHeight()-50);
        setBounds(getX(), getY(), getWidth(), getHeight());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        enemySprite.draw(batch);
    }
}
