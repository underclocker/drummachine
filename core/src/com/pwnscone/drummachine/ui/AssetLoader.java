package com.pwnscone.drummachine.ui;

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
		param.genMipMaps = true;

		am.load("translateOverlay.png", Texture.class, param);
		am.load("rotateOverlay.png", Texture.class, param);

		am.finishLoading();

		Array<Texture> textures = new Array<Texture>(100);
		am.getAll(Texture.class, textures);
		for (int i = 0; i < textures.size; i++) {
			textures.get(i).setFilter(TextureFilter.MipMapLinearLinear,
					TextureFilter.MipMapLinearLinear);
		}
	}
}
