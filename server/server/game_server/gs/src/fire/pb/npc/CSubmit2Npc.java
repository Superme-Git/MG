package fire.pb.npc;

import java.util.HashMap;
import java.util.Map;

import fire.log.Logger;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.MissionColumn;
import fire.pb.mission.PCommitMajorMission;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSubmit2Npc__ extends mkio.Protocol { }

/** 客户端请求提交任务物品或者任务宠物
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSubmit2Npc extends __CSubmit2Npc__ {
	static fire.log.Logger logger = Logger.getLogger("MAPMAIN");

	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		if (!SceneNpcManager.checkDistance(npckey, roleid)) {
			if (submittype == SubmitType.GUILD_ZONE_ITEM) {
				fire.pb.talk.STransChatMessageNotify2Client msg = MessageMgr.getMsgNotify(
						160176, 0, null);
				gnet.link.Onlines.getInstance().send(roleid, msg);
			}
			return;
		}
		Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		int npcid = 0;
		if (npc != null)
			npcid = npc.getNpcID();
		fire.pb.item.Pack submiterBag = new fire.pb.item.Pack(roleid, true);
		for (SubmitUnit unit : things) {
			if (submittype == SubmitType.PET)
				continue;
			if (submittype == SubmitType.MONEY) {
				long money = unit.key;
				if (submiterBag.getMoney() < money)
					logger.error("submit money error.roleid:" + roleid
							+ "unit.key:" + unit.key + "unit.num:" + unit.num
							+ "money:" + money + "npcid:" + npcid);
				continue;
			} else {
				ItemBase bi = submiterBag.getItem(unit.key);
				if (bi == null) {
					logger.error("submit item is empty.roleid:" + roleid
							+ "unit.key:" + unit.key + "unit.num:" + unit.num
							+ "npcid:" + npcid);
					continue;
				}
				if (bi.getNumber() < unit.num) {
					logger.error("submit item num error.roleid:" + roleid
							+ "unit.key" + unit.key + "unit.num:" + unit.num
							+ "realnum:" + bi.getNumber() + "npcid:" + npcid);
					continue;
				}
			}
		}
		// 对提交的宠物的判断，不能是参战和展示的宠物
		java.util.List<xbean.PetInfo> pis = null;
		if (submittype == SubmitType.PET) {
			pis = new java.util.ArrayList<xbean.PetInfo>();
			final fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(
					roleid, PetColumnTypes.PET, true);
			for (SubmitUnit unit : things) {
				fire.pb.pet.Pet pet = petcol.getPet(unit.key);
				if (pet == null || pet.isLocked() != -1)
					return;
				if (petcol.petIsFightPet(unit.key)
						|| petcol.petIsShowPet(unit.key)) {
					MessageMgr.sendMsgNotify(roleid, 144418, null);
					return;
				}
				pis.add(pet.getPetInfo().copy());
			}
		}
		// 对提交物品的判断，不能有锁
		if (submittype == SubmitType.ITEM) {
			Pack bag = new Pack(roleid, true);
			for (SubmitUnit unit : things) {
				ItemBase item = bag.getItem(unit.key);
				if (item == null)
					return;
			}
		}

		if (submittype == SubmitType.INSTANCE_ZONE_ITEM) {// 副本玩法常规提交物品
			new fire.pb.instancezone.PSubmit2Npc(roleid, this).submit();
		}
		if (submittype == SubmitType.GUILD_ZONE_ITEM) {
			new fire.pb.instancezone.PSubmit2Npc(roleid, this).submit();
		}

		// 如果是剧情任务
		if (fire.pb.mission.UtilHelper.isMajorScenarioMission(questid)
				|| fire.pb.mission.UtilHelper.isBranchScenarioMission(questid)) {
			// if ( things.size() != 1 )
			// return;

			if (submittype == SubmitType.ITEM) {
				final Map<Integer, Integer> items = new HashMap<Integer, Integer>();
				for (fire.pb.npc.SubmitUnit su : things) {
					Integer num = items.get(su.key);
					if (num == null) {
						items.put(su.key, su.num);
					} else {
						items.put(su.key, su.num + num);
					}
				}
				// fire.pb.team.Team team =
				// fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
				// if (team == null || team.isAbsentMember(roleid))
				// new fire.pb.mission.PCommitMajorMission(roleid, questid, new
				// fire.pb.mission.ItemCommitParam(npckey, items),
				// true).submit();
				// else if (team.isTeamLeader(roleid)) {
				// for (long memid : team.getNormalMemberIds()) {
				// if (memid == roleid) {
				// new fire.pb.mission.PCommitMajorMission(memid, questid, new
				// fire.pb.mission.ItemCommitParam(npckey, items),
				// true).submit();
				// } else {
				// new fire.pb.mission.PCommitMajorMission(memid, questid, new
				// fire.pb.mission.AutoCommitItemParam(npckey),
				// false).submit();
				// }
				// }
				// }

				// new fire.pb.mission.PTaskProcedure() {
				// @Override
				// protected boolean taskProcess() {
				// return new fire.pb.mission.TaskScenarioColumn( roleid, this
				// ).commitMission(
				// questid, npckey, new fire.pb.mission.ItemCommitParam(
				// npckey, items ) );
				// }
				// }.submit();
			} else if (submittype == SubmitType.PET) {
				if (things.size() != 1)
					return;
				final fire.pb.npc.SubmitUnit su = things.get(0);
				final int petkey = su.key;
				fire.pb.team.Team team = fire.pb.team.TeamManager
						.selectTeamByRoleId(roleid);
				if (team == null || team.isAbsentMember(roleid))
					mkdb.Procedure.execute(new PCommitMajorMission(roleid,
							questid, new fire.pb.mission.util.PetCommitParam(
									npckey, petkey), true),
							new DoneWhileSubmitPet<PCommitMajorMission>(roleid,
									questid, pis));
				else if (team.isTeamLeader(roleid)) {
					int teamshare = new MissionColumn(roleid, true).getMission(
							questid).getConf().exeIndo.share;
					if (teamshare == 0) {
						// 不共享
						mkdb.Procedure.execute(new PCommitMajorMission(roleid,
								questid, new fire.pb.mission.util.PetCommitParam(
										npckey, petkey), true),
								new DoneWhileSubmitPet<PCommitMajorMission>(
										roleid, questid, pis));
					} else {
						for (long memid : team.getNormalMemberIds()) {
							if (memid == roleid) {
								mkdb.Procedure
										.execute(
												new PCommitMajorMission(
														memid,
														questid,
														new fire.pb.mission.util.PetCommitParam(
																npckey, petkey),
														true),
												new DoneWhileSubmitPet<PCommitMajorMission>(
														memid, questid, pis));
							} else {
								mkdb.Procedure
										.execute(
												new PCommitMajorMission(
														memid,
														questid,
														new fire.pb.mission.util.PetCommitParam(
																npckey, petkey),
														false),
												new DoneWhileSubmitPet<PCommitMajorMission>(
														memid, questid, pis));
							}
						}
					}
				}
				// new fire.pb.mission.PTaskProcedure() {
				// @Override
				// protected boolean taskProcess() {
				// return new fire.pb.mission.TaskScenarioColumn( roleid, this
				// ).commitMission(
				// questid, npckey, new fire.pb.mission.PetCommitParam(petkey)
				// );
				// }
				// }.submit();
			} else if (submittype == SubmitType.MONEY) {
				if (things.size() != 1)
					return;
				final fire.pb.npc.SubmitUnit su = things.get(0);
				final long money = su.key;
				fire.pb.team.Team team = fire.pb.team.TeamManager
						.selectTeamByRoleId(roleid);
				if (team == null || team.isAbsentMember(roleid))
					new fire.pb.mission.PCommitMajorMission(
							roleid,
							questid,
							new fire.pb.mission.util.MoneyCommitParam(npckey, money),
							true).submit();
				else if (team.isTeamLeader(roleid)) {
					for (long memid : team.getNormalMemberIds()) {
						if (memid == roleid) {
							new fire.pb.mission.PCommitMajorMission(memid,
									questid,
									new fire.pb.mission.util.MoneyCommitParam(
											npckey, money), true).submit();
						} else {
							new fire.pb.mission.PCommitMajorMission(memid,
									questid,
									new fire.pb.mission.util.MoneyCommitParam(
											npckey, money), false).submit();
						}
					}
				}
			}
			return;
		}
		if (CircleTaskManager.getInstance().getCircTaskTypes()
				.contains(new Integer(questid))) {
			fire.pb.circletask.PSubmitItemPetQuest proc = new fire.pb.circletask.PSubmitItemPetQuest(
					roleid, questid, npckey, things);
			mkdb.Procedure
					.execute(
							proc,
							new DoneWhileSubmitPet<fire.pb.circletask.PSubmitItemPetQuest>(
									roleid, questid, pis));
			return;
		}
	}


	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795456;

	public int getType() {
		return 795456;
	}

	public int questid; // 提交物品相关的任务或者其他相关id
	public long npckey; // npckey
	public int submittype; // 提交的类型
	public java.util.ArrayList<fire.pb.npc.SubmitUnit> things; // 提交的道具key 或者宠物的key

	public CSubmit2Npc() {
		things = new java.util.ArrayList<fire.pb.npc.SubmitUnit>();
	}

	public CSubmit2Npc(int _questid_, long _npckey_, int _submittype_, java.util.ArrayList<fire.pb.npc.SubmitUnit> _things_) {
		this.questid = _questid_;
		this.npckey = _npckey_;
		this.submittype = _submittype_;
		this.things = _things_;
	}

	public final boolean _validator_() {
		if (questid < 0) return false;
		if (npckey < 0) return false;
		if (submittype < 0) return false;
		for (fire.pb.npc.SubmitUnit _v_ : things)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		_os_.marshal(npckey);
		_os_.marshal(submittype);
		_os_.compact_uint32(things.size());
		for (fire.pb.npc.SubmitUnit _v_ : things) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		submittype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.npc.SubmitUnit _v_ = new fire.pb.npc.SubmitUnit();
			_v_.unmarshal(_os_);
			things.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSubmit2Npc) {
			CSubmit2Npc _o_ = (CSubmit2Npc)_o1_;
			if (questid != _o_.questid) return false;
			if (npckey != _o_.npckey) return false;
			if (submittype != _o_.submittype) return false;
			if (!things.equals(_o_.things)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		_h_ += (int)npckey;
		_h_ += submittype;
		_h_ += things.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(submittype).append(",");
		_sb_.append(things).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
