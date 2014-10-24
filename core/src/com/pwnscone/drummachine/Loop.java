package com.pwnscone.drummachine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class Loop {
	private int mStepSize;
	private int mStep;
	private int mSubstep;
	private int mSteps;
	private int mSwing;
	private int mSwingDirection;
	private boolean mSoundEnabled;

	private Track mHiHatClosed;
	private Track mHiHatOpen;
	private Track mSnare;
	private Track mKick;

	private ArrayList<Track> mUsedTracks;

	public enum Drum {
		HI_HAT_CLOSED, HI_HAT_OPEN, SNARE, KICK
	}

	public Loop() {
		this(32, 8, 0);
	}

	public Loop(int stepSize, int steps, int swing) {
		mStep = 0;
		mStepSize = stepSize;
		mSubstep = 0;
		mSteps = steps;
		mSwing = swing;
		mSwingDirection = 1;
		mSoundEnabled = true;

		mHiHatClosed = new Track(Drum.HI_HAT_CLOSED, steps, Color.RED);
		mHiHatOpen = new Track(Drum.HI_HAT_OPEN, steps, Color.GREEN);
		mSnare = new Track(Drum.SNARE, steps, Color.BLUE);
		mKick = new Track(Drum.KICK, steps, Color.ORANGE);

		mUsedTracks = new ArrayList<Track>();
	}

	public void update() {
		if (mSubstep == mStepSize + mSwingDirection * mSwing) {
			mSubstep = 0;
			mStep++;
			mSwingDirection *= -1;
			if (mStep == mSteps) {
				mStep -= mSteps;
			}
		}
		if (mSubstep == 0 && mSoundEnabled) {
			Synth synth = Game.get().getSynth();
			if (mHiHatClosed.mNotes[mStep]) {
				synth.hiHatClosed();
			}
			if (mHiHatOpen.mNotes[mStep]) {
				synth.hiHatOpen();
			}
			if (mSnare.mNotes[mStep]) {
				synth.snare();
			}
			if (mKick.mNotes[mStep]) {
				synth.kick();
			}
		}
		mSubstep++;
	}

	public void addNote(Drum drum, int time) {
		time %= mSteps;
		if (drum == Drum.HI_HAT_CLOSED) {
			mHiHatClosed.mNotes[time] = true;
			if (!mUsedTracks.contains(mHiHatClosed)) {
				mUsedTracks.add(mHiHatClosed);
			}
		} else if (drum == Drum.HI_HAT_OPEN) {
			mHiHatOpen.mNotes[time] = true;
			if (!mUsedTracks.contains(mHiHatOpen)) {
				mUsedTracks.add(mHiHatOpen);
			}
		} else if (drum == Drum.SNARE) {
			mSnare.mNotes[time] = true;
			if (!mUsedTracks.contains(mSnare)) {
				mUsedTracks.add(mSnare);
			}
		} else if (drum == Drum.KICK) {
			mKick.mNotes[time] = true;
			if (!mUsedTracks.contains(mKick)) {
				mUsedTracks.add(mKick);
			}
		}
	}

	public int getSteps() {
		return mSteps;
	}

	public ArrayList<Track> getUsedTracks() {
		return mUsedTracks;
	}
}
