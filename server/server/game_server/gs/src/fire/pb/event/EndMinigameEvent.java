package fire.pb.event;

public final class EndMinigameEvent implements Event {
	private final long roleid;
	private final int gameid;
	public EndMinigameEvent(final long roleid, final int gameid) {
		this.roleid = roleid;
		this.gameid = gameid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

	public int getGameid() {
		return gameid;
	}
}
