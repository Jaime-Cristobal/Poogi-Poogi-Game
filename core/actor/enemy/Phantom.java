package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public final class Phantom
{
    final private CreateAnimation actor;
    final private Array<CreateAnimation> mirrors = new Array<CreateAnimation>();
    private boolean mirrorsSpawn = false, displayMirrors = false;
    private float lastX = 0, lastY = 0;
    private int timer = 0;

    public Phantom(Core core, World world)
    {
        actor = new CreateAnimation("characters.atlas", core, BodyDef.BodyType.KinematicBody);
        actor.addRegion("phantomMove", 4f);
        actor.addRegion("esquibeth", 2f);
        actor.addRegion("phantomStand", 4f);
        actor.setData(0.9f, 0, true);
        actor.setFilter(FilterID.hunter_category, (short)(FilterID.player_category | FilterID.enemy_category |
                FilterID.floor_category | FilterID.mine_catageory));
        actor.setUniqueID("phantom");
        actor.createOriginalCustom(world, -700, -700, 60, 60, 72, 107, false);
        actor.setDisplay(false);
        actor.setActive(false);
        actor.setNoGravity();
        actor.setRegion("phantomMove");

        for(int n = 0; n < 3; n++)
        {
            mirrors.add(new CreateAnimation("characters.atlas", core, BodyDef.BodyType.KinematicBody));
            mirrors.get(n).addRegion("eggTrader", 2f);
            mirrors.get(n).setData(0.9f, 0, true);
            mirrors.get(n).setFilter(FilterID.hunter_category, (short)(FilterID.player_category | FilterID.enemy_category |
                    FilterID.floor_category | FilterID.mine_catageory));
            mirrors.get(n).setUniqueID("mirrors" + n);
            mirrors.get(n).createOriginalCustom(world, -700, -700, 15, 15, 40, 44, false);
            mirrors.get(n).setDisplay(false);
            mirrors.get(n).setActive(false);
            mirrors.get(n).setNoGravity();
        }
    }

    public void spawnPhantom(float x, float y)
    {
        actor.setActive(true);
        actor.setDisplay(true);
        actor.setPosition(x, y);
    }

    public void spawnMirrors()
    {
        if(!mirrorsSpawn)
            mirrorsSpawn = true;
        for(int n = 0; n < mirrors.size; n++)
        {
            if(!mirrors.get(n).ifDisplayed() || !mirrors.get(n).isActive())
            {
                mirrors.get(n).setActive(true);
                mirrors.get(n).setDisplay(true);
                mirrors.get(n).setPosition(actor.getX() + MathUtils.random(-15, 15),
                                                actor.getY() + MathUtils.random(-15, 15));
            }
        }
    }

    public void despawnAll()
    {
        actor.setActive(false);
        actor.setDisplay(false);
        actor.setPosition(-700, -700);

        for(int n = 0; n < mirrors.size; n++)
        {
            mirrors.get(n).setActive(false);
            mirrors.get(n).setDisplay(false);
            mirrors.get(n).setPosition(-700, -700);
        }
        mirrorsSpawn = false;
    }

    /**main phantom can only be killed by the player touching it*/
    public boolean update(float playerX, float playerY, Object playerID, float delta)
    {
        if(actor.isActive() && actor.ifDisplayed() && delta != 0)
        {
            System.out.println(timer);
            timer++;
            if(timer >= 300)
            {
                //if(!mirrorsSpawn)
                    spawnMirrors();
                timer = 0;
                //mirrorsSpawn = true;
            }
            else
            {
                if(mirrorsSpawn)
                    mirrorsSpawn = false;
            }
            if((playerX < actor.getX() + 30 && playerX > actor.getX() - 30) &&
                    (playerY < actor.getY() + 30 && playerY > actor.getY() - 30))
            {
                if(EventManager.conditions(actor.getBody().getUserData(), playerID))
                {
                    despawnAll();
                    return true;
                }

                if(playerY > actor.getY() && playerX > actor.getX())
                    actor.getBody().setLinearVelocity(-10, 0);
                if(playerY < actor.getY() && playerX > actor.getX())
                    actor.getBody().setLinearVelocity(-10, 0);

                if(playerY > actor.getY() && playerX < actor.getX())
                    actor.getBody().setLinearVelocity(10, 0);
                if(playerY < actor.getY() && playerX < actor.getX())
                    actor.getBody().setLinearVelocity(10, 0);

                if(playerX > actor.getX())
                {
                    if(actor.getDirection() > 0)
                        actor.flip();
                }
                else
                {
                    if(actor.getDirection() < 0)
                        actor.flip();
                }
            }
            else
            {
                if(actor.getVelX() != 0 || actor.getVelY() != 0)
                    actor.setSpeed(0, 0);
            }
        }

        return false;
    }

    public boolean display(float playerX, float playerY, Object playerID, float delta)
    {
        if(actor.isActive() && actor.ifDisplayed())
        {
            actor.displayCustom(delta, 0, 0);

            for (int n = 0; n < mirrors.size; n++)
            {
                if (mirrors.get(n).ifDisplayed() && mirrors.get(n).isActive())
                {
                    //collision

                    if (EventManager.conditions(mirrors.get(n).getBody().getUserData(), playerID))
                    {
                        mirrors.get(n).setDisplay(false);
                        mirrors.get(n).setActive(false);
                        mirrors.get(n).setPosition(-700, -700);
                        return true;
                    }

                    //***************tracks the player BLOCK********************
                    if((playerX < mirrors.get(n).getX() + 20 && playerX > mirrors.get(n).getX() - 20) &&
                            (playerY < mirrors.get(n).getY() + 20 && playerY > mirrors.get(n).getY() - 20))
                    {
                        mirrors.get(n).getBody().setLinearVelocity(MathUtils.cos(mirrors.get(n).getBody().getAngle()) * 30,
                                MathUtils.sin(mirrors.get(n).getBody().getAngle()) * 30);
                    }
                    else
                    {
                        if (mirrors.get(n).getX() < playerX)
                        {
                            if (mirrors.get(n).getDirection() > 0)
                                mirrors.get(n).flip();
                        }
                        else
                        {
                            if (mirrors.get(n).getDirection() < 0)
                                mirrors.get(n).flip();
                        }
                        mirrors.get(n).getBody().setLinearVelocity(
                                MathUtils.cos(mirrors.get(n).getBody().getAngle()) * 6,
                                MathUtils.sin(mirrors.get(n).getBody().getAngle()) * 6);
                        mirrors.get(n).getBody().setTransform(mirrors.get(n).getBody().getPosition(),
                                MathUtils.atan2(playerY - mirrors.get(n).getBody().getPosition().y,
                                        playerX - mirrors.get(n).getBody().getPosition().x));
                    }
                    //***************tracks the player BLOCK********************

                    //render
                    mirrors.get(n).display(delta);
                }
            }
        }

        return false;
    }
}
