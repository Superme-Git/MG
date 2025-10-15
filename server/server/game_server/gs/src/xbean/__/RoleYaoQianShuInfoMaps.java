
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleYaoQianShuInfoMaps extends mkdb.XBean implements xbean.RoleYaoQianShuInfoMaps {
	private java.util.HashMap<Long, xbean.RoleYaoQianShuInfo> yaoqianshumaps; // key 为npckey

	@Override
	public void _reset_unsafe_() {
		yaoqianshumaps.clear();
	}

	RoleYaoQianShuInfoMaps(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>();
	}

	public RoleYaoQianShuInfoMaps() {
		this(0, null, null);
	}

	public RoleYaoQianShuInfoMaps(RoleYaoQianShuInfoMaps _o_) {
		this(_o_, null, null);
	}

	RoleYaoQianShuInfoMaps(xbean.RoleYaoQianShuInfoMaps _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleYaoQianShuInfoMaps) assign((RoleYaoQianShuInfoMaps)_o1_);
		else if (_o1_ instanceof RoleYaoQianShuInfoMaps.Data) assign((RoleYaoQianShuInfoMaps.Data)_o1_);
		else if (_o1_ instanceof RoleYaoQianShuInfoMaps.Const) assign(((RoleYaoQianShuInfoMaps.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleYaoQianShuInfoMaps _o_) {
		_o_._xdb_verify_unsafe_();
		yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>();
		for (java.util.Map.Entry<Long, xbean.RoleYaoQianShuInfo> _e_ : _o_.yaoqianshumaps.entrySet())
			yaoqianshumaps.put(_e_.getKey(), new RoleYaoQianShuInfo(_e_.getValue(), this, "yaoqianshumaps"));
	}

	private void assign(RoleYaoQianShuInfoMaps.Data _o_) {
		yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>();
		for (java.util.Map.Entry<Long, xbean.RoleYaoQianShuInfo> _e_ : _o_.yaoqianshumaps.entrySet())
			yaoqianshumaps.put(_e_.getKey(), new RoleYaoQianShuInfo(_e_.getValue(), this, "yaoqianshumaps"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(yaoqianshumaps.size());
		for (java.util.Map.Entry<Long, xbean.RoleYaoQianShuInfo> _e_ : yaoqianshumaps.entrySet())
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
				yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.RoleYaoQianShuInfo _v_ = new RoleYaoQianShuInfo(0, this, "yaoqianshumaps");
				_v_.unmarshal(_os_);
				yaoqianshumaps.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleYaoQianShuInfoMaps copy() {
		_xdb_verify_unsafe_();
		return new RoleYaoQianShuInfoMaps(this);
	}

	@Override
	public xbean.RoleYaoQianShuInfoMaps toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleYaoQianShuInfoMaps toBean() {
		_xdb_verify_unsafe_();
		return new RoleYaoQianShuInfoMaps(this); // same as copy()
	}

	@Override
	public xbean.RoleYaoQianShuInfoMaps toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleYaoQianShuInfoMaps toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.RoleYaoQianShuInfo> getYaoqianshumaps() { // key 为npckey
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "yaoqianshumaps"), yaoqianshumaps);
	}

	@Override
	public java.util.Map<Long, xbean.RoleYaoQianShuInfo> getYaoqianshumapsAsData() { // key 为npckey
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.RoleYaoQianShuInfo> yaoqianshumaps;
		RoleYaoQianShuInfoMaps _o_ = this;
		yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>();
		for (java.util.Map.Entry<Long, xbean.RoleYaoQianShuInfo> _e_ : _o_.yaoqianshumaps.entrySet())
			yaoqianshumaps.put(_e_.getKey(), new RoleYaoQianShuInfo.Data(_e_.getValue()));
		return yaoqianshumaps;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleYaoQianShuInfoMaps _o_ = null;
		if ( _o1_ instanceof RoleYaoQianShuInfoMaps ) _o_ = (RoleYaoQianShuInfoMaps)_o1_;
		else if ( _o1_ instanceof RoleYaoQianShuInfoMaps.Const ) _o_ = ((RoleYaoQianShuInfoMaps.Const)_o1_).nThis();
		else return false;
		if (!yaoqianshumaps.equals(_o_.yaoqianshumaps)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += yaoqianshumaps.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(yaoqianshumaps);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("yaoqianshumaps"));
		return lb;
	}

	private class Const implements xbean.RoleYaoQianShuInfoMaps {
		RoleYaoQianShuInfoMaps nThis() {
			return RoleYaoQianShuInfoMaps.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleYaoQianShuInfoMaps copy() {
			return RoleYaoQianShuInfoMaps.this.copy();
		}

		@Override
		public xbean.RoleYaoQianShuInfoMaps toData() {
			return RoleYaoQianShuInfoMaps.this.toData();
		}

		public xbean.RoleYaoQianShuInfoMaps toBean() {
			return RoleYaoQianShuInfoMaps.this.toBean();
		}

		@Override
		public xbean.RoleYaoQianShuInfoMaps toDataIf() {
			return RoleYaoQianShuInfoMaps.this.toDataIf();
		}

		public xbean.RoleYaoQianShuInfoMaps toBeanIf() {
			return RoleYaoQianShuInfoMaps.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.RoleYaoQianShuInfo> getYaoqianshumaps() { // key 为npckey
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(yaoqianshumaps);
		}

		@Override
		public java.util.Map<Long, xbean.RoleYaoQianShuInfo> getYaoqianshumapsAsData() { // key 为npckey
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.RoleYaoQianShuInfo> yaoqianshumaps;
			RoleYaoQianShuInfoMaps _o_ = RoleYaoQianShuInfoMaps.this;
			yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>();
			for (java.util.Map.Entry<Long, xbean.RoleYaoQianShuInfo> _e_ : _o_.yaoqianshumaps.entrySet())
				yaoqianshumaps.put(_e_.getKey(), new RoleYaoQianShuInfo.Data(_e_.getValue()));
			return yaoqianshumaps;
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
			return RoleYaoQianShuInfoMaps.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleYaoQianShuInfoMaps.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleYaoQianShuInfoMaps.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleYaoQianShuInfoMaps.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleYaoQianShuInfoMaps.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleYaoQianShuInfoMaps.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleYaoQianShuInfoMaps.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleYaoQianShuInfoMaps.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleYaoQianShuInfoMaps.this.toString();
		}

	}

	public static final class Data implements xbean.RoleYaoQianShuInfoMaps {
		private java.util.HashMap<Long, xbean.RoleYaoQianShuInfo> yaoqianshumaps; // key 为npckey

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>();
		}

		Data(xbean.RoleYaoQianShuInfoMaps _o1_) {
			if (_o1_ instanceof RoleYaoQianShuInfoMaps) assign((RoleYaoQianShuInfoMaps)_o1_);
			else if (_o1_ instanceof RoleYaoQianShuInfoMaps.Data) assign((RoleYaoQianShuInfoMaps.Data)_o1_);
			else if (_o1_ instanceof RoleYaoQianShuInfoMaps.Const) assign(((RoleYaoQianShuInfoMaps.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleYaoQianShuInfoMaps _o_) {
			yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>();
			for (java.util.Map.Entry<Long, xbean.RoleYaoQianShuInfo> _e_ : _o_.yaoqianshumaps.entrySet())
				yaoqianshumaps.put(_e_.getKey(), new RoleYaoQianShuInfo.Data(_e_.getValue()));
		}

		private void assign(RoleYaoQianShuInfoMaps.Data _o_) {
			yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>();
			for (java.util.Map.Entry<Long, xbean.RoleYaoQianShuInfo> _e_ : _o_.yaoqianshumaps.entrySet())
				yaoqianshumaps.put(_e_.getKey(), new RoleYaoQianShuInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(yaoqianshumaps.size());
			for (java.util.Map.Entry<Long, xbean.RoleYaoQianShuInfo> _e_ : yaoqianshumaps.entrySet())
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
					yaoqianshumaps = new java.util.HashMap<Long, xbean.RoleYaoQianShuInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.RoleYaoQianShuInfo _v_ = xbean.Pod.newRoleYaoQianShuInfoData();
					_v_.unmarshal(_os_);
					yaoqianshumaps.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleYaoQianShuInfoMaps copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleYaoQianShuInfoMaps toData() {
			return new Data(this);
		}

		public xbean.RoleYaoQianShuInfoMaps toBean() {
			return new RoleYaoQianShuInfoMaps(this, null, null);
		}

		@Override
		public xbean.RoleYaoQianShuInfoMaps toDataIf() {
			return this;
		}

		public xbean.RoleYaoQianShuInfoMaps toBeanIf() {
			return new RoleYaoQianShuInfoMaps(this, null, null);
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
		public java.util.Map<Long, xbean.RoleYaoQianShuInfo> getYaoqianshumaps() { // key 为npckey
			return yaoqianshumaps;
		}

		@Override
		public java.util.Map<Long, xbean.RoleYaoQianShuInfo> getYaoqianshumapsAsData() { // key 为npckey
			return yaoqianshumaps;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleYaoQianShuInfoMaps.Data)) return false;
			RoleYaoQianShuInfoMaps.Data _o_ = (RoleYaoQianShuInfoMaps.Data) _o1_;
			if (!yaoqianshumaps.equals(_o_.yaoqianshumaps)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += yaoqianshumaps.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(yaoqianshumaps);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
