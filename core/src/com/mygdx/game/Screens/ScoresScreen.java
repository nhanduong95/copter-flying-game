package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Camera.OrthoCamera;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.StaticValues;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Master on 1/5/2016.
 */
public class ScoresScreen extends Screen {
    private OrthoCamera camera;
    private TextureRegion dialog;

    BitmapFont text;
    String scoreList = " ";
    private Array<Number> scores;

    private TextureRegion gameTitle;
    private TextureAtlas atlas;

    private Stage stage;
    private Skin skin;
    private Button.ButtonStyle okBtnStyle;
    private Button okBtn;

    @Override
    public void create() {
        // Create a stage based on the VirtualViewPort
        camera = new OrthoCamera();
        camera.resize();
        stage = new Stage(new FitViewport(StaticValues.SCREEN_WIDTH, StaticValues.SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        // Create UI skin
        atlas = new TextureAtlas(Gdx.files.internal(ConstantValues.SCORES_ATLAS));
        skin = new Skin();
        skin.addRegions(atlas);

        // Create OK button
        okBtnStyle = new Button.ButtonStyle();
        okBtnStyle.up = new TextureRegionDrawable(skin.getRegion(ConstantValues.SETTINGS_OK_BTN));
        okBtn = new Button(okBtnStyle);
        okBtn.setPosition(680, 65);
        stage.addActor(okBtn);

        okBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenController.setCurrentScreen(new MenuScreen());
                return true;
            }
        });

        // Create other elements
        gameTitle = atlas.findRegion(ConstantValues.GAME_TITLE);
        dialog = atlas.findRegion(ConstantValues.SCORES_DIALOG);

        scores = new Array<Number>();
        text = StaticValues.ASSET_MANAGER.get(ConstantValues.FONT_BLOGGER_SANS_YELLOW, BitmapFont.class);

        // Get scores from cloud and sort them in descending order
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Highscore");
        if (!StaticValues.ACTION_RESOLVER.checkInternet()) {
            query.fromLocalDatastore();
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (final ParseObject objparse : objects) {
                        scores.add(objparse.getNumber("score"));
                    }
                    scores.sort();
                    if (scores.size >= 10)
                        for (int x = scores.size-1; x > scores.size-11; x--) {
                            scoreList = scoreList+"\n"+(scores.size-x)+". "+scores.get(x);
                        }
                    else
                        for (int x = scores.size-1; x >= 0; x--) {
                            scoreList = scoreList+"\n"+(scores.size-x)+". "+scores.get(x);
                        }
                } else {
                    e.printStackTrace();
                }
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
        spriteBatch.draw(dialog, StaticValues.SCREEN_WIDTH / 2 - 100, StaticValues.SCREEN_HEIGHT / 2 - dialog.getRegionHeight() / 2);
        spriteBatch.draw(gameTitle, 40, StaticValues.SCREEN_HEIGHT / 2 - gameTitle.getRegionHeight() / 2);
        text.draw(spriteBatch, scoreList, 500, StaticValues.SCREEN_HEIGHT - 30);
        spriteBatch.end();

        // Draw ok button and assign its action listener
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int newWidth, int newHeight) {
    }

    @Override
    public void dispose() {
        stage.dispose();
        text.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }
}
