package com.patron.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Fonts {
/*
* !!! - Морісель шрифти
* !!! - Лисичка шрифти
*/
    static FreeTypeFontGenerator albionicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Albionic.ttf"));
    static FreeTypeFontGenerator maurysselBoldFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Mauryssel_Bold.ttf"));
    static FreeTypeFontGenerator lisichkaComicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Lisichka_comic.ttf"));

    public static final String characters = "0123456789()+-=/*!?`%;',.АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯЇїІіЄєабвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    static FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();


    public static final BitmapFont ALBIONIC_BASIC_NAME = setAlbionicBasicName();
    public static final BitmapFont ALBIONIC_LARGE_NAME = setAlbionicLargeName();
    public static final BitmapFont ALBIONIC_BASIC_DESC = setAlbionicBasicDesc();
    public static  final BitmapFont ALBIONIC_LARGE_DESC = setAlbionicLargeDesc();
    public static  final BitmapFont ATTACK_FONT = setAttackFont();
    public static  final BitmapFont ICON_NUMBERS = setIconFont();
    public static  final BitmapFont VALUE_NUMBERS = setValueFont();

    private static BitmapFont setValueFont() {
        BitmapFont basic;

        fontParameter.characters = characters;

        fontParameter.size = 38;
        fontParameter.borderWidth = 3;
        basic = maurysselBoldFontGenerator.generateFont(fontParameter);

        return basic;
    }

    private static BitmapFont setIconFont() {
        BitmapFont basic;

        fontParameter.characters = characters;

        fontParameter.size = 18;
        fontParameter.borderWidth = 1;
        basic = maurysselBoldFontGenerator.generateFont(fontParameter);

        return basic;
    }

    private static BitmapFont setAttackFont(){
        BitmapFont basic;
        fontParameter.characters = characters;

        fontParameter.size = 28;
        fontParameter.borderWidth = 4;
        basic = maurysselBoldFontGenerator.generateFont(fontParameter);

        return basic;
    }
    private static BitmapFont setAlbionicBasicName(){
        BitmapFont basic;

        fontParameter.characters = characters;
        fontParameter.size = 18;
        fontParameter.borderWidth = 2;
        basic = albionicFontGenerator.generateFont(fontParameter);
        return basic;
    }

    public static BitmapFont setAlbionicLargeName(){
        BitmapFont large;

        fontParameter.characters = characters;
        fontParameter.borderWidth = 3;
        fontParameter.size = 24;
        large = albionicFontGenerator.generateFont(fontParameter);
        return large;
    }

    public static BitmapFont setAlbionicBasicDesc(){
        BitmapFont basic;

        fontParameter.characters = characters;
        fontParameter.borderWidth = 1;
        fontParameter.size = 14;
        basic = albionicFontGenerator.generateFont(fontParameter);
        return basic;
    }

    public static BitmapFont setAlbionicLargeDesc(){
        BitmapFont large;

        fontParameter.characters = characters;
        fontParameter.borderWidth = 2;
        fontParameter.size = 18;
        large = albionicFontGenerator.generateFont(fontParameter);
        return large;
    }
}

