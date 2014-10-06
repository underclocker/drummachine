package com.pwnscone.drummachine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

public class Game extends ApplicationAdapter {
	private Level mCurrentLevel;
	private View mView;
	private SceneInputProcessor mSceneInputProcessor;
	private InputMultiplexer mInputMultiplexer;

	private static Game game;

	public static Game get() {
		return game;
	};

	@Override
	public void create() {
		game = this;
		mCurrentLevel = new Level();
		mView = new View();
		mSceneInputProcessor = new SceneInputProcessor();
		mInputMultiplexer = new InputMultiplexer();
		mInputMultiplexer.addProcessor(mSceneInputProcessor);
		Gdx.input.setInputProcessor(mSceneInputProcessor);

		// Create after all main objects are instantiated.
		mSceneInputProcessor.create();
		mCurrentLevel.create();
	}

	@Override
	public void render() {
		mCurrentLevel.update();
		mView.update();
		mSceneInputProcessor.update();
	}

	public Level getLevel() {
		return mCurrentLevel;
	}

	public View getView() {
		return mView;
	}

	@Override
	public void resize(int width, int height) {
		mView.resetCamera();
	}
}
