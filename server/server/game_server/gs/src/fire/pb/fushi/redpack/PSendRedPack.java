package fire.pb.fushi.redpack;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fire.log.YYLogger;
import fire.log.beans.OpBonusBean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.clan.ClanUtils;
import fire.pb.fushi.SRedPackConfig;
import fire.pb.game.MoneyType;
import fire.pb.map.SceneManager;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.Misc;
import mkdb.Mkdb;
import mkdb.Procedure;
import mkdb.util.AutoKey;

public class PSendRedPack extends Procedure {
	public static final Logger logger = Logger.getLogger("REDPACK");
	
	private final long roleid;
	private final int userid;
	private final int modeltype; // 红包类型
	private final int fushinum; // 符石数量
	private final int redpacknum; // 红包数量
	private final String redpackdes; // 红包寄语
	
	public static final int MAX_DES=36;//最大寄语

	public PSendRedPack(int userid,long roleid,int modeltype,int fushinum,int redpacknum,String redpackdes) {
		this.roleid = roleid;
		this.userid = userid;
		this.modeltype = modeltype;
		this.fushinum = fushinum;
		this.redpacknum = redpacknum;
		this.redpackdes = redpackdes;
	}

	@Override
	protected boolean process() throws Exception {
		// 创建角色超出七天不绑定手机限制拍卖行和兑换所的使用
		if (fire.pb.tel.utils.TelBindUtils.isLimitByBindTel(roleid)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(", 红包发送错误！创建角色超出七天不绑定手机限制红包发送");
			logger.error(sbd.toString());
			return false;
		}
		//判断红包类型是否正确
		if(modeltype<RedPackType.TYPE_WORLD||modeltype>RedPackType.TYPE_TEAM){
			return false;
		}
		Team team=null;
		if(modeltype==RedPackType.TYPE_TEAM){
			team = TeamManager.selectTeamByRoleId(roleid);
		}
		xbean.ClanInfo clanInfo=null;
		if(modeltype==RedPackType.TYPE_CLAN){
			clanInfo = ClanUtils.getClanInfoById(roleid, true);
		}
		SRedPackConfig sRedPackConfigdata=RedPackManage.getSRedPackConfigdataByType(modeltype);
		if(sRedPackConfigdata==null){
			return false;
		}
		int proplevel=xtable.Properties.selectLevel(roleid);
		String rolename=xtable.Properties.selectRolename(roleid);
		if(proplevel<sRedPackConfigdata.getLevel()){
			//判断等级要求,提示等级不足
			MessageMgr.sendMsgNotify(roleid, 172010, null);
			return false;
		}
		long valueid=0;
		if(modeltype==RedPackType.TYPE_CLAN){
			//判断有没有工会
			Long clankey=xtable.Properties.selectClankey(roleid);
			if (clankey == null || clankey<=0) {
				MessageMgr.sendMsgNotify(roleid, 145077, null);
				return false;
			}
			valueid=clankey;
		}
		if(modeltype==RedPackType.TYPE_TEAM){
			//判断
			if(team==null){
				MessageMgr.sendMsgNotify(roleid, 140498, null);
				return false;
			}
			valueid=team.getTeamId();
		}
		//符石数量是否正确
		if(fushinum<sRedPackConfigdata.getFushimin()||fushinum>sRedPackConfigdata.getFishimax()){
			return false;
		}
		//判断红包数量是否正确
		if(redpacknum<sRedPackConfigdata.getPackmin()||redpacknum>sRedPackConfigdata.getPackmax()){
			return false;
		}
		int length=0;
		try {
			length=(new String(redpackdes.getBytes( "gb2312"), "ISO-8859-1")).length();
		} catch (UnsupportedEncodingException e) {
			return false;
		} 
		//判断红包寄语是否符合要求
		if(length>MAX_DES){
			return false;
		}
		
		//设置角色和红包id的映射关系
		xbean.RedPackInfoViewList redPackInfoList=xtable.Redpackinfoviewlisttab.get(1);
		if(redPackInfoList==null){
			redPackInfoList=xbean.Pod.newRedPackInfoViewList();
			xtable.Redpackinfoviewlisttab.insert(1, redPackInfoList);
		}
		//这里做一个删除红包列表的操作，如果数量超过了1000，就删除过期的
		if(redPackInfoList.getRedpackinfoviewlist().size()>1000){
			delRedpackinfoviewlist(redPackInfoList);
		}
		//发送红包个数是否达到上限
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
		if(sRRedPackNum.getRedpacksendnum()>=sRedPackConfigdata.getDaysendmax()){
			//提示今天发送红包数量达到上限
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172001, null);
			return false;
		}
		//判断今天发送的符石是否已经达到上限
		if(sRRedPackNum.getRedpacksendfushinum()+fushinum>sRedPackConfigdata.getDayfushisendmax()){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172022, null);
			return false;
		}
		//处理货币是否够
		if(delGoldOrFushi()==false){
			return false;
		}
		//处理红包逻辑
		int allmoney=fushinum;
		if(RedPackManage.isSendGold(modeltype)){//是否是发送金币的
			allmoney=fushinum;
		}else{
			allmoney=fushinum*100;
		}
		//这里处理一下，扣除发红包的税款
		allmoney=allmoney-(int) (allmoney*(RedPackManage.getSendPackTax()/100.0));
		List<Integer> redlist= createRandomRedPack(modeltype,allmoney,redpacknum);
		if(redlist.size()==0){
			return false;
		}
		
		
		//存储数据库
		xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.get(roleid);
		if(redPackRoleInfoList==null){
			redPackRoleInfoList=xbean.Pod.newRedPackRoleInfoList();
			xtable.Redpackroleinfolisttab.insert(roleid, redPackRoleInfoList);
		}
		long time=System.currentTimeMillis();
		AutoKey<Long> autoKey = Mkdb.getInstance().getTables().getTableSys().getAutoKeys().getAutoKeyLong("REDPACK_UID");
		Long uId = autoKey.next();
		if (uId == null){
			logger.error("红包，生成主键错误");
			return false;
		}
		String redpackid=modeltype+"_"+roleid+"_"+fushinum+"_"+redpacknum+"_"+time+"_"+uId;//生成红包的id  类型_角色id_金额_个数_时间
		xbean.RedPackInfo redpackinfo=redPackRoleInfoList.getRedpackinfolist().get(redpackid);
		if(redpackinfo==null){
			redpackinfo=xbean.Pod.newRedPackInfo();
			redPackRoleInfoList.getRedpackinfolist().put(redpackid, redpackinfo);
		}
		//设置红包信息
		redpackinfo.setRedpackid(redpackid); // 红包Id
		redpackinfo.setModeltype(modeltype); // 红包类型
		redpackinfo.setValuekey(valueid); // 工会或者队伍id
		redpackinfo.setSendroleid(roleid); // 角色Id
		redpackinfo.setRedpackdes(redpackdes); // 红包寄语
		redpackinfo.setRedpackstate(RedPackState.STATE_CANGET); // 红包状态
		redpackinfo.setRedpackallnum(redpacknum); // 红包总个数
		redpackinfo.setRedpackallmoney(allmoney); // 红包总金额
		
		redpackinfo.setSendtime(time); // 发送时间
		StringBuffer sb=new StringBuffer();
		for(int money:redlist){
			xbean.RedPackRoleHisInfo info=xbean.Pod.newRedPackRoleHisInfo();
			info.setRedpackmoney(money);
			redpackinfo.getRedpackrolehisinfolist().add(info);
			sb.append(money+"_");
		}
		
		xbean.RedPackBaseInfo base=xbean.Pod.newRedPackBaseInfo();
		base.setRoleid(roleid); // 角色Id
		base.setRedpackid(redpackid); // 红包Id
		base.setSendtime(time); // 发送时间
		redPackInfoList.getRedpackinfoviewlist().put(redpackid, base);
		
		//添加发送红包的数量
		sRRedPackNum.setRedpacksendnum(sRRedPackNum.getRedpacksendnum()+1);
		//设置今天发送的符石数量
		sRRedPackNum.setRedpacksendfushinum(sRRedPackNum.getRedpacksendfushinum()+fushinum);
		//设置玩家记录
		xbean.RoleRedPackRecord roleRedPackRecord=xtable.Roleredpackrecordtab.get(roleid);
		if(roleRedPackRecord==null){
			roleRedPackRecord=xbean.Pod.newRoleRedPackRecord();
			xtable.Roleredpackrecordtab.insert(roleid, roleRedPackRecord);
		}
		roleRedPackRecord.setSendnum(roleRedPackRecord.getSendnum()+1);
		int tokType=MoneyType.MoneyType_GoldCoin;
		if(RedPackManage.isSendGold(modeltype)){//是否是发送金币的
			roleRedPackRecord.setSendgold(roleRedPackRecord.getSendgold()+fushinum);
		}else{
			roleRedPackRecord.setSendfushi(roleRedPackRecord.getSendfushi()+fushinum);
			roleRedPackRecord.setSendgold(roleRedPackRecord.getSendgold()+fushinum*RedPackManage.CHANGE_NUM);
			tokType=MoneyType.MoneyType_HearthStone;
		}
		logger.info("角色id"+"\t"+roleid+"发送红包,红包类型\t"+modeltype+"\t红包id\t"+redpackid+"红包金额符石\t"+fushinum+"\t红包个数"+redpacknum+"生成红包金额的具体详情"+sb.toString());
		
		
		SSendRedPack sSendRedPack=new SSendRedPack();
		psendWhileCommit(roleid, sSendRedPack);
		
		//记录红包运营日志
		OpBonusBean opBonusBean = new OpBonusBean(modeltype, OpBonusBean.OpBonusBean_Payflg_Use, tokType, fushinum, redpacknum,roleid);
		YYLogger.bonusLog(roleid,opBonusBean);
		
		//通知所有在线玩家
		SNoticeRedPack sNoticeRedPack=new SNoticeRedPack();
		RedPackRoleTip redPackRoleTip=new RedPackRoleTip();
		redPackRoleTip.modeltype = modeltype;
		redPackRoleTip.redpackid = redpackid;
		redPackRoleTip.rolename = rolename;
		if(RedPackManage.isSendGold(modeltype)){//是否是发送金币的
			redPackRoleTip.fushi=redpackinfo.getRedpackallmoney();
		}else{
			redPackRoleTip.fushi=redpackinfo.getRedpackallmoney()/RedPackManage.CHANGE_NUM;
		}
		sNoticeRedPack.redpackroletip=redPackRoleTip;
		if(modeltype==RedPackType.TYPE_WORLD){
			SceneManager.sendAll(sNoticeRedPack);
		}else if(modeltype==RedPackType.TYPE_CLAN){
			if (clanInfo != null ) {
				psendWhileCommit(clanInfo.getMembers().keySet(), sNoticeRedPack);
			}
		}else if(modeltype==RedPackType.TYPE_TEAM){
			if(team!=null){
				psendWhileCommit(team.getAllMemberIds(), sNoticeRedPack);
			}
		}
		if(modeltype==RedPackType.TYPE_WORLD){
			//发送跑马灯提示
			MessageMgr.sendSystemMsg(172011, Arrays.asList(rolename,redpackinfo.getRedpackdes()));
			//发送链接
			MessageMgr.sendSystemMsg(172012,Arrays.asList(rolename,redpackinfo.getRedpackdes(),redpackinfo.getRedpackid(),redpackinfo.getModeltype()+""));
		}else if(modeltype==RedPackType.TYPE_CLAN){
			//发送公会消息
			if (clanInfo != null ) {
				MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 172015, 0,
						Arrays.asList(rolename,redpackinfo.getRedpackdes(),redpackinfo.getRedpackid(),redpackinfo.getModeltype()+""));
			}
		}else if(modeltype==RedPackType.TYPE_TEAM){
			if(team!=null){
				MessageMgr.psendMsgNotifyWhileCommit(team.getAllMemberIds(), 172016, 0,
						Arrays.asList(rolename,redpackinfo.getRedpackdes(),redpackinfo.getRedpackid(),redpackinfo.getModeltype()+""));
			}
		}
		
		//刷新界面
		mkdb.Procedure.pexecuteWhileCommit(new PSendRedPackView(roleid,modeltype,"0"));
		
		if (modeltype==RedPackType.TYPE_WORLD) //红包排行榜 by changhao
		{
			mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PWorldRedPackRankProc(roleid, fushinum));
		}
		
		return true;
	}
	
	/**
	 * 生成红包金额
	 * @return
	 */
	public List<Integer> createRandomRedPack(int modeltype,int allmoney,int redpacknum){
		List<Integer> list=new ArrayList<Integer>();
		getMoney(list,allmoney,redpacknum,1);
		return list;
		
	}
	
	/**
	 * 最小单位值<=X<int(当前剩余钱/当前剩余人数*2-最小单位值)
	 * @param money
	 * @param num
	 * @param minutil  最小单位默认是1
	 * @return
	 */
	public  boolean getMoney(List<Integer> list,int money, int num,int minutil){
		if(money<num){
			return false;
		}
        int redMoney = 0;//分配的红包钱
        int remain_money = money;//剩余钱
        
        for(int i=num;i>0;i--){
        	if(i==1){
        		redMoney=remain_money;
        	}else{
        		int maxtemp=(int) (remain_money/i*2-minutil);
        		redMoney=Misc.getRandomNotBetween(minutil, maxtemp);
        		if(redMoney==0){
        			redMoney=minutil;
        		}
        		remain_money=remain_money-redMoney;
        	}
        	list.add(redMoney);
        }
        return true;
    }
	


	/**
	 * 发送红包时候判断货币是否够，并且扣除对应货币
	 * @return
	 */
	public boolean delGoldOrFushi(){
		//点卡服务器公会和世界发送金币，其他发送符石
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
		long creditPoint = 0; 
		if(modeltype==RedPackType.TYPE_WORLD)
			creditPoint = (long)(fushinum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_WORLD_BONUS));
		else if(modeltype==RedPackType.TYPE_CLAN)
			creditPoint = (long)(fushinum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_CLAN_BONUS));
		else if(modeltype==RedPackType.TYPE_TEAM)
			creditPoint = (long)(fushinum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_TEAM_BONUS));
		long bagcreditPoint =  bag.getCreditPoint();
		if(creditPoint + bagcreditPoint < 0)
		{
			List<String> params = new ArrayList<String>();
			params.add("" + bagcreditPoint );
			params.add("" + (0-(creditPoint + bagcreditPoint)));
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 180021, params);	

			MessageMgr.sendSystemMessageToRole(roleid, 180026, null);
			return false;
		}
		if(RedPackManage.isSendGold(modeltype)){//是否是发送金币的
			//公会和世界发送金币
			if(bag.getGold()<fushinum){
				return false;
			}
			//扣除金币
			if (bag.subGold(-fushinum, "发送红包", YYLoggerTuJingEnum.tujing_Value_redpackout, 0) != -fushinum) {
				MessageMgr.sendMsgNotify(roleid, 145005, null);
				return false;
			}
		}else{
			//判断符石数量是否够
			// 获取符石
//			xbean.YbNums ybNums = xtable.Fushinum.get(userid);
//			if (ybNums == null){
//				return false;
//			}
//			xbean.YbNum ybNum = ybNums.getRoleyb().get(roleid);
//			if (ybNum == null){
//				return false;
//			}
//			if(ybNum.getNum()<fushinum){
//				//提示符石不足
//				return false;
//			}
			//扣除符石
			if (!fire.pb.fushi.FushiManager.subFushiFromUser(userid, roleid, fushinum, 0, 0,fire.pb.fushi.FushiConst.REASON_SUB_SEND_REDPACK,
					YYLoggerTuJingEnum.tujing_Value_redpackout,
					true)) {
				return false;
			}
		}		
		{
			if(modeltype==RedPackType.TYPE_WORLD)
				bag.addSysCurrency((long)(fushinum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_WORLD_BONUS))
						, MoneyType.MoneyType_EreditPoint, "发红包扣除",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpackout, 0);
			else if(modeltype==RedPackType.TYPE_CLAN)
				bag.addSysCurrency((long)(fushinum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_CLAN_BONUS))
						, MoneyType.MoneyType_EreditPoint, "发红包扣除",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpackout, 0);
			else if(modeltype==RedPackType.TYPE_TEAM)
				bag.addSysCurrency((long)(fushinum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_TEAM_BONUS))
						, MoneyType.MoneyType_EreditPoint, "发红包扣除",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpackout, 0);
		}
		return true;
	}
	
	/**
	 * 删除列表中过期的红包信息
	 * @param redPackInfoList
	 */
	public void delRedpackinfoviewlist(xbean.RedPackInfoViewList redPackInfoList){
		List<String> dellist = new LinkedList<String>();
		Map<String, xbean.RedPackBaseInfo> map=redPackInfoList.getRedpackinfoviewlist();
		for(xbean.RedPackBaseInfo base:map.values()){
			if(base == null){
				continue;
			}
			//删除2倍红包过期时间的红包信息
			if(System.currentTimeMillis()-base.getSendtime()>=RedPackManage.getValidRedPackTime()*2){
				dellist.add(base.getRedpackid());
			}
		}
		//删除
		for(String packid:dellist){
			redPackInfoList.getRedpackinfoviewlist().remove(packid);
		}
	}

}
