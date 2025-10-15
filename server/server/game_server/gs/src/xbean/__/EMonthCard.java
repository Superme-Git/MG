
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EMonthCard extends mkdb.XBean implements xbean.EMonthCard {
	private long endtime; // 结束时间戳 by changhao
	private long grabtime; // 领取时间戳 by changhao
	private long firstprompttime; // 少于3天提示 by changhao

	@Override
	public void _reset_unsafe_() {
		endtime = 0L;
		grabtime = 0L;
		firstprompttime = 0L;
	}

	EMonthCard(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public EMonthCard() {
		this(0, null, null);
	}

	public EMonthCard(EMonthCard _o_) {
		this(_o_, null, null);
	}

	EMonthCard(xbean.EMonthCard _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EMonthCard) assign((EMonthCard)_o1_);
		else if (_o1_ instanceof EMonthCard.Data) assign((EMonthCard.Data)_o1_);
		else if (_o1_ instanceof EMonthCard.Const) assign(((EMonthCard.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EMonthCard _o_) {
		_o_._xdb_verify_unsafe_();
		endtime = _o_.endtime;
		grabtime = _o_.grabtime;
		firstprompttime = _o_.firstprompttime;
	}

	private void assign(EMonthCard.Data _o_) {
		endtime = _o_.endtime;
		grabtime = _o_.grabtime;
		firstprompttime = _o_.firstprompttime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(endtime);
		_os_.marshal(grabtime);
		_os_.marshal(firstprompttime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		endtime = _os_.unmarshal_long();
		grabtime = _os_.unmarshal_long();
		firstprompttime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.EMonthCard copy() {
		_xdb_verify_unsafe_();
		return new EMonthCard(this);
	}

	@Override
	public xbean.EMonthCard toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EMonthCard toBean() {
		_xdb_verify_unsafe_();
		return new EMonthCard(this); // same as copy()
	}

	@Override
	public xbean.EMonthCard toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EMonthCard toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getEndtime() { // 结束时间戳 by changhao
		_xdb_verify_unsafe_();
		return endtime;
	}

	@Override
	public long getGrabtime() { // 领取时间戳 by changhao
		_xdb_verify_unsafe_();
		return grabtime;
	}

	@Override
	public long getFirstprompttime() { // 少于3天提示 by changhao
		_xdb_verify_unsafe_();
		return firstprompttime;
	}

	@Override
	public void setEndtime(long _v_) { // 结束时间戳 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "endtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, endtime) {
					public void rollback() { endtime = _xdb_saved; }
				};}});
		endtime = _v_;
	}

	@Override
	public void setGrabtime(long _v_) { // 领取时间戳 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "grabtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, grabtime) {
					public void rollback() { grabtime = _xdb_saved; }
				};}});
		grabtime = _v_;
	}

	@Override
	public void setFirstprompttime(long _v_) { // 少于3天提示 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstprompttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, firstprompttime) {
					public void rollback() { firstprompttime = _xdb_saved; }
				};}});
		firstprompttime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EMonthCard _o_ = null;
		if ( _o1_ instanceof EMonthCard ) _o_ = (EMonthCard)_o1_;
		else if ( _o1_ instanceof EMonthCard.Const ) _o_ = ((EMonthCard.Const)_o1_).nThis();
		else return false;
		if (endtime != _o_.endtime) return false;
		if (grabtime != _o_.grabtime) return false;
		if (firstprompttime != _o_.firstprompttime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += endtime;
		_h_ += grabtime;
		_h_ += firstprompttime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(endtime);
		_sb_.append(",");
		_sb_.append(grabtime);
		_sb_.append(",");
		_sb_.append(firstprompttime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("grabtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstprompttime"));
		return lb;
	}

	private class Const implements xbean.EMonthCard {
		EMonthCard nThis() {
			return EMonthCard.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EMonthCard copy() {
			return EMonthCard.this.copy();
		}

		@Override
		public xbean.EMonthCard toData() {
			return EMonthCard.this.toData();
		}

		public xbean.EMonthCard toBean() {
			return EMonthCard.this.toBean();
		}

		@Override
		public xbean.EMonthCard toDataIf() {
			return EMonthCard.this.toDataIf();
		}

		public xbean.EMonthCard toBeanIf() {
			return EMonthCard.this.toBeanIf();
		}

		@Override
		public long getEndtime() { // 结束时间戳 by changhao
			_xdb_verify_unsafe_();
			return endtime;
		}

		@Override
		public long getGrabtime() { // 领取时间戳 by changhao
			_xdb_verify_unsafe_();
			return grabtime;
		}

		@Override
		public long getFirstprompttime() { // 少于3天提示 by changhao
			_xdb_verify_unsafe_();
			return firstprompttime;
		}

		@Override
		public void setEndtime(long _v_) { // 结束时间戳 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGrabtime(long _v_) { // 领取时间戳 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstprompttime(long _v_) { // 少于3天提示 by changhao
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
			return EMonthCard.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EMonthCard.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EMonthCard.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EMonthCard.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EMonthCard.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EMonthCard.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EMonthCard.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EMonthCard.this.hashCode();
		}

		@Override
		public String toString() {
			return EMonthCard.this.toString();
		}

	}

	public static final class Data implements xbean.EMonthCard {
		private long endtime; // 结束时间戳 by changhao
		private long grabtime; // 领取时间戳 by changhao
		private long firstprompttime; // 少于3天提示 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.EMonthCard _o1_) {
			if (_o1_ instanceof EMonthCard) assign((EMonthCard)_o1_);
			else if (_o1_ instanceof EMonthCard.Data) assign((EMonthCard.Data)_o1_);
			else if (_o1_ instanceof EMonthCard.Const) assign(((EMonthCard.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EMonthCard _o_) {
			endtime = _o_.endtime;
			grabtime = _o_.grabtime;
			firstprompttime = _o_.firstprompttime;
		}

		private void assign(EMonthCard.Data _o_) {
			endtime = _o_.endtime;
			grabtime = _o_.grabtime;
			firstprompttime = _o_.firstprompttime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(endtime);
			_os_.marshal(grabtime);
			_os_.marshal(firstprompttime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			endtime = _os_.unmarshal_long();
			grabtime = _os_.unmarshal_long();
			firstprompttime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.EMonthCard copy() {
			return new Data(this);
		}

		@Override
		public xbean.EMonthCard toData() {
			return new Data(this);
		}

		public xbean.EMonthCard toBean() {
			return new EMonthCard(this, null, null);
		}

		@Override
		public xbean.EMonthCard toDataIf() {
			return this;
		}

		public xbean.EMonthCard toBeanIf() {
			return new EMonthCard(this, null, null);
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
		public long getEndtime() { // 结束时间戳 by changhao
			return endtime;
		}

		@Override
		public long getGrabtime() { // 领取时间戳 by changhao
			return grabtime;
		}

		@Override
		public long getFirstprompttime() { // 少于3天提示 by changhao
			return firstprompttime;
		}

		@Override
		public void setEndtime(long _v_) { // 结束时间戳 by changhao
			endtime = _v_;
		}

		@Override
		public void setGrabtime(long _v_) { // 领取时间戳 by changhao
			grabtime = _v_;
		}

		@Override
		public void setFirstprompttime(long _v_) { // 少于3天提示 by changhao
			firstprompttime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EMonthCard.Data)) return false;
			EMonthCard.Data _o_ = (EMonthCard.Data) _o1_;
			if (endtime != _o_.endtime) return false;
			if (grabtime != _o_.grabtime) return false;
			if (firstprompttime != _o_.firstprompttime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += endtime;
			_h_ += grabtime;
			_h_ += firstprompttime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(endtime);
			_sb_.append(",");
			_sb_.append(grabtime);
			_sb_.append(",");
			_sb_.append(firstprompttime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
