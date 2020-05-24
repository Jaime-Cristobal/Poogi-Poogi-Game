package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;

public final class MushroomBoss
{
    private CreateAnimation mushroom;
    private CreateTexture land;
    private float topBlock = 0, leftBlock = 0, rightBlock = 0, bottomBlock = 0;
    private float xVel = 0, yVel = 0;
    private int health = 15;
    private Object mushID = null, landID = null;
    private float spawnerCount = 0, spawnerRate = 0.5f;

    public void createMushroom(String atlas, String region, float frame, Core core, World world,
                               float w, float h, float renderW, float renderH, Object ID)
    {
        mushroom = new CreateAnimation(atlas, core, BodyDef.BodyType.KinematicBody);
        mushroom.addRegion(region, frame);
        mushroom.setData(0.9f, 0, true);
        mushroom.setFilter(FilterID.potion_category, (short) (FilterID.player_category | FilterID.mine_catageory
                                                    | FilterID.collector_category));
        mushroom.setUniqueID(ID);
        mushroom.createOriginalCustom(world, -700, -700, w, h, renderW, renderH, false);
        mushroom.setLoopBack(true);
        mushroom.setActive(false);
        mushroom.setDisplay(false);

        mushID = mushroom.getBody().getUserData();
    }

    public void createLand(String atlas, String texture, Core core, World world, float w, float h,
                           float renderW, float renderH, Object ID)
    {
        land = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core,
                BodyDef.BodyType.KinematicBody);
        land.setData(0.9f, 0, true);
        land.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.mine_catageory));
        land.setUniqueID(ID);
        land.createOriginalCustom(world, -700, -700, w, h, renderW, renderH, false);
        land.setActive(false);

        landID = land.getBody().getUserData();
    }

    public void setLimit(float topBlock, float leftBlock, float rightBlock, float bottomBlock, float spawnerRate)
    {
        this.topBlock = topBlock;
        this.leftBlock = leftBlock;
        this.rightBlock = rightBlock;
        this.bottomBlock = bottomBlock;

        this.spawnerRate = spawnerRate;
    }

    public void spawn(float x, float y, float xVel, float yVel)
    {
        land.setActive(true);
        land.setDisplay(true);
        land.setPosition(x, y);
        land.setSpeed(xVel, yVel);
        this.xVel = xVel;
        this.yVel = yVel;

        mushroom.setActive(true);
        mushroom.setDisplay(true);
        mushroom.setPosition(land.getX(), land.getY()+ 8);
        if(land.getVelX() < 0 && mushroom.getWidth() < 1)
            mushroom.flip();
        else if(land.getVelX() > 0 && mushroom.getWidth() > 1)
            mushroom.flip();
    }

    public void despawn()
    {
        land.setSpeed(0, 0);
        land.setPosition(-700, -700);
        land.setActive(false);
        land.setDisplay(false);

        mushroom.setSpeed(0, 0);
        mushroom.setPosition(-700, -700);
        mushroom.setActive(false);
        mushroom.setDisplay(false);
    }

    public int update(float delta, Object playerID)
    {
        if(land.isActive())
        {
            if (delta != 0)
            {
                if((ObjectID.mineList.containsKey(EventManager.getUserA()) &&
                        EventManager.getUserB() == mushroom.getBody().getUserData())
                        || (ObjectID.mineList.containsKey(EventManager.getUserB()) &&
                        EventManager.getUserA() == mushroom.getBody().getUserData()))
                {
                    health -= 2;
                }
                if((ObjectID.projectileList.containsKey(EventManager.getUserA()) &&
                        EventManager.getUserB() == mushroom.getBody().getUserData())
                        || (ObjectID.projectileList.containsKey(EventManager.getUserB()) &&
                        EventManager.getUserA() == mushroom.getBody().getUserData()))
                {
                    health -= 1;
                }

                if(health <= 0)
                {
                    despawn();
                    return 3;
                }
                if(EventManager.conditions(mushID, playerID)
                        || EventManager.conditions(landID, playerID))
                    return 2;

                spawnerCount += delta;
                if(spawnerCount >= spawnerRate)
                {
                    spawnerCount = 0;
                    return 1;
                }

                if(!mushroom.isBackwards())
                    mushroom.setActive(false);
                else
                    mushroom.setActive(true);

                if(land.getVelX() != xVel && land.getVelY() != yVel)
                    land.setSpeed(xVel, yVel);

                if(land.getX() <= leftBlock || land.getX() >= rightBlock - 5)
                {
                    xVel *= -1;
                    land.setSpeed(xVel, yVel);

                    if(land.getVelX() < 0 && mushroom.getWidth() < 1)
                        mushroom.flip();
                    else if(land.getVelX() > 0 && mushroom.getWidth() > 1)
                        mushroom.flip();
                }

                if(land.getY() >= topBlock - 5 || land.getY() <= bottomBlock)
                {
                    yVel *= -1;
                    land.setSpeed(xVel, yVel);
                }

                mushroom.setPosition(land.getX(), land.getY() + 8);
            }
            else
            {
                if(land.getVelX() != 0 && land.getVelY() != 0)
                    land.setSpeed(0, 0);
            }
        }

        return 0;
    }

    public void display(float delta)
    {
        if(land.ifDisplayed())
        {
            land.display();
            mushroom.display(delta);
        }
    }

    public float getX()
    {
        return land.getX();
    }

    public float getY()
    {
        return land.getY();
    }

    public Object getID()
    {
        return mushID;
    }

    public void setSpeedZero()
    {
        mushroom.setSpeed(0, 0);
        land.setSpeed(0, 0);
    }
}
