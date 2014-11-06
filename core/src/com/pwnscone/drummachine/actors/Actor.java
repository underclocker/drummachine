package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.pwnscone.drummachine.ui.View;
import com.pwnscone.drummachine.util.Misc;
import com.pwnscone.drummachine.util.Poolable;

public class Actor extends Poolable {
	private static final int FIXTURE_CACHE_SIZE = 4;

	protected Body mMainBody;
	protected boolean mCollided = false;

	protected Fixture[] mCollidedFixturesPrimary;
	protected Fixture[] mCollidedFixturesSecondary;
	protected int mFixtureIndex;
	protected boolean mFixtureCacheToggle;

	protected Texture mTexture;
	protected Vector2 mOffset;
	protected float mScale;

	public void create() {
		mCollidedFixturesPrimary = new Fixture[FIXTURE_CACHE_SIZE];
		mCollidedFixturesSecondary = new Fixture[FIXTURE_CACHE_SIZE];
		mFixtureIndex = -1;
	}

	public void update() {
		mCollided = false;
		Fixture[] cache = !mFixtureCacheToggle ? mCollidedFixturesPrimary
				: mCollidedFixturesSecondary;
		for (int i = 0; i < FIXTURE_CACHE_SIZE; i++) {
			cache[i] = null;
		}
		mFixtureIndex = -1;
		mFixtureCacheToggle = !mFixtureCacheToggle;
	}

	public void render(SpriteBatch spriteBatch) {
		if (mTexture == null || !mMainBody.isActive()) {
			return;
		}

		Vector2 pos = Misc.v2r0;
		pos.set(mOffset);
		float rot = getRotation();
		pos.rotate(rot);
		pos.add(getPosition());

		spriteBatch.draw(mTexture, pos.x, pos.y, 0, 0, mTexture.getWidth() * View.SCREEN_SCALE,
				mTexture.getHeight() * View.SCREEN_SCALE, mScale, mScale, getRotation(), 0, 0,
				mTexture.getWidth(), mTexture.getHeight(), false, false);
	}

	public void destroy() {
		if (mMainBody != null) {
			mMainBody.setActive(false);
		}
	}

	public Body getBody() {
		return mMainBody;
	}

	public Vector2 getPosition() {
		if (mMainBody != null) {
			return mMainBody.getPosition();
		}
		return null;
	}

	public float getRotation() {
		if (mMainBody != null) {
			return mMainBody.getAngle() * Misc.RAD_TO_DEG;
		}
		return 0.0f;
	}

	public void setTransformation(float x, float y, float angle) {
		Vector2 position = Misc.v2r0;
		position.set(x, y);
		if (mMainBody != null) {
			mMainBody.setTransform(position, angle);
		}
	}

	protected boolean isInCache(Fixture fixture) {
		Fixture[] cache = mFixtureCacheToggle ? mCollidedFixturesSecondary
				: mCollidedFixturesPrimary;
		for (int i = 0; i < FIXTURE_CACHE_SIZE; i++) {
			if (fixture == cache[i]) {
				return true;
			}
		}
		return false;
	}

	public void collide(Fixture otherFixture) {
		if (++mFixtureIndex < FIXTURE_CACHE_SIZE) {
			Fixture[] cache = mFixtureCacheToggle ? mCollidedFixturesPrimary
					: mCollidedFixturesSecondary;
			cache[mFixtureIndex] = otherFixture;
		}
	}
}
