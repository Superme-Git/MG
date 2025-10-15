
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RuneInfo extends mkdb.XBean implements xbean.RuneInfo {
	private int dayrequestnum; // 请求次数
	private int allgivenum; // 捐符数
	private int allacceptnum; // 收符数
	private int itemlevel; // 物品等级

	@Override
	public void _reset_unsafe_() {
		dayrequestnum = 0;
		allgivenum = 0;
		allacceptnum = 0;
		itemlevel = 0;
	}

	RuneInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RuneInfo() {
		this(0, null, null);
	}

	public RuneInfo(RuneInfo _o_) {
		this(_o_, null, null);
	}

	RuneInfo(xbean.RuneInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RuneInfo) assign((RuneInfo)_o1_);
		else if (_o1_ instanceof RuneInfo.Data) assign((RuneInfo.Data)_o1_);
		else if (_o1_ instanceof RuneInfo.Const) assign(((RuneInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RuneInfo _o_) {
		_o_._xdb_verify_unsafe_();
		dayrequestnum = _o_.dayrequestnum;
		allgivenum = _o_.allgivenum;
		allacceptnum = _o_.allacceptnum;
		itemlevel = _o_.itemlevel;
	}

	private void assign(RuneInfo.Data _o_) {
		dayrequestnum = _o_.dayrequestnum;
		allgivenum = _o_.allgivenum;
		allacceptnum = _o_.allacceptnum;
		itemlevel = _o_.itemlevel;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(dayrequestnum);
		_os_.marshal(allgivenum);
		_os_.marshal(allacceptnum);
		_os_.marshal(itemlevel);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		dayrequestnum = _os_.unmarshal_int();
		allgivenum = _os_.unmarshal_int();
		allacceptnum = _os_.unmarshal_int();
		itemlevel = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RuneInfo copy() {
		_xdb_verify_unsafe_();
		return new RuneInfo(this);
	}

	@Override
	public xbean.RuneInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RuneInfo toBean() {
		_xdb_verify_unsafe_();
		return new RuneInfo(this); // same as copy()
	}

	@Override
	public xbean.RuneInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RuneInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getDayrequestnum() { // 请求次数
		_xdb_verify_unsafe_();
		return dayrequestnum;
	}

	@Override
	public int getAllgivenum() { // 捐符数
		_xdb_verify_unsafe_();
		return allgivenum;
	}

	@Override
	public int getAllacceptnum() { // 收符数
		_xdb_verify_unsafe_();
		return allacceptnum;
	}

	@Override
	public int getItemlevel() { // 物品等级
		_xdb_verify_unsafe_();
		return itemlevel;
	}

	@Override
	public void setDayrequestnum(int _v_) { // 请求次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dayrequestnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dayrequestnum) {
					public void rollback() { dayrequestnum = _xdb_saved; }
				};}});
		dayrequestnum = _v_;
	}

	@Override
	public void setAllgivenum(int _v_) { // 捐符数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "allgivenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, allgivenum) {
					public void rollback() { allgivenum = _xdb_saved; }
				};}});
		allgivenum = _v_;
	}

	@Override
	public void setAllacceptnum(int _v_) { // 收符数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "allacceptnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, allacceptnum) {
					public void rollback() { allacceptnum = _xdb_saved; }
				};}});
		allacceptnum = _v_;
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
		RuneInfo _o_ = null;
		if ( _o1_ instanceof RuneInfo ) _o_ = (RuneInfo)_o1_;
		else if ( _o1_ instanceof RuneInfo.Const ) _o_ = ((RuneInfo.Const)_o1_).nThis();
		else return false;
		if (dayrequestnum != _o_.dayrequestnum) return false;
		if (allgivenum != _o_.allgivenum) return false;
		if (allacceptnum != _o_.allacceptnum) return false;
		if (itemlevel != _o_.itemlevel) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += dayrequestnum;
		_h_ += allgivenum;
		_h_ += allacceptnum;
		_h_ += itemlevel;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(dayrequestnum);
		_sb_.append(",");
		_sb_.append(allgivenum);
		_sb_.append(",");
		_sb_.append(allacceptnum);
		_sb_.append(",");
		_sb_.append(itemlevel);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dayrequestnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("allgivenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("allacceptnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemlevel"));
		return lb;
	}

	private class Const implements xbean.RuneInfo {
		RuneInfo nThis() {
			return RuneInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RuneInfo copy() {
			return RuneInfo.this.copy();
		}

		@Override
		public xbean.RuneInfo toData() {
			return RuneInfo.this.toData();
		}

		public xbean.RuneInfo toBean() {
			return RuneInfo.this.toBean();
		}

		@Override
		public xbean.RuneInfo toDataIf() {
			return RuneInfo.this.toDataIf();
		}

		public xbean.RuneInfo toBeanIf() {
			return RuneInfo.this.toBeanIf();
		}

		@Override
		public int getDayrequestnum() { // 请求次数
			_xdb_verify_unsafe_();
			return dayrequestnum;
		}

		@Override
		public int getAllgivenum() { // 捐符数
			_xdb_verify_unsafe_();
			return allgivenum;
		}

		@Override
		public int getAllacceptnum() { // 收符数
			_xdb_verify_unsafe_();
			return allacceptnum;
		}

		@Override
		public int getItemlevel() { // 物品等级
			_xdb_verify_unsafe_();
			return itemlevel;
		}

		@Override
		public void setDayrequestnum(int _v_) { // 请求次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAllgivenum(int _v_) { // 捐符数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAllacceptnum(int _v_) { // 收符数
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
			return RuneInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RuneInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RuneInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RuneInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RuneInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RuneInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RuneInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RuneInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RuneInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RuneInfo {
		private int dayrequestnum; // 请求次数
		private int allgivenum; // 捐符数
		private int allacceptnum; // 收符数
		private int itemlevel; // 物品等级

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RuneInfo _o1_) {
			if (_o1_ instanceof RuneInfo) assign((RuneInfo)_o1_);
			else if (_o1_ instanceof RuneInfo.Data) assign((RuneInfo.Data)_o1_);
			else if (_o1_ instanceof RuneInfo.Const) assign(((RuneInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RuneInfo _o_) {
			dayrequestnum = _o_.dayrequestnum;
			allgivenum = _o_.allgivenum;
			allacceptnum = _o_.allacceptnum;
			itemlevel = _o_.itemlevel;
		}

		private void assign(RuneInfo.Data _o_) {
			dayrequestnum = _o_.dayrequestnum;
			allgivenum = _o_.allgivenum;
			allacceptnum = _o_.allacceptnum;
			itemlevel = _o_.itemlevel;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(dayrequestnum);
			_os_.marshal(allgivenum);
			_os_.marshal(allacceptnum);
			_os_.marshal(itemlevel);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			dayrequestnum = _os_.unmarshal_int();
			allgivenum = _os_.unmarshal_int();
			allacceptnum = _os_.unmarshal_int();
			itemlevel = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RuneInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RuneInfo toData() {
			return new Data(this);
		}

		public xbean.RuneInfo toBean() {
			return new RuneInfo(this, null, null);
		}

		@Override
		public xbean.RuneInfo toDataIf() {
			return this;
		}

		public xbean.RuneInfo toBeanIf() {
			return new RuneInfo(this, null, null);
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
		public int getDayrequestnum() { // 请求次数
			return dayrequestnum;
		}

		@Override
		public int getAllgivenum() { // 捐符数
			return allgivenum;
		}

		@Override
		public int getAllacceptnum() { // 收符数
			return allacceptnum;
		}

		@Override
		public int getItemlevel() { // 物品等级
			return itemlevel;
		}

		@Override
		public void setDayrequestnum(int _v_) { // 请求次数
			dayrequestnum = _v_;
		}

		@Override
		public void setAllgivenum(int _v_) { // 捐符数
			allgivenum = _v_;
		}

		@Override
		public void setAllacceptnum(int _v_) { // 收符数
			allacceptnum = _v_;
		}

		@Override
		public void setItemlevel(int _v_) { // 物品等级
			itemlevel = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RuneInfo.Data)) return false;
			RuneInfo.Data _o_ = (RuneInfo.Data) _o1_;
			if (dayrequestnum != _o_.dayrequestnum) return false;
			if (allgivenum != _o_.allgivenum) return false;
			if (allacceptnum != _o_.allacceptnum) return false;
			if (itemlevel != _o_.itemlevel) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += dayrequestnum;
			_h_ += allgivenum;
			_h_ += allacceptnum;
			_h_ += itemlevel;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(dayrequestnum);
			_sb_.append(",");
			_sb_.append(allgivenum);
			_sb_.append(",");
			_sb_.append(allacceptnum);
			_sb_.append(",");
			_sb_.append(itemlevel);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
