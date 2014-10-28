package com.pwnscone.drummachine;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.pwnscone.drummachine.ui.ActorInputProcessor;
import com.pwnscone.drummachine.ui.AssetLoader;
import com.pwnscone.drummachine.ui.SceneInputProcessor;
import com.pwnscone.drummachine.ui.View;
import com.pwnscone.drummachine.util.Starter;

public class Game extends ApplicationAdapter {
	private Level mCurrentLevel;
	private View mView;
	private SceneInputProcessor mSceneInputProcessor;
	private ActorInputProcessor mActorInputProcessor;
	private InputMultiplexer mInputMultiplexer;
	private AssetManager mAssetManager;
	private Synth mSynth;

	public static boolean MOBILE;
	public static Starter STARTER;
	public static boolean RUNNING = true;
	public static float FRAME_RATE = 60.0f;

	private static Game game;

	public static Game get() {
		return game;
	};

	@Override
	public void create() {
		game = this;
		MOBILE = Gdx.app.getType() == ApplicationType.Android
				|| Gdx.app.getType() == ApplicationType.iOS;

		mSynth = new Synth();

		mAssetManager = new AssetManager();
		AssetLoader.loadAssets();

		mCurrentLevel = new Level();
		mView = new View();
		mSceneInputProcessor = new SceneInputProcessor();
		mActorInputProcessor = new ActorInputProcessor();
		mInputMultiplexer = new InputMultiplexer();
		mInputMultiplexer.addProcessor(mActorInputProcessor);
		mInputMultiplexer.addProcessor(mSceneInputProcessor);
		Gdx.input.setInputProcessor(mInputMultiplexer);

		// Create after all main objects are instantiated.
		mView.create();
		mActorInputProcessor.create();
		mSceneInputProcessor.create();
		mCurrentLevel.create();

		if (STARTER != null) {
			STARTER.startProcess();
		}
	}

	@Override
	public void pause() {
		RUNNING = false;
	}

	@Override
	public void resume() {
		RUNNING = true;
	}

	@Override
	public void render() {
		mCurrentLevel.update();
		mView.update();
		mActorInputProcessor.update();
		mSceneInputProcessor.update();
		mSynth.update();
	}

	public Level getLevel() {
		return mCurrentLevel;
	}

	public View getView() {
		return mView;
	}

	public AssetManager getAssetManager() {
		return mAssetManager;
	}

	public Synth getSynth() {
		return mSynth;
	}

	@Override
	public void resize(int width, int height) {
		mView.resetCamera();
	}
}
