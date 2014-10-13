package com.pwnscone.drummachine.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.pwnscone.drummachine.Game;

public class AssetLoader {
	public static void loadAssets() {
		AssetManager am = Game.get().getAssetManager();

		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.Linear;
		param.genMipMaps = true;

		am.load("transformOverlay.png", Texture.class, param);

		am.finishLoading();
		am.get("transformOverlay.png", Texture.class).setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.MipMapLinearLinear);
	}
}
