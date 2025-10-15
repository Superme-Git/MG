
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WheelItem extends mkdb.XBean implements xbean.WheelItem {
	private int itemtype; // 1为物品,2为经验,3为金钱
	private int itemid; // 金钱和经验的id为0
	private int num; // 数量
	private int times; // 倍数,为实际倍数的10倍
	private int bind; // 是否绑定,只对物品有效
	private int limit; // 最大上限,只对物品有效
	private int msgid; // 发公告,只对物品有效

	@Override
	public void _reset_unsafe_() {
		itemtype = 0;
		itemid = 0;
		num = 0;
		times = 0;
		bind = 0;
		limit = 0;
		msgid = 0;
	}

	WheelItem(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public WheelItem() {
		this(0, null, null);
	}

	public WheelItem(WheelItem _o_) {
		this(_o_, null, null);
	}

	WheelItem(xbean.WheelItem _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WheelItem) assign((WheelItem)_o1_);
		else if (_o1_ instanceof WheelItem.Data) assign((WheelItem.Data)_o1_);
		else if (_o1_ instanceof WheelItem.Const) assign(((WheelItem.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WheelItem _o_) {
		_o_._xdb_verify_unsafe_();
		itemtype = _o_.itemtype;
		itemid = _o_.itemid;
		num = _o_.num;
		times = _o_.times;
		bind = _o_.bind;
		limit = _o_.limit;
		msgid = _o_.msgid;
	}

	private void assign(WheelItem.Data _o_) {
		itemtype = _o_.itemtype;
		itemid = _o_.itemid;
		num = _o_.num;
		times = _o_.times;
		bind = _o_.bind;
		limit = _o_.limit;
		msgid = _o_.msgid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(itemtype);
		_os_.marshal(itemid);
		_os_.marshal(num);
		_os_.marshal(times);
		_os_.marshal(bind);
		_os_.marshal(limit);
		_os_.marshal(msgid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		itemtype = _os_.unmarshal_int();
		itemid = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		times = _os_.unmarshal_int();
		bind = _os_.unmarshal_int();
		limit = _os_.unmarshal_int();
		msgid = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.WheelItem copy() {
		_xdb_verify_unsafe_();
		return new WheelItem(this);
	}

	@Override
	public xbean.WheelItem toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WheelItem toBean() {
		_xdb_verify_unsafe_();
		return new WheelItem(this); // same as copy()
	}

	@Override
	public xbean.WheelItem toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WheelItem toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getItemtype() { // 1为物品,2为经验,3为金钱
		_xdb_verify_unsafe_();
		return itemtype;
	}

	@Override
	public int getItemid() { // 金钱和经验的id为0
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public int getNum() { // 数量
		_xdb_verify_unsafe_();
		return num;
	}

	@Override
	public int getTimes() { // 倍数,为实际倍数的10倍
		_xdb_verify_unsafe_();
		return times;
	}

	@Override
	public int getBind() { // 是否绑定,只对物品有效
		_xdb_verify_unsafe_();
		return bind;
	}

	@Override
	public int getLimit() { // 最大上限,只对物品有效
		_xdb_verify_unsafe_();
		return limit;
	}

	@Override
	public int getMsgid() { // 发公告,只对物品有效
		_xdb_verify_unsafe_();
		return msgid;
	}

	@Override
	public void setItemtype(int _v_) { // 1为物品,2为经验,3为金钱
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemtype) {
					public void rollback() { itemtype = _xdb_saved; }
				};}});
		itemtype = _v_;
	}

	@Override
	public void setItemid(int _v_) { // 金钱和经验的id为0
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setNum(int _v_) { // 数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "num") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, num) {
					public void rollback() { num = _xdb_saved; }
				};}});
		num = _v_;
	}

	@Override
	public void setTimes(int _v_) { // 倍数,为实际倍数的10倍
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "times") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, times) {
					public void rollback() { times = _xdb_saved; }
				};}});
		times = _v_;
	}

	@Override
	public void setBind(int _v_) { // 是否绑定,只对物品有效
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bind") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bind) {
					public void rollback() { bind = _xdb_saved; }
				};}});
		bind = _v_;
	}

	@Override
	public void setLimit(int _v_) { // 最大上限,只对物品有效
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "limit") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, limit) {
					public void rollback() { limit = _xdb_saved; }
				};}});
		limit = _v_;
	}

	@Override
	public void setMsgid(int _v_) { // 发公告,只对物品有效
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "msgid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, msgid) {
					public void rollback() { msgid = _xdb_saved; }
				};}});
		msgid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WheelItem _o_ = null;
		if ( _o1_ instanceof WheelItem ) _o_ = (WheelItem)_o1_;
		else if ( _o1_ instanceof WheelItem.Const ) _o_ = ((WheelItem.Const)_o1_).nThis();
		else return false;
		if (itemtype != _o_.itemtype) return false;
		if (itemid != _o_.itemid) return false;
		if (num != _o_.num) return false;
		if (times != _o_.times) return false;
		if (bind != _o_.bind) return false;
		if (limit != _o_.limit) return false;
		if (msgid != _o_.msgid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += itemtype;
		_h_ += itemid;
		_h_ += num;
		_h_ += times;
		_h_ += bind;
		_h_ += limit;
		_h_ += msgid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemtype);
		_sb_.append(",");
		_sb_.append(itemid);
		_sb_.append(",");
		_sb_.append(num);
		_sb_.append(",");
		_sb_.append(times);
		_sb_.append(",");
		_sb_.append(bind);
		_sb_.append(",");
		_sb_.append(limit);
		_sb_.append(",");
		_sb_.append(msgid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("num"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("times"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bind"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("limit"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("msgid"));
		return lb;
	}

	private class Const implements xbean.WheelItem {
		WheelItem nThis() {
			return WheelItem.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WheelItem copy() {
			return WheelItem.this.copy();
		}

		@Override
		public xbean.WheelItem toData() {
			return WheelItem.this.toData();
		}

		public xbean.WheelItem toBean() {
			return WheelItem.this.toBean();
		}

		@Override
		public xbean.WheelItem toDataIf() {
			return WheelItem.this.toDataIf();
		}

		public xbean.WheelItem toBeanIf() {
			return WheelItem.this.toBeanIf();
		}

		@Override
		public int getItemtype() { // 1为物品,2为经验,3为金钱
			_xdb_verify_unsafe_();
			return itemtype;
		}

		@Override
		public int getItemid() { // 金钱和经验的id为0
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public int getNum() { // 数量
			_xdb_verify_unsafe_();
			return num;
		}

		@Override
		public int getTimes() { // 倍数,为实际倍数的10倍
			_xdb_verify_unsafe_();
			return times;
		}

		@Override
		public int getBind() { // 是否绑定,只对物品有效
			_xdb_verify_unsafe_();
			return bind;
		}

		@Override
		public int getLimit() { // 最大上限,只对物品有效
			_xdb_verify_unsafe_();
			return limit;
		}

		@Override
		public int getMsgid() { // 发公告,只对物品有效
			_xdb_verify_unsafe_();
			return msgid;
		}

		@Override
		public void setItemtype(int _v_) { // 1为物品,2为经验,3为金钱
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemid(int _v_) { // 金钱和经验的id为0
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNum(int _v_) { // 数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimes(int _v_) { // 倍数,为实际倍数的10倍
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBind(int _v_) { // 是否绑定,只对物品有效
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLimit(int _v_) { // 最大上限,只对物品有效
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMsgid(int _v_) { // 发公告,只对物品有效
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
			return WheelItem.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WheelItem.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WheelItem.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WheelItem.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WheelItem.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WheelItem.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WheelItem.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WheelItem.this.hashCode();
		}

		@Override
		public String toString() {
			return WheelItem.this.toString();
		}

	}

	public static final class Data implements xbean.WheelItem {
		private int itemtype; // 1为物品,2为经验,3为金钱
		private int itemid; // 金钱和经验的id为0
		private int num; // 数量
		private int times; // 倍数,为实际倍数的10倍
		private int bind; // 是否绑定,只对物品有效
		private int limit; // 最大上限,只对物品有效
		private int msgid; // 发公告,只对物品有效

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.WheelItem _o1_) {
			if (_o1_ instanceof WheelItem) assign((WheelItem)_o1_);
			else if (_o1_ instanceof WheelItem.Data) assign((WheelItem.Data)_o1_);
			else if (_o1_ instanceof WheelItem.Const) assign(((WheelItem.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WheelItem _o_) {
			itemtype = _o_.itemtype;
			itemid = _o_.itemid;
			num = _o_.num;
			times = _o_.times;
			bind = _o_.bind;
			limit = _o_.limit;
			msgid = _o_.msgid;
		}

		private void assign(WheelItem.Data _o_) {
			itemtype = _o_.itemtype;
			itemid = _o_.itemid;
			num = _o_.num;
			times = _o_.times;
			bind = _o_.bind;
			limit = _o_.limit;
			msgid = _o_.msgid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(itemtype);
			_os_.marshal(itemid);
			_os_.marshal(num);
			_os_.marshal(times);
			_os_.marshal(bind);
			_os_.marshal(limit);
			_os_.marshal(msgid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			itemtype = _os_.unmarshal_int();
			itemid = _os_.unmarshal_int();
			num = _os_.unmarshal_int();
			times = _os_.unmarshal_int();
			bind = _os_.unmarshal_int();
			limit = _os_.unmarshal_int();
			msgid = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.WheelItem copy() {
			return new Data(this);
		}

		@Override
		public xbean.WheelItem toData() {
			return new Data(this);
		}

		public xbean.WheelItem toBean() {
			return new WheelItem(this, null, null);
		}

		@Override
		public xbean.WheelItem toDataIf() {
			return this;
		}

		public xbean.WheelItem toBeanIf() {
			return new WheelItem(this, null, null);
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
		public int getItemtype() { // 1为物品,2为经验,3为金钱
			return itemtype;
		}

		@Override
		public int getItemid() { // 金钱和经验的id为0
			return itemid;
		}

		@Override
		public int getNum() { // 数量
			return num;
		}

		@Override
		public int getTimes() { // 倍数,为实际倍数的10倍
			return times;
		}

		@Override
		public int getBind() { // 是否绑定,只对物品有效
			return bind;
		}

		@Override
		public int getLimit() { // 最大上限,只对物品有效
			return limit;
		}

		@Override
		public int getMsgid() { // 发公告,只对物品有效
			return msgid;
		}

		@Override
		public void setItemtype(int _v_) { // 1为物品,2为经验,3为金钱
			itemtype = _v_;
		}

		@Override
		public void setItemid(int _v_) { // 金钱和经验的id为0
			itemid = _v_;
		}

		@Override
		public void setNum(int _v_) { // 数量
			num = _v_;
		}

		@Override
		public void setTimes(int _v_) { // 倍数,为实际倍数的10倍
			times = _v_;
		}

		@Override
		public void setBind(int _v_) { // 是否绑定,只对物品有效
			bind = _v_;
		}

		@Override
		public void setLimit(int _v_) { // 最大上限,只对物品有效
			limit = _v_;
		}

		@Override
		public void setMsgid(int _v_) { // 发公告,只对物品有效
			msgid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WheelItem.Data)) return false;
			WheelItem.Data _o_ = (WheelItem.Data) _o1_;
			if (itemtype != _o_.itemtype) return false;
			if (itemid != _o_.itemid) return false;
			if (num != _o_.num) return false;
			if (times != _o_.times) return false;
			if (bind != _o_.bind) return false;
			if (limit != _o_.limit) return false;
			if (msgid != _o_.msgid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += itemtype;
			_h_ += itemid;
			_h_ += num;
			_h_ += times;
			_h_ += bind;
			_h_ += limit;
			_h_ += msgid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(itemtype);
			_sb_.append(",");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(num);
			_sb_.append(",");
			_sb_.append(times);
			_sb_.append(",");
			_sb_.append(bind);
			_sb_.append(",");
			_sb_.append(limit);
			_sb_.append(",");
			_sb_.append(msgid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
