package com.pwnscone.drummachine.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Misc {
	public static final float RAD_TO_DEG = (float) (180.0 / Math.PI);
	public static final float DEG_TO_RAD = (float) (Math.PI / 180.0);
	public static final float TAU = ((float) Math.PI * 2.0f);

	public static Vector2 v2r0 = new Vector2();
	public static Vector2 v2r1 = new Vector2();

	public static Vector3 v3r0 = new Vector3();
	public static Vector3 v3r1 = new Vector3();

	private static float[] randoms;
	private static int randIndex;

	public static float random() {
		if (randIndex == 4096)
			randIndex = 0;
		return randoms[randIndex++];
	}

	static {
		randoms = new float[4096];
		for (int i = 0; i < 4096; i++) {
			randoms[i] = (float) Math.random();
		}
	}

	public static void scaleArray(float[] array, float scale) {
		for (int i = 0; i < array.length; i++) {
			array[i] *= scale;
		}
	}
}
