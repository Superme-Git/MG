
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RolesNotifyMap extends mkdb.XBean implements xbean.RolesNotifyMap {
	private java.util.HashMap<Long, xbean.RoleFutureNotifyMap> rolesfuturemap; // 

	@Override
	public void _reset_unsafe_() {
		rolesfuturemap.clear();
	}

	RolesNotifyMap(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolesfuturemap = new java.util.HashMap<Long, xbean.RoleFutureNotifyMap>();
	}

	public RolesNotifyMap() {
		this(0, null, null);
	}

	public RolesNotifyMap(RolesNotifyMap _o_) {
		this(_o_, null, null);
	}

	RolesNotifyMap(xbean.RolesNotifyMap _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public xbean.RolesNotifyMap copy() {
		_xdb_verify_unsafe_();
		return new RolesNotifyMap(this);
	}

	@Override
	public xbean.RolesNotifyMap toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RolesNotifyMap toBean() {
		_xdb_verify_unsafe_();
		return new RolesNotifyMap(this); // same as copy()
	}

	@Override
	public xbean.RolesNotifyMap toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RolesNotifyMap toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.RoleFutureNotifyMap> getRolesfuturemap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "rolesfuturemap"), rolesfuturemap);
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RolesNotifyMap _o_ = null;
		if ( _o1_ instanceof RolesNotifyMap ) _o_ = (RolesNotifyMap)_o1_;
		else if ( _o1_ instanceof RolesNotifyMap.Const ) _o_ = ((RolesNotifyMap.Const)_o1_).nThis();
		else return false;
		if (!rolesfuturemap.equals(_o_.rolesfuturemap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rolesfuturemap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolesfuturemap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("rolesfuturemap"));
		return lb;
	}

	private class Const implements xbean.RolesNotifyMap {
		RolesNotifyMap nThis() {
			return RolesNotifyMap.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RolesNotifyMap copy() {
			return RolesNotifyMap.this.copy();
		}

		@Override
		public xbean.RolesNotifyMap toData() {
			return RolesNotifyMap.this.toData();
		}

		public xbean.RolesNotifyMap toBean() {
			return RolesNotifyMap.this.toBean();
		}

		@Override
		public xbean.RolesNotifyMap toDataIf() {
			return RolesNotifyMap.this.toDataIf();
		}

		public xbean.RolesNotifyMap toBeanIf() {
			return RolesNotifyMap.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.RoleFutureNotifyMap> getRolesfuturemap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(rolesfuturemap);
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
			return RolesNotifyMap.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RolesNotifyMap.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RolesNotifyMap.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RolesNotifyMap.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RolesNotifyMap.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RolesNotifyMap.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RolesNotifyMap.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RolesNotifyMap.this.hashCode();
		}

		@Override
		public String toString() {
			return RolesNotifyMap.this.toString();
		}

	}

	public static final class Data implements xbean.RolesNotifyMap {
		private java.util.HashMap<Long, xbean.RoleFutureNotifyMap> rolesfuturemap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolesfuturemap = new java.util.HashMap<Long, xbean.RoleFutureNotifyMap>();
		}

		Data(xbean.RolesNotifyMap _o1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RolesNotifyMap copy() {
			return new Data(this);
		}

		@Override
		public xbean.RolesNotifyMap toData() {
			return new Data(this);
		}

		public xbean.RolesNotifyMap toBean() {
			return new RolesNotifyMap(this, null, null);
		}

		@Override
		public xbean.RolesNotifyMap toDataIf() {
			return this;
		}

		public xbean.RolesNotifyMap toBeanIf() {
			return new RolesNotifyMap(this, null, null);
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
		public java.util.Map<Long, xbean.RoleFutureNotifyMap> getRolesfuturemap() { // 
			return rolesfuturemap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RolesNotifyMap.Data)) return false;
			RolesNotifyMap.Data _o_ = (RolesNotifyMap.Data) _o1_;
			if (!rolesfuturemap.equals(_o_.rolesfuturemap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rolesfuturemap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rolesfuturemap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
