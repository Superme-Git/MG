package fire.pb.instancezone.timer;

import java.util.TimerTask;

public class InstMapTimer extends TimerTask {

	private final long ownerid;
	private final int saveid;
	private final int fubenid;
	
	public InstMapTimer(final long ownerid, final int saveid, final int fubenid) {
		this.ownerid = ownerid;
		this.saveid = saveid;
		this.fubenid = fubenid;
	}
	
	@Override
	public void run() {
		new PInstMapTimer(ownerid, saveid, fubenid).submit();
	}

}
