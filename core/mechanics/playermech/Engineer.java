package com.mygdx.core.mechanics.playermech;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.actor.engineer.DemonMine;
import com.mygdx.core.actor.engineer.Mine;
import com.mygdx.core.actor.engineer.PumpkinMine;
import com.mygdx.core.animator.Animator;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;

/**
 * Handles the engineer mechanics of spawning machines, turrets,
 * and other helpful objects for the players.*/
public class Engineer implements PlayerMech
{
    private Array<Mine> mines = new Array<Mine>();
    private Array<DemonMine> demonMines = new Array<DemonMine>();
    private Array<PumpkinMine> pumpkinMines = new Array<PumpkinMine>();
    private Array<Object> minesID = new Array<Object>();
    private int indexAllow = -1;
    private int state = 0;

    public Engineer(Core core, World world)
    {
        //ORDINARY MINES
        for(int n = 0; n < 3; n++)
        {
            mines.add(new Mine());
            mines.get(n).setFilter(FilterID.mine_catageory, (short) (FilterID.floor_category
                                   | FilterID.enemy_category | FilterID.hunter_category | FilterID.firing_category
                                   | FilterID.rubble_category | FilterID.potion_category));
            mines.get(n).create(new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", "bomb"),
                            core, BodyDef.BodyType.DynamicBody),
                    world, true, "mine" + n, 25, 25);
            mines.get(n).setExplosion(core, "effects.atlas", "explosionH");

            minesID.add(mines.get(n).getID());

            ObjectID.mineList.put(mines.get(n).getID(), false);
        }

        //DEMON MINES
        for(int n = 0; n < 3; n++)
        {
            demonMines.add(new DemonMine());
            demonMines.get(n).setFilter(FilterID.mine_catageory, (short) (FilterID.floor_category
                    | FilterID.enemy_category | FilterID.hunter_category | FilterID.firing_category
                    | FilterID.rubble_category | FilterID.potion_category));
            demonMines.get(n).create(new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", "pumpkinDemon"),
                            core, BodyDef.BodyType.DynamicBody),
                    world, true, "pumpkinDemon" + n, 50, 47);
            demonMines.get(n).setExplosion(core, "effects.atlas", "explosionH", 256, 256);

            demonMines.get(n).setRange(world, 100, 40, "range" + n);

            minesID.add(demonMines.get(n).getID());

            ObjectID.mineList.put(demonMines.get(n).getID(), false);
        }

        //CAT MINES
        for(int n = 0; n < 3; n++)
        {
            pumpkinMines.add(new PumpkinMine());
            pumpkinMines.get(n).setFilter(FilterID.mine_catageory, (short) (FilterID.floor_category
                    | FilterID.enemy_category | FilterID.hunter_category | FilterID.firing_category
                    | FilterID.rubble_category | FilterID.potion_category));
            pumpkinMines.get(n).create(new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", "pumpkinCat"),
                            core, BodyDef.BodyType.DynamicBody),
                    world, true, "pumpkinCat" + n, 50, 47);
            pumpkinMines.get(n).setExplosion(core, "effects.atlas", "explosion4Half", 256, 256);
            pumpkinMines.get(n).createMiniMine("assets.atlas", "bomb", core, world, 25, 25);

            minesID.add(pumpkinMines.get(n).getID());

            ObjectID.mineList.put(pumpkinMines.get(n).getID(), false);
        }
    }

    public void setSpawn(float x, float y, int index, float direction)
    {
        switch (state)
        {
            case 0:
                if(index < mines.size)
                {
                    indexAllow++;
                    mines.get(index).setSpawn(x, y, 0, 5);
                }
                break;
            case 1:
                if(index < demonMines.size)
                {
                    indexAllow++;
                    demonMines.get(index).setSpawn(x, y, 0, 5);
                }
                break;
            case 2:
                if(index < mines.size && !pumpkinMines.get(index).isExplodeMiniFinished())
                {
                    indexAllow++;
                    pumpkinMines.get(index).setSpawn(x, y, 0, 5);
                }
                break;
        }
    }

    public void update()
    {
        switch (state)
        {
            case 0:     //ordinary mines
                if(indexAllow >= 0)
                {
                    for (int n = 0; n < mines.size; n++)
                        mines.get(n).explode();
                }
                break;
            case 1:     //demon mines
                if(indexAllow >= 0)
                {
                    for (int n = 0; n < demonMines.size; n++)
                        demonMines.get(n).explode();
                }
                break;
            case 2:     //cat mines
                if(indexAllow >= 0)
                {
                    for (int n = 0; n < pumpkinMines.size; n++)
                        pumpkinMines.get(n).explode();
                }
                break;
        }
    }

    public void render(Core core, float delta)
    {
        switch (state)
        {
            case 0:
                if(indexAllow >= 0)
                {
                    for (int n = 0; n < mines.size; n++)
                        if(mines.get(n).render(core, delta))
                            indexAllow--;
                }
                break;
            case 1:
                if(indexAllow >= 0)
                {
                    for (int n = 0; n < demonMines.size; n++)
                        if(demonMines.get(n).render(core, delta))
                            indexAllow--;
                }
                break;
            case 2:
                if(indexAllow >= 0)
                {
                    for (int n = 0; n < pumpkinMines.size; n++)
                        if(pumpkinMines.get(n).render(core, delta))
                            indexAllow--;
                }
                break;
        }
    }

    public void setState(int val)
    {
        indexAllow = -1;
        state = val;
    }

    public int getState()
    {
        return state;
    }

    public int getSize()
    {
        return mines.size;
    }

    public Array<Object> getMinesID()
    {
        return minesID;
    }

    public Object getMineID()
    {
        return mines.get(0).getID();
    }
}
