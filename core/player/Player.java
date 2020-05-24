package com.mygdx.core.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.Scaler;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.HitboxID;
import com.mygdx.core.collision.ObjectID;

/**
 * Created by FlapJack on 7/22/2017.
 *
 * just have a lever in hud to turn the player in a direction
 *
 * Player sprites have to be facing down in order to move with the
 * direction the player's frontal area is facing.
 */

public class Player implements GestureListener, InputProcessor
{
    float midScreen = (Gdx.graphics.getWidth() / 2) / Scaler.PIXELS_TO_METERS;

    final private Core main;
    final private CreateAnimation actor;

    private float originX, originY = 0;
    private boolean fly = false;
    private float lastY = 0;

    private int direction = 1;
    private boolean exit = false;
    private float delta = 0;
    private boolean tap = false, hit = false;
    private GestureDetector detector = new GestureDetector(this);
    private float actionPts = 0;
    private boolean isPause = false;
    private float speed = 15, counter = 0;

    public Player(String file, Core main)
    {
        this.main = main;
        actor = new CreateAnimation(file, main, BodyDef.BodyType.DynamicBody);
        actor.addRegion("jump", 1.5f);
        actor.addRegion("blink", 5.0f);
        actor.addRegion("fly", 2.5f);
        actor.addRegion("sad", 3.0f);
        actor.addRegion("entry", 1.5f);
        actor.addRegion("disapear", 1.5f);
        actor.addRegion("hit_fly", 2.0f);

        //actor.addRegion("walk", 1.5f);
        //actor.addRegion("look", 2.5f);
        //actor.addRegion("sniff", 2.5f);

        //actor.addRegion("jump", 3.5f);
        //actor.addRegion("Run", 4.7f);
        //actor.addRegion("Idle", 4.7f);
    }

    public void createBody(World world, float posX, float posY, float w, float h, float renderW, float renderH)
    {
        actor.setFilter(FilterID.player_category, (short) (FilterID.floor_category |
                FilterID.egg_category | FilterID.enemy_category | FilterID.platform_category
                | FilterID.trader_category | FilterID.collector_category | FilterID.trash_category
                | FilterID.potion_category | FilterID.door_category | FilterID.boss_category | FilterID.tree_category
                | FilterID.item_category | FilterID.hunter_category));

        actor.setData(0.1f, 0, true);
        actor.setUniqueID(HitboxID.player);
        actor.createOriginalCustom(world, posX, posY, w, h, renderW, renderH, false);
        actor.setRegion("jump");
        actor.setDisplay(true);
        //actor.setRegion("Run");
    }

    /**@param delta is the delta time from render in MapStage.java.
     *              This is the same as Gdx.graphics.getDeltaTime */
    public void display(float delta)
    {
        this.delta = delta;

        if(delta != 0)
        {
            checkHit(delta);
            if(!actor.getBody().isActive())
            {
                actor.setActive(true);
            }

            if(!isPause)
            {
                if (actor.getVelX() < 1 && actor.getVelX() > -1 && !fly) {
                    if (actor.checkRegion("jump"))
                        actor.resetAnimation();
                    //actor.setRegion("sniff");
                    actor.setRegion("blink");
                }
                if (EventManager.filterContact(FilterID.player_category, FilterID.floor_category)) {
                    if (fly)
                        fly = false;
                }
            }
        }
        else {
            if (actor.getBody().isActive())
            {
                actor.setActive(false);
            }
        }

        if(actor.ifDisplayed())
            actor.displayCustom(delta, -15, 10);
    }

    public void setRegion(String set)
    {
        actor.setRegion(set);
    }

    public void setDisplay(boolean val)
    {
        actor.setDisplay(val);
    }

    public void setGravity()
    {
        actor.setSpeed(actor.getVelX(), actor.getBody().getGravityScale());
    }

    public void setGravityScale(float scale)
    {
        actor.getBody().setGravityScale(scale);
    }

    public void setPosition(float x, float y)
    {
        actor.setPosition(x, y);
    }

    public void setDisapear()
    {
        actor.setSpeed(0, 0);
        actor.resetAnimation();
        actor.setLoop(false);
        actor.setRegion("disapear");
    }

    public void setAppear()
    {
        actor.resetAnimation();
        actor.setLoop(false);
        actor.setRegion("entry");
    }

    public void setLoop(boolean val)
    {
        actor.setLoop(val);
    }

    public void setAnimLoop()
    {
        actor.setLoop(true);
    }

    public void resetAnimation()
    {
        actor.setLoop(true);
        actor.resetAnimation();
    }

    public boolean hitRubble()
    {
        if((ObjectID.rubble.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == actor.getBody().getUserData())
                || (ObjectID.rubble.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == actor.getBody().getUserData()))
        {
            return true;
        }

        return false;
    }

    public void hit(float velX, float velY)
    {
        if(!hit)
        {
            isPause = true;
            hit = true;
            actor.setRegion("hit_fly");
            actor.setSpeed(velX, velY);
        }
        else
            counter = 0;
    }

    public void checkHit(float delta)
    {
        if(hit)
        {
            counter += delta;
            if(counter >= 0.5f)
            {
                hit = false;
                isPause = false;
                counter = 0;
                actor.setRegion("fly");
            }
        }
    }

    public void setPause(boolean val)
    {
        isPause = val;
    }

    public boolean isPaused()
    {
        return isPause;
    }

    public String getRegion()
    {
        return actor.getRegion();
    }

    public boolean isAnimFinished()
    {
        return actor.isAnimationFinished();
    }

    public void setExit(boolean exit)
    {
        this.exit = exit;
    }

    public Vector2 getPos()
    {
        return actor.getBody().getPosition();
    }

    public float getAngle()
    {
        return actor.getBody().getAngle();
    }

    /**Box2d physics will no longer apply to the player when called*/
    public void setActive(boolean val)
    {
        actor.setActive(val);
    }

    public void setOrigin(float x, float y)
    {
        originX = x; originY = y;
    }

    public boolean isFlying()
    {
        return fly;
    }

    /**If you need to reset to the original position like after a screen change*/
    public void reset()
    {
        actor.setPosition(0, 0);
    }

    public GestureDetector getGesture()
    {
        return detector;
    }

    public InputProcessor getInputProcessor()
    {
        return this;
    }

    public float getX()
    {
        return actor.getX();
    }

    public Body getBody()
    {
        return actor.getBody();
    }

    public void setFilterEmpty()
    {
        actor.setFilter(FilterID.player_category, (short) 0);
    }

    public void resetFilter()
    {
        actor.setFilter(FilterID.player_category, (short) (FilterID.floor_category |
                FilterID.egg_category | FilterID.enemy_category | FilterID.platform_category
                | FilterID.trader_category));
    }

    public float getY()
    {
        return actor.getY();
    }

    public float getRawX()
    {
        return actor.getX();
    }

    public float getRawY()
    {
        return actor.getY();
    }

    public float getDirection()
    {
        return actor.getSpriteDirection();
    }

    public void resetTap()
    {
        tap = false;
    }

    public void setActionPoints(float val)
    {
        actionPts = val;
    }

    public boolean getTap()
    {
        return tap;
    }

    public void setSpeed(float val)
    {
        speed = val;
    }

    /** @see InputProcessor#touchDown(int, int, int, int) */
    public boolean touchDown (float x, float y, int pointer, int button)
    {
        return false;
    }

    /** Called when a tap occured. A tap happens if a touch went down on the screen and was lifted again without moving outside
     * of the tap square. The tap square is a rectangular area around the initial touch position as specified on construction
     * time of the {@link GestureDetector}.
     * @param count the number of taps. */
    public boolean tap (float x, float y, int count, int button)
    {
        /**
        if(count >= 2)
        {
            lastY = actor.getY();

            actor.setRegion("fly");
            fly = true;
            //if(actor.getVelY() < 5)
            //    actor.getBody().applyLinearImpulse(0, 1, actor.getX(), actor.getY(), true);
            //else
                //actor.getBody().applyLinearImpulse(0, 12, actor.getX(), actor.getY(), true);
            //tap = true;

            if(!doubleCount)
                actor.getBody().applyLinearImpulse(0, 12, actor.getX(), actor.getY(), true);
            doubleCount = !doubleCount;
            //doubleCount = (true) ? false : true;

            //if(doubleCount)
            //    doubleCount = false;
            //else
            //    doubleCount = true;
        }
         */
        /**
        if(count >= 2 && actionPts >= 5)
        {
            lastY = actor.getY();

            actor.setRegion("fly");
            fly = true;
            if(actor.getVelY() < 5)
                actor.getBody().applyLinearImpulse(0, 30, actor.getX(), actor.getY(), true);
            else
                actor.getBody().applyLinearImpulse(0, 10, actor.getX(), actor.getY(), true);
            tap = true;
        }*/

        return false;
    }

    public boolean longPress (float x, float y)
    {
        return false;
    }

    /** Called when the user dragged a finger over the screen and lifted it. Reports the last known velocity of the finger in
     * pixels per second.
     * @param velocityX velocity on x in seconds
     * @param velocityY velocity on y in seconds */
    public boolean fling (float velocityX, float velocityY, int button)
    {
        return false;
    }

    /** Called when the user drags a finger over the screen.
     * @param deltaX the difference in pixels to the last drag event on x.
     * @param deltaY the difference in pixels to the last drag event on y. */
    public boolean pan (float x, float y, float deltaX, float deltaY)
    {

        return false;
    }

    /** Called when no longer panning. */
    public boolean panStop (float x, float y, int pointer, int button)
    {
        return false;
    }

    /** Called when the user performs a pinch zoom gesture. The original distance is the distance in pixels when the gesture
     * started.
     * @param initialDistance distance between fingers when the gesture started.
     * @param distance current distance between fingers. */
    public boolean zoom (float initialDistance, float distance)
    {
        return false;
    }

    /** Called when a user performs a pinch zoom gesture. Reports the initial positions of the two involved fingers and their
     * current positions.
     * @param initialPointer1
     * @param initialPointer2
     * @param pointer1
     * @param pointer2 */
    public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
    {
        return false;
    }

    /** Called when no longer pinching. */
    public void pinchStop (){}

    /** Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyDown (int keycode)
    {
        return false;
    }

    /** Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyUp (int keycode)
    {
        return false;
    }

    /** Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed */
    public boolean keyTyped (char character)
    {
        return false;
    }

    /** Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
        if(!isPause)
        {
            if (!exit && delta != 0) {
                if (screenX / Scaler.PIXELS_TO_METERS > midScreen) {
                    if (actor.getWidth() < 0)
                        actor.flip();
                    actor.setSpeed(speed, actor.getVelY() + actor.getBody().getGravityScale());
                    if (direction < 0)
                        direction *= -1;
                }
                else if (screenX / Scaler.PIXELS_TO_METERS < midScreen)
                {
                    if (actor.getWidth() > 0)
                        actor.flip(30);
                    actor.setSpeed(-speed, actor.getVelY() + actor.getBody().getGravityScale());
                    direction *= -1;
                }
            }

            if (!fly)
                actor.setRegion("jump");
            //actor.setRegion("Run");
            return true;
        }

        return false;
    }

    /** Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
        return false;
    }
    /** Called when a finger or the mouse was dragged.
     * @param pointer the pointer for the event.
     * @return whether the input was processed */
    public boolean touchDragged (int screenX, int screenY, int pointer)
    {
        if(!isPause)
        {
            if (!exit && delta != 0) {
                //if(doubleCount)
                //{
                if (actor.getVelY() < 20)
                    actor.getBody().applyLinearImpulse(0, 2, actor.getX(), actor.getY(), true);
                else if (actor.getVelY() < -20)
                    actor.getBody().applyLinearImpulse(0, 30, actor.getX(), actor.getY(), true);
                //}
                if (screenX / Scaler.PIXELS_TO_METERS > midScreen) {
                    if (actor.getWidth() < 0)
                        actor.flip();
                    actor.setSpeed(speed, actor.getVelY() + actor.getBody().getGravityScale());
                    if (direction < 0)
                        direction *= -1;
                }
                else if (screenX / Scaler.PIXELS_TO_METERS < midScreen)
                {
                    if (actor.getWidth() > 0)
                        actor.flip(30);
                    actor.setSpeed(-speed, actor.getVelY() + actor.getBody().getGravityScale());
                    direction *= -1;
                }
            }

            //if (actor.getVelY() < 5 && actor.getVelY() > -5) {
            //    fly = false;
            //    actor.setRegion("jump");
            //} else {
            //    fly = true;
            //    actor.setRegion("fly");
            //}

            if(!fly)
            {
                fly = true;
                actor.setRegion("fly");
            }
            //actor.setRegion("Run");
            return true;
        }

        return false;
    }

    /** Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     * @return whether the input was processed */
    public boolean mouseMoved (int screenX, int screenY)
    {
        return false;
    }
    /** Called when the mouse wheel was scrolled. Will not be called on iOS.
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed. */
    public boolean scrolled (int amount)
    {
        return false;
    }
}
