package com.mygdx.core.collision;

import com.badlogic.gdx.utils.ArrayMap;

/**Object - user data ID
 * Boolean - true if ID is in collision, set to false if not*/
public final class ObjectID
{
    static final public ArrayMap<Object, Boolean> enemyList = new ArrayMap<Object, Boolean>();
    static final public ArrayMap<Object, Boolean> mineList = new ArrayMap<Object, Boolean>();
    static final public ArrayMap<Object, Boolean> projectileList = new ArrayMap<Object, Boolean>();
    static final public ArrayMap<Object, Boolean> enemyProjectile = new ArrayMap<Object, Boolean>();
    static final public ArrayMap<Object, Boolean> rangeList = new ArrayMap<Object, Boolean>();
    static final public ArrayMap<Object, Boolean> rubble = new ArrayMap<Object, Boolean>();
    static final public ArrayMap<Object, Boolean> spiritOrbs = new ArrayMap<Object, Boolean>();
    static public Object shieldID, lordMineID;
}
