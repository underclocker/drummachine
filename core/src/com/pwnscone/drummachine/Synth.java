package com.pwnscone.drummachine;

import com.badlogic.gdx.Gdx;
import com.pwnscone.drummachine.ui.AssetLoader;

public class Synth {
	public static final int SAMPLING_RATE = 44100;

	private static float BUFFER_TIME = .05f;
	private static final int BUFFER_LENGTH = 10;

	public final int mBufferSamples;
	public int mTrack[];
	public int mIndex;
	public int mAheadIndex;

	private int mTargetAheadIndex;

	private short[] mHiHatClosed;
	private short[] mHiHatOpen;
	private short[] mSnare;
	private short[] mKick;

	public Synth() {
		mBufferSamples = SAMPLING_RATE * BUFFER_LENGTH;

		mTargetAheadIndex = (int) (SAMPLING_RATE * BUFFER_TIME);
		mAheadIndex = mTargetAheadIndex;
		mTrack = new int[mBufferSamples];
		mHiHatClosed = AssetLoader.loadSound("hiHat.raw");
		mHiHatOpen = AssetLoader.loadSound("hiHatOpen.raw");
		mSnare = AssetLoader.loadSound("snare.raw");
		mKick = AssetLoader.loadSound("kick.raw");
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

	public void hiHatClosed() {
		playClip(mHiHatClosed);
	}

	public void hiHatOpen() {
		playClip(mHiHatOpen);
	}

	public void snare() {
		playClip(mSnare);
	}

	public void kick() {
		playClip(mKick);
	}
}
