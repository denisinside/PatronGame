package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Tooltip extends Actor {
    Label nameLabel;
    Label descriptionLabel;
    Sprite background;
    private final float MAX_WIDTH = 200;
    private final float PADDING = 30;
    Actor actor;
    public Tooltip(Actor actor){
        this.actor = actor;

    }
    public Tooltip(String name, String description,Actor actor){
        nameLabel = new Label(name,new Label.LabelStyle(Fonts.ALBIONIC_BASIC_NAME, Color.GOLD));
        descriptionLabel = new Label(description,new Label.LabelStyle(Fonts.ALBIONIC_BASIC_NAME, Color.WHITE));
        this.actor = actor;
        background = new Sprite(new Texture(Gdx.files.internal("tooltip_background.png")));
        descriptionLabel.setWrap(true);
        nameLabel.setWrap(true);
        setText(name,description);

        addAction(Actions.alpha(0));

        actor.addCaptureListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                Tooltip.this.addAction(Actions.sequence(
                        Actions.show(),
                        Actions.fadeIn(0.5f)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                Tooltip.this.addAction(Actions.sequence(
                        Actions.fadeOut(0.5f),
                        Actions.hide()
                ));
            }
        });
    }
    public void setText(String name, String description){
        nameLabel.setText(name);
        descriptionLabel.setText(description);

        GlyphLayout layout = new GlyphLayout();
        layout.setText(this.descriptionLabel.getStyle().font, description);
        float tooltipWidth = Math.min(layout.width, MAX_WIDTH)+PADDING;

        descriptionLabel.setWidth(tooltipWidth);
        nameLabel.setWidth(tooltipWidth );

        setSize(tooltipWidth, (float) ((descriptionLabel.getHeight()*1.5+nameLabel.getHeight())*1.5));

        setPosition(actor.getX()-getWidth(), actor.getY());
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        background.setColor(getColor());
        nameLabel.setColor(getColor());
        descriptionLabel.setColor(getColor());
        if (actor.getX() - getWidth() > 0)
            setPosition(actor.getX() - getWidth(), actor.getY());
        else
            setPosition(actor.getX() + actor.getWidth(), actor.getY());
        nameLabel.setPosition(getX(),getY()+getHeight()+nameLabel.getHeight());
        descriptionLabel.setPosition(getX(), (float) (getY()+getHeight()-nameLabel.getHeight()*1.4));
        background.setBounds(getX()-PADDING/2,getY()+PADDING,getWidth()+PADDING,(float) ((descriptionLabel.getHeight()*1.5+nameLabel.getHeight())*1.5)+PADDING);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        background.draw(batch);
        nameLabel.draw(batch,parentAlpha);
        descriptionLabel.draw(batch,parentAlpha);
    }
}
class CardTooltip extends Tooltip{
    CardActor cardActor;
    public CardTooltip(Card card, Actor tooltipedActor) {
        super(tooltipedActor);
        cardActor = card.cardActor;

        cardActor.addAction(Actions.alpha(0));

        actor.addCaptureListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                cardActor.addAction(Actions.sequence(
                        Actions.show(),
                        Actions.fadeIn(0.5f)
                        ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                cardActor.addAction(Actions.sequence(
                        Actions.fadeOut(0.5f),
                        Actions.hide()
                ));
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        cardActor.draw(batch,parentAlpha);
    }

    @Override
    public void act(float delta) {
        if (actor.getX() - getWidth() > 0)
            setPosition(actor.getX() - CardActor.cardWidth, actor.getY());
        else
            setPosition(actor.getX() + actor.getWidth(), actor.getY());
        cardActor.act(delta);
        cardActor.setBounds(getX(),getY(),CardActor.cardWidth,CardActor.cardHeight);
    }
}
