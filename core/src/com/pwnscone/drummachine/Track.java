package com.pwnscone.drummachine;

import com.pwnscone.drummachine.Loop.Drum;

public class Track {
	public Drum mDrum;
	public boolean[] mNotes;

	public Track(Drum drum, int steps) {
		mDrum = drum;
		mNotes = new boolean[steps];
	}
}
