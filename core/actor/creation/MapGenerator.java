package com.mygdx.core.actor.creation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.core.BodyEditorLoader;
import com.mygdx.core.Core;
import com.mygdx.core.actor.MagicalDoor;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.mechanics.EggGenerator;

import java.util.HashSet;
import java.util.Random;

public class MapGenerator
{
    final private Core core;
    final private World world;
    final private BodyEditorLoader loader;
    private Array<CreateTexture> land = new Array<CreateTexture>();
    private ArrayMap<TextureRegion, Vector2> decorations = new ArrayMap<TextureRegion, Vector2>();
    final private EggGenerator eggGen;
    final private MagicalDoor door;
    final private CreateTexture doorLand;
    private int amountOfEggs = 0;
    private ArrayMap<String, Dimensions> landList = new ArrayMap<String, Dimensions>();
    private Array<Integer> selector = new Array<Integer>();

    private int amountOfLand = 0;
    private boolean generate = false;

    /**Used to determine the dimension when randomly generating land on the screen.*/
    private class Dimensions
    {
        final public float boxW, boxH, renderW, renderH, xOffSet, yOffSet;
        public Dimensions(float boxW, float boxH, float renderW, float renderH,
                          float xOffSet, float yOffSet)
        {
            this.boxW = boxW; this.boxH = boxH;
            this.renderW = renderW; this.renderH = renderH;
            this.xOffSet = xOffSet; this.yOffSet = yOffSet;
        }
    }

    public MapGenerator(Core core, World world, BodyEditorLoader loader)
    {
        this.core = core;
        this.world = world;
        this.loader = loader;
        eggGen = new EggGenerator(core, world);
        door = new MagicalDoor(core, world, "MagicalDoor1");
        //generateLandList();
        generateMaps();

        doorLand = new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", "grass_platform2"), core,
                BodyDef.BodyType.StaticBody);
        doorLand.setData(0.9f, 0, true);
        doorLand.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                FilterID.potion_category | FilterID.mine_catageory | FilterID.egg_category | FilterID.door_category));;
        doorLand.createOriginalCustom(world, -300, -300,100, 4, 125, 75, false);
        doorLand.setActive(false);
    }

    private void generateLandList()
    {
        landList.put("platform1", new Dimensions(100, 8, 125, 35, 10, 50));
        landList.put("platform2", new Dimensions(100, 8, 109, 50, 10, 50));
        landList.put("platform3", new Dimensions(100, 8, 117, 45, 10, 50));
        landList.put("platform4", new Dimensions(100, 8, 117, 45, 10, 50));
        landList.put("platform5", new Dimensions(100, 8, 117, 45, 10, 50));
        landList.put("grass_platform1", new Dimensions(100, 8, 117, 45, 10, 50));
        landList.put("grass_platform2", new Dimensions(100, 8, 117, 45, 10, 50));
        landList.put("grass_platform3", new Dimensions(100, 8, 117, 60, 10, 50));
        //landList.put("grass_platform4", new Dimensions(100, 4, 125, 145, 10, 20));
        //landList.put("grass_platform5", new Dimensions(100, 4, 125, 145, 10, 20));
        //landList.put("flying_platform_2", new Dimensions(100, 8, 117, 45, 10, 50));
    }

    private void generateMaps()
    {
        for(int n = 0; n < 3; n++)
        {
            createLand("assets.atlas", "platform1", -300, -300,
                    240, 30, 255, 95,
                    10, 50);
            createLand("assets.atlas", "platform2", -300, -300,
                            100, 8, 109, 50,
                            10, 50);
            createLand("assets.atlas", "platform3", -300, -300,
                    100, 8, 117, 45,
                    10, 50);
            createLand("assets.atlas", "platform4", -300, -300,
                    100, 8, 117, 45,
                    10, 50);
            createLand("assets.atlas", "platform5", -300, -300,
                    100, 8, 117, 45,
                    10, 50);
        }

        for(int n = 0; n < 9; n++)
        {
            int t = MathUtils.random(0, land.size - 1);
            while (selector.contains(t, false))
                t = MathUtils.random(0, land.size - 1);
            selector.add(t);
        }
            //selector.add(numberRandomizer(MathUtils.random(0, land.size - 1)));
    }

    public Array<CreateActor> generateLand()
    {
        Array<CreateActor> temp = new Array<CreateActor>();
        for(int n = 0; n < 3; n++)
        {
            temp.add(generate("assets.atlas", "platform1", 6, 0,
                    240, 30, 255, 95, 10, 50));
            temp.add(generate("assets.atlas", "platform2", -2, 0,
                    100, 8, 109, 50, 10, 50));
            temp.add(generate("assets.atlas", "platform3", 5, 0,
                    100, 8, 117, 45, 10, 50));
            temp.add(generate("assets.atlas", "platform4", -2, 0,
                    100, 8, 117, 45, 10, 50));
            temp.add(generate("assets.atlas", "platform5", 0, 0,
                    100, 8, 117, 45, 10, 50));
        }//15 objects -> 14 indices

        return temp;
    }

    private CreateTexture generate(String atlas, String texture, float x, float y,
                            float w, float h, float renderW, float renderH, float xSetOff, float ySetOff)
    {
        if(core.assetmanager.getTextureFromAtlas(atlas, texture) == null)
            System.out.println(atlas + " " + texture + " is missing");
        CreateTexture temp = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core,
                BodyDef.BodyType.StaticBody);
        temp.setData(0.9f, 0, true);
        temp.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                FilterID.potion_category | FilterID.mine_catageory | FilterID.egg_category | FilterID.door_category));
        //temp.setOffSet(xSetOff, ySetOff);
        temp.createOriginalCustom(world, x, y, w, h, renderW, renderH, false);
        temp.setActive(false);

        return temp;
    }

    public void randomizeSelector()
    {
        for(int n = 0; n < 9; n++)
        {
            int t = MathUtils.random(0, land.size - 1);
            //while (selector.contains(t, false))
            //    t = MathUtils.random(0, land.size - 1);
            selector.insert(n, t);
        }
            //selector.add(numberRandomizer(MathUtils.random(0, land.size - 1)));
    }

    private int numberRandomizer(int number)
    {
        if(selector.contains(number, false))
            return numberRandomizer(MathUtils.random(0, land.size - 1));
        else
            return number;
    }

    private void createLand(String atlas, String texture, float x, float y, float w, float h)
    {
        CreateTexture temp = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core,
                BodyDef.BodyType.StaticBody);
        temp.setData(0.9f, 0, true);
        temp.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                            FilterID.potion_category | FilterID.mine_catageory));
        temp.create(world, x, y, w, h, false);
        temp.setActive(false);

        land.add(temp);
    }

    private void createLand(String atlas, String texture, float x, float y,
                           float w, float h, float renderW, float renderH, float xSetOff, float ySetOff)
    {
        if(core.assetmanager.getTextureFromAtlas(atlas, texture) == null)
            System.out.println(atlas + " " + texture + " is missing");
        CreateTexture temp = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core,
                BodyDef.BodyType.StaticBody);
        temp.setData(0.9f, 0, true);
        temp.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                FilterID.potion_category | FilterID.mine_catageory | FilterID.egg_category | FilterID.door_category));
        //temp.setOffSet(xSetOff, ySetOff);
        temp.createOriginalCustom(world, x, y, w, h, renderW, renderH, false);
        temp.setActive(false);

        land.add(temp);
    }

    private void createLand(String atlas, String texture, String customBody, float x, float y, float w, float h, float scale)
    {
        CreateTexture temp = new CreateTexture(core.assetmanager.getTextureFromAtlas(atlas, texture), core,
                BodyDef.BodyType.StaticBody);
        temp.setData(0.9f, 0, true);
        temp.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                FilterID.potion_category | FilterID.mine_catageory | FilterID.egg_category | FilterID.door_category));
        temp.setOffSet(50, 20);
        temp.createCustomBody(world, loader, customBody, x, y, w, h, scale, false);
        temp.setActive(false);

        land.add(temp);
    }

    private void addDecoration(String atlas, String texture, float x, float y, int w, int h)
    {
        TextureRegion temp = core.assetmanager.getTextureFromAtlas(atlas, texture);
        temp.setRegionWidth(w);
        temp.setRegionHeight(h);
        decorations.put(temp, new Vector2(x, y));
    }

    public void generateMap()
    {
        Random randomizer = new Random();
        amountOfLand = MathUtils.random(1, 8);
        Array<Integer> tempSelect = new Array<Integer>();
        System.out.println(amountOfLand);
        for(int n = 0; n < amountOfLand; n++)
        {
            float xP = MathUtils.random(-10, 60), yP = MathUtils.random(-10, 40);
            land.get(selector.get(n)).setPosition(xP, yP);
            land.get(selector.get(n)).setActive(true);
            tempSelect.add(selector.get(n));
        }

        amountOfEggs = MathUtils.random(0, 10);
        for(int n = 0; n < amountOfEggs; n++)
        {
            int index = randomizer.nextInt(tempSelect.size);
            eggGen.spawnEggs(n, land.get(tempSelect.get(index)).getX(), land.get(tempSelect.get(index)).getY(),
                    -2, 2, 10);
        }

        doorLand.setPosition(MathUtils.random(-10, 60), MathUtils.random(-10, 40));
        doorLand.setActive(true);
        door.respawn(doorLand.getX(), doorLand.getY() + 5);
        System.out.println(amountOfEggs);
    }


    public void createPlatform1()
    {
        Random randomizer = new Random();
        amountOfLand = MathUtils.random(1, 8);
        Array<Integer> tempSelect = new Array<Integer>();
        System.out.println(amountOfLand);
        for(int n = 0; n < amountOfLand; n++)
        {
            float xP = MathUtils.random(-10, 60), yP = MathUtils.random(-10, 40);
            land.get(selector.get(n)).setPosition(xP, yP);
            land.get(selector.get(n)).setActive(true);
            tempSelect.add(selector.get(n));
        }

        amountOfEggs = MathUtils.random(0, 10);
        for(int n = 0; n < amountOfEggs; n++)
        {
            int index = randomizer.nextInt(tempSelect.size);
            eggGen.spawnEggs(n, land.get(tempSelect.get(index)).getX(), land.get(tempSelect.get(index)).getY(),
                    -2, 2, 10);
        }

        doorLand.setPosition(MathUtils.random(-10, 60), MathUtils.random(-10, 40));
        doorLand.setActive(true);
        door.respawn(doorLand.getX(), doorLand.getY() + 2);
        System.out.println(amountOfEggs);
    }

    public void recreatePlatform()
    {
        amountOfLand = MathUtils.random(1, 8);
        for(int n = 0; n < amountOfLand; n++)
        {
            float xP = MathUtils.random(-10, 60), yP = MathUtils.random(-10, 40);
            land.get(selector.get(n)).setPosition(xP, yP);
        }
        amountOfEggs = MathUtils.random(0, 10);
        for(int n = 0; n < amountOfEggs; n++)
        {
            int landIndex = MathUtils.random(0, land.size - 1);
            eggGen.spawnEggs(n, land.get(landIndex).getX(), land.get(landIndex).getY(), -2, 2, 10);
        }

        int doorIndex = MathUtils.random(0, amountOfLand);
        door.respawn(land.get(doorIndex).getX(), land.get(doorIndex).getY() + 5);
        System.out.println(amountOfEggs);
    }

    public void render(int indexAdder, float delta, Object playerID)
    {
        doorReset(playerID);

        if(land.size > 0)
            for(int n = 0; n < amountOfLand; n++)
            {
                if (generate)
                {
                    if (land.get(selector.get(n)).isActive())
                    {
                        land.get(selector.get(n)).setPosition(-300, -300);
                        land.get(selector.get(n)).setActive(false);
                    }
                } else
                    land.get(selector.get(n)).display();
            }
        if(generate)
        {
            randomizeSelector();
            createPlatform1();
            generate = false;
        }
        if(decorations.size > 0)
            for(ObjectMap.Entry<TextureRegion, Vector2> itr : decorations)
                core.batch.draw(itr.key, itr.value.x, itr.value.y);
        eggGen.display(delta, playerID, amountOfEggs);
        doorLand.display();
        door.display(delta);
    }

    public void displayAll(float delta, Object playerID)
    {
        doorReset(playerID);

        if(land.size > 0)
            for(int n = 0; n < amountOfLand; n++)
            {
                if (generate)
                {
                    if (land.get(selector.get(n)).isActive())
                    {
                        land.get(selector.get(n)).setPosition(-300, -300);
                        land.get(selector.get(n)).setActive(false);
                    }
                } else
                    land.get(selector.get(n)).display();
            }
        if(generate)
        {
            randomizeSelector();
            createPlatform1();
            generate = false;
        }
        if(decorations.size > 0)
            for(ObjectMap.Entry<TextureRegion, Vector2> itr : decorations)
                core.batch.draw(itr.key, itr.value.x, itr.value.y);
        eggGen.display(delta, playerID, amountOfEggs);
        doorLand.display();
        door.display(delta);
    }

    public void doorReset(Object playerID)
    {
        if(door.ifCollide(playerID))
        {
            generate = true;
            eggGen.despawnAll(amountOfEggs);
            eggGen.randomizeSelect();
        }
    }

    public boolean isGenerate()
    {
        return generate;
    }
}
