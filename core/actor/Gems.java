package com.mygdx.core.actor;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.Inventory;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public class Gems
{
    private CreateTexture gem;
    private boolean hasSpawned = false;
    private float side = -1;
    private int ID = 0;

    public Gems(String atlas, String texture, Core core, World world, float width, float height,
                Object userData, BodyDef.BodyType type)
    {
        gem = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core, type);
        gem.setData(0.1f, 0, true);
        gem.setFilter(FilterID.egg_category, (short)(FilterID.player_category | FilterID.mine_catageory | FilterID.floor_category |
                FilterID.platform_category));
        gem.setUniqueID(userData);
        gem.create(world, -700, -700, width, height, false);
        gem.setDisplay(false);
        gem.setActive(false);
    }

    public Gems(String atlas, String texture, Core core, World world, float width, float height,
                Object userData, BodyDef.BodyType type, int gemID)
    {
        gem = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core, type);
        gem.setData(0.1f, 0, true);
        gem.setFilter(FilterID.item_category, (short)(FilterID.player_category | FilterID.mine_catageory | FilterID.floor_category |
                FilterID.platform_category));
        gem.setUniqueID(userData);
        gem.create(world, -700, -700, width, height, false);
        gem.setDisplay(false);
        gem.setActive(false);

        ID = gemID;
    }

    public void spawn(float x, float y, float rad)
    {
        gem.getBody().setAngularVelocity(rad);
        gem.setActive(true);
        gem.setDisplay(true);
        gem.setPosition(x, y);
        hasSpawned = true;
    }

    public void despawn()
    {
        gem.setPosition(-700, -700);
        gem.getBody().setAngularVelocity(0);
        gem.getBody().setTransform(gem.getBody().getPosition(), 0);
        gem.setDisplay(false);
        gem.setActive(false);
    }

    public int update(Object playerID)
    {
        if(EventManager.conditions(gem.getBody().getUserData(), playerID))
        {
            despawn();
            return 1;
        }

        return 0;
    }

    public void display(float delta)
    {
        gem.display(delta);
    }

    public boolean isActive()
    {
        return gem.isActive() || gem.ifDisplayed();
    }

    public int getGemID()
    {
        return ID;
    }

    public boolean isSpawned()
    {
        return hasSpawned;
    }
}
