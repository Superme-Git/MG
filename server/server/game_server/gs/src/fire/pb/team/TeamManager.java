package fire.pb.team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.log.Logger;
import fire.msp.team.GNotifyTeamChange;
import fire.msp.team.TeamChangeType;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.battle.BattleField;
import fire.pb.battle.watch.PSendWatchBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.OperateType;
import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.PEnterInstZone;
import fire.pb.main.ConfigManager;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.GridPos;
import fire.pb.map.MapConfig;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.mission.ExecuteReqGoto;
import fire.pb.mission.SDefineTeam;
import fire.pb.mission.instance.line.LineInstManager;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.scene.Scene;
import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;
import mkdb.Procedure;

/***
 * 队伍管理器
 * @author changhao
 *
 */
public class TeamManager {

	public final static Logger logger = Logger.getLogger("TEAM");
	
	public final static int MAX_MEMBER_COUNT = 4;//4个，作用于队伍成员人数（不包括队长），同时发出的邀请个数
	
	public final static long MAX_INVITE_TIMEOUT = 20*1000;//30s,作用于邀请超时，邀请同一个人时的限制时间

	public final static long MAX_ARRLY_TIMEOUT = 10*60*60*1000;//10小时,作用于申请超时
	
	public final static long MIN_SUCCESS_SWITCH_LEADER_PERIOD = 500;//500毫秒交换一次队长 by changhao
	
	public final static int MAX_TEAM_APPLIER_COUNT = 15; //一个队伍最多的申请人个数为15个
	
	public final static int LEVEL_REQUST_MIN = 1;//队伍等级要求，最小为1
	
	public final static int LEVEL_REQUST_MAX = 155;//队伍等级要求，最大为155
	
	public final static int STRUCT_CHANGE_MEMBER_STATE_NORMAL = 1;

	public final static int STRUCT_CHANGE_MEMBER_STATE_ABSENT = 2;
	
	public final static int STRUCT_CHANGE_MEMBER_STATE_RETURN = 3;

	public final static int STRUCT_CHANGE_MEMBER_STATE_OFFLINE = 4;

	public final static int STRUCT_CHANGE_NEW_MEMBER = 5;
	
	public final static int STRUCT_CHANGE_SWITCH_LEADER = 6;

	public final static int STRUCT_CHANGE_PASSIVE_SWITCH_LEADER = 7;
	
	public final static int STRUCT_CHANGE_REMOVE_MEMBER = 8;
	
	public final static int STRUCT_CHANGE_SWITCH_MEMBER = 9;

	public final static int ERROR_MSG_SELF_CANT_IN_TEAM = 141618;//错误提示：自己处在不能组队的状态
	
	public final static int ERROR_MSG_OBJECT_CANT_IN_TEAM = 141619;//错误提示：对方处在不能组队的状态
	
	public final static int MAX_MATCH_LIST_COUNT = 10; //最大的组队匹配返回数量 by changhao
	
	public final static int MAX_MATCH_CLIENT_TEAM_TIME_OUT = 3 * 1000; //匹配等待客户端超时时间 by changhao
	
	public final static int MAX_MATCH_CLIENT_TIME_OUT = 7 * 1000; //匹配等待客户端超时时间 by changhao
	
	public final static int TEAM_MATCH_TIMER = 5 * 1000; //组队匹配多久做一次 by changhao
	
	public final static int ONE_KEY_APPLY_TEAM = 30 * 1000; //一键组队间隔时间 by changhao
	
	public final static int MAX_ROLL_MELON_TIME = 6 * 1000; //ROLL点分赃最大等待时间 by changhao
	
	public final static int FORMBOOKHALF_COMMON_ID = 130; //光环书残卷ID 的通用配置id by changhao
	
	public final static int FORMBOOKHALF_ADD_EXP = 131; //残卷增加经验 的通用配置id by changhao
	
	public final static int FORMBOOK_NO_MATCH_ADD_EXP = 132; //不匹配光环书增加经验 的通用配置id by changhao
	
	public final static int FORMBOOKHALF_COMBINE_NUM = 5; //光环书残卷合成光环书的数量 by changhao
	
	public final static int MAX_FORM_NUM = 10; //光环最多10个 by changhao
	
	public final static int ITEMTYPE_FORMBOOK_ID = 278; //光环书 类型 by changhao
	
	public final static int ITEMTYPE_FORMBOOK_HALF_ID = 262; //光环残卷类型 by changhao
	
	public final static String MAKE_FORMBOOK = "合成光环书"; // 光环书 by changhao
	
	public final static String USE_FORMBOOK = "使用光环书或残卷"; // 光环书 by changhao
	
	public ScheduledFuture<?> teamMatchTask;
	
	private static TeamManager instance;	
	private TeamManager()
	{
		createTeamMatchTask();
	}
	
	public static TeamManager getInstance(){
		if(null == instance){
			instance = new TeamManager();
		}		
		return instance;
	}	
	
	/**
	 * 通过teamid获取team
	 * lock: teamlock
	 * @return Team
	 */
	public static Team getTeamByTeamID(long teamId)
	{
		xbean.TeamInfo teamInfo = null;
		teamInfo = xtable.Team.get(teamId);
		if(teamInfo == null)
			return null;
		return new Team(teamId, teamInfo, false);
	}
	
	/**
	 * 通过teamid获取team
	 * lock: teamlock
	 * @return Team
	 */
	public static Team selectTeamByTeamID(long teamId) 
	{
		xbean.TeamInfo teamInfo = null;
		teamInfo = xtable.Team.select(teamId);
		if(teamInfo == null)
			return null;
		return new Team(teamId, teamInfo, false);
		/*xdb cache 功能屏蔽xdb.Lockey lockey = null;
		if(Transaction.current() != null)
			lockey = Lockeys.get(xtable.Locks.TEAMLOCK, teamId);
		if(lockey != null && lockey.isHeldByCurrentThread())
		{
			//已取得锁，直接select
			xbean.TeamInfo teaminfo = xtable.Team.select(teamId);
			if(teaminfo == null)
				return null;
			return new Team(teamId,teaminfo,true);
		}
		else
		{
			// 未取得锁时，用xcache的方案
			xcache.Teamcache.Valuetype value = xcache.Teamcache.get(teamId);
			if (value == null)
				return null;
			if (value.getTeam() == null)
				return null;
			return new Team(teamId, deepcopy(value.getTeam()), true);
		}*/
	}
	
	/**
	 * 通过队长roleid获取队伍的正常队员,包括队长
	 * @lock: 无
	 * @return List<Long> 正常状态的队伍成员Id。如果不合法，返回的是个空List（不是null）
	 */
	public List<Long> selectNormalMembersByLeaderId(long leaderRoleId)
	{
		Team team = selectTeamByRoleId(leaderRoleId);
		if(team == null)
			return new ArrayList<Long>();
		
		if(!team.isTeamLeader(leaderRoleId))
			return new ArrayList<Long>();
		else
			return team.getNormalMemberIds();
	}
	
	/**
	 * 通过roleid获取team
	 * @lock: teamlock,rolelock
	 * @return Team,如果该角色没在队伍中，返回null
	 */
	public static Team getTeamByRoleId(long roleId)
	{
		Long teamId = xtable.Roleid2teamid.select(roleId);
		if(teamId !=null)
			return getTeamByTeamID(teamId);
		else
			return null;
	}
	
	/**
	 * 通过roleid获取team
	 * @lock 无
	 * @return Team,如果该角色没在队伍中，返回null
	 */
	public static Team selectTeamByRoleId(long roleId)
	{
		Long teamId = xtable.Roleid2teamid.select(roleId);
		if(teamId !=null)
		{
			try {
				Team team = selectTeamByTeamID(teamId);;
				if(team == null)
					return null;
				if (team.isInTeam(roleId))
					return team;
				else
					return null;
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
		else
			return null;
	}
	
	/**
	 * 通过roleid获取队伍中可进入他战斗的战斗队员
	 * 如果是队长或正常队员，获取队长和正常队员的个数
	 * 如果是暂离或无队伍队员，只有他自己
	 * @lock 无
	 * @return Team,如果该角色没在队伍中，返回null
	 */
	public static List<Long> getFightTeamMembers(long roleId)
	{
		List<Long> members =  new ArrayList<Long>();
		members.add(roleId);
		Team team = selectTeamByRoleId(roleId);
		if(team == null)
			return members;
		if(team.isAbsentMember(roleId))
			return members;
		else
			return team.getNormalMemberIds();
	}
	
//	/**
//	 * 角色下线，更新队伍状态信息
//	 * @lock:无
//	 * @param 角色roleId
//	 * @return false = 角色不在队伍中
//	 */
//	public boolean updateWhileRoleOffline(long roleId)
//	{
//		Long teamId = xtable.Roleid2teamid.select(roleId);
//		if(teamId != null)
//		{
//			PRoleOffline pRoleOffline = new PRoleOffline(teamId, roleId);
//			xdb.Procedure.execute(pRoleOffline);
//			return true;
//		}
//		return false;
//	}
	
//	/**
//	 * 角色上线，更新队伍状态信息
//	 * @lock:无
//	 * @param 角色roleId
//	 * @return false = 角色不在队伍中
//	 */
//	public boolean updateWhileRoleOnline(long roleId)
//	{
//		Long teamId = xtable.Roleid2teamid.select(roleId);
//		if(teamId != null)
//		{
//			PRoleOnline pRoleOnline = new PRoleOnline(teamId, roleId);
//			xdb.Procedure.pexecuteWhileCommit(pRoleOnline);
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * 当队伍退出战斗时，更新队伍状态信息
	 * 起新Procedure执行
	 * @lock:无
	 * @param teamId
	 * @return false =
	 */
	public static boolean processReturnMembers(final long teamId)
	{
		mkdb.Procedure procedure = new mkdb.Procedure()
		{
			//Lock: teamlock 队长和所有回归中队员的rolelock
			@Override
			protected boolean process()
			{
				//lock start
				Team team = TeamManager.getTeamByTeamID(teamId);
				if(team == null)
					return true;
				List<Long> roleIdList = new ArrayList<Long>();
				long leaderRoleId = team.getTeamInfo().getTeamleaderid();
				roleIdList.add(leaderRoleId);
				for(xbean.TeamMember member : team.getTeamInfo().getMembers())
				{
					if(member.getState() == TeamMemberState.eTeamReturn)
						roleIdList.add(member.getRoleid());
				}
				
				this.lock(xtable.Locks.ROLELOCK, roleIdList);
				//lock end
				
				roleIdList.remove(leaderRoleId);
				for(Long roleId : roleIdList)
				{
					if(team.isMemberInReturnScale(roleId))
						team.setTeamMemberStateWithSP(roleId, TeamMemberState.eTeamNormal);
					else
						team.setTeamMemberStateWithSP(roleId, TeamMemberState.eTeamAbsent);
				}
				
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(procedure);
		return true;
	}
	
	
	
	/**
	 * 创建新队伍，只能在Procedure中被调用！！
	 * @lock:队长的rolelock，新队伍的teamlock
	 * @param leaderRoleId
	 * @return Team 新创建的Team对象,如果为null，则创建队伍失败
	 */
	//
	public Team createNewTeam(long leaderRoleId)
	{
		//判断队长状态，看能否创建队伍
		if(!fire.pb.buff.Module.checkBuffClashAndSendMsg(leaderRoleId, OperateType.TEAM_CREATE, false))
		{
			MessageMgr.sendMsgNotify(leaderRoleId, ERROR_MSG_SELF_CANT_IN_TEAM, null);
			return null;
		}
		Role maprole = RoleManager.getInstance().getRoleByID(leaderRoleId);
		if(maprole.getCurMapConfig().visibletype == Scene.VISIBLE_SINGLE)
		{
			MessageMgr.sendMsgNotify(leaderRoleId, 143363,null);
			return null;
		}
		// 1v1竞技场不能组队
		if (fire.pb.battle.pvp1.PvP1Helper.isPvPMap(maprole.getMapId())) {
			MessageMgr.sendMsgNotify(leaderRoleId, 143363, null);
			return null;
		}

		// create xdb team & server protocol
		xbean.TeamInfo newTeam = xbean.Pod.newTeamInfo();
		newTeam.setTeamleaderid(leaderRoleId);
		newTeam.setState(1);
		newTeam.setMinlevel(1);
		newTeam.setMaxlevel(155);
		
		PropRole prole = new PropRole(leaderRoleId, true);
		int formLevel = prole.getFormLevel(prole.getDealutFormId());
		newTeam.setFormation(prole.getDealutFormId());
		newTeam.setFormationlevel(formLevel);
		newTeam.setSwitchleaderid(-1);
		newTeam.setSwitchleadertime(0);
		newTeam.setSuccessswitchtime(0);
		newTeam.setCreatetime(System.currentTimeMillis());
		
		//把玩家建立队伍前的邀请记录加入到新建立的队伍中
		xbean.SingleInvitings singleInvites = xtable.Singleinviting.get(leaderRoleId);
		if(singleInvites!=null)
		{
			newTeam.getInvitingids().putAll(singleInvites.getInvitingids());
			xtable.Singleinviting.remove(leaderRoleId);
		}
		long newTeamId = xtable.Team.insert(newTeam);
		//过滤器
		TeamFilter filter = getActiveFilter(leaderRoleId);
		if(filter != null)
		{
			xbean.TeamFilter xfilter = xbean.Pod.newTeamFilter();
			xfilter.setFilter(filter);
			xtable.Teamfilters.add(newTeamId, xfilter);
		}
		
		Team team = new Team(newTeamId, false);
		
		SCreateTeam sct = new SCreateTeam();
		sct.teamid = newTeamId;
		sct.formation = newTeam.getFormation();
		sct.teamstate = newTeam.getState();
		sct.smapid = newTeam.getSmapid();
		xtable.Roleid2teamid.add(leaderRoleId, sct.teamid);
		
		TeamManager.getInstance().delTeamMatch(leaderRoleId);
		
		//返回创建队伍的协议
		mkdb.Procedure.psendWhileCommit(leaderRoleId, sct);
		
		//新成员（队长）的信息发送给自己（队长）
		SAddTeamMember sAddTeamMemeber = new SAddTeamMember();
		sAddTeamMemeber.memberlist.add(team.getTeamMemeberBasic(leaderRoleId));
		mkdb.Procedure.psendWhileCommit(leaderRoleId, sAddTeamMemeber);
		
		MessageMgr.sendMsgNotify(leaderRoleId, 160493, null);		
		
		
		//notify scene
		GsClient.tSendWhileCommit(new GNotifyTeamChange(TeamChangeType.CREATE, newTeamId, leaderRoleId, 0));//通知地图模块
		//GsClient.tSendWhileCommit(new GCreateTeam(leaderRoleId, newTeamId));//通知地图模块
		//do log
		mkdb.Trace.log(mkdb.Trace.DEBUG, "创建队伍,TeamId: "+newTeamId);
		return team;
	}
	
	
	/**
	 * 把角色的roleId按从小到大排序
	 * @lock:无
	 * @param 角色roleIds
	 * @return 增序的角色roleIds
	 */
	public static Long[] getSequenceRolelocks(Long[] roleIds)
	{
		for(int i = 0; i <roleIds.length;i++)
		{
			for(int j =i; j>0; j--)
			{
				if(roleIds[j] < roleIds[j-1])
				{
					Long temp = roleIds[j];
					roleIds[j] =  roleIds[j-1];
					roleIds[j-1] = temp;							                       
				}
				else
				{
					break;
				}
			}
		}
		return roleIds; 
	}
	/**
	 * 更新队员的当前hp mp
	 * @lock:无
	 * @param roleId
	 * @param hp 当前HP
	 * @param mp 当前MP
	 * @return false = 队伍中不存在这个成员
	 */
	public boolean updateMemberHpMp(final long roleId,final int hp, final int mp)
	{
		final Long teamId = xtable.Roleid2teamid.select(roleId);
		if(teamId != null)
		{
			mkdb.Procedure p = new mkdb.Procedure(){
				
				@Override
				protected boolean process()
				{
					Team team = new Team(teamId,false);
					if(!team.isInTeam(roleId))
						return true;//重新判断是否还在队伍中
					SUpdateMemberHPMP sUpdateMemberHPMP = new SUpdateMemberHPMP(roleId,hp,mp);
					java.util.Set<Long> roleids = team.getTeamBroadcastSet();
					roleids.remove(roleId);//不用发给自己
					mkdb.Procedure.psendWhileCommit(roleids, sUpdateMemberHPMP);
					return true;
				}
			};
			
			mkdb.Procedure.pexecute(p);
			
			return true;
		}
		else
			return false;
	}
	
	/**
	 * 更新队员的最大hp mp
	 * @lock:无
	 * @param roleId
	 * @param maxHp 最大HP
	 * @param maxMp 最大MP
	 * @return false = 队伍中不存在这个成员
	 */
	public boolean updateMemberMaxHpMp(final long roleId,final int maxHp, final int maxMp)
	{
		final Long teamId = xtable.Roleid2teamid.select(roleId);
		if(teamId != null)
		{
			mkdb.Procedure p = new mkdb.Procedure(){
				
				@Override
				protected boolean process()
				{
					Team team = new Team(teamId,false);
					if(!team.isInTeam(roleId))
						return true;//重新判断是否还在队伍中
					SUpdateMemberMaxHPMP sUpdateMemberMaxHPMP = new SUpdateMemberMaxHPMP(roleId,maxHp,maxMp);
					java.util.Set<Long> roleids = team.getTeamBroadcastSet();
					roleids.remove(roleId);//不用发给自己
					mkdb.Procedure.psendWhileCommit(roleids, sUpdateMemberMaxHPMP);
					return true;
				}
			};
			
			mkdb.Procedure.pexecute(p);
			
			return true;
		}
		else
			return false;
	}
	
	/**
	 * 创建teammatch定时 by changhao
	 * 
	 */
	public void createTeamMatchTask()
	{
		if (teamMatchTask == null)
		{
			teamMatchTask = mkdb.Executor.getInstance().scheduleAtFixedRate(new PTeamMatchTask(), 0, TeamManager.TEAM_MATCH_TIMER, TimeUnit.MILLISECONDS);			
		}
	}
	
	public xbean.TeamMatch createTeamMatch(xbean.TeamMatch teammatch)
	{
		xbean.TeamMatch tmp = xbean.Pod.newTeamMatch();
		tmp.setRoleid(teammatch.getRoleid());
		tmp.setTargetid(teammatch.getTargetid());
		tmp.setLevelmax(teammatch.getLevelmax());
		tmp.setLevelmin(teammatch.getLevelmin());
		tmp.setMatchtype(teammatch.getMatchtype());
		tmp.setOnekeytimestamp(teammatch.getOnekeytimestamp());
		tmp.setTimestamp(teammatch.getTimestamp());
		
		return tmp;
	}
	
	/**
	 * 修改匹配信息 by changhao
	 * @param dest
	 * @param src
	 */
	
	public void modifyMatchInfo(xbean.TeamMatch dest, xbean.TeamMatch src)
	{
		dest.setLevelmin(src.getLevelmin());
		dest.setLevelmax(src.getLevelmax());
		dest.setMatchtype(src.getMatchtype());
		dest.setTargetid(src.getTargetid());
	}
	
	/***
	 * 通过teamid 得到对应的teammatch by changhao
	 * @param roleid
	 * @return
	 */
	xbean.TeamMatch getTeamMatchByTeamid(long teamid)
	{
		xbean.ETeamMatch ematch = xtable.Targetid2teammatch.get(0);
		if (ematch == null)
		{
			return null;
		}
		
		xbean.TeamMatch teammatch = ematch.getTeamid2matchdata().get(teamid);
		if (teammatch != null)
		{
			return teammatch;
		}
		
		return null;
	}
					
	/**
	 * 必须在Procedure里执行 根据加锁上下文使用 加一个组队匹配到链表 by changhao
	 */	
	public void addTeamMatch(xbean.TeamMatch match, Long teamid)
	{
		xbean.ETeamMatch ematch = xtable.Targetid2teammatch.get(0);
		if (ematch == null) //如果还没有记录插入一条 by changhao
		{
			xtable.Targetid2teammatch.insert(0, xbean.Pod.newETeamMatch());			
			ematch = xtable.Targetid2teammatch.get(0);
		}
		
		xbean.TeamMatch have = ematch.getRoleid2matchdata().get(match.getRoleid());
		if (have != null)
		{
			modifyMatchInfo(have, match);
		}
		else
		{
			xbean.TeamMatch t0 = createTeamMatch(match);
			
			ematch.getRoleid2matchdata().put(match.getRoleid(), t0);			
		}
		
		if (match.getMatchtype() == 1 && teamid != null)
		{
			xbean.TeamMatch t1 = createTeamMatch(match);			
			ematch.getTeamid2matchdata().put(teamid, t1);
			
			if (have != null) //如果已经有了，只是单纯的修改 by changhao
			{
				for (xbean.TeamMatch e : ematch.getTeammatchdatalist())
				{
					if (e.getRoleid() == match.getRoleid())
					{
						modifyMatchInfo(e, match);
						break;
					}
				}
			}
			else
			{
				xbean.TeamMatch t2 = createTeamMatch(match);
				ematch.getTeammatchdatalist().add(t2);				
			}
		}
		
		if (match.getMatchtype() == 0)
		{
			if (have != null)
			{
				for (xbean.TeamMatch e : ematch.getRolematchdatalist())
				{
					if (e.getRoleid() == match.getRoleid())
					{
						modifyMatchInfo(e, match);
						break;
					}
				}				
			}
			else
			{
				xbean.TeamMatch t3 = createTeamMatch(match);
				ematch.getRolematchdatalist().add(t3);				
			}
		}
	}
	
	/**
	 * 异步调去必须在Procedure里执行 根据加锁上下文使用，删除一个组队匹配到链表 by changhao
	 */	
	public void delTeamMatchAsyn(long roleid)
	{
		Procedure.pexecuteWhileCommit(new mkdb.Procedure()
		{
			@Override
			protected boolean process() throws Exception
			{
				Long teamid = xtable.Roleid2teamid.select(roleid);
				if (teamid != null)
				{
					Team team = TeamManager.getTeamByTeamID(teamid);
					if (team != null)
					{
						lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getAllMemberIdSet()));					
					}					
				}
				else
				{
					Set<Long> roleids = new java.util.HashSet<Long>();
					roleids.add(roleid);
					lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));						
				}
			
				delTeamMatch(roleid);
				
				return true;
			}
		});
	}
	
	/**
	 * 必须在Procedure里执行 根据加锁上下文使用，删除一个组队匹配到链表 by changhao
	 */	
	public boolean delTeamMatch(long roleid)
	{
		xbean.ETeamMatch ematch = xtable.Targetid2teammatch.get(0);
		if (ematch == null)
		{
			return false;
		}
		
		xbean.TeamMatch teammatch = ematch.getRoleid2matchdata().remove(roleid);
		if (teammatch != null) //玩家确实在匹配队列中 by changhao
		{
			final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();
			
			Long teamid = xtable.Roleid2teamid.select(roleid);
			
			if (teamid != null && teammatch.getMatchtype() == 1) //如果是队伍匹配 by changhao
			{
				Team team = new Team(teamid, true);
				
				roleids.addAll(team.getAllMemberIds());
				
				ematch.getTeamid2matchdata().remove(teamid);
								
				Iterator<xbean.TeamMatch> teammatchiter = ematch.getTeammatchdatalist().iterator();
				while (teammatchiter.hasNext())
				{
					xbean.TeamMatch e = teammatchiter.next();
					if (e.getRoleid() == roleid)
					{
						teammatchiter.remove();
					}
				}
				
				/* 谨记 bug 版本 by changhao
				for (xbean.TeamMatch e : ematch.getTeammatchdatalist())
				{
					if (e.getRoleid() == roleid)
					{
						ematch.getTeammatchdatalist().remove(e);
					}
				}
				*/
			}
			
			if (teammatch.getMatchtype() == 0)
			{
				Iterator<xbean.TeamMatch> teammatchiter = ematch.getRolematchdatalist().iterator();
				while (teammatchiter.hasNext())
				{
					xbean.TeamMatch e = teammatchiter.next();
					if (e.getRoleid() == roleid)
					{
						teammatchiter.remove();
					}
				}
				
				/* 谨记 bug 版本 by changhao
				for (xbean.TeamMatch e : ematch.getRolematchdatalist())
				{
					if (e.getRoleid() == roleid)
					{
						ematch.getRolematchdatalist().remove(e);
					}
				}
				*/
				
				roleids.add(roleid);
			}
			
			for (Long r : roleids)
			{
				SStopTeamMatch msg = new SStopTeamMatch(); //发送一个停止匹配到客户端 by changhao			
				mkdb.Procedure.psendWhileCommit(r, msg);				
			}
		}
					
		return true;
	}
	
	/**
	 * 必须在Procedure里执行 根据加锁上下文使用 这个roleid是否有在匹配表中 by changhao
	 */
	public xbean.TeamMatch selectTeamMatchByRoleId(long roleid)
	{
		xbean.ETeamMatch ematch = xtable.Targetid2teammatch.select(0);
		if (ematch == null)
		{
			return null;
		}
		
		xbean.TeamMatch teammatch = ematch.getRoleid2matchdata().get(roleid);
		
		return teammatch;
	}
	
	/**
	 * 必须在Procedure里执行 根据加锁上下文使用 这个roleid是否有在匹配表中 by changhao
	 */
	public xbean.TeamMatch getTeamMatchByRoleId(long roleid)
	{
		xbean.ETeamMatch ematch = xtable.Targetid2teammatch.get(0);
		if (ematch == null)
		{
			return null;
		}
		
		xbean.TeamMatch teammatch = ematch.getRoleid2matchdata().get(roleid);
		
		return teammatch;
	}
	
	public void sendCurTeamMatchStateByRoleId(long roleid, long sendroleid)
	{
		xbean.TeamMatch teammatch = TeamManager.getInstance().selectTeamMatchByRoleId(roleid);
		if (teammatch != null)
		{
			SRequestTeamMatch msg = new SRequestTeamMatch();
			msg.levelmin = teammatch.getLevelmin();
			msg.levelmax = teammatch.getLevelmax();
			msg.targetid = teammatch.getTargetid();
			msg.typematch = teammatch.getMatchtype();
			
			mkdb.Procedure.psendWhileCommit(sendroleid, msg);
		}			
	}
	
	
	/**
	 * 发送队伍设置等级信息 by changhao
	 * @param roleid
	 */
	public void sendCurTeamSetInfo(long roleid)
	{
		Long teamid = xtable.Roleid2teamid.select(roleid);
		
		Team team = null;
		if(teamid != null)
		{
			team = TeamManager.selectTeamByTeamID(teamid);	//有队先把队伍锁住 by changhao
			if (team != null)
			{
				int targetid = team.getTeamInfo().getTargetid();
				if (targetid != 0)
				{
					int levelmin = team.getTeamInfo().getMinlevel();
					int levelmax = team.getTeamInfo().getMaxlevel();
					
					SRequestTeamMatch msg = new SRequestTeamMatch();
					msg.levelmin = levelmin;
					msg.levelmax = levelmax;
					msg.targetid = targetid;
					msg.typematch = 3;
					
					mkdb.Procedure.psendWhileCommit(roleid, msg);						
				}			
			}
		}
	}
	
	/**
	 * 必须在Procedure里执行 是否可以加锁 是否在强制组队状态by changhao
	 */
	public boolean isForceTeam(long teamid, long roleid)
	{
		xbean.ETeamMatch ematch = xtable.Targetid2teammatch.get(0);
		if (ematch == null)
		{
			return false;
		}
		
		xbean.TeamMatch teammatch = ematch.getTeamid2matchdata().get(teamid);
		if (teammatch == null)
		{
			return false;
		}
		
		xbean.TeamMatch roleteammatch = ematch.getRoleid2matchdata().get(roleid);
		
		if (roleteammatch == null)
		{
			return false;
		}
		
		long cur = System.currentTimeMillis();
		if (cur > teammatch.getTimestamp())
		{
			return false;
		}
		
		if (cur > roleteammatch.getTimestamp())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 必须在Procedure里执行 根据加锁上下文使用 队伍队长更换交换 by changhao
	 */
	public boolean swapRoleidTeamMatch(long newroleid, long oldroleid, long teamid)
	{
		xbean.ETeamMatch ematch = xtable.Targetid2teammatch.get(0);
		if (ematch == null)
		{
			return false;
		}
		
		xbean.TeamMatch teammatch = ematch.getRoleid2matchdata().remove(oldroleid);
		if (teammatch != null)
		{
			xbean.TeamMatch t0 = ematch.getTeamid2matchdata().get(teamid);
			t0.setRoleid(newroleid);
		
			xbean.TeamMatch t1 = createTeamMatch(teammatch);
			t1.setRoleid(newroleid);
			ematch.getRoleid2matchdata().put(newroleid, t1);
			
			for (xbean.TeamMatch e : ematch.getTeammatchdatalist())
			{
				if (e.getRoleid() == oldroleid)
				{
					e.setRoleid(newroleid);
					break;
				}
			}
			
			SStopTeamMatch msg = new SStopTeamMatch(); //发送一个停止匹配到客户端 by changhao			
			mkdb.Procedure.psendWhileCommit(oldroleid, msg);
			
			SRequestTeamMatch msgb = new SRequestTeamMatch(); //发送一个匹配到客户端 by changhao			
			mkdb.Procedure.psendWhileCommit(newroleid, msgb);
			
			return true;
		}
		
		return false;
	}
	/***
	 * 	队长处在战斗或观战状态? by changhao
	 * @param team
	 * @return
	 */
	public boolean checkTeamInReturnState(Team team)
	{
		if (fire.pb.buff.Module.existState(team.getTeamInfo().getTeamleaderid(), BuffConstant.StateType.STATE_BATTLE_FIGHTER)
				|| fire.pb.buff.Module.existState(team.getTeamInfo().getTeamleaderid(), BuffConstant.StateType.STATE_BATTLE_WATCHER)
				|| fire.pb.buff.Module.existState(team.getTeamInfo().getTeamleaderid(), BuffConstant.StateType.STATE_MINI_GAME)
				|| fire.pb.buff.Module.existState(team.getTeamInfo().getTeamleaderid(), BuffConstant.StateType.STATE_REPLAY))
			return true;
		else
			return false;
	}
	
	/**
	 * 检查队长是否在战斗状态 by changhao
	 * @param team
	 * @return
	 */
	public boolean checkTeamInFightState(Team team)
	{
		if (fire.pb.buff.Module.existState(team.getTeamInfo().getTeamleaderid(), BuffConstant.StateType.STATE_BATTLE_FIGHTER))
		{
			return true;
		}
		
		return false;
	}
	
	/***
	 * 	队伍处在可以归队的状态?（不是战斗等状态）by changhao
	 * @param team
	 * @return
	 */
	public boolean checkTeamReturnStatusValid(Team team)
	{
		BuffAgent agent = new BuffRoleImpl(team.getTeamLeaderId());
		return agent.canAddBuff(OperateType.TEAM_RETURN);
	}
	
	/***
	 * 成员处在可以归队的状态?（不是战斗等状态）by changhao
	 * @param memberRoleId
	 * @return
	 */
	
	public boolean checkMemberReturnStatusValid(long memberRoleId)
	{
		BuffAgent agent = new BuffRoleImpl(memberRoleId);
		return agent.canAddBuff(BuffConstant.StateType.STATE_TEAM_MEMBER_NORMAL);
	}
	
	/**
	 * 
	 * 返回队伍 by changhao
	 * @param team
	 * @param memberRoleId
	 */
	public boolean returnToTeam(Team team, long memberRoleId)
	{
		//请求回归
		if(team.getTeamMemberState(memberRoleId) != TeamMemberState.eTeamAbsent
				&&team.getTeamMemberState(memberRoleId) != TeamMemberState.eTeamReturn)
		{
			//队员不处于暂离中(illegal)
			TeamManager.logger.debug("FAIL:队员不处于暂离中 , memberRoleId" +memberRoleId);
		}
		else if(!checkMemberReturnStatusValid(memberRoleId))
		{
			//成员处在不可归队的状态（战斗等）(illegal)
			TeamManager.logger.debug("FAIL:成员处在不可归队的状态 , memberRoleId" +memberRoleId);
			fire.pb.talk.MessageMgr.psendMsgNotify(memberRoleId, 141412, null);
		}
		else
		{
			if(team.isMemberInReturnScale(memberRoleId))
			{
				//在回归范围之内
				//如果队长在战斗中，则进入归队中状态
				if(checkTeamInReturnState(team))
				{
					//队伍处在不可以归队的状态
					//改变队员为归队中状态，并群发更新状态协议
					team.setTeamMemberStateWithSP(memberRoleId, TeamMemberState.eTeamReturn);
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit( memberRoleId, 160249 , null);
					//归队中状态不需要  排序并广播队员新顺序
					//team.broadcastMemberSequence();
					TeamManager.logger.debugWhileCommit("SUCC:成员回归队伍 , memberRoleId" +memberRoleId);
					
					new PSendWatchBattle(memberRoleId,team.getTeamLeaderId()).call();
					
				}
				else
				{
					if (checkTeamReturnStatusValid(team))
					{
						// 队伍处在可以归队的状态
						// 改变队员为正常状态，并群发更新状态协议
						team.setTeamMemberStateWithSP(memberRoleId, TeamMemberState.eTeamNormal);
						// 排序并广播队员新顺序
//						team.updateMemberSequenceWithSendProtocol();
						TeamManager.logger.debugWhileCommit("SUCC:队伍处在可以归队的状态，改变队员为正常状态 , memberRoleId" + memberRoleId);
					}
					else
					{
						TeamManager.logger.debug("FAIL: 队伍处于不可归队的状态，teamId" +team.teamId);
						return true;
					}
				}								
			
			}
			else
			{
				//在回归范围之外
				//TODO 自动寻路找队长  OR  返回不能回归的消息
				//psend(memberRoleId, new STeamError(TeamError.TooFar));
				//fire.pb.talk.Message.psendMsgNotify(memberRoleId, 141205, null);
				//TeamManager.logger.debug("FAIL:在回归范围之外 , memberRoleId" +memberRoleId);
				
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * 如果需要拉他到队长旁边 by changhao
	 * 返回0表示没成功因为工会不同1表示已经传送到公会副本2表示成功3不成功其他原因 by changhao
	 */
	public int execGotoLeader(long roleid, Team team, boolean autoreturn, int delay)
	{		
		long leaderroleId = team.getTeamInfo().getTeamleaderid();
		Role memberRole = RoleManager.getInstance().getRoleByID(roleid);
		Role leaderRole = RoleManager.getInstance().getRoleByID(leaderroleId);
		
		if (memberRole == null || leaderRole == null) {
			TeamManager.logger.error("角色没在场景中");
			return 3;
		}
		
		if (memberRole.getScene() != leaderRole.getScene()) {
			MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(leaderRole.getMapId());
			if (cfg == null) {
				TeamManager.logger.error("角色没在场景中");
				return 3;
			}
			
			if (cfg.dynamic == 1) {
				//判断队长是否在精英副本里
				if (fire.pb.buff.Module.existState(leaderroleId, BuffConstant.StateType.STATE_INSTANCE_ZONE)) {
					
					//给申请入队的成员发送队长的副本进度
					xbean.InstanceInfoCol teamLeadreInfoCol = xtable.Roleinstancetask.get(leaderroleId);
					if (teamLeadreInfoCol == null) {
						return -1;
					}
					//要进入的副本的id
					final int lineId = teamLeadreInfoCol.getLastinstanceid();
					xbean.InstanceTaskInfo teamLeadreTaskInfo = teamLeadreInfoCol.getInstinfo().get(lineId);
					if (teamLeadreTaskInfo == null) {
						return -1;
					}
					
					//判断玩家等级
					if (!LineInstManager.checkTask(team, lineId, roleid))
						return -1;
					
					//判断玩家副本开启情况
					if (!LineInstManager.checkInstState(team, roleid, lineId))
						return -1;
					
					xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.get(roleid);
					if (infoCol == null) {
						infoCol = xbean.Pod.newInstanceInfoCol();
						xtable.Roleinstancetask.insert(roleid, infoCol);
					}

					xbean.InstanceTaskInfo taskInfo = infoCol.getInstinfo().get(lineId);
					if (taskInfo == null) {
						taskInfo = xbean.Pod.newInstanceTaskInfo();
						infoCol.getInstinfo().put(lineId, taskInfo);
					}
					
					long currentTime = System.currentTimeMillis();
					if (!DateValidate.inTheSameDay(infoCol.getAccepttime(), currentTime)) {
						infoCol.setCounts(0);
						infoCol.setAccepttime(currentTime);
						infoCol.getInstcount().clear();
					}
					
					if (!DateValidate.inTheSameDay(taskInfo.getAccepttime(), currentTime)) {
						taskInfo.setCounts(0); // 不在同一天则清0
						taskInfo.setCurrentstep(1);
						taskInfo.setCurrenttaskid(0);
						taskInfo.getStepawardtimes().clear();
						taskInfo.setAccepttime(currentTime);
					}
					
					//重新进入副本能再领取奖励
					taskInfo.setAwarding(1);
					
					int step = LineInstManager.getStep(infoCol, lineId);
					SDefineTeam defineTeam = new SDefineTeam();
					defineTeam.instid = lineId;
					defineTeam.mystep = step;
					defineTeam.tlstep = teamLeadreTaskInfo.getCurrentstep();
					
					mkdb.Procedure.psendWhileCommit(roleid, defineTeam);
					return 4;
				}
				
				//判断玩家所在的地图是不是公会地图或者公会副本地图
				if (ClanUtils.isInClanMap(leaderRole.getRoleID())) {
					xbean.ClanInfo leaderclaninfo = ClanUtils.getClanInfoById(leaderroleId, true);
					if (leaderclaninfo == null) {
						fire.pb.talk.MessageMgr.sendMsgNotify(memberRole.getRoleID(), 160168 ,null);
						return 0;
					}

					xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true); 
					if (clanInfo == null || clanInfo.getKey() != leaderclaninfo.getKey()) {
						fire.pb.talk.MessageMgr.sendMsgNotify(memberRole.getRoleID(), 160168, null);
						return 0;
					}
					else {//直接进入队长所在的动态场景中
						GridPos gridPos = leaderRole.getPos().toGridPos();
						DuplicateHelper.enterDynamicSceneByIdWhileCommit(roleid, leaderRole.getMapId(), leaderRole.getScene(),
								gridPos.getX(), gridPos.getY(),
								DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CHEFU);
					}
				}
				else {
					if (xtable.Role2instancezone.select(leaderroleId) != null) {// 当前在副本中
						Procedure.pexecuteWhileCommit(new PEnterInstZone(leaderroleId, roleid));
					}
					
					else {
						//直接进入队长所在的动态场景中
						GridPos gridPos = leaderRole.getPos().toGridPos();
						DuplicateHelper.enterDynamicSceneByIdWhileCommit(roleid, leaderRole.getMapId(), leaderRole.getScene(),
								gridPos.getX(), gridPos.getY(),
								DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CHEFU);
					}
				}
				
				PAbsentReturnTeam absentReturnTeamP = new PAbsentReturnTeam(roleid, 0);	
				mkdb.Executor.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
					   absentReturnTeamP.submit();
					}
				}, delay, TimeUnit.SECONDS);
				
				return 1;
			}
			
			GridPos gridPos = leaderRole.getPos().toGridPos();
			boolean transok = ExecuteReqGoto.trans2Pos(roleid, leaderRole.getMapId(),gridPos.getX(), gridPos.getY(), false);
			if(transok)
			{
				if (autoreturn)
				{
					//准备回归 by changhao
					PAbsentReturnTeam absentReturnTeamP = new PAbsentReturnTeam(roleid, 0);	
					mkdb.Executor.getInstance().schedule(new Runnable() //延迟2秒在执行 by changhao
					{
						@Override
						public void run()
						{
						   absentReturnTeamP.submit();
						}
					}, delay, TimeUnit.SECONDS);
				}
			}
			else
			{
				return 3;
			}
		}
		else
		{
			MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(leaderRole.getMapId());
			if (cfg == null) {
				TeamManager.logger.error("角色没在场景中");
				return 3;
			}
			
			GridPos gridPos = leaderRole.getPos().toGridPos();
			boolean transok = ExecuteReqGoto.trans2Pos(roleid, leaderRole.getMapId(),gridPos.getX(), gridPos.getY(), true);
			if (transok == false)
			{
				return 3;
			}
			else
			{
				if (autoreturn)
				{
					//准备回归 by changhao
					PAbsentReturnTeam absentReturnTeamP = new PAbsentReturnTeam(roleid, 0);	
					mkdb.Executor.getInstance().schedule(new Runnable() //延迟2秒在执行 by changhao
					{
						@Override
						public void run()
						{
						   absentReturnTeamP.submit();
						}
					}, delay, TimeUnit.SECONDS);
				}				
			}
		}
		
		return 2;
	}
	
	/**
	 * 成员处在可以离队的状态?（不是战斗等状态）by changhao
	 * @param memberRoleId
	 * @return
	 */
	public boolean checkMemberAbsentStatusValid(long memberRoleId)
	{
		BuffAgent agent = new BuffRoleImpl(memberRoleId);
		return agent.canAddBuff(OperateType.TEAM_ABSENT);
	}
	
	/**
	 * 申请者是否在线 by changhao
	 * @param roleId
	 * @return
	 */
	public boolean checkOnline(long roleId)
	{
		if(StateCommon.isOnline(roleId))
			return true;
		else
			return false;
	}
				
	/**
	 * 是否在队伍中
	 * @param roleId
	 * @return
	 */
	public static boolean isInTeam(long roleId)
	{
		if(xtable.Roleid2teamid.select(roleId)!=null)
			return true;
		else 
			return false;
	}
	
	/**
	 * 是否是队长
	 * @param roleId
	 * @return
	 */
	public static boolean isTeamLeader(long roleId)
	{
		Team team = TeamManager.getTeamByRoleId(roleId);
		if(team == null)
			return false;
		return team.isTeamLeader(roleId);
	}
	
	public static boolean isTeamMember(long roleId)
	{
		Team team = TeamManager.getTeamByRoleId(roleId);
		if(team == null)
			return false;
		return team.isTeamMember(roleId);
	}

	/**
	 * 为战斗中拿光环
	 * @param roleId
	 * @return 0为无生效光环
	 */
	public static int getTeamFormationForBattle(long roleId){
		if(roleId == 0){
			return 0;
		}
		Team team = TeamManager.selectTeamByRoleId(roleId);
		
		//不在队伍中的返回个人光环
		PropRole role = new PropRole(roleId, true);
		if(team == null||team.getTeamMemberState(roleId) != TeamMemberState.eTeamNormal){
			return role.getDealutFormId();
		}
		
		//开启限制，不是队长，队伍不满不生效
		if(team.isTeamLeader(roleId)){
			return team.getTeamInfo().getFormation();
		}else{
			return role.getDealutFormId();
		}
	}
	
	public static int getTeamFormationLevelForBattle(long roleId){
		if(roleId == 0){
			return 0;
		}
		Team team = TeamManager.selectTeamByRoleId(roleId);
		//不在队伍中的返回个人光环
		PropRole role = new PropRole(roleId, true);
		int formId = role.getDealutFormId();
		if(team == null||team.getTeamMemberState(roleId) != TeamMemberState.eTeamNormal){
			return role.getFormLevel(formId);
		}
		
		//开启限制，不是队长，队伍不满不生效
		if(team.isTeamLeader(roleId)){
			return team.getTeamInfo().getFormationlevel();
		}else{
			return role.getFormLevel(formId);
		}
	}
	
	
	/**
	 * 处理战斗中失败的队员，例如中途逃跑，战斗结束时死亡等
	 * @param roleId
	 * @param battle
	 */
	public static void processFailedMemberWhileOutBattle(final long roleId, xbean.BattleInfo battle)
	{
		Team team = TeamManager.getTeamByRoleId(roleId);
		if (team == null)
			return;// 没有队伍直接返回
		if (team.isTeamLeader(roleId)) {
			// 如果是队长，并且还有其他未出战斗的队员，换队长，暂离
			List<Long> normalIds = team.getNormalMemberIds();
			normalIds.remove(roleId);// 除去队长
			for (Iterator<Long> it = normalIds.iterator(); it.hasNext();) {
				long memberid = it.next();
				Integer fid = battle.getRoleids().get(memberid);
				if (fid == null)
					it.remove();
				else if (!BattleField.checkLiveInBattle(battle, fid) && !BattleField.checkLiveInBattle(battle, fid+5)) {
					//人死了，宠物也没活，暂离并删掉
					int teamstate = team.getTeamMemberState(memberid);
					if (teamstate == TeamMemberState.eTeamNormal)
						team.setTeamMemberStateWithSP(memberid, TeamMemberState.eTeamAbsent);
					it.remove();
				}
			}
			if (normalIds.size() > 0)// 如果还有正常的在战斗内的队员
				team.passiveSwitchLeaderWithSP(TeamMemberState.eTeamAbsent);
		} else {
			// 如果是队员，则检查状态,设置为暂离
			int teamstate = team.getTeamMemberState(roleId);
			if (teamstate == TeamMemberState.eTeamNormal)
				team.setTeamMemberStateWithSP(roleId, TeamMemberState.eTeamAbsent);
		}
	}
	
	/**
	 * 获取一个过滤器
	 * @param leaderId
	 * @return
	 */
	public static TeamFilter getActiveFilter(long leaderId)
	{
		TeamFilter filter1 = new TestTeamFilter();
		if(filter1.checkActive(leaderId)){
			return filter1;
		}
		
		return null;
	}
}
