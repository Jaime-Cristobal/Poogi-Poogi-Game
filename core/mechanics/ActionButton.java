package com.mygdx.core.mechanics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.core.Core;
import com.mygdx.core.EggCount;
import com.mygdx.core.MusicPOD;
import com.mygdx.core.actor.creation.CreateActor;
import com.mygdx.core.mechanics.playermech.PlayerMech;

public final class ActionButton
{
    private final Button button;
    private int lastIndex = 0, index = 0, timer = 0, maxTime = 0;
    private int size = 0;
    private boolean reachMax = false, click = false;
    private Vector2 playerPos = null;
    private float direction = 1;
    private int subEgg = 2;
    private boolean timerOn = true;
    private boolean actionOn = false;
    private float rate = 0, maxRate = 0.4f;

    //icon data
    private Label amount;

    /**A skin JSON file uploaded will be necessary for this to work.
     * See the Skin Composer by Raeleus for more info at
     * https://github.com/raeleus/skin-composer
     * @param styleName -> the corresponding image that will be shown as
     *                     the button.*/
    public ActionButton(Core core, String styleName, float x, float y, float scale)
    {
        button = new Button(core.bubbleSkin, styleName);
        button.setTransform(true);
        button.setScale(scale);
        button.setPosition(x, y);
    }

    public void setMaxRate(float maxRate)
    {
        this.maxRate = maxRate;
    }

    public void setTimerOn(boolean option)
    {
        timerOn = option;
    }

    public ActionButton(Core core, String styleName, float width, float height, float x, float y)
    {
        button = new ImageButton(core.hudSkin, styleName);
        button.setPosition(x, y);
        button.setSize(width, height);
    }

    /**Send to a stage for button to be rendered on the screen*/
    public Button getButton()
    {
        return button;
    }

    /**
     * @param spawner is the projectile to be set a listener with.
     * @param maxTime is the amount of time until the cooldown is finished
     *
     * The lambda clicked(....) is the listener set to cause the projectile to
     * be fired on the screen.
     *
     * The index indicates which of projectiles are being fired (with spawner.getSize() the max).*/
    public void setAction(final Core core, final PlayerMech spawner, final int maxTime) {
        this.maxTime = maxTime;
        size = spawner.getSize() - 1;

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!reachMax && EggCount.eggAmount - subEgg >= 0 && actionOn)
                {
                    actionOn = false;
                    EggCount.eggAmount -= subEgg;

                    if(!core.music.musicOff)
                        core.sounds.spawn.play(0.2f);
                    spawner.setSpawn(playerPos.x, playerPos.y, index, direction);

                    lastIndex = index;
                    if (index < size)
                        index++;
                    else
                        reachMax = true;

                    click = true;
                }
            }
        });
    }

    public void setEggSubtractor(int val)
    {
        subEgg = val;
    }

    public void setAction(final Core core, final PlayerMech spawner, final int maxTime, final float actionLimit) {
        this.maxTime = maxTime;
        size = spawner.getSize() - 1;

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!reachMax && index <= actionLimit && actionOn)
                {
                    actionOn = false;
                    lastIndex = index;
                    if (index < size)
                        index++;
                    else
                        reachMax = true;

                    if(!core.music.musicOff)
                    {
                        core.sounds.laser5.stop();
                        core.sounds.laser5.play(1f);
                    }
                    spawner.setSpawn(playerPos.x, playerPos.y, index, direction);

                    click = true;
                }
            }
        });
    }

    public void setDirection(float val)
    {
        direction = val;
    }

    public void setTracker(Core core, float x, float y)
    {
        amount = new Label("" + size + 1, new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("Chewy-Regular.ttf"), Color.WHITE));
        amount.setTouchable(Touchable.disabled);
        amount.setPosition(x, y);
    }

    public Label getAmountLabel()
    {
        return amount;
    }

    /**
     * */
    public void update(float delta, Vector2 playerPos)
    {
        this.playerPos = playerPos;
        rate += delta;
        if(rate >= 0.4f)
        {
            actionOn = true;
            rate = 0;
        }

        if(amount != null)
        {
            if (!reachMax)
                amount.setText("" + ((size + 1) - index));
            else
                amount.setText("0");
        }

        if(timerOn)
            indexTimer();
        else
        {
            if(index > size)
                resetIndex();
        }
    }

    private void indexTimer()
    {
        if(reachMax || lastIndex != index)
        {
            timer++;
            if (timer >= maxTime)
                resetIndex();
        }
    }

    /**Useful for game mechanics such as items removing the current cooldown*/
    public void resetIndex()
    {
        index = 0;
        timer = 0;
        reachMax = false;
    }

    public boolean getClickVal()
    {
        return click;
    }

    public void resetClickVal()
    {
        click = false;
    }

    public int getIndex()
    {
        return index;
    }

    public int maxSize()
    {
        return size;
    }
}
