package com.pwnscone.drummachine;

import com.badlogic.gdx.graphics.Color;

public class Track {
	public Instrument mInstrument;
	public boolean[] mNotes;
	public byte[] mNoteStatus;
	public int[] mExpandedNotes;
	public Color mColor;
	public Color mColorDark;
	public Color mColorLight;
	public int mTotalNotes;
	private Loop mLoop;

	public Track(Instrument instrument, Loop loop, Color color) {
		int steps = loop.getSteps();
		mLoop = loop;
		mInstrument = instrument;
		instrument.setTrack(this);
		mNotes = new boolean[steps];
		mNoteStatus = new byte[steps];
		mExpandedNotes = new int[steps * loop.getStepSize()];
		for (int i = 0; i < mExpandedNotes.length; i++) {
			mExpandedNotes[i] = -1;
		}
		mColor = color;
		mColorDark = color.cpy().lerp(Color.BLACK, 0.5f);
		mColorLight = color.cpy().lerp(Color.WHITE, 0.7f);
		mTotalNotes = 0;
	}

	public void addNote(int time) {
		if (!mNotes[time]) {
			mNotes[time] = true;
			mTotalNotes++;

			int tolerance = mLoop.getTolerance();
			int iterations = 1 + tolerance * 2;
			int swing = mLoop.getSwing();
			int stepSize = mLoop.getStepSize();
			for (int i = 0; i < iterations; i++) {
				int tick = (mExpandedNotes.length + stepSize * time - tolerance + i + swing
						* (1 - 2 * (time % 2)))
						% mExpandedNotes.length;
				mExpandedNotes[tick] = time;
			}
		}
	}

	public void playNoteAt(int step) {
		if (mNotes[step]) {
			mInstrument.play();
		}
	}

	public boolean record(int time) {
		time %= (mLoop.getSteps() * mLoop.getStepSize());
		int note = mExpandedNotes[time];
		if (note > -1) {
			mNoteStatus[note] = 2;
			return true;
		} else {
			for (int i = 0; i < mNoteStatus.length; i++) {
				mNoteStatus[i] = 0;
			}
			return false;
		}
	}
}
