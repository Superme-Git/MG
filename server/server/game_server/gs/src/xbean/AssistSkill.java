
package xbean;

public interface AssistSkill extends mkdb.Bean {
	public AssistSkill copy(); // deep clone
	public AssistSkill toData(); // a Data instance
	public AssistSkill toBean(); // a Bean instance
	public AssistSkill toDataIf(); // a Data instance If need. else return this
	public AssistSkill toBeanIf(); // a Bean instance If need. else return this

	public int getLevel(); // 
	public long getExp(); // 

	public void setLevel(int _v_); // 
	public void setExp(long _v_); // 
}
