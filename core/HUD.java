package com.mygdx.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.core.mechanics.Bubble;
import com.mygdx.core.mechanics.GameTime;
import com.mygdx.core.presets.StatBar;

/**1560 x 720*/

public final class HUD
{
    final private Stage stage;
    final private Table menuTab;

    final private Button resume, menuBtn;
    final private Image splash, eggImg, doorImg, irrigator;
    final private Button menu;
    private float menuY = 600;
    final private GameTime gameTime = new GameTime();

    private OrthographicCamera camera;
    private ExtendViewport viewport;

    private boolean exit, menuOn, mapOn, teleOn = false;

    final private Label minute, eggCount, lost, resumeText, menuText, doorCount;
    private int timer = 0, eggs = 0;
    private float lostScale = 0.1f, textScale = 0.1f, menuScale = 0.1f,
                    splashScale = 0.1f, splashAdder = 0.1f;
    private boolean descale = true;

    private int projectileNum = -1;
    private boolean fireProjectile = false;
    private boolean mapGeneration = false;
    private boolean timerOn = true;
    private int startMinute = 59;   //59 -> 40(5 doors) -> 30 (10 doors) -> 20 (20 doors)

    public HUD(final Core core)
    {
        gameTime.setTime(59, startMinute, 0);
        //This allows the hud to follow player
        camera = new OrthographicCamera(400, 600);
        viewport = new ExtendViewport(400, 600, camera);

        menu = new Button(core.bubbleSkin, "pause_green");
        menu.setTransform(true);
        menu.setScale(0.25f);
        menu.setPosition(150, -285);
        //menu.setSize(30, 30);

        resume = new Button(core.bubbleSkin, "green");
        resume.setTransform(true);
        resume.setScale(0.1f);
        resume.setPosition(-65, 10);
        resume.setVisible(false);

        menuBtn = new Button(core.bubbleSkin, "red");
        menuBtn.setTransform(true);
        menuBtn.setScale(0.1f);
        menuBtn.setPosition(-65, -80);
        menuBtn.setVisible(false);

        splash = new Image(core.assetmanager.getTextureFromAtlas("assets.atlas", "pause"));
        splash.setPosition(-85, 100);
        splash.setScale(0.6f);
        splash.setVisible(false);

        eggImg = new Image(core.assetmanager.getTextureFromAtlas("assets.atlas", "chicken_egg"));
        eggImg.setScale(0.5f);
        eggImg.setPosition(132, -152);

        doorImg = new Image(core.assetmanager.getTextureFromAtlas("assets.atlas", "door"));
        doorImg.setScale(0.5f);
        doorImg.setPosition(112, -136);

        irrigator = new Image(core.assetmanager.getTextureFromAtlas("assets.atlas", "irrigator"));
        irrigator.setScale(0.5f);
        irrigator.setPosition(155, -60);
        irrigator.setVisible(false);

        //stars = new Image(core.assetmanager.getTexture("pop_stars.png"));
        //stars.setPosition(-200, -200);

        stage = new Stage(viewport);
        stage.getViewport().getCamera().position.setZero();
        //stage.getViewport().getCamera().position.set(0, -35, 0);
        //stage.getViewport().setCamera(camera);

        String font = "Chewy-Regular.ttf";
        minute = new Label(" 59", new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font),
                Color.WHITE));
        minute.setPosition(155, -95);

        eggCount = new Label(":0", new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font),
                Color.WHITE));
        eggCount.setPosition(150, -157);

        doorCount = new Label(":0", new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font),
                Color.WHITE));
        doorCount.setPosition(150, -124);

        lost = new Label("Game Over", new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font),
                Color.RED));
        lost.setPosition(-35, 20);
        lost.setFontScale(0.1f);
        lost.setVisible(false);

        resumeText = new Label("Resume", new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font),
                Color.WHITE));
        resumeText.setPosition(-20, 30);
        resumeText.setTouchable(Touchable.disabled);
        resumeText.setFontScale(0.1f);
        resumeText.setVisible(false);

        menuText = new Label("Menu", new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font),
                Color.WHITE));
        menuText.setPosition(-15, -60);
        menuText.setTouchable(Touchable.disabled);
        menuText.setFontScale(0.1f);
        menuText.setVisible(false);

        menuTab = new Table();
        menuTab.setTransform(true);
        menuTab.setFillParent(true);

        //stage.addActor(menuTab);
        stage.addActor(splash);
        //stage.addActor(stars);
        stage.addActor(menu);
        stage.addActor(minute);
        stage.addActor(doorImg);
        stage.addActor(doorCount);
        stage.addActor(eggImg);
        stage.addActor(eggCount);
        stage.addActor(lost);
        stage.addActor(resume);
        stage.addActor(menuBtn);
        stage.addActor(resumeText);
        stage.addActor(menuText);
        stage.addActor(irrigator);

        //menuTab.setPosition(-195, 600);
        //menuTab.add(resume).uniformX().center().pad(0, -500, -200, -500);
        //menuTab.add(menuBtn).uniformX().center().pad(0, -500, -375, -500);

        menu.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                menuOn = true;
            }
        });

        resume.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                menuOn = false;
            }
        });
    }

    public void addActor(Actor actor)
    {
        stage.addActor(actor);
    }

    public boolean getProjectileAction()
    {
        return fireProjectile;
    }

    public void resetProjectileAction()
    {
        fireProjectile = false;
    }

    public int getProjectileNum()
    {
        return projectileNum;
    }

    public void render(float delta, boolean ifLose)
    {
        if(!ifLose)
        {
            if (delta != 0) {
                //core.batch.setProjectionMatrix(stage.getCamera().combined);           //This allows the hud to follow player

                if (timerOn)
                {
                    if(gameTime.getMinute() < 5)
                        minute.setColor(Color.RED);
                    else
                        minute.setColor(Color.WHITE);

                    gameTime.timerBackwards();

                    if (gameTime.getMinute() > 9)
                        minute.setText(gameTime.getMinute());
                    else
                        minute.setText("0" + gameTime.getMinute());

                    eggCount.setText(":" + EggCount.eggAmount);
                    doorCount.setText(":" + EggCount.doorAmount);
                }
            }
        }
        else
        {
            if(!lost.isVisible())
            {
                if(gameTime.getMinute() <= 0)
                {
                    lost.setText("Game Over: Out of time");
                    lost.setPosition(-100, 20);
                }
                lost.setVisible(true);
            }
            if(lostScale <= 1)
            {
                lostScale += 0.1;
                lost.setFontScale(lostScale);
            }
        }

        menuPopUp();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void resetTimer()
    {
        gameTime.setTime(59, startMinute, 0);
    }

    public void setStartMinute(int val)
    {
        startMinute = val;
        gameTime.setTime(59, startMinute, 0);
    }

    public int getMinute()
    {
        return startMinute;
    }

    public int getCurrentMinute()
    {
        return gameTime.getMinute();
    }

    public boolean ifOutofTime()
    {
        return gameTime.getMinute() <= 0;
    }

    public void stopTimer()
    {
        timerOn = false;
    }

    public void startTimer()
    {
        timerOn = true;
    }

    public void setMapGeneration(boolean val)
    {
        mapGeneration = val;
    }

    public void setIrrigatorDisplay(boolean select)
    {
        irrigator.setVisible(select);
    }

    public boolean getIrrigatorDisplay()
    {
        return irrigator.isVisible();
    }

    /**
    private void setActionPts()
    {
        if(energy.getAmount() < 100)
        {
            if(timer >= 300)
                energy.addValue(0.3f);
            else
                timer++;
        }
        else
        {
            timer = 0;
        }
    }

    public void addActionPts(int value)
    {
        if(value > 0 && energy.getAmount() < 100)
        {
            if(energy.getAmount() + value > 100)
                energy.resetValue();
            else
                energy.addValue(value);
        }
    }

    public void subActionsPts(int value)
    {
        if(value > 0 && energy.getAmount() > 0)
        {
            if(energy.getAmount() - value <= 1) //no energy
                energy.addValue(-1 * energy.getAmount());
            else    //energy is not depleted
            {
                energy.addValue(-1 * value);
                timer = 0;      //energy regeneration is reseted
            }
        }
    }

    public float getActionPoints()
    {
        return energy.getAmount();
    }
     */

    private void menuPopUp()
    {
        if(!menuOn)
        {
            if(!descale)
                descale = true;
            if(splashScale > 0)
            {
                splashScale -= 0.1f;
                splash.setScale(splashScale);
            }
            else
            {
                if(splash.isVisible())
                    splash.setVisible(false);
            }

            if (textScale > 0)
            {
                textScale -= 0.1f;
                resumeText.setFontScale(textScale);
                menuText.setFontScale(textScale);
            }
            else
            {
                if(resumeText.isVisible() || menuText.isVisible())
                {
                    resumeText.setVisible(false);
                    menuText.setVisible(false);
                }
            }

            if (menuScale > 0)
            {
                menuScale -= 0.1f;
                resume.setScale(menuScale);
                menuBtn.setScale(menuScale);
            }
            else
            {
                if(resume.isVisible() || menuBtn.isVisible())
                {
                    resume.setVisible(false);
                    menuBtn.setVisible(false);
                }
            }
        }
        else
        {
            if(!menuBtn.isVisible())
            {
                menuText.setVisible(true);
                menuBtn.setVisible(true);
            }
            if(!resume.isVisible())
            {
                resumeText.setVisible(true);
                resume.setVisible(true);
            }
            if(!splash.isVisible())
            {
                splash.setVisible(true);
            }

            if(textScale <= 1)
            {
                textScale += 0.1f;
                resumeText.setFontScale(textScale);
                menuText.setFontScale(textScale);
            }
            if(menuScale <= 0.35f)
            {
                menuScale += 0.1f;
                resume.setScale(menuScale);
                menuBtn.setScale(menuScale);
            }
            if(splashScale <= 0.5f && descale)
            {
                splashScale += 0.1f;
                splash.setScale(splashScale);
            }
            else
            {
                if(descale)
                    descale = false;
            }
            if(!descale)
            {
                if (splashScale <= 0.55f)
                    splashAdder = 0.001f;
                else if(splashScale >= 0.6f)
                    splashAdder = -0.001f;
                splashScale += splashAdder;
                splash.setScale(splashScale);
            }
        }
    }

    public void update(int width, int height)
    {
        stage.getViewport().update(width, height);
        /**
        if(height >= 1100)
        {
            minute.setPosition(minute.getX(), minute.getY() + 100);
            seconds.setPosition(seconds.getX(), seconds.getY() + 100);
            doorCount.setPosition(doorCount.getX(), doorCount.getY() + 90);
            eggCount.setPosition(eggCount.getX(), eggCount.getY() + 80);
            menu.setPosition(menu.getX(), menu.getY() + 90);
        }*/

        //stage.getCamera().viewportWidth = width / 2;
        //stage.getCamera().viewportHeight = height / 2;

        //stage.getCamera().viewportWidth = width / Scaler.scaleY;
        //stage.getCamera().viewportHeight = height / Scaler.scaleY;
        //stage.getViewport().update((int)(width / Scaler.scaleY), (int)(height / Scaler.scaleY));
    }

    /**exit = true indicates user clicked the teleport prompt*/
    public boolean teleport()
    {
        return teleOn;
    }

    public boolean miniMapPrompt()
    {
        return mapOn;
    }

    public void resetTeleportVal()
    {
        teleOn = false;
        mapOn = false;
        exit = false;
    }

    public Button getMenuBtn()
    {
        return menuBtn;
    }

    public boolean getExit()
    {
        return exit;
    }

    public boolean isMenuOn()
    {
        return menuOn;
    }

    public Stage getStage()
    {
        return stage;
    }

    public void dispose()
    {
        stage.dispose();
    }

    public Viewport getViewport()
    {
        return viewport;
    }
}
