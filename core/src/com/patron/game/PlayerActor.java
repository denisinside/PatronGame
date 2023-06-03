package com.patron.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerActor extends Actor {
    public Player player;
    private Sprite playerSprite;

    public HealthBar healthBar;
    float width, height, x, y;

    public PlayerActor(){
        width = 300;
        height = 300;

        setWidth(width);
        setHeight(height+50);
        player = GameProgress.player;
        healthBar = new HealthBar(getWidth(),20,75);

        // потім шукати буде по клас нейму, але зараз буде 1 картинка
        playerSprite = new Sprite(new Texture(Gdx.files.internal("patron.png")));
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        playerSprite.setBounds(getX(),getY(),getWidth(),getHeight()-50);
        healthBar.setBounds(getX(),getY(),getWidth(),30);
        healthBar.act(delta);
        setBounds(getX(), getY(), getWidth(), getHeight());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        playerSprite.draw(batch);
        healthBar.draw(batch,parentAlpha);
    }
}
