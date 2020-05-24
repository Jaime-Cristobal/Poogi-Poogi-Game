package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.MusicPOD;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.engineer.Laser;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;

public final class TikiBoss
{
    final private Core core;
    final private CreateAnimation actor;
    private Laser lasers;
    private int ammunation = -1;
    private float fireRate = 0, maxRate = 0.55f;
    private boolean foundTarget = false;
    private float timerReload = 0;      //reloads at 15
    private float rateFinder = 0, maxRateFinder = 0.5f;
    private int health = 15, originalHP = 15;
    private float chargeSpeed = 30, moveSpeed = 20;

    public TikiBoss(String file, String region, Core core, World world, Object ID, float w, float h, float renderW, float renderH)
    {
        this.core = core;
        actor = new CreateAnimation(file, this.core, BodyDef.BodyType.DynamicBody);
        actor.addRegion(region, 1f);
        actor.setData(0.9f, 0, true);
        actor.setFilter(FilterID.firing_category, (short)(FilterID.player_category | FilterID.collector_category
                                                        | FilterID.platform_category | FilterID.mine_catageory));

        actor.setUniqueID(ID);
        actor.createOriginalCustom(world, -700, -700, w, h, renderW, renderH, false);
        actor.setNoGravity();
        actor.setActive(false);
        actor.setDisplay(false);
        actor.setLoopBack(true);

        lasers = new Laser("purpleBeam", this.core, world, 10, false);
        lasers.setLimitFromOrigin(35);
    }

    public void setMaxRate(float maxRate)
    {
        this.maxRate = maxRate;
    }

    public void setMaxRateFinder(float val)
    {
        this.maxRateFinder = val;
    }

    public void spawn(float x, float y)
    {
        actor.setActive(true);
        actor.setDisplay(true);
        actor.setPosition(x, y);
        health = originalHP;
    }

    public void despawn()
    {
        actor.setSpeed(0, 0);
        actor.setPosition(-700, -700);
        actor.setDisplay(false);
        actor.setActive(false);
    }

    public void setHealth(int health)
    {
        this.health = health;
        originalHP = this.health;
    }

    public void setSpeed(float moveSpeed, float chargeSpeed)
    {
        this.moveSpeed = moveSpeed;
        this.chargeSpeed = chargeSpeed;
    }

    public boolean update(float delta, float playerX, float playerY)
    {
        if(delta != 0)
        {
            if (actor.isActive())
            {
                if ((ObjectID.projectileList.containsKey(EventManager.getUserA()) &&
                        EventManager.getUserB() == actor.getBody().getUserData())
                        || (ObjectID.projectileList.containsKey(EventManager.getUserB()) &&
                        EventManager.getUserA() == actor.getBody().getUserData()))
                {
                    health--;
                }
                if ((ObjectID.mineList.containsKey(EventManager.getUserA()) &&
                        EventManager.getUserB() == actor.getBody().getUserData())
                        || (ObjectID.mineList.containsKey(EventManager.getUserB()) &&
                        EventManager.getUserA() == actor.getBody().getUserData()))
                {
                    health -= 3;
                }
                if (health <= 0)
                {
                    despawn();
                    return true;
                }

                if ((playerX < actor.getX() + 20 && playerX > actor.getX() - 20) &&
                        (playerY < actor.getY() + 20 && playerY > actor.getY() - 20))
                {
                    rateFinder += delta;
                    if (rateFinder >= 0.5f)
                        actor.getBody().setLinearVelocity(MathUtils.cos(actor.getBody().getAngle()) * chargeSpeed,
                                MathUtils.sin(actor.getBody().getAngle()) * chargeSpeed);

                    if (ammunation < 10)
                    {
                        fireRate += delta;
                        if (fireRate >= maxRate)
                        {
                            if(!core.music.musicOff)
                            {
                                core.sounds.laser5.stop();
                                core.sounds.laser5.play();
                            }
                            ammunation++;
                            if (ammunation >= lasers.getSize())
                                ammunation = 0;
                            lasers.setSpawn(actor.getX(), actor.getY(), ammunation, actor.getDirection());
                            fireRate = 0;
                        }
                    }
                    lasers.update();
                }
                else
                {
                    rateFinder += delta;
                    if (rateFinder >= maxRateFinder)
                    {
                        rateFinder = 0;
                        if (actor.getX() < playerX)
                        {
                            if (actor.getDirection() > 0)
                                actor.flip();
                        }
                        else
                        {
                            if (actor.getDirection() < 0)
                                actor.flip();
                        }
                        actor.getBody().setLinearVelocity(
                                MathUtils.cos(actor.getBody().getAngle()) * moveSpeed,
                                MathUtils.sin(actor.getBody().getAngle()) * moveSpeed);
                        actor.getBody().setTransform(actor.getBody().getPosition(),
                                MathUtils.atan2(playerY - actor.getBody().getPosition().y,
                                        playerX - actor.getBody().getPosition().x));
                    }
                }
            }
        }
        else
        {
            if(actor.getVelX() != 0 && actor.getVelY() != 0)
                actor.setSpeed(0, 0);
        }

        return false;
    }

    public void render(float delta)
    {
        if(actor.ifDisplayed())
        {
            lasers.render(core, delta);
            actor.display(delta);
        }
    }

    public boolean inContact(Object id)
    {
        return EventManager.conditions(actor.getBody().getUserData(), id);
    }
}
