package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateActor;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;

public final class Rubble
{
    private BodyDef.BodyType type;
    private Array<CreateActor> obj = new Array<CreateActor>();
    private float minX = 0, maxX = 0, minY = 0, maxY = 0;
    private float distX = 0, distY = 0, xVel = 0, yVel = 0;
    private boolean willRespawn = false, displayAll = false, haveOffset = false, rotation = false;
    private float offsetX = 0, offsetY = 0;

    public Rubble(BodyDef.BodyType type, boolean rotation)
    {
        this.type = type;
        this.rotation = rotation;
    }

    public void addTexture(String atlas, String texture, Core core, World world, int amount,
                              float w, float h, float renderW, float renderH, boolean gravity)
    {
        int maxSize = obj.size + amount;
        for(int n = obj.size; n < maxSize; n++)
        {
            obj.add(new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core, type));
            obj.get(n).setData(0.9f, 0, rotation);
            obj.get(n).setFilter(FilterID.rubble_category, (short) (FilterID.player_category
                                                                    | FilterID.mine_catageory | FilterID.collector_category));
            String id = texture + n;
            obj.get(n).setUniqueID(id);
            obj.get(n).createOriginalCustom(world, -700, -700, w, h, renderW, renderH, false);
            if(!gravity)
                obj.get(n).setNoGravity();
            obj.get(n).setActive(false);
            obj.get(n).setDisplay(false);

            ObjectID.rubble.put(obj.get(n).getBody().getUserData(), false);
        }
    }

    public void addAnimation(String atlas, String region, float frame, Core core, World world, int amount,
                              float w, float h, float renderW, float renderH, boolean gravity)
    {
        for(int n = 0; n < amount; n++)
        {
            obj.add(new CreateAnimation(atlas, core, type));
            ((CreateAnimation)obj.get(n)).addRegion(region, frame);
            obj.get(n).setData(0.9f, 0, rotation);
            obj.get(n).setFilter(FilterID.rubble_category, (short) (FilterID.player_category
                                                                | FilterID.mine_catageory | FilterID.collector_category));
            obj.get(n).setUniqueID("t" + region + n);
            obj.get(n).createOriginalCustom(world, -700, -700, w, h, renderW, renderH, false);
            if(!gravity)
                obj.get(n).setNoGravity();
            obj.get(n).setActive(false);
            obj.get(n).setDisplay(false);

            ObjectID.rubble.put(obj.get(n).getBody().getUserData(), false);
        }
    }

    public void setOffset(float xOff, float yOff)
    {
        offsetX = xOff;
        offsetY = yOff;
        haveOffset = true;
    }

    public void enableRespawn(float minX, float maxX, float minY, float maxY)
    {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        willRespawn = true;
    }

    public void disableRespawn()
    {
        minY = 0; maxY = 0;
        minX = 0; maxX = 0;
        willRespawn = false;
    }

    public void spawn(float x, float y, float xVel, float yVel, float distX, float distY)
    {
        for(int n = 0; n < obj.size; n++)
        {
            obj.get(n).setActive(true);
            obj.get(n).setDisplay(true);
            if(willRespawn)
                obj.get(n).setPosition(MathUtils.random(minX, maxX),
                        MathUtils.random(minY, maxY));
            else
                obj.get(n).setPosition(x, y);
            obj.get(n).setSpeed(xVel, yVel);
        }
        this.xVel = xVel;
        this.yVel = yVel;
        this.distX = distX;
        this.distY = distY;
        displayAll = true;
    }

    public void spawn(float x, float y, float xVel, float yVel, float distX, float distY, float rotation)
    {
        for(int n = 0; n < obj.size; n++)
        {
            obj.get(n).setActive(true);
            obj.get(n).setDisplay(true);
            if(willRespawn)
                obj.get(n).setPosition(MathUtils.random(minX, maxX),
                        MathUtils.random(minY, maxY));
            else
                obj.get(n).setPosition(x, y);
            obj.get(n).setSpeed(xVel, yVel);
            obj.get(n).getBody().setAngularVelocity(rotation);
        }
        this.xVel = xVel;
        this.yVel = yVel;
        this.distX = distX;
        this.distY = distY;
        displayAll = true;
    }

    public void despawn()
    {
        for(int n = 0; n < obj.size; n++)
        {
            obj.get(n).setPosition(-800, -800);
            obj.get(n).setSpeed(0, 0);
            obj.get(n).getBody().setAngularVelocity(0);
            obj.get(n).setActive(false);
            obj.get(n).setDisplay(false);
        }
        displayAll = false;
    }

    public void respawn()
    {
        for(int n = 0; n < obj.size; n++)
        {
            obj.get(n).setPosition(MathUtils.random(minX, maxX),
                    MathUtils.random(minY, maxY));
        }
    }

    public void display(float delta, boolean doorCollide, boolean portalCollide, boolean playerIn)
    {
        if(displayAll)
        {
            for (int n = 0; n < obj.size; n++)
            {
                if(delta != 0 && !doorCollide && !portalCollide && !playerIn)
                {
                    if (obj.get(n).isActive())
                    {
                        if(obj.get(n).getVelX() != xVel || obj.get(n).getVelY() != yVel)
                            obj.get(n).setSpeed(xVel, yVel);

                        if (obj.get(n).getX() >= distX || obj.get(n).getY() <= distY || obj.get(n).getY() >= 150)
                        {
                            if (willRespawn)
                            {
                                obj.get(n).setPosition(MathUtils.random(minX, maxX),
                                        MathUtils.random(minY, maxY));
                            }
                            else {
                                obj.get(n).setPosition(-800, -800);
                                obj.get(n).setSpeed(0, 0);
                                obj.get(n).getBody().setAngularVelocity(0);
                                obj.get(n).setActive(false);
                                obj.get(n).setDisplay(false);
                            }
                        }
                    }
                }
                else
                {
                    if(obj.get(n).getVelX() != 0 || obj.get(n).getVelY() != 0)
                        obj.get(n).setSpeed(0, 0);
                }

                if(obj.get(n).ifDisplayed())
                {
                    if(haveOffset)
                        obj.get(n).displayCustom(delta, offsetX, offsetY);
                    else
                        obj.get(n).display(delta);
                }
            }
        }
    }
}
