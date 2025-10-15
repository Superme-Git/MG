package fire.pb.friends;

/**
 * 说不得大师
 * 
 * @author yangtao
 * @dateTime 2016年6月23日 下午12:01:54
 * @version 1.0
 */
public class XSHUtils {

	public static final long SXH_ROLE_ID = Long.MAX_VALUE;

	private static XSHUtils _instance = new XSHUtils();

	public static XSHUtils getInstance() {
		return _instance;
	}

	/**
	 * 得到最终的角色id，-1代表说不得大师的id
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月23日 下午12:05:44
	 * @version 1.0
	 * @param roleId
	 * @return
	 */
	public Long getLastRoleId(long roleId) {
		if (roleId == -1) {
			return SXH_ROLE_ID;
		}
		return roleId;
	}
}
