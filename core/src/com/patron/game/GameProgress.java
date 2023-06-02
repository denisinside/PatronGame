package com.patron.game;

import com.badlogic.gdx.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class GameProgress extends Game {
    static ArrayList<Card> playerDeck;
    ArrayList<Card> allCards;
    public static Player player;
    boolean died = false;
    int room = 0;
    int lastEnemyIndex = -1;
    double commonChance = 0.6, rareChance = 0.3, legendaryChance = 0.1;
    int cardRewardAmount = 3;
    Random r = new Random();
    public GameProgress(){

    }
    private void generateFight(){
        ArrayList<Enemy> enemies = new ArrayList<>(Arrays.asList(getEnemiesForFight()));
        Fight fight1 = new Fight(enemies,playerDeck, this);
        setScreen(fight1);
        //if (fight1.loose) died = true;
        //else  getCardReward();
    }
        private Enemy[] getEnemiesForFight(){
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
    private void getCardReward(){
        System.out.println("Твоя нагорода за бій: ");
        Card[] choice = new Card[cardRewardAmount];
        ArrayList<Card> commons = allCards.stream().filter(x -> x.rarity == Rarity.COMMON).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Card> rares = allCards.stream().filter(x -> x.rarity == Rarity.RARE).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Card> legendary = allCards.stream().filter(x -> x.rarity == Rarity.LEGENDARY).collect(Collectors.toCollection(ArrayList::new));

        try {
            for (int i = 0; i < choice.length; i++){
                double random = Math.random();
                System.out.println(random);
                if (random >= 1-legendaryChance && !legendary.isEmpty()) choice[i] = (Card) legendary.remove(r.nextInt(legendary.size())).clone();
                else if (random >= 1-rareChance && !rares.isEmpty()) choice[i] = (Card) rares.remove(r.nextInt(rares.size())).clone();
                else choice[i] = (Card) commons.remove(r.nextInt(commons.size())).clone();
            }
        }catch (CloneNotSupportedException ignored){}
        int index = 0;
        for (Card card : choice){
            System.out.println(++index + ". " + card);
        }
        System.out.println("Напиши яку карту хочеш обрати або інше число щоб скіпнути");
        index = DataInput.getInt()-1;
        if(index < 0 || index > choice.length) return;
        else playerDeck.add(choice[index]);
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
        allCards.add(CardFactory.createCard("Подвійний удар"));
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
        loadAllCards();
        makeStartDeck();

       //do {
            generateFight();
       // } while (!died);

    }
}
