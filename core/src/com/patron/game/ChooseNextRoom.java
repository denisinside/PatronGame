package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;


public class ChooseNextRoom implements Screen {
    Stage stage;
    Screen firstChoice,secondChoice;
    Image firstChoiceIcon,secondChoiceIcon;
    Label firstChoiceLabel,secondChoiceLabel;
    public ChooseNextRoom(Screen firstChoice,Screen secondChoice){
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
        Image background = new Image(new Sprite(new Texture(Gdx.files.internal("next_location.png"))));
        background.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        stage = new Stage();
        stage.addActor(background);

        if (firstChoice instanceof Fight){
            if (((Fight)firstChoice).currentReward == null) {
                if (((Fight) firstChoice).isEventFight){
                    firstChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("IMPACT.png"))));
                    firstChoiceLabel = new Label("Невідома подія",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
                }else {
                    firstChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("ATTACK.png"))));
                    firstChoiceLabel = new Label("Бійка", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
                }
            }else {
                firstChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("Artefacts\\Skull.png"))));
                firstChoiceLabel = new Label("Елітний ворог",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
            }
        }else if (firstChoice instanceof Shop){
            if (((Shop) firstChoice).isEventShop) {
                firstChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("IMPACT.png"))));
                firstChoiceLabel = new Label("Невідома подія",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
            }else {
                firstChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("gold_reward.png"))));
                firstChoiceLabel = new Label("Магазин", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
            }
        }else if (firstChoice instanceof Event){
            firstChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("IMPACT.png"))));
            firstChoiceLabel = new Label("Невідома подія",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
        }else if (firstChoice instanceof Treasury){
            firstChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("treasury.png"))));
            firstChoiceLabel = new Label("Скарбниця",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
        }

        if (secondChoice instanceof Fight){
            if (((Fight)secondChoice).currentReward == null) {
                if (((Fight) secondChoice).isEventFight){
                    secondChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("IMPACT.png"))));
                    secondChoiceLabel = new Label("Невідома подія",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
                }else {
                    secondChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("ATTACK.png"))));
                    secondChoiceLabel = new Label("Бійка", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
                }
            }else {
                secondChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("Artefacts\\Skull.png"))));
                secondChoiceLabel = new Label("Елітний ворог",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
            }
        }else if (secondChoice instanceof Shop){
            if (((Shop) secondChoice).isEventShop) {
                secondChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("IMPACT.png"))));
                secondChoiceLabel = new Label("Невідома подія",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
            }else {
                secondChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("gold_reward.png"))));
                secondChoiceLabel = new Label("Магазин", new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
            }
        }else if (secondChoice instanceof Event){
            secondChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("IMPACT.png"))));
            secondChoiceLabel = new Label("Невідома подія",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
        }else if (secondChoice instanceof Treasury){
            secondChoiceIcon = new Image(new Sprite(new Texture(Gdx.files.internal("treasury.png"))));
            secondChoiceLabel = new Label("Скарбниця",new Label.LabelStyle(Fonts.ICON_NUMBERS, Color.WHITE));
        }

        firstChoiceIcon.setBounds(Gdx.graphics.getWidth()*0.26f,Gdx.graphics.getHeight()*0.5f,128,128);
        firstChoiceLabel.setBounds(firstChoiceIcon.getX(),firstChoiceIcon.getY()-15,firstChoiceIcon.getWidth(),10);
        firstChoiceLabel.setAlignment(Align.center);

        secondChoiceIcon.setBounds(Gdx.graphics.getWidth()*0.62f,Gdx.graphics.getHeight()*0.5f,128,128);
        secondChoiceLabel.setBounds(secondChoiceIcon.getX(),secondChoiceIcon.getY()-15,secondChoiceIcon.getWidth(),10);
        secondChoiceLabel.setAlignment(Align.center);

        stage.addActor(secondChoiceIcon);
        stage.addActor(firstChoiceIcon);
        stage.addActor(secondChoiceLabel);
        stage.addActor(firstChoiceLabel);


        secondChoiceIcon.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSound.buttonSound.setVolume(GameSound.buttonSound.play(),GameSound.soundVolume);
                dispose();
                if (secondChoice instanceof Event){
                    System.out.println(2222);
                    GameProgress.events.removeIf(event1 -> event1.name.getText().equals(((Event)secondChoice).name.getText()));
                }

                GameProgress.game.setScreen(secondChoice);
            }
        });
        firstChoiceIcon.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSound.buttonSound.setVolume(GameSound.buttonSound.play(),GameSound.soundVolume);
                dispose();
                if (firstChoice instanceof Event){
                    System.out.println(1111);
                    GameProgress.events.removeIf(event1 -> event1.name.getText().equals(((Event)firstChoice).name.getText()));
                }
                GameProgress.game.setScreen(firstChoice);
            }
        });
    }
    @Override
    public void show() {
        if (!GameSound.wayMusic.isPlaying())
            GameSound.wayMusic.play();

        Gdx.input.setInputProcessor(stage);
        stage.addActor(GameProgress.topPanel);
        GameProgress.topPanel.addElementsWithListeners(stage);

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
    stage.dispose();
    }
}
