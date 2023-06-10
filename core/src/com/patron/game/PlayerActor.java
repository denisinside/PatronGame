package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class PlayerActor extends Actor {
    public Player player;
    private Sprite playerSprite;
    public HealthBar healthBar;
    public EffectPanel effectPanel;
    float width, height, x, y;
    Array<Label> valuesDisplay;
    Array<Actor> otherValues;

    public PlayerActor(){
        width = 300;
        height = 300;
        valuesDisplay = new Array<>();
        otherValues = new Array<>();

        setWidth(width);
        setHeight(height+50);
        player = GameProgress.player;
        healthBar = new HealthBar(getWidth(),20,75);

        effectPanel = new EffectPanel(getWidth());

        playerSprite = new Sprite(new Texture(Gdx.files.internal("Patron_Idle.png")));
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
    public void addValue(int value, Color color, EnemyActor.valueType valueType){
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
        playerSprite.setBounds(getX(),getY(),getWidth(),getHeight()-50);
        healthBar.setBounds(getX(),getY(),getWidth(),30);
        healthBar.act(delta);

        effectPanel.setPosition(getX(),getY()- effectPanel.getHeight());
        effectPanel.act(delta);

        setBounds(getX(), getY(), getWidth(), getHeight());

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
    public void draw(Batch batch, float parentAlpha) {
        playerSprite.draw(batch);
        healthBar.draw(batch,parentAlpha);
        effectPanel.draw(batch,parentAlpha);
        for (Label label : valuesDisplay){
            label.draw(batch,parentAlpha);
        }
        for (Actor actor : otherValues){
            actor.draw(batch,parentAlpha);
        }
    }
}
