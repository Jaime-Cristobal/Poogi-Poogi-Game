package com.mygdx.core.actor.creation;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by seacow on 12/17/2017.
 */

public interface CreateActor
{
    /**Values have to be in hex values in the powers of 2 (2, 4, 8, 16, 32, etc..)
     * For filter collisions*/
    void setFilter(short category, short mask);

    void setGroupFilter(short categoy);

    /**Manually set the body's density(heaviness) and restitution(bounciness)
     * values. + rotation lock (as in the body will never rotate)
     * The default values are density = 0 and restitution = 0.1f*/
    void setData(float density, float resitution, boolean lockRotation);

    /**For IDing a container of objects containing the same name file. Have the same
     * name files means they all have same UserData ID*/
    void setUniqueID(Object ID);

    void create(World world, float xPos, float yPos, float w, float h, boolean isSensor);

    void createOriginalCustom(World world, float xPos, float yPos, float w, float h,
                                     float renderW, float renderH, boolean isSensor);

    void display();

    void display(float delta);

    void displayCustom(float delta, float offSetX, float offSetY);

    /** ----------------------------------------------------------------------------------
     * NOTE: Only call the following functions below after calling create and creating the
     * actor's body*/

    /**false sets the box2D body to sleep (unmovable)*/
    void setActive(boolean active);

    boolean isActive();

    /**speed is set on a x and y axis plane*/
    void setSpeed(float xSpeed, float ySpeed);

    void applyForce(float xVal, float yVal);

    /**sets the position to whatever the values of xPos and yPos are*/
    void setPosition(float xPos, float yPos);

    void flip();

    float getX();

    float getY();

    float getVelX();

    float getVelY();

    float getWidth();

    float getHeight();

    Body getBody();

    void setNoGravity();

    void resetUserData();

    boolean ifDisplayed();

    void setDisplay(boolean val);

    boolean hasOffSets();
}
