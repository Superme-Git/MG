
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SRRedPackNum extends mkdb.XBean implements xbean.SRRedPackNum {
	private int modeltype; // 红包类型(不要用)
	private int redpacksendnum; // 发红包数量
	private int redpackreceivenum; // 收红包数量
	private int redpacksendfushinum; // 发红包符石总数

	@Override
	public void _reset_unsafe_() {
		modeltype = 0;
		redpacksendnum = 0;
		redpackreceivenum = 0;
		redpacksendfushinum = 0;
	}

	SRRedPackNum(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public SRRedPackNum() {
		this(0, null, null);
	}

	public SRRedPackNum(SRRedPackNum _o_) {
		this(_o_, null, null);
	}

	SRRedPackNum(xbean.SRRedPackNum _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SRRedPackNum) assign((SRRedPackNum)_o1_);
		else if (_o1_ instanceof SRRedPackNum.Data) assign((SRRedPackNum.Data)_o1_);
		else if (_o1_ instanceof SRRedPackNum.Const) assign(((SRRedPackNum.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SRRedPackNum _o_) {
		_o_._xdb_verify_unsafe_();
		modeltype = _o_.modeltype;
		redpacksendnum = _o_.redpacksendnum;
		redpackreceivenum = _o_.redpackreceivenum;
		redpacksendfushinum = _o_.redpacksendfushinum;
	}

	private void assign(SRRedPackNum.Data _o_) {
		modeltype = _o_.modeltype;
		redpacksendnum = _o_.redpacksendnum;
		redpackreceivenum = _o_.redpackreceivenum;
		redpacksendfushinum = _o_.redpacksendfushinum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(modeltype);
		_os_.marshal(redpacksendnum);
		_os_.marshal(redpackreceivenum);
		_os_.marshal(redpacksendfushinum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		modeltype = _os_.unmarshal_int();
		redpacksendnum = _os_.unmarshal_int();
		redpackreceivenum = _os_.unmarshal_int();
		redpacksendfushinum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.SRRedPackNum copy() {
		_xdb_verify_unsafe_();
		return new SRRedPackNum(this);
	}

	@Override
	public xbean.SRRedPackNum toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SRRedPackNum toBean() {
		_xdb_verify_unsafe_();
		return new SRRedPackNum(this); // same as copy()
	}

	@Override
	public xbean.SRRedPackNum toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SRRedPackNum toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getModeltype() { // 红包类型(不要用)
		_xdb_verify_unsafe_();
		return modeltype;
	}

	@Override
	public int getRedpacksendnum() { // 发红包数量
		_xdb_verify_unsafe_();
		return redpacksendnum;
	}

	@Override
	public int getRedpackreceivenum() { // 收红包数量
		_xdb_verify_unsafe_();
		return redpackreceivenum;
	}

	@Override
	public int getRedpacksendfushinum() { // 发红包符石总数
		_xdb_verify_unsafe_();
		return redpacksendfushinum;
	}

	@Override
	public void setModeltype(int _v_) { // 红包类型(不要用)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "modeltype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, modeltype) {
					public void rollback() { modeltype = _xdb_saved; }
				};}});
		modeltype = _v_;
	}

	@Override
	public void setRedpacksendnum(int _v_) { // 发红包数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpacksendnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, redpacksendnum) {
					public void rollback() { redpacksendnum = _xdb_saved; }
				};}});
		redpacksendnum = _v_;
	}

	@Override
	public void setRedpackreceivenum(int _v_) { // 收红包数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackreceivenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, redpackreceivenum) {
					public void rollback() { redpackreceivenum = _xdb_saved; }
				};}});
		redpackreceivenum = _v_;
	}

	@Override
	public void setRedpacksendfushinum(int _v_) { // 发红包符石总数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpacksendfushinum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, redpacksendfushinum) {
					public void rollback() { redpacksendfushinum = _xdb_saved; }
				};}});
		redpacksendfushinum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SRRedPackNum _o_ = null;
		if ( _o1_ instanceof SRRedPackNum ) _o_ = (SRRedPackNum)_o1_;
		else if ( _o1_ instanceof SRRedPackNum.Const ) _o_ = ((SRRedPackNum.Const)_o1_).nThis();
		else return false;
		if (modeltype != _o_.modeltype) return false;
		if (redpacksendnum != _o_.redpacksendnum) return false;
		if (redpackreceivenum != _o_.redpackreceivenum) return false;
		if (redpacksendfushinum != _o_.redpacksendfushinum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += redpacksendnum;
		_h_ += redpackreceivenum;
		_h_ += redpacksendfushinum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype);
		_sb_.append(",");
		_sb_.append(redpacksendnum);
		_sb_.append(",");
		_sb_.append(redpackreceivenum);
		_sb_.append(",");
		_sb_.append(redpacksendfushinum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("modeltype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpacksendnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackreceivenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpacksendfushinum"));
		return lb;
	}

	private class Const implements xbean.SRRedPackNum {
		SRRedPackNum nThis() {
			return SRRedPackNum.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SRRedPackNum copy() {
			return SRRedPackNum.this.copy();
		}

		@Override
		public xbean.SRRedPackNum toData() {
			return SRRedPackNum.this.toData();
		}

		public xbean.SRRedPackNum toBean() {
			return SRRedPackNum.this.toBean();
		}

		@Override
		public xbean.SRRedPackNum toDataIf() {
			return SRRedPackNum.this.toDataIf();
		}

		public xbean.SRRedPackNum toBeanIf() {
			return SRRedPackNum.this.toBeanIf();
		}

		@Override
		public int getModeltype() { // 红包类型(不要用)
			_xdb_verify_unsafe_();
			return modeltype;
		}

		@Override
		public int getRedpacksendnum() { // 发红包数量
			_xdb_verify_unsafe_();
			return redpacksendnum;
		}

		@Override
		public int getRedpackreceivenum() { // 收红包数量
			_xdb_verify_unsafe_();
			return redpackreceivenum;
		}

		@Override
		public int getRedpacksendfushinum() { // 发红包符石总数
			_xdb_verify_unsafe_();
			return redpacksendfushinum;
		}

		@Override
		public void setModeltype(int _v_) { // 红包类型(不要用)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpacksendnum(int _v_) { // 发红包数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackreceivenum(int _v_) { // 收红包数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpacksendfushinum(int _v_) { // 发红包符石总数
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
			return SRRedPackNum.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SRRedPackNum.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SRRedPackNum.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SRRedPackNum.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SRRedPackNum.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SRRedPackNum.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SRRedPackNum.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SRRedPackNum.this.hashCode();
		}

		@Override
		public String toString() {
			return SRRedPackNum.this.toString();
		}

	}

	public static final class Data implements xbean.SRRedPackNum {
		private int modeltype; // 红包类型(不要用)
		private int redpacksendnum; // 发红包数量
		private int redpackreceivenum; // 收红包数量
		private int redpacksendfushinum; // 发红包符石总数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.SRRedPackNum _o1_) {
			if (_o1_ instanceof SRRedPackNum) assign((SRRedPackNum)_o1_);
			else if (_o1_ instanceof SRRedPackNum.Data) assign((SRRedPackNum.Data)_o1_);
			else if (_o1_ instanceof SRRedPackNum.Const) assign(((SRRedPackNum.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SRRedPackNum _o_) {
			modeltype = _o_.modeltype;
			redpacksendnum = _o_.redpacksendnum;
			redpackreceivenum = _o_.redpackreceivenum;
			redpacksendfushinum = _o_.redpacksendfushinum;
		}

		private void assign(SRRedPackNum.Data _o_) {
			modeltype = _o_.modeltype;
			redpacksendnum = _o_.redpacksendnum;
			redpackreceivenum = _o_.redpackreceivenum;
			redpacksendfushinum = _o_.redpacksendfushinum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(modeltype);
			_os_.marshal(redpacksendnum);
			_os_.marshal(redpackreceivenum);
			_os_.marshal(redpacksendfushinum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			modeltype = _os_.unmarshal_int();
			redpacksendnum = _os_.unmarshal_int();
			redpackreceivenum = _os_.unmarshal_int();
			redpacksendfushinum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.SRRedPackNum copy() {
			return new Data(this);
		}

		@Override
		public xbean.SRRedPackNum toData() {
			return new Data(this);
		}

		public xbean.SRRedPackNum toBean() {
			return new SRRedPackNum(this, null, null);
		}

		@Override
		public xbean.SRRedPackNum toDataIf() {
			return this;
		}

		public xbean.SRRedPackNum toBeanIf() {
			return new SRRedPackNum(this, null, null);
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
		public int getModeltype() { // 红包类型(不要用)
			return modeltype;
		}

		@Override
		public int getRedpacksendnum() { // 发红包数量
			return redpacksendnum;
		}

		@Override
		public int getRedpackreceivenum() { // 收红包数量
			return redpackreceivenum;
		}

		@Override
		public int getRedpacksendfushinum() { // 发红包符石总数
			return redpacksendfushinum;
		}

		@Override
		public void setModeltype(int _v_) { // 红包类型(不要用)
			modeltype = _v_;
		}

		@Override
		public void setRedpacksendnum(int _v_) { // 发红包数量
			redpacksendnum = _v_;
		}

		@Override
		public void setRedpackreceivenum(int _v_) { // 收红包数量
			redpackreceivenum = _v_;
		}

		@Override
		public void setRedpacksendfushinum(int _v_) { // 发红包符石总数
			redpacksendfushinum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SRRedPackNum.Data)) return false;
			SRRedPackNum.Data _o_ = (SRRedPackNum.Data) _o1_;
			if (modeltype != _o_.modeltype) return false;
			if (redpacksendnum != _o_.redpacksendnum) return false;
			if (redpackreceivenum != _o_.redpackreceivenum) return false;
			if (redpacksendfushinum != _o_.redpacksendfushinum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += modeltype;
			_h_ += redpacksendnum;
			_h_ += redpackreceivenum;
			_h_ += redpacksendfushinum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(modeltype);
			_sb_.append(",");
			_sb_.append(redpacksendnum);
			_sb_.append(",");
			_sb_.append(redpackreceivenum);
			_sb_.append(",");
			_sb_.append(redpacksendfushinum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
