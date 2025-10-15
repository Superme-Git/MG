
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RequestRuneInfo extends mkdb.XBean implements xbean.RequestRuneInfo {
	private long roleid; // 请求角色roleid
	private long targetroleid; // 目标roleid
	private int actiontype; // 动作类型  0 请求符文    1捐献符文
	private long requesttime; // 请求时间
	private int itemid; // 物品id
	private int itemlevel; // 物品等级

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		targetroleid = 0L;
		actiontype = 0;
		requesttime = 0L;
		itemid = 0;
		itemlevel = 0;
	}

	RequestRuneInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RequestRuneInfo() {
		this(0, null, null);
	}

	public RequestRuneInfo(RequestRuneInfo _o_) {
		this(_o_, null, null);
	}

	RequestRuneInfo(xbean.RequestRuneInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RequestRuneInfo) assign((RequestRuneInfo)_o1_);
		else if (_o1_ instanceof RequestRuneInfo.Data) assign((RequestRuneInfo.Data)_o1_);
		else if (_o1_ instanceof RequestRuneInfo.Const) assign(((RequestRuneInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RequestRuneInfo _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		targetroleid = _o_.targetroleid;
		actiontype = _o_.actiontype;
		requesttime = _o_.requesttime;
		itemid = _o_.itemid;
		itemlevel = _o_.itemlevel;
	}

	private void assign(RequestRuneInfo.Data _o_) {
		roleid = _o_.roleid;
		targetroleid = _o_.targetroleid;
		actiontype = _o_.actiontype;
		requesttime = _o_.requesttime;
		itemid = _o_.itemid;
		itemlevel = _o_.itemlevel;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(targetroleid);
		_os_.marshal(actiontype);
		_os_.marshal(requesttime);
		_os_.marshal(itemid);
		_os_.marshal(itemlevel);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		targetroleid = _os_.unmarshal_long();
		actiontype = _os_.unmarshal_int();
		requesttime = _os_.unmarshal_long();
		itemid = _os_.unmarshal_int();
		itemlevel = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RequestRuneInfo copy() {
		_xdb_verify_unsafe_();
		return new RequestRuneInfo(this);
	}

	@Override
	public xbean.RequestRuneInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RequestRuneInfo toBean() {
		_xdb_verify_unsafe_();
		return new RequestRuneInfo(this); // same as copy()
	}

	@Override
	public xbean.RequestRuneInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RequestRuneInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 请求角色roleid
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public long getTargetroleid() { // 目标roleid
		_xdb_verify_unsafe_();
		return targetroleid;
	}

	@Override
	public int getActiontype() { // 动作类型  0 请求符文    1捐献符文
		_xdb_verify_unsafe_();
		return actiontype;
	}

	@Override
	public long getRequesttime() { // 请求时间
		_xdb_verify_unsafe_();
		return requesttime;
	}

	@Override
	public int getItemid() { // 物品id
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public int getItemlevel() { // 物品等级
		_xdb_verify_unsafe_();
		return itemlevel;
	}

	@Override
	public void setRoleid(long _v_) { // 请求角色roleid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setTargetroleid(long _v_) { // 目标roleid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "targetroleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, targetroleid) {
					public void rollback() { targetroleid = _xdb_saved; }
				};}});
		targetroleid = _v_;
	}

	@Override
	public void setActiontype(int _v_) { // 动作类型  0 请求符文    1捐献符文
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "actiontype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, actiontype) {
					public void rollback() { actiontype = _xdb_saved; }
				};}});
		actiontype = _v_;
	}

	@Override
	public void setRequesttime(long _v_) { // 请求时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "requesttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, requesttime) {
					public void rollback() { requesttime = _xdb_saved; }
				};}});
		requesttime = _v_;
	}

	@Override
	public void setItemid(int _v_) { // 物品id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setItemlevel(int _v_) { // 物品等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemlevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemlevel) {
					public void rollback() { itemlevel = _xdb_saved; }
				};}});
		itemlevel = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RequestRuneInfo _o_ = null;
		if ( _o1_ instanceof RequestRuneInfo ) _o_ = (RequestRuneInfo)_o1_;
		else if ( _o1_ instanceof RequestRuneInfo.Const ) _o_ = ((RequestRuneInfo.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (targetroleid != _o_.targetroleid) return false;
		if (actiontype != _o_.actiontype) return false;
		if (requesttime != _o_.requesttime) return false;
		if (itemid != _o_.itemid) return false;
		if (itemlevel != _o_.itemlevel) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += targetroleid;
		_h_ += actiontype;
		_h_ += requesttime;
		_h_ += itemid;
		_h_ += itemlevel;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(targetroleid);
		_sb_.append(",");
		_sb_.append(actiontype);
		_sb_.append(",");
		_sb_.append(requesttime);
		_sb_.append(",");
		_sb_.append(itemid);
		_sb_.append(",");
		_sb_.append(itemlevel);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("targetroleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("actiontype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("requesttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemlevel"));
		return lb;
	}

	private class Const implements xbean.RequestRuneInfo {
		RequestRuneInfo nThis() {
			return RequestRuneInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RequestRuneInfo copy() {
			return RequestRuneInfo.this.copy();
		}

		@Override
		public xbean.RequestRuneInfo toData() {
			return RequestRuneInfo.this.toData();
		}

		public xbean.RequestRuneInfo toBean() {
			return RequestRuneInfo.this.toBean();
		}

		@Override
		public xbean.RequestRuneInfo toDataIf() {
			return RequestRuneInfo.this.toDataIf();
		}

		public xbean.RequestRuneInfo toBeanIf() {
			return RequestRuneInfo.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 请求角色roleid
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public long getTargetroleid() { // 目标roleid
			_xdb_verify_unsafe_();
			return targetroleid;
		}

		@Override
		public int getActiontype() { // 动作类型  0 请求符文    1捐献符文
			_xdb_verify_unsafe_();
			return actiontype;
		}

		@Override
		public long getRequesttime() { // 请求时间
			_xdb_verify_unsafe_();
			return requesttime;
		}

		@Override
		public int getItemid() { // 物品id
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public int getItemlevel() { // 物品等级
			_xdb_verify_unsafe_();
			return itemlevel;
		}

		@Override
		public void setRoleid(long _v_) { // 请求角色roleid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTargetroleid(long _v_) { // 目标roleid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setActiontype(int _v_) { // 动作类型  0 请求符文    1捐献符文
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRequesttime(long _v_) { // 请求时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemid(int _v_) { // 物品id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemlevel(int _v_) { // 物品等级
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
			return RequestRuneInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RequestRuneInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RequestRuneInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RequestRuneInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RequestRuneInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RequestRuneInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RequestRuneInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RequestRuneInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RequestRuneInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RequestRuneInfo {
		private long roleid; // 请求角色roleid
		private long targetroleid; // 目标roleid
		private int actiontype; // 动作类型  0 请求符文    1捐献符文
		private long requesttime; // 请求时间
		private int itemid; // 物品id
		private int itemlevel; // 物品等级

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RequestRuneInfo _o1_) {
			if (_o1_ instanceof RequestRuneInfo) assign((RequestRuneInfo)_o1_);
			else if (_o1_ instanceof RequestRuneInfo.Data) assign((RequestRuneInfo.Data)_o1_);
			else if (_o1_ instanceof RequestRuneInfo.Const) assign(((RequestRuneInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RequestRuneInfo _o_) {
			roleid = _o_.roleid;
			targetroleid = _o_.targetroleid;
			actiontype = _o_.actiontype;
			requesttime = _o_.requesttime;
			itemid = _o_.itemid;
			itemlevel = _o_.itemlevel;
		}

		private void assign(RequestRuneInfo.Data _o_) {
			roleid = _o_.roleid;
			targetroleid = _o_.targetroleid;
			actiontype = _o_.actiontype;
			requesttime = _o_.requesttime;
			itemid = _o_.itemid;
			itemlevel = _o_.itemlevel;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(targetroleid);
			_os_.marshal(actiontype);
			_os_.marshal(requesttime);
			_os_.marshal(itemid);
			_os_.marshal(itemlevel);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			targetroleid = _os_.unmarshal_long();
			actiontype = _os_.unmarshal_int();
			requesttime = _os_.unmarshal_long();
			itemid = _os_.unmarshal_int();
			itemlevel = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RequestRuneInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RequestRuneInfo toData() {
			return new Data(this);
		}

		public xbean.RequestRuneInfo toBean() {
			return new RequestRuneInfo(this, null, null);
		}

		@Override
		public xbean.RequestRuneInfo toDataIf() {
			return this;
		}

		public xbean.RequestRuneInfo toBeanIf() {
			return new RequestRuneInfo(this, null, null);
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
		public long getRoleid() { // 请求角色roleid
			return roleid;
		}

		@Override
		public long getTargetroleid() { // 目标roleid
			return targetroleid;
		}

		@Override
		public int getActiontype() { // 动作类型  0 请求符文    1捐献符文
			return actiontype;
		}

		@Override
		public long getRequesttime() { // 请求时间
			return requesttime;
		}

		@Override
		public int getItemid() { // 物品id
			return itemid;
		}

		@Override
		public int getItemlevel() { // 物品等级
			return itemlevel;
		}

		@Override
		public void setRoleid(long _v_) { // 请求角色roleid
			roleid = _v_;
		}

		@Override
		public void setTargetroleid(long _v_) { // 目标roleid
			targetroleid = _v_;
		}

		@Override
		public void setActiontype(int _v_) { // 动作类型  0 请求符文    1捐献符文
			actiontype = _v_;
		}

		@Override
		public void setRequesttime(long _v_) { // 请求时间
			requesttime = _v_;
		}

		@Override
		public void setItemid(int _v_) { // 物品id
			itemid = _v_;
		}

		@Override
		public void setItemlevel(int _v_) { // 物品等级
			itemlevel = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RequestRuneInfo.Data)) return false;
			RequestRuneInfo.Data _o_ = (RequestRuneInfo.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (targetroleid != _o_.targetroleid) return false;
			if (actiontype != _o_.actiontype) return false;
			if (requesttime != _o_.requesttime) return false;
			if (itemid != _o_.itemid) return false;
			if (itemlevel != _o_.itemlevel) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += targetroleid;
			_h_ += actiontype;
			_h_ += requesttime;
			_h_ += itemid;
			_h_ += itemlevel;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(targetroleid);
			_sb_.append(",");
			_sb_.append(actiontype);
			_sb_.append(",");
			_sb_.append(requesttime);
			_sb_.append(",");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(itemlevel);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
