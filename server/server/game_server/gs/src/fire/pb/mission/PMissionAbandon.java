package fire.pb.mission;

public class PMissionAbandon extends PMissionProc {
	protected final long roleid;
	protected final int missionid;
	
	public PMissionAbandon(final long roleid, final int missionid) {
		this.roleid = roleid;
		this.missionid = missionid;
	}
	protected boolean abandonExecute() {
		return false;
	}
	@Override
	protected boolean missionExecute() {
		
		/*
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			boolean ok = fire.pb.fushi.DayPayManager.getInstance().CheckDayPayWithLevel(roleid);
			if (ok == false)
			{
				Module.logger.info("PMissionAbandon:角色[" + roleid + "]任务不能放弃-nodaypay:" + missionid);
				return false;
			}
		}
		*/	
		
		if (!abandonExecute()) {
			return false;
		}
		getLogs().addLog(new MissionAbandonLog(roleid, missionid));
		return true;
	}

}
