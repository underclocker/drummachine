package com.pwnscone.drummachine;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.pwnscone.drummachine.ui.ActorInputProcessor;
import com.pwnscone.drummachine.ui.SceneInputProcessor;
import com.pwnscone.drummachine.ui.View;

public class Game extends ApplicationAdapter {
	private Level mCurrentLevel;
	private View mView;
	private SceneInputProcessor mSceneInputProcessor;
	private ActorInputProcessor mActorInputProcessor;
	private InputMultiplexer mInputMultiplexer;

	public static boolean MOBILE;
	private static Game game;

	public static Game get() {
		return game;
	};

	@Override
	public void create() {
		game = this;
		MOBILE = Gdx.app.getType() == ApplicationType.Android
				|| Gdx.app.getType() == ApplicationType.iOS;
		mCurrentLevel = new Level();
		mView = new View();
		mSceneInputProcessor = new SceneInputProcessor();
		mActorInputProcessor = new ActorInputProcessor();
		mInputMultiplexer = new InputMultiplexer();
		mInputMultiplexer.addProcessor(mActorInputProcessor);
		mInputMultiplexer.addProcessor(mSceneInputProcessor);
		Gdx.input.setInputProcessor(mInputMultiplexer);

		// Create after all main objects are instantiated.
		mActorInputProcessor.create();
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
