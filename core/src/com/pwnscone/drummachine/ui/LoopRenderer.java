package com.pwnscone.drummachine.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Instrument;
import com.pwnscone.drummachine.Loop;
import com.pwnscone.drummachine.Synth;
import com.pwnscone.drummachine.Track;
import com.pwnscone.drummachine.actors.Actor;
import com.pwnscone.drummachine.actors.Ball;

public class LoopRenderer {
	private ShapeRenderer mShapeRenderer;
	private Loop mLoop;
	private Color mBackgroundColor;
	private float mTotalHeight;

	public LoopRenderer() {
		mTotalHeight = .125f;
		mBackgroundColor = new Color(0.0f, 0.0f, 0.0f, 0.5f);
	}

	public void render() {

		float height = mTotalHeight * View.INV_RATIO;

		// Render Background
		// Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
		// Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA,
		// Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
		// mShapeRenderer.begin(ShapeType.Filled);
		// mShapeRenderer.setColor(mBackgroundColor);
		// mShapeRenderer.rect(0, View.TOP - height, 1.0f, height);
		// mShapeRenderer.end();
		// Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

		mShapeRenderer.begin(ShapeType.Filled);
		ArrayList<Track> tracks = mLoop.getUsedTracks();
		int size = tracks.size();

		// Render Desired Notes
		int framesTotal = mLoop.getSteps() * mLoop.getStepSize();
		float denominator = 1.0f / framesTotal;
		float barHeight = height / size;
		float barWidth = (1.0f + 2.0f * mLoop.getTolerance()) * denominator;
		float swing = mLoop.getSwing() * denominator;
		for (int i = 0; i < size; i++) {
			Track track = tracks.get(i);
			boolean[] notes = track.mNotes;
			byte[] noteStatus = track.mNoteStatus;
			float length = notes.length;
			for (int j = 0; j < length; j++) {
				if (notes[j]) {
					float x = (j + 0.5f) / length + denominator * 0.5f;
					x += (j % 2 == 0 ? swing : -swing);
					float y = View.TOP - barHeight * i;
					mShapeRenderer.setColor(noteStatus[j] > 0 ? track.mColor : track.mColorDark);
					mShapeRenderer.rect(x - barWidth * 0.5f, y - barHeight, barWidth, barHeight);
				}
			}
		}
		// Cursor Position
		// TODO: Figure out why I have to add denominator to fix this off by
		// one...
		float xPos = (mLoop.getSummedSteps() + 1) * denominator + .5f / mLoop.getSteps();
		xPos -= Math.floor(xPos);

		mShapeRenderer.end();
		Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
		Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
		mShapeRenderer.begin(ShapeType.Filled);

		// Render History
		int now = Game.get().getLevel().getFrame();
		Color color;
		Actor selectedActor = InputManager.getSelectedActor();
		for (int i = 0; i < size; i++) {
			Track track = tracks.get(i);
			Instrument instrument = track.mInstrument;
			int[] history = instrument.getHistory();
			Actor[] actorHistory = instrument.getActorHistory();
			int historyIndex = instrument.getHistoryIndex();
			color = track.mColorLight;
			for (int j = 0; j < history.length; j++) {
				int index = (j + historyIndex) % Synth.HISTORY_LENGTH;
				int hist = history[index];
				Actor actor = actorHistory[index];
				if (hist >= 0) {
					int delta = (now - hist);
					if (delta < framesTotal) {
						float measure = delta * denominator;
						float x = xPos - measure;
						x -= (float) Math.floor(x);
						float y = View.TOP - barHeight * i;

						mShapeRenderer.setColor(color.r, color.g, color.b, Math.min(Math.max(0.0f,
								1.0f - 1.3f * measure), 1.0f));
						mShapeRenderer.rect(x, y - barHeight, denominator, barHeight);

						if (actor == selectedActor) {
							mShapeRenderer.setColor(1.0f, 1.0f, 1.0f, Math.min(Math.max(0.0f,
									2.2f - 3.0f * measure), 1.0f));
							mShapeRenderer.rect(x, y - barHeight, denominator, barHeight);
						}
					}
				}
			}
		}

		// Render Cursor
		for (int i = 0; i < Ball.TRAIL_LENGTH; i++) {
			float tint = 1.0f - i / (float) Ball.TRAIL_LENGTH;
			mShapeRenderer.setColor(tint, tint, tint, tint);
			xPos = (mLoop.getSummedSteps() + 1 - i) * denominator + .5f / mLoop.getSteps();
			xPos -= Math.floor(xPos);
			mShapeRenderer.rect(xPos, View.TOP - height, denominator, height);
		}

		/*
		 * float progress = mLoop.getProgress(); float channel = (2.0f +
		 * progress) / 3.0f; Color tempColor = Misc.cr0.set(channel, channel,
		 * channel, channel); mShapeRenderer.setColor(tempColor);
		 * mShapeRenderer.rect(0, View.TOP - height * 17.0f / 16.0f, progress,
		 * height / 16.0f);
		 */

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
