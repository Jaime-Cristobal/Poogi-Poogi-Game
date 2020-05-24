package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;

public final class TikiBombManager
{
    private Array<TikiBomb> tikiBombs = new Array<TikiBomb>();
    private boolean displayOn = true;

    public TikiBombManager(Core core, World world, int amount)
    {
        for(int n = 0; n < amount; n++)
        {
            tikiBombs.add(new TikiBomb("1st stage/firstStage.atlas", "lotusTiki", 2f, core,
                    world, 30, 30, 49, 78, "tikibomb" + n));
        }
    }

    public void spawn(float x, float y, int index)
    {
        tikiBombs.get(index).spawn(x, y);
        displayOn = true;
    }

    public void despawn(int index)
    {
        tikiBombs.get(index).despawn();
    }

    public void despawnAll()
    {
        for(int n = 0; n < tikiBombs.size; n++)
            tikiBombs.get(n).despawn();
        displayOn = false;
    }

    public int display(Batch batch, float delta, float playerX, float playerY,
                       boolean doorCollide, boolean portalCollide)
    {
        for(int n = 0; n < tikiBombs.size; n++)
        {
            if(!doorCollide && !portalCollide)
            {
                switch (tikiBombs.get(n).update(delta, playerX, playerY)) {
                    case 0:
                        break;
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                }
            }

            tikiBombs.get(n).display(batch, delta);
        }

        return 0;
    }

    public int getSize()
    {
        return tikiBombs.size;
    }
}
