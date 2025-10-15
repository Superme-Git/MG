package fire.pb.mission.util;

public class GMCommitParam implements IMissionCommitParam {

	@Override
	public EMissionCommit getCommitType() {
		return EMissionCommit.GM;
	}

	@Override
	public long getNpcKey() {
		return 0;
	}

}
