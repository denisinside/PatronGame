package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerActor extends Actor {
    public Enemy enemy;
    private Sprite playerSprite;
    float width, height, x, y;

    public PlayerActor(){
        width = 300;
        height = 300;

        setWidth(width);
        setHeight(height+50);

        // потім шукати буде по клас нейму, але зараз буде 1 картинка
        playerSprite = new Sprite(new Texture(Gdx.files.internal("patron.png")));
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        playerSprite.setBounds(getX(),getY(),getWidth(),getHeight()-50);
        setBounds(getX(), getY(), getWidth(), getHeight());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        playerSprite.draw(batch);
    }
}
