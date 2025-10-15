
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class crossMessageBean extends mkdb.XBean implements xbean.crossMessageBean {
	private java.util.LinkedList<String> parms; // 消息参数
	private int messageid; // 消息id

	@Override
	public void _reset_unsafe_() {
		parms.clear();
		messageid = 0;
	}

	crossMessageBean(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		parms = new java.util.LinkedList<String>();
	}

	public crossMessageBean() {
		this(0, null, null);
	}

	public crossMessageBean(crossMessageBean _o_) {
		this(_o_, null, null);
	}

	crossMessageBean(xbean.crossMessageBean _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof crossMessageBean) assign((crossMessageBean)_o1_);
		else if (_o1_ instanceof crossMessageBean.Data) assign((crossMessageBean.Data)_o1_);
		else if (_o1_ instanceof crossMessageBean.Const) assign(((crossMessageBean.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(crossMessageBean _o_) {
		_o_._xdb_verify_unsafe_();
		parms = new java.util.LinkedList<String>();
		parms.addAll(_o_.parms);
		messageid = _o_.messageid;
	}

	private void assign(crossMessageBean.Data _o_) {
		parms = new java.util.LinkedList<String>();
		parms.addAll(_o_.parms);
		messageid = _o_.messageid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(parms.size());
		for (String _v_ : parms) {
			_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
		}
		_os_.marshal(messageid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			String _v_ = "";
			_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			parms.add(_v_);
		}
		messageid = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.crossMessageBean copy() {
		_xdb_verify_unsafe_();
		return new crossMessageBean(this);
	}

	@Override
	public xbean.crossMessageBean toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.crossMessageBean toBean() {
		_xdb_verify_unsafe_();
		return new crossMessageBean(this); // same as copy()
	}

	@Override
	public xbean.crossMessageBean toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.crossMessageBean toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<String> getParms() { // 消息参数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "parms"), parms);
	}

	public java.util.List<String> getParmsAsData() { // 消息参数
		_xdb_verify_unsafe_();
		java.util.List<String> parms;
		crossMessageBean _o_ = this;
		parms = new java.util.LinkedList<String>();
		parms.addAll(_o_.parms);
		return parms;
	}

	@Override
	public int getMessageid() { // 消息id
		_xdb_verify_unsafe_();
		return messageid;
	}

	@Override
	public void setMessageid(int _v_) { // 消息id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "messageid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, messageid) {
					public void rollback() { messageid = _xdb_saved; }
				};}});
		messageid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		crossMessageBean _o_ = null;
		if ( _o1_ instanceof crossMessageBean ) _o_ = (crossMessageBean)_o1_;
		else if ( _o1_ instanceof crossMessageBean.Const ) _o_ = ((crossMessageBean.Const)_o1_).nThis();
		else return false;
		if (!parms.equals(_o_.parms)) return false;
		if (messageid != _o_.messageid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += parms.hashCode();
		_h_ += messageid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(parms);
		_sb_.append(",");
		_sb_.append(messageid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("parms"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("messageid"));
		return lb;
	}

	private class Const implements xbean.crossMessageBean {
		crossMessageBean nThis() {
			return crossMessageBean.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.crossMessageBean copy() {
			return crossMessageBean.this.copy();
		}

		@Override
		public xbean.crossMessageBean toData() {
			return crossMessageBean.this.toData();
		}

		public xbean.crossMessageBean toBean() {
			return crossMessageBean.this.toBean();
		}

		@Override
		public xbean.crossMessageBean toDataIf() {
			return crossMessageBean.this.toDataIf();
		}

		public xbean.crossMessageBean toBeanIf() {
			return crossMessageBean.this.toBeanIf();
		}

		@Override
		public java.util.List<String> getParms() { // 消息参数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(parms);
		}

		public java.util.List<String> getParmsAsData() { // 消息参数
			_xdb_verify_unsafe_();
			java.util.List<String> parms;
			crossMessageBean _o_ = crossMessageBean.this;
		parms = new java.util.LinkedList<String>();
		parms.addAll(_o_.parms);
			return parms;
		}

		@Override
		public int getMessageid() { // 消息id
			_xdb_verify_unsafe_();
			return messageid;
		}

		@Override
		public void setMessageid(int _v_) { // 消息id
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
			return crossMessageBean.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return crossMessageBean.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return crossMessageBean.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return crossMessageBean.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return crossMessageBean.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return crossMessageBean.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return crossMessageBean.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return crossMessageBean.this.hashCode();
		}

		@Override
		public String toString() {
			return crossMessageBean.this.toString();
		}

	}

	public static final class Data implements xbean.crossMessageBean {
		private java.util.LinkedList<String> parms; // 消息参数
		private int messageid; // 消息id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			parms = new java.util.LinkedList<String>();
		}

		Data(xbean.crossMessageBean _o1_) {
			if (_o1_ instanceof crossMessageBean) assign((crossMessageBean)_o1_);
			else if (_o1_ instanceof crossMessageBean.Data) assign((crossMessageBean.Data)_o1_);
			else if (_o1_ instanceof crossMessageBean.Const) assign(((crossMessageBean.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(crossMessageBean _o_) {
			parms = new java.util.LinkedList<String>();
			parms.addAll(_o_.parms);
			messageid = _o_.messageid;
		}

		private void assign(crossMessageBean.Data _o_) {
			parms = new java.util.LinkedList<String>();
			parms.addAll(_o_.parms);
			messageid = _o_.messageid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(parms.size());
			for (String _v_ : parms) {
				_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
			}
			_os_.marshal(messageid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				parms.add(_v_);
			}
			messageid = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.crossMessageBean copy() {
			return new Data(this);
		}

		@Override
		public xbean.crossMessageBean toData() {
			return new Data(this);
		}

		public xbean.crossMessageBean toBean() {
			return new crossMessageBean(this, null, null);
		}

		@Override
		public xbean.crossMessageBean toDataIf() {
			return this;
		}

		public xbean.crossMessageBean toBeanIf() {
			return new crossMessageBean(this, null, null);
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
		public java.util.List<String> getParms() { // 消息参数
			return parms;
		}

		@Override
		public java.util.List<String> getParmsAsData() { // 消息参数
			return parms;
		}

		@Override
		public int getMessageid() { // 消息id
			return messageid;
		}

		@Override
		public void setMessageid(int _v_) { // 消息id
			messageid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof crossMessageBean.Data)) return false;
			crossMessageBean.Data _o_ = (crossMessageBean.Data) _o1_;
			if (!parms.equals(_o_.parms)) return false;
			if (messageid != _o_.messageid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += parms.hashCode();
			_h_ += messageid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(parms);
			_sb_.append(",");
			_sb_.append(messageid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
