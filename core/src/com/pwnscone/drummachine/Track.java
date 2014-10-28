package com.pwnscone.drummachine;

import com.badlogic.gdx.graphics.Color;

public class Track {
	public Instrument mInstrument;
	public boolean[] mNotes;
	public Color mColor;

	public Track(Instrument instrument, int steps, Color color) {
		mInstrument = instrument;
		mNotes = new boolean[steps];
		mColor = color;
	}

	public void playNoteAt(int step) {
		if (mNotes[step]) {
			mInstrument.play();
		}
	}
}
