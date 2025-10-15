
package xbean;

public interface PracticeSkill extends mkdb.Bean {
	public PracticeSkill copy(); // deep clone
	public PracticeSkill toData(); // a Data instance
	public PracticeSkill toBean(); // a Bean instance
	public PracticeSkill toDataIf(); // a Data instance If need. else return this
	public PracticeSkill toBeanIf(); // a Bean instance If need. else return this

	public int getLevel(); // 
	public int getExp(); // 

	public void setLevel(int _v_); // 
	public void setExp(int _v_); // 
}
