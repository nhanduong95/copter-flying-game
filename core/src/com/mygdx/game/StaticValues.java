package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.parse.ParseObject;

/**
 * Created by User on 28-Dec-15.
 */
public class StaticValues {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static float HORIZONTAL_FLOATING_SPEED = -200;
    public static float MUSIC_VOLUME = 50;
    public static float SOUND_VOLUME = 50;
    public static float MUSIC_PLAYING_POSITION;

    public static Player PLAYER = new Player();

    public static boolean START_NEW_GAME = false;
    public static boolean GAME_RUNNING = false;
    public static boolean GAME_STARTED = false;
    public static boolean GAME_OVER = false;
    public static boolean GAME_SAVED = false;

    public static AssetManager ASSET_MANAGER = new AssetManager();

    public static Music ADDITIONAL_MUSIC;
    public static Music GAME_MUSIC_JOURNEY;

    public static ParseObject SCORES_SAVED = null;

    public static ActionResolver ACTION_RESOLVER;
    public static FileHandler FILE_HANDLER;
}
