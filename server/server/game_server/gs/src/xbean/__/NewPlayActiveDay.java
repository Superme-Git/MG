
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class NewPlayActiveDay extends mkdb.XBean implements xbean.NewPlayActiveDay {
	private long time; // 活动记录时间
	private float activeness; // 今天总的活跃度值
	private java.util.HashMap<Integer, Integer> chests; // key为宝箱id,value为是否打开0未开,1已打开
	private java.util.HashMap<Integer, xbean.PlayActiveData> actives; // key为活动id,一天的计次数据
	private java.util.HashMap<Integer, Integer> activescount; // key为活动id, 一天的次数
	private long yingfutime; // 盈福经验记录时间
	private long yingfuexp; // 累积的盈福经验

	@Override
	public void _reset_unsafe_() {
		time = 0L;
		activeness = 0.0f;
		chests.clear();
		actives.clear();
		activescount.clear();
		yingfutime = 0L;
		yingfuexp = 0L;
	}

	NewPlayActiveDay(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		chests = new java.util.HashMap<Integer, Integer>();
		actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
		activescount = new java.util.HashMap<Integer, Integer>();
	}

	public NewPlayActiveDay() {
		this(0, null, null);
	}

	public NewPlayActiveDay(NewPlayActiveDay _o_) {
		this(_o_, null, null);
	}

	NewPlayActiveDay(xbean.NewPlayActiveDay _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof NewPlayActiveDay) assign((NewPlayActiveDay)_o1_);
		else if (_o1_ instanceof NewPlayActiveDay.Data) assign((NewPlayActiveDay.Data)_o1_);
		else if (_o1_ instanceof NewPlayActiveDay.Const) assign(((NewPlayActiveDay.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(NewPlayActiveDay _o_) {
		_o_._xdb_verify_unsafe_();
		time = _o_.time;
		activeness = _o_.activeness;
		chests = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.chests.entrySet())
			chests.put(_e_.getKey(), _e_.getValue());
		actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
		for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
			actives.put(_e_.getKey(), new PlayActiveData(_e_.getValue(), this, "actives"));
		activescount = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.activescount.entrySet())
			activescount.put(_e_.getKey(), _e_.getValue());
		yingfutime = _o_.yingfutime;
		yingfuexp = _o_.yingfuexp;
	}

	private void assign(NewPlayActiveDay.Data _o_) {
		time = _o_.time;
		activeness = _o_.activeness;
		chests = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.chests.entrySet())
			chests.put(_e_.getKey(), _e_.getValue());
		actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
		for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
			actives.put(_e_.getKey(), new PlayActiveData(_e_.getValue(), this, "actives"));
		activescount = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.activescount.entrySet())
			activescount.put(_e_.getKey(), _e_.getValue());
		yingfutime = _o_.yingfutime;
		yingfuexp = _o_.yingfuexp;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(time);
		_os_.marshal(activeness);
		_os_.compact_uint32(chests.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : chests.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(actives.size());
		for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : actives.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(activescount.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : activescount.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(yingfutime);
		_os_.marshal(yingfuexp);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		time = _os_.unmarshal_long();
		activeness = _os_.unmarshal_float();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				chests = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				chests.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				actives = new java.util.HashMap<Integer, xbean.PlayActiveData>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.PlayActiveData _v_ = new PlayActiveData(0, this, "actives");
				_v_.unmarshal(_os_);
				actives.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				activescount = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				activescount.put(_k_, _v_);
			}
		}
		yingfutime = _os_.unmarshal_long();
		yingfuexp = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.NewPlayActiveDay copy() {
		_xdb_verify_unsafe_();
		return new NewPlayActiveDay(this);
	}

	@Override
	public xbean.NewPlayActiveDay toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NewPlayActiveDay toBean() {
		_xdb_verify_unsafe_();
		return new NewPlayActiveDay(this); // same as copy()
	}

	@Override
	public xbean.NewPlayActiveDay toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NewPlayActiveDay toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTime() { // 活动记录时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public float getActiveness() { // 今天总的活跃度值
		_xdb_verify_unsafe_();
		return activeness;
	}

	@Override
	public java.util.Map<Integer, Integer> getChests() { // key为宝箱id,value为是否打开0未开,1已打开
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "chests"), chests);
	}

	@Override
	public java.util.Map<Integer, Integer> getChestsAsData() { // key为宝箱id,value为是否打开0未开,1已打开
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> chests;
		NewPlayActiveDay _o_ = this;
		chests = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.chests.entrySet())
			chests.put(_e_.getKey(), _e_.getValue());
		return chests;
	}

	@Override
	public java.util.Map<Integer, xbean.PlayActiveData> getActives() { // key为活动id,一天的计次数据
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "actives"), actives);
	}

	@Override
	public java.util.Map<Integer, xbean.PlayActiveData> getActivesAsData() { // key为活动id,一天的计次数据
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.PlayActiveData> actives;
		NewPlayActiveDay _o_ = this;
		actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
		for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
			actives.put(_e_.getKey(), new PlayActiveData.Data(_e_.getValue()));
		return actives;
	}

	@Override
	public java.util.Map<Integer, Integer> getActivescount() { // key为活动id, 一天的次数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "activescount"), activescount);
	}

	@Override
	public java.util.Map<Integer, Integer> getActivescountAsData() { // key为活动id, 一天的次数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> activescount;
		NewPlayActiveDay _o_ = this;
		activescount = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.activescount.entrySet())
			activescount.put(_e_.getKey(), _e_.getValue());
		return activescount;
	}

	@Override
	public long getYingfutime() { // 盈福经验记录时间
		_xdb_verify_unsafe_();
		return yingfutime;
	}

	@Override
	public long getYingfuexp() { // 累积的盈福经验
		_xdb_verify_unsafe_();
		return yingfuexp;
	}

	@Override
	public void setTime(long _v_) { // 活动记录时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public void setActiveness(float _v_) { // 今天总的活跃度值
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activeness") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, activeness) {
					public void rollback() { activeness = _xdb_saved; }
				};}});
		activeness = _v_;
	}

	@Override
	public void setYingfutime(long _v_) { // 盈福经验记录时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "yingfutime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, yingfutime) {
					public void rollback() { yingfutime = _xdb_saved; }
				};}});
		yingfutime = _v_;
	}

	@Override
	public void setYingfuexp(long _v_) { // 累积的盈福经验
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "yingfuexp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, yingfuexp) {
					public void rollback() { yingfuexp = _xdb_saved; }
				};}});
		yingfuexp = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		NewPlayActiveDay _o_ = null;
		if ( _o1_ instanceof NewPlayActiveDay ) _o_ = (NewPlayActiveDay)_o1_;
		else if ( _o1_ instanceof NewPlayActiveDay.Const ) _o_ = ((NewPlayActiveDay.Const)_o1_).nThis();
		else return false;
		if (time != _o_.time) return false;
		if (activeness != _o_.activeness) return false;
		if (!chests.equals(_o_.chests)) return false;
		if (!actives.equals(_o_.actives)) return false;
		if (!activescount.equals(_o_.activescount)) return false;
		if (yingfutime != _o_.yingfutime) return false;
		if (yingfuexp != _o_.yingfuexp) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += time;
		_h_ += activeness;
		_h_ += chests.hashCode();
		_h_ += actives.hashCode();
		_h_ += activescount.hashCode();
		_h_ += yingfutime;
		_h_ += yingfuexp;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(activeness);
		_sb_.append(",");
		_sb_.append(chests);
		_sb_.append(",");
		_sb_.append(actives);
		_sb_.append(",");
		_sb_.append(activescount);
		_sb_.append(",");
		_sb_.append(yingfutime);
		_sb_.append(",");
		_sb_.append(yingfuexp);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activeness"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("chests"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("actives"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("activescount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("yingfutime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("yingfuexp"));
		return lb;
	}

	private class Const implements xbean.NewPlayActiveDay {
		NewPlayActiveDay nThis() {
			return NewPlayActiveDay.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.NewPlayActiveDay copy() {
			return NewPlayActiveDay.this.copy();
		}

		@Override
		public xbean.NewPlayActiveDay toData() {
			return NewPlayActiveDay.this.toData();
		}

		public xbean.NewPlayActiveDay toBean() {
			return NewPlayActiveDay.this.toBean();
		}

		@Override
		public xbean.NewPlayActiveDay toDataIf() {
			return NewPlayActiveDay.this.toDataIf();
		}

		public xbean.NewPlayActiveDay toBeanIf() {
			return NewPlayActiveDay.this.toBeanIf();
		}

		@Override
		public long getTime() { // 活动记录时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public float getActiveness() { // 今天总的活跃度值
			_xdb_verify_unsafe_();
			return activeness;
		}

		@Override
		public java.util.Map<Integer, Integer> getChests() { // key为宝箱id,value为是否打开0未开,1已打开
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(chests);
		}

		@Override
		public java.util.Map<Integer, Integer> getChestsAsData() { // key为宝箱id,value为是否打开0未开,1已打开
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> chests;
			NewPlayActiveDay _o_ = NewPlayActiveDay.this;
			chests = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.chests.entrySet())
				chests.put(_e_.getKey(), _e_.getValue());
			return chests;
		}

		@Override
		public java.util.Map<Integer, xbean.PlayActiveData> getActives() { // key为活动id,一天的计次数据
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(actives);
		}

		@Override
		public java.util.Map<Integer, xbean.PlayActiveData> getActivesAsData() { // key为活动id,一天的计次数据
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.PlayActiveData> actives;
			NewPlayActiveDay _o_ = NewPlayActiveDay.this;
			actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
			for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
				actives.put(_e_.getKey(), new PlayActiveData.Data(_e_.getValue()));
			return actives;
		}

		@Override
		public java.util.Map<Integer, Integer> getActivescount() { // key为活动id, 一天的次数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(activescount);
		}

		@Override
		public java.util.Map<Integer, Integer> getActivescountAsData() { // key为活动id, 一天的次数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> activescount;
			NewPlayActiveDay _o_ = NewPlayActiveDay.this;
			activescount = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.activescount.entrySet())
				activescount.put(_e_.getKey(), _e_.getValue());
			return activescount;
		}

		@Override
		public long getYingfutime() { // 盈福经验记录时间
			_xdb_verify_unsafe_();
			return yingfutime;
		}

		@Override
		public long getYingfuexp() { // 累积的盈福经验
			_xdb_verify_unsafe_();
			return yingfuexp;
		}

		@Override
		public void setTime(long _v_) { // 活动记录时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setActiveness(float _v_) { // 今天总的活跃度值
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setYingfutime(long _v_) { // 盈福经验记录时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setYingfuexp(long _v_) { // 累积的盈福经验
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
			return NewPlayActiveDay.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return NewPlayActiveDay.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return NewPlayActiveDay.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return NewPlayActiveDay.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return NewPlayActiveDay.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return NewPlayActiveDay.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return NewPlayActiveDay.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return NewPlayActiveDay.this.hashCode();
		}

		@Override
		public String toString() {
			return NewPlayActiveDay.this.toString();
		}

	}

	public static final class Data implements xbean.NewPlayActiveDay {
		private long time; // 活动记录时间
		private float activeness; // 今天总的活跃度值
		private java.util.HashMap<Integer, Integer> chests; // key为宝箱id,value为是否打开0未开,1已打开
		private java.util.HashMap<Integer, xbean.PlayActiveData> actives; // key为活动id,一天的计次数据
		private java.util.HashMap<Integer, Integer> activescount; // key为活动id, 一天的次数
		private long yingfutime; // 盈福经验记录时间
		private long yingfuexp; // 累积的盈福经验

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			chests = new java.util.HashMap<Integer, Integer>();
			actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
			activescount = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.NewPlayActiveDay _o1_) {
			if (_o1_ instanceof NewPlayActiveDay) assign((NewPlayActiveDay)_o1_);
			else if (_o1_ instanceof NewPlayActiveDay.Data) assign((NewPlayActiveDay.Data)_o1_);
			else if (_o1_ instanceof NewPlayActiveDay.Const) assign(((NewPlayActiveDay.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(NewPlayActiveDay _o_) {
			time = _o_.time;
			activeness = _o_.activeness;
			chests = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.chests.entrySet())
				chests.put(_e_.getKey(), _e_.getValue());
			actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
			for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
				actives.put(_e_.getKey(), new PlayActiveData.Data(_e_.getValue()));
			activescount = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.activescount.entrySet())
				activescount.put(_e_.getKey(), _e_.getValue());
			yingfutime = _o_.yingfutime;
			yingfuexp = _o_.yingfuexp;
		}

		private void assign(NewPlayActiveDay.Data _o_) {
			time = _o_.time;
			activeness = _o_.activeness;
			chests = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.chests.entrySet())
				chests.put(_e_.getKey(), _e_.getValue());
			actives = new java.util.HashMap<Integer, xbean.PlayActiveData>();
			for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : _o_.actives.entrySet())
				actives.put(_e_.getKey(), new PlayActiveData.Data(_e_.getValue()));
			activescount = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.activescount.entrySet())
				activescount.put(_e_.getKey(), _e_.getValue());
			yingfutime = _o_.yingfutime;
			yingfuexp = _o_.yingfuexp;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(time);
			_os_.marshal(activeness);
			_os_.compact_uint32(chests.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : chests.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(actives.size());
			for (java.util.Map.Entry<Integer, xbean.PlayActiveData> _e_ : actives.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(activescount.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : activescount.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(yingfutime);
			_os_.marshal(yingfuexp);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			time = _os_.unmarshal_long();
			activeness = _os_.unmarshal_float();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					chests = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					chests.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					actives = new java.util.HashMap<Integer, xbean.PlayActiveData>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.PlayActiveData _v_ = xbean.Pod.newPlayActiveDataData();
					_v_.unmarshal(_os_);
					actives.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					activescount = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					activescount.put(_k_, _v_);
				}
			}
			yingfutime = _os_.unmarshal_long();
			yingfuexp = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.NewPlayActiveDay copy() {
			return new Data(this);
		}

		@Override
		public xbean.NewPlayActiveDay toData() {
			return new Data(this);
		}

		public xbean.NewPlayActiveDay toBean() {
			return new NewPlayActiveDay(this, null, null);
		}

		@Override
		public xbean.NewPlayActiveDay toDataIf() {
			return this;
		}

		public xbean.NewPlayActiveDay toBeanIf() {
			return new NewPlayActiveDay(this, null, null);
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
		public long getTime() { // 活动记录时间
			return time;
		}

		@Override
		public float getActiveness() { // 今天总的活跃度值
			return activeness;
		}

		@Override
		public java.util.Map<Integer, Integer> getChests() { // key为宝箱id,value为是否打开0未开,1已打开
			return chests;
		}

		@Override
		public java.util.Map<Integer, Integer> getChestsAsData() { // key为宝箱id,value为是否打开0未开,1已打开
			return chests;
		}

		@Override
		public java.util.Map<Integer, xbean.PlayActiveData> getActives() { // key为活动id,一天的计次数据
			return actives;
		}

		@Override
		public java.util.Map<Integer, xbean.PlayActiveData> getActivesAsData() { // key为活动id,一天的计次数据
			return actives;
		}

		@Override
		public java.util.Map<Integer, Integer> getActivescount() { // key为活动id, 一天的次数
			return activescount;
		}

		@Override
		public java.util.Map<Integer, Integer> getActivescountAsData() { // key为活动id, 一天的次数
			return activescount;
		}

		@Override
		public long getYingfutime() { // 盈福经验记录时间
			return yingfutime;
		}

		@Override
		public long getYingfuexp() { // 累积的盈福经验
			return yingfuexp;
		}

		@Override
		public void setTime(long _v_) { // 活动记录时间
			time = _v_;
		}

		@Override
		public void setActiveness(float _v_) { // 今天总的活跃度值
			activeness = _v_;
		}

		@Override
		public void setYingfutime(long _v_) { // 盈福经验记录时间
			yingfutime = _v_;
		}

		@Override
		public void setYingfuexp(long _v_) { // 累积的盈福经验
			yingfuexp = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof NewPlayActiveDay.Data)) return false;
			NewPlayActiveDay.Data _o_ = (NewPlayActiveDay.Data) _o1_;
			if (time != _o_.time) return false;
			if (activeness != _o_.activeness) return false;
			if (!chests.equals(_o_.chests)) return false;
			if (!actives.equals(_o_.actives)) return false;
			if (!activescount.equals(_o_.activescount)) return false;
			if (yingfutime != _o_.yingfutime) return false;
			if (yingfuexp != _o_.yingfuexp) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += time;
			_h_ += activeness;
			_h_ += chests.hashCode();
			_h_ += actives.hashCode();
			_h_ += activescount.hashCode();
			_h_ += yingfutime;
			_h_ += yingfuexp;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(activeness);
			_sb_.append(",");
			_sb_.append(chests);
			_sb_.append(",");
			_sb_.append(actives);
			_sb_.append(",");
			_sb_.append(activescount);
			_sb_.append(",");
			_sb_.append(yingfutime);
			_sb_.append(",");
			_sb_.append(yingfuexp);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
