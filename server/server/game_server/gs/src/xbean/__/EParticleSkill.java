
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EParticleSkill extends mkdb.XBean implements xbean.EParticleSkill {
	private java.util.HashMap<Integer, xbean.ParticleSkill> particleskill; // 

	@Override
	public void _reset_unsafe_() {
		particleskill.clear();
	}

	EParticleSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>();
	}

	public EParticleSkill() {
		this(0, null, null);
	}

	public EParticleSkill(EParticleSkill _o_) {
		this(_o_, null, null);
	}

	EParticleSkill(xbean.EParticleSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EParticleSkill) assign((EParticleSkill)_o1_);
		else if (_o1_ instanceof EParticleSkill.Data) assign((EParticleSkill.Data)_o1_);
		else if (_o1_ instanceof EParticleSkill.Const) assign(((EParticleSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EParticleSkill _o_) {
		_o_._xdb_verify_unsafe_();
		particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>();
		for (java.util.Map.Entry<Integer, xbean.ParticleSkill> _e_ : _o_.particleskill.entrySet())
			particleskill.put(_e_.getKey(), new ParticleSkill(_e_.getValue(), this, "particleskill"));
	}

	private void assign(EParticleSkill.Data _o_) {
		particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>();
		for (java.util.Map.Entry<Integer, xbean.ParticleSkill> _e_ : _o_.particleskill.entrySet())
			particleskill.put(_e_.getKey(), new ParticleSkill(_e_.getValue(), this, "particleskill"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(particleskill.size());
		for (java.util.Map.Entry<Integer, xbean.ParticleSkill> _e_ : particleskill.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.ParticleSkill _v_ = new ParticleSkill(0, this, "particleskill");
				_v_.unmarshal(_os_);
				particleskill.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.EParticleSkill copy() {
		_xdb_verify_unsafe_();
		return new EParticleSkill(this);
	}

	@Override
	public xbean.EParticleSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EParticleSkill toBean() {
		_xdb_verify_unsafe_();
		return new EParticleSkill(this); // same as copy()
	}

	@Override
	public xbean.EParticleSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EParticleSkill toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.ParticleSkill> getParticleskill() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "particleskill"), particleskill);
	}

	@Override
	public java.util.Map<Integer, xbean.ParticleSkill> getParticleskillAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.ParticleSkill> particleskill;
		EParticleSkill _o_ = this;
		particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>();
		for (java.util.Map.Entry<Integer, xbean.ParticleSkill> _e_ : _o_.particleskill.entrySet())
			particleskill.put(_e_.getKey(), new ParticleSkill.Data(_e_.getValue()));
		return particleskill;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EParticleSkill _o_ = null;
		if ( _o1_ instanceof EParticleSkill ) _o_ = (EParticleSkill)_o1_;
		else if ( _o1_ instanceof EParticleSkill.Const ) _o_ = ((EParticleSkill.Const)_o1_).nThis();
		else return false;
		if (!particleskill.equals(_o_.particleskill)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += particleskill.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(particleskill);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("particleskill"));
		return lb;
	}

	private class Const implements xbean.EParticleSkill {
		EParticleSkill nThis() {
			return EParticleSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EParticleSkill copy() {
			return EParticleSkill.this.copy();
		}

		@Override
		public xbean.EParticleSkill toData() {
			return EParticleSkill.this.toData();
		}

		public xbean.EParticleSkill toBean() {
			return EParticleSkill.this.toBean();
		}

		@Override
		public xbean.EParticleSkill toDataIf() {
			return EParticleSkill.this.toDataIf();
		}

		public xbean.EParticleSkill toBeanIf() {
			return EParticleSkill.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.ParticleSkill> getParticleskill() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(particleskill);
		}

		@Override
		public java.util.Map<Integer, xbean.ParticleSkill> getParticleskillAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.ParticleSkill> particleskill;
			EParticleSkill _o_ = EParticleSkill.this;
			particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>();
			for (java.util.Map.Entry<Integer, xbean.ParticleSkill> _e_ : _o_.particleskill.entrySet())
				particleskill.put(_e_.getKey(), new ParticleSkill.Data(_e_.getValue()));
			return particleskill;
		}

		@Override
		public mkdb.Bean toConst() {
			_xdb_verify_unsafe_();
			return this;
		}

		@Override
		public boolean isConst() {
			_xdb_verify_unsafe_();
			return true;
		}

		@Override
		public boolean isData() {
			return EParticleSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EParticleSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EParticleSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EParticleSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EParticleSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EParticleSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EParticleSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EParticleSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return EParticleSkill.this.toString();
		}

	}

	public static final class Data implements xbean.EParticleSkill {
		private java.util.HashMap<Integer, xbean.ParticleSkill> particleskill; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>();
		}

		Data(xbean.EParticleSkill _o1_) {
			if (_o1_ instanceof EParticleSkill) assign((EParticleSkill)_o1_);
			else if (_o1_ instanceof EParticleSkill.Data) assign((EParticleSkill.Data)_o1_);
			else if (_o1_ instanceof EParticleSkill.Const) assign(((EParticleSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EParticleSkill _o_) {
			particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>();
			for (java.util.Map.Entry<Integer, xbean.ParticleSkill> _e_ : _o_.particleskill.entrySet())
				particleskill.put(_e_.getKey(), new ParticleSkill.Data(_e_.getValue()));
		}

		private void assign(EParticleSkill.Data _o_) {
			particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>();
			for (java.util.Map.Entry<Integer, xbean.ParticleSkill> _e_ : _o_.particleskill.entrySet())
				particleskill.put(_e_.getKey(), new ParticleSkill.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(particleskill.size());
			for (java.util.Map.Entry<Integer, xbean.ParticleSkill> _e_ : particleskill.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					particleskill = new java.util.HashMap<Integer, xbean.ParticleSkill>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.ParticleSkill _v_ = xbean.Pod.newParticleSkillData();
					_v_.unmarshal(_os_);
					particleskill.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.EParticleSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.EParticleSkill toData() {
			return new Data(this);
		}

		public xbean.EParticleSkill toBean() {
			return new EParticleSkill(this, null, null);
		}

		@Override
		public xbean.EParticleSkill toDataIf() {
			return this;
		}

		public xbean.EParticleSkill toBeanIf() {
			return new EParticleSkill(this, null, null);
		}

		// mkdb.Bean interface. Data Unsupported
		public boolean xdbManaged() { throw new UnsupportedOperationException(); }
		public mkdb.Bean xdbParent() { throw new UnsupportedOperationException(); }
		public String xdbVarname()  { throw new UnsupportedOperationException(); }
		public Long    xdbObjId()   { throw new UnsupportedOperationException(); }
		public mkdb.Bean toConst()   { throw new UnsupportedOperationException(); }
		public boolean isConst()    { return false; }
		public boolean isData()     { return true; }

		@Override
		public java.util.Map<Integer, xbean.ParticleSkill> getParticleskill() { // 
			return particleskill;
		}

		@Override
		public java.util.Map<Integer, xbean.ParticleSkill> getParticleskillAsData() { // 
			return particleskill;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EParticleSkill.Data)) return false;
			EParticleSkill.Data _o_ = (EParticleSkill.Data) _o1_;
			if (!particleskill.equals(_o_.particleskill)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += particleskill.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(particleskill);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
