
package fire.msp.npc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fire.pb.battle.BattleType;
import fire.pb.battle.QCmodelType;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SLeitaiLevel;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneClient;
import fire.pb.scene.SceneManager;
import fire.pb.scene.movable.Role;
import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;






// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCheckCanPlayPKView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCheckCanPlayPKView extends __GCheckCanPlayPKView__ {
	
	public static final Logger logger = Logger.getLogger("BATTLE");
	public static final int CQMAPID=1615;//切磋地图id
	public static Map<Integer,SLeitaiLevel> sLeitaiLevelmap=ConfigManager.getInstance().getConf(SLeitaiLevel.class);
	
	public static final int MAX_NUM=50;
	
	@Override
	protected void process() {
		//判断客户端发送的门派和等级以及模块数据是否正确
		if(modeltype!=QCmodelType.ONE_FIGHT&&modeltype!=QCmodelType.TEAM_FIGHT&&modeltype!=QCmodelType.WATCH_FIGHT){
			logger.error("切磋模块数据错误，模块错误");
			return ;
		}
		
		if(sLeitaiLevelmap==null){
			logger.error("切磋模块数据错误，擂台等级配置表");
			return ;
		}
		int levelmin=0;
		int levelmax=1000;
		SLeitaiLevel sLeitaiLevel = sLeitaiLevelmap.get(levelindex);
		if(sLeitaiLevel!=null){
			levelmin=sLeitaiLevel.getLevelmin();
			levelmax=sLeitaiLevel.getLevelmax();
		}
		//获得当前场景是所有玩家，并且是在擂台上的
		LinkedList<Long> rolelistid=new LinkedList<Long>();//符合条件的所有玩家
		HashMap<Long, Long> watchfight=new HashMap<Long, Long>();
		Scene scene = SceneManager.getInstance().getSceneByID(CQMAPID);
		Set<Integer> aroundIndexs = new java.util.HashSet<Integer>();
		aroundIndexs.add(3);
		aroundIndexs.add(65539);
		Map<Long, Role> roles=scene.getAllRolesInScreens(aroundIndexs);
		int num=0;
		for(Role role : roles.values()){
			GridPos hostGridPos = role.getPos().toGridPos();
			//是否在擂台
			if (!role.getScene().getMapInfo().getBlockInfo().checkCanQiecuo(hostGridPos.getX(), hostGridPos.getY())) {
				continue;
			}
			//等级是否符合要求
			if(role.getLevel()<fire.pb.battle.CSendInvitePlayPK.PVP_LEVEL){
				continue;
			}
			//判断删选等级
			if(role.getLevel()<levelmin||role.getLevel()>levelmax){
				continue;
			}
			if(school>0){
				if(role.getSchoolId()!=school){
					continue;
				}
			}
			//过滤自己
			if(role.getRoleID()==hostid){
				continue;
			}
			//判断玩家是否组队，过滤队员
			Team host_Team = TeamManager.selectTeamByRoleId(role.getRoleID());
			if (host_Team != null && host_Team.isNormalMember(role.getRoleID())){
				continue;
			}
			//判断对应模块
			if(modeltype==QCmodelType.TEAM_FIGHT){
				//组队，必须是有队伍的
				if(host_Team == null){
					continue;
				}
				//过滤掉非队长成员
				if(host_Team.getTeamLeaderId()!=role.getRoleID()){
					continue;
				}
				//这里需要判断当前的队伍是否是自己所在的队伍
				Team selfTeam = TeamManager.selectTeamByRoleId(hostid);
				if(selfTeam!=null){
					if(host_Team.getTeamId()==selfTeam.getTeamId()){
						continue;
					}
				}
				if(fire.pb.buff.Module.existState(role.getRoleID(), fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)){			
					continue;
				}
				rolelistid.add(role.getRoleID());
				num++;
			}else if(modeltype==QCmodelType.WATCH_FIGHT){
				watchFightView(role,watchfight);
				num=watchfight.size();
			}else if(modeltype==QCmodelType.ONE_FIGHT){
				//单人，过滤组队和在战斗的玩家
				if(host_Team != null){
					continue;
				}
				if(fire.pb.buff.Module.existState(role.getRoleID(), fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)){			
					continue;
				}
				rolelistid.add(role.getRoleID());
				num++;
			}
			if(num>=MAX_NUM){
				break;
			}
		}	
		SceneClient.pSend(new MCheckCanPlayPKView(hostid, modeltype, school, levelindex, rolelistid, watchfight));
	}
	
	/**
	 * 添加观战的玩家
	 * @param role
	 * @param watchfight
	 */
	private  void watchFightView(Role role,HashMap<Long, Long> watchfight){
		//过滤没有在战斗的玩家
		//不在战斗中
		if(!fire.pb.buff.Module.existState(role.getRoleID(), fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)){			
			return;
		}
		//过滤掉不是主方的
		Long battleid = xtable.Roleid2battleid.select(role.getRoleID());
		if (battleid==null){//玩家可能是点击观战者进入观战的
			battleid = xtable.Watcherid2battleid.select(role.getRoleID());
		}
		if(battleid == null){
			return;
		}
		Integer battleType = xtable.Battle.selectBattletype(battleid);
		if(battleType == null || battleType != BattleType.BATTLE_PVP){
			return;
		}
		Map<Integer, xbean.Fighter> fighters = xtable.Battle.selectFighters(battleid);
		if(fighters == null) {
			return;
		}
		if(fighters.get(1)!=null&&fighters.get(1).getUniqueid()==role.getRoleID()){
			if(fighters.get(15)!=null&&fighters.get(15).getFightertype() == xbean.Fighter.FIGHTER_ROLE){
				watchfight.put(role.getRoleID(), fighters.get(15).getUniqueid());
			}
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730439;

	public int getType() {
		return 730439;
	}

	public long hostid;
	public int modeltype; // 模块类型
	public int school; // 删选的职业 -1表示全职业
	public int levelindex; // 删选的角色等级区间 0表示全选

	public GCheckCanPlayPKView() {
	}

	public GCheckCanPlayPKView(long _hostid_, int _modeltype_, int _school_, int _levelindex_) {
		this.hostid = _hostid_;
		this.modeltype = _modeltype_;
		this.school = _school_;
		this.levelindex = _levelindex_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(hostid);
		_os_.marshal(modeltype);
		_os_.marshal(school);
		_os_.marshal(levelindex);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		hostid = _os_.unmarshal_long();
		modeltype = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		levelindex = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCheckCanPlayPKView) {
			GCheckCanPlayPKView _o_ = (GCheckCanPlayPKView)_o1_;
			if (hostid != _o_.hostid) return false;
			if (modeltype != _o_.modeltype) return false;
			if (school != _o_.school) return false;
			if (levelindex != _o_.levelindex) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)hostid;
		_h_ += modeltype;
		_h_ += school;
		_h_ += levelindex;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(hostid).append(",");
		_sb_.append(modeltype).append(",");
		_sb_.append(school).append(",");
		_sb_.append(levelindex).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GCheckCanPlayPKView _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(hostid - _o_.hostid);
		if (0 != _c_) return _c_;
		_c_ = modeltype - _o_.modeltype;
		if (0 != _c_) return _c_;
		_c_ = school - _o_.school;
		if (0 != _c_) return _c_;
		_c_ = levelindex - _o_.levelindex;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

