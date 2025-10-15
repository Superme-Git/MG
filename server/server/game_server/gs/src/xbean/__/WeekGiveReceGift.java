
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WeekGiveReceGift extends mkdb.XBean implements xbean.WeekGiveReceGift {
	private java.util.HashMap<Long, xbean.GiveReceGift> weekdata; // 

	@Override
	public void _reset_unsafe_() {
		weekdata.clear();
	}

	WeekGiveReceGift(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>();
	}

	public WeekGiveReceGift() {
		this(0, null, null);
	}

	public WeekGiveReceGift(WeekGiveReceGift _o_) {
		this(_o_, null, null);
	}

	WeekGiveReceGift(xbean.WeekGiveReceGift _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WeekGiveReceGift) assign((WeekGiveReceGift)_o1_);
		else if (_o1_ instanceof WeekGiveReceGift.Data) assign((WeekGiveReceGift.Data)_o1_);
		else if (_o1_ instanceof WeekGiveReceGift.Const) assign(((WeekGiveReceGift.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WeekGiveReceGift _o_) {
		_o_._xdb_verify_unsafe_();
		weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>();
		for (java.util.Map.Entry<Long, xbean.GiveReceGift> _e_ : _o_.weekdata.entrySet())
			weekdata.put(_e_.getKey(), new GiveReceGift(_e_.getValue(), this, "weekdata"));
	}

	private void assign(WeekGiveReceGift.Data _o_) {
		weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>();
		for (java.util.Map.Entry<Long, xbean.GiveReceGift> _e_ : _o_.weekdata.entrySet())
			weekdata.put(_e_.getKey(), new GiveReceGift(_e_.getValue(), this, "weekdata"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(weekdata.size());
		for (java.util.Map.Entry<Long, xbean.GiveReceGift> _e_ : weekdata.entrySet())
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
				weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.GiveReceGift _v_ = new GiveReceGift(0, this, "weekdata");
				_v_.unmarshal(_os_);
				weekdata.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.WeekGiveReceGift copy() {
		_xdb_verify_unsafe_();
		return new WeekGiveReceGift(this);
	}

	@Override
	public xbean.WeekGiveReceGift toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WeekGiveReceGift toBean() {
		_xdb_verify_unsafe_();
		return new WeekGiveReceGift(this); // same as copy()
	}

	@Override
	public xbean.WeekGiveReceGift toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WeekGiveReceGift toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.GiveReceGift> getWeekdata() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "weekdata"), weekdata);
	}

	@Override
	public java.util.Map<Long, xbean.GiveReceGift> getWeekdataAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.GiveReceGift> weekdata;
		WeekGiveReceGift _o_ = this;
		weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>();
		for (java.util.Map.Entry<Long, xbean.GiveReceGift> _e_ : _o_.weekdata.entrySet())
			weekdata.put(_e_.getKey(), new GiveReceGift.Data(_e_.getValue()));
		return weekdata;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WeekGiveReceGift _o_ = null;
		if ( _o1_ instanceof WeekGiveReceGift ) _o_ = (WeekGiveReceGift)_o1_;
		else if ( _o1_ instanceof WeekGiveReceGift.Const ) _o_ = ((WeekGiveReceGift.Const)_o1_).nThis();
		else return false;
		if (!weekdata.equals(_o_.weekdata)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += weekdata.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(weekdata);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("weekdata"));
		return lb;
	}

	private class Const implements xbean.WeekGiveReceGift {
		WeekGiveReceGift nThis() {
			return WeekGiveReceGift.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WeekGiveReceGift copy() {
			return WeekGiveReceGift.this.copy();
		}

		@Override
		public xbean.WeekGiveReceGift toData() {
			return WeekGiveReceGift.this.toData();
		}

		public xbean.WeekGiveReceGift toBean() {
			return WeekGiveReceGift.this.toBean();
		}

		@Override
		public xbean.WeekGiveReceGift toDataIf() {
			return WeekGiveReceGift.this.toDataIf();
		}

		public xbean.WeekGiveReceGift toBeanIf() {
			return WeekGiveReceGift.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.GiveReceGift> getWeekdata() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(weekdata);
		}

		@Override
		public java.util.Map<Long, xbean.GiveReceGift> getWeekdataAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.GiveReceGift> weekdata;
			WeekGiveReceGift _o_ = WeekGiveReceGift.this;
			weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>();
			for (java.util.Map.Entry<Long, xbean.GiveReceGift> _e_ : _o_.weekdata.entrySet())
				weekdata.put(_e_.getKey(), new GiveReceGift.Data(_e_.getValue()));
			return weekdata;
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
			return WeekGiveReceGift.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WeekGiveReceGift.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WeekGiveReceGift.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WeekGiveReceGift.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WeekGiveReceGift.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WeekGiveReceGift.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WeekGiveReceGift.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WeekGiveReceGift.this.hashCode();
		}

		@Override
		public String toString() {
			return WeekGiveReceGift.this.toString();
		}

	}

	public static final class Data implements xbean.WeekGiveReceGift {
		private java.util.HashMap<Long, xbean.GiveReceGift> weekdata; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>();
		}

		Data(xbean.WeekGiveReceGift _o1_) {
			if (_o1_ instanceof WeekGiveReceGift) assign((WeekGiveReceGift)_o1_);
			else if (_o1_ instanceof WeekGiveReceGift.Data) assign((WeekGiveReceGift.Data)_o1_);
			else if (_o1_ instanceof WeekGiveReceGift.Const) assign(((WeekGiveReceGift.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WeekGiveReceGift _o_) {
			weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>();
			for (java.util.Map.Entry<Long, xbean.GiveReceGift> _e_ : _o_.weekdata.entrySet())
				weekdata.put(_e_.getKey(), new GiveReceGift.Data(_e_.getValue()));
		}

		private void assign(WeekGiveReceGift.Data _o_) {
			weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>();
			for (java.util.Map.Entry<Long, xbean.GiveReceGift> _e_ : _o_.weekdata.entrySet())
				weekdata.put(_e_.getKey(), new GiveReceGift.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(weekdata.size());
			for (java.util.Map.Entry<Long, xbean.GiveReceGift> _e_ : weekdata.entrySet())
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
					weekdata = new java.util.HashMap<Long, xbean.GiveReceGift>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.GiveReceGift _v_ = xbean.Pod.newGiveReceGiftData();
					_v_.unmarshal(_os_);
					weekdata.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.WeekGiveReceGift copy() {
			return new Data(this);
		}

		@Override
		public xbean.WeekGiveReceGift toData() {
			return new Data(this);
		}

		public xbean.WeekGiveReceGift toBean() {
			return new WeekGiveReceGift(this, null, null);
		}

		@Override
		public xbean.WeekGiveReceGift toDataIf() {
			return this;
		}

		public xbean.WeekGiveReceGift toBeanIf() {
			return new WeekGiveReceGift(this, null, null);
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
		public java.util.Map<Long, xbean.GiveReceGift> getWeekdata() { // 
			return weekdata;
		}

		@Override
		public java.util.Map<Long, xbean.GiveReceGift> getWeekdataAsData() { // 
			return weekdata;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WeekGiveReceGift.Data)) return false;
			WeekGiveReceGift.Data _o_ = (WeekGiveReceGift.Data) _o1_;
			if (!weekdata.equals(_o_.weekdata)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += weekdata.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(weekdata);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
