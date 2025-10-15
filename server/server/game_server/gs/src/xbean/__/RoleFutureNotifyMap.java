
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleFutureNotifyMap extends mkdb.XBean implements xbean.RoleFutureNotifyMap {
	private java.util.HashMap<Integer, java.util.concurrent.ScheduledFuture<?>> notifymap; // 

	@Override
	public void _reset_unsafe_() {
		notifymap.clear();
	}

	RoleFutureNotifyMap(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		notifymap = new java.util.HashMap<Integer, java.util.concurrent.ScheduledFuture<?>>();
	}

	public RoleFutureNotifyMap() {
		this(0, null, null);
	}

	public RoleFutureNotifyMap(RoleFutureNotifyMap _o_) {
		this(_o_, null, null);
	}

	RoleFutureNotifyMap(xbean.RoleFutureNotifyMap _o1_, mkdb.XBean _xp_, String _vn_) {
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
	public xbean.RoleFutureNotifyMap copy() {
		_xdb_verify_unsafe_();
		return new RoleFutureNotifyMap(this);
	}

	@Override
	public xbean.RoleFutureNotifyMap toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleFutureNotifyMap toBean() {
		_xdb_verify_unsafe_();
		return new RoleFutureNotifyMap(this); // same as copy()
	}

	@Override
	public xbean.RoleFutureNotifyMap toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleFutureNotifyMap toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, java.util.concurrent.ScheduledFuture<?>> getNotifymap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "notifymap"), notifymap);
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleFutureNotifyMap _o_ = null;
		if ( _o1_ instanceof RoleFutureNotifyMap ) _o_ = (RoleFutureNotifyMap)_o1_;
		else if ( _o1_ instanceof RoleFutureNotifyMap.Const ) _o_ = ((RoleFutureNotifyMap.Const)_o1_).nThis();
		else return false;
		if (!notifymap.equals(_o_.notifymap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += notifymap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(notifymap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("notifymap"));
		return lb;
	}

	private class Const implements xbean.RoleFutureNotifyMap {
		RoleFutureNotifyMap nThis() {
			return RoleFutureNotifyMap.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleFutureNotifyMap copy() {
			return RoleFutureNotifyMap.this.copy();
		}

		@Override
		public xbean.RoleFutureNotifyMap toData() {
			return RoleFutureNotifyMap.this.toData();
		}

		public xbean.RoleFutureNotifyMap toBean() {
			return RoleFutureNotifyMap.this.toBean();
		}

		@Override
		public xbean.RoleFutureNotifyMap toDataIf() {
			return RoleFutureNotifyMap.this.toDataIf();
		}

		public xbean.RoleFutureNotifyMap toBeanIf() {
			return RoleFutureNotifyMap.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, java.util.concurrent.ScheduledFuture<?>> getNotifymap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(notifymap);
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
			return RoleFutureNotifyMap.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleFutureNotifyMap.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleFutureNotifyMap.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleFutureNotifyMap.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleFutureNotifyMap.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleFutureNotifyMap.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleFutureNotifyMap.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleFutureNotifyMap.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleFutureNotifyMap.this.toString();
		}

	}

	public static final class Data implements xbean.RoleFutureNotifyMap {
		private java.util.HashMap<Integer, java.util.concurrent.ScheduledFuture<?>> notifymap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			notifymap = new java.util.HashMap<Integer, java.util.concurrent.ScheduledFuture<?>>();
		}

		Data(xbean.RoleFutureNotifyMap _o1_) {
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
		public xbean.RoleFutureNotifyMap copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleFutureNotifyMap toData() {
			return new Data(this);
		}

		public xbean.RoleFutureNotifyMap toBean() {
			return new RoleFutureNotifyMap(this, null, null);
		}

		@Override
		public xbean.RoleFutureNotifyMap toDataIf() {
			return this;
		}

		public xbean.RoleFutureNotifyMap toBeanIf() {
			return new RoleFutureNotifyMap(this, null, null);
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
		public java.util.Map<Integer, java.util.concurrent.ScheduledFuture<?>> getNotifymap() { // 
			return notifymap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleFutureNotifyMap.Data)) return false;
			RoleFutureNotifyMap.Data _o_ = (RoleFutureNotifyMap.Data) _o1_;
			if (!notifymap.equals(_o_.notifymap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += notifymap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(notifymap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
