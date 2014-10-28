package com.pwnscone.drummachine.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Loop;
import com.pwnscone.drummachine.Synth;
import com.pwnscone.drummachine.Track;

public class LoopRenderer {
	private ShapeRenderer mShapeRenderer;
	private Loop mLoop;
	private Color mBackgroundColor;
	private float mTotalHeight;

	public LoopRenderer() {
		mTotalHeight = .25f;
		mBackgroundColor = new Color(0.2f, 0.2f, 0.2f, 1.0f);
	}

	public void render() {
		mShapeRenderer.begin(ShapeType.Filled);
		mShapeRenderer.setColor(mBackgroundColor);
		ArrayList<Track> mTracks = mLoop.getUsedTracks();
		int size = mTracks.size();
		// mShapeRenderer.rect(0, View.TOP - .1f * size, 1.0f, .1f * size);

		// Render Desired Notes
		int framesTotal = mLoop.getSteps() * mLoop.getStepSize();
		float denominator = 1.0f / framesTotal;
		float barHeight = mTotalHeight / size;
		float barWidth = (1.0f + 2.0f * mLoop.getTolerance()) * denominator;
		float swing = mLoop.getSwing() * 0.5f * denominator;
		for (int i = 0; i < size; i++) {
			Track track = mTracks.get(i);
			mShapeRenderer.setColor(track.mColor);
			boolean[] notes = track.mNotes;
			float length = notes.length;
			for (int j = 0; j < length; j++) {
				if (notes[j]) {
					float x = (j + 0.5f) / length;
					x += (j % 2 == 0 ? swing : -swing);
					float y = View.TOP - barHeight * i;
					mShapeRenderer.rect(x - barWidth * 0.5f, y - barHeight, barWidth, barHeight);
				}
			}
		}
		// Cursor Position
		float xPos = mLoop.getSummedSteps() * denominator + .5f / mLoop.getSteps() + swing;
		// xPos -= Game.FRAME_RATE * Synth.BUFFER_TIME * denominator;
		xPos -= Math.floor(xPos);

		mShapeRenderer.end();
		Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
		Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
		mShapeRenderer.begin(ShapeType.Filled);

		// Render History
		int now = Game.get().getLevel().getFrame();
		for (int i = 0; i < size; i++) {
			Track track = mTracks.get(i);
			int[] history = track.mInstrument.getHistory();
			int historyIndex = track.mInstrument.getHistoryIndex();
			for (int j = 0; j < history.length; j++) {
				int hist = history[(j + historyIndex) % Synth.HISTORY_LENGTH];
				if (hist >= 0) {
					int delta = (now - hist);
					if (delta < framesTotal) {
						float measure = delta * denominator;
						float x = xPos - measure;
						x -= (float) Math.floor(x);
						float y = View.TOP - barHeight * i;
						mShapeRenderer.setColor(1.0f, 1.0f, 1.0f, Math.min(Math.max(0.0f,
								1.0f - 2.0f * measure), 1.0f));
						mShapeRenderer.rect(x - 0.5f * denominator, y - barHeight, denominator,
								barHeight);
					}
				}
			}
		}

		// Render Cursor
		mShapeRenderer.setColor(Color.LIGHT_GRAY);
		mShapeRenderer.rect(xPos - denominator * 0.5f, View.TOP - mTotalHeight, denominator,
				mTotalHeight);

		mShapeRenderer.end();
		Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
	}

	public void setLoop(Loop loop) {
		mLoop = loop;
	}

	public void setShapeRenderer(ShapeRenderer shapeRenderer) {
		mShapeRenderer = shapeRenderer;
	}
}
