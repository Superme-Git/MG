
package xbean;

public interface ParticleSkill extends mkdb.Bean {
	public ParticleSkill copy(); // deep clone
	public ParticleSkill toData(); // a Data instance
	public ParticleSkill toBean(); // a Bean instance
	public ParticleSkill toDataIf(); // a Data instance If need. else return this
	public ParticleSkill toBeanIf(); // a Bean instance If need. else return this

	public int getLevel(); // 
	public int getExp(); // 

	public void setLevel(int _v_); // 
	public void setExp(int _v_); // 
}
