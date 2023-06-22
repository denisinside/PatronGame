package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.patron.game.GameProgress.player;

public class Treasury implements Screen {
    Image background;
    Artefact artefact;
    Image treasury;
    Stage stage;
    RewardScene rewardScene;
    boolean clicked;
    public Treasury(Artefact reward){
        this.background = new Image(new Sprite(GameProgress.getRandomBackground()));
        this.background.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        artefact = reward;
    }
    @Override
    public void show() {

        stage = new Stage();
        stage.addActor(this.background);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(GameProgress.topPanel);
        GameProgress.topPanel.addElementsWithListeners(stage);

        stage.addActor(player.actor);
        player.actor.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight() / 4f);
        treasury = new Image(new Sprite(new Texture(Gdx.files.internal("treasury.png"))));
        stage.addActor(treasury);
        treasury.setBounds(Gdx.graphics.getWidth()/2f,player.actor.getY(),200,200);

        treasury.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rewardScene = new RewardScene(false,0, artefact);
                Gdx.input.setInputProcessor(rewardScene);
                rewardScene.addAction(Actions.fadeIn(0.2f));
                clicked = true;
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();

        if (clicked){
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

    }

}
