package com.patron.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class GameProgress extends Game {
    static ArrayList<Card> playerDeck;
    public static ArrayList<Card> allCards;
    public static Player player;
    boolean died = false;
    static int room = 0;
    static int lastEnemyIndex = -1;
    //double commonChance = 0.6, rareChance = 0.3, legendaryChance = 0.1;
    int cardRewardAmount = 3;
    static Random r = new Random();
    public static Game game;
    public static TopPanel topPanel;
    public GameProgress(){
        game = this;
    }
    public static Screen generateFight(){
        ArrayList<Enemy> enemies = new ArrayList<>(Arrays.asList(getEnemiesForFight()));
        Fight fight1 = new Fight(enemies,playerDeck,game);
        return fight1;
    }
        private static Enemy[] getEnemiesForFight(){
            int enemiesIndex;
            if(room < 10){
                do {
                    enemiesIndex = r.nextInt(5);
                }while (enemiesIndex == lastEnemyIndex);
                lastEnemyIndex = enemiesIndex;
                switch (enemiesIndex){
                    case 0: return new Enemy[]{
                            new RadioactiveRat(),
                            new RadioactiveRat()
                    };
                    case 1: return new Enemy[]{
                            new RadioactiveRat(),
                            new Bandit()
                    };
                    case 2: return new Enemy[]{
                            new FireBagTank(),
                            new FireBagSoldier(),
                            new FireBagMedic()
                    };
                    case 3: return new Enemy[]{
                            new FireBagTank(),
                            new FireBagSniper()
                    };
                    case 4: return new Enemy[]{
                            new Bandit(),
                            new Bandit(),
                            new FireBagSoldier()
                    };
                }
            }
            return null;
        }

    private void loadAllCards(){
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
    private void makeStartDeck(){
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

    @Override
    public void create() {
        CardActor.setFonts();
        player = new Player();
        ArrayList<Enemy> enemies = new ArrayList<>();
        Card.player = player;
        Effect.player = player;
        Enemy.player = player;
        Fight.player = player;
        topPanel = new TopPanel();
        MoveDisplay.init();
        loadAllCards();
        makeStartDeck();

        setScreen(new ChoiceYourWayEvent());
       //do {
        //    setScreen(generateFight());
       // } while (!died);

    }
}
