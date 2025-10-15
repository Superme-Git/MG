
package fire.pb.npc;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.clanfight.ActivityClanFightManager;
import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;






// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __COpenChest__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class COpenChest extends __COpenChest__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId == -1)
			return;
		
		final fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (null == role)
			return;
		
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleId, true);
		if (instzone != null) {
			InstanceZoneConfig zoneconfig = fire.pb.instancezone.Module.getInstance().getInstanceZoneConfigs().get(instzone.getInstZoneId());
			if (zoneconfig != null) {
				long now = System.currentTimeMillis();
				if (zoneconfig.inEnterTime(now)) {
					new mkdb.Procedure() {
						protected boolean process() throws Exception {
							InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleId, false);
							if (instzone instanceof FactionInstZone) {
								return ((FactionInstZone) instzone).openChest(roleId, chestnpckey);
							}

							return false;
						};
					}.submit();
					
					return;
				}
			}
			
			return;
		}

		if (!fire.pb.map.SceneNpcManager.checkDistance(chestnpckey, roleId))
			return;
		
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				Npc npc = SceneNpcManager.getNpcByKey(chestnpckey);
				if (npc == null)
					return false;
				
				SBaoxiang chestcfg = ChestManager.getInstance().getChestConfig(npc.getNpcID());
				if (chestcfg == null)
					return false;
				
//////////////////////////////////////////////
				//胜利者才能拿奖励 by changhao
				Long clanfightid = xtable.Roleid2clanfightid.select(roleId);
				if (clanfightid != null && npc.getNpcID() == ActivityClanFightManager.VICTORY_BOX)
				{
					fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, true);
					if (bf != null)
					{
						if (!bf.getClanfightBean().getEnterroleids().containsKey(roleId))
						{
							return false;
						}
						
						boolean ok = bf.IsWinnerSide(roleId, true);
						if (ok == true)
						{
							mkdb.Procedure.pexecuteWhileCommit(new fire.pb.clan.fight.PClanFightOpenChest(clanfightid, roleId, chestnpckey));
							return true;
						}
					}
					
					return false;
				}
				
				xbean.Properties prop = xtable.Properties.select(roleId);
				if (prop == null)
				{
					return false;
				}
				
				int mapid = role.getMapId();
				if (mapid == ClanUtils.MAPID && npc.getNpcID() == ActivityClanFightManager.CELEBRATE_BOX) //如果玩家在公会领地 by changhao
				{
					xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, true);
					if (clanInfo == null)
					{
						return false;
					}
						
					long clanid = clanInfo.getKey();
					xbean.EClanfightids ids = xtable.Clanid2clanfightids.select(clanid);
					for (Long i : ids.getIds())
					{
						fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(i, true);
						if (bf != null)
						{
							long day1 = 24 * 60 * 60 * 1000;
							long cur = System.currentTimeMillis();
							long t = bf.getClanfightBean().getDatetime() + day1;
							
							if(DateValidate.inTheSameDay(t, cur))
							{
								if (!bf.getClanfightBean().getEnterroleids().containsKey(roleId))
								{
									MessageMgr.sendMsgNotify(roleId, 410050, null); //你没有资格拾取 by changhao	
									return false;
								}
								
								boolean ok = bf.IsWinnerSide(roleId, true);
								if (ok == true)
								{
									mkdb.Procedure.pexecuteWhileCommit(new fire.pb.clan.fight.PClanFightOpenChest(bf.getClanfightBean().getClanfightid(), roleId, chestnpckey));
									return true;
								}
								
								MessageMgr.sendMsgNotify(roleId, 410050, null); //你没有资格拾取 by changhao	
								return false;
							}
						}
					}
					
					MessageMgr.sendMsgNotify(roleId, 410050, null); //你没有资格拾取 by changhao	
					return false;
				}					
/////////////////////////////////////////////
				
				int biaoId = TimerNpcService.getInstance().getEventId(npc.getNpcID());
				
				int opentimes = chestcfg.getOpentimes();
				
				final long currentTime = System.currentTimeMillis();
				
				fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
				if (null != team && team.isTeamLeader(roleId) && chestcfg.getOpenteam() == 1) {
					// 全队正常状态成员 加锁
					final java.util.List<Long> members = team.getNormalMemberIds();
					this.lock(xtable.Locks.ROLELOCK, members);
					
					{
						xbean.BaoXiangInfo baoxiangInfo = xtable.Rolekaibaoxiang.select(roleId);
						if (baoxiangInfo != null) {
							if (DateValidate.inTheSameDay(baoxiangInfo.getLastopentime(), currentTime)) {
									if (baoxiangInfo.getOpentimes() >= opentimes) {
										MessageMgr.psendMsgNotifyWhileRollback(roleId, 166004, null);
										return false;
									}
							}	
						}
						int rolelevel = xtable.Properties.selectLevel(roleId);
						if (chestcfg.openlevel > rolelevel) {
							//等级不足的提示
							MessageMgr.sendMsgNotify(roleId, 166139, null);
							return false;
						}
					}
					
					for (Long rid : members) {
						xbean.BaoXiangInfo baoxiangInfo = xtable.Rolekaibaoxiang.get(rid);
						if (baoxiangInfo == null) {
							baoxiangInfo = xbean.Pod.newBaoXiangInfo();
							xtable.Rolekaibaoxiang.insert(rid, baoxiangInfo);
						}
						if (!DateValidate.inTheSameDay(baoxiangInfo.getLastopentime(), currentTime)) {
							baoxiangInfo.setOpentimes(0);
						}
						
						if (baoxiangInfo.getOpentimes() >= opentimes) {
							MessageMgr.psendMsgNotify(rid, 166003, null);
							continue;
						}
						
						// 给予奖励
						RewardMgr.getInstance().distributeAllAward(rid,
								chestcfg.rewardid, null,
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_baoxiang, 0,
								PAddExpProc.OTHER_QUEST, "世界宝箱");
						
						baoxiangInfo.setOpentimes(baoxiangInfo.getOpentimes() + 1);
						baoxiangInfo.setLastopentime(currentTime);
					}
				}
				else {
					int rolelevel = xtable.Properties.selectLevel(roleId);
					if (chestcfg.openlevel > rolelevel) {
						//等级不足的提示
						MessageMgr.sendMsgNotify(roleId, 166139, null);
						return false;
					}
					
					if (npc.getNpcID() == 300002) {
						xbean.BestowNpcInfo npcInfo = xtable.Bestownpc.get(chestnpckey);
						if (npcInfo == null)
							return false;
						
						xbean.RoleBestowInfo bestowInfo = xtable.Rolebestow.get(roleId);
						if (bestowInfo == null) {
							bestowInfo = xbean.Pod.newRoleBestowInfo();
							xtable.Rolebestow.insert(roleId, bestowInfo);
						}
						
						long npcRoleId = npcInfo.getRoleid();
						xbean.RoleBestowCount bestowCount = bestowInfo.getRolebestowinfo().get(npcInfo.getRoleid());
						if (bestowCount == null) {
							bestowCount = xbean.Pod.newRoleBestowCount();
							bestowInfo.getRolebestowinfo().put(npcRoleId, bestowCount);
						}
						if (bestowCount.getOpentimes() >= opentimes) {
							MessageMgr.psendMsgNotifyWhileRollback(roleId, chestcfg.getOpenovermsg(), null);
							return false;
						}
						
						// 给予奖励
						RewardMgr.getInstance().distributeAllAward(roleId,
								chestcfg.rewardid, null,
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_baoxiang, 0,
								PAddExpProc.OTHER_QUEST, "世界宝箱");
						
						bestowCount.setOpentimes(bestowCount.getOpentimes() + 1);
					
					}
					else {
						xbean.BaoXiangInfo baoxiangInfo = xtable.Rolekaibaoxiang.get(roleId);
						if (baoxiangInfo == null) {
							baoxiangInfo = xbean.Pod.newBaoXiangInfo();
							xtable.Rolekaibaoxiang.insert(roleId, baoxiangInfo);
						}
						
						if (!DateValidate.inTheSameDay(baoxiangInfo.getLastopentime(), currentTime)) {
							baoxiangInfo.setOpentimes(0);
						}
						
						if (baoxiangInfo.getOpentimes() >= opentimes) {
							MessageMgr.psendMsgNotifyWhileRollback(roleId, chestcfg.getOpenovermsg(), null);
							return false;
						}
						
						// 给予奖励
						RewardMgr.getInstance().distributeAllAward(roleId,
								chestcfg.rewardid, null,
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_baoxiang, 0,
								PAddExpProc.OTHER_QUEST, "世界宝箱");
						
						baoxiangInfo.setOpentimes(baoxiangInfo.getOpentimes() + 1);
						baoxiangInfo.setLastopentime(currentTime);
					}
				}
				
				TimerNpcService.getInstance().removeEventNpc(biaoId, chestnpckey);
				return true;
			}
			
		}.submit();
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795522;

	public int getType() {
		return 795522;
	}

	public long chestnpckey; // 宝箱的npckey

	public COpenChest() {
	}

	public COpenChest(long _chestnpckey_) {
		this.chestnpckey = _chestnpckey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(chestnpckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		chestnpckey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof COpenChest) {
			COpenChest _o_ = (COpenChest)_o1_;
			if (chestnpckey != _o_.chestnpckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)chestnpckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(chestnpckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(COpenChest _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(chestnpckey - _o_.chestnpckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

