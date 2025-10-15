
package xbean;

public interface FirstFullSkillbase extends mkdb.Bean {
	public FirstFullSkillbase copy(); // deep clone
	public FirstFullSkillbase toData(); // a Data instance
	public FirstFullSkillbase toBean(); // a Bean instance
	public FirstFullSkillbase toDataIf(); // a Data instance If need. else return this
	public FirstFullSkillbase toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Long> getRoleids(); // key=roleid，value=time
	public java.util.Map<Long, Long> getRoleidsAsData(); // key=roleid，value=time

}
