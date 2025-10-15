
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class HuoBanInfo extends mkdb.XBean implements xbean.HuoBanInfo {
	private int id; // 编号
	private int level; // 等级
	private int color; // 颜色
	private int infight; // 是否参战,1为参战
	private int state; // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
	private int type; // 伙伴类型 法攻,物攻,辅助,治疗,封印
	private long times; // 到期时间;当state为1时,此值无效
	private long settimes; // 最近设置时间
	private int weekindex; // 对应周免费的id

	@Override
	public void _reset_unsafe_() {
		id = 0;
		level = 0;
		color = 0;
		infight = 0;
		state = 0;
		type = 0;
		times = 0L;
		settimes = 0L;
		weekindex = 0;
	}

	HuoBanInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public HuoBanInfo() {
		this(0, null, null);
	}

	public HuoBanInfo(HuoBanInfo _o_) {
		this(_o_, null, null);
	}

	HuoBanInfo(xbean.HuoBanInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof HuoBanInfo) assign((HuoBanInfo)_o1_);
		else if (_o1_ instanceof HuoBanInfo.Data) assign((HuoBanInfo.Data)_o1_);
		else if (_o1_ instanceof HuoBanInfo.Const) assign(((HuoBanInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(HuoBanInfo _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		level = _o_.level;
		color = _o_.color;
		infight = _o_.infight;
		state = _o_.state;
		type = _o_.type;
		times = _o_.times;
		settimes = _o_.settimes;
		weekindex = _o_.weekindex;
	}

	private void assign(HuoBanInfo.Data _o_) {
		id = _o_.id;
		level = _o_.level;
		color = _o_.color;
		infight = _o_.infight;
		state = _o_.state;
		type = _o_.type;
		times = _o_.times;
		settimes = _o_.settimes;
		weekindex = _o_.weekindex;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(level);
		_os_.marshal(color);
		_os_.marshal(infight);
		_os_.marshal(state);
		_os_.marshal(type);
		_os_.marshal(times);
		_os_.marshal(settimes);
		_os_.marshal(weekindex);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		color = _os_.unmarshal_int();
		infight = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		type = _os_.unmarshal_int();
		times = _os_.unmarshal_long();
		settimes = _os_.unmarshal_long();
		weekindex = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.HuoBanInfo copy() {
		_xdb_verify_unsafe_();
		return new HuoBanInfo(this);
	}

	@Override
	public xbean.HuoBanInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanInfo toBean() {
		_xdb_verify_unsafe_();
		return new HuoBanInfo(this); // same as copy()
	}

	@Override
	public xbean.HuoBanInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 编号
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getLevel() { // 等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getColor() { // 颜色
		_xdb_verify_unsafe_();
		return color;
	}

	@Override
	public int getInfight() { // 是否参战,1为参战
		_xdb_verify_unsafe_();
		return infight;
	}

	@Override
	public int getState() { // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public int getType() { // 伙伴类型 法攻,物攻,辅助,治疗,封印
		_xdb_verify_unsafe_();
		return type;
	}

	@Override
	public long getTimes() { // 到期时间;当state为1时,此值无效
		_xdb_verify_unsafe_();
		return times;
	}

	@Override
	public long getSettimes() { // 最近设置时间
		_xdb_verify_unsafe_();
		return settimes;
	}

	@Override
	public int getWeekindex() { // 对应周免费的id
		_xdb_verify_unsafe_();
		return weekindex;
	}

	@Override
	public void setId(int _v_) { // 编号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setColor(int _v_) { // 颜色
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "color") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, color) {
					public void rollback() { color = _xdb_saved; }
				};}});
		color = _v_;
	}

	@Override
	public void setInfight(int _v_) { // 是否参战,1为参战
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "infight") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, infight) {
					public void rollback() { infight = _xdb_saved; }
				};}});
		infight = _v_;
	}

	@Override
	public void setState(int _v_) { // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public void setType(int _v_) { // 伙伴类型 法攻,物攻,辅助,治疗,封印
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "type") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, type) {
					public void rollback() { type = _xdb_saved; }
				};}});
		type = _v_;
	}

	@Override
	public void setTimes(long _v_) { // 到期时间;当state为1时,此值无效
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "times") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, times) {
					public void rollback() { times = _xdb_saved; }
				};}});
		times = _v_;
	}

	@Override
	public void setSettimes(long _v_) { // 最近设置时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "settimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, settimes) {
					public void rollback() { settimes = _xdb_saved; }
				};}});
		settimes = _v_;
	}

	@Override
	public void setWeekindex(int _v_) { // 对应周免费的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "weekindex") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, weekindex) {
					public void rollback() { weekindex = _xdb_saved; }
				};}});
		weekindex = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		HuoBanInfo _o_ = null;
		if ( _o1_ instanceof HuoBanInfo ) _o_ = (HuoBanInfo)_o1_;
		else if ( _o1_ instanceof HuoBanInfo.Const ) _o_ = ((HuoBanInfo.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (level != _o_.level) return false;
		if (color != _o_.color) return false;
		if (infight != _o_.infight) return false;
		if (state != _o_.state) return false;
		if (type != _o_.type) return false;
		if (times != _o_.times) return false;
		if (settimes != _o_.settimes) return false;
		if (weekindex != _o_.weekindex) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += level;
		_h_ += color;
		_h_ += infight;
		_h_ += state;
		_h_ += type;
		_h_ += times;
		_h_ += settimes;
		_h_ += weekindex;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(color);
		_sb_.append(",");
		_sb_.append(infight);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(type);
		_sb_.append(",");
		_sb_.append(times);
		_sb_.append(",");
		_sb_.append(settimes);
		_sb_.append(",");
		_sb_.append(weekindex);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("color"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("infight"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("type"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("times"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("settimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("weekindex"));
		return lb;
	}

	private class Const implements xbean.HuoBanInfo {
		HuoBanInfo nThis() {
			return HuoBanInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.HuoBanInfo copy() {
			return HuoBanInfo.this.copy();
		}

		@Override
		public xbean.HuoBanInfo toData() {
			return HuoBanInfo.this.toData();
		}

		public xbean.HuoBanInfo toBean() {
			return HuoBanInfo.this.toBean();
		}

		@Override
		public xbean.HuoBanInfo toDataIf() {
			return HuoBanInfo.this.toDataIf();
		}

		public xbean.HuoBanInfo toBeanIf() {
			return HuoBanInfo.this.toBeanIf();
		}

		@Override
		public int getId() { // 编号
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getLevel() { // 等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getColor() { // 颜色
			_xdb_verify_unsafe_();
			return color;
		}

		@Override
		public int getInfight() { // 是否参战,1为参战
			_xdb_verify_unsafe_();
			return infight;
		}

		@Override
		public int getState() { // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public int getType() { // 伙伴类型 法攻,物攻,辅助,治疗,封印
			_xdb_verify_unsafe_();
			return type;
		}

		@Override
		public long getTimes() { // 到期时间;当state为1时,此值无效
			_xdb_verify_unsafe_();
			return times;
		}

		@Override
		public long getSettimes() { // 最近设置时间
			_xdb_verify_unsafe_();
			return settimes;
		}

		@Override
		public int getWeekindex() { // 对应周免费的id
			_xdb_verify_unsafe_();
			return weekindex;
		}

		@Override
		public void setId(int _v_) { // 编号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setColor(int _v_) { // 颜色
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInfight(int _v_) { // 是否参战,1为参战
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setType(int _v_) { // 伙伴类型 法攻,物攻,辅助,治疗,封印
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimes(long _v_) { // 到期时间;当state为1时,此值无效
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSettimes(long _v_) { // 最近设置时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWeekindex(int _v_) { // 对应周免费的id
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
			return HuoBanInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return HuoBanInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return HuoBanInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return HuoBanInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return HuoBanInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return HuoBanInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return HuoBanInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return HuoBanInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return HuoBanInfo.this.toString();
		}

	}

	public static final class Data implements xbean.HuoBanInfo {
		private int id; // 编号
		private int level; // 等级
		private int color; // 颜色
		private int infight; // 是否参战,1为参战
		private int state; // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
		private int type; // 伙伴类型 法攻,物攻,辅助,治疗,封印
		private long times; // 到期时间;当state为1时,此值无效
		private long settimes; // 最近设置时间
		private int weekindex; // 对应周免费的id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.HuoBanInfo _o1_) {
			if (_o1_ instanceof HuoBanInfo) assign((HuoBanInfo)_o1_);
			else if (_o1_ instanceof HuoBanInfo.Data) assign((HuoBanInfo.Data)_o1_);
			else if (_o1_ instanceof HuoBanInfo.Const) assign(((HuoBanInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(HuoBanInfo _o_) {
			id = _o_.id;
			level = _o_.level;
			color = _o_.color;
			infight = _o_.infight;
			state = _o_.state;
			type = _o_.type;
			times = _o_.times;
			settimes = _o_.settimes;
			weekindex = _o_.weekindex;
		}

		private void assign(HuoBanInfo.Data _o_) {
			id = _o_.id;
			level = _o_.level;
			color = _o_.color;
			infight = _o_.infight;
			state = _o_.state;
			type = _o_.type;
			times = _o_.times;
			settimes = _o_.settimes;
			weekindex = _o_.weekindex;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(level);
			_os_.marshal(color);
			_os_.marshal(infight);
			_os_.marshal(state);
			_os_.marshal(type);
			_os_.marshal(times);
			_os_.marshal(settimes);
			_os_.marshal(weekindex);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			level = _os_.unmarshal_int();
			color = _os_.unmarshal_int();
			infight = _os_.unmarshal_int();
			state = _os_.unmarshal_int();
			type = _os_.unmarshal_int();
			times = _os_.unmarshal_long();
			settimes = _os_.unmarshal_long();
			weekindex = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.HuoBanInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.HuoBanInfo toData() {
			return new Data(this);
		}

		public xbean.HuoBanInfo toBean() {
			return new HuoBanInfo(this, null, null);
		}

		@Override
		public xbean.HuoBanInfo toDataIf() {
			return this;
		}

		public xbean.HuoBanInfo toBeanIf() {
			return new HuoBanInfo(this, null, null);
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
		public int getId() { // 编号
			return id;
		}

		@Override
		public int getLevel() { // 等级
			return level;
		}

		@Override
		public int getColor() { // 颜色
			return color;
		}

		@Override
		public int getInfight() { // 是否参战,1为参战
			return infight;
		}

		@Override
		public int getState() { // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
			return state;
		}

		@Override
		public int getType() { // 伙伴类型 法攻,物攻,辅助,治疗,封印
			return type;
		}

		@Override
		public long getTimes() { // 到期时间;当state为1时,此值无效
			return times;
		}

		@Override
		public long getSettimes() { // 最近设置时间
			return settimes;
		}

		@Override
		public int getWeekindex() { // 对应周免费的id
			return weekindex;
		}

		@Override
		public void setId(int _v_) { // 编号
			id = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 等级
			level = _v_;
		}

		@Override
		public void setColor(int _v_) { // 颜色
			color = _v_;
		}

		@Override
		public void setInfight(int _v_) { // 是否参战,1为参战
			infight = _v_;
		}

		@Override
		public void setState(int _v_) { // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
			state = _v_;
		}

		@Override
		public void setType(int _v_) { // 伙伴类型 法攻,物攻,辅助,治疗,封印
			type = _v_;
		}

		@Override
		public void setTimes(long _v_) { // 到期时间;当state为1时,此值无效
			times = _v_;
		}

		@Override
		public void setSettimes(long _v_) { // 最近设置时间
			settimes = _v_;
		}

		@Override
		public void setWeekindex(int _v_) { // 对应周免费的id
			weekindex = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof HuoBanInfo.Data)) return false;
			HuoBanInfo.Data _o_ = (HuoBanInfo.Data) _o1_;
			if (id != _o_.id) return false;
			if (level != _o_.level) return false;
			if (color != _o_.color) return false;
			if (infight != _o_.infight) return false;
			if (state != _o_.state) return false;
			if (type != _o_.type) return false;
			if (times != _o_.times) return false;
			if (settimes != _o_.settimes) return false;
			if (weekindex != _o_.weekindex) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += level;
			_h_ += color;
			_h_ += infight;
			_h_ += state;
			_h_ += type;
			_h_ += times;
			_h_ += settimes;
			_h_ += weekindex;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(color);
			_sb_.append(",");
			_sb_.append(infight);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(type);
			_sb_.append(",");
			_sb_.append(times);
			_sb_.append(",");
			_sb_.append(settimes);
			_sb_.append(",");
			_sb_.append(weekindex);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
