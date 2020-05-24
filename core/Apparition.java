package com.mygdx.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.animator.Animator;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;

public final class Apparition
{
    final private CreateAnimation actor;
    private float rateFinder = 0;

    public Apparition(Core core, World world)
    {
        actor = new CreateAnimation("characters.atlas", core, BodyDef.BodyType.DynamicBody);
        actor.addRegion("apparition", 2f);
        actor.setData(0.9f, 0, true);
        actor.setFilter(FilterID.boss_category, (short)(FilterID.player_category | FilterID.enemy_category |
                        FilterID.floor_category | FilterID.egg_category));
        actor.setUniqueID("appartition");
        actor.createOriginalCustom(world, -700, -700, 100, 100, 385, 233, false);
        actor.setDisplay(false);
        actor.setActive(false);
        actor.setNoGravity();

        ObjectID.enemyList.put(actor.getBody().getUserData(), false);
    }

    public void update(float playerX, float playerY, float delta)
    {
        if(actor.isActive() && delta != 0)
        {
            rateFinder += delta;
            if(rateFinder >= 0.7f)
            {
                rateFinder = 0;
                actor.getBody().setLinearVelocity(
                        MathUtils.cos(actor.getBody().getAngle()) * 20,
                        MathUtils.sin(actor.getBody().getAngle()) * 20);
                actor.getBody().setTransform(actor.getBody().getPosition(),
                        MathUtils.atan2(playerY - actor.getBody().getPosition().y,
                                playerX - actor.getBody().getPosition().x));
            }
        }
        else
            actor.getBody().setLinearVelocity(0, 0);
    }

    public void display(float delta)
    {
        if(actor.isActive())
            actor.display(delta);
    }

    public void spawn(float x, float y)
    {
        actor.setDisplay(true);
        actor.setActive(true);
        actor.setPosition(x, y);
    }

    public void despawn()
    {
        actor.setPosition(-700, -700);
        actor.setActive(false);
        actor.setDisplay(false);
    }

    public void setSpeed(float velX, float velY)
    {
        actor.setSpeed(velX, velY);
    }

    public boolean ifCollison()
    {
        return EventManager.inContact(actor.getBody().getUserData());
    }

    public float getX()
    {
        return actor.getX();
    }

    public float getY()
    {
        return actor.getY();
    }

    public boolean getDisplayOrActive()
    {
        return actor.ifDisplayed() || actor.isActive();
    }

    public boolean isSpeedZero()
    {
        return actor.getVelX() == 0 || actor.getVelY() == 0;
    }
}
