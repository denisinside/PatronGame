package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class TopPanel extends Group {
    Image background, healthIcon,moneyIcon,deckIcon,settingsIcon;
    Label name,health, money,room;
    public TopPanel(){
        background = new Image(new Sprite(new Texture(Gdx.files.internal("top_panel_texture.jpg"))));
        healthIcon = new Image(new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\TopPanel\\Heart.png"))));
        moneyIcon = new Image(new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\TopPanel\\Coin.png"))));
        deckIcon = new Image(new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\TopPanel\\Deck.png"))));
        settingsIcon = new Image(new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\TopPanel\\Gear.png"))));

        name = new Label("PatronRush",new Label.LabelStyle(Fonts.ALBIONIC_LARGE_NAME, Color.LIGHT_GRAY));
        health = new Label("",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.PINK));
        money = new Label("",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.GOLD));
        room = new Label("",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.GOLD));


        addActor(background);
        addActor(healthIcon);
        addActor(moneyIcon);
        addActor(deckIcon);
        addActor(settingsIcon);
        addActor(name);
        addActor(health);
        addActor(money);
        addActor(room);
        healthIcon.setZIndex(10);
        System.out.println(healthIcon.getX());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        health.setText(GameProgress.player.getHealth()+"/"+GameProgress.player.getMaxHealth());
        money.setText(GameProgress.player.money);
        room.setText(GameProgress.room + " кімната");
        background.act(delta);
        healthIcon.act(delta);
        moneyIcon.act(delta);
        deckIcon.act(delta);
        settingsIcon.act(delta);

        name.act(delta);
        health.act(delta);
        money.act(delta);
        room.act(delta);

        setBounds(0,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/15f,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/10f);
        background.setBounds(getX(),getY(),getWidth(),getHeight());
        name.setPosition(getX() + 10, getY() + getHeight()/2 - name.getHeight());
        healthIcon.setBounds(Gdx.graphics.getWidth()/9f, getY()+healthIcon.getHeight()/3, 48,48);
        health.setPosition(healthIcon.getX() + healthIcon.getWidth() + 20, getY() + getHeight()/3 - health.getHeight());
        moneyIcon.setBounds(Gdx.graphics.getWidth()/4f, getY()+moneyIcon.getHeight()/3, 48,48);
        money.setPosition(moneyIcon.getX() + moneyIcon.getWidth() + 20, getY() + getHeight()/3 + money.getHeight());
        room.setPosition(Gdx.graphics.getWidth()/2f-room.getWidth()/2,getY() + getHeight()/3 - room.getHeight());
        settingsIcon.setBounds(Gdx.graphics.getWidth()-settingsIcon.getWidth()*1.5f, getY()+settingsIcon.getHeight()/3, 48,48);
        deckIcon.setBounds(settingsIcon.getX()-settingsIcon.getWidth()*2f, getY()+deckIcon.getHeight()/3, 48,48);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        background.draw(batch,parentAlpha);
        healthIcon.draw(batch,parentAlpha);
        moneyIcon.draw(batch,parentAlpha);
        deckIcon.draw(batch,parentAlpha);
        settingsIcon.draw(batch,parentAlpha);

        name.draw(batch,parentAlpha);
        health.draw(batch,parentAlpha);
        money.draw(batch,parentAlpha);
        room.draw(batch,parentAlpha);
    }
}
