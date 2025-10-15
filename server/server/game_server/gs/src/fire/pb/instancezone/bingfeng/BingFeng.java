package fire.pb.instancezone.bingfeng;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.instancezone.event.Event;
import fire.pb.instancezone.event.OtherEvent;
import fire.pb.npc.SHireNpc;

public class BingFeng extends InstanceZone {

	private xbean.BingFengWangZuo xbingfengwangzuo;

	public BingFeng(long landkey, xbean.InstanceZone xzone,
			InstanceZoneConfig zoneconfig, boolean readonly) {
		super(landkey, xzone, zoneconfig, readonly);

		if (xzone.getExtid() > 0) {
			if (readonly)
				xbingfengwangzuo = xtable.Bingfengwangzuos.select(xzone.getExtid());
			else
				xbingfengwangzuo = xtable.Bingfengwangzuos.get(xzone.getExtid());
		}

		if (xbingfengwangzuo == null) {
			xbingfengwangzuo = xbean.Pod.newBingFengWangZuo();
			Long key = null;
			if (!readonly)
				key = xtable.Bingfengwangzuos.insert(xbingfengwangzuo);
			if (key == null)
				throw new IllegalArgumentException();
			xzone.setExtid(key);
		}
	}

	public xbean.BingFengWangZuo getBingFengBean() {
		return xbingfengwangzuo;
	}

	@Override
	public boolean processOtherEvent(OtherEvent e) {
		return true;
	}

	@Override
	public boolean afterEventProcess(Event event) {
		return super.afterEventProcess(event);
	}

	@Override
	protected void onLeave(long roleId) {
		super.onLeave(roleId);
		BuffAgent buffrole = new BuffRoleImpl(roleId, false);
		for (int buffId : buffrole.getAllBuffBeans().keySet()) {
			SHireNpc shirenpc = fire.pb.battle.Module.getInstance()
					.getSHireNpcByBuffId(buffId);
			if (shirenpc == null)
				continue;
			buffrole.removeCBuffWithSP(buffId);
		}
	}

	@Override
	public void onSaveCompleted(int saveId) {
		super.onSaveCompleted(saveId);
		updateRank();
	}

	@Override
	protected void onDestroy(boolean shutdown) {
		xtable.Bingfengwangzuos.remove(getZoneBean().getExtid());
	}

	@Override
	public boolean enter(long roleId, boolean autoSync) {
		return false;
	}

	public void updateRank() {

	}
}
