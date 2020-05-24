package com.mygdx.core.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;
import com.mygdx.core.EggCount;
import com.mygdx.core.MusicPOD;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.Land;
import com.mygdx.core.actor.creation.RenderObject;
import com.mygdx.core.actor.enemy.Enemy;
import com.mygdx.core.animator.Animator;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;
import com.mygdx.core.player.Player;

import java.util.Random;

public class FMapGenerator
{
    final private Core core;
    final private World world;
    final private Array<Land> land = new Array<Land>();
    final private Array<Egg> egg = new Array<Egg>();
    final private Array<Enemy> enemies = new Array<Enemy>();

    private int amountOfLand = 0, amountOfEggs = 0, amountOfEnemy = 0;
    private int largestAmount = 0;
    final private Array<Integer> selector = new Array<Integer>();
    final private Array<Integer> landSelect = new Array<Integer>();
    final private Array<Integer> selectedEgg = new Array<Integer>();
    final private Array<Integer> eggSelector = new Array<Integer>(), enemySelector = new Array<Integer>();
    final private Random randomizer = new Random();

    private boolean fxOn = false, enemyHit = false;
    private float batSpeed = 10;
    private int minLand = 1, minEgg = 0, minEnemies = 0, maxEnemies = 7, maxEggs = 10;
    private float colX = 0, colY = 0;
    private boolean bossSpawn = false;

    public FMapGenerator(Core core, World world)
    {
        this.core = core;
        this.world = world;
    }

    public void setMinimums(int minLand, int minEgg, int minEnemies)
    {
        if(minLand > land.size || minEgg > egg.size || minEnemies > enemies.size)
        {
            Gdx.app.error("FMapGenerator.java", "Minimum is larger than array size of objects!");
            return;
        }
        this.minLand = minLand;
        this.minEgg = minEgg;
        this.minEnemies = minEnemies;
    }

    public void setMaximumEggs(int val)
    {
        maxEggs = val;
    }

    public void setBossSpawn(boolean val)
    {
        bossSpawn = val;
    }

    public void generateLand(int amount, String atlas, String texture, float width, float height,
                             float renderW, float renderH)
    {
        for(int n = 0; n < amount; n++)
        {
            land.add(new Land(core, world, atlas, texture, -700, -700,
                    width, height, renderW, renderH, texture + n));
        }
    }

    public void randomizeLand()
    {
        landSelect.clear();
        if(land.size > 8)
            amountOfLand = MathUtils.random(1, 8);
        else
            amountOfLand = MathUtils.random(minLand, land.size);

        for(int n = 0; n < amountOfLand; n++)     //only covers the land
        {
            int t = MathUtils.random(0, land.size - 1);
            while (landSelect.contains(t, true))
            {
                t = MathUtils.random(0, land.size - 1);
            }

            float xP = 0, yP = 0;

            switch (n)
            {
                case 0:
                    xP = MathUtils.random(-40, -10); yP = MathUtils.random(35, 55);
                    break;
                case 1:
                    xP = MathUtils.random(-40, -10); yP = MathUtils.random(0, 25);
                    break;
                case 2:
                    xP = MathUtils.random(5, 35); yP = MathUtils.random(35, 55);
                    break;
                case 3:
                    xP = MathUtils.random(5, 35); yP = MathUtils.random(0, 25);
                    break;
                case 4:
                    xP = MathUtils.random(50, 80); yP = MathUtils.random(35, 55);
                    break;
                case 5:
                    xP = MathUtils.random(50, 80); yP = MathUtils.random(0, 25);
                    break;
                case 6:
                    xP = MathUtils.random(90, 115); yP = MathUtils.random(35, 55);
                    break;
                case 7:
                    xP = MathUtils.random(90, 115); yP = MathUtils.random(0, 25);
                    break;
                default:
                    System.out.println("land default");
                    xP = MathUtils.random(-50, 115); yP = MathUtils.random(0, 55);
                    break;

            }
            land.get(t).respawn(xP, yP);
            landSelect.add(t);
        }
    }

    public void generateEgg(int amount, String texture, int eggID, float width, float height)
    {
        for(int n = 0; n < amount; n++)
        {
            egg.add(new Egg(texture, eggID, core, world, width, height, texture + n));
        }
    }

    public void randomizedEgg()
    {
        amountOfEggs = MathUtils.random(minEgg, maxEggs);
        selectedEgg.clear();

        for(int n = 0; n < amountOfEggs; n++)
        {
            int t = MathUtils.random(minEgg, egg.size - 1);
            //while (eggSelector.contains(t, false))
            //        t = MathUtils.random(numberOfLand, numberOfEggs - 1);

            int landIndex = landSelect.get(randomizer.nextInt(landSelect.size));
            egg.get(t).respawn(land.get(landIndex).getFixture().getX()
                            + MathUtils.random(-5.5f, 5.5f),
                    land.get(landIndex).getFixture().getY() + 10f);
            egg.get(t).setDisplay(true);

            eggSelector.insert(n, t);
        }
    }

    public void generateEnemies(int amount, String atlas, String texture, float renderSpeed, float width, float height,
                                float renderW, float renderH, boolean movement, int tag, BodyDef.BodyType type, boolean ground)
    {
        for(int n = 0; n < amount; n++)
        {
            Enemy enemy = new Enemy(new CreateAnimation(atlas, core, type));
            enemy.addAnimatedRegion(texture, renderSpeed);
            enemy.setData(0.5f, 0, true);
            if(ground)
                enemy.setFilters(FilterID.floor_category, (short)(FilterID.player_category
                        | FilterID.collector_category  | FilterID.mine_catageory | FilterID.boss_category
                        | FilterID.trash_category));
            else
                enemy.setFilters(FilterID.enemy_category, (short)(FilterID.floor_category | FilterID.player_category
                        | FilterID.collector_category  | FilterID.mine_catageory | FilterID.boss_category
                        | FilterID.trash_category));
            enemy.setRegion(texture);
            enemy.setMovement(movement);
            enemy.setTag(tag);
            enemy.setSpeed(10, 5);
            enemy.create(world, width, height, renderW, renderH, texture + n);
            enemy.setDisplay(false);

            enemies.add(enemy);
        }
    }

    public void generateEnemies(int amount, String atlas, String texture, float renderSpeed, float width, float height,
                                float renderW, float renderH, boolean movement, int tag, BodyDef.BodyType type, float offsetX,
                                float offsetY, boolean ground)
    {
        for(int n = 0; n < amount; n++)
        {
            Enemy enemy = new Enemy(new CreateAnimation(atlas, core, type));
            enemy.addAnimatedRegion(texture, renderSpeed);
            enemy.setData(0.5f, 0, true);
            if(ground)
                enemy.setFilters(FilterID.floor_category, (short)(FilterID.player_category
                        | FilterID.collector_category  | FilterID.mine_catageory | FilterID.boss_category
                        | FilterID.trash_category));
            else
                enemy.setFilters(FilterID.enemy_category, (short)(FilterID.floor_category | FilterID.player_category
                        | FilterID.collector_category  | FilterID.mine_catageory | FilterID.boss_category
                        | FilterID.trash_category));
            enemy.setRegion(texture);
            enemy.setMovement(movement);
            enemy.setTag(tag);
            enemy.setSpeed(10, 5);
            enemy.setOffSets(offsetX, offsetY);
            enemy.create(world, width, height, renderW, renderH, texture + n);
            enemy.setDisplay(false);

            enemies.add(enemy);
        }
    }

    public void generateEnemiesLoopBack(int amount, String atlas, String texture, float renderSpeed, float width, float height,
                                        float renderW, float renderH, boolean movement, int tag, BodyDef.BodyType type, boolean ground)
    {
        for(int n = 0; n < amount; n++)
        {
            Enemy enemy = new Enemy(new CreateAnimation(atlas, core, type));
            enemy.addAnimatedRegion(texture, renderSpeed);
            enemy.setData(0.5f, 0, true);
            if(ground)
                enemy.setFilters(FilterID.floor_category, (short)(FilterID.player_category
                        | FilterID.collector_category  | FilterID.mine_catageory | FilterID.boss_category
                        | FilterID.trash_category));
            else
                enemy.setFilters(FilterID.enemy_category, (short)(FilterID.floor_category | FilterID.player_category
                        | FilterID.collector_category  | FilterID.mine_catageory | FilterID.boss_category
                        | FilterID.trash_category));
            enemy.setRegion(texture);
            enemy.setMovement(movement);
            enemy.setTag(tag);
            enemy.setSpeed(10, 5);
            enemy.setLoopBack();
            enemy.create(world, width, height, renderW, renderH, texture + n);
            enemy.setDisplay(false);

            enemies.add(enemy);
        }
    }

    public void setMaxEnemies(int val)
    {
        if(maxEnemies > enemies.size)
        {
            Gdx.app.error("FMapGenerator.java", "Max value is larger than max enemy size!");
            return;
        }
        maxEnemies = val;
    }

    public void randomizeEnemy()
    {
        Random randomizer = new Random();
        amountOfEnemy = MathUtils.random(minEnemies, maxEnemies);
        enemySelector.clear();

        if(amountOfEnemy > 0)
        {
            for (int n = 0; n < amountOfEnemy; n++)
            {
                int t = MathUtils.random(0, enemies.size - 1);
                /**
                while (enemySelector.contains(t, false))
                {
                    System.out.println("Selecting new enemy");
                    t = MathUtils.random(0, enemies.size - 1);
                }*/
                enemySelector.insert(n, t);
            }

            for (int n = 0; n < enemySelector.size; n++)
            {
                int enemyIndex = enemySelector.get(n);
                switch (enemies.get(enemyIndex).getTag())
                {
                    case 1:
                        enemies.get(enemyIndex).setSpeed(batSpeed, 5);
                        enemies.get(enemyIndex).respawn(MathUtils.random(-20, 80), MathUtils.random(-5, 55));
                        break;
                    case 2:
                        int landIndex = landSelect.get(randomizer.nextInt(landSelect.size));
                        enemies.get(enemyIndex).respawn(land.get(landIndex).getFixture().getX() + MathUtils.random(-2, 2),
                                land.get(landIndex).getFixture().getY() + 1f);
                        break;
                }
            }
        }
    }

    public void setBatSpeed(float speed)
    {
        batSpeed = speed;
    }

    public float getBatSpeed()
    {
        return batSpeed;
    }

    public void generateMap()
    {
        randomizeLand();
        if(!bossSpawn)
        {
            randomizedEgg();
            randomizeEnemy();
        }
    }

    private void updateEgg(int n, Player player, Animator hitFx)
    {
        if ((ObjectID.projectileList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == egg.get(n).getFixture().getBody().getUserData())
                || (ObjectID.projectileList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == egg.get(n).getFixture().getBody().getUserData()))
        {
            egg.get(n).despawn();
        }

        if (egg.get(n).ifCollide(player.getBody().getUserData()))
        {
            fxOn = true;
            egg.get(n).update();
            switch (egg.get(n).getEggType())
            {
                case 0:
                    if(!core.music.musicOff)
                        core.sounds.eggBad.play();
                    hitFx.findRegion("hit_red");
                    break;
                case 1:
                    if(!core.music.musicOff)
                        core.sounds.eggCatch.play();
                    hitFx.findRegion("hit_yellow");
                    break;
                default:
                    if(!core.music.musicOff)
                        core.sounds.eggCatch.play();
                    hitFx.findRegion("hit_yellow");
                    break;
            }

            hitFx.resetTime();
            egg.get(n).despawn();
            egg.get(n).resetCollision();
        }

        if(egg.get(n).getFixture().getY() < -50)
            egg.get(n).despawn();
    }

    private void updateEnemy(Enemy object, Player player, Animator hitFx)
    {
        if ((ObjectID.shieldID == EventManager.getUserA() &&
                EventManager.getUserB() == object.getFixture().getBody().getUserData())
                || (ObjectID.shieldID == EventManager.getUserB() &&
                EventManager.getUserA() == object.getFixture().getBody().getUserData()))
        {
            if(!core.music.musicOff)
                core.sounds.eggCatch.play();
            fxOn = true;
            hitFx.findRegion("charge_fx");
            EggCount.addEgg(1);
            object.despawn();
        }
        if ((ObjectID.mineList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == object.getFixture().getBody().getUserData())
                || (ObjectID.mineList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == object.getFixture().getBody().getUserData()))
        {
            //core.sounds.explode.play();
            object.despawn();
        }

        if ((ObjectID.projectileList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == object.getFixture().getBody().getUserData())
                || (ObjectID.projectileList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == object.getFixture().getBody().getUserData()))
        {
            //core.sounds.explode.play();
            object.despawn();
        }

        if ((ObjectID.rangeList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == object.getFixture().getBody().getUserData())
                || (ObjectID.rangeList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == object.getFixture().getBody().getUserData()))
        {
            //core.sounds.explode.play();
            object.despawn();
        }

            if (object.ifCollide(player.getBody().getUserData()))
            {
                if(!core.music.musicOff)
                {
                    core.sounds.entry.stop();
                    core.sounds.entry.play(0.5f);
                }
                enemyHit = true;
                player.setActive(false);
                player.setPause(true);
                player.setPosition(player.getX() + (1.5f * player.getDirection() * -1), player.getY());
                player.getBody().setLinearVelocity(25 * player.getDirection() * -1,
                        player.getBody().getLinearVelocity().y + player.getBody().getGravityScale());
                player.setLoop(false);
                player.setRegion("hit_fly");
                player.resetAnimation();
                enemyHit = true;

                fxOn = true;
                hitFx.findRegion("charge_fx");

                //if (object.getTag() == 2)
                {
                    if(object.getTag() != 2)
                    {
                        int lost = MathUtils.random(1, 4);
                        if (EggCount.eggAmount - lost <= 0)
                            EggCount.eggAmount = 0;
                        else
                            EggCount.addEgg(-lost);
                    } else
                    {
                        if (EggCount.eggAmount - 1 <= 0)
                            EggCount.eggAmount = 0;
                        else
                            EggCount.addEgg(-1);
                    }
                }

                if(object.getTag() != 2)
                    object.despawn();
            }

        if(object.getFixture().getY() < -50)
            object.despawn();
    }

    private void updateLand(Land object)
    {
        if ((ObjectID.mineList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == object.getFixture().getBody().getUserData())
                || (ObjectID.mineList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == object.getFixture().getBody().getUserData()))
        {
            int gemChance = MathUtils.random(0, 100);
            if(gemChance >= 10 && gemChance <= 45)
            {
                if(!core.music.musicOff)
                    core.sounds.gemFound.play(1);
                object.spawnGems();
            }
            object.despawn();
            //have logic here for rock rumble left over after explosion
        }

        if ((ObjectID.rangeList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == object.getFixture().getBody().getUserData())
                || (ObjectID.rangeList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == object.getFixture().getBody().getUserData()))
        {
            int gemChance = MathUtils.random(0, 100);
            if(gemChance >= 10 && gemChance <= 45)
            {
                if(!core.music.musicOff)
                    core.sounds.gemFound.play(1);
                object.spawnGems();
            }
            object.despawn();
            //have logic here for rock rumble left over after explosion
        }
    }

    private void renderCulling(RenderObject object, Player player)
    {
        if(object.getFixture().getX() > player.getX() + 50 ||
                object.getFixture().getX() < player.getX() - 50 ||
                object.getFixture().getY() > player.getY() + 60 ||
                object.getFixture().getY() < player.getY() - 60)
        {
            if (object.ifDisplayed())
                object.setDisplay(false);
        }
        else if (!object.ifDisplayed())
            object.setDisplay(true);
    }

    public float getColX()
    {
        return colX;
    }

    public float getColY()
    {
        return colY;
    }

    public boolean getEnemyHit()
    {
        return enemyHit;
    }

    public void resetEnemyHit()
    {
        enemyHit = false;
    }

    public void displayScreen(float delta, Player player, Animator hitFx, boolean doorCollide, boolean portalCollide)
    {
        for (int n = 0; n < landSelect.size; n++)
        {
            int landIndex = landSelect.get(n);
            if (land.get(landIndex).ifDisplayed())
            {
                if(!doorCollide && !portalCollide)
                {
                    updateLand(land.get(landIndex));
                    if ((ObjectID.enemyList.containsKey(EventManager.getUserA()) &&
                            EventManager.getUserB() == land.get(landIndex).getFixture().getBody().getUserData())
                            || (ObjectID.enemyList.containsKey(EventManager.getUserB()) &&
                            EventManager.getUserA() == land.get(landIndex).getFixture().getBody().getUserData()))
                    {
                        colX = land.get(landIndex).getFixture().getX();
                        colY = land.get(landIndex).getFixture().getY();
                        land.get(landIndex).despawn();
                        //have logic here for rock rumble left over after explosion
                    }

                    if(land.get(landIndex).updateGem(player.getBody().getUserData()))
                    {
                        fxOn = true;
                        if(!core.music.musicOff)
                            core.sounds.gemPicked.play();
                        hitFx.findRegion("hit_yellow");
                    }

                    //renderCulling(land.get(landIndex), player);
                    //land.get(landIndex).update(player.getBody().getUserData());
                }

                land.get(landIndex).display(delta);
            }
        }

        if(!bossSpawn)
        {
            for (int n = 0; n < eggSelector.size; n++)
            {
                int eggIndex = eggSelector.get(n);
                if (egg.get(eggIndex).ifDisplayed())
                {
                    if (!doorCollide && !portalCollide)
                    {
                        updateEgg(eggIndex, player, hitFx);

                        if ((ObjectID.enemyList.containsKey(EventManager.getUserA()) &&
                                EventManager.getUserB() == egg.get(eggIndex).getFixture().getBody().getUserData())
                                || (ObjectID.enemyList.containsKey(EventManager.getUserB()) &&
                                EventManager.getUserA() == egg.get(eggIndex).getFixture().getBody().getUserData()))
                        {
                            colX = egg.get(eggIndex).getFixture().getX();
                            colY = egg.get(eggIndex).getFixture().getY();
                            egg.get(eggIndex).despawn();
                            //have logic here for rock rumble left over after explosion
                        }

                        //renderCulling(egg.get(eggIndex), player);
                        egg.get(eggIndex).update(player.getBody().getUserData());
                    }

                    egg.get(eggIndex).display(delta);
                }
            }

            for (int n = 0; n < enemySelector.size; n++)
            {
                int enemyIndex = enemySelector.get(n);
                if (enemies.get(enemyIndex).ifDisplayed())
                {
                    if (!doorCollide && !portalCollide)
                    {
                        enemies.get(enemyIndex).setPlayerPos(player.getPos(), player.getAngle());
                        updateEnemy(enemies.get(enemyIndex), player, hitFx);

                        if ((ObjectID.enemyList.containsKey(EventManager.getUserA()) &&
                                EventManager.getUserB() == enemies.get(enemyIndex).getFixture().getBody().getUserData())
                                || (ObjectID.enemyList.containsKey(EventManager.getUserB()) &&
                                EventManager.getUserA() == enemies.get(enemyIndex).getFixture().getBody().getUserData()))
                        {
                            colX = enemies.get(enemyIndex).getFixture().getX();
                            colY = enemies.get(enemyIndex).getFixture().getY();
                            enemies.get(enemyIndex).despawn();
                            //have logic here for rock rumble left over after explosion
                        }

                        //renderCulling(enemies.get(enemyIndex), player);
                        enemies.get(enemyIndex).update(player.getBody().getUserData());
                    }
                    else
                    {
                        //if(enemies.get(n).getFixture().getVelX() != 0 || enemies.get(n).getFixture().getVelY() != 0)
                        enemies.get(n).setNoMovement();
                    }

                    enemies.get(enemyIndex).display(delta);
                }
            }
        }
    }

    public void despawn()
    {
        for (int n = 0; n < egg.size; n++)
            egg.get(n).despawn();  //despawn eggs
        for (int i = 0; i < land.size; i++)
        {
            land.get(i).despawnGems();
            land.get(i).despawn();   //despawn land
        }
        for (int j = 0; j < enemies.size; j++)
            enemies.get(j).despawn();
    }

    public boolean ifEggHit()
    {
        return fxOn;
    }

    public void setEggHitToFalse()
    {
        fxOn = false;
    }

    public Vector2 getRandomLandPos()
    {
        return land.get(landSelect.get(MathUtils.random(0, landSelect.size - 1))).getFixture().getBody().getPosition();
    }
}
