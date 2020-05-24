package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.engineer.Laser;
import com.mygdx.core.animator.Animator;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public final class TikiBomb
{
    final private CreateAnimation tiki;
    final private Animator explosion;
    private float lastX = 0, lastY = 0;
    private boolean explode = false;
    private Object tikiID;

    public TikiBomb(String atlas, String region, float frame, Core core, World world,
                               float w, float h, float renderW, float renderH, Object ID)
    {
        tiki = new CreateAnimation(atlas, core, BodyDef.BodyType.KinematicBody);
        tiki.addRegion(region, frame);
        tiki.setData(0.9f, 0, true);
        tiki.setFilter(FilterID.potion_category, (short) (FilterID.player_category | FilterID.mine_catageory
                | FilterID.collector_category));
        tiki.setUniqueID(ID);
        tiki.createOriginalCustom(world, -700, -700, w, h, renderW, renderH, false);
        tiki.setLoopBack(true);
        tiki.setActive(false);
        tiki.setDisplay(false);

        tikiID = tiki.getBody().getUserData();

        explosion = new Animator("effects.atlas", core);
        explosion.addRegion("explosion4Half", 1f);
        explosion.setScale(256, 256);
        explosion.findRegion(region);
        explosion.setLoop(false);
    }

    public void spawn(float x, float y)
    {
        if(!tiki.isActive())
        {
            tiki.setActive(true);
            tiki.setDisplay(true);
            tiki.setPosition(x, y);
        }
    }

    public void despawn()
    {
        tiki.setPosition(-700, -700);
        tiki.setActive(false);
        tiki.setDisplay(false);
    }

    public int update(float delta, float playerX, float playerY)
    {
        if(tiki.isActive() && delta != 0)
        {
            if(EventManager.inContact(tiki.getBody().getUserData()))
            {
                explode = true;
                lastX = tiki.getX();
                lastY = tiki.getY();
                despawn();
            }
            else if(((playerX >= tiki.getX() - 8 && playerX <= tiki.getX() + 8) &&
                    (playerY >= tiki.getY() - 8 && playerY <= tiki.getY() + 8)))
            {
                explode = true;
                lastX = tiki.getX();
                lastY = tiki.getY();
                despawn();

                if(playerX <= lastX)
                    return 1;
                if(playerX >= lastX)
                    return 2;
            }
        }
        return 0;
    }

    public void display(Batch batch, float delta)
    {
        if(tiki.ifDisplayed())
        {
            if(!explode)
                tiki.display(delta);
        }
        else
        {
            if (explosion != null && explode)
            {
                if (tiki.getBody().isActive())
                {
                    tiki.setPosition(-700, -700);
                    tiki.getBody().setActive(false);
                }

                explosion.recordEndTime();
                if (!explosion.ifFinished())
                    explosion.render(batch, lastX, lastY, delta);
                else
                {
                    explode = false;
                    explosion.resetTime();
                }
            }
        }
    }
}
