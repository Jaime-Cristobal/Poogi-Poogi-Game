package com.mygdx.core.actor.creation;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.core.BodyEditorLoader;
import com.mygdx.core.Core;
import com.mygdx.core.Scaler;
import com.mygdx.core.animator.Animator;

/**
 * Created by seacow on 12/17/2017.
 */

public class CreateAnimation implements CreateActor
{
    final private Core core;

    private CreateBody box2dBody;
    private Animator animate;

    private String file;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean toDisplay = false, addOffSets = false;
    private float offX = 0, offY = 0;

    private CreateAnimation(String name, Core core)
    {
        this.core = core;
        file = name;

        x = 0;
        y = 0;
    }

    public CreateAnimation(String file, ArrayMap<String, Float> region, Core core, BodyDef.BodyType type)
    {
        this(file, core);

        box2dBody = new CreateBody(type);
        animate = new Animator(this.file, core);
        animate.setRegion(region);
    }

    public CreateAnimation(String file, Core core, BodyDef.BodyType type)
    {
        this(file, core);

        box2dBody = new CreateBody(type);
        animate = new Animator(this.file, core);
    }

    public void addRegion(String regionName, float frameSpeed)
    {
        animate.addRegion(regionName, frameSpeed);
    }

    public void addRegion(ArrayMap<String, Float> regionSet)
    {
        animate.setRegion(regionSet);
    }

    public boolean checkRegion(String region)
    {
        return animate.checkRegion(region);
    }

    /**Use this with a contact filtering class*/
    public void setFilter(short category, short mask)
    {
        box2dBody.setFilter(category, mask);
    }

    public void setGroupFilter(short categoy)
    {
        box2dBody.setAsFilterGroup(categoy);
    }

    /**Manually set the body's density(heaviness) and restitution(bounciness)
     * values.
     * The default values are density = 0 and restitution = 0.1f*/
    public void setData(float density, float resitution, boolean lockRotation)
    {
        box2dBody.setData(density, resitution, lockRotation);
    }

    /**For IDing a container of objects containing the same name file. Have the same
     * name files means they all have same UserData ID*/
    public void setUniqueID(Object ID)
    {
        box2dBody.setID(ID);
    }

    public void create(World world, float xPos, float yPos, float w, float h, boolean isSensor)
    {
        animate.setScale(w, h);
        width = animate.getWidth();
        height = animate.getHeight();

        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.create(world, x, y, width, height, isSensor);
        box2dBody.getBody().setActive(true);
    }

    public void createOriginalCustom(World world, float xPos, float yPos, float w, float h,
                                     float renderW, float renderH, boolean isSensor)
    {
        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.create(world, x, y, w, h , isSensor);
        box2dBody.getBody().setActive(true);

        animate.setScale(renderW, renderH);
    }

    public void createCircle(World world, float xPos, float yPos, float radius, boolean isSensor)
    {
        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.createCircle(world, x, y, radius, isSensor);
        box2dBody.getBody().setActive(true);
    }

    public void create(World world, BodyEditorLoader loader, String file, float xPos, float yPos,
                       float w, float h, float scale, boolean sensor)
    {
        animate.setScale(w, h);

        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.setBody(world, loader, file, x, y, scale, sensor);
        box2dBody.getBody().setActive(true);
    }

    public void setOffSets(float offX, float offY)
    {
        this.offX = offX;
        this.offY = offY;
        addOffSets = true;
    }

    public void setDisplay(boolean toDisplay)
    {
        this.toDisplay = toDisplay;
    }

    public void display()
    {
        animate.render(core.batch, box2dBody.getBody().getPosition().x, box2dBody.getBody().getPosition().y);
    }

    public void display(float delta)
    {
        animate.render(core.batch, box2dBody.getBody().getPosition().x, box2dBody.getBody().getPosition().y, delta);
    }

    public void display(float delta, float angle)
    {
        animate.render(core.batch, box2dBody.getBody().getPosition().x, box2dBody.getBody().getPosition().y, delta, angle);
    }

    public void displayCustom(float delta, float offSetX, float offSetY)
    {
        animate.renderCustomBod(core.batch, delta, box2dBody.getBody().getPosition().x, box2dBody.getBody().getPosition().y,
                offSetX, offSetY);
    }

    public void displayStill()
    {
        animate.renderStill(core.batch, box2dBody.getBody().getPosition().x, box2dBody.getBody().getPosition().y);
    }

    public float getSpriteDirection()
    {
        return animate.getDirection();
    }

    /**false sets the box2D body to sleep (unmovable)*/
    public void setActive(boolean active)
    {
        box2dBody.getBody().setActive(active);
    }

    public boolean isActive()
    {
        return box2dBody.getBody().isActive();
    }

    /**speed is set on a x and y axis plane*/
    public void setSpeed(float xSpeed, float ySpeed)
    {
        box2dBody.getBody().setLinearVelocity(xSpeed, ySpeed);
    }

    public void applyForce(float xVal, float yVal)
    {
        box2dBody.getBody().applyLinearImpulse(xVal, yVal, getX(), getY(), true);
    }

    /**sets the position to whatever the values of xPos and yPos are*/
    public void setPosition(float xPos, float yPos)
    {
        box2dBody.getBody().setTransform(xPos, yPos, box2dBody.getBody().getAngle());
    }

    public boolean isAnimationFinished()
    {
        return animate.ifFinished();
    }

    public void setLoop(boolean loop)
    {
        animate.setLoop(loop);
    }

    public void setLoopBack(boolean val)
    {
        animate.setLoopBack(val);
    }

    public void resetAnimation()
    {
        animate.resetTime();
    }

    public float getX()
    {
        return box2dBody.getBody().getPosition().x;
    }

    public float getY()
    {
        return box2dBody.getBody().getPosition().y;
    }

    public float getVelX()
    {
        return box2dBody.getBody().getLinearVelocity().x;
    }

    public float getVelY()
    {
        return box2dBody.getBody().getLinearVelocity().y;
    }

    public float getWidth()
    {
        return animate.getWidth();
    }

    public float getHeight()
    {
        return animate.getHeight();
    }

    public Body getBody()
    {
        return box2dBody.getBody();
    }

    /**Changes the animation played on the screen*/
    public void setRegion(String key)
    {
        animate.findRegion(key);
    }

    public String getRegion()
    {
        return animate.getRegion();
    }

    public void flip()
    {
        animate.flip(0);
    }

    public void flip(float val)
    {
        animate.flip(val);
    }

    public float getDirection()
    {
        return animate.getFlipVal();
    }

    public void setNoGravity()
    {
        box2dBody.getBody().setGravityScale(0);
    }

    public void resetUserData()
    {
        box2dBody.resetUserData();
    }

    public boolean ifDisplayed()
    {
        return toDisplay;
    }

    public boolean hasOffSets()
    {
        return addOffSets;
    }

    public boolean isBackwards()
    {
        return animate.isBackwards();
    }
}
