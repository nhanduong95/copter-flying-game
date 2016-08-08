package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Camera.OrthoCamera;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.Player;
import com.mygdx.game.StaticValues;

/**
 * Created by User on 31-Dec-15.
 */
public class MenuScreen extends Screen {
    private OrthoCamera camera;
    private Stage stage;
    private TextureAtlas atlas;

    private TextureRegion gameTitle;

    public MenuScreen(){
        // Stop the background music of the game
        if(StaticValues.GAME_MUSIC_JOURNEY != null && StaticValues.GAME_MUSIC_JOURNEY.isPlaying())
            StaticValues.GAME_MUSIC_JOURNEY.stop();
    }

    @Override
    public void create() {
        // Create sound and music
        if(StaticValues.ADDITIONAL_MUSIC == null){
            StaticValues.ADDITIONAL_MUSIC = StaticValues.ASSET_MANAGER.get(ConstantValues.MUSIC_MENU, Music.class);
            StaticValues.ADDITIONAL_MUSIC.setLooping(true);
            StaticValues.ADDITIONAL_MUSIC.setVolume(StaticValues.MUSIC_VOLUME);
            StaticValues.ADDITIONAL_MUSIC.play();
        }else
            StaticValues.ADDITIONAL_MUSIC.play();

        // Create a stage based on the VirtualViewPort
        StaticValues.SCREEN_WIDTH = ConstantValues.BACKGROUND.getWidth();
        StaticValues.SCREEN_HEIGHT = ConstantValues.BACKGROUND.getHeight();
        camera = new OrthoCamera();
        camera.resize();

        stage = new Stage(new FitViewport(StaticValues.SCREEN_WIDTH, StaticValues.SCREEN_HEIGHT));

        // Create a table to hold all menu's buttons
        Table table = new Table();
        table.setFillParent(true);

        // Create UI skin for menu
        Skin skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal(ConstantValues.MENU_ATLAS));
        BitmapFont font = StaticValues.ASSET_MANAGER.get(ConstantValues.FONT_AHARONI_SMALLER, BitmapFont.class);
        skin.addRegions(atlas);

        // Create menu's title
        gameTitle = atlas.findRegion(ConstantValues.GAME_TITLE);

        // Create TextButtonStyle for menu's buttons
        TextButton.TextButtonStyle leftBtnStyle01 = new TextButton.TextButtonStyle();
        leftBtnStyle01.up = new TextureRegionDrawable(skin.getRegion(ConstantValues.MENU_LEFT_BTN_01));
        leftBtnStyle01.font = font;

        TextButton.TextButtonStyle leftBtnStyle02 = new TextButton.TextButtonStyle();
        leftBtnStyle02.up = new TextureRegionDrawable(skin.getRegion(ConstantValues.MENU_LEFT_BTN_02));
        leftBtnStyle02.font = font;

        TextButton.TextButtonStyle rightBtnStyle01 = new TextButton.TextButtonStyle();
        rightBtnStyle01.up = new TextureRegionDrawable(skin.getRegion(ConstantValues.MENU_RIGHT_BTN_01));
        rightBtnStyle01.font = font;

        TextButton.TextButtonStyle rightBtnStyle02 = new TextButton.TextButtonStyle();
        rightBtnStyle02.up = new TextureRegionDrawable(skin.getRegion(ConstantValues.MENU_RIGHT_BTN_02));
        rightBtnStyle02.font = font;

        // Create menu's buttons
        TextButton newGameBtn = new TextButton("NEW GAME", leftBtnStyle01);
        TextButton resumeGameBtn = new TextButton("RESUME GAME", rightBtnStyle02);
        TextButton multiplayerBtn = new TextButton("MULTIPLAYER", leftBtnStyle02);
        TextButton scoresBtn = new TextButton("SCORES", rightBtnStyle01);
        TextButton settingsBtn = new TextButton("SETTINGS", leftBtnStyle01);
        TextButton exitBtn = new TextButton("EXIT", rightBtnStyle02);

        table.add(newGameBtn).pad(10);
        table.add(resumeGameBtn).pad(10);
        table.row();
        table.add(multiplayerBtn).pad(10);
        table.add(scoresBtn).pad(10);
        table.row();
        table.add(settingsBtn).pad(10);
        table.add(exitBtn).pad(10);
        table.row();

        table.setPosition(110, 0);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        newGameBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StaticValues.PLAYER = new Player();
                StaticValues.START_NEW_GAME = true;
                ScreenController.setCurrentScreen(new MainScreen());
                return true;
            }
        });

        resumeGameBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StaticValues.START_NEW_GAME = false;
                ScreenController.setCurrentScreen(new MainScreen());
                return true;
            }
        });

        multiplayerBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Multiplayer");
                return true;
            }
        });

        scoresBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenController.setCurrentScreen(new ScoresScreen());
                return true;
            }
        });

        settingsBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenController.setCurrentScreen(new SettingsScreen());
                return true;
            }
        });

        exitBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

    }

    @Override
    public void update() {
        camera.update();
        if(!StaticValues.ADDITIONAL_MUSIC.isPlaying())
            StaticValues.ADDITIONAL_MUSIC.play();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(ConstantValues.BACKGROUND, 0, 0);
        spriteBatch.draw(gameTitle, 40, StaticValues.SCREEN_HEIGHT/2 - gameTitle.getRegionHeight()/2);
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
