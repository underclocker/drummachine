package com.pwnscone.drummachine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.actors.Actor;
import com.pwnscone.drummachine.util.Misc;

public class View {
	public static float WIDTH = 9.0f;
	public static float HEIGHT = 16.0f;
	public static float UI_SCALE = 1.0f;
	public static float DENSITY;

	private static boolean RENDER_DEBUG = true;

	private OrthographicCamera mCamera;
	private SpriteBatch mSpriteBatch;
	private Box2DDebugRenderer mDebugRenderer;

	private Texture mTranslateOverlay;
	private Texture mRotateOverlay;

	public View() {
		mCamera = new OrthographicCamera();
		mSpriteBatch = new SpriteBatch();
		mDebugRenderer = new Box2DDebugRenderer();

		DENSITY = Gdx.graphics.getDensity();

		resetCamera();
	}

	public void create() {
		AssetManager assetManager = Game.get().getAssetManager();
		mTranslateOverlay = assetManager.get("translateOverlay.png", Texture.class);
		mRotateOverlay = assetManager.get("rotateOverlay.png", Texture.class);
	}

	public void update() {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		World world = Game.get().getLevel().getWorld();
		if (RENDER_DEBUG) {
			mDebugRenderer.render(world, mCamera.combined);
		}

		mSpriteBatch.setProjectionMatrix(mCamera.combined);
		mSpriteBatch.begin();

		Actor actor = InputManager.getSelectedActor();
		if (actor != null) {
			Vector3 position = Misc.v3r0;
			position.set(actor.getPosition().x, actor.getPosition().y, 0.0f);
			float scale = 8f * (mCamera.zoom) * UI_SCALE;
			float offset = .5f * scale;
			float rotation = actor.getBody().getAngle() * Misc.RAD_TO_DEG;
			mSpriteBatch.draw(mTranslateOverlay, position.x - offset, position.y - offset, scale,
					scale);
			mSpriteBatch.draw(mRotateOverlay, position.x - offset, position.y - offset, offset,
					offset, scale, scale, 1.0f, 1.0f, rotation, 0, 0, mRotateOverlay.getWidth(),
					mRotateOverlay.getHeight(), false, false);
		}

		mSpriteBatch.end();
	}

	public void resetCamera() {
		float ratio = Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
		WIDTH = HEIGHT * ratio;
		System.out.println(UI_SCALE);
		UI_SCALE = 700.0f / (Gdx.graphics.getHeight() / DENSITY);
		mCamera.setToOrtho(false, WIDTH, HEIGHT);
		mCamera.translate(-WIDTH / 2.0f, -HEIGHT / 2.0f);
		mCamera.update();
	}

	public OrthographicCamera getCamera() {
		return mCamera;
	}
}
