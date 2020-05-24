package com.mygdx.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**Called Pugi Pugi*/

public class Core extends Game
{
	public SpriteBatch batch;
	public Asset assetmanager;

	public Skin hudSkin, bubbleSkin;
	public SoundPOD sounds;
	public MusicPOD music;

	public void create()
	{
		System.out.println("Game is starting....");

		assetmanager = new Asset();
		batch = new SpriteBatch();

		bubbleSkin = assetmanager.manager.get("bubble/bubble.json");

		sounds = new SoundPOD(assetmanager);
		music = new MusicPOD(assetmanager);

		this.setScreen(new StartMenu(this));
	}

	public void render()
	{
		super.render();
	}

	public void dispose()
	{
		batch.dispose();
		sounds.dispose();
		music.dispose();

		if(assetmanager.firstLoaded)
			assetmanager.unload1stStage();
		if(assetmanager.thirdLoaded)
			assetmanager.unload3rdStage();
		if(assetmanager.fourthLoaded)
			assetmanager.unload4thStage();
		if(assetmanager.univAtlasLoaded)
			assetmanager.unloadGameAtlas();
		assetmanager.Dispose();
		bubbleSkin.dispose();

		if(this.getScreen() != null)
			this.getScreen().dispose();

		System.out.println("Game is fully disposed....");
	}
}