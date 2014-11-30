package com.pwnscone.drummachine.android;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Synth;
import com.pwnscone.drummachine.util.Starter;

public class AndroidLauncher extends AndroidApplication implements Starter {
	private AudioTrack audioTrack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.r = 8;
		config.g = 8;
		config.b = 8;
		config.a = 8;
		initialize(new Game(), config);
		Game.STARTER = this;
	}

	@Override
	public void startProcess() {
		Thread thread = new Thread() {
			public void run() {
				setPriority(Thread.MAX_PRIORITY);

				int bufferSize = AudioTrack.getMinBufferSize(Synth.SAMPLING_RATE,
						AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

				audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Synth.SAMPLING_RATE,
						AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize,
						AudioTrack.MODE_STREAM);

				bufferSize = 35;

				short samples[] = new short[bufferSize];

				audioTrack.play();

				Synth synth = Game.get().getSynth();
				while (true) {
					if (Game.RUNNING) {
						synchronized (synth) {
							for (int i = 0; i < bufferSize; i++) {
								int id = (synth.mIndex + i) % synth.mBufferSamples;
								samples[i] = (short) Math.min(32767, Math.max(-32768,
										synth.mTrack[id]));
								synth.mTrack[id] = 0;
							}
							synth.mAheadIndex -= bufferSize;
							synth.mIndex += bufferSize;
							synth.mIndex = synth.mIndex % synth.mBufferSamples;
						}
						audioTrack.write(samples, 0, bufferSize);
					} else {
						try {
							Thread.sleep(25);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		thread.start();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}
}
