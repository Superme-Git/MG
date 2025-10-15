
package fire.pb.activity.reg;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import fire.pb.util.DateValidate;
import fire.pb.util.singlefactory.SingleContainer;
import mkdb.Procedure;
import xbean.RegDay;


//查询玩家签到情况
public class PQueryRegRec extends Procedure {
	private long roleId;
	private RegAssistant regAssistant;

	public PQueryRegRec(long roleId, RegAssistant assistant) {
		this.roleId = roleId;
		this.regAssistant = assistant;
	}

	public PQueryRegRec(long roleId) {
		this(roleId, (RegAssistant) SingleContainer.getInstance(RegAssistant.class));
	}

	@Override
	protected boolean process() throws Exception {
		Calendar calen = Calendar.getInstance();

		int monthOfYear = regAssistant.getMonthOfYear(calen);

		int dayOfMonth = regAssistant.getDayOfMonth(calen);

		xbean.RegRec record = regAssistant.getRecord(roleId);
		
		//角色创建时间
		long createTime = regAssistant.getRoleCreateTime(roleId);
		long now = calen.getTimeInMillis();
		
		//检查是否月变换
		checkOutDate(record.getLastregtime(), now);

		Map<Integer, RegDay> days = Collections.emptyMap();

		// 该月有记录
		if (record.getMonthmap().containsKey(monthOfYear)) {
			days = record.getMonthmap().get(monthOfYear).getDaymap();
		}

		sendRegMsg(days, monthOfYear, dayOfMonth, createTime, now);
		
		return true;
	}

	protected void sendRegMsg(Map<Integer, RegDay> regDayMap, int month, int today, long roleCreateTime, long now) {
		if (regDayMap == null) {
			return;
		}
		
		SQueryRegData msg = new SQueryRegData();
		msg.month = month;
		
		msg.rewardflag = 
				regDayMap.containsKey(today) ? regDayMap.get(today).getRewardflag() : 0;
				
		int cansuppregtimes = 0;
				
		//计算签到天数
		for (int day : regDayMap.keySet()) {
			if (regDayMap.get(day).getSuppregflag() <= 0)
				continue;
				
			msg.suppregdays.add(day);
		}
		
		if (roleCreateTime != 0 
				&& !DateValidate.inTheSameMonth(roleCreateTime, now)) {
			//计算可签到天数
			for(int di=1;di<today;di++){
				RegDay rDay = regDayMap.get(di);
				if(rDay != null){
					if (rDay.getSuppregflag() <= 0 && rDay.getRewardflag() <= 0){
						cansuppregtimes++;
					}
				}else{
					cansuppregtimes++;
				}
			}
		}else{
			if(roleCreateTime != 0 
					&& DateValidate.inTheSameMonth(roleCreateTime, now)){
				Calendar calen = Calendar.getInstance();
				calen.setTime(new Date(roleCreateTime));
				int cDay = calen.get(Calendar.DAY_OF_MONTH);
				//计算可签到天数
				for(int di=cDay;di<today;di++){
					RegDay rDay = regDayMap.get(di);
					if(rDay != null){
						if (rDay.getSuppregflag() <= 0 && rDay.getRewardflag() <= 0){
							cansuppregtimes++;
						}
					}else{
						cansuppregtimes++;
					}
				}
			}
		}
		
		msg.times = regAssistant.getRegTimesOfMonth(regDayMap);
		msg.suppregtimes = regAssistant.getSuppRegTimesOfMonth(roleId, month);
		msg.cansuppregtimes = cansuppregtimes;
		gnet.link.Onlines.getInstance().send(roleId, msg);
	}

	//检查是否过期
	protected void checkOutDate(long lastRegTime, long now) {
		if (lastRegTime < 1) {
			return;
		}
		
		//检查是否月变换
		if (!DateValidate.inTheSameMonth(lastRegTime, now)) {
			new PRmOutOfDateRec(roleId, lastRegTime).call();
		}
	}

}
