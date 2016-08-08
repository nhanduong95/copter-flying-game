package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.StaticValues;

import java.io.Serializable;

/**
 * Created by Master on 12/19/2015.
 */
public class CaveObject extends GameObject implements Serializable{
    private String type;

    public CaveObject(TextureRegion textureRegion, Vector2 pos, String type) {
        super(textureRegion, pos);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    // Make the object move left as the screen scrolling
    @Override
    public void update() {
        pos.x += StaticValues.HORIZONTAL_FLOATING_SPEED * Gdx.graphics.getDeltaTime();

    }
}
