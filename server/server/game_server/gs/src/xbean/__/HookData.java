
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class HookData extends mkdb.XBean implements xbean.HookData {
	private short cangetdpoint; // 可领取双倍点数
	private short getdpoint; // 已领取双倍点数
	private boolean isautobattle; // 是否自动战斗
	private short charoptype; // 人物操作类型
	private int charopid; // 人物操作id
	private short petoptype; // 宠物操作类型
	private int petopid; // 宠物操作id
	private long offlineexp; // 离线经验
	private long lastgettime; // 上次系统发放双倍点数时间

	@Override
	public void _reset_unsafe_() {
		cangetdpoint = 0;
		getdpoint = 0;
		isautobattle = false;
		charoptype = 0;
		charopid = 0;
		petoptype = 0;
		petopid = 0;
		offlineexp = 0L;
		lastgettime = 0L;
	}

	HookData(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public HookData() {
		this(0, null, null);
	}

	public HookData(HookData _o_) {
		this(_o_, null, null);
	}

	HookData(xbean.HookData _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof HookData) assign((HookData)_o1_);
		else if (_o1_ instanceof HookData.Data) assign((HookData.Data)_o1_);
		else if (_o1_ instanceof HookData.Const) assign(((HookData.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(HookData _o_) {
		_o_._xdb_verify_unsafe_();
		cangetdpoint = _o_.cangetdpoint;
		getdpoint = _o_.getdpoint;
		isautobattle = _o_.isautobattle;
		charoptype = _o_.charoptype;
		charopid = _o_.charopid;
		petoptype = _o_.petoptype;
		petopid = _o_.petopid;
		offlineexp = _o_.offlineexp;
		lastgettime = _o_.lastgettime;
	}

	private void assign(HookData.Data _o_) {
		cangetdpoint = _o_.cangetdpoint;
		getdpoint = _o_.getdpoint;
		isautobattle = _o_.isautobattle;
		charoptype = _o_.charoptype;
		charopid = _o_.charopid;
		petoptype = _o_.petoptype;
		petopid = _o_.petopid;
		offlineexp = _o_.offlineexp;
		lastgettime = _o_.lastgettime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(cangetdpoint);
		_os_.marshal(getdpoint);
		_os_.marshal(isautobattle);
		_os_.marshal(charoptype);
		_os_.marshal(charopid);
		_os_.marshal(petoptype);
		_os_.marshal(petopid);
		_os_.marshal(offlineexp);
		_os_.marshal(lastgettime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		cangetdpoint = _os_.unmarshal_short();
		getdpoint = _os_.unmarshal_short();
		isautobattle = _os_.unmarshal_boolean();
		charoptype = _os_.unmarshal_short();
		charopid = _os_.unmarshal_int();
		petoptype = _os_.unmarshal_short();
		petopid = _os_.unmarshal_int();
		offlineexp = _os_.unmarshal_long();
		lastgettime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.HookData copy() {
		_xdb_verify_unsafe_();
		return new HookData(this);
	}

	@Override
	public xbean.HookData toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HookData toBean() {
		_xdb_verify_unsafe_();
		return new HookData(this); // same as copy()
	}

	@Override
	public xbean.HookData toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HookData toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public short getCangetdpoint() { // 可领取双倍点数
		_xdb_verify_unsafe_();
		return cangetdpoint;
	}

	@Override
	public short getGetdpoint() { // 已领取双倍点数
		_xdb_verify_unsafe_();
		return getdpoint;
	}

	@Override
	public boolean getIsautobattle() { // 是否自动战斗
		_xdb_verify_unsafe_();
		return isautobattle;
	}

	@Override
	public short getCharoptype() { // 人物操作类型
		_xdb_verify_unsafe_();
		return charoptype;
	}

	@Override
	public int getCharopid() { // 人物操作id
		_xdb_verify_unsafe_();
		return charopid;
	}

	@Override
	public short getPetoptype() { // 宠物操作类型
		_xdb_verify_unsafe_();
		return petoptype;
	}

	@Override
	public int getPetopid() { // 宠物操作id
		_xdb_verify_unsafe_();
		return petopid;
	}

	@Override
	public long getOfflineexp() { // 离线经验
		_xdb_verify_unsafe_();
		return offlineexp;
	}

	@Override
	public long getLastgettime() { // 上次系统发放双倍点数时间
		_xdb_verify_unsafe_();
		return lastgettime;
	}

	@Override
	public void setCangetdpoint(short _v_) { // 可领取双倍点数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "cangetdpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, cangetdpoint) {
					public void rollback() { cangetdpoint = _xdb_saved; }
				};}});
		cangetdpoint = _v_;
	}

	@Override
	public void setGetdpoint(short _v_) { // 已领取双倍点数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "getdpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, getdpoint) {
					public void rollback() { getdpoint = _xdb_saved; }
				};}});
		getdpoint = _v_;
	}

	@Override
	public void setIsautobattle(boolean _v_) { // 是否自动战斗
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isautobattle") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, isautobattle) {
					public void rollback() { isautobattle = _xdb_saved; }
				};}});
		isautobattle = _v_;
	}

	@Override
	public void setCharoptype(short _v_) { // 人物操作类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "charoptype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, charoptype) {
					public void rollback() { charoptype = _xdb_saved; }
				};}});
		charoptype = _v_;
	}

	@Override
	public void setCharopid(int _v_) { // 人物操作id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "charopid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, charopid) {
					public void rollback() { charopid = _xdb_saved; }
				};}});
		charopid = _v_;
	}

	@Override
	public void setPetoptype(short _v_) { // 宠物操作类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petoptype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, petoptype) {
					public void rollback() { petoptype = _xdb_saved; }
				};}});
		petoptype = _v_;
	}

	@Override
	public void setPetopid(int _v_) { // 宠物操作id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petopid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, petopid) {
					public void rollback() { petopid = _xdb_saved; }
				};}});
		petopid = _v_;
	}

	@Override
	public void setOfflineexp(long _v_) { // 离线经验
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "offlineexp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, offlineexp) {
					public void rollback() { offlineexp = _xdb_saved; }
				};}});
		offlineexp = _v_;
	}

	@Override
	public void setLastgettime(long _v_) { // 上次系统发放双倍点数时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastgettime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastgettime) {
					public void rollback() { lastgettime = _xdb_saved; }
				};}});
		lastgettime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		HookData _o_ = null;
		if ( _o1_ instanceof HookData ) _o_ = (HookData)_o1_;
		else if ( _o1_ instanceof HookData.Const ) _o_ = ((HookData.Const)_o1_).nThis();
		else return false;
		if (cangetdpoint != _o_.cangetdpoint) return false;
		if (getdpoint != _o_.getdpoint) return false;
		if (isautobattle != _o_.isautobattle) return false;
		if (charoptype != _o_.charoptype) return false;
		if (charopid != _o_.charopid) return false;
		if (petoptype != _o_.petoptype) return false;
		if (petopid != _o_.petopid) return false;
		if (offlineexp != _o_.offlineexp) return false;
		if (lastgettime != _o_.lastgettime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += cangetdpoint;
		_h_ += getdpoint;
		_h_ += isautobattle ? 1231 : 1237;
		_h_ += charoptype;
		_h_ += charopid;
		_h_ += petoptype;
		_h_ += petopid;
		_h_ += offlineexp;
		_h_ += lastgettime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(cangetdpoint);
		_sb_.append(",");
		_sb_.append(getdpoint);
		_sb_.append(",");
		_sb_.append(isautobattle);
		_sb_.append(",");
		_sb_.append(charoptype);
		_sb_.append(",");
		_sb_.append(charopid);
		_sb_.append(",");
		_sb_.append(petoptype);
		_sb_.append(",");
		_sb_.append(petopid);
		_sb_.append(",");
		_sb_.append(offlineexp);
		_sb_.append(",");
		_sb_.append(lastgettime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("cangetdpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("getdpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isautobattle"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("charoptype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("charopid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petoptype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petopid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("offlineexp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastgettime"));
		return lb;
	}

	private class Const implements xbean.HookData {
		HookData nThis() {
			return HookData.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.HookData copy() {
			return HookData.this.copy();
		}

		@Override
		public xbean.HookData toData() {
			return HookData.this.toData();
		}

		public xbean.HookData toBean() {
			return HookData.this.toBean();
		}

		@Override
		public xbean.HookData toDataIf() {
			return HookData.this.toDataIf();
		}

		public xbean.HookData toBeanIf() {
			return HookData.this.toBeanIf();
		}

		@Override
		public short getCangetdpoint() { // 可领取双倍点数
			_xdb_verify_unsafe_();
			return cangetdpoint;
		}

		@Override
		public short getGetdpoint() { // 已领取双倍点数
			_xdb_verify_unsafe_();
			return getdpoint;
		}

		@Override
		public boolean getIsautobattle() { // 是否自动战斗
			_xdb_verify_unsafe_();
			return isautobattle;
		}

		@Override
		public short getCharoptype() { // 人物操作类型
			_xdb_verify_unsafe_();
			return charoptype;
		}

		@Override
		public int getCharopid() { // 人物操作id
			_xdb_verify_unsafe_();
			return charopid;
		}

		@Override
		public short getPetoptype() { // 宠物操作类型
			_xdb_verify_unsafe_();
			return petoptype;
		}

		@Override
		public int getPetopid() { // 宠物操作id
			_xdb_verify_unsafe_();
			return petopid;
		}

		@Override
		public long getOfflineexp() { // 离线经验
			_xdb_verify_unsafe_();
			return offlineexp;
		}

		@Override
		public long getLastgettime() { // 上次系统发放双倍点数时间
			_xdb_verify_unsafe_();
			return lastgettime;
		}

		@Override
		public void setCangetdpoint(short _v_) { // 可领取双倍点数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGetdpoint(short _v_) { // 已领取双倍点数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIsautobattle(boolean _v_) { // 是否自动战斗
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCharoptype(short _v_) { // 人物操作类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCharopid(int _v_) { // 人物操作id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetoptype(short _v_) { // 宠物操作类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetopid(int _v_) { // 宠物操作id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOfflineexp(long _v_) { // 离线经验
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastgettime(long _v_) { // 上次系统发放双倍点数时间
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
			return HookData.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return HookData.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return HookData.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return HookData.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return HookData.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return HookData.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return HookData.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return HookData.this.hashCode();
		}

		@Override
		public String toString() {
			return HookData.this.toString();
		}

	}

	public static final class Data implements xbean.HookData {
		private short cangetdpoint; // 可领取双倍点数
		private short getdpoint; // 已领取双倍点数
		private boolean isautobattle; // 是否自动战斗
		private short charoptype; // 人物操作类型
		private int charopid; // 人物操作id
		private short petoptype; // 宠物操作类型
		private int petopid; // 宠物操作id
		private long offlineexp; // 离线经验
		private long lastgettime; // 上次系统发放双倍点数时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.HookData _o1_) {
			if (_o1_ instanceof HookData) assign((HookData)_o1_);
			else if (_o1_ instanceof HookData.Data) assign((HookData.Data)_o1_);
			else if (_o1_ instanceof HookData.Const) assign(((HookData.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(HookData _o_) {
			cangetdpoint = _o_.cangetdpoint;
			getdpoint = _o_.getdpoint;
			isautobattle = _o_.isautobattle;
			charoptype = _o_.charoptype;
			charopid = _o_.charopid;
			petoptype = _o_.petoptype;
			petopid = _o_.petopid;
			offlineexp = _o_.offlineexp;
			lastgettime = _o_.lastgettime;
		}

		private void assign(HookData.Data _o_) {
			cangetdpoint = _o_.cangetdpoint;
			getdpoint = _o_.getdpoint;
			isautobattle = _o_.isautobattle;
			charoptype = _o_.charoptype;
			charopid = _o_.charopid;
			petoptype = _o_.petoptype;
			petopid = _o_.petopid;
			offlineexp = _o_.offlineexp;
			lastgettime = _o_.lastgettime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(cangetdpoint);
			_os_.marshal(getdpoint);
			_os_.marshal(isautobattle);
			_os_.marshal(charoptype);
			_os_.marshal(charopid);
			_os_.marshal(petoptype);
			_os_.marshal(petopid);
			_os_.marshal(offlineexp);
			_os_.marshal(lastgettime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			cangetdpoint = _os_.unmarshal_short();
			getdpoint = _os_.unmarshal_short();
			isautobattle = _os_.unmarshal_boolean();
			charoptype = _os_.unmarshal_short();
			charopid = _os_.unmarshal_int();
			petoptype = _os_.unmarshal_short();
			petopid = _os_.unmarshal_int();
			offlineexp = _os_.unmarshal_long();
			lastgettime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.HookData copy() {
			return new Data(this);
		}

		@Override
		public xbean.HookData toData() {
			return new Data(this);
		}

		public xbean.HookData toBean() {
			return new HookData(this, null, null);
		}

		@Override
		public xbean.HookData toDataIf() {
			return this;
		}

		public xbean.HookData toBeanIf() {
			return new HookData(this, null, null);
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
		public short getCangetdpoint() { // 可领取双倍点数
			return cangetdpoint;
		}

		@Override
		public short getGetdpoint() { // 已领取双倍点数
			return getdpoint;
		}

		@Override
		public boolean getIsautobattle() { // 是否自动战斗
			return isautobattle;
		}

		@Override
		public short getCharoptype() { // 人物操作类型
			return charoptype;
		}

		@Override
		public int getCharopid() { // 人物操作id
			return charopid;
		}

		@Override
		public short getPetoptype() { // 宠物操作类型
			return petoptype;
		}

		@Override
		public int getPetopid() { // 宠物操作id
			return petopid;
		}

		@Override
		public long getOfflineexp() { // 离线经验
			return offlineexp;
		}

		@Override
		public long getLastgettime() { // 上次系统发放双倍点数时间
			return lastgettime;
		}

		@Override
		public void setCangetdpoint(short _v_) { // 可领取双倍点数
			cangetdpoint = _v_;
		}

		@Override
		public void setGetdpoint(short _v_) { // 已领取双倍点数
			getdpoint = _v_;
		}

		@Override
		public void setIsautobattle(boolean _v_) { // 是否自动战斗
			isautobattle = _v_;
		}

		@Override
		public void setCharoptype(short _v_) { // 人物操作类型
			charoptype = _v_;
		}

		@Override
		public void setCharopid(int _v_) { // 人物操作id
			charopid = _v_;
		}

		@Override
		public void setPetoptype(short _v_) { // 宠物操作类型
			petoptype = _v_;
		}

		@Override
		public void setPetopid(int _v_) { // 宠物操作id
			petopid = _v_;
		}

		@Override
		public void setOfflineexp(long _v_) { // 离线经验
			offlineexp = _v_;
		}

		@Override
		public void setLastgettime(long _v_) { // 上次系统发放双倍点数时间
			lastgettime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof HookData.Data)) return false;
			HookData.Data _o_ = (HookData.Data) _o1_;
			if (cangetdpoint != _o_.cangetdpoint) return false;
			if (getdpoint != _o_.getdpoint) return false;
			if (isautobattle != _o_.isautobattle) return false;
			if (charoptype != _o_.charoptype) return false;
			if (charopid != _o_.charopid) return false;
			if (petoptype != _o_.petoptype) return false;
			if (petopid != _o_.petopid) return false;
			if (offlineexp != _o_.offlineexp) return false;
			if (lastgettime != _o_.lastgettime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += cangetdpoint;
			_h_ += getdpoint;
			_h_ += isautobattle ? 1231 : 1237;
			_h_ += charoptype;
			_h_ += charopid;
			_h_ += petoptype;
			_h_ += petopid;
			_h_ += offlineexp;
			_h_ += lastgettime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(cangetdpoint);
			_sb_.append(",");
			_sb_.append(getdpoint);
			_sb_.append(",");
			_sb_.append(isautobattle);
			_sb_.append(",");
			_sb_.append(charoptype);
			_sb_.append(",");
			_sb_.append(charopid);
			_sb_.append(",");
			_sb_.append(petoptype);
			_sb_.append(",");
			_sb_.append(petopid);
			_sb_.append(",");
			_sb_.append(offlineexp);
			_sb_.append(",");
			_sb_.append(lastgettime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
