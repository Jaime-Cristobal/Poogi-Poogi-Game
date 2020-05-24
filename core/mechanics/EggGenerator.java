package com.mygdx.core.mechanics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;
import com.mygdx.core.EggCount;
import com.mygdx.core.actor.Egg;
import com.mygdx.core.actor.ScrambledEgg;
import com.mygdx.core.actor.creation.CreateTexture;

public final class EggGenerator
{
    private final int CHICKEN_EGG = 1, BUTTERFLY_EGG = 2, EGG_PLAIN = 3, EGGHUNT_EGG1 = 4,
        EGGHUNT_EGG2 = 5, EGGHUNT_EGG3 = 6, EGGHUNT_EGG4 = 7;

    final private Array<Egg> eggs = new Array<Egg>();
    final private Array<Egg> cookedEgg = new Array<Egg>();
    final private Array<Integer> selector = new Array<Integer>();
    private boolean newMap = false, butterflySpawn = false;

    public EggGenerator(Core core, World world)
    {
        //10 eggs or less per map generated
        //10 for each egg except the egghunt 1-3 (5), egghunt4 (1), and butterfly(1)
        for(int n = 0; n < 5; n++) //10 total
        {
            eggs.add(new Egg("chicken_egg", CHICKEN_EGG, core, world, 31, 46, "chicken_egg" + n));
            eggs.add(new Egg("egg_plain", EGG_PLAIN, core, world, 27, 34, "egg_plain" + n));
        }
        for(int n = 0; n < 3; n++) //15 total
        {
            eggs.add(new Egg("egghunt_egg1", EGGHUNT_EGG1, core, world, 24, 19, "egghunt_egg1_" + n));
            eggs.add(new Egg("egghunt_egg2", EGGHUNT_EGG2, core, world, 24, 19, "egghunt_egg2_" + n));
            eggs.add(new Egg("egghunt_egg3", EGGHUNT_EGG3, core, world, 30, 24, "egghunt_egg3_" + n));
        }
        eggs.add(new Egg("egghunt_egg4", EGGHUNT_EGG4, core, world, 31, 28, "egghunt_egg4"));
        eggs.add(new Egg("butterfly_egg", BUTTERFLY_EGG, core, world, 21, 27, "butterfly_egg"));
        //10 + 9 + 1 + 1 = 21 total - 1 = 20 indeces

        //COOKED EGGS, 10 each
        for(int n = 0; n < 10; n++)
        {
            cookedEgg.add(new Egg("fried_egg", 0, core, world, 102, 47, "fried_egg" + n));
            cookedEgg.add(new Egg("green_eggs", 0, core, world, 47, 29, "egg_plain" + n));
        }

        //10 max randomly selected eggs on the map
        addRandomizeSelect();
    }

    public void addRandomizeSelect()
    {
        for(int n = 0; n < 10; n++)
            selector.add(MathUtils.random(0, 20));
    }

    public void randomizeSelect()
    {
        for(int n = 0; n < 10; n++)
            selector.insert(n, MathUtils.random(0, 20));
    }

    public void spawnEggs(int index, float x, float y, float minOffSetX, float maxOffSetX, float offSetY)
    {
        eggs.get(selector.get(index)).respawn(x + MathUtils.random(minOffSetX, maxOffSetX), y + offSetY);
    }

    public void despawn(int index)
    {
        int n = selector.get(index);
        eggs.get(n).despawn();
    }

    public void despawnAll(int amount)
    {
        for(int n = 0; n < amount; n++)
        {
            int i = selector.get(n);
            eggs.get(i).despawn();
        }
    }

    public void display(float delta, Object playerID, int amount)
    {
        for(int n = 0; n < amount; n++)
        {
            int i = selector.get(n);
            if(!eggs.get(i).ifDespawned())
            {
                eggs.get(i).update(playerID);
                eggs.get(i).display(delta);
            }
        }
    }
}
