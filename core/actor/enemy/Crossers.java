package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public final class Crossers
{
    public final CreateAnimation actor;
    private float velX = 0, xOff = 0, yOff = 0;

    public Crossers(String atlas, String region, float frame, Core core, World world,
                    float w, float h, float renderW, float renderH, Object ID)
    {
        actor = new CreateAnimation(atlas, core, BodyDef.BodyType.DynamicBody);
        actor.addRegion(region, frame);
        actor.setData(0.9f, 0, true);
        actor.setFilter(FilterID.trader_category, (short) (FilterID.player_category | FilterID.floor_category
                | FilterID.collector_category));
        actor.setUniqueID(ID);
        actor.createOriginalCustom(world, -700, -700, w, h, renderW, renderH, false);
        actor.setActive(false);
        actor.setDisplay(false);
    }

    public void setOffset(float xOff, float yOff)
    {
        this.xOff = xOff;
        this.yOff = yOff;
    }

    public void setNoGravity()
    {
        actor.setNoGravity();
    }

    public void spawn(float x, float y, float speed, boolean flip)
    {
        actor.setActive(true);
        actor.setDisplay(true);
        if(flip)
        {
            actor.flip();
            actor.setSpeed(speed * -1, 0);
        }
        else
            actor.setSpeed(speed * -1, 0);
        velX = actor.getVelX();
        actor.setPosition(x, y);
    }

    public void despawn()
    {
        actor.setSpeed(0, 0);
        actor.setPosition(-700, -700);
        actor.setDisplay(false);
        actor.setActive(false);
    }

    public boolean update(float delta, boolean portalIn, boolean doorIn, Object playerID)
    {
        if(actor.isActive())
        {
            if(delta != 0 && !portalIn && !doorIn)
            {
                if(actor.getVelX() == 0)
                    actor.setSpeed(velX, 0);
                if (actor.getX() >= 130)
                {
                    if(actor.getWidth() > 1)
                        actor.flip();
                    actor.setSpeed(actor.getVelX() * -1, 0);
                    velX = actor.getVelX();
                }
                else if (actor.getX() <= -55)
                {
                    if(actor.getWidth() < 1)
                        actor.flip();
                    actor.setSpeed(actor.getVelX() * -1, 0);
                    velX = actor.getVelX();
                }
            }
            else
            {
                if(actor.getVelX() != 0)
                    actor.setSpeed(0, 0);
            }

            if(EventManager.conditions(actor.getBody().getUserData(), playerID))
                return true;
        }

        return false;
    }

    public void display(float delta)
    {
        if(actor.ifDisplayed())
            actor.displayCustom(delta, xOff, yOff);
    }
}
