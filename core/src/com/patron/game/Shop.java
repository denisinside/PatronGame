package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;

import static com.patron.game.GameProgress.*;

public class Shop implements Screen {
    static int removeCardCost = 50;
    public boolean isEventShop = false;
    Image trader, background, windowBackground;
    Stage stage;
    Array<Card> cards;
    HashMap<Card, Integer> cardPrice;
    Array<Artefact> artefacts;
    HashMap<Artefact, Integer> artefactPrice;
    Label[] cardLabels;
    Label[] artefactLabels;
    Label name;
    Group tradeWindow;
    NextMoveButton nextMoveButton;

    public Shop() {
        background = new Image(new Sprite(getRandomBackground()));
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void addName() {
        trader.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                name.addAction(Actions.fadeIn(0.3f));
                name.setBounds(trader.getX(), trader.getY() - 15, trader.getWidth(), 10);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                name.addAction(Actions.fadeOut(0.3f));
                name.setBounds(trader.getX(), trader.getY() - 15, trader.getWidth(), 10);
            }
        });
    }

    private void openTradeWindowListener() {
        addName();
        trader.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tradeWindow.addAction(Actions.show());
                nextMoveButton.addAction(Actions.hide());

                trader.setPosition(windowBackground.getX() + windowBackground.getWidth() + 50, trader.getY());
                trader.clearListeners();
                addName();
                trader.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameSound.buttonSound.setVolume(GameSound.buttonSound.play(), GameSound.soundVolume);
                        tradeWindow.addAction(Actions.hide());
                        nextMoveButton.addAction(Actions.show());
                        trader.clearListeners();
                        trader.setBounds(Gdx.graphics.getWidth() / 2f, player.actor.getY(), 180, 300);
                        openTradeWindowListener();
                    }
                });
            }
        });
    }

    private void createTradeWindow() {
        tradeWindow = new Group();
        windowBackground = new Image(new Sprite(new Texture(Gdx.files.internal("trade_window_bg.png"))));
        windowBackground.setSize(1500, 800);
        windowBackground.setPosition(Gdx.graphics.getWidth() / 20f, Gdx.graphics.getHeight() / 8f);
        tradeWindow.addActor(windowBackground);


        int indexX = 0;
        int indexY = 0;
        int index = 0;
        for (Card card : cards) {
            tradeWindow.addActor(card.cardActor);
            card.cardActor.setBounds((indexX * CardActor.cardWidth * 1.4f * 1.2f + 80 + windowBackground.getX()),
                    indexY * CardActor.cardHeight * 1.4f + indexY * 30 + 80 + windowBackground.getY(),
                    CardActor.cardWidth * 1.4f, CardActor.cardHeight * 1.4f);
            cardLabels[index].setBounds(card.cardActor.getX(), card.cardActor.getY() - 15, card.cardActor.getWidth(), 10);
            cardLabels[index].setAlignment(Align.center);
            tradeWindow.addActor(cardLabels[index]);
            index++;
            if (index % 2 == 1) {
                indexY = 1;
            } else {
                indexY = 0;
            }
            if (index % 2 == 0) indexX++;
        }

        indexX = 0;
        for (Artefact artefact : artefacts) {
            tradeWindow.addActor(artefact);
            artefact.setSize(128, 128);
            artefact.setPosition((windowBackground.getWidth() * 0.55f + indexX * artefact.getWidth() + 50 + windowBackground.getX()),
                    windowBackground.getHeight() * 0.7f + windowBackground.getY());
            artefactLabels[indexX].setBounds(artefact.getX(), artefact.getY() - 15, artefact.getWidth(), 10);
            artefactLabels[indexX].setAlignment(Align.center);
            tradeWindow.addActor(artefactLabels[indexX]);
            indexX++;
        }
        Image removeCard = new Image(new Sprite(new Texture(Gdx.files.internal("remove_card.png"))));
        Label removeCardLabel = new Label(removeCardCost + "", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.GOLD));

        removeCard.setBounds(windowBackground.getWidth() * 0.55f + 50 + windowBackground.getX(),
                windowBackground.getHeight() * 0.1f + windowBackground.getY(),
                500, 400);
        removeCardLabel.setBounds(removeCard.getX() + removeCard.getWidth() * 0.75f,
                removeCard.getY() + removeCard.getHeight() * 0.4f, removeCard.getWidth() * 0.25f, 10);

        tradeWindow.addActor(removeCard);
        tradeWindow.addActor(removeCardLabel);
        removeCard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.money >= removeCardCost) {
                    player.money -= removeCardCost;

                    removeCardLabel.addAction(Actions.removeActor());
                    removeCardCost += 30;
                    removeCard.addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.fadeOut(0.5f),
                                    Actions.moveTo(removeCard.getX() - (removeCard.getWidth() / 10 - removeCard.getWidth()) / 2, removeCard.getY() - (removeCard.getWidth() / 10 - removeCard.getWidth()) / 2, 2f, Interpolation.fastSlow),
                                    Actions.sizeTo(removeCard.getWidth() / 10, removeCard.getWidth() / 10, 2f, Interpolation.smooth)
                            ),
                            Actions.run(() -> {
                                removeCard.addAction(Actions.removeActor());
                                CardList cardList = new CardList(GameProgress.playerDeck, stage);
                                cardList.activateChoice();
                                cardList.exitListenerActivate(false);
                                cardList.setCardSelectionListener(selectedCard -> {
                                    GameProgress.playerDeck.remove(selectedCard.card);
                                });
                                stage.addActor(cardList);
                            })
                    ));
                }
            }
        });

    }


    private void generateStock() {
        cards = new Array<>();
        artefacts = new Array<>();
        cardPrice = new HashMap<>();
        artefactPrice = new HashMap<>();

        ArrayList<Card> cardStock = new ArrayList<>(GameProgress.allCards);
        ArrayList<Artefact> artefactStock = new ArrayList<>(GameProgress.allArtefacts);
        artefactStock.removeIf(artefact -> artefact.name.contains("злиток"));


        try {
            for (int i = 0; i < 6; i++) {
                cards.add((Card) cardStock.remove(MathUtils.random(cardStock.size() - 1)).clone());
                cards.get(i).cardActor = new CardActor(cards.get(i), 100, 100);
                cardPrice.put(cards.get(i), MathUtils.random(35, 100));
                cardLabels[cards.indexOf(cards.get(i), true)] = new Label(cardPrice.get(cards.get(i)) + "", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.GOLD));
            }
        } catch (CloneNotSupportedException ignored) {
        }
        for (int i = 0; i < 4; i++) {
            if (artefactStock.size() != 0) {
                artefacts.add(artefactStock.remove(MathUtils.random(artefactStock.size() - 1)));
                artefactPrice.put(artefacts.get(i), MathUtils.random(100, 180));
                artefactLabels[artefacts.indexOf(artefacts.get(i), true)] = new Label(artefactPrice.get(artefacts.get(i)) + "", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.GOLD));
            }
        }

        for (Card card : cards) {
            card.cardActor.setDraggable(false);
            card.cardActor.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (player.money >= cardPrice.get(card)) {
                        GameSound.buySound.setVolume(GameSound.buySound.play(), GameSound.soundVolume);
                        player.money -= cardPrice.get(card);

                        cardLabels[cards.indexOf(card, true)].addAction(Actions.removeActor());
                        card.cardActor.addAction(Actions.sequence(
                                Actions.parallel(
                                        Actions.fadeOut(0.5f),
                                        Actions.moveTo(card.cardActor.getX() - (card.cardActor.getWidth() / 10 - card.cardActor.getWidth()) / 2, card.cardActor.getY() - (card.cardActor.getWidth() / 10 - card.cardActor.getWidth()) / 2, 2f, Interpolation.fastSlow),
                                        Actions.sizeTo(card.cardActor.getWidth() / 10, card.cardActor.getWidth() / 10, 2f, Interpolation.smooth)
                                ),
                                Actions.fadeIn(0.01f),
                                Actions.run(() -> {
                                    card.cardActor.addAction(Actions.removeActor());
                                    card.cardActor.setDraggable(true);
                                    playerDeck.add(card);
                                })
                        ));
                    }
                }
            });
        }

        for (Artefact artefact : artefacts) {
            artefact.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (player.money >= artefactPrice.get(artefact)) {
                        GameSound.buySound.setVolume(GameSound.buySound.play(), GameSound.soundVolume);
                        allArtefacts.removeIf(artefact1 -> artefact1.name.equals(artefact.name));
                        player.money -= artefactPrice.get(artefact);
                        artefactLabels[artefacts.indexOf(artefact, true)].addAction(Actions.removeActor());
                        artefact.addAction(Actions.sequence(
                                Actions.fadeOut(0.5f),
                                Actions.fadeIn(0.01f),
                                Actions.run(() -> {
                                    player.addArtefact(artefact);
                                })
                        ));
                    }
                }
            });
        }

    }

    @Override
    public void show() {

        GameSound.shopSound.setVolume(GameSound.shopSound.play(), GameSound.soundVolume);


        trader = new Image(new Sprite(new Texture(Gdx.files.internal("NPC\\Rat_Trader.png"))));
        nextMoveButton = new NextMoveButton("Йти далі");
        cardLabels = new Label[6];
        artefactLabels = new Label[4];

        stage = new Stage();
        stage.addActor(this.background);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(GameProgress.topPanel);
        GameProgress.topPanel.addElementsWithListeners(stage);
        stage.addActor(player.actor);
        player.actor.setPosition(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 4f);
        stage.addActor(trader);
        trader.setBounds(Gdx.graphics.getWidth() / 2f, player.actor.getY(), 180, 300);

        generateStock();
        name = new Label("Дмитро Щур Orange 31см", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
        name.setAlignment(Align.center);
        name.addAction(Actions.alpha(0));

        stage.addActor(name);
        stage.addActor(nextMoveButton);

        createTradeWindow();
        stage.addActor(tradeWindow);
        tradeWindow.addAction(Actions.hide());
        openTradeWindowListener();

        nextMoveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameProgress.next();
            }
        });
    }

    @Override
    public void render(float delta) {

        stage.act();
        stage.draw();

        GameSound.updateVolume();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
