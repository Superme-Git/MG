
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ELiveSkill extends mkdb.XBean implements xbean.ELiveSkill {
	private java.util.HashMap<Integer, xbean.LiveSkill> liveskilllist; // 

	@Override
	public void _reset_unsafe_() {
		liveskilllist.clear();
	}

	ELiveSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>();
	}

	public ELiveSkill() {
		this(0, null, null);
	}

	public ELiveSkill(ELiveSkill _o_) {
		this(_o_, null, null);
	}

	ELiveSkill(xbean.ELiveSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ELiveSkill) assign((ELiveSkill)_o1_);
		else if (_o1_ instanceof ELiveSkill.Data) assign((ELiveSkill.Data)_o1_);
		else if (_o1_ instanceof ELiveSkill.Const) assign(((ELiveSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ELiveSkill _o_) {
		_o_._xdb_verify_unsafe_();
		liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>();
		for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskilllist.entrySet())
			liveskilllist.put(_e_.getKey(), new LiveSkill(_e_.getValue(), this, "liveskilllist"));
	}

	private void assign(ELiveSkill.Data _o_) {
		liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>();
		for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskilllist.entrySet())
			liveskilllist.put(_e_.getKey(), new LiveSkill(_e_.getValue(), this, "liveskilllist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(liveskilllist.size());
		for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : liveskilllist.entrySet())
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
				liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.LiveSkill _v_ = new LiveSkill(0, this, "liveskilllist");
				_v_.unmarshal(_os_);
				liveskilllist.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ELiveSkill copy() {
		_xdb_verify_unsafe_();
		return new ELiveSkill(this);
	}

	@Override
	public xbean.ELiveSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ELiveSkill toBean() {
		_xdb_verify_unsafe_();
		return new ELiveSkill(this); // same as copy()
	}

	@Override
	public xbean.ELiveSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ELiveSkill toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.LiveSkill> getLiveskilllist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "liveskilllist"), liveskilllist);
	}

	@Override
	public java.util.Map<Integer, xbean.LiveSkill> getLiveskilllistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.LiveSkill> liveskilllist;
		ELiveSkill _o_ = this;
		liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>();
		for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskilllist.entrySet())
			liveskilllist.put(_e_.getKey(), new LiveSkill.Data(_e_.getValue()));
		return liveskilllist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ELiveSkill _o_ = null;
		if ( _o1_ instanceof ELiveSkill ) _o_ = (ELiveSkill)_o1_;
		else if ( _o1_ instanceof ELiveSkill.Const ) _o_ = ((ELiveSkill.Const)_o1_).nThis();
		else return false;
		if (!liveskilllist.equals(_o_.liveskilllist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += liveskilllist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(liveskilllist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("liveskilllist"));
		return lb;
	}

	private class Const implements xbean.ELiveSkill {
		ELiveSkill nThis() {
			return ELiveSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ELiveSkill copy() {
			return ELiveSkill.this.copy();
		}

		@Override
		public xbean.ELiveSkill toData() {
			return ELiveSkill.this.toData();
		}

		public xbean.ELiveSkill toBean() {
			return ELiveSkill.this.toBean();
		}

		@Override
		public xbean.ELiveSkill toDataIf() {
			return ELiveSkill.this.toDataIf();
		}

		public xbean.ELiveSkill toBeanIf() {
			return ELiveSkill.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.LiveSkill> getLiveskilllist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(liveskilllist);
		}

		@Override
		public java.util.Map<Integer, xbean.LiveSkill> getLiveskilllistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.LiveSkill> liveskilllist;
			ELiveSkill _o_ = ELiveSkill.this;
			liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>();
			for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskilllist.entrySet())
				liveskilllist.put(_e_.getKey(), new LiveSkill.Data(_e_.getValue()));
			return liveskilllist;
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
			return ELiveSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ELiveSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ELiveSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ELiveSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ELiveSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ELiveSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ELiveSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ELiveSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return ELiveSkill.this.toString();
		}

	}

	public static final class Data implements xbean.ELiveSkill {
		private java.util.HashMap<Integer, xbean.LiveSkill> liveskilllist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>();
		}

		Data(xbean.ELiveSkill _o1_) {
			if (_o1_ instanceof ELiveSkill) assign((ELiveSkill)_o1_);
			else if (_o1_ instanceof ELiveSkill.Data) assign((ELiveSkill.Data)_o1_);
			else if (_o1_ instanceof ELiveSkill.Const) assign(((ELiveSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ELiveSkill _o_) {
			liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>();
			for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskilllist.entrySet())
				liveskilllist.put(_e_.getKey(), new LiveSkill.Data(_e_.getValue()));
		}

		private void assign(ELiveSkill.Data _o_) {
			liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>();
			for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskilllist.entrySet())
				liveskilllist.put(_e_.getKey(), new LiveSkill.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(liveskilllist.size());
			for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : liveskilllist.entrySet())
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
					liveskilllist = new java.util.HashMap<Integer, xbean.LiveSkill>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.LiveSkill _v_ = xbean.Pod.newLiveSkillData();
					_v_.unmarshal(_os_);
					liveskilllist.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ELiveSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.ELiveSkill toData() {
			return new Data(this);
		}

		public xbean.ELiveSkill toBean() {
			return new ELiveSkill(this, null, null);
		}

		@Override
		public xbean.ELiveSkill toDataIf() {
			return this;
		}

		public xbean.ELiveSkill toBeanIf() {
			return new ELiveSkill(this, null, null);
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
		public java.util.Map<Integer, xbean.LiveSkill> getLiveskilllist() { // 
			return liveskilllist;
		}

		@Override
		public java.util.Map<Integer, xbean.LiveSkill> getLiveskilllistAsData() { // 
			return liveskilllist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ELiveSkill.Data)) return false;
			ELiveSkill.Data _o_ = (ELiveSkill.Data) _o1_;
			if (!liveskilllist.equals(_o_.liveskilllist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += liveskilllist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(liveskilllist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
