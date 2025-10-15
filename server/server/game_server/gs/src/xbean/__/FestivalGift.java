
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FestivalGift extends mkdb.XBean implements xbean.FestivalGift {
	private long time; // 上次领取节日礼物的时间
	private long onlinetotal; // 距上次领取礼物累计时间

	@Override
	public void _reset_unsafe_() {
		time = 0L;
		onlinetotal = 0L;
	}

	FestivalGift(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public FestivalGift() {
		this(0, null, null);
	}

	public FestivalGift(FestivalGift _o_) {
		this(_o_, null, null);
	}

	FestivalGift(xbean.FestivalGift _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FestivalGift) assign((FestivalGift)_o1_);
		else if (_o1_ instanceof FestivalGift.Data) assign((FestivalGift.Data)_o1_);
		else if (_o1_ instanceof FestivalGift.Const) assign(((FestivalGift.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FestivalGift _o_) {
		_o_._xdb_verify_unsafe_();
		time = _o_.time;
		onlinetotal = _o_.onlinetotal;
	}

	private void assign(FestivalGift.Data _o_) {
		time = _o_.time;
		onlinetotal = _o_.onlinetotal;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(time);
		_os_.marshal(onlinetotal);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		time = _os_.unmarshal_long();
		onlinetotal = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.FestivalGift copy() {
		_xdb_verify_unsafe_();
		return new FestivalGift(this);
	}

	@Override
	public xbean.FestivalGift toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FestivalGift toBean() {
		_xdb_verify_unsafe_();
		return new FestivalGift(this); // same as copy()
	}

	@Override
	public xbean.FestivalGift toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FestivalGift toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTime() { // 上次领取节日礼物的时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public long getOnlinetotal() { // 距上次领取礼物累计时间
		_xdb_verify_unsafe_();
		return onlinetotal;
	}

	@Override
	public void setTime(long _v_) { // 上次领取节日礼物的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public void setOnlinetotal(long _v_) { // 距上次领取礼物累计时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "onlinetotal") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, onlinetotal) {
					public void rollback() { onlinetotal = _xdb_saved; }
				};}});
		onlinetotal = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FestivalGift _o_ = null;
		if ( _o1_ instanceof FestivalGift ) _o_ = (FestivalGift)_o1_;
		else if ( _o1_ instanceof FestivalGift.Const ) _o_ = ((FestivalGift.Const)_o1_).nThis();
		else return false;
		if (time != _o_.time) return false;
		if (onlinetotal != _o_.onlinetotal) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += time;
		_h_ += onlinetotal;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(onlinetotal);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("onlinetotal"));
		return lb;
	}

	private class Const implements xbean.FestivalGift {
		FestivalGift nThis() {
			return FestivalGift.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FestivalGift copy() {
			return FestivalGift.this.copy();
		}

		@Override
		public xbean.FestivalGift toData() {
			return FestivalGift.this.toData();
		}

		public xbean.FestivalGift toBean() {
			return FestivalGift.this.toBean();
		}

		@Override
		public xbean.FestivalGift toDataIf() {
			return FestivalGift.this.toDataIf();
		}

		public xbean.FestivalGift toBeanIf() {
			return FestivalGift.this.toBeanIf();
		}

		@Override
		public long getTime() { // 上次领取节日礼物的时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public long getOnlinetotal() { // 距上次领取礼物累计时间
			_xdb_verify_unsafe_();
			return onlinetotal;
		}

		@Override
		public void setTime(long _v_) { // 上次领取节日礼物的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOnlinetotal(long _v_) { // 距上次领取礼物累计时间
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
			return FestivalGift.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FestivalGift.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FestivalGift.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FestivalGift.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FestivalGift.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FestivalGift.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FestivalGift.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FestivalGift.this.hashCode();
		}

		@Override
		public String toString() {
			return FestivalGift.this.toString();
		}

	}

	public static final class Data implements xbean.FestivalGift {
		private long time; // 上次领取节日礼物的时间
		private long onlinetotal; // 距上次领取礼物累计时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.FestivalGift _o1_) {
			if (_o1_ instanceof FestivalGift) assign((FestivalGift)_o1_);
			else if (_o1_ instanceof FestivalGift.Data) assign((FestivalGift.Data)_o1_);
			else if (_o1_ instanceof FestivalGift.Const) assign(((FestivalGift.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FestivalGift _o_) {
			time = _o_.time;
			onlinetotal = _o_.onlinetotal;
		}

		private void assign(FestivalGift.Data _o_) {
			time = _o_.time;
			onlinetotal = _o_.onlinetotal;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(time);
			_os_.marshal(onlinetotal);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			time = _os_.unmarshal_long();
			onlinetotal = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.FestivalGift copy() {
			return new Data(this);
		}

		@Override
		public xbean.FestivalGift toData() {
			return new Data(this);
		}

		public xbean.FestivalGift toBean() {
			return new FestivalGift(this, null, null);
		}

		@Override
		public xbean.FestivalGift toDataIf() {
			return this;
		}

		public xbean.FestivalGift toBeanIf() {
			return new FestivalGift(this, null, null);
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
		public long getTime() { // 上次领取节日礼物的时间
			return time;
		}

		@Override
		public long getOnlinetotal() { // 距上次领取礼物累计时间
			return onlinetotal;
		}

		@Override
		public void setTime(long _v_) { // 上次领取节日礼物的时间
			time = _v_;
		}

		@Override
		public void setOnlinetotal(long _v_) { // 距上次领取礼物累计时间
			onlinetotal = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FestivalGift.Data)) return false;
			FestivalGift.Data _o_ = (FestivalGift.Data) _o1_;
			if (time != _o_.time) return false;
			if (onlinetotal != _o_.onlinetotal) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += time;
			_h_ += onlinetotal;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(onlinetotal);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
