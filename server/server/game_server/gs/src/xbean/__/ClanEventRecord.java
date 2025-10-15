
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanEventRecord extends mkdb.XBean implements xbean.ClanEventRecord {
	private long time; // 时间
	private int eventtype; // 时间
	private String content; // 内容
	private long objectroleid; // 内容

	@Override
	public void _reset_unsafe_() {
		time = 0L;
		eventtype = 0;
		content = "";
		objectroleid = 0L;
	}

	ClanEventRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		content = "";
	}

	public ClanEventRecord() {
		this(0, null, null);
	}

	public ClanEventRecord(ClanEventRecord _o_) {
		this(_o_, null, null);
	}

	ClanEventRecord(xbean.ClanEventRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanEventRecord) assign((ClanEventRecord)_o1_);
		else if (_o1_ instanceof ClanEventRecord.Data) assign((ClanEventRecord.Data)_o1_);
		else if (_o1_ instanceof ClanEventRecord.Const) assign(((ClanEventRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanEventRecord _o_) {
		_o_._xdb_verify_unsafe_();
		time = _o_.time;
		eventtype = _o_.eventtype;
		content = _o_.content;
		objectroleid = _o_.objectroleid;
	}

	private void assign(ClanEventRecord.Data _o_) {
		time = _o_.time;
		eventtype = _o_.eventtype;
		content = _o_.content;
		objectroleid = _o_.objectroleid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(time);
		_os_.marshal(eventtype);
		_os_.marshal(content, mkdb.Const.IO_CHARSET);
		_os_.marshal(objectroleid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		time = _os_.unmarshal_long();
		eventtype = _os_.unmarshal_int();
		content = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		objectroleid = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ClanEventRecord copy() {
		_xdb_verify_unsafe_();
		return new ClanEventRecord(this);
	}

	@Override
	public xbean.ClanEventRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanEventRecord toBean() {
		_xdb_verify_unsafe_();
		return new ClanEventRecord(this); // same as copy()
	}

	@Override
	public xbean.ClanEventRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanEventRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTime() { // 时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public int getEventtype() { // 时间
		_xdb_verify_unsafe_();
		return eventtype;
	}

	@Override
	public String getContent() { // 内容
		_xdb_verify_unsafe_();
		return content;
	}

	@Override
	public com.locojoy.base.Octets getContentOctets() { // 内容
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getContent(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getObjectroleid() { // 内容
		_xdb_verify_unsafe_();
		return objectroleid;
	}

	@Override
	public void setTime(long _v_) { // 时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public void setEventtype(int _v_) { // 时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "eventtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, eventtype) {
					public void rollback() { eventtype = _xdb_saved; }
				};}});
		eventtype = _v_;
	}

	@Override
	public void setContent(String _v_) { // 内容
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "content") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, content) {
					public void rollback() { content = _xdb_saved; }
				};}});
		content = _v_;
	}

	@Override
	public void setContentOctets(com.locojoy.base.Octets _v_) { // 内容
		_xdb_verify_unsafe_();
		this.setContent(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setObjectroleid(long _v_) { // 内容
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "objectroleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, objectroleid) {
					public void rollback() { objectroleid = _xdb_saved; }
				};}});
		objectroleid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanEventRecord _o_ = null;
		if ( _o1_ instanceof ClanEventRecord ) _o_ = (ClanEventRecord)_o1_;
		else if ( _o1_ instanceof ClanEventRecord.Const ) _o_ = ((ClanEventRecord.Const)_o1_).nThis();
		else return false;
		if (time != _o_.time) return false;
		if (eventtype != _o_.eventtype) return false;
		if (!content.equals(_o_.content)) return false;
		if (objectroleid != _o_.objectroleid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += time;
		_h_ += eventtype;
		_h_ += content.hashCode();
		_h_ += objectroleid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(eventtype);
		_sb_.append(",");
		_sb_.append("'").append(content).append("'");
		_sb_.append(",");
		_sb_.append(objectroleid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("eventtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("content"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("objectroleid"));
		return lb;
	}

	private class Const implements xbean.ClanEventRecord {
		ClanEventRecord nThis() {
			return ClanEventRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanEventRecord copy() {
			return ClanEventRecord.this.copy();
		}

		@Override
		public xbean.ClanEventRecord toData() {
			return ClanEventRecord.this.toData();
		}

		public xbean.ClanEventRecord toBean() {
			return ClanEventRecord.this.toBean();
		}

		@Override
		public xbean.ClanEventRecord toDataIf() {
			return ClanEventRecord.this.toDataIf();
		}

		public xbean.ClanEventRecord toBeanIf() {
			return ClanEventRecord.this.toBeanIf();
		}

		@Override
		public long getTime() { // 时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public int getEventtype() { // 时间
			_xdb_verify_unsafe_();
			return eventtype;
		}

		@Override
		public String getContent() { // 内容
			_xdb_verify_unsafe_();
			return content;
		}

		@Override
		public com.locojoy.base.Octets getContentOctets() { // 内容
			_xdb_verify_unsafe_();
			return ClanEventRecord.this.getContentOctets();
		}

		@Override
		public long getObjectroleid() { // 内容
			_xdb_verify_unsafe_();
			return objectroleid;
		}

		@Override
		public void setTime(long _v_) { // 时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEventtype(int _v_) { // 时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setContent(String _v_) { // 内容
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setContentOctets(com.locojoy.base.Octets _v_) { // 内容
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setObjectroleid(long _v_) { // 内容
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
			return ClanEventRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanEventRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanEventRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanEventRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanEventRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanEventRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanEventRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanEventRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanEventRecord.this.toString();
		}

	}

	public static final class Data implements xbean.ClanEventRecord {
		private long time; // 时间
		private int eventtype; // 时间
		private String content; // 内容
		private long objectroleid; // 内容

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			content = "";
		}

		Data(xbean.ClanEventRecord _o1_) {
			if (_o1_ instanceof ClanEventRecord) assign((ClanEventRecord)_o1_);
			else if (_o1_ instanceof ClanEventRecord.Data) assign((ClanEventRecord.Data)_o1_);
			else if (_o1_ instanceof ClanEventRecord.Const) assign(((ClanEventRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanEventRecord _o_) {
			time = _o_.time;
			eventtype = _o_.eventtype;
			content = _o_.content;
			objectroleid = _o_.objectroleid;
		}

		private void assign(ClanEventRecord.Data _o_) {
			time = _o_.time;
			eventtype = _o_.eventtype;
			content = _o_.content;
			objectroleid = _o_.objectroleid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(time);
			_os_.marshal(eventtype);
			_os_.marshal(content, mkdb.Const.IO_CHARSET);
			_os_.marshal(objectroleid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			time = _os_.unmarshal_long();
			eventtype = _os_.unmarshal_int();
			content = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			objectroleid = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ClanEventRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanEventRecord toData() {
			return new Data(this);
		}

		public xbean.ClanEventRecord toBean() {
			return new ClanEventRecord(this, null, null);
		}

		@Override
		public xbean.ClanEventRecord toDataIf() {
			return this;
		}

		public xbean.ClanEventRecord toBeanIf() {
			return new ClanEventRecord(this, null, null);
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
		public long getTime() { // 时间
			return time;
		}

		@Override
		public int getEventtype() { // 时间
			return eventtype;
		}

		@Override
		public String getContent() { // 内容
			return content;
		}

		@Override
		public com.locojoy.base.Octets getContentOctets() { // 内容
			return com.locojoy.base.Octets.wrap(getContent(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getObjectroleid() { // 内容
			return objectroleid;
		}

		@Override
		public void setTime(long _v_) { // 时间
			time = _v_;
		}

		@Override
		public void setEventtype(int _v_) { // 时间
			eventtype = _v_;
		}

		@Override
		public void setContent(String _v_) { // 内容
			if (null == _v_)
				throw new NullPointerException();
			content = _v_;
		}

		@Override
		public void setContentOctets(com.locojoy.base.Octets _v_) { // 内容
			this.setContent(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setObjectroleid(long _v_) { // 内容
			objectroleid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanEventRecord.Data)) return false;
			ClanEventRecord.Data _o_ = (ClanEventRecord.Data) _o1_;
			if (time != _o_.time) return false;
			if (eventtype != _o_.eventtype) return false;
			if (!content.equals(_o_.content)) return false;
			if (objectroleid != _o_.objectroleid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += time;
			_h_ += eventtype;
			_h_ += content.hashCode();
			_h_ += objectroleid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(eventtype);
			_sb_.append(",");
			_sb_.append("'").append(content).append("'");
			_sb_.append(",");
			_sb_.append(objectroleid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
