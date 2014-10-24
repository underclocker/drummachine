package com.pwnscone.drummachine.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pwnscone.drummachine.Loop;
import com.pwnscone.drummachine.Track;

public class LoopRenderer {
	private ShapeRenderer mShapeRenderer;
	private Loop mLoop;
	private Color mBackgroundColor;

	public LoopRenderer() {
		mBackgroundColor = new Color(0.2f, 0.2f, 0.2f, 1.0f);
	}

	public void render() {
		mShapeRenderer.begin(ShapeType.Filled);
		mShapeRenderer.setColor(mBackgroundColor);
		ArrayList<Track> mTracks = mLoop.getUsedTracks();
		int size = mTracks.size();
		mShapeRenderer.rect(0, View.TOP - .1f * size, 1.0f, .1f * size);
		for (int i = 0; i < size; i++) {
			Track track = mTracks.get(i);
			mShapeRenderer.setColor(track.mColor);
			boolean[] notes = track.mNotes;
			float length = notes.length;
			for (int j = 0; j < length; j++) {
				if (notes[j]) {
					float x = (j + 0.5f) / length;
					float y = View.TOP - .1f * i;
					mShapeRenderer.rect(x - .005f, y - .1f, .01f, .1f);
				}
			}
		}
		mShapeRenderer.end();
	}

	public void setLoop(Loop loop) {
		mLoop = loop;
	}

	public void setShapeRenderer(ShapeRenderer shapeRenderer) {
		mShapeRenderer = shapeRenderer;
	}
}
