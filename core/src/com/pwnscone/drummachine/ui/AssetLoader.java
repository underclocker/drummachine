package com.pwnscone.drummachine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
import com.pwnscone.drummachine.Game;

public class AssetLoader {
	public static void loadAssets() {
		AssetManager am = Game.get().getAssetManager();

		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.Linear;
		param.genMipMaps = false;

		am.load("spawner.png", Texture.class, param);
		am.load("ball.png", Texture.class, param);
		am.load("kick.png", Texture.class, param);
		am.load("kickWhite.png", Texture.class, param);
		am.load("snare.png", Texture.class, param);
		am.load("snareWhite.png", Texture.class, param);
		am.load("hiHatClosed.png", Texture.class, param);
		am.load("hiHatClosedWhite.png", Texture.class, param);
		am.load("translateOverlay.png", Texture.class, param);
		am.load("rotateOverlay.png", Texture.class, param);
		am.load("particle.png", Texture.class, param);

		am.finishLoading();

		Array<Texture> textures = new Array<Texture>(100);
		am.getAll(Texture.class, textures);
		for (int i = 0; i < textures.size; i++) {
			// textures.get(i).setFilter(TextureFilter.MipMapLinearLinear,
			// TextureFilter.MipMapLinearLinear);
		}
	}

	public static short[] loadSound(String file) {
		byte[] bytes = Gdx.files.internal(file).readBytes();
		int length = bytes.length / 2;
		short[] shorts = new short[length];
		for (int i = 0; i < length; i++) {
			shorts[i] = (short) ((bytes[i * 2] & 0xff) | (bytes[i * 2 + 1] << 8));
		}
		return shorts;
	}
}
