package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.StaticValues;

/**
 * Created by Master on 12/18/2015.
 */
public abstract class GameObject {
    private static final int OFFSET = 10;
    public Vector2 pos;
    protected BitmapFont textOnScreen;
    private TextureRegion textureRegion;
    private Texture texture;
    protected Sprite sprite;
    ParticleEffect explosion;
    Vector2 explosionPos;

    boolean hasOverlapped = false;

    public GameObject(TextureRegion textureRegion, Vector2 pos){
        this.textureRegion = textureRegion;
        sprite = new Sprite(this.textureRegion);
        this.pos = pos;

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("enemy_explosion.p"), Gdx.files.internal("particles"));
        textOnScreen = new BitmapFont();
        explosionPos = new Vector2(pos.x, pos.y/2);
    }

    public boolean getOverlappedState() {
        return hasOverlapped;
    }

    public void setHasOverlapped(boolean b, Helicopter helicopter) {
        if (b) {
            explosionPos.set(helicopter.getHeliPos().x + helicopter.getTexture().getRegionWidth()/2,
                    helicopter.getHeliPos().y + helicopter.getTexture().getRegionHeight() / 2);
            explosion.setPosition(explosionPos.x, explosionPos.y);
            explosion.start();
        }
        hasOverlapped = b;
    }

    public ParticleEffect getExplosion() {
        return explosion;
    }

    public Vector2 getPosition(){
        return pos;
    }

    public TextureRegion getTexture(){
        return textureRegion;
    }

    public Rectangle getPillarBounds() {
        return new Rectangle(pos.x + 50 , pos.y, textureRegion.getRegionWidth() - 60,
                textureRegion.getRegionHeight() - 20);
    }
    public Rectangle getBounds(){
        return new Rectangle(pos.x, pos.y,
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public boolean isOnScreen(){
        return (pos.y <= StaticValues.SCREEN_HEIGHT + OFFSET &&
                pos.y >= -getTexture().getRegionHeight()-OFFSET &&
                pos.x <= StaticValues.SCREEN_WIDTH + OFFSET &&
                pos.x >= -getTexture().getRegionWidth()-OFFSET);
    }

    public abstract void update();

    public void render (SpriteBatch sb){
        sprite.setPosition(pos.x, pos.y);
        sprite.draw(sb);
    }
}
