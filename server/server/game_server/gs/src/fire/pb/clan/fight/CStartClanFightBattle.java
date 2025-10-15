
package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CStartClanFightBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 开始公会战 battle PK by changhao
 * @author Administrator
 *
 */
public class CStartClanFightBattle extends __CStartClanFightBattle__ {
	@Override
	protected void process() {
		// protocol handle
				
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		mkdb.Procedure startclanfightbattle = new mkdb.Procedure()
		{
			@Override
			protected boolean process()  throws Exception
			{
				long cur = System.currentTimeMillis();
				
				Long battleid = xtable.Roleid2battleid.select(roleid);
				if (battleid != null)
				{
					String srcname = xtable.Properties.selectRolename(roleid);
					String targetname = xtable.Properties.selectRolename(targetid);
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ") " + srcname + " 角色(ID = " + targetid + ") " + targetname + " 在战斗中");
					return false;
				}
				
				Long targetbattleid = xtable.Roleid2battleid.select(targetid);
				if (targetbattleid != null)
				{
					String srcname = xtable.Properties.selectRolename(roleid);
					String targetname = xtable.Properties.selectRolename(targetid);
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ") " + srcname + " 角色(ID = " + targetid + ") " + targetname + " 目标在战斗中");
					return false;
				}
				
				Long c1 = xtable.Roleid2clanfightid.select(roleid); //检测是否在战场中 by changhao
				if (c1 == null)
				{
					String srcname = xtable.Properties.selectRolename(roleid);
					String targetname = xtable.Properties.selectRolename(targetid);
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ") " + srcname + " 角色(ID = " + targetid + ") " + targetname + " 不在战场");

					return false;
				}
				
				Long c2 = xtable.Roleid2clanfightid.select(targetid); //检测是否在战场中 by changhao
				if (c2 == null)
				{
					String srcname = xtable.Properties.selectRolename(roleid);
					String targetname = xtable.Properties.selectRolename(targetid);
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ") " + srcname + " 角色(ID = " + targetid + ") " + targetname + " 目标不在战场");

					return false;
				}
				
				if (!c1.equals(c2)) //是否在同一公会战里 by changhao
				{
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ")同一公会");
					return false;
				}
				
				xbean.EClanFightStatistics statistics1 = xtable.Roleid2clanfightstatistics.select(roleid);
				if (statistics1 == null)
				{
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ")没有数据信息");
					return false;
				}
				
				xbean.EClanFightStatistics statistics2 = xtable.Roleid2clanfightstatistics.select(targetid);
				if (statistics2 == null)
				{
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + targetid + ")目标没有数据信息");
					return false;
				}
				
				if (cur < statistics2.getLastlosestamp() + 10 * 1000)
				{
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 410062, null);
					return false;
				}
				
				if (statistics2.getAct() <= 0)
				{
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ")目标没有行动力 ");
					return false;
				}
				
				if (statistics1.getAct() < fire.pb.activity.clanfight.ActivityClanFightManager.ATTACK_SUB_ACT)
				{
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 410056, null);
					return false;
				}
				
				int which1 = -1;
				int which2 = -1;
				fire.pb.clan.fight.ClanFightBattleField bf1 = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(c1, true);
				if (bf1 == null)
				{
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ")战场不存在");
					return false;
				}
				
				which1 = bf1.getSideByRoleId(roleid);
				
				fire.pb.clan.fight.ClanFightBattleField bf2 = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(c2, true);
				if (bf2 == null)
				{
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ")战场不存在");
					return false;
				}
				
				which2 = bf2.getSideByRoleId(targetid);
				
				//判断是否是对立 by changhao
				if (!(which1 == 0 && which2 == 1) && !(which1 == 1 && which2 == 0))
				{
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ")目标阵营不对");
					return false;
				}
				
				if (bf1.getClanfightBean().getState() == xbean.ClanFightBattleField.STATE_CLAN_FIGHT_OVER)
				{
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 410055, null);
					return false;
				}
				
				if (cur > bf1.getClanfightBean().getActivitystarttime() && cur < bf1.getClanfightBean().getActivitypreparetime())
				{
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 410052, null);
					return false;
				}
				
				if (cur < bf1.getClanfightBean().getActivitypreparetime() || cur > bf1.getClanfightBean().getActivityendtime())
				{
					fire.pb.clan.fight.ClanFightBattleField.logger.error("CStartClanFightBattle:角色(ID = " + roleid + ")不在PK时间内");
					return false;
				}
				
				long attackerclanid = 0;
				long hitclanid = 0;
				if (which1 == 0)
				{
					attackerclanid = bf1.getClanfightBean().getClanid1();
					hitclanid = bf1.getClanfightBean().getClanid2();
				}
				else
				{
					attackerclanid = bf1.getClanfightBean().getClanid2();
					hitclanid = bf1.getClanfightBean().getClanid1();
				}
				
				fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(targetid);
				if (team != null)
				{
					if (team.isNormalMember(targetid)) //如果是正常队员 by changhao
					{
						targetid = team.getTeamLeaderId();
					}
				}
				
				if (xtable.Watcherid2battleid.select(targetid) != null)
				{
					new fire.pb.battle.watch.PEndWatchBattle(targetid).call();				
				}
								
				ClanFightEndHandler handler = new ClanFightEndHandler(c1, roleid, targetid, attackerclanid, hitclanid);
				fire.pb.battle.PNewBattle p = new fire.pb.battle.PNewBattle(roleid, targetid, fire.pb.battle.BattleType.BATTLE_CLAN_FIGHT, handler);
				p.call();
				
				return true;
			}
		};
		
		startclanfightbattle.submit();	
	}
	
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808534;

	public int getType() {
		return 808534;
	}

	public long targetid; // 目标id by changhao

	public CStartClanFightBattle() {
	}

	public CStartClanFightBattle(long _targetid_) {
		this.targetid = _targetid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(targetid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		targetid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CStartClanFightBattle) {
			CStartClanFightBattle _o_ = (CStartClanFightBattle)_o1_;
			if (targetid != _o_.targetid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)targetid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(targetid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CStartClanFightBattle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(targetid - _o_.targetid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

