package com.mygdx.core;

public final class GameStat
{
    static public int hour = 0, minute = 0, second = 0;

    static public void globalCounter()
    {
        second++;
        if(second >= 60)
        {
            minute++;
            second = 0;
        }
        if(minute >= 60)
        {
            hour++;
            minute = 0;
        }
    }

    static private int lifeHour = 0, lifeMin = 0, lifeSecond = 0;

    static public void lifeCounter()
    {
        lifeSecond++;
        if(lifeSecond >= 60)
        {
            lifeMin++;
            lifeSecond = 0;
        }
        if(lifeMin >= 60)
        {
            lifeHour++;
            lifeMin = 0;
        }
    }

    static public boolean pumpkinCat = false, pumpkinDemon = false, treeGift = false;
}
