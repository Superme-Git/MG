package fire.pb.battle;

import java.util.Map;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.msp.npc.GCheckCanPlayPKView;
import fire.pb.GsClient;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SLeitaiLevel;

public class PPlayPKFightView extends Procedure {
	public static final Logger logger = Logger.getLogger("BATTLE");
	public static final int CQMAPID=1615;//切磋地图id
	public static Map<Integer,SLeitaiLevel> sLeitaiLevelmap=ConfigManager.getInstance().getConf(SLeitaiLevel.class);
	
	private final long roleid;
	public int modeltype; // 模块类型
	public int school; // 删选的门派 -1表示全职业
	public int levelindex; // 删选的角色等级区间 0表示全选
	
	public static int openflag=0;//擂台默认开关，默认是0开启状态
	
	public PPlayPKFightView(long roleid,int modeltype,int school,int levelindex){
		super();
		this.roleid = roleid;
		this.modeltype = modeltype; // 模块类型
		this.school = school; // 删选的门派
		this.levelindex = levelindex; // 删选的角色等级区间 0表示全选
	}
	
	@Override
	protected boolean process() throws Exception {
		if(openflag==1){
			return false;
		}
//		//判断客户端发送的门派和等级以及模块数据是否正确
//		if(modeltype!=QCmodelType.ONE_FIGHT&&modeltype!=QCmodelType.TEAM_FIGHT&&modeltype!=QCmodelType.WATCH_FIGHT){
//			logger.error("切磋模块数据错误，模块错误");
//			return false;
//		}
//		
//		if(sLeitaiLevelmap==null){
//			logger.error("切磋模块数据错误，擂台等级配置表");
//			return false;
//		}
//		int levelmin=0;
//		int levelmax=1000;
//		SLeitaiLevel sLeitaiLevel = sLeitaiLevelmap.get(levelindex);
//		if(sLeitaiLevel!=null){
//			levelmin=sLeitaiLevel.getLevelmin();
//			levelmax=sLeitaiLevel.getLevelmax();
//		}
//		//获得当前场景是所有玩家，并且是在擂台上的
//		List<Long> rolelistid=new ArrayList<Long>();//符合条件的所有玩家
//		HashMap<Long, Long> watchfight=new HashMap<Long, Long>();
//		
//		Scene scene=SceneManager.getInstance().getSceneByID(CQMAPID);
//		for (Role role :  scene.getSceneRoles().values()) {
//			GridPos hostGridPos = role.getPos().toGridPos();
//			//是否在擂台
//			if (!role.getScene().getMapInfo().getBlockInfo().checkCanQiecuo(hostGridPos.getX(), hostGridPos.getY())) {
//				continue;
//			}
//			//等级是否符合要求
//			if(role.getLevel()<fire.pb.battle.CSendInvitePlayPK.PVP_LEVEL){
//				continue;
//			}
//			//判断删选等级
//			if(role.getLevel()<levelmin||role.getLevel()>levelmax){
//				continue;
//			}
//			if(school>0){
//				if(role.getSchoolId()!=school){
//					continue;
//				}
//			}
//			//判断玩家是否在线
//			if (!StateCommon.isOnlineBuffer(role.getRoleID())) {
//				continue;
//			}
//			//过滤自己
//			if(role.getRoleID()==roleid){
//				continue;
//			}
//			
//			//判断玩家是否组队，过滤队员
//			Team host_Team = TeamManager.selectTeamByRoleId(role.getRoleID());
//			if (host_Team != null && host_Team.isNormalMember(role.getRoleID())){
//				continue;
//			}
//			//判断对应模块
//			if(modeltype==QCmodelType.TEAM_FIGHT){
//				//组队，必须是有队伍的
//				if(host_Team == null){
//					continue;
//				}
//				//过滤掉非队长成员
//				if(host_Team.getTeamLeaderId()!=role.getRoleID()){
//					continue;
//				}
//				//这里需要判断当前的队伍是否是自己所在的队伍
//				Team selfTeam = TeamManager.selectTeamByRoleId(roleid);
//				if(selfTeam!=null){
//					if(host_Team.getTeamId()==selfTeam.getTeamId()){
//						continue;
//					}
//				}
//				if(fire.pb.buff.Module.existState(role.getRoleID(), fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)){			
//					continue;
//				}
//				rolelistid.add(role.getRoleID());
//			}else if(modeltype==QCmodelType.WATCH_FIGHT){
//				watchFightView(role,watchfight);
//			}else if(modeltype==QCmodelType.ONE_FIGHT){
//				//单人，过滤组队和在战斗的玩家
//				if(host_Team != null){
//					continue;
//				}
//				if(fire.pb.buff.Module.existState(role.getRoleID(), fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)){			
//					continue;
//				}
//				rolelistid.add(role.getRoleID());
//			}
//			
//		}
//		SPlayPKFightView sPlayPKFightView=new SPlayPKFightView();
//		sPlayPKFightView.levelindex=levelindex;
//		sPlayPKFightView.modeltype=modeltype;
//		sPlayPKFightView.school=school;
//		
//		if(modeltype==QCmodelType.WATCH_FIGHT){
//			for (Map.Entry<Long, Long> entry : watchfight.entrySet()){
//				long oneid=entry.getKey();
//				long otherid=entry.getValue();
//				QCRoleInfoDes qCRoleInfoDes1 = createQCRoleInfoDes(oneid);
//				QCRoleInfoDes qCRoleInfoDes2 = createQCRoleInfoDes(otherid);
//				QCRoleInfoWatchDes qcRoleInfoWatchDes=new QCRoleInfoWatchDes();
//				qcRoleInfoWatchDes.role1=qCRoleInfoDes1;
//				qcRoleInfoWatchDes.role2=qCRoleInfoDes2;
//				sPlayPKFightView.rolewatchlist.add(qcRoleInfoWatchDes);
//			}
//		}else{
//			for(Long id:rolelistid){
//				QCRoleInfoDes qCRoleInfoDes = createQCRoleInfoDes(id);
//				sPlayPKFightView.rolelist.add(qCRoleInfoDes);
//			}
//		}
//		psendWhileCommit(roleid, sPlayPKFightView);
		GCheckCanPlayPKView gccq = new GCheckCanPlayPKView(roleid,modeltype,school,levelindex);
		GsClient.sendToScene(gccq);
		
		return true;
	}
	
//	/**
//	 * 添加观战的玩家
//	 * @param role
//	 * @param watchfight
//	 */
//	public void watchFightView(Role role,HashMap<Long, Long> watchfight){
//		//过滤没有在战斗的玩家
//		//不在战斗中
//		if(!fire.pb.buff.Module.existState(role.getRoleID(), fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)){			
//			return;
//		}
//		//过滤掉不是主方的
//		Long battleid = xtable.Roleid2battleid.select(role.getRoleID());
//		if (battleid==null){//玩家可能是点击观战者进入观战的
//			battleid = xtable.Watcherid2battleid.select(role.getRoleID());
//		}
//		if(battleid == null){
//			return;
//		}
//		Integer battleType = xtable.Battle.selectBattletype(battleid);
//		if(battleType == null || battleType != BattleType.BATTLE_PVP){
//			return;
//		}
//		Map<Integer, xbean.Fighter> fighters = xtable.Battle.selectFighters(battleid);
//		if(fighters == null) {
//			return;
//		}
//		if(fighters.get(1)!=null&&fighters.get(1).getUniqueid()==role.getRoleID()){
//			if(fighters.get(15)!=null&&fighters.get(15).getFightertype() == xbean.Fighter.FIGHTER_ROLE){
//				watchfight.put(role.getRoleID(), fighters.get(15).getUniqueid());
//			}
//		}
//	}
	
	
	
//	public QCRoleInfoDes createQCRoleInfoDes(long id){
//		PropRole propRole = new PropRole(id, true);
//		QCRoleInfoDes qCRoleInfoDes = new QCRoleInfoDes();
//		qCRoleInfoDes.roleid=propRole.getRoleId();//角色Id
//		qCRoleInfoDes.rolename=propRole.getName();//角色名
//		qCRoleInfoDes.shape=propRole.getShape();//角色造型
//		qCRoleInfoDes.level=propRole.getLevel();//角色等级
//		qCRoleInfoDes.school=propRole.getSchool();//门派
//		Team team = TeamManager.selectTeamByRoleId(id);
//		if(team!=null){
//			//这里队伍标志需要考虑 暂离队伍的玩家在切磋
//			//过滤掉非队长成员
//			if(team.getTeamLeaderId()!=id){
//				qCRoleInfoDes.teamnum=0;
//				qCRoleInfoDes.teamnummax=0;
//			}else{
//				qCRoleInfoDes.teamnum=team.getNormalMemberIds().size();//队伍当前人数
//				qCRoleInfoDes.teamnummax=TeamManager.MAX_MEMBER_COUNT+1;//队伍最大人数
//			}
//		}else{
//			qCRoleInfoDes.teamnum=0;
//			qCRoleInfoDes.teamnummax=0;
//		}
//		return qCRoleInfoDes;
//	}

}
