package com.patron.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Fight implements Screen {
    public static EnergyActor playerEnergy;
    public static Group cardActors, enemiesActors, tooltipedActors;
    public static Artefact reward;
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static Player player;
    static ArrayList<Card> inHand;
    static ArrayList<Card> draw;
    static ArrayList<Card> discard;
    static Stage stage;
    static boolean win;
    static boolean loose = false;
    static RewardScene rewardScene;
    static Timer staticTimer = new Timer();
    static int totalGoldReward;
    public ArrayList<Enemy> currentEnemies = new ArrayList<>();
    public Artefact currentReward;
    public boolean isEventFight = false;
    public static boolean isEliteFight = false;
    public boolean isEliteFightCurrent = false;
    Game game;
    public  static int move = 1;
    Timer timer = new Timer();
    SpriteBatch batch;
    Texture background;
    Image drawButton, discardButton;
    NextMoveButton nextMoveButton;
    private ShapeRenderer shapeRenderer;


    public Fight(ArrayList<Enemy> enemies, ArrayList<Card> playerDeck, Game game) {
        this.game = game;
        currentEnemies = enemies;
        reward = null;
        totalGoldReward = 0;
        move = 1;
        win = loose = false;
        rewardScene = null;


        inHand = new ArrayList<>();
        draw = new ArrayList<>(playerDeck);
        discard = new ArrayList<>();
        Collections.shuffle(draw);

    }

    private static void finalArtefacts() {
        if (player.ifHasArtefact("Артемівське"))
            player.heal(5);
    }

    protected static void cardDeal(int amount) {
        int step = amount % 2 == 1 ? (amount - 1) * (-210) + 100 : (amount - 2) * (-210);
        for (int i = 0; i < amount; i++) {
            step += 210;

            if (draw.size() == 0) {
                shuffleDeck(amount - i);
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
        float targetX = Gdx.graphics.getWidth() / 2f - (cardActors.getChildren().size * (CardActor.cardWidth + 10)) / 2;
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
        GameSound.shuffleSound.setVolume(GameSound.shuffleSound.play(),GameSound.soundVolume);
        draw.clear();
        Gdx.input.setInputProcessor(null);
        for (Card card : discard)
            card.cardActor.setPosition(Gdx.graphics.getWidth() + card.cardActor.getWidth(), -card.cardActor.getHeight());
        for (int i = 0; i < discard.size(); i++) {
            stage.addActor(discard.get(i).cardActor);
            int finalI = i;
            staticTimer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    discard.get(finalI).cardActor.addAction(Actions.sequence(
                            Actions.fadeIn(0.01f),
                            Actions.parallel(
                                    Actions.moveTo(Gdx.graphics.getWidth() / 2f - CardActor.cardWidth, 50, 0.75f, Interpolation.sine),
                                    Actions.sizeTo(CardActor.cardWidth, CardActor.cardHeight, 0.75f, Interpolation.sine)
                            ),
                            Actions.parallel(
                                    Actions.moveTo(-CardActor.cardWidth, 10, 0.75f, Interpolation.sine),
                                    Actions.sizeTo(CardActor.cardWidth / 3, CardActor.cardHeight / 3, 0.75f, Interpolation.sine)
                            ),
                            finalI == discard.size() - 1 ? Actions.run(() -> {
                                draw.addAll(discard);
                                discard.clear();
                                Collections.shuffle(draw);
                                cardDeal(toDeal);
                                Gdx.input.setInputProcessor(stage);
                            }) : Actions.delay(0)
                    ));
                }
            }, i * 0.1f);
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
        enemies.removeIf(enemy -> enemy.isDeath);
        checkFinal();
    }

    private static void checkFinal() {
        if (enemies.size() == 0) {
            finalArtefacts();
            if (isEliteFight) GameProgress.elitePassed = true;
            rewardScene = new RewardScene(true,
                    player.ifHasArtefact("Щасливий ремінь") ? (int) (totalGoldReward * 1.25) : totalGoldReward,
                    reward);
            Gdx.input.setInputProcessor(rewardScene);
            rewardScene.addAction(Actions.fadeIn(0.2f));
            for (Effect effect : player.effects) effect.setBase();
            player.actor.effectPanel.effectIcons.clear();
            player.effects.clear();
            win = true;
        } else if (player.getHealth() <= 0) {
            loose = true;
            new Timer().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    GameSound.fightMusic.stop();
                    GameProgress.game.setScreen(GameProgress.menu);
                }
            }, 3f);
        }
    }

    private void instanceArtefacts() {
        if (player.ifHasArtefact("Трофей"))
            for (Enemy enemy : enemies) enemy.addEffect(new WeaknessEffect(1));
        if (player.ifHasArtefact("Стріла Артеміди"))
            for (Enemy enemy : enemies) enemy.addEffect(new VulnerabilityEffect(1));

        if (player.ifHasArtefact("Залізний щит"))
            player.addArmor(12);

        if (player.ifHasArtefact("Дерев’яний щит"))
            player.addEffect(new AgilityEffect(1));

        if (player.ifHasArtefact("Пилка для кігтів"))
            player.addEffect(new StrengthEffect(1));

        if (player.ifHasArtefact("Артемідова стріла"))
            for (Enemy enemy : enemies) enemy.addEffect(new VulnerabilityEffect(1));

        if (player.ifHasArtefact("Львівське 1715"))
            player.setEnergy(player.getEnergy() + 1);

        if (player.ifHasArtefact("Хліб"))
            player.heal(5);

    }

    private void passiveArtefactsAtStart() {
        if (player.ifHasArtefact("Веселий грибочок"))
            if (MathUtils.random(1, 10) == 1) player.setEnergy(player.getEnergy() + 1);

        if (player.ifHasArtefact("Писанка"))
            if (move % 3 == 0) player.setEnergy(player.getEnergy() + 1);
    }

    private void passiveArtefactsAtEnd() {
        if (player.ifHasArtefact("Обсидіан"))
            if (player.getArmor() == 0) player.addArmor(5);
    }

    public void setReward(Artefact artefact) {
        currentReward = artefact;
    }

    public void nextMove() {
        passiveArtefactsAtEnd();
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
                        passiveArtefactsAtStart();
                        enableListeners();
                        cardDeal(player.getCardPerRound());
                    }
                }, enemies.size());
            }
        }, 0.8f);
    }

    private void effectCheck() {
        player.effectCheck();
        if (enemies.size() != 0)
            for (Enemy enemy : enemies) {
                enemy.effectCheck();
                checkKill();
            }
    }

    private void enemyMove() {
        int index = 0;
        for (Enemy enemy : enemies) {
            if (player.getHealth() > 0) {
                timer.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        enemy.makeMove();
                    }
                }, index * 0.5f);
                index++;
            } else {
                loose = true;
                break;
            }
        }
    }

    private void addEnemiesToGroup() {
        float y = Gdx.graphics.getHeight() / 3.3f;
        float x = Gdx.graphics.getWidth() / 2f - 50;
        for (Enemy enemy : enemies) {
            enemiesActors.addActor(enemy.actor);
            tooltipedActors.addActor(enemy.actor.moveDisplay);

            enemy.actor.setPosition(x, y);
            enemy.setBasicAnimation();
            //    enemy.actor.addAction(Actions.color());
            x += enemy.actor.width * 1.2;
            totalGoldReward += enemy.goldReward;
        }
    }

    private void addButtons() {
        playerEnergy = new EnergyActor(player.getEnergy(), 150, 150);
        drawButton = new Image(new Sprite(new Texture(Gdx.files.internal("draw.png"))));
        discardButton = new Image(new Sprite(new Texture(Gdx.files.internal("discard.png"))));
        nextMoveButton = new NextMoveButton("Наступний хід");
        nextMoveButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameSound.buttonSound.setVolume(GameSound.buttonSound.play(),GameSound.soundVolume);
                nextMove();
                return true;
            }
        });
        drawButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ArrayList<Card> shuffled = new ArrayList<>(draw);
                Collections.shuffle(shuffled);
                stage.addActor(new CardList(shuffled, stage));
            }
        });

        discardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(new CardList(discard, stage));
            }
        });

        playerEnergy.setPosition(Gdx.graphics.getWidth() / 8f, 130);
        stage.addActor(playerEnergy);
        stage.addActor(discardButton);
        stage.addActor(drawButton);
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
        if (GameSound.wayMusic.isPlaying())
            GameSound.wayMusic.pause();

        isEliteFight = isEliteFightCurrent;

        if (!isEliteFight) GameSound.randomFightMusic();
        else GameSound.fightMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\ScientificTriumph.mp3"));
        GameSound.fightMusic.setVolume(GameSound.musicVolume);
        GameSound.fightMusic.setLooping(true);
        GameSound.fightMusic.play();

        background = GameProgress.getRandomBackground();
        batch = new SpriteBatch();
        stage = new Stage();
        cardActors = new Group();
        enemiesActors = new Group();
        tooltipedActors = new Group();

        enemies = new ArrayList<>(currentEnemies);
        reward = currentReward;

        stage.addActor(GameProgress.topPanel);
        GameProgress.topPanel.addElementsWithListeners(stage);
        stage.addActor(player.actor);
        player.actor.clearActions();
        player.actor.setPosition(100, Gdx.graphics.getHeight() / 3.3f);
        player.setBasicAnimation();
        stage.addActor(enemiesActors);
        stage.addActor(cardActors);
        stage.addActor(tooltipedActors);
        //stage.setDebugAll(true);
        addEnemiesToGroup();
        addButtons();
        stage.addActor(nextMoveButton);
        shapeRenderer = new ShapeRenderer();
        stage.addListener(new FightInputController(cardActors));


        player.setEnergy(player.getMaxEnergy());
        player.setArmor(0);
        effectCheck();
        cardDeal(player.getCardPerRound());

        for (Enemy enemy : enemies)
            enemy.initialEffect();
        instanceArtefacts();
        enableListeners();


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
        GameSound.fightMusic.stop();
        GameSound.fightMusic.dispose();

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
            if (keycode == Input.Keys.END) {
                for (Enemy enemy : enemies) {
                    enemy.getDamage(1000);
                }
                checkKill();
            }
            if (keycode == Input.Keys.HOME) player.heal(50);
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
    Image black, goldSprite, artefactSprite, rays;
    Card[] choice;
    Label label;
    int goldReward;
    Artefact artefactReward;
    NextMoveButton nextMoveButton;

    public RewardScene(boolean cardReward, int goldReward, Artefact artefactReward) {
        this.goldReward = goldReward;
        this.artefactReward = artefactReward;
        black = new Image(new Sprite(new Texture(Gdx.files.internal("black.png"))));
        black.setBounds(0, 0, getWidth(), getHeight());
        addActor(black);

        label = new Label("Обери нагороду за бій:", new Label.LabelStyle(Fonts.ALBIONIC_LARGE_NAME, Color.GOLD));
        label.addAction(Actions.alpha(0));
        label.setAlignment(Align.center);
        label.setBounds(0, Gdx.graphics.getHeight() / 2f + CardActor.cardHeight * 2, getWidth(), label.getHeight());
        addActor(label);

        black.addAction(Actions.fadeIn(4f, Interpolation.smooth));
        label.addAction(Actions.fadeIn(1f));

        if (cardReward) getCard();
        else if (goldReward != 0) getGold();
        else getArtefact();

    }

    private void getCard() {
        nextMoveButton = new NextMoveButton("Пропустити");
        addActor(nextMoveButton);
        nextMoveButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                chooseThisCard(null);
            }
        });
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
            card.cardActor.addAction(Actions.moveTo(card.cardActor.getX(), Gdx.graphics.getHeight() / 2f - card.cardActor.getHeight() / 2, 2f));
        }
    }

    private void chooseThisCard(Card card) {
        GameSound.buttonSound.setVolume(GameSound.buttonSound.play(),GameSound.soundVolume);
        for (Card cards : choice) {
            if (card != cards) cards.cardActor.addAction(Actions.sequence(
                    Actions.parallel(
                            Actions.moveTo(cards.cardActor.getX(), -cards.cardActor.getHeight()*5, 1f),
                            Actions.sizeTo(CardActor.cardWidth, CardActor.cardHeight, 1f)
                    ),
                    Actions.run(() -> {
                        cards.cardActor.addAction(Actions.removeActor());
                        cards.cardActor.setDraggable(true);
                    })
            ));
        }
        nextMoveButton.addAction(Actions.removeActor());
       if (card != null)card.cardActor.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(card.cardActor.getX(), Gdx.graphics.getHeight() + card.cardActor.getHeight()*5, 1.1f),
                        Actions.sizeTo(CardActor.cardWidth, CardActor.cardHeight, 1.1f)
                ),
                Actions.run(() -> {
                    card.cardActor.addAction(Actions.removeActor());
                    card.cardActor.setDraggable(true);
                    new Timer().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            GameProgress.playerDeck.add(card);
                            if (goldReward != 0) getGold();
                            else {
                                dispose();
                                GameProgress.next();
                            }
                        }
                    }, 0.5f);
                })
        ));
       else{
           if (goldReward != 0) getGold();
           else {
               dispose();
               GameProgress.next();
           }
       }
    }

    private void getGold() {
        label.setText("Ви витягли з ворогів золото!");
        goldSprite = new Image(new Sprite(new Texture(Gdx.files.internal("gold_reward.png"))));
        rays = new Image(new Sprite(new Texture(Gdx.files.internal("rays.png"))));
        goldSprite.setSize(250, 250);
        goldSprite.setPosition(Gdx.graphics.getWidth() / 2f - goldSprite.getWidth() / 2, -goldSprite.getHeight());
        setupRays(500, 500);
        Label goldAmount = new Label(goldReward + "", new Label.LabelStyle(Fonts.MAURYSSEL_LARGE, Color.WHITE));
        goldAmount.scaleBy(3);
        goldSprite.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.fadeIn(1f),
                        Actions.moveTo(Gdx.graphics.getWidth() / 2f - goldSprite.getWidth() / 2, Gdx.graphics.getHeight() / 2f - goldSprite.getHeight() / 2, 1f)
                ),
                Actions.run(() -> {
                    goldAmount.setPosition(Gdx.graphics.getWidth() / 2f - goldAmount.getWidth() / 2, goldSprite.getY() + goldAmount.getHeight());
                    goldAmount.addAction(Actions.fadeIn(0.5f));
                    addActor(goldAmount);
                })
        ));
        addActor(rays);
        addActor(goldSprite);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rays.addAction(Actions.sequence(
                        Actions.fadeOut(0.3f),
                        Actions.removeActor()
                ));
                goldSprite.addAction(Actions.sequence(
                        Actions.fadeOut(0.3f),
                        Actions.removeActor()
                ));
                goldAmount.addAction(Actions.sequence(
                        Actions.fadeOut(0.3f),
                        Actions.removeActor()
                ));
                removeListener(this);
                GameProgress.player.addGold(goldReward);
                if (artefactReward != null) getArtefact();
                else {
                    dispose();
                    GameProgress.next();
                }

            }
        });
    }

    private void getArtefact() {
        label.setText("Ви знайшли артефакт!");
        artefactSprite = new Image(new Sprite(artefactReward.icon));
        rays = new Image(new Sprite(new Texture(Gdx.files.internal("rays.png"))));
        GameSound.rewardSound.setVolume(GameSound.rewardSound.play(),GameSound.soundVolume*0.7f);
        artefactSprite.setSize(125, 125);
        artefactSprite.setPosition(Gdx.graphics.getWidth() / 2f - artefactSprite.getWidth() / 2, -artefactSprite.getHeight());
        setupRays(375, 375);
        artefactSprite.addAction(Actions.parallel(
                Actions.fadeIn(1f),
                Actions.moveTo(Gdx.graphics.getWidth() / 2f - artefactSprite.getWidth() / 2, Gdx.graphics.getHeight() / 2f - artefactSprite.getHeight() / 2, 1f)
        ));
        addActor(rays);
        addActor(artefactSprite);
        addActor(new Tooltip(artefactReward.name, artefactReward.description, artefactSprite));
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameProgress.player.addArtefact(artefactReward);
                rays.addAction(Actions.sequence(
                        Actions.fadeOut(0.3f),
                        Actions.removeActor()
                ));
                artefactSprite.addAction(Actions.sequence(
                        Actions.fadeOut(0.3f),
                        Actions.removeActor()
                ));
                removeListener(this);

                dispose();
                GameProgress.next();

            }
        });
    }

    private void setupRays(int width, int height) {
        rays.setSize(width, height);
        rays.setPosition(Gdx.graphics.getWidth() / 2f - rays.getWidth() / 2, Gdx.graphics.getHeight() / 2f - rays.getHeight() / 2);
        rays.setOrigin(rays.getWidth() / 2, rays.getHeight() / 2);
        rays.addAction(Actions.parallel(
                Actions.fadeIn(2f),
                Actions.forever(Actions.rotateBy(360, 5f)
                )));
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