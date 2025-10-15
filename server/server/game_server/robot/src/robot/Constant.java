package robot;

import fire.pb.move.SceneState;

public class Constant {
	public final static int MOVE_SPEED = 48;
	public final static int GRID_TO_PIXEL = 16;
	public static final int CANT_MOVE_STATE_SUM = SceneState.BATTLE_FIGHTER
			| SceneState.BATTLE_WATCHER | SceneState.PLAY_CG;
}
