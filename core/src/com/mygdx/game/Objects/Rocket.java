package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.Screens.GameOverScreen;
import com.mygdx.game.StaticValues;

/**
 * Created by Master on 1/2/2016.
 */
public class Rocket extends FloatingItem {

    Texture warning = new Texture("warning.png");
    Sprite warning_sprite = new Sprite(warning);
    Vector2 warning_pos;
    private long spawn_time;
    private Sound warningSound;

    public Rocket(TextureRegion textureRegion, Vector2 pos, long spawn_time) {
        super(textureRegion, pos, ConstantValues.ROCKET, 0, -1, 0);
        this.warning_pos = new Vector2(StaticValues.SCREEN_WIDTH - 50, pos.y);
        this.spawn_time = spawn_time;
        warningSound = StaticValues.ASSET_MANAGER.get(ConstantValues.SOUND_WARNING, Sound.class);
    }

    @Override
    public void render (SpriteBatch sb){
        sprite.setPosition(pos.x, pos.y);
        warning_sprite.setPosition(warning_pos.x, warning_pos.y);
        sprite.draw(sb);
        warning_sprite.draw(sb);
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - spawn_time < 1000) {
            warning_sprite.setAlpha(MathUtils.cos(100 * Gdx.graphics.getDeltaTime()));
            warningSound.play(StaticValues.SOUND_VOLUME);
        } else {
            warning_sprite.setAlpha(0f);
            pos.x += -500 * Gdx.graphics.getDeltaTime();
        }
    }
}
