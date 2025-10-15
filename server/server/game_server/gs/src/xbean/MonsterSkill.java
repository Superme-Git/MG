
package xbean;

public interface MonsterSkill extends mkdb.Bean {
	public MonsterSkill copy(); // deep clone
	public MonsterSkill toData(); // a Data instance
	public MonsterSkill toBean(); // a Bean instance
	public MonsterSkill toDataIf(); // a Data instance If need. else return this
	public MonsterSkill toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // 
	public int getSkilllevel(); // 
	public int getCastrate(); // 以千为底

	public void setId(int _v_); // 
	public void setSkilllevel(int _v_); // 
	public void setCastrate(int _v_); // 以千为底
}
