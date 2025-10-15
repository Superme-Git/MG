
package fire.pb.activity.reg;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import fire.log.Logger;
import fire.pb.util.DateValidate;
import fire.pb.util.singlefactory.SingleContainer;
import mkdb.Procedure;
import xbean.RegDay;

//签到
public class PReg extends Procedure {
	private long roleId;
	private RegAssistant regAssistant;
	private static Logger logger = Logger.getLogger("AWARD");

	public PReg(long roleId, RegAssistant assistant) {
		this.roleId = roleId;
		this.regAssistant = assistant;
	}

	public PReg(long roleId) {
		this(roleId, (RegAssistant) SingleContainer.getInstance(RegAssistant.class));
	}

	@Override
	protected boolean process() throws Exception {
		Calendar calen = Calendar.getInstance();
		int monthOfYear = regAssistant.getMonthOfYear(calen);
		int dayOfMonth = regAssistant.getDayOfMonth(calen);
		
		if(roleId < 0){
			logger.error("PReg roleid:" + roleId);
			return false;
		}

		xbean.RegRec regRec = regAssistant.getRecord(roleId);

		if (!regRec.getMonthmap().containsKey(monthOfYear)) {
			regRec.getMonthmap().put(monthOfYear, regAssistant.createMonth());
		}

		Map<Integer, RegDay> dayMap = regRec.getMonthmap().get(monthOfYear).getDaymap();
		
		//签到次数不允许大于天数
		int total = regAssistant.getRegTimesOfMonth(dayMap);
		if(total >= dayOfMonth){
			logger.error("PReg roleid:" + roleId + " total:" + total + " day:" + dayOfMonth);
			return false;
		}
		
		//角色创建时间
		long roleCreateTime = regAssistant.getRoleCreateTime(roleId);
		long now = calen.getTimeInMillis();
		if(roleCreateTime == 0 || now < roleCreateTime){
			logger.error("PReg roleid:" + roleId + " roleCreateTime:" + roleCreateTime + " now:" + now);
			return false;
		}
		
		int suppRegTimes = regAssistant.getSuppRegTimesOfMonth(roleId, monthOfYear);
		boolean isSuppReg = false;
		int suppRegDay = 0;
		
		// 初始化今天记录
		if (!dayMap.containsKey(dayOfMonth)) {
			dayMap.put(dayOfMonth, regAssistant.createDay());
		}
		
		//判断是不是补签
		RegDay sBeanDay = dayMap.get(dayOfMonth);
		if(sBeanDay.getRewardflag() > 0){
			if(suppRegTimes > 0){
				logger.info("PReg roleid:" + roleId + " suppsignnum:" + suppRegTimes );
				isSuppReg = true;
			}else{
				logger.error("PReg roleid:" + roleId + " suppsignnum:" + suppRegTimes + " day:" + dayOfMonth);
				return false;
			}
		}
		
		if(isSuppReg){
			for(int i1=1;i1<dayOfMonth;i1++){
				if (!dayMap.containsKey(i1)) {
					dayMap.put(i1, regAssistant.createDay());
				}
			}
			if (roleCreateTime != 0 
					&& !DateValidate.inTheSameMonth(roleCreateTime, now)) {
				//设置当天签到过
				for(int i2=1;i2<dayOfMonth;i2++){
					if (dayMap.get(i2).getRewardflag() <= 0) {
						dayMap.get(i2).setDay(1);
						dayMap.get(i2).setSuppregflag(1);
						suppRegDay = i2;
						break;
					}
				}
			}else{
				if(roleCreateTime != 0 
						&& DateValidate.inTheSameMonth(roleCreateTime, now)){
					int cDay = getCreateDate(roleCreateTime, now);
					//设置当天签到过
					for(int i3=cDay;i3<dayOfMonth;i3++){
						if (dayMap.get(i3).getRewardflag() <= 0) {
							dayMap.get(i3).setDay(1);
							dayMap.get(i3).setSuppregflag(1);
							suppRegDay = i3;
							break;
						}
					}
				}
			}
			if(suppRegDay == 0){
				//补签天数错误
				logger.info("PReg roleid:" + roleId + " suppRegDay:" + suppRegDay );
				return false;
			}
			
			return suppRegReward(suppRegDay);
		}
		
		// 签到
		dayMap.get(dayOfMonth).setDay(1);
		logger.info("PReg roleid:" + roleId + " day:" + dayOfMonth + "签到成功." );

		return regReward();
	}

	protected boolean regReward() {
		return new PRegReward(roleId).call();
	}
	
	protected boolean suppRegReward(int suppRegDay) {
		return new PSuppRegReward(roleId,suppRegDay).call();
	}
	
	//获取创建时的天数
	protected int getCreateDate(long roleCreateTime, long now) {
		Calendar calen = Calendar.getInstance();
		calen.setTime(new Date(roleCreateTime));
		int cDay = calen.get(Calendar.DAY_OF_MONTH);
		
		return cDay;
	}
}
