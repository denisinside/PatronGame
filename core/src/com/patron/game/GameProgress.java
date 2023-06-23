package com.patron.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GameProgress extends Game {
    public static ArrayList<Card> allCards;
    public static ArrayList<Artefact> allArtefacts;
    public static ArrayList<Event> events;
    public static Player player;
    public static Game game;
    public static TopPanel topPanel;
    public static Menu menu;
    public static boolean elitePassed = false;
    //double commonChance = 0.6, rareChance = 0.3, legendaryChance = 0.1;
    public static SettingsWindow settingsWindow;
    static ArrayList<Card> playerDeck;
    static int room = 0;
    static int lastEnemyIndex = -1;
    static int fightChance = 70, eliteFightChance = 0, shopChance = 5, treasuryChance = 0, eventChance = 25, eventFightChance = 25;

    public GameProgress() {
        game = this;
    }

    public static Fight generateEliteFight() {
        ArrayList<Enemy> enemies = new ArrayList<>(Arrays.asList(getEliteEnemiesForFight()));

        Fight fight1 = new Fight(enemies, playerDeck, game);
        fight1.isEliteFightCurrent = true;
        if (allArtefacts.size() != 0) fight1.setReward(getRandomArtefact());
        return fight1;
    }

    public static Fight generateFight(ArrayList<Enemy> enemies) {
        return new Fight(enemies, playerDeck, game);
    }

    public static Fight generateFight() {
        ArrayList<Enemy> enemies = new ArrayList<>(Arrays.asList(getEnemiesForFight()));
        Fight fight1 = new Fight(enemies, playerDeck, game);
        return fight1;
    }

    private static Enemy[] getEnemiesForFight() {
        int enemiesIndex;

        if (room < 3) {
            do {
                enemiesIndex = MathUtils.random(3);
            } while (enemiesIndex == lastEnemyIndex);
            lastEnemyIndex = enemiesIndex;
            switch (enemiesIndex) {
                case 0:
                    return new Enemy[]{
                            new FireBagSoldier(),
                            new FireBagMedic()
                    };
                case 1:
                    return new Enemy[]{
                            new Bandit()
                    };
                case 2:
                    return new Enemy[]{
                            new FireBagTank(),
                            new FireBagSoldier(),
                    };
                case 3:
                    return new Enemy[]{
                            new FireBagTank(),
                            new FireBagSniper()
                    };
            }
        } else if (room < 10) {
            do {
                enemiesIndex = MathUtils.random(4);
            } while (enemiesIndex == lastEnemyIndex);
            lastEnemyIndex = enemiesIndex;
            switch (enemiesIndex) {
                case 0:
                    return new Enemy[]{
                            new MossyFox(),
                            new MossyFox()
                    };
                case 1:
                    return new Enemy[]{
                            new RadioactiveRat(),
                            new Bandit()
                    };
                case 2:
                    return new Enemy[]{
                            new FireBagTank(),
                            new FireBagSoldier(),
                            new FireBagMedic()
                    };
                case 3:
                    return new Enemy[]{
                            new FireBagTank(),
                            new FireBagSniper()
                    };
                case 4:
                    return new Enemy[]{
                            new Bandit(),
                            new FireBagSoldier(),
                            new FireBagMedic()
                    };
            }
        } else if (room < 20) {
            do {
                enemiesIndex = MathUtils.random(7);
            } while (enemiesIndex == lastEnemyIndex);
            lastEnemyIndex = enemiesIndex;
            switch (enemiesIndex) {
                case 0:
                    return new Enemy[]{
                            new Diggy(),
                            new Norby()
                    };
                case 1:
                    return new Enemy[]{
                            new Bandit(),
                            new RadioactiveRat(),
                            new FireBagMedic()
                    };
                case 2:
                    return new Enemy[]{
                            new FireBagTank(),
                            new ExplosiveCat(),
                            new FireBagMedic()
                    };
                case 3:
                    return new Enemy[]{
                            new Shroomjak()
                    };
                case 4:
                    return new Enemy[]{
                            new MossyFox(),
                            new Bandit(),
                            new FireBagSniper()
                    };
                case 5:
                    return new Enemy[]{
                            new Schweinokar()
                    };
                case 6:
                    return new Enemy[]{
                            new RadioactiveRat(),
                            new RadioactiveRat(),
                            new FireBagTank()
                    };
                case 7:
                    return new Enemy[]{
                            new Hedgehog(),
                            new ExplosiveCat()
                    };
            }
        } else if (room < 30) {
            do {
                enemiesIndex = MathUtils.random(7);
            } while (enemiesIndex == lastEnemyIndex);
            lastEnemyIndex = enemiesIndex;
            switch (enemiesIndex) {
                case 0:
                    return new Enemy[]{
                            new Diggy(),
                            new Norby(),
                            new FireBagMedic()
                    };
                case 1:
                    return new Enemy[]{
                            new Bandit(),
                            new RadioactiveRat(),
                            new FireBagMedic()
                    };
                case 2:
                    return new Enemy[]{
                            new Hedgehog(),
                            new ExplosiveCat(),
                            new ExplosiveCat(),
                    };
                case 3:
                    return new Enemy[]{
                            new Shroomjak(),
                            new MossyFox()
                    };
                case 4:
                    return new Enemy[]{
                            new Hedgehog(),
                            new Hedgehog(),
                            new ExplosiveCat(),
                    };
                case 5:
                    return new Enemy[]{
                            new FireBagTank(),
                            new Schweinokar(),
                            new FireBagTank()
                    };
                case 6:
                    return new Enemy[]{
                            new RadioactiveRat(),
                            new RadioactiveRat(),
                            new ExplosiveCat()
                    };
                case 7:
                    return new Enemy[]{
                            new Hedgehog(),
                            new ExplosiveCat(),
                            new FireBagMedic()
                    };
            }
        }
        return null;
    }

    private static Enemy[] getEliteEnemiesForFight() {
        if (room <= 10)
            return new Enemy[]{
                    new MisterMeow()
            };
        else if (room <= 20)
            return new Enemy[]{
                    new DeeDee(),
                    new Marky(),
                    new Joey()
            };
        else if (room < 30)
            return new Enemy[]{
                    new Cutter()
            };

        return null;
    }

    public static void next() {
        if (room == 5) {
            events.add(new BusinessOfferEvent());
            events.add(new RelicAndTrapEvent());
            events.add(new AntBlacksmithEvent());

            fightChance = 55;
            eliteFightChance = 0;
            shopChance = 15;
            treasuryChance = 0;
            eventChance = 25;
        }

        if (room == 7) {
            fightChance = 10;
            eliteFightChance = 70;
            shopChance = 10;
            treasuryChance = 0;
            eventChance = 10;
        }
        if (elitePassed) {
            elitePassed = false;
            fightChance = 53;
            eliteFightChance = 0;
            shopChance = 12;
            treasuryChance = 0;
            eventChance = 25;
        }
        if (room == 10) {
            fightChance = 5;
            eliteFightChance = 0;
            shopChance = 0;
            treasuryChance = 95;
            eventChance = 0;
        }
        if (room == 11) {
            fightChance = 53;
            eliteFightChance = 0;
            shopChance = 12;
            treasuryChance = 0;
            eventChance = 25;
        }
        if (room == 15) {
            events.add(new VampireEvent());
            events.add(new BadNewsEvent());
            events.add(new LandLeaseEvent());
            events.add(new PoorRaccoonEvent());
        }
        if (room == 17) {
            fightChance = 10;
            eliteFightChance = 70;
            shopChance = 10;
            treasuryChance = 0;
            eventChance = 10;
        }
        if (room == 20) {
            fightChance = 5;
            eliteFightChance = 0;
            shopChance = 0;
            treasuryChance = 95;
            eventChance = 0;
        }
        if (room == 21) {
            fightChance = 53;
            eliteFightChance = 0;
            shopChance = 10;
            treasuryChance = 0;
            eventChance = 25;
        }
        if (room == 27) {
            fightChance = 10;
            eliteFightChance = 70;
            shopChance = 10;
            treasuryChance = 0;
            eventChance = 10;
        }
        if (room == 30) {
            game.setScreen(new FinalRoom());
        } else {
            game.setScreen(new ChooseNextRoom(getNewRoom(), getNewRoom()));
            room++;
        }
    }

    private static Screen getNewRoom() {

        int randomNumber = MathUtils.random(100);

        if (randomNumber <= fightChance) {
            return generateFight();
        } else if (randomNumber <= fightChance + eliteFightChance) {
            return generateEliteFight();
        } else if (randomNumber <= fightChance + eliteFightChance + shopChance) {
            return new Shop();
        } else if (randomNumber <= fightChance + eliteFightChance + shopChance + treasuryChance) {
            if (allArtefacts.size() != 0) return createTreasury();
            else return getNewRoom();
        } else {
            int chance = MathUtils.random(100);
            if (chance <= 20) {
                Fight fight = generateFight();
                fight.isEventFight = true;
                return fight;
            } else if (chance <= 30) {
                Shop shop = new Shop();
                shop.isEventShop = true;
                return shop;
            } else return getRandomEvent();
        }
    }

    private static Event getRandomEvent() {
        Event event = events.get(MathUtils.random(events.size() - 1));
        events.remove(event);
        return event;
    }

    public static Texture getRandomBackground() {
        return new Texture(Gdx.files.internal("backgrounds\\background" + MathUtils.random(1, 12) + ".png"));
    }

    private static Treasury createTreasury() {
        Artefact artefact = allArtefacts.get(MathUtils.random(allArtefacts.size() - 1));
        allArtefacts.remove(artefact);
        return new Treasury(artefact);
    }

    public static Artefact getRandomArtefact() {
        Artefact artefact = allArtefacts.get(MathUtils.random(allArtefacts.size() - 1));
        allArtefacts.remove(artefact);
        return artefact;
    }

    private static void loadStartEvents() {
        events.add(new ChoiceYourWayEvent());
        events.add(new ChickenEventEvent());
        events.add(new HeavenTempleEvent());
        events.add(new RatCampEvent());
    }

    private static void loadAllArtefacts() {
        allArtefacts = new ArrayList<>();
        allArtefacts.add(ArtefactFactory.getArtefact("Дерев’яний щит"));
        allArtefacts.add(ArtefactFactory.getArtefact("Пилка для кігтів"));
        allArtefacts.add(ArtefactFactory.getArtefact("Стріла Артеміди"));
        allArtefacts.add(ArtefactFactory.getArtefact("Львівське 1715"));
        allArtefacts.add(ArtefactFactory.getArtefact("Хліб"));
        allArtefacts.add(ArtefactFactory.getArtefact("Червоне яблуко"));
        allArtefacts.add(ArtefactFactory.getArtefact("Слиз"));
        allArtefacts.add(ArtefactFactory.getArtefact("Зелене яблуко"));
        allArtefacts.add(ArtefactFactory.getArtefact("Золотий злиток"));
        allArtefacts.add(ArtefactFactory.getArtefact("Срібний злиток"));
        allArtefacts.add(ArtefactFactory.getArtefact("Бронзовий злиток"));
        allArtefacts.add(ArtefactFactory.getArtefact("Рюкзак"));
        allArtefacts.add(ArtefactFactory.getArtefact("Обсидіан"));
        allArtefacts.add(ArtefactFactory.getArtefact("Веселий грибочок"));
        allArtefacts.add(ArtefactFactory.getArtefact("Писанка"));
        allArtefacts.add(ArtefactFactory.getArtefact("Артемівське"));
        allArtefacts.add(ArtefactFactory.getArtefact("Око Буданова"));
        allArtefacts.add(ArtefactFactory.getArtefact("Щасливий ремінь"));
        allArtefacts.add(ArtefactFactory.getArtefact("Залізний щит"));
    }

    private static void loadAllCards() {
        allCards = new ArrayList<>();
        allCards.add(CardFactory.createCard("Шалений хвіст"));
        allCards.add(CardFactory.createCard("Бинт та ліки"));
        allCards.add(CardFactory.createCard("Маневр"));
        allCards.add(CardFactory.createCard("Широкий розмах"));
        allCards.add(CardFactory.createCard("Укус"));
        allCards.add(CardFactory.createCard("Смаколик"));
        allCards.add(CardFactory.createCard("Собаче панування"));
        allCards.add(CardFactory.createCard("Хвіст-бумеранг"));
        allCards.add(CardFactory.createCard("Загострений хвіст"));
        allCards.add(CardFactory.createCard("Безглуздий удар"));
        allCards.add(CardFactory.createCard("У слабке місце"));
        allCards.add(CardFactory.createCard("Відчайдушний удар"));
        allCards.add(CardFactory.createCard("Різанина"));
        allCards.add(CardFactory.createCard("Айкідо"));
    }

    private static void makeStartDeck() {
        playerDeck = new ArrayList<>();
        playerDeck.add(CardFactory.createCard("Удар лапою"));
        playerDeck.add(CardFactory.createCard("Удар лапою"));
        playerDeck.add(CardFactory.createCard("Удар лапою"));
        playerDeck.add(CardFactory.createCard("Удар лапою"));
        playerDeck.add(CardFactory.createCard("Бронежилет"));
        playerDeck.add(CardFactory.createCard("Бронежилет"));
        playerDeck.add(CardFactory.createCard("Бронежилет"));
        playerDeck.add(CardFactory.createCard("Банка огірків"));

    }

    public static void start() {
        room = 0;
        player = new Player();
        events = new ArrayList<>();
        Card.player = player;
        Effect.player = player;
        Enemy.player = player;
        Fight.player = player;
        topPanel = new TopPanel();
        MoveDisplay.init();
        loadAllCards();
        loadAllArtefacts();
        loadStartEvents();
        makeStartDeck();
        next();
    }

    @Override
    public void create() {
        GameSound.init();
        settingsWindow = new SettingsWindow();
        CardActor.setFonts();
        menu = new Menu();
        setScreen(menu);
    }
}
