
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class NotifyList extends mkdb.XBean implements xbean.NotifyList {
	private int notifytypeid; // 
	private java.util.LinkedList<Integer> buffids; // 

	@Override
	public void _reset_unsafe_() {
		notifytypeid = 0;
		buffids.clear();
	}

	NotifyList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		buffids = new java.util.LinkedList<Integer>();
	}

	public NotifyList() {
		this(0, null, null);
	}

	public NotifyList(NotifyList _o_) {
		this(_o_, null, null);
	}

	NotifyList(xbean.NotifyList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof NotifyList) assign((NotifyList)_o1_);
		else if (_o1_ instanceof NotifyList.Data) assign((NotifyList.Data)_o1_);
		else if (_o1_ instanceof NotifyList.Const) assign(((NotifyList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(NotifyList _o_) {
		_o_._xdb_verify_unsafe_();
		notifytypeid = _o_.notifytypeid;
		buffids = new java.util.LinkedList<Integer>();
		buffids.addAll(_o_.buffids);
	}

	private void assign(NotifyList.Data _o_) {
		notifytypeid = _o_.notifytypeid;
		buffids = new java.util.LinkedList<Integer>();
		buffids.addAll(_o_.buffids);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(notifytypeid);
		_os_.compact_uint32(buffids.size());
		for (Integer _v_ : buffids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		notifytypeid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			buffids.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.NotifyList copy() {
		_xdb_verify_unsafe_();
		return new NotifyList(this);
	}

	@Override
	public xbean.NotifyList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NotifyList toBean() {
		_xdb_verify_unsafe_();
		return new NotifyList(this); // same as copy()
	}

	@Override
	public xbean.NotifyList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NotifyList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getNotifytypeid() { // 
		_xdb_verify_unsafe_();
		return notifytypeid;
	}

	@Override
	public java.util.List<Integer> getBuffids() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "buffids"), buffids);
	}

	public java.util.List<Integer> getBuffidsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<Integer> buffids;
		NotifyList _o_ = this;
		buffids = new java.util.LinkedList<Integer>();
		buffids.addAll(_o_.buffids);
		return buffids;
	}

	@Override
	public void setNotifytypeid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "notifytypeid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, notifytypeid) {
					public void rollback() { notifytypeid = _xdb_saved; }
				};}});
		notifytypeid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		NotifyList _o_ = null;
		if ( _o1_ instanceof NotifyList ) _o_ = (NotifyList)_o1_;
		else if ( _o1_ instanceof NotifyList.Const ) _o_ = ((NotifyList.Const)_o1_).nThis();
		else return false;
		if (notifytypeid != _o_.notifytypeid) return false;
		if (!buffids.equals(_o_.buffids)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += notifytypeid;
		_h_ += buffids.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(notifytypeid);
		_sb_.append(",");
		_sb_.append(buffids);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("notifytypeid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buffids"));
		return lb;
	}

	private class Const implements xbean.NotifyList {
		NotifyList nThis() {
			return NotifyList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.NotifyList copy() {
			return NotifyList.this.copy();
		}

		@Override
		public xbean.NotifyList toData() {
			return NotifyList.this.toData();
		}

		public xbean.NotifyList toBean() {
			return NotifyList.this.toBean();
		}

		@Override
		public xbean.NotifyList toDataIf() {
			return NotifyList.this.toDataIf();
		}

		public xbean.NotifyList toBeanIf() {
			return NotifyList.this.toBeanIf();
		}

		@Override
		public int getNotifytypeid() { // 
			_xdb_verify_unsafe_();
			return notifytypeid;
		}

		@Override
		public java.util.List<Integer> getBuffids() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(buffids);
		}

		public java.util.List<Integer> getBuffidsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<Integer> buffids;
			NotifyList _o_ = NotifyList.this;
		buffids = new java.util.LinkedList<Integer>();
		buffids.addAll(_o_.buffids);
			return buffids;
		}

		@Override
		public void setNotifytypeid(int _v_) { // 
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
			return NotifyList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return NotifyList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return NotifyList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return NotifyList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return NotifyList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return NotifyList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return NotifyList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return NotifyList.this.hashCode();
		}

		@Override
		public String toString() {
			return NotifyList.this.toString();
		}

	}

	public static final class Data implements xbean.NotifyList {
		private int notifytypeid; // 
		private java.util.LinkedList<Integer> buffids; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			buffids = new java.util.LinkedList<Integer>();
		}

		Data(xbean.NotifyList _o1_) {
			if (_o1_ instanceof NotifyList) assign((NotifyList)_o1_);
			else if (_o1_ instanceof NotifyList.Data) assign((NotifyList.Data)_o1_);
			else if (_o1_ instanceof NotifyList.Const) assign(((NotifyList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(NotifyList _o_) {
			notifytypeid = _o_.notifytypeid;
			buffids = new java.util.LinkedList<Integer>();
			buffids.addAll(_o_.buffids);
		}

		private void assign(NotifyList.Data _o_) {
			notifytypeid = _o_.notifytypeid;
			buffids = new java.util.LinkedList<Integer>();
			buffids.addAll(_o_.buffids);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(notifytypeid);
			_os_.compact_uint32(buffids.size());
			for (Integer _v_ : buffids) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			notifytypeid = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				buffids.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.NotifyList copy() {
			return new Data(this);
		}

		@Override
		public xbean.NotifyList toData() {
			return new Data(this);
		}

		public xbean.NotifyList toBean() {
			return new NotifyList(this, null, null);
		}

		@Override
		public xbean.NotifyList toDataIf() {
			return this;
		}

		public xbean.NotifyList toBeanIf() {
			return new NotifyList(this, null, null);
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
		public int getNotifytypeid() { // 
			return notifytypeid;
		}

		@Override
		public java.util.List<Integer> getBuffids() { // 
			return buffids;
		}

		@Override
		public java.util.List<Integer> getBuffidsAsData() { // 
			return buffids;
		}

		@Override
		public void setNotifytypeid(int _v_) { // 
			notifytypeid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof NotifyList.Data)) return false;
			NotifyList.Data _o_ = (NotifyList.Data) _o1_;
			if (notifytypeid != _o_.notifytypeid) return false;
			if (!buffids.equals(_o_.buffids)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += notifytypeid;
			_h_ += buffids.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(notifytypeid);
			_sb_.append(",");
			_sb_.append(buffids);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
