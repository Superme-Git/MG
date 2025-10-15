
package xbean;

public interface EParticleSkill extends mkdb.Bean {
	public EParticleSkill copy(); // deep clone
	public EParticleSkill toData(); // a Data instance
	public EParticleSkill toBean(); // a Bean instance
	public EParticleSkill toDataIf(); // a Data instance If need. else return this
	public EParticleSkill toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.ParticleSkill> getParticleskill(); // 
	public java.util.Map<Integer, xbean.ParticleSkill> getParticleskillAsData(); // 

}
