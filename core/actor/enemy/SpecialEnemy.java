package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;

public final class SpecialEnemy
{
    private CreateAnimation enemy;
    private boolean path = true, foundPlayer = false;
    private float lastX = 0;
    private int hp = 7;

    public SpecialEnemy(Core core, World world, String region, Object id)
    {
        enemy = new CreateAnimation("characters.atlas", core, BodyDef.BodyType.KinematicBody);
        enemy.addRegion(region, 2f);
        enemy.setData(0.9f, 0, true);
        enemy.setFilter(FilterID.hunter_category, (short)(FilterID.player_category | FilterID.enemy_category |
                FilterID.floor_category | FilterID.mine_catageory | FilterID.collector_category));
        enemy.setUniqueID(id);
        enemy.createOriginalCustom(world, -700, -700, 60, 60, 142, 155, false);
        enemy.setDisplay(false);
        enemy.setActive(false);
        enemy.setNoGravity();
    }

    public void update(float playerX, float playerY, float delta)
    {
        if(enemy.isActive() && delta > 0)
        {
            if((ObjectID.mineList.containsKey(EventManager.getUserA()) &&
                    EventManager.getUserB() == enemy.getBody().getUserData())
                    || (ObjectID.mineList.containsKey(EventManager.getUserB()) &&
                    EventManager.getUserA() == enemy.getBody().getUserData()))
            {
                hp--;
            }

            if((playerX < enemy.getX() + 20 && playerX > enemy.getX()) &&
                    (playerY < enemy.getY() + 15 && playerY > enemy.getY() - 15) &&
                    enemy.getDirection() > 0)
            {
                if(!foundPlayer)
                    foundPlayer = true;
                if(enemy.getX() < playerX)
                {
                    if(enemy.getDirection() < 0)
                        enemy.flip();
                }
                else
                {
                    if(enemy.getDirection() > 0)
                        enemy.flip();
                }

                enemy.getBody().setLinearVelocity(
                        MathUtils.cos(enemy.getBody().getAngle()) * 17,
                        MathUtils.sin(enemy.getBody().getAngle()) * 17);
                enemy.getBody().setTransform(enemy.getBody().getPosition(),
                        MathUtils.atan2(playerY - enemy.getBody().getPosition().y,
                                playerX - enemy.getBody().getPosition().x));
            }
            else if((playerX < enemy.getX() && playerX > enemy.getX() - 20) &&
                    (playerY < enemy.getY() + 15 && playerY > enemy.getY() - 15) &&
                    enemy.getDirection() < 0)
            {
                if(!foundPlayer)
                    foundPlayer = true;
                if(enemy.getX() < playerX)
                {
                    if(enemy.getDirection() < 0)
                        enemy.flip();
                }
                else
                {
                    if(enemy.getDirection() > 0)
                        enemy.flip();
                }

                enemy.getBody().setLinearVelocity(
                        MathUtils.cos(enemy.getBody().getAngle()) * 17,
                        MathUtils.sin(enemy.getBody().getAngle()) * 17);
                enemy.getBody().setTransform(enemy.getBody().getPosition(),
                        MathUtils.atan2(playerY - enemy.getBody().getPosition().y,
                                playerX - enemy.getBody().getPosition().x));
            }
            else
            {
                if(enemy.getBody().getAngle() != 0)
                    enemy.getBody().setTransform(enemy.getBody().getPosition(), 0);
                if(foundPlayer)
                {
                    enemy.getBody().setLinearVelocity(0, 0);
                    lastX = enemy.getX();
                    foundPlayer = false;
                }

                if(enemy.getX() > lastX + 20)
                    path = false;
                if(enemy.getX() < lastX - 20)
                    path = true;

                if(path)
                {
                    if(enemy.getDirection() < 0)
                        enemy.flip();
                    if(enemy.getVelX() != 5)
                        enemy.getBody().setLinearVelocity(5, 0);
                }
                else
                {
                    if(enemy.getDirection() > 0)
                        enemy.flip();
                    if(enemy.getVelX() != -5)
                        enemy.getBody().setLinearVelocity(-5, 0);
                }
            }
        }
        else
        {
            enemy.getBody().setLinearVelocity(0, 0);
        }
    }

    public void display(float delta)
    {
        if(enemy.isActive() && enemy.ifDisplayed())
            enemy.display(delta);
    }

    public boolean isHPZero()
    {
        return hp <= 0;
    }

    public void spawn(float x, float y, int health)
    {
        enemy.setDisplay(true);
        enemy.setActive(true);
        enemy.setPosition(x, y);
        lastX = enemy.getX();
        hp = health;
    }

    public void despawn()
    {
        enemy.setPosition(-700, -700);
        enemy.setActive(false);
        enemy.setDisplay(false);
        hp = 0;
    }

    public void setSpeed(float velX, float velY)
    {
        enemy.setSpeed(velX, velY);
    }

    public void addRegion(String region, float frames)
    {
        enemy.addRegion(region, frames);
    }

    public void setRegion(String region)
    {
        enemy.setRegion(region);
    }

    public boolean ifCollison()
    {
        return EventManager.inContact(enemy.getBody().getUserData());
    }

    public boolean inCollisonWith(Object id)
    {
        return EventManager.conditions(enemy.getBody().getUserData(), id);
    }

    public float getX()
    {
        return enemy.getX();
    }

    public float getY()
    {
        return enemy.getY();
    }

    public boolean getDisplayOrActive()
    {
        return enemy.ifDisplayed() && enemy.isActive();
    }

    public boolean isSpeedZero()
    {
        return enemy.getVelX() == 0 || enemy.getVelY() == 0;
    }
}
