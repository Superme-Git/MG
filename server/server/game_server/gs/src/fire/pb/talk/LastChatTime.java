package fire.pb.talk;

/**
 * 获得角色上次发言时间
 * 
 * @author liangyanpeng
 *
 */
public class LastChatTime {
	private xbean.ChatTime chatTime;
	private boolean readonly;

	public LastChatTime(long roleid, boolean readonly) {
		this.readonly = readonly;
		if (readonly)
			chatTime = xtable.Chattimetab.select(roleid);
		else
			chatTime = xtable.Chattimetab.get(roleid);
		if (null == chatTime) {
			if (readonly) {
				chatTime = xbean.Pod.newChatTimeData();
			} else {
				chatTime = xbean.Pod.newChatTime();
				xtable.Chattimetab.add(roleid, chatTime);
			}
		}
	}

	public long getLastWorldChatTime() {
		return chatTime.getLastworldchattime();
	}

	public void setLastWorldChatTime(long lastTime) {
		if (readonly)
			throw new IllegalArgumentException("Error Invoke");
		chatTime.setLastworldchattime(lastTime);
	}

	public long getLastCurrentChatTime() {
		return chatTime.getLastcurrentchattime();
	}

	public void setLastCurrentChatTime(long lastTime) {
		if (readonly)
			throw new IllegalArgumentException("Error Invoke");
		chatTime.setLastcurrentchattime(lastTime);
	}

	public long getLastProfessionChatTime() {
		return chatTime.getLastprofessionchattime();
	}

	public void setLastProfessionChatTime(long lastTime) {
		if (readonly)
			throw new IllegalArgumentException("Error Invoke");
		chatTime.setLastprofessionchattime(lastTime);
	}

	public long getLastClanChatTime() {
		return chatTime.getLastclanchattime();
	}

	public void setLastClanChatTime(long lastTime) {
		if (readonly)
			throw new IllegalArgumentException("Error Invoke");
		chatTime.setLastclanchattime(lastTime);
	}

	public long getLastTeamApplyChatTime() {
		return chatTime.getLastteamapplychattime();
	}

	public void setLastTeamApplyChatTime(long lastTime) {
		if (readonly)
			throw new IllegalArgumentException("Error Invoke");
		chatTime.setLastteamapplychattime(lastTime);
	}
}
