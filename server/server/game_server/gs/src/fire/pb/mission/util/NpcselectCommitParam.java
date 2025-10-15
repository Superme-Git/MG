package fire.pb.mission.util;

public class NpcselectCommitParam implements IMissionCommitParam {
	private final int optionid;
	private final long npckey;
	public int getOptionid() {
		return optionid;
	}
	public NpcselectCommitParam( final long npckey, final int optionid ) {
		this.optionid = optionid;
		this.npckey = npckey;
	}
	@Override
	public EMissionCommit getCommitType() {
		return EMissionCommit.NPCSELECT;
	}
	
	@Override
	public long getNpcKey() { return npckey; }
}
