package fire.pb.mission.util;

public class AnswerCommitParam implements IMissionCommitParam {
	private final int answerid;
	private final long npckey;
	public int getAnswerid() { return answerid; }
	@Override
	public long getNpcKey() { return npckey; }
	public AnswerCommitParam( final long npckey, final int answerid ) {
		this.answerid = answerid;
		this.npckey = npckey;
	}
	@Override
	public EMissionCommit getCommitType() {
		return EMissionCommit.ANSWER;
	}

}
