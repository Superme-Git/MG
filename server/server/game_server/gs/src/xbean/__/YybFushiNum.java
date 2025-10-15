
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class YybFushiNum extends mkdb.XBean implements xbean.YybFushiNum {
	private int balance; // 现金充值符石总数
	private int genbalance; // 系统赠送符石数
	private int saveamt; // 累计充值金额
	private long fushiall; // 累计符石总数，包括所有产出途径的符石数

	@Override
	public void _reset_unsafe_() {
		balance = 0;
		genbalance = 0;
		saveamt = 0;
		fushiall = 0L;
	}

	YybFushiNum(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public YybFushiNum() {
		this(0, null, null);
	}

	public YybFushiNum(YybFushiNum _o_) {
		this(_o_, null, null);
	}

	YybFushiNum(xbean.YybFushiNum _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof YybFushiNum) assign((YybFushiNum)_o1_);
		else if (_o1_ instanceof YybFushiNum.Data) assign((YybFushiNum.Data)_o1_);
		else if (_o1_ instanceof YybFushiNum.Const) assign(((YybFushiNum.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(YybFushiNum _o_) {
		_o_._xdb_verify_unsafe_();
		balance = _o_.balance;
		genbalance = _o_.genbalance;
		saveamt = _o_.saveamt;
		fushiall = _o_.fushiall;
	}

	private void assign(YybFushiNum.Data _o_) {
		balance = _o_.balance;
		genbalance = _o_.genbalance;
		saveamt = _o_.saveamt;
		fushiall = _o_.fushiall;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(balance);
		_os_.marshal(genbalance);
		_os_.marshal(saveamt);
		_os_.marshal(fushiall);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		balance = _os_.unmarshal_int();
		genbalance = _os_.unmarshal_int();
		saveamt = _os_.unmarshal_int();
		fushiall = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.YybFushiNum copy() {
		_xdb_verify_unsafe_();
		return new YybFushiNum(this);
	}

	@Override
	public xbean.YybFushiNum toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YybFushiNum toBean() {
		_xdb_verify_unsafe_();
		return new YybFushiNum(this); // same as copy()
	}

	@Override
	public xbean.YybFushiNum toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YybFushiNum toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getBalance() { // 现金充值符石总数
		_xdb_verify_unsafe_();
		return balance;
	}

	@Override
	public int getGenbalance() { // 系统赠送符石数
		_xdb_verify_unsafe_();
		return genbalance;
	}

	@Override
	public int getSaveamt() { // 累计充值金额
		_xdb_verify_unsafe_();
		return saveamt;
	}

	@Override
	public long getFushiall() { // 累计符石总数，包括所有产出途径的符石数
		_xdb_verify_unsafe_();
		return fushiall;
	}

	@Override
	public void setBalance(int _v_) { // 现金充值符石总数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "balance") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, balance) {
					public void rollback() { balance = _xdb_saved; }
				};}});
		balance = _v_;
	}

	@Override
	public void setGenbalance(int _v_) { // 系统赠送符石数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "genbalance") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, genbalance) {
					public void rollback() { genbalance = _xdb_saved; }
				};}});
		genbalance = _v_;
	}

	@Override
	public void setSaveamt(int _v_) { // 累计充值金额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "saveamt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, saveamt) {
					public void rollback() { saveamt = _xdb_saved; }
				};}});
		saveamt = _v_;
	}

	@Override
	public void setFushiall(long _v_) { // 累计符石总数，包括所有产出途径的符石数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fushiall") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fushiall) {
					public void rollback() { fushiall = _xdb_saved; }
				};}});
		fushiall = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		YybFushiNum _o_ = null;
		if ( _o1_ instanceof YybFushiNum ) _o_ = (YybFushiNum)_o1_;
		else if ( _o1_ instanceof YybFushiNum.Const ) _o_ = ((YybFushiNum.Const)_o1_).nThis();
		else return false;
		if (balance != _o_.balance) return false;
		if (genbalance != _o_.genbalance) return false;
		if (saveamt != _o_.saveamt) return false;
		if (fushiall != _o_.fushiall) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += balance;
		_h_ += genbalance;
		_h_ += saveamt;
		_h_ += fushiall;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(balance);
		_sb_.append(",");
		_sb_.append(genbalance);
		_sb_.append(",");
		_sb_.append(saveamt);
		_sb_.append(",");
		_sb_.append(fushiall);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("balance"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("genbalance"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("saveamt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fushiall"));
		return lb;
	}

	private class Const implements xbean.YybFushiNum {
		YybFushiNum nThis() {
			return YybFushiNum.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.YybFushiNum copy() {
			return YybFushiNum.this.copy();
		}

		@Override
		public xbean.YybFushiNum toData() {
			return YybFushiNum.this.toData();
		}

		public xbean.YybFushiNum toBean() {
			return YybFushiNum.this.toBean();
		}

		@Override
		public xbean.YybFushiNum toDataIf() {
			return YybFushiNum.this.toDataIf();
		}

		public xbean.YybFushiNum toBeanIf() {
			return YybFushiNum.this.toBeanIf();
		}

		@Override
		public int getBalance() { // 现金充值符石总数
			_xdb_verify_unsafe_();
			return balance;
		}

		@Override
		public int getGenbalance() { // 系统赠送符石数
			_xdb_verify_unsafe_();
			return genbalance;
		}

		@Override
		public int getSaveamt() { // 累计充值金额
			_xdb_verify_unsafe_();
			return saveamt;
		}

		@Override
		public long getFushiall() { // 累计符石总数，包括所有产出途径的符石数
			_xdb_verify_unsafe_();
			return fushiall;
		}

		@Override
		public void setBalance(int _v_) { // 现金充值符石总数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGenbalance(int _v_) { // 系统赠送符石数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSaveamt(int _v_) { // 累计充值金额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFushiall(long _v_) { // 累计符石总数，包括所有产出途径的符石数
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
			return YybFushiNum.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return YybFushiNum.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return YybFushiNum.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return YybFushiNum.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return YybFushiNum.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return YybFushiNum.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return YybFushiNum.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return YybFushiNum.this.hashCode();
		}

		@Override
		public String toString() {
			return YybFushiNum.this.toString();
		}

	}

	public static final class Data implements xbean.YybFushiNum {
		private int balance; // 现金充值符石总数
		private int genbalance; // 系统赠送符石数
		private int saveamt; // 累计充值金额
		private long fushiall; // 累计符石总数，包括所有产出途径的符石数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.YybFushiNum _o1_) {
			if (_o1_ instanceof YybFushiNum) assign((YybFushiNum)_o1_);
			else if (_o1_ instanceof YybFushiNum.Data) assign((YybFushiNum.Data)_o1_);
			else if (_o1_ instanceof YybFushiNum.Const) assign(((YybFushiNum.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(YybFushiNum _o_) {
			balance = _o_.balance;
			genbalance = _o_.genbalance;
			saveamt = _o_.saveamt;
			fushiall = _o_.fushiall;
		}

		private void assign(YybFushiNum.Data _o_) {
			balance = _o_.balance;
			genbalance = _o_.genbalance;
			saveamt = _o_.saveamt;
			fushiall = _o_.fushiall;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(balance);
			_os_.marshal(genbalance);
			_os_.marshal(saveamt);
			_os_.marshal(fushiall);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			balance = _os_.unmarshal_int();
			genbalance = _os_.unmarshal_int();
			saveamt = _os_.unmarshal_int();
			fushiall = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.YybFushiNum copy() {
			return new Data(this);
		}

		@Override
		public xbean.YybFushiNum toData() {
			return new Data(this);
		}

		public xbean.YybFushiNum toBean() {
			return new YybFushiNum(this, null, null);
		}

		@Override
		public xbean.YybFushiNum toDataIf() {
			return this;
		}

		public xbean.YybFushiNum toBeanIf() {
			return new YybFushiNum(this, null, null);
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
		public int getBalance() { // 现金充值符石总数
			return balance;
		}

		@Override
		public int getGenbalance() { // 系统赠送符石数
			return genbalance;
		}

		@Override
		public int getSaveamt() { // 累计充值金额
			return saveamt;
		}

		@Override
		public long getFushiall() { // 累计符石总数，包括所有产出途径的符石数
			return fushiall;
		}

		@Override
		public void setBalance(int _v_) { // 现金充值符石总数
			balance = _v_;
		}

		@Override
		public void setGenbalance(int _v_) { // 系统赠送符石数
			genbalance = _v_;
		}

		@Override
		public void setSaveamt(int _v_) { // 累计充值金额
			saveamt = _v_;
		}

		@Override
		public void setFushiall(long _v_) { // 累计符石总数，包括所有产出途径的符石数
			fushiall = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof YybFushiNum.Data)) return false;
			YybFushiNum.Data _o_ = (YybFushiNum.Data) _o1_;
			if (balance != _o_.balance) return false;
			if (genbalance != _o_.genbalance) return false;
			if (saveamt != _o_.saveamt) return false;
			if (fushiall != _o_.fushiall) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += balance;
			_h_ += genbalance;
			_h_ += saveamt;
			_h_ += fushiall;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(balance);
			_sb_.append(",");
			_sb_.append(genbalance);
			_sb_.append(",");
			_sb_.append(saveamt);
			_sb_.append(",");
			_sb_.append(fushiall);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
