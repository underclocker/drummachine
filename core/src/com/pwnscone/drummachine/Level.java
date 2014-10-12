package com.pwnscone.drummachine;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.actors.Actor;
import com.pwnscone.drummachine.actors.Ball;
import com.pwnscone.drummachine.actors.Spawner;
import com.pwnscone.drummachine.util.Pool;

public class Level {
	private World mWorld;
	private HashMap<Class, Pool<?>> mActorPoolMap;
	private ArrayList<Pool<?>> mActorPoolArrayList;
	private ArrayList<Actor> mDestroyQueue;
	private int mFramesPerBeat;
	private int mFrame = 0;
	private float mWidth;
	private float mHeight;

	public Level() {
		mFramesPerBeat = 64;
		mWidth = 100f;
		mHeight = 100f;
		mWorld = new World(new Vector2(0.0f, -4.9f), true);
		mDestroyQueue = new ArrayList<Actor>(128);
		mActorPoolMap = new HashMap<Class, Pool<?>>();
		mActorPoolMap.put(Ball.class, new Pool<Ball>(Ball.class));
		mActorPoolMap.put(Spawner.class, new Pool<Spawner>(Spawner.class));
		mActorPoolArrayList = new ArrayList<Pool<?>>();
		mActorPoolArrayList.addAll(mActorPoolMap.values());
	}

	public void create() {
		createActor(Spawner.class);
		Spawner spawner2 = (Spawner) createActor(Spawner.class);
		spawner2.setTransformation(0.05f, -10.0f, (float) Math.PI);
	}

	public void update() {
		mWorld.step(1.0f / 60.0f, 8, 3);

		float listSize = mActorPoolArrayList.size();
		for (int i = 0; i < listSize; i++) {
			Pool<?> pool = mActorPoolArrayList.get(i);
			int size = pool.size();
			for (int j = 0; j < size; j++) {
				((Actor) pool.get(j)).update();
			}
		}
		while (mDestroyQueue.size() > 0) {
			Actor actor = mDestroyQueue.remove(0);
			mActorPoolMap.get(actor.getClass()).remove(actor);
		}

		mFrame++;
		if (mFrame >= mFramesPerBeat) {
			mFrame = 0;
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

	public World getWorld() {
		return mWorld;
	}

	public int getFramesPerBeat() {
		return mFramesPerBeat;
	}

	public int getFrame() {
		return mFrame;
	}
}