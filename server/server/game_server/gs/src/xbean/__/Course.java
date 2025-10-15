
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Course extends mkdb.XBean implements xbean.Course {
	private int id; // id
	private int count; // 完成次数, 有的历程需要多次完成一个操作
	private int state; // 状态 0 未完成, 1 已经完成, 2 已经领奖

	@Override
	public void _reset_unsafe_() {
		id = 0;
		count = 0;
		state = 0;
	}

	Course(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public Course() {
		this(0, null, null);
	}

	public Course(Course _o_) {
		this(_o_, null, null);
	}

	Course(xbean.Course _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Course) assign((Course)_o1_);
		else if (_o1_ instanceof Course.Data) assign((Course.Data)_o1_);
		else if (_o1_ instanceof Course.Const) assign(((Course.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Course _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		count = _o_.count;
		state = _o_.state;
	}

	private void assign(Course.Data _o_) {
		id = _o_.id;
		count = _o_.count;
		state = _o_.state;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(count);
		_os_.marshal(state);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		count = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.Course copy() {
		_xdb_verify_unsafe_();
		return new Course(this);
	}

	@Override
	public xbean.Course toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Course toBean() {
		_xdb_verify_unsafe_();
		return new Course(this); // same as copy()
	}

	@Override
	public xbean.Course toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Course toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getCount() { // 完成次数, 有的历程需要多次完成一个操作
		_xdb_verify_unsafe_();
		return count;
	}

	@Override
	public int getState() { // 状态 0 未完成, 1 已经完成, 2 已经领奖
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public void setId(int _v_) { // id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setCount(int _v_) { // 完成次数, 有的历程需要多次完成一个操作
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "count") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, count) {
					public void rollback() { count = _xdb_saved; }
				};}});
		count = _v_;
	}

	@Override
	public void setState(int _v_) { // 状态 0 未完成, 1 已经完成, 2 已经领奖
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Course _o_ = null;
		if ( _o1_ instanceof Course ) _o_ = (Course)_o1_;
		else if ( _o1_ instanceof Course.Const ) _o_ = ((Course.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (count != _o_.count) return false;
		if (state != _o_.state) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += count;
		_h_ += state;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(count);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("count"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		return lb;
	}

	private class Const implements xbean.Course {
		Course nThis() {
			return Course.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Course copy() {
			return Course.this.copy();
		}

		@Override
		public xbean.Course toData() {
			return Course.this.toData();
		}

		public xbean.Course toBean() {
			return Course.this.toBean();
		}

		@Override
		public xbean.Course toDataIf() {
			return Course.this.toDataIf();
		}

		public xbean.Course toBeanIf() {
			return Course.this.toBeanIf();
		}

		@Override
		public int getId() { // id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getCount() { // 完成次数, 有的历程需要多次完成一个操作
			_xdb_verify_unsafe_();
			return count;
		}

		@Override
		public int getState() { // 状态 0 未完成, 1 已经完成, 2 已经领奖
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public void setId(int _v_) { // id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCount(int _v_) { // 完成次数, 有的历程需要多次完成一个操作
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 状态 0 未完成, 1 已经完成, 2 已经领奖
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
			return Course.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Course.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Course.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Course.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Course.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Course.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Course.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Course.this.hashCode();
		}

		@Override
		public String toString() {
			return Course.this.toString();
		}

	}

	public static final class Data implements xbean.Course {
		private int id; // id
		private int count; // 完成次数, 有的历程需要多次完成一个操作
		private int state; // 状态 0 未完成, 1 已经完成, 2 已经领奖

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.Course _o1_) {
			if (_o1_ instanceof Course) assign((Course)_o1_);
			else if (_o1_ instanceof Course.Data) assign((Course.Data)_o1_);
			else if (_o1_ instanceof Course.Const) assign(((Course.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Course _o_) {
			id = _o_.id;
			count = _o_.count;
			state = _o_.state;
		}

		private void assign(Course.Data _o_) {
			id = _o_.id;
			count = _o_.count;
			state = _o_.state;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(count);
			_os_.marshal(state);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			count = _os_.unmarshal_int();
			state = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.Course copy() {
			return new Data(this);
		}

		@Override
		public xbean.Course toData() {
			return new Data(this);
		}

		public xbean.Course toBean() {
			return new Course(this, null, null);
		}

		@Override
		public xbean.Course toDataIf() {
			return this;
		}

		public xbean.Course toBeanIf() {
			return new Course(this, null, null);
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
		public int getId() { // id
			return id;
		}

		@Override
		public int getCount() { // 完成次数, 有的历程需要多次完成一个操作
			return count;
		}

		@Override
		public int getState() { // 状态 0 未完成, 1 已经完成, 2 已经领奖
			return state;
		}

		@Override
		public void setId(int _v_) { // id
			id = _v_;
		}

		@Override
		public void setCount(int _v_) { // 完成次数, 有的历程需要多次完成一个操作
			count = _v_;
		}

		@Override
		public void setState(int _v_) { // 状态 0 未完成, 1 已经完成, 2 已经领奖
			state = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Course.Data)) return false;
			Course.Data _o_ = (Course.Data) _o1_;
			if (id != _o_.id) return false;
			if (count != _o_.count) return false;
			if (state != _o_.state) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += count;
			_h_ += state;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(count);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
