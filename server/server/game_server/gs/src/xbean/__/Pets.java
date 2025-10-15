
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Pets extends mkdb.XBean implements xbean.Pets {
	private int capacity; // 
	private int nextid; // 下一个id
	private java.util.HashMap<Integer, xbean.PetInfo> petmap; // 

	@Override
	public void _reset_unsafe_() {
		capacity = 0;
		nextid = 0;
		petmap.clear();
	}

	Pets(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		petmap = new java.util.HashMap<Integer, xbean.PetInfo>();
	}

	public Pets() {
		this(0, null, null);
	}

	public Pets(Pets _o_) {
		this(_o_, null, null);
	}

	Pets(xbean.Pets _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Pets) assign((Pets)_o1_);
		else if (_o1_ instanceof Pets.Data) assign((Pets.Data)_o1_);
		else if (_o1_ instanceof Pets.Const) assign(((Pets.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Pets _o_) {
		_o_._xdb_verify_unsafe_();
		capacity = _o_.capacity;
		nextid = _o_.nextid;
		petmap = new java.util.HashMap<Integer, xbean.PetInfo>();
		for (java.util.Map.Entry<Integer, xbean.PetInfo> _e_ : _o_.petmap.entrySet())
			petmap.put(_e_.getKey(), new PetInfo(_e_.getValue(), this, "petmap"));
	}

	private void assign(Pets.Data _o_) {
		capacity = _o_.capacity;
		nextid = _o_.nextid;
		petmap = new java.util.HashMap<Integer, xbean.PetInfo>();
		for (java.util.Map.Entry<Integer, xbean.PetInfo> _e_ : _o_.petmap.entrySet())
			petmap.put(_e_.getKey(), new PetInfo(_e_.getValue(), this, "petmap"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(capacity);
		_os_.marshal(nextid);
		_os_.compact_uint32(petmap.size());
		for (java.util.Map.Entry<Integer, xbean.PetInfo> _e_ : petmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		capacity = _os_.unmarshal_int();
		nextid = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				petmap = new java.util.HashMap<Integer, xbean.PetInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.PetInfo _v_ = new PetInfo(0, this, "petmap");
				_v_.unmarshal(_os_);
				petmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.Pets copy() {
		_xdb_verify_unsafe_();
		return new Pets(this);
	}

	@Override
	public xbean.Pets toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Pets toBean() {
		_xdb_verify_unsafe_();
		return new Pets(this); // same as copy()
	}

	@Override
	public xbean.Pets toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Pets toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getCapacity() { // 
		_xdb_verify_unsafe_();
		return capacity;
	}

	@Override
	public int getNextid() { // 下一个id
		_xdb_verify_unsafe_();
		return nextid;
	}

	@Override
	public java.util.Map<Integer, xbean.PetInfo> getPetmap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "petmap"), petmap);
	}

	@Override
	public java.util.Map<Integer, xbean.PetInfo> getPetmapAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.PetInfo> petmap;
		Pets _o_ = this;
		petmap = new java.util.HashMap<Integer, xbean.PetInfo>();
		for (java.util.Map.Entry<Integer, xbean.PetInfo> _e_ : _o_.petmap.entrySet())
			petmap.put(_e_.getKey(), new PetInfo.Data(_e_.getValue()));
		return petmap;
	}

	@Override
	public void setCapacity(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "capacity") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, capacity) {
					public void rollback() { capacity = _xdb_saved; }
				};}});
		capacity = _v_;
	}

	@Override
	public void setNextid(int _v_) { // 下一个id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "nextid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, nextid) {
					public void rollback() { nextid = _xdb_saved; }
				};}});
		nextid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Pets _o_ = null;
		if ( _o1_ instanceof Pets ) _o_ = (Pets)_o1_;
		else if ( _o1_ instanceof Pets.Const ) _o_ = ((Pets.Const)_o1_).nThis();
		else return false;
		if (capacity != _o_.capacity) return false;
		if (nextid != _o_.nextid) return false;
		if (!petmap.equals(_o_.petmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += capacity;
		_h_ += nextid;
		_h_ += petmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(capacity);
		_sb_.append(",");
		_sb_.append(nextid);
		_sb_.append(",");
		_sb_.append(petmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("capacity"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("nextid"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("petmap"));
		return lb;
	}

	private class Const implements xbean.Pets {
		Pets nThis() {
			return Pets.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Pets copy() {
			return Pets.this.copy();
		}

		@Override
		public xbean.Pets toData() {
			return Pets.this.toData();
		}

		public xbean.Pets toBean() {
			return Pets.this.toBean();
		}

		@Override
		public xbean.Pets toDataIf() {
			return Pets.this.toDataIf();
		}

		public xbean.Pets toBeanIf() {
			return Pets.this.toBeanIf();
		}

		@Override
		public int getCapacity() { // 
			_xdb_verify_unsafe_();
			return capacity;
		}

		@Override
		public int getNextid() { // 下一个id
			_xdb_verify_unsafe_();
			return nextid;
		}

		@Override
		public java.util.Map<Integer, xbean.PetInfo> getPetmap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(petmap);
		}

		@Override
		public java.util.Map<Integer, xbean.PetInfo> getPetmapAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.PetInfo> petmap;
			Pets _o_ = Pets.this;
			petmap = new java.util.HashMap<Integer, xbean.PetInfo>();
			for (java.util.Map.Entry<Integer, xbean.PetInfo> _e_ : _o_.petmap.entrySet())
				petmap.put(_e_.getKey(), new PetInfo.Data(_e_.getValue()));
			return petmap;
		}

		@Override
		public void setCapacity(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNextid(int _v_) { // 下一个id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
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
			return Pets.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Pets.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Pets.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Pets.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Pets.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Pets.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Pets.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Pets.this.hashCode();
		}

		@Override
		public String toString() {
			return Pets.this.toString();
		}

	}

	public static final class Data implements xbean.Pets {
		private int capacity; // 
		private int nextid; // 下一个id
		private java.util.HashMap<Integer, xbean.PetInfo> petmap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			petmap = new java.util.HashMap<Integer, xbean.PetInfo>();
		}

		Data(xbean.Pets _o1_) {
			if (_o1_ instanceof Pets) assign((Pets)_o1_);
			else if (_o1_ instanceof Pets.Data) assign((Pets.Data)_o1_);
			else if (_o1_ instanceof Pets.Const) assign(((Pets.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Pets _o_) {
			capacity = _o_.capacity;
			nextid = _o_.nextid;
			petmap = new java.util.HashMap<Integer, xbean.PetInfo>();
			for (java.util.Map.Entry<Integer, xbean.PetInfo> _e_ : _o_.petmap.entrySet())
				petmap.put(_e_.getKey(), new PetInfo.Data(_e_.getValue()));
		}

		private void assign(Pets.Data _o_) {
			capacity = _o_.capacity;
			nextid = _o_.nextid;
			petmap = new java.util.HashMap<Integer, xbean.PetInfo>();
			for (java.util.Map.Entry<Integer, xbean.PetInfo> _e_ : _o_.petmap.entrySet())
				petmap.put(_e_.getKey(), new PetInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(capacity);
			_os_.marshal(nextid);
			_os_.compact_uint32(petmap.size());
			for (java.util.Map.Entry<Integer, xbean.PetInfo> _e_ : petmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			capacity = _os_.unmarshal_int();
			nextid = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					petmap = new java.util.HashMap<Integer, xbean.PetInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.PetInfo _v_ = xbean.Pod.newPetInfoData();
					_v_.unmarshal(_os_);
					petmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.Pets copy() {
			return new Data(this);
		}

		@Override
		public xbean.Pets toData() {
			return new Data(this);
		}

		public xbean.Pets toBean() {
			return new Pets(this, null, null);
		}

		@Override
		public xbean.Pets toDataIf() {
			return this;
		}

		public xbean.Pets toBeanIf() {
			return new Pets(this, null, null);
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
		public int getCapacity() { // 
			return capacity;
		}

		@Override
		public int getNextid() { // 下一个id
			return nextid;
		}

		@Override
		public java.util.Map<Integer, xbean.PetInfo> getPetmap() { // 
			return petmap;
		}

		@Override
		public java.util.Map<Integer, xbean.PetInfo> getPetmapAsData() { // 
			return petmap;
		}

		@Override
		public void setCapacity(int _v_) { // 
			capacity = _v_;
		}

		@Override
		public void setNextid(int _v_) { // 下一个id
			nextid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Pets.Data)) return false;
			Pets.Data _o_ = (Pets.Data) _o1_;
			if (capacity != _o_.capacity) return false;
			if (nextid != _o_.nextid) return false;
			if (!petmap.equals(_o_.petmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += capacity;
			_h_ += nextid;
			_h_ += petmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(capacity);
			_sb_.append(",");
			_sb_.append(nextid);
			_sb_.append(",");
			_sb_.append(petmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
