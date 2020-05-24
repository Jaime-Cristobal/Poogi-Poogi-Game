package com.mygdx.core.mechanics.playermech;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.core.Core;
import com.mygdx.core.HUD;
import com.mygdx.core.actor.creation.CreateActor;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.mechanics.playermech.Consumable;

import java.util.HashSet;

public final class ConsumableManager
{
    private final HashSet<Integer> indeces;
    private final ArrayMap<String, Boolean> test = new ArrayMap<String, Boolean>();
    private final Array<CreateActor> items;
    final private Consumable healthPotion;

    public ConsumableManager(Core core, World world, Stage stage)
    {
        healthPotion = new Consumable("potion-red.png", core, stage);
        //healthPotion.setSize(45, 45);
        healthPotion.setSize(52, 52);
        healthPotion.setPosition(-164, -290, 10, 3);

        indeces = new HashSet<Integer>();

        items = new Array<CreateActor>();
        createHPItems(core, world);
    }

    private void createHPItems(Core core, World world)
    {
        //create test potion
        for(int n = 0; n < 3; n++)
        {
            items.add(new CreateTexture("potion-red.png", core, BodyDef.BodyType.DynamicBody));
            items.get(n).setData(0.9f, 0, true);
            items.get(n).setFilter(FilterID.potion_category, (short) (FilterID.floor_category | FilterID.player_category));
            items.get(n).setUniqueID("health" + n);
            items.get(n).create(world, MathUtils.random(10, 150), 200, 38, 38, false);
        }

        //create random indeces
        //for(int n = 0; n < items.size; n++)
            //MathUtils.rando
            //if(indeces.contains())
    }

    public void render(float delta, Object playerID)
    {
        //for(Integer itr : indeces)
        //    chooseIndex(itr, delta, playerID);


        for(CreateActor itr : items)
        {

            itr.display(delta);

            //add health potions
            if(EventManager.conditions(playerID, itr.getBody().getUserData()))
            {
                itr.setPosition(-100, -100);
                itr.setActive(false);

                healthPotion.addAmount(1, 5);
            }
        }
    }

    public void chooseIndex(int index, float delta, Object playerID)
    {
        items.get(index).display(delta);
        if(EventManager.conditions(playerID, items.get(index).getBody().getUserData()))
        {
            items.get(index).setPosition(-100, -100);
            items.get(index).setActive(false);

            healthPotion.addAmount(1, 5);
        }
    }

    public void updateStats(HUD hud)
    {
         if(healthPotion.getClick())
         {
             //hud.addActionPts(10);
             healthPotion.reset();
         }
    }
}
