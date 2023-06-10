package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Artefact extends Actor {
    public Sprite icon;
    public String name;
    public String description;
    public Tooltip tooltip;
    public Artefact(String name,String description, String path){
        this.description = description;
        this.name = name;

        icon = new Sprite(new Texture(Gdx.files.internal(path)));
        setSize(100,100);

        tooltip = new Tooltip(name,description,this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        icon.draw(batch);
        tooltip.draw(batch,parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        icon.setBounds(getX(),getY(),getWidth(),getHeight());
        icon.setColor(getColor());
        tooltip.act(delta);
    }
}
