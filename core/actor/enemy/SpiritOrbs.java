package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;

public final class SpiritOrbs
{
    final private Array<CreateAnimation> orbs = new Array<CreateAnimation>();
    private float speed = 15;
    private int range = 20;

    public SpiritOrbs(String atlas, String region, float frame, int amount, Core core, World world,
                      float w, float h, float renderW, float renderH)
    {
        for(int n = 0; n < amount; n++)
        {
            orbs.add(new CreateAnimation(atlas, core, BodyDef.BodyType.KinematicBody));
            orbs.get(n).addRegion(region, frame);
            orbs.get(n).setData(0.1f, 0.3f, true);
            orbs.get(n).setFilter(FilterID.potion_category, (short) (FilterID.player_category | FilterID.mine_catageory
                    | FilterID.collector_category));
            orbs.get(n).setUniqueID(region + n);
            orbs.get(n).createOriginalCustom(world, -700, -700, w, h, renderW, renderH, false);
            orbs.get(n).setActive(false);
            orbs.get(n).setDisplay(false);

            ObjectID.spiritOrbs.put(orbs.get(n).getBody().getUserData(), false);
        }
    }

    public void spawn(float x, float y, int index)
    {
        if(!orbs.get(index).isActive())
        {
            orbs.get(index).setActive(true);
            orbs.get(index).setDisplay(true);
            orbs.get(index).setPosition(x, y);
        }
    }

    public void setSpeed(float val)
    {
        speed = val;
    }

    public void setRange(int val)
    {
        range = val;
    }

    public void despawn(int index)
    {
        orbs.get(index).setSpeed(0, 0);
        orbs.get(index).setPosition(-700, -700);
        orbs.get(index).setActive(false);
        orbs.get(index).setDisplay(false);
    }

    public void despawnAll()
    {
        for(int n = 0; n < orbs.size; n++)
        {
            orbs.get(n).setSpeed(0, 0);
            orbs.get(n).setPosition(-700, -700);
            orbs.get(n).setActive(false);
            orbs.get(n).setDisplay(false);
        }
    }

    private void update(int index, float playerX, float playerY, float delta)
    {
        if(orbs.get(index).isActive())
        {
            if ((playerX <= orbs.get(index).getX() + range && playerX >= orbs.get(index).getX() - range)
                    && (playerY <= orbs.get(index).getY() + range && playerY >= orbs.get(index).getY() - range))
            {
                orbs.get(index).getBody().setLinearVelocity(
                        MathUtils.cos(orbs.get(index).getBody().getAngle()) * speed,
                        MathUtils.sin(orbs.get(index).getBody().getAngle()) * speed);
                orbs.get(index).getBody().setTransform(orbs.get(index).getBody().getPosition(),
                        MathUtils.atan2(playerY - orbs.get(index).getBody().getPosition().y,
                                playerX - orbs.get(index).getBody().getPosition().x));
            }
            else
            {
                if (orbs.get(index).getVelX() != 0 && orbs.get(index).getVelY() != 0)
                    orbs.get(index).setSpeed(0, 0);
            }
        }
    }

    public void display(float delta, float playerX, float playerY, boolean doorCollide, boolean portalCollide)
    {
        for(int n = 0; n < orbs.size; n++)
        {
            if(orbs.get(n).ifDisplayed())
            {
                if(EventManager.inContact(orbs.get(n).getBody().getUserData()))
                {
                    despawn(n);
                    continue;
                }
                if(delta != 0 && !doorCollide && !portalCollide)
                    update(n, playerX, playerY, delta);
                else
                {
                    if(orbs.get(n).getVelX() != 0 && orbs.get(n).getVelY() != 0)
                        orbs.get(n).setSpeed(0, 0);
                }
                orbs.get(n).display(delta);
            }
        }
    }

    public int getSize()
    {
        return orbs.size;
    }
}
