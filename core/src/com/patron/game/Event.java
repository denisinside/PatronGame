package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;



public class Event implements Screen {
    Sprite eventSprite;
    Sprite background;
    String eventName;
    String eventDescription;
    String eventResult;
    Array<EventButton> buttons;
    SpriteBatch batch;
    Stage stage;
    Label name,description;

    public Event(String eventName, String eventDescription){
        this.eventDescription = eventDescription;
        batch = new SpriteBatch();
        this.eventName = eventName;
        this.background = new Sprite(new Texture(Gdx.files.internal("event_template.png")));
        buttons = new Array<>();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(GameProgress.topPanel);
        background.setBounds(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        name = new Label(eventName,new Label.LabelStyle(Fonts.MAURYSSEL_LARGE,Color.GOLD));
        name.setAlignment(Align.center);
        name.setBounds(Gdx.graphics.getWidth()/5f,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/3.7f,Gdx.graphics.getWidth()/1.5f,80);
        stage.addActor(name);

        description = new Label(eventDescription,new Label.LabelStyle(Fonts.MAURYSSEL_LARGE,Color.WHITE));
        description.setAlignment(Align.topLeft);
        description.setWrap(true);
        description.setBounds(Gdx.graphics.getWidth()/2.2f,Gdx.graphics.getHeight()/2f,Gdx.graphics.getWidth()/2.5f,Gdx.graphics.getHeight()/4.5f);
        stage.addActor(description);

       // stage.setDebugAll(true);

    }
    protected void addButtonsToStage(){
        for (EventButton eventButton : buttons) stage.addActor(eventButton);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();

        if (eventSprite != null) eventSprite.draw(batch);
        background.draw(batch);

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
    public void dispose() {

    }

    protected void firstChoice(){

    }
    protected void secondChoice(){

    }
    protected void thirdChoice(){

    }

    static class EventButton extends Actor{
        Sprite button, buttonHovered;
        Label choiceLabel,bonusLabel,penaltyLabel;
        boolean hovered;
        int choiceNumber;
        Event event;
        Tooltip tooltip;
        public EventButton(String choiceText,String bonusText,String penaltyText,int choiceNumber, Event event){
            button = new Sprite(new Texture(Gdx.files.internal("event_button.png")));
            buttonHovered = new Sprite(new Texture(Gdx.files.internal("event_button_hovered.png")));

            this.choiceNumber = choiceNumber;
            this.event = event;
            setBounds((float) (Gdx.graphics.getWidth()/2.2),Gdx.graphics.getHeight()/5 + 75 * (choiceNumber-1),800,75);

            choiceLabel = new Label("[" + choiceText + "]",new Label.LabelStyle(Fonts.MAURYSSEL_BASIC, Color.WHITE));
            if (bonusText!=null) bonusLabel = new Label(bonusText,new Label.LabelStyle(Fonts.MAURYSSEL_BASIC, Color.GREEN));
            if (penaltyText!=null) penaltyLabel = new Label(penaltyText,new Label.LabelStyle(Fonts.MAURYSSEL_BASIC, Color.RED));
            addListener(new InputListener(){
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
                    if (button == Input.Buttons.LEFT){
                        if (choiceNumber == 1) EventButton.this.event.firstChoice();
                        if (choiceNumber == 2) EventButton.this.event.secondChoice();
                        if (choiceNumber == 3) EventButton.this.event.thirdChoice();
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
            choiceLabel.draw(batch,parentAlpha);
            if (bonusLabel!=null) bonusLabel.draw(batch,parentAlpha);
            if (penaltyLabel!=null) penaltyLabel.draw(batch,parentAlpha);
            if (tooltip != null) tooltip.draw(batch,parentAlpha);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            button.setBounds(getX(),getY(),getWidth(),getHeight());
            buttonHovered.setBounds(getX(),getY(),getWidth(),getHeight());
            choiceLabel.setPosition(getX()+getWidth()/20,getY()+choiceLabel.getHeight());
            if (bonusLabel != null) bonusLabel.setPosition(10 + choiceLabel.getX() + choiceLabel.getWidth(), choiceLabel.getY());
            if (penaltyLabel != null) penaltyLabel.setPosition(
                    bonusLabel == null ? 10 + choiceLabel.getX() + choiceLabel.getWidth() :10 + bonusLabel.getX() + bonusLabel.getWidth(), choiceLabel.getY());
            if (tooltip != null) tooltip.act(delta);
        }
    }
}
class ChoiceYourWayEvent extends Event{

    public ChoiceYourWayEvent() {
        super("Обери свій шлях",
                "Патрон підходить до кімнати з двома таємничими дверима." +
                        " На першій двері читається напис \"Перемога\" з яскравим і блискучим шрифтом," +
                        " що виблискує в променях світла. На другій двері виводиться напис \"Зрада\"" +
                        " глибокими червоними літерами, що випливають з-під темряви." +
                        "\nКуди вирушити Патрону?");

        EventButton first = new EventButton("Зрада","Отримуєте 100 золота","Отримуєте прокляття \"Зрада\"",1,this);
        first.setTooltip(new CardTooltip(CardFactory.getCurse("Зрада"),first));

        EventButton second = new EventButton("Перемога","Отримуєте реліквію \"Трофей\"","Бій з ворогом",2,this);
        second.setTooltip(new Tooltip("Трофей","Всі вороги на першому ході отримують 1 слабкість",second));

        eventSprite = new Sprite(new Texture(Gdx.files.internal("icons\\EventSprites\\ChoiceYourWayEvent.png")));
        buttons.add(first,second);

        addButtonsToStage();
        eventSprite.setBounds(Gdx.graphics.getWidth()/10f,Gdx.graphics.getHeight()/8f,Gdx.graphics.getWidth()/2.8f,Gdx.graphics.getWidth()/2.8f);

    }
    @Override
    protected void firstChoice(){

    }

    @Override
    protected void secondChoice() {

    }
}
