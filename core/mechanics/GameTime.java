package com.mygdx.core.mechanics;

/**This class is a counter for minutes and hours for
 * the day & night cycle mechanics. Should be used in hand with
 * @see TimeState .*/

public class GameTime
{
    private int seconds = 0, minute = 0, hour = 0;

    public void setTime(int seconds, int minute, int hour)
    {
        this.seconds = seconds;
        this.minute = minute;
        this.hour = hour;
    }

    public void timerBackwards()
    {
        if(minute > 0)
        {
            seconds--;
            if (seconds <= 0) {
                minute--;
                seconds = 59;
            }
        }
        else
            if(seconds != 0)
                seconds = 0;
    }

    /**A counter which counts the minutes and adds an hour if the minutes
     * reach 60. Resets both time and hour if prompt is false. Would be
     * recommended to be used with a running game state loop which passes
     * true if the game is still running.*/
    public void timerOn(boolean prompt)
    {
        if (prompt)
        {
            seconds++;
            if(seconds >= 60)
            {
                minute++;
                seconds = 0;

                if(minute >= 60)
                {
                    hour++;
                    minute = 0;
                }
            }
        }
        else if (seconds != 0)      //resets if prompt is false
        {
            seconds = 0;
            minute = 0;
            hour = 0;
        }
    }

    /**Resets the value of hour to 0 if 24 hours is reached (full day).
     * Only resets if used continously in a kind of loop.*/
    public boolean checkDay()
    {
        if(hour >= 24) {
            hour = 0;
            return true;
        }

        return false;
    }

    public int getHour()
    {
        return hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public void reaet()
    {
        minute = 0;
        seconds = 0;
    }
}
