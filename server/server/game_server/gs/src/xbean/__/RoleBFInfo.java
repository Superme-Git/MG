
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleBFInfo extends mkdb.XBean implements xbean.RoleBFInfo {
	private int instzoneid; // 
	private int losetimes; // 
	private int receivelastaward; // 
	private int lastrank; // 
	private int stage; // 
	private int lastenterstage; // 
	private java.util.HashMap<Integer, Long> stagetime; // 
	private java.util.HashMap<Integer, Integer> stageround; // 每关的所用的回合数
	private int yesterstage; // 
	private long lastchangetime; // 
	private java.util.HashMap<Integer, Integer> stagefanpai; // 记录每个关卡翻牌的次数
	private int autogo; // 是否自动寻路

	@Override
	public void _reset_unsafe_() {
		instzoneid = 0;
		losetimes = 0;
		receivelastaward = 0;
		lastrank = 0;
		stage = 0;
		lastenterstage = 0;
		stagetime.clear();
		stageround.clear();
		yesterstage = 0;
		lastchangetime = 0;
		stagefanpai.clear();
		autogo = 0;
	}

	RoleBFInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		instzoneid = 0;
		losetimes = 0;
		receivelastaward = 0;
		lastrank = 0;
		stage = 0;
		lastenterstage = 0;
		stagetime = new java.util.HashMap<Integer, Long>();
		stageround = new java.util.HashMap<Integer, Integer>();
		yesterstage = 0;
		lastchangetime = 0;
		stagefanpai = new java.util.HashMap<Integer, Integer>();
		autogo = 0;
	}

	public RoleBFInfo() {
		this(0, null, null);
	}

	public RoleBFInfo(RoleBFInfo _o_) {
		this(_o_, null, null);
	}

	RoleBFInfo(xbean.RoleBFInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleBFInfo) assign((RoleBFInfo)_o1_);
		else if (_o1_ instanceof RoleBFInfo.Data) assign((RoleBFInfo.Data)_o1_);
		else if (_o1_ instanceof RoleBFInfo.Const) assign(((RoleBFInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleBFInfo _o_) {
		_o_._xdb_verify_unsafe_();
		instzoneid = _o_.instzoneid;
		losetimes = _o_.losetimes;
		receivelastaward = _o_.receivelastaward;
		lastrank = _o_.lastrank;
		stage = _o_.stage;
		lastenterstage = _o_.lastenterstage;
		stagetime = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagetime.entrySet())
			stagetime.put(_e_.getKey(), _e_.getValue());
		stageround = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
			stageround.put(_e_.getKey(), _e_.getValue());
		yesterstage = _o_.yesterstage;
		lastchangetime = _o_.lastchangetime;
		stagefanpai = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagefanpai.entrySet())
			stagefanpai.put(_e_.getKey(), _e_.getValue());
		autogo = _o_.autogo;
	}

	private void assign(RoleBFInfo.Data _o_) {
		instzoneid = _o_.instzoneid;
		losetimes = _o_.losetimes;
		receivelastaward = _o_.receivelastaward;
		lastrank = _o_.lastrank;
		stage = _o_.stage;
		lastenterstage = _o_.lastenterstage;
		stagetime = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagetime.entrySet())
			stagetime.put(_e_.getKey(), _e_.getValue());
		stageround = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
			stageround.put(_e_.getKey(), _e_.getValue());
		yesterstage = _o_.yesterstage;
		lastchangetime = _o_.lastchangetime;
		stagefanpai = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagefanpai.entrySet())
			stagefanpai.put(_e_.getKey(), _e_.getValue());
		autogo = _o_.autogo;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(instzoneid);
		_os_.marshal(losetimes);
		_os_.marshal(receivelastaward);
		_os_.marshal(lastrank);
		_os_.marshal(stage);
		_os_.marshal(lastenterstage);
		_os_.compact_uint32(stagetime.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : stagetime.entrySet())
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
		_os_.marshal(yesterstage);
		_os_.marshal(lastchangetime);
		_os_.compact_uint32(stagefanpai.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : stagefanpai.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(autogo);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		instzoneid = _os_.unmarshal_int();
		losetimes = _os_.unmarshal_int();
		receivelastaward = _os_.unmarshal_int();
		lastrank = _os_.unmarshal_int();
		stage = _os_.unmarshal_int();
		lastenterstage = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				stagetime = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
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
		yesterstage = _os_.unmarshal_int();
		lastchangetime = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				stagefanpai = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				stagefanpai.put(_k_, _v_);
			}
		}
		autogo = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RoleBFInfo copy() {
		_xdb_verify_unsafe_();
		return new RoleBFInfo(this);
	}

	@Override
	public xbean.RoleBFInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleBFInfo toBean() {
		_xdb_verify_unsafe_();
		return new RoleBFInfo(this); // same as copy()
	}

	@Override
	public xbean.RoleBFInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleBFInfo toBeanIf() {
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
	public int getLosetimes() { // 
		_xdb_verify_unsafe_();
		return losetimes;
	}

	@Override
	public int getReceivelastaward() { // 
		_xdb_verify_unsafe_();
		return receivelastaward;
	}

	@Override
	public int getLastrank() { // 
		_xdb_verify_unsafe_();
		return lastrank;
	}

	@Override
	public int getStage() { // 
		_xdb_verify_unsafe_();
		return stage;
	}

	@Override
	public int getLastenterstage() { // 
		_xdb_verify_unsafe_();
		return lastenterstage;
	}

	@Override
	public java.util.Map<Integer, Long> getStagetime() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "stagetime"), stagetime);
	}

	@Override
	public java.util.Map<Integer, Long> getStagetimeAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> stagetime;
		RoleBFInfo _o_ = this;
		stagetime = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagetime.entrySet())
			stagetime.put(_e_.getKey(), _e_.getValue());
		return stagetime;
	}

	@Override
	public java.util.Map<Integer, Integer> getStageround() { // 每关的所用的回合数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "stageround"), stageround);
	}

	@Override
	public java.util.Map<Integer, Integer> getStageroundAsData() { // 每关的所用的回合数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> stageround;
		RoleBFInfo _o_ = this;
		stageround = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
			stageround.put(_e_.getKey(), _e_.getValue());
		return stageround;
	}

	@Override
	public int getYesterstage() { // 
		_xdb_verify_unsafe_();
		return yesterstage;
	}

	@Override
	public long getLastchangetime() { // 
		_xdb_verify_unsafe_();
		return lastchangetime;
	}

	@Override
	public java.util.Map<Integer, Integer> getStagefanpai() { // 记录每个关卡翻牌的次数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "stagefanpai"), stagefanpai);
	}

	@Override
	public java.util.Map<Integer, Integer> getStagefanpaiAsData() { // 记录每个关卡翻牌的次数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> stagefanpai;
		RoleBFInfo _o_ = this;
		stagefanpai = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagefanpai.entrySet())
			stagefanpai.put(_e_.getKey(), _e_.getValue());
		return stagefanpai;
	}

	@Override
	public int getAutogo() { // 是否自动寻路
		_xdb_verify_unsafe_();
		return autogo;
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
	public void setLosetimes(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "losetimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, losetimes) {
					public void rollback() { losetimes = _xdb_saved; }
				};}});
		losetimes = _v_;
	}

	@Override
	public void setReceivelastaward(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "receivelastaward") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, receivelastaward) {
					public void rollback() { receivelastaward = _xdb_saved; }
				};}});
		receivelastaward = _v_;
	}

	@Override
	public void setLastrank(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastrank") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lastrank) {
					public void rollback() { lastrank = _xdb_saved; }
				};}});
		lastrank = _v_;
	}

	@Override
	public void setStage(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "stage") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, stage) {
					public void rollback() { stage = _xdb_saved; }
				};}});
		stage = _v_;
	}

	@Override
	public void setLastenterstage(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastenterstage") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lastenterstage) {
					public void rollback() { lastenterstage = _xdb_saved; }
				};}});
		lastenterstage = _v_;
	}

	@Override
	public void setYesterstage(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "yesterstage") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, yesterstage) {
					public void rollback() { yesterstage = _xdb_saved; }
				};}});
		yesterstage = _v_;
	}

	@Override
	public void setLastchangetime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastchangetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastchangetime) {
					public void rollback() { lastchangetime = _xdb_saved; }
				};}});
		lastchangetime = _v_;
	}

	@Override
	public void setAutogo(int _v_) { // 是否自动寻路
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "autogo") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, autogo) {
					public void rollback() { autogo = _xdb_saved; }
				};}});
		autogo = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleBFInfo _o_ = null;
		if ( _o1_ instanceof RoleBFInfo ) _o_ = (RoleBFInfo)_o1_;
		else if ( _o1_ instanceof RoleBFInfo.Const ) _o_ = ((RoleBFInfo.Const)_o1_).nThis();
		else return false;
		if (instzoneid != _o_.instzoneid) return false;
		if (losetimes != _o_.losetimes) return false;
		if (receivelastaward != _o_.receivelastaward) return false;
		if (lastrank != _o_.lastrank) return false;
		if (stage != _o_.stage) return false;
		if (lastenterstage != _o_.lastenterstage) return false;
		if (!stagetime.equals(_o_.stagetime)) return false;
		if (!stageround.equals(_o_.stageround)) return false;
		if (yesterstage != _o_.yesterstage) return false;
		if (lastchangetime != _o_.lastchangetime) return false;
		if (!stagefanpai.equals(_o_.stagefanpai)) return false;
		if (autogo != _o_.autogo) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += instzoneid;
		_h_ += losetimes;
		_h_ += receivelastaward;
		_h_ += lastrank;
		_h_ += stage;
		_h_ += lastenterstage;
		_h_ += stagetime.hashCode();
		_h_ += stageround.hashCode();
		_h_ += yesterstage;
		_h_ += lastchangetime;
		_h_ += stagefanpai.hashCode();
		_h_ += autogo;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(instzoneid);
		_sb_.append(",");
		_sb_.append(losetimes);
		_sb_.append(",");
		_sb_.append(receivelastaward);
		_sb_.append(",");
		_sb_.append(lastrank);
		_sb_.append(",");
		_sb_.append(stage);
		_sb_.append(",");
		_sb_.append(lastenterstage);
		_sb_.append(",");
		_sb_.append(stagetime);
		_sb_.append(",");
		_sb_.append(stageround);
		_sb_.append(",");
		_sb_.append(yesterstage);
		_sb_.append(",");
		_sb_.append(lastchangetime);
		_sb_.append(",");
		_sb_.append(stagefanpai);
		_sb_.append(",");
		_sb_.append(autogo);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instzoneid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("losetimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receivelastaward"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastrank"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("stage"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastenterstage"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("stagetime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("stageround"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("yesterstage"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastchangetime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("stagefanpai"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("autogo"));
		return lb;
	}

	private class Const implements xbean.RoleBFInfo {
		RoleBFInfo nThis() {
			return RoleBFInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleBFInfo copy() {
			return RoleBFInfo.this.copy();
		}

		@Override
		public xbean.RoleBFInfo toData() {
			return RoleBFInfo.this.toData();
		}

		public xbean.RoleBFInfo toBean() {
			return RoleBFInfo.this.toBean();
		}

		@Override
		public xbean.RoleBFInfo toDataIf() {
			return RoleBFInfo.this.toDataIf();
		}

		public xbean.RoleBFInfo toBeanIf() {
			return RoleBFInfo.this.toBeanIf();
		}

		@Override
		public int getInstzoneid() { // 
			_xdb_verify_unsafe_();
			return instzoneid;
		}

		@Override
		public int getLosetimes() { // 
			_xdb_verify_unsafe_();
			return losetimes;
		}

		@Override
		public int getReceivelastaward() { // 
			_xdb_verify_unsafe_();
			return receivelastaward;
		}

		@Override
		public int getLastrank() { // 
			_xdb_verify_unsafe_();
			return lastrank;
		}

		@Override
		public int getStage() { // 
			_xdb_verify_unsafe_();
			return stage;
		}

		@Override
		public int getLastenterstage() { // 
			_xdb_verify_unsafe_();
			return lastenterstage;
		}

		@Override
		public java.util.Map<Integer, Long> getStagetime() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(stagetime);
		}

		@Override
		public java.util.Map<Integer, Long> getStagetimeAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> stagetime;
			RoleBFInfo _o_ = RoleBFInfo.this;
			stagetime = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagetime.entrySet())
				stagetime.put(_e_.getKey(), _e_.getValue());
			return stagetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getStageround() { // 每关的所用的回合数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(stageround);
		}

		@Override
		public java.util.Map<Integer, Integer> getStageroundAsData() { // 每关的所用的回合数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> stageround;
			RoleBFInfo _o_ = RoleBFInfo.this;
			stageround = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
				stageround.put(_e_.getKey(), _e_.getValue());
			return stageround;
		}

		@Override
		public int getYesterstage() { // 
			_xdb_verify_unsafe_();
			return yesterstage;
		}

		@Override
		public long getLastchangetime() { // 
			_xdb_verify_unsafe_();
			return lastchangetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getStagefanpai() { // 记录每个关卡翻牌的次数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(stagefanpai);
		}

		@Override
		public java.util.Map<Integer, Integer> getStagefanpaiAsData() { // 记录每个关卡翻牌的次数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> stagefanpai;
			RoleBFInfo _o_ = RoleBFInfo.this;
			stagefanpai = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagefanpai.entrySet())
				stagefanpai.put(_e_.getKey(), _e_.getValue());
			return stagefanpai;
		}

		@Override
		public int getAutogo() { // 是否自动寻路
			_xdb_verify_unsafe_();
			return autogo;
		}

		@Override
		public void setInstzoneid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLosetimes(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReceivelastaward(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastrank(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStage(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastenterstage(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setYesterstage(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastchangetime(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAutogo(int _v_) { // 是否自动寻路
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
			return RoleBFInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleBFInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleBFInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleBFInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleBFInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleBFInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleBFInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleBFInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleBFInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RoleBFInfo {
		private int instzoneid; // 
		private int losetimes; // 
		private int receivelastaward; // 
		private int lastrank; // 
		private int stage; // 
		private int lastenterstage; // 
		private java.util.HashMap<Integer, Long> stagetime; // 
		private java.util.HashMap<Integer, Integer> stageround; // 每关的所用的回合数
		private int yesterstage; // 
		private long lastchangetime; // 
		private java.util.HashMap<Integer, Integer> stagefanpai; // 记录每个关卡翻牌的次数
		private int autogo; // 是否自动寻路

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			instzoneid = 0;
			losetimes = 0;
			receivelastaward = 0;
			lastrank = 0;
			stage = 0;
			lastenterstage = 0;
			stagetime = new java.util.HashMap<Integer, Long>();
			stageround = new java.util.HashMap<Integer, Integer>();
			yesterstage = 0;
			lastchangetime = 0;
			stagefanpai = new java.util.HashMap<Integer, Integer>();
			autogo = 0;
		}

		Data(xbean.RoleBFInfo _o1_) {
			if (_o1_ instanceof RoleBFInfo) assign((RoleBFInfo)_o1_);
			else if (_o1_ instanceof RoleBFInfo.Data) assign((RoleBFInfo.Data)_o1_);
			else if (_o1_ instanceof RoleBFInfo.Const) assign(((RoleBFInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleBFInfo _o_) {
			instzoneid = _o_.instzoneid;
			losetimes = _o_.losetimes;
			receivelastaward = _o_.receivelastaward;
			lastrank = _o_.lastrank;
			stage = _o_.stage;
			lastenterstage = _o_.lastenterstage;
			stagetime = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagetime.entrySet())
				stagetime.put(_e_.getKey(), _e_.getValue());
			stageround = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
				stageround.put(_e_.getKey(), _e_.getValue());
			yesterstage = _o_.yesterstage;
			lastchangetime = _o_.lastchangetime;
			stagefanpai = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagefanpai.entrySet())
				stagefanpai.put(_e_.getKey(), _e_.getValue());
			autogo = _o_.autogo;
		}

		private void assign(RoleBFInfo.Data _o_) {
			instzoneid = _o_.instzoneid;
			losetimes = _o_.losetimes;
			receivelastaward = _o_.receivelastaward;
			lastrank = _o_.lastrank;
			stage = _o_.stage;
			lastenterstage = _o_.lastenterstage;
			stagetime = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.stagetime.entrySet())
				stagetime.put(_e_.getKey(), _e_.getValue());
			stageround = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stageround.entrySet())
				stageround.put(_e_.getKey(), _e_.getValue());
			yesterstage = _o_.yesterstage;
			lastchangetime = _o_.lastchangetime;
			stagefanpai = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stagefanpai.entrySet())
				stagefanpai.put(_e_.getKey(), _e_.getValue());
			autogo = _o_.autogo;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(instzoneid);
			_os_.marshal(losetimes);
			_os_.marshal(receivelastaward);
			_os_.marshal(lastrank);
			_os_.marshal(stage);
			_os_.marshal(lastenterstage);
			_os_.compact_uint32(stagetime.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : stagetime.entrySet())
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
			_os_.marshal(yesterstage);
			_os_.marshal(lastchangetime);
			_os_.compact_uint32(stagefanpai.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : stagefanpai.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(autogo);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			instzoneid = _os_.unmarshal_int();
			losetimes = _os_.unmarshal_int();
			receivelastaward = _os_.unmarshal_int();
			lastrank = _os_.unmarshal_int();
			stage = _os_.unmarshal_int();
			lastenterstage = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					stagetime = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
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
			yesterstage = _os_.unmarshal_int();
			lastchangetime = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					stagefanpai = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					stagefanpai.put(_k_, _v_);
				}
			}
			autogo = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RoleBFInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleBFInfo toData() {
			return new Data(this);
		}

		public xbean.RoleBFInfo toBean() {
			return new RoleBFInfo(this, null, null);
		}

		@Override
		public xbean.RoleBFInfo toDataIf() {
			return this;
		}

		public xbean.RoleBFInfo toBeanIf() {
			return new RoleBFInfo(this, null, null);
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
		public int getLosetimes() { // 
			return losetimes;
		}

		@Override
		public int getReceivelastaward() { // 
			return receivelastaward;
		}

		@Override
		public int getLastrank() { // 
			return lastrank;
		}

		@Override
		public int getStage() { // 
			return stage;
		}

		@Override
		public int getLastenterstage() { // 
			return lastenterstage;
		}

		@Override
		public java.util.Map<Integer, Long> getStagetime() { // 
			return stagetime;
		}

		@Override
		public java.util.Map<Integer, Long> getStagetimeAsData() { // 
			return stagetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getStageround() { // 每关的所用的回合数
			return stageround;
		}

		@Override
		public java.util.Map<Integer, Integer> getStageroundAsData() { // 每关的所用的回合数
			return stageround;
		}

		@Override
		public int getYesterstage() { // 
			return yesterstage;
		}

		@Override
		public long getLastchangetime() { // 
			return lastchangetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getStagefanpai() { // 记录每个关卡翻牌的次数
			return stagefanpai;
		}

		@Override
		public java.util.Map<Integer, Integer> getStagefanpaiAsData() { // 记录每个关卡翻牌的次数
			return stagefanpai;
		}

		@Override
		public int getAutogo() { // 是否自动寻路
			return autogo;
		}

		@Override
		public void setInstzoneid(int _v_) { // 
			instzoneid = _v_;
		}

		@Override
		public void setLosetimes(int _v_) { // 
			losetimes = _v_;
		}

		@Override
		public void setReceivelastaward(int _v_) { // 
			receivelastaward = _v_;
		}

		@Override
		public void setLastrank(int _v_) { // 
			lastrank = _v_;
		}

		@Override
		public void setStage(int _v_) { // 
			stage = _v_;
		}

		@Override
		public void setLastenterstage(int _v_) { // 
			lastenterstage = _v_;
		}

		@Override
		public void setYesterstage(int _v_) { // 
			yesterstage = _v_;
		}

		@Override
		public void setLastchangetime(long _v_) { // 
			lastchangetime = _v_;
		}

		@Override
		public void setAutogo(int _v_) { // 是否自动寻路
			autogo = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleBFInfo.Data)) return false;
			RoleBFInfo.Data _o_ = (RoleBFInfo.Data) _o1_;
			if (instzoneid != _o_.instzoneid) return false;
			if (losetimes != _o_.losetimes) return false;
			if (receivelastaward != _o_.receivelastaward) return false;
			if (lastrank != _o_.lastrank) return false;
			if (stage != _o_.stage) return false;
			if (lastenterstage != _o_.lastenterstage) return false;
			if (!stagetime.equals(_o_.stagetime)) return false;
			if (!stageround.equals(_o_.stageround)) return false;
			if (yesterstage != _o_.yesterstage) return false;
			if (lastchangetime != _o_.lastchangetime) return false;
			if (!stagefanpai.equals(_o_.stagefanpai)) return false;
			if (autogo != _o_.autogo) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += instzoneid;
			_h_ += losetimes;
			_h_ += receivelastaward;
			_h_ += lastrank;
			_h_ += stage;
			_h_ += lastenterstage;
			_h_ += stagetime.hashCode();
			_h_ += stageround.hashCode();
			_h_ += yesterstage;
			_h_ += lastchangetime;
			_h_ += stagefanpai.hashCode();
			_h_ += autogo;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(instzoneid);
			_sb_.append(",");
			_sb_.append(losetimes);
			_sb_.append(",");
			_sb_.append(receivelastaward);
			_sb_.append(",");
			_sb_.append(lastrank);
			_sb_.append(",");
			_sb_.append(stage);
			_sb_.append(",");
			_sb_.append(lastenterstage);
			_sb_.append(",");
			_sb_.append(stagetime);
			_sb_.append(",");
			_sb_.append(stageround);
			_sb_.append(",");
			_sb_.append(yesterstage);
			_sb_.append(",");
			_sb_.append(lastchangetime);
			_sb_.append(",");
			_sb_.append(stagefanpai);
			_sb_.append(",");
			_sb_.append(autogo);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
