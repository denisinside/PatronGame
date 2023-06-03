package com.patron.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.patron.game.PatronGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setResizable(false);
//		config.setWindowedMode(1920,1080);
		config.setTitle("Patron Game");
		config.setMaximized(true);

		new Lwjgl3Application(new GameProgress(), config);
	}
}
