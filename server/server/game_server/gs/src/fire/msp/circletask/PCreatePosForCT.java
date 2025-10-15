

package fire.msp.circletask;

import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.CircTaskConf;
import fire.pb.circletask.CircleTaskManager;
import mkdb.Procedure;


public class PCreatePosForCT extends Procedure {

	private final long roleid;
	private final long npckey;

	private final int mapid;

	private final int posx;

	private final int posy;
	
	private final int ctconfid;
	
	private final int dstnpcid;
	
	private final int cttype;
	
	private final boolean isnpcgive;

	public PCreatePosForCT(long roleid,long npckey, int mapid, int posx, int posy, int ctconfid, int dstnpcid, int cttype, boolean isnpcgive) {

		super();
		this.roleid = roleid;
		this.npckey = npckey;
		this.mapid = mapid;
		this.posx = posx;
		this.posy = posy;
		this.ctconfid = ctconfid;
		this.dstnpcid = dstnpcid;
		this.cttype = cttype;
		this.isnpcgive = isnpcgive;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleid);
		if (null == quest_map) {
			quest_map = xbean.Pod.newCircleTaskMap();
			xtable.Rolecircletask.insert(roleid, quest_map);
		}
		xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(cttype);
		if (null == sqinfo) {
			sqinfo = xbean.Pod.newCircleTaskInfo();
			quest_map.getTaskmap().put(cttype, sqinfo);
		}
		
		CircTaskConf ctc = CircleTaskManager.getCircTaskConf(ctconfid);
		if(ctc == null){
			return false;
		}
		
		boolean giveSchoolquestResult = false;
		switch (ctc.cttype) {
			case CircTaskClass.CircTask_Mail: {
				giveSchoolquestResult = CircleTaskManager.getInstance().giveRoleCTMailGoto(roleid, sqinfo, posx,posy, isnpcgive);
				break;
			}
			case CircTaskClass.CircTask_ItemUse: {
				giveSchoolquestResult = CircleTaskManager.getInstance().giveRoleCTItemUseGoto(roleid, sqinfo, posx,posy, isnpcgive);
				break;
			}
			case CircTaskClass.CircTask_ItemCollect: {
				giveSchoolquestResult = CircleTaskManager.getInstance().giveRoleCTItemCollectGoto(roleid, sqinfo, posx,posy, isnpcgive);
				break;
			}
			case CircTaskClass.CircTask_ItemFind: {
				giveSchoolquestResult = CircleTaskManager.getInstance().giveRoleCTItemFindGoto(roleid, sqinfo, posx,posy, isnpcgive);
				break;
			}
			case CircTaskClass.CircTask_PetCatch: {
				giveSchoolquestResult = CircleTaskManager.getInstance().giveRoleCTPetCatchGoto(roleid, sqinfo, posx,posy, isnpcgive);
				break;
			}
			case CircTaskClass.CircTask_Patrol: {
				giveSchoolquestResult = CircleTaskManager.getInstance().giveRoleCTPatrolGoto(roleid, sqinfo, posx,posy, isnpcgive);
				break;
			}
			case CircTaskClass.CircTask_KillMonster: {
				giveSchoolquestResult = CircleTaskManager.getInstance().giveRoleCTKillMonsterGoto(roleid, sqinfo, posx, posy, isnpcgive);
				break;	
			}
		}

		return true;
	}

	public long getRoleid() {
		return roleid;
	}

	public long getNpckey() {
		return npckey;
	}

	public int getMapid() {
		return mapid;
	}

	public int getPosx() {
		return posx;
	}

	public int getPosy() {
		return posy;
	}

	public int getCtconfid() {
		return ctconfid;
	}

	public int getDstnpcid() {
		return dstnpcid;
	}

	public int getCttype() {
		return cttype;
	}

	public boolean isIsnpcgive() {
		return isnpcgive;
	}
	
	

}
