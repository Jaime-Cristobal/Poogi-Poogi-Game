package com.mygdx.core.collision;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactListener;

/**
 * Static functions used in:
 *      - class MapStage.java -> render()
 *      - class LowMob.java -> render()
 *
 * @see CollisionEvent
 * @see FilterDetector
 * */

public final class EventManager
{
    private final static CollisionEvent event = new CollisionEvent();
    private final static FilterDetector filterEvent = new FilterDetector();

    public static ContactListener getContactListener()
    {
        return event.getContactListener();
    }
    public static ContactFilter getContactFilter()
    {
        return filterEvent;
    }

    public static boolean conditions(Object userData1, Object userData2)
    {
        event.noCollision = false;
        return event.ifContact(userData1, userData2);
    }

    public static boolean conditionsOnce(Object userData1, Object userData2)
    {
        event.noCollision = true;
        return event.ifContact(userData1, userData2);
    }

    public static boolean conditionsEqual(Object userData1, Object userData2)
    {
        event.noCollision = false;
        return event.ifContactEquals(userData1, userData2);
    }

    public static boolean inContact(Object userData)
    {
        return event.inContact(userData);
    }

    public static Object getUserA()
    {
        return event.getUserDataA();
    }

    public static Object getUserB()
    {
        return event.getUserDataB();
    }

    public static void resetUserData()
    {
        event.resetUserData();
    }

    /**A ContactFilter will have to be passed to a world before this
     * can be put into use.*/
    public static boolean filterContact(short id1, short id2)
    {
        return filterEvent.FloorFeedback(id1, id2);
    }
}
