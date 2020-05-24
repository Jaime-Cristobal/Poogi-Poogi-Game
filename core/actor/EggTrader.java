package com.mygdx.core.actor;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public final class EggTrader
{
    private CreateAnimation actor;

    public EggTrader(Core core, World world)
    {
        actor = new CreateAnimation("characters.atlas", core, BodyDef.BodyType.StaticBody);
        actor.addRegion("rook_head", 3f);
        actor.setData(0.9f, 0, true);
        actor.setFilter(FilterID.trader_category, FilterID.player_category);
        actor.setUniqueID("trader");
        actor.createOriginalCustom(world, 1100, 20, 50, 50, 108, 127, false);
    }

    public EggTrader(String texture, Core core, World world, float x, float y, float boxW, float boxH, float width, float height)
    {
        actor = new CreateAnimation("characters.atlas", core, BodyDef.BodyType.StaticBody);
        actor.addRegion(texture, 3f);
        actor.setData(0.9f, 0, true);
        actor.setFilter(FilterID.trader_category, FilterID.player_category);
        actor.setUniqueID("trader");
        actor.createOriginalCustom(world, x, y, boxW, boxH, width, height, false);
    }

    public void setPosition(float x, float y)
    {
        actor.setPosition(x, y);
    }

    public void spawn(float x, float y)
    {
        actor.setActive(true);
        actor.setDisplay(true);
        actor.setPosition(x, y);
    }

    public void despawn()
    {
        actor.setPosition(-700, -700);
        actor.setDisplay(false);
        actor.setActive(false);
    }

    public int update(Object playerID)
    {
        if(EventManager.conditions(actor.getBody().getUserData(), playerID))
        {
            return 1;
        }

        return 0;
    }

    public void display(float delta)
    {
        actor.displayCustom(delta, 0, 20);
    }

    public void display(float delta, float offX, float offY)
    {
        actor.displayCustom(delta, offX, offY);
    }

    public boolean playerInRange(float x, float y)
    {
        if((x > actor.getX() - 10 && x < actor.getX() + 10) &&
                (y > actor.getY() - 10 && y < actor.getY() + 10))
        {
            return true;
        }

        return false;
    }
}
