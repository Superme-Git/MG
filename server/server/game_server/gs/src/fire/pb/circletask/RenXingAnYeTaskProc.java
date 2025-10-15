package fire.pb.circletask;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.game.MoneyType;
import fire.pb.main.ConfigManager;
import fire.pb.util.MessageUtil;

public class RenXingAnYeTaskProc extends mkdb.Procedure{
	private int userid;
	private long roleid;
	private int taskpos;
	private int moneytype;
	
	public RenXingAnYeTaskProc(int userid, long roleid, int taskpos, int moneytype){
		this.userid = userid;
		this.roleid = roleid;
		this.taskpos = taskpos;
		this.moneytype = moneytype;
	}
	
	public boolean process() {
		RoleAnYeTask rayt = new RoleAnYeTask(roleid);
		xbean.AnYeTask ayt = rayt.getTasks().get(taskpos);
		if (ayt == null)
			return false;
		if(ayt.getState() == SpecialQuestState.SUCCESS)
			return false;
		RoleAnYeTask.logger.info("角色[" + roleid + "]暗夜马戏任务" + 1080000 + "的" + ayt.getId() + ",类型" + ayt.getKind() + "准备任性!");
		final AnYeMaXiTuanConf conf = ConfigManager.getInstance().getConf(AnYeMaXiTuanConf.class).get(ayt.getId());
		if (conf == null)
			return false;
		//判断给任务是否可以任性
		if (conf.isrenxing <= 0)
			return false;
		
		//任性次数
		int curRenTimes = rayt.getAnYeData().getRenxins();
		RoleAnYeTask.logger.info("角色[" + roleid + "]当前暗夜任务已任性次数:" + curRenTimes);
		//根据moneytype以及任性次数得出扣钱数量！
		int totalcost = CircleTaskManager.getInstance().getRenXingCost(curRenTimes + 1, moneytype);
		RoleAnYeTask.logger.info("角色[" + roleid + "]当前任性次数:" + curRenTimes + ",应消耗货币:" + moneytype + ",共计:" + totalcost);
		if (totalcost >= 0) {
			if (moneytype == MoneyType.MoneyType_HearthStone) {
				if (!FushiManager.subFushiFromUser(userid, roleid, totalcost, 0, 0, FushiConst.REASON_RENXING_CT, YYLoggerTuJingEnum.tujing_Value_renxing, false)) {
					//符石不足
					RoleAnYeTask.logger.error("角色[" + roleid + "]符石不足" + totalcost + ",不可任性！当前任性次数:" + curRenTimes + ",货币类型:" + moneytype);
					return false;
				}
			} else if (moneytype == MoneyType.MoneyType_ShengWang) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				long ret = bag.subCurrency(-totalcost, MoneyType.MoneyType_ShengWang, "任性马戏团", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_renxingmaxituan, 0);
				if(ret == -totalcost){
					//MessageUtil.pSendAddOrRemoveCurrency(roleid, ret, MoneyType.MoneyType_ShengWang);
				} else {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 166086, null);
					RoleAnYeTask.logger.error("角色[" + roleid + "]任性马戏团:subCurrency shengwang error!ret=" + ret + ",totalcost=" + totalcost);
					return false;
				}
			} else if (moneytype == MoneyType.MoneyType_GoldCoin) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				long ret = bag.subGold(-totalcost, "任性马戏团", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_renxing, 0);
				if(ret == -totalcost){
					//MessageUtil.pSendAddOrRemoveCurrency(roleid, ret, MoneyType.MoneyType_GoldCoin);
				} else {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 160118, null);
					RoleAnYeTask.logger.error("角色[" + roleid + "]任性马戏团:subGold error!ret=" + ret + ",totalcost=" + totalcost);
					return false;
				}
			}
		} else {
			RoleAnYeTask.logger.error("角色[" + roleid + "]货币消耗配置计算错误！当前任性次数:" + curRenTimes + ",货币类型:" + moneytype);
			return false;
		}
		
		rayt.renXinTask(taskpos);
		return true;
	}
}
