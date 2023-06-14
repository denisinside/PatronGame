package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;


public class TopPanel extends Group {
    Image background, healthIcon, moneyIcon, deckIcon, settingsIcon;
    Label name, health, money, room;


    public TopPanel() {
        background = new Image(new Sprite(new Texture(Gdx.files.internal("top_panel_texture.jpg"))));
        healthIcon = new Image(new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\TopPanel\\Heart.png"))));
        moneyIcon = new Image(new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\TopPanel\\Coin.png"))));
        deckIcon = new Image(new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\TopPanel\\Deck.png"))));
        settingsIcon = new Image(new Sprite(new Texture(Gdx.files.internal("icons\\Interface\\TopPanel\\Gear.png"))));

        name = new Label("PatronRush", new Label.LabelStyle(Fonts.ALBIONIC_LARGE_NAME, Color.LIGHT_GRAY));
        health = new Label("", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.PINK));
        money = new Label("", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.GOLD));
        room = new Label("", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.GOLD));


        addActor(background);
        addActor(healthIcon);
        addActor(moneyIcon);
        addActor(deckIcon);
        addActor(settingsIcon);
        addActor(name);
        addActor(health);
        addActor(money);
        addActor(room);

        deckIcon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getStage().addActor(new CardList(GameProgress.playerDeck, getStage()));
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        health.setText(GameProgress.player.getHealth() + "/" + GameProgress.player.getMaxHealth());
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

        setBounds(0, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 15f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 10f);
        background.setBounds(getX(), getY(), getWidth(), getHeight());
        name.setPosition(getX() + 10, getY() + getHeight() / 2 - name.getHeight());
        healthIcon.setBounds(Gdx.graphics.getWidth() / 9f, getY() + healthIcon.getHeight() / 3, 48, 48);
        health.setPosition(healthIcon.getX() + healthIcon.getWidth() + 20, getY() + getHeight() / 3 - health.getHeight());
        moneyIcon.setBounds(Gdx.graphics.getWidth() / 4f, getY() + moneyIcon.getHeight() / 3, 48, 48);
        money.setPosition(moneyIcon.getX() + moneyIcon.getWidth() + 20, getY() + getHeight() / 3 + money.getHeight());
        room.setPosition(Gdx.graphics.getWidth() / 2f - room.getWidth() / 2, getY() + getHeight() / 3 - room.getHeight());
        settingsIcon.setBounds(Gdx.graphics.getWidth() - settingsIcon.getWidth() * 1.5f, getY() + settingsIcon.getHeight() / 3, 48, 48);
        deckIcon.setBounds(settingsIcon.getX() - settingsIcon.getWidth() * 2f, getY() + deckIcon.getHeight() / 3, 48, 48);


        int index = 0;
        for (Artefact artefact : GameProgress.player.artefacts) {
            artefact.setPosition(artefact.getWidth() / 2 + (artefact.getWidth() * 1 + 15) * index, getY() - artefact.getHeight() - 10);
            artefact.act(delta);
            index++;
        }
    }

    public void addElementsWithListeners(Stage stage) {
        for (Artefact artefact : GameProgress.player.artefacts) stage.addActor(artefact);
        stage.addActor(deckIcon);
        stage.addActor(settingsIcon);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        background.draw(batch, parentAlpha);
        healthIcon.draw(batch, parentAlpha);
        moneyIcon.draw(batch, parentAlpha);
        deckIcon.draw(batch, parentAlpha);
        settingsIcon.draw(batch, parentAlpha);

        name.draw(batch, parentAlpha);
        health.draw(batch, parentAlpha);
        money.draw(batch, parentAlpha);
        room.draw(batch, parentAlpha);

        for (Artefact artefact : GameProgress.player.artefacts) artefact.draw(batch, parentAlpha);
    }
}

class CardList extends Group{
    Image background, exitButton;
    ArrayList<Card> cardArray;
    Table actorTable;
    Stage stage;
    private CardSelectionListener cardSelectionListener;
    private ClickListener exitListener;

    public void exitListenerActivate(boolean active){
        if (active) {
            stage.addListener(exitListener);
        }else {
            stage.removeListener(exitListener);
        }
    }

    public void setCardSelectionListener(CardSelectionListener cardSelectionListener) {
        this.cardSelectionListener = cardSelectionListener;
    }

    public CardList(ArrayList<Card> cards, Stage stage) {
        this.stage = stage;
        cardArray = new ArrayList<>(cards);
        for (Card card : cards) card.cardActor.setDraggable(false);
        init();
    }
    public void activateChoice(){
        for (Card card : cardArray){
            card.cardActor.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    cardSelectionListener.onCardSelected(card.cardActor);
                    for (Card card :cardArray){
                        card.cardActor.clearListeners();
                    }
                    CardList.this.exit();
                }
            });
        }
    }

    private void init() {
        background = new Image(new Sprite(new Texture(Gdx.files.internal("black.png"))));
        exitButton = new Image(new Sprite(new Texture(Gdx.files.internal("exit_button.png"))));
        setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        actorTable = new Table();
        tablePack();

        exitListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {


                exitButton.addAction(Actions.removeActor());
                CardList.this.addAction(Actions.removeActor());
                actorTable.addAction(Actions.removeActor());
                stage.addAction(Actions.removeActor(CardList.this));
                stage.removeListener(this);


                for (Card card : cardArray) {
                    card.cardActor.setDraggable(true);
                    if (Fight.inHand != null) for (Card inHand : Fight.inHand) {
                        if (inHand == card) {
                            Fight.cardActors.addActor(card.cardActor);
                            card.cardActor.setPosition(card.cardActor.xPos, card.cardActor.yPos);
                        }
                    }
                }

            }
        };

        stage.addActor(exitButton);
        exitListenerActivate(true);
    }
    public void exit(){
        try {
            exitButton.addAction(Actions.removeActor());
            CardList.this.addAction(Actions.removeActor());
            actorTable.addAction(Actions.removeActor());
            stage.addAction(Actions.removeActor(CardList.this));
            stage.removeListener(exitListener);

            for (Card card : cardArray) card.cardActor.setDraggable(true);
        }catch (Exception ignored){}
    }

    private void tablePack() {
        actorTable.setFillParent(true);
        actorTable.center();
        addActor(actorTable);

        int columnCount = 0;
        if (cardArray != null)
            for (Card card : cardArray) {
                card.cardActor.setSize(CardActor.cardWidth, CardActor.cardHeight);
                actorTable.add(card.cardActor).pad(10);

                columnCount++;
                if (columnCount >= 5) {
                    columnCount = 0;
                    actorTable.row();
                }
            }

        // actorTable.pack();
    }

    @Override
    public void act(float delta) {
        background.setColor(getColor());
        background.setBounds(getX(), getY(), getWidth(), getHeight());
        exitButton.setColor(getColor());
        exitButton.setBounds(getWidth() - exitButton.getWidth(), getHeight() - exitButton.getHeight(), 100, 100);

        actorTable.setColor(getColor());
        actorTable.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.draw(batch, parentAlpha);
        exitButton.draw(batch, parentAlpha);
        actorTable.draw(batch, parentAlpha);
    }
}
interface CardSelectionListener {
    void onCardSelected(CardActor selectedCard);
}
