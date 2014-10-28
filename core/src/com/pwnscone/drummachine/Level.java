package com.pwnscone.drummachine;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.actors.Actor;
import com.pwnscone.drummachine.actors.Ball;
import com.pwnscone.drummachine.actors.Snare;
import com.pwnscone.drummachine.actors.Spawner;
import com.pwnscone.drummachine.util.Pool;

public class Level {
	private World mWorld;
	private PhysicsContactListener mPhysicsContactListener;
	private HashMap<Class, Pool<?>> mActorPoolMap;
	private ArrayList<Pool<?>> mActorPoolArrayList;
	private ArrayList<Actor> mDestroyQueue;
	private int mFramesPerBeat;
	private int mFrame = 0;
	private Loop mLoop;

	public Level() {
		mFramesPerBeat = 64;
		mWorld = new World(new Vector2(0.0f, -4.9f), true);
		mPhysicsContactListener = new PhysicsContactListener();
		mWorld.setContactListener(mPhysicsContactListener);

		mDestroyQueue = new ArrayList<Actor>(128);
		mActorPoolMap = new HashMap<Class, Pool<?>>();
		mActorPoolMap.put(Ball.class, new Pool<Ball>(Ball.class));
		mActorPoolMap.put(Spawner.class, new Pool<Spawner>(Spawner.class));
		mActorPoolMap.put(Snare.class, new Pool<Snare>(Snare.class));
		mActorPoolArrayList = new ArrayList<Pool<?>>();
		mActorPoolArrayList.addAll(mActorPoolMap.values());
	}

	public void create() {
		Spawner spawner = (Spawner) createActor(Spawner.class);
		spawner.setTransformation(-1.25f, -5.0f, 3.025f);

		Snare snare = (Snare) createActor(Snare.class);
		snare.setTransformation(-4.0f, 4.0f, 0.0f);

		snare = (Snare) createActor(Snare.class);
		snare.setTransformation(-2.0f, 4.0f, 0.0f);

		snare = (Snare) createActor(Snare.class);
		snare.setTransformation(0.0f, 4.0f, 0.0f);

		snare = (Snare) createActor(Snare.class);
		snare.setTransformation(2.0f, 4.0f, 0.0f);

		snare = (Snare) createActor(Snare.class);
		snare.setTransformation(4.0f, 4.0f, 0.0f);

		mLoop = new Loop(8, 16, 1);

		mLoop.addHiHatClosed(0);
		mLoop.addHiHatClosed(2);
		mLoop.addHiHatClosed(4);
		mLoop.addHiHatClosed(6);
		mLoop.addHiHatClosed(8);
		mLoop.addHiHatClosed(10);
		mLoop.addHiHatClosed(12);
		mLoop.addHiHatClosed(14);

		mLoop.addSnare(4);
		mLoop.addSnare(7);
		mLoop.addSnare(9);
		mLoop.addSnare(12);
		mLoop.addSnare(15);

		mLoop.addKick(0);
		mLoop.addKick(2);
		mLoop.addKick(10);
		mLoop.addKick(11);

		Game.get().getView().setLevel(this);
	}

	public void update() {
		synchronized (Game.get().getSynth()) {
			mWorld.step(1.0f / 60.0f, 8, 3);
		}

		float listSize = mActorPoolArrayList.size();
		for (int i = 0; i < listSize; i++) {
			Pool<?> pool = mActorPoolArrayList.get(i);
			int size = pool.size();
			for (int j = 0; j < size; j++) {
				((Actor) pool.get(j)).update();
			}
		}

		if (mLoop != null) {
			mLoop.update();
		}

		while (mDestroyQueue.size() > 0) {
			Actor actor = mDestroyQueue.remove(0);
			mActorPoolMap.get(actor.getClass()).remove(actor);
		}

		mFrame++;
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

	public int getFrame() {
		return mFrame;
	}

	public Loop getLoop() {
		return mLoop;
	}
}