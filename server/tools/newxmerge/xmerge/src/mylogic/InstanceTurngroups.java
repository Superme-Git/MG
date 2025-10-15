package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class InstanceTurngroups implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.InstanceTurnGroup src_InstanceTurnGroups = xbean.Pod.newInstanceTurnGroupData();
		xbean.InstanceTurnGroup dest_InstanceTurnGroups = xbean.Pod.newInstanceTurnGroupData();
		src_InstanceTurnGroups.unmarshal(src_value);
		dest_InstanceTurnGroups.unmarshal(dest_value);
		
		dest_InstanceTurnGroups.setTurngroup(src_InstanceTurnGroups.getTurngroup()); // 轮换组id
		dest_InstanceTurnGroups.setTurnindex(src_InstanceTurnGroups.getTurnindex()); // 轮换组当前id
		dest_InstanceTurnGroups.setTurntype(src_InstanceTurnGroups.getTurntype()); // 轮换类型
		
		
		dest_value.clear();
		dest_InstanceTurnGroups.marshal(dest_value);
		
		return true;
	}
	
}