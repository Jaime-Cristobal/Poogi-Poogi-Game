package com.mygdx.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Random;

public class StartMenu implements Screen
{
    final private Core core;

    private ExtendViewport viewport;
    private Stage stage;
    private Table menuTable;
    private Array<Button> button = new Array<Button>();
    private Array<Button> subButton = new Array<Button>();
    private Array<Label> label = new Array<Label>();
    private Array<Image> title = new Array<Image>();
    private float titleScale = 1, adder = 0.1f;
    private float splashScale = 2.0f;
    private Random randomizer = new Random();
    private ImageButton musicButton;

    private boolean justStarted = true;
    private boolean toPlay = false, toCredits = false, toLicense = false;
    private Image splash;

    public StartMenu(Core core)
    {
        this.core = core;

        viewport = new ExtendViewport(400, 600);
        stage = new Stage(viewport);
        menuTable = new Table();

        //background
        Array<String> mapList = new Array<String>();
        mapList.add("menu_back1");
        mapList.add("menu_back2");
        mapList.add("menu_back3");
        mapList.add("menu_back4");
        mapList.add("menu_back5");

        title.add(new Image(core.assetmanager.getTextureFromAtlas("menu_back.atlas", mapList.get(randomizer.nextInt(mapList.size)))));
        title.get(0).setPosition(title.get(0).getX(), title.get(0).getY());
        title.add(new Image(core.assetmanager.getTextureFromAtlas("menu_back.atlas", "title_slant")));
        title.get(1).setPosition(40, 400);
        title.add(new Image(core.assetmanager.getTextureFromAtlas("menu_back.atlas", "title")));
        title.get(2).setPosition(230, 360);

        for(int n = 0; n < 3; n++)
            stage.addActor(title.get(n));

        musicButton = new ImageButton(core.bubbleSkin, "musicOption");
        musicButton.setTransform(true);
        musicButton.setPosition(170, 85);
        musicButton.setScale(0.25f);
        musicButton.setChecked(core.music.musicOff);
        stage.addActor(musicButton);

        button.add(new Button(core.bubbleSkin, "red"));
        button.add(new Button(core.bubbleSkin, "red"));
        button.add(new Button(core.bubbleSkin, "red"));

        for(Button iter : button)
        {
            iter.setTransform(true);
            iter.setScale(0.40f);
            stage.addActor(iter);
        }

        button.get(0).setPosition(125, 275);
        button.get(1).setPosition(125, 205);
        button.get(2).setPosition(125, 135);

        subButton.add(new Button(core.bubbleSkin, "red"));
        subButton.add(new Button(core.bubbleSkin, "red"));
        subButton.add(new Button(core.bubbleSkin, "red"));

        for(Button iter : subButton)
        {
            iter.setTransform(true);
            iter.setScale(0.40f);
            iter.setVisible(false);
            stage.addActor(iter);
        }

        subButton.get(0).setPosition(125, 275);
        subButton.get(1).setPosition(125, 205);
        subButton.get(2).setPosition(125, 135);

        String font = "Chewy-Regular.ttf";
        label.add(new Label("Play",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font), Color.WHITE)));
        label.get(0).setPosition(177, 295);
        label.add(new Label("More",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font), Color.WHITE)));
        label.get(1).setPosition(177, 225);
        label.add(new Label("Quit",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font), Color.WHITE)));
        label.get(2).setPosition(177, 155);

        //more sub labels for buttons
        label.add(new Label("Credits",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font), Color.WHITE)));
        label.get(3).setPosition(175, 295);
        label.get(3).setVisible(false);
        label.add(new Label("License",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font), Color.WHITE)));
        label.get(4).setPosition(173, 225);
        label.get(4).setVisible(false);
        label.add(new Label("Back",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile(font), Color.WHITE)));
        label.get(5).setPosition(173, 155);
        label.get(5).setVisible(false);

        for(int n = 0; n < label.size; n++)
        {
            label.get(n).setTouchable(Touchable.disabled);
            stage.addActor(label.get(n));
        }

        splash = new Image(core.assetmanager.getTextureFromAtlas("misc.atlas", "splash"));
        splash.setPosition(-300, -300);
        splash.setScale(splashScale);
        stage.addActor(splash);

        buttonFunc();

        Gdx.input.setInputProcessor(stage);
    }

    /** Called when this screen becomes the current screen for a {@link Game}. */
    public void show ()
    {
        if(!core.music.ifGummaPlay())
        {
            core.music.playGumma();
        }
        EggCount.doorAmount = 0;
        EggCount.eggAmount = 0;

        menuTable.setPosition(0, -180);

        menuTable.setFillParent(true);
        stage.addActor(menuTable);
        //menuTable.add(button.get(0)).fillX().uniformX().center().right().pad(20, 0, 20, 0);
        //menuTable.add(button.get(1)).fillX().uniformX().center().pad(20, 0, 20, 0);

        core.batch.enableBlending();
    }

    private void buttonFunc()
    {
        //FOR MUSIC OPTION
        musicButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                core.music.musicOff = !core.music.musicOff;
                musicButton.setChecked(core.music.musicOff);
            }
        });

        button.get(0).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                toPlay = true;
            }
        });

        //to sub menu
        button.get(1).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();

                for(int i = 0; i < button.size; i++)
                    button.get(i).setVisible(false);

                for(int n = 0; n < label.size; n++)
                {
                    if(n < 3)
                        label.get(n).setVisible(false);
                    else
                        label.get(n).setVisible(true);
                }

                for(int j = 0; j < subButton.size; j++)
                    subButton.get(j).setVisible(true);
            }
        });

        //quit
        button.get(2).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                Gdx.app.exit();
            }
        });

        //credits
        subButton.get(0).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                toCredits = true;
            }
        });

        //license
        subButton.get(1).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                toLicense = true;
            }
        });

        //back to main menu
        subButton.get(2).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                for(int i = 0; i < button.size; i++)
                    button.get(i).setVisible(true);

                for(int n = 0; n < label.size; n++)
                {
                    if(n < 3)
                        label.get(n).setVisible(true);
                    else
                        label.get(n).setVisible(false);
                }

                for(int j = 0; j < subButton.size; j++)
                    subButton.get(j).setVisible(false);
            }
        });
    }

    private void toPlayMap()
    {
        core.music.stopGumma();
        EggCount.eggAmount = 10;
        EggCount.totalEggs = EggCount.eggAmount;
        EggCount.doorAmount = 0;
        EggCount.maxDoors = 10;
        EggCount.previousEggs = EggCount.eggAmount;
        EggCount.previousTotalEgg = EggCount.totalEggs;
        EggCount.previousDoors = EggCount.doorAmount;
        EggCount.deaths = 0;
        GameStat.pumpkinDemon = false;
        GameStat.pumpkinCat = false;
        GameStat.treeGift = false;
        core.setScreen(new LoadScreen(core, 1));
        dispose();
    }

    private void toCreditsPage()
    {
        core.setScreen(new Credits(core));
        dispose();
    }

    private void toLicensePage()
    {
        core.setScreen(new LicenseScreen(core));
        dispose();
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta)
    {
        Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        core.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.getCamera().update();

        if(core.music.musicOff)
            core.music.pauseGumma();
        else
        {
            if (!core.music.ifGummaPlay())
                core.music.playGumma();
        }

        if(justStarted)
        {
            if (splashScale >= 0f)
                splashScale -= 0.03f;
            else
                justStarted = false;
            splash.setScale(splashScale);
        }
        else
        {
            if (toPlay || toCredits || toLicense)
            {
                if (splashScale <= 2.0f)
                    splashScale += 0.03f;
                else
                {
                    if(toCredits)
                        toCreditsPage();
                    else if(toLicense)
                        toLicensePage();
                    else
                        toPlayMap();
                }
                splash.setScale(splashScale);
            }
            /*
            else if (toCredits) {
                if (splashScale <= 2.0f)
                    splashScale += 0.03f;
                else
                    toCreditsPage();
                splash.setScale(splashScale);
            }*/

            if (titleScale >= 1)
                adder = -0.001f;
            else if (titleScale <= 0.95)
                adder = 0.001f;
            titleScale += adder;
            title.get(1).setScale(titleScale);
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /** @see ApplicationListener#resize(int, int) */
    public void resize (int width, int height)
    {
        viewport.update(width, height, false);
        //stage.getCamera().viewportWidth = width / Scaler.scaleY;
        //stage.getCamera().viewportHeight = height / Scaler.scaleY;
    }

    /** @see ApplicationListener#pause() */
    public void pause ()
    {

    }

    /** @see ApplicationListener#resume() */
    public void resume ()
    {

    }

    /** Called when this screen is no longer the current screen for a {@link Game}. */
    public void hide ()
    {

    }

    /** Called when this screen should release all resources. */
    public void dispose ()
    {
        stage.dispose();

        Gdx.app.log("StartMenu.java", "disposed successfully");
    }
}
