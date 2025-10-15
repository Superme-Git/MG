package fire.pb.clan.fight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import mkdb.Lockeys;
import mkdb.Procedure;
import mkdb.Transaction;
import fire.log.Logger;
import fire.pb.PAddExpProc;
import fire.pb.StateCommon;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.clanfight.ActivityClanFightManager;
import fire.pb.clan.ClanUtils;
import fire.pb.main.ConfigManager;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.MapConfig;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.map.Transfer;
import fire.pb.mission.ExecuteReqGoto;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.activelist.SActivityOpen;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.npc.ChestManager;
import fire.pb.npc.SBaoxiang;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.MapUtil;
import fire.pb.scene.SceneManager;
import fire.pb.scene.sPos.GridPos;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;

/***
 * 公会战场 by changhao
 */
public class ClanFightBattleField
{
	static long DELETE_TIMER = 5000; //战场删除定时器 by changhao
	static long COMMON_TIMER = 5001; //普通定时器 （加经验，加积分 等) by changhao
	static long CLANFIGHT_OVER = 5002; //战场结束定时器 by changhao
	static long CLANFIGHT_ADDSCROE = 5003; //战场积分定时器 by changhao
	
	final public long clanfightid;
	final public fire.pb.clan.SClanFight config;
	final public xbean.ClanFightBattleField clanfightbean;
	final protected boolean readonly;
	
	public class RankInfo
	{
		int rank;
		int scroe;
	};

	public static final Logger logger = Logger.getLogger("CLAN");

	public ClanFightBattleField(long key, xbean.ClanFightBattleField clanfightbean, fire.pb.clan.SClanFight config, boolean readonly)
	{
		this.clanfightid = key;
		this.clanfightbean = clanfightbean;
		this.readonly = readonly;
		this.config = config;
	}

	public xbean.ClanFightBattleField getClanfightBean()
	{
		return clanfightbean;
	}

	public fire.pb.clan.SClanFight getConfig()
	{
		return config;
	}
	
	/***
	 * 初始化战场开始结束时间等 by changhao
	 * @throws Exception
	 */
	public void initTime() throws Exception
	{
		long cur = System.currentTimeMillis();
		
		long starttime = 0;
		long endtime = 0;
		if (clanfightbean.getDatetime() == ActivityClanFightManager.GetMonday1500ByTime(cur))
		{
			starttime = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().GetClanFightActivityStartTime(2);
			endtime = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().GetClanFightActivityEndTime(2);
		}
		else if (clanfightbean.getDatetime() == ActivityClanFightManager.GetWednesday1500ByTime(cur))
		{
			starttime = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().GetClanFightActivityStartTime(4);
			endtime = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().GetClanFightActivityEndTime(4);
		}
		
		clanfightbean.setActivitystarttime(starttime);		
		clanfightbean.setActivitypreparetime(starttime + 60 * 1000 * 30);
		clanfightbean.setActivitynoentertime(starttime + 60 * 1000 * 40);
		clanfightbean.setActivityendtime(endtime);
	}

	boolean init() throws Exception
	{
		//初始化活动时间相关 by changhao
		initTime();
		
		//创建动态地图 by changhao
		initMap();
	
		//创建战场需要的定时器 by changhao
		createTimer();
		
		clanfightbean.setState(xbean.ClanFightBattleField.STATE_CLAN_FIGHT_NORMAL);
		
		return true;
	}

	private void initMap()
	{
		//创建动态地图 by changhao
		MapConfig mapcfg = ConfigManager.getInstance().getConf(MapConfig.class).get(config.mapid);		
		if (mapcfg != null)
		{
			int mapId = mapcfg.id;
			if (mapcfg.dynamic == 1)
			{
				DuplicateHelper.createDynamicMap(mapId, clanfightid, "",true, -1, DynamicSceneType.NORMAL_SCENE, null); //FIXME
			}
		}
	}
	
	/***
	 * 销毁动态地图 by changhao
	 */
	void deinitMap()
	{
		//销毁地图 by changhao
		MapConfig mapcfg = ConfigManager.getInstance().getConf(MapConfig.class).get(config.mapid);		
		if (mapcfg != null)
		{
			if (mapcfg.dynamic == 1)
			{
				DuplicateHelper.destroyDynamicSceneWhileCommit(mapcfg.id, clanfightid);
			}
		}
	}
	
	/***
	 * 创建公会战场用的定时器 by changhao
	 */
	public void createTimer() throws Exception
	{
		long cur = System.currentTimeMillis();
				
		//注册公会战场结束定时器 by changhao
		long delta = clanfightbean.getActivityendtime() - cur;
		if (delta < 0)
		{
			delta = 0;
		}
		
		long activitydelta = clanfightbean.getActivitystarttime() - cur;
		if (activitydelta < 0)
		{
			activitydelta = 0;
		}
		
		long t = getClanfightBean().getDatetime();
		long index = clanfightid - t;
		
		//战场积分定时器 by changhao
		ScheduledFuture<?> addScroeTimer = mkdb.Mkdb.executor().scheduleAtFixedRate(new PClanFightAddScroeTask(this.clanfightid), (index * 4 + 0) * 100 + activitydelta, 60 * 1000 * 4, TimeUnit.MILLISECONDS);
		fire.pb.instancezone.Module.getInstance().createInstMapFuture(this.clanfightid + CLANFIGHT_ADDSCROE * (index + 1), addScroeTimer);
		
		//创建删除战场定时器 by changhao
		ScheduledFuture<?> deleteTimer = mkdb.Executor.getInstance().schedule(new PClanFightDestroyTask(clanfightid), (index * 4 + 1) * 100 + delta + 1 * 60 * 45 * 1000,TimeUnit.MILLISECONDS);
		fire.pb.instancezone.Module.getInstance().createInstMapFuture(this.clanfightid + DELETE_TIMER * (index + 1), deleteTimer);	
		
		//创建普通定时器 by changhao
		ScheduledFuture<?> commonTimer = mkdb.Mkdb.executor().scheduleAtFixedRate(new PClanFightTimerTask(this.clanfightid), (index * 4 + 2) * 100 + activitydelta, 60 * 1000, TimeUnit.MILLISECONDS);
		fire.pb.instancezone.Module.getInstance().createInstMapFuture(this.clanfightid + COMMON_TIMER * (index + 1), commonTimer);
		
		ScheduledFuture<?> clanfightendTimer = mkdb.Executor.getInstance().schedule(new PClanFightTimeOutEndTask(clanfightid), (index * 4 + 3) * 100 + delta,TimeUnit.MILLISECONDS);
		fire.pb.instancezone.Module.getInstance().createInstMapFuture(this.clanfightid + CLANFIGHT_OVER * (index + 1), clanfightendTimer);
	}
	
	/***
	 * 删除定时器 by changhao
	 */
	public void destroyTimer()
	{
		long t = getClanfightBean().getDatetime();
		long index = clanfightid - t;
		
		fire.pb.instancezone.Module.getInstance().cancleInstMapFuture(this.clanfightid + DELETE_TIMER * (index + 1));
		fire.pb.instancezone.Module.getInstance().cancleInstMapFuture(this.clanfightid + COMMON_TIMER * (index + 1));
		fire.pb.instancezone.Module.getInstance().cancleInstMapFuture(this.clanfightid + CLANFIGHT_OVER * (index + 1));
		fire.pb.instancezone.Module.getInstance().cancleInstMapFuture(this.clanfightid + CLANFIGHT_ADDSCROE * (index + 1));
	}

	/***
	 * 进入战场 by changhao
	 * @param roleId
	 * @return
	 */
	protected boolean enterBattleFieldMap(long roleId, int which)
	{	
		MapConfig mapcfg = ConfigManager.getInstance().getConf(MapConfig.class).get(config.mapid);
		if (mapcfg.dynamic == 0)
		{
			return Transfer.justGotoWhileCommit(roleId, config.mapid, config.x1, config.y1, SRoleEnterScene.CHEFU);
		}
		else
		{
			int x = config.x1;
			int y = config.y1;
			if (which == 1)
			{
				x = config.x2;
				y = config.y2;
			}
			
			return DuplicateHelper.enterDynamicSceneWhileCommit(roleId, config.mapid, x, y, clanfightid, config.name, -1,DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CHEFU, null);
		}
	}

	/***
	 * 离开战场 by changhao
	 * lock roleid;
	 */
	protected void leaveBattleFieldMap(long roleId)
	{
		java.util.List<Long> roleids = new java.util.ArrayList<Long>();
		roleids.add(roleId);
		// 如果是公会领地 by changhao
		xbean.Properties roleprop = xtable.Properties.get(roleId);
		if (roleprop == null)
		{
			return;
		}
		
		Long clankey = roleprop.getClankey();
		if (clankey == null || clankey <= 0)
		{
			MessageMgr.sendMsgNotify(roleId, 145077, null);
			return;
		}

		DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(ClanUtils.MAPID, clankey);
		long destsceneid = 0;
		int x = config.outx1;
		int y = config.outy1;
		if (ds == null)
		{
			xbean.Properties prop = xtable.Properties.get(roleId);
			if (prop == null)
			{
				return;
			}
			
			destsceneid = prop.getLaststaticsceneid();
			if (destsceneid == 0)
			{
				destsceneid = ActivityClanFightManager.OUT_MAP_ID;
				x = ActivityClanFightManager.OUT_MAP_ID_X;
				y = ActivityClanFightManager.OUT_MAP_ID_Y;
			}
			else
			{
				fire.pb.scene.sPos.Position pos = new fire.pb.scene.sPos.Position(prop.getLaststaticposx(), prop.getLaststaticposy(), 0);
				GridPos gridpos = pos.toGridPos();
				x = gridpos.getX();
				y = gridpos.getY();				
			}
		}
		else
		{
			destsceneid = ds.getSceneID();
		}

		final Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
		if (team != null) 
		{
			if (team.isTeamLeader(roleId))
			{
				Transfer.justGotoWhileCommit(roleId, destsceneid, x, y, SRoleEnterScene.CHEFU);
			}
		}
		else 
		{
			Transfer.justGotoWhileCommit(roleId, destsceneid, x, y, SRoleEnterScene.CHEFU);
		}
	}

	/**
	 * 跳转地图 by changhao
	 * @param roleId
	 * @param oldSceneId
	 * @param newSceneId
	 */
	public void afterGoto(long roleId, long oldSceneId, long newSceneId, boolean dismiss)
	{
		int newMapId = (int) newSceneId;
		int oldMapId = (int)oldSceneId;
		if ((config.mapid != newMapId) && (config.mapid == oldMapId))
		{
			if (!leaveBattleField(roleId, false, false))
			{
				xtable.Roleid2clanfightid.remove(roleId);
				logger.error("角色(ID=" + roleId + ")离开战场时出现异常:" + config.mapid);
			}
			else
			{
				logger.error("角色(ID=" + roleId + ")正常离开战场:" + config.mapid);				
			}
		}
		else if (config.mapid != newMapId && config.mapid != oldMapId)
		{
			leaveBattleField(roleId, false, dismiss);
			xtable.Roleid2clanfightid.remove(roleId);	
			logger.error("角色(ID=" + roleId + ")清除战场状态");
		}
	}

	/***
	 * 进入战场 by changhao
	 * @param roleId
	 * @param which
	 * lock teamid -> roleids by changhao
	 * @return
	 */
	public int enterBattleField(long roleId, int which)
	{
		long cur = System.currentTimeMillis();
		
		List<Long> roleids = new java.util.ArrayList<Long>();
		
		Team team = TeamManager.getTeamByRoleId(roleId);
		if (team != null)
		{
			roleids.addAll(team.getNormalMemberIds());
		}
		else
		{
			roleids.add(roleId);
		}
		
		if (roleids.isEmpty())
		{
			return 1;
		}
		
		if (this.getClanfightBean().getState() == xbean.ClanFightBattleField.STATE_CLAN_FIGHT_OVER)
		{
			MessageMgr.sendMsgNotify(roleId, 410055, null);
			return 4;
		}
		
		mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));
		
		for (Long eroleid : roleids)
		{
			//获取角色公会战时的统计 by changhao
			xbean.EClanFightStatistics statistics = xtable.Roleid2clanfightstatistics.get(eroleid);
			if (statistics == null)
			{
				statistics = xbean.Pod.newEClanFightStatistics();
				xtable.Roleid2clanfightstatistics.insert(eroleid, statistics);
				
				if (cur < getClanfightBean().getActivitypreparetime())
				{
					statistics.setAct(ActivityClanFightManager.INIT_ACT);
				}
				else if (cur < getClanfightBean().getActivitynoentertime())
				{
					statistics.setAct(ActivityClanFightManager.INIT_ACT1);
				}
			}
			else
			{
				long entertime = statistics.getEntertime();
				if (entertime != 0)
				{
					if(!DateValidate.inTheSameDay(cur, entertime))
					{
						if (cur < getClanfightBean().getActivitypreparetime())
						{
							statistics.setAct(ActivityClanFightManager.INIT_ACT);
						}
						else if (cur < getClanfightBean().getActivitynoentertime())
						{
							statistics.setAct(ActivityClanFightManager.INIT_ACT1);
						}
						
						statistics.setClancelebrateitemnum(0);
						statistics.setWinneritemnum(0);
						statistics.setScore(0);
						statistics.setLastcbattlefieldrankliststamp(0);
						statistics.setWinnum(0);
					}
				}
			}
			
			statistics.setEntertime(cur);
			
			if (statistics.getAct() <= 0) //行动力不足不能进了 by changhao
			{
				String name = xtable.Properties.selectRolename(eroleid);
				java.util.List<String> p = new java.util.ArrayList<String>();
				p.add(name);
				MessageMgr.sendMsgNotify(roleId, 410066, p);				
				return 2;
			}
			
			String name = xtable.Properties.selectRolename(eroleid);
			clanfightbean.getEnterroleids().put(eroleid, name);
			
			if (which == 0)
			{
				clanfightbean.getClan1roleids().put(eroleid, statistics.getScore());
				clanfightbean.getClan1enterroleids().put(eroleid, 0);
			}
			else
			{
				clanfightbean.getClan2roleids().put(eroleid, statistics.getScore());
				clanfightbean.getClan2enterroleids().put(eroleid, 0);
			}
			
			boolean removed = xtable.Roleid2clanfightid.remove(eroleid);
			if (removed)
			{
				logger.error("角色(ID = " + eroleid + ")已经在战场中了 = " + config.mapid);
			}
			
			xtable.Roleid2clanfightid.insert(eroleid, clanfightid);
			
			SBattleFieldAct msg = new SBattleFieldAct();
			msg.roleact = statistics.getAct();
			mkdb.Procedure.psendWhileCommit(eroleid, msg); //发送行动力数据 by changhao
			
			this.addActivityValue(eroleid);
			//添加记录公会战参加次数
			fire.pb.clan.srv.ClanManage.addClanFightNum(eroleid, 1);
						
			logger.info(new StringBuilder().append("role 进入战场,roleid:").append(eroleid).append(",landkey:").append(clanfightid));
		}
		
		if (!enterBattleFieldMap(roleId, which))
		{
			return 3;
		}
		
		for (Long eroleid : roleids)
		{
			fire.pb.clan.fight.SBattleFieldInfo infomsg = new fire.pb.clan.fight.SBattleFieldInfo();
			infomsg.clanid1 = getClanfightBean().getClanid1();
			infomsg.clanid2 = getClanfightBean().getClanid2();
			infomsg.clanname1 = getClanfightBean().getClanname1();
			infomsg.clanname2 = getClanfightBean().getClanname2();
			mkdb.Procedure.psendWhileCommit(eroleid, infomsg);				
		}
		
		TeamManager.getInstance().delTeamMatchAsyn(roleId);
		
		return 0;
	}

	/***
	 * 离开战场 by changhao
	 * @param roleId
	 * lock teamid -> roleid by changhao
	 * @return
	 */
	public boolean leaveBattleField(long roleId, boolean trans, boolean dismiss)
	{	
		if (!clanfightbean.getClan1roleids().containsKey(roleId) && !clanfightbean.getClan2roleids().containsKey(roleId))
		{
			return false;
		}
		
		List<Long> leaveRoleIds = new LinkedList<Long>();
		List<Long> quitteamroleids = new LinkedList<Long>(); //需要离队的角色 by changhao
		Team team = TeamManager.getTeamByRoleId(roleId); //锁定teamid by changhao
		
		long cur = System.currentTimeMillis();
		if (team != null)
		{
			int num = team.getNormalMemberIds().size();
			boolean fightperiod = false;
			if (cur > getClanfightBean().getActivitypreparetime() && cur < getClanfightBean().getActivityendtime() && getClanfightBean().getState() != xbean.ClanFightBattleField.STATE_CLAN_FIGHT_OVER)
			{
				fightperiod = true;
			}
					
			if (num != team.getAllMemberIds().size() || ((fightperiod == true) && dismiss == true) || dismiss == true) //如果有非正常队员 by changhao
			{
				if (team.isTeamLeader(roleId))
				{
					quitteamroleids.add(roleId);
				}
				else if (team.isAbsentMember(roleId))
				{
					quitteamroleids.add(roleId);
				}
				else
				{
					quitteamroleids.add(roleId);
				}				
			}
			else
			{
				leaveRoleIds.addAll(team.getAllMemberIds());
			}
		}
		else
		{
			leaveRoleIds.add(roleId);
		}
		
		java.util.Set<Long> locks = new java.util.HashSet<Long>();
		locks.addAll(leaveRoleIds);
		locks.addAll(quitteamroleids);
		mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, locks));

		for (Long r : leaveRoleIds)
		{
			clanfightbean.getClan1roleids().remove(r);
			clanfightbean.getClan2roleids().remove(r);
			
			boolean removed = xtable.Roleid2clanfightid.remove(r);
			if (!removed)
			{
				logger.error("角色 ID = " + r + " 离开战场时Roleid2clanfightinstance表里没有roleid。ID = "+ config.id);
			}
			
			SLeaveBattleField msg = new SLeaveBattleField();
			mkdb.Procedure.psendWhileCommit(r, msg);
			
			if (trans == true)
			{
				leaveBattleFieldMap(r);				
			}
		}
		
		for (Long e : quitteamroleids) //执行离队操作 by changhao
		{
			mkdb.Procedure.pexecuteWhileCommit(new fire.pb.team.PQuitTeamProc(e));
			mkdb.Executor.getInstance().schedule(new PClanFightLeaveClanFieldBattleFieldTask(clanfightid, roleId, true, dismiss), 2000, TimeUnit.MILLISECONDS);		
		}
		
		checkClanFightOver();

		return true;
	}

	/***
	 * @param 删除战场 by changhao
	 * lock  roleids by changhao
	 */
	public boolean destroyBattleField()
	{	
		mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, getClanfightBean().getEnterroleids().keySet()));		
		for (Long ee : getClanfightBean().getEnterroleids().keySet())
		{
			Long r = xtable.Roleid2clanfightid.get(ee);
			if (r != null)
			{
				xtable.Roleid2clanfightid.remove(r);
			}
		}
		
		deinitMap();
		
		//删除定时器 by changhao
		destroyTimer();
	
		return true;
	}
	
	/***
	 * 给公会加积分 by changhao
	 * which == 0 是左边公会 which == 1 是右边公会 by changhao
	 * @param which
	 * @param score
	 */
	public void addClanScore(int which, int score)
	{
		if (clanfightbean != null)
		{
			if (which == 0)
			{
				clanfightbean.setClanscore1(clanfightbean.getClanscore1() + score);				
			}
			else if (which == 1)
			{
				clanfightbean.setClanscore2(clanfightbean.getClanscore2() + score);				
			}
		}
	}
	
	/***
	 * 通过公会id 得到是战场那边（左边0右边1 中立-1) by changhao
	 * @param clanid
	 * @return
	 */
	public int getSideByClanid(long clanid)
	{
		if (clanfightbean.getClanid1() == clanid)
		{
			return 0;
		}
		
		if (clanfightbean.getClanid2() == clanid)
		{
			return 1;
		}
		
		return -1;
	}
	
	/***
	 * 通过role id 得到是战场那边（左边0右边1 中立-1) by changhao
	 * @param clanid
	 * @return
	 */
	public int getSideByRoleId(long roleid)
	{
		if (clanfightbean.getClan1roleids().containsKey(roleid))
		{
			return 0;
		}
		
		if (clanfightbean.getClan2roleids().containsKey(roleid))
		{
			return 1;
		}
		
		return -1;
	}
	
	/***
	 * 设置某个角色的积分 by changhao
	 */
	public void setRoleIdScore(long roleid, int score)
	{
		if (clanfightbean.getClan1roleids().containsKey(roleid))
		{
			clanfightbean.getClan1roleids().put(roleid, score);
		}
		else if (clanfightbean.getClan2roleids().containsKey(roleid))
		{
			clanfightbean.getClan2roleids().put(roleid, score);			
		}
	}
	
	/***
	 * 得到角色的积分通过id by changhao
	 * @param roleid
	 * @return
	 */
	public RankInfo getRankSorceByRoleId(long roleid)
	{
		RankInfo info = new RankInfo();
		info.rank = -1;
		
		int which = this.getSideByRoleId(roleid);
		if (which == 0)
		{
			Integer rank = clanfightbean.getClan1rolerankmap().get(roleid);
			if (rank != null)
			{
				info.rank = rank;
				xbean.BattleFieldScroeRank r = clanfightbean.getClan1roleranklist().get(rank);
				if (r != null)
				{
					info.scroe = r.getScroe();
				}
			}				
		}
		else if (which == 1)
		{
			Integer rank = clanfightbean.getClan2rolerankmap().get(roleid);
			if (rank != null)
			{
				info.rank = rank;
				xbean.BattleFieldScroeRank r = clanfightbean.getClan2roleranklist().get(rank);
				if (r != null)
				{
					info.scroe = r.getScroe();
				}
			}				
		}
		
		return info;
	}
	
	/***
	 * 排序战场角色根据积分几分钟做一次 by changhao
	 */
	public void scroeSort(int which, boolean focre)
	{
		long cur = System.currentTimeMillis();
		long stamp = 0;
		
		java.util.Map<Long, Integer> w = null;
		java.util.List<xbean.BattleFieldScroeRank> w1 = null;
		java.util.Map<Long, Integer> w2 = null;
		if (which == 0)
		{
			w = clanfightbean.getClan1roleids();
			w1 = clanfightbean.getClan1roleranklist();
			w2 = clanfightbean.getClan1rolerankmap();
			stamp = clanfightbean.getLastclan1rolesortstamp();
		}
		else if (which == 1)
		{
			w = clanfightbean.getClan2roleids();
			w1 = clanfightbean.getClan2roleranklist();
			w2 = clanfightbean.getClan2rolerankmap();
			stamp = clanfightbean.getLastclan2rolesortstamp();
		}
		
		if (cur < stamp + 60000 * 1 && focre == false) //至少1分钟才能排一次 by changhao
		{
			return;
		}
				
		if (w != null && w1 != null)
		{
			List<xbean.BattleFieldScroeRank> list = new ArrayList<xbean.BattleFieldScroeRank>();
			
			for (Long r : w.keySet())
			{
				Integer v = w.get(r);
				xbean.BattleFieldScroeRank scroerank = xbean.Pod.newBattleFieldScroeRank();
				scroerank.setRoleid(r);
				scroerank.setScroe(v);
				list.add(scroerank);
			}			
			
			//分数从大到小排序 by changhao
			list.sort(new Comparator<xbean.BattleFieldScroeRank>()
			{
				@Override
				public int compare(xbean.BattleFieldScroeRank o1, xbean.BattleFieldScroeRank o2)
				{
					int ret = o2.getScroe() - o1.getScroe();
					if (ret != 0)
					{
						return ret;
					}
					else
					{
						long r = o1.getRoleid() - o2.getRoleid();
						return (int)r;						
					}
				}
			});
		
			w1.clear();
			
			int count = 0;
			
			if (w2 != null)
			{
				w2.clear();				
			}
			
			for (xbean.BattleFieldScroeRank e : list)
			{
				if (w2 != null)
				{
					w2.put(e.getRoleid(), count);
					count ++;					
				}
			}
			
			w1.addAll(list);
			
			if (which == 0)
			{
				clanfightbean.setLastclan1rolesortstamp(cur);
			}
			else if (which == 1)
			{
				clanfightbean.setLastclan2rolesortstamp(cur);
			}
		}
	}
	
	/***
	 * 刷新胜利宝箱 by changhao
	 * @param npckey
	 * @param saveId
	 */
	public void createVictoryBox(int num)
	{
		DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(this.getConfig().mapid, clanfightid);
		if (ds == null)
		{
			return;
		}
		
		for (int i = 0; i < num; i ++)
		{
			final long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();
			
			int mapid = this.getConfig().mapid;
			
			SceneNpcManager.createNpcByMap(npcKey, ActivityClanFightManager.VICTORY_BOX, "", mapid, clanfightid, 3, 0);
			
			clanfightbean.getVictoryboxids().add(npcKey);
		}
	}
	
	/***
	 * 刷新庆贺宝箱 by changhao
	 */
	public void createCelebrateBox(int whichclanwin)
	{	
		//胜利的公会有庆贺宝箱 by changhao
		if (whichclanwin != -1) //获胜方才刷庆贺宝箱 by changhao
		{
			long winclanid = 0;
			int num = 0;
			if (whichclanwin == 0)
			{
				winclanid = getClanfightBean().getClanid1();
				num = getClanfightBean().getClan1enterroleids().size();
			}
			else
			{
				winclanid = getClanfightBean().getClanid2();
				num = getClanfightBean().getClan2enterroleids().size();
			}
			
			DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(ClanUtils.MAPID, winclanid);
			if (ds == null)
			{
				return;
			}
			
			for (int i = 0; i < num; i ++)
			{
				final long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();		
				SceneNpcManager.createNpcByMap(npcKey, ActivityClanFightManager.CELEBRATE_BOX, "", ClanUtils.MAPID, winclanid, 3, 0); //公会领地刷新庆贺宝箱 by changhao
				
				clanfightbean.getVictoryboxids().add(npcKey);
			}
			
			if (num > 0)
			{
				if (whichclanwin == 0) //向获胜方发布公会地图中,散落了庆贺宝箱,所有参加了公会战的玩家都可以开启。by changhao
				{
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(getClanfightBean().getClanid1(), 410036, 0, null);
				}
				else
				{
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(getClanfightBean().getClanid2(), 410036, 0, null);				
				}				
			}
		}
	}
	
	/***
	 * 销毁胜利宝箱 by changhao
	 */
	public void destroyVictoryBox()
	{
		DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(this.getConfig().mapid, clanfightid);
		if (ds == null)
		{
			return;
		}		
		
		for (Long e : clanfightbean.getVictoryboxids())
		{
			SceneNpcManager.removeNpc(e);	
		}
		
		clanfightbean.getVictoryboxids().clear();
	}
	
	/***
	 * 销毁庆贺宝箱 by changhao
	 */
	public void destroyCelebrateBox()
	{
		int whichclanwin = getClanfightBean().getWhichwin();
		if (whichclanwin != -1) //获胜方才刷庆贺宝箱 by changhao
		{
			long winclanid = 0;
			if (whichclanwin == 0)
			{
				winclanid = getClanfightBean().getClanid1();
			}
			else
			{
				winclanid = getClanfightBean().getClanid2();
			}
			
			DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(ClanUtils.MAPID, winclanid);
			if (ds == null)
			{
				return;
			}
			
			for (Long e : clanfightbean.getCelebrateboxids())
			{
				SceneNpcManager.removeNpc(e);	
			}
			
			clanfightbean.getCelebrateboxids().clear();
		}
	}
	
	/***
	 * 检查战场是否需要结束 by changhao
	 */
	public void checkClanFightOver()
	{	
		if (getClanfightBean().getState() == xbean.ClanFightBattleField.STATE_CLAN_FIGHT_OVER)
		{
			return;
		}
		
		int whichwin = checkWhichWinByPeopleNum();
		if (whichwin != -1)
		{
			long clanid = 0;
			if (whichwin == 0)
			{
				clanid = getClanfightBean().getClanid1();
			}
			else
			{
				clanid = getClanfightBean().getClanid2();
			}

			mkdb.Procedure.pexecuteWhileCommit(new fire.pb.clan.fight.PClanFightOver(clanfightid, whichwin, clanfightbean.getDatetime(), clanid));
			return;
		}
	}
	
	/***
	 * 检查战场那边赢通过人数判断  by changhao
	 */
	public int checkWhichWinByPeopleNum()
	{
		if (clanfightbean != null)
		{
			long cur = System.currentTimeMillis();
			
			if (cur < getClanfightBean().getActivitypreparetime()) //至少过了准备时间 by changhao
			{
				return -1;
			}
			
			int count1 = 0;
			for (Long r : clanfightbean.getClan1roleids().keySet())
			{
				if (StateCommon.isOnlineBuffer(r))
				{
					count1 ++;
					break;
				}
			}
			
			if (count1 == 0)
			{
				return 1;
			}
			
			int count2 = 0;
			for (Long r : clanfightbean.getClan2roleids().keySet())
			{
				if (StateCommon.isOnlineBuffer(r))
				{
					count2 ++;
					break;
				}
			}
			
			if (count2 == 0)
			{
				return 0;
			}
		}
		
		return -1;
	}
	
	/***
	 * 发送某个消息给战场中的每个人 by changhao
	 */
	public void sendMsgToBattleRoleids(mkio.Protocol msg)
	{
		for (Long e : getClanfightBean().getClan1roleids().keySet())
		{
			if (StateCommon.isOnlineBuffer(e))
			{
				mkdb.Procedure.psendWhileCommit(e, msg);					
			}
		}
		
		for (Long e : getClanfightBean().getClan2roleids().keySet())
		{
			if (StateCommon.isOnlineBuffer(e))
			{
				mkdb.Procedure.psendWhileCommit(e, msg);					
			}
		}
	}
	
	/***
	 * 清理进入战场的每个人的统计数据 by changhao
	 */
	public void clearStatistics()
	{
		for (Long e : getClanfightBean().getEnterroleids().keySet())
		{
			if (e != null)
			{
				xtable.Roleid2clanfightstatistics.remove(e);		
			}
		}
	}
	
	/***
	 * 打开奖励 by changhao
	 * @param roleId
	 * @param npckey
	 * @param boxid
	 * @return
	 */
	public boolean openChest(final long roleId, final long npckey, int boxid)
	{
		Npc npc = SceneNpcManager.getNpcByKey(npckey);
		if (npc == null)
		{
			MessageMgr.psendMsgNotify(roleId, 166111, null);
			return false;
		}
		
		SBaoxiang chestcfg = ChestManager.getInstance().getChestConfig(npc.getNpcID());
		if (chestcfg == null)
		{
			return false;
		}
	
		xbean.EClanFightStatistics stat = xtable.Roleid2clanfightstatistics.get(roleId);
		if (stat == null)
		{
			return false;
		}
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("MonsterLv", 1);
		para.put(RewardMgr.TEAM_NUM, 1);
		
		if (boxid == ActivityClanFightManager.CELEBRATE_BOX)
		{
			if (stat.getClancelebrateitemnum() >= 1)
			{
				MessageMgr.psendMsgNotify(roleId, 410058, null);
				return false;
			}
			
			// 给予奖励 by changhao
			RewardMgr.getInstance().distributeAllAward(roleId, chestcfg.rewardid, para, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_clanfight_reward, chestcfg.rewardid, PAddExpProc.CLAN_FIGHT, "");
			
			stat.setClancelebrateitemnum(stat.getClancelebrateitemnum() + 1);

		}
		else if (boxid == ActivityClanFightManager.VICTORY_BOX)
		{
			if (stat.getWinneritemnum() >= 1)
			{
				MessageMgr.psendMsgNotify(roleId, 410057, null);
				return false;
			}
			
			// 给予奖励 by changhao
			RewardMgr.getInstance().distributeAllAward(roleId, chestcfg.rewardid, para, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_clanfight_reward, chestcfg.rewardid, PAddExpProc.CLAN_FIGHT, "");
			
			stat.setWinneritemnum(stat.getWinneritemnum() + 1);
		}
		
		SceneNpcManager.removeNpc(npckey);
		
		return true;
	}
	
	public boolean IsWinnerSide(long roleid, boolean msg)
	{
		if (getClanfightBean().getWhichwin() == 0)
		{
			Integer v = getClanfightBean().getClan1enterroleids().get(roleid);
			if (v == null)
			{
				if (msg == true)
				{
					MessageMgr.psendMsgNotify(roleid, 410050, null);
				}
				
				return false;
			}
		}
		else if (getClanfightBean().getWhichwin() == 1)
		{
			Integer v = getClanfightBean().getClan2enterroleids().get(roleid);
			if (v == null)
			{
				if (msg == true)
				{
					MessageMgr.psendMsgNotify(roleid, 410050, null);					
				}
				
				return false;
			}					
		}
		else
		{
			return false;
		}	
		
		return true;
	}
	
	/***
	 * 得到当前还在战场中的人某一边 by changhao
	 * @param which
	 * @return
	 */
	public java.util.Map<Long, Integer> getClanroleidsByWhich(int which)
	{
		if (which == 0)
		{
			return this.getClanfightBean().getClan1roleids();
		}
		else if (which == 1)
		{
			return this.getClanfightBean().getClan2roleids();			
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 增加活力 by changhao
	 */
	void addActivityValue(long roleId)
	{				
		mkdb.Procedure proc = new mkdb.Procedure()
		{
			@Override
			protected boolean process() throws Exception
			{
				fire.pb.mission.activelist.RoleLiveness actrole = fire.pb.mission.activelist.RoleLiveness.getRoleLiveness(roleId, false);
				if (actrole != null)
				{			
					fire.pb.activity.ActivityConfNew activity = RoleLiveness.getConfigActivity(fire.pb.mission.activelist.RoleLiveness.CLANFIGHT_START);
					if (activity != null)
					{
						for (int i = 0; i < activity.getMaxact(); i ++)
						{
						    actrole.handleActiveLivenessData(RoleLiveness.CLANFIGHT_START);					
						}
					}			
				}	
				
				return true;
			}
		};
		
		Procedure.pexecuteWhileCommit(proc);
	}
	
	/**
	 * 随机跳转个位置 by changhao
	 */
	void randomGotoPos(long roleId)
	{				
		mkdb.Procedure proc = new mkdb.Procedure()
		{
			@Override
			protected boolean process() throws Exception
			{
				long cur = System.currentTimeMillis();
				fire.pb.clan.SClanFight config = ConfigManager.getInstance().getConf(fire.pb.clan.SClanFight.class).get(200);
				if (config != null)
				{
					boolean ok = false;
					DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(config.mapid, clanfightid);
					if (ds != null)
					{
						GridPos gpos = MapUtil.getRandomArrivablePos(ds.getSceneID());
						if (gpos != null)
						{
							ExecuteReqGoto.trans2Pos(roleId, config.mapid, gpos.getX(), gpos.getY(), false);
							ok = true;
						}							
					}
					
					if (ok == false)
					{
						long r = cur % 2;
						if (r == 0)
							ExecuteReqGoto.trans2Pos(roleId, config.mapid, config.x1, config.y1, false);
						else
							ExecuteReqGoto.trans2Pos(roleId, config.mapid, config.x2, config.y2, false);								
					}
				}					
				return true;
			}
		};
		
		Procedure.pexecuteWhileCommit(proc);
	}
}
