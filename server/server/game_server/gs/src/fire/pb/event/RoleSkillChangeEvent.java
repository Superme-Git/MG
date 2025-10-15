package fire.pb.event;
/**
 * 技能改变事件
 * @author cn
 */
public class RoleSkillChangeEvent implements Event {
	private final long roleid;
	private final int skillid;

	public RoleSkillChangeEvent( final long roleid, final int skillid ) {
		this.roleid = roleid;
		this.skillid = skillid;
	}
	
	@Override
	public long getRoleid() {
		return roleid;
	}
	
	public int getSkillid() {
		return skillid;
	}
}
