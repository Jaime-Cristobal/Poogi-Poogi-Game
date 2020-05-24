package com.mygdx.core.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by seacow on 11/8/2017.
 */

public final class CollisionEvent implements ContactListener
{
    private Object userDataA, userDataB = null;
    public boolean noCollision = false;

    public ContactListener getContactListener()
    {
        return this;
    }

    /**Used to see if there is collision between 2 bodies*/
    public boolean ifContact(Object id1, Object id2)
    {
        return (id1.equals(userDataA) && id2.equals(userDataB)) ||
                (id1.equals(userDataB) && id2.equals(userDataA));
    }

    public boolean ifContactEquals(Object id1, Object id2)
    {
        return (id1 == userDataA && id2 == userDataB) ||
                (id1 == userDataB && id2 == userDataA);
    }

    public Object getUserDataA()
    {
        return userDataA;
    }

    public Object getUserDataB()
    {
        return userDataB;
    }

    public void resetUserData()
    {
        userDataA = null;
        userDataB = null;
    }

    /**Used to see if there is a collision detected in the body.*/
    public boolean inContact(Object id1)
    {
        /**
        if(userDataA != null)
            return id1 == userDataA;
        if(userDataB != null)
            return id1 == userDataB;
         */

        return id1.equals(userDataA) || id1.equals(userDataB);
    }

    /** Called when two fixtures begin to touch. */
    public void beginContact (Contact contact)
    {
        //Gdx.app.log("Contact", "begin");

        userDataA = contact.getFixtureA().getUserData();
        userDataB = contact.getFixtureB().getUserData();
    }

    /** Called when two fixtures cease to touch. */
    public void endContact (Contact contact)
    {
        //Gdx.app.log("Contact", "end");

        userDataA = null;
        userDataB = null;
    }

    /*
     * This is called after a contact is updated. This allows you to inspect a contact before it goes to the solver. If you are
     * careful, you can modify the contact manifold (e.g. disable contact). A copy of the old manifold is provided so that you can
     * detect changes. Note: this is called only for awake bodies. Note: this is called even when the number of contact points is
     * zero. Note: this is not called for sensors. Note: if you set the number of contact points to zero, you will not get an
     * EndContact callback. However, you may get a BeginContact callback the next step.
     */
    public void preSolve (Contact contact, Manifold oldManifold)
    {
        //if(noCollision)
        //    contact.setEnabled(false);
    }

    /*
     * This lets you inspect a contact after the solver is finished. This is useful for inspecting impulses. Note: the contact
     * manifold does not include time of impact impulses, which can be arbitrarily large if the sub-step is small. Hence the
     * impulse is provided explicitly in a separate data structure. Note: this is only called for contacts that are touching,
     * solid, and awake.
     */
    public void postSolve (Contact contact, ContactImpulse impulse)
    {
        //if(noCollision)
        //    contact.setEnabled(true);
    }
}
