package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.StaticValues;

/*
 * Created by Master on 12/18/2015.
 */
public class Helicopter extends GameObject {
    private int gravity = -100;
    private int flyUpSpeed = 200;
    private long lastTapTime, flyInterval, fuelLastUse;
    private ParticleEffect fallingEffect;

    private Sound tapSound;

    public Helicopter(TextureRegion texture, Vector2 pos) {
        super(texture, pos);
        flyInterval = 200;
        lastTapTime = -flyInterval;
        fuelLastUse = System.currentTimeMillis();

        fallingEffect = new ParticleEffect();
        fallingEffect.load(Gdx.files.internal("homing_trail.p"), Gdx.files.internal("particles"));
        fallingEffect.setPosition(pos.x + getTexture().getRegionWidth() / 2, pos.y + getTexture().getRegionHeight() / 2);
        fallingEffect.start();

        tapSound = StaticValues.ASSET_MANAGER.get(ConstantValues.SOUND_COPTER_TAP, Sound.class);
    }

    public void move() {
        fallingEffect.setPosition(pos.x+getTexture().getRegionWidth()/2, pos.y+getTexture().getRegionHeight()/2);
        if (Gdx.input.justTouched()) {
            lastTapTime = System.currentTimeMillis();
            tapSound.play(StaticValues.SOUND_VOLUME);
        }
        if (System.currentTimeMillis() - lastTapTime < flyInterval) {
            pos.y += flyUpSpeed * Gdx.graphics.getDeltaTime();
        } else
            pos.y += gravity * Gdx.graphics.getDeltaTime();
    }

    public void crashHelicopter() {
        pos.add(new Vector2(-3, -5));
        sprite.setRotation(sprite.getRotation() + 360 * Gdx.graphics.getDeltaTime());
    }

    public Vector2 getHeliPos() {
        return pos;
    }

    public void setHeliPos(Vector2 pos) {
        this.pos = pos;
    }

    @Override
    public void update() {
        fallingEffect.setPosition(pos.x+getTexture().getRegionWidth()/2, pos.y+getTexture().getRegionHeight()/2);
        if (!StaticValues.GAME_OVER) {
            move();
            if (System.currentTimeMillis() - fuelLastUse > 1000 && StaticValues.PLAYER.getFuel() > 0) {
                fuelLastUse = System.currentTimeMillis();
                StaticValues.PLAYER.alterFuel(-1);
            } else if (StaticValues.PLAYER.getFuel() <= 0)
                StaticValues.GAME_OVER = true;
        } else
            crashHelicopter();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
        if (StaticValues.GAME_OVER) {
            fallingEffect.draw(spriteBatch);
            fallingEffect.update(Gdx.graphics.getDeltaTime());
        }
    }
}
