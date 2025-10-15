
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class NewPlayActiveWeek extends mkdb.XBean implements xbean.NewPlayActiveWeek {
	private long time; // 活动记录时间
	private java.util.HashMap<Integer, xbean.PlayActiveData> actives; // key为活动id,一周的计次数据

	@Override
	public void _reset_unsafe_() {
		time = 0L;
		actives.clear();
	}

	NewPlayActiveWeek(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
	}

	public NewPlayActiveWeek() {
		this(0, null, null);
	}

	public NewPlayActiveWeek(NewPlayActiveWeek _o_) {
		this(_o_, null, null);
	}

	NewPlayActiveWeek(xbean.NewPlayActiveWeek _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof NewPlayActiveWeek) assign((NewPlayActiveWeek)_o1_);
		else if (_o1_ instanceof NewPlayActiveWeek.Data) assign((NewPlayActiveWeek.Data)_o1_);
		else if (_o1_ instanceof NewPlayActiveWeek.Const) assign(((NewPlayActiveWeek.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(NewPlayActiveWeek _o_) {
		_o_._xdb_verify_unsafe_();
		time = _o_.time;
		actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
		for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
			actives.put(_e_.getKey(), new PlayActiveData(_e_.getValue(), this, "actives"));
	}

	private void assign(NewPlayActiveWeek.Data _o_) {
		time = _o_.time;
		actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
		for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
			actives.put(_e_.getKey(), new PlayActiveData(_e_.getValue(), this, "actives"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(time);
		_os_.compact_uint32(actives.size());
		for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : actives.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		time = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				actives = new java.util.HashMap<Integer, xbean.PlayActiveData>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.PlayActiveData _v_ = new PlayActiveData(0, this, "actives");
				_v_.unmarshal(_os_);
				actives.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.NewPlayActiveWeek copy() {
		_xdb_verify_unsafe_();
		return new NewPlayActiveWeek(this);
	}

	@Override
	public xbean.NewPlayActiveWeek toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NewPlayActiveWeek toBean() {
		_xdb_verify_unsafe_();
		return new NewPlayActiveWeek(this); // same as copy()
	}

	@Override
	public xbean.NewPlayActiveWeek toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NewPlayActiveWeek toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTime() { // 活动记录时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public java.util.Map<Integer, xbean.PlayActiveData> getActives() { // key为活动id,一周的计次数据
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "actives"), actives);
	}

	@Override
	public java.util.Map<Integer, xbean.PlayActiveData> getActivesAsData() { // key为活动id,一周的计次数据
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.PlayActiveData> actives;
		NewPlayActiveWeek _o_ = this;
		actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
		for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
			actives.put(_e_.getKey(), new PlayActiveData.Data(_e_.getValue()));
		return actives;
	}

	@Override
	public void setTime(long _v_) { // 活动记录时间
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
		NewPlayActiveWeek _o_ = null;
		if ( _o1_ instanceof NewPlayActiveWeek ) _o_ = (NewPlayActiveWeek)_o1_;
		else if ( _o1_ instanceof NewPlayActiveWeek.Const ) _o_ = ((NewPlayActiveWeek.Const)_o1_).nThis();
		else return false;
		if (time != _o_.time) return false;
		if (!actives.equals(_o_.actives)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += time;
		_h_ += actives.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(actives);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("actives"));
		return lb;
	}

	private class Const implements xbean.NewPlayActiveWeek {
		NewPlayActiveWeek nThis() {
			return NewPlayActiveWeek.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.NewPlayActiveWeek copy() {
			return NewPlayActiveWeek.this.copy();
		}

		@Override
		public xbean.NewPlayActiveWeek toData() {
			return NewPlayActiveWeek.this.toData();
		}

		public xbean.NewPlayActiveWeek toBean() {
			return NewPlayActiveWeek.this.toBean();
		}

		@Override
		public xbean.NewPlayActiveWeek toDataIf() {
			return NewPlayActiveWeek.this.toDataIf();
		}

		public xbean.NewPlayActiveWeek toBeanIf() {
			return NewPlayActiveWeek.this.toBeanIf();
		}

		@Override
		public long getTime() { // 活动记录时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public java.util.Map<Integer, xbean.PlayActiveData> getActives() { // key为活动id,一周的计次数据
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(actives);
		}

		@Override
		public java.util.Map<Integer, xbean.PlayActiveData> getActivesAsData() { // key为活动id,一周的计次数据
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.PlayActiveData> actives;
			NewPlayActiveWeek _o_ = NewPlayActiveWeek.this;
			actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
			for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
				actives.put(_e_.getKey(), new PlayActiveData.Data(_e_.getValue()));
			return actives;
		}

		@Override
		public void setTime(long _v_) { // 活动记录时间
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
			return NewPlayActiveWeek.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return NewPlayActiveWeek.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return NewPlayActiveWeek.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return NewPlayActiveWeek.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return NewPlayActiveWeek.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return NewPlayActiveWeek.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return NewPlayActiveWeek.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return NewPlayActiveWeek.this.hashCode();
		}

		@Override
		public String toString() {
			return NewPlayActiveWeek.this.toString();
		}

	}

	public static final class Data implements xbean.NewPlayActiveWeek {
		private long time; // 活动记录时间
		private java.util.HashMap<Integer, xbean.PlayActiveData> actives; // key为活动id,一周的计次数据

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
		}

		Data(xbean.NewPlayActiveWeek _o1_) {
			if (_o1_ instanceof NewPlayActiveWeek) assign((NewPlayActiveWeek)_o1_);
			else if (_o1_ instanceof NewPlayActiveWeek.Data) assign((NewPlayActiveWeek.Data)_o1_);
			else if (_o1_ instanceof NewPlayActiveWeek.Const) assign(((NewPlayActiveWeek.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(NewPlayActiveWeek _o_) {
			time = _o_.time;
			actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
			for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
				actives.put(_e_.getKey(), new PlayActiveData.Data(_e_.getValue()));
		}

		private void assign(NewPlayActiveWeek.Data _o_) {
			time = _o_.time;
			actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
			for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
				actives.put(_e_.getKey(), new PlayActiveData.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(time);
			_os_.compact_uint32(actives.size());
			for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : actives.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			time = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					actives = new java.util.HashMap<Integer, xbean.PlayActiveData>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.PlayActiveData _v_ = xbean.Pod.newPlayActiveDataData();
					_v_.unmarshal(_os_);
					actives.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.NewPlayActiveWeek copy() {
			return new Data(this);
		}

		@Override
		public xbean.NewPlayActiveWeek toData() {
			return new Data(this);
		}

		public xbean.NewPlayActiveWeek toBean() {
			return new NewPlayActiveWeek(this, null, null);
		}

		@Override
		public xbean.NewPlayActiveWeek toDataIf() {
			return this;
		}

		public xbean.NewPlayActiveWeek toBeanIf() {
			return new NewPlayActiveWeek(this, null, null);
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
		public long getTime() { // 活动记录时间
			return time;
		}

		@Override
		public java.util.Map<Integer, xbean.PlayActiveData> getActives() { // key为活动id,一周的计次数据
			return actives;
		}

		@Override
		public java.util.Map<Integer, xbean.PlayActiveData> getActivesAsData() { // key为活动id,一周的计次数据
			return actives;
		}

		@Override
		public void setTime(long _v_) { // 活动记录时间
			time = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof NewPlayActiveWeek.Data)) return false;
			NewPlayActiveWeek.Data _o_ = (NewPlayActiveWeek.Data) _o1_;
			if (time != _o_.time) return false;
			if (!actives.equals(_o_.actives)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += time;
			_h_ += actives.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(actives);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
