
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ApprentceChieve extends mkdb.XBean implements xbean.ApprentceChieve {
	private int currnumber; // 当前次数
	private int total; // 当前总量
	private int flag; // 0=未完成 1=完成 2=已经领奖
	private int content; // 记录一个数值

	@Override
	public void _reset_unsafe_() {
		currnumber = 0;
		total = 0;
		flag = 0;
		content = 0;
	}

	ApprentceChieve(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ApprentceChieve() {
		this(0, null, null);
	}

	public ApprentceChieve(ApprentceChieve _o_) {
		this(_o_, null, null);
	}

	ApprentceChieve(xbean.ApprentceChieve _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ApprentceChieve) assign((ApprentceChieve)_o1_);
		else if (_o1_ instanceof ApprentceChieve.Data) assign((ApprentceChieve.Data)_o1_);
		else if (_o1_ instanceof ApprentceChieve.Const) assign(((ApprentceChieve.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ApprentceChieve _o_) {
		_o_._xdb_verify_unsafe_();
		currnumber = _o_.currnumber;
		total = _o_.total;
		flag = _o_.flag;
		content = _o_.content;
	}

	private void assign(ApprentceChieve.Data _o_) {
		currnumber = _o_.currnumber;
		total = _o_.total;
		flag = _o_.flag;
		content = _o_.content;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(currnumber);
		_os_.marshal(total);
		_os_.marshal(flag);
		_os_.marshal(content);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		currnumber = _os_.unmarshal_int();
		total = _os_.unmarshal_int();
		flag = _os_.unmarshal_int();
		content = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ApprentceChieve copy() {
		_xdb_verify_unsafe_();
		return new ApprentceChieve(this);
	}

	@Override
	public xbean.ApprentceChieve toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ApprentceChieve toBean() {
		_xdb_verify_unsafe_();
		return new ApprentceChieve(this); // same as copy()
	}

	@Override
	public xbean.ApprentceChieve toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ApprentceChieve toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getCurrnumber() { // 当前次数
		_xdb_verify_unsafe_();
		return currnumber;
	}

	@Override
	public int getTotal() { // 当前总量
		_xdb_verify_unsafe_();
		return total;
	}

	@Override
	public int getFlag() { // 0=未完成 1=完成 2=已经领奖
		_xdb_verify_unsafe_();
		return flag;
	}

	@Override
	public int getContent() { // 记录一个数值
		_xdb_verify_unsafe_();
		return content;
	}

	@Override
	public void setCurrnumber(int _v_) { // 当前次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currnumber") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currnumber) {
					public void rollback() { currnumber = _xdb_saved; }
				};}});
		currnumber = _v_;
	}

	@Override
	public void setTotal(int _v_) { // 当前总量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "total") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, total) {
					public void rollback() { total = _xdb_saved; }
				};}});
		total = _v_;
	}

	@Override
	public void setFlag(int _v_) { // 0=未完成 1=完成 2=已经领奖
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "flag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, flag) {
					public void rollback() { flag = _xdb_saved; }
				};}});
		flag = _v_;
	}

	@Override
	public void setContent(int _v_) { // 记录一个数值
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "content") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, content) {
					public void rollback() { content = _xdb_saved; }
				};}});
		content = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ApprentceChieve _o_ = null;
		if ( _o1_ instanceof ApprentceChieve ) _o_ = (ApprentceChieve)_o1_;
		else if ( _o1_ instanceof ApprentceChieve.Const ) _o_ = ((ApprentceChieve.Const)_o1_).nThis();
		else return false;
		if (currnumber != _o_.currnumber) return false;
		if (total != _o_.total) return false;
		if (flag != _o_.flag) return false;
		if (content != _o_.content) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += currnumber;
		_h_ += total;
		_h_ += flag;
		_h_ += content;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(currnumber);
		_sb_.append(",");
		_sb_.append(total);
		_sb_.append(",");
		_sb_.append(flag);
		_sb_.append(",");
		_sb_.append(content);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currnumber"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("total"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("flag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("content"));
		return lb;
	}

	private class Const implements xbean.ApprentceChieve {
		ApprentceChieve nThis() {
			return ApprentceChieve.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ApprentceChieve copy() {
			return ApprentceChieve.this.copy();
		}

		@Override
		public xbean.ApprentceChieve toData() {
			return ApprentceChieve.this.toData();
		}

		public xbean.ApprentceChieve toBean() {
			return ApprentceChieve.this.toBean();
		}

		@Override
		public xbean.ApprentceChieve toDataIf() {
			return ApprentceChieve.this.toDataIf();
		}

		public xbean.ApprentceChieve toBeanIf() {
			return ApprentceChieve.this.toBeanIf();
		}

		@Override
		public int getCurrnumber() { // 当前次数
			_xdb_verify_unsafe_();
			return currnumber;
		}

		@Override
		public int getTotal() { // 当前总量
			_xdb_verify_unsafe_();
			return total;
		}

		@Override
		public int getFlag() { // 0=未完成 1=完成 2=已经领奖
			_xdb_verify_unsafe_();
			return flag;
		}

		@Override
		public int getContent() { // 记录一个数值
			_xdb_verify_unsafe_();
			return content;
		}

		@Override
		public void setCurrnumber(int _v_) { // 当前次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotal(int _v_) { // 当前总量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFlag(int _v_) { // 0=未完成 1=完成 2=已经领奖
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setContent(int _v_) { // 记录一个数值
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
			return ApprentceChieve.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ApprentceChieve.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ApprentceChieve.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ApprentceChieve.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ApprentceChieve.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ApprentceChieve.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ApprentceChieve.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ApprentceChieve.this.hashCode();
		}

		@Override
		public String toString() {
			return ApprentceChieve.this.toString();
		}

	}

	public static final class Data implements xbean.ApprentceChieve {
		private int currnumber; // 当前次数
		private int total; // 当前总量
		private int flag; // 0=未完成 1=完成 2=已经领奖
		private int content; // 记录一个数值

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ApprentceChieve _o1_) {
			if (_o1_ instanceof ApprentceChieve) assign((ApprentceChieve)_o1_);
			else if (_o1_ instanceof ApprentceChieve.Data) assign((ApprentceChieve.Data)_o1_);
			else if (_o1_ instanceof ApprentceChieve.Const) assign(((ApprentceChieve.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ApprentceChieve _o_) {
			currnumber = _o_.currnumber;
			total = _o_.total;
			flag = _o_.flag;
			content = _o_.content;
		}

		private void assign(ApprentceChieve.Data _o_) {
			currnumber = _o_.currnumber;
			total = _o_.total;
			flag = _o_.flag;
			content = _o_.content;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(currnumber);
			_os_.marshal(total);
			_os_.marshal(flag);
			_os_.marshal(content);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			currnumber = _os_.unmarshal_int();
			total = _os_.unmarshal_int();
			flag = _os_.unmarshal_int();
			content = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ApprentceChieve copy() {
			return new Data(this);
		}

		@Override
		public xbean.ApprentceChieve toData() {
			return new Data(this);
		}

		public xbean.ApprentceChieve toBean() {
			return new ApprentceChieve(this, null, null);
		}

		@Override
		public xbean.ApprentceChieve toDataIf() {
			return this;
		}

		public xbean.ApprentceChieve toBeanIf() {
			return new ApprentceChieve(this, null, null);
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
		public int getCurrnumber() { // 当前次数
			return currnumber;
		}

		@Override
		public int getTotal() { // 当前总量
			return total;
		}

		@Override
		public int getFlag() { // 0=未完成 1=完成 2=已经领奖
			return flag;
		}

		@Override
		public int getContent() { // 记录一个数值
			return content;
		}

		@Override
		public void setCurrnumber(int _v_) { // 当前次数
			currnumber = _v_;
		}

		@Override
		public void setTotal(int _v_) { // 当前总量
			total = _v_;
		}

		@Override
		public void setFlag(int _v_) { // 0=未完成 1=完成 2=已经领奖
			flag = _v_;
		}

		@Override
		public void setContent(int _v_) { // 记录一个数值
			content = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ApprentceChieve.Data)) return false;
			ApprentceChieve.Data _o_ = (ApprentceChieve.Data) _o1_;
			if (currnumber != _o_.currnumber) return false;
			if (total != _o_.total) return false;
			if (flag != _o_.flag) return false;
			if (content != _o_.content) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += currnumber;
			_h_ += total;
			_h_ += flag;
			_h_ += content;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(currnumber);
			_sb_.append(",");
			_sb_.append(total);
			_sb_.append(",");
			_sb_.append(flag);
			_sb_.append(",");
			_sb_.append(content);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
