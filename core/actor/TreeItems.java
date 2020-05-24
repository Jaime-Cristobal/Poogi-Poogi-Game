package com.mygdx.core.actor;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.Inventory;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public final class TreeItems
{
    final private CreateTexture items;
    private boolean stickOnPlayer = false;
    private float side = -1;

    public TreeItems(String atlas, String texture, Core core, World world, Object userData, BodyDef.BodyType type)
    {
        items = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core, type);
        items.setData(0.1f, 0, true);
        items.setFilter(FilterID.tree_category, (short)(FilterID.player_category | FilterID.mine_catageory | FilterID.floor_category |
                                                        FilterID.platform_category));
        items.setUniqueID(userData);
        items.create(world, -700, -700, 55, 45, false);
        items.setDisplay(false);
        items.setActive(false);
    }

    public void spawn(float x, float y)
    {
        items.setActive(true);
        items.setDisplay(true);
        items.setPosition(x, y);
    }

    public void despawn()
    {
        items.setPosition(-700, -700);
        items.setDisplay(false);
        items.setActive(false);
    }

    public int update(Object playerID, float playerX, float playerY, float direction)
    {
        if(EventManager.conditions(items.getBody().getUserData(), playerID) && !Inventory.water)
        {
            despawn();
            Inventory.water = true;
            //stickOnPlayer = true;

            return 1;
        }
        if(!Inventory.water)
            stickOnPlayer = false;

        if(stickOnPlayer)
        {
            items.setPosition(playerX, playerY - 3);
            if(direction != side)
            {
                side = direction;
                items.flip();
            }
        }

        if(items.getY() <= -50)
            despawn();

        return 0;
    }

    public void display(float delta)
    {
        items.display(delta);
    }

    public boolean isActive()
    {
        return items.isActive() && items.ifDisplayed();
    }

    public void setRotation(float rad)
    {
        items.getBody().setAngularVelocity(rad);
    }

    public void stopRotation()
    {
        items.getBody().setAngularVelocity(0);
        items.getBody().setTransform(items.getBody().getPosition(), 0);
    }
}
