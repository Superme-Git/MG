package fire.pb.instancezone.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.instance.SInstaceNpc;
import fire.pb.instancezone.Module;
import fire.pb.mission.instance.LevelGroupAward;
import fire.pb.util.Misc;

public class NpcConfig {
	public final SInstaceNpc sFubennpc;

	public final int npcbaseId;

	public final int landId;

	public final int initstate;
	public final int npctype;
	public final int npcminnumber;
	public final int npcmaxnumber;
	public final int rolenumber;
	public final int baoxiangid;
	public final int baoxiangnumber;
	public final int awardid;
	public final int actid;
	public final int posx;
	public final int posy;
	public final int matchtype;
	public final int multibattle;
	public final List<Integer> saveId = new ArrayList<Integer>();

	public final Map<TriggerCondition, Integer> changestates = new HashMap<TriggerCondition, Integer>();
	public List<LevelGroupAward> groupAward = new ArrayList<LevelGroupAward>();

	public NpcConfig(SInstaceNpc sFubennpc) {
		this.sFubennpc = sFubennpc;
		this.npcbaseId = sFubennpc.id;
		this.landId = sFubennpc.belongfuben;
		this.initstate = sFubennpc.friststate;
		this.npctype = sFubennpc.npctype;
		this.npcminnumber = sFubennpc.npcminnumber;
		this.npcmaxnumber = sFubennpc.npcmaxnumber;
		this.rolenumber = sFubennpc.rolenumber;
		this.baoxiangid = sFubennpc.baoxiangid;
		this.baoxiangnumber = sFubennpc.baoxingnumber;
		this.awardid = sFubennpc.awardid;
		this.actid = sFubennpc.actId;
		this.posx = sFubennpc.posx;
		this.posy = sFubennpc.posy;
		this.matchtype = sFubennpc.matchtype;
		this.multibattle = sFubennpc.multibattle;

		if (sFubennpc.changestate != null)
			for (String changestate : sFubennpc.changestate) {
				String[] strs = changestate.split("=");
				changestates.put(new TriggerCondition(strs[0]),
						Integer.valueOf(strs[1]));
			}

		if (sFubennpc.jinduidlist != null) {
			String[] strJindu = sFubennpc.jinduidlist.split(";");
			for (String jindu : strJindu) {
				saveId.add(Integer.parseInt(jindu));
			}
		}

		InitGroupAward(sFubennpc.getAwardids());
	}

	public Integer getSaveChange(SaveCondition condition) {
		Set<Integer> changes = new HashSet<Integer>();
		Integer state = null;
		for (Map.Entry<TriggerCondition, Integer> entry : changestates
				.entrySet()) {
			if (condition.trigger(entry.getKey())) {
				state = entry.getValue();
				changes.add(entry.getValue());
			}
		}
		if (changes.size() > 1)
			Module.logger.error("在SaveCondition为 ：" + condition + "时，NPC："
					+ npcbaseId + "可能有多种状态变化");
		return state;
	}

	public Integer getNpcNumber() {
		int nNumber = Misc.getRandomBetween(npcminnumber, npcmaxnumber);
		return nNumber;
	}

	private void InitGroupAward(final String awardstring) {
		if (awardstring != null) {
			String[] awardLst = awardstring.split(";");
			for (String awardStr : awardLst) {
				String[] awardSub = awardStr.split(",");
				LevelGroupAward group = new LevelGroupAward(npcbaseId,
						Integer.parseInt(awardSub[0]),
						Integer.parseInt(awardSub[1]),
						Integer.parseInt(awardSub[2]));

				groupAward.add(group);
			}
		}
	}
}
