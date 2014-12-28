package levels;

import com.badlogic.gdx.math.Vector2;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Level;
import com.pwnscone.drummachine.Loop;
import com.pwnscone.drummachine.actors.Kick;
import com.pwnscone.drummachine.actors.Snare;
import com.pwnscone.drummachine.actors.Spawner;
import com.pwnscone.drummachine.util.Misc;

public class BeginnerLevel extends Level {
	private Snare mSnare;
	private Kick mKick;

	public void create() {
		this.mBounds.set(4.0f, 8.0f);
		Spawner spawner = (Spawner) createActor(Spawner.class);
		spawner.setTransformation(-2.1f, 4.0f, 0.75f);
		spawner.setLocked(true);

		mKick = (Kick) createActor(Kick.class);
		mKick.setTransformation(1.05f, -3.5f, -1.75f);
		mKick.setRotationLocked(true);
		mKick.showGhost(true);
		Vector2 pos = Misc.v2r0.set(2.42f, -0.72f);

		mKick.setGhostTransform(pos, -1.75f);

		mSnare = (Snare) createActor(Snare.class);
		mSnare.setRotationLocked(true);
		mSnare.setTransformation(-1.0f, -5.55f, 0.0f);
		pos.set(-2.69f, -3.91f);
		mSnare.setGhostTransform(pos, 0.0f);
		mSnare.showGhost(true);
		mSnare.setLocked(true);

		mLoop = new Loop(16, 8, 0);

		mLoop.addKick(2);
		mLoop.addKick(6);

		mLoop.addSnare(0);
		mLoop.addSnare(4);

		Game.get().getView().setLevel(this);
	}

	@Override
	public void update() {
		super.update();
		if (mKick.getDistFromGhost() < .00001f) {
			if (!mKick.isLocked()) {
				mKick.setLocked(true);
				mSnare.setLocked(false);
				mKick.setLinearVelocity(Vector2.Zero);
			}
			if (mSnare.getDistFromGhost() < .00001f) {
				if (!mSnare.isLocked()) {
					mSnare.setLocked(true);
					mSnare.setLinearVelocity(Vector2.Zero);
				}
			}
		}
	}
}
