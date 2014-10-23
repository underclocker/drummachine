package com.pwnscone.drummachine.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pwnscone.drummachine.Loop;

public class LoopRenderer {
	private ShapeRenderer mShapeRenderer;
	private Loop mLoop;
	private Color mBackgroundColor;

	public LoopRenderer() {
		mBackgroundColor = new Color(0.2f, 0.2f, 0.2f, 2.0f);
	}

	public void render() {
		mShapeRenderer.begin(ShapeType.Filled);
		mShapeRenderer.setColor(mBackgroundColor);
		mShapeRenderer.rect(0, View.TOP - .1f, 1.0f, .1f);
		mShapeRenderer.end();
	}

	public void setLoop(Loop loop) {
		mLoop = loop;
	}

	public void setShapeRenderer(ShapeRenderer shapeRenderer) {
		mShapeRenderer = shapeRenderer;
	}
}
