
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SchoolStageTime extends mkdb.XBean implements xbean.SchoolStageTime {
	private java.util.HashMap<Integer, Integer> stagetime; // 按职业区分的每关时间
	private java.util.HashMap<Integer, Integer> stageround; // 记录每个关卡最快通关的回合数
	private java.util.HashMap<Integer, Long> stagebest; // 记录每个关卡最快通关的角色的id

	@Override
	public void _reset_unsafe_() {
		stagetime.clear();
		stageround.clear();
		stagebest.clear();
	}

	SchoolStageTime(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		stagetime = new java.util.HashMap<Integer, Integer>();
		stageround = new java.util.HashMap<Integer, Integer>();
		stagebest = new java.util.HashMap<Integer, Long>();
	}

	public SchoolStageTime() {
		this(0, null, null);
	}

	public SchoolStageTime(SchoolStageTime _o_) {
		this(_o_, null, null);
	}

	SchoolStageTime(xbean.SchoolStageTime _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SchoolStageTime) assign((SchoolStageTime)_o1_);
		else if (_o1_ instanceof SchoolStageTime.Data) assign((SchoolStageTime.Data)_o1_);
		else if (_o1_ instanceof SchoolStageTime.Const) assign(((SchoolStageTime.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SchoolStageTime _o_) {
		_o_._xdb_verify_unsafe_();
		stagetime = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
			stagetime.put(_e_.getKey(), _e_.getValue());
		stageround = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
			stageround.put(_e_.getKey(), _e_.getValue());
		stagebest = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
			stagebest.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(SchoolStageTime.Data _o_) {
		stagetime = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
			stagetime.put(_e_.getKey(), _e_.getValue());
		stageround = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
			stageround.put(_e_.getKey(), _e_.getValue());
		stagebest = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
			stagebest.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(stagetime.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : stagetime.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(stageround.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : stageround.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(stagebest.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : stagebest.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				stagetime = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				stagetime.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				stageround = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				stageround.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				stagebest = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				stagebest.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.SchoolStageTime copy() {
		_xdb_verify_unsafe_();
		return new SchoolStageTime(this);
	}

	@Override
	public xbean.SchoolStageTime toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SchoolStageTime toBean() {
		_xdb_verify_unsafe_();
		return new SchoolStageTime(this); // same as copy()
	}

	@Override
	public xbean.SchoolStageTime toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SchoolStageTime toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getStagetime() { // 按职业区分的每关时间
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "stagetime"), stagetime);
	}

	@Override
	public java.util.Map<Integer, Integer> getStagetimeAsData() { // 按职业区分的每关时间
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> stagetime;
		SchoolStageTime _o_ = this;
		stagetime = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
			stagetime.put(_e_.getKey(), _e_.getValue());
		return stagetime;
	}

	@Override
	public java.util.Map<Integer, Integer> getStageround() { // 记录每个关卡最快通关的回合数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "stageround"), stageround);
	}

	@Override
	public java.util.Map<Integer, Integer> getStageroundAsData() { // 记录每个关卡最快通关的回合数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> stageround;
		SchoolStageTime _o_ = this;
		stageround = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
			stageround.put(_e_.getKey(), _e_.getValue());
		return stageround;
	}

	@Override
	public java.util.Map<Integer, Long> getStagebest() { // 记录每个关卡最快通关的角色的id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "stagebest"), stagebest);
	}

	@Override
	public java.util.Map<Integer, Long> getStagebestAsData() { // 记录每个关卡最快通关的角色的id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> stagebest;
		SchoolStageTime _o_ = this;
		stagebest = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
			stagebest.put(_e_.getKey(), _e_.getValue());
		return stagebest;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SchoolStageTime _o_ = null;
		if ( _o1_ instanceof SchoolStageTime ) _o_ = (SchoolStageTime)_o1_;
		else if ( _o1_ instanceof SchoolStageTime.Const ) _o_ = ((SchoolStageTime.Const)_o1_).nThis();
		else return false;
		if (!stagetime.equals(_o_.stagetime)) return false;
		if (!stageround.equals(_o_.stageround)) return false;
		if (!stagebest.equals(_o_.stagebest)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += stagetime.hashCode();
		_h_ += stageround.hashCode();
		_h_ += stagebest.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(stagetime);
		_sb_.append(",");
		_sb_.append(stageround);
		_sb_.append(",");
		_sb_.append(stagebest);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("stagetime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("stageround"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("stagebest"));
		return lb;
	}

	private class Const implements xbean.SchoolStageTime {
		SchoolStageTime nThis() {
			return SchoolStageTime.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SchoolStageTime copy() {
			return SchoolStageTime.this.copy();
		}

		@Override
		public xbean.SchoolStageTime toData() {
			return SchoolStageTime.this.toData();
		}

		public xbean.SchoolStageTime toBean() {
			return SchoolStageTime.this.toBean();
		}

		@Override
		public xbean.SchoolStageTime toDataIf() {
			return SchoolStageTime.this.toDataIf();
		}

		public xbean.SchoolStageTime toBeanIf() {
			return SchoolStageTime.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getStagetime() { // 按职业区分的每关时间
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(stagetime);
		}

		@Override
		public java.util.Map<Integer, Integer> getStagetimeAsData() { // 按职业区分的每关时间
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> stagetime;
			SchoolStageTime _o_ = SchoolStageTime.this;
			stagetime = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
				stagetime.put(_e_.getKey(), _e_.getValue());
			return stagetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getStageround() { // 记录每个关卡最快通关的回合数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(stageround);
		}

		@Override
		public java.util.Map<Integer, Integer> getStageroundAsData() { // 记录每个关卡最快通关的回合数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> stageround;
			SchoolStageTime _o_ = SchoolStageTime.this;
			stageround = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
				stageround.put(_e_.getKey(), _e_.getValue());
			return stageround;
		}

		@Override
		public java.util.Map<Integer, Long> getStagebest() { // 记录每个关卡最快通关的角色的id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(stagebest);
		}

		@Override
		public java.util.Map<Integer, Long> getStagebestAsData() { // 记录每个关卡最快通关的角色的id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> stagebest;
			SchoolStageTime _o_ = SchoolStageTime.this;
			stagebest = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
				stagebest.put(_e_.getKey(), _e_.getValue());
			return stagebest;
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
			return SchoolStageTime.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SchoolStageTime.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SchoolStageTime.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SchoolStageTime.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SchoolStageTime.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SchoolStageTime.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SchoolStageTime.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SchoolStageTime.this.hashCode();
		}

		@Override
		public String toString() {
			return SchoolStageTime.this.toString();
		}

	}

	public static final class Data implements xbean.SchoolStageTime {
		private java.util.HashMap<Integer, Integer> stagetime; // 按职业区分的每关时间
		private java.util.HashMap<Integer, Integer> stageround; // 记录每个关卡最快通关的回合数
		private java.util.HashMap<Integer, Long> stagebest; // 记录每个关卡最快通关的角色的id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			stagetime = new java.util.HashMap<Integer, Integer>();
			stageround = new java.util.HashMap<Integer, Integer>();
			stagebest = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.SchoolStageTime _o1_) {
			if (_o1_ instanceof SchoolStageTime) assign((SchoolStageTime)_o1_);
			else if (_o1_ instanceof SchoolStageTime.Data) assign((SchoolStageTime.Data)_o1_);
			else if (_o1_ instanceof SchoolStageTime.Const) assign(((SchoolStageTime.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SchoolStageTime _o_) {
			stagetime = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
				stagetime.put(_e_.getKey(), _e_.getValue());
			stageround = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
				stageround.put(_e_.getKey(), _e_.getValue());
			stagebest = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
				stagebest.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(SchoolStageTime.Data _o_) {
			stagetime = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
				stagetime.put(_e_.getKey(), _e_.getValue());
			stageround = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
				stageround.put(_e_.getKey(), _e_.getValue());
			stagebest = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
				stagebest.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(stagetime.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : stagetime.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(stageround.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : stageround.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(stagebest.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : stagebest.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					stagetime = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					stagetime.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					stageround = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					stageround.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					stagebest = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					stagebest.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.SchoolStageTime copy() {
			return new Data(this);
		}

		@Override
		public xbean.SchoolStageTime toData() {
			return new Data(this);
		}

		public xbean.SchoolStageTime toBean() {
			return new SchoolStageTime(this, null, null);
		}

		@Override
		public xbean.SchoolStageTime toDataIf() {
			return this;
		}

		public xbean.SchoolStageTime toBeanIf() {
			return new SchoolStageTime(this, null, null);
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
		public java.util.Map<Integer, Integer> getStagetime() { // 按职业区分的每关时间
			return stagetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getStagetimeAsData() { // 按职业区分的每关时间
			return stagetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getStageround() { // 记录每个关卡最快通关的回合数
			return stageround;
		}

		@Override
		public java.util.Map<Integer, Integer> getStageroundAsData() { // 记录每个关卡最快通关的回合数
			return stageround;
		}

		@Override
		public java.util.Map<Integer, Long> getStagebest() { // 记录每个关卡最快通关的角色的id
			return stagebest;
		}

		@Override
		public java.util.Map<Integer, Long> getStagebestAsData() { // 记录每个关卡最快通关的角色的id
			return stagebest;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SchoolStageTime.Data)) return false;
			SchoolStageTime.Data _o_ = (SchoolStageTime.Data) _o1_;
			if (!stagetime.equals(_o_.stagetime)) return false;
			if (!stageround.equals(_o_.stageround)) return false;
			if (!stagebest.equals(_o_.stagebest)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += stagetime.hashCode();
			_h_ += stageround.hashCode();
			_h_ += stagebest.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(stagetime);
			_sb_.append(",");
			_sb_.append(stageround);
			_sb_.append(",");
			_sb_.append(stagebest);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
