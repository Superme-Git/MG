package fire.pb.role;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mkdb.util.UniqName;
import net.sf.json.JSONObject;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.locojoy.base.Runnable;

import xbean.BasicFightProperties;
import fire.log.YYLogger;
import fire.pb.CCreateRole;
import fire.pb.DataInit;
import fire.pb.GameSystemConfig;
import fire.pb.PInitRoleProp;
import fire.pb.PropConf;
import fire.pb.RoleConfigManager;
import fire.pb.SCreateRole;
import fire.pb.SCreateRoleError;
import fire.pb.SysConfigType;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.common.SCommon;
import fire.pb.fushi.SCommonDayPay;
import fire.pb.fushi.YybGetCurrencyHandler;
import fire.pb.game.MoneyType;
import fire.pb.hook.PInitHookDataProc;
import fire.pb.http.HttpCallBackHandler;
import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemMaps;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.mission.SLineTask;
import fire.pb.mysql.C3P0Util;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.skill.SkillRole;
import fire.pb.state.StateManager;
import fire.pb.statistics.StatisticUtil;
import fire.pb.talk.MessageMgr;
import fire.pb.util.FireProp;

public class PCreateRole extends mkdb.Procedure {
	static private final Logger logger = Logger.getLogger("SYSTEM");
	
	public static final String address = FireProp.getStringValue("sys", "sys.zhaomu.address");
	//http://mt3.pengyouquan001.locojoy.com:8830/enlist/submit_code?code=7956261002&new_serverid=1101011002&new_roleid=795626
	public static final String func = "/enlist/submit_code";
	
	// 每个用户最多可显示的角色数目
	public static int maxRoleNum = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.maxRoleNum");
   // 每个用户最多可创建的角色数目
	public static int maxCreateRoleNum=1;
	
	// 新生成的角色ID long类型
	private long newRoleID;
	
	// 创建角色时对应的客户端发送的协议
	private final mkio.Protocol thisProtocol;
	
	// 创建角色需要的基本数据
	private final int userID;
	private final String name;
	private final int school;
	private final int shape;
	private final java.util.ArrayList<Integer> initequips;
	
	public PCreateRole(CCreateRole protocol, int userID, java.util.ArrayList<Integer> initequips) {
		this.userID = userID;
		this.name = protocol.name;
		this.school = protocol.school;
		this.shape = protocol.shape;
		this.thisProtocol = protocol;
		this.initequips = initequips;
	}

	public long getNewRoleID() {
		return newRoleID;
	}

	private boolean  sendError(int err){
		final SCreateRoleError res=new SCreateRoleError();
		res.err=err;
		return gnet.link.Onlines.getInstance().sendResponse(thisProtocol, res);
	}
	
	private static void initBasicFightProperties(xbean.BasicFightProperties b,fire.pb.role.RoleModData m){
		b.setAgi((short) m.agiinit);
		b.setCons((short) m.consinit);
		b.setEndu((short) m.enduinit);
		b.setIq((short) m.iqinit);
		b.setStr((short) m.strinit);
	}
	
	private boolean InsertMysqlRelation(long roleId, String rolename, int shapeid, int level) {
		boolean updateRet = false;
		Connection conn = C3P0Util.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			//创建角色的时候采取没有就插入数据，有就更新数据的策略
			String sqlstr = "INSERT INTO `role`(roleid, name, avatar, level) "
					+ "VALUES ('" + roleId + "', '" + rolename + "', '" + shapeid + "', '" + level + "') " +
					"ON DUPLICATE KEY UPDATE name='" + rolename + "', avatar=" + shapeid + ", level=" + level;
			int ret = stmt.executeUpdate(sqlstr);
			fire.pb.friends.Module.logger.info("[" + roleId + "]InsertMysqlRealtion:" + sqlstr + ";ret = " + ret);
			if(ret > 0) {
				updateRet = true;
				if(ret > 1) {
					fire.pb.friends.Module.logger.warn("角色[" + roleId + "]空间mysql数据id已占用,强制更新为新id数据！");
					//既然强制更新数据了，好友关系也重新同步一遍
					Gs.getExecService().execute(new Runnable() {
						@Override
						public void run() {
							Connection conn = C3P0Util.getConnection();
							Statement stmt = null;
							Statement stmt2 = null;
							try {
								stmt = conn.createStatement();
								stmt2 = conn.createStatement();
								String delsqlstr = "DELETE FROM `role_relation` WHERE roleid=" + roleId;
								int ret2 = stmt2.executeUpdate(delsqlstr);
								fire.pb.friends.Module.logger.info("InsertMysqlRelation:[" + roleId + "]" + delsqlstr + ";ret2 = " + ret2);
								if(ret2 < 0) {
									fire.pb.friends.Module.logger.error("InsertMysqlRelation:[" + roleId + "]删除mysql好友关系错误！");
									if (stmt != null) {
										try {
											stmt.close();
										} catch (SQLException sqlEx) {
										}
										stmt = null;
									}
									if (stmt2 != null) {
										try {
											stmt2.close();
										} catch (SQLException sqlEx) {
										}
										stmt2 = null;
									}
									C3P0Util.close(conn, null, null);
									return;
								}	
								xbean.FriendGroups groups = xtable.Friends.select(roleId);
								if (groups == null) {
									fire.pb.friends.Module.logger.info("InsertMysqlRelation:[" + roleId + "]xdb没有好友数据,不需要同步到mysql！");
									if (stmt != null) {
										try {
											stmt.close();
										} catch (SQLException sqlEx) {
										}
										stmt = null;
									}
									if (stmt2 != null) {
										try {
											stmt2.close();
										} catch (SQLException sqlEx) {
										}
										stmt2 = null;
									}
									C3P0Util.close(conn, null, null);
									return;
								}
								fire.pb.friends.Module.logger.info("InsertMysqlRelation:准备同步[" + roleId + "]好友关系到mysql！好友数:" + groups.getFriendmap().size());
								for(Long friendid : groups.getFriendmap().keySet()) {
									//根据好友系统的设计，这里最多情况可能会有150次Insert
									String sqlstr = "INSERT INTO `role_relation`(roleid, friendid, relation) "
											+ "VALUES ('" + roleId + "', '" + friendid + "', '" + 1 + "')";
									stmt.addBatch(sqlstr);
								}	
								int ret[] = stmt.executeBatch();
								fire.pb.friends.Module.logger.info("InsertMysqlRelation.length=" + ret.length + ",friends.size=" + groups.getFriendmap().size());
								if(ret.length == groups.getFriendmap().size()) {
									fire.pb.friends.Module.logger.info("InsertMysqlRelation:同步[" + roleId + "]好友关系到mysql完成！同步好友数:" + ret.length);
								} else {
									fire.pb.friends.Module.logger.warn("InsertMysqlRelation:同步[" + roleId + "]好友关系到mysql完！但同步不完整！");
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
								if (stmt2 != null) {
									try {
										stmt2.close();
									} catch (SQLException sqlEx) {
									}
									stmt2 = null;
								}
								C3P0Util.close(conn, null, null);
								return;
							} finally {
								if (stmt2 != null) {
									try {
										stmt2.close();
									} catch (SQLException sqlEx) {
									}
									stmt2 = null;
								}
								if (stmt != null) {
									try {
										stmt.close();
									} catch (SQLException sqlEx) {
									}
									stmt = null;
								}
							}
							C3P0Util.close(conn, null, null);	
							return;
						}
					});
				}
			}
		} catch (SQLException ex) {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				}
				stmt = null;
			}
			C3P0Util.close(conn, null, null);
			ex.printStackTrace();
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
	public boolean process() {

		final SCreateRole snd = new SCreateRole();

	
		// 检查角色名是否已经用过
		String lowerCaseName = name.toLowerCase();
		if (!UniqName.allocate("role", lowerCaseName)) {
			// 告诉客户端说角色名已重复
//			if (name.length()==8) 
			sendError(SCreateRoleError.CREATE_DUPLICATED);
//			else {
//			  List<StringBuilder> result = NameManage.addSymbols(new StringBuilder(name),4);
//			  if (!result.isEmpty()) {
//			  SRecommendsNames sRecommendsNames = new SRecommendsNames();
//			  for (StringBuilder sb : result) {
//				sRecommendsNames.recommendnames.add(MessageMgr.convertString2Octets(sb.toString()));
//			  }
//			  gnet.link.Onlines.getInstance().sendResponse(thisProtocol, sRecommendsNames);
//			  }else {
//				  sendError(SCreateRoleError.CREATE_DUPLICATED);
//			 }
//			}
			return false;
		}
		
		xbean.User u = xtable.User.get(userID);
		//再次验证创建的数量上限
		if(null != u){
			int roleNum = 0;
			for (Long rid : u.getIdlist()) {
				xbean.Properties prop = xtable.Properties.select(rid);
				if (prop.getDeletetime()==0)
					roleNum++;
			}
			if(roleNum >= PCreateRole.maxCreateRoleNum){
				sendError(SCreateRoleError.CREATE_OVERCOUNT);
				return false;
			}
		}
		final long now = java.util.Calendar.getInstance().getTimeInMillis();
		final fire.pb.role.RoleConfig config=RoleConfigManager.getRoleConfigBySchoolID(school);
		final fire.pb.role.SCreateRoleConfig createConfig = RoleConfigManager.getCreateRoleConfig(shape);
		// 原来的种族概念已经去掉了，所有的角色的默认"种族"为1，读种族修正表里的1行初始值
		final fire.pb.role.RoleModData moddata=RoleConfigManager.getRoleModData(1);
		
		final xbean.Properties pro = xbean.Pod.newProperties();
		
		pro.setCreatetime(now);
		//TASK #2342::【优化】角色上次登录时间和角色上次下线时间 在创建角色的时候 置为角色创建时间
		pro.setOnlinetime(now);
		pro.setOfflinetime(now);
		pro.setRolename(name);
		pro.setSchool(school);
		pro.setShape(shape);
		pro.setRace(1);
		//pro.setSchoolvalue(0);
		pro.setSex(createConfig.sex);
		pro.setLevel(config.level);
		pro.setExp(0);
		pro.setTitle(-1);
		pro.setSceneid(config.mapid);
		if(pro.getSysconfigmap().size() == 0){
			GameSystemConfig.resetSysConfig(pro);
		}
		pro.getSysconfigmap().put(SysConfigType.MaxScreenShowNum, FireProp.getIntValue("sys", "sys.maxScreenShowNum"));
		
		if (pro.getLineconfigmap().size() == 0) {
			Map<Integer, SLineTask> instanceTask = fire.pb.main.ConfigManager.getInstance().getConf(SLineTask.class);
			if (instanceTask != null) {
				for (SLineTask linetask : instanceTask.values()) {
					pro.getLineconfigmap().put(linetask.minlevel, 0);
				}
			}
		}
		
		if (pro.getAddpointfp().getAgi_save().size() == 0 || pro.getAddpointfp().getCons_save().size() == 0
				|| pro.getAddpointfp().getEndu_save().size() == 0 || pro.getAddpointfp().getIq_save().size() == 0
				|| pro.getAddpointfp().getStr_save().size() == 0)
		{
			GameSystemConfig.resetRolePropAddPointFp(pro);
		}
		
		if (pro.getPoint().size() == 0) {
			GameSystemConfig.resetRolePropPoint(pro);
		}		
		
		int posx = fire.pb.util.Misc.getRandomBetween(config.mapx, config.mapx + config.regionx);
		int posy = fire.pb.util.Misc.getRandomBetween(config.mapy, config.mapy + config.regiony);
		pro.setPosx(posx);
		pro.setPosy(posy);
		// 计算人物属性
		pro.setSp(PropConf.Battle.PROP_SP_INITIAL);
		pro.setUserid(userID);
		
		BasicFightProperties bfp = pro.getBfp();
		initBasicFightProperties(bfp, moddata);
		
		// 出生时的初始等级设置为1时，自动加点,防止策划配置的初始等级改变后不为1级，与自动升级加点不符。
		ArrayList<Integer> points = null;
		switch (config.defaultscheme)
		{
		case 1:
			points = config.addpoint;
			break;
		case 2:
			points = config.addpoint2;
			break;
		case 3:
			points = config.addpoint3;
			break;
		default:
			points = config.addpoint;
		}
		
		if (pro.getLevel() == 1){
			bfp.setAgi((short) (bfp.getAgi() + 1 + points.get(4)));
			bfp.setCons((short) (bfp.getCons() + 1 + points.get(0)));
			bfp.setEndu((short) (bfp.getEndu() + 1 + points.get(3)));
			bfp.setIq((short) (bfp.getIq() + 1 + points.get(1)));
			bfp.setStr((short) (bfp.getStr() + 1 + points.get(2)));
		}
		else {
			for (int i=1; i<4; i++) {
				pro.getPoint().put(i, DataInit.ROLE_UP_POINT);
			}
		}
		
		List<Integer> aiids =  fire.pb.battle.ai.BattleAIManager.getInstance().getRoleDefaultFighteAIIDs(school);
		for(int aiid:aiids)
		{
			
			xbean.BattleAI newai = xbean.Pod.newBattleAI();
			newai.setId(aiid);
			pro.getFighteai().add(newai);
		}
		
		final long newRoleID = xtable.Properties.insert(pro);

		if (null == u) {
			u = xbean.Pod.newUser();
			u.setCreatetime(now);
			xtable.User.insert(userID, u);
			StatisticUtil.setUserFirsttimeIntoWorld(userID, now);
		}

		u.setPrevloginroleid(newRoleID);
		u.getIdlist().add(newRoleID);
		xbean.AUUserInfo auUserInfo = xtable.Auuserinfo.select(userID);
		if (auUserInfo!=null){
			pro.setPlatformuid(auUserInfo.getUsername());
		}else {
			mkdb.Trace.error("auuserinfo doesn't exist when create role.userid:"+userID);
		}
//		new PInitRoleProp(newRoleID).call();
		
		snd.newinfo.roleid = newRoleID;
		snd.newinfo.rolename = name;
		snd.newinfo.school = school;
		snd.newinfo.shape = shape;
		snd.newinfo.level = pro.getLevel();
		snd.newinfo.rolecreatetime=now;
		if(ConfigManager.getUseMysql()){
			//为个人空间好友圈同步信息
			Gs.getExecService().execute(new Runnable() {
				@Override
				public void run() {
					if(!InsertMysqlRelation(snd.newinfo.roleid, snd.newinfo.rolename, snd.newinfo.shape, snd.newinfo.level)){
						logger.error("PCreateRole.InsertMysqlRelation failed!");
					}
				}
			});	
//			if(!InsertMysqlRelation(snd.newinfo.roleid, snd.newinfo.rolename, snd.newinfo.shape, snd.newinfo.level)){ //如果mysql插入失败，直接回滚，角色创建失败
//				logger.error("PCreateRole.InsertMysqlRelation failed!");
//				return false;
//			}
		}
		
		if(xtable.Rolename2key.select(name) != null)
		{
			sendError(SCreateRoleError.CREATE_DUPLICATED);
			return false;
		}
		
		xbean.RoleSpace rs = xtable.Rolespaces.get(newRoleID);
		if(rs != null)
			return false;
		else {
			xbean.RoleSpace newrs = xbean.Pod.newRoleSpace();
			newrs.setGift(0);
			newrs.setPopularity(0);
			newrs.setRecvgift(0);
			newrs.setGetgiftnum(0);
			newrs.setGetgifttime(0);
			xtable.Rolespaces.insert(newRoleID, newrs);
		}
		
		//将人物信息加入到人物反转表中。
		xtable.Rolename2key.insert(name, newRoleID);
		
		// 运营日志
//		YYLogger.rolNameLog(newRoleID, name);
		
		// 添加装备
		if (initequips != null) {
			final ItemMaps equip = fire.pb.item.Module.getInstance().getItemMaps(newRoleID, 
						BagTypes.EQUIP, false);
			for (int i = 0; i < initequips.size(); i++) {
				fire.pb.item.EquipItem ei = (fire.pb.item.EquipItem)fire.pb.item.Module.getInstance().getItemManager().genItemBase(initequips.get(i), 1);
				if (ei == null)
					continue;
				equip.doAddItem( ei, -1, "物品赠送", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xitongzengsong, 0);
			}
		}
		// fire.pb.event.Poster.getPoster().dispatchEvent( new fire.pb.event.CreateRoleEvent( newRoleID ) );
		// 添加赠送物品
		if ( config.presents != null ) {
			final ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(newRoleID, 
						BagTypes.BAG, false);
			
			if (config.presents.size() == config.nums.size()) {
				for (int i = 0; i < config.presents.size(); i++) {
					bag.doAddItem(config.presents.get(i), config.nums.get(i), "物品赠送", 
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xitongzengsong, 0);
				}
			} 
			else {
				fire.pb.item.Module.getInstance().getLogger().error( 
						"人物职业配置表id="+school+"出错,赠送物品和数量不匹配" );
			}
		}
		
		//加装备Buff
		fire.pb.item.Equip equip = new fire.pb.item.Equip(newRoleID, false);
		fire.pb.skill.SceneSkillRole nrole = 
			fire.pb.skill.SkillManager.getSceneSkillRole(newRoleID);
		for (fire.pb.item.ItemBase item : equip) {
			if (item instanceof EquipItem) {
				if (((EquipItem) item).getExtInfo().getEndure() > 0) {
					Map<Integer, Float> effects = new HashMap<Integer, Float>();
					List<ConstantlyBuffConfig> buffs = new LinkedList<ConstantlyBuffConfig>();
					((EquipItem) item).getEffectsAndBuffs(effects, buffs);

					xbean.Equip equipAttr = ((EquipItem) item).getExtInfo();
					List<Integer> skills = new ArrayList<Integer>();
					if (equipAttr.getSkill() > 0) {
						skills.add(equipAttr.getSkill());
					}
					if (equipAttr.getEffect() > 0) {
						skills.add(equipAttr.getEffect());
					}
					nrole.equip(((EquipItem) item).getEquipType(), effects,
							skills);
				}
			}
		}
		
		new PInitRoleProp(newRoleID).call();
		//添加角色助战
		fire.pb.huoban.HuoBanFactory.getFactory().initRoleHuoban(newRoleID);
		
		new PInitHookDataProc(newRoleID).call();
		
		logger.info("创建角色 :\t" + name + "\t" + shape + "\troleID:" + newRoleID
				+ "\tuserID:" + userID);
		StatisticUtil.setRoleCreateTime(newRoleID, now);
		fire.pb.event.Poster.getPoster().dispatchEvent( 
				new fire.pb.event.CreateRoleEvent(newRoleID,school) );
		//Title.addTitle( newRoleID, 48, "", -1 );
		gnet.link.Onlines.getInstance().sendResponse(thisProtocol, snd);//最后再发送创建成功的消息，以免失败后无法回滚
		//统计服务器创建的角色数,充值功能会用到
		pexecuteWhileCommit(new PAddRoleNum());
		
		pexecuteWhileCommit(new PAddBuffToCreatRoleProc(newRoleID));
		for(int i = 0; i < 3;i++)
		{
			int schoolId = xtable.Properties.selectSchool(newRoleID);
			int apId = schoolId * 100 + fire.pb.skill.Module.inbornSeq[i];
			
			SkillRole srole = new SkillRole(newRoleID);
			if(srole.getInbornLevel(apId) == null)
				srole.updateInbornWithSP(apId);
		}
		
		fire.pb.item.Pack bag = new fire.pb.item.Pack(newRoleID, false);
		bag.addSysCurrency((long)(fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_ONBORN)), MoneyType.MoneyType_EreditPoint, "创建人物时赠送", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_createrole, 0);
/**/	
		//更新人物评分
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(newRoleID));	
		//初始化玩家更新时间
		xbean.RoleUpdateTime roleUpdateTime=xtable.Roleupdatetimetab.get(newRoleID);
		if(roleUpdateTime==null){
			roleUpdateTime=xbean.Pod.newRoleUpdateTime();
			xtable.Roleupdatetimetab.insert(newRoleID, roleUpdateTime);
		}
		roleUpdateTime.setDateupdatetime(System.currentTimeMillis());
		roleUpdateTime.setWeekupdatetime(fire.pb.util.DateValidate.getWeekNumber());
		//运营日志信息
		fire.log.YYLogger.OpRegLog(userID,newRoleID,1);
		fire.log.YYLogger.createRoleLog(newRoleID, new fire.log.beans.RoleCreateBean(pro.getShape(),pro.getSchool()));
		
		//http://mt3.pengyouquan001.locojoy.com:8830/enlist/submit_code?code=7956261002&new_serverid=1101011002&new_roleid=795626
		final CCreateRole crproto = (CCreateRole)thisProtocol;
		if(ConfigManager.isOpenRecruit && !crproto.code.isEmpty() && !crproto.code.equals("") && !crproto.code.equals("0")){
			final String buildurl = String.format(
					"http://%1$s%2$s?code=%3$s&new_serverid=%4$s&new_roleid=%5$s",
					address, func, crproto.code, Gs.serverid, newRoleID);
			HttpGet request = new HttpGet(buildurl);
			Gs.getHttpClient().execute(request, new HttpCallBackHandler() {
				@Override
				protected boolean process(JSONObject json){
					final String errno = json.getString("errno");
					if(errno.isEmpty() || errno.equals("")) {
						logger.info("PCreateRole.HttpCallBackHandler:角色[" + newRoleID + "]发送招募信息成功！");
						new mkdb.Procedure() {
		    				@Override
		    				protected boolean process() {	
								if (fire.pb.fushi.Module.GetPayServiceType() == 1)
								{
									final SCommonDayPay dpcc = ConfigManager.getInstance().getConf(SCommonDayPay.class).get(14);//点卡服招募奖励配置
									int itemid = dpcc != null ? dpcc.getServerdata() : 0;
									logger.info("PCreateRole.HttpCallBackHandler角色[" + newRoleID + "]发放点卡服招募奖励:" + itemid);
									if(itemid > 0) {
										fire.pb.item.Pack bag = new fire.pb.item.Pack(newRoleID, false);
										if(bag.doAddItem(itemid, 1, 0, 0, "招募奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shifu, 0) != 1){
											logger.error("PCreateRole.HttpCallBackHandler角色[" + newRoleID + "]添加点卡服招募奖励物品:" + itemid + ",doAddItem失败！");
											return false;
										}
									}
								} else {
									final SCommon cc = RoleConfigManager.getRoleCommonConfig(425);//普通服招募奖励配置
									int itemid = cc != null ? Integer.parseInt(cc.getValue()) : 0;
									logger.info("PCreateRole.HttpCallBackHandler角色[" + newRoleID + "]发放普通服招募奖励:" + itemid);
									if(itemid > 0) {
										fire.pb.item.Pack bag = new fire.pb.item.Pack(newRoleID, false);
										if(bag.doAddItem(itemid, 1, 0, 0, "招募奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shifu, 0) != 1){
											logger.error("PCreateRole.HttpCallBackHandler角色[" + newRoleID + "]添加普通服招募奖励物品:" + itemid + ",doAddItem失败！");
											return false;
										}
									}
								}
		    					return true;
		    				}
						}.submit();
		            } else {
		            	logger.warn("PCreateRole.HttpCallBackHandler:errno=" + errno + ",message=" + json.getString("message"));
		            }
					return true;
				}
			});
		} else {
			logger.info("PCreateRole.HttpCallBackHandler:角色[" + newRoleID + "]没有招募信息.");
		}
		return true;
	}
}
