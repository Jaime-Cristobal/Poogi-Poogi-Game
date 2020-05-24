package com.mygdx.core.collision;


/**
 * This is for collision tracking purposes ONLY FOR box2D bodies.
 * This is REQUIRED for collision to be tracked between 2 bodies.
 *
 * For objects with multiple copies, just add another number such as
 * .player + n, where n = 1,2,3,4,5,6...
 * This will make a unique ID for each object.
 * */
public final class HitboxID
{
    public static final int player = 1, lowMob = 2, midMob = 3, highMob = 4, base = 5, item = 6, ladder = 7
            , floor = 8, leftWall = 9, pile = 10, teleport = 11;
}