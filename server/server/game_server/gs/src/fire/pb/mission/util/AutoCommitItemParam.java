package fire.pb.mission.util;

public class AutoCommitItemParam implements IMissionCommitParam {
	private final long npckey;
	public AutoCommitItemParam(long npckey) {
		this.npckey = npckey;
	}
	@Override
	public EMissionCommit getCommitType() {
		return EMissionCommit.SIMPLEITEM;
	}

	@Override
	public long getNpcKey() {
		return npckey;
	}

}
