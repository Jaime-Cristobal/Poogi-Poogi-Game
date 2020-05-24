package com.mygdx.core.actor.engineer;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;
import com.mygdx.core.mechanics.playermech.PlayerMech;

public class Laser implements PlayerMech
{
    private Array<Mine> projectile = new Array<Mine>();
    private int indexAllow = -1;

    public Laser(String texture, Core core, World world, int amount, boolean forPlayer)
    {
        for(int n = 0; n < amount; n++)
        {
            projectile.add(new Mine());
            projectile.get(n).setDensity(0.1f);

            if(forPlayer)
                projectile.get(n).setFilter(FilterID.collector_category, (short) (FilterID.floor_category
                        | FilterID.enemy_category | FilterID.hunter_category | FilterID.firing_category | FilterID.potion_category
                        | FilterID.egg_category));
            else
                projectile.get(n).setFilter(FilterID.collector_category, (short) (FilterID.player_category));

            projectile.get(n).create(new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", texture),
                            core, BodyDef.BodyType.DynamicBody),
                    world, true, "laser" + n, 25, 25);
            projectile.get(n).noGravity();
            projectile.get(n).setLimitAtOrigin();
            projectile.get(n).setExplosion(core, "effects.atlas", "explosionG");

            if(forPlayer)
                ObjectID.projectileList.put(projectile.get(n).getID(), false);
            else
                ObjectID.enemyProjectile.put(projectile.get(n).getID(), false);
        }
    }

    public void setLimitFromOrigin(int val)
    {
        for(int n = 0; n < projectile.size; n++)
            projectile.get(n).setOriginLimit(val);
    }

    public void setSpawn(float x, float y, int index, float direction)
    {
        indexAllow++;
        projectile.get(index).setSpawn(x, y, 0, 0, 55 * direction * -1, 0);
    }

    public void randomDirectSpawn()
    {

    }

    public void update()
    {
        if(indexAllow >= 0)
        {
            for (int n = 0; n < projectile.size; n++)
            {
                projectile.get(n).explode();
            }
        }
    }

    public void render(Core core, float delta)
    {
        if(indexAllow >= 0)
        {
            for (int n = 0; n < projectile.size; n++)
                if(projectile.get(n).render(core, delta))
                    indexAllow--;
        }
    }

    public int getSize()
    {
        return projectile.size;
    }

    public int getState()
    {
        return -1;
    }
}
