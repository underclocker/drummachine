package com.pwnscone.drummachine;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.pwnscone.drummachine.actors.Actor;

public class Synth {
	public static final int SAMPLING_RATE = 44100;
	public static final float BUFFER_TIME = .05f;
	public static final int HISTORY_LENGTH = 64;

	private static final int BUFFER_LENGTH = 10;

	public final int mBufferSamples;
	public int mTrack[];
	public int mIndex;
	public int mAheadIndex;

	private int mTargetAheadIndex;

	private Instrument mHiHatClosed;
	private Instrument mHiHatOpen;
	private Instrument mSnare;
	private Instrument mKick;
	private ArrayList<Instrument> mInstruments;

	public Synth() {
		mBufferSamples = SAMPLING_RATE * BUFFER_LENGTH;

		mTargetAheadIndex = (int) (SAMPLING_RATE * BUFFER_TIME);
		mAheadIndex = mTargetAheadIndex;
		mTrack = new int[mBufferSamples];

		mHiHatClosed = new Instrument("hiHat.raw");
		mHiHatOpen = new Instrument("hiHatOpen.raw");
		mSnare = new Instrument("snare.raw");
		mKick = new Instrument("kick.raw");

		mInstruments = new ArrayList<Instrument>();
		mInstruments.add(mHiHatClosed);
		mInstruments.add(mHiHatOpen);
		mInstruments.add(mSnare);
		mInstruments.add(mKick);
	}

	public void update() {
		mAheadIndex += SAMPLING_RATE * Gdx.graphics.getDeltaTime()
				* (1.0f + (mTargetAheadIndex - mAheadIndex) / (float) mTargetAheadIndex);
	}

	public void playClip(short[] clip) {
		int combinedIndex = mIndex + mAheadIndex;
		for (int i = 0; i < clip.length; i++) {
			mTrack[(i + combinedIndex) % mBufferSamples] += clip[i];
		}
	}

	public void clearHistory() {
		for (int i = 0; i < mInstruments.size(); i++) {
			mInstruments.get(i).clearHistory();
		}
	}

	public boolean hiHatClosed(Actor actor) {
		return mHiHatClosed.play(actor);
	}

	public boolean hiHatOpen(Actor actor) {
		return mHiHatOpen.play(actor);
	}

	public boolean snare(Actor actor) {
		return mSnare.play(actor);
	}

	public boolean kick(Actor actor) {
		return mKick.play(actor);
	}

	public Instrument getHiHatClosed() {
		return mHiHatClosed;
	}

	public Instrument getHiHatOpen() {
		return mHiHatOpen;
	}

	public Instrument getSnare() {
		return mSnare;
	}

	public Instrument getKick() {
		return mKick;
	}
}