package com.patron.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Fight implements Screen {
    public static EnergyActor playerEnergy;
    public static Group cardActors, enemiesActors, tooltipedActors;
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static Player player;
    static ArrayList<Card> inHand;
    static ArrayList<Card> draw;
    static ArrayList<Card> discard;
    static Stage stage;
    static boolean win;
    static boolean loose = false;
    static RewardScene rewardScene;
    Game game;
    int move = 1;
    Timer timer = new Timer();
    static Timer staticTimer = new Timer();
    SpriteBatch batch;
    Texture background;
    Sprite drawButton, discardButton;
    NextMoveButton nextMoveButton;
    private ShapeRenderer shapeRenderer;

    public Fight(ArrayList<Enemy> enemies, ArrayList<Card> playerDeck, Game game) {
        player.setEnergy(player.getMaxEnergy());
        this.game = game;
        Fight.enemies = enemies;
        player.actor.effectPanel.effectIcons.clear();
        player.effects.clear();

        win = loose = false;
        rewardScene = null;


        inHand = new ArrayList<>();
        draw = new ArrayList<>(playerDeck);
        discard = new ArrayList<>();
        Collections.shuffle(draw);

        background = new Texture(Gdx.files.internal("background1.png"));
        batch = new SpriteBatch();
        stage = new Stage();
        cardActors = new Group();
        enemiesActors = new Group();
        tooltipedActors = new Group();

        player.setArmor(0);
        effectCheck();
        cardDeal(player.getCardPerRound());
    }

    protected static void cardDeal(int amount) {
        int step = amount % 2 == 1 ? (amount - 1) * (-210) + 100 : (amount - 2) * (-210);
        for (int i = 0; i < amount; i++) {
            step += 210;

            if (draw.size() == 0) {
                shuffleDeck(amount-i);
                break;
            } else {
                inHand.add(draw.get(0));

                draw.get(0).cardActor.setPos(-step, 10);
                draw.get(0).cardActor.setSize(CardActor.cardWidth / 3, CardActor.cardHeight / 3);

                cardActors.addActor(draw.get(0).cardActor);

                draw.remove(0);
            }
        }
        centerCardDeck();
    }

    public static void centerCardDeck() {
        float targetX = Gdx.graphics.getWidth() / 2 - (cardActors.getChildren().size * (CardActor.cardWidth + 10)) / 2;
        int number = 0;
        for (Actor actor : cardActors.getChildren()) {
            actor.addAction(Actions.parallel(
                    Actions.moveTo(targetX, actor.getY(), 0.8f),
                    Actions.sizeTo(CardActor.cardWidth, CardActor.cardHeight, 0.8f)
            ));
            ((CardActor) actor).xPos = targetX;
            ((CardActor) actor).number = number;
            targetX += CardActor.cardWidth + 10;
            number++;
        }
    }

    private static void shuffleDeck(int toDeal) {
        draw.clear();
        Gdx.input.setInputProcessor(null);
        for (int i = 0; i < discard.size(); i++) {
            stage.addActor(discard.get(i).cardActor);
            int finalI = i;
            staticTimer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    System.out.println(finalI == discard.size()-1);
                    discard.get(finalI).cardActor.addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.moveTo(Gdx.graphics.getWidth()/2-CardActor.cardWidth, 50, 0.75f, Interpolation.sine),
                                    Actions.sizeTo(CardActor.cardWidth, CardActor.cardHeight, 0.75f, Interpolation.sine)
                            ),
                            Actions.parallel(
                                    Actions.moveTo(-CardActor.cardWidth, 10, 0.75f, Interpolation.sine),
                                    Actions.sizeTo(CardActor.cardWidth/3, CardActor.cardHeight/3, 0.75f, Interpolation.sine)
                            ),
                            finalI == discard.size()-1 ?  Actions.run(()->{
                                System.out.println(discard.size() + "  " + finalI);
                                draw.addAll(discard);
                                discard.clear();
                                Collections.shuffle(draw);
                                cardDeal(toDeal);
                                Gdx.input.setInputProcessor(stage);
                            }) : Actions.delay(0)
                    ));
                }
            },i*0.1f);

        }

    }

    public static Card getCardFromDiscard() {
        if (discard.size() == 0) return null;
        int index = 1;
        for (Card c : discard) System.out.println(index++ + ". " + c);
        int choice = DataInput.getInt();
        while (choice > discard.size() || discard.size() < 1) choice = DataInput.getInt();
        Card c = discard.get(choice - 1);
        discard.remove(c);
        return c;
    }

    public static void checkKill() {
        for (Enemy enemy : enemies) {
            if (enemy.getHealth() <= 0) enemy.death();
        }
        enemies.removeIf(enemy -> enemy.getHealth() <= 0);
        checkFinal();
    }

    private static void checkFinal() {
        if (enemies.size() == 0) {
            rewardScene = new RewardScene();
            Gdx.input.setInputProcessor(rewardScene);
            rewardScene.addAction(Actions.fadeIn(0.2f));
            win = true;
        } else if (player.getHealth() <= 0) {
            loose = true;
            new Timer().scheduleTask(new Timer.Task() {
                @Override
                public void run() {

                    Gdx.app.exit();
                }
            }, 3f);
        }
    }


    public void nextMove() {
        disableListeners();
        for (Enemy enemy : enemies) enemy.setArmor(0);
        for (Card card : inHand) {
            if (card.rarity == Rarity.CURSE || card.rarity == Rarity.STATUS) card.use(null, null);
        }
        inHand.removeIf(card -> card.ghostly);
        discard.addAll(inHand);
        for (Actor actor : cardActors.getChildren()) {
            actor.addAction(Actions.parallel(
                    Actions.moveTo(discardButton.getX() + discardButton.getWidth() / 2, discardButton.getY() + discardButton.getHeight() / 2, 0.8f),
                    Actions.sizeTo(CardActor.cardWidth / 3, CardActor.cardHeight / 3, 0.8f)

            ));
        }
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                inHand.clear();
                cardActors.clear();
                player.setEnergy(player.getMaxEnergy());
                enemyMove();
                timer.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        move++;
                        player.setArmor(0);
                        effectCheck();
                        enableListeners();
                        cardDeal(player.getCardPerRound());
                    }
                }, enemies.size());
            }
        }, 0.8f);
    }

    private void effectCheck() {
        player.effectCheck();
        for (Enemy enemy : enemies) {
            enemy.effectCheck();
            checkKill();
        }
    }

    private void enemyMove() {
        for (Enemy enemy : enemies) {
            if (player.getHealth() > 0) enemy.makeMove();
            else {
                loose = true;
                break;
            }
        }
    }

    private void addEnemiesToGroup() {
        int y = Gdx.graphics.getHeight() / 3;
        int x = Gdx.graphics.getWidth() / 2 + 100;
        for (Enemy enemy : enemies) {
            enemiesActors.addActor(enemy.actor);
            tooltipedActors.addActor(enemy.actor.moveDisplay);

            enemy.actor.setPosition(x, y);
            x += enemy.actor.width * 1.2;
        }
    }

    private void addButtons() {
        playerEnergy = new EnergyActor(player.getEnergy(), 150, 150);
        drawButton = new Sprite(new Texture(Gdx.files.internal("draw.png")));
        discardButton = new Sprite(new Texture(Gdx.files.internal("discard.png")));
        nextMoveButton = new NextMoveButton();
        nextMoveButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nextMove();
                return true;
            }
        });

        playerEnergy.setPosition(Gdx.graphics.getWidth() / 8, 130);
        stage.addActor(playerEnergy);
        drawButton.setBounds(10, 10, 200, 200);
        discardButton.setBounds(Gdx.graphics.getWidth() - 210, 10, 200, 200);
    }

    private void disableListeners() {
        Gdx.input.setInputProcessor(null);
    }

    private void enableListeners() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        stage.addActor(player.actor);
        player.actor.setPosition(100, Gdx.graphics.getHeight() / 3);
        stage.addActor(enemiesActors);
        stage.addActor(cardActors);
        stage.addActor(tooltipedActors);
        //stage.setDebugAll(true);
        addEnemiesToGroup();
        addButtons();
        stage.addActor(nextMoveButton);
        shapeRenderer = new ShapeRenderer();
        stage.addListener(new FightInputController(cardActors));

        enableListeners();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        drawButton.draw(batch);
        discardButton.draw(batch);

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.circle((float) (drawButton.getX() + (drawButton.getWidth() * 0.9)), (float) (drawButton.getY() * 4), drawButton.getWidth() / 6);
        shapeRenderer.circle((float) (discardButton.getX()), (float) (discardButton.getY() * 4), discardButton.getWidth() / 6);
        shapeRenderer.end();

        batch.begin();
        Fonts.ALBIONIC_LARGE_NAME.draw(batch, draw.size() + "", (float) (drawButton.getX() + (drawButton.getWidth() * 0.8)), drawButton.getY() * 5, drawButton.getWidth() / 5, Align.center, true);
        Fonts.ALBIONIC_LARGE_NAME.draw(batch, discard.size() + "", (float) (discardButton.getX() - (discardButton.getWidth() * 0.1)), discardButton.getY() * 5, discardButton.getWidth() / 5, Align.center, true);

        batch.end();

        playerEnergy.setEnergyAmount(player.getEnergy());
        stage.act();
        stage.draw();
        if (win) {
            rewardScene.act();
            rewardScene.draw();
        }

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
        batch.dispose();
        stage.dispose();
    }

    class FightInputController extends InputListener {
        Group cardActors;

        public FightInputController(Group cardActors) {
            this.cardActors = cardActors;
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
                if (keycode - Input.Keys.NUM_1 < cardActors.getChildren().size) {
                    CardActor selectedActor = null;
                    for (Actor cardActor : cardActors.getChildren()) {
                        if (((CardActor) cardActor).number == keycode - Input.Keys.NUM_1)
                            selectedActor = (CardActor) cardActor;
                    }
                    if (selectedActor != null) selectedActor.select();
                }
            }
            if (keycode == Input.Keys.ENTER && Gdx.input.getInputProcessor() != null) nextMove();
            if (keycode == Input.Keys.END)
                for (Enemy enemy : enemies) enemy.getDamage(1000);
            return super.keyDown(event, keycode);
        }

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (button == Input.Buttons.LEFT) {
                for (Actor cardActor : cardActors.getChildren()) {
                    if (((CardActor) cardActor).selected &&
                            !(stage.hit(x, y, true) instanceof CardActor) &&
                            !(stage.hit(x, y, true) instanceof NextMoveButton)) {
                        cardActor.addAction(Actions.parallel(
                                Actions.moveTo(x, y, 0.6f),
                                Actions.sizeTo(CardActor.cardWidth, CardActor.cardHeight, 0.6f)
                        ));
                        timer.scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {
                                ((CardActor) cardActor).checkUsing();
                            }
                        }, 0.6f);

                    }
                }


                return true;
            }
            return false;
        }
    }
}

class RewardScene extends Stage {
    EnemyActor.EnemySprite black;
    Card[] choice;
    ShapeRenderer shapeRenderer;
    Label label;

    public RewardScene() {
        shapeRenderer = new ShapeRenderer();
        black = new EnemyActor.EnemySprite(new Sprite(new Texture(Gdx.files.internal("black.jpg"))));
        black.setBounds(0, 0, getWidth(), getHeight());
        black.addAction(Actions.alpha(0));
        addActor(black);

        label = new Label("Обери нагороду за бій:", new Label.LabelStyle(Fonts.ALBIONIC_LARGE_NAME, Color.GOLD));
        label.addAction(Actions.alpha(0));
        label.setAlignment(Align.center);
        label.setBounds(0, Gdx.graphics.getHeight() / 2 + CardActor.cardHeight * 2, getWidth(), label.getHeight());
        addActor(label);

        black.addAction(Actions.fadeIn(4f, Interpolation.smooth));
        label.addAction(Actions.fadeIn(1f));

        choice = new Card[3];
        ArrayList<Card> cards = new ArrayList<>(GameProgress.allCards);

        try {
            for (int i = 0; i < choice.length; i++) {
                choice[i] = (Card) cards.remove(new Random().nextInt(cards.size())).clone();
            }
        } catch (CloneNotSupportedException ignored) {
        }

        int gap = 0;
        for (Card card : choice) {
            card.cardActor = new CardActor(card, 100, 100);
            addActor(card.cardActor);
            card.cardActor.setSize(CardActor.cardWidth * 2, CardActor.cardHeight * 2);
            card.cardActor.setDraggable(false);
            card.cardActor.setPosition((float) (Gdx.graphics.getWidth() / 2 - card.cardActor.getWidth() * 2 + gap), -card.cardActor.getHeight());
            gap += 400;
            card.cardActor.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.LEFT) {
                        chooseThisCard(card);
                        return true;
                    }
                    return false;
                }
            });
            card.cardActor.addAction(Actions.moveTo(card.cardActor.getX(), Gdx.graphics.getHeight() / 2 - card.cardActor.getHeight() / 2, 2f));
        }

    }

    private void chooseThisCard(Card card) {
        for (Card cards : choice) {
            cards.cardActor.setDraggable(true);
            if (card != cards) cards.cardActor.addAction(Actions.sequence(
                    Actions.parallel(
                            Actions.moveBy(0, -1000, 1f),
                            Actions.sizeTo(CardActor.cardWidth, CardActor.cardHeight, 1f)
                    ),
                    Actions.run(Actions::removeActor)
            ));
        }
        card.cardActor.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, 1000, 1.1f),
                        Actions.sizeTo(CardActor.cardWidth, CardActor.cardHeight, 1.1f)
                ),
                Actions.run(() -> {
                    Actions.removeActor();
                    new Timer().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            GameProgress.playerDeck.add(card);
                            dispose();
                            GameProgress.game.setScreen(GameProgress.generateFight());
                        }
                    }, 0.5f);
                })
        ));
    }

    @Override
    public void draw() {
        super.draw();

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}