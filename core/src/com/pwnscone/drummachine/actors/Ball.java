package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Particle;
import com.pwnscone.drummachine.ui.View;
import com.pwnscone.drummachine.util.Misc;

public class Ball extends Actor {
	private static int POS_HIST_SIZE = 30;
	private static float EPSILON = 0.0001f;
	public static int TRAIL_LENGTH = 3;

	private float[] xPos;
	private float[] yPos;
	private int mHistIndex;

	@Override
	public void create() {
		super.create();
		mHistIndex = 0;
		if (mMainBody == null) {
			World world = Game.get().getLevel().getWorld();
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.bullet = true;
			Body body = world.createBody(bodyDef);
			mMainBody = body;
			mMainBody.setUserData(this);

			CircleShape circle = new CircleShape();
			circle.setRadius(.21875f);

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = circle;
			fixtureDef.density = 0.5f;
			fixtureDef.friction = 0.4f;
			fixtureDef.restitution = 0.85f;

			body.createFixture(fixtureDef);
			circle.dispose();

			// Graphics
			mTexture = Game.get().getAssetManager().get("ball.png", Texture.class);
			mOffset = new Vector2(-0.21875f, -0.21875f);
			mScale = .5f;
			mColor = Color.WHITE;

			xPos = new float[POS_HIST_SIZE];
			yPos = new float[POS_HIST_SIZE];

		} else {
			mMainBody.setActive(true);
		}
		for (int i = 0; i < POS_HIST_SIZE; i++) {
			xPos[i] = Float.MAX_VALUE;
			yPos[i] = Float.MAX_VALUE;
		}
		mMainBody.setLinearVelocity(Vector2.Zero);
		mMainBody.setAngularVelocity(0.0f);
	}

	@Override
	public void update() {
		super.update();
		if (mMainBody.isActive()) {
			Vector2 pos = mMainBody.getPosition();
			if (pos.len2() > 4000000.0f) {
				Game.get().getLevel().destroyActor(this);
				return;
			}
			xPos[mHistIndex] = pos.x;
			yPos[mHistIndex] = pos.y;
			mHistIndex++;
			if (mHistIndex == POS_HIST_SIZE) {
				mHistIndex = 0;
				Vector2 posCache = Misc.v2r0;
				for (int i = 0; i < POS_HIST_SIZE; i++) {
					if (pos.dst2(xPos[mHistIndex], yPos[mHistIndex]) > EPSILON) {
						return;
					}
				}
				Game.get().getLevel().destroyActor(this);
			}
		}
	}

	@Override
	public void collide(Fixture otherFixture) {
		super.collide(otherFixture);
		if (mCollided || otherFixture.isSensor()) {
			return;
		}
		mCollided = true;
		Actor otherActor = (Actor) otherFixture.getBody().getUserData();
		if (otherActor.mOnTime == 1.0f) {
			for (int i = 0; i < 2; i++) {
				Particle particle = Game.get().getLevel().createParticle();
				particle.mPosition.set(this.mContactPoint);
				particle.setActor(otherActor);
				Vector2 vel = Misc.v2r0.set(this.getPosition());
				vel.sub(this.mContactPoint);
				vel.rotate(80.0f - Misc.random() * 160.0f);
				vel.scl(.01f * (4 + Misc.random()));
				particle.mVelocity.set(vel);
			}
		}
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		if (mTexture == null || !mMainBody.isActive()) {
			return;
		}

		for (int i = TRAIL_LENGTH - 1; i >= 0; i--) {
			Vector2 pos = Misc.v2r0;
			pos.set(mOffset);
			float rot = getRotation();
			pos.rotate(rot);

			int index = (POS_HIST_SIZE + mHistIndex - i - 1) % POS_HIST_SIZE;
			pos.add(xPos[index], yPos[index]);

			float tint = 1 - i / (float) TRAIL_LENGTH;
			spriteBatch.setColor(tint, tint, tint, tint);

			float scale = mScale;
			float width = mTexture.getWidth() * View.SCREEN_SCALE;
			float height = mTexture.getHeight() * View.SCREEN_SCALE;
			spriteBatch.draw(mTexture, pos.x, pos.y, 0, 0, width, height, scale, scale, rot, 0, 0,
					mTexture.getWidth(), mTexture.getHeight(), false, false);
		}
	}

}
