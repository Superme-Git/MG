package fire.pb.mbean.beans;

import java.util.List;

import fire.log.Module;
import fire.log.beans.ConsumeRole;
import fire.log.beans.TotalValueState;

/**
 * 
 * 消耗角色相关类型
 * 周新增消耗角色 401
 * 周回流消耗角色 402
 * 周持续消耗角色 403
 * 周新增消耗流失角色 404
 * 周回流消耗流失角色 405
 * 周持续消耗流失角色 406
 * 
 * 日新增消耗角色 407
 * 日回流消耗角色 408
 * 日持续消耗角色 409
 * 日新增消耗流失角色 410
 * 日回流消耗流失角色 411
 * 日持续消耗流失角色 412
 * 
 *
 */
public interface ConsumeRoleStateMXBean {
	
	java.util.Map<ConsumeRoleKey, FushiStateValue> getConsumeRoleState();

}

class ConsumeRoleState implements ConsumeRoleStateMXBean{

	@Override
	public java.util.Map<ConsumeRoleKey, FushiStateValue> getConsumeRoleState() {
		TotalValueState state = Module.getLogAnalyser();
		List<ConsumeRole> roleList = state.getConsumeRolesList();
		
		java.util.Map<ConsumeRoleKey, FushiStateValue> result = new java.util.HashMap<ConsumeRoleKey, FushiStateValue>();
		for(ConsumeRole role : roleList){
			ConsumeRoleKey key = new ConsumeRoleKey(role.getConsumeType(), role.getLevel(), role.getConsumeValue(), role.getSchool(), role.getShape(), role.getOnlineTime());
			FushiStateValue value = new FushiStateValue(role.getRoleNum(), role.getTotalNum());
			result.put(key, value);
		}
		
		return result;
	}
	
}
