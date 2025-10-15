package fire.pb.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.event.MissionCompleteEvent;

public class MissionCommitLog implements MissionLog {
	private final long roleid;
	private final int taskid;
	private final MissionReward reward;
	public MissionCommitLog( final long roleid, final int taskid, final MissionReward reward ) {
		this.roleid = roleid;
		this.taskid = taskid;
		this.reward = reward;
	}
	@Override
	public long getRoleId() {
		return roleid;
	}

	@Override
	public int getMissionId() {
		return taskid;
	}

	@Override
	public MissionLogType getType() {
		return MissionLogType.COMMIT;
	}
	
	@Override
	public void write() {
		Module.logger.debug( "人物id=" + roleid + "完成了任务" + taskid );
		Map<Integer, Integer> items = reward.getRewardItems();
		if (items != null && !items.isEmpty()) {
			List<Integer> itemlist = new ArrayList<Integer>();
			for ( Map.Entry<Integer, Integer> item : items.entrySet() ) {
				itemlist.add( item.getKey() );
				itemlist.add( item.getValue() );
				itemlist.add( 0 );
			}
		} else {
			
		}
		fire.pb.event.Poster.getPoster().dispatchEvent(new MissionCompleteEvent(roleid, taskid));
		Module.getInstance().untrackMission(roleid, taskid);
	}

}
