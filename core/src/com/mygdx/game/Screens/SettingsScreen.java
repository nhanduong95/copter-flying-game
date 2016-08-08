package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Camera.OrthoCamera;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.StaticValues;

import javax.swing.GroupLayout;

/**
 * Created by User on 05-Jan-16.
 */
public class SettingsScreen extends Screen {
    private OrthoCamera camera;
    private Stage stage;
    private TextureAtlas atlas;

    private TextureRegion gameTitle;
    private TextureRegion dialog;

    private Slider musicSlider;
    private Slider soundSlider;
    private Slider gameSpeedSlider;

    private Button okBtn;

    @Override
    public void create() {
        // Create a stage based on the VirtualViewPort
        StaticValues.SCREEN_WIDTH = ConstantValues.BACKGROUND.getWidth();
        StaticValues.SCREEN_HEIGHT = ConstantValues.BACKGROUND.getHeight();
        camera = new OrthoCamera();
        camera.resize();

        stage = new Stage(new FitViewport(StaticValues.SCREEN_WIDTH, StaticValues.SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        // Create a table layout that holds all settings UI labels and sliders
        Table table = new Table();
        table.setFillParent(true);

        // Create UI skin for settings
        Skin skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal(ConstantValues.SETTINGS_ATLAS));
        skin.addRegions(atlas);

        // Create game title
        gameTitle = atlas.findRegion(ConstantValues.GAME_TITLE);
        dialog = atlas.findRegion(ConstantValues.SETTINGS_DIALOG);

        // Create OK button
        Button.ButtonStyle okBtnStyle = new Button.ButtonStyle();
        okBtnStyle.up = new TextureRegionDrawable(skin.getRegion(ConstantValues.SETTINGS_OK_BTN));
        okBtn = new Button(okBtnStyle);
        okBtn.setPosition(680, 65);
        stage.addActor(okBtn);

        // Create labels
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = StaticValues.ASSET_MANAGER.get(ConstantValues.FONT_AHARONI_SMALLER);

        Label musicLabel = new Label("MUSIC", labelStyle);
        Label soundLabel = new Label("SOUND", labelStyle);
        Label gameSpeedLabel = new Label("GAME SPEED", labelStyle);
        musicLabel.setAlignment(Align.center);
        soundLabel.setAlignment(Align.center);
        gameSpeedLabel.setAlignment(Align.center);

        // Create sliders
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.knob = new TextureRegionDrawable(skin.getRegion(ConstantValues.SLIDER_KNOB));
        sliderStyle.background = new TextureRegionDrawable(skin.getRegion(ConstantValues.SLIDER_BACKGROUND));

        musicSlider = new Slider(0, 2, 0.2f, false, sliderStyle);
        soundSlider = new Slider(0, 2, 0.2f, false, sliderStyle);
        gameSpeedSlider = new Slider(200, 350, 30, false, sliderStyle);

        musicSlider.setValue(StaticValues.MUSIC_VOLUME);
        soundSlider.setValue(StaticValues.SOUND_VOLUME);
        gameSpeedSlider.setValue(-StaticValues.HORIZONTAL_FLOATING_SPEED);

        // Add sliders and labels to a table layout
        table.setFillParent(true);
        table.defaults().width(415);
        table.add(musicLabel).pad(5).center();
        table.row();
        table.add(musicSlider).pad(5);
        table.row();
        table.add(soundLabel).pad(5).center();
        table.row();
        table.add(soundSlider).pad(5);
        table.row();
        table.add(gameSpeedLabel).pad(5).center();
        table.row();
        table.add(gameSpeedSlider).pad(5);
        table.row();
        table.pack();
        table.setPosition(140, 0);
        stage.addActor(table);

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                StaticValues.MUSIC_VOLUME = musicSlider.getValue();
            }
        });

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                StaticValues.SOUND_VOLUME = soundSlider.getValue();
                System.out.println("SOUND " + StaticValues.SOUND_VOLUME);
            }
        });

        gameSpeedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                StaticValues.HORIZONTAL_FLOATING_SPEED = -gameSpeedSlider.getValue();
            }
        });

        okBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(StaticValues.ADDITIONAL_MUSIC.isPlaying()){
                    StaticValues.ADDITIONAL_MUSIC.stop();
                    StaticValues.ADDITIONAL_MUSIC.setVolume(StaticValues.MUSIC_VOLUME);
                    System.out.println("VOLUME " + StaticValues.MUSIC_VOLUME);
                    StaticValues.ADDITIONAL_MUSIC.play();
                }

                ScreenController.setCurrentScreen(new MenuScreen());
                return true;
            }
        });
    }

    @Override
    public void update() {
        camera.update();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(ConstantValues.BACKGROUND, 0, 0);
        spriteBatch.draw(gameTitle, 40, StaticValues.SCREEN_HEIGHT/2 - gameTitle.getRegionHeight()/2);
        spriteBatch.draw(dialog, StaticValues.SCREEN_WIDTH/2 - 100,StaticValues.SCREEN_HEIGHT/2 - dialog.getRegionHeight()/2);
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
//        if(music != null)
//            music.dispose();
//        if(tapSound != null)
//            tapSound.dispose();
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
