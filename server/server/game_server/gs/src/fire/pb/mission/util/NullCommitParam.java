package fire.pb.mission.util;

public class NullCommitParam implements IMissionCommitParam {
	
	@Override
	public EMissionCommit getCommitType() {
		return EMissionCommit.NULL;
	}

	@Override
	public long getNpcKey() {
		return 0;
	}

}
