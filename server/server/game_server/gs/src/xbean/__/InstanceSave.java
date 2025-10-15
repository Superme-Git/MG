
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceSave extends mkdb.XBean implements xbean.InstanceSave {
	private int saveid; // 
	private int state; // 
	private java.util.HashMap<Integer, Integer> subsaves; // 子进度状态，value是子进度计数
	private java.util.HashMap<Long, xbean.InstanceNpcSave> npcsaves; // npc的状态存储, key 是npckey

	@Override
	public void _reset_unsafe_() {
		saveid = 0;
		state = 0;
		subsaves.clear();
		npcsaves.clear();
	}

	InstanceSave(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		subsaves = new java.util.HashMap<Integer, Integer>();
		npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>();
	}

	public InstanceSave() {
		this(0, null, null);
	}

	public InstanceSave(InstanceSave _o_) {
		this(_o_, null, null);
	}

	InstanceSave(xbean.InstanceSave _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceSave) assign((InstanceSave)_o1_);
		else if (_o1_ instanceof InstanceSave.Data) assign((InstanceSave.Data)_o1_);
		else if (_o1_ instanceof InstanceSave.Const) assign(((InstanceSave.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceSave _o_) {
		_o_._xdb_verify_unsafe_();
		saveid = _o_.saveid;
		state = _o_.state;
		subsaves = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
			subsaves.put(_e_.getKey(), _e_.getValue());
		npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>();
		for (java.util.Map.Entry<Long, xbean.InstanceNpcSave> _e_ : _o_.npcsaves.entrySet())
			npcsaves.put(_e_.getKey(), new InstanceNpcSave(_e_.getValue(), this, "npcsaves"));
	}

	private void assign(InstanceSave.Data _o_) {
		saveid = _o_.saveid;
		state = _o_.state;
		subsaves = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
			subsaves.put(_e_.getKey(), _e_.getValue());
		npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>();
		for (java.util.Map.Entry<Long, xbean.InstanceNpcSave> _e_ : _o_.npcsaves.entrySet())
			npcsaves.put(_e_.getKey(), new InstanceNpcSave(_e_.getValue(), this, "npcsaves"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(saveid);
		_os_.marshal(state);
		_os_.compact_uint32(subsaves.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : subsaves.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(npcsaves.size());
		for (java.util.Map.Entry<Long, xbean.InstanceNpcSave> _e_ : npcsaves.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		saveid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				subsaves = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				subsaves.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.InstanceNpcSave _v_ = new InstanceNpcSave(0, this, "npcsaves");
				_v_.unmarshal(_os_);
				npcsaves.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.InstanceSave copy() {
		_xdb_verify_unsafe_();
		return new InstanceSave(this);
	}

	@Override
	public xbean.InstanceSave toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceSave toBean() {
		_xdb_verify_unsafe_();
		return new InstanceSave(this); // same as copy()
	}

	@Override
	public xbean.InstanceSave toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceSave toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getSaveid() { // 
		_xdb_verify_unsafe_();
		return saveid;
	}

	@Override
	public int getState() { // 
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public java.util.Map<Integer, Integer> getSubsaves() { // 子进度状态，value是子进度计数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "subsaves"), subsaves);
	}

	@Override
	public java.util.Map<Integer, Integer> getSubsavesAsData() { // 子进度状态，value是子进度计数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> subsaves;
		InstanceSave _o_ = this;
		subsaves = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
			subsaves.put(_e_.getKey(), _e_.getValue());
		return subsaves;
	}

	@Override
	public java.util.Map<Long, xbean.InstanceNpcSave> getNpcsaves() { // npc的状态存储, key 是npckey
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "npcsaves"), npcsaves);
	}

	@Override
	public java.util.Map<Long, xbean.InstanceNpcSave> getNpcsavesAsData() { // npc的状态存储, key 是npckey
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.InstanceNpcSave> npcsaves;
		InstanceSave _o_ = this;
		npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>();
		for (java.util.Map.Entry<Long, xbean.InstanceNpcSave> _e_ : _o_.npcsaves.entrySet())
			npcsaves.put(_e_.getKey(), new InstanceNpcSave.Data(_e_.getValue()));
		return npcsaves;
	}

	@Override
	public void setSaveid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "saveid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, saveid) {
					public void rollback() { saveid = _xdb_saved; }
				};}});
		saveid = _v_;
	}

	@Override
	public void setState(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceSave _o_ = null;
		if ( _o1_ instanceof InstanceSave ) _o_ = (InstanceSave)_o1_;
		else if ( _o1_ instanceof InstanceSave.Const ) _o_ = ((InstanceSave.Const)_o1_).nThis();
		else return false;
		if (saveid != _o_.saveid) return false;
		if (state != _o_.state) return false;
		if (!subsaves.equals(_o_.subsaves)) return false;
		if (!npcsaves.equals(_o_.npcsaves)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += saveid;
		_h_ += state;
		_h_ += subsaves.hashCode();
		_h_ += npcsaves.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(saveid);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(subsaves);
		_sb_.append(",");
		_sb_.append(npcsaves);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("saveid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("subsaves"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("npcsaves"));
		return lb;
	}

	private class Const implements xbean.InstanceSave {
		InstanceSave nThis() {
			return InstanceSave.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceSave copy() {
			return InstanceSave.this.copy();
		}

		@Override
		public xbean.InstanceSave toData() {
			return InstanceSave.this.toData();
		}

		public xbean.InstanceSave toBean() {
			return InstanceSave.this.toBean();
		}

		@Override
		public xbean.InstanceSave toDataIf() {
			return InstanceSave.this.toDataIf();
		}

		public xbean.InstanceSave toBeanIf() {
			return InstanceSave.this.toBeanIf();
		}

		@Override
		public int getSaveid() { // 
			_xdb_verify_unsafe_();
			return saveid;
		}

		@Override
		public int getState() { // 
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public java.util.Map<Integer, Integer> getSubsaves() { // 子进度状态，value是子进度计数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(subsaves);
		}

		@Override
		public java.util.Map<Integer, Integer> getSubsavesAsData() { // 子进度状态，value是子进度计数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> subsaves;
			InstanceSave _o_ = InstanceSave.this;
			subsaves = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
				subsaves.put(_e_.getKey(), _e_.getValue());
			return subsaves;
		}

		@Override
		public java.util.Map<Long, xbean.InstanceNpcSave> getNpcsaves() { // npc的状态存储, key 是npckey
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(npcsaves);
		}

		@Override
		public java.util.Map<Long, xbean.InstanceNpcSave> getNpcsavesAsData() { // npc的状态存储, key 是npckey
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.InstanceNpcSave> npcsaves;
			InstanceSave _o_ = InstanceSave.this;
			npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>();
			for (java.util.Map.Entry<Long, xbean.InstanceNpcSave> _e_ : _o_.npcsaves.entrySet())
				npcsaves.put(_e_.getKey(), new InstanceNpcSave.Data(_e_.getValue()));
			return npcsaves;
		}

		@Override
		public void setSaveid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 
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
			return InstanceSave.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceSave.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceSave.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceSave.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceSave.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceSave.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceSave.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceSave.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceSave.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceSave {
		private int saveid; // 
		private int state; // 
		private java.util.HashMap<Integer, Integer> subsaves; // 子进度状态，value是子进度计数
		private java.util.HashMap<Long, xbean.InstanceNpcSave> npcsaves; // npc的状态存储, key 是npckey

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			subsaves = new java.util.HashMap<Integer, Integer>();
			npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>();
		}

		Data(xbean.InstanceSave _o1_) {
			if (_o1_ instanceof InstanceSave) assign((InstanceSave)_o1_);
			else if (_o1_ instanceof InstanceSave.Data) assign((InstanceSave.Data)_o1_);
			else if (_o1_ instanceof InstanceSave.Const) assign(((InstanceSave.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceSave _o_) {
			saveid = _o_.saveid;
			state = _o_.state;
			subsaves = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
				subsaves.put(_e_.getKey(), _e_.getValue());
			npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>();
			for (java.util.Map.Entry<Long, xbean.InstanceNpcSave> _e_ : _o_.npcsaves.entrySet())
				npcsaves.put(_e_.getKey(), new InstanceNpcSave.Data(_e_.getValue()));
		}

		private void assign(InstanceSave.Data _o_) {
			saveid = _o_.saveid;
			state = _o_.state;
			subsaves = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
				subsaves.put(_e_.getKey(), _e_.getValue());
			npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>();
			for (java.util.Map.Entry<Long, xbean.InstanceNpcSave> _e_ : _o_.npcsaves.entrySet())
				npcsaves.put(_e_.getKey(), new InstanceNpcSave.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(saveid);
			_os_.marshal(state);
			_os_.compact_uint32(subsaves.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : subsaves.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(npcsaves.size());
			for (java.util.Map.Entry<Long, xbean.InstanceNpcSave> _e_ : npcsaves.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			saveid = _os_.unmarshal_int();
			state = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					subsaves = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					subsaves.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					npcsaves = new java.util.HashMap<Long, xbean.InstanceNpcSave>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.InstanceNpcSave _v_ = xbean.Pod.newInstanceNpcSaveData();
					_v_.unmarshal(_os_);
					npcsaves.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.InstanceSave copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceSave toData() {
			return new Data(this);
		}

		public xbean.InstanceSave toBean() {
			return new InstanceSave(this, null, null);
		}

		@Override
		public xbean.InstanceSave toDataIf() {
			return this;
		}

		public xbean.InstanceSave toBeanIf() {
			return new InstanceSave(this, null, null);
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
		public int getSaveid() { // 
			return saveid;
		}

		@Override
		public int getState() { // 
			return state;
		}

		@Override
		public java.util.Map<Integer, Integer> getSubsaves() { // 子进度状态，value是子进度计数
			return subsaves;
		}

		@Override
		public java.util.Map<Integer, Integer> getSubsavesAsData() { // 子进度状态，value是子进度计数
			return subsaves;
		}

		@Override
		public java.util.Map<Long, xbean.InstanceNpcSave> getNpcsaves() { // npc的状态存储, key 是npckey
			return npcsaves;
		}

		@Override
		public java.util.Map<Long, xbean.InstanceNpcSave> getNpcsavesAsData() { // npc的状态存储, key 是npckey
			return npcsaves;
		}

		@Override
		public void setSaveid(int _v_) { // 
			saveid = _v_;
		}

		@Override
		public void setState(int _v_) { // 
			state = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceSave.Data)) return false;
			InstanceSave.Data _o_ = (InstanceSave.Data) _o1_;
			if (saveid != _o_.saveid) return false;
			if (state != _o_.state) return false;
			if (!subsaves.equals(_o_.subsaves)) return false;
			if (!npcsaves.equals(_o_.npcsaves)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += saveid;
			_h_ += state;
			_h_ += subsaves.hashCode();
			_h_ += npcsaves.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(saveid);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(subsaves);
			_sb_.append(",");
			_sb_.append(npcsaves);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
