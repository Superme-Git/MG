package robot.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.clan.ClanMgr;

public class TestUtils {

	public static List<Integer> giftList = new ArrayList<>();
	static {
		giftList.add(339103);
		giftList.add(339104);
	}

	/**
	 * 随机找赠送的玩家id
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月27日 上午2:18:56
	 * @version 1.0
	 * @return
	 */
	public static Long getGiveGiftRoleId(Long roleId) {
		int roles_num = LoginRoleMgr.getInstance().getRoles().size();
		if (roles_num > 0) {
			int index = ClanMgr.getInstance().getRandom(roles_num);
			return (Long) LoginRoleMgr.getInstance().getRoles().keySet().toArray()[index];
		}
		return roleId;
	}

	/**
	 * 随机找赠送的玩家id
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月27日 上午2:18:56
	 * @version 1.0
	 * @return
	 */
	public static Long getFrendId(Long roleId, List<Long> friendList) {
		Map<Long, LoginRole> dataMap = new HashMap<Long, LoginRole>(1000);
		dataMap.putAll(LoginRoleMgr.getInstance().getRoles());
		dataMap.remove(roleId);
		int roles_num = dataMap.size();
		if (roles_num > 0) {
			for (long frendId : friendList) {
				dataMap.remove(frendId);
			}
			roles_num = dataMap.size();
			if (roles_num <= 0)
				return null;
			int index = ClanMgr.getInstance().getRandom(roles_num);
			long frendId = (Long) dataMap.keySet().toArray()[index];
			return frendId;
		}
		return null;
	}
}
