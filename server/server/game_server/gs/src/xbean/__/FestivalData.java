
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FestivalData extends mkdb.XBean implements xbean.FestivalData {
	private java.util.HashMap<Integer, Long> rewardmap; // 已经领取的节日奖励(key-奖励ID, value-领取时间)

	@Override
	public void _reset_unsafe_() {
		rewardmap.clear();
	}

	FestivalData(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rewardmap = new java.util.HashMap<Integer, Long>();
	}

	public FestivalData() {
		this(0, null, null);
	}

	public FestivalData(FestivalData _o_) {
		this(_o_, null, null);
	}

	FestivalData(xbean.FestivalData _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FestivalData) assign((FestivalData)_o1_);
		else if (_o1_ instanceof FestivalData.Data) assign((FestivalData.Data)_o1_);
		else if (_o1_ instanceof FestivalData.Const) assign(((FestivalData.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FestivalData _o_) {
		_o_._xdb_verify_unsafe_();
		rewardmap = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
			rewardmap.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(FestivalData.Data _o_) {
		rewardmap = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
			rewardmap.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(rewardmap.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : rewardmap.entrySet())
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
				rewardmap = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				rewardmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.FestivalData copy() {
		_xdb_verify_unsafe_();
		return new FestivalData(this);
	}

	@Override
	public xbean.FestivalData toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FestivalData toBean() {
		_xdb_verify_unsafe_();
		return new FestivalData(this); // same as copy()
	}

	@Override
	public xbean.FestivalData toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FestivalData toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Long> getRewardmap() { // 已经领取的节日奖励(key-奖励ID, value-领取时间)
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "rewardmap"), rewardmap);
	}

	@Override
	public java.util.Map<Integer, Long> getRewardmapAsData() { // 已经领取的节日奖励(key-奖励ID, value-领取时间)
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> rewardmap;
		FestivalData _o_ = this;
		rewardmap = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
			rewardmap.put(_e_.getKey(), _e_.getValue());
		return rewardmap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FestivalData _o_ = null;
		if ( _o1_ instanceof FestivalData ) _o_ = (FestivalData)_o1_;
		else if ( _o1_ instanceof FestivalData.Const ) _o_ = ((FestivalData.Const)_o1_).nThis();
		else return false;
		if (!rewardmap.equals(_o_.rewardmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rewardmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rewardmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("rewardmap"));
		return lb;
	}

	private class Const implements xbean.FestivalData {
		FestivalData nThis() {
			return FestivalData.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FestivalData copy() {
			return FestivalData.this.copy();
		}

		@Override
		public xbean.FestivalData toData() {
			return FestivalData.this.toData();
		}

		public xbean.FestivalData toBean() {
			return FestivalData.this.toBean();
		}

		@Override
		public xbean.FestivalData toDataIf() {
			return FestivalData.this.toDataIf();
		}

		public xbean.FestivalData toBeanIf() {
			return FestivalData.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Long> getRewardmap() { // 已经领取的节日奖励(key-奖励ID, value-领取时间)
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(rewardmap);
		}

		@Override
		public java.util.Map<Integer, Long> getRewardmapAsData() { // 已经领取的节日奖励(key-奖励ID, value-领取时间)
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> rewardmap;
			FestivalData _o_ = FestivalData.this;
			rewardmap = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
				rewardmap.put(_e_.getKey(), _e_.getValue());
			return rewardmap;
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
			return FestivalData.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FestivalData.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FestivalData.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FestivalData.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FestivalData.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FestivalData.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FestivalData.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FestivalData.this.hashCode();
		}

		@Override
		public String toString() {
			return FestivalData.this.toString();
		}

	}

	public static final class Data implements xbean.FestivalData {
		private java.util.HashMap<Integer, Long> rewardmap; // 已经领取的节日奖励(key-奖励ID, value-领取时间)

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rewardmap = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.FestivalData _o1_) {
			if (_o1_ instanceof FestivalData) assign((FestivalData)_o1_);
			else if (_o1_ instanceof FestivalData.Data) assign((FestivalData.Data)_o1_);
			else if (_o1_ instanceof FestivalData.Const) assign(((FestivalData.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FestivalData _o_) {
			rewardmap = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
				rewardmap.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(FestivalData.Data _o_) {
			rewardmap = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
				rewardmap.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(rewardmap.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : rewardmap.entrySet())
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
					rewardmap = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					rewardmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.FestivalData copy() {
			return new Data(this);
		}

		@Override
		public xbean.FestivalData toData() {
			return new Data(this);
		}

		public xbean.FestivalData toBean() {
			return new FestivalData(this, null, null);
		}

		@Override
		public xbean.FestivalData toDataIf() {
			return this;
		}

		public xbean.FestivalData toBeanIf() {
			return new FestivalData(this, null, null);
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
		public java.util.Map<Integer, Long> getRewardmap() { // 已经领取的节日奖励(key-奖励ID, value-领取时间)
			return rewardmap;
		}

		@Override
		public java.util.Map<Integer, Long> getRewardmapAsData() { // 已经领取的节日奖励(key-奖励ID, value-领取时间)
			return rewardmap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FestivalData.Data)) return false;
			FestivalData.Data _o_ = (FestivalData.Data) _o1_;
			if (!rewardmap.equals(_o_.rewardmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rewardmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rewardmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
