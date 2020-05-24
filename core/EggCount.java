package com.mygdx.core;

public class EggCount
{
    static public int previousEggs = 0;
    static public int previousDoors = 0;
    static public int eggAmount = 0;
    static public int previousTotalEgg = 0;
    static public int totalEggs = 0;
    static public int doorAmount = 0;
    static public int maxDoors = 0;
    static public int deaths = 0;

    static public void addEgg(int val)
    {
        if(eggAmount + val > 999)
            eggAmount = 999;
        else if(eggAmount + val < 0)
            eggAmount = 0;
        else
            eggAmount += val;

        if(totalEggs + val >= 0)
            totalEggs += val;
        else if(totalEggs + val > 99999999)
            totalEggs = 99999999;
        else
            totalEggs = 0;
    }
}
