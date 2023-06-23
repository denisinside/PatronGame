package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;



public class CardActor extends Actor {
    public static float cardWidth = 150, cardHeight = 225;
    public BitmapFont fontName, fontDescription;
    public Card card;
    public int number;
    public float xPos, yPos, offsetX = 4, offsetY = (float) (4);
    boolean selected = false;
    public EnergyActor energyActor;
    private Sprite cardTemplateTexture, cardImageTexture;
    private Label name,description;


    public CardActor(Card card, float x, float y) {
        super();

        this.card = card;

        if (card instanceof AttackCard)
            cardTemplateTexture = new Sprite(new Texture(Gdx.files.internal("ATTACK_CARD.png")));
        else if (card instanceof SkillCard)
            cardTemplateTexture = new Sprite(new Texture(Gdx.files.internal("SKILL.png")));
        else if (card instanceof CurseCard)
            cardTemplateTexture = new Sprite(new Texture(Gdx.files.internal("CURSE.png")));
        else if (card instanceof StatusCard)
            cardTemplateTexture = new Sprite(new Texture(Gdx.files.internal("STATUS.png")));
       // if (card instanceof TalantCard)
       //     cardTemplateTexture = new Sprite(new Texture(Gdx.files.internal("TALANT.png")));

        if(card.name.equals("Удар лапою"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Arm_Attack.png")));
        else if(card.name.equals("Бронежилет"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Bulletproof.png")));
        else if(card.name.equals("Шалений хвіст"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Crazy_tail.png")));
        else if(card.name.equals("Банка огірків"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Cucumber_banka.png")));
        else if(card.name.equals("Бинт та ліки"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Bandage_cure.png")));
        else if(card.name.equals("Смаколик"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Sweety.png")));
        else if(card.name.equals("Укус"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Bite.png")));
        else if(card.name.equals("Різанина"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Butcher.jpg")));
        else if(card.name.equals("Собаче панування"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Domination.png")));
        else if(card.name.equals("Айкідо"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Aikido.jpg")));
        else if(card.name.equals("Відчайдушний удар"))
            cardImageTexture = new Sprite(new Texture(Gdx.files.internal("assets/icons/Cards/Reckless_attack.jpg")));
        else cardImageTexture = new Sprite(new Texture(Gdx.files.internal("example.jpg")));

        energyActor = new EnergyActor(card.cost, 100, 100);

        setBounds(x, y, cardWidth, cardHeight);
        xPos = x;
        yPos = y;
        setDraggable(true);

        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = Fonts.characters;
        fontParameter.size = 14;
        fontParameter.borderWidth = 2;
        fontName = new FreeTypeFontGenerator(Gdx.files.internal("Albionic.ttf")).generateFont(fontParameter);
        fontName.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        fontParameter.size = 14;
        fontDescription = new FreeTypeFontGenerator(Gdx.files.internal("Albionic.ttf")).generateFont(fontParameter);
        fontDescription.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        name = new Label(card.getName(),new Label.LabelStyle(fontName, Color.WHITE));
        name.setAlignment(Align.center);
        name.setWrap(false);

        description = new Label(card.getActualDescription(),new Label.LabelStyle(fontDescription, Color.WHITE));
        description.setAlignment(Align.center);
        description.setWrap(true);
    }

    private float calculateFontSize(Label label,String text) {
        GlyphLayout layout = new GlyphLayout();
        float fontSize = 1.5f;

        while (fontSize > 0) {
            label.setScale(fontSize);
            label.getStyle().font.getData().setScale(fontSize);
            layout.setText(label.getStyle().font, text);

            if (layout.width <= getWidth()*0.8 && layout.height <= getHeight()*0.08) {
                return fontSize;
            }
            fontSize -= 0.1;
        }
        return 0.01f;
    }
    public static void setFonts() {
    }

    public void afterUsing() {
        Fight.player.setEnergy(Fight.player.getEnergy() - card.getCostNow());
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
                Actions.moveTo(Gdx.graphics.getWidth() + getWidth(), -getHeight(), 0.6f),
                Actions.run(() -> {
                    Fight.cardActors.removeActor(CardActor.this);
                    Fight.centerCardDeck();
                })
        ));
        Fight.checkKill();

    }

    public void select() {
        if (selected) {
            selected = false;
            addAction(Actions.sizeTo(cardWidth, cardHeight, 0.5f));
            setZIndex(2);
        } else {
            selected = true;
            setZIndex(15);
            addAction(Actions.sizeTo((float) (cardWidth * 1.5), (float) (cardHeight * 1.5), 0.5f));

            for (Actor cardActor : Fight.cardActors.getChildren()) {
                if (cardActor instanceof CardActor && cardActor != CardActor.this) {
                    if (((CardActor) cardActor).selected) ((CardActor) cardActor).select();

                }
            }
        }
    }

    public void setDraggable(boolean isDraggable) {
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
                        if (getWidth() != cardWidth) addAction(Actions.sizeTo(cardWidth, cardHeight, 0.5f));
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
                        checkUsing();
                    }
                    isDragging = false;
                }
            });
    }

    public void checkUsing() {
        Rectangle cardBounds = new Rectangle(CardActor.this.getX(), CardActor.this.getY(), CardActor.this.getWidth(), CardActor.this.getHeight());
        boolean found = false;
        for (Actor actor : Fight.enemiesActors.getChildren()) {
            Rectangle clickedActorBounds = new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
            if (Intersector.intersectRectangles(cardBounds, clickedActorBounds, new Rectangle())) {
                if (card.getCostNow() <= Fight.player.getEnergy() && card.target != null && card.playable) {
                    found = true;
                    switch (card.target) {
                        case "one":
                            card.use(((EnemyActor) actor).enemy, null);
                            afterUsing();
                            break;
                        case "random":
                        case "all":
                            card.use(null, Fight.enemies);
                            afterUsing();
                            break;
                        default:
                            found = false;
                    }
                    break;
                }
            }
        }
        if (!found) {
            Rectangle clickedActorBounds = new Rectangle(Fight.player.actor.getX(), Fight.player.actor.getY(), Fight.player.actor.getWidth(), Fight.player.actor.getHeight());
            if (Intersector.intersectRectangles(cardBounds, clickedActorBounds, new Rectangle())) {
                if (card.getCostNow() <= Fight.player.getEnergy() && card.target != null && card.playable) {
                    found = true;
                    switch (card.target) {
                        case "player":
                            card.use(null, null);
                            afterUsing();
                            break;
                        default:
                            found = false;
                    }
                }
            }
        }
        if (!found) addAction(Actions.moveTo(xPos, yPos, 0.5f));
        selected = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        energyActor.setColor(getColor());
        cardImageTexture.setColor(getColor());
        cardTemplateTexture.setColor(getColor());
        name.setColor(getColor());
        description.setColor(getColor());

        offsetX = (cardWidth / 8) * getWidth() / cardWidth;
        offsetY = (float) ((cardHeight / 2.6) * getHeight() / cardHeight);
        cardImageTexture.setBounds(getX() + offsetX, getY() + offsetY, 150 * getWidth() / 200, 150 * getHeight() / 300);
        cardTemplateTexture.setBounds(getX(), getY(), getWidth(), getHeight());
        energyActor.setBounds(getX(), getY() + getHeight() - energyActor.getHeight(), 75 * getWidth() / 200, 75 * getHeight() / 300);
        energyActor.act(delta);

        setBounds(getX(), getY(), getWidth(), getHeight());
        name.setBounds((float) (getX() + offsetX*0.3), (float) (getY() + offsetY * 0.9 - getHeight()*0.05f), (float) ((cardWidth - offsetX*0.2 )* getWidth() / cardWidth), getHeight()*0.1f);
        description.setBounds((float) (getX() + offsetX * 0.3), (float) (getY() + offsetY*0.1), (float) ((cardWidth - offsetX*0.2 )* getWidth() / cardWidth), getHeight()*0.3f);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // картинка картки

        cardImageTexture.draw(batch);
        // шаблон картки


        cardTemplateTexture.draw(batch);

        // назва, опис картки
        name.setScale(calculateFontSize(name,card.getName()));
        name.draw(batch,parentAlpha);

        description.setScale(calculateFontSize(name,card.getActualDescription()));
        description.setText(card.getActualDescription());
        description.draw(batch,parentAlpha);

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
    Sprite numberIcon;
    BitmapFont font;
    Label cost;

    public EnergyActor(int energyAmount, int width, int height) {
        this.energyAmount = energyAmount;
        energyIcon = new Sprite(new Texture(Gdx.files.internal("energy.png")));
        setWidth(width);
        setHeight(height);

        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 30;
        fontParameter.borderWidth = 2;
        font = new FreeTypeFontGenerator(Gdx.files.internal("Albionic.ttf")).generateFont(fontParameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        cost = new Label(energyAmount+"",new Label.LabelStyle(font,Color.WHITE));
        cost.setAlignment(Align.center);
    }

    public void setEnergyAmount(int energyAmount) {
        this.energyAmount = energyAmount;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        energyIcon.draw(batch);

        font.getData().setScale(getWidth() / 80);
        cost.draw(batch,parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        energyIcon.setColor(getColor());
        cost.setColor(getColor());
        energyIcon.setBounds(getX(), getY(), getWidth(), getHeight());
        cost.setBounds(getX() + getWidth() / 5, getY() + getHeight()*0.75f, getWidth(),0);
        cost.act(delta);
        cost.setText(energyAmount);
        setBounds(getX(), getY(), getWidth(), getHeight());

    }
}

class NextMoveButton extends Actor {
    Sprite button;
    String text;
    public int buttonWidth = 300,buttonHeight = 100;
    public float positionX = Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 3f, positionY = Gdx.graphics.getHeight() / 5f;

    public NextMoveButton(String text) {
        this.text = text;
        setBounds(positionX, positionY, buttonWidth, buttonHeight);
        button = new Sprite(new Texture(Gdx.files.internal("nextmovebutton.png")));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        button.draw(batch);
        Fonts.ALBIONIC_LARGE_NAME.draw(batch, text, positionX, (float) (positionY + buttonHeight / 1.5), buttonWidth, Align.center, true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        button.setColor(getColor());
        setBounds(positionX, positionY, buttonWidth, buttonHeight);
        button.setBounds(positionX, positionY, buttonWidth, buttonHeight);
    }
}

