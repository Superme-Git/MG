
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BeginnerTip extends mkdb.XBean implements xbean.BeginnerTip {
	private java.util.HashMap<Integer, Integer> tips; // 

	@Override
	public void _reset_unsafe_() {
		tips.clear();
	}

	BeginnerTip(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		tips = new java.util.HashMap<Integer, Integer>();
	}

	public BeginnerTip() {
		this(0, null, null);
	}

	public BeginnerTip(BeginnerTip _o_) {
		this(_o_, null, null);
	}

	BeginnerTip(xbean.BeginnerTip _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BeginnerTip) assign((BeginnerTip)_o1_);
		else if (_o1_ instanceof BeginnerTip.Data) assign((BeginnerTip.Data)_o1_);
		else if (_o1_ instanceof BeginnerTip.Const) assign(((BeginnerTip.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BeginnerTip _o_) {
		_o_._xdb_verify_unsafe_();
		tips = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tips.entrySet())
			tips.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(BeginnerTip.Data _o_) {
		tips = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tips.entrySet())
			tips.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(tips.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : tips.entrySet())
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
				tips = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				tips.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.BeginnerTip copy() {
		_xdb_verify_unsafe_();
		return new BeginnerTip(this);
	}

	@Override
	public xbean.BeginnerTip toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BeginnerTip toBean() {
		_xdb_verify_unsafe_();
		return new BeginnerTip(this); // same as copy()
	}

	@Override
	public xbean.BeginnerTip toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BeginnerTip toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getTips() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "tips"), tips);
	}

	@Override
	public java.util.Map<Integer, Integer> getTipsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> tips;
		BeginnerTip _o_ = this;
		tips = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tips.entrySet())
			tips.put(_e_.getKey(), _e_.getValue());
		return tips;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BeginnerTip _o_ = null;
		if ( _o1_ instanceof BeginnerTip ) _o_ = (BeginnerTip)_o1_;
		else if ( _o1_ instanceof BeginnerTip.Const ) _o_ = ((BeginnerTip.Const)_o1_).nThis();
		else return false;
		if (!tips.equals(_o_.tips)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += tips.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tips);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("tips"));
		return lb;
	}

	private class Const implements xbean.BeginnerTip {
		BeginnerTip nThis() {
			return BeginnerTip.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BeginnerTip copy() {
			return BeginnerTip.this.copy();
		}

		@Override
		public xbean.BeginnerTip toData() {
			return BeginnerTip.this.toData();
		}

		public xbean.BeginnerTip toBean() {
			return BeginnerTip.this.toBean();
		}

		@Override
		public xbean.BeginnerTip toDataIf() {
			return BeginnerTip.this.toDataIf();
		}

		public xbean.BeginnerTip toBeanIf() {
			return BeginnerTip.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getTips() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(tips);
		}

		@Override
		public java.util.Map<Integer, Integer> getTipsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> tips;
			BeginnerTip _o_ = BeginnerTip.this;
			tips = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tips.entrySet())
				tips.put(_e_.getKey(), _e_.getValue());
			return tips;
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
			return BeginnerTip.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BeginnerTip.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BeginnerTip.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BeginnerTip.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BeginnerTip.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BeginnerTip.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BeginnerTip.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BeginnerTip.this.hashCode();
		}

		@Override
		public String toString() {
			return BeginnerTip.this.toString();
		}

	}

	public static final class Data implements xbean.BeginnerTip {
		private java.util.HashMap<Integer, Integer> tips; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			tips = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.BeginnerTip _o1_) {
			if (_o1_ instanceof BeginnerTip) assign((BeginnerTip)_o1_);
			else if (_o1_ instanceof BeginnerTip.Data) assign((BeginnerTip.Data)_o1_);
			else if (_o1_ instanceof BeginnerTip.Const) assign(((BeginnerTip.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BeginnerTip _o_) {
			tips = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tips.entrySet())
				tips.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(BeginnerTip.Data _o_) {
			tips = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tips.entrySet())
				tips.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(tips.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : tips.entrySet())
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
					tips = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					tips.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.BeginnerTip copy() {
			return new Data(this);
		}

		@Override
		public xbean.BeginnerTip toData() {
			return new Data(this);
		}

		public xbean.BeginnerTip toBean() {
			return new BeginnerTip(this, null, null);
		}

		@Override
		public xbean.BeginnerTip toDataIf() {
			return this;
		}

		public xbean.BeginnerTip toBeanIf() {
			return new BeginnerTip(this, null, null);
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
		public java.util.Map<Integer, Integer> getTips() { // 
			return tips;
		}

		@Override
		public java.util.Map<Integer, Integer> getTipsAsData() { // 
			return tips;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BeginnerTip.Data)) return false;
			BeginnerTip.Data _o_ = (BeginnerTip.Data) _o1_;
			if (!tips.equals(_o_.tips)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += tips.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(tips);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
