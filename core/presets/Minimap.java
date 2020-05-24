package com.mygdx.core.presets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;

public final class Minimap
{
    /**mapCatcher catches the map so it gets a bounce effect when coming down*/
    private boolean minimapSet = false;
    private boolean targetVisible = true;

    final private Array<Image> targets = new Array<Image>(9);
    private Image current, select, padMenu;
    private int w, h = 0;
    private int angle = -90;
    private int cycle, currentCycle = 0;

    public Minimap(String file, Core core, World world)
    {
        padMenu = new Image((Texture)core.assetmanager.manager.get(file));
        padMenu.setPosition(-115, -30);
        padMenu.setSize(1, 1);
        padMenu.setRotation(angle);
    }

    public void setMapTargets(String targetImg, String selectImg, Core core, Stage stage)
    {
        stage.addActor(padMenu);

        for(int n = 0; n < 9; n++)
        {
            targets.add(new Image((Texture) core.assetmanager.manager.get(targetImg)));
            targets.get(n).setSize(20, 20);
            targets.get(n).setVisible(false);
            stage.addActor(targets.get(n));
        }
        setPos();
        current = new Image(((Texture)core.assetmanager.manager.get(selectImg)));
        current.setSize(20, 20);
        current.setPosition(-300, -300);
        select = new Image(((Texture)core.assetmanager.manager.get(selectImg)));
        select.setSize(20, 20);
        select.setPosition(-300, -300);
        stage.addActor(current);
        stage.addActor(select);

        setClickables();
        setPos();
    }

    private void setPos()
    {
        targets.get(0).setPosition(-20, -3);
        targets.get(1).setPosition(25 , 0);
        targets.get(2).setPosition(35, 30);
        targets.get(3).setPosition(-30, 55);
        targets.get(4).setPosition(75, 60);
        targets.get(5).setPosition(15, 85);
        targets.get(6).setPosition(-60, 84);
        targets.get(7).setPosition(-30, 122);
        targets.get(8).setPosition(70, 115);
    }

    private void reset()
    {
        if(!targetVisible)
        {
            for (Image targetItr : targets)
                targetItr.setVisible(false);
            targetVisible = true;
            select.setVisible(false);
            current.setVisible(false);
        }
    }

    private void allVisible()
    {
        if(targetVisible)
        {
            for (Image targetItr : targets)
                targetItr.setVisible(true);
            targetVisible = false;
        }
        current.setVisible(true);
        select.setVisible(true);
    }

    private void setClickables()
    {
        targets.get(0).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.setPosition(targets.get(0).getX(), targets.get(0).getY());
                //select[0] will be the where the player is, select[1] becomes the targeted teleporter
                cycle = 0;
                targetVisible = false;
            }
        });
        targets.get(1).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.setPosition(targets.get(1).getX(), targets.get(1).getY());
                //select[0] will be the where the player is, select[1] becomes the targeted teleporter
                cycle = 1;
                targetVisible = false;
            }
        });
        targets.get(2).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.setPosition(targets.get(2).getX(), targets.get(2).getY());
                //select[0] will be the where the player is, select[1] becomes the targeted teleporter
                cycle = 2;
                targetVisible = false;
            }
        });
        targets.get(3).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.setPosition(targets.get(3).getX(), targets.get(3).getY());
                //select[0] will be the where the player is, select[1] becomes the targeted teleporter
                cycle = 3;
                targetVisible = false;
            }
        });
        targets.get(4).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.setPosition(targets.get(4).getX(), targets.get(4).getY());
                //select[0] will be the where the player is, select[1] becomes the targeted teleporter
                cycle = 4;
                targetVisible = false;
            }
        });
        targets.get(5).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.setPosition(targets.get(5).getX(), targets.get(5).getY());
                //select[0] will be the where the player is, select[1] becomes the targeted teleporter
                cycle = 5;
                targetVisible = false;
            }
        });
        targets.get(6).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.setPosition(targets.get(6).getX(), targets.get(6).getY());
                //select[0] will be the where the player is, select[1] becomes the targeted teleporter
                cycle = 6;
                targetVisible = false;
            }
        });
        targets.get(7).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.setPosition(targets.get(7).getX(), targets.get(7).getY());
                //select[0] will be the where the player is, select[1] becomes the targeted teleporter
                cycle = 7;
                targetVisible = false;
            }
        });
        targets.get(8).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.setPosition(targets.get(8).getX(), targets.get(8).getY());
                //select[0] will be the where the player is, select[1] becomes the targeted teleporter
                cycle = 8;
                targetVisible = false;
            }
        });
    }

    public Array<Image> getTargets()
    {
        return targets;
    }

    public Image getSelect()
    {
        return select;
    }

    public Image getCurrent()
    {
        return current;
    }

    public int getCycle()
    {
        return cycle;
    }

    /**@param mapPrompt is from hud.miniMapPrompt() which is true when the map
     *        bubble is clicked by the user.
     * @param telePrompt is from hud.teleport() which is true when the user teleports.*/
    public void display(boolean mapPrompt, boolean telePrompt)
    {
        if(mapPrompt)
        {
            if(!minimapSet)
            {
                currentCycle = cycle;
                current.setPosition(targets.get(cycle).getX(), targets.get(cycle).getY());
                padMenu.setVisible(true);
                minimapSet = true;
            }

            if(w < 250)
                w += 10;
            if(h < 188)
                h += 10;

            if(angle != 0)
                angle += 5;
            else
                allVisible();
        }
        else if(!mapPrompt || telePrompt)
        {
            if(minimapSet)
            {
                currentCycle = cycle;
                if(angle != -90)
                    angle -= 5;

                reset();     //teleport markers are gone
                if(w > 1)
                {
                    w -= 10;
                }
                if(h > 1)
                {
                    h -= 10;
                }
                if(h <= 1 && w <= 1)
                {
                    padMenu.setVisible(false);
                    minimapSet = false;
                }
            }
        }

        padMenu.setRotation(angle);
        padMenu.setSize(w, h);
    }
}
