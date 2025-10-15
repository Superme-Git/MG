package fire.pb.clan.srv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import xbean.ClanMemberInfo;
import xbean.RequestRuneInfo;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.HelpCountManage;
import fire.pb.PAddClanPointProc;
import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.attr.RoleCurrency;
import fire.pb.attr.SRefreshRoleCurrency;
import fire.pb.circletask.PAbandonCircleTask;
import fire.pb.clan.ApplyClan;
import fire.pb.clan.ClanMember;
import fire.pb.clan.ClanPositionType;
import fire.pb.clan.ClanUtils;
import fire.pb.clan.FireReasonType;
import fire.pb.clan.PClanMedicItem;
import fire.pb.clan.PRefreshRoleClanKey;
import fire.pb.clan.RoleBaseInfo;
import fire.pb.clan.RuneCountInfo;
import fire.pb.clan.RuneInfo;
import fire.pb.clan.RuneRequestInfo;
import fire.pb.clan.SApplyClanList;
import fire.pb.clan.SClanRedTip;
import fire.pb.clan.SClanyaofang;
import fire.pb.clan.SFireMember;
import fire.pb.clan.SLeaveClan;
import fire.pb.clan.SOpenClan;
import fire.pb.clan.SRefreshContribution;
import fire.pb.clan.SRefreshMemberList;
import fire.pb.clan.SRequestApply;
import fire.pb.clan.SRequestRuneCount;
import fire.pb.clan.SRequestRuneInfo;
import fire.pb.clan.SRuneGive;
import fire.pb.clan.SRuneRequest;
import fire.pb.clan.SRuneRequestView;
import fire.pb.clan.SRuneSet;
import fire.pb.common.SCommon;
import fire.pb.game.MoneyType;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;
import fire.pb.util.StringConstant;
import gnet.link.Onlines;
import mkdb.Procedure;


public abstract class ClanManage {
	public static final Logger logger = Logger.getLogger("CLAN");
	
	public static int Lobby = 1; //公会大厅 by changhao
	public static int GoldBank = 2; //金库 by changhao
	public static int DrugStone = 3; // 药店 by changhao
	public static int Hotel = 4; //旅店 by changhao
	
	public static final int STATE_QUIT=0;//主动退出
	public static final int STATE_KITE=1;//踢出
	public static final int STATE_DISMISS=2;//解散
	public static final int STATE_ADD=3;//加入
	
	public static final int NORMAL_LEAVE_REASON = 1;// 自己离开
	public static final int FIRE_LEAVE_REASON = 2;// 被驱逐离开
	public static final int DISMISS_LEAVE_REASON = 3;// 公会解散离开
	
	public static final int FREERED_TIME=3*24*60*60*1000;//冻结帮贡解冻的时间处理
	public static final int REQUEST_RUNE_TIME=3*60*60*1000;//请求到期时间
	
	
	public static final int ACTION_TYPE_REQUEST=0;//请求
	public static final int ACTION_TYPE_GIVE=1;//捐赠
	
	public static final int AIMWORD_MAX=150;//公会公告的最大数量
	
	public Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);
	
	public static  Map<Integer, SRuneSet> sRuneSet = ConfigManager.getInstance().getConf(SRuneSet.class);//符文配置表
	
	public static Map<Integer, SClanyaofang> confs = ConfigManager.getInstance().getConf(SClanyaofang.class);
	
	public static HashMap<Integer, List<SClanyaofang>> group = null;
	
	public static final int EVENT_CREATE=1;//	创建公会	年-月-日 创建公会
	public static final int EVENT_ADD=2;//	加入公会	年-月-日 恭喜新成员XXX加入公会
	public static final int EVENT_QUIT=3;//	退出公会	年-月-日 很遗憾，XXX离开了公会
	public static final int EVENT_APPOINT=4;//	公会任命	年-月-日 XX把XXX任命为XXXX
	public static final int EVENT_KICK=5;//	踢出公会	年-月-日 XXX因XXXX被XXX踢出公会
	public static final int EVENT_MAINTENANCE=6;//	公会维护	年-月-日 公会维护消耗了XXX公会资金
	public static final int EVENT_BUILD_UP=7;//	公会建筑升级	年-月-日 公会XX提升至X级
	public static final int EVENT_BUILD_DOWN=8;//	公会建筑降级	年-月-日 公会XX降至X级
	public static final int EVENT_CHANGE_NAME=9;//	修改公会名称 年-月-日 XX将公会名称修改为XXXX
	public static final int EVENT_CHANGE_INST_SYS=10;//经过广大成员历尽千辛万苦的努力，终于通关公会副本$parameter1$,下次活动将默认挑战公会副本$parameter2$.(可在活动面板管理按钮处设置)
	public static final int EVENT_CHANGE_INST_MAN=11;// 公会成员$parameter1$修改挑战公会副本成功，下次公会副本活动将挑战公会副本$parameter2$.(可在活动面板管理按钮处设置)
	public static final int EVENT_IMPEACH_START=12;// <T t='$parameter1$' c='ff005b0f'/><T t='$parameter2$' c='ff005b0f'/><T t='发起了弹劾' c='ff50321a'/>
	public static final int EVENT_IMPEACH_SUCC=13;// <T t='$parameter1$' c='ff005b0f'/><T t='弹劾成功，' c='ff50321a'/><T t='$parameter2$' c='ff005b0f'/><T t='成为新的会长' c='ff50321a'/>
	
	
	
	
	public static final int EVENT_MAX_NUM=20;//事件最大记录数量  
	
	public static final int MAX_RUNE_NUM=100;//最大符文显示数量
	
	public static final int MAX_APPLY_NUM = 100;//公会申请人数发送给客户端的上限值
	
	/**
	 * 添加公会的分红
	 * @param roleId
	 * @param addvalue
	 * @param reason
	 * @param showMsg
	 * @return
	 */
	public static boolean addClanBonus(long roleId,int addvalue,String reason,boolean showMsg){
		if(addvalue<=0){
			return false;
		}
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, false);
		if(clanInfo==null){
			return false;
		}
		if(Integer.MAX_VALUE-clanInfo.getBonus()<=addvalue){
			addvalue=Integer.MAX_VALUE-clanInfo.getBonus();
		}
		if(addvalue<=0){
			return false;
		}
		
		clanInfo.setBonus(clanInfo.getBonus()+addvalue);
		
		//这里添加以下公会分红次数，公会分红只有公会任务产出，所以在这里添加
		xbean.ClanMemberInfo clanMemberInfo=clanInfo.getMembers().get(roleId);
		if(clanMemberInfo!=null){
			clanMemberInfo.setTasknum(clanMemberInfo.getTasknum()+1);
		}
		if(showMsg){
			//提示获得公会分红
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 160158,Arrays.asList(addvalue+""));
		}
		logger.info("roleId "+roleId+"\t公会key\t"+clanInfo.getKey()+"\t获得公会分红\t"+reason+"\t"+addvalue+" 玩家当前公会分红 "+clanInfo.getBonus());
		return true;
	}
	
	
	/**
	 * 添加贡献度
	 * @param roleId
	 * @param addvalue
	 * @param reason 添加说明
	 */
	public static boolean addContribution(long roleId,int addvalue,String reason, YYLoggerTuJingEnum way, boolean showMsg){
		if(addvalue<=0){
			return false;
		}
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, false);
		if(clanInfo==null){
			return false;
		}
		ClanMemberInfo member = clanInfo.getMembers().get(roleId);
		if(member==null){
			return false;
		}
		xbean.RoleClanPoint clanpoint = ClanUtils.getRoleClanPoint(roleId, false);
		//防止溢出
		if(Integer.MAX_VALUE - clanpoint.getCurrentclanpoint() <= addvalue){
			addvalue=Integer.MAX_VALUE - clanpoint.getCurrentclanpoint();
		}
		if(addvalue<=0){
			return false;
		}
		int addclandkp=addvalue;
		
		clanpoint.setCurrentclanpoint(clanpoint.getCurrentclanpoint()+addvalue);//当前帮贡
		if(Integer.MAX_VALUE - clanpoint.getHistoryclanpoint() <= addvalue){
			clanpoint.setHistoryclanpoint(Integer.MAX_VALUE);//历史帮贡
		}else{
			clanpoint.setHistoryclanpoint(clanpoint.getHistoryclanpoint()+addvalue);//历史帮贡
		}
		if(Integer.MAX_VALUE - member.getWeekclanpoint() <= addvalue){
			member.setWeekclanpoint(Integer.MAX_VALUE);//周帮贡
		}else{
			member.setWeekclanpoint(member.getWeekclanpoint()+addvalue);//周帮贡
		}
		if(showMsg){
			//提示获得帮贡
			final List<String> parameters1 = new ArrayList<String>();
			parameters1.add(addvalue+"");
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 160064,parameters1);
			
		}
		
		//判断加入公会时间是否超过3天
		int tipvalue=0;
		if(System.currentTimeMillis()-member.getJointime()>FREERED_TIME){
			if(clanpoint.getFreezedclanpoint()>addvalue){
				tipvalue=addvalue;
				clanpoint.setFreezedclanpoint(clanpoint.getFreezedclanpoint()-addvalue);
				
			}else{
				//提示
				tipvalue=clanpoint.getFreezedclanpoint();
				clanpoint.setFreezedclanpoint(0);//冻结帮贡
			}
			if(showMsg){
				//发送客户端提示
				if(tipvalue>0){
					MessageMgr.psendMsgNotifyWhileCommit(roleId, 160065,Arrays.asList(tipvalue+""));
				}
			}
		}
		if(tipvalue>0){
			if(Integer.MAX_VALUE-clanpoint.getCurrentclanpoint()<=tipvalue){
				clanpoint.setCurrentclanpoint(Integer.MAX_VALUE);//当前帮贡
			}else{
				clanpoint.setCurrentclanpoint(clanpoint.getCurrentclanpoint()+tipvalue);//当前帮贡
			}
			
		}
		
		if(Integer.MAX_VALUE - clanInfo.getClandkp() <= addclandkp){
			addclandkp=Integer.MAX_VALUE - clanInfo.getClandkp();
		}
		if(addclandkp>0){
			clanInfo.setClandkp(clanInfo.getClandkp()+addclandkp);
		}
		//发送给客户端更新帮贡信息
		sendRefreshCurrentClanPoint(roleId,clanpoint.getCurrentclanpoint());
		fire.log.YYLogger.OpTokenGetLog(roleId, way, MoneyType.MoneyType_FactionContribute,
				addvalue, clanpoint.getCurrentclanpoint(), new fire.log.beans.ItemBean());
		logger.info("roleId "+roleId+"\t公会key\t"+clanInfo.getKey()+"\t获得帮贡\t"+reason+"\t"+addvalue+"\t解冻当前帮贡"+tipvalue+" 玩家当前帮贡 "+clanpoint.getCurrentclanpoint());
		return true;
	}
	
	/**
	 * 处理公会事件触发逻辑
	 * @param clanInfo
	 * @param eventType
	 * @param objectroleid
	 * @param parameter1
	 * @param parameter2
	 * @param parameter3
	 */
	public static void addDealClanEventInfo(xbean.ClanInfo clanInfo,int eventType,long objectroleid,String parameter1,String parameter2,String parameter3){
		java.util.List<xbean.ClanEventRecord> list=clanInfo.getClaneventrecordlist();
		xbean.ClanEventRecord record=xbean.Pod.newClanEventRecord();
		record.setTime(System.currentTimeMillis());
		String str=fire.pb.util.DateValidate.getCurrentTime()+"#";
		str+=StringConstant.ID2String("",getClanEventInfoIndexByType(eventType));
		switch(eventType){
		case EVENT_CREATE://创建公会
		case EVENT_ADD://加入公会
		case EVENT_QUIT://退出公会
		case EVENT_MAINTENANCE://公会维护
		case EVENT_IMPEACH_START://发起弹劾
		case EVENT_IMPEACH_SUCC://弹劾成功
			str=str.replaceAll("\\$parameter1\\$",  parameter1);
			break;
		case EVENT_APPOINT://公会任命
		case EVENT_KICK://踢出公会
			str=str.replaceAll("\\$parameter1\\$",  parameter1);
			str=str.replaceAll("\\$parameter2\\$",  parameter2);
			str=str.replaceAll("\\$parameter3\\$",  parameter3);
			break;
		case EVENT_BUILD_UP://公会建筑升级
		case EVENT_BUILD_DOWN://公会建筑降级
		case EVENT_CHANGE_NAME://修改公会名称
		case EVENT_CHANGE_INST_MAN://修改公会副本_人为
		case EVENT_CHANGE_INST_SYS://修改公会副本_系统
			str=str.replaceAll("\\$parameter1\\$",  parameter1);
			str=str.replaceAll("\\$parameter2\\$",  parameter2);
			break;
		}
		
		record.setContent(str);
		record.setEventtype(eventType);
		record.setObjectroleid(objectroleid);
		
		//删除超过的条数
		list.add(record);
		if(list.size() > EVENT_MAX_NUM){
			list.remove(0);
		}
	}
	
	
	/**
	 * 加入公会的时候处理一下，是不是回到了原来的公会
	 * @param roleId
	 * @param newfactionkey
	 */
	public static void addClanDealContribution(long roleId,long newfactionkey){
		xbean.RoleClanPoint clanpoint = ClanUtils.getRoleClanPoint(roleId, false);
		if(clanpoint!=null){
			xbean.Properties prop = xtable.Properties.select(roleId);
			if(prop.getOldclankey()==newfactionkey){
				//如果未加入其他公会的情况下重新加入原有公会则立即恢复所有“现有贡献度”和“历史贡献度”
				if(Integer.MAX_VALUE-clanpoint.getCurrentclanpoint()<=clanpoint.getOldfreezedclanpoint()){
					clanpoint.setCurrentclanpoint(Integer.MAX_VALUE);
				}else{
					clanpoint.setCurrentclanpoint(clanpoint.getOldfreezedclanpoint()+clanpoint.getCurrentclanpoint());
				}
				//处理冻结帮贡
				clanpoint.setFreezedclanpoint(clanpoint.getFreezedclanpoint() - clanpoint.getOldfreezedclanpoint());//冻结
				clanpoint.setOldfreezedclanpoint(0);
				
				clanpoint.setHistoryclanpoint(clanpoint.getOldhistoryclanpoint());
				clanpoint.setOldhistoryclanpoint(0);
				//发送给客户端更新帮贡信息
				sendRefreshCurrentClanPoint(roleId,clanpoint.getCurrentclanpoint());
			}else{
				xbean.helpcount helpcount = xtable.Helpcount.get(roleId);
		    	if (helpcount != null) {
		    		helpcount.setClanhishelpnum(0);
		    	}
			}
		}
	}
	
	/**
	 * 添加公会资金
	 * @param money
	 * @param faction
	 * @return
	 */
	public static boolean AddClanMoney(int money, xbean.ClanInfo faction)
	{
		if (money < 0)
		{
			if (faction.getMoney() < -money)
			{
				return false;
			}
		}
		
		int level = faction.getHouse().get(ClanManage.GoldBank);
		fire.pb.clan.SClanGoldBank bank =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanGoldBank.class).get(level);
		if (bank == null)
		{
			return false;
		}
		int v =0;
		if(money>0){
			v = (Integer.MAX_VALUE - faction.getMoney() <= money) ? Integer.MAX_VALUE
					: money + faction.getMoney();// 防止溢出;
		}else{
			v = money + faction.getMoney();
		}
		if (v > bank.limitmoney) //超过资金上限 by changhao
		{
			v = bank.limitmoney;
		}
				
		faction.setMoney(v);
		
		return true;
	}
	
	
	/**
	 * 处理所有的在玩家加入公会或者创建公会时候，需要清除上次的数据
	 * @param roleId
	 */
	public static void clearAllInfoAddandCreate(long roleId){
		ClanManage.clearRuneinfo(roleId);//清理符文统计信息
	}
	
	/**
	 * 在加入新公会的时候需清除符文统计信息
	 * @param roleid
	 */
	public static void clearRuneinfo(long roleid){
		xbean.RuneInfo runeinfo=xtable.Runeinfotab.get(roleid);//玩家符文相关信息
		if(runeinfo!=null){
			runeinfo.setAllgivenum(0); // 捐符数
			runeinfo.setAllacceptnum(0); // 收符数
			runeinfo.setItemlevel(0); // 物品等级
		}
	}
	
	/**
	 * 玩家加入或者创建公会的时候处理一下帮贡
	 * @param roleId
	 * @param newfactionkey
	 */
	public static void dealContribution(long roleId,int state){
		//加入的还是上次的公会，数值都不变,所以需要加上上次冻结的值，以及历史帮贡
		//根据玩家上次退出帮派的状态来处理帮贡
		//当玩家“主动退出公会”或“被踢出公会”时未消耗的现有贡献将冻结50%。“公会解散”时未消耗的现有贡献将冻结100%。
		//如果未加入其他公会的情况下重新加入原有公会则立即恢复所有“现有贡献度”和“历史贡献度”。如果加入新的公会，所冻结的公会贡献将在加入新公会的3天后1:1返还。
		if(state==STATE_QUIT||state==STATE_KITE){
			//主动退出或者被踢出
			xbean.RoleClanPoint clanpoint = ClanUtils.getRoleClanPoint(roleId, false);
			int freenum=clanpoint.getCurrentclanpoint()/2;
			if(Integer.MAX_VALUE-clanpoint.getFreezedclanpoint()<=freenum){
				clanpoint.setFreezedclanpoint(Integer.MAX_VALUE);
			}else{
				clanpoint.setFreezedclanpoint(clanpoint.getFreezedclanpoint() + freenum);//冻结
			}
			clanpoint.setOldfreezedclanpoint(freenum);//记录一下退出时候冻结了多少帮贡
			clanpoint.setOldhistoryclanpoint(clanpoint.getHistoryclanpoint());//记录一下退出时候有多少历史帮贡
			clanpoint.setCurrentclanpoint(clanpoint.getCurrentclanpoint()/2);//当前
			clanpoint.setHistoryclanpoint(0);//历史清零
//			clanpoint.setWeekclanpoint(0);//周清零
			//发送给客户端更新帮贡信息
			sendRefreshCurrentClanPoint(roleId,clanpoint.getCurrentclanpoint());
		}else if(state==STATE_DISMISS){
			//解散
			//“公会解散”时未消耗的现有贡献将冻结100%。
			xbean.RoleClanPoint clanpoint = ClanUtils.getRoleClanPoint(roleId, false);
			if(Integer.MAX_VALUE-clanpoint.getFreezedclanpoint()<=clanpoint.getCurrentclanpoint()){
				clanpoint.setFreezedclanpoint(Integer.MAX_VALUE);
			}else{
				clanpoint.setFreezedclanpoint(clanpoint.getFreezedclanpoint()+clanpoint.getCurrentclanpoint());//冻结
			}
			clanpoint.setOldfreezedclanpoint(clanpoint.getCurrentclanpoint());//记录一下退出时候冻结了多少帮贡
			clanpoint.setOldhistoryclanpoint(clanpoint.getHistoryclanpoint());//记录一下退出时候有多少历史帮贡
			clanpoint.setCurrentclanpoint(0);//当前
			clanpoint.setHistoryclanpoint(0);//历史清零
//			clanpoint.setWeekclanpoint(0);//周清零
			//发送给客户端更新帮贡信息
			sendRefreshCurrentClanPoint(roleId,clanpoint.getCurrentclanpoint());
		}
	}
	
	/**
	 * 消耗帮贡
	 * @param roleId
	 * @param delvalue 消耗值
	 * @param reason  消耗原因
	 * @param showMsg 是否通知客户端
	 */
	public static boolean delContribution(long roleId,int delvalue,String reason,boolean showMsg){ 
		if(delvalue<=0){
			return false;
		}
		xbean.RoleClanPoint clanpoint = ClanUtils.getRoleClanPoint(roleId, false);
		//判断当前值是否够扣除
		if(clanpoint.getCurrentclanpoint()<delvalue){
			logger.info("roleId "+roleId+"\t扣除帮贡错误\t"+reason+"\t扣除值"+delvalue+"\t数量不足，玩家当前帮贡 "+clanpoint.getCurrentclanpoint());
			return false;
		}
		
		clanpoint.setCurrentclanpoint(clanpoint.getCurrentclanpoint()-delvalue);//当前帮贡
		if(clanpoint.getCurrentclanpoint()<0){
			clanpoint.setCurrentclanpoint(0);
		}
		
		if(showMsg){
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 160313,Arrays.asList(delvalue+""));
			
		}
		//发送给客户端更新帮贡信息
		sendRefreshCurrentClanPoint(roleId,clanpoint.getCurrentclanpoint());
		
		Long clankey=xtable.Properties.selectClankey(roleId);
		if(clankey==null){
			clankey=0l;
		}
		logger.info("roleId "+roleId+"\t公会key\t"+clankey+"\t消耗帮贡\t"+reason+"\t"+delvalue+" 玩家当前帮贡 "+clanpoint.getCurrentclanpoint());
		return true;
	}
	
//	/**
//	 * 加入公会职位区分等级
//	 * @return
//	 */
//	public static int getAddClanPositionLevel(){
//		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(210);
//		if(conf==null){
//			logger.error("通用数据表数据错误，不存在编号"+210);
//			return 40;
//		}
//		return Integer.parseInt(conf.getValue());
//	}
	
	
	/**
	 * 获得一键申请入会的最大公会数
	 * @return
	 */
	public static int getApplyMaxNum(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(238);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+238);
			return 20;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 根据建筑类型获得建筑名称
	 * @param type
	 * @return
	 */
	public static String getClanBuildNameByType(int type){
		if(type==ClanManage.GoldBank){
			return StringConstant.ID2String("",StringConstant.BUILD_GOLD);
		}else if(type==ClanManage.DrugStone){
			return StringConstant.ID2String("",StringConstant.BUILD_DRUGSTONE);
		}else if(type==ClanManage.Hotel){
			return StringConstant.ID2String("",StringConstant.BUILD_HOTIAL);
		}else{
			return StringConstant.ID2String("",StringConstant.BUILD_CLAN);
		}
	}
	
	/**
	 * 根据事件类型获得内容提示
	 * @param eventType
	 * @return
	 */
	public static int getClanEventInfoIndexByType(int eventType){
		int index=-1;
		switch(eventType){
		case EVENT_CREATE://创建公会
			index=StringConstant.EVENT_CREATE_STR;
			break;
		case EVENT_ADD://加入公会
			index=StringConstant.EVENT_ADD_STR;
			break;
		case EVENT_QUIT://退出公会
			index=StringConstant.EVENT_QUIT_STR;
			break;
		case EVENT_APPOINT://公会任命
			index=StringConstant.EVENT_APPOINT_STR;
			break;
		case EVENT_KICK://踢出公会
			index=StringConstant.EVENT_KICK_STR;
			break;
		case EVENT_MAINTENANCE://公会维护
			index=StringConstant.EVENT_MAINTENANCE_STR;
			break;
		case EVENT_BUILD_UP://公会建筑升级
			index=StringConstant.EVENT_BUILD_UP_STR;
			break;
		case EVENT_BUILD_DOWN://公会建筑降级
			index=StringConstant.EVENT_BUILD_DOWN_STR;
			break;
		case EVENT_CHANGE_NAME://修改公告
			index=StringConstant.EVENT_CHANGE_NAME_STR;
			break;
		case EVENT_CHANGE_INST_MAN://修改公会副本_认为
			index=StringConstant.EVENT_CHANGE_INST_MAN_STR;
			break;
		case EVENT_CHANGE_INST_SYS://修改公会副本_认为
			index=StringConstant.EVENT_CHANGE_INST_SYS_STR;
			break;
		case EVENT_IMPEACH_START://发起弹劾
			index=StringConstant.EVENT_IMPEACH_START_STR;
			break;
		case EVENT_IMPEACH_SUCC://弹劾成功
			index=StringConstant.EVENT_IMPEACH_SUCC_STR;
			break;
		}
		return index;
	}
	
	
	/**
	 * 捐赠符文获得的帮贡
	 * @return
	 */
	public static int getGiveRuneAwardContri(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(214);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+214);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 捐赠符文获得的声望
	 * @return
	 */
	public static int getGiveRuneAwardSW(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(213);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+213);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 获得禁言剩余时间  分钟
	 * @param memberInfo
	 * @return
	 */
	public static long getLeaveStopTalkMin(xbean.ClanMemberInfo memberInfo){
		long bannedtalktime = memberInfo.getBannedtalktime();
		long leveltime=bannedtalktime-System.currentTimeMillis();
		if (leveltime>0) {
			//计算时间
			leveltime=leveltime/1000/60;//分钟
			if(leveltime==0){
				leveltime=1;//如果是0分钟，处理为1分钟
			}
		}
		return leveltime;
	}
	
	/**
	 * 获得创建公会的最大数
	 * @return
	 */
	public static int getMaxCreateClanNum(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(273);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+273);
			return -1;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 每日踢出人数上限请调用通用配置表ID：272
	 * @return
	 */
	public static int getMaxFireMemberNum(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(272);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+272);
			return -1;
		}
		return Integer.parseInt(conf.getValue());
	}
	/**
	 * 50级学徒自动转为成员时间需求
	 * @return
	 */
	public static int getPositionChangeAddTime(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(263);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+263);
			return 48;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 50级学徒自动转为成员帮贡需求需求
	 * @return
	 */
	public static int getPositionChangeBanggong(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(262);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+262);
			return 150;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 50级学徒自动转为成员等级需求
	 * @return
	 */
	public static int getPositionChangeLevel(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(211);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+211);
			return 50;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	
	
	
	/**
	 * 获得禁言的时长，单位是分钟
	 * @return
	 */
	public static int getStopTalkMin(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(212);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+212);
			return 120;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 三倍产药功能保底资金
	 * @return
	 */
	public static int getThreeMedicBaseMoney(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(244);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+244);
			return 10000000;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 2倍产药功能保底资金
	 * @return
	 */
	public static int getTwoMedicBaseMoney(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(245);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+245);
			return 8000000;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 封装申请列表成员信息
	 * @param clanInfo
	 * @return
	 */
	public static SRequestApply packagingToApplicantMemberList(xbean.ClanInfo clanInfo){
		long clankey=clanInfo.getKey();
		java.util.Map<Long, Long> applicants = clanInfo.getApplicants();
		SRequestApply sRequestApply = new SRequestApply();
		int i=0;
		List<Long> delrole = new LinkedList<Long>();
		for (java.util.Map.Entry<Long, Long> applicant : applicants.entrySet()) {
			xbean.Properties propRole=xtable.Properties.select(applicant.getKey());
			//这里处理一下，玩家可能被删除了
			if(propRole==null){
				continue;
			}
			if(propRole.getClankey()>0){
				delrole.add(applicant.getKey());//已经有公会的玩家，需要删除
				continue;
			}
			RoleBaseInfo roleBaseInfo = new RoleBaseInfo();
			roleBaseInfo.roleid = applicant.getKey();
			roleBaseInfo.rolename = propRole.getRolename();
			roleBaseInfo.rolelevel = propRole.getLevel();
			roleBaseInfo.roleschool = propRole.getSchool();
			roleBaseInfo.applytime = applicant.getValue();
			roleBaseInfo.fightvalue = propRole.getRolezonghemaxscore();//综合战力
			sRequestApply.applylist.add(roleBaseInfo);
			i++;
			if(i>=MAX_APPLY_NUM){
				break;
			}
		}
		//删除已经有公会的玩家
		if(delrole.size()>0){
			xbean.ClanInfo clanInfodel = xtable.Clans.get(clankey);
			if(clanInfodel!=null){
				for(long roleid:delrole){
					clanInfodel.getApplicants().remove(roleid);
				}
			}
		}
		return sRequestApply;
	}
	
	/**
	 * 刷新公会药房
	 * @param factionkey
	 */
	public static mkdb.Procedure refreshMedic(long factionkey){
		return new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				if(null==group){
					group=new HashMap<Integer, List<SClanyaofang>>();//根据类型分组
					for(SClanyaofang sf:confs.values()){
						if(sf==null){
							continue;
						}
						List<SClanyaofang> list=group.get(sf.getRandomgroup());
						if(list==null){
							list=new ArrayList<SClanyaofang>();
							group.put(sf.getRandomgroup(), list);
						}
						list.add(sf);
					}
				}
				//扣除公会资金
				xbean.ClanInfo fa=xtable.Clans.get(factionkey);
				if(fa==null){
					return false;
				}
				xbean.MedicItemList itemlist=xtable.Medicitemlisttab.get(fa.getKey());
				if(itemlist==null){
					itemlist=xbean.Pod.newMedicItemList();
					xtable.Medicitemlisttab.insert(fa.getKey(), itemlist);
				}
				
				int level = fa.getHouse().get(ClanManage.DrugStone);
				int level_lobby=fa.getClanlevel();
				//这里需要获得公会等级，哪个小取哪个
				level=level>level_lobby?level_lobby:level;
				//如果是0级公会不处理下面的逻辑
				if(level==0){
					return false;
				}
				fire.pb.clan.SClanDrugstore drugstone =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanDrugstore.class).get(level);
				if (drugstone == null){
					return false;
				}
				//按规则随机药品
				int cost=0;
				int type=itemlist.getSelecttype();
				int oldtype=itemlist.getSelecttype();
				
				if(type==PClanMedicItem.SELECT_THREE){//三倍产药
					//判断资金帮会资金是否够
					cost=drugstone.getTrimoney();
				}else if(type==PClanMedicItem.SELECT_DOUBLE){//2倍产药
					cost=drugstone.getDoublemoney();
				}
				//判断资金帮会资金是否够,这里2倍和3被产药需要保底资金
				if(fa.getMoney()>=cost){
					if(type==PClanMedicItem.SELECT_THREE&&fa.getMoney()>=ClanManage.getThreeMedicBaseMoney()){
						fa.setMoney(fa.getMoney()-cost);
					}else if(type==PClanMedicItem.SELECT_DOUBLE&&fa.getMoney()>=ClanManage.getTwoMedicBaseMoney()){
						fa.setMoney(fa.getMoney()-cost);
					}else{
						type=PClanMedicItem.SELECT_BASE;
					}
				}else{
					type=PClanMedicItem.SELECT_BASE;
				}
				
				//根据选择类型刷新
				int randomnum=PClanMedicItem.SELECT_BASE_NUM;
				if(type==PClanMedicItem.SELECT_THREE){
					randomnum=PClanMedicItem.SELECT_THREE_NUM;
				}else if(type==PClanMedicItem.SELECT_DOUBLE){
					randomnum=PClanMedicItem.SELECT_DOUBLE_NUM;
				}
				
				itemlist.getMedicitems().clear();//清除上一次数据
				
				for(List<SClanyaofang> list:group.values()){
					if(list==null||list.size()==0){
						continue;
					}
					Collections.shuffle(list);//打乱排序
					if(list.size()<randomnum){
						randomnum=list.size();
					}
					for(int i=0;i<randomnum;i++){
						SClanyaofang sClanyaofang=list.get(i);
						if(sClanyaofang!=null){
							xbean.MedicItem item=xbean.Pod.newMedicItem();
							item.setItemid(sClanyaofang.getId());
							item.setItemnum(drugstone.getDragnummax());
							itemlist.getMedicitems().put(item.getItemid(), item);
						}
					}
				}
				
				//重新设置玩家的选择类型
				itemlist.setSelecttype(type);
				itemlist.setMedicitemrefreshtime(System.currentTimeMillis());//设置刷新时间
				if(oldtype!=type){
					//160443<T t="由于公会资金不足，公会药房已被系统设定为" c="ff693f00"></T><T t="$parameter1$" c="ff693f00"></T><T t="产药。" c="ff693f00"></T>
					MessageMgr.psendMsgNotifyWhileCommit(fa.getMembers().keySet(), 160443, 0,Arrays.asList(fire.pb.clan.CRequestSelectType.getTypeName(type)));
				}
				// 通知公会中的兄弟
				MessageMgr.psendMsgNotifyWhileCommit(fa.getMembers().keySet(), 160368, 0,null);
				if(type==PClanMedicItem.SELECT_BASE){
					cost=0;
				}
				
				logger.info("公会模块处理公会药房刷新\t"+"公会id "+fa.getKey()+"\t公会名称 "+fa.getClanname()+"\t消耗公会资金 "+cost+"\t刷新类型 "+type);
				return true;
			}
		};
	}
	
	
	
	/**
	 * 返回符文统计界面
	 * @param roleid
	 * @return
	 */
	public static boolean sendCRequestRuneCount(long roleid){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		
		java.util.ArrayList<fire.pb.clan.RuneCountInfo> runecountinfolist=new ArrayList<RuneCountInfo>();
		
		for(Map.Entry<Long, xbean.ClanMemberInfo> entry : clanInfo.getMembers().entrySet()){
			long memberId = entry.getKey();
			fire.pb.clan.RuneCountInfo info=new RuneCountInfo();
			info.roleid=memberId;
			PropRole propRole = new PropRole(memberId, true);
			info.rolename=propRole.getName();//名称
			info.level=propRole.getLevel(); // 角色等级
			info.school=propRole.getSchool(); // 门派职业
			xbean.RuneInfo otherruneinfo=xtable.Runeinfotab.select(memberId);//玩家符文相关信息
			if(otherruneinfo!=null){
				//过滤一下没有捐赠符文的人
				if(otherruneinfo.getAllgivenum()==0 && otherruneinfo.getAllacceptnum()==0){
					continue;
				}
				info.givenum=otherruneinfo.getAllgivenum(); // 捐符数
				info.acceptnum=otherruneinfo.getAllacceptnum(); // 收符数
				info.givelevel=otherruneinfo.getItemlevel();//最高等级
			}else{
				continue;
			}
			info.position=entry.getValue().getClanposition();
			runecountinfolist.add(info);
		}
		
		SRequestRuneCount sRequestRuneCount=new SRequestRuneCount();
		sRequestRuneCount.runecountinfolist=runecountinfolist;
		Procedure.psendWhileCommit(roleid, sRequestRuneCount);
		return true;
	}
	
	/**
	 * 返回请求的请求符文请求信息
	 * @param roleid
	 * @return
	 */
	public static mkdb.Procedure sendCRequestRuneInfo(long roleid){
		return new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
				if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
					return false;
				}
				//需要消耗的活力
				int cost=-1;//如果消耗值是-1，说明不能用活力捐赠
				xbean.Properties prop = xtable.Properties.select(roleid);
				int school = prop.getSchool();
				int skilllevel = LiveSkillManager.getInstance().GetEnhanceLevelBySchool(roleid, school);//取得职业技能等级  by changhao
				if (skilllevel>0)
				{
					int liveskillid = LiveSkillManager.getInstance().GetEnhanceLiveSkillBySchool(school);
					cost=LiveSkillManager.getInstance().getEneryMakeCost(liveskillid, skilllevel);
				}
				
				int createitemid = LiveSkillManager.getInstance().GetEnhanceItemIdBySchool(school);//自己职业可以制作的符文
				
				SRequestRuneInfo sRequestRuneInfo=new SRequestRuneInfo();
				java.util.ArrayList<fire.pb.clan.RuneInfo> list=new ArrayList<fire.pb.clan.RuneInfo>();
				
				//请求列表信息
				xbean.RequestRuneInfoList requestRuneInfoList= xtable.Requestruneinfotab.select(clanInfo.getKey());
				if(requestRuneInfoList!=null){
					xbean.RuneInfo runeinfo=xtable.Runeinfotab.select(roleid);//玩家符文相关信息
					if(runeinfo==null){
						sRequestRuneInfo.requestnum=0;//当前使用次数
					}else{
						sRequestRuneInfo.requestnum=runeinfo.getDayrequestnum();//当前使用次数
					}
					List<xbean.RequestRuneInfo> requestruneinfo=new ArrayList<xbean.RequestRuneInfo>();
					List<xbean.RequestRuneInfo> selflist=new ArrayList<xbean.RequestRuneInfo>();
					
					for(xbean.RequestRuneInfo request:requestRuneInfoList.getRequestrunelists()){
						//iv.请求列表中请求信息达到180分钟后系统自动删除，如此时间段内有公会其他成员进行捐赠，则对应符文请求信息删除。
						//现在请求和捐赠统一处理先,差5秒就算过期
						if(System.currentTimeMillis()-request.getRequesttime()>(REQUEST_RUNE_TIME-5000)){
							continue;
						}
						if(request.getRoleid()==roleid){
							selflist.add(request.toData());
							continue;
						}
						//过滤掉请求符文不是自己职业的
						if(request.getActiontype()==0 && createitemid != request.getItemid()){
							continue;
						}
						//过滤掉已经不在当前公会的玩家
						if(request.getActiontype()==0 && xtable.Properties.selectClankey(request.getRoleid())!=clanInfo.getKey()){
							continue;
						}
						requestruneinfo.add(request.toData());
					}
					//添加自己的信息
					for(xbean.RequestRuneInfo info:selflist){
						RuneInfo runeInfo=createRuneInfo(info);
						list.add(runeInfo);
					}
					//排序
					if(requestruneinfo.size()>1){
						try {
							Collections.sort(requestruneinfo,new RequestRuneInfoComparator());
						} catch (Exception e) {
							//e.printStackTrace();
						}
					}
					
					int startnum=0;
					if(requestruneinfo.size()>MAX_RUNE_NUM){
						startnum=requestruneinfo.size()-MAX_RUNE_NUM;
					}
					
					for(int i=startnum;i<requestruneinfo.size();i++){
						xbean.RequestRuneInfo info=requestruneinfo.get(i);
						if(info==null){
							continue;
						}
						RuneInfo runeInfo=createRuneInfo(info);
						list.add(runeInfo);
					}
				}
				
				sRequestRuneInfo.runeinfolist=list;
				sRequestRuneInfo.useenergy=cost;
				Procedure.psendWhileCommit(roleid, sRequestRuneInfo);
				return true;
			}
		};
	}
	
	
	/**
	 * 创建符文信息
	 * @param request
	 * @return
	 */
	public static fire.pb.clan.RuneInfo createRuneInfo(xbean.RequestRuneInfo request){
		fire.pb.clan.RuneInfo runinfo=new RuneInfo();
		xbean.RuneInfo otherruneinfo=xtable.Runeinfotab.select(request.getRoleid());//玩家符文相关信息
		if(otherruneinfo!=null){
			runinfo.givenum=otherruneinfo.getAllgivenum(); // 捐符数
			runinfo.acceptnum=otherruneinfo.getAllacceptnum(); // 收符数
		}
		runinfo.roleid=request.getRoleid(); // 请求角色roleid
		PropRole propRole = new PropRole(request.getRoleid(), true);
		runinfo.rolename=propRole.getName();
		runinfo.level=propRole.getLevel(); // 角色等级
		runinfo.school=propRole.getSchool(); // 门派职业
		runinfo.shape=propRole.getShape(); // 角色造型头像
		runinfo.targetroleid=request.getTargetroleid();
		if(request.getTargetroleid()>0){
			PropRole targetpropRole = new PropRole(request.getTargetroleid(), true);
			runinfo.targetrolename=targetpropRole.getName();
		}
		runinfo.actiontype=request.getActiontype(); // 动作类型  0 请求符文    1捐献符文
		long leveltime=System.currentTimeMillis()-request.getRequesttime();
		if(leveltime<0){
			leveltime=0;
		}
		runinfo.requesttime=leveltime; // 请求时间
		runinfo.itemid=request.getItemid(); // 物品id
		runinfo.itemlevel=request.getItemlevel(); // 物品等级
		return runinfo;
	}
	
	
	
	
	/**
	 * 清除过期的请求
	 * @param cleankey
	 * @return
	 */
	public static mkdb.Procedure clearTimeAlived(long cleankey){
		return new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				List<xbean.RequestRuneInfo> delrequest=new ArrayList<RequestRuneInfo>();
				xbean.RequestRuneInfoList requestRuneInfoList= xtable.Requestruneinfotab.get(cleankey);
				if(requestRuneInfoList!=null){
					for(xbean.RequestRuneInfo request:requestRuneInfoList.getRequestrunelists()){
						//iv.请求列表中请求信息达到180分钟后系统自动删除，如此时间段内有公会其他成员进行捐赠，则对应符文请求信息删除。
						//现在请求和捐赠统一处理先
						if(System.currentTimeMillis()-request.getRequesttime()>REQUEST_RUNE_TIME){
							//删除
							delrequest.add(request);
						}
					}
					requestRuneInfoList.getRequestrunelists().removeAll(delrequest);
				}
				return true;
			}
		};
	}
	
	
	
	/**
	 * 捐献符文
	 * @param roleid
	 * @return
	 */
	public static boolean sendCRuneGive(long selfroleid,long targetid,int selecttype,int itemid,int itemkey,int bagtype){
		//不能自己发，自己捐献
		if(selfroleid==targetid){
			logger.info("玩家roleid "+selfroleid+"捐赠符文，自己不能给自己捐赠");
			return false;
		}
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(selfroleid, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(selfroleid)) {
			return false;
		}
		xbean.Properties prop = xtable.Properties.select(selfroleid);
		//判断目标是否还在当前公会
		if(!clanInfo.getMembers().containsKey(targetid)){
			MessageMgr.sendMsgNotify(selfroleid, 160601, null);
			return false;
		}
		
		if(selecttype!=0&&selecttype!=1){
			logger.info("玩家roleid "+selfroleid+"捐赠符文，数量错误");
			return false;
		}
		//请求列表信息
		xbean.RequestRuneInfoList requestRuneInfoList= xtable.Requestruneinfotab.get(clanInfo.getKey());
		if(requestRuneInfoList==null){
			return false;
		}
		xbean.RequestRuneInfo accept=null;
		
		for(xbean.RequestRuneInfo request:requestRuneInfoList.getRequestrunelists()){
			if(request.getRoleid()==targetid&&request.getItemid()==itemid&&request.getActiontype()==ACTION_TYPE_REQUEST){
				accept=request;
				break;
			}
		}
		//判断捐献的对象是否存在
		if(accept==null){
			logger.info("玩家roleid "+selfroleid+"捐赠符文，对象不存在");
			return false;
		}
		//加锁
		List<Long> list = new ArrayList<Long>();
		list.add(selfroleid);
		list.add(targetid);
		mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, list));
		//添加玩家捐献和被捐献次数
		xbean.RuneInfo runeinfo=xtable.Runeinfotab.get(selfroleid);//玩家符文相关信息
		if(runeinfo==null){
			runeinfo=xbean.Pod.newRuneInfo();
			xtable.Runeinfotab.insert(selfroleid, runeinfo);
		}
		
		xbean.RuneInfo targetruneinfo=xtable.Runeinfotab.get(targetid);//玩家符文相关信息
		if(targetruneinfo==null){
			targetruneinfo=xbean.Pod.newRuneInfo();
			xtable.Runeinfotab.insert(targetid, targetruneinfo);
		}
		
		int itemlevel=0;
		ItemBase item=null;//添加的物品
		if(selecttype==0){
			//消耗活力
			int school = prop.getSchool();
			
			int skilllevel = LiveSkillManager.getInstance().GetEnhanceLevelBySchool(selfroleid, school);//取得职业技能等级  by changhao
			if (skilllevel == 0)
			{
				MessageMgr.sendMsgNotify(selfroleid, 160366, null);
				return false;
			}
			
			int liveskillid = LiveSkillManager.getInstance().GetEnhanceLiveSkillBySchool(school);
			int createitemid = LiveSkillManager.getInstance().GetEnhanceItemIdBySchool(school);
			
			//判断是否可以生成这个物品
			if(createitemid!=itemid){
				MessageMgr.sendMsgNotify(selfroleid, 160366, null);
				return false;
			}
						
			boolean energyenough = LiveSkillManager.getInstance().ConsumeMakeEnergy(selfroleid, liveskillid, 
					skilllevel,"捐献符文消耗活力", YYLoggerTuJingEnum.tujing_Value_gonghuijuanzeng, true);
			
			if (energyenough == false) 
			{	//活力不足
				java.util.List<String> parameters = new java.util.ArrayList<String>();
				String s = Integer.toString(LiveSkillManager.getInstance().NeedEnergy(liveskillid, skilllevel));
				parameters.add(s);
				MessageMgr.psendMsgNotify(selfroleid, 143432, parameters);
				return false;						
			}							
			fire.pb.item.groceries.AttrUpItem itemtemp = (fire.pb.item.groceries.AttrUpItem)fire.pb.item.Module.getInstance().getItemManager().genItemBase(createitemid, 1);
			itemtemp.GenAttr(skilllevel);
			item=itemtemp;
			itemlevel=skilllevel;
			
		}else{
			//扣除捐赠人的物品
			ItemMaps bagContainer = Module.getInstance().getItemMaps(selfroleid, bagtype, false);//捐赠的人背包容器
			if(bagContainer==null){
				logger.info("玩家roleid "+selfroleid+"捐赠符文不存在");
				return false;
			}
			ItemBase giveitem = bagContainer.TransOut(itemkey, 1, "捐赠符文");
			if(giveitem==null){
				logger.info("玩家roleid "+selfroleid+"捐赠符文不存在2");
				return false;
			}
			item=giveitem;
		}
		
		
		
		//添加捐献次数,以及物品等级
		runeinfo.setAllgivenum(runeinfo.getAllgivenum()+1);
		if(runeinfo.getItemlevel()<=itemlevel){
			runeinfo.setItemlevel(itemlevel);
		}
		//添加获得次数
		targetruneinfo.setAllacceptnum(targetruneinfo.getAllacceptnum()+1);
		
		//发送捐赠成功信息
		MessageMgr.psendMsgNotifyWhileCommit(selfroleid, 160373, null);
		//通知被捐献人，有人给他捐赠物品了
		MessageMgr.psendMsgNotifyWhileCommit(targetid, 160394, Arrays.asList(prop.getRolename(),item.getName()));
		//成功捐赠符文的玩家将立即获得公会贡献度、银币等资源奖励
		int resultvalue=HelpCountManage.getInstance().addFactionValue(selfroleid, ClanManage.getGiveRuneAwardContri(), "捐赠符文");
		if(resultvalue>0){
			new PAddClanPointProc(selfroleid, resultvalue, YYLoggerTuJingEnum.tujing_Value_gonghuijuanzengget,false, "捐赠符文").call();
		}
		//移除请求捐赠信息，添加捐赠信息
		requestRuneInfoList.getRequestrunelists().remove(accept);//移除记录
		xbean.RequestRuneInfo request=xbean.Pod.newRequestRuneInfo();
		request.setRoleid(selfroleid); // 请求角色roleid
		request.setTargetroleid(targetid);//目标id
		request.setActiontype(ACTION_TYPE_GIVE); // 动作类型  0 请求符文    1捐献符文
		request.setRequesttime(System.currentTimeMillis()); // 请求时间
		request.setItemid(itemid); // 物品id
		request.setItemlevel(itemlevel); // 物品等级
		requestRuneInfoList.getRequestrunelists().add(request);
		
		//添加道具
		Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(targetid, fire.pb.item.BagTypes.BAG, false);
		if (bag.isFull())
		{
			fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(targetid, BagTypes.TEMP, false);
			if (tempBag.doAddItem(item, -1, "捐赠符文添加物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuijuanzengget, 2) != AddItemResult.SUCC)
			{
				logger.info("玩家roleid "+selfroleid+"捐赠符文添加物品出错1");
				return false;
			}
		}
		else
		{
			if(bag.doAddItem(item, -1, "捐赠符文添加物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuijuanzengget, 2) != AddItemResult.SUCC)
			{
				logger.info("玩家roleid "+selfroleid+"捐赠符文添加物品出错2");
				return false;
			}						
		}
		item.bindAndNotify();//设置物品为绑定
		logger.info("玩家roleid "+selfroleid+"\t公会key\t"+clanInfo.getKey()+"捐赠一个符文，物品id "+itemid+"\t消耗类型"+selecttype+"\t值"+itemid+"\t目标玩家id  "+targetid);
		
		
		SRuneGive sRuneGive=new SRuneGive();
		sRuneGive.givevalue=prop.getEnergy();
		Procedure.psendWhileCommit(selfroleid, sRuneGive);
		//刷新一下捐赠信息列表
		mkdb.Procedure.pexecuteWhileCommit(sendCRequestRuneInfo(selfroleid));
		
		ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(itemid);
		xbean.Properties proptarger = xtable.Properties.select(targetid);
		
		fire.pb.course.CourseManager.achieveUpdate(selfroleid, fire.pb.course.CourseType.GONG_HUI_JUAN_ZHU);
		
		// 通知公会中的兄弟
		if(iAttr!=null){
			MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 160370,0,Arrays.asList(proptarger.getRolename(),iAttr.getName(),prop.getRolename()));
		}
		
		
		return true;
	}
	/**
	 * 玩家请求符文
	 * @param roleid
	 * @param runerequestinfolist
	 * @return
	 */
	public static boolean sendCRuneRequest(long roleid,java.util.ArrayList<fire.pb.clan.RuneRequestInfo> runerequestinfolist){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		//判断客户端发送的数据是否正确
		if(runerequestinfolist.size()==0||runerequestinfolist.size()>sRuneSet.size()){
			logger.info("客户端请求符文信息，发送的数据错误,角色id "+roleid);
			return false;
		}
		//判断是否已经请求过了
		xbean.RequestRuneInfoList requestRuneInfoList= xtable.Requestruneinfotab.get(clanInfo.getKey());
		if(requestRuneInfoList==null){
			requestRuneInfoList=xbean.Pod.newRequestRuneInfoList();
			xtable.Requestruneinfotab.insert(clanInfo.getKey(), requestRuneInfoList);
		}
		
		//判断是否有这个请求的物品(这里就不判断物品是否重复了)
		for(RuneRequestInfo runeRequestInfo:runerequestinfolist){
			if(!sRuneSet.containsKey(runeRequestInfo.itemid)){
				logger.info("客户端请求符文信息，发送的数据错误,角色id "+roleid);
				return false;
			}
		}
		xbean.RuneInfo runeinfo=xtable.Runeinfotab.get(roleid);
		if(runeinfo==null){
			runeinfo=xbean.Pod.newRuneInfo();
			xtable.Runeinfotab.insert(roleid, runeinfo);
		}
		//判断是否已经达到在今天上限
		int max=0;
		SCommon conf = ClanUtils.commonMap.get(207);
		if (conf != null) {
			max= Integer.parseInt(conf.getValue());
		}else{
			return false;
		}
		if(runeinfo.getDayrequestnum()+runerequestinfolist.size()>max){
			MessageMgr.sendMsgNotify(roleid, 160255, null);
			logger.info("客户端请求符文信息，请求次数超出上限,角色id "+roleid);
			return false;
		}
		xbean.Properties prop = xtable.Properties.select(roleid);
		
		//处理这个主要是为了验证客户端发送的数据是否正确
		for(RuneRequestInfo runeRequestInfo:runerequestinfolist){
			for(xbean.RequestRuneInfo request:requestRuneInfoList.getRequestrunelists()){
				if(request.getRoleid()==roleid&&request.getItemid()==runeRequestInfo.itemid&&request.getActiontype()==ACTION_TYPE_REQUEST){
					//已经存在同样的请求
					logger.info("客户端请求符文信息，发送的数据错误,已经存在同样的请求，角色id "+roleid);
					return false;
				}
			}
		}
		runeinfo.setDayrequestnum(runeinfo.getDayrequestnum()+runerequestinfolist.size());//设置今天请求次数
		
		for(RuneRequestInfo runeRequestInfo:runerequestinfolist){
			xbean.RequestRuneInfo request=xbean.Pod.newRequestRuneInfo();
			request.setRoleid(roleid); // 请求角色roleid
			request.setActiontype(ACTION_TYPE_REQUEST); // 动作类型  0 请求符文    1捐献符文
			request.setRequesttime(System.currentTimeMillis()); // 请求时间
			request.setItemid(runeRequestInfo.itemid); // 物品id
			request.setItemlevel(0); // 物品等级
			requestRuneInfoList.getRequestrunelists().add(request);
			ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(runeRequestInfo.itemid);
			// 通知公会中的兄弟
			if(iAttr!=null){
				MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 160369,0,Arrays.asList(prop.getRolename(),iAttr.getName()));
			}
		}
		
		SRuneRequest sRuneRequest=new SRuneRequest();
		sRuneRequest.requestnum=runeinfo.getDayrequestnum();//今天请求次数
		
		java.util.ArrayList<fire.pb.clan.RuneRequestInfo> list=new ArrayList<RuneRequestInfo>();
		for(xbean.RequestRuneInfo request:requestRuneInfoList.getRequestrunelists()){
			if(request.getRoleid()==roleid&&request.getActiontype()==ACTION_TYPE_REQUEST){
				RuneRequestInfo info=new RuneRequestInfo();
				info.itemid=request.getItemid(); // 物品id
				list.add(info);
			}
		}
		sRuneRequest.runerequestinfolist=list;
		
		Procedure.psendWhileCommit(roleid, sRuneRequest);
		
		MessageMgr.sendMsgNotify(roleid, 160254, null);
		
		logger.info("玩家roleid "+roleid+"\t公会key\t"+clanInfo.getKey()+"请求符文，请求数量 "+runerequestinfolist.size()+"\t当前次数 "+runeinfo.getDayrequestnum());
		return true;
	}
	/**
	 * 返回请求符文界面
	 * @param roleid
	 * @return
	 */
	public static boolean sendCRuneRequestView(long roleid){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		Procedure.pexecute(ClanManage.clearTimeAlived(clanInfo.getKey()));//清除公会符文请求
		
		SRuneRequestView sRuneRequest=new SRuneRequestView();
		int num=0;
		xbean.RequestRuneInfoList requestRuneInfoList= xtable.Requestruneinfotab.select(clanInfo.getKey());
		if(requestRuneInfoList!=null){
			java.util.ArrayList<fire.pb.clan.RuneRequestInfo> list=new ArrayList<RuneRequestInfo>();
			for(xbean.RequestRuneInfo request:requestRuneInfoList.getRequestrunelists()){
				if(request.getRoleid()==roleid&&request.getActiontype()==ACTION_TYPE_REQUEST){
					RuneRequestInfo info=new RuneRequestInfo();
					info.itemid=request.getItemid(); // 物品id
					list.add(info);
				}
			}
			sRuneRequest.runerequestinfolist=list;
		}
		
		xbean.RuneInfo runeinfo=xtable.Runeinfotab.select(roleid);
		if(runeinfo!=null){
			num=runeinfo.getDayrequestnum();//今天请求次数
			sRuneRequest.requestnum=num;//今天请求次数
		}
		Procedure.psendWhileCommit(roleid, sRuneRequest);
		return true;
	}
	/**
	 * 发送公会红点信息
	 * @param roleId
	 */
	public static mkdb.Procedure sendClanRedTip(long roleId) {
		return new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				int value=0;
				Long factionId = xtable.Roleidclan.select(roleId);
				if (factionId != null && factionId > 0) {
					xbean.ClanInfo clanInfo = xtable.Clans.select(factionId);
					//判断是不是公会会长或者副会长
					if(roleId!=clanInfo.getClanvicemaster()&&roleId!=clanInfo.getClanmaster()){
						return false;
					}
					if(clanInfo.getApplicants().size()>0){
						value=1;
					}
				}
				SClanRedTip sClanRedTip=new SClanRedTip();
				sClanRedTip.redtips.put(SClanRedTip.TYPE_APPLY, value);
				Procedure.psendWhileCommit(roleId, sClanRedTip);
				return true;
			}
		};
	}
	/**
	 * 给客户端刷新一下帮贡
	 * @param roleId
	 * @param currvalue
	 */
	public static void sendRefreshCurrentClanPoint(long roleId,int currvalue){
		//发送给客户端更新帮贡信息
		SRefreshContribution sRefreshContribution=new SRefreshContribution();
		sRefreshContribution.currentcontribution=currvalue;
		Procedure.psendWhileCommit(roleId, sRefreshContribution);
		
		SRefreshRoleCurrency send = new SRefreshRoleCurrency();
		send.datas.put(RoleCurrency.GUILD_DED, (long)currvalue);
		Procedure.psendWhileCommit(roleId, send);
	}
	/**
	 * 更新发送玩家公会key
	 * @param roleid
	 */
	public static void sendUpdateRoleClanKey(long roleid){
		mkdb.Procedure.pexecuteWhileCommit(new PRefreshRoleClanKey(roleid));
	}

	/**
	 * 在玩家加入公会后, 创建公会成员信息.
	 * 顺便完成一个逻辑, 清空玩家之前的其它公会申请列表
	 * @return 公会成员信息
	 */
	protected xbean.ClanMemberInfo createClanMemeberInfo(long roleId, xbean.Properties prop, int position) {
		xbean.ClanMemberInfo fmi = xbean.Pod.newClanMemberInfo();
		fmi.setClanposition(position);
		fmi.setJointime(System.currentTimeMillis());
		fmi.setOwnbonus(0);//清除可领取的公会分红
		fmi.setTasknum(0);
		// 删除玩家公会申请列表
		xtable.Roleapplyclantab.remove(roleId);
		
		return fmi;
	}
	/**
	 * 创建角色和公会对应关系表
	 * one to one
	 * @param roleId  角色id
	 * @param clankey  公会key
	 */
	protected void createRoleToClan(long roleId, long clankey) {
		xtable.Roleidclan.remove(roleId);
		xtable.Roleidclan.insert(roleId, clankey);
	}
	
	/**
	 * 成员离开公会处理
	 * @param roleId
	 * @param position
	 * @param reason 哪里退出的。1自动离开公会  2被开除公会   3公会解散
	 */
	protected void exitClan(long roleId,long masterId, xbean.ClanInfo claninfo, int reason) {
		
		xbean.ClanMemberInfo fmi = claninfo.getMembers().get(roleId);
		int position = fmi.getClanposition();
		//称谓处理
		ClanUtils.removeClanTitleByPosition(roleId, position);
//		//主动和被动离开公会对应职位员数-1
//		if (reason == NORMAL_LEAVE_REASON || reason == FIRE_LEAVE_REASON) {
			claninfo.getMembers().remove(roleId);
//		}
		
		//个人帮派key属性处理
		xbean.Properties prop = xtable.Properties.get(roleId);
		prop.setClankey(0);
		
		//设置玩家退出状态,用于处理帮贡
		if (reason == NORMAL_LEAVE_REASON) {
			prop.setExitstate(ClanManage.NORMAL_LEAVE_REASON);
		} else if (reason == FIRE_LEAVE_REASON) {
			prop.setExitstate(ClanManage.STATE_KITE);
		} else if (reason == DISMISS_LEAVE_REASON) {
			prop.setExitstate(ClanManage.STATE_DISMISS);
		}
		//设置原公会key
		prop.setOldclankey(claninfo.getKey());
		xtable.Roleidclan.remove(roleId);
		
		// 通知
		if (reason == NORMAL_LEAVE_REASON) {
			Procedure.psendWhileCommit(roleId, new SLeaveClan(roleId));
		} else if (reason == FIRE_LEAVE_REASON || reason == DISMISS_LEAVE_REASON) {
			SFireMember sFireMember = new SFireMember();
			sFireMember.memberroleid = roleId;
			if(masterId>0){
				Procedure.psendWhileCommit(masterId, sFireMember);
			}
			Procedure.psendWhileCommit(roleId, sFireMember);
		}
		
		//玩家公会改变需要重新计算评分 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(roleId));
		claninfo.setTotalscore(claninfo.getTotalscore() - prop.getRolezonghemaxscore()); //公会综合实力评分变化 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PFactionZongHeProc(claninfo.getKey(), false));		
		mkdb.Procedure.pexecuteWhileCommit(new PClanUpdateMemberNum(claninfo.getKey(), claninfo.getMembers().size()));

		//放弃公会任务
		mkdb.Procedure.pexecuteWhileCommit(new PAbandonCircleTask(roleId, 1060000));	
		//这里统一处理踢出公会地图逻辑
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.clan.PUpdateMemberDataProc(roleId));
		
		ClanManage.sendUpdateRoleClanKey(roleId);//更新玩家加入公会信息
		
	}

	/**
	 * @return 返回公会改名扣取符石数量,获取失败返回-1
	 */
	public int getChangeClanNameCostHearthStone() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(171);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		logger.error("获取公会改名扣取符石常量错误");
		return -1;
	}
	
	/**
	 * @return 返回创建公会等级,获取失败返回-1
	 */
	protected int getCreateClanLevel() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(163);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		logger.error("获取创建公会等级常量错误");
		return -1;
	}
	
	/**
	 * @return 返回创建公会金额,获取失败返回-1
	 */
	protected int getCreateClanMoney() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(161);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		logger.error("获取创建公会金额常量错误");
		return -1;
	}
	
	/**
	 * @return 返回创建公会金额,获取失败返回-1
	 */
	protected int getCreateClanFushi() {
		fire.pb.fushi.SCommonDayPay daypay = ConfigManager.getInstance().getConf(fire.pb.fushi.SCommonDayPay.class).get(5);
		if(daypay!=null){
			return daypay.serverdata;
		}
		logger.error("获取创建公会点卡服金额常量错误");
		return -1;
	}
	
	
	/**
	 * @return 返回加入公会等级,获取失败返回-1
	 */
	public int getJoinClanLevel() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(162);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		logger.error("获取加入公会等级常量错误");
		return -1;
		
//		int level = 0;
//		SNewFunctionOpen curFunc = ConfigManager.getInstance().getConf(fire.pb.mission.SNewFunctionOpen.class).get(5);
//		if(curFunc != null){
//			level = (curFunc.getLvtrig() > 0) ? curFunc.getLvtrig() : 0;
//		}
//		return level;
		
	}
	
	
	/**
	 * @return 返回一键申请冻结时间,获取失败返回-1
	 */
	public int getOnekeyTime() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(182);
			if (conf != null) {
				return Integer.parseInt(conf.getValue()) * 1000;
			}
		}
		logger.error("获取一键申请冻结时间常量错误");
		return -1;
	}
	
	/**
	 * 获得开除出公会成员事由
	 * @param reasonType  事由类型
	 * @return
	 */
	protected String getReasonContent(int reasonType) {
		switch (reasonType) {
		case FireReasonType.CLEAN_MEMBER:
			return StringConstant.ID2String("人员清理", StringConstant.人员清理);
		case FireReasonType.LONG_TIME_OFF_LINE:
			return StringConstant.ID2String("长期不上线", StringConstant.长期不上线);
		case FireReasonType.NO_ACTIVE:
			return StringConstant.ID2String("表现不积极", StringConstant.表现不积极);
		case FireReasonType.NO_INTEREST:
			return StringConstant.ID2String("志趣不合", StringConstant.志趣不合);
		}
		return "";
	}
	
	/**
	 * @return 返回群发消息扣除银币数量,获取失败返回-1
	 */
	protected int getSendMsgCostMoney() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(174);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		logger.error("获取群发消息扣除银币常量错误");
		return -1;
	}
	
	/**
	 * 添加新成员加入公会初始化
	 * @param roleId   成员id
	 * @param properties  成员属性对象
	 * @param clanInfo 公会信息
	 * @param flag true表示加入的职位为成员
	 * @return 
	 */
	protected xbean.ClanMemberInfo joinClanMember(long roleId, xbean.Properties properties,
			xbean.ClanInfo clanInfo,boolean flag) {
		// 清理角色id和公会对应表的数据
		createRoleToClan(roleId, clanInfo.getKey());
		clearAllInfoAddandCreate(roleId);//处理所有的在玩家加入公会或者创建公会时候，需要清除上次的数据
		properties.setClankey(clanInfo.getKey());
		// 添加公会成员信息
		int addposition=ClanPositionType.ClanMember;
		
		xbean.ClanMemberInfo fmi = createClanMemeberInfo(roleId, properties, addposition);
		clanInfo.getMembers().put(roleId, fmi);
		
		clanInfo.setTotalscore(clanInfo.getTotalscore() + properties.getRolezonghemaxscore()); //公会综合实力评分增加 by changhao

		// 新入会成员是否在申请列表中,如果在清除
		if (clanInfo.getApplicants().containsKey(roleId)) {
			clanInfo.getApplicants().remove(roleId);
		}
		// 新入会成员自身申请列表清空
		xbean.RoleApplyClanList roleApplyClanList = xtable.Roleapplyclantab.get(roleId);
		if (roleApplyClanList != null) {
			roleApplyClanList.getApplymap().clear();
			roleApplyClanList.getOnekeyapplymap().clear();
		}
		// 添加公会称谓
		ClanUtils.addClanTitleByPosition(roleId, clanInfo.getClanname(),fmi.getClanposition());
		mkdb.Procedure.pexecuteWhileCommit(new PClanUpdateMemberNum(clanInfo.getKey(), clanInfo.getMembers().size()));
		return fmi;
	}
	
	private void packagingMemberToBean(xbean.ClanInfo clanInfo, java.util.ArrayList<ClanMember> memberlist) {
		for (Map.Entry<Long, xbean.ClanMemberInfo> memberEntry : clanInfo.getMembers().entrySet()) {
			memberlist.add(packagingToClanMember2(memberEntry.getValue(),memberEntry.getKey()));
		}
	}
	
	/**
	 * @return 公会成员返回协议封装
	 */
	protected ClanMember packagingToClanMember2(xbean.ClanMemberInfo memberEntry,long roleid) {
		PropRole propRole = new PropRole(roleid, true);
		ClanMember fm = new ClanMember();
		fm.roleid = roleid;
		fm.rolename = propRole.getName();
		//如果玩家下线，在保护时间内也需要显示为在线
		if (StateCommon.isOnlineBuffer(fm.roleid)) {
			fm.lastonlinetime = 0;
		} else {
			fm.lastonlinetime = (int) (propRole.getLastOfflineTime()/1000);
		}
		fm.rolelevel = (short) propRole.getLevel();
		fm.position = (byte) memberEntry.getClanposition();
		fm.school = (byte) propRole.getSchool();
		fm.jointime = (int) ((System.currentTimeMillis() - memberEntry.getJointime())/1000);
		//处理一下禁言到时间(这里公会信息可能是select获得的，所有无法改变值，但是不会影响这里的逻辑)
		if(memberEntry.getBannedtalktime()-System.currentTimeMillis()<0){
			memberEntry.setIsbannedtalk(false);
			memberEntry.setBannedtalktime(0L);
		}
		fm.isbannedtalk = (byte) (memberEntry.getIsbannedtalk() == true ? 1 : 0);
		fm.shapeid = propRole.getShape();
		//公会贡献度相关
		xbean.RoleClanPoint roleCon = ClanUtils.getRoleClanPoint(fm.roleid, true);
		if(roleCon!=null){
			fm.rolecontribution=roleCon.getCurrentclanpoint(); // 公会贡献度
			fm.weekcontribution=memberEntry.getWeekclanpoint(); // 本周公会贡献度
			fm.historycontribution=roleCon.getHistoryclanpoint(); // 历史公会贡献度
			fm.rolefreezedcontribution=roleCon.getFreezedclanpoint(); // 冻结的帮派贡献度
			fm.preweekcontribution=memberEntry.getPreweekclanpoint();
		}
		fm.weekaid=(short) memberEntry.getClanhelpnum();//本周援助
		xbean.helpcount helpcount = xtable.Helpcount.select(roleid);
		if(helpcount!=null){
			fm.historyaid=helpcount.getClanhishelpnum();//历史援助
		}
		fm.fightvalue = propRole.getZhongheScore(); // 综合战力
		fm.claninstnum = memberEntry.getClaninstnum(); // 参加公会副本次数
		fm.clanfightnum = memberEntry.getClanfightnum(); // 参加公会战次数
		
		return fm;
	}


	/**
	 * 返回公会信息
	 * @param clanInfo
	 * @return
	 */
	protected SOpenClan packagingToProtocol(xbean.ClanInfo clanInfo) {
		SOpenClan sof = new SOpenClan();
		sof.index = clanInfo.getIndex();
		sof.clanname = clanInfo.getClanname();
		sof.clanid = clanInfo.getKey();
		sof.clanlevel = clanInfo.getClanlevel();
		sof.membersnum = clanInfo.getMembers().size();
		String clancreatorname=xtable.Properties.selectRolename(clanInfo.getCreator());
		if(clancreatorname!=null){
			sof.clancreator = clancreatorname;
		}
		String clanmastername=xtable.Properties.selectRolename(clanInfo.getClanmaster());
		if(clanmastername!=null){
			sof.clanmaster = clanmastername;
		}
		sof.clanaim = clanInfo.getClanaim();
		sof.masterid = clanInfo.getClanmaster();
		sof.vicemasterid = clanInfo.getClanvicemaster();
		sof.money = clanInfo.getMoney(); //公会资金 by changhao
		sof.house.putAll(clanInfo.getHouse()); //公会房子情况 by changhao
		sof.oldclanname = clanInfo.getOldclanname();
		sof.clancreatorid = clanInfo.getCreator();// 公会创始人id
		java.util.ArrayList<ClanMember> memberlist = new java.util.ArrayList<ClanMember>(64);
		this.packagingMemberToBean(clanInfo, memberlist);
		sof.memberlist = memberlist;
		sof.autostate = clanInfo.getAutostate();
		sof.requestlevel = clanInfo.getRequestlevel();
		sof.costeverymoney=ClanUtils.getCostClanDTMoney(clanInfo);//公会维护需要的资金
		sof.costmax.putAll(ClanUtils.getBuildLevelupMax(clanInfo));//公会不同建筑维护需要的最大资金
		sof.claninstservice = ClanUtils.getClanInstOpen(clanInfo);
		
		return sof;
	}
	
	/**
	 * 发送公会信息
	 * @param roleid
	 * @param clanInfo
	 * @return
	 */
	protected void sendClanInfoToRole(long roleid, long clanId) {
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.ClanInfo clanInfo = xtable.Clans.select(clanId);
				mkdb.Procedure.psendWhileCommit(roleid, packagingToProtocol(clanInfo));
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
	}
	
	/**
	 * 封装成刷新公会成员列表的协议
	 * @param clanInfo
	 * @return
	 */
	protected SRefreshMemberList packagingToSRefreshMemberListProtocol(xbean.ClanInfo clanInfo) {
		SRefreshMemberList sRefreshMemberList = new SRefreshMemberList();
		java.util.ArrayList<ClanMember> memberlist = new java.util.ArrayList<ClanMember>(64);
		this.packagingMemberToBean(clanInfo, memberlist);
		sRefreshMemberList.memberlist = memberlist;
		return sRefreshMemberList;
	}
	
	/**
	 * 返回角色公会申请状态
	 * @param roleId
	 */
	protected void retApplyClanState(long roleId) {
		xbean.RoleApplyClanList roleApplyClanList = xtable.Roleapplyclantab.select(roleId);
		SApplyClanList sApplyClanList = new SApplyClanList();
		if (null != roleApplyClanList && roleApplyClanList.getApplymap().size() > 0) {
			for (Map.Entry<Long, xbean.RoleApplyClan> entry : roleApplyClanList.getApplymap().entrySet()) {
				xbean.RoleApplyClan roleApplyClan = entry.getValue();
				ApplyClan applyFaction = new ApplyClan();
				applyFaction.clankey = roleApplyClan.getClankey();
				applyFaction.applystate = roleApplyClan.getState();
				sApplyClanList.applyclanlist.add(applyFaction);
			}
		}
		if (null != roleApplyClanList && roleApplyClanList.getOnekeyapplymap().size() > 0) {
			for (Map.Entry<Long, xbean.RoleApplyClan> entry : roleApplyClanList.getOnekeyapplymap().entrySet()) {
				xbean.RoleApplyClan roleApplyClan = entry.getValue();
				ApplyClan applyFaction = new ApplyClan();
				applyFaction.clankey = roleApplyClan.getClankey();
				applyFaction.applystate = roleApplyClan.getState();
				sApplyClanList.applyclanlist.add(applyFaction);
			}
		}
		
		// 返回客户端
		Onlines.getInstance().send(roleId, sApplyClanList);
	}
	
	/**
	 * 获得公会成员本周贡献
	 * @param roleId
	 * @param clanInfo
	 * @return
	 */
	public static int getWeekclanpoint(long roleId, xbean.ClanInfo clanInfo){
		for(Map.Entry<Long, xbean.ClanMemberInfo> entry : clanInfo.getMembers().entrySet()){
			if(entry.getKey()==roleId){
				xbean.ClanMemberInfo clanMemberInfo=entry.getValue();
				return clanMemberInfo.getWeekclanpoint();
			}
		}
		return 0;
	}
	
	/**
	 * 添加参加公会战次数
	 * @param roleid
	 * @param addnum
	 */
	public static void addClanFightNum(long roleid,int addnum){
		mkdb.Procedure proc=new mkdb.Procedure(){
			@Override
			protected boolean process() {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
				if (clanInfo == null) {
					return false;
				}
				xbean.ClanMemberInfo clanMemberInfo=clanInfo.getMembers().get(roleid);
				if(clanMemberInfo==null){
					return false;
				}
				if(DateValidate.inTheSameDay(clanMemberInfo.getClanfightnumtime(), System.currentTimeMillis())){
					return false;
				}
				clanMemberInfo.setClanfightnum((short) (clanMemberInfo.getClanfightnum()+addnum));
				clanMemberInfo.setClanfightnumtime(System.currentTimeMillis());
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
	}
	
	/**
	 * 添加参加公会副本次数
	 * @param roleid
	 * @param addnum
	 */
	public static void addClanInstNum(long roleid,int addnum){
		mkdb.Procedure proc=new mkdb.Procedure(){
			@Override
			protected boolean process() {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
				if (clanInfo == null) {
					return false;
				}
				xbean.ClanMemberInfo clanMemberInfo=clanInfo.getMembers().get(roleid);
				if(clanMemberInfo==null){
					return false;
				}
				if(DateValidate.inTheSameDay(clanMemberInfo.getClaninstnumtime(), System.currentTimeMillis())){
					return false;
				}
				clanMemberInfo.setClaninstnum((short) (clanMemberInfo.getClaninstnum()+addnum));
				clanMemberInfo.setClaninstnumtime(System.currentTimeMillis());
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
	}
	
	
}

