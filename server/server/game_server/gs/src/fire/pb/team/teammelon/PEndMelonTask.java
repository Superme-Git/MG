/**
 * 
 */
package fire.pb.team.teammelon;

import java.util.TimerTask;
/**
 *  roll点结束 回调by changhao 
 *
 */
public class PEndMelonTask extends TimerTask {
	
	@Override
	public void run() {
		
		new fire.pb.team.teammelon.PTeamRollMelonInfo(battlemelonid, 1).submit();
	}
	
	public PEndMelonTask(long battlemelonid)
	{
		this.battlemelonid = battlemelonid;
	}
	
	public final long battlemelonid;
}
