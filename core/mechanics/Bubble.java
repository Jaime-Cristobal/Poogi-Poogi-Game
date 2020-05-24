package com.mygdx.core.mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.core.Core;

public final class Bubble
{
    private final Image bubblePop;
    private float w, h = 1;
    private boolean reachSize, loop = false;

    public Bubble(String fileImg, Core core)
    {
        bubblePop = new Image((Texture) core.assetmanager.getFile(fileImg));
        bubblePop.setSize(1, 1);
    }

    /**Should be used to be passed to Stage.addActor(..).
     * Can also be used to add a listener by calling addListener(new ...).*/
    public Image getImage()
    {
        return bubblePop;
    }

    /**@param listener could either be passed with a new ClickListener as
     *                 a parameter or create a new object explicitly.*/
    public void addClickEvent(ClickListener listener)
    {
        bubblePop.addListener(listener);
    }

    /**This is designed for popup warnings for low stat values such as health.
     * NOT TESTED YET
     * @param stat player stats such as health. Create a separate variable for this
     *             and reference it through this function.
     * @param value the value the particular stat has to be for it to popup.*/
    public void popup(int stat, int value)
    {
        if(stat <= value)
        {
            //The bubble goes from (width, height) -> (0, 1) -> (32, 41)
            //it is 41 for height since the +4 will put the value above 38
            //reachSize indicates it has reached the max size or full scale resolution
            if(w < 32 && !reachSize)
                w += 4;
            if(h < 38 && !reachSize)
                h += 4;
            if(w >= 32 && h >= 38)
                reachSize = true;

            //all this does is makes the bubble go from large to small to large loop
            if(reachSize)
            {
                //goes smaller
                if(loop)
                {
                    if (w >= 21)
                        w -= 0.2f;
                    if (h >= 32)
                        h -= 0.2f;
                }
                if(h <= 32 && w <= 38)
                    loop = false;

                //goes larger
                if(!loop)
                {
                    if(w <= 32)
                        w += 0.2f;
                    if(h <= 38)
                        h += 0.2f;
                    if(w >= 32 && h >= 38)
                        loop = true;
                }
            }

            bubblePop.setPosition(-15, 27);
        }
        else    //bubble gets smaller movement and then placed somewhere on the edge
        {
            reachSize = false;
            if(w > 0 && h > 0)
            {
                w -= 4;
                h -= 4;
            }
            else
                bubblePop.setPosition(-600, 0);
        }

        bubblePop.setSize(w, h);
    }

    /**This handles the mining bubble pop-up on the screen.
     * */
    public void imageInOut(boolean condition, float x, float y, float delta)
    {
        if(condition)
        {
            //The bubble goes from (width, height) -> (0, 1) -> (32, 41)
            //it is 41 for height since the +4 will put the value above 38
            //reachSize indicates it has reached the max size or full scale resolution
            if(delta != 0)
            {
                if (w < bubblePop.getImageWidth() && !reachSize)
                    w += 4;
                if (h < bubblePop.getImageHeight() && !reachSize)
                    h += 4;
            }
            if(w >= bubblePop.getImageWidth() && h >= bubblePop.getImageHeight())
                reachSize = true;

            //all this does is makes the bubble go from large to small to large loop
            if(reachSize && delta != 0)
            {
                //goes smaller
                if(loop)
                {
                    if (w >= bubblePop.getImageWidth() - 10)
                        w -= 0.2f;
                    if (h >= bubblePop.getImageHeight() - 10)
                        h -= 0.2f;
                }
                if(w <= bubblePop.getImageWidth() && h <= bubblePop.getImageHeight())
                    loop = false;

                //goes larger
                if(!loop)
                {
                    if(w <= bubblePop.getImageWidth())
                        w += 0.2f;
                    if(h <= bubblePop.getImageHeight())
                        h += 0.2f;
                    if(w >= bubblePop.getImageWidth() && h >= bubblePop.getImageHeight())
                        loop = true;
                }
            }

            //the exit bubble replaces the mining bubble
            bubblePop.setPosition(x, y);
        }
        else    //bubble gets smaller movement and then placed somewhere on the edge
        {
            reachSize = false;
            if(w > 0 && h > 0)
            {
                w -= 4;
                h -= 4;
            }
            else
                bubblePop.setPosition(-600, 0);
        }

        bubblePop.setSize(w, h);
    }

    /**This handles the mining bubble pop-up on the screen.
     * */
    public void popupPrompt(float x, float y, Vector2 playerPos, Vector2 telePos, float delta)
    {
        //if(EventManager.conditions(idA, idB))
        if(playerPos.x > telePos.x - 3 && playerPos.x < telePos.x + 3
                && playerPos.y > telePos.y - 3 && playerPos.y < telePos.y + 3)
        {
            //The bubble goes from (width, height) -> (0, 1) -> (32, 41)
            //it is 41 for height since the +4 will put the value above 38
            //reachSize indicates it has reached the max size or full scale resolution
            if(delta != 0)
            {
                if (w < 32 && !reachSize)
                    w += 4;
                if (h < 38 && !reachSize)
                    h += 4;
            }
            if(w >= 32 && h >= 38)
                reachSize = true;

            //all this does is makes the bubble go from large to small to large loop
            if(reachSize && delta != 0)
            {
                //goes smaller
                if(loop)
                {
                    if (w >= 21)
                        w -= 0.2f;
                    if (h >= 32)
                        h -= 0.2f;
                }
                if(h <= 32 && w <= 38)
                    loop = false;

                //goes larger
                if(!loop)
                {
                    if(w <= 32)
                        w += 0.2f;
                    if(h <= 38)
                        h += 0.2f;
                    if(w >= 32 && h >= 38)
                        loop = true;
                }
            }

            //the exit bubble replaces the mining bubble
            bubblePop.setPosition(x, y);
        }
        else    //bubble gets smaller movement and then placed somewhere on the edge
        {
            reachSize = false;
            if(w > 0 && h > 0)
            {
                w -= 4;
                h -= 4;
            }
            else
                bubblePop.setPosition(-600, 0);
        }

        bubblePop.setSize(w, h);
    }

    /**This handles the mining bubble pop-up on the screen.
     * @param exitBubble is the exit prompt to be projected inside the bubble.
     * */
    public void popupPromptExit(Image exitBubble, boolean exit, float x, float y, Vector2 playerPos, Vector2 telePos, float delta)
    {
        //if(EventManager.conditions(idA, idB))
        if(playerPos.x > telePos.x - 3 && playerPos.x < telePos.x + 3
                && playerPos.y > telePos.y - 3 && playerPos.y < telePos.y + 3)
        {
            //The bubble goes from (width, height) -> (0, 1) -> (32, 41)
            //it is 41 for height since the +4 will put the value above 38
            //reachSize indicates it has reached the max size or full scale resolution
            if(delta != 0)
            {
                if (w < 32 && !reachSize)
                    w += 4;
                if (h < 38 && !reachSize)
                    h += 4;
            }
            if(w >= 32 && h >= 38)
                reachSize = true;

            //all this does is makes the bubble go from large to small to large loop
            if(reachSize && delta != 0)
            {
                //goes smaller
                if(loop)
                {
                    if (w >= 21)
                        w -= 0.2f;
                    if (h >= 32)
                        h -= 0.2f;
                }
                if(h <= 32 && w <= 38)
                    loop = false;

                //goes larger
                if(!loop)
                {
                    if(w <= 32)
                        w += 0.2f;
                    if(h <= 38)
                        h += 0.2f;
                    if(w >= 32 && h >= 38)
                        loop = true;
                }
            }

            //the exit bubble replaces the mining bubble
            if(!exit)
                bubblePop.setPosition(x, y);
            else {
                exitBubble.setPosition(x, y);
                bubblePop.setPosition(-600, 0);
            }
        }
        else    //bubble gets smaller movement and then placed somewhere on the edge
        {
            reachSize = false;
            if(w > 0 && h > 0)
            {
                w -= 4;
                h -= 4;
            }
            else {
                bubblePop.setPosition(-600, 0);
                exit = false;
                exitBubble.setPosition(-600, 0);
            }
        }

        //dictates whether the mining or exit bubble is moving on the screen
        if(!exit)
            bubblePop.setSize(w, h);
        else
            exitBubble.setSize(w, h);
    }

    public boolean ifTouch()
    {
        return reachSize;
    }
}
