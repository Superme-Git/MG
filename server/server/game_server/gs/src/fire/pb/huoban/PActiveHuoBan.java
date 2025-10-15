package fire.pb.huoban;

import org.apache.log4j.Logger;

import fire.pb.game.MoneyType;
import fire.pb.item.Pack;
import fire.pb.npc.SHeroBaseInfo;
import fire.pb.product.Commontext;
import fire.pb.product.SErrorCode;
import fire.pb.util.CalendarUtil;
import mkdb.Procedure;

public class PActiveHuoBan extends Procedure {
	
	private long roleId;
	private int huobanId;
	
	
	private int activeType;
	private int activeTime;
	
	private Pack bag = null;
	private static Logger logger = Logger.getLogger("HUOBAN");
	
	public PActiveHuoBan(long roleId, int huobanId, int activeType, int activeTime) {
		this.roleId = roleId;
		this.huobanId = huobanId;
		this.activeType = activeType;
		this.activeTime = activeTime;
	}
	
	public boolean process() {
		try	
		{
			bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleId, fire.pb.item.BagTypes.BAG, false);
			final SErrorCode sErrorCode = new SErrorCode();
			
			SHeroBaseInfo hero = Module.getInstance().getHuoBanBaseInfo().get(huobanId);
			if (hero == null) {
				logger.error("huobaninfo is null" + huobanId);
				return false;
			}
			
			HuoBanColumn huobanCol = HuoBanColumn.getHuoBanColumn(roleId, false);
			HuoBan huoban = huobanCol.getHuoBan(huobanId);
			
			int nState = huoban.getHuoBanState();
			if (nState == 1) { //永久使用
				SActiveHuoBan activeHuoban = new SActiveHuoBan();
				activeHuoban.huobanid = this.huobanId;
				activeHuoban.state = nState;
				logger.info(new StringBuffer().append("HuoBanState is forever: ").append(huoban.getHuoBanState()).append(" SetState:").append(nState));
				Procedure.psendWhileCommit(roleId, activeHuoban);
				return true;
			}
			
			int nMoney = 0;
			switch (activeTime) 
			{
			case 0: //解锁7天
				nMoney = hero.day7_money.get(activeType);
				break;
			case 1: //解锁30天
				nMoney = hero.day30_money.get(activeType);
				break;
			case 2: //永久解锁
				nMoney = hero.forever_money.get(activeType);
				break;
			default:
				{
					sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
					Procedure.psendWhileCommit(roleId, sErrorCode);
					return true;
				}
			}
			
			if (activeType == 0) {
				if (nMoney > bag.getCurrency(MoneyType.MoneyType_RongYu)) {
					sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
					Procedure.psendWhileCommit(roleId, sErrorCode);
					return true;
				}
				//扣钱
				if (bag.subCurrency(-nMoney, MoneyType.MoneyType_RongYu, "解锁伙伴", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jihuohuoban, 0) != -nMoney) {
					sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
					Procedure.psendWhileCommit(roleId, sErrorCode);
					return true;
				}
			}
			else if (activeType == 1) {
				if (nMoney > bag.getMoney()) {
					sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
					Procedure.psendWhileCommit(roleId, sErrorCode);
					return true;
				}
				//扣钱
				if (bag.subMoney(-nMoney, "解锁伙伴", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jihuohuoban, 0) != -nMoney) {
					sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
					Procedure.psendWhileCommit(roleId, sErrorCode);
					return true;
				}
			}
			
			long nDays = 0;
			if (nState == 0 || nState == 2) { //0未解锁 2本周免费状态
				nDays = System.currentTimeMillis();
			}
			else if (nState == 3) { //3免费剩余天数
				nDays = huoban.getHuoBanTimes();
			}
			
			if (nState == 0 || nState == 3) {//0未解锁  3免费剩余天数
				switch (activeTime) 
				{
				case 0: //解锁7天				
					nDays += CalendarUtil.getTimeByContinueDays(7);
					nState = 3;
					break;
				case 1: //解锁30天
					nDays += CalendarUtil.getTimeByContinueDays(30);
					nState = 3;
					break;
				case 2: //永久解锁
					nState = 1;
					break;
				default:
					break;
				}
			}
			else if (nState == 2) {//本周免费
				switch (activeTime) 
				{
				case 0: //解锁7天				
					nDays = CalendarUtil.getThisWeekSunday() + CalendarUtil.getTimeByContinueDays(7);
					nState = 3;
					break;
				case 1: //解锁30天
					nDays = CalendarUtil.getThisWeekSunday() + CalendarUtil.getTimeByContinueDays(30);
					nState = 3;
					break;
				case 2: //永久解锁
					nState = 1;
					break;
				default:
					break;
				}
			}
			
			huoban.setHuoBanTimes(nDays);
			huoban.setHuoBanState(nState);
			logger.info(new StringBuffer().append("HuoBanState: ").append(huoban.getHuoBanState()).append(" SetState:").append(nState));
			
			SActiveHuoBan activeHuoban = new SActiveHuoBan();
			activeHuoban.huobanid = this.huobanId;
			if (nState == 3) {
				int timeLast = CalendarUtil.getLastListFormat(nDays) + 10; //分钟
				activeHuoban.state = timeLast;
			}
			else if (nState == 1)
				activeHuoban.state = nState;
			logger.info(new StringBuffer().append("activeHuoban ").append("roleid:").append(roleId).append(" huobanid:").append(huobanId)
					.append(" activeType:").append(activeType).append(" activeTime:").append(activeTime).append(" releaseTime:").append(huoban.getHuoBanFinalState()));
			Procedure.psendWhileCommit(roleId, activeHuoban);		
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error(new StringBuffer().append(e.toString()));
		}
		return false;
	}

}
