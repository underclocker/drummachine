package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.ui.InputManager;
import com.pwnscone.drummachine.ui.View;
import com.pwnscone.drummachine.util.Misc;
import com.pwnscone.drummachine.util.Poolable;

public class Actor extends Poolable {
	private static final int FIXTURE_CACHE_SIZE = 4;

	protected Body mMainBody;
	protected boolean mCollided;
	protected boolean mLocked;
	protected boolean mRotationLocked;
	protected boolean mTranslationLocked;

	protected Fixture[] mCollidedFixturesPrimary;
	protected Fixture[] mCollidedFixturesSecondary;
	protected int mFixtureIndex;
	protected boolean mFixtureCacheToggle;

	protected Texture mTexture;
	protected Texture mHitTexture;
	protected Vector2 mOffset;
	protected float mScale;

	protected boolean mGlowOnHit;
	protected float mOnTime;
	protected float mOnTimeDecay;
	protected float mMinGlow;
	protected Color mColor;

	protected Vector2 mGhostPos;
	protected float mGhostRot;
	protected boolean mShowGhost;
	protected float mGhostCycle;
	protected float mGhostCycleSpeed;
	protected boolean mSnapped;

	public void create() {
		if (mCollidedFixturesPrimary == null) {
			mCollidedFixturesPrimary = new Fixture[FIXTURE_CACHE_SIZE];
			mCollidedFixturesSecondary = new Fixture[FIXTURE_CACHE_SIZE];
			mGhostPos = new Vector2();
		}
		mGhostPos.set(Float.MAX_VALUE, Float.MAX_VALUE);
		mGhostRot = 0;
		mShowGhost = false;
		mGhostCycle = 0;
		mGhostCycleSpeed = (float) (2 * Math.PI / Game.get().getLevel().getFramesPerBeat());
		mCollided = false;
		mLocked = false;
		mRotationLocked = false;
		mTranslationLocked = false;
		mFixtureIndex = -1;
		mOnTime = 0;
		mOnTimeDecay = 0.75f;
		mMinGlow = 0.9f;
	}

	public void update() {
		mGhostCycle += mGhostCycleSpeed;
		mOnTime *= mOnTimeDecay;
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

		if (mGlowOnHit) {
			float brightness = mMinGlow + (1 - mMinGlow) * mOnTime;
			spriteBatch.setColor(brightness, brightness, brightness, 1.0f);
		} else {
			spriteBatch.setColor(Color.WHITE);
		}

		float scale = mScale;
		float width = mTexture.getWidth() * View.SCREEN_SCALE;
		float height = mTexture.getHeight() * View.SCREEN_SCALE;
		spriteBatch.draw(mTexture, pos.x, pos.y, 0, 0, width, height, scale, scale, rot, 0, 0,
				mTexture.getWidth(), mTexture.getHeight(), false, false);
		if (mHitTexture != null) {
			if (mOnTime >= 0.0038f) {
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, mOnTime);
				spriteBatch.draw(mHitTexture, pos.x, pos.y, 0, 0, width, height, scale, scale, rot,
						0, 0, mHitTexture.getWidth(), mHitTexture.getHeight(), false, false);
			}
			if (mShowGhost && InputManager.getSelectedActor() == this && !mLocked) {
				pos.set(mOffset);
				pos.rotate(mGhostRot);
				pos.add(mGhostPos);
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.3f - ((float) Math.cos(mGhostCycle)) * .1f
						- .2f * (10 / (10 + mGhostCycle)));
				spriteBatch.draw(mHitTexture, pos.x, pos.y, 0, 0, width, height, scale, scale,
						mGhostRot, 0, 0, mHitTexture.getWidth(), mHitTexture.getHeight(), false,
						false);
			}
		}
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

	public void setLinearVelocity(float x, float y) {
		mMainBody.setLinearVelocity(x, y);
	}

	public void setLinearVelocity(Vector2 velocity) {
		mMainBody.setLinearVelocity(velocity);
	}

	public void setAngularVelocity(float velocity) {
		mMainBody.setAngularVelocity(velocity);
	}

	public Color getColor() {
		return mColor;
	}

	public float getOnTime() {
		return mOnTime;
	}

	public void setLocked(boolean locked) {
		mLocked = locked;
	}

	public boolean isLocked() {
		return mLocked;
	}

	public void setRotationLocked(boolean locked) {
		mRotationLocked = locked;
	}

	public boolean isRotationLocked() {
		return mRotationLocked;
	}

	public void setTranslationLocked(boolean locked) {
		mTranslationLocked = locked;
	}

	public boolean isTranslationLocked() {
		return mTranslationLocked;
	}

	public void setGhostTransform(Vector2 pos, float rot) {
		mGhostPos.set(pos);
		mGhostRot = Misc.RAD_TO_DEG * rot;
	}

	public void showGhost(boolean show) {
		mShowGhost = show;
	}

	public void resetGhostCycle() {
		mGhostCycle = 0;
	}

	public boolean isShowingGhost() {
		return mShowGhost;
	}

	public Vector2 getGhostPos() {
		return mGhostPos;
	}

	public float getDistFromGhost() {
		Vector2 v0 = Misc.v2r0.set(getGhostPos());
		v0.sub(mMainBody.getPosition());
		return v0.len2();
	}
}
