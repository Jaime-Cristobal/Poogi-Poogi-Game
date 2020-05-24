package com.mygdx.core.actor.creation;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;
import com.mygdx.core.EggCount;
import com.mygdx.core.EggPOD;
import com.mygdx.core.actor.Gems;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public class Land implements RenderObject
{
    final private CreateTexture fixture;
    final private Array<Gems> gems = new Array<Gems>();
    private boolean containPortal = false, spawnGem = false;
    private int gemIndex = -1;

    public Land(Core core, World world, String atlas, String texture, float x, float y,
                                   float w, float h, float renderW, float renderH, Object ID)
    {
        if(core.assetmanager.getTextureFromAtlas(atlas, texture) == null)
            System.out.println(atlas + " " + texture + " is missing");
        fixture = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core,
                BodyDef.BodyType.StaticBody);
        fixture.setData(0.9f, 0, true);
        fixture.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                FilterID.potion_category | FilterID.mine_catageory | FilterID.egg_category | FilterID.door_category |
                FilterID.boss_category | FilterID.item_category | FilterID.collector_category | FilterID.tree_category));
        fixture.setUniqueID(ID);
        //temp.setOffSet(xSetOff, ySetOff);
        fixture.createOriginalCustom(world, x, y, w, h, renderW, renderH, false);
        fixture.setActive(false);


        gems.add(new Gems("assets.atlas", "ruby", core, world, 24, 27,
                "ruby" + ID, BodyDef.BodyType.KinematicBody, 1));
        gems.add(new Gems("assets.atlas", "saphire", core, world, 31, 20,
                "saphire" + ID, BodyDef.BodyType.KinematicBody, 2));
        gems.add(new Gems("assets.atlas", "diamond", core, world, 23, 26,
                "diamond" + ID, BodyDef.BodyType.KinematicBody, 3));
        gems.add(new Gems("assets.atlas", "moonstone", core, world, 44, 41,
                "moonstone" + ID, BodyDef.BodyType.KinematicBody, 4));
        //return fixture;
    }

    public void update(Object playerID)
    {
        if(spawnGem)
        {
            if(gems.get(gemIndex).update(playerID) == 1)
            {
                switch (gems.get(gemIndex).getGemID())
                {
                    case 1:
                        if(EggPOD.doubleOn)
                            EggCount.addEgg(20);
                        else
                            EggCount.addEgg(10);
                        break;
                    case 2:
                        if(EggPOD.doubleOn)
                            EggCount.addEgg(10);
                        else
                            EggCount.addEgg(5);
                        break;
                    case 3:
                        if(EggPOD.doubleOn)
                            EggCount.addEgg(40);
                        else
                            EggCount.addEgg(20);
                        break;
                    case 4:
                        if(EggPOD.doubleOn)
                            EggCount.addEgg(100);
                        else
                            EggCount.addEgg(50);
                        break;
                }
                gems.get(gemIndex).despawn();
                gemIndex = -1;
                spawnGem = false;
            }
        }
    }

    public boolean updateGem(Object playerID)
    {
        if(spawnGem)
        {
            if(gems.get(gemIndex).update(playerID) == 1)
            {
                switch (gems.get(gemIndex).getGemID())
                {
                    case 1:
                        if(EggPOD.doubleOn)
                            EggCount.addEgg(20);
                        else
                            EggCount.addEgg(10);
                        break;
                    case 2:
                        if(EggPOD.doubleOn)
                            EggCount.addEgg(10);
                        else
                            EggCount.addEgg(5);
                        break;
                    case 3:
                        if(EggPOD.doubleOn)
                            EggCount.addEgg(40);
                        else
                            EggCount.addEgg(20);
                        break;
                    case 4:
                        if(EggPOD.doubleOn)
                            EggCount.addEgg(100);
                        else
                            EggCount.addEgg(50);
                        break;
                }
                gems.get(gemIndex).despawn();
                gemIndex = -1;
                spawnGem = false;

                return true;
            }
        }

        return false;
    }

    public void respawn(float x, float y)
    {
        fixture.setActive(true);
        fixture.setDisplay(true);
        fixture.setPosition(x, y);
    }

    public void despawn()
    {
        fixture.setPosition(-700, -700);
        fixture.setActive(false);
        fixture.setDisplay(false);
    }

    public void setTag(int id)
    {

    }

    public void setContainPortal(boolean val)
    {
        containPortal = val;
    }

    public boolean isContainPortal()
    {
        return containPortal;
    }

    public int getTag()
    {
        return 1;
    }


    public void display(float delta)
    {
        if(fixture.ifDisplayed())
            fixture.display();
        else if(spawnGem)
            gems.get(gemIndex).display(delta);
    }

    public void setDisplay(boolean val)
    {
        fixture.setDisplay(val);
    }

    public boolean ifDisplayed()
    {
        return fixture.ifDisplayed() || spawnGem;
    }

    public CreateTexture getFixture()
    {
        return fixture;
    }

    public boolean ifCollide(Object playerID)
    {
        return EventManager.conditions(fixture.getBody().getUserData(), playerID);
    }

    public void spawnGems()
    {
        spawnGem = true;
        int gemChance = MathUtils.random(0, 20);
        if(gemChance >= 0 && gemChance <= 10)
            gemIndex = 1;
        if(gemChance >= 11 && gemChance <= 17)
            gemIndex = 0;
        if(gemChance == 18 || gemChance == 19)
            gemIndex = 2;
        if(gemChance == 20)
            gemIndex = 3;
        gems.get(gemIndex).spawn(fixture.getX(), fixture.getY(), 0.5f);
    }

    public void despawnGems()
    {
        spawnGem = false;
        if(gemIndex >= 0)
            gems.get(gemIndex).despawn();
    }
}
