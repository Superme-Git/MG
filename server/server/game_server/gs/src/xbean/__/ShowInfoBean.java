
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ShowInfoBean extends mkdb.XBean implements xbean.ShowInfoBean {
	private int showtype; // 类型
	private long roleid; // 角色id
	private long shopid; // 商店id,成就类型的时候是完成时间
	private int counterid; // 柜台id,成就类型的时候是成就进度
	private int thisid; // 唯一id,成就类型的时候是是成就id

	@Override
	public void _reset_unsafe_() {
		showtype = 0;
		roleid = 0L;
		shopid = 0L;
		counterid = 0;
		thisid = 0;
	}

	ShowInfoBean(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ShowInfoBean() {
		this(0, null, null);
	}

	public ShowInfoBean(ShowInfoBean _o_) {
		this(_o_, null, null);
	}

	ShowInfoBean(xbean.ShowInfoBean _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ShowInfoBean) assign((ShowInfoBean)_o1_);
		else if (_o1_ instanceof ShowInfoBean.Data) assign((ShowInfoBean.Data)_o1_);
		else if (_o1_ instanceof ShowInfoBean.Const) assign(((ShowInfoBean.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ShowInfoBean _o_) {
		_o_._xdb_verify_unsafe_();
		showtype = _o_.showtype;
		roleid = _o_.roleid;
		shopid = _o_.shopid;
		counterid = _o_.counterid;
		thisid = _o_.thisid;
	}

	private void assign(ShowInfoBean.Data _o_) {
		showtype = _o_.showtype;
		roleid = _o_.roleid;
		shopid = _o_.shopid;
		counterid = _o_.counterid;
		thisid = _o_.thisid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(showtype);
		_os_.marshal(roleid);
		_os_.marshal(shopid);
		_os_.marshal(counterid);
		_os_.marshal(thisid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		showtype = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		shopid = _os_.unmarshal_long();
		counterid = _os_.unmarshal_int();
		thisid = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ShowInfoBean copy() {
		_xdb_verify_unsafe_();
		return new ShowInfoBean(this);
	}

	@Override
	public xbean.ShowInfoBean toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ShowInfoBean toBean() {
		_xdb_verify_unsafe_();
		return new ShowInfoBean(this); // same as copy()
	}

	@Override
	public xbean.ShowInfoBean toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ShowInfoBean toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getShowtype() { // 类型
		_xdb_verify_unsafe_();
		return showtype;
	}

	@Override
	public long getRoleid() { // 角色id
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public long getShopid() { // 商店id,成就类型的时候是完成时间
		_xdb_verify_unsafe_();
		return shopid;
	}

	@Override
	public int getCounterid() { // 柜台id,成就类型的时候是成就进度
		_xdb_verify_unsafe_();
		return counterid;
	}

	@Override
	public int getThisid() { // 唯一id,成就类型的时候是是成就id
		_xdb_verify_unsafe_();
		return thisid;
	}

	@Override
	public void setShowtype(int _v_) { // 类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "showtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, showtype) {
					public void rollback() { showtype = _xdb_saved; }
				};}});
		showtype = _v_;
	}

	@Override
	public void setRoleid(long _v_) { // 角色id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setShopid(long _v_) { // 商店id,成就类型的时候是完成时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shopid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, shopid) {
					public void rollback() { shopid = _xdb_saved; }
				};}});
		shopid = _v_;
	}

	@Override
	public void setCounterid(int _v_) { // 柜台id,成就类型的时候是成就进度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "counterid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, counterid) {
					public void rollback() { counterid = _xdb_saved; }
				};}});
		counterid = _v_;
	}

	@Override
	public void setThisid(int _v_) { // 唯一id,成就类型的时候是是成就id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "thisid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, thisid) {
					public void rollback() { thisid = _xdb_saved; }
				};}});
		thisid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ShowInfoBean _o_ = null;
		if ( _o1_ instanceof ShowInfoBean ) _o_ = (ShowInfoBean)_o1_;
		else if ( _o1_ instanceof ShowInfoBean.Const ) _o_ = ((ShowInfoBean.Const)_o1_).nThis();
		else return false;
		if (showtype != _o_.showtype) return false;
		if (roleid != _o_.roleid) return false;
		if (shopid != _o_.shopid) return false;
		if (counterid != _o_.counterid) return false;
		if (thisid != _o_.thisid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += showtype;
		_h_ += roleid;
		_h_ += shopid;
		_h_ += counterid;
		_h_ += thisid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(showtype);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(shopid);
		_sb_.append(",");
		_sb_.append(counterid);
		_sb_.append(",");
		_sb_.append(thisid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("showtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shopid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("counterid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("thisid"));
		return lb;
	}

	private class Const implements xbean.ShowInfoBean {
		ShowInfoBean nThis() {
			return ShowInfoBean.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ShowInfoBean copy() {
			return ShowInfoBean.this.copy();
		}

		@Override
		public xbean.ShowInfoBean toData() {
			return ShowInfoBean.this.toData();
		}

		public xbean.ShowInfoBean toBean() {
			return ShowInfoBean.this.toBean();
		}

		@Override
		public xbean.ShowInfoBean toDataIf() {
			return ShowInfoBean.this.toDataIf();
		}

		public xbean.ShowInfoBean toBeanIf() {
			return ShowInfoBean.this.toBeanIf();
		}

		@Override
		public int getShowtype() { // 类型
			_xdb_verify_unsafe_();
			return showtype;
		}

		@Override
		public long getRoleid() { // 角色id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public long getShopid() { // 商店id,成就类型的时候是完成时间
			_xdb_verify_unsafe_();
			return shopid;
		}

		@Override
		public int getCounterid() { // 柜台id,成就类型的时候是成就进度
			_xdb_verify_unsafe_();
			return counterid;
		}

		@Override
		public int getThisid() { // 唯一id,成就类型的时候是是成就id
			_xdb_verify_unsafe_();
			return thisid;
		}

		@Override
		public void setShowtype(int _v_) { // 类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 角色id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShopid(long _v_) { // 商店id,成就类型的时候是完成时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCounterid(int _v_) { // 柜台id,成就类型的时候是成就进度
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setThisid(int _v_) { // 唯一id,成就类型的时候是是成就id
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
			return ShowInfoBean.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ShowInfoBean.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ShowInfoBean.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ShowInfoBean.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ShowInfoBean.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ShowInfoBean.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ShowInfoBean.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ShowInfoBean.this.hashCode();
		}

		@Override
		public String toString() {
			return ShowInfoBean.this.toString();
		}

	}

	public static final class Data implements xbean.ShowInfoBean {
		private int showtype; // 类型
		private long roleid; // 角色id
		private long shopid; // 商店id,成就类型的时候是完成时间
		private int counterid; // 柜台id,成就类型的时候是成就进度
		private int thisid; // 唯一id,成就类型的时候是是成就id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ShowInfoBean _o1_) {
			if (_o1_ instanceof ShowInfoBean) assign((ShowInfoBean)_o1_);
			else if (_o1_ instanceof ShowInfoBean.Data) assign((ShowInfoBean.Data)_o1_);
			else if (_o1_ instanceof ShowInfoBean.Const) assign(((ShowInfoBean.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ShowInfoBean _o_) {
			showtype = _o_.showtype;
			roleid = _o_.roleid;
			shopid = _o_.shopid;
			counterid = _o_.counterid;
			thisid = _o_.thisid;
		}

		private void assign(ShowInfoBean.Data _o_) {
			showtype = _o_.showtype;
			roleid = _o_.roleid;
			shopid = _o_.shopid;
			counterid = _o_.counterid;
			thisid = _o_.thisid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(showtype);
			_os_.marshal(roleid);
			_os_.marshal(shopid);
			_os_.marshal(counterid);
			_os_.marshal(thisid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			showtype = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			shopid = _os_.unmarshal_long();
			counterid = _os_.unmarshal_int();
			thisid = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ShowInfoBean copy() {
			return new Data(this);
		}

		@Override
		public xbean.ShowInfoBean toData() {
			return new Data(this);
		}

		public xbean.ShowInfoBean toBean() {
			return new ShowInfoBean(this, null, null);
		}

		@Override
		public xbean.ShowInfoBean toDataIf() {
			return this;
		}

		public xbean.ShowInfoBean toBeanIf() {
			return new ShowInfoBean(this, null, null);
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
		public int getShowtype() { // 类型
			return showtype;
		}

		@Override
		public long getRoleid() { // 角色id
			return roleid;
		}

		@Override
		public long getShopid() { // 商店id,成就类型的时候是完成时间
			return shopid;
		}

		@Override
		public int getCounterid() { // 柜台id,成就类型的时候是成就进度
			return counterid;
		}

		@Override
		public int getThisid() { // 唯一id,成就类型的时候是是成就id
			return thisid;
		}

		@Override
		public void setShowtype(int _v_) { // 类型
			showtype = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // 角色id
			roleid = _v_;
		}

		@Override
		public void setShopid(long _v_) { // 商店id,成就类型的时候是完成时间
			shopid = _v_;
		}

		@Override
		public void setCounterid(int _v_) { // 柜台id,成就类型的时候是成就进度
			counterid = _v_;
		}

		@Override
		public void setThisid(int _v_) { // 唯一id,成就类型的时候是是成就id
			thisid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ShowInfoBean.Data)) return false;
			ShowInfoBean.Data _o_ = (ShowInfoBean.Data) _o1_;
			if (showtype != _o_.showtype) return false;
			if (roleid != _o_.roleid) return false;
			if (shopid != _o_.shopid) return false;
			if (counterid != _o_.counterid) return false;
			if (thisid != _o_.thisid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += showtype;
			_h_ += roleid;
			_h_ += shopid;
			_h_ += counterid;
			_h_ += thisid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(showtype);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(shopid);
			_sb_.append(",");
			_sb_.append(counterid);
			_sb_.append(",");
			_sb_.append(thisid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
