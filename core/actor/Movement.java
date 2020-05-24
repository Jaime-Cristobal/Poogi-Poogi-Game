package com.mygdx.core.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.core.Scaler;
import com.mygdx.core.actor.creation.CreateActor;

/**
 * Created by seacow on 12/17/2017.
 */

public final class Movement
{
    private float vertical = 0;
    private float horizontal = 1;

    private float xMin = 0;
    private float xMax = 0;
    private float yMin = 0;
    private float yMax = 0;
    private float limit = 0;

    private boolean justRespawned = false;

    /**these values will be needed for respawning*/
    public void setSpawn(float xmin, float xmax, float ymin, float ymax)
    {
        xMin = xmin;
        xMax = xmax;
        yMin = ymin;
        yMax = ymax;
    }

    public void setLimit(float val)
    {
        limit = val;
    }

    /**int instead of boolean values because whatever direction has a value of 1
     * will move and 0 will result with 0 speed (no movement).*/
    public void setDirection(int vert, int horiz)
    {
        vertical = vert;
        horizontal = horiz;
    }

    /**currentActor should be an iterator passed*/
    public void move(CreateActor currentActor, float speed)
    {
        //These 2 lines just moves the actor across the map
        currentActor.setActive(true);   //body might not be active for moving
        currentActor.setSpeed(speed * horizontal, speed * vertical);

        //If it reaches at the limit meter, it will respawn
        if(respawn(currentActor))
            currentActor.setPosition(MathUtils.random(xMin, xMax),
                    MathUtils.random(yMin, yMax));
    }

    public void setSpeed(CreateActor currentActor, float x, float y)
    {
        currentActor.setActive(true);
        currentActor.setSpeed(x * Gdx.graphics.getDeltaTime() * 1000,
                y * Gdx.graphics.getDeltaTime() * 1000);
    }

    public void forceRespawn(CreateActor currentActor)
    {
        System.out.println("FORCED");
        //currentActor.setSpeed(0, 0);
        //currentActor.applyForce(0, 0);
        currentActor.setPosition(MathUtils.random(xMin, xMax),
                MathUtils.random(yMin, yMax));
    }

    /**NOT WORKING AS INTENDED*/
    public void setPosition(Vector2 position)
    {
        horizontal = position.x;
        vertical = position.y;
    }

    private boolean respawn(CreateActor currentActor)
    {
        if(((int)currentActor.getX() == limit / Scaler.PIXELS_TO_METERS && vertical == 0) ||
                ((int)currentActor.getY() == limit / Scaler.PIXELS_TO_METERS && horizontal == 0))
        {
            justRespawned = true;
            return true;
        }

        return false;
    }

    public boolean getRespawnVal()
    {
        return justRespawned;
    }

    public void resetRespawnVal()
    {
        justRespawned = false;
    }

    public float getRandomX()
    {
        return MathUtils.random(xMin, xMax);
    }

    public float getRandomY()
    {
        return MathUtils.random(yMin, yMax);
    }
}
