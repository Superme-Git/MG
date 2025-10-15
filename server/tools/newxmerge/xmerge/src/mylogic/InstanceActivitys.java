package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class InstanceActivitys implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.InstanceActivity src_instanceActivitys = xbean.Pod.newInstanceActivityData();
		xbean.InstanceActivity dest_instanceActivitys = xbean.Pod.newInstanceActivityData();
		src_instanceActivitys.unmarshal(src_value);
		dest_instanceActivitys.unmarshal(dest_value);
		
		dest_instanceActivitys.setInstanceid(src_instanceActivitys.getInstanceid()); // 副本id,跟任务配置.xlsx中的id对应
		dest_instanceActivitys.setState(src_instanceActivitys.getState()); // 状态,0=未开始；1=已开始；2=已结束
		dest_instanceActivitys.setBaseid(src_instanceActivitys.getBaseid()); // 活动基础id
		dest_instanceActivitys.setStarttime(src_instanceActivitys.getStarttime()); // 开始时间
		dest_instanceActivitys.setEndtime(src_instanceActivitys.getEndtime()); // 结束时间
		
		dest_value.clear();
		dest_instanceActivitys.marshal(dest_value);
		
		return true;
	}
	
}