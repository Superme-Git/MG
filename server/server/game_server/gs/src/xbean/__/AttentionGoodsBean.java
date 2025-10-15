
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AttentionGoodsBean extends mkdb.XBean implements xbean.AttentionGoodsBean {
	private String id; // 物品类型+数据库id
	private long showtime; // 公示时间
	private long expiretime; // 物品过期时间

	@Override
	public void _reset_unsafe_() {
		id = "";
		showtime = 0L;
		expiretime = 0L;
	}

	AttentionGoodsBean(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		id = "";
	}

	public AttentionGoodsBean() {
		this(0, null, null);
	}

	public AttentionGoodsBean(AttentionGoodsBean _o_) {
		this(_o_, null, null);
	}

	AttentionGoodsBean(xbean.AttentionGoodsBean _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AttentionGoodsBean) assign((AttentionGoodsBean)_o1_);
		else if (_o1_ instanceof AttentionGoodsBean.Data) assign((AttentionGoodsBean.Data)_o1_);
		else if (_o1_ instanceof AttentionGoodsBean.Const) assign(((AttentionGoodsBean.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AttentionGoodsBean _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		showtime = _o_.showtime;
		expiretime = _o_.expiretime;
	}

	private void assign(AttentionGoodsBean.Data _o_) {
		id = _o_.id;
		showtime = _o_.showtime;
		expiretime = _o_.expiretime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id, mkdb.Const.IO_CHARSET);
		_os_.marshal(showtime);
		_os_.marshal(expiretime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		showtime = _os_.unmarshal_long();
		expiretime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.AttentionGoodsBean copy() {
		_xdb_verify_unsafe_();
		return new AttentionGoodsBean(this);
	}

	@Override
	public xbean.AttentionGoodsBean toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AttentionGoodsBean toBean() {
		_xdb_verify_unsafe_();
		return new AttentionGoodsBean(this); // same as copy()
	}

	@Override
	public xbean.AttentionGoodsBean toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AttentionGoodsBean toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getId() { // 物品类型+数据库id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public com.locojoy.base.Octets getIdOctets() { // 物品类型+数据库id
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getId(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getShowtime() { // 公示时间
		_xdb_verify_unsafe_();
		return showtime;
	}

	@Override
	public long getExpiretime() { // 物品过期时间
		_xdb_verify_unsafe_();
		return expiretime;
	}

	@Override
	public void setId(String _v_) { // 物品类型+数据库id
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setIdOctets(com.locojoy.base.Octets _v_) { // 物品类型+数据库id
		_xdb_verify_unsafe_();
		this.setId(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setShowtime(long _v_) { // 公示时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "showtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, showtime) {
					public void rollback() { showtime = _xdb_saved; }
				};}});
		showtime = _v_;
	}

	@Override
	public void setExpiretime(long _v_) { // 物品过期时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "expiretime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, expiretime) {
					public void rollback() { expiretime = _xdb_saved; }
				};}});
		expiretime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AttentionGoodsBean _o_ = null;
		if ( _o1_ instanceof AttentionGoodsBean ) _o_ = (AttentionGoodsBean)_o1_;
		else if ( _o1_ instanceof AttentionGoodsBean.Const ) _o_ = ((AttentionGoodsBean.Const)_o1_).nThis();
		else return false;
		if (!id.equals(_o_.id)) return false;
		if (showtime != _o_.showtime) return false;
		if (expiretime != _o_.expiretime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id.hashCode();
		_h_ += showtime;
		_h_ += expiretime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(id).append("'");
		_sb_.append(",");
		_sb_.append(showtime);
		_sb_.append(",");
		_sb_.append(expiretime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("showtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("expiretime"));
		return lb;
	}

	private class Const implements xbean.AttentionGoodsBean {
		AttentionGoodsBean nThis() {
			return AttentionGoodsBean.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AttentionGoodsBean copy() {
			return AttentionGoodsBean.this.copy();
		}

		@Override
		public xbean.AttentionGoodsBean toData() {
			return AttentionGoodsBean.this.toData();
		}

		public xbean.AttentionGoodsBean toBean() {
			return AttentionGoodsBean.this.toBean();
		}

		@Override
		public xbean.AttentionGoodsBean toDataIf() {
			return AttentionGoodsBean.this.toDataIf();
		}

		public xbean.AttentionGoodsBean toBeanIf() {
			return AttentionGoodsBean.this.toBeanIf();
		}

		@Override
		public String getId() { // 物品类型+数据库id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public com.locojoy.base.Octets getIdOctets() { // 物品类型+数据库id
			_xdb_verify_unsafe_();
			return AttentionGoodsBean.this.getIdOctets();
		}

		@Override
		public long getShowtime() { // 公示时间
			_xdb_verify_unsafe_();
			return showtime;
		}

		@Override
		public long getExpiretime() { // 物品过期时间
			_xdb_verify_unsafe_();
			return expiretime;
		}

		@Override
		public void setId(String _v_) { // 物品类型+数据库id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIdOctets(com.locojoy.base.Octets _v_) { // 物品类型+数据库id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShowtime(long _v_) { // 公示时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExpiretime(long _v_) { // 物品过期时间
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
			return AttentionGoodsBean.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AttentionGoodsBean.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AttentionGoodsBean.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AttentionGoodsBean.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AttentionGoodsBean.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AttentionGoodsBean.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AttentionGoodsBean.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AttentionGoodsBean.this.hashCode();
		}

		@Override
		public String toString() {
			return AttentionGoodsBean.this.toString();
		}

	}

	public static final class Data implements xbean.AttentionGoodsBean {
		private String id; // 物品类型+数据库id
		private long showtime; // 公示时间
		private long expiretime; // 物品过期时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			id = "";
		}

		Data(xbean.AttentionGoodsBean _o1_) {
			if (_o1_ instanceof AttentionGoodsBean) assign((AttentionGoodsBean)_o1_);
			else if (_o1_ instanceof AttentionGoodsBean.Data) assign((AttentionGoodsBean.Data)_o1_);
			else if (_o1_ instanceof AttentionGoodsBean.Const) assign(((AttentionGoodsBean.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AttentionGoodsBean _o_) {
			id = _o_.id;
			showtime = _o_.showtime;
			expiretime = _o_.expiretime;
		}

		private void assign(AttentionGoodsBean.Data _o_) {
			id = _o_.id;
			showtime = _o_.showtime;
			expiretime = _o_.expiretime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id, mkdb.Const.IO_CHARSET);
			_os_.marshal(showtime);
			_os_.marshal(expiretime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			showtime = _os_.unmarshal_long();
			expiretime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.AttentionGoodsBean copy() {
			return new Data(this);
		}

		@Override
		public xbean.AttentionGoodsBean toData() {
			return new Data(this);
		}

		public xbean.AttentionGoodsBean toBean() {
			return new AttentionGoodsBean(this, null, null);
		}

		@Override
		public xbean.AttentionGoodsBean toDataIf() {
			return this;
		}

		public xbean.AttentionGoodsBean toBeanIf() {
			return new AttentionGoodsBean(this, null, null);
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
		public String getId() { // 物品类型+数据库id
			return id;
		}

		@Override
		public com.locojoy.base.Octets getIdOctets() { // 物品类型+数据库id
			return com.locojoy.base.Octets.wrap(getId(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getShowtime() { // 公示时间
			return showtime;
		}

		@Override
		public long getExpiretime() { // 物品过期时间
			return expiretime;
		}

		@Override
		public void setId(String _v_) { // 物品类型+数据库id
			if (null == _v_)
				throw new NullPointerException();
			id = _v_;
		}

		@Override
		public void setIdOctets(com.locojoy.base.Octets _v_) { // 物品类型+数据库id
			this.setId(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setShowtime(long _v_) { // 公示时间
			showtime = _v_;
		}

		@Override
		public void setExpiretime(long _v_) { // 物品过期时间
			expiretime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AttentionGoodsBean.Data)) return false;
			AttentionGoodsBean.Data _o_ = (AttentionGoodsBean.Data) _o1_;
			if (!id.equals(_o_.id)) return false;
			if (showtime != _o_.showtime) return false;
			if (expiretime != _o_.expiretime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id.hashCode();
			_h_ += showtime;
			_h_ += expiretime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(id).append("'");
			_sb_.append(",");
			_sb_.append(showtime);
			_sb_.append(",");
			_sb_.append(expiretime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
