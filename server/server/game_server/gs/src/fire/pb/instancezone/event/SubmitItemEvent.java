package fire.pb.instancezone.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.instancezone.conf.EventConfig;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.item.ItemBase;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.npc.CSubmit2Npc;
import fire.pb.npc.SubmitType;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class SubmitItemEvent extends Event {
	private long roleId;
	private CSubmit2Npc protocol;

	private int npcid;
	private int submittype;
	private final java.util.ArrayList<fire.pb.npc.SubmitUnit> things;
	private long npckey;

	public SubmitItemEvent(long roleId, CSubmit2Npc protocol) {
		this.roleId = roleId;
		this.protocol = protocol;
		this.things = new ArrayList<fire.pb.npc.SubmitUnit>();
		this.things.addAll(protocol.things);
		this.npckey = protocol.npckey;
		getParams().put(ConfigParamName.ROLE_ID, roleId);
		getParams().put(ConfigParamName.NPC_KEY, npckey);

		Team team = TeamManager.getTeamByRoleId(roleId);
		if (team != null) {
			if (team.isTeamLeader(roleId)) {
				getParams().put(ConfigParamName.ROLE_IDS, team.getNormalMemberIds());
			}
		}

		if (npckey > 0) {
			Npc npc = SceneNpcManager.getNpcByKey(npckey);
			if (npc != null)
				getParams().put(ConfigParamName.NPC_ID, npc.getNpcID());
		}

		if (protocol.submittype == SubmitType.INSTANCE_ZONE_ITEM) {
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, true);
			List<Integer> itemids = new LinkedList<Integer>();
			for (fire.pb.npc.SubmitUnit unit : this.things) {
				ItemBase item = bag.getItem(unit.key);
				if (item == null)
					throw new IllegalArgumentException("RoleId = " + roleId
							+ "Key = " + unit.key + "没有这个物品，可能是物品刚移走");
				itemids.add(item.getItemId());
			}
			getParams().put(ConfigParamName.SUBMIT_ITEM_IDS, itemids);
		}
	}

	public SubmitItemEvent(final long roleid, final int npcid,
			final int submittype, final List<fire.pb.npc.SubmitUnit> things) {
		this.roleId = roleid;
		this.npcid = npcid;
		this.submittype = submittype;
		this.things = new ArrayList<fire.pb.npc.SubmitUnit>();
		this.things.addAll(things);

		getParams().put(ConfigParamName.ROLE_ID, roleId);
		if (this.npcid > 0) {
			getParams().put(ConfigParamName.NPC_ID, npcid);
		}

		if (this.submittype == SubmitType.INSTANCE_ZONE_ITEM) {
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, true);
			List<Integer> itemids = new LinkedList<Integer>();
			for (fire.pb.npc.SubmitUnit unit : things) {
				ItemBase item = bag.getItem(unit.key);
				if (item == null)
					throw new IllegalArgumentException("RoleId = " + roleId
							+ "Key = " + unit.key + "没有这个物品，可能是物品刚移走");
				itemids.add(item.getItemId());
			}
			getParams().put(ConfigParamName.SUBMIT_ITEM_IDS, itemids);
		}
	}

	public CSubmit2Npc getCSubmitNpc() {
		return protocol;
	}

	public java.util.ArrayList<fire.pb.npc.SubmitUnit> getThings() {
		return things;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	@Override
	public int getType() {
		return EventConfig.TYPE_SUBMIT_ITEMS;
	}

	@Override
	public boolean process(InstanceZone instzone) {
		if (instzone instanceof FactionInstZone) {
			if (((FactionInstZone) instzone).processSubmitItem(this))
				return processDefaultResults(instzone);
			return false;
		}

		return false;
	}

}
