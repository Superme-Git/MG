
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleClanPoint extends mkdb.XBean implements xbean.RoleClanPoint {
	private int freezedclanpoint; // 已冻结公会贡献度
	private int currentclanpoint; // 当前公会贡献度
	private int historyclanpoint; // 历史公会贡献度
	private int weekclanpoint; // 周公会贡献度by hzl
	private int preweekclanpoint; // 上周周公会贡献度by hzl
	private int oldhistoryclanpoint; // 旧的历史公会贡献度
	private int oldfreezedclanpoint; // 上次退出公会时候冻结公会贡献度

	@Override
	public void _reset_unsafe_() {
		freezedclanpoint = 0;
		currentclanpoint = 0;
		historyclanpoint = 0;
		weekclanpoint = 0;
		preweekclanpoint = 0;
		oldhistoryclanpoint = 0;
		oldfreezedclanpoint = 0;
	}

	RoleClanPoint(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		freezedclanpoint = 0;
		currentclanpoint = 0;
		historyclanpoint = 0;
		weekclanpoint = 0;
		preweekclanpoint = 0;
		oldhistoryclanpoint = 0;
		oldfreezedclanpoint = 0;
	}

	public RoleClanPoint() {
		this(0, null, null);
	}

	public RoleClanPoint(RoleClanPoint _o_) {
		this(_o_, null, null);
	}

	RoleClanPoint(xbean.RoleClanPoint _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleClanPoint) assign((RoleClanPoint)_o1_);
		else if (_o1_ instanceof RoleClanPoint.Data) assign((RoleClanPoint.Data)_o1_);
		else if (_o1_ instanceof RoleClanPoint.Const) assign(((RoleClanPoint.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleClanPoint _o_) {
		_o_._xdb_verify_unsafe_();
		freezedclanpoint = _o_.freezedclanpoint;
		currentclanpoint = _o_.currentclanpoint;
		historyclanpoint = _o_.historyclanpoint;
		weekclanpoint = _o_.weekclanpoint;
		preweekclanpoint = _o_.preweekclanpoint;
		oldhistoryclanpoint = _o_.oldhistoryclanpoint;
		oldfreezedclanpoint = _o_.oldfreezedclanpoint;
	}

	private void assign(RoleClanPoint.Data _o_) {
		freezedclanpoint = _o_.freezedclanpoint;
		currentclanpoint = _o_.currentclanpoint;
		historyclanpoint = _o_.historyclanpoint;
		weekclanpoint = _o_.weekclanpoint;
		preweekclanpoint = _o_.preweekclanpoint;
		oldhistoryclanpoint = _o_.oldhistoryclanpoint;
		oldfreezedclanpoint = _o_.oldfreezedclanpoint;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(freezedclanpoint);
		_os_.marshal(currentclanpoint);
		_os_.marshal(historyclanpoint);
		_os_.marshal(weekclanpoint);
		_os_.marshal(preweekclanpoint);
		_os_.marshal(oldhistoryclanpoint);
		_os_.marshal(oldfreezedclanpoint);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		freezedclanpoint = _os_.unmarshal_int();
		currentclanpoint = _os_.unmarshal_int();
		historyclanpoint = _os_.unmarshal_int();
		weekclanpoint = _os_.unmarshal_int();
		preweekclanpoint = _os_.unmarshal_int();
		oldhistoryclanpoint = _os_.unmarshal_int();
		oldfreezedclanpoint = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RoleClanPoint copy() {
		_xdb_verify_unsafe_();
		return new RoleClanPoint(this);
	}

	@Override
	public xbean.RoleClanPoint toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleClanPoint toBean() {
		_xdb_verify_unsafe_();
		return new RoleClanPoint(this); // same as copy()
	}

	@Override
	public xbean.RoleClanPoint toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleClanPoint toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getFreezedclanpoint() { // 已冻结公会贡献度
		_xdb_verify_unsafe_();
		return freezedclanpoint;
	}

	@Override
	public int getCurrentclanpoint() { // 当前公会贡献度
		_xdb_verify_unsafe_();
		return currentclanpoint;
	}

	@Override
	public int getHistoryclanpoint() { // 历史公会贡献度
		_xdb_verify_unsafe_();
		return historyclanpoint;
	}

	@Override
	public int getWeekclanpoint() { // 周公会贡献度by hzl
		_xdb_verify_unsafe_();
		return weekclanpoint;
	}

	@Override
	public int getPreweekclanpoint() { // 上周周公会贡献度by hzl
		_xdb_verify_unsafe_();
		return preweekclanpoint;
	}

	@Override
	public int getOldhistoryclanpoint() { // 旧的历史公会贡献度
		_xdb_verify_unsafe_();
		return oldhistoryclanpoint;
	}

	@Override
	public int getOldfreezedclanpoint() { // 上次退出公会时候冻结公会贡献度
		_xdb_verify_unsafe_();
		return oldfreezedclanpoint;
	}

	@Override
	public void setFreezedclanpoint(int _v_) { // 已冻结公会贡献度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "freezedclanpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, freezedclanpoint) {
					public void rollback() { freezedclanpoint = _xdb_saved; }
				};}});
		freezedclanpoint = _v_;
	}

	@Override
	public void setCurrentclanpoint(int _v_) { // 当前公会贡献度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currentclanpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currentclanpoint) {
					public void rollback() { currentclanpoint = _xdb_saved; }
				};}});
		currentclanpoint = _v_;
	}

	@Override
	public void setHistoryclanpoint(int _v_) { // 历史公会贡献度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "historyclanpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, historyclanpoint) {
					public void rollback() { historyclanpoint = _xdb_saved; }
				};}});
		historyclanpoint = _v_;
	}

	@Override
	public void setWeekclanpoint(int _v_) { // 周公会贡献度by hzl
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "weekclanpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, weekclanpoint) {
					public void rollback() { weekclanpoint = _xdb_saved; }
				};}});
		weekclanpoint = _v_;
	}

	@Override
	public void setPreweekclanpoint(int _v_) { // 上周周公会贡献度by hzl
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "preweekclanpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, preweekclanpoint) {
					public void rollback() { preweekclanpoint = _xdb_saved; }
				};}});
		preweekclanpoint = _v_;
	}

	@Override
	public void setOldhistoryclanpoint(int _v_) { // 旧的历史公会贡献度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "oldhistoryclanpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, oldhistoryclanpoint) {
					public void rollback() { oldhistoryclanpoint = _xdb_saved; }
				};}});
		oldhistoryclanpoint = _v_;
	}

	@Override
	public void setOldfreezedclanpoint(int _v_) { // 上次退出公会时候冻结公会贡献度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "oldfreezedclanpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, oldfreezedclanpoint) {
					public void rollback() { oldfreezedclanpoint = _xdb_saved; }
				};}});
		oldfreezedclanpoint = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleClanPoint _o_ = null;
		if ( _o1_ instanceof RoleClanPoint ) _o_ = (RoleClanPoint)_o1_;
		else if ( _o1_ instanceof RoleClanPoint.Const ) _o_ = ((RoleClanPoint.Const)_o1_).nThis();
		else return false;
		if (freezedclanpoint != _o_.freezedclanpoint) return false;
		if (currentclanpoint != _o_.currentclanpoint) return false;
		if (historyclanpoint != _o_.historyclanpoint) return false;
		if (weekclanpoint != _o_.weekclanpoint) return false;
		if (preweekclanpoint != _o_.preweekclanpoint) return false;
		if (oldhistoryclanpoint != _o_.oldhistoryclanpoint) return false;
		if (oldfreezedclanpoint != _o_.oldfreezedclanpoint) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += freezedclanpoint;
		_h_ += currentclanpoint;
		_h_ += historyclanpoint;
		_h_ += weekclanpoint;
		_h_ += preweekclanpoint;
		_h_ += oldhistoryclanpoint;
		_h_ += oldfreezedclanpoint;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(freezedclanpoint);
		_sb_.append(",");
		_sb_.append(currentclanpoint);
		_sb_.append(",");
		_sb_.append(historyclanpoint);
		_sb_.append(",");
		_sb_.append(weekclanpoint);
		_sb_.append(",");
		_sb_.append(preweekclanpoint);
		_sb_.append(",");
		_sb_.append(oldhistoryclanpoint);
		_sb_.append(",");
		_sb_.append(oldfreezedclanpoint);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("freezedclanpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currentclanpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("historyclanpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("weekclanpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("preweekclanpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("oldhistoryclanpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("oldfreezedclanpoint"));
		return lb;
	}

	private class Const implements xbean.RoleClanPoint {
		RoleClanPoint nThis() {
			return RoleClanPoint.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleClanPoint copy() {
			return RoleClanPoint.this.copy();
		}

		@Override
		public xbean.RoleClanPoint toData() {
			return RoleClanPoint.this.toData();
		}

		public xbean.RoleClanPoint toBean() {
			return RoleClanPoint.this.toBean();
		}

		@Override
		public xbean.RoleClanPoint toDataIf() {
			return RoleClanPoint.this.toDataIf();
		}

		public xbean.RoleClanPoint toBeanIf() {
			return RoleClanPoint.this.toBeanIf();
		}

		@Override
		public int getFreezedclanpoint() { // 已冻结公会贡献度
			_xdb_verify_unsafe_();
			return freezedclanpoint;
		}

		@Override
		public int getCurrentclanpoint() { // 当前公会贡献度
			_xdb_verify_unsafe_();
			return currentclanpoint;
		}

		@Override
		public int getHistoryclanpoint() { // 历史公会贡献度
			_xdb_verify_unsafe_();
			return historyclanpoint;
		}

		@Override
		public int getWeekclanpoint() { // 周公会贡献度by hzl
			_xdb_verify_unsafe_();
			return weekclanpoint;
		}

		@Override
		public int getPreweekclanpoint() { // 上周周公会贡献度by hzl
			_xdb_verify_unsafe_();
			return preweekclanpoint;
		}

		@Override
		public int getOldhistoryclanpoint() { // 旧的历史公会贡献度
			_xdb_verify_unsafe_();
			return oldhistoryclanpoint;
		}

		@Override
		public int getOldfreezedclanpoint() { // 上次退出公会时候冻结公会贡献度
			_xdb_verify_unsafe_();
			return oldfreezedclanpoint;
		}

		@Override
		public void setFreezedclanpoint(int _v_) { // 已冻结公会贡献度
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrentclanpoint(int _v_) { // 当前公会贡献度
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHistoryclanpoint(int _v_) { // 历史公会贡献度
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWeekclanpoint(int _v_) { // 周公会贡献度by hzl
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPreweekclanpoint(int _v_) { // 上周周公会贡献度by hzl
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOldhistoryclanpoint(int _v_) { // 旧的历史公会贡献度
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOldfreezedclanpoint(int _v_) { // 上次退出公会时候冻结公会贡献度
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
			return RoleClanPoint.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleClanPoint.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleClanPoint.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleClanPoint.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleClanPoint.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleClanPoint.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleClanPoint.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleClanPoint.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleClanPoint.this.toString();
		}

	}

	public static final class Data implements xbean.RoleClanPoint {
		private int freezedclanpoint; // 已冻结公会贡献度
		private int currentclanpoint; // 当前公会贡献度
		private int historyclanpoint; // 历史公会贡献度
		private int weekclanpoint; // 周公会贡献度by hzl
		private int preweekclanpoint; // 上周周公会贡献度by hzl
		private int oldhistoryclanpoint; // 旧的历史公会贡献度
		private int oldfreezedclanpoint; // 上次退出公会时候冻结公会贡献度

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			freezedclanpoint = 0;
			currentclanpoint = 0;
			historyclanpoint = 0;
			weekclanpoint = 0;
			preweekclanpoint = 0;
			oldhistoryclanpoint = 0;
			oldfreezedclanpoint = 0;
		}

		Data(xbean.RoleClanPoint _o1_) {
			if (_o1_ instanceof RoleClanPoint) assign((RoleClanPoint)_o1_);
			else if (_o1_ instanceof RoleClanPoint.Data) assign((RoleClanPoint.Data)_o1_);
			else if (_o1_ instanceof RoleClanPoint.Const) assign(((RoleClanPoint.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleClanPoint _o_) {
			freezedclanpoint = _o_.freezedclanpoint;
			currentclanpoint = _o_.currentclanpoint;
			historyclanpoint = _o_.historyclanpoint;
			weekclanpoint = _o_.weekclanpoint;
			preweekclanpoint = _o_.preweekclanpoint;
			oldhistoryclanpoint = _o_.oldhistoryclanpoint;
			oldfreezedclanpoint = _o_.oldfreezedclanpoint;
		}

		private void assign(RoleClanPoint.Data _o_) {
			freezedclanpoint = _o_.freezedclanpoint;
			currentclanpoint = _o_.currentclanpoint;
			historyclanpoint = _o_.historyclanpoint;
			weekclanpoint = _o_.weekclanpoint;
			preweekclanpoint = _o_.preweekclanpoint;
			oldhistoryclanpoint = _o_.oldhistoryclanpoint;
			oldfreezedclanpoint = _o_.oldfreezedclanpoint;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(freezedclanpoint);
			_os_.marshal(currentclanpoint);
			_os_.marshal(historyclanpoint);
			_os_.marshal(weekclanpoint);
			_os_.marshal(preweekclanpoint);
			_os_.marshal(oldhistoryclanpoint);
			_os_.marshal(oldfreezedclanpoint);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			freezedclanpoint = _os_.unmarshal_int();
			currentclanpoint = _os_.unmarshal_int();
			historyclanpoint = _os_.unmarshal_int();
			weekclanpoint = _os_.unmarshal_int();
			preweekclanpoint = _os_.unmarshal_int();
			oldhistoryclanpoint = _os_.unmarshal_int();
			oldfreezedclanpoint = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RoleClanPoint copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleClanPoint toData() {
			return new Data(this);
		}

		public xbean.RoleClanPoint toBean() {
			return new RoleClanPoint(this, null, null);
		}

		@Override
		public xbean.RoleClanPoint toDataIf() {
			return this;
		}

		public xbean.RoleClanPoint toBeanIf() {
			return new RoleClanPoint(this, null, null);
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
		public int getFreezedclanpoint() { // 已冻结公会贡献度
			return freezedclanpoint;
		}

		@Override
		public int getCurrentclanpoint() { // 当前公会贡献度
			return currentclanpoint;
		}

		@Override
		public int getHistoryclanpoint() { // 历史公会贡献度
			return historyclanpoint;
		}

		@Override
		public int getWeekclanpoint() { // 周公会贡献度by hzl
			return weekclanpoint;
		}

		@Override
		public int getPreweekclanpoint() { // 上周周公会贡献度by hzl
			return preweekclanpoint;
		}

		@Override
		public int getOldhistoryclanpoint() { // 旧的历史公会贡献度
			return oldhistoryclanpoint;
		}

		@Override
		public int getOldfreezedclanpoint() { // 上次退出公会时候冻结公会贡献度
			return oldfreezedclanpoint;
		}

		@Override
		public void setFreezedclanpoint(int _v_) { // 已冻结公会贡献度
			freezedclanpoint = _v_;
		}

		@Override
		public void setCurrentclanpoint(int _v_) { // 当前公会贡献度
			currentclanpoint = _v_;
		}

		@Override
		public void setHistoryclanpoint(int _v_) { // 历史公会贡献度
			historyclanpoint = _v_;
		}

		@Override
		public void setWeekclanpoint(int _v_) { // 周公会贡献度by hzl
			weekclanpoint = _v_;
		}

		@Override
		public void setPreweekclanpoint(int _v_) { // 上周周公会贡献度by hzl
			preweekclanpoint = _v_;
		}

		@Override
		public void setOldhistoryclanpoint(int _v_) { // 旧的历史公会贡献度
			oldhistoryclanpoint = _v_;
		}

		@Override
		public void setOldfreezedclanpoint(int _v_) { // 上次退出公会时候冻结公会贡献度
			oldfreezedclanpoint = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleClanPoint.Data)) return false;
			RoleClanPoint.Data _o_ = (RoleClanPoint.Data) _o1_;
			if (freezedclanpoint != _o_.freezedclanpoint) return false;
			if (currentclanpoint != _o_.currentclanpoint) return false;
			if (historyclanpoint != _o_.historyclanpoint) return false;
			if (weekclanpoint != _o_.weekclanpoint) return false;
			if (preweekclanpoint != _o_.preweekclanpoint) return false;
			if (oldhistoryclanpoint != _o_.oldhistoryclanpoint) return false;
			if (oldfreezedclanpoint != _o_.oldfreezedclanpoint) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += freezedclanpoint;
			_h_ += currentclanpoint;
			_h_ += historyclanpoint;
			_h_ += weekclanpoint;
			_h_ += preweekclanpoint;
			_h_ += oldhistoryclanpoint;
			_h_ += oldfreezedclanpoint;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(freezedclanpoint);
			_sb_.append(",");
			_sb_.append(currentclanpoint);
			_sb_.append(",");
			_sb_.append(historyclanpoint);
			_sb_.append(",");
			_sb_.append(weekclanpoint);
			_sb_.append(",");
			_sb_.append(preweekclanpoint);
			_sb_.append(",");
			_sb_.append(oldhistoryclanpoint);
			_sb_.append(",");
			_sb_.append(oldfreezedclanpoint);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
