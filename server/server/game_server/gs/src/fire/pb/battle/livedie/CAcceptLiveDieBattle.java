
package fire.pb.battle.livedie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.battle.pvp.PvPHelperManager;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.talk.MessageMgr;
import fire.pb.team.PAbsentReturnTeam;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.title.COffTitle;
import fire.pb.util.MessageUtil;

import org.apache.log4j.Logger;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAcceptLiveDieBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAcceptLiveDieBattle extends __CAcceptLiveDieBattle__ {
	@Override
	protected void process() {
		// npc处应战开战
		final long guestid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (guestid <= 0)
			return;
		
		//判断下战书的人是否存在
		Long hostid=xtable.Livedie2key.select(guestid);
		//判断是否有下战书的人
		if(hostid==null){
			//提示没有给你下战书的人162079
			fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162079, null);
			return ;
		}
		//获得下战书信息，判断是否过期
		xbean.LiveDieRoleInfo hostliveDieRoleInfo=xtable.Livedieroleinfotab.select(hostid);
		if(hostliveDieRoleInfo==null){
			return ;
		}
		if(System.currentTimeMillis()-hostliveDieRoleInfo.getInvitationtime()>LiveDieMange.getLiveDieTime()){
			logger.info("战书已经过期了");
			return ;
		}
		
		//判断玩家是否在线
		Role hostRole = RoleManager.getInstance().getRoleByID(hostid);
		if (hostRole == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 145001, null);
			return ;
		}
		//判断自己是否在副本，在副本无法发送请求
		MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(hostRole.getMapId());
		if(cfg.dynamic ==1){
			fire.pb.talk.MessageMgr.sendMsgNotify(guestid,162002, null);
			return ;
		}
		xbean.Properties guestprop=xtable.Properties.select(guestid);
		//竞技场地图无法
		if (PvPHelperManager.isPvPMap(hostRole.getMapId())){
			fire.pb.talk.MessageMgr.sendMsgNotify(guestid,162121, null);
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid,162130, Arrays.asList(guestprop.getRolename()));
			return ;
		}
		//判断下战书的玩家是否在战斗或者观战
		BuffAgent hostAgent = new BuffRoleImpl(hostid, true);
		if (hostAgent.existBuff(BuffConstant.StateType.STATE_REPLAY)||hostAgent.existBuff(BuffConstant.StateType.STATE_BATTLE_FIGHTER)||hostAgent.existBuff(BuffConstant.StateType.STATE_BATTLE_WATCHER)) {
			MessageMgr.sendMsgNotify(guestid, 162132, null);
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid,162130, Arrays.asList(guestprop.getRolename()));
			return ;
		}
		if (hostAgent.existBuff(500343)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 144999, 0, null);
			return ;
		}
		//判断对方是否在线
		if (!StateCommon.isOnlineBuffer(hostid)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 145001, null);
			return ;
		}
		int isteamfight=0;
		
		//判断战斗类型，是组队还是单人
		if(hostliveDieRoleInfo.getSelecttype()==1){//组队
			isteamfight=1;
			//如果是组队，需要判断当前队伍是否都是队长
			//组队决斗,发起人不是队长时,接受人点击开战,接受人会提示发起人不是队长,发起人会提示接受人已经接受决斗,请成为队长
			Team hostteam = TeamManager.selectTeamByRoleId(hostid);
			if(hostteam==null){
				//提示发起人，成为队长
				fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162125, Arrays.asList(guestprop.getRolename()));
				fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162121, null);
				return ;
			}
			if(hostteam.getTeamLeaderId()!=hostid){
				//提示发起人，成为队长
				fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162125, Arrays.asList(guestprop.getRolename()));
				fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162121, null);
				return ;
			}
			//判断队伍中的成员是否符合要求
			if(isTeamCanFight(hostteam,hostid)==false){
				fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162121, null);
				return ;
			}
			
			Team guestteam = TeamManager.selectTeamByRoleId(guestid);
			if(guestteam==null){
				//不是队长，无法应战
				fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162081, null);
				return ;
			}
			//判断是否是队长
			if(guestteam.getTeamLeaderId()!=guestid){
				//不是队长，无法应战
				fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162081, null);
				return ;
			}
			//判断队伍中的成员是否符合要求
			if(isTeamCanFight(guestteam,guestid)==false){
				return ;
			}
			//如果有队伍，暂离状态也不能开战
			if(hostteam.getAbsentMemberIds().contains(hostid)){
				//提示发起人，成为队长
				fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162080, null);
				return ;
			}
			if(guestteam.getAbsentMemberIds().contains(guestid)){
				fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162081, null);
				return ;
			}
			
		}else{
			Team guestteam = TeamManager.selectTeamByRoleId(guestid);
			if(guestteam!=null){
				fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162112, null);
				return ;
			}
			//单人战书必须都是单人才能参加
			Team hostteam = TeamManager.selectTeamByRoleId(hostid);
			if(hostteam!=null&&hostteam.getFighterMemberIds().contains(hostid)){
				//直接把目标玩家暂离了
				//如果有队伍就暂离队伍
				new PAbsentReturnTeam(hostid, 1).submit();
			}
		}

		new PLiveDieBattle(hostid,guestid,isteamfight).submit();
		
	}
	
	/**
	 * 判断队伍中的成员是否符合要求
	 * @param guestteam
	 * @param guestid  需要提示消息的目标id
	 * @return
	 */
	public boolean isTeamCanFight(Team guestteam,long guestid){
		//判断当前队伍中的玩家等级是否符合要求，是否有下过战书或者被失败
		List<Long> guestlevelLess50 = new ArrayList<Long>();
		List<Long> guestfightLess50 = new ArrayList<Long>();
		
		List<Long> guestmembers = guestteam.getNormalMemberIds();
		for (long mem : guestmembers) {
			PropRole guestrole = new PropRole(mem, true);
			if (guestrole.getLevel() < LiveDieMange.getLiveDieLevel()) {
				//提示队伍中有等级不符合要求的
				guestlevelLess50.add(mem);
			}
			//身上有生死战称号
			if(COffTitle.isLiveDieTitle(guestrole)){
				//提示队伍中有身上有生死战称号
				guestfightLess50.add(mem);
			}
		}
		if (!guestlevelLess50.isEmpty()) {
			String roleNames = MessageUtil.getRoleNames(guestlevelLess50);
			List<String> paras = MessageUtil.getMsgParaList(roleNames);
			//提示队伍中有等级不符合要求的
			fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162119,paras);
			return false;
		}
		if (!guestfightLess50.isEmpty()) {
			String roleNames = MessageUtil.getRoleNames(guestfightLess50);
			List<String> paras = MessageUtil.getMsgParaList(roleNames);
			//提示队伍中有身上有生死战称号
			fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162118,paras);
			return false;
		}
		return true;
	}
	
	
	public static final Logger logger = Logger.getLogger("BATTLE");
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793839;

	public int getType() {
		return 793839;
	}


	public CAcceptLiveDieBattle() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAcceptLiveDieBattle) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAcceptLiveDieBattle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

