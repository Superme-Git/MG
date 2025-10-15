
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MedicItemList extends mkdb.XBean implements xbean.MedicItemList {
	private long medicitemrefreshtime; // 刷新时间
	private int selecttype; // 选择几倍产药类型   0正常  1双倍    2三倍
	private java.util.HashMap<Integer, xbean.MedicItem> medicitems; // 道具

	@Override
	public void _reset_unsafe_() {
		medicitemrefreshtime = 0L;
		selecttype = 0;
		medicitems.clear();
	}

	MedicItemList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		selecttype = 0;
		medicitems = new java.util.HashMap<Integer, xbean.MedicItem>();
	}

	public MedicItemList() {
		this(0, null, null);
	}

	public MedicItemList(MedicItemList _o_) {
		this(_o_, null, null);
	}

	MedicItemList(xbean.MedicItemList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MedicItemList) assign((MedicItemList)_o1_);
		else if (_o1_ instanceof MedicItemList.Data) assign((MedicItemList.Data)_o1_);
		else if (_o1_ instanceof MedicItemList.Const) assign(((MedicItemList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MedicItemList _o_) {
		_o_._xdb_verify_unsafe_();
		medicitemrefreshtime = _o_.medicitemrefreshtime;
		selecttype = _o_.selecttype;
		medicitems = new java.util.HashMap<Integer, xbean.MedicItem>();
		for (java.util.Map.Entry<Integer, xbean.MedicItem> _e_ : _o_.medicitems.entrySet())
			medicitems.put(_e_.getKey(), new MedicItem(_e_.getValue(), this, "medicitems"));
	}

	private void assign(MedicItemList.Data _o_) {
		medicitemrefreshtime = _o_.medicitemrefreshtime;
		selecttype = _o_.selecttype;
		medicitems = new java.util.HashMap<Integer, xbean.MedicItem>();
		for (java.util.Map.Entry<Integer, xbean.MedicItem> _e_ : _o_.medicitems.entrySet())
			medicitems.put(_e_.getKey(), new MedicItem(_e_.getValue(), this, "medicitems"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(medicitemrefreshtime);
		_os_.marshal(selecttype);
		_os_.compact_uint32(medicitems.size());
		for (java.util.Map.Entry<Integer, xbean.MedicItem> _e_ : medicitems.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		medicitemrefreshtime = _os_.unmarshal_long();
		selecttype = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				medicitems = new java.util.HashMap<Integer, xbean.MedicItem>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.MedicItem _v_ = new MedicItem(0, this, "medicitems");
				_v_.unmarshal(_os_);
				medicitems.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.MedicItemList copy() {
		_xdb_verify_unsafe_();
		return new MedicItemList(this);
	}

	@Override
	public xbean.MedicItemList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MedicItemList toBean() {
		_xdb_verify_unsafe_();
		return new MedicItemList(this); // same as copy()
	}

	@Override
	public xbean.MedicItemList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MedicItemList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getMedicitemrefreshtime() { // 刷新时间
		_xdb_verify_unsafe_();
		return medicitemrefreshtime;
	}

	@Override
	public int getSelecttype() { // 选择几倍产药类型   0正常  1双倍    2三倍
		_xdb_verify_unsafe_();
		return selecttype;
	}

	@Override
	public java.util.Map<Integer, xbean.MedicItem> getMedicitems() { // 道具
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "medicitems"), medicitems);
	}

	@Override
	public java.util.Map<Integer, xbean.MedicItem> getMedicitemsAsData() { // 道具
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.MedicItem> medicitems;
		MedicItemList _o_ = this;
		medicitems = new java.util.HashMap<Integer, xbean.MedicItem>();
		for (java.util.Map.Entry<Integer, xbean.MedicItem> _e_ : _o_.medicitems.entrySet())
			medicitems.put(_e_.getKey(), new MedicItem.Data(_e_.getValue()));
		return medicitems;
	}

	@Override
	public void setMedicitemrefreshtime(long _v_) { // 刷新时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "medicitemrefreshtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, medicitemrefreshtime) {
					public void rollback() { medicitemrefreshtime = _xdb_saved; }
				};}});
		medicitemrefreshtime = _v_;
	}

	@Override
	public void setSelecttype(int _v_) { // 选择几倍产药类型   0正常  1双倍    2三倍
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "selecttype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, selecttype) {
					public void rollback() { selecttype = _xdb_saved; }
				};}});
		selecttype = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MedicItemList _o_ = null;
		if ( _o1_ instanceof MedicItemList ) _o_ = (MedicItemList)_o1_;
		else if ( _o1_ instanceof MedicItemList.Const ) _o_ = ((MedicItemList.Const)_o1_).nThis();
		else return false;
		if (medicitemrefreshtime != _o_.medicitemrefreshtime) return false;
		if (selecttype != _o_.selecttype) return false;
		if (!medicitems.equals(_o_.medicitems)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += medicitemrefreshtime;
		_h_ += selecttype;
		_h_ += medicitems.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(medicitemrefreshtime);
		_sb_.append(",");
		_sb_.append(selecttype);
		_sb_.append(",");
		_sb_.append(medicitems);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("medicitemrefreshtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("selecttype"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("medicitems"));
		return lb;
	}

	private class Const implements xbean.MedicItemList {
		MedicItemList nThis() {
			return MedicItemList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MedicItemList copy() {
			return MedicItemList.this.copy();
		}

		@Override
		public xbean.MedicItemList toData() {
			return MedicItemList.this.toData();
		}

		public xbean.MedicItemList toBean() {
			return MedicItemList.this.toBean();
		}

		@Override
		public xbean.MedicItemList toDataIf() {
			return MedicItemList.this.toDataIf();
		}

		public xbean.MedicItemList toBeanIf() {
			return MedicItemList.this.toBeanIf();
		}

		@Override
		public long getMedicitemrefreshtime() { // 刷新时间
			_xdb_verify_unsafe_();
			return medicitemrefreshtime;
		}

		@Override
		public int getSelecttype() { // 选择几倍产药类型   0正常  1双倍    2三倍
			_xdb_verify_unsafe_();
			return selecttype;
		}

		@Override
		public java.util.Map<Integer, xbean.MedicItem> getMedicitems() { // 道具
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(medicitems);
		}

		@Override
		public java.util.Map<Integer, xbean.MedicItem> getMedicitemsAsData() { // 道具
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.MedicItem> medicitems;
			MedicItemList _o_ = MedicItemList.this;
			medicitems = new java.util.HashMap<Integer, xbean.MedicItem>();
			for (java.util.Map.Entry<Integer, xbean.MedicItem> _e_ : _o_.medicitems.entrySet())
				medicitems.put(_e_.getKey(), new MedicItem.Data(_e_.getValue()));
			return medicitems;
		}

		@Override
		public void setMedicitemrefreshtime(long _v_) { // 刷新时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSelecttype(int _v_) { // 选择几倍产药类型   0正常  1双倍    2三倍
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
			return MedicItemList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MedicItemList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MedicItemList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MedicItemList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MedicItemList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MedicItemList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MedicItemList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MedicItemList.this.hashCode();
		}

		@Override
		public String toString() {
			return MedicItemList.this.toString();
		}

	}

	public static final class Data implements xbean.MedicItemList {
		private long medicitemrefreshtime; // 刷新时间
		private int selecttype; // 选择几倍产药类型   0正常  1双倍    2三倍
		private java.util.HashMap<Integer, xbean.MedicItem> medicitems; // 道具

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			selecttype = 0;
			medicitems = new java.util.HashMap<Integer, xbean.MedicItem>();
		}

		Data(xbean.MedicItemList _o1_) {
			if (_o1_ instanceof MedicItemList) assign((MedicItemList)_o1_);
			else if (_o1_ instanceof MedicItemList.Data) assign((MedicItemList.Data)_o1_);
			else if (_o1_ instanceof MedicItemList.Const) assign(((MedicItemList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MedicItemList _o_) {
			medicitemrefreshtime = _o_.medicitemrefreshtime;
			selecttype = _o_.selecttype;
			medicitems = new java.util.HashMap<Integer, xbean.MedicItem>();
			for (java.util.Map.Entry<Integer, xbean.MedicItem> _e_ : _o_.medicitems.entrySet())
				medicitems.put(_e_.getKey(), new MedicItem.Data(_e_.getValue()));
		}

		private void assign(MedicItemList.Data _o_) {
			medicitemrefreshtime = _o_.medicitemrefreshtime;
			selecttype = _o_.selecttype;
			medicitems = new java.util.HashMap<Integer, xbean.MedicItem>();
			for (java.util.Map.Entry<Integer, xbean.MedicItem> _e_ : _o_.medicitems.entrySet())
				medicitems.put(_e_.getKey(), new MedicItem.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(medicitemrefreshtime);
			_os_.marshal(selecttype);
			_os_.compact_uint32(medicitems.size());
			for (java.util.Map.Entry<Integer, xbean.MedicItem> _e_ : medicitems.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			medicitemrefreshtime = _os_.unmarshal_long();
			selecttype = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					medicitems = new java.util.HashMap<Integer, xbean.MedicItem>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.MedicItem _v_ = xbean.Pod.newMedicItemData();
					_v_.unmarshal(_os_);
					medicitems.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.MedicItemList copy() {
			return new Data(this);
		}

		@Override
		public xbean.MedicItemList toData() {
			return new Data(this);
		}

		public xbean.MedicItemList toBean() {
			return new MedicItemList(this, null, null);
		}

		@Override
		public xbean.MedicItemList toDataIf() {
			return this;
		}

		public xbean.MedicItemList toBeanIf() {
			return new MedicItemList(this, null, null);
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
		public long getMedicitemrefreshtime() { // 刷新时间
			return medicitemrefreshtime;
		}

		@Override
		public int getSelecttype() { // 选择几倍产药类型   0正常  1双倍    2三倍
			return selecttype;
		}

		@Override
		public java.util.Map<Integer, xbean.MedicItem> getMedicitems() { // 道具
			return medicitems;
		}

		@Override
		public java.util.Map<Integer, xbean.MedicItem> getMedicitemsAsData() { // 道具
			return medicitems;
		}

		@Override
		public void setMedicitemrefreshtime(long _v_) { // 刷新时间
			medicitemrefreshtime = _v_;
		}

		@Override
		public void setSelecttype(int _v_) { // 选择几倍产药类型   0正常  1双倍    2三倍
			selecttype = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MedicItemList.Data)) return false;
			MedicItemList.Data _o_ = (MedicItemList.Data) _o1_;
			if (medicitemrefreshtime != _o_.medicitemrefreshtime) return false;
			if (selecttype != _o_.selecttype) return false;
			if (!medicitems.equals(_o_.medicitems)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += medicitemrefreshtime;
			_h_ += selecttype;
			_h_ += medicitems.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(medicitemrefreshtime);
			_sb_.append(",");
			_sb_.append(selecttype);
			_sb_.append(",");
			_sb_.append(medicitems);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
