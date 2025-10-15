package fire.pb.clan.srv;

import java.util.Arrays;

import fire.log.beans.FactionOpbean;
import fire.pb.PropRole;
import fire.pb.clan.ClanPositionType;
import fire.pb.clan.ClanUtils;
import fire.pb.clan.PRequestImpeachMent;
import fire.pb.clan.SBannedtalk;
import fire.pb.clan.SOpenAutoJoinClan;
import fire.pb.clan.SRefreshPosition;
import fire.pb.talk.MessageMgr;
import fire.pb.util.StringConstant;
import mkdb.Procedure;

public class ClanAuthManager extends ClanManage {
	private static class AuthManagerHolder {
		static ClanAuthManager instance = new ClanAuthManager();
	}
	public static ClanAuthManager getInstance() {
		return AuthManagerHolder.instance;
	}
	
	private ClanAuthManager() {
		
	}
	
	/**
	 * 2.禁言  或    解除
	 * @param masterId  
	 * @param memberId
	 * @param flag 操作标示：1禁言  2解禁
	 */
	public boolean bannedTalk(long masterId, long memberId, int flag) {
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(masterId, false);
		if (clanInfo == null) {
			logger.error("你还没有加入任何帮派，请先找一个帮派吧！");
			MessageMgr.psendMsgNotify(masterId, 145077, null);
			return false;
		}
		xbean.ClanMemberInfo masterInfo = clanInfo.getMembers().get(masterId);
		xbean.ClanMemberInfo memberInfo = clanInfo.getMembers().get(memberId);
		if(memberInfo==null){
			MessageMgr.psendMsgNotify(masterId, 160311, null);//提示目标玩家不是公会成员
			return false;
		}
		int masterPosition = masterInfo.getClanposition();
		int memeberPosition = memberInfo.getClanposition();
		
		if (memeberPosition <= masterPosition) {
			logger.error("禁言或解除禁言权限不够.");
			return false;
		}
		String mastername=new PropRole(masterId,true).getName();
		if (flag == 1) {
			long removeBannedTime = System.currentTimeMillis() + ClanManage.getStopTalkMin() * 60 * 1000;
			memberInfo.setIsbannedtalk(true);
			memberInfo.setBannedtalktime(removeBannedTime);
			SBannedtalk sBannedtalk = new SBannedtalk(memberId, 1);
			Procedure.psendWhileCommit(masterId, sBannedtalk);
			MessageMgr.sendMsgNotify(masterId, 141834, null);
			MessageMgr.sendMsgNotify(memberId, 141161, Arrays.asList(ClanManage.getLeaveStopTalkMin(memberInfo)+""));
			//<T t="你已被"  c="FF693F00"></T><T t="$parameter1$" c="ff06cc11"></T><T t="禁言，无法在公会频道发言，2小时后将自动解除。" c="FF693F00"></T>
			MessageMgr.psendSystemMessageToRole(memberId, 160241, Arrays.asList(mastername));
			logger.info("roleId "+masterId+"\t公会key\t"+clanInfo.getKey()+"\t给玩家禁言roleid\t"+memberId);
		}else if (flag == 2) {
			if (!memberInfo.getIsbannedtalk()) {
				MessageMgr.sendMsgNotify(masterId, 141836, null);
				return false;
			}
			memberInfo.setIsbannedtalk(false);
			memberInfo.setBannedtalktime(0L);
			
			SBannedtalk sBannedtalk = new SBannedtalk(memberId, 2);
			Procedure.psendWhileCommit(masterId, sBannedtalk);
			
			MessageMgr.sendMsgNotify(masterId, 141835, null);
			MessageMgr.sendMsgNotify(memberId, 141835, null);
			
			MessageMgr.psendSystemMessageToRole(memberId, 160242, Arrays.asList(mastername));
			logger.info("roleId "+masterId+"\t公会key\t"+clanInfo.getKey()+"\t给玩家解除禁言roleid\t"+memberId);
		}
		
		return true;
	}
	
	/**
	 * 8.开启或关闭公会自动接收
	 */
	public void changeAutoJoinClan(long roleId, int autoState, short requestlevel) {
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, false);
		clanInfo.setAutostate(autoState);
		clanInfo.setRequestlevel(requestlevel);
		Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), new SOpenAutoJoinClan(autoState,requestlevel));
	}
	
	
	
//	/**
//	 * 成员升级触发职位变动
//	 * 学徒升级成员
//	 */
//	public int changePosition(long roleId) {
//		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, false);
//		if (null == clanInfo){
//			return -1;
//		}
//		//判断玩家当前帮贡是否满足要求getPositionChangeBanggong
//		int value = ClanUtils.getCurrentClanPoint(roleId);
//		if(value<getPositionChangeBanggong()){
//			return -1;
//		}
//		PropRole memberProp = new PropRole(roleId, true);
//		xbean.ClanMemberInfo factionMemberInfo = clanInfo.getMembers().get(roleId);
//		//判断入会时间
//		if(System.currentTimeMillis()-factionMemberInfo.getJointime()<getPositionChangeAddTime()*60*60*1000){
//			return -1;
//		}
//		//职位变化后的一些逻辑处理
//		changePosition(0,roleId,ClanPositionType.ClanMember,ClanPositionType.ClanApprentice,factionMemberInfo,memberProp,clanInfo);
//		return factionMemberInfo.getClanposition();
//	}
	


	/**
	 * 1.公会任命 
	 * @param masterId   管理员
	 * @param memberId   成员
	 * @param position   新的位置
	 * @return
	 */
	public boolean changePosition(long masterId, long memberId, int position) {
		if (masterId == memberId) {
			logger.error("这是你自己，不能对自己进行操作。");
			MessageMgr.sendMsgNotify(masterId, 145018, null);
			return false;
		}
		
		if (!ClanUtils.validatePosition(position)) {
			logger.error("目标职位无效。");
			return false;
		}
		
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(masterId, false);
		if (clanInfo == null) {
			MessageMgr.psendMsgNotify(masterId, 145077, null);
			return false;
		}
		
		PropRole memberProp = new PropRole(memberId, true);
		xbean.ClanMemberInfo masterInfo = clanInfo.getMembers().get(masterId);
		xbean.ClanMemberInfo memberInfo = clanInfo.getMembers().get(memberId);
		if(memberInfo==null){
			MessageMgr.psendMsgNotify(masterId, 160311, null);//提示目标玩家不是公会成员
			return false;
		}
		int oldposition=memberInfo.getClanposition();
		//只有军团长以上的职位有任命权限
		if(masterInfo.getClanposition()>ClanPositionType.ClanArmyGroup4||masterInfo.getClanposition()<ClanPositionType.ClanMaster){
			logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"只有军团长以上的职位有任命权限");
			return false;
		}
		if (position < masterInfo.getClanposition()) {
			if (logger.isDebugEnabled()) {
				logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"权限不够不能调整大于自己职务或等于自己职务的操作。");
			}
			return false;
		}
		
		if (masterInfo.getClanposition() > memberInfo.getClanposition()) {
			logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"改变职务者比将被改变职务者职务低或相等，不能操作。");
			return false;
		}
		
		if (clanInfo.getClanmaster() == memberId) {
			logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"现任帮主不能被任命。");
			return false;
		}
		
		if (position == ClanPositionType.ClanMaster && masterId != clanInfo.getClanmaster()) {
			logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"只有现任帮主能任命下一任帮主。");
			return false;
		}
		
		if (position == ClanPositionType.ClanViceMaster) {
			if (clanInfo.getClanmaster() != masterId) {
				logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"副会长只能会长任命。");
				return false;
			}
		}
		//这里判断特殊军团任命
		if(checkArmyCanDo(masterInfo.getClanposition(),position)==false){
			return false;
		}
		
		// 获取职务人数
		Integer oldMem = ClanUtils.getClanPosition(clanInfo,position);//当前职位人数;
		oldMem = oldMem == null ? 0 : oldMem;
		
		if (isPositionFull(masterId, position, clanInfo)) {
			logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"任命的职务已经满员。");
			MessageMgr.psendMsgNotify(masterId, 160427, Arrays.asList(ClanUtils.retNameByPosition(position)));
			return false;
		}
		
		if (position == memberInfo.getClanposition()) {
			// 任命没变化
			String[] paras = { memberProp.getName(), ClanUtils.retNameByPosition(position) };
			MessageMgr.psendMsgNotify(masterId, 145105, Arrays.asList(paras));
			return false;
		}
		// 职位从这里开始变化
		if (position == ClanPositionType.ClanMaster) {
			// 现任会长任命下任会长
			clanInfo.setClanmaster(memberId);
			masterInfo.setClanposition(ClanPositionType.ClanMember);
			// 称谓变更为成员
			ClanUtils.changeClanTitleByPosition(masterId, ClanPositionType.ClanMaster,
					ClanPositionType.ClanMember);
			Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), new SRefreshPosition(masterId, ClanPositionType.ClanMember));
			
		} else if (position == ClanPositionType.ClanViceMaster) {
			// 任命副会长
			clanInfo.setClanvicemaster(memberId);
		}
		//这里需要处理原来的职位是不是副会长，如果是需要把副会长标志设置为空
		if(oldposition==ClanPositionType.ClanViceMaster){
			clanInfo.setClanvicemaster(0);
		}
		//职位变化后的一些逻辑处理
		changePosition(masterId,memberId,position,oldposition,memberInfo,memberProp,clanInfo);
		
		return true;
	}
	
	/**
	 * 职位变化后续逻辑
	 * @param masterId
	 * @param memberId
	 * @param position
	 * @param oldposition
	 * @param memberInfo
	 * @param memberProp
	 * @param clanInfo
	 */
	public void changePosition(long masterId, long memberId, int position,int oldposition, xbean.ClanMemberInfo memberInfo,PropRole memberProp,xbean.ClanInfo clanInfo){
		// 称谓变更
		ClanUtils.changeClanTitleByPosition(memberId, memberInfo.getClanposition(), position);
		// 更新成员职位，须在公会职位对应数量变更之后
		memberInfo.setClanposition(position);

		String mastername="";//系统
		mastername=StringConstant.ID2String(mastername,StringConstant.SYSTEM_WORD);
		if(masterId>0){
			mastername=new PropRole(masterId, true).getName();
		}
		
		// 发送帮派消息
		if (position != ClanPositionType.ClanMember) {
			// 除了帮众任务，其余发布帮派消息
			String[] paras = { mastername, memberProp.getName(),
					ClanUtils.retNameByPosition(position) };
			MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 145023, 0,
					Arrays.asList(paras));
		}
		
		String[] parasToOperator = { memberProp.getName(), ClanUtils.retNameByPosition(position) };
		
		if(masterId>0){
			MessageMgr.psendMsgNotifyWhileCommit(masterId, 145092, 0, Arrays.asList(parasToOperator));
		}
		Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), new SRefreshPosition(memberId, position));
		//处理帮派事件
		ClanManage.addDealClanEventInfo(clanInfo,ClanManage.EVENT_APPOINT,memberId,mastername,memberProp.getName(),ClanUtils.retNameByPosition(position));
		//运营日志
		int opid=FactionOpbean.TYPE_POSITION_DOWN;
		if(position == ClanPositionType.ClanMaster){
			opid=FactionOpbean.TYPE_CHANGE_LEDER;
		}else{
			if(oldposition>position){
				opid=FactionOpbean.TYPE_POSITION_UP;
			}
		}

		xbean.ClanMemberInfo fmi = clanInfo.getMembers().get(memberId);
		FactionOpbean factionOpbean = new FactionOpbean(clanInfo.getKey(), clanInfo.getClanname(), 
				opid, fmi.getClanposition(), clanInfo.getMembers().size(),
				clanInfo.getClanlevel(), clanInfo.getHouse().entrySet());
		int weekpoint=fire.pb.clan.srv.ClanManage.getWeekclanpoint(memberId,clanInfo);
		fire.log.YYLogger.factionOPEventLog(memberId,factionOpbean,weekpoint);
		logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"任命玩家，目标玩家id "+memberId+"职位\t"+position);
	}
	
	/**
	 * 处理弹劾成功之后的逻辑
	 * @param masterId
	 * @param memberId
	 * @param position
	 * @param oldposition
	 * @param memberInfo
	 * @param membername
	 * @param clanInfo
	 */
	public void changeImpeachMentPosition(long masterId, long memberId, int position,int oldposition, xbean.ClanMemberInfo memberInfo,String membername,xbean.ClanInfo clanInfo){
		// 称谓变更
		ClanUtils.changeClanTitleByPosition(memberId, memberInfo.getClanposition(), position);
		// 更新成员职位，须在公会职位对应数量变更之后
		memberInfo.setClanposition(position);
		
		String mastername="";//系统
		mastername=StringConstant.ID2String(mastername,StringConstant.SYSTEM_WORD);
		if(masterId>0){
			mastername=xtable.Properties.selectRolename(masterId);
		}
		MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 172056, 0,Arrays.asList(membername));//发送弹劾成功的消息
		
		Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), new SRefreshPosition(memberId, position));
		//处理帮派事件
		ClanManage.addDealClanEventInfo(clanInfo,ClanManage.EVENT_IMPEACH_SUCC,memberId,membername,"","");
		//运营日志
		int opid=FactionOpbean.TYPE_POSITION_DOWN;
		if(position == ClanPositionType.ClanMaster){
			opid=FactionOpbean.TYPE_CHANGE_LEDER;
		}else{
			if(oldposition>position){
				opid=FactionOpbean.TYPE_POSITION_UP;
			}
		}
		xbean.ClanMemberInfo fmi = clanInfo.getMembers().get(memberId);
		FactionOpbean factionOpbean = new FactionOpbean(clanInfo.getKey(), clanInfo.getClanname(), 
				opid, fmi.getClanposition(), clanInfo.getMembers().size(),
				clanInfo.getClanlevel(), clanInfo.getHouse().entrySet());
		int weekpoint=fire.pb.clan.srv.ClanManage.getWeekclanpoint(memberId,clanInfo);
		fire.log.YYLogger.factionOPEventLog(memberId,factionOpbean,weekpoint);
		logger.info("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"弹劾成功玩家，目标玩家id "+memberId+"职位\t"+position);
	}
	
	
	
	
	
	
	
	/**
	 * 判断军团踢出和任命的权限
	 * @param masterposition  任命人或者踢出人当前职位
	 * @param position  任命的职位，或者被踢出人的职位
	 * @return
	 */
	public boolean checkArmyCanDo(int masterposition,int position){
		//这里判断特殊军团任命
		if(masterposition==ClanPositionType.ClanArmyGroup1){
			//第一军团，只能任命自己军团的，和成员以及学徒
			if(position!=ClanPositionType.ClanArmyGroupElite1// 第一军团精英
					&&position!=ClanPositionType.ClanMember){// 成员
				logger.error("第一军团只能任命或者踢出自己军团的");
				return false;
			}
		}else if(masterposition==ClanPositionType.ClanArmyGroup2){
			if(position!=ClanPositionType.ClanArmyGroupElite2// 第2军团精英
					&&position!=ClanPositionType.ClanMember){// 成员
				logger.error("第2军团只能任命或者踢出自己军团的");
				return false;
			}
		}else if(masterposition==ClanPositionType.ClanArmyGroup3){
			if(position!=ClanPositionType.ClanArmyGroupElite3// 第3军团精英
					&&position!=ClanPositionType.ClanMember){// 成员
				logger.error("第3军团只能任命或者踢出自己军团的");
				return false;
			}
		}else if(masterposition==ClanPositionType.ClanArmyGroup4){
			if(position!=ClanPositionType.ClanArmyGroupElite4// 第4军团精英
					&&position!=ClanPositionType.ClanMember){// 成员
				logger.error("第4军团只能任命或者踢出自己军团的");
				return false;
			}
		}
		return true;
	}
	
	public boolean enterClanMap(long roleId) {
		return ClanUtils.enterClanMap(roleId);
	}
	
	/**
	 * 4.踢出公会
	 * @param roleid
	 * @param memberId  开除公会的成员id
	 * @return
	 */
	public boolean fireMemeberClan(long masterId, long memberId, int reasonType) {
		if(masterId == memberId){
			MessageMgr.sendMsgNotify(masterId, 145018, null);
			return false;
		}
				
		//需要判断是否是一个公会的成员
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(masterId, false);
		if(clanInfo == null){
			MessageMgr.psendMsgNotify(masterId, 145077, null);
			return false;
		}
		xbean.ClanInfo info = ClanUtils.getClanInfoById(memberId, true);
		if (info == null || !info.getMembers().containsKey(memberId)) {
			logger.error("玩家id "+masterId+"\t公会id "+"踢出的玩家已经不是公会成员，目标玩家id"+memberId);
			MessageMgr.psendMsgNotify(masterId, 160311, null);//提示目标玩家不是公会成员
			return false;
		}
		if(info.getKey()!=clanInfo.getKey()){
			logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"踢出的玩家和自己不是一个公会的，目标玩家id"+memberId);
			return false;
		}
		
		if (clanInfo.getClanmaster() == memberId){//帮主不能被驱逐
			return false;
		}
		//这里判断踢出权限
		xbean.ClanMemberInfo masterInfo = clanInfo.getMembers().get(masterId);
		xbean.ClanMemberInfo memberInfo = clanInfo.getMembers().get(memberId);
		
		//只有军团长以上的职位有任命权限
		if(masterInfo.getClanposition()>ClanPositionType.ClanArmyGroup4||masterInfo.getClanposition()<ClanPositionType.ClanMaster){
			logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"只有军团长以上的职位有任命或者踢出权限");
			return false;
		}
		if (masterInfo.getClanposition() > memberInfo.getClanposition()) {
			logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"改变职务者比将被改变职务者职务低或相等，不能操作。");
			return false;
		}
		if (clanInfo.getClanmaster() == memberId) {
			logger.error("玩家id "+masterId+"\t公会id "+clanInfo.getKey()+"现任帮主不能被踢出。");
			return false;
		}
		int position=memberInfo.getClanposition();
		//这里判断特殊军团任命
		if(checkArmyCanDo(masterInfo.getClanposition(),memberInfo.getClanposition())==false){
			return false;
		}
		if (clanInfo.getMembers().get(memberId) == null){
			return false;
		}
		int max=getMaxFireMemberNum();
		if(clanInfo.getFiremembernum()>=max){
			MessageMgr.psendMsgNotify(masterId, 162061, null);
			return false;
		}
		
		Long clanfightid = xtable.Roleid2clanfightid.select(memberId); //是否在公会战中 by changhao
		if (clanfightid != null) //如果此人在公会战中不能踢出 by changhao
		{
			MessageMgr.psendMsgNotify(masterId, 410054, null);
			return false;
		}
		
		//处理一下弹劾逻辑，发起弹劾和响应弹劾的人不能被踢出公会
		xbean.ImpeachMent impeachMent = clanInfo.getImpeachment();
		if(impeachMent!=null){
			if(impeachMent.getImpeachroleid()>0 && System.currentTimeMillis()-impeachMent.getImpeachtime()<=PRequestImpeachMent.IMPEACHMENT_TIME){
				if(impeachMent.getImpeachroleid()==memberId||impeachMent.getAcceptimpeachroleids().contains(memberId)){
					MessageMgr.psendMsgNotify(masterId, 172059, null);
					return false;
				}
			}
		}
		
		//记录踢出次数
		clanInfo.setFiremembernum(clanInfo.getFiremembernum()+1);
		fireMemeberClanResult(masterId,memberId,reasonType,clanInfo,position);//处理踢出玩家的逻辑
		logger.info("玩家id "+masterId+"职位 "+masterInfo.getClanposition()+"\t公会id "+clanInfo.getKey()+"踢出玩家，目标玩家id "+memberId+"职位\t"+position);
		return true;
	}
	
	
	/**
	 * 被驱逐离开统一处理逻辑
	 * @param masterId
	 * @param memberId
	 * @param reasonType
	 * @param clanInfo
	 */
	public void fireMemeberClanResult(long masterId,long memberId,int reasonType,xbean.ClanInfo clanInfo,int position){
		exitClan(memberId,masterId, clanInfo, FIRE_LEAVE_REASON);
		
		if (memberId == clanInfo.getClanvicemaster()){//如果驱逐的是副帮主
			clanInfo.setClanvicemaster(0);
		}

		String roleName = xtable.Properties.selectRolename(memberId);
		String masterroleName =""; //系统
		masterroleName=StringConstant.ID2String(masterroleName,StringConstant.SYSTEM_WORD);
		if(masterId>0){
			masterroleName = new PropRole(masterId, true).getName();
		}
		MessageMgr.psendSystemMessageToRole(memberId, 160188, Arrays.asList(masterroleName, getReasonContent(reasonType), Integer.toString(ClanUtils.getCurrentClanPoint(memberId)/2)));
		if(masterId>0){
			MessageMgr.psendMsgNotifyWhileCommit(masterId, 145096, 0, null);
		}
		//发送帮派消息
		MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 145419, 0, Arrays.asList(roleName,getReasonContent(reasonType)));
		
		
		
		//处理帮贡当玩家“主动退出公会”或“被踢出公会”时未消耗的现有贡献将冻结50%。
		ClanManage.dealContribution(memberId,ClanManage.STATE_KITE);
		//处理帮派事件
		ClanManage.addDealClanEventInfo(clanInfo,ClanManage.EVENT_KICK,memberId,roleName,getReasonContent(reasonType),masterroleName);
		//运营日志
		FactionOpbean factionOpbean = new FactionOpbean(clanInfo.getKey(), clanInfo.getClanname(), 
				FactionOpbean.TYPE_KICK, position, clanInfo.getMembers().size(),
				clanInfo.getClanlevel(), clanInfo.getHouse().entrySet());
		int weekpoint=fire.pb.clan.srv.ClanManage.getWeekclanpoint(memberId,clanInfo);
		fire.log.YYLogger.factionOPEventLog(memberId,factionOpbean,weekpoint);
		logger.info("玩家角色id "+memberId+"\t公会key\t"+clanInfo.getKey()+"被踢出公会，踢人的人角色id"+masterId);
	}
	
	/**
	 * 职位是否满员,这个只是用于任命
	 * @param position   要分配的职位
	 * @param number     职位数量
	 * @return
	 */
	private boolean isPositionFull(long masterId, int position,xbean.ClanInfo clanInfo) {
		//这里需要特殊处理会长的职位,会长可以任命会长
		if(position == ClanPositionType.ClanMaster){
			return false;
		}
		if(ClanUtils.isClanPositionFull(clanInfo,position)){
			if(position == ClanPositionType.ClanViceMaster){
				logger.error("请先撤销原来的副会长再来任命吧。");
				MessageMgr.sendMsgNotify(masterId, 145020, null);
				return true;
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 3.群发消息 
	 * @return
	 */
	public boolean sendClanMessage(long roleId, String message){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, true);
		
		if(clanInfo == null){
			MessageMgr.psendMsgNotify(roleId, 145077, null);
			return false;
		}
		// 职位小于军团长的不能发群消息
		if(clanInfo.getMembers().get(roleId).getClanposition() > ClanPositionType.ClanArmyGroup4){
			MessageMgr.psendMsgNotify(roleId, 145013, null);
			return false;
		}
		// 扣钱
		int cost=getSendMsgCostMoney();
		if (cost <=0){
			logger.error("玩家id "+roleId+"\t公会id "+clanInfo.getKey()+"群发消息，扣除金钱数量错误"+cost);
			return false;
		}
		
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		
		//判断是否有足够的钱
		if (bag.getMoney() < cost) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 160246, 0, null);
			return false;
		}
		if (bag.subMoney(-cost, "群发消息 ", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_qunfaxiaoxi, 0) == 0) {
			return false;
		}
		
		String[] params = {clanInfo.getClanname(), new PropRole(roleId,true).getName(), message};
		MessageMgr.psendSystemMessageToRoles(clanInfo.getMembers().keySet(), 142763, Arrays.asList(params));
		
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 145107, null);
		logger.info("玩家id "+roleId+"\t公会id "+clanInfo.getKey()+"群发消息，扣除金钱"+cost);
		return true;
	}
}
