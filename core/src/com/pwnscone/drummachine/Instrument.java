package com.pwnscone.drummachine;

import com.pwnscone.drummachine.ui.AssetLoader;

public class Instrument {
	private short[] mSamples;
	private int[] mHistory;
	private int mHistoryIndex;

	public Instrument(String file) {
		mHistory = new int[Synth.HISTORY_LENGTH];
		for (int i = 0; i < mHistory.length; i++) {
			mHistory[i] = -1;
		}
		mHistoryIndex = 0;
		mSamples = AssetLoader.loadSound(file);
	}

	public void play() {
		mHistory[mHistoryIndex++ % Synth.HISTORY_LENGTH] = Game.get().getLevel().getFrame();
		Game.get().getSynth().playClip(mSamples);
	}

	public int[] getHistory() {
		return mHistory;
	}

	public int getHistoryIndex() {
		return mHistoryIndex;
	}
}
