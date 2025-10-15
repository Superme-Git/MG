package fire.pb.instancezone.faction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import fire.msp.IGetRolesCallBack;
import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.map.RoleManager;
import fire.pb.mission.activelist.SActivityOpen;
import fire.pb.talk.MessageMgr;
import fire.pb.timer.AbstractScheduledActivity;

public class ClanScheduleActivity extends AbstractScheduledActivity {
	
	public static AtomicBoolean isdoing = new AtomicBoolean(false);
	public ClanScheduleActivity(int baseid, int id, long startTime, long endTime) {
		super(baseid, id, startTime, endTime);
	}

	@Override
	protected boolean init() throws Exception {
		RoleManager.getRolesByConditions(-1, 30, -1, -1, -1, new IGetRolesCallBack() {
					@Override
					public void process(List<Long> roleIds) {
						for (long roleId : roleIds) {
							MessageMgr.sendMsgNotify(roleId, 160475, null);
						}
					}
				});

		mkdb.Mkdb.executor().schedule(new SendAll(0), 5, TimeUnit.MINUTES);
		//先删除一下公会最好成绩榜
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				Map<Integer, InstanceZoneConfig> instanceZoneConfigs = fire.pb.instancezone.Module.getInstance().getInstanceZoneConfigs();
				for (Integer instid : instanceZoneConfigs.keySet()) {
					if (xtable.Claninstbestlevel.select(instid) != null)
						xtable.Claninstbestlevel.remove(instid);
				}
				return true;
			}
			
		}.submit();

		return true;
	}

	@Override
	protected boolean stopActivity() {
		return true;
	}

	@Override
	protected boolean endActivity() {
		isdoing.set(false);
		return true;
	}
	
	public static class SendAll implements Runnable {
		public final int num;

		public SendAll(int num) {
			this.num = num;
		}

		@Override
		public void run() {
			isdoing.set(true);
			SActivityOpen so = new SActivityOpen();
			so.activityid = 218;
			gnet.link.Onlines.getInstance().broadcast(so, 997);
		}
	}
	
	public static void roleOnline(long roleId) {
		if (!isdoing.get())
			return;
		
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, true);
		if (null == clanInfo)
			return;
		
		SActivityOpen so = new SActivityOpen();
		so.activityid = 218;
		
		mkdb.Procedure.psendWhileCommit(roleId, so);
	}
}
