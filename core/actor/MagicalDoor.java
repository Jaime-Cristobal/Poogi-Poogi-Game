package com.mygdx.core.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public final class MagicalDoor
{
    final private CreateAnimation actor;
    final private ArrayMap<String, Float> region = new ArrayMap<String, Float>();

    public MagicalDoor(Core core, World world, Object ID)
    {
        region.put("door_open", 3f);
        region.put("door_flash", 2f);

        actor = new CreateAnimation("passages.atlas", region, core, BodyDef.BodyType.StaticBody);
        actor.setData(0.9f, 0, true);
        actor.setFilter(FilterID.door_category, (short) (FilterID.egg_category | FilterID.player_category |
                FilterID.floor_category | FilterID.mine_catageory));
        actor.setUniqueID(ID);
        //temp.setOffSet(xSetOff, ySetOff);
        actor.createOriginalCustom(world, -700, -700, 50, 57, 98, 147, false);
        actor.setRegion("door_flash");
        actor.setActive(false);
    }

    public void setFlash()
    {
        actor.setLoop(true);
        actor.setRegion("door_flash");
    }

    public void setOpen()
    {
        actor.resetAnimation();
        actor.setLoop(false);
        actor.setRegion("door_open");
    }

    public String getRegion()
    {
        return actor.getRegion();
    }

    public boolean ifAnimFinished()
    {
        return actor.isAnimationFinished();
    }

    public void resetAnimation()
    {
        actor.resetAnimation();
    }

    public void respawn(float x, float y)
    {
        actor.setPosition(x, y);
        actor.setActive(true);
    }

    public void despawn()
    {
        actor.setPosition(-700, -700);
        actor.setActive(false);
    }

    public boolean ifCollide(Object id)
    {
        return EventManager.conditions(actor.getBody().getUserData(), id);
    }

    public boolean despawnIfCollide(Object id)
    {
        if(EventManager.conditions(actor.getBody().getUserData(), id))
        {
            actor.setPosition(-200, -200);
            actor.setActive(false);
            return true;
        }
        return false;
    }

    public void setCollision(Object id)
    {
        if(EventManager.conditions(actor.getBody().getUserData(), id))
            despawn();
    }

    public void display(float delta)
    {
        actor.displayCustom(delta, 0, -25);
    }

    public Vector2 getPos()
    {
        return actor.getBody().getPosition();
    }
}
