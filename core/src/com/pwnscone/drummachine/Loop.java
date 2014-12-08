package com.pwnscone.drummachine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class Loop {
	private float mProgress;
	private float mProgressDamper;

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
		mProgressDamper = 50.0f;
		mProgress = 0;
		mStep = 0;
		mStepSize = stepSize;
		mSubstep = -1;
		mSteps = steps;
		mSwing = swing;
		mSummedSteps = -1;
		mSwingDirection = 1;
		mTolerance = 1;
		mSoundEnabled = false;

		Synth synth = Game.get().getSynth();

		mHiHatClosed = new Track(synth.getHiHatClosed(), this, Color.RED);
		mHiHatOpen = new Track(synth.getHiHatOpen(), this, Color.GREEN);
		mSnare = new Track(synth.getSnare(), this, Color.BLUE);
		mKick = new Track(synth.getKick(), this, Color.ORANGE);

		mUsedTracks = new ArrayList<Track>();
	}

	public void update() {
		mSubstep++;
		mSummedSteps++;
		if (mSubstep == mStepSize - mSwingDirection * mSwing) {
			mSubstep = 0;
			mStep++;
			mSwingDirection *= -1;
			if (mStep == mSteps) {
				mStep -= mSteps;
				mSummedSteps = 0;
			}
		}
		if (mSubstep == 0) {
			for (int i = 0; i < mUsedTracks.size(); i++) {
				Track track = mUsedTracks.get(i);

				if (track.mNoteStatus[mStep] > 0) {
					track.mNoteStatus[mStep]--;
				}
				if (mSoundEnabled) {
					track.playNoteAt(mStep);
				}
			}
		}
		float total = 0;
		for (int i = 0; i < mUsedTracks.size(); i++) {
			total += mUsedTracks.get(i).getProgress();
		}
		mProgress = mProgress * mProgressDamper + total / mUsedTracks.size();
		mProgress /= mProgressDamper + 1;
	}

	private void addNote(int time, Track track) {
		time %= mSteps;
		track.addNote(time);
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

	public float getProgress() {
		return mProgress;
	}
}
