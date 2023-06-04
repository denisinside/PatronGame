package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class CardActor extends Actor {
    public static BitmapFont fontNameBasic, fontDescriptionBasic;
    public static BitmapFont fontNameBig, fontDescriptionBig;
    public Card card;
    public int number;
    boolean selected = false;
    EnergyActor energyActor;
    public static float cardWidth = 150,cardHeight = 225;
    public float xPos, yPos, offsetX = 4, offsetY = (float) (4);
    private Sprite cardTemplateTexture, cardImageTexture;
    private Rectangle textBounds;


    public CardActor(Card card, float x, float y) {
        super();

        this.card = card;

        cardTemplateTexture = new Sprite(new Texture(Gdx.files.internal("TALANT.png")));
        cardImageTexture = new Sprite(new Texture(Gdx.files.internal("example.jpg")));
        energyActor = new EnergyActor(card.cost, 100, 100);

        setBounds(x, y, cardWidth, cardHeight);
        xPos = x;
        yPos = y;
        setDraggable(true);

    }
    public void afterUsing(){
        Fight.player.setEnergy( Fight.player.getEnergy() - card.getCostNow());
        Fight.playerEnergy.setEnergyAmount(Fight.player.getEnergy());
        if (card.burning) Fight.inHand.remove(card);
        else if (card.disposable) {
            Fight.inHand.remove(card);
            GameProgress.playerDeck.remove(card);
        } else {
            Fight.discard.add(card);
            Fight.inHand.remove(card);
        }
        addAction(Actions.sequence(
                Actions.moveTo(Gdx.graphics.getWidth()+getWidth(),-getHeight(),0.6f),
                Actions.run(() -> {
                    Fight.cardActors.removeActor(CardActor.this);
                    Fight.centerCardDeck();
                })
        ));

    }

    public static void setFonts() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Albionic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 18;
        fontParameter.characters = "0123456789()+-=/*!?,.АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯЇїІіЄєабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
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

    public void select() {
        if (selected) {
            selected = false;
            addAction(Actions.sizeTo(cardWidth, cardHeight, 0.5f));
            setZIndex(2);
        } else {
            selected = true;
            setZIndex(15);
            addAction(Actions.sizeTo((float) (cardWidth*1.5), (float) (cardHeight*1.5), 0.5f));
            // я єбав (тут треба зробити, щоб інші селектід картки були вже не селектід)
            //upd ура зробив (залишу для історії)
            for (Actor cardActor : Fight.cardActors.getChildren()) {
                if (cardActor instanceof CardActor && cardActor != CardActor.this) {
                    if (((CardActor) cardActor).selected) ((CardActor) cardActor).select();

                }
            }
        }
    }
    public void setDraggable(boolean isDraggable){
        clearListeners();
        if (isDraggable)
            addListener(new InputListener() {
                private boolean isDragging = false;
                private float dragOffsetX, dragOffsetY;

                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.LEFT) {
                        dragOffsetX = x;
                        dragOffsetY = y;
                        isDragging = false;
                        setZIndex(15);

                        for (Actor cardActor : Fight.cardActors.getChildren()) {
                            if (cardActor instanceof CardActor && cardActor != CardActor.this) {
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
                        Rectangle cardBounds = new Rectangle(CardActor.this.getX(), CardActor.this.getY(), CardActor.this.getWidth(), CardActor.this.getHeight());
                        boolean found = false;
                        for (Actor actor : Fight.enemiesActors.getChildren()) {
                            Rectangle clickedActorBounds = new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
                            if (Intersector.intersectRectangles(cardBounds, clickedActorBounds, new Rectangle())) {
                                if (card.getCostNow() <= Fight.player.getEnergy() && card.target != null && card.playable) {
                                    found = true;
                                    switch (card.target){
                                        case "one":
                                            card.use(((EnemyActor) actor).enemy,null);
                                            afterUsing();
                                            break;
                                        case "random":
                                        case "all":
                                            card.use(null,Fight.enemies);
                                            afterUsing();
                                            break;
                                        default: found = false;
                                    }
                                    break;
                                }
                            }
                        }
                        if (!found){
                            Rectangle clickedActorBounds = new Rectangle(Fight.player.actor.getX(), Fight.player.actor.getY(), Fight.player.actor.getWidth(), Fight.player.actor.getHeight());
                            if (Intersector.intersectRectangles(cardBounds, clickedActorBounds, new Rectangle())) {
                                if (card.getCostNow() <= Fight.player.getEnergy() && card.target != null && card.playable) {
                                    found = true;
                                    switch (card.target){
                                        case "player":
                                            card.use(null,null);
                                            afterUsing();
                                            break;
                                    }
                                }
                            }
                        }
                        if (!found) addAction(Actions.moveTo(xPos, yPos, 0.5f));
                    }
                    isDragging = false;
                }
            });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        offsetX = (cardWidth/8) * getWidth() / cardWidth;
        offsetY = (float) ((cardHeight/2.6) * getHeight() / cardHeight);
        cardImageTexture.setBounds(getX() + offsetX, getY() + offsetY, 150 * getWidth() / 200, 150 * getHeight() / 300);
        cardTemplateTexture.setBounds(getX(), getY(), getWidth(), getHeight());
        energyActor.setBounds(getX(), getY() + getHeight() - energyActor.getHeight(), 75 * getWidth() / 200, 75 * getHeight() / 300);
        energyActor.act(delta);

        setBounds(getX(), getY(), getWidth(), getHeight());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // картинка картки

        cardImageTexture.draw(batch);
        // шаблон картки


        cardTemplateTexture.draw(batch);

        // назва, опис картки
        textBounds = new Rectangle(getX() + offsetX, (float) (getY() + offsetY * 0.95), 150 * getWidth() / cardWidth, 30);

        if (getWidth() >= cardWidth*1.5)
            fontNameBig.draw(batch, card.getName(), textBounds.x, textBounds.y, textBounds.width, Align.center, true);
        else
            fontNameBasic.draw(batch, card.getName(), textBounds.x, textBounds.y, textBounds.width, Align.center, true);

        textBounds = new Rectangle((float) (getX() + offsetX * 0.9), (float) (getY() + offsetY * 0.7), 160 * getWidth() / cardWidth, 0);
        if (getWidth() >= cardWidth*1.5)
            fontDescriptionBig.draw(batch, card.getActualDescription(), textBounds.x, textBounds.y, textBounds.width, Align.center, true);
        else
            fontDescriptionBasic.draw(batch, card.getActualDescription(), textBounds.x, textBounds.y, textBounds.width, Align.center, true);


        energyActor.draw(batch, parentAlpha);
    }

    public void setPos(float x, float y) {
        setPosition(x, y);
        xPos = x;
        yPos = y;
    }

}

class EnergyActor extends Actor {
    int energyAmount;
    Sprite energyIcon;
    BitmapFont font;

    public EnergyActor(int energyAmount, int width, int height) {
        this.energyAmount = energyAmount;
        energyIcon = new Sprite(new Texture(Gdx.files.internal("energy.png")));
        setWidth(width);
        setHeight(height);

        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 18;
        fontParameter.borderWidth = 2;
        font = new FreeTypeFontGenerator(Gdx.files.internal("Albionic.ttf")).generateFont(fontParameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void setEnergyAmount(int energyAmount) {
        this.energyAmount = energyAmount;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        energyIcon.draw(batch);

        font.getData().setScale(getWidth()/50);
        font.draw(batch, energyAmount + "", getX() + getWidth() / 5, getY() + getHeight() - getHeight() / 7, getWidth(), Align.center, true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        energyIcon.setBounds(getX(), getY(), getWidth(), getHeight());
        setBounds(getX(), getY(), getWidth(), getHeight());

    }
}

