package com.pwnscone.drummachine;

import com.badlogic.gdx.graphics.Color;
import com.pwnscone.drummachine.Loop.Drum;

public class Track {
	public Drum mDrum;
	public boolean[] mNotes;
	public Color mColor;

	public Track(Drum drum, int steps, Color color) {
		mDrum = drum;
		mNotes = new boolean[steps];
		mColor = color;
	}
}
