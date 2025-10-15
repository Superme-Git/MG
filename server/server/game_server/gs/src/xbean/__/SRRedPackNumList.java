
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SRRedPackNumList extends mkdb.XBean implements xbean.SRRedPackNumList {
	private java.util.HashMap<Integer, xbean.SRRedPackNum> srredpacknumlist; // 

	@Override
	public void _reset_unsafe_() {
		srredpacknumlist.clear();
	}

	SRRedPackNumList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>();
	}

	public SRRedPackNumList() {
		this(0, null, null);
	}

	public SRRedPackNumList(SRRedPackNumList _o_) {
		this(_o_, null, null);
	}

	SRRedPackNumList(xbean.SRRedPackNumList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SRRedPackNumList) assign((SRRedPackNumList)_o1_);
		else if (_o1_ instanceof SRRedPackNumList.Data) assign((SRRedPackNumList.Data)_o1_);
		else if (_o1_ instanceof SRRedPackNumList.Const) assign(((SRRedPackNumList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SRRedPackNumList _o_) {
		_o_._xdb_verify_unsafe_();
		srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>();
		for (java.util.Map.Entry<Integer, xbean.SRRedPackNum> _e_ : _o_.srredpacknumlist.entrySet())
			srredpacknumlist.put(_e_.getKey(), new SRRedPackNum(_e_.getValue(), this, "srredpacknumlist"));
	}

	private void assign(SRRedPackNumList.Data _o_) {
		srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>();
		for (java.util.Map.Entry<Integer, xbean.SRRedPackNum> _e_ : _o_.srredpacknumlist.entrySet())
			srredpacknumlist.put(_e_.getKey(), new SRRedPackNum(_e_.getValue(), this, "srredpacknumlist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(srredpacknumlist.size());
		for (java.util.Map.Entry<Integer, xbean.SRRedPackNum> _e_ : srredpacknumlist.entrySet())
		{
			_os_.marshal(_e_.getKey());
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
				srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.SRRedPackNum _v_ = new SRRedPackNum(0, this, "srredpacknumlist");
				_v_.unmarshal(_os_);
				srredpacknumlist.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.SRRedPackNumList copy() {
		_xdb_verify_unsafe_();
		return new SRRedPackNumList(this);
	}

	@Override
	public xbean.SRRedPackNumList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SRRedPackNumList toBean() {
		_xdb_verify_unsafe_();
		return new SRRedPackNumList(this); // same as copy()
	}

	@Override
	public xbean.SRRedPackNumList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SRRedPackNumList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.SRRedPackNum> getSrredpacknumlist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "srredpacknumlist"), srredpacknumlist);
	}

	@Override
	public java.util.Map<Integer, xbean.SRRedPackNum> getSrredpacknumlistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.SRRedPackNum> srredpacknumlist;
		SRRedPackNumList _o_ = this;
		srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>();
		for (java.util.Map.Entry<Integer, xbean.SRRedPackNum> _e_ : _o_.srredpacknumlist.entrySet())
			srredpacknumlist.put(_e_.getKey(), new SRRedPackNum.Data(_e_.getValue()));
		return srredpacknumlist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SRRedPackNumList _o_ = null;
		if ( _o1_ instanceof SRRedPackNumList ) _o_ = (SRRedPackNumList)_o1_;
		else if ( _o1_ instanceof SRRedPackNumList.Const ) _o_ = ((SRRedPackNumList.Const)_o1_).nThis();
		else return false;
		if (!srredpacknumlist.equals(_o_.srredpacknumlist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += srredpacknumlist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(srredpacknumlist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("srredpacknumlist"));
		return lb;
	}

	private class Const implements xbean.SRRedPackNumList {
		SRRedPackNumList nThis() {
			return SRRedPackNumList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SRRedPackNumList copy() {
			return SRRedPackNumList.this.copy();
		}

		@Override
		public xbean.SRRedPackNumList toData() {
			return SRRedPackNumList.this.toData();
		}

		public xbean.SRRedPackNumList toBean() {
			return SRRedPackNumList.this.toBean();
		}

		@Override
		public xbean.SRRedPackNumList toDataIf() {
			return SRRedPackNumList.this.toDataIf();
		}

		public xbean.SRRedPackNumList toBeanIf() {
			return SRRedPackNumList.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.SRRedPackNum> getSrredpacknumlist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(srredpacknumlist);
		}

		@Override
		public java.util.Map<Integer, xbean.SRRedPackNum> getSrredpacknumlistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.SRRedPackNum> srredpacknumlist;
			SRRedPackNumList _o_ = SRRedPackNumList.this;
			srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>();
			for (java.util.Map.Entry<Integer, xbean.SRRedPackNum> _e_ : _o_.srredpacknumlist.entrySet())
				srredpacknumlist.put(_e_.getKey(), new SRRedPackNum.Data(_e_.getValue()));
			return srredpacknumlist;
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
			return SRRedPackNumList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SRRedPackNumList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SRRedPackNumList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SRRedPackNumList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SRRedPackNumList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SRRedPackNumList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SRRedPackNumList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SRRedPackNumList.this.hashCode();
		}

		@Override
		public String toString() {
			return SRRedPackNumList.this.toString();
		}

	}

	public static final class Data implements xbean.SRRedPackNumList {
		private java.util.HashMap<Integer, xbean.SRRedPackNum> srredpacknumlist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>();
		}

		Data(xbean.SRRedPackNumList _o1_) {
			if (_o1_ instanceof SRRedPackNumList) assign((SRRedPackNumList)_o1_);
			else if (_o1_ instanceof SRRedPackNumList.Data) assign((SRRedPackNumList.Data)_o1_);
			else if (_o1_ instanceof SRRedPackNumList.Const) assign(((SRRedPackNumList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SRRedPackNumList _o_) {
			srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>();
			for (java.util.Map.Entry<Integer, xbean.SRRedPackNum> _e_ : _o_.srredpacknumlist.entrySet())
				srredpacknumlist.put(_e_.getKey(), new SRRedPackNum.Data(_e_.getValue()));
		}

		private void assign(SRRedPackNumList.Data _o_) {
			srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>();
			for (java.util.Map.Entry<Integer, xbean.SRRedPackNum> _e_ : _o_.srredpacknumlist.entrySet())
				srredpacknumlist.put(_e_.getKey(), new SRRedPackNum.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(srredpacknumlist.size());
			for (java.util.Map.Entry<Integer, xbean.SRRedPackNum> _e_ : srredpacknumlist.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					srredpacknumlist = new java.util.HashMap<Integer, xbean.SRRedPackNum>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.SRRedPackNum _v_ = xbean.Pod.newSRRedPackNumData();
					_v_.unmarshal(_os_);
					srredpacknumlist.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.SRRedPackNumList copy() {
			return new Data(this);
		}

		@Override
		public xbean.SRRedPackNumList toData() {
			return new Data(this);
		}

		public xbean.SRRedPackNumList toBean() {
			return new SRRedPackNumList(this, null, null);
		}

		@Override
		public xbean.SRRedPackNumList toDataIf() {
			return this;
		}

		public xbean.SRRedPackNumList toBeanIf() {
			return new SRRedPackNumList(this, null, null);
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
		public java.util.Map<Integer, xbean.SRRedPackNum> getSrredpacknumlist() { // 
			return srredpacknumlist;
		}

		@Override
		public java.util.Map<Integer, xbean.SRRedPackNum> getSrredpacknumlistAsData() { // 
			return srredpacknumlist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SRRedPackNumList.Data)) return false;
			SRRedPackNumList.Data _o_ = (SRRedPackNumList.Data) _o1_;
			if (!srredpacknumlist.equals(_o_.srredpacknumlist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += srredpacknumlist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(srredpacknumlist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
