package fire.pb.instancezone.act;

import java.util.List;
import java.util.Map;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.npc.SSubmit2Npc;
import fire.pb.npc.SubmitType;
import mkdb.Transaction;

public class SubmitItemAct extends ServiceAct {
	private List<Integer> itemids;
	private int posnum;

	@SuppressWarnings("unchecked")
	public SubmitItemAct(int actId, Map<String, Object> params) {
		super(actId, params);
		itemids = (List<Integer>) params.get(ConfigParamName.SUBMIT_ITEM_IDS);
		posnum = (Integer) params.get(ConfigParamName.COUNT);
	}

	@Override
	public int getType() {
		return ActionConfig.TYPE_SUBMIT_ITEM;
	}

	@Override
	public boolean defaultprocess(InstanceZone instzone) {
		SSubmit2Npc snd = new SSubmit2Npc();
		snd.npckey = npckey;
		snd.questid = getActId();
		snd.submittype = SubmitType.GUILD_ZONE_ITEM;
		snd.availableids.addAll(itemids);
		snd.availablepos = posnum;
		if (Transaction.current() == null)
			gnet.link.Onlines.getInstance().send(roleId, snd);
		else
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		return true;
	}
}
