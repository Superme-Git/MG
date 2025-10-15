package fire.pb.instancezone;

import fire.pb.battle.BasicEndHandler;
import fire.pb.instancezone.event.BattleEndEvent;

public class InstZoneBattleEndHandler extends BasicEndHandler {
	protected final long landkey;
	protected final long npckey;

	public InstZoneBattleEndHandler(final long landkey, final long npckey) {
		this.landkey = landkey;
		this.npckey = npckey;
	}

	public long getInstZoneKey() {
		return landkey;
	}

	public long getNpcKey() {
		return npckey;
	}

	@Override
	public boolean handleDeath() {
		super.handleDeath();
		Module.getInstance().setDynamicNpcState(landkey, npckey, 0);

		return true;
	}

	@Override
	public boolean handleAward() {
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(landkey,
				false);
		if (instzone == null)
			return true;
		instzone.processEvent(new BattleEndEvent(getBattle(), npckey));
		return true;
	}

}
