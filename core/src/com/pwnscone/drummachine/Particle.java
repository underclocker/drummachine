package com.pwnscone.drummachine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pwnscone.drummachine.ui.View;
import com.pwnscone.drummachine.util.Misc;
import com.pwnscone.drummachine.util.Poolable;

public class Particle extends Poolable {
	protected Vector2 mPosition;
	protected Texture mTexture;
	protected Vector2 mOffset;
	protected Vector2 mVelocity;
	protected Vector2 mGravity;

	protected float mScale;

	protected float mTurn;

	public void create() {
		if (mPosition == null) {
			mPosition = new Vector2(0, 0f);
			mTexture = Game.get().getAssetManager().get("particle.png", Texture.class);
			mOffset = new Vector2();
			mVelocity = new Vector2();
			mGravity = Game.get().getLevel().getGravity().cpy().scl(.001f);
			mScale = 0.5f;
			mTurn = 10 * (Misc.random() - 0.5f);
		}
		mVelocity.set(1.0f, 0.0f);
		mVelocity.rotate(Misc.random() * 360.0f);
		mVelocity.scl(.005f * (4 + Misc.random()));
	}

	public void update() {
		mVelocity.scl(.99f);
		mTurn += Math.random() - 0.5f;
		mTurn *= .95;
		mVelocity.rotate(mTurn);
		mVelocity.add(mGravity);
		mPosition.add(mVelocity);
	};

	public void render(SpriteBatch spriteBatch) {

		Vector2 pos = Misc.v2r0;
		pos.set(mOffset);
		float rot = 0;
		// pos.rotate(rot);
		pos.add(mPosition);

		spriteBatch.setColor(Color.WHITE);

		float scale = mScale;
		float width = mTexture.getWidth() * View.SCREEN_SCALE;
		float height = mTexture.getHeight() * View.SCREEN_SCALE;
		spriteBatch.draw(mTexture, pos.x, pos.y, 0, 0, width, height, scale, scale, rot, 0, 0,
				mTexture.getWidth(), mTexture.getHeight(), false, false);
	}

}
