package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Screens.MenuScreen;
import com.mygdx.game.Screens.ScreenController;

/**
 * Created by Master on 12/18/2015.
 */
public class GameExecution extends ApplicationAdapter {
    private SpriteBatch spriteBatch;

    public GameExecution(ActionResolver actionResolver, FileHandler fileHandler) {
        StaticValues.ACTION_RESOLVER = actionResolver;
        StaticValues.FILE_HANDLER = fileHandler;
    }

    @Override
    public void create () {
        StaticValues.ASSET_MANAGER.load(ConstantValues.MUSIC_JOURNEY, Music.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.MUSIC_MENU, Music.class);

        StaticValues.ASSET_MANAGER.load(ConstantValues.SOUND_COPTER_TAP, Sound.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.SOUND_CRASH, Sound.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.SOUND_EXPLOSION, Sound.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.SOUND_PICKUP, Sound.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.SOUND_WARNING, Sound.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.SOUND_GAME_OVER, Sound.class);

        StaticValues.ASSET_MANAGER.load(ConstantValues.FONT_AHARONI_BIGGER, BitmapFont.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.FONT_AHARONI_SMALLER, BitmapFont.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.FONT_BLOGGER_SANS_YELLOW, BitmapFont.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.FONT_BLOGGER_SANS_GREEN, BitmapFont.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.FONT_BLOGGER_SANS_RED, BitmapFont.class);
        StaticValues.ASSET_MANAGER.load(ConstantValues.FONT_ROCKWELL, BitmapFont.class);

        StaticValues.ASSET_MANAGER.finishLoading();

        spriteBatch = new SpriteBatch();
        ScreenController.setCurrentScreen(new MenuScreen());
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (ScreenController.getCurrentScreen() != null) {
            ScreenController.getCurrentScreen().update();
            ScreenController.getCurrentScreen().render(spriteBatch);
        }
    }

    @Override
    public void dispose(){
        if (ScreenController.getCurrentScreen() != null)
            ScreenController.getCurrentScreen().dispose();
        spriteBatch.dispose();
    }

    @Override
    public void resize(int width, int height){
        if (ScreenController.getCurrentScreen() != null)
            ScreenController.getCurrentScreen().resize(width, height);
    }

    @Override
    public void pause(){
        if (ScreenController.getCurrentScreen() != null)
            ScreenController.getCurrentScreen().pause();

    }

    @Override
    public void resume(){
        if (ScreenController.getCurrentScreen() != null)
            ScreenController.getCurrentScreen().resume();
    }

}
