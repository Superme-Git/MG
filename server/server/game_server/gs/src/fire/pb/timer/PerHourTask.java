package fire.pb.timer;
import java.util.TimerTask;

import fire.pb.ranklist.proc.RoleLevelSort;

/**
 * 按小时收取运营费用
 *
 */
public class PerHourTask  extends TimerTask{

	@Override
	public void run() {
		
		new RoleLevelSort().submit();
	}
}