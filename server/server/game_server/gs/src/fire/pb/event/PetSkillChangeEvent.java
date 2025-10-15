package fire.pb.event;
/**
 * 宠物技能改变事件
 * @author XGM
 */
public class PetSkillChangeEvent implements Event {
	private final long roleid;
	private final int petkey;

	public PetSkillChangeEvent( final long roleid, final int petkey ) {
		this.roleid = roleid;
		this.petkey = petkey;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}
	public int getPetkey() {
		return petkey;
	}
}
