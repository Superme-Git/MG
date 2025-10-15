
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WeekIndex extends mkdb.XBean implements xbean.WeekIndex {
	private int index; // 伙伴免费轮换索引
	private int instanceindex; // 副本轮换索引

	@Override
	public void _reset_unsafe_() {
		index = 0;
		instanceindex = 0;
	}

	WeekIndex(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public WeekIndex() {
		this(0, null, null);
	}

	public WeekIndex(WeekIndex _o_) {
		this(_o_, null, null);
	}

	WeekIndex(xbean.WeekIndex _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WeekIndex) assign((WeekIndex)_o1_);
		else if (_o1_ instanceof WeekIndex.Data) assign((WeekIndex.Data)_o1_);
		else if (_o1_ instanceof WeekIndex.Const) assign(((WeekIndex.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WeekIndex _o_) {
		_o_._xdb_verify_unsafe_();
		index = _o_.index;
		instanceindex = _o_.instanceindex;
	}

	private void assign(WeekIndex.Data _o_) {
		index = _o_.index;
		instanceindex = _o_.instanceindex;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(index);
		_os_.marshal(instanceindex);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		index = _os_.unmarshal_int();
		instanceindex = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.WeekIndex copy() {
		_xdb_verify_unsafe_();
		return new WeekIndex(this);
	}

	@Override
	public xbean.WeekIndex toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WeekIndex toBean() {
		_xdb_verify_unsafe_();
		return new WeekIndex(this); // same as copy()
	}

	@Override
	public xbean.WeekIndex toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WeekIndex toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getIndex() { // 伙伴免费轮换索引
		_xdb_verify_unsafe_();
		return index;
	}

	@Override
	public int getInstanceindex() { // 副本轮换索引
		_xdb_verify_unsafe_();
		return instanceindex;
	}

	@Override
	public void setIndex(int _v_) { // 伙伴免费轮换索引
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "index") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, index) {
					public void rollback() { index = _xdb_saved; }
				};}});
		index = _v_;
	}

	@Override
	public void setInstanceindex(int _v_) { // 副本轮换索引
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "instanceindex") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, instanceindex) {
					public void rollback() { instanceindex = _xdb_saved; }
				};}});
		instanceindex = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WeekIndex _o_ = null;
		if ( _o1_ instanceof WeekIndex ) _o_ = (WeekIndex)_o1_;
		else if ( _o1_ instanceof WeekIndex.Const ) _o_ = ((WeekIndex.Const)_o1_).nThis();
		else return false;
		if (index != _o_.index) return false;
		if (instanceindex != _o_.instanceindex) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += index;
		_h_ += instanceindex;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(index);
		_sb_.append(",");
		_sb_.append(instanceindex);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("index"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instanceindex"));
		return lb;
	}

	private class Const implements xbean.WeekIndex {
		WeekIndex nThis() {
			return WeekIndex.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WeekIndex copy() {
			return WeekIndex.this.copy();
		}

		@Override
		public xbean.WeekIndex toData() {
			return WeekIndex.this.toData();
		}

		public xbean.WeekIndex toBean() {
			return WeekIndex.this.toBean();
		}

		@Override
		public xbean.WeekIndex toDataIf() {
			return WeekIndex.this.toDataIf();
		}

		public xbean.WeekIndex toBeanIf() {
			return WeekIndex.this.toBeanIf();
		}

		@Override
		public int getIndex() { // 伙伴免费轮换索引
			_xdb_verify_unsafe_();
			return index;
		}

		@Override
		public int getInstanceindex() { // 副本轮换索引
			_xdb_verify_unsafe_();
			return instanceindex;
		}

		@Override
		public void setIndex(int _v_) { // 伙伴免费轮换索引
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInstanceindex(int _v_) { // 副本轮换索引
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
			return WeekIndex.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WeekIndex.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WeekIndex.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WeekIndex.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WeekIndex.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WeekIndex.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WeekIndex.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WeekIndex.this.hashCode();
		}

		@Override
		public String toString() {
			return WeekIndex.this.toString();
		}

	}

	public static final class Data implements xbean.WeekIndex {
		private int index; // 伙伴免费轮换索引
		private int instanceindex; // 副本轮换索引

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.WeekIndex _o1_) {
			if (_o1_ instanceof WeekIndex) assign((WeekIndex)_o1_);
			else if (_o1_ instanceof WeekIndex.Data) assign((WeekIndex.Data)_o1_);
			else if (_o1_ instanceof WeekIndex.Const) assign(((WeekIndex.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WeekIndex _o_) {
			index = _o_.index;
			instanceindex = _o_.instanceindex;
		}

		private void assign(WeekIndex.Data _o_) {
			index = _o_.index;
			instanceindex = _o_.instanceindex;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(index);
			_os_.marshal(instanceindex);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			index = _os_.unmarshal_int();
			instanceindex = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.WeekIndex copy() {
			return new Data(this);
		}

		@Override
		public xbean.WeekIndex toData() {
			return new Data(this);
		}

		public xbean.WeekIndex toBean() {
			return new WeekIndex(this, null, null);
		}

		@Override
		public xbean.WeekIndex toDataIf() {
			return this;
		}

		public xbean.WeekIndex toBeanIf() {
			return new WeekIndex(this, null, null);
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
		public int getIndex() { // 伙伴免费轮换索引
			return index;
		}

		@Override
		public int getInstanceindex() { // 副本轮换索引
			return instanceindex;
		}

		@Override
		public void setIndex(int _v_) { // 伙伴免费轮换索引
			index = _v_;
		}

		@Override
		public void setInstanceindex(int _v_) { // 副本轮换索引
			instanceindex = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WeekIndex.Data)) return false;
			WeekIndex.Data _o_ = (WeekIndex.Data) _o1_;
			if (index != _o_.index) return false;
			if (instanceindex != _o_.instanceindex) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += index;
			_h_ += instanceindex;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(index);
			_sb_.append(",");
			_sb_.append(instanceindex);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
