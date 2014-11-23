package com.pwnscone.drummachine.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Level;
import com.pwnscone.drummachine.Particle;
import com.pwnscone.drummachine.actors.Actor;
import com.pwnscone.drummachine.util.Misc;
import com.pwnscone.drummachine.util.Pool;

public class View {
	public static float WIDTH = 9.0f;
	public static float HEIGHT = 16.0f;
	public static float TOP = HEIGHT / WIDTH;

	public static float UI_SCALE = 1.0f;
	public static float SCREEN_SCALE = 1.0f / 128.0f;
	public static float DENSITY;
	public static float RATIO;
	public static float INV_RATIO;

	private static boolean RENDER_DEBUG = false;

	private OrthographicCamera mCamera;
	private OrthographicCamera mUICamera;
	private SpriteBatch mSpriteBatch;
	private Box2DDebugRenderer mDebugRenderer;
	private ShapeRenderer mShapeRenderer;
	private LoopRenderer mLoopRenderer;

	private Texture mTranslateOverlay;
	private Texture mRotateOverlay;
	private World mWorld;

	/*
	 * private Color mBackgroundColor; private FrameBuffer mBackgroundBuffer;
	 * private TextureRegion mBackgroundTextureRegion;
	 */

	public View() {
		mCamera = new OrthographicCamera();
		mUICamera = new OrthographicCamera();
		mSpriteBatch = new SpriteBatch();
		mDebugRenderer = new Box2DDebugRenderer();
		mShapeRenderer = new ShapeRenderer();
		mLoopRenderer = new LoopRenderer();

		/*
		 * mBackgroundColor = new Color(0.0f, 0.0f, 0.0f, 0.025f);
		 * mBackgroundTextureRegion = new TextureRegion();
		 */

		mLoopRenderer.setShapeRenderer(mShapeRenderer);

		DENSITY = Gdx.graphics.getDensity();

		resetCamera();
		mCamera.position.set(0.0f, 0.0f, 0.0f);
		mCamera.update();
	}

	public void create() {
		AssetManager assetManager = Game.get().getAssetManager();
		mTranslateOverlay = assetManager.get("translateOverlay.png", Texture.class);
		mRotateOverlay = assetManager.get("rotateOverlay.png", Texture.class);
	}

	public void update() {

		mShapeRenderer.setProjectionMatrix(mUICamera.combined);
		mSpriteBatch.setProjectionMatrix(mCamera.combined);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/*
		 * Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
		 * Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA,
		 * Gdx.gl.GL_ONE_MINUS_SRC_ALPHA); mBackgroundBuffer.begin();
		 * mShapeRenderer.begin(ShapeType.Filled);
		 * mShapeRenderer.setColor(mBackgroundColor); mShapeRenderer.rect(0, 0,
		 * 1.0f, TOP); mShapeRenderer.end(); mBackgroundBuffer.end();
		 * Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
		 */

		if (RENDER_DEBUG) {
			mDebugRenderer.render(mWorld, mCamera.combined);
		}

		// mBackgroundBuffer.begin();

		Pool<Particle> particles = Game.get().getLevel().getParticles();
		int listSize = particles.fill;
		mSpriteBatch.begin();
		for (int i = 0; i < listSize; i++) {
			particles.get(i).render(mSpriteBatch);
		}
		mSpriteBatch.end();

		ArrayList<Pool<?>> actors = Game.get().getLevel().getActorPoolArrayList();
		listSize = actors.size();
		for (int i = 0; i < listSize; i++) {
			mSpriteBatch.begin();
			Pool<?> pool = actors.get(i);
			int size = pool.fill;
			for (int j = 0; j < size; j++) {
				((Actor) pool.get(j)).render(mSpriteBatch);
			}
			mSpriteBatch.end();
		}
		// mBackgroundBuffer.end();

		mSpriteBatch.setColor(Color.WHITE);

		/*
		 * mSpriteBatch.setProjectionMatrix(mUICamera.combined);
		 * mSpriteBatch.begin(); mSpriteBatch.draw(mBackgroundTextureRegion, 0,
		 * 0, 1, TOP); mSpriteBatch.end();
		 * mSpriteBatch.setProjectionMatrix(mCamera.combined);
		 */

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

		mLoopRenderer.render();
	}

	public void setLevel(Level level) {
		mWorld = level.getWorld();
		mLoopRenderer.setLoop(level.getLoop());
	}

	public void resetCamera() {
		/*
		 * if (mBackgroundBuffer != null) { mBackgroundBuffer.dispose(); }
		 * mBackgroundBuffer = new FrameBuffer(Pixmap.Format.RGBA8888,
		 * Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		 * mBackgroundTextureRegion
		 * .setTexture(mBackgroundBuffer.getColorBufferTexture());
		 * mBackgroundTextureRegion.setRegion(0, 0, Gdx.graphics.getWidth(),
		 * Gdx.graphics.getHeight()); mBackgroundTextureRegion.flip(false,
		 * true);
		 */

		RATIO = Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
		INV_RATIO = (float) (1.0f / (RATIO));
		WIDTH = HEIGHT * RATIO;

		Vector3 position = Misc.v3r0;
		position.set(mCamera.position);

		TOP = HEIGHT / WIDTH;
		UI_SCALE = 600.0f / (Gdx.graphics.getHeight() / DENSITY);

		mCamera.setToOrtho(false, WIDTH, HEIGHT);
		mCamera.position.set(position);
		mCamera.update();

		mUICamera.setToOrtho(false, 1.0f, HEIGHT / WIDTH);
		mUICamera.update();
	}

	public OrthographicCamera getCamera() {
		return mCamera;
	}
}
