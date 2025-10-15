package fire.pb.event;

public class LevelContinueEvent implements Event {
	private final long roleid;
	private final int quest;
	
	public LevelContinueEvent(final long roleid, final int quest) {
		this.roleid = roleid;
		this.quest = quest;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}
	
	public int getQuestid() {
		return quest;
	}

}
