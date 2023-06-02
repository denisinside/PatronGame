package com.patron.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.ScreenUtils;

public class PatronGame extends Game {
	SpriteBatch batch;
	Texture background;
	Stage stage;
	
	@Override
	public void create () {
		com.patron.game.Card.player = new Player();
		CardActor.setFonts();

		background = new Texture(Gdx.files.internal("background1.png"));
		batch = new SpriteBatch();
		stage = new Stage();
		CardActor cardActor = new CardActor(CardFactory.createCard("Укус"), 300,300);
		stage.addActor(cardActor);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();

		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		batch.end();

		stage.act();
		stage.draw();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
	}
}
