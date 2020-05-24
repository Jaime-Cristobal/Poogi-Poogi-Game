package com.mygdx.core.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.core.Core;
import com.mygdx.core.Scaler;

/**
 * Created by seacow on 9/28/2017.
 *
 * Renders textures; rendering animated sprites are in Animate
 */

public final class Render
{
    public OrthographicCamera camera = new OrthographicCamera();

    public Render()
    {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
    }

    public void render(Core project, Sprite sprite, Body body, float width, float height)
    {
        sprite.setPosition((body.getPosition().x * Scaler.PIXELS_TO_METERS) - width/2,
                (body.getPosition().y * Scaler.PIXELS_TO_METERS) - height/2);

        sprite.setOrigin(width / 2, height / 2);    //sets the rotation at  the sprite's center

        project.batch.draw(sprite, sprite.getX(), sprite.getY(),
                sprite.getOriginX(), sprite.getOriginY(),
                width, height,
                sprite.getScaleX(), sprite.getScaleY(),
                sprite.getRotation());
    }

    public void render(Core project, Sprite sprite, Body body, float width, float height, float offSetX, float offSetY)
    {
        sprite.setPosition((body.getPosition().x * Scaler.PIXELS_TO_METERS) - width/2,
                (body.getPosition().y * Scaler.PIXELS_TO_METERS) - height/2);

        sprite.setOrigin(width / 2, height / 2);    //sets the rotation at  the sprite's center

        project.batch.draw(sprite, sprite.getX(), sprite.getY(),
                sprite.getOriginX() + offSetX, sprite.getOriginY() + offSetY,
                width, height,
                sprite.getScaleX(), sprite.getScaleY(),
                sprite.getRotation());
    }

    public void renderCustomBod(Core project, Sprite sprite, Body body, float width, float height, float offsetX, float offsetY)
    {
        sprite.setPosition((body.getPosition().x * Scaler.PIXELS_TO_METERS) - width/2,
                (body.getPosition().y * Scaler.PIXELS_TO_METERS) - height/2);

        sprite.setOrigin(width / 2, height / 2);    //sets the rotation at  the sprite's center

        project.batch.draw(sprite, (sprite.getX() + offsetX), (sprite.getY() + offsetY),
                sprite.getOriginX(), sprite.getOriginY(),
                width, height,
                sprite.getScaleX(), sprite.getScaleY(),
                sprite.getRotation());
    }
}
