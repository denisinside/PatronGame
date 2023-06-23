package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Menu implements Screen {
    Image background;
    NextMoveButton startGameButton, settingsButton, exitButton;
    Stage stage;
    SettingsWindow settingsWindow;

    public Menu() {

        background = new Image(new Sprite(new Texture(Gdx.files.internal("MenuBackground.jpg"))));
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        startGameButton = new NextMoveButton("Грати");
        settingsButton = new NextMoveButton("Налаштування");
        exitButton = new NextMoveButton("Вийти");

        startGameButton.positionX = Gdx.graphics.getWidth() / 10f;
        startGameButton.positionY = Gdx.graphics.getHeight() / 1.5f;
        settingsButton.positionX = Gdx.graphics.getWidth() / 10f;
        settingsButton.positionY = Gdx.graphics.getHeight() / 2f;
        exitButton.positionX = Gdx.graphics.getWidth() / 10f;
        exitButton.positionY = Gdx.graphics.getHeight() / 3f;

        startGameButton.buttonWidth = 500;
        settingsButton.buttonWidth = 500;
        exitButton.buttonWidth = 500;

        stage = new Stage();
        stage.addActor(background);
        stage.addActor(startGameButton);
        stage.addActor(settingsButton);
        stage.addActor(exitButton);

        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameProgress.start();
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameProgress.settingsWindow.addAction(Actions.show());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        settingsWindow = new SettingsWindow();
        stage.addActor(GameProgress.settingsWindow);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        GameSound.menuMusic.play();
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

        GameSound.menuMusic.stop();
    }

    @Override
    public void dispose() {

    }
}

class SettingsWindow extends Group {
    Image windowBackground;
    Image musicVolumeLess, musicIcon, musicVolumeMore;
    Image soundVolumeLess, soundIcon, soundVolumeMore;
    NextMoveButton exit, close;

    public SettingsWindow() {
        windowBackground = new Image(new Sprite(new Texture(Gdx.files.internal("settings_window.png"))));
        windowBackground.setSize(500, 400);
        windowBackground.setPosition(Gdx.graphics.getWidth() / 2f - windowBackground.getWidth() / 2, Gdx.graphics.getHeight() / 2f - windowBackground.getHeight() / 2);
        addActor(windowBackground);

        musicIcon = new Image(new Sprite(new Texture(Gdx.files.internal("music_on.png"))));
        musicIcon.setSize(100, 100);
        musicIcon.setPosition(windowBackground.getX() + windowBackground.getWidth() / 2 - musicIcon.getWidth() / 2,
                windowBackground.getY() + windowBackground.getHeight() - musicIcon.getHeight() * 1.5f);

        musicVolumeLess = new Image(new Sprite(new Texture(Gdx.files.internal("volume_less.png"))));
        musicVolumeLess.setSize(80, 100);
        musicVolumeLess.setPosition(musicIcon.getX() - musicVolumeLess.getWidth() * 1.5f,
                windowBackground.getY() + windowBackground.getHeight() - musicIcon.getHeight() * 1.5f);
        musicVolumeLess.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSound.musicVolume - GameSound.volumeStep >= 0)
                    GameSound.musicVolume -= GameSound.volumeStep;
            }
        });

        musicVolumeMore = new Image(new Sprite(new Texture(Gdx.files.internal("volume_more.png"))));
        musicVolumeMore.setSize(80, 100);
        musicVolumeMore.setPosition(musicIcon.getX() + musicIcon.getWidth() + musicVolumeMore.getWidth() - musicIcon.getWidth() / 2,
                windowBackground.getY() + windowBackground.getHeight() - musicIcon.getHeight() * 1.5f);
        musicVolumeMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSound.musicVolume += GameSound.volumeStep;
            }
        });

        addActor(musicIcon);
        addActor(musicVolumeLess);
        addActor(musicVolumeMore);


        soundIcon = new Image(new Sprite(new Texture(Gdx.files.internal("sound_on.png"))));
        soundIcon.setSize(100, 100);
        soundIcon.setPosition(windowBackground.getX() + windowBackground.getWidth() / 2 - soundIcon.getWidth() / 2,
                musicIcon.getY() - soundIcon.getHeight() * 1.2f);

        soundVolumeLess = new Image(new Sprite(new Texture(Gdx.files.internal("volume_less.png"))));
        soundVolumeLess.setSize(80, 100);
        soundVolumeLess.setPosition(soundIcon.getX() - soundVolumeLess.getWidth() * 1.5f,
                soundIcon.getY());
        soundVolumeLess.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSound.soundVolume - GameSound.volumeStep >= 0)
                    GameSound.soundVolume -= GameSound.volumeStep;
            }
        });

        soundVolumeMore = new Image(new Sprite(new Texture(Gdx.files.internal("volume_more.png"))));
        soundVolumeMore.setSize(80, 100);
        soundVolumeMore.setPosition(soundIcon.getX() + soundIcon.getWidth() + soundVolumeMore.getWidth() - soundIcon.getWidth() / 2,
                soundIcon.getY());
        soundVolumeMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSound.soundVolume += GameSound.volumeStep;
            }
        });

        addActor(soundIcon);
        addActor(soundVolumeLess);
        addActor(soundVolumeMore);

        close = new NextMoveButton("Закрити");
        close.positionX = windowBackground.getX() + 20;
        close.positionY = windowBackground.getY() + 20;
        close.buttonWidth = 200;
        close.button.setAlpha(50);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addAction(Actions.hide());
            }
        });


        exit = new NextMoveButton("Вийти");
        exit.positionX = windowBackground.getX() + windowBackground.getWidth() - 20 - 200;
        exit.positionY = windowBackground.getY() + 20;
        exit.buttonWidth = 200;
        exit.button.setAlpha(0);
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        addActor(close);
        addActor(exit);

        addAction(Actions.hide());

    }


}