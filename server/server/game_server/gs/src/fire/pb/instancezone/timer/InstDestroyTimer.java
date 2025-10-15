package fire.pb.instancezone.timer;

import com.locojoy.base.Runnable;

public class InstDestroyTimer extends Runnable {

	private final long landkey;
	
	public InstDestroyTimer(final long landkey) {
		this.landkey = landkey;
	}


	@Override
	public void run() {
		new PInstDestroy(landkey).submit();
	}

}
