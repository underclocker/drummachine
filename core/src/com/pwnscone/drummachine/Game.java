package com.pwnscone.drummachine;

import com.badlogic.gdx.ApplicationAdapter;

public class Game extends ApplicationAdapter {
	private Level mCurrentLevel;
	private View mView;

	private static Game game;

	public static Game get() {
		return game;
	};

	@Override
	public void create() {
		game = this;
		mCurrentLevel = new Level();
		mView = new View();
	}

	@Override
	public void render() {
		mCurrentLevel.update();
		mView.update();
	}

	public Level getLevel() {
		return mCurrentLevel;
	}
}
