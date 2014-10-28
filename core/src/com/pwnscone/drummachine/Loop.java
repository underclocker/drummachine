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
	private int mTolerance;
	private int mSummedSteps;
	private boolean mSoundEnabled;

	private Track mHiHatClosed;
	private Track mHiHatOpen;
	private Track mSnare;
	private Track mKick;

	private ArrayList<Track> mUsedTracks;

	public Loop() {
		this(32, 8, 0);
	}

	public Loop(int stepSize, int steps, int swing) {
		mStep = 0;
		mStepSize = stepSize;
		mSubstep = 0;
		mSteps = steps;
		mSwing = swing;
		mSummedSteps = 0;
		mSwingDirection = 1;
		mTolerance = 1;
		mSoundEnabled = false;

		Synth synth = Game.get().getSynth();

		mHiHatClosed = new Track(synth.getHiHatClosed(), steps, Color.RED);
		mHiHatOpen = new Track(synth.getHiHatOpen(), steps, Color.GREEN);
		mSnare = new Track(synth.getSnare(), steps, Color.BLUE);
		mKick = new Track(synth.getKick(), steps, Color.ORANGE);

		mUsedTracks = new ArrayList<Track>();
	}

	public void update() {
		if (mSubstep == mStepSize - mSwingDirection * mSwing) {
			mSubstep = 0;
			mStep++;
			mSwingDirection *= -1;
			if (mStep == mSteps) {
				mStep -= mSteps;
				mSummedSteps = 0;
			}
		}
		if (mSubstep == 0 && mSoundEnabled) {
			Synth synth = Game.get().getSynth();
			for (int i = 0; i < mUsedTracks.size(); i++) {
				mUsedTracks.get(i).playNoteAt(mStep);
			}
		}
		mSubstep++;
		mSummedSteps++;
	}

	private void addNote(int time, Track track) {
		time %= mSteps;
		track.mNotes[time] = true;
		if (!mUsedTracks.contains(track)) {
			mUsedTracks.add(track);
		}
	}

	public void addHiHatClosed(int time) {
		addNote(time, mHiHatClosed);
	}

	public void addHiHatOpen(int time) {
		addNote(time, mHiHatOpen);
	}

	public void addSnare(int time) {
		addNote(time, mSnare);
	}

	public void addKick(int time) {
		addNote(time, mKick);
	}

	public int getSteps() {
		return mSteps;
	}

	public int getStepSize() {
		return mStepSize;
	}

	public int getTolerance() {
		return mTolerance;
	}

	public int getSwing() {
		return mSwing;
	}

	public int getSummedSteps() {
		return mSummedSteps;
	}

	public ArrayList<Track> getUsedTracks() {
		return mUsedTracks;
	}
}
