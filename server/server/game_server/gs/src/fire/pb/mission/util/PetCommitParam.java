package fire.pb.mission.util;

public class PetCommitParam implements IMissionCommitParam {
	private final int petkey;
	private final long npckey;
	public int getPetkey() {
		return petkey;
	}
	
	public PetCommitParam( final long npckey, final int petkey ) {
		this.petkey = petkey;
		this.npckey = npckey;
	}
	@Override
	public EMissionCommit getCommitType() {
		return EMissionCommit.PET;
	}
	@Override
	public long getNpcKey() {
		return npckey;
	}

}
