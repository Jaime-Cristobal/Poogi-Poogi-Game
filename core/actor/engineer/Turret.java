package com.mygdx.core.actor.engineer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.core.Core;
import com.mygdx.core.actor.Projectile;
import com.mygdx.core.actor.creation.CreateAnimation;

public final class Turret
{
    private Projectile actor = null;
    private Array<Projectile> ammo = new Array<Projectile>();
    private ArrayMap<String, Float> regionSet = new ArrayMap<String, Float>();
    private short id1 = 0, id2 = 0;

    public Turret(String animatedRegion, float frameSpeed)
    {
        regionSet.put(animatedRegion, frameSpeed);
    }

    public Turret(ArrayMap<String, Float> regionSet)
    {
        this.regionSet = regionSet;
    }

    public void setAmmo()
    {

    }

    public void setFilter(short id1, short id2)
    {
        this.id1 = id1;
        this.id2 = id2;
    }

    public void create(String file, Core core, World world, boolean gravity, Object ID)
    {
        actor = new Projectile(new CreateAnimation(file, core, BodyDef.BodyType.DynamicBody), regionSet);
        actor.setData(0.9f, 0.1f, true);
        actor.setFilter(id1, id2);
        actor.create(world, 64, 65, ID);
        if(!gravity)
            actor.getBody().setGravityScale(0);
    }

    public void setSpawn(Vector2 playerPos, float xOffSet, float yOffSet)
    {
        actor.setPosition(playerPos.x + xOffSet, playerPos.y + yOffSet);
    }

    public void render(float delta)
    {
        actor.render(delta);
    }

    public Vector2 getPos()
    {
        return actor.getBody().getPosition();
    }

    public void detection(Vector2 position, float xOffset, float yOffset)
    {
        if((position.x > actor.getBody().getPosition().x + xOffset || position.x < actor.getBody().getPosition().x - xOffset)
                && (position.y > actor.getBody().getPosition().y + yOffset || position.y < actor.getBody().getPosition().y - yOffset))
        {
            //fire ammo at the "position"'s direction here
        }
    }
}
