
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SingleInvitings extends mkdb.XBean implements xbean.SingleInvitings {
	private java.util.HashMap<Long, Long> invitingids; // 

	@Override
	public void _reset_unsafe_() {
		invitingids.clear();
	}

	SingleInvitings(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		invitingids = new java.util.HashMap<Long, Long>();
	}

	public SingleInvitings() {
		this(0, null, null);
	}

	public SingleInvitings(SingleInvitings _o_) {
		this(_o_, null, null);
	}

	SingleInvitings(xbean.SingleInvitings _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SingleInvitings) assign((SingleInvitings)_o1_);
		else if (_o1_ instanceof SingleInvitings.Data) assign((SingleInvitings.Data)_o1_);
		else if (_o1_ instanceof SingleInvitings.Const) assign(((SingleInvitings.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SingleInvitings _o_) {
		_o_._xdb_verify_unsafe_();
		invitingids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
			invitingids.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(SingleInvitings.Data _o_) {
		invitingids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
			invitingids.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(invitingids.size());
		for (java.util.Map.Entry<Long, Long> _e_ : invitingids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				invitingids = new java.util.HashMap<Long, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				invitingids.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.SingleInvitings copy() {
		_xdb_verify_unsafe_();
		return new SingleInvitings(this);
	}

	@Override
	public xbean.SingleInvitings toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SingleInvitings toBean() {
		_xdb_verify_unsafe_();
		return new SingleInvitings(this); // same as copy()
	}

	@Override
	public xbean.SingleInvitings toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SingleInvitings toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Long> getInvitingids() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "invitingids"), invitingids);
	}

	@Override
	public java.util.Map<Long, Long> getInvitingidsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Long, Long> invitingids;
		SingleInvitings _o_ = this;
		invitingids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
			invitingids.put(_e_.getKey(), _e_.getValue());
		return invitingids;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SingleInvitings _o_ = null;
		if ( _o1_ instanceof SingleInvitings ) _o_ = (SingleInvitings)_o1_;
		else if ( _o1_ instanceof SingleInvitings.Const ) _o_ = ((SingleInvitings.Const)_o1_).nThis();
		else return false;
		if (!invitingids.equals(_o_.invitingids)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += invitingids.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(invitingids);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("invitingids"));
		return lb;
	}

	private class Const implements xbean.SingleInvitings {
		SingleInvitings nThis() {
			return SingleInvitings.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SingleInvitings copy() {
			return SingleInvitings.this.copy();
		}

		@Override
		public xbean.SingleInvitings toData() {
			return SingleInvitings.this.toData();
		}

		public xbean.SingleInvitings toBean() {
			return SingleInvitings.this.toBean();
		}

		@Override
		public xbean.SingleInvitings toDataIf() {
			return SingleInvitings.this.toDataIf();
		}

		public xbean.SingleInvitings toBeanIf() {
			return SingleInvitings.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Long> getInvitingids() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(invitingids);
		}

		@Override
		public java.util.Map<Long, Long> getInvitingidsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Long, Long> invitingids;
			SingleInvitings _o_ = SingleInvitings.this;
			invitingids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
				invitingids.put(_e_.getKey(), _e_.getValue());
			return invitingids;
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
			return SingleInvitings.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SingleInvitings.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SingleInvitings.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SingleInvitings.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SingleInvitings.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SingleInvitings.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SingleInvitings.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SingleInvitings.this.hashCode();
		}

		@Override
		public String toString() {
			return SingleInvitings.this.toString();
		}

	}

	public static final class Data implements xbean.SingleInvitings {
		private java.util.HashMap<Long, Long> invitingids; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			invitingids = new java.util.HashMap<Long, Long>();
		}

		Data(xbean.SingleInvitings _o1_) {
			if (_o1_ instanceof SingleInvitings) assign((SingleInvitings)_o1_);
			else if (_o1_ instanceof SingleInvitings.Data) assign((SingleInvitings.Data)_o1_);
			else if (_o1_ instanceof SingleInvitings.Const) assign(((SingleInvitings.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SingleInvitings _o_) {
			invitingids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
				invitingids.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(SingleInvitings.Data _o_) {
			invitingids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
				invitingids.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(invitingids.size());
			for (java.util.Map.Entry<Long, Long> _e_ : invitingids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					invitingids = new java.util.HashMap<Long, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					invitingids.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.SingleInvitings copy() {
			return new Data(this);
		}

		@Override
		public xbean.SingleInvitings toData() {
			return new Data(this);
		}

		public xbean.SingleInvitings toBean() {
			return new SingleInvitings(this, null, null);
		}

		@Override
		public xbean.SingleInvitings toDataIf() {
			return this;
		}

		public xbean.SingleInvitings toBeanIf() {
			return new SingleInvitings(this, null, null);
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
		public java.util.Map<Long, Long> getInvitingids() { // 
			return invitingids;
		}

		@Override
		public java.util.Map<Long, Long> getInvitingidsAsData() { // 
			return invitingids;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SingleInvitings.Data)) return false;
			SingleInvitings.Data _o_ = (SingleInvitings.Data) _o1_;
			if (!invitingids.equals(_o_.invitingids)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += invitingids.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(invitingids);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
