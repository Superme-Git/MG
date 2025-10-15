package fire.pb.mission.instance;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.map.SceneNpcManager;
import fire.pb.npc.CSubmit2Npc;
import fire.pb.npc.SubmitUnit;
import mkdb.Procedure;

public class PSubmit2Npc extends Procedure {
	private final CSubmit2Npc protocol;
	private final long roleId;
	private int npcid; // npcid
	private java.util.ArrayList<fire.pb.npc.SubmitUnit> things; // 提交的道具key 或者宠物的key

	public PSubmit2Npc(long roleId, CSubmit2Npc protocol)
	{
		this.roleId = roleId;
		this.protocol = protocol;
		this.npcid = 0;
		this.things = new ArrayList<fire.pb.npc.SubmitUnit>();
		this.things.addAll(protocol.things);
	}
	
	public PSubmit2Npc(final long roleid, final int npcid, final int submittype, final List<fire.pb.npc.SubmitUnit> things) {
		this.roleId = roleid;
		this.npcid = npcid;
		this.things = new ArrayList<fire.pb.npc.SubmitUnit>();
		this.things.addAll(things);
		this.protocol = null;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(roleId);
		if (taskInfo == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId, 144838, null);
			return false;
		}
		
		xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(taskInfo.getLastinstanceid());
		if (info == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId, 144838, null);
			return false;
		}
		
		final long instanceOnlyId = info.getInstanceonlyid();
		xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceOnlyId);
		if (instInfo == null) {
			return false;
		}
		
		ServiceConfig serviceConfig = InstanceManager.getInstance().getServiceConfig(instInfo.getServiceid());
		if (serviceConfig == null)
			return false;
		
		@SuppressWarnings({"unchecked"})
		List<Integer> itemids = (List<Integer>)serviceConfig.params.get(ConfigParamName.SUBMIT_ITEM_IDS);
		
		List<Integer> subItemIds = new LinkedList<Integer>(); 
		Pack bag = new Pack(roleId, false);
		for (SubmitUnit unit : things) {
			ItemBase item = bag.getItem(unit.key);
			if (item == null)
				return false;
			
			subItemIds.add(item.getItemId());
		}
		
		//判断提交的物品是否符合要求
		if (!subItemIds.contains(itemids))
			return false;
		
		if (npcid == 0) {
			Long npckey = protocol.npckey;
			fire.pb.map.Npc npc = SceneNpcManager.selectNpcByKey(npckey);
			if (npc == null)
				return false;
			
			npcid = npc.getNpcID();
		}
		
		
		for (SubmitUnit unit : things) {
			ItemBase item = bag.getItem(unit.key);
			if (item == null)
				return false;
			
			int removed = bag.removeItemWithKey(unit.key, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, npcid, "submit item in instance");
			if (removed != 1)
				return false;
		}
		InstanceManager.getInstance().doNextStep(instInfo, instInfo.getServiceid(), instInfo.getInstanceid(), instanceOnlyId);
		return true;
	}

}
