package com.patron.game;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;
import java.util.Collections;

public class Fight implements Screen {
    Game game;
    int move = 1;
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static  Player player;
    static ArrayList<Card> inHand= new ArrayList<>();
    static ArrayList<Card> draw= new ArrayList<>();
    static  ArrayList<Card> discard= new ArrayList<>();

    public Fight(ArrayList<Enemy> enemies, ArrayList<Card> playerDeck, Game game){
        draw = playerDeck;
        Fight.enemies = enemies;
        Collections.shuffle(draw);

        background = new Texture(Gdx.files.internal("background1.png"));
        batch = new SpriteBatch();
        stage = new Stage();
        cardActors = new Group();
        enemiesActors = new Group();
        Gdx.input.setInputProcessor(stage);

        playing();
    }
    boolean moveEnd = false;
    boolean win, loose  = false;
    public void playing(){
//        while (!win && !loose) {
//            player.setArmor(0);
//            effectCheck();
//            cardDeal(player.getCardPerRound());
//            moveEnd = false;
//            while (!moveEnd) {
//                printStats();
//                chooseCard();
//                checkKill();
//                checkFinal();
//            }
//            moveEnd();
//            enemyMove();
//            move++;
//        }
//        player.effects.clear();

        cardDeal(player.getCardPerRound());
    }

    private void moveEnd(){
        for (Enemy enemy : enemies) enemy.setArmor(0);
        for (Card card : inHand){
            if (card.rarity == Rarity.CURSE || card.rarity == Rarity.STATUS) card.use(null,null);
        }
        inHand.removeIf(card -> card.ghostly);
        discard.addAll(inHand);
        inHand.clear();
        player.setEnergy(player.getMaxEnergy());
    }
    private void checkFinal(){
        if (enemies.size() == 0){
            moveEnd = true;
            win = true;
        } else if (player.getHealth() <= 0){
            moveEnd = true;
            loose = true;
        }
    }
    private void checkKill(){
        enemies.removeIf(enemy -> enemy.getHealth() <= 0);
    }
    private void chooseCard(){
        System.out.println("Вибери карту за номером, якщо хочеш пропустити хід, то напиши інше число");
        for(int i = 0; i < inHand.size(); i++){
            System.out.println(i+1 + ". " + inHand.get(i));
        }
        int choice = DataInput.getInt();
        if (choice > inHand.size() || choice < 1 ) moveEnd = true;
        else if (inHand.get(choice-1).costNow > player.getEnergy()) System.out.println("Недостатньо енергії");
        else if(!inHand.get(choice-1).playable) System.out.println("Цю карту не можна грати");
        else useCard(choice-1);
    }

    private void useCard(int cardIndex) {
        Card card = inHand.get(cardIndex);
        switch (card.getClass().getSimpleName()) {
            case "AttackCard":
                if (card.target.equals("one")) {
                    card.use(chooseEnemy(), enemies);
                } else {
                    card.use(null, enemies);
                }
                break;

            case "SkillCard":
                switch (card.type) {
                    case DEFENSE:
                    case HEAL:
                        card.use(null, enemies);
                        break;
                }
        }
        player.setEnergy(player.getEnergy() - card.getCostNow());
        if (card.burning) inHand.remove(card);
        else if (card.disposable) {
            inHand.remove(card);
            GameProgress.playerDeck.remove(card);
        } else {
            discard.add(card);
            inHand.remove(card);
        }
    }
    private  void  effectCheck(){
        player.effectCheck();
        for (Enemy enemy : enemies){
            enemy.effectCheck();
            checkKill();
        }
    }
    private void enemyMove(){
        for (Enemy enemy : enemies){
            if (player.getHealth() > 0)enemy.makeMove();
            else loose = true;
        }
    }
    private void printStats(){
        System.out.println("\n\n\n\n\n\n\n\n");
        player.printStats();
        for (Enemy enemy : enemies) enemy.printStats();
    }
    private Enemy chooseEnemy(){
        System.out.println("Вибери ворога, на якого хочеш використати карту: ");
        for(int i = 0; i < enemies.size(); i++){
            System.out.print(i+1 + ". "); enemies.get(i).printStats();
        }
        int choice = -1;
        while (choice < 1 || choice > enemies.size()) {
            choice = DataInput.getInt();
        }
        return enemies.get(choice-1);
    }

    protected static void cardDeal(int amount){
        int step = amount % 2 == 1 ? (amount - 1)*(-210) + 100 :(amount - 2)*(-210);
        for (int i = 0; i < amount; i++){
            step += 210;
            if (draw.size() == 0) shuffleDeck();
            inHand.add(draw.get(0));
//            addCardActorToGroup(draw.get(0));
            draw.get(0).cardActor.setPos(step + Gdx.graphics.getWidth() /2, 10);
            draw.get(0).cardActor.number = i;
            cardActors.addActor(draw.get(0).cardActor);

            draw.remove(0);
        }
    }
    //фігня метод вийшов
    public static void addCardActorToGroup(Card card){
        SnapshotArray<Actor> cardActorsTemp = cardActors.getChildren();
        int step = (cardActorsTemp.size+1) % 2 == 1 ? (cardActorsTemp.size)*(-210) + 100 :(cardActorsTemp.size - 1)*(-210);
        cardActors.clear();
        for (Actor actor : cardActorsTemp){
            step += 210;
            cardActors.addActor(actor);
            ((CardActor) actor).setPos(step + Gdx.graphics.getWidth() /2, 30);
        }
        cardActors.addActor(card.cardActor);
        card.cardActor.setPos(step + Gdx.graphics.getWidth() /2, 30);
    }
    private static void shuffleDeck(){
        draw.clear();
        Collections.shuffle(discard);
        draw.addAll(discard);
        discard.clear();

    }
    public static Card getCardFromDiscard(){
        if (discard.size() == 0) return null;
        int index = 1;
        for (Card c : discard) System.out.println(index++ + ". " + c);
        int choice = DataInput.getInt();
        while (choice > discard.size() || discard.size() < 1) choice = DataInput.getInt();
        Card c = discard.get(choice-1);
        discard.remove(c);
        return c;
    }
    SpriteBatch batch;
    Texture background;
    static Stage stage;
    static Group cardActors;
    static Group enemiesActors;

    private void addEnemiesToGroup(){
        int y = Gdx.graphics.getHeight()/3;
        int x = Gdx.graphics.getWidth()/2 + 100;
        for (Enemy enemy : enemies){
             enemiesActors.addActor(enemy.enemyActor);
             enemy.enemyActor.setPosition(x,y);
             x += enemy.enemyActor.width*1.2;
        }
    }

    @Override
    public void show() {
        stage.addActor(enemiesActors);
        stage.addActor(cardActors);
        stage.addListener(new FightInputController(cardActors));
        addEnemiesToGroup();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        batch.end();

        stage.act();
        stage.draw();
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
    public void dispose () {
        batch.dispose();
        stage.dispose();
    }
}
class FightInputController extends InputListener{
    Group cardActors;
    public FightInputController(Group cardActors){
        this.cardActors = cardActors;
    }
    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            if (keycode - Input.Keys.NUM_1 < cardActors.getChildren().size) {
                CardActor selectedActor = null;
                for (Actor cardActor : cardActors.getChildren()) {
                    if (((CardActor) cardActor).number == keycode - Input.Keys.NUM_1)  selectedActor = (CardActor) cardActor;
                }
                if (selectedActor != null)selectedActor.select();
            }
        }
        return super.keyDown(event, keycode);
    }
}