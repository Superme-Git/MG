package fire.pb.mbean.beans;

import java.util.List;

import fire.log.Module;
import fire.log.beans.ActiveRole;
import fire.log.beans.TotalValueState;

/**
 * 
 * 活跃角色相关类型
 * 周新增活跃角色 101
 * 周回流活跃角色 102
 * 周持续活跃角色 103
 * 周新增活跃流失角色 104
 * 周回流活跃流失角色 105
 * 周持续活跃流失角色 106
 * 
 * 日新增活跃角色 107
 * 日回流活跃角色 108
 * 日持续活跃角色 109
 * 日新增活跃流失角色 110
 * 日回流活跃流失角色 111
 * 日持续活跃流失角色 112
 * 
 *
 */

public interface ActiveRoleStateMXBean {
	
	java.util.Map<ActiveRoleKey, Integer> getActiveRoleState();

}

class ActiveRoleState implements ActiveRoleStateMXBean{

	@Override
	public java.util.Map<ActiveRoleKey, Integer> getActiveRoleState() {
		TotalValueState state = Module.getLogAnalyser();
		List<ActiveRole> roleList = state.getActiveRoleList();
		
		java.util.Map<ActiveRoleKey, Integer> result = new java.util.HashMap<ActiveRoleKey, Integer>();
		for(ActiveRole role : roleList){
			ActiveRoleKey key = new ActiveRoleKey(role.getActiveType(), role.getLevel(), role.getSchool(), role.getShape(), role.getOnlineTime());
			result.put(key, role.getTotalNum());
		}
		
		return result;
	}
	
}
