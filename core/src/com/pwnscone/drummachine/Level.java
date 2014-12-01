package com.pwnscone.drummachine;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.actors.Actor;
import com.pwnscone.drummachine.actors.Ball;
import com.pwnscone.drummachine.actors.HiHatClosed;
import com.pwnscone.drummachine.actors.Kick;
import com.pwnscone.drummachine.actors.Snare;
import com.pwnscone.drummachine.actors.Spawner;
import com.pwnscone.drummachine.util.Pool;

public class Level {
	private World mWorld;
	private PhysicsContactListener mPhysicsContactListener;
	private HashMap<Class, Pool<?>> mActorPoolMap;
	private ArrayList<Pool<?>> mActorPoolArrayList;
	private ArrayList<Actor> mDestroyQueue;
	private Pool<Particle> mParticles;
	private int mFramesPerBeat;
	private int mFrame = 0;
	protected Loop mLoop;
	protected Vector2 mBounds;
	protected Vector2 mGravity;

	public Level() {
		mFramesPerBeat = 64;
		mGravity = new Vector2(0.0f, -4.9f);
		mWorld = new World(mGravity, true);
		mGravity.scl(1.0f / Game.FRAME_RATE);
		mPhysicsContactListener = new PhysicsContactListener();
		mWorld.setContactListener(mPhysicsContactListener);

		mDestroyQueue = new ArrayList<Actor>(128);
		mActorPoolMap = new HashMap<Class, Pool<?>>();
		mActorPoolMap.put(Ball.class, new Pool<Ball>(Ball.class));
		mActorPoolMap.put(Spawner.class, new Pool<Spawner>(Spawner.class));
		mActorPoolMap.put(Snare.class, new Pool<Snare>(Snare.class));
		mActorPoolMap.put(Kick.class, new Pool<Kick>(Kick.class));
		mActorPoolMap.put(HiHatClosed.class, new Pool<HiHatClosed>(HiHatClosed.class));
		mActorPoolArrayList = new ArrayList<Pool<?>>();
		mActorPoolArrayList.add(mActorPoolMap.get(Snare.class));
		mActorPoolArrayList.add(mActorPoolMap.get(Kick.class));
		mActorPoolArrayList.add(mActorPoolMap.get(HiHatClosed.class));
		mActorPoolArrayList.add(mActorPoolMap.get(Ball.class));
		mActorPoolArrayList.add(mActorPoolMap.get(Spawner.class));

		mParticles = new Pool<Particle>(Particle.class);

		mLoop = new Loop();
		mBounds = new Vector2(20.0f, 20.0f);
	}

	public void create() {
	}

	public void update() {
		mFrame++;
		synchronized (Game.get().getSynth()) {
			mWorld.step(1.0f / 60.0f, 8, 3);
		}

		int listSize = mActorPoolArrayList.size();
		for (int i = 0; i < listSize; i++) {
			Pool<?> pool = mActorPoolArrayList.get(i);
			int size = pool.fill;
			for (int j = 0; j < size; j++) {
				((Actor) pool.get(j)).update();
			}
		}

		for (int i = 0; i < mParticles.fill; i++) {
			Particle particle = mParticles.get(i);
			particle.update();
			if (particle.mLife <= 0) {
				destroyParticle(particle);
				i--;
			}
		}

		if (mLoop != null) {
			mLoop.update();
		}

		while (mDestroyQueue.size() > 0) {
			Actor actor = mDestroyQueue.remove(0);
			mActorPoolMap.get(actor.getClass()).remove(actor);
		}
	}

	public Actor createActor(Class classType) {
		Actor actor = (Actor) mActorPoolMap.get(classType).add();
		actor.create();
		return actor;
	}

	public void destroyActor(Actor actor) {
		actor.destroy();
		mDestroyQueue.add(actor);
	}

	public Particle createParticle() {
		Particle particle = mParticles.add();
		particle.create();
		return particle;
	}

	public void destroyParticle(Particle particle) {
		mParticles.remove(particle);
	}

	public World getWorld() {
		return mWorld;
	}

	public Vector2 getGravity() {
		return mGravity;
	}

	public int getFrame() {
		return mFrame;
	}

	public int getFramesPerBeat() {
		return mFramesPerBeat;
	}

	public Loop getLoop() {
		return mLoop;
	}

	public ArrayList<Pool<?>> getActorPoolArrayList() {
		return mActorPoolArrayList;
	}

	public Pool<Particle> getParticles() {
		return mParticles;
	}

	public Vector2 getBounds() {
		return mBounds;
	}
}