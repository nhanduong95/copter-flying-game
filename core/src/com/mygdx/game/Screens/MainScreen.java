//package com.mygdx.game.Screens;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.mygdx.game.Camera.OrthoCamera;
//import com.mygdx.game.ConstantValues;
//import com.mygdx.game.Objects.GameController;
//import com.mygdx.game.Player;
//import com.mygdx.game.StaticValues;
//
///**
// * Created by Master on 12/18/2015.
// */
//public class MainScreen extends Screen {
//    BitmapFont textOnScreen;
//    BitmapFont playerDataFont;
//    BitmapFont lifeOnScreen;
//
//    private OrthoCamera camera;
//    private GameController gameController;
//
//    @Override
//    public void create() {
//        StaticValues.SCREEN_WIDTH = ConstantValues.BACKGROUND.getWidth();
//        StaticValues.SCREEN_HEIGHT = ConstantValues.BACKGROUND.getHeight();
//
//        camera = new OrthoCamera();
//
//        textOnScreen = new BitmapFont();
//        playerDataFont = new BitmapFont(Gdx.files.internal(ConstantValues.FONT_BLOGGER_SANS_YELLOW));
//        lifeOnScreen = new BitmapFont(Gdx.files.internal(ConstantValues.FONT_BLOGGER_SANS_YELLOW));
//
//        gameController = new GameController();
//        StaticValues.PLAYER = new Player();
//
//        camera.resize();
//    }
//
//    @Override
//    public void update() {
//        if (Gdx.input.justTouched() && !StaticValues.GAME_STARTED) {
//            StaticValues.GAME_RUNNING = true;
//            StaticValues.GAME_STARTED = true;
//        }
//        if (StaticValues.GAME_RUNNING) {
//            gameController.updateObjects();
//            camera.update();
//        }
//    }
//
//    @Override
//    public void render(SpriteBatch spriteBatch) {
//        spriteBatch.setProjectionMatrix(camera.combined);
////        spriteBatch.disableBlending();
//        // Drawing process
//        spriteBatch.begin();
//
//        textOnScreen.setColor(0.0f, 1.0f, 1.0f, 1.0f);
//
//        spriteBatch.draw(ConstantValues.BACKGROUND, 0, 0);
//
//        gameController.renderObjects(spriteBatch);
//
////        textOnScreen.draw(spriteBatch, "FPS: " + String.valueOf(1 / Gdx.graphics.getDeltaTime()), 50, 50);
//        playerDataFont.draw(spriteBatch, "SCORE: " + String.valueOf(StaticValues.PLAYER.getScore()), 150, 40);
//        lifeOnScreen.draw(spriteBatch, "LIFE: " + String.valueOf(StaticValues.PLAYER.getLife()), 700, 40);
//
//        spriteBatch.end();
//    }
//
//    @Override
//    public void resize(int newWidth, int newHeight) {
//
//    }
//
//    @Override
//    public void dispose() {
//
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//}
package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Camera.OrthoCamera;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.Objects.GameController;
import com.mygdx.game.StaticValues;

/**
 * Created by Master on 12/18/2015.
 */
public class MainScreen extends Screen {
    private BitmapFont playerDataFont;
    private BitmapFont pauseBtnFont;
    private BitmapFont pauseDialogFont;

    private OrthoCamera camera;
    private GameController gameController;

    private Stage stage;
    private Skin skin;

    private Window.WindowStyle pauseDialogStyle;
    private Dialog pauseDialog;

    private TextButton.TextButtonStyle pauseBtnStyle;
    private Button.ButtonStyle settingIconStyle;
    private Button.ButtonStyle playBtnStyle;

    private Button settingIcon;
    private Button playBtn;

    private TextButton resumeBtn;
    private TextButton goToMenuBtn;
    private TextureAtlas atlas;

    public MainScreen(){
        // Stop background music of the menu
        if(StaticValues.ADDITIONAL_MUSIC.isPlaying())
            StaticValues.ADDITIONAL_MUSIC.stop();
    }

    @Override
    public void create() {
        // Create music and sounds
        if(StaticValues.GAME_MUSIC_JOURNEY == null) {
            StaticValues.GAME_MUSIC_JOURNEY = StaticValues.ASSET_MANAGER.get(ConstantValues.MUSIC_JOURNEY, Music.class);
            StaticValues.GAME_MUSIC_JOURNEY.setLooping(true);
            StaticValues.GAME_MUSIC_JOURNEY.setVolume(StaticValues.MUSIC_VOLUME);
        }

        // Create camera and stage
        camera = new OrthoCamera();
        camera.resize();

        stage = new Stage(new FitViewport(StaticValues.SCREEN_WIDTH, StaticValues.SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        // Create UI skin for the pause dialog
        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal(ConstantValues.MAIN_SCREEN_ATLAS));
        pauseBtnFont = StaticValues.ASSET_MANAGER.get(ConstantValues.FONT_AHARONI_BIGGER, BitmapFont.class);
        pauseDialogFont = StaticValues.ASSET_MANAGER.get(ConstantValues.FONT_ROCKWELL, BitmapFont.class);
        skin.addRegions(atlas);

        playBtnStyle = new TextButton.TextButtonStyle();
        playBtnStyle.up = new TextureRegionDrawable(skin.getRegion(ConstantValues.PLAY_BTN));
        playBtn = new Button(playBtnStyle);
        playBtn.setPosition(StaticValues.SCREEN_WIDTH / 2 - playBtn.getWidth() / 2,
                StaticValues.SCREEN_HEIGHT / 2 - playBtn.getHeight() / 2);
        stage.addActor(playBtn);

        playBtn.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if (!StaticValues.GAME_MUSIC_JOURNEY.isPlaying())
                    StaticValues.GAME_MUSIC_JOURNEY.play();
                if (StaticValues.GAME_MUSIC_JOURNEY.isPlaying())
                    System.out.println("PLAY");
                else
                    System.out.println("NOT PLAYING");
                StaticValues.GAME_STARTED = true;
                StaticValues.GAME_RUNNING = true;
                playBtn.setVisible(false);
                playBtn.setDisabled(true);
                return true;
            }
        });

        settingIconStyle = new TextButton.TextButtonStyle();
        settingIconStyle.up = new TextureRegionDrawable(skin.getRegion(ConstantValues.COG_BTN));
        settingIcon = new Button(settingIconStyle);
        settingIcon.setPosition(StaticValues.SCREEN_WIDTH - 70, StaticValues.SCREEN_HEIGHT - 70);
        stage.addActor(settingIcon);

        settingIcon.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StaticValues.GAME_RUNNING = false;

                pauseDialogStyle = new Window.WindowStyle();
                pauseDialogStyle.titleFont = pauseDialogFont;
                pauseDialog = new Dialog("DO YOU WANT TO...?", pauseDialogStyle);
                pauseDialog.getTitleLabel().clear();
                pauseDialog.getTitleTable().clear();
                pauseDialog.getContentTable().remove();

                pauseBtnStyle = new TextButton.TextButtonStyle();
                pauseBtnStyle.up = new TextureRegionDrawable(skin.getRegion(ConstantValues.PAUSE_BTN));
                pauseBtnStyle.font = pauseBtnFont;
                resumeBtn = new TextButton("RESUME", pauseBtnStyle);
                goToMenuBtn = new TextButton("GO TO MENU", pauseBtnStyle);

                pauseDialog.getButtonTable().setBackground(new TextureRegionDrawable(skin.getRegion(ConstantValues.PAUSE_DIALOG)));
                pauseDialog.getButtonTable().add(resumeBtn).pad(20);
                pauseDialog.getButtonTable().row();
                pauseDialog.getButtonTable().add(goToMenuBtn).pad(20);
                pauseDialog.getButtonTable().row();
                pauseDialog.show(stage);

                if(StaticValues.GAME_MUSIC_JOURNEY.isPlaying())
                    StaticValues.GAME_MUSIC_JOURNEY.pause();

                resumeBtn.addListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if(StaticValues.GAME_STARTED)
                            StaticValues.GAME_RUNNING = true;
                        pauseDialog.hide();
                        StaticValues.GAME_MUSIC_JOURNEY.play();
                        return true;
                    }
                });

                goToMenuBtn.addListener(new InputListener(){
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        gameController.saveGame_new();
                        StaticValues.GAME_MUSIC_JOURNEY.stop();
                        ScreenController.setCurrentScreen(new MenuScreen());
                        return true;
                    }
                });

                return true;
            }
        });

        playerDataFont = new BitmapFont(Gdx.files.internal(ConstantValues.FONT_BLOGGER_SANS_YELLOW));
        gameController = new GameController(atlas);
        camera.resize();
    }

    @Override
    public void update() {
        if (StaticValues.GAME_RUNNING) {
            gameController.updateObjects();
            camera.update();
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        // Drawing process
        spriteBatch.begin();
        spriteBatch.draw(ConstantValues.BACKGROUND, 0, 0);
        gameController.renderObjects(spriteBatch);
        playerDataFont.draw(spriteBatch, "SCORE: " + String.valueOf(StaticValues.PLAYER.getScore()), 150, 40);
        playerDataFont.draw(spriteBatch, "LIFE: " + String.valueOf(StaticValues.PLAYER.getLife()), 700, 40);
        playerDataFont.draw(spriteBatch, "FUEL: " + String.valueOf(StaticValues.PLAYER.getFuel())
                + "/" + String.valueOf(StaticValues.PLAYER.getMaxFuel()), 150, StaticValues.SCREEN_HEIGHT - 15);
        spriteBatch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int newWidth, int newHeight) {

    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
