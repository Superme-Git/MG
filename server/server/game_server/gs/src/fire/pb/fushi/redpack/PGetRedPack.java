package fire.pb.fushi.redpack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.log.YYLogger;
import fire.log.beans.OpBonusBean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.clan.ClanUtils;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.PAddFuShi;
import fire.pb.fushi.SRedPackConfig;
import fire.pb.game.MoneyType;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class PGetRedPack extends Procedure {
	public static final Logger logger = Logger.getLogger("REDPACK");
	
	private final long roleid;
	private final int userid;
	private final int modeltype; // 红包类型
	private final String redpackid; // 红包id
	

	public PGetRedPack(int userid,long roleid,int modeltype,String redpackid) {
		this.roleid = roleid;
		this.userid = userid;
		this.modeltype = modeltype;
		this.redpackid = redpackid;
	}

	@Override
	protected boolean process() throws Exception {
		//判断红包类型是否正确
		if(modeltype<RedPackType.TYPE_WORLD||modeltype>RedPackType.TYPE_TEAM){
			return true;
		}
		String canget = RedPackManage.rolegetredpackrecords.get(redpackid);
		if(canget != null){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172014, null);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		
		Team team =null;
		if(modeltype==RedPackType.TYPE_TEAM){
			//判断
			team = TeamManager.selectTeamByRoleId(roleid);
		}
		SRedPackConfig sRedPackConfigdata=RedPackManage.getSRedPackConfigdataByType(modeltype);
		if(sRedPackConfigdata==null){
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		xbean.Properties prop=xtable.Properties.select(roleid);
		if(prop.getLevel()<sRedPackConfigdata.getLevel()){
			//判断等级要求,提示等级不足
			MessageMgr.sendMsgNotify(roleid, 172032, null);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		long clanid=0;
		long teamid=0;
		if(modeltype==RedPackType.TYPE_CLAN){
			//判断有没有工会
			Long clanKey = xtable.Properties.selectClankey(roleid);
			if (clanKey == null || clanKey <= 0) {
				MessageMgr.sendMsgNotify(roleid, 145077, null);
				noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
				return true;
			}
			clanid=clanKey.longValue();
		}
		if(modeltype==RedPackType.TYPE_TEAM){
			//判断
			if(team==null){
				MessageMgr.sendMsgNotify(roleid, 140498, null);
				noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
				return true;
			}
			teamid=team.getTeamId();
		}
		//判断红包是否可以领取
		xbean.RedPackInfoViewList redPackInfoList=xtable.Redpackinfoviewlisttab.select(1);
		if(redPackInfoList==null){
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		xbean.RedPackBaseInfo baseinfo=redPackInfoList.getRedpackinfoviewlist().get(redpackid);
		if(baseinfo==null){
			logger.info("角色id\t"+roleid+"\t红包不存在1,红包id\t"+redpackid+"\t红包类型"+modeltype);
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172003, null);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.get(baseinfo.getRoleid());
		if(redPackRoleInfoList==null){
			logger.info("角色id\t"+roleid+"\t红包不存在2,红包id\t"+redpackid+"\t红包类型"+modeltype);
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172003, null);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		xbean.RedPackInfo redpackinfo=redPackRoleInfoList.getRedpackinfolist().get(redpackid);
		if(redpackinfo==null){
			logger.info("角色id\t"+roleid+"\t红包不存在3,红包id\t"+redpackid+"\t红包类型"+modeltype);
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172003, null);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		if(redpackinfo.getRedpackreceivednum()>=redpackinfo.getRedpackallnum()){
			RedPackManage.rolegetredpackrecords.put(redpackid, redpackid);
			logger.info("角色id\t"+roleid+"\t红包已经领取完了,红包id\t"+redpackid+"\t红包类型"+modeltype);
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172014, null);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		//判断是否是自己队伍或者自己公会的红包
		if(modeltype==RedPackType.TYPE_CLAN){
			if(clanid!=redpackinfo.getValuekey()){
				logger.info("角色id\t"+roleid+"\t红包不是自己公会或者队伍的,红包id\t"+redpackid+"\t红包类型"+modeltype);
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172018, null);
				noticeRemoveRedPack(modeltype,redpackid,redpackinfo.getRedpackstate());
				return true;
			}
		}
		if(modeltype==RedPackType.TYPE_TEAM){
			if(teamid!=redpackinfo.getValuekey()){
				logger.info("角色id\t"+roleid+"\t红包不是自己公会或者队伍的,红包id\t"+redpackid+"\t红包类型"+modeltype);
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172018, null);
				noticeRemoveRedPack(modeltype,redpackid,redpackinfo.getRedpackstate());
				return true;
			}
		}
		//判断红包是否过期
		//过滤掉过期的红包
		if(System.currentTimeMillis()-redpackinfo.getSendtime()>=RedPackManage.getValidRedPackTime()){
			logger.info("角色id\t"+roleid+"\t红包过期,红包id\t"+redpackid+"\t红包类型"+modeltype);
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172003, null);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		//判断这个红包是否已经退回了
		if(redpackinfo.getBackflag()==1){
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		
		
		//判断这个玩家是否已经领取过这个红包
		boolean haveflag=false;
		for(int i=0;i<redpackinfo.getRedpackrolehisinfolist().size();i++){
			xbean.RedPackRoleHisInfo his=redpackinfo.getRedpackrolehisinfolist().get(i);
			if(his.getRoleid()==roleid){
				haveflag=true;//已经领取过了
				break;
			}
		}
		if(haveflag){
			//在内存中设置这个玩家领取了这个红包
			setGetRedpackCache();
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172013, null);
			logger.info("角色id\t"+roleid+"\t玩家已经领取过了红包,红包id\t"+redpackid+"\t红包类型"+modeltype);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_HAVE);
			return true;
		}
		//判断今天领取的红包是否已经达到上限
		xbean.SRRedPackNumList sRRedPackNumList=xtable.Srredpacknumlisttab.get(roleid);
		if(sRRedPackNumList==null){
			sRRedPackNumList=xbean.Pod.newSRRedPackNumList();
			xtable.Srredpacknumlisttab.insert(roleid, sRRedPackNumList);
		}
		xbean.SRRedPackNum sRRedPackNum=sRRedPackNumList.getSrredpacknumlist().get(modeltype);
		if(sRRedPackNum==null){
			sRRedPackNum= xbean.Pod.newSRRedPackNum();
			sRRedPackNumList.getSrredpacknumlist().put(modeltype, sRRedPackNum);
		}
		if(sRRedPackNum.getRedpackreceivenum()>=sRedPackConfigdata.getDayreceivemax()){
			//提示今天收红包数量达到上限
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172002, null);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_CANGET);
			return true;
		}
		xbean.RedPackRoleHisInfo his=redpackinfo.getRedpackrolehisinfolist().get(redpackinfo.getRedpackreceivednum());
		if(his==null){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172014, null);
			//没有可以领取的红包
			logger.info("角色id\t"+roleid+"\t玩家没有可以领取的红包,红包id\t"+redpackid+"\t红包类型"+modeltype);
			noticeRemoveRedPack(modeltype,redpackid,RedPackState.STATE_NONE);
			return true;
		}
		//添加次数
		sRRedPackNum.setRedpackreceivenum(sRRedPackNum.getRedpackreceivenum()+1);
		long time=System.currentTimeMillis();
		//设置红包领取
		his.setReceivetime(time);
		his.setRoleid(roleid);
		//设置领取索引
		redpackinfo.setRedpackreceivednum(redpackinfo.getRedpackreceivednum()+1);
		redpackinfo.setRedpackreceiveallmoney(redpackinfo.getRedpackreceiveallmoney()+his.getRedpackmoney());
		//设置玩家记录
		xbean.RoleRedPackRecord roleRedPackRecord=xtable.Roleredpackrecordtab.get(roleid);
		if(roleRedPackRecord==null){
			roleRedPackRecord=xbean.Pod.newRoleRedPackRecord();
			xtable.Roleredpackrecordtab.insert(roleid, roleRedPackRecord);
		}
		roleRedPackRecord.setReceivenum(roleRedPackRecord.getReceivenum()+1);//设置领取次数
		
		//记录领取的红包信息
		xbean.RoleReceiveRedPackRecordList roleReceiveRedPackRecordList=xtable.Rolereceiveredpackrecordtab.get(roleid);
		if(roleReceiveRedPackRecordList==null){
			roleReceiveRedPackRecordList=xbean.Pod.newRoleReceiveRedPackRecordList();
			xtable.Rolereceiveredpackrecordtab.insert(roleid, roleReceiveRedPackRecordList);
		}
		
		xbean.RoleReceiveRedPackRecord roleReceiveRedPackRecord=xbean.Pod.newRoleReceiveRedPackRecord();
		roleReceiveRedPackRecord.setSendroleid(redpackinfo.getSendroleid()); // 发送红包角色Id
		roleReceiveRedPackRecord.setRedpackid(redpackinfo.getRedpackid()); // 红包Id
		roleReceiveRedPackRecord.setReceivetime(time); // 领取时间
		roleReceiveRedPackRecord.setModeltype(modeltype);
		roleReceiveRedPackRecordList.getRolereceiveredpacklist().put(redpackinfo.getRedpackid(), roleReceiveRedPackRecord);
		
		//添加金币
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
		bag.addSysGold(his.getRedpackmoney(), "领取红包", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpack, 0);
/*		if( redpackinfo.getSendroleid() != roleid)
		{MTG-15781【BUG】【红包】抢自己发出的红包，需要增加信用值【0.0.190PC】 
*/			if(modeltype==RedPackType.TYPE_WORLD)
				bag.addSysCurrency((long)(his.getRedpackmoney()*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_WORLD_BONUS))
						, MoneyType.MoneyType_EreditPoint, "领红包中获得",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpack, 0);
			else if(modeltype==RedPackType.TYPE_CLAN)
				bag.addSysCurrency((long)(his.getRedpackmoney()*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_CLAN_BONUS))
						, MoneyType.MoneyType_EreditPoint, "领红包中获得",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpack, 0);
			else if(modeltype==RedPackType.TYPE_TEAM)
				bag.addSysCurrency((long)(his.getRedpackmoney()*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_TEAM_BONUS))
						, MoneyType.MoneyType_EreditPoint, "领红包中获得",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpack, 0);
//		}
		roleRedPackRecord.setReceivegold(roleRedPackRecord.getReceivegold()+his.getRedpackmoney());
		roleReceiveRedPackRecord.setReceivegold(his.getRedpackmoney()); // 收红包金币数量
		//通知
		fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172006, Arrays.asList(his.getRedpackmoney()+""));
		
		//设置当前红包状态
		if(redpackinfo.getRedpackreceivednum()>=redpackinfo.getRedpackallnum()){
			redpackinfo.setRedpackstate(RedPackState.STATE_NONE);
			RedPackManage.rolegetredpackrecords.put(redpackid, redpackid);
		}
		//在内存中设置这个玩家领取了这个红包
		setGetRedpackCache();
		
		logger.info("角色id"+"\t"+roleid+"领取红包,红包类型\t"+modeltype+"红包金额\t"+his.getRedpackmoney()+"\t红包id"+redpackid+"\t已经领取次数"+sRRedPackNum.getRedpackreceivenum());
		noticeGetRedPackResult(redpackinfo);//通知
		
		//完成抢到红包的奖励
		fire.pb.course.CourseManager.achieveCourse(roleid, fire.pb.course.CourseType.QIANG_HONG_BAO, 0, 0);
		
		fire.pb.course.CourseManager.checkAchieveCourse(roleid, fire.pb.course.CourseType.QIANG_HONG_BAO, 1, 0, (int)roleRedPackRecord.getReceivegold());
		
		//记录红包运营日志
		OpBonusBean opBonusBean = new OpBonusBean(modeltype, OpBonusBean.OpBonusBean_Payflg_Get, MoneyType.MoneyType_GoldCoin, his.getRedpackmoney(), 1 ,redpackinfo.getSendroleid());
		YYLogger.bonusLog(roleid,opBonusBean);
		
		return true;
	}
	
	public void setGetRedpackCache(){
		ArrayList<String> list=PSendRedPackView.recordsrolegetinfo.get(roleid);
		if(list==null){
			list = new ArrayList<String>();
			PSendRedPackView.recordsrolegetinfo.put(roleid, list);
		}
		list.add(redpackid);
	}
	
	
	/**
	 * 返回给客户端消息
	 * @param redpackinfo
	 */
	public void noticeGetRedPackResult(xbean.RedPackInfo redpackinfo){
		SGetRedPack sGetRedPack=new SGetRedPack();
		sGetRedPack.modeltype=modeltype; // 红包类型
		sGetRedPack.redpackid=redpackid; // 红包Id
		int state=redpackinfo.getRedpackstate();
		if(redpackinfo.getRedpackstate()!=RedPackState.STATE_NONE){
			state=RedPackState.STATE_HAVE;
		}
		sGetRedPack.state=state; // 红包状态
		if(RedPackManage.isSendGold(redpackinfo.getModeltype())){//是否是发送金币的
			sGetRedPack.fushinum=redpackinfo.getRedpackallmoney();
		}else{
			sGetRedPack.fushinum=redpackinfo.getRedpackallmoney()/RedPackManage.CHANGE_NUM;
		}
		
		sGetRedPack.successflag=1;
		psendWhileCommit(roleid, sGetRedPack);
	}
	
	/**
	 * 通知客户端移除
	 * @param modeltype
	 * @param redpackid
	 */
	public void noticeRemoveRedPack(int modeltype,String redpackid,int state){
		SGetRedPack sGetRedPack=new SGetRedPack();
		sGetRedPack.modeltype=modeltype; // 红包类型
		sGetRedPack.redpackid=redpackid; // 红包Id
		sGetRedPack.state=state; // 红包状态
		sGetRedPack.successflag=0;
		psendWhileCommit(roleid, sGetRedPack);
	}


	
	
	

}
