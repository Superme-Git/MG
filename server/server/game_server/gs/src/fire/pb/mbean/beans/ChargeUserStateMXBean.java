package fire.pb.mbean.beans;

import java.util.List;

import fire.log.Module;
import fire.log.beans.ChargeUser;
import fire.log.beans.TotalValueState;

/**
 * 
 * 付费账户相关类型
 * 周新增付费账户 201
 * 周回流付费账户 202
 * 周持续付费账户 203
 * 周新增付费流失账户 204
 * 周回流付费流失账户 205
 * 周持续付费流失账户 206
 * 
 * 日新增付费账户 207
 * 日回流付费账户 208
 * 日持续付费账户 209
 * 日新增付费流失账户 210
 * 日回流付费流失账户 211
 * 日持续付费流失账户 212
 * 
 *
 */
public interface ChargeUserStateMXBean {
	
	java.util.Map<ChargeUserKey, FushiStateValue> getChargeUserState();

}

class ChargeUserState implements ChargeUserStateMXBean{

	@Override
	public java.util.Map<ChargeUserKey, FushiStateValue> getChargeUserState() {
		TotalValueState state = Module.getLogAnalyser();
		List<ChargeUser> userList = state.getChargeUsersList();
		
		 java.util.Map<ChargeUserKey, FushiStateValue> result = new java.util.HashMap<ChargeUserKey, FushiStateValue>();
		 for(ChargeUser user : userList){
			 ChargeUserKey key = new ChargeUserKey(user.getChargeType(), user.getChargeValue());
			 FushiStateValue value = new FushiStateValue(user.getUserNum(), user.getTotalNum());
			 result.put(key, value);
		 }
		 
		 return result;
	}
	
}