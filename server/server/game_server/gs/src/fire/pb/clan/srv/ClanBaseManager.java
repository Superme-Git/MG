package fire.pb.clan.srv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import fire.log.beans.FactionOpbean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.clan.ClanEventInfo;
import fire.pb.clan.ClanMember;
import fire.pb.clan.ClanPositionType;
import fire.pb.clan.ClanSummaryInfo;
import fire.pb.clan.ClanUtils;
import fire.pb.clan.MedicItem;
import fire.pb.clan.Module;
import fire.pb.clan.PRequestImpeachMent;
import fire.pb.clan.RoleInfoDes;
import fire.pb.clan.SAcceptApply;
import fire.pb.clan.SBonusQuery;
import fire.pb.clan.SCancelApplyClan;
import fire.pb.clan.SClanRedTip;
import fire.pb.clan.SLeaveClan;
import fire.pb.clan.SOpenClan;
import fire.pb.clan.SOpenClanList;
import fire.pb.clan.SOpenClanMedic;
import fire.pb.clan.SRefreshMemberList;
import fire.pb.clan.SRefreshPosition;
import fire.pb.clan.SRefuseApply;
import fire.pb.clan.SRequestEventInfo;
import fire.pb.clan.SRequestRoleInfo;
import fire.pb.event.FactionCreateOrJoinEvent;
import fire.pb.item.Pack;
import fire.pb.map.SceneManager;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.talk.MessageMgr;
import fire.pb.team.TeamManager;
import fire.pb.util.StringConstant;
import mkdb.Procedure;
import mkdb.util.UniqName;


public class ClanBaseManager extends ClanManage {
	public static final int BUILD_FACTION_INIT_MONEY=1000000;//创建公会给是初始资金数
	
	private static class BaseManagerHolder {
		static ClanBaseManager instance = new ClanBaseManager();
	}
	
	public static ClanBaseManager getInstance() {
		return BaseManagerHolder.instance;
	}
	
	private ClanBaseManager() {
		
	}
	
	/**
	 * 添加到申请列表
	 * @param roleId
	 * @param claninfo
	 * @param isOneKey
	 */
	public void addApplyClanList(long roleId,xbean.ClanInfo claninfo,boolean isOneKey){
		xbean.RoleApplyClanList roleApplyClanList = xtable.Roleapplyclantab.get(roleId);
		if (null == roleApplyClanList) {
			roleApplyClanList = xbean.Pod.newRoleApplyClanList();
			xtable.Roleapplyclantab.insert(roleId, roleApplyClanList);
		}
		// 添加到角色申请公会列表中
		long currTime = System.currentTimeMillis();
		xbean.RoleApplyClan roleApplyClan = xbean.Pod.newRoleApplyClan();
		roleApplyClan.setClankey(claninfo.getKey());
		roleApplyClan.setState(1);
		roleApplyClan.setApplytiem(currTime);
		roleApplyClanList.getApplymap().put(claninfo.getKey(), roleApplyClan);
		
		// 添加到公会申请列表
		claninfo.getApplicants().put(roleId, System.currentTimeMillis());
		// 同步申请状态
		retApplyClanState(roleId);
		if (!isOneKey) {
			// 返回消息
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 145009,
					Arrays.asList(claninfo.getClanname()));
		}
		//提示红点
		SClanRedTip sClanRedTip=new SClanRedTip();
		if(claninfo.getClanmaster()>0){
			sClanRedTip.redtips.put(SClanRedTip.TYPE_APPLY, 1);
			Procedure.psendWhileCommit(claninfo.getClanmaster(), sClanRedTip);
		}
		if(claninfo.getClanvicemaster()>0){
			sClanRedTip.redtips.put(SClanRedTip.TYPE_APPLY, 1);
			Procedure.psendWhileCommit(claninfo.getClanvicemaster(), sClanRedTip);
		}
	}
	
	/**
	 * 玩家加入公会的统一处理逻辑
	 * @param masterid
	 * @param applyroleid
	 * @param properties
	 * @param claninfo
	 * @param flag  true表示加入的成员职位为成员
	 */
	public void addNewClanMember(long masterid, long applyroleid,xbean.Properties properties,xbean.ClanInfo claninfo,boolean flag){
		// 添加新成员
		xbean.ClanMemberInfo fmi = joinClanMember(applyroleid, properties, claninfo,flag);
		ClanMember fm = packagingToClanMember2(fmi,applyroleid);
		
		// 添加公会称谓
		ClanUtils.addClanTitleByPosition(applyroleid,claninfo.getClanname(), fmi.getClanposition());
		// 通知公会会长
		Procedure.psendWhileCommit(masterid, new SAcceptApply(fm));
		
		// 通知公会中的兄弟有新人加入
		MessageMgr.psendMsgNotifyWhileCommit(claninfo.getMembers().keySet(), 145069, 0,
				Arrays.asList(properties.getRolename()));
		// 通知申请者
		sendClanInfoToRole(applyroleid,claninfo.getKey());
		
		//玩家公会改变需要重新计算评分 by changhao
		Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(applyroleid));
		
		mkdb.Procedure.pexecuteWhileCommit(new PClanUpdateMemberNum(claninfo.getKey(), claninfo.getMembers().size()));
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PFactionZongHeProc(claninfo.getKey(), false));
		
		MessageMgr.psendSystemMessageToRole(applyroleid, 160186, Arrays.asList(claninfo.getClanname()));
		//通过申请加入公会后屏幕中间没有系统提示160332
		MessageMgr.psendMsgNotifyWhileCommit(applyroleid, 160332, Arrays.asList(claninfo.getClanname()));

		mkdb.Procedure.pexecuteWhileCommit(
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						TeamManager.getTeamByRoleId(applyroleid);//强行锁队伍
						fire.pb.event.Poster.getPoster().dispatchEvent(new FactionCreateOrJoinEvent(applyroleid));
						return true;
					}
			} );
		
		
		ClanManage.addClanDealContribution(applyroleid,claninfo.getKey());//处理加入公会，处理帮贡逻辑
		//处理帮派事件
		ClanManage.addDealClanEventInfo(claninfo,ClanManage.EVENT_ADD,applyroleid,properties.getRolename(),"","");
		
		//运营日志
		FactionOpbean factionOpbean = new FactionOpbean(claninfo.getKey(), claninfo.getClanname(), 
				FactionOpbean.TYPE_ADD, fmi.getClanposition(), claninfo.getMembers().size(),
				claninfo.getClanlevel(), claninfo.getHouse().entrySet());
		int weekpoint=fire.pb.clan.srv.ClanManage.getWeekclanpoint(applyroleid,claninfo);
		fire.log.YYLogger.factionOPEventLog(applyroleid,factionOpbean,weekpoint);
		
		ClanManage.sendUpdateRoleClanKey(applyroleid);//更新玩家加入公会信息
		logger.info("roleId "+applyroleid+"\t公会key\t"+claninfo.getKey()+"\t加入公会\t");
	}
	
	
	
	/**
	 * 2.申请加入公会
	 * @param roleId
	 * @param clankey
	 * @param isOneKey true是一件申请调用，false其它操作调用
	 * @return 成功true
	 */
	public boolean applyClan(long roleId, long clankey, boolean isOneKey) {
		xbean.ClanInfo clanInfo = xtable.Clans.get(clankey);
		if (clanInfo == null) {
			logger.error("申请加入的公会不存在！");
			return false;
		}
		xbean.Properties properties = xtable.Properties.get(roleId);
		if (properties.getLevel() < getJoinClanLevel()) {
			MessageMgr.sendMsgNotify(roleId, 150137, Arrays.asList(getJoinClanLevel()+""));
			return false;
		}
		
		if (properties.getClankey() > 0) {
			logger.error("请求申请入会的角色,已经有公会！");
			return false;
		}

		
		if (clanInfo.getApplicants().containsKey(roleId)) {
			// 已经申请过了,这里如果是一键申请的，不需要提升这个消息
			if(isOneKey==false){
				MessageMgr.sendMsgNotify(roleId, 145008, null);
			}
			return false;
		}
		
		
		// 是否开启角色申请自动入会
		boolean auto = clanInfo.getAutostate() == 0 ? false : true;
		if (!auto) {
			addApplyClanList(roleId,clanInfo,isOneKey);//添加到申请列表
		} else {
			//过滤等级
			if(properties.getLevel()<clanInfo.getRequestlevel()){
				//直接放入申请列表
				addApplyClanList(roleId,clanInfo,isOneKey);//添加到申请列表
				return true;
			}
			// 判断公会是否满员  /判断这个职位是否已经满了
			if (ClanUtils.isClanMemberFull(clanInfo)||ClanUtils.isAddClanPositionFull(clanInfo)) {
				//直接放入申请列表
				addApplyClanList(roleId,clanInfo,isOneKey);//添加到申请列表
				return true;
			}
			addNewClanMember(clanInfo.getClanmaster(),roleId,properties,clanInfo,false);//添加新成员统一处理加入公会逻辑
		}
		return true;
	}
	
	/**
	 * 取消公会申请
	 * @param roleId 
	 * @param clankey
	 * @return
	 */
	public boolean cancelApply(long roleId, long clankey) {
		// 删除xdb的
		xbean.ClanInfo clanInfo = xtable.Clans.get(clankey);
		if (null == clanInfo) {
			return false;
		}
		
		clanInfo.getApplicants().remove(roleId);
		
		// 删除角色自身的
		xbean.RoleApplyClanList roleApplyClanList = xtable.Roleapplyclantab.get(roleId);
		if (null == roleApplyClanList) {
			return false;
		}
		
		if (roleApplyClanList.getApplymap().size() > 0) {
			roleApplyClanList.getApplymap().remove(clankey);
		}
		
		if (roleApplyClanList.getOnekeyapplymap().size() > 0) {
			roleApplyClanList.getOnekeyapplymap().remove(clankey);
		}
		
		Procedure.psendWhileCommit(roleId, new SCancelApplyClan(clankey));
		
		return true;
	}
	
	
	
	/**
	 * 创建公会
	 * @param roleId 创建角色id
	 * @param clanname 公会名称
	 * @param clanaim  公会宗旨
	 * @return 成功true
	 */
	public boolean createClan(long roleId, String clanname, String clanaim) {
		PropRole propRole = new PropRole(roleId, true);
		if (propRole.getLevel() < getCreateClanLevel()) {
			MessageMgr.sendMsgNotify(roleId,145004, null);
			return false;
		}
		//判断当前公会是否已经达到上限
		int max=getMaxCreateClanNum();
		if(max==-1){
			MessageMgr.sendMsgNotify(roleId, 164000, null);
			return false;
		}
		if(Module.clanListMap.values().size()>=max){
			MessageMgr.sendMsgNotify(roleId, 164000, null);
			return false;
		}
		Long factionId = xtable.Roleidclan.select(roleId);
		if (factionId != null && factionId > 0) {
			return false;
		}
		if (!UniqName.allocate("clan", clanname)) {
			MessageMgr.sendMsgNotify(roleId, 142402, null);
			return false;
		}
		
		//判断是否是点卡服，点卡服消耗符石，非点卡服消耗金币
		if(fire.pb.fushi.Module.GetPayServiceType() == 1){//点卡服务器
			int fushinum=getCreateClanFushi();
			if(fushinum<=0){
				return false;
			}
			//扣除符石
			if (!fire.pb.fushi.FushiManager.subFushiFromUser(propRole.getProperties().getUserid(), roleId, fushinum, 0, 0,fire.pb.fushi.FushiConst.REASON_CREATE_CLAN,
					YYLoggerTuJingEnum.tujing_Value_createclan,true)) {
				return false;
			}
			logger.info("角色id"+roleId+"创建公会扣除符石"+fushinum);
		}else{
			int gold=getCreateClanMoney();
			//判断创建公会的钱是否够
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
			if(bag.getGold()<gold){
				MessageMgr.sendMsgNotify(roleId, 145005, null);
				return false;
			}
			if (bag.subGold(-gold, "创建公会", YYLoggerTuJingEnum.tujing_Value_createclan, 0) != -gold) {
				MessageMgr.sendMsgNotify(roleId, 145005, null);
				return false;
			}
			logger.info("角色id"+roleId+"创建公会扣除金币"+gold);
		}
		xbean.ClanInfo factionInfo = xbean.Pod.newClanInfo();
		long factionKey = xtable.Clans.insert(factionInfo);

		factionInfo.setKey(factionKey);
		factionInfo.setOldclanname(clanname);
		factionInfo.setClanname(clanname);
		factionInfo.setClanaim(clanaim);
		factionInfo.setClanlevel(1);
		factionInfo.setClanmaster(roleId);
		factionInfo.setPopularity(0);
		factionInfo.setCreator(roleId);
		factionInfo.setCreatetime(System.currentTimeMillis());
		factionInfo.setClanrank(-1);
		factionInfo.setMoney(BUILD_FACTION_INIT_MONEY); //设置初始公会资金
		factionInfo.getHouse().put(ClanManage.GoldBank, 0); //金库 初始0级 by changhao
		factionInfo.getHouse().put(ClanManage.DrugStone, 0); //药房 初始0级 by changhao
		factionInfo.getHouse().put(ClanManage.Hotel, 0); //旅馆 初始0级 by changhao
		factionInfo.setBonus(0); //公会分红 by changhao
		factionInfo.setAutostate(0);//1表示自动开启   0表示关闭
		factionInfo.setRequestlevel((short) 1);
		factionInfo.setTotalscore(propRole.getProperties().getRolezonghemaxscore()); //刚创建公会评分就是会长的  by changhao

		if (!ClanUtils.addClanIndex(factionInfo)) {
			return false;
		}
		// 添加到缓存中
		ClanUtils.addClanToCache(factionInfo.toData());
		// 绑定角色和公会
		
		Long teamId = xtable.Roleid2teamid.select(roleId);
		if (teamId != null) //先把team 锁住 ,后面公会创建任务完成后,会锁定队伍 by changhao
		{
			fire.pb.team.Team team = fire.pb.team.TeamManager.getTeamByTeamID(teamId);
			mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK,team.getAllMemberIds())); //把所有队员锁掉 by changhao			
		}		
		
		xbean.Properties properties = xtable.Properties.get(roleId);
		properties.setClankey(factionKey);
		
		clearAllInfoAddandCreate(roleId);//处理所有的在玩家加入公会或者创建公会时候，需要清除上次的数据
		
		// 添加公会成员信息
		xbean.ClanMemberInfo fmi = createClanMemeberInfo(roleId, properties, ClanPositionType.ClanMaster);
		factionInfo.getMembers().put(roleId, fmi);

		// 创建角色和公会关系
		createRoleToClan(roleId, factionKey);
		initCreateClanTime(factionKey);//初始化周更新时间

		
		// 添加称谓
		ClanUtils.addClanTitleByPosition(roleId,clanname, ClanPositionType.ClanMaster);
		// 创建成功返回协议
		sendClanInfoToRole(roleId,factionInfo.getKey());
		// 创建成功消息
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 145068, null);

		List<String> parm = new ArrayList<String>(2);
		parm.add(propRole.getName());
		parm.add(clanname);
		SceneManager.psendAllWhileCommit(MessageMgr.getMsgNotify(145006, 0, parm));
		// 公会排行榜
		mkdb.Procedure.pexecuteWhileCommit(new ClanRank(factionInfo.toData()));
		
		//玩家公会改变需要重新计算评分 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(roleId));
		
		//公会综合排行榜 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PFactionZongHeProc(factionInfo.getKey(), false));

		mkdb.Procedure.pexecuteWhileCommit(
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						TeamManager.getTeamByRoleId(roleId);//强行锁队伍
						fire.pb.event.Poster.getPoster().dispatchEvent(new FactionCreateOrJoinEvent(roleId));
						return true;
					}
			} );
		//处理帮派事件
		ClanManage.addDealClanEventInfo(factionInfo,ClanManage.EVENT_CREATE,roleId,properties.getRolename(),"","");
		//运营日志
		FactionOpbean factionOpbean = new FactionOpbean(factionInfo.getKey(), factionInfo.getClanname(), 
				FactionOpbean.TYPE_CREATE, ClanPositionType.ClanMaster, factionInfo.getMembers().size(),
				factionInfo.getClanlevel(), factionInfo.getHouse().entrySet());
		int weekpoint=fire.pb.clan.srv.ClanManage.getWeekclanpoint(roleId,factionInfo);
		fire.log.YYLogger.factionOPEventLog(roleId,factionOpbean,weekpoint);
		return true;
	}
	
	
	
	/**
	 * 处理入会申请
	 * 接受或拒绝
	 * @param masterid
	 * @param applyroleid
	 * @param accept
	 * @return
	 */
	public boolean dealAcceptOrRefuse(long masterid, long applyroleid, boolean accept) {
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(masterid, false);
		if (clanInfo == null) {
			MessageMgr.psendMsgNotify(masterid, 145077, null);
			return false;
		}
		if (accept) {
			if (clanInfo.getClanmaster() != masterid && clanInfo.getClanvicemaster() != masterid) {
				// 不是会长也不是副会长
				return false;
			}
			
			if (!clanInfo.getApplicants().containsKey(applyroleid)) {
				// 申请列表中没有该申请人
				MessageMgr.psendMsgNotify(masterid, 160372, null);
//				Procedure.psendWhileCommit(masterid,ClanManage.packagingToApplicantMemberList(clanInfo));
				return true;
			}
			if (clanInfo.getMembers().containsKey(applyroleid)) {
				// 该成员已经在公会中了
				MessageMgr.psendMsgNotify(masterid, 160372,null);
//				Procedure.psendWhileCommit(masterid,ClanManage.packagingToApplicantMemberList(clanInfo));
				return true;
			}
			if (ClanUtils.getClanInfoById(applyroleid, true) != null) {
				// 已经入了其他帮了
				MessageMgr.psendMsgNotify(masterid, 145116, Arrays.asList(new PropRole(applyroleid, true).getName()));
				//这里把玩家从申请列表中删除
				clanInfo.getApplicants().remove(applyroleid);
				cancelApply(applyroleid, clanInfo.getKey());
				Procedure.psendWhileCommit(masterid, new SRefuseApply(applyroleid));
				return true;
			}
			// 判断公会是否满员
			if (ClanUtils.isClanMemberFull(clanInfo)) {
				MessageMgr.sendMsgNotify(masterid, 145011, null);
				return false;
			}
			xbean.Properties properties = xtable.Properties.get(applyroleid);
			//判断这个职位是否已经满了
			if(ClanUtils.isAddClanPositionFull(clanInfo)){
				MessageMgr.sendMsgNotify(masterid, 160310, null);
				return false;
			}
			
			mkdb.Procedure.pexecuteWhileCommit(
					new mkdb.Procedure() {
						@Override
						protected boolean process() {
							TeamManager.getTeamByRoleId(applyroleid);//强行锁队伍
							fire.pb.event.Poster.getPoster().dispatchEvent(new FactionCreateOrJoinEvent(applyroleid));
							return true;
						}
				} );
			addNewClanMember(masterid,applyroleid,properties,clanInfo,true);//添加新成员统一处理加入公会逻辑
		} else {
			clanInfo.getApplicants().remove(applyroleid);
			cancelApply(applyroleid, clanInfo.getKey());
			MessageMgr.psendSystemMessageToRole(applyroleid, 160213, Arrays.asList(clanInfo.getClanname()));
			MessageMgr.sendMsgNotify(applyroleid, 150134, Arrays.asList(clanInfo.getClanname()));
			Procedure.psendWhileCommit(masterid, new SRefuseApply(applyroleid));
		}
		return true;
	}
	
	/**
	 * 解散公会处理排行榜
	 * @param clankey
	 */
	public void dealClanRank(long clankey){
		mkdb.Procedure proc1 = new mkdb.Procedure(){
			@Override
			protected boolean process() {
				//帮派排行榜清除
				xbean.ClanRankList ranlListBean = xtable.Clanranklist.get(1);
				if(null != ranlListBean)
				{
					List<xbean.ClanRankRecord> list = ranlListBean.getRecords();
					xbean.ClanRankRecord toRemove = null;
					for(xbean.ClanRankRecord record : list)
					{
						if(record.getClankey() == clankey)
						{
							toRemove = record;
							break;
						}
					}
					list.remove(toRemove);
				}
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc1);
		
		mkdb.Procedure proc2 = new mkdb.Procedure(){
			@Override
			protected boolean process() {
				//公会综合榜删除 by changhao
				xbean.ClanZongHeRankList clanzongheranklist = xtable.Clanzonghelist.get(1);
				if(null != clanzongheranklist){
					List<xbean.ClanZongHeRankRecord> list = clanzongheranklist.getRecords();
					xbean.ClanZongHeRankRecord toRemove = null;
					for(xbean.ClanZongHeRankRecord record : list){
						if(record.getClankey() == clankey){
							toRemove = record;
							break;
						}
					}
					list.remove(toRemove);
				}		
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc2);
		
		mkdb.Procedure proc3 = new mkdb.Procedure(){
			@Override
			protected boolean process() {
				//工会竞速榜删除 by changhao
				xbean.ClanProgressRankList clanprogressrankmclist = xtable.Clanprogressranklist.get(RankType.FACTION_COPY);
				if(null != clanprogressrankmclist){
					List<xbean.ClanProgressRankRecord> list = clanprogressrankmclist.getRecords();
					xbean.ClanProgressRankRecord toRemove = null;
					for(xbean.ClanProgressRankRecord record : list){
						if(record.getClankey() == clankey){
							toRemove = record;
							break;
						}
					}
					list.remove(toRemove);
				}
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc3);
		
		mkdb.Procedure proc4 = new mkdb.Procedure(){
			@Override
			protected boolean process() {
				//公会战历史榜 by changhao
				xbean.ClanFightHistroyRankList list4 = xtable.Clanfighthistroylist.get(0);
				if(null != list4){
					List<xbean.ClanFightHistroyRankRecord> list = list4.getRecords();
					xbean.ClanFightHistroyRankRecord toRemove = null;
					for(xbean.ClanFightHistroyRankRecord record : list){
						if(record.getMarshaldata().getClanid() == clankey){
							toRemove = record;
							break;
						}
					}
					list.remove(toRemove);
				}
								
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc4);
	}
	
	
	/**
	 * 公会解散的事物处理
	 */
	public void dealDisMiss(long clanKey, int roleNum){
		
		xbean.ClanInfo claninfo = xtable.Clans.select(clanKey);
		long leadid=claninfo.getClanmaster();
		int clanIndex =  claninfo.getIndex();
		
		// 在清除前先获得日志所需的信息 yebin
		FactionOpbean factionOpbean = new FactionOpbean(claninfo.getKey(), claninfo.getClanname(), 
				FactionOpbean.TYPE_DISMISS, ClanPositionType.ClanMaster, roleNum,
				claninfo.getClanlevel(), claninfo.getHouse().entrySet());
		
		//清理帮派数据
		xtable.Clans.remove(claninfo.getKey());
		ClanUtils.removeFactionFromCache(clanIndex);
	
		mkdb.Procedure proc = new mkdb.Procedure(){
			@Override
			protected boolean process() {
				xbean.ClanIndex factionIndex = xtable.Clanindex.get(ClanUtils.CLANINDEX_KEY);
				if(factionIndex != null){
					factionIndex.getIndex().remove(clanIndex);
				}
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);

		dealClanRank(claninfo.getKey());//处理排行榜
		
		//运营日志
		int weekpoint=fire.pb.clan.srv.ClanManage.getWeekclanpoint(leadid,claninfo);
		fire.log.YYLogger.factionOPEventLog(leadid,factionOpbean,weekpoint);
	}

	/**
	 * 解散公会
	 * @param claninfo
	 */
	public void dismissClan(xbean.ClanInfo claninfo) {
		//成员处理
		long clanKey = claninfo.getKey();
		int roleNum = claninfo.getMembers().size();
		for(Map.Entry<Long, xbean.ClanMemberInfo> entry : claninfo.getMembers().entrySet()){
			mkdb.Procedure proc=new mkdb.Procedure(){
				@Override
				protected boolean process() {
					xbean.ClanInfo claninfo2 = xtable.Clans.get(clanKey);
					long roleId = entry.getKey();
					exitClan(roleId,0, claninfo2, DISMISS_LEAVE_REASON);
					// 你的公会由于公会资金长期不足，因此公会被迫解散。
					MessageMgr.psendSystemMessageToRole(roleId, 160233, Arrays.asList(Integer.toString(ClanUtils.getCurrentClanPoint(roleId)/2)));
					//处理帮贡当玩家“主动退出公会”或“被踢出公会”时未消耗的现有贡献将冻结50%。
					ClanManage.dealContribution(roleId,ClanManage.STATE_DISMISS);
					if (claninfo2.getMembers().size() == 0) {
						mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
							@Override
							protected boolean process() {
								dealDisMiss(clanKey,roleNum);//公会解散后的事物处理
								return true;
							}
						});
					}
					return true;
				}
			};
			mkdb.Procedure.pexecuteWhileCommit(proc);
		}
	}
	
	/**
	 * 返回帮派事件信息
	 * @param roleid
	 * @return
	 */
	public boolean getClanEventInfo(long roleid){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		xbean.ClanMemberInfo fmi = clanInfo.getMembers().get(roleid);
		if(fmi==null){
			return false;
		}
		
		SRequestEventInfo sRequestEventInfo=new SRequestEventInfo();
		java.util.List<xbean.ClanEventRecord> list=clanInfo.getClaneventrecordlist();
		java.util.ArrayList<fire.pb.clan.ClanEventInfo> eventlist=new ArrayList<ClanEventInfo>();
		sRequestEventInfo.eventlist=eventlist;
		for(xbean.ClanEventRecord record:list){
			//过滤那些玩家还没有入会之前的信息
			if(record.getTime()<fmi.getJointime()){
				continue;
			}
			fire.pb.clan.ClanEventInfo clanEventInfo=new ClanEventInfo();
			clanEventInfo.eventtime=fire.pb.util.DateValidate.getTimeString(record.getTime());//时间
			clanEventInfo.eventinfo=record.getContent();//内容
			clanEventInfo.eventtype=record.getEventtype();
			clanEventInfo.eventvalue=record.getObjectroleid();
			eventlist.add(clanEventInfo);
		}
		Procedure.psendWhileCommit(roleid, sRequestEventInfo);
		return true;
	}

	/**
	 * 获得公会成员信息
	 * @param roleid
	 * @return
	 */
	public boolean getClanMembers(long roleid){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		SRefreshMemberList sRefreshMemberList=packagingToSRefreshMemberListProtocol(clanInfo);
		
		Procedure.psendWhileCommit(roleid, sRefreshMemberList);
		return true;
		
	}

	/**
	 * 返回玩家基本信息
	 * @param roleid
	 * @param moduletype
	 * @return
	 */
	public boolean getRoleInfoDes(long selfid,long targetid,int moduletype){
		PropRole prop = new PropRole(targetid,true);
		fire.pb.clan.RoleInfoDes des=new RoleInfoDes();
		des.moduletype = moduletype;
		des.roleid = prop.getRoleId();
		des.rolename = prop.getName();
		des.shape = prop.getShape();
		des.level = prop.getLevel();
		des.school = prop.getSchool();
		Long factionId = prop.getClanKey();
		des.clanname = "";
		if (factionId > 0){
			xbean.ClanInfo info = xtable.Clans.select(factionId);
			if(info != null && info.getClanname() != null){
				des.clanname = info.getClanname();
			}
		}
		
		SRequestRoleInfo sRequestRoleInfo=new SRequestRoleInfo();
		sRequestRoleInfo.roleinfo=des;
		Procedure.psendWhileCommit(selfid, sRequestRoleInfo);
		return true;
	}

	/**
	 * 初始化帮派周更新时间
	 * @param factionkey
	 */
	public void initCreateClanTime(long factionkey){
		mkdb.Procedure proc=new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				xbean.ClanWeekNum factionweeknum=xtable.Clanweeknumtab.get(factionkey);
				if(factionweeknum==null){
					factionweeknum=xbean.Pod.newClanWeekNum();
					xtable.Clanweeknumtab.insert(factionkey, factionweeknum);
				}
				if(factionweeknum.getWeeknum()==-1){
					//这里的初始值，如果这个方法是一天执行一次的，就需要在启动服务器的时候设置一下，要不如果是刚好的周一启动服务器，就不会处理更新逻辑了
					factionweeknum.setWeeknum(fire.pb.util.DateValidate.getWeekNumber());
				}
				return true;
			}
		};
		mkdb.Procedure.pexecute(proc);
	}

	/**
	 * 3.邀请入会
	 * @param guestroleid   被邀请者
	 * @param hostroleid    邀请者
	 * @return
	 */
	public boolean invitationClan(long guestroleid, long hostroleid) {
		if (guestroleid < 0 || hostroleid < 0)
			return false;
		if (ClanUtils.getClanInfoById(guestroleid, true) != null) {
			// 已经有公会了
			MessageMgr.sendMsgNotify(hostroleid, 145115, null);
			return false;
		}
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(hostroleid, false);
		if (clanInfo == null) {
			MessageMgr.psendMsgNotify(hostroleid, 145077, null);
			return false;
		}
		xbean.Properties properties = xtable.Properties.get(guestroleid);
		if (properties.getLevel() < getJoinClanLevel()) {// 判断被邀请入会者等级
			MessageMgr.sendMsgNotify(guestroleid, 150137, Arrays.asList(getJoinClanLevel()+""));
			return false;
		}
		xbean.ClanMemberInfo masterInfo = clanInfo.getMembers().get(hostroleid);
		if (masterInfo.getClanposition()>=ClanPositionType.ClanMaster&&masterInfo.getClanposition()<=ClanPositionType.ClanArmyGroup4) {
			// 会长或者副会长邀请。直接入帮
			if (ClanUtils.isClanMemberFull(clanInfo)) {
				// 公会满员
				MessageMgr.sendMsgNotify(hostroleid, 145011, null);
				return false;
			}
			//判断这个职位是否已经满了
			if(ClanUtils.isAddClanPositionFull(clanInfo)){
				MessageMgr.sendMsgNotify(guestroleid, 160310, null);
				return false;
			}
			addNewClanMember(hostroleid,guestroleid,properties,clanInfo,true);//添加新成员统一处理加入公会逻辑
		} else {
			// 普通人员邀请，受邀人进入公会申请列表
			if (new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					return ClanBaseManager.getInstance().applyClan(guestroleid, clanInfo.getKey(), false);
				}
			}.call()) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(hostroleid, 145109,
						Arrays.asList(new PropRole(guestroleid, true).getName()));
			} else {
				// 普通人员邀请不成功，回滚事务
				return false;
			}
		}
	

		return true;
	}
	
	/**
	 * 5.退出公会
	 * 主动退出
	 * @param roleId 退出公会的人
	 * @return
	 */
	public boolean leaveClan(long roleId) {
		if (xtable.Roleid2battleid.select(roleId) != null) {
			MessageMgr.sendMsgNotify(roleId, 131451, null);
			return false;
		}
		
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, false);
		
		if (clanInfo == null){
			MessageMgr.psendMsgNotify(roleId, 145077, null);
			return false;
		}
		String nameprop=xtable.Properties.selectRolename(roleId);
		xbean.ClanMemberInfo fmi = clanInfo.getMembers().get(roleId);
		
		if (fmi != null)
		{
			Long clanfightid = xtable.Roleid2clanfightid.select(roleId); //在公会战场中不能退会  by changhao
			if (clanfightid != null)
			{
				MessageMgr.sendMsgNotify(roleId, 410041, null);
				return false;
			}
		}
		
		int oldposition=fmi.getClanposition();//当前玩家职位
		if(roleId == clanInfo.getClanmaster()){
			//会长脱离
			if(clanInfo.getMembers().size() == 1){
				//仅剩会长自己，脱离公会则解散
				//清理会长称谓
				ClanUtils.removeClanTitleByPosition(roleId, fmi.getClanposition());
				dealDisMiss(clanInfo.getKey(),1);//帮派解散事物逻辑
			} else{
				if (clanInfo.getClanvicemaster() < 1){
					MessageMgr.sendMsgNotify(roleId, 145073, null);
					return false;
				}
				clanInfo.getMembers().get(clanInfo.getClanvicemaster()).setClanposition(ClanPositionType.ClanMaster);
				clanInfo.setClanmaster(clanInfo.getClanvicemaster());
				clanInfo.setClanvicemaster(0);
				//副会长升为会长称谓变化处理
				ClanUtils.changeClanTitleByPosition(clanInfo.getClanmaster(), ClanPositionType.ClanViceMaster, ClanPositionType.ClanMaster);
				//在公会频道发送系统消息140296
				String nameotherprop=new PropRole(clanInfo.getClanmaster(),true).getName();
				MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 140296, 0,Arrays.asList(nameprop,nameotherprop,StringConstant.ID2String("会长",283)));
				
			}
		}else if(roleId == clanInfo.getClanvicemaster()){
			//副会长脱离
			clanInfo.setClanvicemaster(0);
		}
		
		this.exitClan(roleId,clanInfo.getClanmaster(), clanInfo, NORMAL_LEAVE_REASON);
		
		Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), new SLeaveClan(roleId));
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 145027, 0, null);
		
		//发送公会消息
		MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 145028, 0, Arrays.asList(nameprop));
		
		//在好友频道发送消息160257
		MessageMgr.psendSystemMessageToRole(roleId, 160257, Arrays.asList(Integer.toString(ClanUtils.getCurrentClanPoint(roleId)/2)));
		
		//处理帮贡当玩家“主动退出公会”或“被踢出公会”时未消耗的现有贡献将冻结50%。
		ClanManage.dealContribution(roleId,ClanManage.STATE_QUIT);
		//处理帮派事件
		ClanManage.addDealClanEventInfo(clanInfo,ClanManage.EVENT_QUIT,roleId,nameprop,"","");
		
		//处理弹劾逻辑
		xbean.ImpeachMent impeachMent=clanInfo.getImpeachment();
		if(impeachMent!=null){
			if(impeachMent.getImpeachroleid()==roleId){
				PRequestImpeachMent.impeachmentclans.remove(clanInfo.getKey());
				MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 172057, 0,null);
				PRequestImpeachMent.clearImpeachroleInfo(impeachMent);
				//更新弹劾,显示发起弹劾界面
				Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), PRequestImpeachMent.sendSRequestImpeachMentView(impeachMent,PRequestImpeachMent.STATE_REQUEST));
				logger.info("roleid\t"+roleId+"\tclankey\t"+clanInfo.getKey()+"\t弹劾的人主动退出公会弹劾失败");
			}
		}
		logger.info("玩家id "+roleId+"\t公会id "+clanInfo.getKey()+"主动退出公会");
		//运营日志
		FactionOpbean factionOpbean = new FactionOpbean(clanInfo.getKey(), clanInfo.getClanname(), 
				FactionOpbean.TYPE_QUIT, oldposition, clanInfo.getMembers().size(),
				clanInfo.getClanlevel(), clanInfo.getHouse().entrySet());
		int weekpoint=fire.pb.clan.srv.ClanManage.getWeekclanpoint(roleId,clanInfo);
		fire.log.YYLogger.factionOPEventLog(roleId,factionOpbean,weekpoint);
		return true;
	}
	
//	/**
//	 * 一键申请加入公会
//	 * @param roleId
//	 * @return 成功true
//	 */
//	@Deprecated
//	public boolean oneKeyApplyClan(long roleId) {
//		xbean.Properties properties = xtable.Properties.select(roleId);
//		if (properties.getLevel() < getJoinClanLevel()) {
//			MessageMgr.sendMsgNotify(roleId, 150137, Arrays.asList(getJoinClanLevel()+""));
//			return false;
//		}
//		
//		if (properties.getClankey() > 0) {
//			logger.error("请求申请入会的角色,已经有公会！");
//			return false;
//		}
//		
//		// 获得玩家公会申请记录列表
//		xbean.RoleApplyClanList roleApplyClanList = xtable.Roleapplyclantab.get(roleId);
//		if (null == roleApplyClanList) {
//			roleApplyClanList = xbean.Pod.newRoleApplyClanList();
//			xtable.Roleapplyclantab.insert(roleId, roleApplyClanList);
//		}
//		// 验证一键申请冻结时间
//		long oneKeyLastTime = roleApplyClanList.getOnekeylasttime();
//		long currTime = System.currentTimeMillis();
//		long nextTime = currTime - oneKeyLastTime;
//		if (nextTime < getOnekeyTime()) {
//			if (logger.isInfoEnabled()) {
//				logger.info("一键申请时间冻结中");
//			}
//			MessageMgr.sendMsgNotify(roleId, 160192, null);
//			return false;
//		}
//		
//		roleApplyClanList.setOnekeylasttime(currTime);
//		roleApplyClanList.getOnekeyapplymap().clear();// 清空上一次一键申请的公会
//		
//		List<xbean.ClanInfo> ClanInfolist=new ArrayList<xbean.ClanInfo>();
//		ClanInfolist.addAll(Module.clanListMap.values());
//		
//		//这里打乱一下排序吧，随机申请
//		Collections.shuffle(ClanInfolist);//打乱排序
//		if(ClanInfolist.size()==0){
//			MessageMgr.sendMsgNotify(roleId, 160235, null);
//			return false;
//		}
//		int index=0;
//		
//		for (xbean.ClanInfo v : ClanInfolist) {
//			
//			v = xtable.Clans.get(v.getKey());
//			
//			if (!this.applyClan(roleId, v.getKey(), true)) {
//				// 如果申请不成功继续尝试其它公会申请
//				continue;
//			}
//			
//			xbean.ClanInfo factionInfo = ClanUtils.getClanInfoById(roleId, false);
//			if (null != factionInfo) {
//				// 如果公会申请成功, 结束继续申请
//				break;
//			}
//			index++;
//			//判断是否超出最大公会数限制
//			if(index>=getApplyMaxNum()){
//				break;
//			}
//		}
//		MessageMgr.sendMsgNotify(roleId, 160119, null);
//		return true;
//	}
	
	/**
	 * 7.获取公会信息
	 * @param roleid
	 * @return
	 */
	public boolean getClanInfo(long roleid) {
		xbean.ClanInfo cInfo = ClanUtils.getClanInfoById(roleid, true);
		if (cInfo == null || !cInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		SRefreshPosition sRefreshPosition = new SRefreshPosition(roleid,
				cInfo.getMembers().get(roleid).getClanposition());
		
		Procedure.psendWhileCommit(roleid, sRefreshPosition);

		sendClanInfoToRole(roleid,cInfo.getKey());
		return true;
	}
	
	/**
	 * 8.获取公会列表
	 * @param roleId
	 * @param currPage
	 * @return
	 */
	public boolean getClanList(long roleId, int currPage) {
		retApplyClanState(roleId);
		ArrayList<ClanSummaryInfo> clanList = null;
		List<ClanSummaryInfo> pageList = ClanUtils.getPageFactionList(currPage);
		if (pageList == null) {
			clanList = new ArrayList<>();
		}else {
			clanList = new ArrayList<>(pageList);
		}
		// 返回协议
		SOpenClanList resprotocol = new SOpenClanList();
		resprotocol.currpage = currPage;
		resprotocol.clanlist = clanList;
		// 返回客户端
		Procedure.psendWhileCommit(roleId, resprotocol);
		return true;
	}
	
	/**
	 * 发送请求的药房信息
	 * @param roleid
	 * @return
	 */
	public boolean sendMedicShopInfo(long roleid){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
//		//判断建筑等级，1级以后开启
		int level = clanInfo.getHouse().get(ClanManage.DrugStone);
		if(level==0){
			return false;
		}
		int buynum=0;
		xbean.BuyMedicItemNum buyMedicItemNum=xtable.Buymedicitemnum.select(roleid);
		if(buyMedicItemNum!=null){
			buynum=buyMedicItemNum.getBuynum();//当天购买数量
		}
		
		SOpenClanMedic sOpenClanMedic=new SOpenClanMedic();
		java.util.ArrayList<fire.pb.clan.MedicItem> medicitems=new ArrayList<MedicItem>();
		xbean.MedicItemList itemlist=xtable.Medicitemlisttab.select(clanInfo.getKey());
		if(itemlist!=null){
			sOpenClanMedic.selecttype=itemlist.getSelecttype();
			sOpenClanMedic.buyitemnum=buynum;
			for(xbean.MedicItem item:itemlist.getMedicitems().values()){
				fire.pb.clan.MedicItem it=new MedicItem();
				it.itemid=item.getItemid();
				it.itemnum=item.getItemnum();
				medicitems.add(it);
			}
			sOpenClanMedic.medicitemlist=medicitems;
		}
		
		Procedure.psendWhileCommit(roleid, sOpenClanMedic);
		
		return true;
	}
	
	/**
	 * 领取公会分红
	 * @param roleid
	 * @return
	 */
	public boolean sendGetBonus(long roleid){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		xbean.ClanMemberInfo clanMemberInfo  = clanInfo.getMembers().get(roleid);
		if(clanMemberInfo==null){
			return false;
		}
		int value=clanMemberInfo.getOwnbonus();
		if(value<=0){
			return false;
		}
		clanMemberInfo.setOwnbonus(0);
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
		bag.addSysMoney(value, "领取公会分红加钱", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifenhong, 0);
		MessageMgr.psendMsgNotify(roleid, 160049, MessageMgr.getStringList(value));
		SBonusQuery sBonusQuery=new SBonusQuery();
		sBonusQuery.bonus=clanMemberInfo.getOwnbonus();
		Procedure.psendWhileCommit(roleid, sBonusQuery);
		logger.info("公会id"+clanInfo.getKey()+"\t公会名称"+clanInfo.getClanname()+"\t玩家获得公会分红"+"\t角色id\t"+roleid+"\t领取分红数量，转换为金币\t"+value);
		return true;
	}
	
	/**
	 * 返回查询结果
	 * @param roleid
	 * @return
	 */
	public boolean sendSearchBonus(long roleid){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		xbean.ClanMemberInfo clanMemberInfo  = clanInfo.getMembers().get(roleid);
		if(clanMemberInfo==null){
			return false;
		}
		SBonusQuery sBonusQuery=new SBonusQuery();
		sBonusQuery.bonus=clanMemberInfo.getOwnbonus();
		Procedure.psendWhileCommit(roleid, sBonusQuery);
		return true;
	}
	
	/**
	 * GM创建公会
	 * @param roleId 创建角色id
	 * @param clanname 公会名称
	 * @param clanaim  公会宗旨
	 * @return 成功true
	 */
	public boolean GMCreateClan(long roleId, String clanname, String clanaim, int guildLv) {
		if(guildLv > 5){
			guildLv = 5;
		}
		
		PropRole propRole = new PropRole(roleId, true);
		if (propRole.getLevel() < getCreateClanLevel()) {
			MessageMgr.sendMsgNotify(roleId,145004, null);
			return false;
		}
		//判断当前公会是否已经达到上限
		int max=getMaxCreateClanNum();
		if(max==-1){
			MessageMgr.sendMsgNotify(roleId, 164000, null);
			return false;
		}
		if(Module.clanListMap.values().size()>=max){
			MessageMgr.sendMsgNotify(roleId, 164000, null);
			return false;
		}
		Long factionId = xtable.Roleidclan.select(roleId);
		if (factionId != null && factionId > 0) {
			return false;
		}
		if (!UniqName.allocate("clan", clanname)) {
			MessageMgr.sendMsgNotify(roleId, 142402, null);
			return false;
		}
		
		//判断是否是点卡服，点卡服消耗符石，非点卡服消耗金币
		if(fire.pb.fushi.Module.GetPayServiceType() == 1){//点卡服务器
			int fushinum=getCreateClanFushi();
			if(fushinum<=0){
				return false;
			}
			//扣除符石
			if (!fire.pb.fushi.FushiManager.subFushiFromUser(propRole.getProperties().getUserid(), roleId, fushinum, 0, 0,fire.pb.fushi.FushiConst.REASON_CREATE_CLAN,
					YYLoggerTuJingEnum.tujing_Value_createclan,true)) {
				return false;
			}
			logger.info("角色id"+roleId+"创建公会扣除符石"+fushinum);
		}else{
			int gold=getCreateClanMoney();
			//判断创建公会的钱是否够
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
			if(bag.getGold()<gold){
				MessageMgr.sendMsgNotify(roleId, 145005, null);
				return false;
			}
			if (bag.subGold(-gold, "创建公会", YYLoggerTuJingEnum.tujing_Value_createclan, 0) != -gold) {
				MessageMgr.sendMsgNotify(roleId, 145005, null);
				return false;
			}
			logger.info("角色id"+roleId+"创建公会扣除金币"+gold);
		}
		xbean.ClanInfo factionInfo = xbean.Pod.newClanInfo();
		long factionKey = xtable.Clans.insert(factionInfo);

		factionInfo.setKey(factionKey);
		factionInfo.setOldclanname(clanname);
		factionInfo.setClanname(clanname);
		factionInfo.setClanaim(clanaim);
		factionInfo.setClanlevel(guildLv);
		factionInfo.setClanmaster(roleId);
		factionInfo.setPopularity(0);
		factionInfo.setCreator(roleId);
		factionInfo.setCreatetime(System.currentTimeMillis());
		factionInfo.setClanrank(-1);
		factionInfo.setMoney(BUILD_FACTION_INIT_MONEY); //设置初始公会资金
		factionInfo.getHouse().put(ClanManage.GoldBank, 0); //金库 初始0级 by changhao
		factionInfo.getHouse().put(ClanManage.DrugStone, 0); //药房 初始0级 by changhao
		factionInfo.getHouse().put(ClanManage.Hotel, 0); //旅馆 初始0级 by changhao
		factionInfo.setBonus(0); //公会分红 by changhao
		factionInfo.setAutostate(0);//1表示自动开启   0表示关闭
		factionInfo.setRequestlevel((short) 1);
		factionInfo.setTotalscore(propRole.getProperties().getRolezonghemaxscore()); //刚创建公会评分就是会长的  by changhao

		if (!ClanUtils.addClanIndex(factionInfo)) {
			return false;
		}
		// 添加到缓存中
		ClanUtils.addClanToCache(factionInfo.toData());
		// 绑定角色和公会
		
		Long teamId = xtable.Roleid2teamid.select(roleId);
		if (teamId != null) //先把team 锁住 ,后面公会创建任务完成后,会锁定队伍 by changhao
		{
			fire.pb.team.Team team = fire.pb.team.TeamManager.getTeamByTeamID(teamId);
			mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK,team.getAllMemberIds())); //把所有队员锁掉 by changhao			
		}		
		
		xbean.Properties properties = xtable.Properties.get(roleId);
		properties.setClankey(factionKey);
		
		clearAllInfoAddandCreate(roleId);//处理所有的在玩家加入公会或者创建公会时候，需要清除上次的数据
		
		// 添加公会成员信息
		xbean.ClanMemberInfo fmi = createClanMemeberInfo(roleId, properties, ClanPositionType.ClanMaster);
		factionInfo.getMembers().put(roleId, fmi);

		// 创建角色和公会关系
		createRoleToClan(roleId, factionKey);
		initCreateClanTime(factionKey);//初始化周更新时间

		
		// 添加称谓
		ClanUtils.addClanTitleByPosition(roleId, clanname, ClanPositionType.ClanMaster);
		// 创建成功返回协议
		sendClanInfoToRole(roleId,factionInfo.getKey());
		// 创建成功消息
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 145068, null);

		List<String> parm = new ArrayList<String>(2);
		parm.add(propRole.getName());
		parm.add(clanname);
		SceneManager.psendAllWhileCommit(MessageMgr.getMsgNotify(145006, 0, parm));
		// 公会排行榜
		mkdb.Procedure.pexecuteWhileCommit(new ClanRank(factionInfo.toData()));
		
		//玩家公会改变需要重新计算评分 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(roleId));
		
		//公会综合排行榜 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PFactionZongHeProc(factionInfo.getKey(), false));

		mkdb.Procedure.pexecuteWhileCommit(
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						TeamManager.getTeamByRoleId(roleId);//强行锁队伍
						fire.pb.event.Poster.getPoster().dispatchEvent(new FactionCreateOrJoinEvent(roleId));
						return true;
					}
			} );
		//处理帮派事件
		ClanManage.addDealClanEventInfo(factionInfo,ClanManage.EVENT_CREATE,roleId,properties.getRolename(),"","");
		//运营日志
		FactionOpbean factionOpbean = new FactionOpbean(factionInfo.getKey(), factionInfo.getClanname(), 
				FactionOpbean.TYPE_CREATE, ClanPositionType.ClanMaster, factionInfo.getMembers().size(),
				factionInfo.getClanlevel(), factionInfo.getHouse().entrySet());
		int weekpoint=fire.pb.clan.srv.ClanManage.getWeekclanpoint(roleId,factionInfo);
		fire.log.YYLogger.factionOPEventLog(roleId,factionOpbean, weekpoint);
		return true;
	}
}
