package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Camera.OrthoCamera;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.StaticValues;

/**
 * Created by Master on 12/22/2015.
 */
public class GameOverScreen extends Screen {
    private OrthoCamera camera;
    private TextureRegion gameOverTile;
    private TextureRegion gameOverInstruct;

    private BitmapFont playerScore;
    private BitmapFont internetStatus;

    private Sound sound;

    public GameOverScreen() {
        playerScore = StaticValues.ASSET_MANAGER.get(ConstantValues.FONT_BLOGGER_SANS_GREEN, BitmapFont.class);
        internetStatus = StaticValues.ASSET_MANAGER.get(ConstantValues.FONT_BLOGGER_SANS_RED, BitmapFont.class);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(ConstantValues.GAME_OVER_ATLAS));
        gameOverTile = atlas.findRegion(ConstantValues.GAME_OVER_TITLE);
        gameOverInstruct = atlas.findRegion(ConstantValues.GAME_OVER_INSTRUCT);
        if(StaticValues.GAME_MUSIC_JOURNEY != null && StaticValues.GAME_MUSIC_JOURNEY.isPlaying())
            StaticValues.GAME_MUSIC_JOURNEY.stop();
    }

    @Override
    public void create() {
        sound = StaticValues.ASSET_MANAGER.get(ConstantValues.SOUND_GAME_OVER, Sound.class);
        sound.play(StaticValues.SOUND_VOLUME);

        camera = new OrthoCamera();
        camera.resize();
    }

    private void checkInput() {
        if (Gdx.input.justTouched()){
            ScreenController.setCurrentScreen(new MenuScreen());
        }
    }

    @Override
    public void update() {
        camera.update();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        checkInput();
        spriteBatch.draw(ConstantValues.BACKGROUND, 0, 0);
        spriteBatch.draw(gameOverTile,
                Math.abs((StaticValues.SCREEN_WIDTH - gameOverTile.getRegionWidth())/2),
                Math.abs(StaticValues.SCREEN_HEIGHT - gameOverTile.getRegionHeight()) - 70);
        spriteBatch.draw(gameOverInstruct, StaticValues.SCREEN_WIDTH/2 - gameOverInstruct.getRegionWidth()/2,
                            StaticValues.SCREEN_HEIGHT/2 - gameOverTile.getRegionY());
        playerScore.draw(spriteBatch, "YOUR SCORE: " + String.valueOf(StaticValues.PLAYER.getScore()),
                        StaticValues.SCREEN_WIDTH/2 - 120, 130);
        if (!StaticValues.ACTION_RESOLVER.checkInternet()) {
            internetStatus.draw(spriteBatch, "No internet connection.Your score will be saved locally.", 250, 80);
        }
        spriteBatch.end();
    }


    @Override
    public void resize(int newWidth, int newHeight) {

    }

    @Override
    public void dispose() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
