package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class CardActor extends Actor {
    public Card card;
    private Sprite cardTemplateTexture;
    private Sprite cardImageTexture;
    private static BitmapFont fontNameBasic, fontDescriptionBasic;
    private static BitmapFont fontNameBig, fontDescriptionBig;
    private  Rectangle textBounds;
    boolean selected = false;
    public int number;

    float xPos, yPos, offsetX = 25,offsetY = 115;

    public CardActor(Card card, float x, float y) {
        super();
        this.card = card;
        init();


        // Установите размеры и позицию вашего актера в соответствии с размерами карты
        setBounds(x,y,200,300);
        this.xPos = x;
        this.yPos = y;

        addListener(new InputListener() {
            private boolean isDragging = false;
            private float dragOffsetX, dragOffsetY;
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    dragOffsetX = x;
                    dragOffsetY = y;
                    isDragging = false;
                    setZIndex(99);

// я єбав (тут треба зробити, щоб інші селектід картки були вже не селектід)
                    //upd ура зробив (залишу для історії)
                    for (Actor cardActor : Fight.cardActors.getChildren()){
                        if (cardActor instanceof CardActor &&  cardActor != CardActor.this) {
                            if (((CardActor) cardActor).selected) ((CardActor) cardActor).select();

                        }
                    }

                    return true;
                }
                return false;
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!isDragging && Math.abs(x - dragOffsetX) > 10 || Math.abs(y - dragOffsetY) > 10) {
                    isDragging = true;
                    clearActions();
                }

                if (isDragging) {
                    float newX = event.getStageX() - dragOffsetX;
                    float newY = event.getStageY() - dragOffsetY;
                    setPosition(newX, newY);
                }
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!isDragging) {
                    // пропрацювання кліка
                    select();

                } else {
                    // тут вже треба потім перевірки зробити куди воно приземлюється
                    addAction(Actions.moveTo(xPos, yPos, 0.5f));
                }
                isDragging = false;
            }
        });
    }
    public void select(){
        if (selected) {
            selected = false;
            addAction(Actions.sizeTo(200, 300, 0.5f));
            setZIndex(2);
        } else {
            selected = true;
            setZIndex(99);
            addAction(Actions.sizeTo(300, 450, 0.5f));
            // я єбав (тут треба зробити, щоб інші селектід картки були вже не селектід)
            //upd ура зробив (залишу для історії)
            for (Actor cardActor : Fight.cardActors.getChildren()){
                if (cardActor instanceof CardActor &&  cardActor != CardActor.this) {
                    if (((CardActor) cardActor).selected) ((CardActor) cardActor).select();

                }
            }
        }
    }

    private void init() {
        Texture cardTemplateTexture = new Texture(Gdx.files.internal("TALANT.png"));
        Texture cardImageTexture = new Texture(Gdx.files.internal("example.jpg"));

        this.cardTemplateTexture = new Sprite(cardTemplateTexture);
        this.cardImageTexture = new Sprite(cardImageTexture);


    }
    public static void setFonts(){
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Albionic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 18;
        fontParameter.characters = "123456789()+-=*!?,.АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯЇїІіЄєабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        fontParameter.borderWidth = 2;
        fontNameBasic = fontGenerator.generateFont(fontParameter);


        fontParameter.borderWidth = 3;
        fontParameter.size = 24;
        fontNameBig = fontGenerator.generateFont(fontParameter);

        fontParameter.borderWidth = 1;
        fontParameter.size = 14;
        fontDescriptionBasic = fontGenerator.generateFont(fontParameter);

        fontParameter.borderWidth = 2;
        fontParameter.size = 18;
        fontDescriptionBig = fontGenerator.generateFont(fontParameter);


        fontGenerator.dispose();

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        offsetX = 25*getWidth()/200;
        offsetY = 115*getHeight()/300;
        cardImageTexture.setBounds(getX()+ offsetX,getY() + offsetY,150*getWidth()/200,150*getHeight()/300);
        cardTemplateTexture.setBounds(getX(),getY(),getWidth(),getHeight());

        setBounds(getX(), getY(), getWidth(), getHeight());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // картинка картки

        cardImageTexture.draw(batch);
        // шаблон картки


        cardTemplateTexture.draw(batch);

        // назва, опис картки
         textBounds = new Rectangle(getX() + offsetX, (float) (getY() + offsetY*0.95), 150*getWidth()/200, 30);

        if (getWidth() >= 250) fontNameBig.draw(batch, card.getName(), textBounds.x, textBounds.y, textBounds.width, Align.center, true);
        else fontNameBasic.draw(batch, card.getName(), textBounds.x, textBounds.y, textBounds.width, Align.center, true);

        textBounds = new Rectangle((float) (getX() + offsetX*0.9), (float) (getY() + offsetY*0.7), 160*getWidth()/200, 0);
        if (getWidth() >= 250) fontDescriptionBig.draw(batch, card.getActualDescription(), textBounds.x, textBounds.y, textBounds.width, Align.center, true);
        else fontDescriptionBasic.draw(batch, card.getActualDescription(), textBounds.x, textBounds.y, textBounds.width, Align.center, true);


    }
    public void setPos(float x,float y){
        setPosition(x,y);
        xPos = x;
        yPos = y;
    }

}

