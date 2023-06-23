package com.patron.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class GameSound {
    public static float soundVolume = 0.44f;
    public static float musicVolume = 0.24f;
    public static Sound rewardSound;
    public static Sound buySound;
    public static Sound buttonSound;
    public static Sound shuffleSound;
    public static Sound shopSound;
    public static Sound goldSound;
    public static Music wayMusic;
    public static Music menuMusic;
    public static Music fightMusic;

    public static float volumeStep = 0.04f;

    public static void init(){
        rewardSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\reward.wav"));
        buySound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Buy.mp3"));
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Button.mp3"));
        shuffleSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Shuffle.mp3"));
        shopSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Shop.mp3"));
        goldSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Gold.wav"));

        randomFightMusic();

        wayMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\TheFirstLayer.mp3"));
        wayMusic.setLooping(true);

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\MenuTheme.mp3"));
        menuMusic.setLooping(true);
    }
    public static void randomFightMusic(){
        switch (MathUtils.random(3)){
            case 0:
                fightMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\LoveIsJustice.mp3"));
            case 1:
                fightMusic =  Gdx.audio.newMusic(Gdx.files.internal("music\\AllOutAttacks.mp3"));
            case 2:
                fightMusic =  Gdx.audio.newMusic(Gdx.files.internal("music\\Area11.mp3"));
            default:
            case 3:
                fightMusic =  Gdx.audio.newMusic(Gdx.files.internal("music\\WhereAmI.mp3"));
        }
    }
    public static Sound getAttackSound(){
        return Gdx.audio.newSound(Gdx.files.internal("sounds\\Attack"+(MathUtils.random(3)+1)+".mp3"));
    }
    public static void updateVolume(){
        menuMusic.setVolume(musicVolume);
        fightMusic.setVolume(musicVolume);
        wayMusic.setVolume(musicVolume);
    }

}
