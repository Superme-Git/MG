
package fire.pb.mission;
import fire.pb.battle.PSendCameraUrl;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CActiveMissionAIBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CActiveMissionAIBattle extends __CActiveMissionAIBattle__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid( this );
		if ( roleid < 0 )
			return;
		final MissionConfig conf = MissionManager.getInstance().getMissionConfig( missionid );
		if (conf == null)
			throw new NullPointerException( "任务id=" + missionid + "的配置为null" );
		if(activetype == 0){
			if (conf.exeIndo.share == 0) {
				fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
				if (team != null){
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 174001, null);
					Module.logger.info("角色[" + roleid + "]妄想在组队状态下进入非组队共享任务[" + missionid + "]的战斗,朕不准！" );
					return;
				}
			}
			
			if ( !fire.pb.map.SceneNpcManager.checkDistance( npckey, roleid ) )
				return;
			
			final int npcid = fire.pb.npc.NpcServiceManager.getNpcIDByKey( npckey );
			if (npcid == -1)
				return;
		
			MissionColumn sml = new MissionColumn( roleid, true );
		
			RoleMission mission = sml.getMission(missionid);
			if ( mission == null ) {
				Module.logger.debug( "没有该任务的信息" );
				return;
			}
			final int status = mission.getState();
		
			if ( npcid == conf.exeIndo.npcID && status == MissionStatus.PROCESSING ) {
				if ( !checkEnterBattle( roleid, conf ) ) {
					return;
				}
				final int aiid = conf.aiInfo.aIID;
				new MissionBattle(roleid, aiid, 
						new MissionBattleEndHandler(missionid, conf.aiInfo.battleResult, roleid),
						conf.scenarioInfo.animationID).submit();
			}
		} else if (activetype == 1) {
			//TODO:战斗录像功能
			final int aiid = conf.aiInfo.aIID;
			new PSendCameraUrl(aiid,roleid).submit();
		}
	}
	
	private static boolean checkEnterBattle( final long roleid, final MissionConfig conf ) {
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId( roleid );
		if ( team == null && conf.exeIndo.teamState == 2 ) {
			return false;
		}
		if ( team != null && !team.isAbsentMember( roleid ) && conf.exeIndo.teamState == 0 ) {
			Module.logger.error( "任务" + conf.missionId + "不能有队伍提交" );
			fire.pb.talk.MessageMgr.sendMsgNotify( roleid, 141711, null );
			return false;
		}
		if ( conf.exeIndo.hasOtherLimit == 1 ) {
			final SSpecialScenarioQuestConfig sconf = conf.getSpecialScenarioQuestConfig();
			if ( sconf == null )
				return false;
			if ( sconf.类型ID2 == 2 ) {
				// 判断出战宠物
				final int fightpetkey =
					xtable.Properties.selectFightpetkey( roleid );
				fire.pb.pet.PetColumn pc = new fire.pb.pet.PetColumn( roleid, 
						fire.pb.pet.PetColumnTypes.PET, true );
				xbean.PetInfo pi = pc.getPetInfo( fightpetkey );
				if ( pi == null || 
						(sconf.activeparams != null && 
						!sconf.activeparams.contains( pi.getId() ) ) ) {
					fire.pb.talk.MessageMgr.sendMsgNotify( roleid, sconf.emsg, conf.exeIndo.npcID, null );
					return false;
				}
			}
		}
		return true;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805452;

	public int getType() {
		return 805452;
	}

	public int missionid; // 任务id
	public long npckey; // npc的key
	public int activetype; // 激活类型:0进入AI战斗,1观看战斗录像

	public CActiveMissionAIBattle() {
	}

	public CActiveMissionAIBattle(int _missionid_, long _npckey_, int _activetype_) {
		this.missionid = _missionid_;
		this.npckey = _npckey_;
		this.activetype = _activetype_;
	}

	public final boolean _validator_() {
		if (missionid < 1001 || missionid >  999999) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(missionid);
		_os_.marshal(npckey);
		_os_.marshal(activetype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		missionid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		activetype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CActiveMissionAIBattle) {
			CActiveMissionAIBattle _o_ = (CActiveMissionAIBattle)_o1_;
			if (missionid != _o_.missionid) return false;
			if (npckey != _o_.npckey) return false;
			if (activetype != _o_.activetype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += missionid;
		_h_ += (int)npckey;
		_h_ += activetype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missionid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(activetype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CActiveMissionAIBattle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = missionid - _o_.missionid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = activetype - _o_.activetype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

