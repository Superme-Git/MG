package fire.pb.instancezone.conf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.instance.SInstanceSave;
import fire.pb.instancezone.InstanceText;
import fire.pb.instancezone.Module;

public class InstanceSaveConfig {
	public final SInstanceSave sFubenjindu;

	public final int saveId;
	public final String name;

	public final int landId;
	public final int mapId;
	public final int gposx;
	public final int gposy;
	public final boolean canSync;
	public final int maxscore;
	public final Map<Integer, SubSaveConfig> subsaves = new HashMap<Integer, SubSaveConfig>();
	public final List<List<Integer>> activeReqSaves = new LinkedList<List<Integer>>();
	public final List<List<Integer>> completeReqSubsaves = new LinkedList<List<Integer>>();
	public final int scoreAwardId;
	public final int chestLibId;
	public final int chestNum;
	public final int activeCG;
	public final int endCG;
	
	public final int haveboss;
	public final int pastexpaward;
	public final int pastaward;
	public final int mendaward;

	public InstanceSaveConfig(SInstanceSave sFubenjindu) {
		this.sFubenjindu = sFubenjindu;
		this.saveId = sFubenjindu.getJinduid();
		this.name = sFubenjindu.name;
		this.landId = sFubenjindu.getBelongfuben();
		if (sFubenjindu.gotoposition != null) {
			String[] strs = sFubenjindu.gotoposition.split(",");
			this.mapId = Integer.valueOf(strs[0]);
			this.gposx = Integer.valueOf(strs[1]);
			this.gposy = Integer.valueOf(strs[2]);
		} else {
			this.mapId = -1;
			this.gposx = -1;
			this.gposy = -1;
		}
		this.canSync = (sFubenjindu.getIsstep() == 1);
		this.maxscore = sFubenjindu.maxpoint;
		this.chestLibId = sFubenjindu.chestlibid;
		this.chestNum = sFubenjindu.chestnum;
		this.scoreAwardId = sFubenjindu.awardid;
		if (sFubenjindu.activeif != null)
			for (String activereqs : sFubenjindu.activeif) {
				if (activereqs == null || activereqs.equals(""))
					continue;
				String[] activestrs = activereqs.split(";");
				if (activestrs.length == 0)
					continue;
				List<Integer> reqsaves = new LinkedList<Integer>();
				for (String str : activestrs)
					reqsaves.add(Float.valueOf(str).intValue());
				activeReqSaves.add(reqsaves);
			}

		if (sFubenjindu.endif != null)
			for (String activereqs : sFubenjindu.endif) {
				if (activereqs == null || activereqs.equals(""))
					continue;
				String[] activestrs = activereqs.split(";");
				if (activestrs.length == 0)
					continue;
				List<Integer> reqsaves = new LinkedList<Integer>();
				for (String str : activestrs)
					reqsaves.add(Float.valueOf(str).intValue());
				completeReqSubsaves.add(reqsaves);
			}

		activeCG = sFubenjindu.activeCG;
		endCG = sFubenjindu.endCG;
		haveboss = sFubenjindu.haveboss;
		pastaward = sFubenjindu.pastaward;
		mendaward = sFubenjindu.mendaward;
		pastexpaward = sFubenjindu.pastexpaward;
	}

	public boolean canActive(Map<Integer, xbean.InstanceSave> xsaves) {
		if (xsaves == null || xsaves.isEmpty())
			return false;

		for (List<Integer> activecondition : activeReqSaves) {
			if (activecondition.isEmpty())
				continue;
			boolean alltrue = true;
			for (Integer reqSave : activecondition) {
				xbean.InstanceSave save = xsaves.get(reqSave);
				if (save == null) {
					alltrue = false;
					break;
				}
				if (save.getState() != InstanceText.COMPLETE) {
					alltrue = false;
					break;
				}
			}
			if (alltrue)
				return true;
		}
		return false;
	}

	public boolean canComplete(xbean.InstanceSave xsave) {
		if (xsave == null)
			return false;
		for (List<Integer> completecondition : completeReqSubsaves) {
			if (completecondition.isEmpty())
				continue;
			boolean alltrue = true;
			for (Integer reqSubSaveId : completecondition) {
				if (!Module.isSubSaveCompleted(xsave, this, reqSubSaveId)) {
					alltrue = false;
					break;
				}
			}
			if (alltrue)
				return true;
		}
		return false;
	}

}
