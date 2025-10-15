package fire.pb;

/**
 *  提供角色上下线状态的一个常用的操作
 *
 */
public class StateCommon {
	/*public static int getCurState(long roleid){
		final Integer state = xtable.Roleonoffstate.select(roleid);
		if (null != state){
			return state;
		}
		else return State.UNENTRY_STATE;
		
	}
	
	public static boolean allowEnterWorld(long roleid){
		final int curState = getCurState(roleid);
		switch (curState){
		// 在客户端请求登录时，下面这些状态是不允许登录的。
		case State.ENTRY_STATE:
		case State.PREENTRY_STATE:
		case State.PRETRUSTEESHIP_STATE:
		case State.PREOFFLINEPROTECT_STATE:
			return false;
		}
		return true;
	}*/
	
	/**
	 * 判断一个玩家是否确切的在场景中。
	 * @param roleid
	 * @return
	 */
	public static boolean isOnline(final long roleid){
		final Integer state = xtable.Roleonoffstate.select(roleid);
		if(null == state)
			return false;
		if (state == fire.pb.state.State.ENTRY_STATE)
			return true;
		else return false;
	}
	
	/**
	 * 判断玩家是否在线，包括15分钟的离线时间，也算在线
	 * @param roleid
	 * @return
	 */
	public static boolean isOnlineBuffer(final long roleid){
		final Integer state = xtable.Roleonoffstate.select(roleid);
		if(null == state)
			return false;
		if (state >= fire.pb.state.State.ENTRY_STATE&&state <= fire.pb.state.State.TRUSTEESHIP_STATE)
			return true;
		else return false;
	}
	
	/**
	 * 判断玩家是否在托管状态 by changhao
	 * @param roleid
	 * @return
	 */
	public static boolean isTrusteeshipState(final long roleid){
		final Integer state = xtable.Roleonoffstate.select(roleid);
		if(null == state)
			return false;
		if (state == fire.pb.state.State.TRUSTEESHIP_STATE)
			return true;
		else return false;
	}
	
	
	
}