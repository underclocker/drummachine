package com.pwnscone.drummachine;

import com.pwnscone.drummachine.actors.Actor;
import com.pwnscone.drummachine.ui.AssetLoader;

public class Instrument {
	private short[] mSamples;
	private int[] mHistory;
	private Actor[] mActorHistory;
	private int mHistoryIndex;
	private Track mTrack;

	public Instrument(String file) {
		mHistory = new int[Synth.HISTORY_LENGTH];
		mActorHistory = new Actor[Synth.HISTORY_LENGTH];
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

	public boolean play(Actor actor) {
		mActorHistory[mHistoryIndex % Synth.HISTORY_LENGTH] = actor;
		boolean result = false;
		if (mTrack != null) {
			result = mTrack.record(Game.get().getLevel().getFrame());
		}
		play();
		return result;
	}

	public int[] getHistory() {
		return mHistory;
	}

	public Actor[] getActorHistory() {
		return mActorHistory;
	}

	public int getHistoryIndex() {
		return mHistoryIndex;
	}

	public void clearHistory() {
		for (int i = 0; i < mHistory.length; i++) {
			mHistory[i] = -1;
			mActorHistory[i] = null;
			mHistoryIndex = 0;
		}
	}

	public void setTrack(Track track) {
		mTrack = track;
	}
}
