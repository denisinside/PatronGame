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

public class FinalRoom implements Screen {
    Image background, comics,explosives;
    Stage stage;
    @Override
    public void show() {
        GameSound.wayMusic.play();
        stage = new Stage();
        background = new Image(new Sprite(new Texture(Gdx.files.internal("backgrounds\\background12.png"))));
        background.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        comics = new Image(new Sprite(new Texture(Gdx.files.internal("final_comics.jpg"))));
        comics.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        comics.addAction(Actions.fadeOut(0.01f));
        comics.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameProgress.game.setScreen(GameProgress.menu);
            }
        });

        explosives = new Image(new Sprite(new Texture(Gdx.files.internal("explosives.png"))));
        explosives.setBounds(Gdx.graphics.getWidth()/2f -200, Gdx.graphics.getHeight()/4f,400,300);
        explosives.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                explosives.remove();
                comics.addAction(Actions.sequence(
                        Actions.fadeIn(3f)
                ));
            }
        });

        stage.addActor(background);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(GameProgress.topPanel);
        GameProgress.topPanel.addElementsWithListeners(stage);
        stage.addActor(comics);
        stage.addActor(explosives);
        stage.addAction(Actions.fadeIn(2));
    }

    @Override
    public void render(float delta) {
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

        GameSound.wayMusic.stop();
    }

    @Override
    public void dispose() {

    }
}
