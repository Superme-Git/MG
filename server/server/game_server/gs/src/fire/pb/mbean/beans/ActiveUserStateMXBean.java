package fire.pb.mbean.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.Module;
import fire.log.beans.ActiveUser;
import fire.log.beans.TotalValueState;

/**
 * 
 * 活跃账户相关类型
 * 周新增活跃账户 301
 * 周回流活跃账户 302
 * 周持续活跃账户 303
 * 周新增活跃流失账户 304
 * 周回流活跃流失账户 305
 * 周持续活跃流失账户 306
 * 
 * 日新增活跃账户 307
 * 日回流活跃账户 308
 * 日持续活跃账户 309
 * 日新增活跃流失账户 310
 * 日回流活跃流失账户 311
 * 日持续活跃流失账户 312
 * 
 *
 */
public interface ActiveUserStateMXBean {
	
	java.util.Map<Integer, Integer> getActiveUser();

}

class ActiveUserState implements ActiveUserStateMXBean{

	@Override
	public java.util.Map<Integer, Integer> getActiveUser() {
		TotalValueState state = Module.getLogAnalyser();
		List<ActiveUser> userList = state.getActiveUsersList();
		
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		for(ActiveUser user : userList){
			result.put(user.getActiveType(), user.getTotalNum());
		}
		
		return result;
	}
	
}