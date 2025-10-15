
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class NameState extends mkdb.XBean implements xbean.NameState {
	private int state; // ���ַ���״̬
	private int localid; // ���ַ����Զ�˷�����
	private String peerip; // ���ַ����Զ�˷�������ַ
	private long time; // ���ַ����ʱ��

	@Override
	public void _reset_unsafe_() {
		state = 0;
		localid = 0;
		peerip = "";
		time = 0L;
	}

	NameState(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		peerip = "";
	}

	public NameState() {
		this(0, null, null);
	}

	public NameState(NameState _o_) {
		this(_o_, null, null);
	}

	NameState(xbean.NameState _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof NameState) assign((NameState)_o1_);
		else if (_o1_ instanceof NameState.Data) assign((NameState.Data)_o1_);
		else if (_o1_ instanceof NameState.Const) assign(((NameState.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(NameState _o_) {
		_o_._xdb_verify_unsafe_();
		state = _o_.state;
		localid = _o_.localid;
		peerip = _o_.peerip;
		time = _o_.time;
	}

	private void assign(NameState.Data _o_) {
		state = _o_.state;
		localid = _o_.localid;
		peerip = _o_.peerip;
		time = _o_.time;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(state);
		_os_.marshal(localid);
		_os_.marshal(peerip, mkdb.Const.IO_CHARSET);
		_os_.marshal(time);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		state = _os_.unmarshal_int();
		localid = _os_.unmarshal_int();
		peerip = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		time = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.NameState copy() {
		_xdb_verify_unsafe_();
		return new NameState(this);
	}

	@Override
	public xbean.NameState toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NameState toBean() {
		_xdb_verify_unsafe_();
		return new NameState(this); // same as copy()
	}

	@Override
	public xbean.NameState toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NameState toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getState() { // ���ַ���״̬
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public int getLocalid() { // ���ַ����Զ�˷�����
		_xdb_verify_unsafe_();
		return localid;
	}

	@Override
	public String getPeerip() { // ���ַ����Զ�˷�������ַ
		_xdb_verify_unsafe_();
		return peerip;
	}

	@Override
	public com.locojoy.base.Octets getPeeripOctets() { // ���ַ����Զ�˷�������ַ
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPeerip(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getTime() { // ���ַ����ʱ��
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public void setState(int _v_) { // ���ַ���״̬
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public void setLocalid(int _v_) { // ���ַ����Զ�˷�����
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "localid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, localid) {
					public void rollback() { localid = _xdb_saved; }
				};}});
		localid = _v_;
	}

	@Override
	public void setPeerip(String _v_) { // ���ַ����Զ�˷�������ַ
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "peerip") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, peerip) {
					public void rollback() { peerip = _xdb_saved; }
				};}});
		peerip = _v_;
	}

	@Override
	public void setPeeripOctets(com.locojoy.base.Octets _v_) { // ���ַ����Զ�˷�������ַ
		_xdb_verify_unsafe_();
		this.setPeerip(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setTime(long _v_) { // ���ַ����ʱ��
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		NameState _o_ = null;
		if ( _o1_ instanceof NameState ) _o_ = (NameState)_o1_;
		else if ( _o1_ instanceof NameState.Const ) _o_ = ((NameState.Const)_o1_).nThis();
		else return false;
		if (state != _o_.state) return false;
		if (localid != _o_.localid) return false;
		if (!peerip.equals(_o_.peerip)) return false;
		if (time != _o_.time) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += state;
		_h_ += localid;
		_h_ += peerip.hashCode();
		_h_ += time;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(localid);
		_sb_.append(",");
		_sb_.append("'").append(peerip).append("'");
		_sb_.append(",");
		_sb_.append(time);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("localid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("peerip"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		return lb;
	}

	private class Const implements xbean.NameState {
		NameState nThis() {
			return NameState.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.NameState copy() {
			return NameState.this.copy();
		}

		@Override
		public xbean.NameState toData() {
			return NameState.this.toData();
		}

		public xbean.NameState toBean() {
			return NameState.this.toBean();
		}

		@Override
		public xbean.NameState toDataIf() {
			return NameState.this.toDataIf();
		}

		public xbean.NameState toBeanIf() {
			return NameState.this.toBeanIf();
		}

		@Override
		public int getState() { // ���ַ���״̬
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public int getLocalid() { // ���ַ����Զ�˷�����
			_xdb_verify_unsafe_();
			return localid;
		}

		@Override
		public String getPeerip() { // ���ַ����Զ�˷�������ַ
			_xdb_verify_unsafe_();
			return peerip;
		}

		@Override
		public com.locojoy.base.Octets getPeeripOctets() { // ���ַ����Զ�˷�������ַ
			_xdb_verify_unsafe_();
			return NameState.this.getPeeripOctets();
		}

		@Override
		public long getTime() { // ���ַ����ʱ��
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public void setState(int _v_) { // ���ַ���״̬
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLocalid(int _v_) { // ���ַ����Զ�˷�����
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPeerip(String _v_) { // ���ַ����Զ�˷�������ַ
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPeeripOctets(com.locojoy.base.Octets _v_) { // ���ַ����Զ�˷�������ַ
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTime(long _v_) { // ���ַ����ʱ��
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
			return NameState.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return NameState.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return NameState.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return NameState.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return NameState.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return NameState.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return NameState.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return NameState.this.hashCode();
		}

		@Override
		public String toString() {
			return NameState.this.toString();
		}

	}

	public static final class Data implements xbean.NameState {
		private int state; // ���ַ���״̬
		private int localid; // ���ַ����Զ�˷�����
		private String peerip; // ���ַ����Զ�˷�������ַ
		private long time; // ���ַ����ʱ��

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			peerip = "";
		}

		Data(xbean.NameState _o1_) {
			if (_o1_ instanceof NameState) assign((NameState)_o1_);
			else if (_o1_ instanceof NameState.Data) assign((NameState.Data)_o1_);
			else if (_o1_ instanceof NameState.Const) assign(((NameState.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(NameState _o_) {
			state = _o_.state;
			localid = _o_.localid;
			peerip = _o_.peerip;
			time = _o_.time;
		}

		private void assign(NameState.Data _o_) {
			state = _o_.state;
			localid = _o_.localid;
			peerip = _o_.peerip;
			time = _o_.time;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(state);
			_os_.marshal(localid);
			_os_.marshal(peerip, mkdb.Const.IO_CHARSET);
			_os_.marshal(time);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			state = _os_.unmarshal_int();
			localid = _os_.unmarshal_int();
			peerip = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			time = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.NameState copy() {
			return new Data(this);
		}

		@Override
		public xbean.NameState toData() {
			return new Data(this);
		}

		public xbean.NameState toBean() {
			return new NameState(this, null, null);
		}

		@Override
		public xbean.NameState toDataIf() {
			return this;
		}

		public xbean.NameState toBeanIf() {
			return new NameState(this, null, null);
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
		public int getState() { // ���ַ���״̬
			return state;
		}

		@Override
		public int getLocalid() { // ���ַ����Զ�˷�����
			return localid;
		}

		@Override
		public String getPeerip() { // ���ַ����Զ�˷�������ַ
			return peerip;
		}

		@Override
		public com.locojoy.base.Octets getPeeripOctets() { // ���ַ����Զ�˷�������ַ
			return com.locojoy.base.Octets.wrap(getPeerip(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getTime() { // ���ַ����ʱ��
			return time;
		}

		@Override
		public void setState(int _v_) { // ���ַ���״̬
			state = _v_;
		}

		@Override
		public void setLocalid(int _v_) { // ���ַ����Զ�˷�����
			localid = _v_;
		}

		@Override
		public void setPeerip(String _v_) { // ���ַ����Զ�˷�������ַ
			if (null == _v_)
				throw new NullPointerException();
			peerip = _v_;
		}

		@Override
		public void setPeeripOctets(com.locojoy.base.Octets _v_) { // ���ַ����Զ�˷�������ַ
			this.setPeerip(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setTime(long _v_) { // ���ַ����ʱ��
			time = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof NameState.Data)) return false;
			NameState.Data _o_ = (NameState.Data) _o1_;
			if (state != _o_.state) return false;
			if (localid != _o_.localid) return false;
			if (!peerip.equals(_o_.peerip)) return false;
			if (time != _o_.time) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += state;
			_h_ += localid;
			_h_ += peerip.hashCode();
			_h_ += time;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(localid);
			_sb_.append(",");
			_sb_.append("'").append(peerip).append("'");
			_sb_.append(",");
			_sb_.append(time);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
