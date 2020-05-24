package com.mygdx.core;

import com.badlogic.gdx.Gdx;

/**
 * Created by seacow on 9/8/2017.
 */

public class Scaler
{
    public final static float PIXELS_TO_METERS = 10f;
    public final static float scaleX = (Gdx.graphics.getWidth() / 400);
    public final static float scaleY = (Gdx.graphics.getHeight() / 600);
    public final static float scale = 600 / Gdx.graphics.getHeight();
}
