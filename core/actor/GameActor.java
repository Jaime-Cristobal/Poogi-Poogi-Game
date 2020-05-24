package com.mygdx.core.actor;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by seacow on 12/17/2017.
 */

public interface GameActor
{
    void setSpawn(float xmin, float xmax, float ymin, float ymax);

    /**Manually set the body's density(heaviness) and restitution(bounciness)
     * values.
     * The default values are density = 0 and restitution = 0.1f*/
    void setData(float density, float restitution, BodyDef.BodyType type);

    /**Values have to be in hex values in the powers of 2 (2, 4, 8, 16, 32, etc..)
     * For filter collisions*/
    void setFilter(short hex1, short hex2);

    /**A point in the screen where the actor will reach and respawn back at the spawn area
     * @param isVertical -> the actor will move vertically instead of the default horizontal*/
    void setLimit(float value, boolean isVertical);

    /**Uses all the data above to create the actor's body. It is required to call setSpawn
     * and setResolution for the body to be created.
     *
     * Ignoring the rest will just result
     * in an actor that cannot be interacted and possible random effects due to the limit's
     * value initialized at 0.
     */
    void create(World world, Object ID, float width, float height, int amount, boolean isSensor);

    void displayAll(float speed);

    /**For controlling the specific amount of actors present on the screen*/
    void display(float speed, int amount);

    void setNoGravity();

    Object getUserData();
}
