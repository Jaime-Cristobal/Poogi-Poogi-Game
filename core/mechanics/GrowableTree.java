package com.mygdx.core.mechanics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.core.Core;
import com.mygdx.core.Inventory;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.actor.creation.Land;
import com.mygdx.core.animator.Animator;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;

public final class GrowableTree
{
    private CreateTexture tree;
    private CreateTexture land;
    private Animator fx;
    private int state = 1;
    final private String currentText, currentAtlas;
    private boolean fxOn = false, gameLost = false, nextMap = false, fullGrown = false;

    public GrowableTree(String atlasFile, String texture, Core core, World world)
    {
        currentAtlas = atlasFile;
        currentText = texture;

        tree = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlasFile, currentText + state),
                core, BodyDef.BodyType.StaticBody);
        tree.setData(0.9f, 0, true);
        tree.setFilter(FilterID.tree_category, FilterID.player_category);
        tree.setUniqueID("tree1");
        tree.createOriginalCustom(world, -700, -700, 10, 45, 203, 187, false);
        tree.setDisplay(false);
        tree.setActive(false);

        //addState();

        fx = new Animator("assets.atlas", core);
        fx.addRegion("irrigator", 2f);
        fx.setScale(47, 38);
    }

    public void createLand(Core core, World world, String atlas, String texture, float width, float height,
                           float renderW, float renderH)
    {
        land = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core,
                BodyDef.BodyType.StaticBody);
        land.setData(0.9f, 0, true);
        land.setUniqueID("Treeland");
        land.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                FilterID.potion_category | FilterID.mine_catageory | FilterID.egg_category | FilterID.door_category));
        land.createOriginalCustom(world, -700, -700, width, height, renderW, renderH, false);
        land.setActive(false);
        land.setDisplay(false);
    }

    public void spawn()
    {
        fullGrown = false;
        tree.setDisplay(true);
        tree.setActive(true);
        tree.setPosition(10, 0);

        land.setDisplay(true);
        land.setActive(true);
        land.setPosition(tree.getX(), tree.getY() - 3);
    }

    public void despawn()
    {
        fullGrown = false;
        tree.setPosition(-700, -700);
        tree.setDisplay(false);
        tree.setActive(false);

        land.setPosition(-700, -700);
        land.setDisplay(false);
        land.setActive(false);
    }

    public int update(Object playerID, int maxState)
    {
        if(state >= maxState)
        {
            fullGrown = true;
        }

        //if collide with player
        //if inventory.something == true
        //index to next stage of the tree, increment state, and return true for watering animation in render
        if(EventManager.conditions(tree.getBody().getUserData(), playerID))
        {
            if(Inventory.water)
            {
                if (state < maxState)
                {
                    Inventory.water = false;
                    addState(maxState);
                    if (!fxOn)
                        fxOn = true;
                }
                setRegion(currentAtlas, currentText + state);
                return 1;
            }
        }

        return 0;
    }

    private void addState(int maxState)
    {
        if(state >= maxState)
        {
            if(!fullGrown)
                fullGrown = true;
        }
        else
        {
            state++;
        }
    }

    private void removeState()
    {
        if(state - 1 <= 0)
            gameLost = true;
        else if(state > 0)
            state--;
    }

    public void revertState()
    {
        if(state - 1 <= 0)
            gameLost = true;
        else if(state > 1)
        {
            state--;
            setRegion(currentAtlas, currentText + state);
        }
    }

    public void advanceState(int maxState)
    {
        if(state >= maxState)
        {
            if(!fullGrown)
                fullGrown = true;

        }
        else
        {
            state++;
            setRegion(currentAtlas, currentText + state);

        }
    }

    public void display(Batch batch, float delta)
    {
        //tree.display(delta);
        land.display(delta);
        tree.displayCustom(delta, 0, 65);
        if(fxOn)
        {
            if(!fx.ifFinished())
                fx.render(batch, tree.getX(), tree.getY(), delta);
            else
            {
                fxOn = false;
                fx.resetTime();
            }
        }
    }

    public boolean inArea(float playerX, float playerY)
    {
        if(playerX > tree.getX() - 3 && playerX < tree.getX() + 3
            && playerY > tree.getY() - 3 && playerY < tree.getY() + 6)
            return true;

        return false;
    }

    public void setRegion(String atlas, String texture)
    {
        tree.setNewTexture(atlas, texture);
    }

    public int getState()
    {
        return state;
    }

    public boolean isGameLost()
    {
        return gameLost;
    }

    public boolean isFullGrown()
    {
        return fullGrown;
    }
}
