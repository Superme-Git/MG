
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanInstances extends mkdb.XBean implements xbean.ClanInstances {
	private long clanid; // 公会id
	private long currentzonekey; // 当前激活的公会副本key，0为该公会没有激活公会副本
	private java.util.HashMap<Integer, Long> instkey; // 公会副本,key副本id; value为对应的副本key.
	private java.util.HashMap<Integer, Integer> inststate; // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
	private java.util.HashMap<Integer, Integer> inststep; // 公会副本进行的副本id对应的层数
	private java.util.HashMap<Integer, xbean.ClanSaveInfo> saveinfo; // key副本id
	private java.util.HashMap<Integer, Long> instreset; // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
	private java.util.HashMap<Integer, xbean.ClanBossInfo> bossinfo; // key:battleconfigId value:bossinfo

	@Override
	public void _reset_unsafe_() {
		clanid = 0;
		currentzonekey = 0;
		instkey.clear();
		inststate.clear();
		inststep.clear();
		saveinfo.clear();
		instreset.clear();
		bossinfo.clear();
	}

	ClanInstances(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		clanid = 0;
		currentzonekey = 0;
		instkey = new java.util.HashMap<Integer, Long>();
		inststate = new java.util.HashMap<Integer, Integer>();
		inststep = new java.util.HashMap<Integer, Integer>();
		saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>();
		instreset = new java.util.HashMap<Integer, Long>();
		bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>();
	}

	public ClanInstances() {
		this(0, null, null);
	}

	public ClanInstances(ClanInstances _o_) {
		this(_o_, null, null);
	}

	ClanInstances(xbean.ClanInstances _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanInstances) assign((ClanInstances)_o1_);
		else if (_o1_ instanceof ClanInstances.Data) assign((ClanInstances.Data)_o1_);
		else if (_o1_ instanceof ClanInstances.Const) assign(((ClanInstances.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanInstances _o_) {
		_o_._xdb_verify_unsafe_();
		clanid = _o_.clanid;
		currentzonekey = _o_.currentzonekey;
		instkey = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instkey.entrySet())
			instkey.put(_e_.getKey(), _e_.getValue());
		inststate = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststate.entrySet())
			inststate.put(_e_.getKey(), _e_.getValue());
		inststep = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststep.entrySet())
			inststep.put(_e_.getKey(), _e_.getValue());
		saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>();
		for (java.util.Map.Entry<Integer, xbean.ClanSaveInfo> _e_ : _o_.saveinfo.entrySet())
			saveinfo.put(_e_.getKey(), new ClanSaveInfo(_e_.getValue(), this, "saveinfo"));
		instreset = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instreset.entrySet())
			instreset.put(_e_.getKey(), _e_.getValue());
		bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>();
		for (java.util.Map.Entry<Integer, xbean.ClanBossInfo> _e_ : _o_.bossinfo.entrySet())
			bossinfo.put(_e_.getKey(), new ClanBossInfo(_e_.getValue(), this, "bossinfo"));
	}

	private void assign(ClanInstances.Data _o_) {
		clanid = _o_.clanid;
		currentzonekey = _o_.currentzonekey;
		instkey = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instkey.entrySet())
			instkey.put(_e_.getKey(), _e_.getValue());
		inststate = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststate.entrySet())
			inststate.put(_e_.getKey(), _e_.getValue());
		inststep = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststep.entrySet())
			inststep.put(_e_.getKey(), _e_.getValue());
		saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>();
		for (java.util.Map.Entry<Integer, xbean.ClanSaveInfo> _e_ : _o_.saveinfo.entrySet())
			saveinfo.put(_e_.getKey(), new ClanSaveInfo(_e_.getValue(), this, "saveinfo"));
		instreset = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instreset.entrySet())
			instreset.put(_e_.getKey(), _e_.getValue());
		bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>();
		for (java.util.Map.Entry<Integer, xbean.ClanBossInfo> _e_ : _o_.bossinfo.entrySet())
			bossinfo.put(_e_.getKey(), new ClanBossInfo(_e_.getValue(), this, "bossinfo"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(clanid);
		_os_.marshal(currentzonekey);
		_os_.compact_uint32(instkey.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : instkey.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(inststate.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : inststate.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(inststep.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : inststep.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(saveinfo.size());
		for (java.util.Map.Entry<Integer, xbean.ClanSaveInfo> _e_ : saveinfo.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(instreset.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : instreset.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(bossinfo.size());
		for (java.util.Map.Entry<Integer, xbean.ClanBossInfo> _e_ : bossinfo.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		clanid = _os_.unmarshal_long();
		currentzonekey = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				instkey = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				instkey.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				inststate = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				inststate.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				inststep = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				inststep.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.ClanSaveInfo _v_ = new ClanSaveInfo(0, this, "saveinfo");
				_v_.unmarshal(_os_);
				saveinfo.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				instreset = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				instreset.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.ClanBossInfo _v_ = new ClanBossInfo(0, this, "bossinfo");
				_v_.unmarshal(_os_);
				bossinfo.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ClanInstances copy() {
		_xdb_verify_unsafe_();
		return new ClanInstances(this);
	}

	@Override
	public xbean.ClanInstances toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanInstances toBean() {
		_xdb_verify_unsafe_();
		return new ClanInstances(this); // same as copy()
	}

	@Override
	public xbean.ClanInstances toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanInstances toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getClanid() { // 公会id
		_xdb_verify_unsafe_();
		return clanid;
	}

	@Override
	public long getCurrentzonekey() { // 当前激活的公会副本key，0为该公会没有激活公会副本
		_xdb_verify_unsafe_();
		return currentzonekey;
	}

	@Override
	public java.util.Map<Integer, Long> getInstkey() { // 公会副本,key副本id; value为对应的副本key.
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "instkey"), instkey);
	}

	@Override
	public java.util.Map<Integer, Long> getInstkeyAsData() { // 公会副本,key副本id; value为对应的副本key.
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> instkey;
		ClanInstances _o_ = this;
		instkey = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instkey.entrySet())
			instkey.put(_e_.getKey(), _e_.getValue());
		return instkey;
	}

	@Override
	public java.util.Map<Integer, Integer> getInststate() { // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "inststate"), inststate);
	}

	@Override
	public java.util.Map<Integer, Integer> getInststateAsData() { // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> inststate;
		ClanInstances _o_ = this;
		inststate = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststate.entrySet())
			inststate.put(_e_.getKey(), _e_.getValue());
		return inststate;
	}

	@Override
	public java.util.Map<Integer, Integer> getInststep() { // 公会副本进行的副本id对应的层数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "inststep"), inststep);
	}

	@Override
	public java.util.Map<Integer, Integer> getInststepAsData() { // 公会副本进行的副本id对应的层数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> inststep;
		ClanInstances _o_ = this;
		inststep = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststep.entrySet())
			inststep.put(_e_.getKey(), _e_.getValue());
		return inststep;
	}

	@Override
	public java.util.Map<Integer, xbean.ClanSaveInfo> getSaveinfo() { // key副本id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "saveinfo"), saveinfo);
	}

	@Override
	public java.util.Map<Integer, xbean.ClanSaveInfo> getSaveinfoAsData() { // key副本id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.ClanSaveInfo> saveinfo;
		ClanInstances _o_ = this;
		saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>();
		for (java.util.Map.Entry<Integer, xbean.ClanSaveInfo> _e_ : _o_.saveinfo.entrySet())
			saveinfo.put(_e_.getKey(), new ClanSaveInfo.Data(_e_.getValue()));
		return saveinfo;
	}

	@Override
	public java.util.Map<Integer, Long> getInstreset() { // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "instreset"), instreset);
	}

	@Override
	public java.util.Map<Integer, Long> getInstresetAsData() { // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> instreset;
		ClanInstances _o_ = this;
		instreset = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instreset.entrySet())
			instreset.put(_e_.getKey(), _e_.getValue());
		return instreset;
	}

	@Override
	public java.util.Map<Integer, xbean.ClanBossInfo> getBossinfo() { // key:battleconfigId value:bossinfo
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "bossinfo"), bossinfo);
	}

	@Override
	public java.util.Map<Integer, xbean.ClanBossInfo> getBossinfoAsData() { // key:battleconfigId value:bossinfo
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.ClanBossInfo> bossinfo;
		ClanInstances _o_ = this;
		bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>();
		for (java.util.Map.Entry<Integer, xbean.ClanBossInfo> _e_ : _o_.bossinfo.entrySet())
			bossinfo.put(_e_.getKey(), new ClanBossInfo.Data(_e_.getValue()));
		return bossinfo;
	}

	@Override
	public void setClanid(long _v_) { // 公会id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clanid) {
					public void rollback() { clanid = _xdb_saved; }
				};}});
		clanid = _v_;
	}

	@Override
	public void setCurrentzonekey(long _v_) { // 当前激活的公会副本key，0为该公会没有激活公会副本
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currentzonekey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, currentzonekey) {
					public void rollback() { currentzonekey = _xdb_saved; }
				};}});
		currentzonekey = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanInstances _o_ = null;
		if ( _o1_ instanceof ClanInstances ) _o_ = (ClanInstances)_o1_;
		else if ( _o1_ instanceof ClanInstances.Const ) _o_ = ((ClanInstances.Const)_o1_).nThis();
		else return false;
		if (clanid != _o_.clanid) return false;
		if (currentzonekey != _o_.currentzonekey) return false;
		if (!instkey.equals(_o_.instkey)) return false;
		if (!inststate.equals(_o_.inststate)) return false;
		if (!inststep.equals(_o_.inststep)) return false;
		if (!saveinfo.equals(_o_.saveinfo)) return false;
		if (!instreset.equals(_o_.instreset)) return false;
		if (!bossinfo.equals(_o_.bossinfo)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += clanid;
		_h_ += currentzonekey;
		_h_ += instkey.hashCode();
		_h_ += inststate.hashCode();
		_h_ += inststep.hashCode();
		_h_ += saveinfo.hashCode();
		_h_ += instreset.hashCode();
		_h_ += bossinfo.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanid);
		_sb_.append(",");
		_sb_.append(currentzonekey);
		_sb_.append(",");
		_sb_.append(instkey);
		_sb_.append(",");
		_sb_.append(inststate);
		_sb_.append(",");
		_sb_.append(inststep);
		_sb_.append(",");
		_sb_.append(saveinfo);
		_sb_.append(",");
		_sb_.append(instreset);
		_sb_.append(",");
		_sb_.append(bossinfo);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currentzonekey"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("instkey"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("inststate"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("inststep"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("saveinfo"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("instreset"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("bossinfo"));
		return lb;
	}

	private class Const implements xbean.ClanInstances {
		ClanInstances nThis() {
			return ClanInstances.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanInstances copy() {
			return ClanInstances.this.copy();
		}

		@Override
		public xbean.ClanInstances toData() {
			return ClanInstances.this.toData();
		}

		public xbean.ClanInstances toBean() {
			return ClanInstances.this.toBean();
		}

		@Override
		public xbean.ClanInstances toDataIf() {
			return ClanInstances.this.toDataIf();
		}

		public xbean.ClanInstances toBeanIf() {
			return ClanInstances.this.toBeanIf();
		}

		@Override
		public long getClanid() { // 公会id
			_xdb_verify_unsafe_();
			return clanid;
		}

		@Override
		public long getCurrentzonekey() { // 当前激活的公会副本key，0为该公会没有激活公会副本
			_xdb_verify_unsafe_();
			return currentzonekey;
		}

		@Override
		public java.util.Map<Integer, Long> getInstkey() { // 公会副本,key副本id; value为对应的副本key.
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(instkey);
		}

		@Override
		public java.util.Map<Integer, Long> getInstkeyAsData() { // 公会副本,key副本id; value为对应的副本key.
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> instkey;
			ClanInstances _o_ = ClanInstances.this;
			instkey = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instkey.entrySet())
				instkey.put(_e_.getKey(), _e_.getValue());
			return instkey;
		}

		@Override
		public java.util.Map<Integer, Integer> getInststate() { // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(inststate);
		}

		@Override
		public java.util.Map<Integer, Integer> getInststateAsData() { // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> inststate;
			ClanInstances _o_ = ClanInstances.this;
			inststate = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststate.entrySet())
				inststate.put(_e_.getKey(), _e_.getValue());
			return inststate;
		}

		@Override
		public java.util.Map<Integer, Integer> getInststep() { // 公会副本进行的副本id对应的层数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(inststep);
		}

		@Override
		public java.util.Map<Integer, Integer> getInststepAsData() { // 公会副本进行的副本id对应的层数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> inststep;
			ClanInstances _o_ = ClanInstances.this;
			inststep = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststep.entrySet())
				inststep.put(_e_.getKey(), _e_.getValue());
			return inststep;
		}

		@Override
		public java.util.Map<Integer, xbean.ClanSaveInfo> getSaveinfo() { // key副本id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(saveinfo);
		}

		@Override
		public java.util.Map<Integer, xbean.ClanSaveInfo> getSaveinfoAsData() { // key副本id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.ClanSaveInfo> saveinfo;
			ClanInstances _o_ = ClanInstances.this;
			saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>();
			for (java.util.Map.Entry<Integer, xbean.ClanSaveInfo> _e_ : _o_.saveinfo.entrySet())
				saveinfo.put(_e_.getKey(), new ClanSaveInfo.Data(_e_.getValue()));
			return saveinfo;
		}

		@Override
		public java.util.Map<Integer, Long> getInstreset() { // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(instreset);
		}

		@Override
		public java.util.Map<Integer, Long> getInstresetAsData() { // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> instreset;
			ClanInstances _o_ = ClanInstances.this;
			instreset = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instreset.entrySet())
				instreset.put(_e_.getKey(), _e_.getValue());
			return instreset;
		}

		@Override
		public java.util.Map<Integer, xbean.ClanBossInfo> getBossinfo() { // key:battleconfigId value:bossinfo
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(bossinfo);
		}

		@Override
		public java.util.Map<Integer, xbean.ClanBossInfo> getBossinfoAsData() { // key:battleconfigId value:bossinfo
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.ClanBossInfo> bossinfo;
			ClanInstances _o_ = ClanInstances.this;
			bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>();
			for (java.util.Map.Entry<Integer, xbean.ClanBossInfo> _e_ : _o_.bossinfo.entrySet())
				bossinfo.put(_e_.getKey(), new ClanBossInfo.Data(_e_.getValue()));
			return bossinfo;
		}

		@Override
		public void setClanid(long _v_) { // 公会id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrentzonekey(long _v_) { // 当前激活的公会副本key，0为该公会没有激活公会副本
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
			return ClanInstances.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanInstances.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanInstances.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanInstances.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanInstances.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanInstances.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanInstances.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanInstances.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanInstances.this.toString();
		}

	}

	public static final class Data implements xbean.ClanInstances {
		private long clanid; // 公会id
		private long currentzonekey; // 当前激活的公会副本key，0为该公会没有激活公会副本
		private java.util.HashMap<Integer, Long> instkey; // 公会副本,key副本id; value为对应的副本key.
		private java.util.HashMap<Integer, Integer> inststate; // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
		private java.util.HashMap<Integer, Integer> inststep; // 公会副本进行的副本id对应的层数
		private java.util.HashMap<Integer, xbean.ClanSaveInfo> saveinfo; // key副本id
		private java.util.HashMap<Integer, Long> instreset; // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
		private java.util.HashMap<Integer, xbean.ClanBossInfo> bossinfo; // key:battleconfigId value:bossinfo

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			clanid = 0;
			currentzonekey = 0;
			instkey = new java.util.HashMap<Integer, Long>();
			inststate = new java.util.HashMap<Integer, Integer>();
			inststep = new java.util.HashMap<Integer, Integer>();
			saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>();
			instreset = new java.util.HashMap<Integer, Long>();
			bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>();
		}

		Data(xbean.ClanInstances _o1_) {
			if (_o1_ instanceof ClanInstances) assign((ClanInstances)_o1_);
			else if (_o1_ instanceof ClanInstances.Data) assign((ClanInstances.Data)_o1_);
			else if (_o1_ instanceof ClanInstances.Const) assign(((ClanInstances.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanInstances _o_) {
			clanid = _o_.clanid;
			currentzonekey = _o_.currentzonekey;
			instkey = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instkey.entrySet())
				instkey.put(_e_.getKey(), _e_.getValue());
			inststate = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststate.entrySet())
				inststate.put(_e_.getKey(), _e_.getValue());
			inststep = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststep.entrySet())
				inststep.put(_e_.getKey(), _e_.getValue());
			saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>();
			for (java.util.Map.Entry<Integer, xbean.ClanSaveInfo> _e_ : _o_.saveinfo.entrySet())
				saveinfo.put(_e_.getKey(), new ClanSaveInfo.Data(_e_.getValue()));
			instreset = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instreset.entrySet())
				instreset.put(_e_.getKey(), _e_.getValue());
			bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>();
			for (java.util.Map.Entry<Integer, xbean.ClanBossInfo> _e_ : _o_.bossinfo.entrySet())
				bossinfo.put(_e_.getKey(), new ClanBossInfo.Data(_e_.getValue()));
		}

		private void assign(ClanInstances.Data _o_) {
			clanid = _o_.clanid;
			currentzonekey = _o_.currentzonekey;
			instkey = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instkey.entrySet())
				instkey.put(_e_.getKey(), _e_.getValue());
			inststate = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststate.entrySet())
				inststate.put(_e_.getKey(), _e_.getValue());
			inststep = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inststep.entrySet())
				inststep.put(_e_.getKey(), _e_.getValue());
			saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>();
			for (java.util.Map.Entry<Integer, xbean.ClanSaveInfo> _e_ : _o_.saveinfo.entrySet())
				saveinfo.put(_e_.getKey(), new ClanSaveInfo.Data(_e_.getValue()));
			instreset = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instreset.entrySet())
				instreset.put(_e_.getKey(), _e_.getValue());
			bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>();
			for (java.util.Map.Entry<Integer, xbean.ClanBossInfo> _e_ : _o_.bossinfo.entrySet())
				bossinfo.put(_e_.getKey(), new ClanBossInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(clanid);
			_os_.marshal(currentzonekey);
			_os_.compact_uint32(instkey.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : instkey.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(inststate.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : inststate.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(inststep.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : inststep.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(saveinfo.size());
			for (java.util.Map.Entry<Integer, xbean.ClanSaveInfo> _e_ : saveinfo.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(instreset.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : instreset.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(bossinfo.size());
			for (java.util.Map.Entry<Integer, xbean.ClanBossInfo> _e_ : bossinfo.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			clanid = _os_.unmarshal_long();
			currentzonekey = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					instkey = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					instkey.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					inststate = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					inststate.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					inststep = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					inststep.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					saveinfo = new java.util.HashMap<Integer, xbean.ClanSaveInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.ClanSaveInfo _v_ = xbean.Pod.newClanSaveInfoData();
					_v_.unmarshal(_os_);
					saveinfo.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					instreset = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					instreset.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					bossinfo = new java.util.HashMap<Integer, xbean.ClanBossInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.ClanBossInfo _v_ = xbean.Pod.newClanBossInfoData();
					_v_.unmarshal(_os_);
					bossinfo.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ClanInstances copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanInstances toData() {
			return new Data(this);
		}

		public xbean.ClanInstances toBean() {
			return new ClanInstances(this, null, null);
		}

		@Override
		public xbean.ClanInstances toDataIf() {
			return this;
		}

		public xbean.ClanInstances toBeanIf() {
			return new ClanInstances(this, null, null);
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
		public long getClanid() { // 公会id
			return clanid;
		}

		@Override
		public long getCurrentzonekey() { // 当前激活的公会副本key，0为该公会没有激活公会副本
			return currentzonekey;
		}

		@Override
		public java.util.Map<Integer, Long> getInstkey() { // 公会副本,key副本id; value为对应的副本key.
			return instkey;
		}

		@Override
		public java.util.Map<Integer, Long> getInstkeyAsData() { // 公会副本,key副本id; value为对应的副本key.
			return instkey;
		}

		@Override
		public java.util.Map<Integer, Integer> getInststate() { // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
			return inststate;
		}

		@Override
		public java.util.Map<Integer, Integer> getInststateAsData() { // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
			return inststate;
		}

		@Override
		public java.util.Map<Integer, Integer> getInststep() { // 公会副本进行的副本id对应的层数
			return inststep;
		}

		@Override
		public java.util.Map<Integer, Integer> getInststepAsData() { // 公会副本进行的副本id对应的层数
			return inststep;
		}

		@Override
		public java.util.Map<Integer, xbean.ClanSaveInfo> getSaveinfo() { // key副本id
			return saveinfo;
		}

		@Override
		public java.util.Map<Integer, xbean.ClanSaveInfo> getSaveinfoAsData() { // key副本id
			return saveinfo;
		}

		@Override
		public java.util.Map<Integer, Long> getInstreset() { // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
			return instreset;
		}

		@Override
		public java.util.Map<Integer, Long> getInstresetAsData() { // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
			return instreset;
		}

		@Override
		public java.util.Map<Integer, xbean.ClanBossInfo> getBossinfo() { // key:battleconfigId value:bossinfo
			return bossinfo;
		}

		@Override
		public java.util.Map<Integer, xbean.ClanBossInfo> getBossinfoAsData() { // key:battleconfigId value:bossinfo
			return bossinfo;
		}

		@Override
		public void setClanid(long _v_) { // 公会id
			clanid = _v_;
		}

		@Override
		public void setCurrentzonekey(long _v_) { // 当前激活的公会副本key，0为该公会没有激活公会副本
			currentzonekey = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanInstances.Data)) return false;
			ClanInstances.Data _o_ = (ClanInstances.Data) _o1_;
			if (clanid != _o_.clanid) return false;
			if (currentzonekey != _o_.currentzonekey) return false;
			if (!instkey.equals(_o_.instkey)) return false;
			if (!inststate.equals(_o_.inststate)) return false;
			if (!inststep.equals(_o_.inststep)) return false;
			if (!saveinfo.equals(_o_.saveinfo)) return false;
			if (!instreset.equals(_o_.instreset)) return false;
			if (!bossinfo.equals(_o_.bossinfo)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += clanid;
			_h_ += currentzonekey;
			_h_ += instkey.hashCode();
			_h_ += inststate.hashCode();
			_h_ += inststep.hashCode();
			_h_ += saveinfo.hashCode();
			_h_ += instreset.hashCode();
			_h_ += bossinfo.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(clanid);
			_sb_.append(",");
			_sb_.append(currentzonekey);
			_sb_.append(",");
			_sb_.append(instkey);
			_sb_.append(",");
			_sb_.append(inststate);
			_sb_.append(",");
			_sb_.append(inststep);
			_sb_.append(",");
			_sb_.append(saveinfo);
			_sb_.append(",");
			_sb_.append(instreset);
			_sb_.append(",");
			_sb_.append(bossinfo);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
