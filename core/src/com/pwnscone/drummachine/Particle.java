package com.pwnscone.drummachine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pwnscone.drummachine.actors.Actor;
import com.pwnscone.drummachine.ui.View;
import com.pwnscone.drummachine.util.Misc;
import com.pwnscone.drummachine.util.Poolable;

public class Particle extends Poolable {
	public Vector2 mPosition;
	public Vector2 mVelocity;
	protected Actor mActor;
	protected Vector2 mOffset;
	protected Texture mTexture;
	protected Vector2 mGravity;

	protected float mScale;

	protected float mTurn;
	protected float mLifeStart;
	protected float mLife;
	protected Color mColor;

	private static int LIFE = 120;

	public void create() {
		if (mPosition == null) {
			mPosition = new Vector2(0, 0f);
			mTexture = Game.get().getAssetManager().get("snareWhite.png", Texture.class);
			mOffset = new Vector2();
			mVelocity = new Vector2();
			mGravity = Game.get().getLevel().getGravity().cpy().scl(.0003f);
			mScale = 0.5f;
			mTurn = 10 * (Misc.random() - 0.5f);
		}
		mVelocity.set(0.0f, 0.0f);
		mLifeStart = (LIFE * (1 + Misc.random()));
		mLife = mLifeStart;
	}

	public void update() {
		mLife--;
		mTurn += Math.random() - 0.5f;
		mTurn *= .95;
		mVelocity.scl(1.005f);
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

		float lifeRatio = mLife / mLifeStart;
		float mirrorRatio = 1.0f - lifeRatio;

		spriteBatch.setColor(mColor.r, mColor.g, mColor.b, (0.3f + mActor.getOnTime() * 0.7f)
				* lifeRatio);

		float scale = mScale * (mirrorRatio + 0.05f);
		float width = mTexture.getWidth() * View.SCREEN_SCALE;
		float height = mTexture.getHeight() * View.SCREEN_SCALE;
		float halfw = width / 2 * scale;
		float halfh = width / 2 * scale;
		spriteBatch.draw(mTexture, pos.x - halfw, pos.y - halfh, 0, 0, width, height, scale, scale,
				rot, 0, 0, mTexture.getWidth(), mTexture.getHeight(), false, false);
	}

	public void setActor(Actor actor) {
		mActor = actor;
		mColor = actor.getColor();
	}
}
