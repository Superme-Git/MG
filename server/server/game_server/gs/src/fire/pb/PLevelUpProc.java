package fire.pb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.locojoy.base.Runnable;

import mkdb.Procedure;
import xbean.BasicFightProperties;
import fire.log.LogManager;
import fire.pb.activity.ActivityConfNew;
import fire.pb.activity.impexam.ImpExamManager;
import fire.pb.activity.winner.WinnerManager;
import fire.pb.battle.pvp1.PvP1Control;
import fire.pb.battle.pvp1.PvP1Helper;
import fire.pb.battle.pvp3.PvP3Control;
import fire.pb.battle.pvp3.PvP3Helper;
import fire.pb.battle.pvp5.PvP5Control;
import fire.pb.battle.pvp5.PvP5Helper;
import fire.pb.buff.BuffConstant;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.PAutoAddCircleTask;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.master.MasterManager;
import fire.pb.mission.MissionColumn;
import fire.pb.mission.MissionStatus;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.notify.GiftBagMgr;
import fire.pb.mysql.C3P0Util;
import fire.pb.npc.SAttendImpExam;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.RankListManager;
import fire.pb.role.SServerMaxLevelConfig;
import fire.pb.state.StateManager;
import fire.pb.talk.MessageMgr;
import fire.pb.triggers.TriggerRole;
import fire.pb.triggers.event.TriggerEvent;
import fire.pb.util.FireProp;

public class PLevelUpProc extends mkdb.Procedure {
	
	private final long roleId;
	
	public PLevelUpProc(int userid,long roleid){
		this.roleId = roleid;
	}
	@Override
	public boolean process(){
		
		Integer curlevel = xtable.Properties.selectLevel(roleId);
		if(curlevel == null)
			return false;
		int levellimit = getMaxLevel();
		
		if(curlevel >= levellimit)
		{//level limit
			MessageMgr.psendMsgNotify(roleId, 142320, MessageMgr.getStringList(levellimit));
			return false;
		}
		
		final fire.pb.effect.Role role = new fire.pb.effect.RoleImpl(roleId);

		if(fire.pb.buff.Module.existState(roleId, BuffConstant.StateType.STATE_BATTLE_FIGHTER))
			return false;
		
		java.util.Map<Integer, Float> res = role.levelUp();
		if (null == res)
			return false;
		final fire.pb.attr.SRefreshRoleData send = new fire.pb.attr.SRefreshRoleData();
		send.datas.putAll(res);

		//gnet.link.Onlines.getInstance().send(roleId,send);
		mkdb.Procedure.psendWhileCommit(roleId, send);
		
		//刷新人物加点信息
		final fire.pb.attr.SRefreshPointType refresh = new fire.pb.attr.SRefreshPointType();

		xbean.Properties prop = xtable.Properties.get(roleId);
		BasicFightProperties bfp = prop.getBfp();
		
		refresh.bfp.agi = (short)bfp.getAgi();
		refresh.bfp.cons = (short)bfp.getCons();
		refresh.bfp.endu = (short)bfp.getEndu();
		refresh.bfp.iq = (short)bfp.getIq();
		refresh.bfp.str = (short)bfp.getStr();			
		
		refresh.bfp.agi_save.putAll(prop.getAddpointfp().getAgi_save());
		refresh.bfp.cons_save.putAll(prop.getAddpointfp().getCons_save());
		refresh.bfp.endu_save.putAll(prop.getAddpointfp().getEndu_save());
		refresh.bfp.iq_save.putAll(prop.getAddpointfp().getIq_save());
		refresh.bfp.str_save.putAll(prop.getAddpointfp().getStr_save());
		
		refresh.point.putAll(prop.getPoint());
		refresh.pointscheme = prop.getScheme();
		refresh.schemechanges = prop.getSchemechanges();
		
		Procedure.psendWhileCommit(roleId, refresh);
		
		//看是否触发隐藏事件
		TriggerRole trole = new TriggerRole(roleId, false);
		trole.triggersByType(TriggerEvent.TRIGGER_TYPE_UPLEVEL, null);
		
		fire.pb.activity.ActivityConfNew activity = 
				RoleLiveness.getConfigActivity(fire.pb.mission.activelist.RoleLiveness.IMPEXAMVILL);
		if (role.getLevel() == activity.level) {
			int kejuType = ImpExamManager.getInstance().isInImpExamTime();
			if (kejuType != -1) {
				SAttendImpExam sak = new SAttendImpExam();
				sak.impexamtype = kejuType;
				mkdb.Procedure.psendWhileCommit(roleId, sak);
			}
		}
		
		//通知队友
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.team.PRoleLevelUp(roleId,role.getLevel()));
		
		//人物属性排行榜
		final int level = role.getLevel();
		if(level >= 30){
			mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure(){
				@Override
				public boolean process(){
					xbean.RoleLevelListRecord record = xbean.Pod.newRoleLevelListRecord();
					record.setTime(System.currentTimeMillis());
					
					xbean.MarshalRoleLevelRecord marshRecord = record.getMarshaldata();
					marshRecord.setLevel(level);
					marshRecord.setRoleid(roleId);
					PropRole pRole = new PropRole(roleId, true);
					marshRecord.setRolename(pRole.getName());
					marshRecord.setSchool(pRole.getSchool());
					
					xbean.RoleLevelRankList list = xtable.Rolelevellist.get(1);
					if(null == list){
						list = xbean.Pod.newRoleLevelRankList();
						xtable.Rolelevellist.insert(1, list);
					}
					
					RankListManager.getInstance().tryInsertRecord(RankType.LEVEL_RANK, list.getRecords(), record);
					
					return true;
				}
			});
		}
		

		ActivityConfNew actiConf = RoleLiveness.getConfigActivity(fire.pb.activity.ActivityType.ActivityType_CircTaskSchool);
		if(null != actiConf) {
			int CTSchoolNeedLevel = actiConf.level;
			if(level == CTSchoolNeedLevel) {
				new PAutoAddCircleTask(roleId, 1010000, 0, true).call();
			}
		}
		
		
		RoleAnYeTask rayt = new RoleAnYeTask(roleId);
		rayt.generateTasks();
		if(rayt.getTasks().size() >= RoleAnYeTask.TASK_SIZE) {
			rayt.refreshAnYeTasks(-1);
		}
		
		
		java.util.List<Integer> circletasks = CircleTaskManager.getInstance().getCircleTypeByLevel(level);
		for(Integer curtype : circletasks) {
			new PAutoAddCircleTask(roleId, curtype, 0, true).call();
		}

		if (level == PvP1Helper.ENTER_MIN_LEVEL) {
			if (PvP1Control.getInstance().isActivityTime()) {
				fire.pb.map.Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
				if (mapRole == null) {
					return false;
				}
				int grade = PvP1Helper.getRaceGradeByRoleId(roleId);
				if (grade != -1) {
					if (PvP1Helper.isPvPMap(mapRole.getMapId()) == false) {
						PvP1Helper.summonByRole(roleId, mapRole.getMapId());
					}
				}
			}
		}

		if (level == PvP3Helper.ENTER_MIN_LEVEL) {
			if (PvP3Control.getInstance().isActivityTime()) {
				fire.pb.map.Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
				if(mapRole == null) {
					return false;
				}
				int grade = PvP3Helper.getRaceGradeByRoleId(roleId);
				if (grade != -1) {
					if (PvP3Helper.isPvPMap(mapRole.getMapId()) == false) {
						PvP3Helper.summonByRole(roleId, mapRole.getMapId());
					}
				}
			}
		}

		if (level == PvP5Helper.ENTER_MIN_LEVEL) {
			if (PvP5Control.getInstance().isActivityTime()) {
				fire.pb.map.Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
				if(mapRole == null) {
					return false;
				}
				int grade = PvP5Helper.getRaceGradeByRoleId(roleId);
				if (grade != -1) {
					if (PvP5Helper.isPvPMap(mapRole.getMapId()) == false) {
						PvP5Helper.summonByRole(roleId, mapRole.getMapId());
					}
				}
			}
		}
		
		//这里处理一下冠军试炼的上线拉人
		if (level == WinnerManager.MIN_LEVEL) {
			WinnerManager.getInstance().sendWinnerCallPlayer(roleId);
		}
		
		if (level == getLvTrigHuoban(6).getLvtrig()) {
			new fire.pb.huoban.PInitHuobanInfo(roleId).call();
		}
		
		if(level == 35){
			int taskstate = new MissionColumn(roleId, true).getMissionState(102019);
			if(taskstate != MissionStatus.COMMITED){
			}
		}
		
		if(level == 60){
			MasterManager.getInstance().updateStlx(roleId, MasterManager.LEVEL_60_KEY, 1);
			MasterManager.getInstance().updateStlx(roleId, MasterManager.TASKMAJOR_60_KEY, 1);
		}
		
		try {
	
		} catch (Exception e) {
			LogManager.logger.error("do levelup log failed");
			e.printStackTrace();
		}
		
		if(ConfigManager.getUseMysql() && ((level <= 60 && level % 3 == 0) || level > 60)){
			final xbean.Properties pro = xtable.Properties.get(roleId);
			if (null == pro)
				return true;
			Gs.getExecService().execute(new Runnable() {
				private boolean InsertMysqlRelation(long roleId, String rolename, int shapeid, int level) {
					boolean updateRet = false;
					Connection conn = C3P0Util.getConnection();
					Statement stmt = null;
					try {
						stmt = conn.createStatement();
						//采取没有就插入插入数据，有就更新数据的策略
						String sqlstr = "INSERT INTO `role`(roleid, name, avatar, level) "
								+ "VALUES ('" + roleId + "', '" + rolename + "', '" + shapeid + "', '" + level + "') "
								+ "ON DUPLICATE KEY UPDATE " + "name='" + rolename + "', avatar='" + shapeid + "', level='" + level + "'";
						int ret = stmt.executeUpdate(sqlstr);
						fire.pb.friends.Module.logger.info("PLevelUpProc.[" + roleId + "]InsertMysqlRealtion:" + sqlstr + ";ret = " + ret);
						if(ret >= 0) {
							updateRet = true;
							if(ret == 1) {
								fire.pb.friends.Module.logger.warn("PLevelUpProc.角色[" + roleId + "]空间mysql数据没有,插入最新数据！");
							} else if(ret == 2) {
								fire.pb.friends.Module.logger.info("PLevelUpProc.角色[" + roleId + "]空间mysql数据ret=2.");
							} else if(ret == 3) {
								fire.pb.friends.Module.logger.info("PLevelUpProc.角色[" + roleId + "]空间mysql数据更新!");
							} else {
								fire.pb.friends.Module.logger.warn("PLevelUpProc.角色[" + roleId + "]空间mysql数据预料之外的ret=" + ret);
							}
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
						if (stmt != null) {
							try {
								stmt.close();
							} catch (SQLException sqlEx) {
							}
							stmt = null;
						}
						C3P0Util.close(conn, null, null);
						return false;
					} finally {
						if (stmt != null) {
							try {
								stmt.close();
							} catch (SQLException sqlEx) {
							}
							stmt = null;
						}
					}
					C3P0Util.close(conn, null, null);
					return updateRet;
				}
				
				@Override
				public void run() {
					if(!InsertMysqlRelation(roleId, pro.getRolename(), pro.getShape(), pro.getLevel())){
						StateManager.logger.error("[" + roleId + "]PLevelUpProc.InsertMysqlRelation failed!");
					}
				}
			});
		}
		
		return true;
	}
	

	
	public static int getMaxLevel(){
		int levellimit = DataInit.ROLE_LEVEL_MAX;
		SServerMaxLevelConfig levelcfg = ConfigManager.getInstance().getConf(fire.pb.role.SServerMaxLevelConfig.class).get(ConfigManager.getGsZoneId());
		if(levelcfg != null)
			levellimit = levelcfg.maxlevel;
		else
		{
			SServerMaxLevelConfig defaultlevelcfg = ConfigManager.getInstance().getConf(fire.pb.role.SServerMaxLevelConfig.class).get(1);
			if(defaultlevelcfg != null)
				levellimit = defaultlevelcfg.maxlevel;
			else
				levellimit = FireProp.getIntValue("sys", "sys.levelup.limit");
		}
		return levellimit;
	}
	
	public fire.pb.mission.SNewFunctionOpen getLvTrigHuoban(int nfoid){
		return fire.pb.main.ConfigManager.getInstance().getConf(
				fire.pb.mission.SNewFunctionOpen.class).get(nfoid);
	}
	
}
