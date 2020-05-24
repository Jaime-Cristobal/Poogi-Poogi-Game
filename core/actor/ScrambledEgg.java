package com.mygdx.core.actor;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public class ScrambledEgg
{
    final private CreateTexture actor;

    public ScrambledEgg(String file, Core core, World world, float w, float h, Object ID)
    {
        actor = new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", file), core,
                BodyDef.BodyType.StaticBody);
        actor.setData(0.9f, 0, true);
        actor.setFilter(FilterID.floor_category, (short) (FilterID.egg_category | FilterID.player_category |
                FilterID.floor_category | FilterID.mine_catageory));
        actor.setUniqueID(ID);
        //temp.setOffSet(xSetOff, ySetOff);
        actor.create(world, -200, -200, w, h, false);
        actor.setActive(false);
    }

    public void respawn(float x, float y)
    {
        actor.setPosition(x, y);
        actor.setActive(true);
    }

    public void despawn()
    {
        actor.setPosition(-200, -200);
        actor.setActive(false);
    }

    public boolean ifCollide(Object id)
    {
        return EventManager.conditions(actor.getBody().getUserData(), id);
    }

    public boolean despawnIfCollide(Object id)
    {
        actor.setPosition(-200, -200);
        actor.setActive(false);
        return EventManager.conditions(actor.getBody().getUserData(), id);
    }

    public void setCollision(Object id)
    {
        if(EventManager.conditions(actor.getBody().getUserData(), id))
            despawn();
    }

    public void display()
    {
        actor.display();
    }
}
