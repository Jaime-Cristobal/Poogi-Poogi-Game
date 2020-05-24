package com.mygdx.core.collision;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by FlapJack on 7/22/2017.
 */

public class FilterDetector implements ContactFilter
{
    private boolean collide;
    private Filter isRightFilter;

    private Filter filter1;
    private Filter filter2;

    public FilterDetector()
    {
        isRightFilter = new Filter();
        filter1 = new Filter();
        filter2 = new Filter();
    }

    /**Happens automatically, no need to explicitly call*/
    public boolean shouldCollide (Fixture fixtureA, Fixture fixtureB)
    {
        collide = false;

        final Filter filterA = fixtureA.getFilterData();
        final Filter filterB = fixtureB.getFilterData();

        filter1 = filterA;
        filter2 = filterB;

        if (filterA.categoryBits == filterB.categoryBits && filterA.categoryBits != 0)
        {
            //Checks if there are no collisions, imply there are no collision if
            //collide isn't returned.

            return filterA.categoryBits > 0;
        }

        collide = (filterA.maskBits & filterB.categoryBits) != 0 &&
                (filterA.categoryBits & filterB.maskBits) != 0;

        return collide;
    }

    public boolean isColliding()
    {
        return collide;
    }

    public boolean FloorFeedback(short playerBit, short floorBit)
    {
        if((filter1.categoryBits == playerBit && filter2.categoryBits == floorBit) ||
                (filter1.categoryBits == floorBit && filter2.categoryBits == playerBit)) {
            if (collide) {
                System.out.println("colliding");
                //filter1.categoryBits = 0;
                filter2.categoryBits = 0;
                return true;
            }
        }

        //System.out.println(floor_filter.categoryBits + " " + filter1.categoryBits + " " + filter2.categoryBits);

        return false;
    }

    public boolean feedback(short bit1, short bit2)
    {
        if(collide)
        {
            if ((filter1.categoryBits == bit1 && filter2.categoryBits == bit2) ||
                    (filter1.categoryBits == bit2 && filter2.categoryBits == bit1))
            {
                //filter2.categoryBits = 0;
                return true;
            }
        }
        else
            release();

        return false;
    }

    public void release()
    {
        filter1.categoryBits = 0;
        filter2.categoryBits = 0;
    }
}