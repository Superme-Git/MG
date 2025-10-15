package fire.pb.circletask;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircTaskConf;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.game.MoneyType;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.util.MessageUtil;


public class RenXingCircleTaskProc extends mkdb.Procedure{
	private int userid;
	private long roleid;
	private int circletype;
	private int moneytype;
	
	public RenXingCircleTaskProc(int userid, long roleid, int circletype, int state, int moneytype){
		this.userid = userid;
		this.roleid = roleid;
		this.circletype = circletype;
		this.moneytype = moneytype;
	}
	
	public boolean process() {
		final CircleTask sq = new CircleTask(roleid, false);
		if(!sq.canRenXingCircTask(roleid, circletype)) { //判断给任务是否可以任性
			CircleTaskManager.logger.error("角色[" + roleid + "]当前循环类型任务" + circletype + "不可任性！");
			return false;
		}
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, circletype);
		if (ct == null)
			return false;
		
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleid);
		if (quest_map == null)
			return false;
		
		int curRenTimes = sq.getRenXingCircTaskCount(roleid, circletype);
		CircleTaskManager.logger.debug("角色[" + roleid + "]当前循环类型:" + circletype + ",已任性次数:" + curRenTimes);
		//根据moneytype以及任性次数得出扣钱数量！
		int totalcost = CircleTaskManager.getInstance().getRenXingCost(curRenTimes, moneytype);
		CircleTaskManager.logger.debug("角色[" + roleid + "]当前任性次数:" + curRenTimes + ",应消耗货币:" + moneytype + ",共计:" + totalcost);
		if (totalcost >= 0) {
			if (moneytype == MoneyType.MoneyType_HearthStone) {
				if (!FushiManager.subFushiFromUser(userid, roleid, totalcost, 0, 0, FushiConst.REASON_RENXING_CT, YYLoggerTuJingEnum.tujing_Value_renxing, true)) {
					//符石不足
					CircleTaskManager.logger.error("角色[" + roleid + "]符石不足,不可任性！");
					return false;
				}
			} else if (moneytype == MoneyType.MoneyType_ShengWang) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				long ret = bag.subCurrency(totalcost, MoneyType.MoneyType_ShengWang, "任性马戏团", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_renxingmaxituan, 0);
				if(ret > 0){
					MessageUtil.pSendAddOrRemoveCurrency(roleid, ret, MoneyType.MoneyType_ShengWang);
				} else {
					RoleAnYeTask.logger.error("角色[" + roleid + "]任性马戏团:subCurrency shengwang error!ret=" + ret);
					return false;
				}
			}
		} else {
			CircleTaskManager.logger.error("角色[" + roleid + "]货币消耗配置计算错误！");
			return false;
		}
		
		xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(circletype);
		if (null == sqinfo) {
			CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务信息null！");
			return false;
		}
		CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
		if(null == ctc) {
			CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务类型" + sqinfo.getQuesttype() + "配置null！");
			return false;
		}
		sqinfo.setQueststate(SpecialQuestState.DONE);
		
		final PropRole prole = new PropRole(roleid,true);
		long masterkey = CircleTaskManager.getInstance().getRoleMastaerKey(roleid,prole.getSchool());
		fire.pb.map.Npc npc = SceneNpcManager.selectNpcByKey(masterkey);
		CircleTask.giveCTAward(roleid, prole.getLevel(), circletype, npc, sqinfo, null, false);
		
		//sq.clearSpecialQuestInfo(sqinfo);
		CircleTaskManager.getInstance().refreshQuestState(roleid, circletype, SpecialQuestState.SUCCESS);
		if (ct.getTaskcnt() == 2) {
			sqinfo.setSumnum(sqinfo.getSumnum() + 1);
			int circleMax = CircleTaskManager.getInstance().getCTCircleMax(ct);
			if (sqinfo.getSumnum() >= circleMax) {
				if (circletype == 1060000) {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150551, null);
				}
				else {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ct.getMaxnumtips(), null);
				}
			}
		}
		sq.addCircleTaskCompleteTimes(roleid, circletype);
		sq.dealGaoJiangCircleTask(circletype, ct);
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
		if (actrole != null && ct.getActiveid() > 0){
			actrole.handleActiveLivenessData(ct.getActiveid());
		}
		sq.clearSpecialQuestInfo(sqinfo);
		if(ct.gjhuanshu == sqinfo.getSumnum()) {
			CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务" + circletype + "达到高奖环数 ！");
			sq.addRenXingCircTaskCount(roleid, circletype);
			return true;
		}
		
		try {
			CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务" + circletype + "开始自动接受 ！");
			new PAutoAddCircleTask(roleid, circletype).call();
			sq.addRenXingCircTaskCount(roleid, circletype);
			return true;
		} catch (Exception e) {
			CircleTaskManager.logger.error("上一环完成发送自动循环师门任务异常：" , e);
		}
		
		return false;
	}
}
