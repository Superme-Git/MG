
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CrossTempRole extends mkdb.XBean implements xbean.CrossTempRole {
	private int zoneid; // 
	private long lastcopytime; // 
	private short copyresult; // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
	private java.util.HashMap<String, Boolean> finishedtable; // 已经完成拷贝的数据表.这个字段在跨服服务器用到
	private boolean crosstatus; // 跨服状态
	private int flag; // 0=战队赛

	@Override
	public void _reset_unsafe_() {
		zoneid = 0;
		lastcopytime = 0;
		copyresult = 0;
		finishedtable.clear();
		crosstatus = false;
		flag = 0;
	}

	CrossTempRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		lastcopytime = 0;
		finishedtable = new java.util.HashMap<String, Boolean>();
	}

	public CrossTempRole() {
		this(0, null, null);
	}

	public CrossTempRole(CrossTempRole _o_) {
		this(_o_, null, null);
	}

	CrossTempRole(xbean.CrossTempRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CrossTempRole) assign((CrossTempRole)_o1_);
		else if (_o1_ instanceof CrossTempRole.Data) assign((CrossTempRole.Data)_o1_);
		else if (_o1_ instanceof CrossTempRole.Const) assign(((CrossTempRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CrossTempRole _o_) {
		_o_._xdb_verify_unsafe_();
		zoneid = _o_.zoneid;
		lastcopytime = _o_.lastcopytime;
		copyresult = _o_.copyresult;
		finishedtable = new java.util.HashMap<String, Boolean>();
		for (java.util.Map.Entry<String, Boolean> _e_ : _o_.finishedtable.entrySet())
			finishedtable.put(_e_.getKey(), _e_.getValue());
		crosstatus = _o_.crosstatus;
		flag = _o_.flag;
	}

	private void assign(CrossTempRole.Data _o_) {
		zoneid = _o_.zoneid;
		lastcopytime = _o_.lastcopytime;
		copyresult = _o_.copyresult;
		finishedtable = new java.util.HashMap<String, Boolean>();
		for (java.util.Map.Entry<String, Boolean> _e_ : _o_.finishedtable.entrySet())
			finishedtable.put(_e_.getKey(), _e_.getValue());
		crosstatus = _o_.crosstatus;
		flag = _o_.flag;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(zoneid);
		_os_.marshal(lastcopytime);
		_os_.marshal(copyresult);
		_os_.compact_uint32(finishedtable.size());
		for (java.util.Map.Entry<String, Boolean> _e_ : finishedtable.entrySet())
		{
			_os_.marshal(_e_.getKey(), mkdb.Const.IO_CHARSET);
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(crosstatus);
		_os_.marshal(flag);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		zoneid = _os_.unmarshal_int();
		lastcopytime = _os_.unmarshal_long();
		copyresult = _os_.unmarshal_short();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				finishedtable = new java.util.HashMap<String, Boolean>(size * 2);
			}
			for (; size > 0; --size)
			{
				String _k_ = "";
				_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				boolean _v_ = false;
				_v_ = _os_.unmarshal_boolean();
				finishedtable.put(_k_, _v_);
			}
		}
		crosstatus = _os_.unmarshal_boolean();
		flag = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.CrossTempRole copy() {
		_xdb_verify_unsafe_();
		return new CrossTempRole(this);
	}

	@Override
	public xbean.CrossTempRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CrossTempRole toBean() {
		_xdb_verify_unsafe_();
		return new CrossTempRole(this); // same as copy()
	}

	@Override
	public xbean.CrossTempRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CrossTempRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getZoneid() { // 
		_xdb_verify_unsafe_();
		return zoneid;
	}

	@Override
	public long getLastcopytime() { // 
		_xdb_verify_unsafe_();
		return lastcopytime;
	}

	@Override
	public short getCopyresult() { // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
		_xdb_verify_unsafe_();
		return copyresult;
	}

	@Override
	public java.util.Map<String, Boolean> getFinishedtable() { // 已经完成拷贝的数据表.这个字段在跨服服务器用到
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "finishedtable"), finishedtable);
	}

	@Override
	public java.util.Map<String, Boolean> getFinishedtableAsData() { // 已经完成拷贝的数据表.这个字段在跨服服务器用到
		_xdb_verify_unsafe_();
		java.util.Map<String, Boolean> finishedtable;
		CrossTempRole _o_ = this;
		finishedtable = new java.util.HashMap<String, Boolean>();
		for (java.util.Map.Entry<String, Boolean> _e_ : _o_.finishedtable.entrySet())
			finishedtable.put(_e_.getKey(), _e_.getValue());
		return finishedtable;
	}

	@Override
	public boolean getCrosstatus() { // 跨服状态
		_xdb_verify_unsafe_();
		return crosstatus;
	}

	@Override
	public int getFlag() { // 0=战队赛
		_xdb_verify_unsafe_();
		return flag;
	}

	@Override
	public void setZoneid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "zoneid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, zoneid) {
					public void rollback() { zoneid = _xdb_saved; }
				};}});
		zoneid = _v_;
	}

	@Override
	public void setLastcopytime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastcopytime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastcopytime) {
					public void rollback() { lastcopytime = _xdb_saved; }
				};}});
		lastcopytime = _v_;
	}

	@Override
	public void setCopyresult(short _v_) { // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "copyresult") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, copyresult) {
					public void rollback() { copyresult = _xdb_saved; }
				};}});
		copyresult = _v_;
	}

	@Override
	public void setCrosstatus(boolean _v_) { // 跨服状态
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "crosstatus") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, crosstatus) {
					public void rollback() { crosstatus = _xdb_saved; }
				};}});
		crosstatus = _v_;
	}

	@Override
	public void setFlag(int _v_) { // 0=战队赛
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "flag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, flag) {
					public void rollback() { flag = _xdb_saved; }
				};}});
		flag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CrossTempRole _o_ = null;
		if ( _o1_ instanceof CrossTempRole ) _o_ = (CrossTempRole)_o1_;
		else if ( _o1_ instanceof CrossTempRole.Const ) _o_ = ((CrossTempRole.Const)_o1_).nThis();
		else return false;
		if (zoneid != _o_.zoneid) return false;
		if (lastcopytime != _o_.lastcopytime) return false;
		if (copyresult != _o_.copyresult) return false;
		if (!finishedtable.equals(_o_.finishedtable)) return false;
		if (crosstatus != _o_.crosstatus) return false;
		if (flag != _o_.flag) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += zoneid;
		_h_ += lastcopytime;
		_h_ += copyresult;
		_h_ += finishedtable.hashCode();
		_h_ += crosstatus ? 1231 : 1237;
		_h_ += flag;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(zoneid);
		_sb_.append(",");
		_sb_.append(lastcopytime);
		_sb_.append(",");
		_sb_.append(copyresult);
		_sb_.append(",");
		_sb_.append(finishedtable);
		_sb_.append(",");
		_sb_.append(crosstatus);
		_sb_.append(",");
		_sb_.append(flag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("zoneid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastcopytime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("copyresult"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("finishedtable"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("crosstatus"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("flag"));
		return lb;
	}

	private class Const implements xbean.CrossTempRole {
		CrossTempRole nThis() {
			return CrossTempRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CrossTempRole copy() {
			return CrossTempRole.this.copy();
		}

		@Override
		public xbean.CrossTempRole toData() {
			return CrossTempRole.this.toData();
		}

		public xbean.CrossTempRole toBean() {
			return CrossTempRole.this.toBean();
		}

		@Override
		public xbean.CrossTempRole toDataIf() {
			return CrossTempRole.this.toDataIf();
		}

		public xbean.CrossTempRole toBeanIf() {
			return CrossTempRole.this.toBeanIf();
		}

		@Override
		public int getZoneid() { // 
			_xdb_verify_unsafe_();
			return zoneid;
		}

		@Override
		public long getLastcopytime() { // 
			_xdb_verify_unsafe_();
			return lastcopytime;
		}

		@Override
		public short getCopyresult() { // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
			_xdb_verify_unsafe_();
			return copyresult;
		}

		@Override
		public java.util.Map<String, Boolean> getFinishedtable() { // 已经完成拷贝的数据表.这个字段在跨服服务器用到
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(finishedtable);
		}

		@Override
		public java.util.Map<String, Boolean> getFinishedtableAsData() { // 已经完成拷贝的数据表.这个字段在跨服服务器用到
			_xdb_verify_unsafe_();
			java.util.Map<String, Boolean> finishedtable;
			CrossTempRole _o_ = CrossTempRole.this;
			finishedtable = new java.util.HashMap<String, Boolean>();
			for (java.util.Map.Entry<String, Boolean> _e_ : _o_.finishedtable.entrySet())
				finishedtable.put(_e_.getKey(), _e_.getValue());
			return finishedtable;
		}

		@Override
		public boolean getCrosstatus() { // 跨服状态
			_xdb_verify_unsafe_();
			return crosstatus;
		}

		@Override
		public int getFlag() { // 0=战队赛
			_xdb_verify_unsafe_();
			return flag;
		}

		@Override
		public void setZoneid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastcopytime(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCopyresult(short _v_) { // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCrosstatus(boolean _v_) { // 跨服状态
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFlag(int _v_) { // 0=战队赛
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
			return CrossTempRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CrossTempRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CrossTempRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CrossTempRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CrossTempRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CrossTempRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CrossTempRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CrossTempRole.this.hashCode();
		}

		@Override
		public String toString() {
			return CrossTempRole.this.toString();
		}

	}

	public static final class Data implements xbean.CrossTempRole {
		private int zoneid; // 
		private long lastcopytime; // 
		private short copyresult; // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
		private java.util.HashMap<String, Boolean> finishedtable; // 已经完成拷贝的数据表.这个字段在跨服服务器用到
		private boolean crosstatus; // 跨服状态
		private int flag; // 0=战队赛

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			lastcopytime = 0;
			finishedtable = new java.util.HashMap<String, Boolean>();
		}

		Data(xbean.CrossTempRole _o1_) {
			if (_o1_ instanceof CrossTempRole) assign((CrossTempRole)_o1_);
			else if (_o1_ instanceof CrossTempRole.Data) assign((CrossTempRole.Data)_o1_);
			else if (_o1_ instanceof CrossTempRole.Const) assign(((CrossTempRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CrossTempRole _o_) {
			zoneid = _o_.zoneid;
			lastcopytime = _o_.lastcopytime;
			copyresult = _o_.copyresult;
			finishedtable = new java.util.HashMap<String, Boolean>();
			for (java.util.Map.Entry<String, Boolean> _e_ : _o_.finishedtable.entrySet())
				finishedtable.put(_e_.getKey(), _e_.getValue());
			crosstatus = _o_.crosstatus;
			flag = _o_.flag;
		}

		private void assign(CrossTempRole.Data _o_) {
			zoneid = _o_.zoneid;
			lastcopytime = _o_.lastcopytime;
			copyresult = _o_.copyresult;
			finishedtable = new java.util.HashMap<String, Boolean>();
			for (java.util.Map.Entry<String, Boolean> _e_ : _o_.finishedtable.entrySet())
				finishedtable.put(_e_.getKey(), _e_.getValue());
			crosstatus = _o_.crosstatus;
			flag = _o_.flag;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(zoneid);
			_os_.marshal(lastcopytime);
			_os_.marshal(copyresult);
			_os_.compact_uint32(finishedtable.size());
			for (java.util.Map.Entry<String, Boolean> _e_ : finishedtable.entrySet())
			{
				_os_.marshal(_e_.getKey(), mkdb.Const.IO_CHARSET);
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(crosstatus);
			_os_.marshal(flag);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			zoneid = _os_.unmarshal_int();
			lastcopytime = _os_.unmarshal_long();
			copyresult = _os_.unmarshal_short();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					finishedtable = new java.util.HashMap<String, Boolean>(size * 2);
				}
				for (; size > 0; --size)
				{
					String _k_ = "";
					_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
					boolean _v_ = false;
					_v_ = _os_.unmarshal_boolean();
					finishedtable.put(_k_, _v_);
				}
			}
			crosstatus = _os_.unmarshal_boolean();
			flag = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.CrossTempRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.CrossTempRole toData() {
			return new Data(this);
		}

		public xbean.CrossTempRole toBean() {
			return new CrossTempRole(this, null, null);
		}

		@Override
		public xbean.CrossTempRole toDataIf() {
			return this;
		}

		public xbean.CrossTempRole toBeanIf() {
			return new CrossTempRole(this, null, null);
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
		public int getZoneid() { // 
			return zoneid;
		}

		@Override
		public long getLastcopytime() { // 
			return lastcopytime;
		}

		@Override
		public short getCopyresult() { // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
			return copyresult;
		}

		@Override
		public java.util.Map<String, Boolean> getFinishedtable() { // 已经完成拷贝的数据表.这个字段在跨服服务器用到
			return finishedtable;
		}

		@Override
		public java.util.Map<String, Boolean> getFinishedtableAsData() { // 已经完成拷贝的数据表.这个字段在跨服服务器用到
			return finishedtable;
		}

		@Override
		public boolean getCrosstatus() { // 跨服状态
			return crosstatus;
		}

		@Override
		public int getFlag() { // 0=战队赛
			return flag;
		}

		@Override
		public void setZoneid(int _v_) { // 
			zoneid = _v_;
		}

		@Override
		public void setLastcopytime(long _v_) { // 
			lastcopytime = _v_;
		}

		@Override
		public void setCopyresult(short _v_) { // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
			copyresult = _v_;
		}

		@Override
		public void setCrosstatus(boolean _v_) { // 跨服状态
			crosstatus = _v_;
		}

		@Override
		public void setFlag(int _v_) { // 0=战队赛
			flag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CrossTempRole.Data)) return false;
			CrossTempRole.Data _o_ = (CrossTempRole.Data) _o1_;
			if (zoneid != _o_.zoneid) return false;
			if (lastcopytime != _o_.lastcopytime) return false;
			if (copyresult != _o_.copyresult) return false;
			if (!finishedtable.equals(_o_.finishedtable)) return false;
			if (crosstatus != _o_.crosstatus) return false;
			if (flag != _o_.flag) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += zoneid;
			_h_ += lastcopytime;
			_h_ += copyresult;
			_h_ += finishedtable.hashCode();
			_h_ += crosstatus ? 1231 : 1237;
			_h_ += flag;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(zoneid);
			_sb_.append(",");
			_sb_.append(lastcopytime);
			_sb_.append(",");
			_sb_.append(copyresult);
			_sb_.append(",");
			_sb_.append(finishedtable);
			_sb_.append(",");
			_sb_.append(crosstatus);
			_sb_.append(",");
			_sb_.append(flag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
