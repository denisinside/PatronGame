package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;


public class Event implements Screen {
    Sprite eventSprite;
    Sprite background;
    String eventName;
    String eventDescription;
    String eventResult;
    String exitButtonString = "Йти далі";
    Array<EventButton> buttons;
    SpriteBatch batch;
    Stage stage;
    Label name, description;

    public Event(String eventName, String eventDescription) {
        this.eventDescription = eventDescription;
        batch = new SpriteBatch();
        this.eventName = eventName;
        this.background = new Sprite(new Texture(Gdx.files.internal("event_template.png")));
        buttons = new Array<>();
        stage = new Stage();
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        name = new Label(eventName, new Label.LabelStyle(Fonts.MAURYSSEL_LARGE, Color.GOLD));
        name.setAlignment(Align.center);
        name.setBounds(Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 3.7f, Gdx.graphics.getWidth() / 1.5f, 80);
        stage.addActor(name);

        description = new Label(eventDescription, new Label.LabelStyle(Fonts.MAURYSSEL_LARGE, Color.WHITE));
        description.setAlignment(Align.topLeft);
        description.setWrap(true);
        description.setBounds(Gdx.graphics.getWidth() / 2.2f, Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 4.5f);
        stage.addActor(description);


        //stage.setDebugAll(true);

    }


    protected void addButtonsToStage() {
        for (EventButton eventButton : buttons) stage.addActor(eventButton);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        stage.addActor(GameProgress.topPanel);
        GameProgress.topPanel.addElementsWithListeners(stage);
    }

    @Override
    public void render(float delta) {
        batch.begin();

        if (eventSprite != null) eventSprite.draw(batch);
        background.draw(batch);

        batch.end();

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

    protected void firstChoice() {

    }

    protected void secondChoice() {

    }

    protected void thirdChoice() {

    }

    protected void forthChoice() {
    }

    protected void setEventResult() {
        description.setText(eventResult);
        for (EventButton eventButton : buttons) eventButton.addAction(Actions.removeActor());
        buttons.clear();
        buttons.add(
                new EventButton(exitButtonString, null, null, 5, this)
        );
        addButtonsToStage();
    }

    protected void exit() {

    }

    static class EventButton extends Actor {
        Sprite button, buttonHovered;
        Label choiceLabel, bonusLabel, penaltyLabel;
        boolean hovered;
        int choiceNumber;
        Event event;
        Tooltip tooltip;

        public EventButton(String choiceText, String bonusText, String penaltyText, int choiceNumber, Event event) {
            button = new Sprite(new Texture(Gdx.files.internal("event_button.png")));
            buttonHovered = new Sprite(new Texture(Gdx.files.internal("event_button_hovered.png")));

            this.choiceNumber = choiceNumber;
            this.event = event;
            setBounds((float) (Gdx.graphics.getWidth() / 2.2), Gdx.graphics.getHeight() / 5f + 75 *
                    (choiceNumber == 4 || choiceNumber == 5 ? choiceNumber-5 : (choiceNumber - 1)), 800, 75);

            choiceLabel = new Label("[" + choiceText + "]", new Label.LabelStyle(Fonts.MAURYSSEL_BASIC, Color.WHITE));
            if (bonusText != null)
                bonusLabel = new Label(bonusText, new Label.LabelStyle(Fonts.MAURYSSEL_BASIC, Color.GREEN));
            if (penaltyText != null)
                penaltyLabel = new Label(penaltyText, new Label.LabelStyle(Fonts.MAURYSSEL_BASIC, Color.RED));
            addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    hovered = true;
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    hovered = false;
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.LEFT) {
                        GameSound.buttonSound.setVolume(GameSound.buttonSound.play(),GameSound.soundVolume);
                        if (choiceNumber == 1) EventButton.this.event.firstChoice();
                        if (choiceNumber == 2) EventButton.this.event.secondChoice();
                        if (choiceNumber == 3) EventButton.this.event.thirdChoice();
                        if (choiceNumber == 4) EventButton.this.event.forthChoice();
                        if (choiceNumber == 5) EventButton.this.event.exit();
                        return true;
                    }
                    return false;
                }
            });
        }

        public void setTooltip(Tooltip tooltip) {
            this.tooltip = tooltip;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            if (hovered) buttonHovered.draw(batch);
            else button.draw(batch);
            choiceLabel.draw(batch, parentAlpha);
            if (bonusLabel != null) bonusLabel.draw(batch, parentAlpha);
            if (penaltyLabel != null) penaltyLabel.draw(batch, parentAlpha);
            if (tooltip != null) tooltip.draw(batch, parentAlpha);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            button.setBounds(getX(), getY(), getWidth(), getHeight());
            buttonHovered.setBounds(getX(), getY(), getWidth(), getHeight());
            choiceLabel.setPosition(getX() + getWidth() / 20, getY() + choiceLabel.getHeight());
            if (bonusLabel != null)
                bonusLabel.setPosition(10 + choiceLabel.getX() + choiceLabel.getWidth(), choiceLabel.getY());
            if (penaltyLabel != null) penaltyLabel.setPosition(
                    bonusLabel == null ? 10 + choiceLabel.getX() + choiceLabel.getWidth() : 10 + bonusLabel.getX() + bonusLabel.getWidth(), choiceLabel.getY());
            if (tooltip != null) tooltip.act(delta);
        }
    }
}

class ChoiceYourWayEvent extends Event {

    public ChoiceYourWayEvent() {
        super("Обери свій шлях",
                "Патрон підходить до кімнати з двома таємничими дверима." +
                        " На першій двері читається напис \"Перемога\" з яскравим і блискучим шрифтом," +
                        " що виблискує в променях світла. На другій двері виводиться напис \"Зрада\"" +
                        " глибокими червоними літерами, що випливають з-під темряви." +
                        "\nКуди вирушити Патрону?");

        EventButton first = new EventButton("Зрада", "Отримуєте 100 золота", "Отримуєте прокляття \"Зрада\"", 1, this);
        first.setTooltip(new CardTooltip(CardFactory.getCurse("Зрада"), first));

        EventButton second = new EventButton("Перемога", "Отримуєте реліквію \"Трофей\"", "Бій з ворогом", 2, this);
        second.setTooltip(new Tooltip("Трофей", "Всі вороги на першому ході отримують 1 слабкість", second));

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\ChoiceYourWayEvent.png")));
        buttons.add(first, second);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        eventResult = "\"Кому потрібні ці бої? Мені ж треба якнайскоріше бігти рятувати країну!\" - подумав Патрон," +
                "відчуваючи як зрада поширюється по його венам.";
        setEventResult();
        GameProgress.player.addGold(100);
        GameProgress.playerDeck.add(CardFactory.getCurse("Зрада"));
    }

    @Override
    protected void secondChoice() {
        eventResult = "\"Пес Патрон не боїться викликів! Стулю їм всім пельку своїм хвостом!\" - вигукнув Патрон, розминаючи лапи.";
        exitButtonString = "В бій!";
        setEventResult();
    }

    @Override
    protected void exit() {
        if (exitButtonString.equals("В бій!")) {
            Fight eventFight = GameProgress.generateFight();
            eventFight.setReward(ArtefactFactory.getArtefact("Трофей"));
            GameProgress.game.setScreen(eventFight);
        } else {
            GameProgress.next();
        }
    }
}

class AntBlacksmithEvent extends Event {
    public AntBlacksmithEvent() {
        super("Мураха-коваль",
                "Патрон натрапив на розумну мураху, яка, як надиво, дружелюбна." +
                        " Взамін на дозвіл погладити хвіст Патрона він готовий безкоштовно виконати заказ:" +
                        " зробити копію картки, прибрати непотрібну картку чи перетворити її на випадкову іншу." +
                        " \nЧи дозволить Патрон помацати свій хвіст та за яку послугу?");

        EventButton first = new EventButton("Копія", "Копіювати картку", "Полоскочуть хвіст", 1, this);
        EventButton second = new EventButton("Перевтілення", "Перевтілити картку", "Зав'яжуть хвіст у вузол", 2, this);
        EventButton third = new EventButton("Прибрати", "Прибрати картку", "Оближуть хвіст", 3, this);

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\AntBlacksmith.png")));
        buttons.add(first, second, third);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        CardList cardList = new CardList(GameProgress.playerDeck, stage);
        cardList.activateChoice();
        cardList.exitListenerActivate(false);
        cardList.setCardSelectionListener(selectedCard -> {
            try {
                Card copy = (Card) selectedCard.card.clone();
                copy.cardActor = new CardActor(copy, 1, 1);
                GameProgress.playerDeck.add(copy);
                eventResult = "\"Це були справжні тортури\" - подумав Патрон," +
                        "розглядаючи копію обраної картки.";
                setEventResult();
            } catch (Exception ignored) {
            }

        });
        stage.addActor(cardList);

    }

    @Override
    protected void secondChoice() {
        CardList cardList = new CardList(GameProgress.playerDeck, stage);
        cardList.activateChoice();
        cardList.exitListenerActivate(false);
        cardList.setCardSelectionListener(selectedCard -> {
            try {
                int index = GameProgress.playerDeck.indexOf(selectedCard.card);
                selectedCard.card = null;
                selectedCard.remove();
                GameProgress.playerDeck.set(index,
                        (Card) GameProgress.allCards.get(MathUtils.random(0, GameProgress.allCards.size() - 1)).clone());

                eventResult = "\"І як мені його назад розв'язати?\" - сказав з нерозумінням Патрон," +
                        "вивчаючи нову отриману картку";
                setEventResult();
            } catch (Exception ignored) {
            }
        });
        stage.addActor(cardList);
    }

    @Override
    protected void thirdChoice() {
        CardList cardList = new CardList(GameProgress.playerDeck, stage);
        cardList.activateChoice();
        cardList.exitListenerActivate(false);
        cardList.setCardSelectionListener(selectedCard -> {
            GameProgress.playerDeck.remove(selectedCard.card);

            eventResult = "\"Дорогий щоденник, мені не передати ту біль....\" - повторював Патрон раз за разом," +
                    "не відчуваючи подальшої жаги життя, зате на одну непотрібну картку менше";
            setEventResult();
        });
        stage.addActor(cardList);
    }

    @Override
    protected void exit() {
        GameProgress.next();

    }
}

class ChickenEventEvent extends Event {

    public ChickenEventEvent() {
        super("Голодний шлях",
                "Патрон зголоднів настільки, що готовий з'їсти свій хвіст." +
                        " Песобог почув його і наступна кімната була немов рай:" +
                        " все блискуче та в хмарах, а найголовніше це левітуюча куряча ніжка! " +
                        " Звісно такий шанс втрачати не можна!");

        EventButton first = new EventButton("З'їсти", "Зцілитись до максимуму", "Все добре!", 1, this);

        EventButton second = new EventButton("Піти далі", null, "Патрон від голоду втратить 5 ОЗ", 2, this);

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\ChickenEvent.png")));
        buttons.add(first, second);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        eventResult = "Виявляється, все це було суцільне марево! Як тільки Патрон вкусив, він зрозумів, що їсть свій хвіст!" +
                "Хмари одразу зникли, а голод посилився. Поки Патрон крутився по кімнаті від сорому," +
                "віе випадково під закутком знайшов чиюсь заначку.";
        exitButtonString = "Депресувати і йти далі";
        setEventResult();
        GameProgress.player.addGold(50);
        GameProgress.playerDeck.add(CardFactory.getCurse("Пуста миска"));
    }

    @Override
    protected void secondChoice() {
        eventResult = "\"Занадто дивно та підозріло все це, да і атеїст я\" - розміркував Патрон, проходячи по хмарам.";
        GameProgress.player.setHealth(GameProgress.player.getHealth() - 5);
        setEventResult();
    }

    @Override
    protected void exit() {
        GameProgress.next();
    }
}

class BusinessOfferEvent extends Event {
    int healAmount;

    public BusinessOfferEvent() {
        super("Ділова зустріч",
                "Патрон якимось чином зайшов на пацюковий бізнес-центр. Спочатку він навіть подумав, що це дніпровські офіси." +
                        "Починаючі підприємці одразу запропонували свої послуги: " +
                        "Видалення карти, зцілення та випадкову реліквію зі складу підземного АТБ."
        );
        healAmount = MathUtils.random(20) + 10;

        EventButton first = new EventButton("Видалити картку", null, "-50 монет", 1, this);

        EventButton second = new EventButton("Зцілитися", "Зцілитися на " + healAmount + " ОЗ", "-30 монет", 2, this);
        EventButton third = new EventButton("Реліквія", null, "-80 монет", 3, this);
        EventButton forth = new EventButton("Піти", null, null, 4, this);

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\BusinessOffer.png")));
        buttons.add(first, second, third, forth);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        if (GameProgress.player.money >= 50) {
            CardList cardList = new CardList(GameProgress.playerDeck, stage);
            cardList.activateChoice();
            cardList.exitListenerActivate(false);
            cardList.setCardSelectionListener(selectedCard -> {
                try {
                    GameProgress.playerDeck.remove(selectedCard.card);


                    eventResult = "До Патрона підійшла якась дивна пацючка, яка спочатку пропонувала " +
                            "погадати йому майбутнє по лапі, потім сказала, що порчу зніме." +
                            "А коли вона пішла, то Патрон зрозумів, що у нього пропали монети й картка." +
                            "На щастя, це була та сама картка, яку він хотів позбутися, мабуть, все ж таки гадати вона вміє.";
                    setEventResult();
                    GameProgress.player.money -= 50;
                } catch (Exception ignored) {
                }
            });
            stage.addActor(cardList);
        }
    }

    @Override
    protected void secondChoice() {
        if (GameProgress.player.money >= 30) {
            eventResult = "Пацюки принесли Патрону святу воду, заряджену від телевізора. Надиво, вона спрацювала і Патрон почуває себе краще";
            GameProgress.player.heal(healAmount);
            GameProgress.player.money -= 50;
            setEventResult();
        }
    }
    @Override
    protected void thirdChoice() {
        if (GameProgress.player.money >= 80) {
            eventResult = "Неймовірний збіг, але АТБ розташовувалося прямо над Патроном. Пацюк поліз по трубі до складу, а звідти в " +
                    "зубах приніс Патрону нову реліквію. Але було темно, тому він навіть й не знає, що взяв.";
            GameProgress.player.addArtefact(GameProgress.getRandomArtefact());
            GameProgress.player.money -= 80;
            setEventResult();
        }
    }
    @Override
    protected void forthChoice() {
            eventResult = "\"Нема грошей, я бідний студент ВинюхоЗнавчого Факультету!\" - " +
                    "закричав Патрон та побіг геть звідти";

            setEventResult();
    }

    @Override
    protected void exit() {
        GameProgress.next();
    }
}
class VampireEvent extends Event{

    public VampireEvent() {
        super("У кого гостріші ікла?",
                "Ввійшовши до кімнати, Патрон побачив величезного пацюка, який сидів на троні посередині кімнати." +
                        "\"Ви потрапили в лігво вампирів. Ми можемо надати вам нашу силу за невелику плату..\" - заговорив пацюк." +
                        "Патрон вагався. Це було спокусливо - отримати потужну силу,але це буде не згідно з його принципами.\n"
        );

        EventButton first = new EventButton("Погодитись", "Отримати реліквію", "-30% макс. ОЗ", 1, this);
        first.setTooltip(new Tooltip("Фляга з кров'ю","Всі атаки замінюються укусами. Укуси зцілюють на 2 ОЗ",first));

        EventButton second = new EventButton("Піти", null, null, 2, this);

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\VampireEvent.png")));
        buttons.add(first, second);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        eventResult = "Патрон вирішив прийняти виклик вампірів і стати на шляху темної сили. " +
                "Пацюк, вкусивши його кров, пішов. А Патрон відчував нову силу...";
        GameProgress.player.addArtefact(ArtefactFactory.getArtefact("Фляга з кров'ю"));
        setEventResult();
    }

    @Override
    protected void secondChoice() {
        eventResult = "\"Патрон не проміняє свої принципи та життєву силу на темну силу!\" - гукнув Патрон." +
                " Пацюк з незадоволеним обличчям вигнав Патрона далі.";
        setEventResult();
    }
    @Override
    protected void exit() {
        GameProgress.next();
    }
}

class LandLeaseEvent extends Event{

    public LandLeaseEvent() {
        super("Ленд-ліз",
                "Патрону прийшло повідомлення, що за його поточним місцезнаходженням була" +
                        "надіслана допомога у вигляді ленд-лізу. Вона була відправлена по трубам у наступну кімнату," +
                        "але виявилось, що монстри вже оточили ящик з ленд-лізом." +
                        "\nЯк поступити Патрону?"
        );

        EventButton first = new EventButton("В бій!", "Отримати реліквію", "Бій з ворогом", 1, this);
        first.setTooltip(new Tooltip("Ленд-ліз","Отримуєте випадкових 3 картки",first));

        EventButton second = new EventButton("Піти", null, null, 2, this);

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\LandLease.png")));
        buttons.add(first, second);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        eventResult = "Ця допомога була чесно відправлена Патрону! Не можна залишати її монстрам чи бандитам!";
        exitButtonString = "В бій!";
        setEventResult();
    }

    @Override
    protected void secondChoice() {
        eventResult = "\"Дякую звісно, але я краще піду...\" - сказав пошепки Патрон.";
        setEventResult();
    }
    @Override
    protected void exit() {
        if (exitButtonString.equals("В бій!")) {
            ArrayList<Enemy> enemies = new ArrayList<>();
            enemies.add(new MossyFox());
            enemies.add(new MossyFox());
            enemies.add(new MossyFox());
            Fight eventFight = GameProgress.generateFight(enemies);
            eventFight.setReward(ArtefactFactory.getArtefact("Ленд-ліз"));
            GameProgress.game.setScreen(eventFight);
        } else {
            GameProgress.next();
        }
    }
}
class HeavenTempleEvent extends Event{

    public HeavenTempleEvent() {
        super("Божествений храм",
                "Увійшовши в цю кімнату, Патрон побачив неймовірно високі сходи та світло, схоже на сонячне." +
                        "Піднявшись, він знайшов труну, к якій був приставлений сундук з реліквією." +
                        "\nЧи варто Патрону злити духа похованої тут істоти?"
        );

        EventButton first = new EventButton("Обчистити", "Отримати випадкову реліквію", "Отримати прокляття", 1, this);
        first.setTooltip(new CardTooltip(CardFactory.getCurse("Безсилля"),first));

        EventButton second = new EventButton("Піти", null, null, 2, this);

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\HeavenTemple.png")));
        buttons.add(first, second);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        eventResult = "\"Ця штука йому вже не понадобиться! Ха-ха-гав!\" - сміявся Патрон, поки не почув" +
                "неймовірне безсилля, через яке реліквія покотилася по сходам і йому треба було повертатись...";
        GameProgress.player.addArtefact(GameProgress.getRandomArtefact());
        GameProgress.playerDeck.add(CardFactory.getCurse("Безсилля"));
        setEventResult();
    }

    @Override
    protected void secondChoice() {
        eventResult = "\"Я звісно в усілякі забобони не вірю, просто я чемний...\" - сказав пошепки Патрон.";
        setEventResult();
    }
    @Override
    protected void exit() {
            GameProgress.next();
    }
}

class PoorRaccoonEvent extends Event{

    public PoorRaccoonEvent() {
        super("Єнот-волоцюга",
                "Патрон йшов тунелем, як раптом його потянули за хвіст:" +
                        "\"Мені нема за що їсти, купи цю річ, яка дісталася мені від батька, а" +
                        "йому від його батька, а йому...\" - каже Єнот." +
                        "\n\"Стій-стій, я зрозумів\" - перебив Патрон." +
                        "Щось Патрону не вірилося у історію Єнота, але річ здається корисною."
        );

        EventButton first = new EventButton("Купити", "Отримати випадкову реліквію", "-80 монет", 1, this);
        EventButton second = new EventButton("Вкрасти", "Отримати випадкову реліквію", "Отримати прокляття", 2, this);
        second.setTooltip(new CardTooltip(CardFactory.getCurse("Сором"),second));

        EventButton third = new EventButton("Піти", null, null, 3, this);

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\PoorRaccoon.png")));
        buttons.add(first, second,third);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        if (GameProgress.player.money >= 80){
            eventResult = "\"Дякую за покупку, приходьте ще, хє-хє\" - єхидно сказав Єнот та пішов геть." +
                    "А поки він йшов, він побачив, як копії купленої реліквії випадали у нього з карманів...";
            GameProgress.player.addArtefact(GameProgress.getRandomArtefact());
            GameProgress.player.money -= 80;
            setEventResult();
        }
    }

    @Override
    protected void secondChoice() {
        eventResult = "Поки Єнот поклав пакет з реліквією та відвернувся, Патрон вхопив пакет" +
                "у зуби та побіг геть під крики Єнота. Потім він не зрозумів, навіщо це зробив," +
                "він ж чемна собака... а раптом хтось дізнається.. Жах!";
        GameProgress.player.addArtefact(GameProgress.getRandomArtefact());
        GameProgress.playerDeck.add(CardFactory.getCurse("Сором"));
        setEventResult();
    }

    @Override
    protected void thirdChoice() {
        eventResult = "Патрон розглядів, що Єнот тримає у руках гайковий ключ." +
                "\n\"Здається, я бачив такий у одного президента в штанях, краще піду далі\" - вирішив Патрон.";
        setEventResult();
    }

    @Override
    protected void exit() {
        GameProgress.next();
    }
}
class RelicAndTrapEvent extends Event{

    public RelicAndTrapEvent() {
        super("Агент Патрон 007",
                "Патрон йшов по тунелю, як раптом в нього полетіли стріли!" +
                        "Він зрозумів, що це пастка і ця дорога небезпечна! Як раптом згадав," +
                        "що його дід розповідав, як у лихі 90-ті сховав тут сімейну реліквію." +
                        "\nЧи ризикувати Патрону?"
        );

        EventButton first = new EventButton("Ризикнути", "Отримати випадкову реліквію", "Отримати прокляття", 1, this);
        first.setTooltip(new CardTooltip(CardFactory.getCurse("Травма"),first));
        EventButton second = new EventButton("Піти", null, null, 2, this);

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\RelicAndTrap.png")));
        buttons.add(first, second);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        eventResult = "Патрон немов агент перестрибував усі пастки, окрім однієї, яка травмувала його." +
                "Але він дістав реліквію та переміг пастки свого діда!";
        GameProgress.player.addArtefact(GameProgress.getRandomArtefact());
        GameProgress.playerDeck.add(CardFactory.getCurse("Травма"));
        setEventResult();
    }

    @Override
    protected void secondChoice() {
        eventResult = "\"Знаючи свого діда Пса Пороха, я не переживу ці пастки\" - розміркував Патрон і пішов далі.";
        setEventResult();
    }
    @Override
    protected void exit() {
        GameProgress.next();
    }
}

class BadNewsEvent extends Event{

    Card card1 = GameProgress.playerDeck.get(MathUtils.random(GameProgress.playerDeck.size()-1));
    Card card2 = GameProgress.playerDeck.get(MathUtils.random(GameProgress.playerDeck.size()-1));
    Card card3 = GameProgress.playerDeck.get(MathUtils.random(GameProgress.playerDeck.size()-1));

    public BadNewsEvent() {
        super("Погані новини",
                "Патрон загубився в кімнаті, яка була повна темним лісом. Раптом він" +
                        "потрапив у пастку лісника, і зрозумів, що просто так без втрат не вибереться." +
                        "\n Діяти треба швидко: що залишити Патрону?"
        );

        EventButton first = new EventButton(card1.name, null, "Втратити картку", 1, this);
        first.setTooltip(new CardTooltip(card1,first));
        EventButton second = new EventButton(card2.name, null, "Втратити картку", 2, this);
        second.setTooltip(new CardTooltip(card2,second));
        EventButton third;
        if (GameProgress.player.money >= 100)
            third = new EventButton("Гроші", null,"Втратити всі гроші",3,this);
        else{
            third  = new EventButton(card3.name, null, "Втратити картку", 3, this);
            third.setTooltip(new CardTooltip(card3,third));
        }

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\BadNews.png")));
        buttons.add(first, second,third);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        eventResult = "Патрон зміг вибратись із пастки неушкодженим! Маленька перемога для Патрона для майбутньої перемоги над злом!";
        setEventResult();
        GameProgress.playerDeck.remove(card1);
    }

    @Override
    protected void secondChoice() {
        eventResult = "Патрон зміг вибратись із пастки неушкодженим! Маленька перемога для Патрона для майбутньої перемоги над злом!";
        setEventResult();
        GameProgress.playerDeck.remove(card2);
    }

    @Override
    protected void thirdChoice() {
        eventResult = "Патрон зміг вибратись із пастки неушкодженим! Маленька перемога для Патрона для майбутньої перемоги над злом!";
        setEventResult();
        if (GameProgress.player.money >= 100)
            GameProgress.player.money = 0;
        else
            GameProgress.playerDeck.remove(card3);
    }

    @Override
    protected void exit() {
        GameProgress.next();
    }
}

class RatCampEvent extends Event{

    public RatCampEvent() {
        super("Плем'я пацюків",
                "Патрон, побачивши плем'я мутантів,вже готувався йти в бій." +
                        "Але вони виявились дружніми та запропонували Патрону відпочити в них."
        );

        EventButton first = new EventButton("Поїсти", "Зцілитись на 25 ОЗ", null, 1, this);
        EventButton second = new EventButton("Взяти грошей", "Отримати 50 монет", null, 2, this);
        EventButton third = new EventButton("Поспати", "Прибрати картку", null, 3, this);
        EventButton forth = new EventButton("Піти", null, null, 4, this);

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\RatCamp.png")));
        buttons.add(first, second,third,forth);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 8f, Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getWidth() / 2.8f);

    }

    @Override
    protected void firstChoice() {
        eventResult = "Пацюки запропонували сир з пліснявою, сир з грибком, сир з мхом. " +
                        "Патрон вирішив з'їсти звичайний сир, який вони не вважали делікатесом";
        GameProgress.player.heal(25);
        setEventResult();
    }

    @Override
    protected void secondChoice() {
        eventResult = "Пацюки використовували Грушевського задля підставки для сиру, але " +
                "Патрон знайде більш корисне використання цій купюрі";
        GameProgress.player.money += 50;
        setEventResult();
    }

    @Override
    protected void thirdChoice() {

        CardList cardList = new CardList(GameProgress.playerDeck, stage);
        cardList.activateChoice();
        cardList.exitListenerActivate(false);
        cardList.setCardSelectionListener(selectedCard -> {
            GameProgress.playerDeck.remove(selectedCard.card);

            eventResult = "Ліжко було настільки зручним, що Патрону захотілося віддати картку пацюкам";
            setEventResult();
        });
        stage.addActor(cardList);
    }

    @Override
    protected void forthChoice() {
        eventResult = "Чи то з недовіри, чи з соромливості, але Патрон відмовився та пішов далі.";
        setEventResult();
    }

    @Override
    protected void exit() {
        GameProgress.next();
    }
}
