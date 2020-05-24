package com.mygdx.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.core.Stages.Intermission;

public class UnloadScreen implements Screen
{
    final private Core core;
    final private ExtendViewport viewport = new ExtendViewport(400, 600);
    final private Stage stage = new Stage(viewport);
    private boolean toMenu = false;
    private int mapStage = 0;
    private boolean toIntermission = false;

    /**@param mapStage should be the current map state/level*/
    public UnloadScreen(Core core, boolean toMenu, int mapStage, boolean toIntermission)
    {
        this.core = core;
        this.toMenu = toMenu;
        this.mapStage = mapStage;
        this.toIntermission = toIntermission;
    }

    /** Called when this screen becomes the current screen for a {@link Game}. */
    public void show ()
    {
        core.assetmanager.loadMenu();
        System.out.println("to unload");

        if(!toIntermission)
        {
            switch (mapStage)
            {
                case 1:
                    core.music.disposeFirstStage();
                    core.assetmanager.unload1stStage();
                    break;
                case 2:
                    core.assetmanager.unload2ndStage();
                    break;
                case 3:
                    core.music.disposeThirdStage();
                    core.assetmanager.unload3rdStage();
                    break;
                case 4:
                    System.out.println("dispose 4th");
                    core.music.disposeFourthStage();
                    core.assetmanager.unload4thStage();
                    break;
                default:
                    break;
            }
        }
    }

    private void toAddScreen()
    {
        core.setScreen(new AddScreen(core, mapStage));
        dispose();
    }

    private void toMenuScreen()
    {
        if(core.assetmanager.univAtlasLoaded)
            core.assetmanager.unloadGameAtlas();
        core.setScreen(new StartMenu(core));
        dispose();
    }

    private void toIntermission()
    {
        core.setScreen(new Intermission(core, mapStage));
        dispose();
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta)
    {
        if(core.assetmanager.manager.update((int)delta))
        {
            if(toMenu)
                toMenuScreen();
            else if (toIntermission)
                toIntermission();
            else
                toAddScreen();
        }

        Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /** @see ApplicationListener#resize(int, int) */
    public void resize (int width, int height)
    {
        viewport.update(width, height, false);
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
    }
}
