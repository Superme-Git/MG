
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Track extends mkdb.XBean implements xbean.Track {
	private long date; // 接受任务

	@Override
	public void _reset_unsafe_() {
		date = 0L;
	}

	Track(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public Track() {
		this(0, null, null);
	}

	public Track(Track _o_) {
		this(_o_, null, null);
	}

	Track(xbean.Track _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Track) assign((Track)_o1_);
		else if (_o1_ instanceof Track.Data) assign((Track.Data)_o1_);
		else if (_o1_ instanceof Track.Const) assign(((Track.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Track _o_) {
		_o_._xdb_verify_unsafe_();
		date = _o_.date;
	}

	private void assign(Track.Data _o_) {
		date = _o_.date;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(date);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		date = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.Track copy() {
		_xdb_verify_unsafe_();
		return new Track(this);
	}

	@Override
	public xbean.Track toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Track toBean() {
		_xdb_verify_unsafe_();
		return new Track(this); // same as copy()
	}

	@Override
	public xbean.Track toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Track toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getDate() { // 接受任务
		_xdb_verify_unsafe_();
		return date;
	}

	@Override
	public void setDate(long _v_) { // 接受任务
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "date") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, date) {
					public void rollback() { date = _xdb_saved; }
				};}});
		date = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Track _o_ = null;
		if ( _o1_ instanceof Track ) _o_ = (Track)_o1_;
		else if ( _o1_ instanceof Track.Const ) _o_ = ((Track.Const)_o1_).nThis();
		else return false;
		if (date != _o_.date) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += date;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(date);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("date"));
		return lb;
	}

	private class Const implements xbean.Track {
		Track nThis() {
			return Track.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Track copy() {
			return Track.this.copy();
		}

		@Override
		public xbean.Track toData() {
			return Track.this.toData();
		}

		public xbean.Track toBean() {
			return Track.this.toBean();
		}

		@Override
		public xbean.Track toDataIf() {
			return Track.this.toDataIf();
		}

		public xbean.Track toBeanIf() {
			return Track.this.toBeanIf();
		}

		@Override
		public long getDate() { // 接受任务
			_xdb_verify_unsafe_();
			return date;
		}

		@Override
		public void setDate(long _v_) { // 接受任务
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
			return Track.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Track.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Track.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Track.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Track.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Track.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Track.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Track.this.hashCode();
		}

		@Override
		public String toString() {
			return Track.this.toString();
		}

	}

	public static final class Data implements xbean.Track {
		private long date; // 接受任务

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.Track _o1_) {
			if (_o1_ instanceof Track) assign((Track)_o1_);
			else if (_o1_ instanceof Track.Data) assign((Track.Data)_o1_);
			else if (_o1_ instanceof Track.Const) assign(((Track.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Track _o_) {
			date = _o_.date;
		}

		private void assign(Track.Data _o_) {
			date = _o_.date;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(date);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			date = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.Track copy() {
			return new Data(this);
		}

		@Override
		public xbean.Track toData() {
			return new Data(this);
		}

		public xbean.Track toBean() {
			return new Track(this, null, null);
		}

		@Override
		public xbean.Track toDataIf() {
			return this;
		}

		public xbean.Track toBeanIf() {
			return new Track(this, null, null);
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
		public long getDate() { // 接受任务
			return date;
		}

		@Override
		public void setDate(long _v_) { // 接受任务
			date = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Track.Data)) return false;
			Track.Data _o_ = (Track.Data) _o1_;
			if (date != _o_.date) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += date;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(date);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
