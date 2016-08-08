package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Master on 12/18/2015.
 */
public abstract class Screen {

    public abstract void create();

    public abstract void update();

    public abstract void render (SpriteBatch spriteBatch);

    public abstract void resize (int newWidth, int newHeight);

    public abstract void dispose();

    public abstract void pause();

    public abstract void resume();
}
