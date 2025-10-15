
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BingFengInfo extends mkdb.XBean implements xbean.BingFengInfo {
	private int instzoneid; // 
	private java.util.HashMap<Integer, Integer> stagetime; // 记录每个关卡最快通关的时间
	private java.util.HashMap<Integer, Integer> stageround; // 记录每个关卡最快通关的回合数
	private java.util.HashMap<Integer, Long> stagebest; // 记录每个关卡最快通关的角色的id
	private java.util.HashMap<Integer, xbean.SchoolStageTime> schoolstage; // key 职业id

	@Override
	public void _reset_unsafe_() {
		instzoneid = 0;
		stagetime.clear();
		stageround.clear();
		stagebest.clear();
		schoolstage.clear();
	}

	BingFengInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		instzoneid = 0;
		stagetime = new java.util.HashMap<Integer, Integer>();
		stageround = new java.util.HashMap<Integer, Integer>();
		stagebest = new java.util.HashMap<Integer, Long>();
		schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>();
	}

	public BingFengInfo() {
		this(0, null, null);
	}

	public BingFengInfo(BingFengInfo _o_) {
		this(_o_, null, null);
	}

	BingFengInfo(xbean.BingFengInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BingFengInfo) assign((BingFengInfo)_o1_);
		else if (_o1_ instanceof BingFengInfo.Data) assign((BingFengInfo.Data)_o1_);
		else if (_o1_ instanceof BingFengInfo.Const) assign(((BingFengInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BingFengInfo _o_) {
		_o_._xdb_verify_unsafe_();
		instzoneid = _o_.instzoneid;
		stagetime = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
			stagetime.put(_e_.getKey(), _e_.getValue());
		stageround = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
			stageround.put(_e_.getKey(), _e_.getValue());
		stagebest = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
			stagebest.put(_e_.getKey(), _e_.getValue());
		schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>();
		for (java.util.Map.Entry<Integer, xbean.SchoolStageTime> _e_ : _o_.schoolstage.entrySet())
			schoolstage.put(_e_.getKey(), new SchoolStageTime(_e_.getValue(), this, "schoolstage"));
	}

	private void assign(BingFengInfo.Data _o_) {
		instzoneid = _o_.instzoneid;
		stagetime = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
			stagetime.put(_e_.getKey(), _e_.getValue());
		stageround = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
			stageround.put(_e_.getKey(), _e_.getValue());
		stagebest = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
			stagebest.put(_e_.getKey(), _e_.getValue());
		schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>();
		for (java.util.Map.Entry<Integer, xbean.SchoolStageTime> _e_ : _o_.schoolstage.entrySet())
			schoolstage.put(_e_.getKey(), new SchoolStageTime(_e_.getValue(), this, "schoolstage"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(instzoneid);
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
		_os_.compact_uint32(schoolstage.size());
		for (java.util.Map.Entry<Integer, xbean.SchoolStageTime> _e_ : schoolstage.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		instzoneid = _os_.unmarshal_int();
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
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.SchoolStageTime _v_ = new SchoolStageTime(0, this, "schoolstage");
				_v_.unmarshal(_os_);
				schoolstage.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.BingFengInfo copy() {
		_xdb_verify_unsafe_();
		return new BingFengInfo(this);
	}

	@Override
	public xbean.BingFengInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengInfo toBean() {
		_xdb_verify_unsafe_();
		return new BingFengInfo(this); // same as copy()
	}

	@Override
	public xbean.BingFengInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getInstzoneid() { // 
		_xdb_verify_unsafe_();
		return instzoneid;
	}

	@Override
	public java.util.Map<Integer, Integer> getStagetime() { // 记录每个关卡最快通关的时间
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "stagetime"), stagetime);
	}

	@Override
	public java.util.Map<Integer, Integer> getStagetimeAsData() { // 记录每个关卡最快通关的时间
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> stagetime;
		BingFengInfo _o_ = this;
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
		BingFengInfo _o_ = this;
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
		BingFengInfo _o_ = this;
		stagebest = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
			stagebest.put(_e_.getKey(), _e_.getValue());
		return stagebest;
	}

	@Override
	public java.util.Map<Integer, xbean.SchoolStageTime> getSchoolstage() { // key 职业id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "schoolstage"), schoolstage);
	}

	@Override
	public java.util.Map<Integer, xbean.SchoolStageTime> getSchoolstageAsData() { // key 职业id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.SchoolStageTime> schoolstage;
		BingFengInfo _o_ = this;
		schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>();
		for (java.util.Map.Entry<Integer, xbean.SchoolStageTime> _e_ : _o_.schoolstage.entrySet())
			schoolstage.put(_e_.getKey(), new SchoolStageTime.Data(_e_.getValue()));
		return schoolstage;
	}

	@Override
	public void setInstzoneid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "instzoneid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, instzoneid) {
					public void rollback() { instzoneid = _xdb_saved; }
				};}});
		instzoneid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BingFengInfo _o_ = null;
		if ( _o1_ instanceof BingFengInfo ) _o_ = (BingFengInfo)_o1_;
		else if ( _o1_ instanceof BingFengInfo.Const ) _o_ = ((BingFengInfo.Const)_o1_).nThis();
		else return false;
		if (instzoneid != _o_.instzoneid) return false;
		if (!stagetime.equals(_o_.stagetime)) return false;
		if (!stageround.equals(_o_.stageround)) return false;
		if (!stagebest.equals(_o_.stagebest)) return false;
		if (!schoolstage.equals(_o_.schoolstage)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += instzoneid;
		_h_ += stagetime.hashCode();
		_h_ += stageround.hashCode();
		_h_ += stagebest.hashCode();
		_h_ += schoolstage.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(instzoneid);
		_sb_.append(",");
		_sb_.append(stagetime);
		_sb_.append(",");
		_sb_.append(stageround);
		_sb_.append(",");
		_sb_.append(stagebest);
		_sb_.append(",");
		_sb_.append(schoolstage);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instzoneid"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("stagetime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("stageround"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("stagebest"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("schoolstage"));
		return lb;
	}

	private class Const implements xbean.BingFengInfo {
		BingFengInfo nThis() {
			return BingFengInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BingFengInfo copy() {
			return BingFengInfo.this.copy();
		}

		@Override
		public xbean.BingFengInfo toData() {
			return BingFengInfo.this.toData();
		}

		public xbean.BingFengInfo toBean() {
			return BingFengInfo.this.toBean();
		}

		@Override
		public xbean.BingFengInfo toDataIf() {
			return BingFengInfo.this.toDataIf();
		}

		public xbean.BingFengInfo toBeanIf() {
			return BingFengInfo.this.toBeanIf();
		}

		@Override
		public int getInstzoneid() { // 
			_xdb_verify_unsafe_();
			return instzoneid;
		}

		@Override
		public java.util.Map<Integer, Integer> getStagetime() { // 记录每个关卡最快通关的时间
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(stagetime);
		}

		@Override
		public java.util.Map<Integer, Integer> getStagetimeAsData() { // 记录每个关卡最快通关的时间
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> stagetime;
			BingFengInfo _o_ = BingFengInfo.this;
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
			BingFengInfo _o_ = BingFengInfo.this;
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
			BingFengInfo _o_ = BingFengInfo.this;
			stagebest = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
				stagebest.put(_e_.getKey(), _e_.getValue());
			return stagebest;
		}

		@Override
		public java.util.Map<Integer, xbean.SchoolStageTime> getSchoolstage() { // key 职业id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(schoolstage);
		}

		@Override
		public java.util.Map<Integer, xbean.SchoolStageTime> getSchoolstageAsData() { // key 职业id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.SchoolStageTime> schoolstage;
			BingFengInfo _o_ = BingFengInfo.this;
			schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>();
			for (java.util.Map.Entry<Integer, xbean.SchoolStageTime> _e_ : _o_.schoolstage.entrySet())
				schoolstage.put(_e_.getKey(), new SchoolStageTime.Data(_e_.getValue()));
			return schoolstage;
		}

		@Override
		public void setInstzoneid(int _v_) { // 
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
			return BingFengInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BingFengInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BingFengInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BingFengInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BingFengInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BingFengInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BingFengInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BingFengInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return BingFengInfo.this.toString();
		}

	}

	public static final class Data implements xbean.BingFengInfo {
		private int instzoneid; // 
		private java.util.HashMap<Integer, Integer> stagetime; // 记录每个关卡最快通关的时间
		private java.util.HashMap<Integer, Integer> stageround; // 记录每个关卡最快通关的回合数
		private java.util.HashMap<Integer, Long> stagebest; // 记录每个关卡最快通关的角色的id
		private java.util.HashMap<Integer, xbean.SchoolStageTime> schoolstage; // key 职业id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			instzoneid = 0;
			stagetime = new java.util.HashMap<Integer, Integer>();
			stageround = new java.util.HashMap<Integer, Integer>();
			stagebest = new java.util.HashMap<Integer, Long>();
			schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>();
		}

		Data(xbean.BingFengInfo _o1_) {
			if (_o1_ instanceof BingFengInfo) assign((BingFengInfo)_o1_);
			else if (_o1_ instanceof BingFengInfo.Data) assign((BingFengInfo.Data)_o1_);
			else if (_o1_ instanceof BingFengInfo.Const) assign(((BingFengInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BingFengInfo _o_) {
			instzoneid = _o_.instzoneid;
			stagetime = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
				stagetime.put(_e_.getKey(), _e_.getValue());
			stageround = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
				stageround.put(_e_.getKey(), _e_.getValue());
			stagebest = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
				stagebest.put(_e_.getKey(), _e_.getValue());
			schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>();
			for (java.util.Map.Entry<Integer, xbean.SchoolStageTime> _e_ : _o_.schoolstage.entrySet())
				schoolstage.put(_e_.getKey(), new SchoolStageTime.Data(_e_.getValue()));
		}

		private void assign(BingFengInfo.Data _o_) {
			instzoneid = _o_.instzoneid;
			stagetime = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagetime.entrySet())
				stagetime.put(_e_.getKey(), _e_.getValue());
			stageround = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
				stageround.put(_e_.getKey(), _e_.getValue());
			stagebest = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagebest.entrySet())
				stagebest.put(_e_.getKey(), _e_.getValue());
			schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>();
			for (java.util.Map.Entry<Integer, xbean.SchoolStageTime> _e_ : _o_.schoolstage.entrySet())
				schoolstage.put(_e_.getKey(), new SchoolStageTime.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(instzoneid);
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
			_os_.compact_uint32(schoolstage.size());
			for (java.util.Map.Entry<Integer, xbean.SchoolStageTime> _e_ : schoolstage.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			instzoneid = _os_.unmarshal_int();
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
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					schoolstage = new java.util.HashMap<Integer, xbean.SchoolStageTime>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.SchoolStageTime _v_ = xbean.Pod.newSchoolStageTimeData();
					_v_.unmarshal(_os_);
					schoolstage.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.BingFengInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.BingFengInfo toData() {
			return new Data(this);
		}

		public xbean.BingFengInfo toBean() {
			return new BingFengInfo(this, null, null);
		}

		@Override
		public xbean.BingFengInfo toDataIf() {
			return this;
		}

		public xbean.BingFengInfo toBeanIf() {
			return new BingFengInfo(this, null, null);
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
		public int getInstzoneid() { // 
			return instzoneid;
		}

		@Override
		public java.util.Map<Integer, Integer> getStagetime() { // 记录每个关卡最快通关的时间
			return stagetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getStagetimeAsData() { // 记录每个关卡最快通关的时间
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
		public java.util.Map<Integer, xbean.SchoolStageTime> getSchoolstage() { // key 职业id
			return schoolstage;
		}

		@Override
		public java.util.Map<Integer, xbean.SchoolStageTime> getSchoolstageAsData() { // key 职业id
			return schoolstage;
		}

		@Override
		public void setInstzoneid(int _v_) { // 
			instzoneid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BingFengInfo.Data)) return false;
			BingFengInfo.Data _o_ = (BingFengInfo.Data) _o1_;
			if (instzoneid != _o_.instzoneid) return false;
			if (!stagetime.equals(_o_.stagetime)) return false;
			if (!stageround.equals(_o_.stageround)) return false;
			if (!stagebest.equals(_o_.stagebest)) return false;
			if (!schoolstage.equals(_o_.schoolstage)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += instzoneid;
			_h_ += stagetime.hashCode();
			_h_ += stageround.hashCode();
			_h_ += stagebest.hashCode();
			_h_ += schoolstage.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(instzoneid);
			_sb_.append(",");
			_sb_.append(stagetime);
			_sb_.append(",");
			_sb_.append(stageround);
			_sb_.append(",");
			_sb_.append(stagebest);
			_sb_.append(",");
			_sb_.append(schoolstage);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
