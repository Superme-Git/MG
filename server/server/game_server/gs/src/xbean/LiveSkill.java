
package xbean;

public interface LiveSkill extends mkdb.Bean {
	public LiveSkill copy(); // deep clone
	public LiveSkill toData(); // a Data instance
	public LiveSkill toBean(); // a Bean instance
	public LiveSkill toDataIf(); // a Data instance If need. else return this
	public LiveSkill toBeanIf(); // a Bean instance If need. else return this

	public int getLevel(); // 

	public void setLevel(int _v_); // 
}
