package com.mygdx.core.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public final class Clouds
{
    final private CreateTexture cloud;
    private float minX = 0, maxX = 0, minY = 0, maxY = 0;

    public Clouds(String atlas, String texture, Core core, World world, float x, float y, float width, float height)
    {
        cloud = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture),
                core, BodyDef.BodyType.DynamicBody);
        cloud.setData(0.9f, 0, true);
        cloud.setFilter(FilterID.potion_category, FilterID.player_category);
        cloud.create(world, x, y, width, height, false);
        cloud.setActive(false);
        cloud.setDisplay(false);
    }

    public void spawn(float x, float y)
    {
        cloud.setDisplay(true);
        cloud.setActive(true);
        cloud.setPosition(x, y);
        cloud.setSpeed(10, 0);
    }

    public void spawnRandom(float minX, float maxX, float minY, float maxY)
    {
        this.minX = minX; this.maxX = maxX; this.minY = minY; this.maxY = maxY;
        cloud.setDisplay(true);
        cloud.setActive(true);
        cloud.setPosition(MathUtils.random(this.minX, this.maxX), MathUtils.random(this.minY, this.maxY));
        cloud.setSpeed(10, 0);
    }

    public void despawn()
    {
        cloud.setDisplay(false);
        cloud.setActive(false);
        cloud.setPosition(-700, -700);
    }

    /**If cloud update returns true, just have the player hide either behind or have
     * the player position somewhere else and have the camera focus on the cloud.*/
    public boolean update(Object playerID)
    {
        if(cloud.isActive())
        {
            if (cloud.getX() > 150)
                cloud.setPosition(MathUtils.random(minX, maxX), MathUtils.random(minY, maxY));

            if (EventManager.conditions(cloud.getBody().getUserData(), playerID))
                return true;
        }

        return false;
    }

    public void display()
    {
        if(cloud.ifDisplayed())
            cloud.display();
    }
}
