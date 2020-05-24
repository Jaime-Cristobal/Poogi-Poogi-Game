package com.mygdx.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class MusicPOD
{
    private Music horror, gumma;

    private int currentIndex = -1;
    private Array<Music> firstStage = new Array<Music>(),
                         thirdStage = new Array<Music>(),
                         fourthStage = new Array<Music>();

    public boolean musicOff = false;

    public MusicPOD(Asset assetmanager)
    {
        gumma = (Music) assetmanager.getFile("Gunma-chanGambol.ogg");
        gumma.setLooping(true);

        horror = (Music) assetmanager.getFile("Rising.ogg");
    }

    /**BEGIN 1st STAGE BLOCK 1st STAGE BLOCK 1st STAGE BLOCK 1st STAGE BLOCK 1st STAGE BLOCK BEGIN*/
    public void createFirstStage(Asset assetmanager)
    {
        firstStage.add((Music) assetmanager.getFile("1st stage/Music/Children's March Theme.ogg"));
        firstStage.add((Music) assetmanager.getFile("1st stage/Music/one_0.ogg"));
        firstStage.add((Music) assetmanager.getFile("1st stage/Music/Runaround.ogg"));

        for(int n = 0; n < firstStage.size; n++)
            firstStage.get(n).setLooping(false);
        firstStage.get(0).setVolume(0.8f);
        firstStage.get(2).setVolume(0.6f);
    }

    public void disposeFirstStage()
    {
        for(int n = 0; n < firstStage.size; n++)
        {
            if(firstStage.get(n) != null)
            {
                firstStage.get(n).stop();
                firstStage.get(n).dispose();
            }
        }
        firstStage.clear();
    }

    public void playFirstStageRandom()
    {
        currentIndex = MathUtils.random(0, firstStage.size - 1);
        firstStage.get(currentIndex).play();
    }

    public void playFirstStage(int index, float volume)
    {
        firstStage.get(index).play();
        firstStage.get(index).setVolume(volume);
    }

    public void stopFirstStage()
    {
        for(int n = 0; n < firstStage.size; n++)
            firstStage.get(n).stop();
    }

    public void setFirstStageVolume(int index, float volume)
    {
        firstStage.get(index).setVolume(volume);
    }

    public void pauseFirstCurrent()
    {
        if(currentIndex < 0)
        {
            Gdx.app.error("Cannot pause first current music", "current index has not yet beeen initialize!");
            return;
        }
        firstStage.get(currentIndex).pause();
    }

    public void playFirstCurrent()
    {
        if(currentIndex < 0)
        {
            Gdx.app.error("Cannot play first current music", "current index has not yet beeen initialize!");
            return;
        }
        firstStage.get(currentIndex).play();
    }

    public boolean isFirstCurrentPlaying()
    {
        return firstStage.get(currentIndex).isPlaying();
    }
    /**END 1st STAGE BLOCK 1st STAGE BLOCK 1st STAGE BLOCK 1st STAGE BLOCK 1st STAGE BLOCK END*/

    /***/
    public void createThirdStage(Asset assetmanager)
    {
        thirdStage.add((Music) assetmanager.getFile("thirdStage/Music/Armadillo.ogg"));
        thirdStage.add((Music) assetmanager.getFile("thirdStage/Music/banana_track.ogg"));
        thirdStage.add((Music) assetmanager.getFile("thirdStage/Music/OsanpoRag.ogg"));

        thirdStage.get(0).setVolume(0.6f);
        thirdStage.get(1).setVolume(0.2f);
        thirdStage.get(2).setVolume(0.6f);
    }

    public void disposeThirdStage()
    {
        for(int n = 0; n < thirdStage.size; n++)
        {
            if(thirdStage.get(n) != null)
            {
                thirdStage.get(n).stop();
                thirdStage.get(n).dispose();
            }
        }
        thirdStage.clear();
    }

    public void playThirdStageRandom()
    {
        currentIndex = MathUtils.random(0, thirdStage.size - 1);
        thirdStage.get(currentIndex).play();
    }

    public void playThirdStage(int index, float volume)
    {
        thirdStage.get(index).play();
        thirdStage.get(index).setVolume(volume);
    }

    public void stopThirdStage()
    {
        for(int n = 0; n < thirdStage.size; n++)
            thirdStage.get(n).stop();
    }

    public void setThirdStageVolume(int index, float volume)
    {
        thirdStage.get(index).setVolume(volume);
    }

    public void pauseAllThird()
    {
        for(int n = 0; n < thirdStage.size; n++)
            if(thirdStage.get(n).isPlaying())
                thirdStage.get(n).pause();
    }

    public void pauseThirdCurrent()
    {
        if(currentIndex < 0)
        {
            Gdx.app.error("Cannot pause thirdStage current music", "current index has not yet beeen initialize!");
            return;
        }
        thirdStage.get(currentIndex).pause();
    }

    public void playThirdCurrent()
    {
        if(currentIndex < 0)
        {
            Gdx.app.error("Cannot play thirdStage current music", "current index has not yet beeen initialize!");
            return;
        }
        thirdStage.get(currentIndex).play();
    }

    /************END 3rd STAGE BLOCK*******************************/

    public void createFourthStage(Asset assetmanager)
    {
        fourthStage.add((Music) assetmanager.getFile("fourthStage/Music/Happy town-02.ogg"));
        fourthStage.add((Music) assetmanager.getFile("fourthStage/Music/In Tranquil Spring.ogg"));
        fourthStage.add((Music) assetmanager.getFile("fourthStage/Music/spring day.ogg"));

        //fourthStage.get(0).setVolume(0.6f);
        fourthStage.get(1).setVolume(0.6f);
        //fourthStage.get(3).setVolume(0.6f);

        //wind = (Music) assetmanager.getFile("wind1.wav");
        //wind.play();
        //wind.setLooping(true);
    }

    public void disposeFourthStage()
    {
        for(int n = 0; n < fourthStage.size; n++)
        {
            if(fourthStage.get(n) != null)
            {
                fourthStage.get(n).stop();
                fourthStage.get(n).dispose();
            }
        }
        fourthStage.clear();
    }

    public void playFourthStageRandom()
    {
        currentIndex = MathUtils.random(0, fourthStage.size - 1);
        fourthStage.get(currentIndex).play();
    }

    public void playFourthStage(int index, float volume)
    {
        fourthStage.get(index).play();
        fourthStage.get(index).setVolume(volume);
    }

    public void stopFourthStage()
    {
        for(int n = 0; n < fourthStage.size; n++)
            fourthStage.get(n).stop();
        //wind.stop();
    }

    public void setFourthVolume(int index, float volume)
    {
        fourthStage.get(index).setVolume(volume);
    }

    public void pauseAllFourth()
    {
        for(int n = 0; n < fourthStage.size; n++)
            if(fourthStage.get(n).isPlaying())
                fourthStage.get(n).pause();
    }

    public void pauseFourthCurrent()
    {
        if(currentIndex < 0)
        {
            Gdx.app.error("Cannot pause thirdStage current music", "current index has not yet beeen initialize!");
            return;
        }
        fourthStage.get(currentIndex).pause();
    }

    public void playFourthCurrent()
    {
        if(currentIndex < 0)
        {
            Gdx.app.error("Cannot play thirdStage current music", "current index has not yet beeen initialize!");
            return;
        }
        fourthStage.get(currentIndex).play();
    }

    public int getCurrentIndex()
    {
        return currentIndex;
    }

    public boolean isThirdCurrentPlaying()
    {
        return thirdStage.get(currentIndex).isPlaying();
    }

    public boolean isFourthCurrentPlaying()
    {
        return fourthStage.get(currentIndex).isPlaying();
    }
    /***/

    public void playGumma()
    {
        gumma.play();
    }

    public void playGumma(float volume)
    {
        gumma.setVolume(volume);
        gumma.play();
    }

    public void pauseGumma()
    {
        gumma.pause();
    }

    public boolean ifGummaPlay()
    {
        return gumma.isPlaying();
    }

    public void stopGumma()
    {
        gumma.stop();
    }

    public void playHorror()
    {
        horror.play();
    }

    public void playHorror(float volume)
    {
        horror.setVolume(volume);
        horror.play();
    }

    public void pauseHorror()
    {
        horror.pause();
    }

    public boolean ifHorrorPlay()
    {
        return horror.isPlaying();
    }

    public void stopHorror()
    {
        horror.stop();
    }

    public void dispose()
    {
        horror.dispose();
        gumma.dispose();
        disposeFirstStage();
        disposeThirdStage();
    }
}
