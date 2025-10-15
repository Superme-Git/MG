
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EChargeReturnProfit extends mkdb.XBean implements xbean.EChargeReturnProfit {
	private java.util.HashMap<Integer, xbean.ChargeReturnProfit> returnprofitmap; // 

	@Override
	public void _reset_unsafe_() {
		returnprofitmap.clear();
	}

	EChargeReturnProfit(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>();
	}

	public EChargeReturnProfit() {
		this(0, null, null);
	}

	public EChargeReturnProfit(EChargeReturnProfit _o_) {
		this(_o_, null, null);
	}

	EChargeReturnProfit(xbean.EChargeReturnProfit _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EChargeReturnProfit) assign((EChargeReturnProfit)_o1_);
		else if (_o1_ instanceof EChargeReturnProfit.Data) assign((EChargeReturnProfit.Data)_o1_);
		else if (_o1_ instanceof EChargeReturnProfit.Const) assign(((EChargeReturnProfit.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EChargeReturnProfit _o_) {
		_o_._xdb_verify_unsafe_();
		returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>();
		for (java.util.Map.Entry<Integer, xbean.ChargeReturnProfit> _e_ : _o_.returnprofitmap.entrySet())
			returnprofitmap.put(_e_.getKey(), new ChargeReturnProfit(_e_.getValue(), this, "returnprofitmap"));
	}

	private void assign(EChargeReturnProfit.Data _o_) {
		returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>();
		for (java.util.Map.Entry<Integer, xbean.ChargeReturnProfit> _e_ : _o_.returnprofitmap.entrySet())
			returnprofitmap.put(_e_.getKey(), new ChargeReturnProfit(_e_.getValue(), this, "returnprofitmap"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(returnprofitmap.size());
		for (java.util.Map.Entry<Integer, xbean.ChargeReturnProfit> _e_ : returnprofitmap.entrySet())
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
				returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.ChargeReturnProfit _v_ = new ChargeReturnProfit(0, this, "returnprofitmap");
				_v_.unmarshal(_os_);
				returnprofitmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.EChargeReturnProfit copy() {
		_xdb_verify_unsafe_();
		return new EChargeReturnProfit(this);
	}

	@Override
	public xbean.EChargeReturnProfit toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EChargeReturnProfit toBean() {
		_xdb_verify_unsafe_();
		return new EChargeReturnProfit(this); // same as copy()
	}

	@Override
	public xbean.EChargeReturnProfit toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EChargeReturnProfit toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.ChargeReturnProfit> getReturnprofitmap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "returnprofitmap"), returnprofitmap);
	}

	@Override
	public java.util.Map<Integer, xbean.ChargeReturnProfit> getReturnprofitmapAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.ChargeReturnProfit> returnprofitmap;
		EChargeReturnProfit _o_ = this;
		returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>();
		for (java.util.Map.Entry<Integer, xbean.ChargeReturnProfit> _e_ : _o_.returnprofitmap.entrySet())
			returnprofitmap.put(_e_.getKey(), new ChargeReturnProfit.Data(_e_.getValue()));
		return returnprofitmap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EChargeReturnProfit _o_ = null;
		if ( _o1_ instanceof EChargeReturnProfit ) _o_ = (EChargeReturnProfit)_o1_;
		else if ( _o1_ instanceof EChargeReturnProfit.Const ) _o_ = ((EChargeReturnProfit.Const)_o1_).nThis();
		else return false;
		if (!returnprofitmap.equals(_o_.returnprofitmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += returnprofitmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(returnprofitmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("returnprofitmap"));
		return lb;
	}

	private class Const implements xbean.EChargeReturnProfit {
		EChargeReturnProfit nThis() {
			return EChargeReturnProfit.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EChargeReturnProfit copy() {
			return EChargeReturnProfit.this.copy();
		}

		@Override
		public xbean.EChargeReturnProfit toData() {
			return EChargeReturnProfit.this.toData();
		}

		public xbean.EChargeReturnProfit toBean() {
			return EChargeReturnProfit.this.toBean();
		}

		@Override
		public xbean.EChargeReturnProfit toDataIf() {
			return EChargeReturnProfit.this.toDataIf();
		}

		public xbean.EChargeReturnProfit toBeanIf() {
			return EChargeReturnProfit.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.ChargeReturnProfit> getReturnprofitmap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(returnprofitmap);
		}

		@Override
		public java.util.Map<Integer, xbean.ChargeReturnProfit> getReturnprofitmapAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.ChargeReturnProfit> returnprofitmap;
			EChargeReturnProfit _o_ = EChargeReturnProfit.this;
			returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>();
			for (java.util.Map.Entry<Integer, xbean.ChargeReturnProfit> _e_ : _o_.returnprofitmap.entrySet())
				returnprofitmap.put(_e_.getKey(), new ChargeReturnProfit.Data(_e_.getValue()));
			return returnprofitmap;
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
			return EChargeReturnProfit.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EChargeReturnProfit.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EChargeReturnProfit.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EChargeReturnProfit.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EChargeReturnProfit.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EChargeReturnProfit.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EChargeReturnProfit.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EChargeReturnProfit.this.hashCode();
		}

		@Override
		public String toString() {
			return EChargeReturnProfit.this.toString();
		}

	}

	public static final class Data implements xbean.EChargeReturnProfit {
		private java.util.HashMap<Integer, xbean.ChargeReturnProfit> returnprofitmap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>();
		}

		Data(xbean.EChargeReturnProfit _o1_) {
			if (_o1_ instanceof EChargeReturnProfit) assign((EChargeReturnProfit)_o1_);
			else if (_o1_ instanceof EChargeReturnProfit.Data) assign((EChargeReturnProfit.Data)_o1_);
			else if (_o1_ instanceof EChargeReturnProfit.Const) assign(((EChargeReturnProfit.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EChargeReturnProfit _o_) {
			returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>();
			for (java.util.Map.Entry<Integer, xbean.ChargeReturnProfit> _e_ : _o_.returnprofitmap.entrySet())
				returnprofitmap.put(_e_.getKey(), new ChargeReturnProfit.Data(_e_.getValue()));
		}

		private void assign(EChargeReturnProfit.Data _o_) {
			returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>();
			for (java.util.Map.Entry<Integer, xbean.ChargeReturnProfit> _e_ : _o_.returnprofitmap.entrySet())
				returnprofitmap.put(_e_.getKey(), new ChargeReturnProfit.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(returnprofitmap.size());
			for (java.util.Map.Entry<Integer, xbean.ChargeReturnProfit> _e_ : returnprofitmap.entrySet())
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
					returnprofitmap = new java.util.HashMap<Integer, xbean.ChargeReturnProfit>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.ChargeReturnProfit _v_ = xbean.Pod.newChargeReturnProfitData();
					_v_.unmarshal(_os_);
					returnprofitmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.EChargeReturnProfit copy() {
			return new Data(this);
		}

		@Override
		public xbean.EChargeReturnProfit toData() {
			return new Data(this);
		}

		public xbean.EChargeReturnProfit toBean() {
			return new EChargeReturnProfit(this, null, null);
		}

		@Override
		public xbean.EChargeReturnProfit toDataIf() {
			return this;
		}

		public xbean.EChargeReturnProfit toBeanIf() {
			return new EChargeReturnProfit(this, null, null);
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
		public java.util.Map<Integer, xbean.ChargeReturnProfit> getReturnprofitmap() { // 
			return returnprofitmap;
		}

		@Override
		public java.util.Map<Integer, xbean.ChargeReturnProfit> getReturnprofitmapAsData() { // 
			return returnprofitmap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EChargeReturnProfit.Data)) return false;
			EChargeReturnProfit.Data _o_ = (EChargeReturnProfit.Data) _o1_;
			if (!returnprofitmap.equals(_o_.returnprofitmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += returnprofitmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(returnprofitmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
