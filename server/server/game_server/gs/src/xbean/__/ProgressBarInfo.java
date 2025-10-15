
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ProgressBarInfo extends mkdb.XBean implements xbean.ProgressBarInfo {
	private int bartype; // 进度条类型
	private int itemkey; // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
	private int inprogress; // 进度条是否已经开始.1表示已经开始,0表示没有开始
	private long starttime; // 进度条的开始时间

	@Override
	public void _reset_unsafe_() {
		bartype = 0;
		itemkey = 0;
		inprogress = 0;
		starttime = 0L;
	}

	ProgressBarInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ProgressBarInfo() {
		this(0, null, null);
	}

	public ProgressBarInfo(ProgressBarInfo _o_) {
		this(_o_, null, null);
	}

	ProgressBarInfo(xbean.ProgressBarInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ProgressBarInfo) assign((ProgressBarInfo)_o1_);
		else if (_o1_ instanceof ProgressBarInfo.Data) assign((ProgressBarInfo.Data)_o1_);
		else if (_o1_ instanceof ProgressBarInfo.Const) assign(((ProgressBarInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ProgressBarInfo _o_) {
		_o_._xdb_verify_unsafe_();
		bartype = _o_.bartype;
		itemkey = _o_.itemkey;
		inprogress = _o_.inprogress;
		starttime = _o_.starttime;
	}

	private void assign(ProgressBarInfo.Data _o_) {
		bartype = _o_.bartype;
		itemkey = _o_.itemkey;
		inprogress = _o_.inprogress;
		starttime = _o_.starttime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(bartype);
		_os_.marshal(itemkey);
		_os_.marshal(inprogress);
		_os_.marshal(starttime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		bartype = _os_.unmarshal_int();
		itemkey = _os_.unmarshal_int();
		inprogress = _os_.unmarshal_int();
		starttime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ProgressBarInfo copy() {
		_xdb_verify_unsafe_();
		return new ProgressBarInfo(this);
	}

	@Override
	public xbean.ProgressBarInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProgressBarInfo toBean() {
		_xdb_verify_unsafe_();
		return new ProgressBarInfo(this); // same as copy()
	}

	@Override
	public xbean.ProgressBarInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProgressBarInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getBartype() { // 进度条类型
		_xdb_verify_unsafe_();
		return bartype;
	}

	@Override
	public int getItemkey() { // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
		_xdb_verify_unsafe_();
		return itemkey;
	}

	@Override
	public int getInprogress() { // 进度条是否已经开始.1表示已经开始,0表示没有开始
		_xdb_verify_unsafe_();
		return inprogress;
	}

	@Override
	public long getStarttime() { // 进度条的开始时间
		_xdb_verify_unsafe_();
		return starttime;
	}

	@Override
	public void setBartype(int _v_) { // 进度条类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bartype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bartype) {
					public void rollback() { bartype = _xdb_saved; }
				};}});
		bartype = _v_;
	}

	@Override
	public void setItemkey(int _v_) { // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemkey) {
					public void rollback() { itemkey = _xdb_saved; }
				};}});
		itemkey = _v_;
	}

	@Override
	public void setInprogress(int _v_) { // 进度条是否已经开始.1表示已经开始,0表示没有开始
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "inprogress") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, inprogress) {
					public void rollback() { inprogress = _xdb_saved; }
				};}});
		inprogress = _v_;
	}

	@Override
	public void setStarttime(long _v_) { // 进度条的开始时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "starttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, starttime) {
					public void rollback() { starttime = _xdb_saved; }
				};}});
		starttime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ProgressBarInfo _o_ = null;
		if ( _o1_ instanceof ProgressBarInfo ) _o_ = (ProgressBarInfo)_o1_;
		else if ( _o1_ instanceof ProgressBarInfo.Const ) _o_ = ((ProgressBarInfo.Const)_o1_).nThis();
		else return false;
		if (bartype != _o_.bartype) return false;
		if (itemkey != _o_.itemkey) return false;
		if (inprogress != _o_.inprogress) return false;
		if (starttime != _o_.starttime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += bartype;
		_h_ += itemkey;
		_h_ += inprogress;
		_h_ += starttime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(bartype);
		_sb_.append(",");
		_sb_.append(itemkey);
		_sb_.append(",");
		_sb_.append(inprogress);
		_sb_.append(",");
		_sb_.append(starttime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bartype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemkey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("inprogress"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("starttime"));
		return lb;
	}

	private class Const implements xbean.ProgressBarInfo {
		ProgressBarInfo nThis() {
			return ProgressBarInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ProgressBarInfo copy() {
			return ProgressBarInfo.this.copy();
		}

		@Override
		public xbean.ProgressBarInfo toData() {
			return ProgressBarInfo.this.toData();
		}

		public xbean.ProgressBarInfo toBean() {
			return ProgressBarInfo.this.toBean();
		}

		@Override
		public xbean.ProgressBarInfo toDataIf() {
			return ProgressBarInfo.this.toDataIf();
		}

		public xbean.ProgressBarInfo toBeanIf() {
			return ProgressBarInfo.this.toBeanIf();
		}

		@Override
		public int getBartype() { // 进度条类型
			_xdb_verify_unsafe_();
			return bartype;
		}

		@Override
		public int getItemkey() { // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
			_xdb_verify_unsafe_();
			return itemkey;
		}

		@Override
		public int getInprogress() { // 进度条是否已经开始.1表示已经开始,0表示没有开始
			_xdb_verify_unsafe_();
			return inprogress;
		}

		@Override
		public long getStarttime() { // 进度条的开始时间
			_xdb_verify_unsafe_();
			return starttime;
		}

		@Override
		public void setBartype(int _v_) { // 进度条类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemkey(int _v_) { // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInprogress(int _v_) { // 进度条是否已经开始.1表示已经开始,0表示没有开始
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStarttime(long _v_) { // 进度条的开始时间
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
			return ProgressBarInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ProgressBarInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ProgressBarInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ProgressBarInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ProgressBarInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ProgressBarInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ProgressBarInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ProgressBarInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ProgressBarInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ProgressBarInfo {
		private int bartype; // 进度条类型
		private int itemkey; // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
		private int inprogress; // 进度条是否已经开始.1表示已经开始,0表示没有开始
		private long starttime; // 进度条的开始时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ProgressBarInfo _o1_) {
			if (_o1_ instanceof ProgressBarInfo) assign((ProgressBarInfo)_o1_);
			else if (_o1_ instanceof ProgressBarInfo.Data) assign((ProgressBarInfo.Data)_o1_);
			else if (_o1_ instanceof ProgressBarInfo.Const) assign(((ProgressBarInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ProgressBarInfo _o_) {
			bartype = _o_.bartype;
			itemkey = _o_.itemkey;
			inprogress = _o_.inprogress;
			starttime = _o_.starttime;
		}

		private void assign(ProgressBarInfo.Data _o_) {
			bartype = _o_.bartype;
			itemkey = _o_.itemkey;
			inprogress = _o_.inprogress;
			starttime = _o_.starttime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(bartype);
			_os_.marshal(itemkey);
			_os_.marshal(inprogress);
			_os_.marshal(starttime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			bartype = _os_.unmarshal_int();
			itemkey = _os_.unmarshal_int();
			inprogress = _os_.unmarshal_int();
			starttime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ProgressBarInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ProgressBarInfo toData() {
			return new Data(this);
		}

		public xbean.ProgressBarInfo toBean() {
			return new ProgressBarInfo(this, null, null);
		}

		@Override
		public xbean.ProgressBarInfo toDataIf() {
			return this;
		}

		public xbean.ProgressBarInfo toBeanIf() {
			return new ProgressBarInfo(this, null, null);
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
		public int getBartype() { // 进度条类型
			return bartype;
		}

		@Override
		public int getItemkey() { // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
			return itemkey;
		}

		@Override
		public int getInprogress() { // 进度条是否已经开始.1表示已经开始,0表示没有开始
			return inprogress;
		}

		@Override
		public long getStarttime() { // 进度条的开始时间
			return starttime;
		}

		@Override
		public void setBartype(int _v_) { // 进度条类型
			bartype = _v_;
		}

		@Override
		public void setItemkey(int _v_) { // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
			itemkey = _v_;
		}

		@Override
		public void setInprogress(int _v_) { // 进度条是否已经开始.1表示已经开始,0表示没有开始
			inprogress = _v_;
		}

		@Override
		public void setStarttime(long _v_) { // 进度条的开始时间
			starttime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ProgressBarInfo.Data)) return false;
			ProgressBarInfo.Data _o_ = (ProgressBarInfo.Data) _o1_;
			if (bartype != _o_.bartype) return false;
			if (itemkey != _o_.itemkey) return false;
			if (inprogress != _o_.inprogress) return false;
			if (starttime != _o_.starttime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += bartype;
			_h_ += itemkey;
			_h_ += inprogress;
			_h_ += starttime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(bartype);
			_sb_.append(",");
			_sb_.append(itemkey);
			_sb_.append(",");
			_sb_.append(inprogress);
			_sb_.append(",");
			_sb_.append(starttime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
