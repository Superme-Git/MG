package fire.pb.mission.util;

public class MoneyCommitParam implements IMissionCommitParam {
	private final long money;
	private final long npckey;
	public long getMoney() {
		return money;
	}
	public MoneyCommitParam( final long npckey, final long money ) {
		this.money = money;
		this.npckey = npckey;
	}
	@Override
	public EMissionCommit getCommitType() {
		return EMissionCommit.MONEY;
	}
	@Override
	public long getNpcKey() {
		return npckey;
	}

}
