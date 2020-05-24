package com.mygdx.core.actor.engineer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.core.Core;
import com.mygdx.core.MusicPOD;
import com.mygdx.core.actor.Projectile;
import com.mygdx.core.actor.creation.CreateActor;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.animator.Animator;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;

public final class PumpkinMine
{
    private Projectile actor = null;
    private Array<Mine> miniMines = new Array<Mine>();
    private short id1 = 0, id2 = 0;
    private boolean explode = false;

    private Animator explosion = null;
    private float lastX = 0, lastY = 0;
    private boolean onScreen = false, display = false, explodeMini = false, spawnLastPos = false,
                    timerOn = false;
    private int amountOfMini = 0, timer = 0;

    public PumpkinMine()
    {
        //empty if constructing with textures
    }

    public void setFilter(short id1, short id2)
    {
        this.id1 = id1;
        this.id2 = id2;
    }

    /**@param attribute can either be a CreateAnimation or CreateTexture instance.
     *                  Can either be passed from an object or just directly create it
     *                  in the parameters.*/
    public void create(CreateActor attribute, World world, boolean gravity, Object ID, float width, float height)
    {
        actor = new Projectile(attribute, null);
        actor.setData(0.9f, 0.1f, true);
        actor.setFilter(id1, id2);
        actor.create(world, width, height, ID);
        if(!gravity)
            actor.getBody().setGravityScale(0);
        actor.getBody().setActive(false);
    }

    /**Default as an animated sprite
     * @see PumpkinMine#create(CreateActor, World, boolean, Object, float, float) above for texture based creation.*/
    public void create(String file, Core core, World world, boolean gravity, Object ID, float width, float height)
    {
        actor = new Projectile(new CreateAnimation(file, core, BodyDef.BodyType.DynamicBody), null);
        actor.setData(0.9f, 0.1f, true);
        actor.setFilter(id1, id2);
        actor.create(world, width, height, ID);
        if(!gravity)
            actor.getBody().setGravityScale(0);
        actor.getBody().setActive(false);
    }

    public void createMiniMine(String atlas, String file, Core core, World world, float width, float height)
    {
        for(int n = 0; n < 3; n++)
        {
            miniMines.add(new Mine());
            miniMines.get(n).setFilter(FilterID.mine_catageory, (short) (FilterID.floor_category
                    | FilterID.enemy_category | FilterID.hunter_category | FilterID.firing_category
                    | FilterID.rubble_category | FilterID.potion_category));
            miniMines.get(n).create(new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, file),
                            core, BodyDef.BodyType.DynamicBody),
                    world, false, file + n, width, height);
            miniMines.get(n).setExplosion(core, "effects.atlas", "explosionH");

            ObjectID.mineList.put(miniMines.get(n).getID(), false);
        }
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

    public void setSpawn(float x, float y, float xOffSet, float yOffSet)
    {
        if(!explode)
        {
            display = true;
            explosion.resetTime();
            actor.getBody().setActive(true);
            actor.getBody().setLinearVelocity(0, actor.getBody().getGravityScale());
            actor.setPosition(x + xOffSet, y + yOffSet);

            onScreen = true;
        }
    }

    public void setSpawn(Vector2 playerPos, float xOffSet, float yOffSet, float xForce, float yForce)
    {
        if(!explode)
        {
            display = true;
            explosion.resetTime();
            actor.getBody().setActive(true);
            actor.getBody().setLinearVelocity(0, actor.getBody().getGravityScale());
            actor.setPosition(playerPos.x + xOffSet, playerPos.y + yOffSet);
            actor.getBody().applyLinearImpulse(xForce, yForce, actor.getBody().getPosition().x,
                    actor.getBody().getPosition().y, true);

            onScreen = true;
        }
    }

    public boolean render(Core core, float delta)
    {
        if(explodeMini)
        {
            if(timerOn)
                timer++;
            if(amountOfMini >= 3)
            {
                spawnLastPos = false;
                explodeMini = false;
                return true;
            }
            for (int n = 0; n < miniMines.size; n++)
            {
                if(timer >= 40 && !miniMines.get(n).getAddVal())
                    miniMines.get(n).forceExplode();
                if(miniMines.get(n).render(core, delta))
                    amountOfMini++;
            }
            if(timer >= 40)
            {
                timer = 0;
                timerOn = false;
            }
        }

        if(display)
        {
            if (!explode && onScreen)
            {
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
                        //return true;
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

    public void explode()
    {
        //if((ObjectID.enemyList.containsKey(EventManager.getUserA()) && EventManager.getUserB() == actor.getID())
        //        || (ObjectID.enemyList.containsKey(EventManager.getUserB()) && EventManager.getUserA() == actor.getID()))
        if(explodeMini)
        {
            for(int n = 0; n < miniMines.size; n++)
            {
                if(miniMines.get(n).explode())
                    miniMines.get(n).setAddVal(true);
                /**
                if(spawnLastPos)
                {
                    if (miniMines.get(n).getPos().x < lastX - 20
                            || miniMines.get(n).getPos().x > lastX + 20
                            || miniMines.get(n).getPos().y < lastY - 20
                            || miniMines.get(n).getPos().y > lastY + 20
                            && !miniMines.get(n).getAddVal() && !miniMines.get(n).getExplodeVal())
                    {
                        System.out.println("force Explode");
                        miniMines.get(n).setAddVal(true);
                        miniMines.get(n).forceExplode();
                        //amountOfMini++;
                    }
                }*/
            }
        }
        if(EventManager.inContact(actor.getBody().getUserData()))
        {
            spawnLastPos = true;
            lastX = actor.getBody().getPosition().x;
            lastY = actor.getBody().getPosition().y;

            timerOn = true;
            timer = 0;
            amountOfMini = 0;
            for(int n = 0; n < miniMines.size; n++)
            {
                miniMines.get(n).despawn();
                switch (n)
                {
                    case 0:
                        miniMines.get(n).setSpawn(lastX, lastY, 5, 2);
                        miniMines.get(n).setDirSpeed(15, -45);
                        break;
                    case 1:
                        miniMines.get(n).setSpawn(lastX, lastY, -5, 2);
                        miniMines.get(n).setDirSpeed(-15, -45);
                        break;
                    case 2:
                        miniMines.get(n).setSpawn(lastX, lastY, 0, -4);
                        miniMines.get(n).setDirSpeed(0, -35);
                        break;
                    default:
                        miniMines.get(n).setSpawn(lastX, lastY, 0, 0);
                        miniMines.get(n).setDirSpeed(0, -50);
                        break;
                }
                miniMines.get(n).setAddVal(false);
            }
            explodeMini = true;
            explode = true;
        }
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

    public boolean isExplodeMiniFinished()
    {
        return explodeMini;
    }

    public boolean ifDisplay()
    {
        return display;
    }
}
