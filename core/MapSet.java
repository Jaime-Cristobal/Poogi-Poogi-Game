package com.mygdx.core;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Stages.FirstStage;
import com.mygdx.core.actor.creation.MapGenerator;


/**
 * @see FirstStage and call the functions on the same functions with
 *      the same name. Ex: show() at show, render() at render, etc...
 * */

public final class MapSet
{
    private final Core core;
    private final int set;
    private final MapGenerator generator;

    /**@param set is the pre-set settings chosen for the current game.
     *            Pre-set settings include things such as characters,
     *            textures, and color settings.
     * */
    public MapSet(Core core, World world, BodyEditorLoader loader, int set)
    {
        this.core = core;
        this.set = set;
        generator = new MapGenerator(core, world, loader);
        generator.createPlatform1();
    }

    /**@see FirstStage show()*/
    public void show()
    {

    }

    /**
     * Renders objects behind the player.
     * Should be placed between a batch.begin and batch.end or Gdx will assert.
     * @see FirstStage render() for more info.
     * */
    public void renderBehind(float delta, Object playerID)
    {
        //core.batch.draw(core.assetmanager.getTexture("map5.png"), -400, -425, 2048, 2048);
        generator.displayAll(delta, playerID);
    }

    /**
     * Renders objects in front of the player.
     * Should be placed between a batch.begin and batch.end or Gdx will assert.
     * @see FirstStage render() for more info.
     * */
    public void renderFront()
    {

    }

    public boolean isGenerate()
    {
        return generator.isGenerate();
    }
}
