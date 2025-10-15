
package fire.pb.activity.reg;

import java.util.Calendar;
import java.util.Map;

import xbean.RegDay;
import fire.pb.util.singlefactory.ICreateable;

//签到工具类
public class RegAssistant implements ICreateable{

	//获得月份
	public int getMonthOfYear(Calendar calen){
		return calen.get(Calendar.MONTH)+1;
	}
	
	//获得当前天是本月的第几天
	public int getDayOfMonth(Calendar calen){
		return calen.get(Calendar.DAY_OF_MONTH);
	}
	
	
	//获得用户签到的记录
	public xbean.RegRec getRecord(long roleId){
		xbean.RegRec regRec=xtable.Regtbl.get(roleId);
		if (regRec==null) {
			regRec=xbean.Pod.newRegRec();
			xtable.Regtbl.insert(roleId,regRec);
		}
		
		return regRec;
	}
	
	public xbean.RegMonth createMonth(){
		return xbean.Pod.newRegMonth();
	}
	
	public xbean.RegDay createDay(){
		return xbean.Pod.newRegDay();
	}
	
	
	//获得本月签到次数
	public int getRegTimesOfMonth(Map<Integer,RegDay> dayMap){
		if (dayMap==null||dayMap.isEmpty()) {
			return 0;
		}
		
		int cnt=0;
		
		for(RegDay rdVal:dayMap.values()){
			//已签到的次数
			if (rdVal.getDay()>0) {
				cnt+=1;
			}
		}
		
		return cnt;
	}
	

	//获得本月的补签次数
	public int getSuppRegTimesOfMonth(long roleId, int month){
		xbean.RegRec regRec = getRecord(roleId);
		int suppRegTimes = 0;
		
		if (regRec.getMonthmap().containsKey(month)) {
			suppRegTimes = regRec.getMonthmap().get(month).getSuppregnum();
		}
		
		return suppRegTimes;
	}

	protected int getVipLv(long roleId){
		return 0;
	}
	
	protected long getRoleCreateTime(long roleId){
		long createTime = 0;
		xbean.Properties pro = xtable.Properties.get(roleId);
		if(pro == null)
			return createTime;
		
		createTime = pro.getCreatetime();
		
		return createTime;
	}
	
	@Override
	public void init(Map<?, ?> map) {
		// TODO Auto-generated method stub
		
	}
	
	
}
