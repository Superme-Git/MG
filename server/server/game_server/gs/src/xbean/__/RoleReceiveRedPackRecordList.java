
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleReceiveRedPackRecordList extends mkdb.XBean implements xbean.RoleReceiveRedPackRecordList {
	private java.util.HashMap<String, xbean.RoleReceiveRedPackRecord> rolereceiveredpacklist; // 

	@Override
	public void _reset_unsafe_() {
		rolereceiveredpacklist.clear();
	}

	RoleReceiveRedPackRecordList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>();
	}

	public RoleReceiveRedPackRecordList() {
		this(0, null, null);
	}

	public RoleReceiveRedPackRecordList(RoleReceiveRedPackRecordList _o_) {
		this(_o_, null, null);
	}

	RoleReceiveRedPackRecordList(xbean.RoleReceiveRedPackRecordList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleReceiveRedPackRecordList) assign((RoleReceiveRedPackRecordList)_o1_);
		else if (_o1_ instanceof RoleReceiveRedPackRecordList.Data) assign((RoleReceiveRedPackRecordList.Data)_o1_);
		else if (_o1_ instanceof RoleReceiveRedPackRecordList.Const) assign(((RoleReceiveRedPackRecordList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleReceiveRedPackRecordList _o_) {
		_o_._xdb_verify_unsafe_();
		rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>();
		for (java.util.Map.Entry<String, xbean.RoleReceiveRedPackRecord> _e_ : _o_.rolereceiveredpacklist.entrySet())
			rolereceiveredpacklist.put(_e_.getKey(), new RoleReceiveRedPackRecord(_e_.getValue(), this, "rolereceiveredpacklist"));
	}

	private void assign(RoleReceiveRedPackRecordList.Data _o_) {
		rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>();
		for (java.util.Map.Entry<String, xbean.RoleReceiveRedPackRecord> _e_ : _o_.rolereceiveredpacklist.entrySet())
			rolereceiveredpacklist.put(_e_.getKey(), new RoleReceiveRedPackRecord(_e_.getValue(), this, "rolereceiveredpacklist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(rolereceiveredpacklist.size());
		for (java.util.Map.Entry<String, xbean.RoleReceiveRedPackRecord> _e_ : rolereceiveredpacklist.entrySet())
		{
			_os_.marshal(_e_.getKey(), mkdb.Const.IO_CHARSET);
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
				rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>(size * 2);
			}
			for (; size > 0; --size)
			{
				String _k_ = "";
				_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				xbean.RoleReceiveRedPackRecord _v_ = new RoleReceiveRedPackRecord(0, this, "rolereceiveredpacklist");
				_v_.unmarshal(_os_);
				rolereceiveredpacklist.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleReceiveRedPackRecordList copy() {
		_xdb_verify_unsafe_();
		return new RoleReceiveRedPackRecordList(this);
	}

	@Override
	public xbean.RoleReceiveRedPackRecordList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleReceiveRedPackRecordList toBean() {
		_xdb_verify_unsafe_();
		return new RoleReceiveRedPackRecordList(this); // same as copy()
	}

	@Override
	public xbean.RoleReceiveRedPackRecordList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleReceiveRedPackRecordList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<String, xbean.RoleReceiveRedPackRecord> getRolereceiveredpacklist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "rolereceiveredpacklist"), rolereceiveredpacklist);
	}

	@Override
	public java.util.Map<String, xbean.RoleReceiveRedPackRecord> getRolereceiveredpacklistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<String, xbean.RoleReceiveRedPackRecord> rolereceiveredpacklist;
		RoleReceiveRedPackRecordList _o_ = this;
		rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>();
		for (java.util.Map.Entry<String, xbean.RoleReceiveRedPackRecord> _e_ : _o_.rolereceiveredpacklist.entrySet())
			rolereceiveredpacklist.put(_e_.getKey(), new RoleReceiveRedPackRecord.Data(_e_.getValue()));
		return rolereceiveredpacklist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleReceiveRedPackRecordList _o_ = null;
		if ( _o1_ instanceof RoleReceiveRedPackRecordList ) _o_ = (RoleReceiveRedPackRecordList)_o1_;
		else if ( _o1_ instanceof RoleReceiveRedPackRecordList.Const ) _o_ = ((RoleReceiveRedPackRecordList.Const)_o1_).nThis();
		else return false;
		if (!rolereceiveredpacklist.equals(_o_.rolereceiveredpacklist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rolereceiveredpacklist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolereceiveredpacklist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("rolereceiveredpacklist"));
		return lb;
	}

	private class Const implements xbean.RoleReceiveRedPackRecordList {
		RoleReceiveRedPackRecordList nThis() {
			return RoleReceiveRedPackRecordList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleReceiveRedPackRecordList copy() {
			return RoleReceiveRedPackRecordList.this.copy();
		}

		@Override
		public xbean.RoleReceiveRedPackRecordList toData() {
			return RoleReceiveRedPackRecordList.this.toData();
		}

		public xbean.RoleReceiveRedPackRecordList toBean() {
			return RoleReceiveRedPackRecordList.this.toBean();
		}

		@Override
		public xbean.RoleReceiveRedPackRecordList toDataIf() {
			return RoleReceiveRedPackRecordList.this.toDataIf();
		}

		public xbean.RoleReceiveRedPackRecordList toBeanIf() {
			return RoleReceiveRedPackRecordList.this.toBeanIf();
		}

		@Override
		public java.util.Map<String, xbean.RoleReceiveRedPackRecord> getRolereceiveredpacklist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(rolereceiveredpacklist);
		}

		@Override
		public java.util.Map<String, xbean.RoleReceiveRedPackRecord> getRolereceiveredpacklistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<String, xbean.RoleReceiveRedPackRecord> rolereceiveredpacklist;
			RoleReceiveRedPackRecordList _o_ = RoleReceiveRedPackRecordList.this;
			rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>();
			for (java.util.Map.Entry<String, xbean.RoleReceiveRedPackRecord> _e_ : _o_.rolereceiveredpacklist.entrySet())
				rolereceiveredpacklist.put(_e_.getKey(), new RoleReceiveRedPackRecord.Data(_e_.getValue()));
			return rolereceiveredpacklist;
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
			return RoleReceiveRedPackRecordList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleReceiveRedPackRecordList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleReceiveRedPackRecordList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleReceiveRedPackRecordList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleReceiveRedPackRecordList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleReceiveRedPackRecordList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleReceiveRedPackRecordList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleReceiveRedPackRecordList.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleReceiveRedPackRecordList.this.toString();
		}

	}

	public static final class Data implements xbean.RoleReceiveRedPackRecordList {
		private java.util.HashMap<String, xbean.RoleReceiveRedPackRecord> rolereceiveredpacklist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>();
		}

		Data(xbean.RoleReceiveRedPackRecordList _o1_) {
			if (_o1_ instanceof RoleReceiveRedPackRecordList) assign((RoleReceiveRedPackRecordList)_o1_);
			else if (_o1_ instanceof RoleReceiveRedPackRecordList.Data) assign((RoleReceiveRedPackRecordList.Data)_o1_);
			else if (_o1_ instanceof RoleReceiveRedPackRecordList.Const) assign(((RoleReceiveRedPackRecordList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleReceiveRedPackRecordList _o_) {
			rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>();
			for (java.util.Map.Entry<String, xbean.RoleReceiveRedPackRecord> _e_ : _o_.rolereceiveredpacklist.entrySet())
				rolereceiveredpacklist.put(_e_.getKey(), new RoleReceiveRedPackRecord.Data(_e_.getValue()));
		}

		private void assign(RoleReceiveRedPackRecordList.Data _o_) {
			rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>();
			for (java.util.Map.Entry<String, xbean.RoleReceiveRedPackRecord> _e_ : _o_.rolereceiveredpacklist.entrySet())
				rolereceiveredpacklist.put(_e_.getKey(), new RoleReceiveRedPackRecord.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(rolereceiveredpacklist.size());
			for (java.util.Map.Entry<String, xbean.RoleReceiveRedPackRecord> _e_ : rolereceiveredpacklist.entrySet())
			{
				_os_.marshal(_e_.getKey(), mkdb.Const.IO_CHARSET);
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					rolereceiveredpacklist = new java.util.HashMap<String, xbean.RoleReceiveRedPackRecord>(size * 2);
				}
				for (; size > 0; --size)
				{
					String _k_ = "";
					_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
					xbean.RoleReceiveRedPackRecord _v_ = xbean.Pod.newRoleReceiveRedPackRecordData();
					_v_.unmarshal(_os_);
					rolereceiveredpacklist.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleReceiveRedPackRecordList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleReceiveRedPackRecordList toData() {
			return new Data(this);
		}

		public xbean.RoleReceiveRedPackRecordList toBean() {
			return new RoleReceiveRedPackRecordList(this, null, null);
		}

		@Override
		public xbean.RoleReceiveRedPackRecordList toDataIf() {
			return this;
		}

		public xbean.RoleReceiveRedPackRecordList toBeanIf() {
			return new RoleReceiveRedPackRecordList(this, null, null);
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
		public java.util.Map<String, xbean.RoleReceiveRedPackRecord> getRolereceiveredpacklist() { // 
			return rolereceiveredpacklist;
		}

		@Override
		public java.util.Map<String, xbean.RoleReceiveRedPackRecord> getRolereceiveredpacklistAsData() { // 
			return rolereceiveredpacklist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleReceiveRedPackRecordList.Data)) return false;
			RoleReceiveRedPackRecordList.Data _o_ = (RoleReceiveRedPackRecordList.Data) _o1_;
			if (!rolereceiveredpacklist.equals(_o_.rolereceiveredpacklist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rolereceiveredpacklist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rolereceiveredpacklist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
