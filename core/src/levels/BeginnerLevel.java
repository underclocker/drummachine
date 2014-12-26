package levels;

import com.badlogic.gdx.math.Vector2;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Level;
import com.pwnscone.drummachine.Loop;
import com.pwnscone.drummachine.actors.HiHatClosed;
import com.pwnscone.drummachine.actors.Kick;
import com.pwnscone.drummachine.actors.Snare;
import com.pwnscone.drummachine.actors.Spawner;
import com.pwnscone.drummachine.util.Misc;

public class BeginnerLevel extends Level {
	public void create() {
		this.mBounds.set(4.0f, 8.0f);
		Spawner spawner = (Spawner) createActor(Spawner.class);
		spawner.setTransformation(-2.1f, 4.0f, 0.95f);
		spawner.setExitSpeed(16.0f);
		spawner.setLocked(true);

		Snare snare = (Snare) createActor(Snare.class);
		snare.setTransformation(2.7f, -.95f, 0.0f);
		snare.setLocked(true);

		Kick kick = (Kick) createActor(Kick.class);
		kick.setTransformation(1.45f, -5.5f, 3.0f);
		kick.setRotationLocked(true);
		kick.showGhost(true);
		Vector2 pos = Misc.v2r0.set(-.72f, -4.41f);

		kick.setGhostTransform(pos, 3.0f);

		HiHatClosed hhc = (HiHatClosed) createActor(HiHatClosed.class);
		hhc.setTransformation(2.46f, -3.135f, 2.695f);
		hhc.setLocked(true);

		hhc = (HiHatClosed) createActor(HiHatClosed.class);
		hhc.setTransformation(-2.33f, -1.68f, 1.00f);
		hhc.setLocked(true);

		mLoop = new Loop(16, 8, 0);

		mLoop.addHiHatClosed(1);
		mLoop.addHiHatClosed(5);
		mLoop.addHiHatClosed(3);
		mLoop.addHiHatClosed(7);

		mLoop.addSnare(0);
		mLoop.addSnare(4);

		mLoop.addKick(2);
		mLoop.addKick(6);

		Game.get().getView().setLevel(this);
	}
}
