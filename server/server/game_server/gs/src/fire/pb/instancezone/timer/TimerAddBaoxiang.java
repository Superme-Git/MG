package fire.pb.instancezone.timer;

import java.util.List;
import java.util.TimerTask;

import fire.pb.instancezone.Module;
import fire.pb.map.SceneNpcManager;
import fire.pb.npc.ChestLib;
import fire.pb.npc.ChestManager;
import fire.pb.npc.SBaoxiang;

public class TimerAddBaoxiang extends TimerTask {

	private final int baoxiangid;
	private final int baoxiangsize;
	private final int mapid;
	private final long landkey;
	private final String clanname;
	private final int instid;
	private final int baoxiangnumber;
	
	public TimerAddBaoxiang(final int baoxiangid, final int baoxiangsize,
			final int mapid, final long landkey,
			final String clanname, final int instid, final int baoxiangnumber) {
		this.baoxiangid = baoxiangid;
		this.baoxiangsize = baoxiangsize;
		this.mapid = mapid;
		this.landkey = landkey;
		this.clanname = clanname;
		this.instid = instid;
		this.baoxiangnumber = baoxiangnumber;
	}
	@Override
	public void run() {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				if (baoxiangsize == 0)
					return false;
				
				ChestLib chestlib = ChestManager.getInstance().getChestLib(baoxiangid);
				if (chestlib == null)
					return false;
				List<SBaoxiang> chestcfgs = chestlib.getRandomChests(baoxiangsize * baoxiangnumber / 100, true);
				if (chestcfgs.isEmpty())
					return false;

				for (SBaoxiang chestcfg : chestcfgs) {
					final long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();
					SceneNpcManager.createNpcByMap(npcKey, chestcfg.id, "",	mapid, landkey, 3, 0);
				}
				
				return true;
			}
			
		}.submit();
	}

}
