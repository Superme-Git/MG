package xtable;

// typed table access point
public class Roleid2particleskill {
	Roleid2particleskill() {
	}

	public static xbean.EParticleSkill get(Long key) {
		return _Tables_.getInstance().roleid2particleskill.get(key);
	}

	public static xbean.EParticleSkill get(Long key, xbean.EParticleSkill value) {
		return _Tables_.getInstance().roleid2particleskill.get(key, value);
	}

	public static void insert(Long key, xbean.EParticleSkill value) {
		_Tables_.getInstance().roleid2particleskill.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2particleskill.delete(key);
	}

	public static boolean add(Long key, xbean.EParticleSkill value) {
		return _Tables_.getInstance().roleid2particleskill.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2particleskill.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.EParticleSkill> getCache() {
		return _Tables_.getInstance().roleid2particleskill.getCache();
	}

	public static mkdb.TTable<Long, xbean.EParticleSkill> getTable() {
		return _Tables_.getInstance().roleid2particleskill;
	}

	public static xbean.EParticleSkill select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EParticleSkill, xbean.EParticleSkill>() {
			public xbean.EParticleSkill get(xbean.EParticleSkill v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.ParticleSkill> selectParticleskill(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EParticleSkill, java.util.Map<Integer, xbean.ParticleSkill>>() {
				public java.util.Map<Integer, xbean.ParticleSkill> get(xbean.EParticleSkill v) { return v.getParticleskillAsData(); }
			});
	}

}
