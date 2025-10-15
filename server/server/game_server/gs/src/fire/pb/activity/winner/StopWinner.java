package fire.pb.activity.winner;

import java.util.TimerTask;

import fire.pb.util.XdbUtil;


public class StopWinner extends TimerTask {
    private long futureKey;

	@Override
	public void run() {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				WinnerManager.getInstance().stopwinner();
				if (futureKey > 0) {
					XdbUtil.cancelScheduleTask(futureKey);
				}
				return true;
			}

		}.submit();

	}
	
	public void setFutureKey(long futureKey) {
		this.futureKey = futureKey;
	}

}

