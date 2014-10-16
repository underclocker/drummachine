package com.pwnscone.drummachine;

import com.badlogic.gdx.Gdx;
import com.pwnscone.drummachine.ui.AssetLoader;

public class Synth {
	public static final int SAMPLING_RATE = 44100;

	private static float BUFFER_TIME = .05f;
	private static final int BUFFER_LENGTH = 10;

	public final int bufferSamples;
	public int track[];
	public int index;
	public int aheadIndex;

	private int targetAheadIndex;

	private short[] hiHat;

	public Synth() {
		bufferSamples = SAMPLING_RATE * BUFFER_LENGTH;

		targetAheadIndex = (int) (SAMPLING_RATE * BUFFER_TIME);
		aheadIndex = targetAheadIndex;
		track = new int[bufferSamples];
		hiHat = AssetLoader.loadSound("hiHat.raw");

	}

	public void update() {
		aheadIndex += SAMPLING_RATE * Gdx.graphics.getDeltaTime()
				* (1.0f + (targetAheadIndex - aheadIndex) / (float) targetAheadIndex);
	}

	public void hiHat() {
		int combinedIndex = index + aheadIndex;
		for (int i = 0; i < hiHat.length; i++) {
			track[(i + combinedIndex) % bufferSamples] += hiHat[i];
		}
	}
}
