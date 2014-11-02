package levels;

import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Level;
import com.pwnscone.drummachine.Loop;
import com.pwnscone.drummachine.actors.HiHatClosed;
import com.pwnscone.drummachine.actors.Kick;
import com.pwnscone.drummachine.actors.Snare;
import com.pwnscone.drummachine.actors.Spawner;

public class PopRockLevel extends Level {
	public void create() {
		Spawner spawner = (Spawner) createActor(Spawner.class);
		spawner.setTransformation(-1.25f, -5.0f, 3.0f);

		Snare snare = (Snare) createActor(Snare.class);
		snare.setTransformation(-4.0f, 4.0f, 0.0f);

		Kick kick = (Kick) createActor(Kick.class);
		kick.setTransformation(-8.0f, 6.0f, 0.0f);

		HiHatClosed hiHatClosed = (HiHatClosed) createActor(HiHatClosed.class);
		hiHatClosed.setTransformation(4.0f, 4.0f, 0.0f);

		hiHatClosed = (HiHatClosed) createActor(HiHatClosed.class);
		hiHatClosed.setTransformation(4.0f, 6.0f, 0.0f);

		hiHatClosed = (HiHatClosed) createActor(HiHatClosed.class);
		hiHatClosed.setTransformation(8.0f, 4.0f, 0.0f);

		hiHatClosed = (HiHatClosed) createActor(HiHatClosed.class);
		hiHatClosed.setTransformation(8.0f, 6.0f, 0.0f);

		mLoop = new Loop(14, 8, 0);

		mLoop.addHiHatClosed(0);
		mLoop.addHiHatClosed(1);
		mLoop.addHiHatClosed(2);
		mLoop.addHiHatClosed(3);
		mLoop.addHiHatClosed(4);
		mLoop.addHiHatClosed(5);
		mLoop.addHiHatClosed(6);
		mLoop.addHiHatClosed(7);

		mLoop.addSnare(2);
		mLoop.addSnare(6);

		mLoop.addKick(0);
		mLoop.addKick(4);

		Game.get().getView().setLevel(this);
	}
}
