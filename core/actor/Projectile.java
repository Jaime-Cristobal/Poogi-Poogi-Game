package com.mygdx.core.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.core.actor.creation.CreateActor;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.EventManager;

public class Projectile
{
    final private CreateActor body;
    private float originX, originY;

    /**Leave region null if CreateTexture is created*/
    public Projectile(CreateActor body, ArrayMap<String, Float> regionSet)
    {
        this.body = body;

        if(body instanceof CreateAnimation)
            ((CreateAnimation) body).addRegion(regionSet);
        else
            Gdx.app.error("Projectile.java", "This is not a CreateAnimation class instance!");
    }

    public void setData(float density, float restitution, boolean rotationLock)
    {
        body.setData(density, restitution, rotationLock);
    }

    public void setFilter(short bodyFilter, short colliding)
    {
        body.setFilter(bodyFilter, colliding);
    }

    public void create(World world, float width, float height, Object ID)
    {
        body.setUniqueID(ID);
        body.create(world, -100, -100, width, height, false);
    }

    public void setPosition(float x, float y)
    {
        body.setPosition(x, y);
    }

    public void setOrigin(float x, float y)
    {
        originX = x;
        originY = y;
    }

    public void setSpeed(float xVel, float yVel)
    {
        body.setSpeed(xVel, yVel);
    }

    public void render(float delta)
    {
        body.display(delta);
    }

    public Object getID()
    {
        return body.getBody().getUserData();
    }

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     * Called with .free()*/
    public void reset ()
    {
        //just have the reset set a boolean to not render
        //and then set the bodies somewhere else off screen

        body.setPosition(-600, -600);
        body.setSpeed(0, 0);
        body.setDisplay(false);
        body.setActive(false);
    }

    public boolean contactReset()
    {
        if(ifContact())
        {
            reset();
            return true;
        }
        return false;
    }

    public boolean ifContact()
    {
        return EventManager.inContact(body.getBody().getUserData());
    }

    public Body getBody()
    {
        return body.getBody();
    }

    public void setDisplay(boolean option)
    {
        body.setDisplay(option);
    }

    public void setActive(boolean option)
    {
        body.setActive(option);
    }

    public void setActiveAndDisplay(boolean option)
    {
        body.setDisplay(option);
        body.setActive(option);
    }

    public void replaceTexture(String atlas, String texture)
    {
        if(body instanceof CreateTexture)
        {
            ((CreateTexture)body).setNewTexture(atlas, texture);
        }
    }

    public boolean isDisplayed()
    {
        return body.ifDisplayed();
    }

    public boolean isActiveAndDisplayed()
    {
        return body.isActive() || body.ifDisplayed();
    }

    public float getOriginX()
    {
        return originX;
    }

    public float getOriginY()
    {
        return originY;
    }
}
