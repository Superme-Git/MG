
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BingFengRole extends mkdb.XBean implements xbean.BingFengRole {
	private int instzoneid; // 
	private int times; // 
	private int chargetimes; // 
	private java.util.HashMap<Integer, xbean.RoleBFInfo> infos; // 每一个冰封王座的状态

	@Override
	public void _reset_unsafe_() {
		instzoneid = 0;
		times = 0;
		chargetimes = 0;
		infos.clear();
	}

	BingFengRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		instzoneid = 0;
		times = 0;
		chargetimes = 0;
		infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>();
	}

	public BingFengRole() {
		this(0, null, null);
	}

	public BingFengRole(BingFengRole _o_) {
		this(_o_, null, null);
	}

	BingFengRole(xbean.BingFengRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BingFengRole) assign((BingFengRole)_o1_);
		else if (_o1_ instanceof BingFengRole.Data) assign((BingFengRole.Data)_o1_);
		else if (_o1_ instanceof BingFengRole.Const) assign(((BingFengRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BingFengRole _o_) {
		_o_._xdb_verify_unsafe_();
		instzoneid = _o_.instzoneid;
		times = _o_.times;
		chargetimes = _o_.chargetimes;
		infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>();
		for (java.util.Map.Entry<Integer, xbean.RoleBFInfo> _e_ : _o_.infos.entrySet())
			infos.put(_e_.getKey(), new RoleBFInfo(_e_.getValue(), this, "infos"));
	}

	private void assign(BingFengRole.Data _o_) {
		instzoneid = _o_.instzoneid;
		times = _o_.times;
		chargetimes = _o_.chargetimes;
		infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>();
		for (java.util.Map.Entry<Integer, xbean.RoleBFInfo> _e_ : _o_.infos.entrySet())
			infos.put(_e_.getKey(), new RoleBFInfo(_e_.getValue(), this, "infos"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(instzoneid);
		_os_.marshal(times);
		_os_.marshal(chargetimes);
		_os_.compact_uint32(infos.size());
		for (java.util.Map.Entry<Integer, xbean.RoleBFInfo> _e_ : infos.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		instzoneid = _os_.unmarshal_int();
		times = _os_.unmarshal_int();
		chargetimes = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.RoleBFInfo _v_ = new RoleBFInfo(0, this, "infos");
				_v_.unmarshal(_os_);
				infos.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.BingFengRole copy() {
		_xdb_verify_unsafe_();
		return new BingFengRole(this);
	}

	@Override
	public xbean.BingFengRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengRole toBean() {
		_xdb_verify_unsafe_();
		return new BingFengRole(this); // same as copy()
	}

	@Override
	public xbean.BingFengRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getInstzoneid() { // 
		_xdb_verify_unsafe_();
		return instzoneid;
	}

	@Override
	public int getTimes() { // 
		_xdb_verify_unsafe_();
		return times;
	}

	@Override
	public int getChargetimes() { // 
		_xdb_verify_unsafe_();
		return chargetimes;
	}

	@Override
	public java.util.Map<Integer, xbean.RoleBFInfo> getInfos() { // 每一个冰封王座的状态
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "infos"), infos);
	}

	@Override
	public java.util.Map<Integer, xbean.RoleBFInfo> getInfosAsData() { // 每一个冰封王座的状态
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.RoleBFInfo> infos;
		BingFengRole _o_ = this;
		infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>();
		for (java.util.Map.Entry<Integer, xbean.RoleBFInfo> _e_ : _o_.infos.entrySet())
			infos.put(_e_.getKey(), new RoleBFInfo.Data(_e_.getValue()));
		return infos;
	}

	@Override
	public void setInstzoneid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "instzoneid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, instzoneid) {
					public void rollback() { instzoneid = _xdb_saved; }
				};}});
		instzoneid = _v_;
	}

	@Override
	public void setTimes(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "times") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, times) {
					public void rollback() { times = _xdb_saved; }
				};}});
		times = _v_;
	}

	@Override
	public void setChargetimes(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargetimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, chargetimes) {
					public void rollback() { chargetimes = _xdb_saved; }
				};}});
		chargetimes = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BingFengRole _o_ = null;
		if ( _o1_ instanceof BingFengRole ) _o_ = (BingFengRole)_o1_;
		else if ( _o1_ instanceof BingFengRole.Const ) _o_ = ((BingFengRole.Const)_o1_).nThis();
		else return false;
		if (instzoneid != _o_.instzoneid) return false;
		if (times != _o_.times) return false;
		if (chargetimes != _o_.chargetimes) return false;
		if (!infos.equals(_o_.infos)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += instzoneid;
		_h_ += times;
		_h_ += chargetimes;
		_h_ += infos.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(instzoneid);
		_sb_.append(",");
		_sb_.append(times);
		_sb_.append(",");
		_sb_.append(chargetimes);
		_sb_.append(",");
		_sb_.append(infos);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instzoneid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("times"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargetimes"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("infos"));
		return lb;
	}

	private class Const implements xbean.BingFengRole {
		BingFengRole nThis() {
			return BingFengRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BingFengRole copy() {
			return BingFengRole.this.copy();
		}

		@Override
		public xbean.BingFengRole toData() {
			return BingFengRole.this.toData();
		}

		public xbean.BingFengRole toBean() {
			return BingFengRole.this.toBean();
		}

		@Override
		public xbean.BingFengRole toDataIf() {
			return BingFengRole.this.toDataIf();
		}

		public xbean.BingFengRole toBeanIf() {
			return BingFengRole.this.toBeanIf();
		}

		@Override
		public int getInstzoneid() { // 
			_xdb_verify_unsafe_();
			return instzoneid;
		}

		@Override
		public int getTimes() { // 
			_xdb_verify_unsafe_();
			return times;
		}

		@Override
		public int getChargetimes() { // 
			_xdb_verify_unsafe_();
			return chargetimes;
		}

		@Override
		public java.util.Map<Integer, xbean.RoleBFInfo> getInfos() { // 每一个冰封王座的状态
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(infos);
		}

		@Override
		public java.util.Map<Integer, xbean.RoleBFInfo> getInfosAsData() { // 每一个冰封王座的状态
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.RoleBFInfo> infos;
			BingFengRole _o_ = BingFengRole.this;
			infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>();
			for (java.util.Map.Entry<Integer, xbean.RoleBFInfo> _e_ : _o_.infos.entrySet())
				infos.put(_e_.getKey(), new RoleBFInfo.Data(_e_.getValue()));
			return infos;
		}

		@Override
		public void setInstzoneid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimes(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChargetimes(int _v_) { // 
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
			return BingFengRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BingFengRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BingFengRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BingFengRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BingFengRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BingFengRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BingFengRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BingFengRole.this.hashCode();
		}

		@Override
		public String toString() {
			return BingFengRole.this.toString();
		}

	}

	public static final class Data implements xbean.BingFengRole {
		private int instzoneid; // 
		private int times; // 
		private int chargetimes; // 
		private java.util.HashMap<Integer, xbean.RoleBFInfo> infos; // 每一个冰封王座的状态

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			instzoneid = 0;
			times = 0;
			chargetimes = 0;
			infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>();
		}

		Data(xbean.BingFengRole _o1_) {
			if (_o1_ instanceof BingFengRole) assign((BingFengRole)_o1_);
			else if (_o1_ instanceof BingFengRole.Data) assign((BingFengRole.Data)_o1_);
			else if (_o1_ instanceof BingFengRole.Const) assign(((BingFengRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BingFengRole _o_) {
			instzoneid = _o_.instzoneid;
			times = _o_.times;
			chargetimes = _o_.chargetimes;
			infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>();
			for (java.util.Map.Entry<Integer, xbean.RoleBFInfo> _e_ : _o_.infos.entrySet())
				infos.put(_e_.getKey(), new RoleBFInfo.Data(_e_.getValue()));
		}

		private void assign(BingFengRole.Data _o_) {
			instzoneid = _o_.instzoneid;
			times = _o_.times;
			chargetimes = _o_.chargetimes;
			infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>();
			for (java.util.Map.Entry<Integer, xbean.RoleBFInfo> _e_ : _o_.infos.entrySet())
				infos.put(_e_.getKey(), new RoleBFInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(instzoneid);
			_os_.marshal(times);
			_os_.marshal(chargetimes);
			_os_.compact_uint32(infos.size());
			for (java.util.Map.Entry<Integer, xbean.RoleBFInfo> _e_ : infos.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			instzoneid = _os_.unmarshal_int();
			times = _os_.unmarshal_int();
			chargetimes = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					infos = new java.util.HashMap<Integer, xbean.RoleBFInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.RoleBFInfo _v_ = xbean.Pod.newRoleBFInfoData();
					_v_.unmarshal(_os_);
					infos.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.BingFengRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.BingFengRole toData() {
			return new Data(this);
		}

		public xbean.BingFengRole toBean() {
			return new BingFengRole(this, null, null);
		}

		@Override
		public xbean.BingFengRole toDataIf() {
			return this;
		}

		public xbean.BingFengRole toBeanIf() {
			return new BingFengRole(this, null, null);
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
		public int getInstzoneid() { // 
			return instzoneid;
		}

		@Override
		public int getTimes() { // 
			return times;
		}

		@Override
		public int getChargetimes() { // 
			return chargetimes;
		}

		@Override
		public java.util.Map<Integer, xbean.RoleBFInfo> getInfos() { // 每一个冰封王座的状态
			return infos;
		}

		@Override
		public java.util.Map<Integer, xbean.RoleBFInfo> getInfosAsData() { // 每一个冰封王座的状态
			return infos;
		}

		@Override
		public void setInstzoneid(int _v_) { // 
			instzoneid = _v_;
		}

		@Override
		public void setTimes(int _v_) { // 
			times = _v_;
		}

		@Override
		public void setChargetimes(int _v_) { // 
			chargetimes = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BingFengRole.Data)) return false;
			BingFengRole.Data _o_ = (BingFengRole.Data) _o1_;
			if (instzoneid != _o_.instzoneid) return false;
			if (times != _o_.times) return false;
			if (chargetimes != _o_.chargetimes) return false;
			if (!infos.equals(_o_.infos)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += instzoneid;
			_h_ += times;
			_h_ += chargetimes;
			_h_ += infos.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(instzoneid);
			_sb_.append(",");
			_sb_.append(times);
			_sb_.append(",");
			_sb_.append(chargetimes);
			_sb_.append(",");
			_sb_.append(infos);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
