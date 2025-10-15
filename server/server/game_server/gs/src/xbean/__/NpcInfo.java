
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class NpcInfo extends mkdb.XBean implements xbean.NpcInfo {
	private int isinbattle; // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除

	@Override
	public void _reset_unsafe_() {
		isinbattle = 0;
	}

	NpcInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public NpcInfo() {
		this(0, null, null);
	}

	public NpcInfo(NpcInfo _o_) {
		this(_o_, null, null);
	}

	NpcInfo(xbean.NpcInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof NpcInfo) assign((NpcInfo)_o1_);
		else if (_o1_ instanceof NpcInfo.Data) assign((NpcInfo.Data)_o1_);
		else if (_o1_ instanceof NpcInfo.Const) assign(((NpcInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(NpcInfo _o_) {
		_o_._xdb_verify_unsafe_();
		isinbattle = _o_.isinbattle;
	}

	private void assign(NpcInfo.Data _o_) {
		isinbattle = _o_.isinbattle;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(isinbattle);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		isinbattle = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.NpcInfo copy() {
		_xdb_verify_unsafe_();
		return new NpcInfo(this);
	}

	@Override
	public xbean.NpcInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NpcInfo toBean() {
		_xdb_verify_unsafe_();
		return new NpcInfo(this); // same as copy()
	}

	@Override
	public xbean.NpcInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NpcInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getIsinbattle() { // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除
		_xdb_verify_unsafe_();
		return isinbattle;
	}

	@Override
	public void setIsinbattle(int _v_) { // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isinbattle") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, isinbattle) {
					public void rollback() { isinbattle = _xdb_saved; }
				};}});
		isinbattle = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		NpcInfo _o_ = null;
		if ( _o1_ instanceof NpcInfo ) _o_ = (NpcInfo)_o1_;
		else if ( _o1_ instanceof NpcInfo.Const ) _o_ = ((NpcInfo.Const)_o1_).nThis();
		else return false;
		if (isinbattle != _o_.isinbattle) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += isinbattle;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(isinbattle);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isinbattle"));
		return lb;
	}

	private class Const implements xbean.NpcInfo {
		NpcInfo nThis() {
			return NpcInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.NpcInfo copy() {
			return NpcInfo.this.copy();
		}

		@Override
		public xbean.NpcInfo toData() {
			return NpcInfo.this.toData();
		}

		public xbean.NpcInfo toBean() {
			return NpcInfo.this.toBean();
		}

		@Override
		public xbean.NpcInfo toDataIf() {
			return NpcInfo.this.toDataIf();
		}

		public xbean.NpcInfo toBeanIf() {
			return NpcInfo.this.toBeanIf();
		}

		@Override
		public int getIsinbattle() { // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除
			_xdb_verify_unsafe_();
			return isinbattle;
		}

		@Override
		public void setIsinbattle(int _v_) { // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除
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
			return NpcInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return NpcInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return NpcInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return NpcInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return NpcInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return NpcInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return NpcInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return NpcInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return NpcInfo.this.toString();
		}

	}

	public static final class Data implements xbean.NpcInfo {
		private int isinbattle; // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.NpcInfo _o1_) {
			if (_o1_ instanceof NpcInfo) assign((NpcInfo)_o1_);
			else if (_o1_ instanceof NpcInfo.Data) assign((NpcInfo.Data)_o1_);
			else if (_o1_ instanceof NpcInfo.Const) assign(((NpcInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(NpcInfo _o_) {
			isinbattle = _o_.isinbattle;
		}

		private void assign(NpcInfo.Data _o_) {
			isinbattle = _o_.isinbattle;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(isinbattle);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			isinbattle = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.NpcInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.NpcInfo toData() {
			return new Data(this);
		}

		public xbean.NpcInfo toBean() {
			return new NpcInfo(this, null, null);
		}

		@Override
		public xbean.NpcInfo toDataIf() {
			return this;
		}

		public xbean.NpcInfo toBeanIf() {
			return new NpcInfo(this, null, null);
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
		public int getIsinbattle() { // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除
			return isinbattle;
		}

		@Override
		public void setIsinbattle(int _v_) { // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除
			isinbattle = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof NpcInfo.Data)) return false;
			NpcInfo.Data _o_ = (NpcInfo.Data) _o1_;
			if (isinbattle != _o_.isinbattle) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += isinbattle;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(isinbattle);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
