
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class KickInfo extends mkdb.XBean implements xbean.KickInfo {
	private java.util.LinkedList<Long> kicktime; // 踢人信息 by changhao
	private long expire; // 10分钟内不让登陆 by changhao

	@Override
	public void _reset_unsafe_() {
		kicktime.clear();
		expire = 0L;
	}

	KickInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		kicktime = new java.util.LinkedList<Long>();
	}

	public KickInfo() {
		this(0, null, null);
	}

	public KickInfo(KickInfo _o_) {
		this(_o_, null, null);
	}

	KickInfo(xbean.KickInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof KickInfo) assign((KickInfo)_o1_);
		else if (_o1_ instanceof KickInfo.Data) assign((KickInfo.Data)_o1_);
		else if (_o1_ instanceof KickInfo.Const) assign(((KickInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(KickInfo _o_) {
		_o_._xdb_verify_unsafe_();
		kicktime = new java.util.LinkedList<Long>();
		kicktime.addAll(_o_.kicktime);
		expire = _o_.expire;
	}

	private void assign(KickInfo.Data _o_) {
		kicktime = new java.util.LinkedList<Long>();
		kicktime.addAll(_o_.kicktime);
		expire = _o_.expire;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(kicktime.size());
		for (Long _v_ : kicktime) {
			_os_.marshal(_v_);
		}
		_os_.marshal(expire);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			kicktime.add(_v_);
		}
		expire = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.KickInfo copy() {
		_xdb_verify_unsafe_();
		return new KickInfo(this);
	}

	@Override
	public xbean.KickInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.KickInfo toBean() {
		_xdb_verify_unsafe_();
		return new KickInfo(this); // same as copy()
	}

	@Override
	public xbean.KickInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.KickInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Long> getKicktime() { // 踢人信息 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "kicktime"), kicktime);
	}

	public java.util.List<Long> getKicktimeAsData() { // 踢人信息 by changhao
		_xdb_verify_unsafe_();
		java.util.List<Long> kicktime;
		KickInfo _o_ = this;
		kicktime = new java.util.LinkedList<Long>();
		kicktime.addAll(_o_.kicktime);
		return kicktime;
	}

	@Override
	public long getExpire() { // 10分钟内不让登陆 by changhao
		_xdb_verify_unsafe_();
		return expire;
	}

	@Override
	public void setExpire(long _v_) { // 10分钟内不让登陆 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "expire") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, expire) {
					public void rollback() { expire = _xdb_saved; }
				};}});
		expire = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		KickInfo _o_ = null;
		if ( _o1_ instanceof KickInfo ) _o_ = (KickInfo)_o1_;
		else if ( _o1_ instanceof KickInfo.Const ) _o_ = ((KickInfo.Const)_o1_).nThis();
		else return false;
		if (!kicktime.equals(_o_.kicktime)) return false;
		if (expire != _o_.expire) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += kicktime.hashCode();
		_h_ += expire;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(kicktime);
		_sb_.append(",");
		_sb_.append(expire);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("kicktime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("expire"));
		return lb;
	}

	private class Const implements xbean.KickInfo {
		KickInfo nThis() {
			return KickInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.KickInfo copy() {
			return KickInfo.this.copy();
		}

		@Override
		public xbean.KickInfo toData() {
			return KickInfo.this.toData();
		}

		public xbean.KickInfo toBean() {
			return KickInfo.this.toBean();
		}

		@Override
		public xbean.KickInfo toDataIf() {
			return KickInfo.this.toDataIf();
		}

		public xbean.KickInfo toBeanIf() {
			return KickInfo.this.toBeanIf();
		}

		@Override
		public java.util.List<Long> getKicktime() { // 踢人信息 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(kicktime);
		}

		public java.util.List<Long> getKicktimeAsData() { // 踢人信息 by changhao
			_xdb_verify_unsafe_();
			java.util.List<Long> kicktime;
			KickInfo _o_ = KickInfo.this;
		kicktime = new java.util.LinkedList<Long>();
		kicktime.addAll(_o_.kicktime);
			return kicktime;
		}

		@Override
		public long getExpire() { // 10分钟内不让登陆 by changhao
			_xdb_verify_unsafe_();
			return expire;
		}

		@Override
		public void setExpire(long _v_) { // 10分钟内不让登陆 by changhao
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
			return KickInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return KickInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return KickInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return KickInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return KickInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return KickInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return KickInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return KickInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return KickInfo.this.toString();
		}

	}

	public static final class Data implements xbean.KickInfo {
		private java.util.LinkedList<Long> kicktime; // 踢人信息 by changhao
		private long expire; // 10分钟内不让登陆 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			kicktime = new java.util.LinkedList<Long>();
		}

		Data(xbean.KickInfo _o1_) {
			if (_o1_ instanceof KickInfo) assign((KickInfo)_o1_);
			else if (_o1_ instanceof KickInfo.Data) assign((KickInfo.Data)_o1_);
			else if (_o1_ instanceof KickInfo.Const) assign(((KickInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(KickInfo _o_) {
			kicktime = new java.util.LinkedList<Long>();
			kicktime.addAll(_o_.kicktime);
			expire = _o_.expire;
		}

		private void assign(KickInfo.Data _o_) {
			kicktime = new java.util.LinkedList<Long>();
			kicktime.addAll(_o_.kicktime);
			expire = _o_.expire;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(kicktime.size());
			for (Long _v_ : kicktime) {
				_os_.marshal(_v_);
			}
			_os_.marshal(expire);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				kicktime.add(_v_);
			}
			expire = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.KickInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.KickInfo toData() {
			return new Data(this);
		}

		public xbean.KickInfo toBean() {
			return new KickInfo(this, null, null);
		}

		@Override
		public xbean.KickInfo toDataIf() {
			return this;
		}

		public xbean.KickInfo toBeanIf() {
			return new KickInfo(this, null, null);
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
		public java.util.List<Long> getKicktime() { // 踢人信息 by changhao
			return kicktime;
		}

		@Override
		public java.util.List<Long> getKicktimeAsData() { // 踢人信息 by changhao
			return kicktime;
		}

		@Override
		public long getExpire() { // 10分钟内不让登陆 by changhao
			return expire;
		}

		@Override
		public void setExpire(long _v_) { // 10分钟内不让登陆 by changhao
			expire = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof KickInfo.Data)) return false;
			KickInfo.Data _o_ = (KickInfo.Data) _o1_;
			if (!kicktime.equals(_o_.kicktime)) return false;
			if (expire != _o_.expire) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += kicktime.hashCode();
			_h_ += expire;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(kicktime);
			_sb_.append(",");
			_sb_.append(expire);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
