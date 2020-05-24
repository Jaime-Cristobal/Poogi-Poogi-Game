package com.mygdx.core.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.EggCount;
import com.mygdx.core.EggPOD;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.actor.creation.RenderObject;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public final class Egg implements RenderObject
{
    final private CreateTexture actor;
    final private int eggID;
    private boolean despawned = false, collide = false;
    private int eggType = 1; //determines if it is good (adds) 1 or bad (subtracts) 0
    private boolean containPortal = false;

    public Egg(String file, int eggID, Core core, World world, float w, float h, Object ID)
    {
        actor = new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", file), core,
                BodyDef.BodyType.DynamicBody);
        actor.setData(0.1f, 0, true);
        actor.setFilter(FilterID.egg_category, (short) (FilterID.player_category |
                FilterID.floor_category | FilterID.mine_catageory | FilterID.boss_category | FilterID.collector_category));
        actor.setUniqueID(ID);
        //temp.setOffSet(xSetOff, ySetOff);
        actor.create(world, -700, -700, w, h, false);
        actor.setActive(false);

        this.eggID = eggID;
    }

    public void respawn(float x, float y)
    {
        despawned = false;
        actor.setPosition(x, y);
        actor.setActive(true);
        actor.setDisplay(true);
    }

    public void despawn()
    {
        actor.setPosition(-200, -200);
        actor.setActive(false);
        actor.setDisplay(false);
    }

    public boolean ifCollide(Object id)
    {
        return EventManager.conditions(actor.getBody().getUserData(), id);
    }

    public boolean despawnIfCollide(Object id)
    {
        if(EventManager.conditions(actor.getBody().getUserData(), id))
        {
            actor.setPosition(-700, -700);
            actor.setActive(false);
            despawned = true;
            return true;
        }
        return false;
    }

    public void setCollision(Object id)
    {
        if(EventManager.conditions(actor.getBody().getUserData(), id))
            despawn();
    }

    public void setDisplay(boolean val)
    {
        actor.setDisplay(val);
    }

    public void update()
    {
        eggType = 1;
        if (eggID == EggPOD.CHICKEN_EGG)
        {
            if(EggPOD.doubleOn)
                EggCount.addEgg(4);
            else
                EggCount.addEgg(2);
        }
        else if (eggID == EggPOD.EGG_PLAIN)
        {
            if(EggPOD.doubleOn)
                EggCount.addEgg(2);
            else
                EggCount.addEgg(1);
        }
        else if (eggID == EggPOD.EGGHUNT_EGG1)
        {
            if(EggPOD.doubleOn)
                EggCount.addEgg(4);
            else
                EggCount.addEgg(MathUtils.random(1, 2));
        }
        else if (eggID == EggPOD.EGGHUNT_EGG2)
        {
            EggCount.addEgg(-1);
            eggType = 0;
        }
        else if (eggID == EggPOD.EGGHUNT_EGG3)
        {
            if(EggPOD.doubleOn)
                EggCount.addEgg(8);
            else
                EggCount.addEgg(MathUtils.random(1, 4));
        }
        else if (eggID == EggPOD.EGGHUNT_EGG4)
        {
            int val = MathUtils.random(-10, 5);

            if(EggPOD.doubleOn)
                EggCount.addEgg(10);
            else
                EggCount.addEgg(val);
            if(EggCount.eggAmount < 0)
                EggCount.eggAmount = 0;
            if(val < 0)
                eggType = 0;
        }
        else if (eggID == EggPOD.BUTTERFLY_EGG)
        {
            EggPOD.butterflySpawn = true;
        }

        System.out.println("egg type is " + eggType);
        collide = true;
    }

    public void update(Object playerID)
    {
        /**
        if (despawnIfCollide(playerID))
        {
            eggType = 1;
            if (eggID == EggPOD.CHICKEN_EGG)
            {
                EggCount.eggAmount += 2;
            }
            else if (eggID == EggPOD.EGG_PLAIN)
            {
                EggCount.eggAmount += 1;
            }
            else if (eggID == EggPOD.EGGHUNT_EGG1)
            {
                EggCount.eggAmount += MathUtils.random(0, 2);
            }
            else if (eggID == EggPOD.EGGHUNT_EGG2)
            {
                if (EggCount.eggAmount - 1 >= 0)
                    EggCount.eggAmount -= 1;
                eggType = 0;
            }
            else if (eggID == EggPOD.EGGHUNT_EGG3)
            {
                EggCount.eggAmount += MathUtils.random(1, 4);
            }
            else if (eggID == EggPOD.EGGHUNT_EGG4)
            {
                int val = MathUtils.random(-10, 5);
                EggCount.eggAmount += val;
                if(EggCount.eggAmount < 0)
                    EggCount.eggAmount = 0;
                if(val < 0)
                    eggType = 0;
            }
            else if (eggID == EggPOD.BUTTERFLY_EGG)
            {
                EggPOD.butterflySpawn = true;
            }

            System.out.println("egg type is " + eggType);
            collide = true;
        }
         */
    }

    public void setTag(int id)
    {

    }

    public int getTag()
    {
        return 1;
    }

    public boolean ifCollide()
    {
        return collide;
    }

    public int getEggType()
    {
        return eggType;
    }

    public void resetCollision()
    {
        collide = false;
    }

    public Vector2 getPosition()
    {
        return actor.getBody().getPosition();
    }

    public void display(float delta)
    {
        actor.display(delta);
    }

    public int getEggID()
    {
        return eggID;
    }

    public boolean ifDespawned()
    {
        return despawned;
    }

    public boolean ifDisplayed()
    {
        return actor.ifDisplayed();
    }

    public CreateTexture getFixture()
    {
        return actor;
    }

    public void setContainPortal(boolean val)
    {
        containPortal = val;
    }

    public boolean isContainPortal()
    {
        return containPortal;
    }
}
