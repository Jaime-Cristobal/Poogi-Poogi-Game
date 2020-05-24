package com.mygdx.core.actor.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Scaler;
import com.mygdx.core.actor.creation.CreateActor;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.RenderObject;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.ObjectID;
import com.mygdx.core.mechanics.FSM;

public final class Enemy implements RenderObject
{
    final private CreateActor actor;
    final private FSM ai;
    private float playerAngle = 0;
    private int idleMovement = 0;
    private float iniPos = 0;
    private float landX = 0;
    private float speed = 7, idleSpeed = 5;
    private float offsetX = 0, offsetY = 0;

    private float playerX = 0;
    private float minX, maxX, minY, maxY = 0;
    private boolean display = false, hasMovement = true;
    private int enemyID = 0;
    private boolean containPortal = false, outAnimation = false;

    public Enemy(CreateActor actor)
    {
        this.actor = actor;

        ai = new FSM();
    }

    public void setData(float density, float restitution, boolean rotation)
    {
        actor.setData(density, restitution, rotation);
    }

    public void setFilters(short enemyID, short collidingID)
    {
        actor.setFilter(enemyID, collidingID);
    }

    public void setSpawn(float minX, float maxX, float minY, float maxY)
    {
        this.minX = minX * Scaler.PIXELS_TO_METERS;
        this.maxX = maxX * Scaler.PIXELS_TO_METERS;
        this.minY = minY * Scaler.PIXELS_TO_METERS;
        this.maxY = maxY * Scaler.PIXELS_TO_METERS;
    }

    public void addAnimatedRegion(String region, float speed)
    {
        if(actor instanceof CreateAnimation)
            ((CreateAnimation) actor).addRegion(region, speed);
        else
            Gdx.app.error("Enemy.java", "actor is not a CreateAnimation class!");
    }

    /**This should called last after setData, setFilters, and addAnimatedRegion.
     * setSpawn is optional but the actor will spawn at (0, 0) coord as a default.*/
    public void create(World world, float width, float height, Object ID)
    {
        actor.setUniqueID(ID);
        actor.create(world, -700, -700, width, height, false);
    }

    public void create(World world, float width, float height, float renderW, float renderH, Object ID)
    {
        actor.setUniqueID(ID);
        //ObjectID.enemyList.put(ID, true);
        ((CreateAnimation)actor).createOriginalCustom(world, -700,
                -700, width, height, renderW, renderH, false);
    }

    public void setRegion(String region)
    {
        if(actor instanceof CreateAnimation)
            ((CreateAnimation) actor).setRegion(region);
        else
            Gdx.app.error("Enemy.java", "actor is not a CreateAnimation class!");
    }

    public void setLoopBack()
    {
        if(actor instanceof CreateAnimation)
            ((CreateAnimation) actor).setLoopBack(true);
    }

    public void setMovement(boolean val)
    {
        hasMovement = val;
    }

    public void setNoMovement()
    {
        actor.setSpeed(0, 0);
    }

    public void setSpeed(float speed, float idleSpeed)
    {
        this.speed = speed;
        this.idleSpeed = idleSpeed;
    }

    public void setTag(int id)
    {
        enemyID = id;
    }

    public int getTag()
    {
        return enemyID;
    }

    /**state = 0 -> COMBAT
     *       = 1 -> IDLE
     *
     * This pathfinding is for enemies that moves towards the players to attack.
     **/
    public void setLandMovement()
    {
        if(display)
        {
            switch (ai.getState(actor.getBody().getPosition())) {
                case 0:
                    if (iniPos != 0)     //reset idle initial position
                        iniPos = 0;     //idle will happen with the current position then set here
                    actor.setActive(true);
                    if (playerX + 2 < actor.getX())
                    {
                        if(hasMovement)
                            actor.getBody().setLinearVelocity(-speed, actor.getBody().getLinearVelocity().y);
                        if (actor.getWidth() > 0)
                            actor.flip();
                    }
                    else if (playerX + 2 > actor.getX())
                    {
                        if(hasMovement)
                            actor.getBody().setLinearVelocity(speed, actor.getBody().getLinearVelocity().y);
                        if (actor.getWidth() < 0)
                            actor.flip();
                    }
                    else
                    {
                        if (hasMovement)
                            actor.getBody().setLinearVelocity(0, actor.getBody().getLinearVelocity().y);
                    }
                    //actor.getBody().setLinearVelocity(MathUtils.cos(playerAngle) * 10,       //moves toward the player
                    //        actor.getBody().getLinearVelocity().y);
                    break;
                case 1:
                    actor.setActive(true);

                    if (iniPos == 0)
                        iniPos = actor.getX();
                    if (idleMovement == 0) {
                        idleMovement = MathUtils.randomBoolean() ? 1 : -1;
                        if (idleMovement < 0)      //makes sure the sprite is facing the leftward direction
                            actor.flip();         //if it's going to the left.
                    }

                    //moves back and forth
                    //reaches the farthest left and has to go right direction

                    if (iniPos - 10 > actor.getX()) {
                        idleMovement *= -1;
                        if (actor.getWidth() < 0)
                            actor.flip();
                    }
                    //reaches the farthest right and has to go left direction
                    if (iniPos + 10 < actor.getX()) {
                        idleMovement *= -1;
                        if (actor.getWidth() > 0)
                            actor.flip();
                    }
                    /**
                    if (actor.getX() > landX - 7) {
                        idleMovement *= -1;
                        if (actor.getWidth() < 0)
                            actor.flip();
                    }
                    //reaches the farthest right and has to go left direction
                    if (actor.getX() < landX + 7) {
                        idleMovement *= -1;
                        if (actor.getWidth() > 0)
                            actor.flip();
                    }*/
                    if(hasMovement)
                        actor.getBody().setLinearVelocity(idleMovement * idleSpeed, actor.getBody().getLinearVelocity().y);
                    else
                        actor.getBody().setLinearVelocity(0, actor.getBody().getLinearVelocity().y - actor.getBody().getGravityScale());
                    break;

                default:
                    actor.getBody().setLinearVelocity(0, actor.getBody().getLinearVelocity().y - actor.getBody().getGravityScale());
                    actor.setActive(false);
                    break;
            }
        }
    }

    public void setLandPos(float landX)
    {
        this.landX = landX;
    }

    public void display(float delta)
    {
        if(display)
            ((CreateAnimation)actor).displayCustom(delta, offsetX, offsetY);

        if(delta != 0)
        {
            if(!actor.getBody().isActive())
                actor.setActive(true);
        }
        else
            if(actor.getBody().isActive())
                actor.setActive(false);
    }

    public void update(Object playerID)
    {
        setLandMovement();
    }

    public void setDisplay(boolean val)
    {
        display = val;
    }

    public boolean ifDisplayed()
    {
        return display;
    }

    public void respawn(float x, float y)
    {
        display = true;
        actor.setActive(true);
        actor.setPosition(x, y);
        if(hasMovement)
            actor.setSpeed(idleMovement * idleSpeed, actor.getVelY());
    }

    public void despawn()
    {
        display = false;
        actor.setActive(false);
        if(hasMovement)
            actor.setSpeed(0, 0);
        actor.setPosition(-700, -700);
    }

    public void setAirMovement()
    {

    }

    public void setGroundMovement()
    {

    }

    public void setPlayerPos(Vector2 playerPos, float playerAngle)
    {
        ai.update(playerPos);
        playerX = playerPos.x;
        this.playerAngle = playerAngle;
    }

    public boolean ifCollide(Object playerID)
    {
        return EventManager.conditionsEqual(playerID, actor.getBody().getUserData());
    }

    public Body getBody()
    {
        return actor.getBody();
    }

    public CreateActor getFixture()
    {
        return actor;
    }

    public void setPosition(float x, float y)
    {
        actor.setPosition(x, y);
    }

    public Object getID()
    {
        return actor.getBody().getUserData();
    }

    public void setContainPortal(boolean val)
    {
        containPortal = val;
    }

    public boolean isContainPortal()
    {
        return containPortal;
    }

    public void setOffSets(float offX, float offY)
    {
        offsetX = offX;
        offsetY = offY;
    }

    public boolean ifRegion(String region)
    {
        if(!(actor instanceof CreateAnimation))
            return false;
        return ((CreateAnimation)actor).getRegion().equals(region);
    }

    public boolean isOutAnimation()
    {
        return outAnimation;
    }

    public void setOutAnimation(boolean flag)
    {
        outAnimation = flag;
    }
}
