package levels;

import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Level;
import com.pwnscone.drummachine.Loop;
import com.pwnscone.drummachine.actors.HiHatClosed;
import com.pwnscone.drummachine.actors.Kick;
import com.pwnscone.drummachine.actors.Snare;
import com.pwnscone.drummachine.actors.Spawner;

public class AmenLevel extends Level {
	public void create() {
		Spawner spawner = (Spawner) createActor(Spawner.class);
		spawner.setTransformation(-1.25f, -5.0f, 3.025f);
		spawner.setLocked(true);

		Snare snare = (Snare) createActor(Snare.class);
		snare.setTransformation(-4.0f, 4.0f, 0.0f);

		snare = (Snare) createActor(Snare.class);
		snare.setTransformation(-2.0f, 4.0f, 0.0f);

		snare = (Snare) createActor(Snare.class);
		snare.setTransformation(0.0f, 4.0f, 0.0f);

		Kick kick = (Kick) createActor(Kick.class);
		kick.setTransformation(-8.0f, 6.0f, 0.0f);

		kick = (Kick) createActor(Kick.class);
		kick.setTransformation(-4.0f, 6.0f, 0.0f);

		kick = (Kick) createActor(Kick.class);
		kick.setTransformation(0.0f, 6.0f, 0.0f);

		HiHatClosed hiHatClosed = (HiHatClosed) createActor(HiHatClosed.class);
		hiHatClosed.setTransformation(4.0f, 4.0f, 0.0f);

		hiHatClosed = (HiHatClosed) createActor(HiHatClosed.class);
		hiHatClosed.setTransformation(4.0f, 6.0f, 0.0f);

		hiHatClosed = (HiHatClosed) createActor(HiHatClosed.class);
		hiHatClosed.setTransformation(8.0f, 4.0f, 0.0f);

		hiHatClosed = (HiHatClosed) createActor(HiHatClosed.class);
		hiHatClosed.setTransformation(8.0f, 6.0f, 0.0f);

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
}
