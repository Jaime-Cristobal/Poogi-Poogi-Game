package com.mygdx.core.actor.engineer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.core.Core;
import com.mygdx.core.MusicPOD;
import com.mygdx.core.actor.Projectile;
import com.mygdx.core.actor.creation.CreateActor;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.animator.Animator;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.ObjectID;

public final class Mine
{
    private Projectile actor = null;
    private short id1 = 0, id2 = 0;
    private boolean explode = false;

    private Animator explosion = null;
    private float lastX = 0, lastY = 0;
    private boolean onScreen = false, display = false;
    private boolean addVal = false; //for pumpkin mine mini mines
    private boolean limitAtOrigin = false;
    private float minLimitX = -100, maxLimitX = 150, minLimitY = -100, maxLimitY = 300;
    private int originLimit = 20;
    private float density = 0.9f;

    public Mine ()
    {
        //empty if constructing with textures
    }

    public void setFilter(short id1, short id2)
    {
        this.id1 = id1;
        this.id2 = id2;
    }

    public void setLimits(float minX, float maxX, float minY, float maxY)
    {
        minLimitX = minX;
        maxLimitX = maxX;
        minLimitY = minY;
        maxLimitY = maxY;
    }

    public void setLimitAtOrigin()
    {
        limitAtOrigin = true;
    }

    public void setDensity(float density)
    {
        this.density = density;
    }

    public void setOriginLimit(int val)
    {
        originLimit = val;
    }

    /**@param attribute can either be a CreateAnimation or CreateTexture instance.
     *                  Can either be passed from an object or just directly create it
     *                  in the parameters.*/
    public void create(CreateActor attribute, World world, boolean gravity, Object ID, float width, float height)
    {
        actor = new Projectile(attribute, null);
        actor.setData(density, 0, true);
        actor.setFilter(id1, id2);
        actor.create(world, width, height, ID);
        if(!gravity)
            actor.getBody().setGravityScale(0);
        actor.getBody().setActive(false);
    }

    /**Default as an animated sprite
     * @see Mine#create(CreateActor, World, boolean, Object, float, float) above for texture based creation.*/
    public void create(String file, Core core, World world, boolean gravity, Object ID, float width, float height)
    {
        actor = new Projectile(new CreateAnimation(file, core, BodyDef.BodyType.DynamicBody), null);
        actor.setData(density, 0.1f, true);
        actor.setFilter(id1, id2);
        actor.create(world, width, height, ID);
        if(!gravity)
            actor.getBody().setGravityScale(0);
        actor.getBody().setActive(false);
    }

    public void setExplosion(Core core, String file, String region)
    {
        explosion = new Animator(file, core);
        explosion.addRegion(region, 1f);
        explosion.findRegion(region);
        explosion.setLoop(false);
    }

    public void setExplosion(Core core, String file, String region, float width, float height)
    {
        setExplosion(core, file, region);
        explosion.setScale(width, height);
    }

    public void setSpawn(Vector2 playerPos, float xOffSet, float yOffSet)
    {
        if(!explode)
        {
            if(limitAtOrigin)
            {
                minLimitX = playerPos.x - originLimit;
                maxLimitX = playerPos.x + originLimit;
                minLimitY = playerPos.y - 20;
                maxLimitY = playerPos.y + 20;
            }
            display = true;
            explosion.resetTime();
            actor.getBody().setActive(true);
            actor.getBody().setLinearVelocity(0, actor.getBody().getGravityScale());
            actor.setPosition(playerPos.x + xOffSet, playerPos.y + yOffSet);

            onScreen = true;
        }
    }

    public void setSpawn(float x, float y, float xOffSet, float yOffSet)
    {
        if(!explode)
        {
            if(limitAtOrigin)
            {
                minLimitX = x - originLimit;
                maxLimitX = x + originLimit;
                minLimitY = y - 20;
                maxLimitY = y + 20;
            }
            display = true;
            explosion.resetTime();
            actor.getBody().setActive(true);
            actor.getBody().setLinearVelocity(0, actor.getBody().getGravityScale());
            actor.setPosition(x + xOffSet, y + yOffSet);

            onScreen = true;
        }
    }

    public void noDisplay()
    {
        display = false;
        onScreen = false;
    }

    public void setSpawn(float x, float y, float xOffSet, float yOffSet, float xVel, float yVel)
    {
        if(!explode)
        {
            if(limitAtOrigin)
            {
                minLimitX = x - originLimit;
                maxLimitX = x + originLimit;
                minLimitY = y - 20;
                maxLimitY = y + 20;
            }
            display = true;
            explosion.resetTime();
            actor.getBody().setActive(true);
            actor.getBody().setLinearVelocity(xVel, yVel);
            actor.setPosition(x + xOffSet, y + yOffSet);

            onScreen = true;
        }
    }

    public void setDirSpeed(float xVel, float yVel)
    {
        actor.getBody().setLinearVelocity(xVel, yVel);
    }

    public void despawn()
    {
        actor.setPosition(-700, -800);
        actor.getBody().setActive(false);
        display = false;
        explode = false;
        onScreen = false;
    }

    public boolean render(Core core, float delta)
    {
        if(display)
        {
            if (!explode && onScreen)
            {
                if(actor.getBody().getPosition().x < minLimitX || actor.getBody().getPosition().x > maxLimitX
                        || actor.getBody().getPosition().y < minLimitY || actor.getBody().getPosition().y > maxLimitY)
                {
                    System.out.println("mine despawned");
                    despawn();
                    return true;
                }
                if (actor.getBody().isActive())
                    actor.render(delta);
            }
            else
            {
                if (explosion != null && explode)
                {
                    if (actor.getBody().isActive())
                    {
                        actor.setPosition(-50, -50);
                        actor.getBody().setActive(false);
                        if(!core.music.musicOff)
                        {
                            core.sounds.explode.stop();
                            core.sounds.explode.play(0.5f);
                        }
                    }

                    explosion.recordEndTime();
                    if (!explosion.ifFinished())
                        explosion.render(core.batch, lastX, lastY, delta);
                    else
                    {
                        display = false;
                        explode = false;
                        explosion.resetTime();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public Vector2 getPos()
    {
        return actor.getBody().getPosition();
    }

    public boolean explode()
    {
        //if((ObjectID.enemyList.containsKey(EventManager.getUserA()) && EventManager.getUserB() == actor.getID())
        //        || (ObjectID.enemyList.containsKey(EventManager.getUserB()) && EventManager.getUserA() == actor.getID()))
        if(EventManager.inContact(actor.getBody().getUserData()))
        {
            lastX = actor.getBody().getPosition().x;
            lastY = actor.getBody().getPosition().y;

            explode = true;
            return true;
        }

        return false;
    }

    public void forceExplode()
    {
        lastX = actor.getBody().getPosition().x;
        lastY = actor.getBody().getPosition().y;

        explode = true;
    }

    public void detection(Vector2 position, float xOffset, float yOffset)
    {
        if((position.x > actor.getBody().getPosition().x + xOffset || position.x < actor.getBody().getPosition().x - xOffset)
                && (position.y > actor.getBody().getPosition().y + yOffset || position.y < actor.getBody().getPosition().y - yOffset))
        {
            explode = true;
            actor.setPosition(-300, -300);
            actor.getBody().setActive(false);

            //set either the explosion animation here or explicitely by externally calling explode
        }
    }

    public void contact(Object ID)
    {
        if(EventManager.conditions(actor.getID(), ID))
        {
            explode = true;
            actor.setPosition(-300, -300);
            actor.getBody().setActive(false);

            //set either the explosion animation here or explicitely by externally calling explode
        }
    }

    public void reset()
    {
        explode = false;
    }

    public boolean getExplodeVal()
    {
        return explode;
    }

    public Object getID()
    {
        return actor.getID();
    }

    public boolean ifDisplay()
    {
        return display;
    }

    public void setAddVal(boolean val)
    {
        addVal = val;
    }

    public boolean getAddVal()
    {
        return addVal;
    }

    public float getLastX()
    {
        return lastX;
    }

    public float getLastY()
    {
        return lastY;
    }

    public float getX()
    {
        return actor.getBody().getPosition().x;
    }

    public float getY()
    {
        return actor.getBody().getPosition().y;
    }

    public void noGravity()
    {
        actor.getBody().setGravityScale(0);
    }
}
