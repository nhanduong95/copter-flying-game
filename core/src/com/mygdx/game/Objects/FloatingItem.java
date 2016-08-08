package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.StaticValues;

/**
 * Created by User on 26-Dec-15.
 */
public class FloatingItem extends GameObject {
    private String type;
    private int points;
    private int life;
    private int fuel;

    public FloatingItem(TextureRegion textureRegion, Vector2 pos, String type,
                        int points, int life, int fuel){
        super(textureRegion, pos);
        this.type = type;
        this.points = points;
        this.life = life;
        this.fuel = fuel;
    }

    public int getPoints(){
        return points;
    }
    public String getType(){
        return type;
    }
    public int getLife(){
        return life;
    }
    public int getFuel(){
        return fuel;
    }

    @Override
    public void update() {
        pos.x += StaticValues.HORIZONTAL_FLOATING_SPEED * Gdx.graphics.getDeltaTime();
    }
}
