package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Serviceinfos implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.ServiceInfo srcServiceInfo = xbean.Pod.newServiceInfoData(); 
		xbean.ServiceInfo dstServiceInfo = xbean.Pod.newServiceInfoData(); 
		srcServiceInfo.unmarshal(src_value);
		dstServiceInfo.unmarshal(dest_value);
		
		if(srcServiceInfo.getDays()>dstServiceInfo.getDays()){
			dstServiceInfo.setYear(srcServiceInfo.getYear()); // 年
			dstServiceInfo.setWeeks(srcServiceInfo.getWeeks()); // 当前开服第几周(今年的第几周)
			dstServiceInfo.setActiverecommend(srcServiceInfo.getActiverecommend()); // 当前推荐活动
			dstServiceInfo.setUpdatetime(srcServiceInfo.getUpdatetime()); // 上次更新天数的时间
			dstServiceInfo.setDays(srcServiceInfo.getDays()); // 累计开服天数
			
		}
		dest_value.clear();
		dstServiceInfo.marshal(dest_value);
		
		
		
		return true;
	}
	
}